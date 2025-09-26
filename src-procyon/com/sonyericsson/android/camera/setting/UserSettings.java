// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;
import com.sonyericsson.android.camera.configuration.IntentReader;
import android.net.Uri;
import com.sonyericsson.android.camera.LaunchCondition;
import android.content.Context;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

public interface UserSettings
{
    void applyCapturingMode();
    
    void changeCapturingMode(final CapturingMode p0);
    
    void clearCachedUserSetting();
    
    void clearSavedUserSetting();
    
    void commit();
    
    UserSettingValue get(final UserSettingKey p0);
    
    UserSettingValue get(final CapturingMode p0, final UserSettingKey p1);
    
    MaxVideoSize getMaxVideoSize(final Storage p0, final Storage.StorageType p1, final RecordingProfile p2);
    
    UserSettingValue[] getOptions(final UserSettingKey p0);
    
    Parameters getParameters();
    
    boolean isLimitForSizeOrDuration();
    
    void prepare(final Context p0, final LaunchCondition.OneShotMode p1, final Uri p2, final IntentReader.VideoQualityConfigurations p3, final ExtraSettings p4);
    
    void register(final UserSettingApplicable p0);
    
    void release();
    
    void resetTempParameters();
    
    void set(final UserSettingValue p0);
}
