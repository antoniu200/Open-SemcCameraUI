// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

public enum ShutterSound implements UserSettingValue
{
    private static final ShutterSound[] $VALUES;
    
    OFF(-1, 2131690115, Boolean.valueOf(false), "sound0/"), 
    SOUND1(-1, 2131690116, Boolean.valueOf(true), "sound1/");
    
    public static final String TAG = "ShutterSound";
    private static final int sParameterTextId = 2131689664;
    private final Boolean mBooleanValue;
    private final String mDirectoryName;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new ShutterSound[] { ShutterSound.SOUND1, ShutterSound.OFF };
    }
    
    private ShutterSound(final int mIconId, final int mTextId, final Boolean mBooleanValue, final String mDirectoryName) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mBooleanValue = mBooleanValue;
        this.mDirectoryName = mDirectoryName;
    }
    
    public static ShutterSound[] getOptions(final boolean b) {
        final ArrayList list = new ArrayList();
        for (final ShutterSound shutterSound : values()) {
            if (shutterSound.mBooleanValue) {
                list.add(shutterSound);
            }
            else if (!b) {
                list.add(shutterSound);
            }
        }
        return list.toArray(new ShutterSound[0]);
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public Boolean getBooleanValue() {
        return this.mBooleanValue;
    }
    
    public String getDirectoryName() {
        return this.mDirectoryName;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.SHUTTER_SOUND;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689664;
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
