// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.net.Uri;
import android.os.Environment;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Locale;
import java.io.File;

public class ManualBurstPathBuilder
{
    private static final String DCF_DIR_NAME_FREE_WORD_XPERIA_BURST;
    private static final String DCF_FILE_NAME_CONTENT_TYPE_BURST = "DSC";
    public static final String DCF_FILE_NAME_DATE_FORMAT = "yyyyMMddHHmmssSSS";
    private static final String DCF_FILE_NAME_FREE_WORD_BURST = "BURST";
    private static final String TAG = "ManualBurstPathBuilder";
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append("XPERIA");
        sb.append(File.separator);
        sb.append("BURST");
        DCF_DIR_NAME_FREE_WORD_XPERIA_BURST = sb.toString();
    }
    
    public static String getPhotoPath(String string, final SavingRequest savingRequest) {
        final StringBuilder sb = new StringBuilder();
        sb.append(ManualBurstPathBuilder.DCF_DIR_NAME_FREE_WORD_XPERIA_BURST);
        sb.append(File.separator);
        sb.append("DSC_");
        sb.append(savingRequest.getSaveTimeForPredictiveCapture());
        final String string2 = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(File.separator);
        sb2.append(string2);
        final String string3 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("DSC_");
        sb3.append(String.format(Locale.US, "%04d", savingRequest.getCaptureIdForPredictiveCapture()));
        sb3.append("_");
        sb3.append("BURST");
        sb3.append(savingRequest.getSaveTimeForPredictiveCapture().replaceAll("_", ""));
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
                sb6.append("getPhotoPath create dir failed: ");
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
}
