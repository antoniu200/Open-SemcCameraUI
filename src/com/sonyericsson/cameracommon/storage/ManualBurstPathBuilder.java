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

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ManualBurstPathBuilder {
    private static final String DCF_DIR_NAME_FREE_WORD_XPERIA_BURST = "XPERIA" + File.separator + "BURST";
    private static final String DCF_FILE_NAME_CONTENT_TYPE_BURST = "DSC";
    public static final String DCF_FILE_NAME_DATE_FORMAT = "yyyyMMddHHmmssSSS";
    private static final String DCF_FILE_NAME_FREE_WORD_BURST = "BURST";
    private static final String TAG = "ManualBurstPathBuilder";

    public static String getPhotoPath(String str, SavingRequest savingRequest) {
        String str2 = DCF_DIR_NAME_FREE_WORD_XPERIA_BURST + File.separator + DcfPathBuilder.DCF_FILE_NAME_FREE_WORD_PICTURE + savingRequest.getSaveTimeForPredictiveCapture();
        String str3 = str + File.separator + str2;
        String str4 = DcfPathBuilder.DCF_FILE_NAME_FREE_WORD_PICTURE + String.format(Locale.US, "%04d", Integer.valueOf(savingRequest.getCaptureIdForPredictiveCapture())) + "_BURST" + savingRequest.getSaveTimeForPredictiveCapture().replaceAll("_", "") + MediaSavingConstants.MEDIA_TYPE_JPEG_EXT;
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
                CamLog.e("getPhotoPath create dir failed: " + str2);
                return null;
            }
        }
        return str3 + File.separator + str4;
    }
}
