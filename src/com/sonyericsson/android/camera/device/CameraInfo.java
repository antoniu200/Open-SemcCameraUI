package com.sonyericsson.android.camera.device;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraInfo {
    public boolean canDisableShutterSound;
    public CameraId facing;
    public int orientation;

    public enum CameraId {
        BACK("0", 0),
        FRONT("1", 1);

        private final String mCameraDeviceId;
        private final int mCameraDeviceIdApi1;

        CameraId(String str, int i) {
            this.mCameraDeviceId = str;
            this.mCameraDeviceIdApi1 = i;
        }

        public String getCameraDeviceId() {
            return this.mCameraDeviceId;
        }

        public int getCameraDeviceIdApi1() {
            return this.mCameraDeviceIdApi1;
        }
    }
}
