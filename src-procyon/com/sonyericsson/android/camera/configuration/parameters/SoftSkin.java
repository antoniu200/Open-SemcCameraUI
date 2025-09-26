// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import android.content.Context;

public enum SoftSkin implements UserSettingValue
{
    private static final SoftSkin[] $VALUES;
    
    OFF(-1, 2131690115, 0.0f), 
    ON(-1, 2131690116, 0.5f);
    
    public static final String TAG = "SoftSkin";
    private static final int sParameterTextId = 2131690167;
    private final int mIconId;
    private final int mTextId;
    private final float mValue;
    
    static {
        $VALUES = new SoftSkin[] { SoftSkin.ON, SoftSkin.OFF };
    }
    
    private SoftSkin(final int mIconId, final int mTextId, final float mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static SoftSkin getDefaultValue(final Context context, final CapturingMode capturingMode) {
        if (capturingMode.isFront()) {
            return valueOf(context.getResources().getString(2131690316));
        }
        return SoftSkin.ON;
    }
    
    public static SoftSkin[] getOptions(final CapturingMode capturingMode) {
        if (capturingMode != CapturingMode.FRONT_PHOTO && capturingMode != CapturingMode.SUPERIOR_FRONT) {
            return new SoftSkin[0];
        }
        if (PlatformCapability.isSoftSkinSupported(capturingMode.getCameraId())) {
            return new SoftSkin[] { SoftSkin.ON, SoftSkin.OFF };
        }
        return new SoftSkin[0];
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
        return UserSettingKey.SOFT_SKIN;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690167;
    }
    
    public int getLevel(final int n) {
        return (int)(this.mValue * n);
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public int getParameterkeyTitleTextId() {
        return 2131690168;
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
