// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.net.Uri;
import android.os.Environment;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Locale;
import java.util.regex.Matcher;
import java.io.File;
import java.util.regex.Pattern;

public class PredictiveCapturePathBuilder
{
    private static final int BURST_COVER_FILE_NAME_LENGTH;
    private static final int BURST_FILE_NAME_LENGTH;
    public static final int CAPTURE_ID_STRING_LENGTH = 4;
    public static final String DCF_DIR_NAME_FREE_WORD_XPERIA_BURST;
    public static final String DCF_FILE_NAME_CONTENT_TYPE_PREDICTIVE_CAPTURE = "DSCPDC";
    public static final String DCF_FILE_NAME_DATE_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String DCF_FILE_NAME_FREE_WORD_BURST = "BURST";
    public static final String DCF_FILE_NAME_FREE_WORD_COVER = "COVER";
    private static final int FILE_TIMESTAMP_END_POS;
    private static final int FILE_TIMESTAMP_START_POS;
    public static final String TAG = "PredictiveCapturePathBuilder";
    private static final Pattern mBurstCoverDetector;
    private static final Pattern mBurstDetector;
    private static final Pattern mBurstDirectoryDetector;
    private static final Pattern mBurstLastDetector;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append("XPERIA");
        sb.append(File.separator);
        sb.append("PREDICTIVE_CAPTURE");
        DCF_DIR_NAME_FREE_WORD_XPERIA_BURST = sb.toString();
        FILE_TIMESTAMP_START_POS = "DSCPDC".length() + "_".length() + 4 + "_".length() + "BURST".length();
        FILE_TIMESTAMP_END_POS = PredictiveCapturePathBuilder.FILE_TIMESTAMP_START_POS + "yyyyMMddHHmmssSSS".length();
        BURST_FILE_NAME_LENGTH = "DSCPDC".length() + "_".length() + 4 + "_".length() + "BURST".length() + "yyyyMMddHHmmssSSS".length() + ".JPG".length();
        BURST_COVER_FILE_NAME_LENGTH = "DSCPDC".length() + "_".length() + 4 + "_".length() + "BURST".length() + "_".length() + "COVER".length() + "yyyyMMddHHmmssSSS".length() + ".JPG".length();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("/DSCPDC_\\d{4}_BURST\\d{");
        sb2.append("yyyyMMddHHmmssSSS".length());
        sb2.append("}(|_");
        sb2.append("COVER");
        sb2.append(").(JPE?G|jpe?g)\\z");
        mBurstDetector = Pattern.compile(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("/DSCPDC_\\d{4}_BURST\\d{");
        sb3.append("yyyyMMddHHmmssSSS".length());
        sb3.append("}_");
        sb3.append("COVER");
        sb3.append(".(JPE?G|jpe?g)\\z");
        mBurstCoverDetector = Pattern.compile(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("/DSCPDC_0000_BURST\\d{");
        sb4.append("yyyyMMddHHmmssSSS".length());
        sb4.append("}.(JPE?G|jpe?g)\\z");
        mBurstLastDetector = Pattern.compile(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("(\\ADSC_)(\\d{");
        sb5.append("yyyyMMddHHmmssSSS".length());
        sb5.append("}\\z)");
        mBurstDirectoryDetector = Pattern.compile(sb5.toString());
    }
    
    private static String getFileName(final String s) {
        if (isPredictiveCaptureCoverImage(s)) {
            return s.substring(s.length() - PredictiveCapturePathBuilder.BURST_COVER_FILE_NAME_LENGTH, s.length());
        }
        return s.substring(s.length() - PredictiveCapturePathBuilder.BURST_FILE_NAME_LENGTH, s.length());
    }
    
    private static String getParentDirectoryTimeStamp(String s) {
        final String s2 = "";
        s = new File(s).getParentFile().getName();
        final Matcher matcher = PredictiveCapturePathBuilder.mBurstDirectoryDetector.matcher(s);
        s = s2;
        if (matcher.matches()) {
            s = matcher.group(2);
        }
        return s;
    }
    
    public static String getPhotoPath(String string, final SavingRequest savingRequest) {
        final StringBuilder sb = new StringBuilder();
        sb.append(PredictiveCapturePathBuilder.DCF_DIR_NAME_FREE_WORD_XPERIA_BURST);
        sb.append(File.separator);
        sb.append("DSC_");
        sb.append(savingRequest.getSaveTimeForPredictiveCapture());
        final String string2 = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(File.separator);
        sb2.append(string2);
        final String string3 = sb2.toString();
        string = "";
        if (savingRequest.getSomcType() == 100) {
            string = "_COVER";
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("DSCPDC_");
        sb3.append(String.format(Locale.US, "%04d", savingRequest.getCaptureIdForPredictiveCapture()));
        sb3.append("_");
        sb3.append("BURST");
        sb3.append(savingRequest.getSaveTimeForPredictiveCapture().replaceAll("_", ""));
        sb3.append(string);
        sb3.append(".JPG");
        final String string4 = sb3.toString();
        if (savingRequest.getStorageType() != Storage.StorageType.EXTERNAL_CARD) {
            final File obj = new File(string3);
            if (!obj.exists() && !obj.mkdirs()) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("getPhotoPath create dir failed: ");
                sb4.append(obj);
                CamLog.e(sb4.toString());
                return null;
            }
        }
        else {
            final Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(CameraApplication.getContext());
            if (StorageUtil.isExistDcimDirectory(sdCardGrantedUri)) {
                string = string2;
            }
            else {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(Environment.DIRECTORY_DCIM);
                sb5.append("/");
                sb5.append(string2);
                string = sb5.toString();
            }
            if (StorageUtil.createDirectory(CameraApplication.getContext(), sdCardGrantedUri, string) == null) {
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("getPhotoPath create dir failed for sd: ");
                sb6.append(string);
                CamLog.e(sb6.toString());
                return null;
            }
        }
        final StringBuilder sb7 = new StringBuilder();
        sb7.append(string3);
        sb7.append(File.separator);
        sb7.append(string4);
        return sb7.toString();
    }
    
    public static String getPredictiveCaptureGroupIdPath(final String pathname) {
        return new File(pathname).getParentFile().getPath();
    }
    
    public static String getTimeStamp(final String s) {
        return getFileName(s).substring(PredictiveCapturePathBuilder.FILE_TIMESTAMP_START_POS, PredictiveCapturePathBuilder.FILE_TIMESTAMP_END_POS);
    }
    
    public static boolean isPredictiveCaptureCoverImage(final String input) {
        return PredictiveCapturePathBuilder.mBurstCoverDetector.matcher(input).find();
    }
    
    public static boolean isPredictiveCaptureImage(final String input) {
        return PredictiveCapturePathBuilder.mBurstDetector.matcher(input).find() && getTimeStamp(input).equals(getParentDirectoryTimeStamp(input));
    }
    
    public static boolean isPredictiveCaptureLastImage(final String input) {
        return PredictiveCapturePathBuilder.mBurstLastDetector.matcher(input).find();
    }
}
