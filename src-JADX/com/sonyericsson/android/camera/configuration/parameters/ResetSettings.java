package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ResetSettings implements UserSettingValue {
    DUMMY_ON(-1, -1),
    DUMMY_OFF(-1, -1);

    public static final String TAG = "ResetSettings";
    private static final int sParameterTextId = -1;
    private final int mIconId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return -1;
    }

    ResetSettings(int i, int i2) {
        this.mIconId = i;
        this.mTextId = i2;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.RESET_SETTINGS;
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

    public static ResetSettings[] getOptions() {
        return values();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public static ResetSettings getDefaultValue() {
        return DUMMY_OFF;
    }
}
