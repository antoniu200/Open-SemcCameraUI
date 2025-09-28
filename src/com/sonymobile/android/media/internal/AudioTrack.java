package com.sonymobile.android.media.internal;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonymobile.android.media.internal.Track;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class AudioTrack extends Track implements ClockInterface {
    private static final int ENCODING_PCM_SETTING = 2;
    private static final String HW_AAC_ENCODER = "OMX.qcom.audio.encoder.aac";
    private static final boolean HW_AAC_ENCODER_AVAILABLE = true;
    private static final int HW_ENCODER_BITRATE = 156000;
    private static final int INIT_PAUSE_ARRAY_SIZE = 100;
    private static final int MAX_AUDIO_BITRATE = 156000;
    private static final int MIN_STOP_AUDIO_DURATION_US = 2000000;
    protected static final String TAG = "AudioTrack";
    private String mAudioMime;
    private long mAudioOffset;
    private boolean mAudioRecord;
    private final AudioRecordHandler mAudioRecordHandler;
    private final HandlerThread mAudioRecordThread;
    private int mAudioSource;
    private int mBytesPerSample;
    private final Handler mCallback;
    private long mClockTotalPauseDurationUs;
    private final CodecHandler mCodecHandler;
    private int mInputBufferSize;
    private final Track.MuxerHandler mMuxerHandler;
    private int mPauseArrayIndex;
    private long[] mPauseResumeIndices;
    private AudioRecord mAudioRecorder = null;
    private int mAudioSamplingRate = 44100;
    private int mNumAudioChannels = 2;
    private int mAudioBitRate = RecordingProfile.VIDEO_AUDIO_BIT_RATE_AAC;
    private boolean mEosFlagged = false;
    private int mStartVolumeDelayUs = 0;
    private final String[] audioMimeTypes = {"", "audio/3gpp", "audio/amr-wb", "audio/mp4a-latm", "audio/mp4a-latm", "audio/mp4a-latm", "audio/vorbis"};
    private long mAudioDurationUs = 0;
    private long mClockStartTimeUs = -1;
    private long mClockPauseTimeUs = -1;
    private long mClockResumeTimeUs = -1;
    private long mClockStopTimeUs = -1;
    private boolean mFirstCodecFrame = true;
    private boolean mStopAudioRecording = false;

    AudioTrack(int i, Handler handler, HandlerThread handlerThread, HandlerThread handlerThread2, HandlerThread handlerThread3, boolean z) {
        this.mAudioSource = 0;
        this.mBytesPerSample = 2;
        this.mAudioRecord = true;
        this.mAudioSource = i;
        this.mCodecHandler = new CodecHandler(handlerThread.getLooper());
        this.mEventHandler = new Track.EventHandler(handlerThread2.getLooper());
        this.mMuxerHandler = new Track.MuxerHandler(handlerThread3.getLooper());
        this.mAudioRecordThread = new HandlerThread("AudioRecord", -1);
        this.mAudioRecordThread.start();
        this.mAudioRecordHandler = new AudioRecordHandler(this.mAudioRecordThread.getLooper());
        this.mHandlerHelper = new HandlerHelper();
        this.mCallback = handler;
        this.mAudioMime = this.audioMimeTypes[3];
        this.mState = Track.States.STOPPED;
        this.mBytesPerSample = 2;
        this.mAudioRecord = z;
    }

    protected void stopAudioRecording() {
        this.mStopAudioRecording = true;
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doPause() {
        this.mState = Track.States.PAUSED;
        if (this.mAudioRecord || this.mIsPauseLatchDown) {
            return;
        }
        this.mPauseLatch.countDown();
        this.mIsPauseLatchDown = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0061 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.sonymobile.android.media.internal.Track
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void doPrepare() {
        /*
            Method dump skipped, instructions count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonymobile.android.media.internal.AudioTrack.doPrepare():void");
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doStart() {
        this.mStopAudioRecording = false;
        if (this.mAudioRecord) {
            this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(109));
        } else {
            this.mCallback.obtainMessage(1, 11, 0).sendToTarget();
        }
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doStop() {
        if (this.mAudioRecord) {
            this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(107));
        } else {
            this.mCallback.sendMessage(this.mCallback.obtainMessage(103));
        }
    }

    void setAudioBitRate(int i) {
        if (i < 156000) {
            this.mAudioBitRate = i;
        }
    }

    void setAudioChannels(int i) {
        this.mNumAudioChannels = i;
    }

    void setAudioSamplingRate(int i) {
        this.mAudioSamplingRate = i;
    }

    void setAudioEncoder(int i) {
        if (i < 0 || i > this.audioMimeTypes.length - 1) {
            this.mAudioMime = null;
        } else {
            this.mAudioMime = this.audioMimeTypes[i];
        }
    }

    @Override // com.sonymobile.android.media.internal.Track
    public void doRelease() {
        if (this.mAudioRecord) {
            this.mMuxerHandler.removeMessages(104);
            this.mAudioRecordHandler.removeMessages(102);
            this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(107));
            this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(108));
        }
        this.mAudioRecordThread.quitSafely();
        if (this.mEncoder != null) {
            this.mEncoder.release();
            this.mEncoder = null;
        }
        this.mState = Track.States.STOPPED;
    }

    void adjustAudioTimeStamp(long j) {
        this.mAudioOffset = j * 1000;
    }

    void adjustStartVolume(int i) {
        this.mStartVolumeDelayUs = i * 1000;
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doResume(CountDownLatch countDownLatch) {
        this.mState = Track.States.STARTED;
        countDownLatch.countDown();
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doReset() {
        if (this.mState == Track.States.STOPPED || !this.mAudioRecord) {
            return;
        }
        this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(107));
    }

    @Override // com.sonymobile.android.media.internal.Track
    protected void doWriteOutputBuffer() throws IllegalStateException {
        ByteBuffer outputBuffer;
        if (this.mClock.isStarted() && !this.mBufferList.isEmpty() && isMuxerStarted()) {
            Track.EncodedBuffer encodedBufferRemove = this.mBufferList.remove();
            if (encodedBufferRemove.containsCopiedBuffer) {
                outputBuffer = encodedBufferRemove.byteBuffer;
            } else {
                outputBuffer = this.mEncoder.getOutputBuffer(encodedBufferRemove.bufferIndex);
            }
            if (outputBuffer != null && (!this.mStopAudioRecording || encodedBufferRemove.bufferInfo.presentationTimeUs < 2000000)) {
                this.mMuxerWrapper.writeSampleData(this.mMuxerTrackIndex, outputBuffer, encodedBufferRemove.bufferInfo);
            }
            if (!encodedBufferRemove.containsCopiedBuffer) {
                this.mEncoder.releaseOutputBuffer(encodedBufferRemove.bufferIndex, false);
            }
            if ((encodedBufferRemove.bufferInfo.flags & 4) != 0) {
                this.mEncoder.stop();
                this.mCallback.sendMessage(this.mCallback.obtainMessage(103));
                this.mState = Track.States.STOPPED;
                this.mMuxerWrapper.endTrack(this.mMuxerTrackIndex);
            }
        }
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getCurrentTimeUs() {
        if (this.mClockStartTimeUs < 0) {
            return this.mClockStartTimeUs;
        }
        if (this.mClockPauseTimeUs > this.mClockResumeTimeUs) {
            return getDurationAtPauseUs();
        }
        return (getSystemTimeUs() - this.mClockStartTimeUs) - this.mClockTotalPauseDurationUs;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public void resetClock() {
        this.mClockStartTimeUs = -1L;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public void setStartTime() {
        this.mClockStartTimeUs = getSystemTimeUs();
        this.mPauseArrayIndex = 0;
        this.mPauseResumeIndices = new long[100];
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getStartTimeUs() {
        return this.mClockStartTimeUs;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getSystemTimeUs() {
        return System.nanoTime() / 1000;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public boolean isStarted() {
        return this.mClockStartTimeUs > -1;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getRecordedDurationUs() {
        return this.mAudioDurationUs;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public void pauseClock() {
        this.mClockPauseTimeUs = System.nanoTime() / 1000;
        if (this.mPauseArrayIndex % 2 != 0) {
            return;
        }
        this.mPauseResumeIndices[this.mPauseArrayIndex] = this.mClockPauseTimeUs - this.mClockStartTimeUs;
        this.mPauseArrayIndex++;
        if (this.mPauseArrayIndex >= this.mPauseResumeIndices.length) {
            this.mPauseResumeIndices = Arrays.copyOf(this.mPauseResumeIndices, this.mPauseResumeIndices.length * 2);
        }
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public void resumeClock() {
        this.mClockResumeTimeUs = System.nanoTime() / 1000;
        if (this.mPauseArrayIndex % 2 == 0) {
            return;
        }
        this.mClockTotalPauseDurationUs += this.mClockResumeTimeUs - this.mClockPauseTimeUs;
        this.mPauseResumeIndices[this.mPauseArrayIndex] = this.mClockResumeTimeUs - this.mClockStartTimeUs;
        this.mPauseArrayIndex++;
        if (this.mPauseArrayIndex >= this.mPauseResumeIndices.length) {
            this.mPauseResumeIndices = Arrays.copyOf(this.mPauseResumeIndices, this.mPauseResumeIndices.length * 2);
        }
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public boolean isPausedAt(long j) {
        int iBinarySearch = Arrays.binarySearch(this.mPauseResumeIndices, 0, this.mPauseArrayIndex, j);
        if (iBinarySearch >= 0) {
            if (iBinarySearch % 2 == 0) {
                return true;
            }
        } else if (iBinarySearch % 2 == 0) {
            return true;
        }
        return false;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getDurationAtPauseUs() {
        return (this.mClockPauseTimeUs - this.mClockStartTimeUs) - this.mClockTotalPauseDurationUs;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getDurationAtStopUs() {
        return (this.mClockStopTimeUs - this.mClockStartTimeUs) - this.mClockTotalPauseDurationUs;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getTotalPausedDurationUs() {
        return this.mClockTotalPauseDurationUs;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public void stopClock() {
        this.mClockStopTimeUs = System.nanoTime() / 1000;
    }

    @Override // com.sonymobile.android.media.internal.ClockInterface
    public long getStopTimeUs() {
        return this.mClockStopTimeUs;
    }

    @Override // com.sonymobile.android.media.internal.Track
    public void setMediaMuxerStarted() {
        super.setMediaMuxerStarted();
        this.mMuxerHandler.obtainMessage(110).sendToTarget();
    }

    private class AudioRecordHandler extends Handler {
        AudioRecordHandler(Looper looper) {
            super(looper);
        }

        private void doHandleInputBuffer(int i) throws IllegalStateException, MediaCodec.CryptoException {
            int i2;
            int i3;
            ByteBuffer inputBuffer = AudioTrack.this.mEncoder.getInputBuffer(i);
            long j = AudioTrack.this.mAudioDurationUs + AudioTrack.this.mAudioOffset;
            int i4 = AudioTrack.this.mAudioRecorder.read(inputBuffer, AudioTrack.this.mInputBufferSize);
            long j2 = (1000000 * ((i4 / AudioTrack.this.mNumAudioChannels) / AudioTrack.this.mBytesPerSample)) / AudioTrack.this.mAudioSamplingRate;
            if (AudioTrack.this.mState == Track.States.PAUSED && AudioTrack.this.mAudioDurationUs + j2 > AudioTrack.this.mClock.getDurationAtPauseUs()) {
                if (!AudioTrack.this.mIsPauseLatchDown) {
                    AudioTrack.this.mPauseLatch.countDown();
                    AudioTrack.this.mIsPauseLatchDown = true;
                }
                if (inputBuffer == null || AudioTrack.this.mEosFlagged) {
                    return;
                }
                if (AudioTrack.this.mInputBufferSize > inputBuffer.limit()) {
                    AudioTrack.this.mInputBufferSize = inputBuffer.limit();
                }
                if (i4 >= 0) {
                    AudioTrack.this.mAudioRecordHandler.sendMessageDelayed(AudioTrack.this.mAudioRecordHandler.obtainMessage(102, i, 0), 1L);
                    return;
                } else {
                    Log.e(AudioTrack.TAG, "Read audio data is empty.");
                    AudioTrack.this.mCallback.obtainMessage(1, 4, 0).sendToTarget();
                    return;
                }
            }
            if (AudioTrack.this.mState == Track.States.STOPPED || inputBuffer == null || AudioTrack.this.mEosFlagged) {
                return;
            }
            if (AudioTrack.this.mInputBufferSize > inputBuffer.limit()) {
                AudioTrack.this.mInputBufferSize = inputBuffer.limit();
            }
            if (j < AudioTrack.this.mStartVolumeDelayUs) {
                byte[] bArr = new byte[i4];
                Arrays.fill(bArr, (byte) 0);
                inputBuffer.put(bArr);
            }
            AudioTrack.this.mAudioDurationUs += j2;
            if (AudioTrack.this.mState == Track.States.STOPPING) {
                AudioTrack.this.mEosFlagged = true;
                i3 = i4 >= 0 ? i4 : 0;
                i2 = 4;
            } else if (i4 < 0) {
                Log.e(AudioTrack.TAG, "Read audio data is empty.");
                AudioTrack.this.mCallback.obtainMessage(1, 4, 0).sendToTarget();
                return;
            } else {
                i2 = 0;
                i3 = i4;
            }
            AudioTrack.this.mEncoder.queueInputBuffer(i, 0, i3, j, i2);
        }

        private void doStartAudioRecorder() throws IllegalStateException {
            AudioTrack.this.mEosFlagged = false;
            AudioTrack.this.mFirstCodecFrame = true;
            try {
                AudioTrack.this.mAudioRecorder.startRecording();
            } catch (IllegalStateException unused) {
                Log.e(AudioTrack.TAG, "Could not start audio recorder, illegal state");
                AudioTrack.this.mCallback.obtainMessage(1, 4, 3);
            }
            AudioTrack.this.mEncoder.start();
            AudioTrack.this.mState = Track.States.STARTED;
        }

        private void doStopAudioRecorder() throws IllegalStateException {
            if (AudioTrack.this.mAudioRecorder != null) {
                try {
                    AudioTrack.this.mAudioRecorder.stop();
                } catch (IllegalStateException unused) {
                    Log.e(AudioTrack.TAG, "Could not stop audio recorder, illegal state");
                    AudioTrack.this.mCallback.obtainMessage(1, 4, 3);
                }
            }
            AudioTrack.this.mState = Track.States.STOPPING;
        }

        private void doReleaseAudioRecorder() {
            if (AudioTrack.this.mAudioRecorder != null) {
                AudioTrack.this.mAudioRecorder.release();
                AudioTrack.this.mAudioRecorder = null;
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws IllegalStateException, MediaCodec.CryptoException {
            int i = message.what;
            if (i == 102) {
                try {
                    doHandleInputBuffer(message.arg1);
                } catch (IllegalStateException unused) {
                    return;
                }
            }
            switch (i) {
                case 107:
                    doStopAudioRecorder();
                    Message messageObtainMessage = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage.obj = new Object();
                    messageObtainMessage.sendToTarget();
                    break;
                case 108:
                    doReleaseAudioRecorder();
                    Message messageObtainMessage2 = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage2.obj = new Object();
                    messageObtainMessage2.sendToTarget();
                    break;
                case 109:
                    doStartAudioRecorder();
                    Message messageObtainMessage3 = ((Handler) message.obj).obtainMessage();
                    messageObtainMessage3.obj = new Object();
                    messageObtainMessage3.sendToTarget();
                    break;
            }
        }
    }

    private class CodecHandler extends Handler {
        CodecHandler(Looper looper) {
            super(looper);
        }

        private void doQueueOutputBuffer(int i, MediaCodec.BufferInfo bufferInfo) throws IllegalStateException {
            if (AudioTrack.this.mFirstCodecFrame) {
                boolean z = (bufferInfo.flags & 2) == 2;
                addTrack();
                AudioTrack.this.mFirstCodecFrame = false;
                if (z) {
                    AudioTrack.this.mEncoder.releaseOutputBuffer(i, false);
                    return;
                }
            }
            Track.EncodedBuffer encodedBuffer = new Track.EncodedBuffer(i, bufferInfo);
            if (AudioTrack.this.mMuxerState == Track.MuxerState.IDLE) {
                ByteBuffer outputBuffer = AudioTrack.this.mEncoder.getOutputBuffer(i);
                if (outputBuffer != null) {
                    encodedBuffer.byteBuffer = ByteBuffer.allocate(outputBuffer.limit());
                    outputBuffer.rewind();
                    encodedBuffer.byteBuffer.put(outputBuffer);
                    encodedBuffer.containsCopiedBuffer = true;
                }
                AudioTrack.this.mEncoder.releaseOutputBuffer(i, false);
            }
            AudioTrack.this.mBufferList.add(encodedBuffer);
            if ((encodedBuffer.bufferInfo.flags & 4) != 0) {
                AudioTrack.this.mMuxerHandler.obtainMessage(110).sendToTarget();
            } else {
                AudioTrack.this.mMuxerHandler.obtainMessage(104).sendToTarget();
            }
        }

        private void addTrack() {
            if (AudioTrack.this.mMuxerTrackIndex < 0) {
                AudioTrack.this.mMuxerTrackIndex = AudioTrack.this.mMuxerWrapper.addTrack(AudioTrack.this.mEncoder.getOutputFormat());
                AudioTrack.this.mCallback.obtainMessage(1, 11, 0).sendToTarget();
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 105) {
                return;
            }
            try {
                doQueueOutputBuffer(message.arg1, (MediaCodec.BufferInfo) message.obj);
            } catch (IllegalStateException unused) {
            }
        }
    }

    private class AudioEncoderCallback extends MediaCodec.Callback {
        @Override // android.media.MediaCodec.Callback
        public void onOutputFormatChanged(@NonNull MediaCodec mediaCodec, @NonNull MediaFormat mediaFormat) {
        }

        private AudioEncoderCallback() {
        }

        @Override // android.media.MediaCodec.Callback
        public void onInputBufferAvailable(@NonNull MediaCodec mediaCodec, int i) {
            AudioTrack.this.mAudioRecordHandler.obtainMessage(102, i, 0).sendToTarget();
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputBufferAvailable(@NonNull MediaCodec mediaCodec, int i, @NonNull MediaCodec.BufferInfo bufferInfo) {
            AudioTrack.this.mCodecHandler.obtainMessage(105, i, 0, bufferInfo).sendToTarget();
        }

        @Override // android.media.MediaCodec.Callback
        public void onError(@NonNull MediaCodec mediaCodec, @NonNull MediaCodec.CodecException codecException) {
            Log.e(AudioTrack.TAG, "Error from encoder", codecException);
            AudioTrack.this.mCallback.obtainMessage(1, 4, 0).sendToTarget();
        }
    }
}
