package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum CameraKey implements UserSettingValue {
    BURST_SHOT(R.string.cam_strings_settings_on_txt),
    TAKE_PHOTO(R.string.cam_strings_settings_off_txt);

    public static final String TAG = "CameraKey";
    private static final int sParameterTextId = 2131689663;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getIconId() {
        return -1;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689663;
    }

    CameraKey(int i) {
        this.mTextId = i;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.CAMERA_KEY;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getTextId() {
        return this.mTextId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return toString();
    }

    public static CameraKey[] getOptions(ActionMode actionMode) {
        if (actionMode.mIsOneShot) {
            return new CameraKey[]{TAKE_PHOTO};
        }
        return values();
    }

    public static CameraKey getDefaultValue() {
        return TAKE_PHOTO;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
    }
}
