// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum Geotag implements UserSettingValue
{
    private static final Geotag[] $VALUES;
    
    OFF(-1, 2131690115, false), 
    ON(-1, 2131690116, true);
    
    public static final String TAG = "Geotag";
    private static final int sParameterTextId = 2131689857;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new Geotag[] { Geotag.ON, Geotag.OFF };
    }
    
    private Geotag(final int mIconId, final int mTextId, final boolean mBooleanValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mBooleanValue = mBooleanValue;
    }
    
    public static Geotag[] getOptions() {
        return new Geotag[] { Geotag.ON, Geotag.OFF };
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
        return UserSettingKey.GEO_TAG;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689857;
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
    
    public boolean isGeotagOn() {
        return this.mBooleanValue;
    }
}
