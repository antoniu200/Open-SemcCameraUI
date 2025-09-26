package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OnlineRemote extends EnumValue<Value> implements EachCameraStatusValue {
    public static final Value DEFAULT_VALUE = Value.OFF;
    public static final String KEY = "online_remote";
    private static final int REQUIRED_VERSION = 10;

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public String getKey() {
        return KEY;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusValue
    public int minRequiredVersion() {
        return 10;
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

    public OnlineRemote(Value value) {
        super(value);
    }
}
