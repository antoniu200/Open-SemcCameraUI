// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import com.sonyericsson.cameracommon.activity.RequestPermissionSdCardActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.Intent;
import com.sonyericsson.cameracommon.activity.RequestPermissionActivity;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import android.app.Activity;

public class PermissionsUtil
{
    private static final String[] REQUEST_LOCATION_PERMISSION;
    public static final String TAG = "PermissionsUtil";
    
    static {
        REQUEST_LOCATION_PERMISSION = new String[] { "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION" };
    }
    
    public static boolean areCallerGeoPermissionsGranted(final Activity activity) {
        final String[] request_LOCATION_PERMISSION = PermissionsUtil.REQUEST_LOCATION_PERMISSION;
        for (int length = request_LOCATION_PERMISSION.length, i = 0; i < length; ++i) {
            if (!checkCallerPermission(activity, request_LOCATION_PERMISSION[i])) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean arePermissionsGranted(final Context context, final String[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (context.checkSelfPermission(array[i]) != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkAndRequestSelfPermissions(final Activity activity, final int n, final String[] array) {
        if (array == null || array.length == 0) {
            return false;
        }
        final ArrayList list = new ArrayList();
        getSelfPermissions((Context)activity, array, null, list);
        if (list.size() > 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("start RequestPermissionActivity");
            }
            final Intent intent = new Intent((Context)activity, (Class)RequestPermissionActivity.class);
            intent.putStringArrayListExtra("permissions_list", list);
            activity.startActivityForResult(intent, n);
            return true;
        }
        return false;
    }
    
    private static boolean checkCallerPermission(final Activity activity, final String s) {
        final PackageManager packageManager = activity.getApplicationContext().getPackageManager();
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(activity.getCallingPackage(), 4096);
            if (packageInfo != null && packageInfo.requestedPermissions != null) {
                int i = 0;
                while (i < packageInfo.requestedPermissions.length) {
                    if (s.equals(packageInfo.requestedPermissions[i])) {
                        if ((packageInfo.requestedPermissionsFlags[i] & 0x2) != 0x0) {
                            return true;
                        }
                        break;
                    }
                    else {
                        ++i;
                    }
                }
                return false;
            }
            return false;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    private static void getSelfPermissions(final Context context, final String[] array, final List<String> list, final List<String> list2) {
        for (final String s : array) {
            if (context.checkSelfPermission(s) != 0) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("permission denied:");
                    sb.append(s);
                    CamLog.d(sb.toString());
                }
                if (list2 != null) {
                    list2.add(s);
                }
            }
            else {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("permission allowed:");
                    sb2.append(s);
                    CamLog.d(sb2.toString());
                }
                if (list != null) {
                    list.add(s);
                }
            }
        }
    }
    
    public static void requestSdCardGranted(final Activity activity, final int n, final String s) {
        final Intent intent = new Intent((Context)activity, (Class)RequestPermissionSdCardActivity.class);
        intent.putExtra("extra_key_uuid", s);
        activity.startActivityForResult(intent, n);
    }
}
