package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum PredictiveLaunch implements UserSettingValue {
    TOUCH_TO_LAUNCH(-1, R.string.cam_strings_predictive_launch_launch_only_txt, "1", false),
    TOUCH_TO_LAUNCH_AND_CAPTURE(-1, R.string.cam_strings_predictive_launch_launch_and_capture_txt, "1", true),
    OFF(-1, R.string.cam_strings_settings_off_txt, "0", false);

    private static final String TAG = "PredictiveLaunch";
    private final boolean mDoCapture;
    private final int mIconId;
    private final String mSecureValue;
    private final int mTextId;

    PredictiveLaunch(int i, int i2, String str, boolean z) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mSecureValue = str;
        this.mDoCapture = z;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.PREDICTIVE_LAUNCH;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return this.mIconId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return toString();
    }

    public String getSecureValue() {
        return this.mSecureValue;
    }

    public boolean doCapture() {
        return this.mDoCapture;
    }

    public static PredictiveLaunch[] getOptions() {
        return values();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return getKey().getTitleTextId();
    }

    public static PredictiveLaunch getDefaultValue() {
        return OFF;
    }
}
