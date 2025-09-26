// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.os.UserManager;
import android.content.Context;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.util.CamLog;

public class RecordingUtil
{
    private static final String DISALLOW_RECORD_AUDIO = "no_record_audio";
    public static final String TAG = "RecordingUtil";
    public static final int UPDATE_REMAIN_INTERVAL = 10;
    public static final int VIDEO_PROGRESS_BAR_UPDATE_INTERVAL = 100;
    public static final int VIDEO_REC_TIME_UPDATE_INTERVAL_MILLISEC = 1000;
    
    public static long getDurationMillsFromAverage(final long lng, final long lng2) {
        final long max = Math.max(0L, (long)Math.floor(lng * 60.0 / lng2));
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getMaxDuration: available storage size[kbyte]: ");
            sb.append(lng);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("getMaxDuration: current video size average file size[kbyte/min]: ");
            sb2.append(lng2);
            CamLog.d(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("getMaxDuration: calculated max duration sec: ");
            sb3.append(max);
            CamLog.d(sb3.toString());
        }
        return 1000L * max;
    }
    
    public static long getRecordableSizeKBytes(final Storage storage, final Storage.StorageType storageType) {
        long n;
        if ((n = storage.getRemainStorage(storageType) - 61440L + 15360L) < 0L) {
            n = 0L;
        }
        return n;
    }
    
    public static boolean isAudioPolicyActive(final Context context) {
        if (CamLog.VERBOSE) {
            CamLog.d("isAudioPolicyActive: Android N or later");
        }
        final boolean hasUserRestriction = ((UserManager)context.getSystemService((Class)UserManager.class)).hasUserRestriction("no_record_audio");
        final SomcDevicePolicyManager instance = SomcDevicePolicyManager.getInstance(context);
        return (instance != null && instance.hasUserRestriction(SomcDevicePolicyManager.DISALLOW_RECORD_AUDIO)) | hasUserRestriction;
    }
}
