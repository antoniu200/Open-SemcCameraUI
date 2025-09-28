package com.sonyericsson.cameracommon.utility;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OneShotUtility {
    public static final String KEY_ADD_TO_MEDIA_STORE = "addToMediaStore";
    public static final int REQUEST_ONE_SHOT = 1;
    public static final String TAG = "OneShotUtility";

    public static Intent createResultIntent(Activity activity, Uri uri, String str, int i, Bitmap bitmap) {
        if (CamLog.VERBOSE) {
            CamLog.d("createResultIntent: uri: " + uri);
        }
        return createResultIntent(uri, str, bitmap);
    }

    public static Intent createResultIntent(Uri uri, String str, Bitmap bitmap) {
        Intent intent = new Intent("inline-data");
        if (bitmap != null) {
            float fComputeSampleSize = 1.0f / computeSampleSize(bitmap.getWidth(), bitmap.getHeight(), -1, 51200);
            Matrix matrix = new Matrix();
            matrix.setScale(fComputeSampleSize, fComputeSampleSize);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            Bitmap bitmapCopy = bitmapCreateBitmap.copy(Bitmap.Config.ARGB_8888, false);
            bitmapCreateBitmap.recycle();
            intent.putExtra("data", bitmapCopy);
        }
        intent.setDataAndType(uri, str);
        return intent;
    }

    public static int computeSampleSize(double d, double d2, int i, int i2) {
        int iComputeInitialSampleSize = computeInitialSampleSize(d, d2, i, i2);
        if (iComputeInitialSampleSize > 8) {
            return 8 * ((iComputeInitialSampleSize + 7) / 8);
        }
        int i3 = 1;
        while (i3 < iComputeInitialSampleSize) {
            i3 <<= 1;
        }
        return i3;
    }

    private static int computeInitialSampleSize(double d, double d2, int i, int i2) {
        int iMin;
        int iCeil = i2 < 0 ? 1 : (int) Math.ceil(Math.sqrt((d * d2) / i2));
        if (i < 0) {
            iMin = 128;
        } else {
            double d3 = i;
            iMin = (int) Math.min(Math.floor(d / d3), Math.floor(d2 / d3));
        }
        if (iMin < iCeil) {
            return iCeil;
        }
        if (i2 >= 0 || i >= 0) {
            return i < 0 ? iCeil : iMin;
        }
        return 1;
    }
}
