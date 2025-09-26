// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving;

import android.os.Environment;
import java.io.File;
import android.provider.MediaStore$Video$Thumbnails;
import android.provider.MediaStore$Images$Thumbnails;
import android.net.Uri;

public class MediaSavingConstants
{
    public static final String BURST_DIR_NAME;
    public static final Uri EXTENDED_PHOTO_STORAGE_URI;
    public static final Uri EXTENDED_VIDEO_STORAGE_URI;
    public static final String INVALID_FILE_PATH = "/dev/null";
    public static final String MEDIA_TYPE_3GP_EXT = ".3gp";
    public static final String MEDIA_TYPE_3GP_MIME = "video/3gpp";
    public static final String MEDIA_TYPE_JPEG_EXT = ".JPG";
    public static final String MEDIA_TYPE_JPEG_MIME = "image/jpeg";
    public static final String MEDIA_TYPE_MPEG4_EXT = ".mp4";
    public static final String MEDIA_TYPE_MPEG4_MIME = "video/mp4";
    public static final String MEDIA_TYPE_MPO_MIME = "image/mpo";
    public static final Uri PHOTO_THUMBNAIL_URI;
    public static final String SHARED_PREFERENCE_NAME = "storage_preferences";
    public static final int SOMC_FILE_TYPE_PREDICTIVE_CAPTURE_COVER = 100;
    public static final Uri STANDARD_PHOTO_STORAGE_URI;
    public static final int STORAGE_PRIORITY_INTERNAL = 1;
    public static final int STORAGE_PRIORITY_LOWEST = 100;
    public static final int STORAGE_PRIORITY_SD = 0;
    public static final String TAG = "MediaSavingConstants";
    public static final String THREAD_STORE_VIDEO = "Store video thread";
    public static final String TIMESHIFT_DIR_NAME;
    public static final String TIMESHIFT_RELATIVE_ROOT_DIR_NAME;
    public static final String TIMESHIFT_VIDEO_120F_DIR_NAME;
    public static final Uri VIDEO_THUMBNAIL_URI;
    
    static {
        EXTENDED_PHOTO_STORAGE_URI = Uri.parse("content://media/external/extended_images/media");
        EXTENDED_VIDEO_STORAGE_URI = Uri.parse("content://media/external/extended_video/media");
        STANDARD_PHOTO_STORAGE_URI = Uri.parse("content://media/external/images/media");
        PHOTO_THUMBNAIL_URI = MediaStore$Images$Thumbnails.EXTERNAL_CONTENT_URI;
        VIDEO_THUMBNAIL_URI = MediaStore$Video$Thumbnails.EXTERNAL_CONTENT_URI;
        final StringBuilder sb = new StringBuilder();
        sb.append("XPERIA");
        sb.append(File.separator);
        sb.append("BURST");
        BURST_DIR_NAME = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("XPERIA");
        sb2.append(File.separator);
        sb2.append("TIMESHIFT");
        TIMESHIFT_DIR_NAME = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(File.separator);
        sb3.append(Environment.DIRECTORY_DCIM);
        sb3.append(File.separator);
        sb3.append(MediaSavingConstants.TIMESHIFT_DIR_NAME);
        TIMESHIFT_RELATIVE_ROOT_DIR_NAME = sb3.toString();
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("XPERIA");
        sb4.append(File.separator);
        sb4.append("TIMESHIFT_VIDEO");
        sb4.append(File.separator);
        sb4.append("120F");
        TIMESHIFT_VIDEO_120F_DIR_NAME = sb4.toString();
    }
    
    public static class JpegQuality
    {
        public static final int ECONOMY = 85;
        public static final int FINE = 97;
        public static final int STANDARD = 93;
        
        public static int getPlatformQualityFromCameraProfile(int n) {
            switch (n) {
                default: {
                    n = 97;
                    break;
                }
                case 1: {
                    n = 93;
                    break;
                }
                case 0: {
                    n = 85;
                    break;
                }
            }
            return n;
        }
    }
}
