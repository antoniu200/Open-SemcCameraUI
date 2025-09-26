package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.os.UserManager;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RecordingUtil {
    private static final String DISALLOW_RECORD_AUDIO = "no_record_audio";
    public static final String TAG = "RecordingUtil";
    public static final int UPDATE_REMAIN_INTERVAL = 10;
    public static final int VIDEO_PROGRESS_BAR_UPDATE_INTERVAL = 100;
    public static final int VIDEO_REC_TIME_UPDATE_INTERVAL_MILLISEC = 1000;

    public static long getDurationMillsFromAverage(long j, long j2) {
        long jMax = Math.max(0L, (long) Math.floor((j * 60.0d) / j2));
        long j3 = 1000 * jMax;
        if (CamLog.VERBOSE) {
            CamLog.d("getMaxDuration: available storage size[kbyte]: " + j);
            CamLog.d("getMaxDuration: current video size average file size[kbyte/min]: " + j2);
            CamLog.d("getMaxDuration: calculated max duration sec: " + jMax);
        }
        return j3;
    }

    public static long getRecordableSizeKBytes(Storage storage, Storage.StorageType storageType) {
        long remainStorage = (storage.getRemainStorage(storageType) - CommonConstants.STORAGE_REMAIN_MIN) + CommonConstants.EXTRA_RECORDABLE_FILESIZE;
        if (remainStorage < 0) {
            return 0L;
        }
        return remainStorage;
    }

    public static boolean isAudioPolicyActive(Context context) {
        if (CamLog.VERBOSE) {
            CamLog.d("isAudioPolicyActive: Android N or later");
        }
        boolean zHasUserRestriction = ((UserManager) context.getSystemService(UserManager.class)).hasUserRestriction(DISALLOW_RECORD_AUDIO);
        SomcDevicePolicyManager somcDevicePolicyManager = SomcDevicePolicyManager.getInstance(context);
        return (somcDevicePolicyManager == null ? false : somcDevicePolicyManager.hasUserRestriction(SomcDevicePolicyManager.DISALLOW_RECORD_AUDIO)) | zHasUserRestriction;
    }
}
