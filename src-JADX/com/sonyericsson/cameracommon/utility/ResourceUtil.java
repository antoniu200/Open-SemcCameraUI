package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResourceUtil {
    public static final int INVALID_RESOURCE_ID = -1;
    private static final String PACKAGE_NAME = "com.sonymobile.cameracommon";
    public static final String TAG = "ResourceUtil";

    public static String getString(Context context, int i) {
        if (i == -1) {
            return null;
        }
        return context.getResources().getString(i);
    }

    public static String getString(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getString(i);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            return null;
        }
    }

    public static int getInteger(Context context, int i) {
        return getInteger(context, "com.sonymobile.cameracommon", i);
    }

    public static int getInteger(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getInteger(i);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static float getFloat(Context context, int i) {
        return Float.valueOf(context.getResources().getString(i)).floatValue();
    }

    public static boolean getBoolean(Context context, int i) {
        return getBoolean(context, "com.sonymobile.cameracommon", i);
    }

    public static boolean getBoolean(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getBoolean(i);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static Drawable getDrawable(Context context, int i) {
        return getDrawable(context, "com.sonymobile.cameracommon", i);
    }

    public static Drawable getDrawable(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getDrawable(i, null);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static int getDimensionPixelSize(Context context, int i) {
        return getDimensionPixelSize(context, "com.sonymobile.cameracommon", i);
    }

    public static int getDimensionPixelSize(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getDimensionPixelSize(i);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static int getDimensionPixelOffset(Context context, int i) {
        return getDimensionPixelOffset(context, "com.sonymobile.cameracommon", i);
    }

    public static int getDimensionPixelOffset(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getDimensionPixelOffset(i);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static int getPixelFromRate(Context context, int i, int i2) {
        return (int) ((i2 * getFloat(context, i)) / 100.0f);
    }

    public static float getDimension(Context context, int i) {
        return getDimension(context, "com.sonymobile.cameracommon", i);
    }

    public static float getDimension(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getDimension(i);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static ColorStateList getColorStateList(Context context, int i) {
        return getColorStateList(context, "com.sonymobile.cameracommon", i);
    }

    public static ColorStateList getColorStateList(Context context, String str, int i) {
        try {
            return context.getPackageManager().getResourcesForApplication(str).getColorStateList(i, null);
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static String getApplicationLabel(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 0));
        } catch (PackageManager.NameNotFoundException e) {
            CamLog.e(TAG, e);
            throw new RuntimeException();
        }
    }
}
