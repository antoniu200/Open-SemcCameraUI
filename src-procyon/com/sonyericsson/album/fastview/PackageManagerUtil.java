// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.album.fastview;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.support.annotation.NonNull;
import android.content.Context;

public class PackageManagerUtil
{
    private static final String LOG_TAG = "PackageManagerUtil";
    
    private PackageManagerUtil() {
    }
    
    public static String getApkPath(@NonNull final Context context, final String s) {
        String sourceDir;
        try {
            sourceDir = context.getPackageManager().getApplicationInfo(s, 0).sourceDir;
        }
        catch (final PackageManager$NameNotFoundException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("NameNotFoundException : ");
            sb.append(obj);
            Log.d("PackageManagerUtil", sb.toString());
            sourceDir = null;
        }
        return sourceDir;
    }
    
    public static String getMetadataString(@NonNull final Context context, final String s, final String s2) {
        final String s3 = null;
        String string;
        try {
            final ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(s, 128);
            string = s3;
            if (applicationInfo != null) {
                string = s3;
                if (applicationInfo.metaData != null) {
                    string = applicationInfo.metaData.getString(s2);
                }
            }
        }
        catch (final PackageManager$NameNotFoundException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("NameNotFoundException : ");
            sb.append(obj);
            Log.d("PackageManagerUtil", sb.toString());
            string = s3;
        }
        return string;
    }
    
    public static Context getPackageContext(@NonNull Context packageContext, @NonNull final String s) {
        if (s.equals(packageContext.getPackageName())) {
            return packageContext;
        }
        try {
            packageContext = packageContext.createPackageContext(s, 3);
        }
        catch (final PackageManager$NameNotFoundException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("NameNotFoundException : ");
            sb.append(obj);
            Log.e("PackageManagerUtil", sb.toString());
            packageContext = null;
        }
        return packageContext;
    }
}
