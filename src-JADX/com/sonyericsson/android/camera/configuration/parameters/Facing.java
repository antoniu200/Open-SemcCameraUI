package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum Facing implements UserSettingValue {
    BACK(R.drawable.cam_core_main_front_toggle_icn, R.string.cam_strings_accessibility_switch_to_front_txt, CameraInfo.CameraId.BACK),
    FRONT(R.drawable.cam_core_main_front_toggle_icn, R.string.cam_strings_accessibility_switch_to_main_txt, CameraInfo.CameraId.FRONT);

    public static final String TAG = "Facing";
    private static Facing[] sOptions = null;
    private static final int sParameterTextId = 2131689665;
    private final int mIconId;
    private final int mTextId;
    private final CameraInfo.CameraId mValue;

    public static final void preload() {
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public int getKeyTextId() {
        return 2131689665;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public String getValue() {
        return null;
    }

    Facing(int i, int i2, CameraInfo.CameraId cameraId) {
        this.mIconId = i;
        this.mTextId = i2;
        this.mValue = cameraId;
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public void apply(UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingValue
    public UserSettingKey getKey() {
        return UserSettingKey.FACING;
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

    public CameraInfo.CameraId getCameraId() {
        return this.mValue;
    }

    public static Facing[] getOptions() {
        if (sOptions == null) {
            if (PlatformCapability.isFrontCameraSupported()) {
                sOptions = new Facing[]{BACK, FRONT};
            } else {
                sOptions = new Facing[]{BACK};
            }
        }
        return (Facing[]) sOptions.clone();
    }
}
