// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum DistortionCorrection implements UserSettingValue
{
    private static final DistortionCorrection[] $VALUES;
    
    OFF(-1, 2131689931, "off"), 
    ON(-1, 2131689932, "on");
    
    private static final int sParameterTextId;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new DistortionCorrection[] { DistortionCorrection.OFF, DistortionCorrection.ON };
        sParameterTextId = UserSettingKey.DISTORTION_CORRECTION.getTitleTextId();
    }
    
    private DistortionCorrection(final int mIconId, final int mTextId, final String mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static DistortionCorrection[] getOptions() {
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
        return UserSettingKey.DISTORTION_CORRECTION;
    }
    
    @Override
    public int getKeyTextId() {
        return DistortionCorrection.sParameterTextId;
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
        return this.mValue;
    }
}
