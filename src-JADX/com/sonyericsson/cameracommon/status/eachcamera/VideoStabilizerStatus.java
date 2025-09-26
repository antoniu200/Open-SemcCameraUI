package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoStabilizerStatus extends EnumValue<Value> implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.OFF;
    public static final String KEY = "video_stabilizer";
    private static int REQUIRED_PROVIDER_VERSION = 1;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    public enum Value {
        ON("on"),
        OFF("off"),
        INTELLIGENT_ACTIVE(CameraParameters.VS_ON_INTELLIGENT_ACTIVE);

        private final String mStringExpression;

        Value(String str) {
            this.mStringExpression = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mStringExpression;
        }
    }

    public VideoStabilizerStatus(Value value) {
        super(value);
    }

    public static VideoStabilizerStatus fromCameraParameter(String str) {
        if ("on".equals(str)) {
            return new VideoStabilizerStatus(Value.ON);
        }
        if ("on".equals(str)) {
            return new VideoStabilizerStatus(Value.ON);
        }
        if (CameraParameters.VS_ON_INTELLIGENT_ACTIVE.equals(str)) {
            return new VideoStabilizerStatus(Value.INTELLIGENT_ACTIVE);
        }
        return new VideoStabilizerStatus(Value.OFF);
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
