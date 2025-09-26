// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum HelpGuide implements UserSettingValue
{
    private static final HelpGuide[] $VALUES;
    
    DUMMY_OFF(-1, -1), 
    DUMMY_ON(-1, -1);
    
    public static final String TAG = "HelpGuide";
    private static final int sParameterTextId = -1;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new HelpGuide[] { HelpGuide.DUMMY_ON, HelpGuide.DUMMY_OFF };
    }
    
    private HelpGuide(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static HelpGuide getDefaultValue() {
        return HelpGuide.DUMMY_OFF;
    }
    
    public static HelpGuide[] getOptions() {
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
        return UserSettingKey.HELP_GUIDE;
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
