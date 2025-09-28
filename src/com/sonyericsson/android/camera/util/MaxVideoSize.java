package com.sonyericsson.android.camera.util;

import android.support.v4.media.session.PlaybackStateCompat;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.utility.RecordingUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MaxVideoSize {
    public static final long GUARANTEED_MIN_DURATION_IN_MILLIS = 3000;
    private static final long MAX_FILE_SIZE_BYTES = 256000000000L;
    private static final long MAX_RECORDING_DURATION_IN_MILLIS = 21600000;
    private static final long QUALITY_LOW_MAX_FILE_SIZE = 300000;
    public static final String TAG = "MaxVideoSize";
    private long mMaxDurationMillis;
    private long mMaxFileSizeBytes;

    public int getMaxDuration() {
        return (int) this.mMaxDurationMillis;
    }

    public long getMaxFileSize() {
        return this.mMaxFileSizeBytes;
    }

    private void setMaxDurationMillis(long j) {
        if (j > 2147483647L) {
            j = 2147483647L;
        }
        this.mMaxDurationMillis = j;
    }

    private void setMaxFileSizeBytes(long j) {
        this.mMaxFileSizeBytes = j;
    }

    private static long getDurationFromSizeInMillis(RecordingProfile recordingProfile, long j) {
        long j2 = (recordingProfile.averageFileSize * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / 60;
        long jFloor = (long) Math.floor(j / j2);
        if (CamLog.VERBOSE) {
            CamLog.d("sizePerSecond(Byte): " + j2);
            CamLog.d("durationInSecond(sec): " + jFloor);
        }
        return jFloor * 1000;
    }

    public static MaxVideoSize create(Configurations configurations, RecordingProfile recordingProfile, Storage storage, Storage.StorageType storageType) {
        long jMin = Math.min(RecordingUtil.getRecordableSizeKBytes(storage, storageType), 256000000L);
        MaxVideoSize maxVideoSizeCreateMaxVideoSize = createMaxVideoSize(configurations, recordingProfile, jMin);
        if (CamLog.VERBOSE) {
            CamLog.d("Recordable storage size(kbytes): " + jMin);
            CamLog.d("Modified max size(bytes): " + maxVideoSizeCreateMaxVideoSize.mMaxFileSizeBytes);
            CamLog.d("Modified max duration(millisecs): " + maxVideoSizeCreateMaxVideoSize.mMaxDurationMillis);
        }
        return maxVideoSizeCreateMaxVideoSize;
    }

    private static MaxVideoSize createMaxVideoSize(Configurations configurations, RecordingProfile recordingProfile, long j) {
        if (recordingProfile.isMms) {
            return createQualityLowMaxVideoSize(configurations, recordingProfile, j);
        }
        return createQualityHighMaxVideoSize(configurations, recordingProfile, j);
    }

    private static MaxVideoSize createQualityHighMaxVideoSize(Configurations configurations, RecordingProfile recordingProfile, long j) {
        long jMin;
        long j2 = 1000 * j;
        MaxVideoSize maxVideoSize = new MaxVideoSize();
        long videoMaxDurationInMillisecs = configurations.getVideoMaxDurationInMillisecs();
        long videoMaxFileSizeInBytes = configurations.getVideoMaxFileSizeInBytes();
        if (CamLog.VERBOSE) {
            CamLog.d("Config Max duration: " + videoMaxDurationInMillisecs);
            CamLog.d("Config Max size: " + videoMaxFileSizeInBytes);
        }
        if (videoMaxFileSizeInBytes <= 0 && videoMaxDurationInMillisecs <= 0) {
            maxVideoSize.setMaxDurationMillis(MAX_RECORDING_DURATION_IN_MILLIS);
            maxVideoSize.setMaxFileSizeBytes(j2);
        }
        if (videoMaxFileSizeInBytes > 0 || videoMaxDurationInMillisecs <= 0) {
            jMin = 21600000;
        } else {
            jMin = Math.min(videoMaxDurationInMillisecs, MAX_RECORDING_DURATION_IN_MILLIS);
            maxVideoSize.setMaxDurationMillis(jMin);
            maxVideoSize.setMaxFileSizeBytes(j2);
        }
        if (videoMaxFileSizeInBytes > 0 && videoMaxDurationInMillisecs <= 0) {
            maxVideoSize.setMaxDurationMillis(Math.min(jMin, getDurationFromSizeInMillis(recordingProfile, videoMaxFileSizeInBytes)));
            maxVideoSize.setMaxFileSizeBytes(Math.min(videoMaxFileSizeInBytes, j2));
        }
        if (videoMaxFileSizeInBytes > 0 && videoMaxDurationInMillisecs > 0) {
            maxVideoSize.setMaxDurationMillis(Math.min(Math.min(videoMaxDurationInMillisecs, MAX_RECORDING_DURATION_IN_MILLIS), getDurationFromSizeInMillis(recordingProfile, videoMaxFileSizeInBytes)));
            maxVideoSize.setMaxFileSizeBytes(Math.min(videoMaxFileSizeInBytes, j2));
        }
        return maxVideoSize;
    }

    private static MaxVideoSize createQualityLowMaxVideoSize(Configurations configurations, RecordingProfile recordingProfile, long j) {
        MaxVideoSize maxVideoSize = new MaxVideoSize();
        long j2 = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID * j;
        long videoMaxFileSizeInBytes = configurations.getVideoMaxFileSizeInBytes();
        if (CamLog.VERBOSE) {
            CamLog.d("Max size: " + j2);
            CamLog.d("Config Max size: " + videoMaxFileSizeInBytes);
        }
        if (videoMaxFileSizeInBytes > 0 && videoMaxFileSizeInBytes < j2) {
            maxVideoSize.setMaxFileSizeBytes(videoMaxFileSizeInBytes);
        } else {
            maxVideoSize.setMaxFileSizeBytes(j2);
        }
        long durationMillsFromAverage = RecordingUtil.getDurationMillsFromAverage(j, recordingProfile.averageFileSize);
        long videoMaxDurationInMillisecs = configurations.getVideoMaxDurationInMillisecs();
        if (CamLog.VERBOSE) {
            CamLog.d("Max duration: " + durationMillsFromAverage);
            CamLog.d("Config Max duration: " + videoMaxDurationInMillisecs);
        }
        if (videoMaxDurationInMillisecs > 0) {
            maxVideoSize.setMaxDurationMillis(Math.min(videoMaxDurationInMillisecs, durationMillsFromAverage));
        } else {
            maxVideoSize.setMaxDurationMillis(durationMillsFromAverage);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Quality Low Max duration: 2147483647");
            CamLog.d("Quality Low Max size: " + QUALITY_LOW_MAX_FILE_SIZE);
        }
        maxVideoSize.setMaxFileSizeBytes(Math.min(QUALITY_LOW_MAX_FILE_SIZE, maxVideoSize.getMaxFileSize()));
        long durationFromSizeInMillis = getDurationFromSizeInMillis(recordingProfile, maxVideoSize.getMaxFileSize());
        if (videoMaxDurationInMillisecs > 0) {
            durationFromSizeInMillis = Math.min(videoMaxDurationInMillisecs, durationFromSizeInMillis);
        }
        maxVideoSize.setMaxDurationMillis(Math.min(2147483647L, durationFromSizeInMillis));
        return maxVideoSize;
    }
}
