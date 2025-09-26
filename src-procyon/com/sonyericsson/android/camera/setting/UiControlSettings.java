// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import android.content.Context;

public class UiControlSettings
{
    private final SharedPreferencesAccessor mAccessor;
    
    UiControlSettings(final Context context) {
        this.mAccessor = new SharedPreferencesAccessor(context, "com.sonyericsson.android.camera.shared_preferences_view");
    }
    
    public void clearUIControlSettings() {
        this.mAccessor.clearParameters(true);
    }
    
    public UserSettingKey getLastImageQualityControlTab(final boolean b) {
        String s;
        if (b) {
            s = this.mAccessor.readString("KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_FRONT", UserSettingKey.EV.toString());
        }
        else {
            s = this.mAccessor.readString("KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_MAIN", UserSettingKey.EV.toString());
        }
        UserSettingKey userSettingKey;
        if (s.equals(UserSettingKey.EV.toString())) {
            userSettingKey = UserSettingKey.EV;
        }
        else if (s.equals(UserSettingKey.WHITE_BALANCE.toString())) {
            userSettingKey = UserSettingKey.WHITE_BALANCE;
        }
        else if (s.equals(UserSettingKey.ISO.toString())) {
            userSettingKey = UserSettingKey.ISO;
        }
        else if (s.equals(UserSettingKey.SHUTTER_SPEED.toString())) {
            userSettingKey = UserSettingKey.SHUTTER_SPEED;
        }
        else if (s.equals(UserSettingKey.FOCUS_RANGE.toString())) {
            userSettingKey = UserSettingKey.FOCUS_RANGE;
        }
        else {
            userSettingKey = UserSettingKey.EV;
        }
        return userSettingKey;
    }
    
    public void save() {
        this.mAccessor.apply();
    }
    
    public void setLastImageQualityControlTab(final UserSettingKey userSettingKey, final boolean b) {
        if (b) {
            this.mAccessor.writeString("KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_FRONT", userSettingKey.toString(), false);
        }
        else {
            this.mAccessor.writeString("KEY_LAST_IMAGE_QUALITY_CONTROL_TAB_MAIN", userSettingKey.toString(), false);
        }
    }
}
