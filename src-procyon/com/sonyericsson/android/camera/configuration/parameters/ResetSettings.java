// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum ResetSettings implements UserSettingValue
{
    private static final ResetSettings[] $VALUES;
    
    DUMMY_OFF(-1, -1), 
    DUMMY_ON(-1, -1);
    
    public static final String TAG = "ResetSettings";
    private static final int sParameterTextId = -1;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new ResetSettings[] { ResetSettings.DUMMY_ON, ResetSettings.DUMMY_OFF };
    }
    
    private ResetSettings(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static ResetSettings getDefaultValue() {
        return ResetSettings.DUMMY_OFF;
    }
    
    public static ResetSettings[] getOptions() {
        return values();
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.RESET_SETTINGS;
    }
    
    @Override
    public int getKeyTextId() {
        return -1;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
}
