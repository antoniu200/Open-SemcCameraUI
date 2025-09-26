// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import java.util.List;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

public enum ShutterSpeed implements UserSettingValue
{
    private static final ShutterSpeed[] $VALUES;
    
    AUTO(-1, 2131689894, "auto", -1), 
    D1(-1, 2131689893, "shutter-prio", 1), 
    D1000(-1, 2131689895, "shutter-prio", 1000), 
    D125(-1, 2131689896, "shutter-prio", 125), 
    D15(-1, 2131689897, "shutter-prio", 15), 
    D2(-1, 2131689892, "shutter-prio", 2), 
    D2000(-1, 2131689898, "shutter-prio", 2000), 
    D250(-1, 2131689899, "shutter-prio", 250), 
    D30(-1, 2131689900, "shutter-prio", 30), 
    D4(-1, 2131689902, "shutter-prio", 4), 
    D4000(-1, 2131689901, "shutter-prio", 4000), 
    D500(-1, 2131689903, "shutter-prio", 500), 
    D60(-1, 2131689904, "shutter-prio", 60), 
    D8(-1, 2131689905, "shutter-prio", 8);
    
    private static final int INVALID_VALUE = -1;
    private static int mIndexOfDefault = 1;
    private final String mAeMode;
    private final int mIconId;
    private final int mTextId;
    private final int mValue;
    
    static {
        $VALUES = new ShutterSpeed[] { ShutterSpeed.AUTO, ShutterSpeed.D1, ShutterSpeed.D2, ShutterSpeed.D4, ShutterSpeed.D8, ShutterSpeed.D15, ShutterSpeed.D30, ShutterSpeed.D60, ShutterSpeed.D125, ShutterSpeed.D250, ShutterSpeed.D500, ShutterSpeed.D1000, ShutterSpeed.D2000, ShutterSpeed.D4000 };
    }
    
    private ShutterSpeed(final int mIconId, final int mTextId, final String mAeMode, final int mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mAeMode = mAeMode;
        this.mValue = mValue;
    }
    
    public static int getIndexOfDefault() {
        return ShutterSpeed.mIndexOfDefault;
    }
    
    public static ShutterSpeed[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        final List list2 = cameraCapability.AE.get();
        if (list2.isEmpty()) {
            return list.toArray(new ShutterSpeed[0]);
        }
        if (list2.contains(ShutterSpeed.AUTO.getValue())) {
            list.add(ShutterSpeed.AUTO);
            if (capturingMode == CapturingMode.SCENE_RECOGNITION || capturingMode == CapturingMode.SUPERIOR_FRONT || capturingMode == CapturingMode.FRONT_PHOTO || capturingMode.getType() == 2) {
                return list.toArray(new ShutterSpeed[0]);
            }
        }
        if (!list2.contains("shutter-prio")) {
            return list.toArray(new ShutterSpeed[0]);
        }
        final List list3 = cameraCapability.SHUTTER_SPEED_VALUES.get();
        for (final ShutterSpeed e : values()) {
            if (list3.contains(Long.toString(e.getShutterSpeedInNanoMillis()))) {
                list.add(e);
                if (e.getShutterSpeedValue() <= ShutterSpeed.D125.getShutterSpeedValue()) {
                    ShutterSpeed.mIndexOfDefault = list.size() - 1;
                }
            }
        }
        return list.toArray(new ShutterSpeed[0]);
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
        return UserSettingKey.SHUTTER_SPEED;
    }
    
    @Override
    public int getKeyTextId() {
        return 0;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public long getShutterSpeedInNanoMillis() {
        return 1000000000 / this.mValue;
    }
    
    public int getShutterSpeedValue() {
        return this.mValue;
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.mAeMode;
    }
}
