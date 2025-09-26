// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.graphics.drawable.Drawable;
import android.content.res.Resources$Theme;
import android.content.res.ColorStateList;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.Context;

public class ResourceUtil
{
    public static final int INVALID_RESOURCE_ID = -1;
    private static final String PACKAGE_NAME = "com.sonymobile.cameracommon";
    public static final String TAG = "ResourceUtil";
    
    public static String getApplicationLabel(final Context context, final String s) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            return (String)packageManager.getApplicationLabel(packageManager.getApplicationInfo(s, 0));
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static boolean getBoolean(final Context context, final int n) {
        return getBoolean(context, "com.sonymobile.cameracommon", n);
    }
    
    public static boolean getBoolean(final Context context, final String s, final int n) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getResourcesForApplication(s).getBoolean(n);
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static ColorStateList getColorStateList(final Context context, final int n) {
        return getColorStateList(context, "com.sonymobile.cameracommon", n);
    }
    
    public static ColorStateList getColorStateList(final Context context, final String s, final int n) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getResourcesForApplication(s).getColorStateList(n, (Resources$Theme)null);
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static float getDimension(final Context context, final int n) {
        return getDimension(context, "com.sonymobile.cameracommon", n);
    }
    
    public static float getDimension(final Context context, final String s, final int n) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getResourcesForApplication(s).getDimension(n);
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static int getDimensionPixelOffset(final Context context, final int n) {
        return getDimensionPixelOffset(context, "com.sonymobile.cameracommon", n);
    }
    
    public static int getDimensionPixelOffset(final Context context, final String s, int dimensionPixelOffset) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            dimensionPixelOffset = packageManager.getResourcesForApplication(s).getDimensionPixelOffset(dimensionPixelOffset);
            return dimensionPixelOffset;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static int getDimensionPixelSize(final Context context, final int n) {
        return getDimensionPixelSize(context, "com.sonymobile.cameracommon", n);
    }
    
    public static int getDimensionPixelSize(final Context context, final String s, int dimensionPixelSize) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            dimensionPixelSize = packageManager.getResourcesForApplication(s).getDimensionPixelSize(dimensionPixelSize);
            return dimensionPixelSize;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static Drawable getDrawable(final Context context, final int n) {
        return getDrawable(context, "com.sonymobile.cameracommon", n);
    }
    
    public static Drawable getDrawable(final Context context, final String s, final int n) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getResourcesForApplication(s).getDrawable(n, (Resources$Theme)null);
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static float getFloat(final Context context, final int n) {
        return Float.valueOf(context.getResources().getString(n));
    }
    
    public static int getInteger(final Context context, final int n) {
        return getInteger(context, "com.sonymobile.cameracommon", n);
    }
    
    public static int getInteger(final Context context, final String s, int integer) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            integer = packageManager.getResourcesForApplication(s).getInteger(integer);
            return integer;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            throw new RuntimeException();
        }
    }
    
    public static int getPixelFromRate(final Context context, final int n, final int n2) {
        return (int)(n2 * getFloat(context, n) / 100.0f);
    }
    
    public static String getString(final Context context, final int n) {
        if (n == -1) {
            return null;
        }
        return context.getResources().getString(n);
    }
    
    public static String getString(final Context context, final String s, final int n) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getResourcesForApplication(s).getString(n);
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("ResourceUtil", (Throwable)ex);
            return null;
        }
    }
}
