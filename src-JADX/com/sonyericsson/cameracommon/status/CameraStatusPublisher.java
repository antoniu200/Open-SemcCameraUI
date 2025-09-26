package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.status.CameraStatusValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class CameraStatusPublisher<T extends CameraStatusValue> {
    private static final String ACTION_CAMERA_STATUS_UPDATE = "com.sonymobile.cameracommon.action.CAMERA_STATUS_UPDATE";
    private static final String EXTRA_CAMERA_STATUS = "CAMERA_STATUS";
    private static final String PACKAGE = "com.sonymobile.cameracommon";
    public static final String TAG = "CameraStatusPublisher";
    private static volatile int sCameraCommonVersion = -1;
    private final ContentValues mContentValues = new ContentValues();
    private final Context mContext;

    protected String keyPrefix() {
        return "";
    }

    public abstract CameraStatusPublisher<T> putDefaultAll();

    CameraStatusPublisher(Context context) {
        this.mContext = context;
        if (sCameraCommonVersion < 0) {
            sCameraCommonVersion = getCameraCommonVersion(context.getPackageManager());
        }
    }

    public CameraStatusPublisher<T> put(T t) {
        if (t != null && getCameraCommonVersion() >= t.minRequiredVersion()) {
            t.putInto(this.mContentValues, keyPrefix());
            if (CamLog.VERBOSE) {
                CamLog.d("### ### ### key/value: " + keyPrefix() + t.getKey() + " / " + t.getValueForDebug());
            }
        }
        return this;
    }

    public void publish() {
        publish(this.mContext, this.mContentValues);
    }

    private static void publish(Context context, ContentValues contentValues) {
        if (CamLog.VERBOSE) {
            CamLog.d("### ### ### publish() start");
        }
        if (context == null) {
            return;
        }
        Intent intent = new Intent(ACTION_CAMERA_STATUS_UPDATE);
        intent.setPackage("com.sonymobile.cameracommon");
        intent.putExtra(EXTRA_CAMERA_STATUS, contentValues);
        try {
            context.startService(intent);
        } catch (SecurityException e) {
            if (CamLog.VERBOSE) {
                CamLog.e("Fail to update the camera status.", e);
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("### ### ### publish() end");
        }
    }

    protected int getCameraCommonVersion() {
        return sCameraCommonVersion;
    }

    private static int getCameraCommonVersion(PackageManager packageManager) throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.sonymobile.cameracommon", 0);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException unused) {
            CamLog.e("com.sonymobile.cameracommon package doesn't exist.");
        }
        return 0;
    }
}
