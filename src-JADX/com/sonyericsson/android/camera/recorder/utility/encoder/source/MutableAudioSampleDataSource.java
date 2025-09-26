package com.sonyericsson.android.camera.recorder.utility.encoder.source;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CamLog;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MutableAudioSampleDataSource extends AudioSampleDataSourceBase {
    private static final long AUDIO_READ_INTERVAL_MILLIS = 100;
    private static final long AUDIO_READ_TIME_OUT_DURATION_MILLIS = 5000;
    private static final int BUFFER_DURATION_MILLIS = 2000;
    private int mAudioRecordBuferSize;
    private final ByteBuffer mGarbageBuffer;
    private final int mMinAudioBufferCount;
    private final int mMuteDurationBytes;
    private int mMuteDurationRemainingBytes;
    private int mNotificationCounter;
    private final int mSilentDurationBytes;

    public MutableAudioSampleDataSource(MediaCodec mediaCodec, int i, int i2, int i3, int i4, int i5) {
        super(mediaCodec, i, i2, i3);
        if (i4 < 0 || i5 < 0) {
            throw new IllegalArgumentException("Duration cannot be negative values");
        }
        this.mMuteDurationBytes = ((getSampleRate() * getSampleDataBytes()) * i4) / 1000;
        this.mSilentDurationBytes = ((getSampleRate() * getSampleDataBytes()) * i5) / 1000;
        this.mMinAudioBufferCount = (int) Math.ceil(i4 / 100.0d);
        this.mGarbageBuffer = ByteBuffer.allocateDirect(getBufferSize());
    }

    public void startMute() throws InterruptedException {
        if (getAudioRecord().getRecordingState() != 3) {
            throw new IllegalStateException("startMute can only be called during recording");
        }
        if (this.mMuteDurationRemainingBytes > 0) {
            throw new IllegalStateException("startMute cannot be called more than once");
        }
        this.mMuteDurationRemainingBytes = this.mMuteDurationBytes;
        sendAudioSamplesToGarbage();
        this.mNotificationCounter = 0;
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.source.AudioSampleDataSourceBase, android.media.AudioRecord.OnRecordPositionUpdateListener
    public void onPeriodicNotification(AudioRecord audioRecord) {
        if (this.mNotificationCounter > this.mMinAudioBufferCount) {
            super.onPeriodicNotification(audioRecord);
        } else {
            this.mNotificationCounter++;
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.source.AudioSampleDataSourceBase
    protected int getBufferSize() {
        if (this.mAudioRecordBuferSize == 0) {
            this.mAudioRecordBuferSize = ((2000 * getSampleRate()) * getSampleDataBytes()) / 1000;
            if (this.mAudioRecordBuferSize < getMinBufferSize() * 8) {
                this.mAudioRecordBuferSize = getMinBufferSize() * 8;
            }
        }
        return this.mAudioRecordBuferSize;
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.source.AudioSampleDataSourceBase
    protected int getAudioBufferSize() {
        return (getSampleRate() * getSampleDataBytes()) / 10;
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.source.AudioSampleDataSourceBase
    protected long pushToEncoder(byte[] bArr, int i, boolean z) throws MediaCodec.CryptoException {
        boolean z2 = false;
        int i2 = 0;
        while (!isCancelled() && !z2) {
            DequeuedBuffer dequeuedBufferDequeueBuffer = dequeueBuffer();
            if (dequeuedBufferDequeueBuffer != null) {
                long currentPresentationTime = getCurrentPresentationTime(i2);
                dequeuedBufferDequeueBuffer.mBuffer.put(bArr, i2 + 0, Math.min(dequeuedBufferDequeueBuffer.getLimit(), i - i2));
                int iPosition = dequeuedBufferDequeueBuffer.mBuffer.position();
                i2 += iPosition;
                boolean z3 = i2 >= i;
                queueInputBuffer(dequeuedBufferDequeueBuffer.mIndex, 0, iPosition, currentPresentationTime, 0);
                z2 = z3;
            }
        }
        if (z) {
            DequeuedBuffer dequeuedBufferDequeueBuffer2 = dequeueBuffer();
            long currentPresentationTime2 = getCurrentPresentationTime(i2);
            if (dequeuedBufferDequeueBuffer2 != null) {
                queueInputBuffer(dequeuedBufferDequeueBuffer2.mIndex, 0, 0, currentPresentationTime2, 4);
            }
        }
        return i2 / getSampleDataBytes();
    }

    private void sendAudioSamplesToGarbage() throws InterruptedException {
        int i;
        AudioRecord audioRecord = getAudioRecord();
        this.mGarbageBuffer.clear();
        if (this.mNotificationCounter > this.mMinAudioBufferCount) {
            i = audioRecord.read(this.mGarbageBuffer, this.mGarbageBuffer.limit(), 1);
            if (i < this.mMuteDurationRemainingBytes) {
                CamLog.w("Could not read enough audio samples.");
                long jUptimeMillis = SystemClock.uptimeMillis();
                while (SystemClock.uptimeMillis() - jUptimeMillis < AUDIO_READ_TIME_OUT_DURATION_MILLIS) {
                    i += audioRecord.read(this.mGarbageBuffer, this.mMuteDurationRemainingBytes - i, 1);
                    CamLog.w("Re-try to read until audio samples are retrieved enough. read-bytes:" + i);
                    if (i >= this.mMuteDurationRemainingBytes) {
                        break;
                    }
                    try {
                        Thread.sleep(AUDIO_READ_INTERVAL_MILLIS, 0);
                    } catch (InterruptedException unused) {
                        CamLog.e("Interrupt skipping audio samples in mute range.");
                    }
                }
            }
            int i2 = i - this.mMuteDurationRemainingBytes;
            if (i2 < 0) {
                CamLog.e("Could not read enough audio samples by re-trying. read-bytes:" + i + " remain-bytes:" + this.mMuteDurationRemainingBytes);
                i2 = 0;
            }
            byte[] bArr = new byte[i2];
            this.mGarbageBuffer.get(bArr, 0, i2);
            requestToEnqueueSamples(bArr, false);
        } else {
            i = audioRecord.read(this.mGarbageBuffer, this.mMuteDurationRemainingBytes, 0);
        }
        this.mMuteDurationRemainingBytes -= i;
        requestToEnqueueSilentSamples();
    }

    private void requestToEnqueueSamples(final byte[] bArr, final boolean z) {
        getBackgroundWorker().getHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.recorder.utility.encoder.source.MutableAudioSampleDataSource.1
            @Override // java.lang.Runnable
            public void run() throws MediaCodec.CryptoException {
                MutableAudioSampleDataSource.this.addSampleCount(MutableAudioSampleDataSource.this.pushToEncoder(bArr, bArr.length, z));
            }
        });
    }

    private void requestToEnqueueSilentSamples() {
        getBackgroundWorker().getHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.recorder.utility.encoder.source.MutableAudioSampleDataSource.2
            @Override // java.lang.Runnable
            public void run() throws MediaCodec.CryptoException {
                MutableAudioSampleDataSource.this.addSampleCount(MutableAudioSampleDataSource.this.enqueueSilentSamples());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long enqueueSilentSamples() throws MediaCodec.CryptoException {
        int i = this.mSilentDurationBytes;
        byte[] bArr = null;
        int i2 = 0;
        while (i > 0) {
            DequeuedBuffer dequeuedBufferDequeueBuffer = dequeueBuffer();
            if (dequeuedBufferDequeueBuffer != null) {
                if (bArr == null || bArr.length < dequeuedBufferDequeueBuffer.getLimit()) {
                    bArr = new byte[dequeuedBufferDequeueBuffer.getLimit()];
                }
                dequeuedBufferDequeueBuffer.mBuffer.put(bArr, 0, Math.min(dequeuedBufferDequeueBuffer.getLimit(), i));
                int iPosition = dequeuedBufferDequeueBuffer.mBuffer.position();
                queueInputBuffer(dequeuedBufferDequeueBuffer.mIndex, 0, iPosition, getCurrentPresentationTime(i2), 0);
                i2 += iPosition;
                i -= iPosition;
                if (CamLog.VERBOSE) {
                    CamLog.d(i + " silent bytes are remaining to write");
                }
            }
        }
        return i2 / getSampleDataBytes();
    }

    public long getCurrentPresentationTimeMillis() {
        return getPresentationTime(0L) / 1000;
    }

    private long getCurrentPresentationTime(long j) {
        return getPresentationTime(j / getSampleDataBytes());
    }

    private DequeuedBuffer dequeueBuffer() {
        int iDequeueInputBuffer = getCodec().dequeueInputBuffer(100000L);
        if (iDequeueInputBuffer < 0) {
            if (!CamLog.VERBOSE) {
                return null;
            }
            CamLog.d("  dequeue input buffer failed");
            return null;
        }
        return new DequeuedBuffer(iDequeueInputBuffer, getCodec().getInputBuffer(iDequeueInputBuffer));
    }

    private void queueInputBuffer(int i, int i2, int i3, long j, int i4) throws MediaCodec.CryptoException {
        getCodec().queueInputBuffer(i, i2, i3, j, i4);
        if (CamLog.VERBOSE) {
            CamLog.d("  Buffer index " + i + " is queued");
        }
    }

    private static class DequeuedBuffer {
        private final ByteBuffer mBuffer;
        private final int mIndex;

        public DequeuedBuffer(int i, ByteBuffer byteBuffer) {
            this.mIndex = i;
            this.mBuffer = byteBuffer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLimit() {
            return this.mBuffer.limit();
        }
    }
}
