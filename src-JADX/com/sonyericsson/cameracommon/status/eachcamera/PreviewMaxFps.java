package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.IntegerValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PreviewMaxFps extends IntegerValue implements EachCameraStatusValue {
    public static final int DEFAULT_VALUE = 0;
    public static final String KEY = "preview_max_fps";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    public PreviewMaxFps(int i) {
        super(i);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
