// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility.encoder;

import android.media.MediaCodec$BufferInfo;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
import java.io.IOException;
import java.io.FileDescriptor;
import android.media.MediaMuxer;
import java.io.File;

public class MediaMuxerWrapper
{
    private static final float EXPECTED_OVERHEAD = 0.95f;
    private static final int MIN_DURATION_MS = 100;
    private final File mFile;
    private long[] mFirstFramePresentationTimeUs;
    private boolean[] mIsFirstFrameArrived;
    private long[] mLastProgressTimeUs;
    private final MuxerListener mListener;
    private long mMaxDurationUs;
    private long mMaxFileSize;
    private boolean mMaxLimitationReached;
    private final MediaMuxer mMuxer;
    
    public MediaMuxerWrapper(final FileDescriptor fileDescriptor, final int n, final MuxerListener mListener) throws IOException {
        this.mMaxDurationUs = Long.MAX_VALUE;
        this.mMaxLimitationReached = false;
        this.mMuxer = new MediaMuxer(fileDescriptor, n);
        this.mFile = null;
        this.mListener = mListener;
        this.mIsFirstFrameArrived = new boolean[1];
        this.mFirstFramePresentationTimeUs = new long[1];
        this.mLastProgressTimeUs = new long[1];
    }
    
    public MediaMuxerWrapper(final String pathname, final int n, final MuxerListener mListener) throws IOException {
        this.mMaxDurationUs = Long.MAX_VALUE;
        this.mMaxLimitationReached = false;
        this.mMuxer = new MediaMuxer(pathname, n);
        this.mFile = new File(pathname);
        this.mListener = mListener;
        this.mIsFirstFrameArrived = new boolean[1];
        this.mFirstFramePresentationTimeUs = new long[1];
        this.mLastProgressTimeUs = new long[1];
    }
    
    public int addTrack(final MediaFormat mediaFormat) {
        final int addTrack = this.mMuxer.addTrack(mediaFormat);
        if (addTrack > this.mFirstFramePresentationTimeUs.length - 1) {
            final int n = addTrack + 1;
            this.mIsFirstFrameArrived = new boolean[n];
            this.mFirstFramePresentationTimeUs = new long[n];
            this.mLastProgressTimeUs = new long[n];
        }
        return addTrack;
    }
    
    public void release() {
        this.mMuxer.release();
    }
    
    public void setLocation(final float n, final float n2) {
        this.mMuxer.setLocation(n, n2);
    }
    
    public void setMaxDuration(final long n) {
        if (n > 0L && n < 100L) {
            throw new IllegalArgumentException("Minmimum duration too short");
        }
        this.mMaxDurationUs = n * 1000L;
    }
    
    public void setMaxFileSize(final long a) {
        long b = a;
        if (this.mFile != null) {
            b = (long)(0.95f * Math.min(a, this.mFile.getUsableSpace()));
        }
        this.mMaxFileSize = Math.max(0L, b);
    }
    
    public void setOrientationHint(final int orientationHint) {
        this.mMuxer.setOrientationHint(orientationHint);
    }
    
    public void start() {
        this.mMuxer.start();
    }
    
    public void stop() {
        this.mMuxer.stop();
    }
    
    public void writeSampleData(final int n, final ByteBuffer byteBuffer, final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
        Label_0046: {
            if (mediaCodec$BufferInfo.flags != 4) {
                break Label_0046;
            }
            mediaCodec$BufferInfo.presentationTimeUs = this.mLastProgressTimeUs[n] + 1L;
            mediaCodec$BufferInfo.size = 0;
            Block_10_Outer:Block_7_Outer:
            while (true) {
                try {
                    this.mMuxer.writeSampleData(n, byteBuffer, mediaCodec$BufferInfo);
                    this.mLastProgressTimeUs[n] = mediaCodec$BufferInfo.presentationTimeUs;
                    return;
                    while (true) {
                    Block_4_Outer:
                        while (true) {
                            while (true) {
                                while (true) {
                                    mediaCodec$BufferInfo.presentationTimeUs -= this.mFirstFramePresentationTimeUs[n];
                                    try {
                                        this.mMuxer.writeSampleData(n, byteBuffer, mediaCodec$BufferInfo);
                                    }
                                    catch (final IllegalStateException ex) {
                                        this.mMaxLimitationReached = true;
                                        this.mListener.onStorageFull();
                                    }
                                    Block_11: {
                                        Label_0234: {
                                            break Label_0234;
                                            this.mListener.onMaxFileSizeReached();
                                            this.mMaxLimitationReached = true;
                                            return;
                                            Label_0165: {
                                                iftrue(Label_0191:)(this.mIsFirstFrameArrived[n]);
                                            }
                                            break Block_11;
                                            iftrue(Label_0075:)(!this.mMaxLimitationReached);
                                            return;
                                        }
                                        this.mLastProgressTimeUs[n] = mediaCodec$BufferInfo.presentationTimeUs;
                                        this.mListener.onProgress(this.mLastProgressTimeUs[n]);
                                        return;
                                        this.mMaxLimitationReached = true;
                                        this.mListener.onMaxDurationReached();
                                        return;
                                        Label_0259: {
                                            return;
                                        }
                                    }
                                    this.mIsFirstFrameArrived[n] = true;
                                    this.mFirstFramePresentationTimeUs[n] = mediaCodec$BufferInfo.presentationTimeUs;
                                    continue Block_10_Outer;
                                }
                                iftrue(Label_0259:)(this.mLastProgressTimeUs[n] >= mediaCodec$BufferInfo.presentationTimeUs - this.mFirstFramePresentationTimeUs[n]);
                                continue Block_7_Outer;
                            }
                            Label_0113: {
                                iftrue(Label_0165:)(this.mFile == null || this.mMaxFileSize <= 0L || this.mFile.length() + byteBuffer.limit() <= this.mMaxFileSize);
                            }
                            continue Block_4_Outer;
                        }
                        Label_0075: {
                            iftrue(Label_0113:)(this.mLastProgressTimeUs[n] < this.mMaxDurationUs || this.mMaxDurationUs <= 0L);
                        }
                        continue;
                    }
                }
                catch (final IllegalStateException ex2) {
                    continue;
                }
                break;
            }
        }
    }
    
    public interface MuxerListener
    {
        void onMaxDurationReached();
        
        void onMaxFileSizeReached();
        
        void onProgress(final long p0);
        
        void onStorageFull();
    }
}
