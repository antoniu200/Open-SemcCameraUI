package com.sonyericsson.cameracommon.status.global;

import com.sonyericsson.cameracommon.status.GlobalCameraStatusValue;
import com.sonyericsson.cameracommon.status.IntegerValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraId extends IntegerValue implements GlobalCameraStatusValue {
    public static final int DEFAULT_VALUE = 0;
    public static final String KEY = "camera_id";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    public static int defaultValue(int i) {
        return i >= 10 ? 0 : 0;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    public CameraId(int i) {
        super(i);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
