// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media.internal;

import android.os.Message;
import android.os.Looper;
import android.media.MediaCodec$BufferInfo;
import android.media.MediaCodec$CodecException;
import android.support.annotation.NonNull;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import android.media.MediaCrypto;
import android.view.Surface;
import android.media.MediaCodec$Callback;
import java.util.concurrent.LinkedBlockingDeque;
import java.io.IOException;
import android.util.Log;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaCodecList;
import android.os.Handler;
import android.media.AudioRecord;
import android.os.HandlerThread;

class AudioTrack extends Track implements ClockInterface
{
    private static final int ENCODING_PCM_SETTING = 2;
    private static final String HW_AAC_ENCODER = "OMX.qcom.audio.encoder.aac";
    private static final boolean HW_AAC_ENCODER_AVAILABLE = true;
    private static final int HW_ENCODER_BITRATE = 156000;
    private static final int INIT_PAUSE_ARRAY_SIZE = 100;
    private static final int MAX_AUDIO_BITRATE = 156000;
    private static final int MIN_STOP_AUDIO_DURATION_US = 2000000;
    protected static final String TAG = "AudioTrack";
    private final String[] audioMimeTypes;
    private int mAudioBitRate;
    private long mAudioDurationUs;
    private String mAudioMime;
    private long mAudioOffset;
    private boolean mAudioRecord;
    private final AudioRecordHandler mAudioRecordHandler;
    private final HandlerThread mAudioRecordThread;
    private AudioRecord mAudioRecorder;
    private int mAudioSamplingRate;
    private int mAudioSource;
    private int mBytesPerSample;
    private final Handler mCallback;
    private long mClockPauseTimeUs;
    private long mClockResumeTimeUs;
    private long mClockStartTimeUs;
    private long mClockStopTimeUs;
    private long mClockTotalPauseDurationUs;
    private final CodecHandler mCodecHandler;
    private boolean mEosFlagged;
    private boolean mFirstCodecFrame;
    private int mInputBufferSize;
    private final MuxerHandler mMuxerHandler;
    private int mNumAudioChannels;
    private int mPauseArrayIndex;
    private long[] mPauseResumeIndices;
    private int mStartVolumeDelayUs;
    private boolean mStopAudioRecording;
    
    AudioTrack(final int mAudioSource, final Handler mCallback, final HandlerThread handlerThread, final HandlerThread handlerThread2, final HandlerThread handlerThread3, final boolean mAudioRecord) {
        this.mAudioRecorder = null;
        this.mAudioSource = 0;
        this.mAudioSamplingRate = 44100;
        this.mNumAudioChannels = 2;
        this.mAudioBitRate = 156000;
        this.mBytesPerSample = 2;
        this.mEosFlagged = false;
        this.mStartVolumeDelayUs = 0;
        this.audioMimeTypes = new String[] { "", "audio/3gpp", "audio/amr-wb", "audio/mp4a-latm", "audio/mp4a-latm", "audio/mp4a-latm", "audio/vorbis" };
        this.mAudioDurationUs = 0L;
        this.mClockStartTimeUs = -1L;
        this.mClockPauseTimeUs = -1L;
        this.mClockResumeTimeUs = -1L;
        this.mClockStopTimeUs = -1L;
        this.mFirstCodecFrame = true;
        this.mAudioRecord = true;
        this.mStopAudioRecording = false;
        this.mAudioSource = mAudioSource;
        this.mCodecHandler = new CodecHandler(handlerThread.getLooper());
        this.mEventHandler = new EventHandler(handlerThread2.getLooper());
        this.mMuxerHandler = new MuxerHandler(handlerThread3.getLooper());
        (this.mAudioRecordThread = new HandlerThread("AudioRecord", -1)).start();
        this.mAudioRecordHandler = new AudioRecordHandler(this.mAudioRecordThread.getLooper());
        this.mHandlerHelper = new HandlerHelper();
        this.mCallback = mCallback;
        this.mAudioMime = this.audioMimeTypes[3];
        this.mState = States.STOPPED;
        this.mBytesPerSample = 2;
        this.mAudioRecord = mAudioRecord;
    }
    
    void adjustAudioTimeStamp(final long n) {
        this.mAudioOffset = n * 1000L;
    }
    
    void adjustStartVolume(final int n) {
        this.mStartVolumeDelayUs = n * 1000;
    }
    
    @Override
    protected void doPause() {
        this.mState = States.PAUSED;
        if (!this.mAudioRecord && !this.mIsPauseLatchDown) {
            this.mPauseLatch.countDown();
            this.mIsPauseLatchDown = true;
        }
    }
    
    @Override
    protected void doPrepare() {
        final MediaCodecList list = new MediaCodecList(0);
        final MediaFormat audioFormat = MediaFormat.createAudioFormat(this.mAudioMime, this.mAudioSamplingRate, this.mNumAudioChannels);
        audioFormat.setInteger("bitrate", this.mAudioBitRate);
        if (this.mOperatingRate > 0) {
            audioFormat.setInteger("operating-rate", this.mOperatingRate);
        }
        if (!this.checkFormat(list, audioFormat, this.mAudioMime)) {
            goto Label_0334;
        }
        int n;
        if (this.mNumAudioChannels == 1) {
            n = 16;
        }
        else if (this.mNumAudioChannels == 2) {
            n = 12;
        }
        else {
            n = 1;
        }
        try {
            this.mInputBufferSize = AudioRecord.getMinBufferSize(this.mAudioSamplingRate, this.mNumAudioChannels, 2) * 2;
            this.mAudioRecorder = new AudioRecord(this.mAudioSource, this.mAudioSamplingRate, n, 2, this.mInputBufferSize);
            if (!this.mAudioMime.equals("audio/mp4a-latm")) {
                goto Label_0202;
            }
            try {
                this.mEncoder = MediaCodec.createByCodecName("OMX.qcom.audio.encoder.aac");
                goto Label_0202;
            }
            catch (final IOException | NullPointerException ex) {
                Log.e("AudioTrack", "Unable to create encoder", (Throwable)ex);
                this.mCallback.obtainMessage(1, 4, 2).sendToTarget();
                return;
            }
            catch (final IllegalArgumentException ex2) {
                goto Label_0202;
            }
            try {
                final String s;
                this.mEncoder = MediaCodec.createByCodecName(s);
                this.mBufferList = new LinkedBlockingDeque<EncodedBuffer>();
                this.mEncoder.setCallback((MediaCodec$Callback)new AudioEncoderCallback());
                this.mEncoder.configure(audioFormat, (Surface)null, (MediaCrypto)null, 1);
            }
            catch (final IOException | NullPointerException | IllegalArgumentException ex3) {}
        }
        catch (final IllegalArgumentException ex4) {}
    }
    
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
        this.mState = States.STOPPED;
    }
    
    @Override
    protected void doReset() {
        if (this.mState != States.STOPPED && this.mAudioRecord) {
            this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(107));
        }
    }
    
    @Override
    protected void doResume(final CountDownLatch countDownLatch) {
        this.mState = States.STARTED;
        countDownLatch.countDown();
    }
    
    @Override
    protected void doStart() {
        this.mStopAudioRecording = false;
        if (this.mAudioRecord) {
            this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(109));
        }
        else {
            this.mCallback.obtainMessage(1, 11, 0).sendToTarget();
        }
    }
    
    @Override
    protected void doStop() {
        if (this.mAudioRecord) {
            this.mHandlerHelper.sendMessageAndAwaitResponse(this.mAudioRecordHandler.obtainMessage(107));
        }
        else {
            this.mCallback.sendMessage(this.mCallback.obtainMessage(103));
        }
    }
    
    @Override
    protected void doWriteOutputBuffer() throws IllegalStateException {
        if (this.mClock.isStarted() && !this.mBufferList.isEmpty()) {
            if (!this.isMuxerStarted()) {
                return;
            }
            final EncodedBuffer encodedBuffer = this.mBufferList.remove();
            ByteBuffer byteBuffer;
            if (encodedBuffer.containsCopiedBuffer) {
                byteBuffer = encodedBuffer.byteBuffer;
            }
            else {
                byteBuffer = this.mEncoder.getOutputBuffer(encodedBuffer.bufferIndex);
            }
            if (byteBuffer != null) {
                if (!this.mStopAudioRecording || encodedBuffer.bufferInfo.presentationTimeUs < 2000000L) {
                    this.mMuxerWrapper.writeSampleData(this.mMuxerTrackIndex, byteBuffer, encodedBuffer.bufferInfo);
                }
            }
            if (!encodedBuffer.containsCopiedBuffer) {
                this.mEncoder.releaseOutputBuffer(encodedBuffer.bufferIndex, false);
            }
            if ((encodedBuffer.bufferInfo.flags & 0x4) != 0x0) {
                this.mEncoder.stop();
                this.mCallback.sendMessage(this.mCallback.obtainMessage(103));
                this.mState = States.STOPPED;
                this.mMuxerWrapper.endTrack(this.mMuxerTrackIndex);
            }
        }
    }
    
    @Override
    public long getCurrentTimeUs() {
        if (this.mClockStartTimeUs < 0L) {
            return this.mClockStartTimeUs;
        }
        if (this.mClockPauseTimeUs > this.mClockResumeTimeUs) {
            return this.getDurationAtPauseUs();
        }
        return this.getSystemTimeUs() - this.mClockStartTimeUs - this.mClockTotalPauseDurationUs;
    }
    
    @Override
    public long getDurationAtPauseUs() {
        return this.mClockPauseTimeUs - this.mClockStartTimeUs - this.mClockTotalPauseDurationUs;
    }
    
    @Override
    public long getDurationAtStopUs() {
        return this.mClockStopTimeUs - this.mClockStartTimeUs - this.mClockTotalPauseDurationUs;
    }
    
    @Override
    public long getRecordedDurationUs() {
        return this.mAudioDurationUs;
    }
    
    @Override
    public long getStartTimeUs() {
        return this.mClockStartTimeUs;
    }
    
    @Override
    public long getStopTimeUs() {
        return this.mClockStopTimeUs;
    }
    
    @Override
    public long getSystemTimeUs() {
        return System.nanoTime() / 1000L;
    }
    
    @Override
    public long getTotalPausedDurationUs() {
        return this.mClockTotalPauseDurationUs;
    }
    
    @Override
    public boolean isPausedAt(final long key) {
        final int binarySearch = Arrays.binarySearch(this.mPauseResumeIndices, 0, this.mPauseArrayIndex, key);
        boolean b = true;
        if (binarySearch >= 0) {
            if (binarySearch % 2 == 0) {
                return b;
            }
        }
        else if (binarySearch % 2 == 0) {
            return b;
        }
        b = false;
        return b;
    }
    
    @Override
    public boolean isStarted() {
        return this.mClockStartTimeUs > -1L;
    }
    
    @Override
    public void pauseClock() {
        this.mClockPauseTimeUs = System.nanoTime() / 1000L;
        if (this.mPauseArrayIndex % 2 != 0) {
            return;
        }
        this.mPauseResumeIndices[this.mPauseArrayIndex] = this.mClockPauseTimeUs - this.mClockStartTimeUs;
        ++this.mPauseArrayIndex;
        if (this.mPauseArrayIndex >= this.mPauseResumeIndices.length) {
            this.mPauseResumeIndices = Arrays.copyOf(this.mPauseResumeIndices, this.mPauseResumeIndices.length * 2);
        }
    }
    
    @Override
    public void resetClock() {
        this.mClockStartTimeUs = -1L;
    }
    
    @Override
    public void resumeClock() {
        this.mClockResumeTimeUs = System.nanoTime() / 1000L;
        if (this.mPauseArrayIndex % 2 == 0) {
            return;
        }
        this.mClockTotalPauseDurationUs += this.mClockResumeTimeUs - this.mClockPauseTimeUs;
        this.mPauseResumeIndices[this.mPauseArrayIndex] = this.mClockResumeTimeUs - this.mClockStartTimeUs;
        ++this.mPauseArrayIndex;
        if (this.mPauseArrayIndex >= this.mPauseResumeIndices.length) {
            this.mPauseResumeIndices = Arrays.copyOf(this.mPauseResumeIndices, this.mPauseResumeIndices.length * 2);
        }
    }
    
    void setAudioBitRate(final int mAudioBitRate) {
        if (mAudioBitRate < 156000) {
            this.mAudioBitRate = mAudioBitRate;
        }
    }
    
    void setAudioChannels(final int mNumAudioChannels) {
        this.mNumAudioChannels = mNumAudioChannels;
    }
    
    void setAudioEncoder(final int n) {
        if (n >= 0 && n <= this.audioMimeTypes.length - 1) {
            this.mAudioMime = this.audioMimeTypes[n];
        }
        else {
            this.mAudioMime = null;
        }
    }
    
    void setAudioSamplingRate(final int mAudioSamplingRate) {
        this.mAudioSamplingRate = mAudioSamplingRate;
    }
    
    @Override
    public void setMediaMuxerStarted() {
        super.setMediaMuxerStarted();
        this.mMuxerHandler.obtainMessage(110).sendToTarget();
    }
    
    @Override
    public void setStartTime() {
        this.mClockStartTimeUs = this.getSystemTimeUs();
        this.mPauseArrayIndex = 0;
        this.mPauseResumeIndices = new long[100];
    }
    
    protected void stopAudioRecording() {
        this.mStopAudioRecording = true;
    }
    
    @Override
    public void stopClock() {
        this.mClockStopTimeUs = System.nanoTime() / 1000L;
    }
    
    private class AudioEncoderCallback extends MediaCodec$Callback
    {
        final AudioTrack this$0;
        
        private AudioEncoderCallback(final AudioTrack this$0) {
            this.this$0 = this$0;
        }
        
        public void onError(@NonNull final MediaCodec mediaCodec, @NonNull final MediaCodec$CodecException ex) {
            Log.e("AudioTrack", "Error from encoder", (Throwable)ex);
            this.this$0.mCallback.obtainMessage(1, 4, 0).sendToTarget();
        }
        
        public void onInputBufferAvailable(@NonNull final MediaCodec mediaCodec, final int n) {
            this.this$0.mAudioRecordHandler.obtainMessage(102, n, 0).sendToTarget();
        }
        
        public void onOutputBufferAvailable(@NonNull final MediaCodec mediaCodec, final int n, @NonNull final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
            this.this$0.mCodecHandler.obtainMessage(105, n, 0, (Object)mediaCodec$BufferInfo).sendToTarget();
        }
        
        public void onOutputFormatChanged(@NonNull final MediaCodec mediaCodec, @NonNull final MediaFormat mediaFormat) {
        }
    }
    
    private class AudioRecordHandler extends Handler
    {
        final AudioTrack this$0;
        
        AudioRecordHandler(final AudioTrack this$0, final Looper looper) {
            this.this$0 = this$0;
            super(looper);
        }
        
        private void doHandleInputBuffer(final int n) throws IllegalStateException {
            final ByteBuffer inputBuffer = this.this$0.mEncoder.getInputBuffer(n);
            final long n2 = this.this$0.mAudioDurationUs + this.this$0.mAudioOffset;
            int read = this.this$0.mAudioRecorder.read(inputBuffer, this.this$0.mInputBufferSize);
            final long n3 = 1000000L * (read / this.this$0.mNumAudioChannels / this.this$0.mBytesPerSample) / this.this$0.mAudioSamplingRate;
            final States mState = this.this$0.mState;
            final States paused = States.PAUSED;
            final int n4 = 0;
            if (mState == paused && this.this$0.mAudioDurationUs + n3 > this.this$0.mClock.getDurationAtPauseUs()) {
                if (!this.this$0.mIsPauseLatchDown) {
                    this.this$0.mPauseLatch.countDown();
                    this.this$0.mIsPauseLatchDown = true;
                }
                if (inputBuffer != null && !this.this$0.mEosFlagged) {
                    if (this.this$0.mInputBufferSize > inputBuffer.limit()) {
                        this.this$0.mInputBufferSize = inputBuffer.limit();
                    }
                    if (read < 0) {
                        Log.e("AudioTrack", "Read audio data is empty.");
                        this.this$0.mCallback.obtainMessage(1, 4, 0).sendToTarget();
                        return;
                    }
                    this.this$0.mAudioRecordHandler.sendMessageDelayed(this.this$0.mAudioRecordHandler.obtainMessage(102, n, 0), 1L);
                }
            }
            else if (this.this$0.mState != States.STOPPED && inputBuffer != null && !this.this$0.mEosFlagged) {
                if (this.this$0.mInputBufferSize > inputBuffer.limit()) {
                    this.this$0.mInputBufferSize = inputBuffer.limit();
                }
                if (n2 < this.this$0.mStartVolumeDelayUs) {
                    final byte[] array = new byte[read];
                    Arrays.fill(array, (byte)0);
                    inputBuffer.put(array);
                }
                this.this$0.mAudioDurationUs += n3;
                int n5;
                if (this.this$0.mState == States.STOPPING) {
                    this.this$0.mEosFlagged = true;
                    if (read < 0) {
                        read = n4;
                    }
                    n5 = 4;
                }
                else {
                    if (read < 0) {
                        Log.e("AudioTrack", "Read audio data is empty.");
                        this.this$0.mCallback.obtainMessage(1, 4, 0).sendToTarget();
                        return;
                    }
                    n5 = 0;
                }
                this.this$0.mEncoder.queueInputBuffer(n, 0, read, n2, n5);
            }
        }
        
        private void doReleaseAudioRecorder() {
            if (this.this$0.mAudioRecorder != null) {
                this.this$0.mAudioRecorder.release();
                this.this$0.mAudioRecorder = null;
            }
        }
        
        private void doStartAudioRecorder() {
            this.this$0.mEosFlagged = false;
            this.this$0.mFirstCodecFrame = true;
            try {
                this.this$0.mAudioRecorder.startRecording();
            }
            catch (final IllegalStateException ex) {
                Log.e("AudioTrack", "Could not start audio recorder, illegal state");
                this.this$0.mCallback.obtainMessage(1, 4, 3);
            }
            this.this$0.mEncoder.start();
            this.this$0.mState = States.STARTED;
        }
        
        private void doStopAudioRecorder() {
            if (this.this$0.mAudioRecorder != null) {
                try {
                    this.this$0.mAudioRecorder.stop();
                }
                catch (final IllegalStateException ex) {
                    Log.e("AudioTrack", "Could not stop audio recorder, illegal state");
                    this.this$0.mCallback.obtainMessage(1, 4, 3);
                }
            }
            this.this$0.mState = States.STOPPING;
        }
        
        public void handleMessage(Message message) {
            final int what = message.what;
            if (what != 102) {
                switch (what) {
                    default: {
                        return;
                    }
                    case 109: {
                        this.doStartAudioRecorder();
                        message = ((Handler)message.obj).obtainMessage();
                        message.obj = new Object();
                        message.sendToTarget();
                        return;
                    }
                    case 108: {
                        this.doReleaseAudioRecorder();
                        message = ((Handler)message.obj).obtainMessage();
                        message.obj = new Object();
                        message.sendToTarget();
                        return;
                    }
                    case 107: {
                        this.doStopAudioRecorder();
                        message = ((Handler)message.obj).obtainMessage();
                        message.obj = new Object();
                        message.sendToTarget();
                        return;
                    }
                }
            }
            try {
                this.doHandleInputBuffer(message.arg1);
            }
            catch (final IllegalStateException ex) {}
        }
    }
    
    private class CodecHandler extends Handler
    {
        final AudioTrack this$0;
        
        CodecHandler(final AudioTrack this$0, final Looper looper) {
            this.this$0 = this$0;
            super(looper);
        }
        
        private void addTrack() {
            if (this.this$0.mMuxerTrackIndex < 0) {
                this.this$0.mMuxerTrackIndex = this.this$0.mMuxerWrapper.addTrack(this.this$0.mEncoder.getOutputFormat());
                this.this$0.mCallback.obtainMessage(1, 11, 0).sendToTarget();
            }
        }
        
        private void doQueueOutputBuffer(final int n, final MediaCodec$BufferInfo mediaCodec$BufferInfo) throws IllegalStateException {
            if (this.this$0.mFirstCodecFrame) {
                final boolean b = (mediaCodec$BufferInfo.flags & 0x2) == 0x2;
                this.addTrack();
                this.this$0.mFirstCodecFrame = false;
                if (b) {
                    this.this$0.mEncoder.releaseOutputBuffer(n, false);
                    return;
                }
            }
            final EncodedBuffer e = new EncodedBuffer(n, mediaCodec$BufferInfo);
            if (this.this$0.mMuxerState == MuxerState.IDLE) {
                final ByteBuffer outputBuffer = this.this$0.mEncoder.getOutputBuffer(n);
                if (outputBuffer != null) {
                    e.byteBuffer = ByteBuffer.allocate(outputBuffer.limit());
                    outputBuffer.rewind();
                    e.byteBuffer.put(outputBuffer);
                    e.containsCopiedBuffer = true;
                }
                this.this$0.mEncoder.releaseOutputBuffer(n, false);
            }
            this.this$0.mBufferList.add(e);
            if ((e.bufferInfo.flags & 0x4) != 0x0) {
                this.this$0.mMuxerHandler.obtainMessage(110).sendToTarget();
            }
            else {
                this.this$0.mMuxerHandler.obtainMessage(104).sendToTarget();
            }
        }
        
        public void handleMessage(final Message message) {
            if (message.what != 105) {
                return;
            }
            try {
                this.doQueueOutputBuffer(message.arg1, (MediaCodec$BufferInfo)message.obj);
            }
            catch (final IllegalStateException ex) {}
        }
    }
}
