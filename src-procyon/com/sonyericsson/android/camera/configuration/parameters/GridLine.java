// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum GridLine implements UserSettingValue
{
    private static final GridLine[] $VALUES;
    
    OFF(-1, 2131690115), 
    ON(-1, 2131690116);
    
    public static final String TAG = "GridLine";
    private static final int sParameterTextId = 2131689865;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new GridLine[] { GridLine.ON, GridLine.OFF };
    }
    
    private GridLine(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static GridLine getDefaultValue() {
        return GridLine.OFF;
    }
    
    public static GridLine[] getOptions() {
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
        return UserSettingKey.GRID_LINE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689865;
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
