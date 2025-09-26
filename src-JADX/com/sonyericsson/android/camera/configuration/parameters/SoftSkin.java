package com.sonyericsson.android.camera.configuration.parameters;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum SoftSkin implements UserSettingValue {
    ON(-1, R.string.cam_strings_settings_on_txt, 0.5f),
    OFF(-1, R.string.cam_strings_settings_off_txt, 0.0f);

    public static final String TAG = "SoftSkin";
    private static final int sParameterTextId = 2131690167;
    private final int mIconId;
    private final int mTextId;
    private final float mValue;

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131690167;
    }

    public int getParameterkeyTitleTextId() {
        return R.string.cam_strings_soft_skin_effect_title_cy_txt;
    }

    SoftSkin(int i, int i2, float f) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = f;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.SOFT_SKIN;
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

    public static SoftSkin[] getOptions(CapturingMode capturingMode) {
        if (capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT) {
            if (PlatformCapability.isSoftSkinSupported(capturingMode.getCameraId())) {
                return new SoftSkin[]{ON, OFF};
            }
            return new SoftSkin[0];
        }
        return new SoftSkin[0];
    }

    public int getLevel(int i) {
        return (int) (this.mValue * i);
    }

    public static SoftSkin getDefaultValue(Context context, CapturingMode capturingMode) {
        if (capturingMode.isFront()) {
            return valueOf(context.getResources().getString(R.string.default_frontcamera_softskin_setting));
        }
        return ON;
    }
}
