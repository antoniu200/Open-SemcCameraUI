// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;

public enum FrontAngle implements UserSettingValue
{
    private static final FrontAngle[] $VALUES;
    
    CROPPED, 
    DEFAULT;
    
    public static final String TAG = "FrontAngle";
    
    static {
        $VALUES = new FrontAngle[] { FrontAngle.DEFAULT, FrontAngle.CROPPED };
    }
    
    public static FrontAngle[] getOptions() {
        if (PlatformCapability.isSuperWideSupported(CameraInfo.CameraId.FRONT)) {
            return values();
        }
        return new FrontAngle[0];
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return 0;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.FRONT_ANGLE;
    }
    
    @Override
    public int getKeyTextId() {
        return 0;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return 0;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
}
