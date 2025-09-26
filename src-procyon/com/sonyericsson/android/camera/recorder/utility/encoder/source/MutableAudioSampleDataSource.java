// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility.encoder.source;

import android.media.AudioRecord;
import android.os.SystemClock;
import com.sonyericsson.android.camera.util.CamLog;
import android.media.MediaCodec;
import java.nio.ByteBuffer;

public class MutableAudioSampleDataSource extends AudioSampleDataSourceBase
{
    private static final long AUDIO_READ_INTERVAL_MILLIS = 100L;
    private static final long AUDIO_READ_TIME_OUT_DURATION_MILLIS = 5000L;
    private static final int BUFFER_DURATION_MILLIS = 2000;
    private int mAudioRecordBuferSize;
    private final ByteBuffer mGarbageBuffer;
    private final int mMinAudioBufferCount;
    private final int mMuteDurationBytes;
    private int mMuteDurationRemainingBytes;
    private int mNotificationCounter;
    private final int mSilentDurationBytes;
    
    public MutableAudioSampleDataSource(final MediaCodec mediaCodec, final int n, final int n2, final int n3, final int n4, final int n5) {
        super(mediaCodec, n, n2, n3);
        if (n4 >= 0 && n5 >= 0) {
            this.mMuteDurationBytes = this.getSampleRate() * this.getSampleDataBytes() * n4 / 1000;
            this.mSilentDurationBytes = this.getSampleRate() * this.getSampleDataBytes() * n5 / 1000;
            this.mMinAudioBufferCount = (int)Math.ceil(n4 / 100.0);
            this.mGarbageBuffer = ByteBuffer.allocateDirect(this.getBufferSize());
            return;
        }
        throw new IllegalArgumentException("Duration cannot be negative values");
    }
    
    private DequeuedBuffer dequeueBuffer() {
        final int dequeueInputBuffer = this.getCodec().dequeueInputBuffer(100000L);
        if (dequeueInputBuffer < 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("  dequeue input buffer failed");
            }
            return null;
        }
        return new DequeuedBuffer(dequeueInputBuffer, this.getCodec().getInputBuffer(dequeueInputBuffer));
    }
    
    private long enqueueSilentSamples() {
        int i = this.mSilentDurationBytes;
        byte[] array = null;
        int n = 0;
        while (i > 0) {
            final DequeuedBuffer dequeueBuffer = this.dequeueBuffer();
            if (dequeueBuffer == null) {
                continue;
            }
            byte[] src = null;
            Label_0057: {
                if (array != null) {
                    src = array;
                    if (array.length >= dequeueBuffer.getLimit()) {
                        break Label_0057;
                    }
                }
                src = new byte[dequeueBuffer.getLimit()];
            }
            dequeueBuffer.mBuffer.put(src, 0, Math.min(dequeueBuffer.getLimit(), i));
            final int position = dequeueBuffer.mBuffer.position();
            this.queueInputBuffer(dequeueBuffer.mIndex, 0, position, this.getCurrentPresentationTime(n), 0);
            final int n2 = n + position;
            final int j = i -= position;
            n = n2;
            array = src;
            if (!CamLog.VERBOSE) {
                continue;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(j);
            sb.append(" silent bytes are remaining to write");
            CamLog.d(sb.toString());
            i = j;
            n = n2;
            array = src;
        }
        return n / this.getSampleDataBytes();
    }
    
    private long getCurrentPresentationTime(final long n) {
        return this.getPresentationTime(n / this.getSampleDataBytes());
    }
    
    private void queueInputBuffer(final int i, final int n, final int n2, final long n3, final int n4) {
        this.getCodec().queueInputBuffer(i, n, n2, n3, n4);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("  Buffer index ");
            sb.append(i);
            sb.append(" is queued");
            CamLog.d(sb.toString());
        }
    }
    
    private void requestToEnqueueSamples(final byte[] array, final boolean b) {
        this.getBackgroundWorker().getHandler().post((Runnable)new Runnable(this, array, b) {
            final MutableAudioSampleDataSource this$0;
            final boolean val$eos;
            final byte[] val$inputByteArray;
            
            @Override
            public void run() {
                this.this$0.addSampleCount(this.this$0.pushToEncoder(this.val$inputByteArray, this.val$inputByteArray.length, this.val$eos));
            }
        });
    }
    
    private void requestToEnqueueSilentSamples() {
        this.getBackgroundWorker().getHandler().post((Runnable)new Runnable(this) {
            final MutableAudioSampleDataSource this$0;
            
            @Override
            public void run() {
                this.this$0.addSampleCount(this.this$0.enqueueSilentSamples());
            }
        });
    }
    
    private void sendAudioSamplesToGarbage() {
        final AudioRecord audioRecord = this.getAudioRecord();
        this.mGarbageBuffer.clear();
        int read2;
        if (this.mNotificationCounter > this.mMinAudioBufferCount) {
            int read = audioRecord.read(this.mGarbageBuffer, this.mGarbageBuffer.limit(), 1);
            if ((read2 = read) < this.mMuteDurationRemainingBytes) {
                CamLog.w("Could not read enough audio samples.");
                final long uptimeMillis = SystemClock.uptimeMillis();
                while (true) {
                    read2 = read;
                    if (SystemClock.uptimeMillis() - uptimeMillis >= 5000L) {
                        break;
                    }
                    read += audioRecord.read(this.mGarbageBuffer, this.mMuteDurationRemainingBytes - read, 1);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Re-try to read until audio samples are retrieved enough. read-bytes:");
                    sb.append(read);
                    CamLog.w(sb.toString());
                    if ((read2 = read) >= this.mMuteDurationRemainingBytes) {
                        break;
                    }
                    try {
                        Thread.sleep(100L, 0);
                    }
                    catch (final InterruptedException ex) {
                        CamLog.e("Interrupt skipping audio samples in mute range.");
                    }
                }
            }
            int length;
            if ((length = read2 - this.mMuteDurationRemainingBytes) < 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Could not read enough audio samples by re-trying. read-bytes:");
                sb2.append(read2);
                sb2.append(" remain-bytes:");
                sb2.append(this.mMuteDurationRemainingBytes);
                CamLog.e(sb2.toString());
                length = 0;
            }
            final byte[] dst = new byte[length];
            this.mGarbageBuffer.get(dst, 0, length);
            this.requestToEnqueueSamples(dst, false);
        }
        else {
            read2 = audioRecord.read(this.mGarbageBuffer, this.mMuteDurationRemainingBytes, 0);
        }
        this.mMuteDurationRemainingBytes -= read2;
        this.requestToEnqueueSilentSamples();
    }
    
    @Override
    protected int getAudioBufferSize() {
        return this.getSampleRate() * this.getSampleDataBytes() / 10;
    }
    
    @Override
    protected int getBufferSize() {
        if (this.mAudioRecordBuferSize == 0) {
            this.mAudioRecordBuferSize = 2000 * this.getSampleRate() * this.getSampleDataBytes() / 1000;
            if (this.mAudioRecordBuferSize < this.getMinBufferSize() * 8) {
                this.mAudioRecordBuferSize = this.getMinBufferSize() * 8;
            }
        }
        return this.mAudioRecordBuferSize;
    }
    
    public long getCurrentPresentationTimeMillis() {
        return this.getPresentationTime(0L) / 1000L;
    }
    
    @Override
    public void onPeriodicNotification(final AudioRecord audioRecord) {
        if (this.mNotificationCounter > this.mMinAudioBufferCount) {
            super.onPeriodicNotification(audioRecord);
        }
        else {
            ++this.mNotificationCounter;
        }
    }
    
    @Override
    protected long pushToEncoder(final byte[] src, final int n, final boolean b) {
        int n2 = 0;
        int n3 = 0;
        while (!AudioSampleDataSourceBase.isCancelled() && n2 == 0) {
            final DequeuedBuffer dequeueBuffer = this.dequeueBuffer();
            if (dequeueBuffer == null) {
                continue;
            }
            final long currentPresentationTime = this.getCurrentPresentationTime(n3);
            dequeueBuffer.mBuffer.put(src, n3 + 0, Math.min(dequeueBuffer.getLimit(), n - n3));
            final int position = dequeueBuffer.mBuffer.position();
            final int n4 = n3 + position;
            final boolean b2 = n4 >= n;
            this.queueInputBuffer(dequeueBuffer.mIndex, 0, position, currentPresentationTime, 0);
            n2 = (b2 ? 1 : 0);
            n3 = n4;
        }
        if (b) {
            final DequeuedBuffer dequeueBuffer2 = this.dequeueBuffer();
            final long currentPresentationTime2 = this.getCurrentPresentationTime(n3);
            if (dequeueBuffer2 != null) {
                this.queueInputBuffer(dequeueBuffer2.mIndex, 0, 0, currentPresentationTime2, 4);
            }
        }
        return n3 / this.getSampleDataBytes();
    }
    
    public void startMute() {
        if (this.getAudioRecord().getRecordingState() != 3) {
            throw new IllegalStateException("startMute can only be called during recording");
        }
        if (this.mMuteDurationRemainingBytes > 0) {
            throw new IllegalStateException("startMute cannot be called more than once");
        }
        this.mMuteDurationRemainingBytes = this.mMuteDurationBytes;
        this.sendAudioSamplesToGarbage();
        this.mNotificationCounter = 0;
    }
    
    private static class DequeuedBuffer
    {
        private final ByteBuffer mBuffer;
        private final int mIndex;
        
        public DequeuedBuffer(final int mIndex, final ByteBuffer mBuffer) {
            this.mIndex = mIndex;
            this.mBuffer = mBuffer;
        }
        
        private int getLimit() {
            return this.mBuffer.limit();
        }
    }
}
