package com.sonyericsson.album.fastview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PackageManagerUtil {
    private static final String LOG_TAG = "PackageManagerUtil";

    private PackageManagerUtil() {
    }

    public static String getMetadataString(@NonNull Context context, String str, String str2) throws PackageManager.NameNotFoundException {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
            if (applicationInfo == null || applicationInfo.metaData == null) {
                return null;
            }
            return applicationInfo.metaData.getString(str2);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(LOG_TAG, "NameNotFoundException : " + e);
            return null;
        }
    }

    public static String getApkPath(@NonNull Context context, String str) {
        try {
            return context.getPackageManager().getApplicationInfo(str, 0).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(LOG_TAG, "NameNotFoundException : " + e);
            return null;
        }
    }

    public static Context getPackageContext(@NonNull Context context, @NonNull String str) {
        if (str.equals(context.getPackageName())) {
            return context;
        }
        try {
            return context.createPackageContext(str, 3);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "NameNotFoundException : " + e);
            return null;
        }
    }
}
