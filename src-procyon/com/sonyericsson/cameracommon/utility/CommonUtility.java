// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.text.Bidi;
import android.graphics.Rect;
import android.graphics.Point;
import android.view.View;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.net.Uri;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.Context;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.IOException;
import java.io.FileOutputStream;

public class CommonUtility
{
    public static final String TAG = "CommonUtility";
    private static final String WRITE_MEDIA_STORAGE = "android.permission.WRITE_MEDIA_STORAGE";
    
    public static void dumpFile(final byte[] b, final String str) {
        FileOutputStream fileOutputStream2;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("/sdcard/");
            sb.append(str);
            final FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
            try {
                fileOutputStream.write(b);
                fileOutputStream2 = fileOutputStream;
            }
            catch (final IOException ex) {
                fileOutputStream2 = fileOutputStream;
            }
        }
        catch (final IOException ex) {
            fileOutputStream2 = null;
        }
        final IOException ex;
        CamLog.e("dumpFile Open / Write Error", ex);
        if (fileOutputStream2 != null) {
            try {
                fileOutputStream2.close();
            }
            catch (final IOException ex2) {
                CamLog.e("dumpFile Close Error", ex2);
            }
        }
    }
    
    private static ApplicationType getApplicationType(final Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                CamLog.w("Can't get packeage manager. assume user app.");
                return ApplicationType.OTHER;
            }
            final PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (packageInfo == null || packageInfo.applicationInfo == null) {
                CamLog.w("Can't get packeage info. assume user app.");
                return ApplicationType.OTHER;
            }
            final int flags = packageInfo.applicationInfo.flags;
            if ((flags & 0x80) != 0x0) {
                return ApplicationType.UPDATED_SYSTEM_APP;
            }
            if ((flags & 0x1) != 0x0) {
                return ApplicationType.SYSTEM;
            }
            return ApplicationType.OTHER;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.w("Can't get packeage info. assume user app.");
            return ApplicationType.OTHER;
        }
    }
    
    public static DefaultGallerySetting getDefaultGallery(final Context context, final Uri uri, final String s) {
        final Intent intent = new Intent("com.android.camera.action.REVIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(uri, s);
        final ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 65536);
        if (resolveActivity == null) {
            return DefaultGallerySetting.OTHER;
        }
        if (resolveActivity.activityInfo.packageName.equals("com.sonyericsson.album")) {
            return DefaultGallerySetting.SONY_ALBUM;
        }
        if (resolveActivity.activityInfo.packageName.equals("com.google.android.apps.photos")) {
            return DefaultGallerySetting.GOOGLE_PHOTOS;
        }
        return DefaultGallerySetting.OTHER;
    }
    
    public static String getFileExtension(final String s) {
        if (s == null) {
            return null;
        }
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex == -1) {
            return null;
        }
        if (lastIndex == 0) {
            return null;
        }
        return s.substring(lastIndex);
    }
    
    public static boolean isActivityAvailable(final Context context, final Intent obj) {
        final ComponentName resolveActivity = obj.resolveActivity(context.getPackageManager());
        boolean b = false;
        if (resolveActivity != null) {
            b = true;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("isActivityAvailable: ");
            sb.append(false);
            sb.append(" : ");
            sb.append(obj);
            CamLog.w(sb.toString());
        }
        return b;
    }
    
    public static boolean isCoreCameraApp(final Context context) {
        return "com.sonyericsson.android.camera".equals(context.getPackageName());
    }
    
    public static boolean isEventContainedInView(final View view, final Point point) {
        final Rect rect = new Rect();
        return view.getGlobalVisibleRect(rect) && rect.contains(point.x, point.y);
    }
    
    public static boolean isMirroringRequired(final Context context) {
        return context != null && new Bidi(context.getResources().getString(2131690264), -2).isRightToLeft();
    }
    
    public static boolean isPackageExist(final String str, final Context context) {
        final boolean b = false;
        if (context == null) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        boolean b2;
        try {
            packageManager.getApplicationInfo(str, 0);
            b2 = true;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            b2 = b;
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("isPackageExist NotExist:");
                sb.append(str);
                CamLog.d(sb.toString());
                b2 = b;
            }
        }
        return b2;
    }
    
    public static boolean isPermissionGranted(final Context context, final String s) {
        return context.getPackageManager().checkPermission(s, context.getPackageName()) == 0;
    }
    
    public static boolean isPreinstalledApp(final Context context) {
        return getApplicationType(context).equals(ApplicationType.SYSTEM);
    }
    
    public static boolean isSystemApp(final Context context) {
        return !getApplicationType(context).equals(ApplicationType.OTHER);
    }
    
    public static void preload() {
    }
    
    public static List<ResolveInfo> removeExcludeItemsFromList(final List<ResolveInfo> c, final List<String> list) {
        final ArrayList list2 = new ArrayList((Collection<? extends E>)c);
        for (final ResolveInfo resolveInfo : c) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("package :");
                sb.append(resolveInfo.activityInfo.packageName);
                CamLog.d(sb.toString());
            }
            final Iterator<String> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                if (iterator2.next().equals(resolveInfo.activityInfo.packageName)) {
                    list2.remove(resolveInfo);
                    break;
                }
            }
        }
        return list2;
    }
    
    public static String removeFileExtension(final String s) {
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex == -1) {
            return s;
        }
        if (lastIndex == 0) {
            return s;
        }
        return s.substring(0, lastIndex);
    }
    
    public static boolean sameStrings(final String s, final String anObject) {
        if (s == null) {
            return anObject == null;
        }
        return s.equals(anObject);
    }
    
    public static boolean shouldStorageForceInternal(final Context context) {
        return ("android.permission.WRITE_MEDIA_STORAGE" != null && !isPermissionGranted(context, "android.permission.WRITE_MEDIA_STORAGE")) || !isSystemApp(context);
    }
    
    public enum ApplicationType
    {
        private static final ApplicationType[] $VALUES;
        
        OTHER, 
        SYSTEM, 
        UPDATED_SYSTEM_APP;
        
        static {
            $VALUES = new ApplicationType[] { ApplicationType.SYSTEM, ApplicationType.UPDATED_SYSTEM_APP, ApplicationType.OTHER };
        }
    }
    
    public enum DefaultGallerySetting
    {
        private static final DefaultGallerySetting[] $VALUES;
        
        GOOGLE_PHOTOS, 
        OTHER, 
        SONY_ALBUM;
        
        static {
            $VALUES = new DefaultGallerySetting[] { DefaultGallerySetting.SONY_ALBUM, DefaultGallerySetting.GOOGLE_PHOTOS, DefaultGallerySetting.OTHER };
        }
    }
}
