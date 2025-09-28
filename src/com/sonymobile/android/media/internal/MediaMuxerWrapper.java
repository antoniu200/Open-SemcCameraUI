package com.sonymobile.android.media.internal;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class MediaMuxerWrapper {
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
    private long mRequestProgressInfoIntervalUs = -1;
    private long mTotalBuffer;
    private long[] mTrackTimeUs;

    interface MuxerListener {
        void onInfo(int i, int i2);

        void onMaxDurationReached();

        void onMaxFileSizeReached();

        void onStopError();

        void onWriteError();
    }

    MediaMuxerWrapper(String str, FileDescriptor fileDescriptor, int i, MuxerListener muxerListener) throws IOException, IllegalArgumentException {
        int i2 = i == 9 ? 1 : 0;
        if (Build.VERSION.SDK_INT >= 26 && i == 1) {
            i2 = 2;
        }
        this.mMuxer = new MediaMuxer(fileDescriptor, i2);
        if (str != null && !str.isEmpty()) {
            this.mFile = new File(str);
        } else {
            this.mFile = null;
        }
        this.mMuxerListener = muxerListener;
        this.mTrackTimeUs = new long[1];
        this.mLastProgressTimeUs = new long[1];
        this.externalToInternalTrackIdMap = new ArrayMap<>(4);
        this.mLimitReached = new boolean[1];
    }

    MediaMuxerWrapper(String str, int i, MuxerListener muxerListener) throws IOException, IllegalArgumentException {
        int i2 = i == 9 ? 1 : 0;
        if (Build.VERSION.SDK_INT >= 26 && i == 1) {
            i2 = 2;
        }
        this.mMuxer = new MediaMuxer(str, i2);
        this.mFile = new File(str);
        this.mMuxerListener = muxerListener;
        this.mTrackTimeUs = new long[1];
        this.mLastProgressTimeUs = new long[1];
        this.externalToInternalTrackIdMap = new ArrayMap<>(4);
        this.mLimitReached = new boolean[1];
    }

    void setOrientationHint(int i) {
        this.mMuxer.setOrientationHint(i);
    }

    void setLocation(float f, float f2) {
        this.mMuxer.setLocation(f, f2);
    }

    void setRequestProgressInfoInterval(long j) {
        this.mRequestProgressInfoIntervalUs = 1000 * j;
    }

    public void start() {
        for (int i = 0; i < this.mLastProgressTimeUs.length; i++) {
            this.mLastProgressTimeUs[i] = 0;
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
        } catch (IllegalStateException e) {
            Log.e(TAG, "exception when stopping mediaMuxer", e);
            this.mMuxerListener.onStopError();
        }
    }

    int addTrack(MediaFormat mediaFormat) {
        int iAddTrack = this.mMuxer.addTrack(mediaFormat);
        int i = 1;
        if (iAddTrack > this.mTrackTimeUs.length - 1) {
            int i2 = iAddTrack + 1;
            this.mTrackTimeUs = new long[i2];
            this.mLastProgressTimeUs = new long[i2];
            this.mLimitReached = new boolean[i2];
        }
        String string = mediaFormat.getString("mime");
        if (string.startsWith("audio/")) {
            i = 2;
        } else if (!string.startsWith("video/")) {
            i = 0;
        }
        this.externalToInternalTrackIdMap.put(Integer.valueOf(i), Integer.valueOf(iAddTrack));
        return i;
    }

    void endTrack(int i) {
        this.mMuxerListener.onInfo((i << 28) | 1000, 0);
    }

    void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        int iIntValue = this.externalToInternalTrackIdMap.get(Integer.valueOf(i)).intValue();
        if (this.mLimitReached[iIntValue]) {
            return;
        }
        if (this.mMaxDurationMs > 0 && bufferInfo.presentationTimeUs / 1000 > this.mMaxDurationMs) {
            this.mMuxerListener.onMaxDurationReached();
            this.mLimitReached[iIntValue] = true;
            return;
        }
        if (this.mFile != null && this.mMaxFileSizeBytes > 0 && (this.mFile.length() + byteBuffer.limit() > this.mMaxFileSizeBytes || this.mTotalBuffer + byteBuffer.limit() > this.mMaxFileSizeBytes)) {
            this.mMuxerListener.onMaxFileSizeReached();
            this.mLimitReached[iIntValue] = true;
            return;
        }
        try {
            this.mTotalBuffer += byteBuffer.limit();
            this.mMuxer.writeSampleData(iIntValue, byteBuffer, bufferInfo);
            this.mTrackTimeUs[iIntValue] = bufferInfo.presentationTimeUs;
        } catch (IllegalStateException unused) {
            CamLog.e("Muxer can not write");
            this.mMuxerListener.onWriteError();
            this.mLimitReached[iIntValue] = true;
        }
        if (this.mRequestProgressInfoIntervalUs < 0 || bufferInfo.presentationTimeUs - this.mLastProgressTimeUs[iIntValue] <= this.mRequestProgressInfoIntervalUs) {
            return;
        }
        this.mMuxerListener.onInfo((i << 28) | 1001, (int) ((bufferInfo.presentationTimeUs - this.mLastProgressTimeUs[iIntValue]) / 1000));
        this.mLastProgressTimeUs[iIntValue] = bufferInfo.presentationTimeUs;
    }

    void setMaxDuration(int i) {
        this.mMaxDurationMs = Math.max(0, i);
    }

    void setMaxFileSize(long j) {
        if (this.mFile != null) {
            j = (long) (EXPECTED_OVERHEAD * Math.min(j, this.mFile.getUsableSpace()));
        }
        this.mMaxFileSizeBytes = Math.max(0L, j);
    }

    public void release() {
        this.mMuxer.release();
        this.mMuxer = null;
    }
}
