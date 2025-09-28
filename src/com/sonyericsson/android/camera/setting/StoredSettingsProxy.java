package com.sonyericsson.android.camera.setting;

import android.content.Context;
import com.sonyericsson.android.camera.parameter.UserSettingManager;
import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class StoredSettingsProxy implements StoredSettings {
    private LastSettings mLastSettings;
    private MessageSettings mMessageSettingManager;
    private UiControlSettings mUiControlSettings;
    private UserSettings mUserSettingManager;

    StoredSettingsProxy(Context context, Storage storage) {
        this.mUserSettingManager = new UserSettingManager(context, storage);
        this.mMessageSettingManager = new MessageSettingsManager(context);
        this.mLastSettings = new LastSettings(context);
        this.mUiControlSettings = new UiControlSettings(context);
    }

    @Override // com.sonyericsson.android.camera.setting.StoredSettings
    public UserSettings getUserSettings() {
        return this.mUserSettingManager;
    }

    @Override // com.sonyericsson.android.camera.setting.StoredSettings
    public MessageSettings getMessageSettings() {
        return this.mMessageSettingManager;
    }

    @Override // com.sonyericsson.android.camera.setting.StoredSettings
    public LastSettings getLastSettings() {
        return this.mLastSettings;
    }

    @Override // com.sonyericsson.android.camera.setting.StoredSettings
    public UiControlSettings getUiControlSettings() {
        return this.mUiControlSettings;
    }

    @Override // com.sonyericsson.android.camera.setting.StoredSettings
    public void clearAllSettings(Storage storage) {
        this.mUserSettingManager.clearSavedUserSetting();
        this.mUserSettingManager.release();
        this.mMessageSettingManager.clearSavedMessageSettings();
        this.mUiControlSettings.clearUIControlSettings();
        this.mLastSettings.clear();
    }
}
