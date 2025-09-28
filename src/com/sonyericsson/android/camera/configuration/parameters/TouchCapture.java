package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum TouchCapture implements UserSettingValue {
    ON(-1, R.string.cam_strings_settings_on_txt, -1, true),
    FRONT_ONLY(-1, R.string.cam_strings_preview_only_front_txt, -1, false),
    OFF(-1, R.string.cam_strings_settings_off_txt, -1, false);

    public static final String TAG = "TouchCapture";
    private static final int sParameterTextId = 2131690194;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mNotificationId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690194;
    }

    TouchCapture(int i, int i2, int i3, boolean z) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mNotificationId = i3;
        this.mBooleanValue = z;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.TOUCH_CAPTURE;
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

    public static TouchCapture[] getOptions() {
        return values();
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }
}
