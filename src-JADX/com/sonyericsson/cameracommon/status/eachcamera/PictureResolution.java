package com.sonyericsson.cameracommon.status.eachcamera;

import android.graphics.Rect;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PictureResolution extends ResolutionValue implements EachCameraStatusValue {
    public static final Rect DEFAULT_VALUE = new Rect(0, 0, 0, 0);
    public static final String KEY = "picture_resolution";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    public PictureResolution(Rect rect) {
        super(rect);
    }

    @Override // com.sonyericsson.cameracommon.status.eachcamera.ResolutionValue, com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
