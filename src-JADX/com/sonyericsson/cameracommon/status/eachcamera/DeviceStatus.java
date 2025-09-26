package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DeviceStatus extends EnumValue<Value> implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.POWER_OFF;
    public static final String KEY = "device_status";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    public enum Value {
        POWER_ON("power_on"),
        POWER_OFF("power_off"),
        STILL_PREVIEW("still_preview"),
        VIDEO_PREVIEW("video_preview"),
        PICTURE_TAKING("picture_taking"),
        VIDEO_RECORDING("video_recording"),
        PICTURE_TAKING_DURING_VIDEO_RECORDING("picture_taking_during_video_recording");

        private final String mStringExpression;

        Value(String str) {
            this.mStringExpression = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mStringExpression;
        }
    }

    public DeviceStatus(Value value) {
        super(value);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
