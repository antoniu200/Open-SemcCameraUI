package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.view.View;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CommonUtility {
    public static final String TAG = "CommonUtility";
    private static final String WRITE_MEDIA_STORAGE = "android.permission.WRITE_MEDIA_STORAGE";

    public enum ApplicationType {
        SYSTEM,
        UPDATED_SYSTEM_APP,
        OTHER
    }

    public enum DefaultGallerySetting {
        SONY_ALBUM,
        GOOGLE_PHOTOS,
        OTHER
    }

    public static void preload() {
    }

    public static List<ResolveInfo> removeExcludeItemsFromList(List<ResolveInfo> list, List<String> list2) {
        ArrayList arrayList = new ArrayList(list);
        for (ResolveInfo resolveInfo : list) {
            if (CamLog.VERBOSE) {
                CamLog.d("package :" + resolveInfo.activityInfo.packageName);
            }
            Iterator<String> it = list2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (it.next().equals(resolveInfo.activityInfo.packageName)) {
                    arrayList.remove(resolveInfo);
                    break;
                }
            }
        }
        return arrayList;
    }

    public static boolean isPackageExist(String str, Context context) throws PackageManager.NameNotFoundException {
        if (context == null) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            if (!CamLog.VERBOSE) {
                return false;
            }
            CamLog.d("isPackageExist NotExist:" + str);
            return false;
        }
    }

    public static boolean isMirroringRequired(Context context) {
        if (context == null) {
            return false;
        }
        return new Bidi(context.getResources().getString(R.string.capturing_mode_selector_bidicheck_string), -2).isRightToLeft();
    }

    public static boolean isActivityAvailable(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return true;
        }
        CamLog.w("isActivityAvailable: false : " + intent);
        return false;
    }

    public static DefaultGallerySetting getDefaultGallery(Context context, Uri uri, String str) {
        Intent intent = new Intent("com.android.camera.action.REVIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(uri, str);
        ResolveInfo resolveInfoResolveActivity = context.getPackageManager().resolveActivity(intent, 65536);
        if (resolveInfoResolveActivity == null) {
            return DefaultGallerySetting.OTHER;
        }
        if (resolveInfoResolveActivity.activityInfo.packageName.equals("com.sonyericsson.album")) {
            return DefaultGallerySetting.SONY_ALBUM;
        }
        if (resolveInfoResolveActivity.activityInfo.packageName.equals("com.google.android.apps.photos")) {
            return DefaultGallerySetting.GOOGLE_PHOTOS;
        }
        return DefaultGallerySetting.OTHER;
    }

    public static boolean isPreinstalledApp(Context context) {
        return getApplicationType(context).equals(ApplicationType.SYSTEM);
    }

    public static boolean isSystemApp(Context context) {
        return !getApplicationType(context).equals(ApplicationType.OTHER);
    }

    public static boolean isCoreCameraApp(Context context) {
        return "com.sonyericsson.android.camera".equals(context.getPackageName());
    }

    public static boolean isPermissionGranted(Context context, String str) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }

    public static boolean shouldStorageForceInternal(Context context) {
        return ((WRITE_MEDIA_STORAGE == 0 || isPermissionGranted(context, WRITE_MEDIA_STORAGE)) && isSystemApp(context)) ? false : true;
    }

    private static ApplicationType getApplicationType(Context context) throws PackageManager.NameNotFoundException {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null && packageInfo.applicationInfo != null) {
                    int i = packageInfo.applicationInfo.flags;
                    if ((i & 128) != 0) {
                        return ApplicationType.UPDATED_SYSTEM_APP;
                    }
                    if ((i & 1) != 0) {
                        return ApplicationType.SYSTEM;
                    }
                    return ApplicationType.OTHER;
                }
                CamLog.w("Can't get packeage info. assume user app.");
                return ApplicationType.OTHER;
            }
            CamLog.w("Can't get packeage manager. assume user app.");
            return ApplicationType.OTHER;
        } catch (PackageManager.NameNotFoundException unused) {
            CamLog.w("Can't get packeage info. assume user app.");
            return ApplicationType.OTHER;
        }
    }

    public static boolean isEventContainedInView(View view, Point point) {
        Rect rect = new Rect();
        if (view.getGlobalVisibleRect(rect)) {
            return rect.contains(point.x, point.y);
        }
        return false;
    }

    public static String removeFileExtension(String str) {
        int iLastIndexOf = str.lastIndexOf(46);
        return (iLastIndexOf == -1 || iLastIndexOf == 0) ? str : str.substring(0, iLastIndexOf);
    }

    public static String getFileExtension(String str) {
        int iLastIndexOf;
        if (str == null || (iLastIndexOf = str.lastIndexOf(46)) == -1 || iLastIndexOf == 0) {
            return null;
        }
        return str.substring(iLastIndexOf);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0026 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void dumpFile(byte[] r4, java.lang.String r5) throws java.io.IOException {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch: java.io.IOException -> L1d
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L1d
            r2.<init>()     // Catch: java.io.IOException -> L1d
            java.lang.String r3 = "/sdcard/"
            r2.append(r3)     // Catch: java.io.IOException -> L1d
            r2.append(r5)     // Catch: java.io.IOException -> L1d
            java.lang.String r5 = r2.toString()     // Catch: java.io.IOException -> L1d
            r1.<init>(r5)     // Catch: java.io.IOException -> L1d
            r1.write(r4)     // Catch: java.io.IOException -> L1b
            goto L24
        L1b:
            r4 = move-exception
            goto L1f
        L1d:
            r4 = move-exception
            r1 = r0
        L1f:
            java.lang.String r5 = "dumpFile Open / Write Error"
            com.sonyericsson.android.camera.util.CamLog.e(r5, r4)
        L24:
            if (r1 == 0) goto L30
            r1.close()     // Catch: java.io.IOException -> L2a
            goto L30
        L2a:
            r4 = move-exception
            java.lang.String r5 = "dumpFile Close Error"
            com.sonyericsson.android.camera.util.CamLog.e(r5, r4)
        L30:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.utility.CommonUtility.dumpFile(byte[], java.lang.String):void");
    }

    public static boolean sameStrings(String str, String str2) {
        if (str == null) {
            return str2 == null;
        }
        return str.equals(str2);
    }
}
