// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.net.Uri;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import android.os.Environment;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.File;
import java.util.regex.Pattern;

public class SlowMotionPathBuilder
{
    private static final String DCF_DIR_NAME_FREE_WORD_XPERIA_SLOW_MOTION;
    private static final String DCF_FILE_NAME_DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String DCF_FILE_NAME_FREE_WORD_120F = "MOV_HFR_120F_";
    private static final String DCF_FILE_NAME_FREE_WORD_SM = "MOV_SM_P120F_";
    private static final String DCF_FILE_NAME_FREE_WORD_SSM = "MOV_SM_P960F_";
    private static final String DCF_FILE_NAME_FREE_WORD_SSS = "MOV_SM_960F_";
    private static final int RETRY_COUNT = 10;
    private static final String TAG = "SlowMotionPathBuilder";
    private static final Pattern mHFRDetector;
    private static final Pattern mSMDetector;
    private static final Pattern mSSMDetector;
    private static final Pattern mSSSDetector;
    private String mPrefix;
    private final String mSuffix;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append("XPERIA");
        sb.append(File.separator);
        sb.append("SLOW_VIDEO");
        DCF_DIR_NAME_FREE_WORD_XPERIA_SLOW_MOTION = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("/MOV_SM_P960F_\\d{");
        sb2.append("yyyyMMddHHmmss".length());
        sb2.append("}");
        sb2.append(".mp4");
        sb2.append("\\z");
        mSSMDetector = Pattern.compile(sb2.toString(), 2);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("/MOV_SM_960F_\\d{");
        sb3.append("yyyyMMddHHmmss".length());
        sb3.append("}");
        sb3.append(".mp4");
        sb3.append("\\z");
        mSSSDetector = Pattern.compile(sb3.toString(), 2);
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("/MOV_HFR_120F_\\d{");
        sb4.append("yyyyMMddHHmmss".length());
        sb4.append("}");
        sb4.append(".mp4");
        sb4.append("\\z");
        mHFRDetector = Pattern.compile(sb4.toString(), 2);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("/MOV_SM_P120F_\\d{");
        sb5.append("yyyyMMddHHmmss".length());
        sb5.append("}");
        sb5.append(".mp4");
        sb5.append("\\z");
        mSMDetector = Pattern.compile(sb5.toString(), 2);
    }
    
    public SlowMotionPathBuilder(final String mSuffix) {
        this.mSuffix = mSuffix;
    }
    
    public static boolean isHFRVideo(final String input) {
        return SlowMotionPathBuilder.mHFRDetector.matcher(input).find();
    }
    
    public static boolean isStandardSlowMotionVideo(final String input) {
        return SlowMotionPathBuilder.mSMDetector.matcher(input).find();
    }
    
    public static boolean isSuperSlowMotionVideo(final String input) {
        return SlowMotionPathBuilder.mSSMDetector.matcher(input).find();
    }
    
    public static boolean isSuperSlowShotVideo(final String input) {
        return SlowMotionPathBuilder.mSSSDetector.matcher(input).find();
    }
    
    private static boolean makeDirectories(final String pathname) {
        final File file = new File(pathname);
        if (file.isDirectory()) {
            return true;
        }
        if (file.mkdirs()) {
            return true;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed mkdirs() : ");
        sb.append(file.getPath());
        CamLog.e(sb.toString());
        return false;
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
}
