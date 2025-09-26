package com.sonyericsson.cameracommon.status.global;

import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.cameracommon.status.CameraIdArrayValue;
import com.sonyericsson.cameracommon.status.GlobalCameraStatusValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BuiltInCameraIds extends CameraIdArrayValue implements GlobalCameraStatusValue {
    public static final CameraInfo.CameraId[] DEFAULT_VALUE = new CameraInfo.CameraId[0];
    public static final String KEY = "built_in_camera_ids";
    private static int REQUIRED_PROVIDER_VERSION = 10;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    public BuiltInCameraIds(CameraInfo.CameraId... cameraIdArr) {
        super(cameraIdArr);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
