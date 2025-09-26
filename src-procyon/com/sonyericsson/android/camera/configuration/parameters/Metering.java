// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Iterator;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;

public enum Metering implements UserSettingValue
{
    private static final Metering[] $VALUES;
    
    AVERAGE(-1, 2131689997, "frame-average"), 
    CENTER(-1, 2131689998, "center-weighted"), 
    FACE(-1, 2131689999, "face"), 
    MULTI(-1, 2131689947, "multi"), 
    SPOT(-1, 2131690000, "spot");
    
    public static final String TAG = "Metering";
    
    TOUCH(-1, 2131690002, "face");
    
    private static final int sParameterTextId = 2131690003;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new Metering[] { Metering.FACE, Metering.MULTI, Metering.CENTER, Metering.SPOT, Metering.AVERAGE, Metering.TOUCH };
    }
    
    private Metering(final int mIconId, final int mTextId, final String mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static Metering getDefaultValue(final CapturingMode capturingMode) {
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).METERING.get().contains(Metering.FACE.mValue)) {
            return Metering.FACE;
        }
        return Metering.CENTER;
    }
    
    public static Metering[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        final List list2 = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).METERING.get();
        if (!list2.isEmpty()) {
            if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.FRONT_PHOTO && capturingMode != CapturingMode.SUPERIOR_FRONT && capturingMode != CapturingMode.FRONT_VIDEO && capturingMode != CapturingMode.SLOW_MOTION && capturingMode != CapturingMode.VIDEO) {
                for (final Metering e : values()) {
                    if (isParameterSupported(e, list2)) {
                        list.add(e);
                    }
                }
            }
            else {
                list.add(getDefaultValue(capturingMode));
            }
        }
        return list.toArray(new Metering[0]);
    }
    
    private static boolean isParameterSupported(final Metering metering, final List<String> list) {
        if (metering == Metering.TOUCH) {
            return false;
        }
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (metering.getValue().equals(iterator.next())) {
                return true;
            }
        }
        return false;
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
        return UserSettingKey.METERING;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690003;
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
