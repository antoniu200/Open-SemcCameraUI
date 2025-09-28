package com.sonyericsson.android.camera.setting;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.UserSettingKey;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UiControlSettings {
    private final SharedPreferencesAccessor mAccessor;

    UiControlSettings(Context context) {
        this.mAccessor = new SharedPreferencesAccessor(context, SharedPreferencesConstants.CAMERA_VIEW_SHARED_PREFERENCES_NAME);
    }

    public UserSettingKey getLastImageQualityControlTab(boolean z) {
        String string;
        if (z) {
            string = this.mAccessor.readString(SharedPreferencesConstants.KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_FRONT, UserSettingKey.EV.toString());
        } else {
            string = this.mAccessor.readString(SharedPreferencesConstants.KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_MAIN, UserSettingKey.EV.toString());
        }
        if (string.equals(UserSettingKey.EV.toString())) {
            return UserSettingKey.EV;
        }
        if (string.equals(UserSettingKey.WHITE_BALANCE.toString())) {
            return UserSettingKey.WHITE_BALANCE;
        }
        if (string.equals(UserSettingKey.ISO.toString())) {
            return UserSettingKey.ISO;
        }
        if (string.equals(UserSettingKey.SHUTTER_SPEED.toString())) {
            return UserSettingKey.SHUTTER_SPEED;
        }
        if (string.equals(UserSettingKey.FOCUS_RANGE.toString())) {
            return UserSettingKey.FOCUS_RANGE;
        }
        return UserSettingKey.EV;
    }

    public void setLastImageQualityControlTab(UserSettingKey userSettingKey, boolean z) {
        if (z) {
            this.mAccessor.writeString(SharedPreferencesConstants.KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_FRONT, userSettingKey.toString(), false);
        } else {
            this.mAccessor.writeString(SharedPreferencesConstants.KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_MAIN, userSettingKey.toString(), false);
        }
    }

    public void save() {
        this.mAccessor.apply();
    }

    public void clearUIControlSettings() {
        this.mAccessor.clearParameters(true);
    }
}
