// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import com.sonyericsson.android.camera.parameter.UserSettingManager;
import com.sonyericsson.cameracommon.storage.Storage;
import android.content.Context;

class StoredSettingsProxy implements StoredSettings
{
    private LastSettings mLastSettings;
    private MessageSettings mMessageSettingManager;
    private UiControlSettings mUiControlSettings;
    private UserSettings mUserSettingManager;
    
    StoredSettingsProxy(final Context context, final Storage storage) {
        this.mUserSettingManager = new UserSettingManager(context, storage);
        this.mMessageSettingManager = new MessageSettingsManager(context);
        this.mLastSettings = new LastSettings(context);
        this.mUiControlSettings = new UiControlSettings(context);
    }
    
    @Override
    public void clearAllSettings(final Storage storage) {
        this.mUserSettingManager.clearSavedUserSetting();
        this.mUserSettingManager.release();
        this.mMessageSettingManager.clearSavedMessageSettings();
        this.mUiControlSettings.clearUIControlSettings();
        this.mLastSettings.clear();
    }
    
    @Override
    public LastSettings getLastSettings() {
        return this.mLastSettings;
    }
    
    @Override
    public MessageSettings getMessageSettings() {
        return this.mMessageSettingManager;
    }
    
    @Override
    public UiControlSettings getUiControlSettings() {
        return this.mUiControlSettings;
    }
    
    @Override
    public UserSettings getUserSettings() {
        return this.mUserSettingManager;
    }
}
