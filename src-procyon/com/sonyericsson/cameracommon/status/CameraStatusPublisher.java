// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status;

import android.os.Parcelable;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager$NameNotFoundException;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.ContentValues;

public abstract class CameraStatusPublisher<T extends CameraStatusValue>
{
    private static final String ACTION_CAMERA_STATUS_UPDATE = "com.sonymobile.cameracommon.action.CAMERA_STATUS_UPDATE";
    private static final String EXTRA_CAMERA_STATUS = "CAMERA_STATUS";
    private static final String PACKAGE = "com.sonymobile.cameracommon";
    public static final String TAG = "CameraStatusPublisher";
    private static volatile int sCameraCommonVersion = -1;
    private final ContentValues mContentValues;
    private final Context mContext;
    
    CameraStatusPublisher(final Context mContext) {
        this.mContext = mContext;
        this.mContentValues = new ContentValues();
        if (CameraStatusPublisher.sCameraCommonVersion < 0) {
            CameraStatusPublisher.sCameraCommonVersion = getCameraCommonVersion(mContext.getPackageManager());
        }
    }
    
    private static int getCameraCommonVersion(final PackageManager packageManager) {
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo("com.sonymobile.cameracommon", 0);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("com.sonymobile.cameracommon package doesn't exist.");
        }
        return 0;
    }
    
    private static void publish(final Context context, final ContentValues contentValues) {
        if (CamLog.VERBOSE) {
            CamLog.d("### ### ### publish() start");
        }
        if (context == null) {
            return;
        }
        final Intent intent = new Intent("com.sonymobile.cameracommon.action.CAMERA_STATUS_UPDATE");
        intent.setPackage("com.sonymobile.cameracommon");
        intent.putExtra("CAMERA_STATUS", (Parcelable)contentValues);
        try {
            context.startService(intent);
        }
        catch (final SecurityException ex) {
            if (CamLog.VERBOSE) {
                CamLog.e("Fail to update the camera status.", ex);
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("### ### ### publish() end");
        }
    }
    
    protected int getCameraCommonVersion() {
        return CameraStatusPublisher.sCameraCommonVersion;
    }
    
    protected String keyPrefix() {
        return "";
    }
    
    public void publish() {
        publish(this.mContext, this.mContentValues);
    }
    
    public CameraStatusPublisher<T> put(final T t) {
        if (t != null && this.getCameraCommonVersion() >= t.minRequiredVersion()) {
            t.putInto(this.mContentValues, this.keyPrefix());
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("### ### ### key/value: ");
                sb.append(this.keyPrefix());
                sb.append(t.getKey());
                sb.append(" / ");
                sb.append(t.getValueForDebug());
                CamLog.d(sb.toString());
            }
        }
        return this;
    }
    
    public abstract CameraStatusPublisher<T> putDefaultAll();
}
