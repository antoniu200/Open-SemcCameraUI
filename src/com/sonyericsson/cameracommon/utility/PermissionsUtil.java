package com.sonyericsson.cameracommon.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.activity.RequestPermissionActivity;
import com.sonyericsson.cameracommon.activity.RequestPermissionSdCardActivity;
import com.sonyericsson.cameracommon.intent.IntentConstants;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PermissionsUtil {
    private static final String[] REQUEST_LOCATION_PERMISSION = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    public static final String TAG = "PermissionsUtil";

    public static boolean checkAndRequestSelfPermissions(Activity activity, int i, String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return false;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        getSelfPermissions(activity, strArr, null, arrayList);
        if (arrayList.size() <= 0) {
            return false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("start RequestPermissionActivity");
        }
        Intent intent = new Intent(activity, (Class<?>) RequestPermissionActivity.class);
        intent.putStringArrayListExtra(IntentConstants.EXTRA_PERMISSIONS_LIST, arrayList);
        activity.startActivityForResult(intent, i);
        return true;
    }

    private static void getSelfPermissions(Context context, String[] strArr, List<String> list, List<String> list2) {
        for (String str : strArr) {
            if (context.checkSelfPermission(str) != 0) {
                if (CamLog.VERBOSE) {
                    CamLog.d("permission denied:" + str);
                }
                if (list2 != null) {
                    list2.add(str);
                }
            } else {
                if (CamLog.VERBOSE) {
                    CamLog.d("permission allowed:" + str);
                }
                if (list != null) {
                    list.add(str);
                }
            }
        }
    }

    public static boolean arePermissionsGranted(Context context, String[] strArr) {
        for (String str : strArr) {
            if (context.checkSelfPermission(str) != 0) {
                return false;
            }
        }
        return true;
    }

    public static void requestSdCardGranted(Activity activity, int i, String str) {
        Intent intent = new Intent(activity, (Class<?>) RequestPermissionSdCardActivity.class);
        intent.putExtra(RequestPermissionSdCardActivity.EXTRA_UUID, str);
        activity.startActivityForResult(intent, i);
    }

    public static boolean areCallerGeoPermissionsGranted(Activity activity) {
        for (String str : REQUEST_LOCATION_PERMISSION) {
            if (!checkCallerPermission(activity, str)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkCallerPermission(Activity activity, String str) throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = activity.getApplicationContext().getPackageManager().getPackageInfo(activity.getCallingPackage(), 4096);
            if (packageInfo == null || packageInfo.requestedPermissions == null) {
                return false;
            }
            int i = 0;
            while (true) {
                if (i >= packageInfo.requestedPermissions.length) {
                    break;
                }
                if (!str.equals(packageInfo.requestedPermissions[i])) {
                    i++;
                } else if ((packageInfo.requestedPermissionsFlags[i] & 2) != 0) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
