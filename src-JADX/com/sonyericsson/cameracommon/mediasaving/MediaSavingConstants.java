package com.sonyericsson.cameracommon.mediasaving;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.sonyericsson.cameracommon.storage.PredictiveCapturePathBuilder;
import java.io.File;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MediaSavingConstants {
    public static final String INVALID_FILE_PATH = "/dev/null";
    public static final String MEDIA_TYPE_3GP_EXT = ".3gp";
    public static final String MEDIA_TYPE_3GP_MIME = "video/3gpp";
    public static final String MEDIA_TYPE_JPEG_EXT = ".JPG";
    public static final String MEDIA_TYPE_JPEG_MIME = "image/jpeg";
    public static final String MEDIA_TYPE_MPEG4_EXT = ".mp4";
    public static final String MEDIA_TYPE_MPEG4_MIME = "video/mp4";
    public static final String MEDIA_TYPE_MPO_MIME = "image/mpo";
    public static final String SHARED_PREFERENCE_NAME = "storage_preferences";
    public static final int SOMC_FILE_TYPE_PREDICTIVE_CAPTURE_COVER = 100;
    public static final int STORAGE_PRIORITY_INTERNAL = 1;
    public static final int STORAGE_PRIORITY_LOWEST = 100;
    public static final int STORAGE_PRIORITY_SD = 0;
    public static final String TAG = "MediaSavingConstants";
    public static final String THREAD_STORE_VIDEO = "Store video thread";
    public static final Uri EXTENDED_PHOTO_STORAGE_URI = Uri.parse("content://media/external/extended_images/media");
    public static final Uri EXTENDED_VIDEO_STORAGE_URI = Uri.parse("content://media/external/extended_video/media");
    public static final Uri STANDARD_PHOTO_STORAGE_URI = Uri.parse("content://media/external/images/media");
    public static final Uri PHOTO_THUMBNAIL_URI = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    public static final Uri VIDEO_THUMBNAIL_URI = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
    public static final String BURST_DIR_NAME = "XPERIA" + File.separator + PredictiveCapturePathBuilder.DCF_FILE_NAME_FREE_WORD_BURST;
    public static final String TIMESHIFT_DIR_NAME = "XPERIA" + File.separator + "TIMESHIFT";
    public static final String TIMESHIFT_RELATIVE_ROOT_DIR_NAME = File.separator + Environment.DIRECTORY_DCIM + File.separator + TIMESHIFT_DIR_NAME;
    public static final String TIMESHIFT_VIDEO_120F_DIR_NAME = "XPERIA" + File.separator + "TIMESHIFT_VIDEO" + File.separator + "120F";

    public static class JpegQuality {
        public static final int ECONOMY = 85;
        public static final int FINE = 97;
        public static final int STANDARD = 93;

        public static int getPlatformQualityFromCameraProfile(int i) {
            switch (i) {
                case 0:
                    return 85;
                case 1:
                    return 93;
                default:
                    return 97;
            }
        }
    }
}
