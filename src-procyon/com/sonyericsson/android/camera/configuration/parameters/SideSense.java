// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum SideSense implements UserSettingValue
{
    private static final SideSense[] $VALUES;
    
    OFF(-1, 2131690115), 
    ON(-1, 2131690116);
    
    public static final String TAG = "SideSense";
    private static final int sParameterTextId = 2131690131;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new SideSense[] { SideSense.ON, SideSense.OFF };
    }
    
    private SideSense(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static SideSense getDefaultValue() {
        return SideSense.ON;
    }
    
    public static SideSense[] getOptions() {
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
        return UserSettingKey.SIDE_SENSE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690131;
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
