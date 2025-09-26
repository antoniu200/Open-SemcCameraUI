package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum AutoReview implements UserSettingValue {
    ALWAYS(-1, R.string.cam_strings_preview_always_txt, 3000),
    FRONT_ONLY(-1, R.string.cam_strings_preview_only_front_txt, 3000),
    OFF(-1, R.string.cam_strings_settings_off_txt, 0);

    public static final String TAG = "AutoReview";
    private static final int sParameterTextId = 2131690021;
    private final int mDuration;
    private final int mIconId;
    private final int mTextId;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690021;
    }

    AutoReview(int i, int i2, int i3) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mDuration = i3;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.AUTO_REVIEW;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getName() {
        return getClass().getName();
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

    public static AutoReview[] getOptions(ActionMode actionMode) {
        if (actionMode.mIsOneShot) {
            return new AutoReview[]{OFF};
        }
        return values();
    }

    public static AutoReview getDefaultValue(boolean z) {
        if (z) {
            return OFF;
        }
        return FRONT_ONLY;
    }

    public int getDuration() {
        return this.mDuration;
    }
}
