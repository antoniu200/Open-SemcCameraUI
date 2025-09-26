// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import com.sonyericsson.cameracommon.utility.RecordingUtil;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.configuration.Configurations;

public class MaxVideoSize
{
    public static final long GUARANTEED_MIN_DURATION_IN_MILLIS = 3000L;
    private static final long MAX_FILE_SIZE_BYTES = 256000000000L;
    private static final long MAX_RECORDING_DURATION_IN_MILLIS = 21600000L;
    private static final long QUALITY_LOW_MAX_FILE_SIZE = 300000L;
    public static final String TAG = "MaxVideoSize";
    private long mMaxDurationMillis;
    private long mMaxFileSizeBytes;
    
    public static MaxVideoSize create(final Configurations configurations, final RecordingProfile recordingProfile, final Storage storage, final Storage.StorageType storageType) {
        final long min = Math.min(RecordingUtil.getRecordableSizeKBytes(storage, storageType), 256000000L);
        final MaxVideoSize maxVideoSize = createMaxVideoSize(configurations, recordingProfile, min);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Recordable storage size(kbytes): ");
            sb.append(min);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Modified max size(bytes): ");
            sb2.append(maxVideoSize.mMaxFileSizeBytes);
            CamLog.d(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Modified max duration(millisecs): ");
            sb3.append(maxVideoSize.mMaxDurationMillis);
            CamLog.d(sb3.toString());
        }
        return maxVideoSize;
    }
    
    private static MaxVideoSize createMaxVideoSize(final Configurations configurations, final RecordingProfile recordingProfile, final long n) {
        if (recordingProfile.isMms) {
            return createQualityLowMaxVideoSize(configurations, recordingProfile, n);
        }
        return createQualityHighMaxVideoSize(configurations, recordingProfile, n);
    }
    
    private static MaxVideoSize createQualityHighMaxVideoSize(final Configurations configurations, final RecordingProfile recordingProfile, long min) {
        final long n = 1000L * min;
        final MaxVideoSize maxVideoSize = new MaxVideoSize();
        final long videoMaxDurationInMillisecs = configurations.getVideoMaxDurationInMillisecs();
        final long videoMaxFileSizeInBytes = configurations.getVideoMaxFileSizeInBytes();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Config Max duration: ");
            sb.append(videoMaxDurationInMillisecs);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Config Max size: ");
            sb2.append(videoMaxFileSizeInBytes);
            CamLog.d(sb2.toString());
        }
        final long n2 = lcmp(videoMaxFileSizeInBytes, 0L);
        if (n2 <= 0 && videoMaxDurationInMillisecs <= 0L) {
            maxVideoSize.setMaxDurationMillis(21600000L);
            maxVideoSize.setMaxFileSizeBytes(n);
        }
        if (n2 <= 0 && videoMaxDurationInMillisecs > 0L) {
            min = Math.min(videoMaxDurationInMillisecs, 21600000L);
            maxVideoSize.setMaxDurationMillis(min);
            maxVideoSize.setMaxFileSizeBytes(n);
        }
        else {
            min = 21600000L;
        }
        if (n2 > 0 && videoMaxDurationInMillisecs <= 0L) {
            maxVideoSize.setMaxDurationMillis(Math.min(min, getDurationFromSizeInMillis(recordingProfile, videoMaxFileSizeInBytes)));
            maxVideoSize.setMaxFileSizeBytes(Math.min(videoMaxFileSizeInBytes, n));
        }
        if (n2 > 0 && videoMaxDurationInMillisecs > 0L) {
            maxVideoSize.setMaxDurationMillis(Math.min(Math.min(videoMaxDurationInMillisecs, 21600000L), getDurationFromSizeInMillis(recordingProfile, videoMaxFileSizeInBytes)));
            maxVideoSize.setMaxFileSizeBytes(Math.min(videoMaxFileSizeInBytes, n));
        }
        return maxVideoSize;
    }
    
    private static MaxVideoSize createQualityLowMaxVideoSize(final Configurations configurations, final RecordingProfile recordingProfile, long n) {
        final MaxVideoSize maxVideoSize = new MaxVideoSize();
        final long n2 = 1024L * n;
        final long videoMaxFileSizeInBytes = configurations.getVideoMaxFileSizeInBytes();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Max size: ");
            sb.append(n2);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Config Max size: ");
            sb2.append(videoMaxFileSizeInBytes);
            CamLog.d(sb2.toString());
        }
        if (videoMaxFileSizeInBytes > 0L && videoMaxFileSizeInBytes < n2) {
            maxVideoSize.setMaxFileSizeBytes(videoMaxFileSizeInBytes);
        }
        else {
            maxVideoSize.setMaxFileSizeBytes(n2);
        }
        n = RecordingUtil.getDurationMillsFromAverage(n, recordingProfile.averageFileSize);
        final long videoMaxDurationInMillisecs = configurations.getVideoMaxDurationInMillisecs();
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Max duration: ");
            sb3.append(n);
            CamLog.d(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Config Max duration: ");
            sb4.append(videoMaxDurationInMillisecs);
            CamLog.d(sb4.toString());
        }
        final long n3 = lcmp(videoMaxDurationInMillisecs, 0L);
        if (n3 > 0) {
            maxVideoSize.setMaxDurationMillis(Math.min(videoMaxDurationInMillisecs, n));
        }
        else {
            maxVideoSize.setMaxDurationMillis(n);
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Quality Low Max duration: ");
            sb5.append(2147483647L);
            CamLog.d(sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Quality Low Max size: ");
            sb6.append(300000L);
            CamLog.d(sb6.toString());
        }
        maxVideoSize.setMaxFileSizeBytes(Math.min(300000L, maxVideoSize.getMaxFileSize()));
        final long b = n = getDurationFromSizeInMillis(recordingProfile, maxVideoSize.getMaxFileSize());
        if (n3 > 0) {
            n = Math.min(videoMaxDurationInMillisecs, b);
        }
        maxVideoSize.setMaxDurationMillis(Math.min(2147483647L, n));
        return maxVideoSize;
    }
    
    private static long getDurationFromSizeInMillis(final RecordingProfile recordingProfile, long lng) {
        final long lng2 = recordingProfile.averageFileSize * 1024L / 60L;
        lng = (long)Math.floor(lng / (double)lng2);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sizePerSecond(Byte): ");
            sb.append(lng2);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("durationInSecond(sec): ");
            sb2.append(lng);
            CamLog.d(sb2.toString());
        }
        return lng * 1000L;
    }
    
    private void setMaxDurationMillis(final long n) {
        long mMaxDurationMillis = n;
        if (n > 2147483647L) {
            mMaxDurationMillis = 2147483647L;
        }
        this.mMaxDurationMillis = mMaxDurationMillis;
    }
    
    private void setMaxFileSizeBytes(final long mMaxFileSizeBytes) {
        this.mMaxFileSizeBytes = mMaxFileSizeBytes;
    }
    
    public int getMaxDuration() {
        return (int)this.mMaxDurationMillis;
    }
    
    public long getMaxFileSize() {
        return this.mMaxFileSizeBytes;
    }
}
