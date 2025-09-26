package com.sonyericsson.cameracommon.storage;

import android.net.Uri;
import android.os.Environment;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.storage.Storage;
import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PredictiveCapturePathBuilder {
    public static final int CAPTURE_ID_STRING_LENGTH = 4;
    public static final String DCF_FILE_NAME_DATE_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String TAG = "PredictiveCapturePathBuilder";
    private static final Pattern mBurstDirectoryDetector;
    private static final Pattern mBurstLastDetector;
    public static final String DCF_DIR_NAME_FREE_WORD_XPERIA_BURST = "XPERIA" + File.separator + "PREDICTIVE_CAPTURE";
    public static final String DCF_FILE_NAME_CONTENT_TYPE_PREDICTIVE_CAPTURE = "DSCPDC";
    public static final String DCF_FILE_NAME_FREE_WORD_BURST = "BURST";
    private static final int FILE_TIMESTAMP_START_POS = (((DCF_FILE_NAME_CONTENT_TYPE_PREDICTIVE_CAPTURE.length() + "_".length()) + 4) + "_".length()) + DCF_FILE_NAME_FREE_WORD_BURST.length();
    private static final int FILE_TIMESTAMP_END_POS = FILE_TIMESTAMP_START_POS + "yyyyMMddHHmmssSSS".length();
    private static final int BURST_FILE_NAME_LENGTH = (((((DCF_FILE_NAME_CONTENT_TYPE_PREDICTIVE_CAPTURE.length() + "_".length()) + 4) + "_".length()) + DCF_FILE_NAME_FREE_WORD_BURST.length()) + "yyyyMMddHHmmssSSS".length()) + MediaSavingConstants.MEDIA_TYPE_JPEG_EXT.length();
    public static final String DCF_FILE_NAME_FREE_WORD_COVER = "COVER";
    private static final int BURST_COVER_FILE_NAME_LENGTH = (((((((DCF_FILE_NAME_CONTENT_TYPE_PREDICTIVE_CAPTURE.length() + "_".length()) + 4) + "_".length()) + DCF_FILE_NAME_FREE_WORD_BURST.length()) + "_".length()) + DCF_FILE_NAME_FREE_WORD_COVER.length()) + "yyyyMMddHHmmssSSS".length()) + MediaSavingConstants.MEDIA_TYPE_JPEG_EXT.length();
    private static final Pattern mBurstDetector = Pattern.compile("/DSCPDC_\\d{4}_BURST\\d{" + "yyyyMMddHHmmssSSS".length() + "}(|_" + DCF_FILE_NAME_FREE_WORD_COVER + ").(JPE?G|jpe?g)\\z");
    private static final Pattern mBurstCoverDetector = Pattern.compile("/DSCPDC_\\d{4}_BURST\\d{" + "yyyyMMddHHmmssSSS".length() + "}_" + DCF_FILE_NAME_FREE_WORD_COVER + ".(JPE?G|jpe?g)\\z");

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("/DSCPDC_0000_BURST\\d{");
        sb.append("yyyyMMddHHmmssSSS".length());
        sb.append("}.(JPE?G|jpe?g)\\z");
        mBurstLastDetector = Pattern.compile(sb.toString());
        mBurstDirectoryDetector = Pattern.compile("(\\ADSC_)(\\d{" + "yyyyMMddHHmmssSSS".length() + "}\\z)");
    }

    public static String getPhotoPath(String str, SavingRequest savingRequest) {
        String str2 = DCF_DIR_NAME_FREE_WORD_XPERIA_BURST + File.separator + DcfPathBuilder.DCF_FILE_NAME_FREE_WORD_PICTURE + savingRequest.getSaveTimeForPredictiveCapture();
        String str3 = str + File.separator + str2;
        String str4 = "DSCPDC_" + String.format(Locale.US, "%04d", Integer.valueOf(savingRequest.getCaptureIdForPredictiveCapture())) + "_" + DCF_FILE_NAME_FREE_WORD_BURST + savingRequest.getSaveTimeForPredictiveCapture().replaceAll("_", "") + (savingRequest.getSomcType() == 100 ? "_COVER" : "") + MediaSavingConstants.MEDIA_TYPE_JPEG_EXT;
        if (savingRequest.getStorageType() != Storage.StorageType.EXTERNAL_CARD) {
            File file = new File(str3);
            if (!file.exists() && !file.mkdirs()) {
                CamLog.e("getPhotoPath create dir failed: " + file);
                return null;
            }
        } else {
            Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(CameraApplication.getContext());
            if (!StorageUtil.isExistDcimDirectory(sdCardGrantedUri)) {
                str2 = Environment.DIRECTORY_DCIM + SharedPrefsTranslator.CONNECTOR_SLASH + str2;
            }
            if (StorageUtil.createDirectory(CameraApplication.getContext(), sdCardGrantedUri, str2) == null) {
                CamLog.e("getPhotoPath create dir failed for sd: " + str2);
                return null;
            }
        }
        return str3 + File.separator + str4;
    }

    public static boolean isPredictiveCaptureImage(String str) {
        return mBurstDetector.matcher(str).find() && getTimeStamp(str).equals(getParentDirectoryTimeStamp(str));
    }

    public static String getPredictiveCaptureGroupIdPath(String str) {
        return new File(str).getParentFile().getPath();
    }

    private static String getParentDirectoryTimeStamp(String str) {
        Matcher matcher = mBurstDirectoryDetector.matcher(new File(str).getParentFile().getName());
        return matcher.matches() ? matcher.group(2) : "";
    }

    public static boolean isPredictiveCaptureCoverImage(String str) {
        return mBurstCoverDetector.matcher(str).find();
    }

    public static boolean isPredictiveCaptureLastImage(String str) {
        return mBurstLastDetector.matcher(str).find();
    }

    public static String getTimeStamp(String str) {
        return getFileName(str).substring(FILE_TIMESTAMP_START_POS, FILE_TIMESTAMP_END_POS);
    }

    private static String getFileName(String str) {
        if (isPredictiveCaptureCoverImage(str)) {
            return str.substring(str.length() - BURST_COVER_FILE_NAME_LENGTH, str.length());
        }
        return str.substring(str.length() - BURST_FILE_NAME_LENGTH, str.length());
    }
}
