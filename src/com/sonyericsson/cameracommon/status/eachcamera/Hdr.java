package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Hdr extends EnumValue<Value> implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.OFF;
    public static final String KEY = "hdr";
    private static int REQUIRED_PROVIDER_VERSION = 12;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return "hdr";
    }

    public static Value fromCameraParameter(VideoHdr videoHdr) {
        if (videoHdr == VideoHdr.HDR_ON) {
            return Value.ON;
        }
        return Value.OFF;
    }

    public Hdr(Value value) {
        super(value);
    }

    public enum Value {
        ON("on"),
        OFF("off");

        private final String mStringExpression;

        Value(String str) {
            this.mStringExpression = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mStringExpression;
        }
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return REQUIRED_PROVIDER_VERSION;
    }
}
