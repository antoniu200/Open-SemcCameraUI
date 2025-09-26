package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ArtFilter extends EnumValue<Value> implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.OFF;
    public static final String KEY = "art_filter";
    private static final int REQUIRED_VERSION = 1;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return 1;
    }

    public enum Value {
        SINGLE("single"),
        MULTI("multi"),
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

    public ArtFilter(Value value) {
        super(value);
    }
}
