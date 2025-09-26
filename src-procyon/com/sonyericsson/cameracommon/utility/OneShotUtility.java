// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.os.Parcelable;
import android.graphics.Bitmap$Config;
import android.graphics.Matrix;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.app.Activity;

public class OneShotUtility
{
    public static final String KEY_ADD_TO_MEDIA_STORE = "addToMediaStore";
    public static final int REQUEST_ONE_SHOT = 1;
    public static final String TAG = "OneShotUtility";
    
    private static int computeInitialSampleSize(final double n, final double n2, final int n3, final int n4) {
        int n5;
        if (n4 < 0) {
            n5 = 1;
        }
        else {
            n5 = (int)Math.ceil(Math.sqrt(n * n2 / n4));
        }
        int n6;
        if (n3 < 0) {
            n6 = 128;
        }
        else {
            final double n7 = n3;
            n6 = (int)Math.min(Math.floor(n / n7), Math.floor(n2 / n7));
        }
        if (n6 < n5) {
            return n5;
        }
        if (n4 < 0 && n3 < 0) {
            return 1;
        }
        if (n3 < 0) {
            return n5;
        }
        return n6;
    }
    
    public static int computeSampleSize(final double n, final double n2, int n3, int n4) {
        final int computeInitialSampleSize = computeInitialSampleSize(n, n2, n3, n4);
        if (computeInitialSampleSize <= 8) {
            n3 = 1;
            while (true) {
                n4 = n3;
                if (n3 >= computeInitialSampleSize) {
                    break;
                }
                n3 <<= 1;
            }
        }
        else {
            n4 = 8 * ((computeInitialSampleSize + 7) / 8);
        }
        return n4;
    }
    
    public static Intent createResultIntent(final Activity activity, final Uri obj, final String s, final int n, final Bitmap bitmap) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("createResultIntent: uri: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return createResultIntent(obj, s, bitmap);
    }
    
    public static Intent createResultIntent(final Uri uri, final String s, Bitmap copy) {
        final Intent intent = new Intent("inline-data");
        if (copy != null) {
            final float n = 1.0f / computeSampleSize(copy.getWidth(), copy.getHeight(), -1, 51200);
            final Matrix matrix = new Matrix();
            matrix.setScale(n, n);
            final Bitmap bitmap = Bitmap.createBitmap(copy, 0, 0, copy.getWidth(), copy.getHeight(), matrix, true);
            copy = bitmap.copy(Bitmap$Config.ARGB_8888, false);
            bitmap.recycle();
            intent.putExtra("data", (Parcelable)copy);
        }
        intent.setDataAndType(uri, s);
        return intent;
    }
}
