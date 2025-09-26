// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility.encoder.source;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.BackgroundWorker;
import android.media.MediaCodec;
import android.media.AudioRecord;
import android.media.AudioRecord$OnRecordPositionUpdateListener;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;

public abstract class AudioSampleDataSourceBase implements InputDataSource, AudioRecord$OnRecordPositionUpdateListener
{
    protected static final long INPUTBUFFER_TIMEOUT_MICROSECONDS = 100000L;
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
    
    public AudioSampleDataSourceBase(final MediaCodec mCodec, int mSampleRate, final int n, final int n2) {
        if (mCodec == null) {
            throw new IllegalArgumentException("MediaCodec cannot be null");
        }
        this.mCodec = mCodec;
        this.mSampleRate = mSampleRate;
        this.mSampleDataBytes = this.bytesInFrame(n2) * n;
        if (n == 2) {
            mSampleRate = 12;
        }
        else {
            mSampleRate = 16;
        }
        this.mMinBufferSize = AudioRecord.getMinBufferSize(this.mSampleRate, mSampleRate, n2);
        this.mAudioRecord = new AudioRecord(1, this.mSampleRate, mSampleRate, n2, this.getBufferSize());
        if (this.mAudioRecord.getState() == 0) {
            throw new IllegalArgumentException("AudioRecord failed to initialize. Parameters might be invalid");
        }
        this.mAudioBuffer = new byte[this.getAudioBufferSize()];
        this.mNotificationPeriod = this.mSampleRate / 10;
        this.mWorker = new BackgroundWorker("AudioSampleDataReaderThread");
    }
    
    private int bytesInFrame(final int n) {
        switch (n) {
            default: {
                throw new IllegalStateException("Specified Audio format is not supported.");
            }
            case 3: {
                return 1;
            }
            case 2: {
                return 2;
            }
        }
    }
    
    protected static boolean isCancelled() {
        return Thread.currentThread().isInterrupted();
    }
    
    private void readSampleData(boolean b) {
        if (this.mAlreadyEos) {
            if (CamLog.VERBOSE) {
                CamLog.d("Already End of Stream");
            }
            return;
        }
        final int read = this.mAudioRecord.read(this.mAudioBuffer, 0, this.mAudioBuffer.length, 1);
        switch (read) {
            case -2: {
                if (CamLog.VERBOSE) {
                    CamLog.d("ERROR_BAD_VALUE");
                }
                return;
            }
            case -3: {
                if (CamLog.VERBOSE) {
                    CamLog.d("ERROR_INVALID_OPERATION");
                }
                b = true;
                break;
            }
        }
        this.addSampleCount(this.pushToEncoder(this.mAudioBuffer, read, b));
        if (b) {
            this.mAlreadyEos = true;
        }
    }
    
    private void requestToReadSampleData(final boolean b) {
        this.mWorker.getHandler().post((Runnable)new Runnable(this, b) {
            final AudioSampleDataSourceBase this$0;
            final boolean val$eos;
            
            @Override
            public void run() {
                this.this$0.readSampleData(this.val$eos);
            }
        });
    }
    
    protected void addSampleCount(final long n) {
        this.mSampleCount += n;
    }
    
    protected int getAudioBufferSize() {
        return this.mMinBufferSize * 8;
    }
    
    protected AudioRecord getAudioRecord() {
        return this.mAudioRecord;
    }
    
    protected BackgroundWorker getBackgroundWorker() {
        return this.mWorker;
    }
    
    protected int getBufferSize() {
        return this.mMinBufferSize * 8;
    }
    
    protected MediaCodec getCodec() {
        return this.mCodec;
    }
    
    protected int getMinBufferSize() {
        return this.mMinBufferSize;
    }
    
    protected long getPresentationTime(final long n) {
        return 1000000L * (this.mSampleCount + n) / this.mSampleRate;
    }
    
    protected int getSampleDataBytes() {
        return this.mSampleDataBytes;
    }
    
    protected int getSampleRate() {
        return this.mSampleRate;
    }
    
    public void onMarkerReached(final AudioRecord audioRecord) {
    }
    
    public void onPeriodicNotification(final AudioRecord audioRecord) {
        this.readSampleData(false);
    }
    
    protected abstract long pushToEncoder(final byte[] p0, final int p1, final boolean p2);
    
    @Override
    public void release() {
        this.mAudioRecord.release();
        try {
            this.mWorker.quit();
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit FINISHED");
            }
        }
        catch (final InterruptedException ex) {
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit INTERRUPTED");
            }
        }
    }
    
    @Override
    public void start() {
        this.mSampleCount = 0L;
        if (this.mAudioRecord.setPositionNotificationPeriod(this.mNotificationPeriod) != 0) {
            CamLog.e("setPositionNotificationPeriod:failed");
        }
        this.mAudioRecord.setRecordPositionUpdateListener((AudioRecord$OnRecordPositionUpdateListener)this, this.mWorker.getHandler());
        this.mAudioRecord.startRecording();
        this.requestToReadSampleData(false);
    }
    
    @Override
    public void stop() {
        this.mAudioRecord.stop();
        this.requestToReadSampleData(true);
        try {
            this.mWorker.quit();
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit FINISHED");
            }
        }
        catch (final InterruptedException ex) {
            if (CamLog.VERBOSE) {
                CamLog.d("worker.quit INTERRUPTED");
            }
        }
    }
}
