// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum TouchCapture implements UserSettingValue
{
    private static final TouchCapture[] $VALUES;
    
    FRONT_ONLY(-1, 2131690024, -1, false), 
    OFF(-1, 2131690115, -1, false), 
    ON(-1, 2131690116, -1, true);
    
    public static final String TAG = "TouchCapture";
    private static final int sParameterTextId = 2131690194;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mNotificationId;
    private final int mTextId;
    
    static {
        $VALUES = new TouchCapture[] { TouchCapture.ON, TouchCapture.FRONT_ONLY, TouchCapture.OFF };
    }
    
    private TouchCapture(final int mIconId, final int mTextId, final int mNotificationId, final boolean mBooleanValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mNotificationId = mNotificationId;
        this.mBooleanValue = mBooleanValue;
    }
    
    public static TouchCapture[] getOptions() {
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
        return UserSettingKey.TOUCH_CAPTURE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690194;
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
