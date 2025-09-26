// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;

public enum Facing implements UserSettingValue
{
    private static final Facing[] $VALUES;
    
    BACK(2131231095, 2131689610, CameraInfo.CameraId.BACK), 
    FRONT(2131231095, 2131689612, CameraInfo.CameraId.FRONT);
    
    public static final String TAG = "Facing";
    private static Facing[] sOptions;
    private static final int sParameterTextId = 2131689665;
    private final int mIconId;
    private final int mTextId;
    private final CameraInfo.CameraId mValue;
    
    static {
        $VALUES = new Facing[] { Facing.BACK, Facing.FRONT };
    }
    
    private Facing(final int mIconId, final int mTextId, final CameraInfo.CameraId mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static Facing[] getOptions() {
        if (Facing.sOptions == null) {
            if (PlatformCapability.isFrontCameraSupported()) {
                Facing.sOptions = new Facing[] { Facing.BACK, Facing.FRONT };
            }
            else {
                Facing.sOptions = new Facing[] { Facing.BACK };
            }
        }
        return Facing.sOptions.clone();
    }
    
    public static final void preload() {
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public CameraInfo.CameraId getCameraId() {
        return this.mValue;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.FACING;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689665;
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
        return null;
    }
}
