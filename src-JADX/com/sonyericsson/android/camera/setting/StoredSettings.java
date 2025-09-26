package com.sonyericsson.android.camera.setting;

import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface StoredSettings {
    void clearAllSettings(Storage storage);

    LastSettings getLastSettings();

    MessageSettings getMessageSettings();

    UiControlSettings getUiControlSettings();

    UserSettings getUserSettings();
}
