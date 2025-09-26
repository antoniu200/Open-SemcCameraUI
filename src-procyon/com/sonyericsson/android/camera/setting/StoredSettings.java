// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import com.sonyericsson.cameracommon.storage.Storage;

public interface StoredSettings
{
    void clearAllSettings(final Storage p0);
    
    LastSettings getLastSettings();
    
    MessageSettings getMessageSettings();
    
    UiControlSettings getUiControlSettings();
    
    UserSettings getUserSettings();
}
