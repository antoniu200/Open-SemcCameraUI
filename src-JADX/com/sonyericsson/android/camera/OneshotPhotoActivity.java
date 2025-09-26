package com.sonyericsson.android.camera;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OneshotPhotoActivity extends CameraActivity {
    @Override // com.sonyericsson.android.camera.CameraActivity, com.sonyericsson.cameracommon.activity.TerminateListener
    public void terminateApplication() {
        finish();
    }
}
