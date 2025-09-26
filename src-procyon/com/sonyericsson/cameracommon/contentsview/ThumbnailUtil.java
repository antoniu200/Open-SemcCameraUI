// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import android.graphics.Matrix;
import java.io.InputStream;
import java.io.IOException;
import android.graphics.Rect;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.content.Context;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.android.camera.util.CamLog;
import android.widget.RelativeLayout;
import android.app.Activity;

public class ThumbnailUtil
{
    public static final String TAG = "ThumbnailUtil";
    
    public static RelativeLayout createThumbnailViewFromJpeg(final Activity activity, final byte[] array, final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("createEarlyThumbnailViewFromJpeg()");
        }
        if (array == null) {
            CamLog.e("data is null");
            return null;
        }
        final LayoutInflater layoutInflater = activity.getLayoutInflater();
        RelativeLayout relativeLayout;
        if (CommonUtility.isCoreCameraApp((Context)activity)) {
            relativeLayout = (RelativeLayout)layoutInflater.inflate(2131492909, (ViewGroup)null);
        }
        else {
            relativeLayout = (RelativeLayout)layoutInflater.inflate(2131492908, (ViewGroup)null);
        }
        final ImageView imageView = (ImageView)relativeLayout.findViewById(2131296390);
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
        final int outWidth = bitmapFactory$Options.outWidth;
        final int outHeight = bitmapFactory$Options.outHeight;
        if (outWidth > outHeight) {
            bitmapFactory$Options.inSampleSize = Math.round(outHeight / 96.0f);
        }
        else {
            bitmapFactory$Options.inSampleSize = Math.round(outWidth / 96.0f);
        }
        bitmapFactory$Options.inJustDecodeBounds = false;
        bitmapFactory$Options.inPreferredConfig = Bitmap$Config.RGB_565;
        bitmapFactory$Options.inPurgeable = true;
        final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options), 96, 96);
        Bitmap rotateThumbnail;
        if (thumbnail != null) {
            rotateThumbnail = rotateThumbnail(thumbnail, n);
        }
        else {
            rotateThumbnail = null;
        }
        if (rotateThumbnail != null) {
            imageView.setImageBitmap(rotateThumbnail);
        }
        else {
            imageView.setImageDrawable((Drawable)null);
        }
        return relativeLayout;
    }
    
    public static RelativeLayout createThumbnailViewFromUri(final Activity activity, final Uri uri) {
        return createThumbnailViewFromUri(activity, uri, 0);
    }
    
    public static RelativeLayout createThumbnailViewFromUri(final Activity activity, Uri decodeStream, final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("createEarlyThumbnailViewFromUri()");
        }
        if (decodeStream == null) {
            CamLog.e("uri is null");
            return null;
        }
        final LayoutInflater layoutInflater = activity.getLayoutInflater();
        RelativeLayout relativeLayout;
        if (CommonUtility.isCoreCameraApp((Context)activity)) {
            relativeLayout = (RelativeLayout)layoutInflater.inflate(2131492909, (ViewGroup)null);
        }
        else {
            relativeLayout = (RelativeLayout)layoutInflater.inflate(2131492908, (ViewGroup)null);
        }
        final ImageView imageView = (ImageView)relativeLayout.findViewById(2131296390);
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        InputStream openInputStream;
        try {
            openInputStream = activity.getContentResolver().openInputStream(decodeStream);
        }
        catch (final FileNotFoundException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("FileNotFoundException :  = ");
            sb.append(obj);
            CamLog.e(sb.toString());
            openInputStream = null;
        }
        BitmapFactory.decodeStream(openInputStream, (Rect)null, bitmapFactory$Options);
        if (openInputStream != null) {
            try {
                openInputStream.close();
            }
            catch (final IOException obj2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("IOException :  = ");
                sb2.append(obj2);
                CamLog.e(sb2.toString());
            }
        }
        final int outWidth = bitmapFactory$Options.outWidth;
        final int outHeight = bitmapFactory$Options.outHeight;
        if (outWidth > outHeight) {
            bitmapFactory$Options.inSampleSize = Math.round(outHeight / 96.0f);
        }
        else {
            bitmapFactory$Options.inSampleSize = Math.round(outWidth / 96.0f);
        }
        bitmapFactory$Options.inJustDecodeBounds = false;
        bitmapFactory$Options.inPreferredConfig = Bitmap$Config.RGB_565;
        bitmapFactory$Options.inPurgeable = true;
        InputStream openInputStream2;
        try {
            openInputStream2 = activity.getContentResolver().openInputStream(decodeStream);
        }
        catch (final FileNotFoundException obj3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("FileNotFoundException :  = ");
            sb3.append(obj3);
            CamLog.e(sb3.toString());
            openInputStream2 = openInputStream;
        }
        decodeStream = (Uri)BitmapFactory.decodeStream(openInputStream2, (Rect)null, bitmapFactory$Options);
        if (openInputStream2 != null) {
            try {
                openInputStream2.close();
            }
            catch (final IOException obj4) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("IOException :  = ");
                sb4.append(obj4);
                CamLog.e(sb4.toString());
            }
        }
        final Bitmap thumbnail = ThumbnailUtils.extractThumbnail((Bitmap)decodeStream, 96, 96);
        Bitmap rotateThumbnail;
        if (thumbnail != null) {
            rotateThumbnail = rotateThumbnail(thumbnail, n);
        }
        else {
            rotateThumbnail = null;
        }
        if (rotateThumbnail != null) {
            imageView.setImageBitmap(rotateThumbnail);
        }
        else {
            imageView.setImageDrawable((Drawable)null);
        }
        return relativeLayout;
    }
    
    public static Bitmap rotateThumbnail(final Bitmap bitmap, final int n) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        Bitmap bitmap2 = bitmap;
        if (n != 0) {
            try {
                final Matrix matrix = new Matrix();
                matrix.setRotate((float)n, width / 2.0f, height / 2.0f);
                bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                bitmap.recycle();
            }
            catch (final Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Exception : width = ");
                sb.append(width);
                sb.append(", height = ");
                sb.append(height);
                CamLog.e(sb.toString());
                bitmap2 = bitmap;
            }
            catch (final IllegalArgumentException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("IllegalArgumentException : width = ");
                sb2.append(width);
                sb2.append(", height = ");
                sb2.append(height);
                CamLog.e(sb2.toString());
                bitmap2 = bitmap;
            }
        }
        return bitmap2;
    }
}
