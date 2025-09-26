package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum SideSense implements UserSettingValue {
    ON(-1, R.string.cam_strings_settings_on_txt),
    OFF(-1, R.string.cam_strings_settings_off_txt);

    public static final String TAG = "SideSense";
    private static final int sParameterTextId = 2131690131;
    private final int mIconId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690131;
    }

    SideSense(int i, int i2) {
        this.mIconId = i;
        this.mTextId = i2;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.SIDE_SENSE;
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

    public static SideSense[] getOptions() {
        return values();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }

    public static SideSense getDefaultValue() {
        return ON;
    }
}
