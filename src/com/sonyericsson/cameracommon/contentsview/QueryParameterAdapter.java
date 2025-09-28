package com.sonyericsson.cameracommon.contentsview;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class QueryParameterAdapter {
    public static final Uri MPO_3DPICTURES_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static final String MPO_3DPICTURES_DATA = "";
    public static final String MPO_3DPICTURES_DATE_TAKEN = "";
    public static final String MPO_3DPICTURES_ID = "";
    public static final String MPO_3DPICTURES_MIME_TYPE = "";
    public static final String MPO_3DPICTURES_MINI_THUMB_MAGIC = "";
    public static final String MPO_3DPICTURES_TYPE = "";
    public static final int MPO_THUMB_MICRO_KIND = 0;

    public static Bitmap getThumbnail(ContentResolver contentResolver, long j, int i, BitmapFactory.Options options) {
        return null;
    }
}
