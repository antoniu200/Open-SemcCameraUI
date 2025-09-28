package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum FocusRange implements UserSettingIntValue {
    AF(-1, R.string.cam_strings_image_quality_control_focus_auto_txt),
    MF(-1, R.string.cam_strings_image_quality_control_focus_manual_txt),
    DEFAULT(-1, R.string.cam_strings_image_quality_control_focus_manual_txt);

    public static final int NUMBER_OF_DIVISION_FOR_THE_WHOLE_RANGE = 99;
    public static final int RATIO_OF_THE_RANGE_FROM_1M_TO_INFINITY = 1;
    public static final int RATIO_OF_THE_RANGE_FROM_MACRO_TO_1M = 2;
    private final int mIconId;
    private final int mTextId;
    private int mValue;

    public static float getThreshold() {
        return 33.0f;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 0;
    }

    FocusRange(int i, int i2) {
        this.mIconId = i;
        this.mTextId = i2;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.FOCUS_RANGE;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return toString();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingIntValue
    public void setInt(int i) {
        this.mValue = i;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingIntValue
    public int getInt() {
        return this.mValue;
    }

    public float getFocusRange(CameraInfo.CameraId cameraId) {
        float fFloatValue = PlatformCapability.getCameraCapability(cameraId).MACRO_FOCUS_RANGE.get().floatValue();
        float fFloatValue2 = CameraParameters.MANUAL_FOCUS_INFINITY.floatValue();
        float fFloatValue3 = CameraParameters.MANUAL_FOCUS_1M.floatValue();
        float f = this.mValue;
        float threshold = getThreshold();
        if (0.0f <= f && f < threshold) {
            return fFloatValue2 + ((Math.abs(fFloatValue3 - fFloatValue2) / threshold) * f);
        }
        if (threshold > f || f > 99.0f) {
            return 1.0f;
        }
        return fFloatValue3 + ((Math.abs(fFloatValue3 - fFloatValue) / (99.0f - threshold)) * (f - threshold));
    }

    public static FocusRange[] getOptions(CameraInfo.CameraId cameraId) {
        if (PlatformCapability.isManualFocusSupported(cameraId) && CameraInfo.CameraId.BACK == cameraId) {
            return values();
        }
        return new FocusRange[0];
    }
}
