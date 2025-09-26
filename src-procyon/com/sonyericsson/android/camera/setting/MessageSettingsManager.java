// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import android.content.Context;

public class MessageSettingsManager implements MessageSettings
{
    private SharedPreferencesAccessor mAppAccessor;
    private SharedPreferencesAccessor mCurrentAccessor;
    private SharedPreferencesAccessor mDefaultAccessor;
    private String mKeyPrefix;
    private SharedPreferencesAccessor mTutorialAccessor;
    
    public MessageSettingsManager(final Context context) {
        this.mAppAccessor = new SharedPreferencesAccessor(context, "com.sonyericsson.android.camera.shared_preferences");
        this.mDefaultAccessor = new SharedPreferencesAccessor(context);
        this.mTutorialAccessor = new SharedPreferencesAccessor(context, "tutorial");
        this.mKeyPrefix = context.getPackageName();
    }
    
    private SharedPreferencesAccessor getAccessor(final MessageType messageType) {
        switch (MessageSettingsManager$1.$SwitchMap$com$sonyericsson$android$camera$setting$MessageType[messageType.ordinal()]) {
            default: {
                return this.mAppAccessor;
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
                return this.mTutorialAccessor;
            }
            case 1: {
                return this.mDefaultAccessor;
            }
        }
    }
    
    private String makeKey(final MessageType messageType) {
        if (messageType.isPrefix()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(messageType.getKey());
            sb.append(this.mKeyPrefix);
            return sb.toString();
        }
        return messageType.getKey();
    }
    
    @Override
    public void clearSavedMessageSettings() {
        this.mAppAccessor.clearParameters(true);
        this.mDefaultAccessor.clearParameters(true);
        this.mTutorialAccessor.clearParameters(true);
        this.mCurrentAccessor = null;
    }
    
    @Override
    public int getDisplayCount(final MessageType messageType) {
        final String key = this.makeKey(messageType);
        this.mCurrentAccessor = this.getAccessor(messageType);
        return this.mCurrentAccessor.readInt(key, 0);
    }
    
    @Override
    public boolean isNeverShow(final MessageType messageType) {
        final String key = this.makeKey(messageType);
        this.mCurrentAccessor = this.getAccessor(messageType);
        return this.mCurrentAccessor.readBoolean(key, false);
    }
    
    @Override
    public void save() {
        this.mCurrentAccessor.apply();
    }
    
    @Override
    public void setDisplayCount(final MessageType messageType, final int n) {
        (this.mCurrentAccessor = this.getAccessor(messageType)).writeInt(this.makeKey(messageType), n, false);
    }
    
    @Override
    public void setNeverShow(final MessageType messageType, final boolean b) {
        (this.mCurrentAccessor = this.getAccessor(messageType)).writeBoolean(this.makeKey(messageType), b, false);
    }
}
