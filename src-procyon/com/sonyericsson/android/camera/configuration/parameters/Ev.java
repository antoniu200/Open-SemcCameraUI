// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

public enum Ev implements UserSettingValue
{
    private static final Ev[] $VALUES;
    
    M1_3(2131231032, 2131689877, -0.3f), 
    M2_3(2131231032, 2131689878, -0.7f), 
    M3_3(2131231032, 2131689879, -1.0f), 
    M4_3(2131231032, 2131689880, -1.3f), 
    M5_3(2131231032, 2131689881, -1.7f), 
    M6_3(2131231032, 2131689882, -2.0f), 
    P1_3(2131231032, 2131689883, 0.3f), 
    P2_3(2131231032, 2131689884, 0.7f), 
    P3_3(2131231032, 2131689885, 1.0f), 
    P4_3(2131231032, 2131689886, 1.3f), 
    P5_3(2131231032, 2131689887, 1.7f), 
    P6_3(2131231032, 2131689888, 2.0f);
    
    public static final String TAG = "Ev";
    
    ZERO(2131231031, 2131689889, 0.0f);
    
    private static final int sParameterTextId = 2131689812;
    private final int mIconId;
    private int mIndex;
    private final int mTextId;
    private final float mValue;
    
    static {
        $VALUES = new Ev[] { Ev.M6_3, Ev.M5_3, Ev.M4_3, Ev.M3_3, Ev.M2_3, Ev.M1_3, Ev.ZERO, Ev.P1_3, Ev.P2_3, Ev.P3_3, Ev.P4_3, Ev.P5_3, Ev.P6_3 };
    }
    
    private Ev(final int mIconId, final int mTextId, final float mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static Ev[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        final int intValue = cameraCapability.EV_MAX.get();
        final int intValue2 = cameraCapability.EV_MIN.get();
        if (intValue != 0 || intValue2 != 0) {
            final float floatValue = cameraCapability.EV_STEP.get();
            for (final Ev e : values()) {
                for (int j = intValue2; j <= intValue; ++j) {
                    if ((int)(j * floatValue * 10.0f + 0.5) == (int)(e.mValue * 10.0f + 0.5)) {
                        e.mIndex = j;
                        list.add(e);
                        break;
                    }
                }
            }
        }
        return list.toArray(new Ev[0]);
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    public int getIntValue() {
        return this.mIndex;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.EV;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689812;
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
