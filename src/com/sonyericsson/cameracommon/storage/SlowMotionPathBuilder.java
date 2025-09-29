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

    public String get(final String s, String s2, final long n, final Storage.StorageType storageType) {
        final int hashCode = s2.hashCode();
        int n2 = 0;
        Label_0078: {
            if (hashCode != -351666540) {
                if (hashCode != 1185702096) {
                    if (hashCode == 1616114994) {
                        if (s2.equals("STANDARD_SLOW_MOTION")) {
                            n2 = 1;
                            break Label_0078;
                        }
                    }
                }
                else if (s2.equals("SUPER_SLOW_MOTION")) {
                    n2 = 0;
                    break Label_0078;
                }
            }
            else if (s2.equals("SUPER_SLOW_SHOT")) {
                n2 = 2;
                break Label_0078;
            }
            n2 = -1;
        }
        switch (n2) {
            case 2: {
                this.mPrefix = "MOV_SM_960F_";
                break;
            }
            case 1: {
                this.mPrefix = "MOV_HFR_120F_";
                break;
            }
            case 0: {
                this.mPrefix = "MOV_SM_P960F_";
                break;
            }
        }
        if (storageType != Storage.StorageType.EXTERNAL_CARD) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(File.separator);
            sb.append(SlowMotionPathBuilder.DCF_DIR_NAME_FREE_WORD_XPERIA_SLOW_MOTION);
            if (!makeDirectories(sb.toString())) {
                CamLog.e("Failed to make directory for slow motion video content.");
                return null;
            }
        }
        else {
            final Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(CameraApplication.getContext());
            if (StorageUtil.isExistDcimDirectory(sdCardGrantedUri)) {
                s2 = SlowMotionPathBuilder.DCF_DIR_NAME_FREE_WORD_XPERIA_SLOW_MOTION;
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(Environment.DIRECTORY_DCIM);
                sb2.append("/");
                sb2.append(SlowMotionPathBuilder.DCF_DIR_NAME_FREE_WORD_XPERIA_SLOW_MOTION);
                s2 = sb2.toString();
            }
            if (StorageUtil.createDirectory(CameraApplication.getContext(), sdCardGrantedUri, s2) == null) {
                CamLog.e("Failed to make directory on SD card for slow motion video content.");
                return null;
            }
        }
        final Calendar instance = Calendar.getInstance();
        for (int i = 0; i < 10; ++i) {
            instance.setTimeInMillis(i * 1000L + n);
            final String format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(instance.getTime());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(File.separator);
            sb3.append(SlowMotionPathBuilder.DCF_DIR_NAME_FREE_WORD_XPERIA_SLOW_MOTION);
            sb3.append(File.separator);
            sb3.append(this.mPrefix);
            sb3.append(format);
            sb3.append(this.mSuffix);
            final String string = sb3.toString();
            if (!new File(string).exists()) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Generate path:");
                    sb4.append(string);
                    CamLog.d(sb4.toString());
                }
                return string;
            }
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Generated path already exists. Try to generate next path. tryCount:");
            sb5.append(i);
            CamLog.w(sb5.toString());
        }
        CamLog.e("Failed to generate path. retry:10");
        return null;
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
