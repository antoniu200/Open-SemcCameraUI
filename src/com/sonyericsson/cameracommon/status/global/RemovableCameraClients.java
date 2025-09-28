package com.sonyericsson.cameracommon.status.global;

import com.sonyericsson.cameracommon.status.GlobalCameraStatusValue;
import com.sonyericsson.cameracommon.status.IntegerValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RemovableCameraClients extends IntegerValue implements GlobalCameraStatusValue {
    public static final int DEFAULT_VALUE = 0;
    public static final String KEY = "removable_camera_clients";
    private static int REQUIRED_PROVIDER_VERSION = 10;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    public RemovableCameraClients(int i) {
        super(i);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
