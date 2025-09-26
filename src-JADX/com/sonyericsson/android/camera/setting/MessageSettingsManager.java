package com.sonyericsson.android.camera.setting;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MessageSettingsManager implements MessageSettings {
    private SharedPreferencesAccessor mAppAccessor;
    private SharedPreferencesAccessor mCurrentAccessor;
    private SharedPreferencesAccessor mDefaultAccessor;
    private String mKeyPrefix;
    private SharedPreferencesAccessor mTutorialAccessor;

    public MessageSettingsManager(Context context) {
        this.mAppAccessor = new SharedPreferencesAccessor(context, SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME);
        this.mDefaultAccessor = new SharedPreferencesAccessor(context);
        this.mTutorialAccessor = new SharedPreferencesAccessor(context, SharedPreferencesConstants.TUTORIAL_SHARED_PREFS_NAME);
        this.mKeyPrefix = context.getPackageName();
    }

    private String makeKey(MessageType messageType) {
        if (messageType.isPrefix()) {
            return messageType.getKey() + this.mKeyPrefix;
        }
        return messageType.getKey();
    }

    private SharedPreferencesAccessor getAccessor(MessageType messageType) {
        switch (messageType) {
            case EXTRA_HINT_STARTUP_COUNT_KEY:
                return this.mDefaultAccessor;
            case SETUP_WIZARD:
            case TUTORIAL_EYE_GUIDE:
            case TUTORIAL_HAND_SHUTTER:
            case TUTORIAL_SUPER_SLOW_MOTION:
            case TUTORIAL_SUPER_SLOW_MOTION_SHOT:
            case TUTORIAL_STANDARD_SLOW_MOTION:
            case TUTORIAL_MANUAL_FUSION:
            case TUTORIAL_VIDEO_FUSION:
                return this.mTutorialAccessor;
            default:
                return this.mAppAccessor;
        }
    }

    @Override // com.sonyericsson.android.camera.setting.MessageSettings
    public boolean isNeverShow(MessageType messageType) {
        String strMakeKey = makeKey(messageType);
        this.mCurrentAccessor = getAccessor(messageType);
        return this.mCurrentAccessor.readBoolean(strMakeKey, false);
    }

    @Override // com.sonyericsson.android.camera.setting.MessageSettings
    public void setNeverShow(MessageType messageType, boolean z) {
        String strMakeKey = makeKey(messageType);
        this.mCurrentAccessor = getAccessor(messageType);
        this.mCurrentAccessor.writeBoolean(strMakeKey, z, false);
    }

    @Override // com.sonyericsson.android.camera.setting.MessageSettings
    public int getDisplayCount(MessageType messageType) {
        String strMakeKey = makeKey(messageType);
        this.mCurrentAccessor = getAccessor(messageType);
        return this.mCurrentAccessor.readInt(strMakeKey, 0);
    }

    @Override // com.sonyericsson.android.camera.setting.MessageSettings
    public void setDisplayCount(MessageType messageType, int i) {
        String strMakeKey = makeKey(messageType);
        this.mCurrentAccessor = getAccessor(messageType);
        this.mCurrentAccessor.writeInt(strMakeKey, i, false);
    }

    @Override // com.sonyericsson.android.camera.setting.MessageSettings
    public void save() {
        this.mCurrentAccessor.apply();
    }

    @Override // com.sonyericsson.android.camera.setting.MessageSettings
    public void clearSavedMessageSettings() {
        this.mAppAccessor.clearParameters(true);
        this.mDefaultAccessor.clearParameters(true);
        this.mTutorialAccessor.clearParameters(true);
        this.mCurrentAccessor = null;
    }
}
