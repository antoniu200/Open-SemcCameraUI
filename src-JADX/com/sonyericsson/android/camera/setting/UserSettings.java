package com.sonyericsson.android.camera.setting;

import android.content.Context;
import android.net.Uri;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.recorder.RecordingProfile;
import com.sonyericsson.android.camera.util.MaxVideoSize;
import com.sonyericsson.cameracommon.storage.Storage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface UserSettings {
    void applyCapturingMode();

    void changeCapturingMode(CapturingMode capturingMode);

    void clearCachedUserSetting();

    void clearSavedUserSetting();

    void commit();

    UserSettingValue get(UserSettingKey userSettingKey);

    UserSettingValue get(CapturingMode capturingMode, UserSettingKey userSettingKey);

    MaxVideoSize getMaxVideoSize(Storage storage, Storage.StorageType storageType, RecordingProfile recordingProfile);

    UserSettingValue[] getOptions(UserSettingKey userSettingKey);

    Parameters getParameters();

    boolean isLimitForSizeOrDuration();

    void prepare(Context context, LaunchCondition.OneShotMode oneShotMode, Uri uri, IntentReader.VideoQualityConfigurations videoQualityConfigurations, ExtraSettings extraSettings);

    void register(UserSettingApplicable userSettingApplicable);

    void release();

    void resetTempParameters();

    void set(UserSettingValue userSettingValue);
}
