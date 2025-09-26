package com.sonyericsson.android.camera;

import com.sonyericsson.android.camera.device.CameraInfo;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ActionMode {
    public final CameraInfo.CameraId mCameraId;
    public final boolean mIsOneShot;
    public final int mType;

    public ActionMode(boolean z, int i, CameraInfo.CameraId cameraId) {
        this.mIsOneShot = z;
        this.mType = i;
        this.mCameraId = cameraId;
    }
}
