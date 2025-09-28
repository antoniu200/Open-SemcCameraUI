package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import java.io.File;
import java.util.regex.Pattern;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SlowMotionPathBuilder {
    private static final String DCF_FILE_NAME_FREE_WORD_120F = "MOV_HFR_120F_";
    private static final String DCF_FILE_NAME_FREE_WORD_SM = "MOV_SM_P120F_";
    private static final String DCF_FILE_NAME_FREE_WORD_SSM = "MOV_SM_P960F_";
    private static final String DCF_FILE_NAME_FREE_WORD_SSS = "MOV_SM_960F_";
    private static final int RETRY_COUNT = 10;
    private static final String TAG = "SlowMotionPathBuilder";
    private String mPrefix;
    private final String mSuffix;
    private static final String DCF_DIR_NAME_FREE_WORD_XPERIA_SLOW_MOTION = "XPERIA" + File.separator + "SLOW_VIDEO";
    private static final String DCF_FILE_NAME_DATE_FORMAT = "yyyyMMddHHmmss";
    private static final Pattern mSSMDetector = Pattern.compile("/MOV_SM_P960F_\\d{" + DCF_FILE_NAME_DATE_FORMAT.length() + "}" + MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT + "\\z", 2);
    private static final Pattern mSSSDetector = Pattern.compile("/MOV_SM_960F_\\d{" + DCF_FILE_NAME_DATE_FORMAT.length() + "}" + MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT + "\\z", 2);
    private static final Pattern mHFRDetector = Pattern.compile("/MOV_HFR_120F_\\d{" + DCF_FILE_NAME_DATE_FORMAT.length() + "}" + MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT + "\\z", 2);
    private static final Pattern mSMDetector = Pattern.compile("/MOV_SM_P120F_\\d{" + DCF_FILE_NAME_DATE_FORMAT.length() + "}" + MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT + "\\z", 2);

    public SlowMotionPathBuilder(String str) {
        this.mSuffix = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String get(java.lang.String r9, java.lang.String r10, long r11, com.sonyericsson.cameracommon.storage.Storage.StorageType r13) {
        /*
            Method dump skipped, instructions count: 336
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.storage.SlowMotionPathBuilder.get(java.lang.String, java.lang.String, long, com.sonyericsson.cameracommon.storage.Storage$StorageType):java.lang.String");
    }

    private static boolean makeDirectories(String str) {
        File file = new File(str);
        if (file.isDirectory() || file.mkdirs()) {
            return true;
        }
        CamLog.e("Failed mkdirs() : " + file.getPath());
        return false;
    }

    public static boolean isSuperSlowMotionVideo(String str) {
        return mSSMDetector.matcher(str).find();
    }

    public static boolean isSuperSlowShotVideo(String str) {
        return mSSSDetector.matcher(str).find();
    }

    public static boolean isHFRVideo(String str) {
        return mHFRDetector.matcher(str).find();
    }

    public static boolean isStandardSlowMotionVideo(String str) {
        return mSMDetector.matcher(str).find();
    }
}
