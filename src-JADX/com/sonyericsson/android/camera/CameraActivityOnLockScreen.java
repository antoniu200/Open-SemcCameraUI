package com.sonyericsson.android.camera;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraActivityOnLockScreen extends CameraActivity {
    @Override // com.sonyericsson.android.camera.CameraActivity
    protected boolean shouldShowWhenLocked() {
        return true;
    }

    @Override // com.sonyericsson.android.camera.CameraActivity
    public void requestSuspend() {
        finish();
    }
}
