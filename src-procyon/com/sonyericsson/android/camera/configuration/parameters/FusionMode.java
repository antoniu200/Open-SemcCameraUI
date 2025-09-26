// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.List;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

public enum FusionMode implements UserSettingValue
{
    private static final FusionMode[] $VALUES;
    
    AUTO(2131231097, 2131690114, "auto"), 
    OFF(2131231102, 2131690115, "off"), 
    ON(2131231097, 2131690116, "on");
    
    private static final String TAG = "FusionMode";
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new FusionMode[] { FusionMode.OFF, FusionMode.ON, FusionMode.AUTO };
    }
    
    private FusionMode(final int mIconId, final int mTextId, final String mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static FusionMode getDefaultValue(final CapturingMode capturingMode) {
        if (PlatformCapability.isHighSensitivityFusionSupported(capturingMode.getCameraId())) {
            final List list = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).FUSION_MODE.get();
            if (FusionMode$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()] == 2) {
                if (list.contains("auto")) {
                    return FusionMode.AUTO;
                }
            }
        }
        return FusionMode.OFF;
    }
    
    public static FusionMode[] getOptions(final CapturingMode capturingMode) {
        if (PlatformCapability.isHighSensitivityFusionSupported(capturingMode.getCameraId())) {
            final List list = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).FUSION_MODE.get();
            switch (FusionMode$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
                case 3: {
                    if (list.contains("on")) {
                        return new FusionMode[] { FusionMode.OFF, FusionMode.ON };
                    }
                    break;
                }
                case 1:
                case 2: {
                    if (list.contains("auto")) {
                        return new FusionMode[] { FusionMode.AUTO, FusionMode.OFF };
                    }
                    break;
                }
            }
        }
        return new FusionMode[] { FusionMode.OFF };
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
        return UserSettingKey.FUSION_MODE;
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
        return this.mValue;
    }
}
