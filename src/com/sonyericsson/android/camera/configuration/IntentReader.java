package com.sonyericsson.android.camera.configuration;

import android.content.Intent;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IntentReader {
    private static final long INVALID = -1;
    private static final String KEY_CROP = "crop";
    public static final String TAG = "IntentReader";
    long mVideoMaxDurationInMillisecs;
    long mVideoMaxFileSizeInBytes;
    int mVideoQuality;
    boolean mhasLimit = false;

    public static class VideoQualityConfigurations {
        private static final int EXTRA_VIDEO_QUALITY_LOW = 0;
        public final boolean hasSizeLimit;
        public final long maxDuration;
        public final long maxFileSize;
        public final int quality;

        public VideoQualityConfigurations(long j, long j2, int i, boolean z) {
            this.maxFileSize = j;
            this.maxDuration = j2;
            this.quality = i;
            this.hasSizeLimit = z;
        }

        public boolean isQualityLow() {
            return this.quality == 0;
        }
    }

    public VideoQualityConfigurations getVideoQualityConfigurations(Intent intent) {
        readIntent(intent);
        return new VideoQualityConfigurations(this.mVideoMaxFileSizeInBytes, this.mVideoMaxDurationInMillisecs, this.mVideoQuality, this.mhasLimit);
    }

    private void readIntent(Intent intent) {
        String action = intent.getAction();
        if (CamLog.VERBOSE) {
            CamLog.d("#### intent action : " + action);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("#### dump all extra: " + intent.getExtras());
        }
        if (action.equals("android.media.action.VIDEO_CAPTURE")) {
            long longExtra = intent.getLongExtra("android.intent.extra.sizeLimit", -1L);
            if (CamLog.VERBOSE) {
                CamLog.d("#### extra MediaStore.EXTRA_SIZE_LIMIT long: " + longExtra);
            }
            if (longExtra == -1) {
                int intExtra = intent.getIntExtra("android.intent.extra.sizeLimit", -1);
                if (CamLog.VERBOSE) {
                    CamLog.d("#### extra MediaStore.EXTRA_SIZE_LIMIT int: " + intExtra);
                }
                setVideoMaxFileSizeInBytes(intExtra);
            } else {
                setVideoMaxFileSizeInBytes(longExtra);
            }
            int intExtra2 = intent.getIntExtra("android.intent.extra.durationLimit", -1);
            if (CamLog.VERBOSE) {
                CamLog.d("#### extra MediaStore.EXTRA_DURATION_LIMIT: " + intExtra2);
            }
            long j = intExtra2;
            if (j == -1) {
                setVideoMaxDurationInMillisecs(-1L);
            } else {
                setVideoMaxDurationInMillisecs(j * 1000);
            }
            setVideoQuality(intent.getIntExtra("android.intent.extra.videoQuality", -1), this.mVideoMaxFileSizeInBytes, this.mVideoMaxDurationInMillisecs);
        } else {
            setVideoMaxFileSizeInBytes(-1L);
            setVideoMaxDurationInMillisecs(-1L);
            setVideoQuality(-1L, this.mVideoMaxFileSizeInBytes, this.mVideoMaxDurationInMillisecs);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("#### intent extra.maxFileSize      : " + this.mVideoMaxFileSizeInBytes);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("#### intent extra.maxDuration      : " + this.mVideoMaxDurationInMillisecs);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("#### intent extra.quality          : " + this.mVideoQuality);
        }
    }

    private void setVideoMaxFileSizeInBytes(long j) {
        this.mVideoMaxFileSizeInBytes = j;
        if (j > 0) {
            this.mhasLimit = true;
        }
    }

    private void setVideoMaxDurationInMillisecs(long j) {
        this.mVideoMaxDurationInMillisecs = j;
        if (j > 0) {
            this.mhasLimit = true;
        }
    }

    private void setVideoQuality(long j, long j2, long j3) {
        if (j == 0) {
            this.mVideoQuality = 0;
            return;
        }
        if (j == 1) {
            this.mVideoQuality = 1;
            return;
        }
        if (j == 5) {
            this.mVideoQuality = 5;
        } else if (j == 4) {
            this.mVideoQuality = 4;
        } else {
            this.mVideoQuality = 1;
        }
    }
}
