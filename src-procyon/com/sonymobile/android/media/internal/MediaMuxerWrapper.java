// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media.internal;

import com.sonyericsson.android.camera.util.CamLog;
import android.media.MediaCodec$BufferInfo;
import java.nio.ByteBuffer;
import android.util.Log;
import android.media.MediaFormat;
import java.io.FileDescriptor;
import java.io.IOException;
import android.os.Build$VERSION;
import android.media.MediaMuxer;
import java.io.File;
import android.util.ArrayMap;

class MediaMuxerWrapper
{
    private static final float EXPECTED_OVERHEAD = 0.95f;
    private static final int EXTERNAL_TRACK_ID_AUDIO = 2;
    private static final int EXTERNAL_TRACK_ID_OTHER = 0;
    private static final int EXTERNAL_TRACK_ID_VIDEO = 1;
    private static final String TAG = "MediaMuxerWrapper";
    private ArrayMap<Integer, Integer> externalToInternalTrackIdMap;
    private final File mFile;
    private long[] mLastProgressTimeUs;
    private boolean[] mLimitReached;
    private int mMaxDurationMs;
    private long mMaxFileSizeBytes;
    private MediaMuxer mMuxer;
    private MuxerListener mMuxerListener;
    private long mRequestProgressInfoIntervalUs;
    private long mTotalBuffer;
    private long[] mTrackTimeUs;
    
    MediaMuxerWrapper(final String pathname, final int n, final MuxerListener mMuxerListener) throws IOException, IllegalArgumentException {
        this.mRequestProgressInfoIntervalUs = -1L;
        boolean b;
        if (n == 9) {
            b = true;
        }
        else {
            b = false;
        }
        int n2 = b ? 1 : 0;
        if (Build$VERSION.SDK_INT >= 26) {
            n2 = (b ? 1 : 0);
            if (n == 1) {
                n2 = 2;
            }
        }
        this.mMuxer = new MediaMuxer(pathname, n2);
        this.mFile = new File(pathname);
        this.mMuxerListener = mMuxerListener;
        this.mTrackTimeUs = new long[1];
        this.mLastProgressTimeUs = new long[1];
        this.externalToInternalTrackIdMap = (ArrayMap<Integer, Integer>)new ArrayMap(4);
        this.mLimitReached = new boolean[1];
    }
    
    MediaMuxerWrapper(final String pathname, final FileDescriptor fileDescriptor, final int n, final MuxerListener mMuxerListener) throws IOException, IllegalArgumentException {
        this.mRequestProgressInfoIntervalUs = -1L;
        boolean b;
        if (n == 9) {
            b = true;
        }
        else {
            b = false;
        }
        int n2 = b ? 1 : 0;
        if (Build$VERSION.SDK_INT >= 26) {
            n2 = (b ? 1 : 0);
            if (n == 1) {
                n2 = 2;
            }
        }
        this.mMuxer = new MediaMuxer(fileDescriptor, n2);
        if (pathname != null && !pathname.isEmpty()) {
            this.mFile = new File(pathname);
        }
        else {
            this.mFile = null;
        }
        this.mMuxerListener = mMuxerListener;
        this.mTrackTimeUs = new long[1];
        this.mLastProgressTimeUs = new long[1];
        this.externalToInternalTrackIdMap = (ArrayMap<Integer, Integer>)new ArrayMap(4);
        this.mLimitReached = new boolean[1];
    }
    
    int addTrack(final MediaFormat mediaFormat) {
        final int addTrack = this.mMuxer.addTrack(mediaFormat);
        final int length = this.mTrackTimeUs.length;
        int i = 1;
        if (addTrack > length - 1) {
            final int n = addTrack + 1;
            this.mTrackTimeUs = new long[n];
            this.mLastProgressTimeUs = new long[n];
            this.mLimitReached = new boolean[n];
        }
        final String string = mediaFormat.getString("mime");
        if (string.startsWith("audio/")) {
            i = 2;
        }
        else if (!string.startsWith("video/")) {
            i = 0;
        }
        this.externalToInternalTrackIdMap.put((Object)i, (Object)addTrack);
        return i;
    }
    
    void endTrack(final int n) {
        this.mMuxerListener.onInfo(n << 28 | 0x3E8, 0);
    }
    
    public void release() {
        this.mMuxer.release();
        this.mMuxer = null;
    }
    
    void setLocation(final float n, final float n2) {
        this.mMuxer.setLocation(n, n2);
    }
    
    void setMaxDuration(final int b) {
        this.mMaxDurationMs = Math.max(0, b);
    }
    
    void setMaxFileSize(final long a) {
        long b = a;
        if (this.mFile != null) {
            b = (long)(0.95f * Math.min(a, this.mFile.getUsableSpace()));
        }
        this.mMaxFileSizeBytes = Math.max(0L, b);
    }
    
    void setOrientationHint(final int orientationHint) {
        this.mMuxer.setOrientationHint(orientationHint);
    }
    
    void setRequestProgressInfoInterval(final long n) {
        this.mRequestProgressInfoIntervalUs = 1000L * n;
    }
    
    public void start() {
        for (int i = 0; i < this.mLastProgressTimeUs.length; ++i) {
            this.mLastProgressTimeUs[i] = 0L;
            this.mLimitReached[i] = false;
        }
        this.mMuxer.start();
        if (this.mFile != null) {
            this.mTotalBuffer = this.mFile.length();
        }
    }
    
    public void stop() {
        try {
            this.mMuxer.stop();
        }
        catch (final IllegalStateException ex) {
            Log.e("MediaMuxerWrapper", "exception when stopping mediaMuxer", (Throwable)ex);
            this.mMuxerListener.onStopError();
        }
    }
    
    void writeSampleData(final int i, final ByteBuffer byteBuffer, final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
        final int intValue = (int)this.externalToInternalTrackIdMap.get((Object)i);
        if (!this.mLimitReached[intValue]) {
            if (this.mMaxDurationMs > 0 && mediaCodec$BufferInfo.presentationTimeUs / 1000L > this.mMaxDurationMs) {
                this.mMuxerListener.onMaxDurationReached();
                this.mLimitReached[intValue] = true;
                return;
            }
            if (this.mFile != null && this.mMaxFileSizeBytes > 0L && (this.mFile.length() + byteBuffer.limit() > this.mMaxFileSizeBytes || this.mTotalBuffer + byteBuffer.limit() > this.mMaxFileSizeBytes)) {
                this.mMuxerListener.onMaxFileSizeReached();
                this.mLimitReached[intValue] = true;
                return;
            }
            try {
                this.mTotalBuffer += byteBuffer.limit();
                this.mMuxer.writeSampleData(intValue, byteBuffer, mediaCodec$BufferInfo);
                this.mTrackTimeUs[intValue] = mediaCodec$BufferInfo.presentationTimeUs;
            }
            catch (final IllegalStateException ex) {
                CamLog.e("Muxer can not write");
                this.mMuxerListener.onWriteError();
                this.mLimitReached[intValue] = true;
            }
            if (this.mRequestProgressInfoIntervalUs >= 0L && mediaCodec$BufferInfo.presentationTimeUs - this.mLastProgressTimeUs[intValue] > this.mRequestProgressInfoIntervalUs) {
                this.mMuxerListener.onInfo(i << 28 | 0x3E9, (int)((mediaCodec$BufferInfo.presentationTimeUs - this.mLastProgressTimeUs[intValue]) / 1000L));
                this.mLastProgressTimeUs[intValue] = mediaCodec$BufferInfo.presentationTimeUs;
            }
        }
    }
    
    interface MuxerListener
    {
        void onInfo(final int p0, final int p1);
        
        void onMaxDurationReached();
        
        void onMaxFileSizeReached();
        
        void onStopError();
        
        void onWriteError();
    }
}
