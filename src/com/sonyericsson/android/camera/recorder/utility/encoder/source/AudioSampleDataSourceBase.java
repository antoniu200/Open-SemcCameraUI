package com.sonyericsson.android.camera.recorder.utility.encoder.source;

import android.media.AudioRecord;
import android.media.MediaCodec;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;
import com.sonyericsson.android.camera.util.BackgroundWorker;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class AudioSampleDataSourceBase implements InputDataSource, AudioRecord.OnRecordPositionUpdateListener {
    protected static final long INPUTBUFFER_TIMEOUT_MICROSECONDS = 100000;
    protected static final int NOTIFICATION_COUNT_PER_SECOND = 10;
    private volatile boolean mAlreadyEos;
    private final byte[] mAudioBuffer;
    private final AudioRecord mAudioRecord;
    private final MediaCodec mCodec;
    private final int mMinBufferSize;
    private final int mNotificationPeriod;
    private long mSampleCount;
    private final int mSampleDataBytes;
    private final int mSampleRate;
    private final BackgroundWorker mWorker;

    @Override // android.media.AudioRecord.OnRecordPositionUpdateListener
    public void onMarkerReached(AudioRecord audioRecord) {
    }

    protected abstract long pushToEncoder(byte[] bArr, int i, boolean z);

    public AudioSampleDataSourceBase(MediaCodec mediaCodec, int i, int i2, int i3) {
        if (mediaCodec == null) {
            throw new IllegalArgumentException("MediaCodec cannot be null");
        }
        this.mCodec = mediaCodec;
        this.mSampleRate = i;
        this.mSampleDataBytes = bytesInFrame(i3) * i2;
        int i4 = i2 == 2 ? 12 : 16;
        this.mMinBufferSize = AudioRecord.getMinBufferSize(this.mSampleRate, i4, i3);
        this.mAudioRecord = new AudioRecord(1, this.mSampleRate, i4, i3, getBufferSize());
        if (this.mAudioRecord.getState() == 0) {
            throw new IllegalArgumentException("AudioRecord failed to initialize. Parameters might be invalid");
        }
        this.mAudioBuffer = new byte[getAudioBufferSize()];
        this.mNotificationPeriod = this.mSampleRate / 10;
        this.mWorker = new BackgroundWorker("AudioSampleDataReaderThread");
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource
    public void release() {
        this.mAudioRecord.release();
        try {
            this.mWorker.quit();
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit FINISHED");
            }
        } catch (InterruptedException unused) {
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit INTERRUPTED");
            }
        }
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource
    public void start() throws IllegalStateException {
        this.mSampleCount = 0L;
        if (this.mAudioRecord.setPositionNotificationPeriod(this.mNotificationPeriod) != 0) {
            CamLog.e("setPositionNotificationPeriod:failed");
        }
        this.mAudioRecord.setRecordPositionUpdateListener(this, this.mWorker.getHandler());
        this.mAudioRecord.startRecording();
        requestToReadSampleData(false);
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource
    public void stop() throws IllegalStateException {
        this.mAudioRecord.stop();
        requestToReadSampleData(true);
        try {
            this.mWorker.quit();
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit FINISHED");
            }
        } catch (InterruptedException unused) {
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit INTERRUPTED");
            }
        }
    }

    @Override // android.media.AudioRecord.OnRecordPositionUpdateListener
    public void onPeriodicNotification(AudioRecord audioRecord) {
        readSampleData(false);
    }

    protected static boolean isCancelled() {
        return Thread.currentThread().isInterrupted();
    }

    protected long getPresentationTime(long j) {
        return (1000000 * (this.mSampleCount + j)) / this.mSampleRate;
    }

    protected AudioRecord getAudioRecord() {
        return this.mAudioRecord;
    }

    protected MediaCodec getCodec() {
        return this.mCodec;
    }

    protected int getSampleRate() {
        return this.mSampleRate;
    }

    protected int getSampleDataBytes() {
        return this.mSampleDataBytes;
    }

    protected BackgroundWorker getBackgroundWorker() {
        return this.mWorker;
    }

    protected void addSampleCount(long j) {
        this.mSampleCount += j;
    }

    protected int getBufferSize() {
        return this.mMinBufferSize * 8;
    }

    protected int getAudioBufferSize() {
        return this.mMinBufferSize * 8;
    }

    protected int getMinBufferSize() {
        return this.mMinBufferSize;
    }

    private void requestToReadSampleData(final boolean z) {
        this.mWorker.getHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.recorder.utility.encoder.source.AudioSampleDataSourceBase.1
            @Override // java.lang.Runnable
            public void run() {
                AudioSampleDataSourceBase.this.readSampleData(z);
            }
        });
    }

    private int bytesInFrame(int i) {
        switch (i) {
            case 2:
                return 2;
            case 3:
                return 1;
            default:
                throw new IllegalStateException("Specified Audio format is not supported.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readSampleData(boolean z) {
        if (this.mAlreadyEos) {
            if (CamLog.VERBOSE) {
                CamLog.d("Already End of Stream");
                return;
            }
            return;
        }
        int i = this.mAudioRecord.read(this.mAudioBuffer, 0, this.mAudioBuffer.length, 1);
        switch (i) {
            case -3:
                if (CamLog.VERBOSE) {
                    CamLog.d("ERROR_INVALID_OPERATION");
                }
                z = true;
                break;
            case -2:
                if (CamLog.VERBOSE) {
                    CamLog.d("ERROR_BAD_VALUE");
                    return;
                }
                return;
        }
        addSampleCount(pushToEncoder(this.mAudioBuffer, i, z));
        if (z) {
            this.mAlreadyEos = true;
        }
    }
}
