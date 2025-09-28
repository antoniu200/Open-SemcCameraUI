package com.sonyericsson.cameracommon.contentsview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ThumbnailUtil {
    public static final String TAG = "ThumbnailUtil";

    public static RelativeLayout createThumbnailViewFromJpeg(Activity activity, byte[] bArr, int i) {
        RelativeLayout relativeLayout;
        if (CamLog.VERBOSE) {
            CamLog.d("createEarlyThumbnailViewFromJpeg()");
        }
        if (bArr == null) {
            CamLog.e("data is null");
            return null;
        }
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (CommonUtility.isCoreCameraApp(activity)) {
            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.content_early_thumbnail_core_camera, (ViewGroup) null);
        } else {
            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.content_early_thumbnail, (ViewGroup) null);
        }
        ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.early_thumbnail_image);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        int i2 = options.outWidth;
        int i3 = options.outHeight;
        if (i2 > i3) {
            options.inSampleSize = Math.round(i3 / 96.0f);
        } else {
            options.inSampleSize = Math.round(i2 / 96.0f);
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        Bitmap bitmapExtractThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options), 96, 96);
        Bitmap bitmapRotateThumbnail = bitmapExtractThumbnail != null ? rotateThumbnail(bitmapExtractThumbnail, i) : null;
        if (bitmapRotateThumbnail != null) {
            imageView.setImageBitmap(bitmapRotateThumbnail);
        } else {
            imageView.setImageDrawable(null);
        }
        return relativeLayout;
    }

    public static Bitmap rotateThumbnail(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (i == 0) {
            return bitmap;
        }
        try {
            Matrix matrix = new Matrix();
            matrix.setRotate(i, width / 2.0f, height / 2.0f);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            bitmap.recycle();
            return bitmapCreateBitmap;
        } catch (IllegalArgumentException unused) {
            CamLog.e("IllegalArgumentException : width = " + width + ", height = " + height);
            return bitmap;
        } catch (Exception unused2) {
            CamLog.e("Exception : width = " + width + ", height = " + height);
            return bitmap;
        }
    }

    public static RelativeLayout createThumbnailViewFromUri(Activity activity, Uri uri) {
        return createThumbnailViewFromUri(activity, uri, 0);
    }

    public static RelativeLayout createThumbnailViewFromUri(Activity activity, Uri uri, int i) throws IOException {
        RelativeLayout relativeLayout;
        InputStream inputStreamOpenInputStream;
        InputStream inputStreamOpenInputStream2;
        if (CamLog.VERBOSE) {
            CamLog.d("createEarlyThumbnailViewFromUri()");
        }
        if (uri == null) {
            CamLog.e("uri is null");
            return null;
        }
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        if (CommonUtility.isCoreCameraApp(activity)) {
            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.content_early_thumbnail_core_camera, (ViewGroup) null);
        } else {
            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.content_early_thumbnail, (ViewGroup) null);
        }
        ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.early_thumbnail_image);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            inputStreamOpenInputStream = activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            CamLog.e("FileNotFoundException :  = " + e);
            inputStreamOpenInputStream = null;
        }
        BitmapFactory.decodeStream(inputStreamOpenInputStream, null, options);
        if (inputStreamOpenInputStream != null) {
            try {
                inputStreamOpenInputStream.close();
            } catch (IOException e2) {
                CamLog.e("IOException :  = " + e2);
            }
        }
        int i2 = options.outWidth;
        int i3 = options.outHeight;
        if (i2 > i3) {
            options.inSampleSize = Math.round(i3 / 96.0f);
        } else {
            options.inSampleSize = Math.round(i2 / 96.0f);
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        try {
            inputStreamOpenInputStream2 = activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e3) {
            CamLog.e("FileNotFoundException :  = " + e3);
            inputStreamOpenInputStream2 = inputStreamOpenInputStream;
        }
        Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStreamOpenInputStream2, null, options);
        if (inputStreamOpenInputStream2 != null) {
            try {
                inputStreamOpenInputStream2.close();
            } catch (IOException e4) {
                CamLog.e("IOException :  = " + e4);
            }
        }
        Bitmap bitmapExtractThumbnail = ThumbnailUtils.extractThumbnail(bitmapDecodeStream, 96, 96);
        Bitmap bitmapRotateThumbnail = bitmapExtractThumbnail != null ? rotateThumbnail(bitmapExtractThumbnail, i) : null;
        if (bitmapRotateThumbnail != null) {
            imageView.setImageBitmap(bitmapRotateThumbnail);
        } else {
            imageView.setImageDrawable(null);
        }
        return relativeLayout;
    }
}
