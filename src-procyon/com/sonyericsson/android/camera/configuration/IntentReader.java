// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration;

import com.sonyericsson.android.camera.util.CamLog;
import android.content.Intent;

public class IntentReader
{
    private static final long INVALID = -1L;
    private static final String KEY_CROP = "crop";
    public static final String TAG = "IntentReader";
    long mVideoMaxDurationInMillisecs;
    long mVideoMaxFileSizeInBytes;
    int mVideoQuality;
    boolean mhasLimit;
    
    public IntentReader() {
        this.mhasLimit = false;
    }
    
    private void readIntent(final Intent intent) {
        final String action = intent.getAction();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("#### intent action : ");
            sb.append(action);
            CamLog.d(sb.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("#### dump all extra: ");
            sb2.append(intent.getExtras());
            CamLog.d(sb2.toString());
        }
        if (action.equals("android.media.action.VIDEO_CAPTURE")) {
            final long longExtra = intent.getLongExtra("android.intent.extra.sizeLimit", -1L);
            if (CamLog.VERBOSE) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("#### extra MediaStore.EXTRA_SIZE_LIMIT long: ");
                sb3.append(longExtra);
                CamLog.d(sb3.toString());
            }
            if (longExtra == -1L) {
                final int intExtra = intent.getIntExtra("android.intent.extra.sizeLimit", -1);
                if (CamLog.VERBOSE) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("#### extra MediaStore.EXTRA_SIZE_LIMIT int: ");
                    sb4.append(intExtra);
                    CamLog.d(sb4.toString());
                }
                this.setVideoMaxFileSizeInBytes(intExtra);
            }
            else {
                this.setVideoMaxFileSizeInBytes(longExtra);
            }
            final int intExtra2 = intent.getIntExtra("android.intent.extra.durationLimit", -1);
            if (CamLog.VERBOSE) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("#### extra MediaStore.EXTRA_DURATION_LIMIT: ");
                sb5.append(intExtra2);
                CamLog.d(sb5.toString());
            }
            final long n = intExtra2;
            if (n == -1L) {
                this.setVideoMaxDurationInMillisecs(-1L);
            }
            else {
                this.setVideoMaxDurationInMillisecs(n * 1000L);
            }
            this.setVideoQuality(intent.getIntExtra("android.intent.extra.videoQuality", -1), this.mVideoMaxFileSizeInBytes, this.mVideoMaxDurationInMillisecs);
        }
        else {
            this.setVideoMaxFileSizeInBytes(-1L);
            this.setVideoMaxDurationInMillisecs(-1L);
            this.setVideoQuality(-1L, this.mVideoMaxFileSizeInBytes, this.mVideoMaxDurationInMillisecs);
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("#### intent extra.maxFileSize      : ");
            sb6.append(this.mVideoMaxFileSizeInBytes);
            CamLog.d(sb6.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("#### intent extra.maxDuration      : ");
            sb7.append(this.mVideoMaxDurationInMillisecs);
            CamLog.d(sb7.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("#### intent extra.quality          : ");
            sb8.append(this.mVideoQuality);
            CamLog.d(sb8.toString());
        }
    }
    
    private void setVideoMaxDurationInMillisecs(final long mVideoMaxDurationInMillisecs) {
        this.mVideoMaxDurationInMillisecs = mVideoMaxDurationInMillisecs;
        if (mVideoMaxDurationInMillisecs > 0L) {
            this.mhasLimit = true;
        }
    }
    
    private void setVideoMaxFileSizeInBytes(final long mVideoMaxFileSizeInBytes) {
        this.mVideoMaxFileSizeInBytes = mVideoMaxFileSizeInBytes;
        if (mVideoMaxFileSizeInBytes > 0L) {
            this.mhasLimit = true;
        }
    }
    
    private void setVideoQuality(final long n, final long n2, final long n3) {
        if (n == 0L) {
            this.mVideoQuality = 0;
        }
        else if (n == 1L) {
            this.mVideoQuality = 1;
        }
        else if (n == 5L) {
            this.mVideoQuality = 5;
        }
        else if (n == 4L) {
            this.mVideoQuality = 4;
        }
        else {
            this.mVideoQuality = 1;
        }
    }
    
    public VideoQualityConfigurations getVideoQualityConfigurations(final Intent intent) {
        this.readIntent(intent);
        return new VideoQualityConfigurations(this.mVideoMaxFileSizeInBytes, this.mVideoMaxDurationInMillisecs, this.mVideoQuality, this.mhasLimit);
    }
    
    public static class VideoQualityConfigurations
    {
        private static final int EXTRA_VIDEO_QUALITY_LOW = 0;
        public final boolean hasSizeLimit;
        public final long maxDuration;
        public final long maxFileSize;
        public final int quality;
        
        public VideoQualityConfigurations(final long maxFileSize, final long maxDuration, final int quality, final boolean hasSizeLimit) {
            this.maxFileSize = maxFileSize;
            this.maxDuration = maxDuration;
            this.quality = quality;
            this.hasSizeLimit = hasSizeLimit;
        }
        
        public boolean isQualityLow() {
            return this.quality == 0;
        }
    }
}
