// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;
import java.util.ArrayList;

public enum WhiteBalance implements UserSettingValue
{
    private static final WhiteBalance[] $VALUES;
    
    AUTO(2131231082, 2131690255, "auto", 2131231089), 
    CLOUDY_DAYLIGHT(2131231085, 2131690256, "cloudy-daylight", 2131231090), 
    DAYLIGHT(2131231086, 2131690257, "daylight", 2131231091), 
    FLUORESCENT(2131231087, 2131690258, "fluorescent", 2131231092), 
    INCANDESCENT(2131231088, 2131690259, "incandescent", 2131231093);
    
    public static final String TAG = "WhiteBalance";
    private static final int sParameterTextId = 2131690261;
    private final int mIconId;
    private final int mTabIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new WhiteBalance[] { WhiteBalance.INCANDESCENT, WhiteBalance.FLUORESCENT, WhiteBalance.DAYLIGHT, WhiteBalance.CLOUDY_DAYLIGHT, WhiteBalance.AUTO };
    }
    
    private WhiteBalance(final int mIconId, final int mTextId, final String mValue, final int mTabIconId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
        this.mTabIconId = mTabIconId;
    }
    
    public static WhiteBalance[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        final List list2 = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).WHITE_BALANCE.get();
        if (!list2.isEmpty()) {
            if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.SUPERIOR_FRONT) {
                for (final WhiteBalance e : values()) {
                    final Iterator iterator = list2.iterator();
                    while (iterator.hasNext()) {
                        if (e.getValue().equals(iterator.next())) {
                            list.add(e);
                            break;
                        }
                    }
                }
            }
            else {
                list.add(WhiteBalance.AUTO);
            }
        }
        return list.toArray(new WhiteBalance[0]);
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
        return UserSettingKey.WHITE_BALANCE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690261;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public int getTabIconId() {
        return this.mTabIconId;
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
