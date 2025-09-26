// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;
import java.util.ArrayList;

public enum Hdr implements UserSettingValue
{
    private static final Hdr[] $VALUES;
    
    HDR_AUTO(2131231054, 2131690116, "auto"), 
    HDR_OFF(2131231053, 2131690115, "off"), 
    HDR_ON(2131231054, 2131690116, "on-still-hdr");
    
    public static final String TAG = "Hdr";
    private static final int sParameterTextId = 2131689871;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new Hdr[] { Hdr.HDR_ON, Hdr.HDR_AUTO, Hdr.HDR_OFF };
    }
    
    private Hdr(final int mIconId, final int mTextId, final String mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static Hdr getDefault(final CapturingMode capturingMode) {
        if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.SUPERIOR_FRONT) {
            return Hdr.HDR_OFF;
        }
        return Hdr.HDR_AUTO;
    }
    
    public static Hdr[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        final List list2 = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).HDR.get();
        if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.SUPERIOR_FRONT) {
            if (capturingMode.getType() == 1 && list2.contains(Hdr.HDR_ON.getValue())) {
                list.add(Hdr.HDR_ON);
                list.add(Hdr.HDR_OFF);
            }
        }
        else if (list2.contains(Hdr.HDR_AUTO.getValue())) {
            list.add(Hdr.HDR_AUTO);
        }
        return list.toArray(new Hdr[0]);
    }
    
    public static boolean isResolutionIndependentHdrSupported(final List<String> list) {
        return list.contains("hdr");
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
        return UserSettingKey.HDR;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689871;
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
