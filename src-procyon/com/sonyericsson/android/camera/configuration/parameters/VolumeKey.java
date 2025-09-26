// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

public enum VolumeKey implements UserSettingValue
{
    private static final VolumeKey[] $VALUES;
    
    HW_CAMERA_KEY(-1, 2131690251);
    
    public static final String TAG = "VolumeKey";
    
    VOLUME(-1, 2131690253), 
    ZOOM(-1, 2131690254);
    
    private static final int sParameterTextId = 2131690252;
    private final int mIconId;
    private final int mTextId;
    private String mValue;
    
    static {
        $VALUES = new VolumeKey[] { VolumeKey.ZOOM, VolumeKey.VOLUME, VolumeKey.HW_CAMERA_KEY };
    }
    
    private VolumeKey(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static VolumeKey getDefault() {
        return VolumeKey.ZOOM;
    }
    
    public static VolumeKey[] getOptions() {
        final ArrayList list = new ArrayList();
        list.add(VolumeKey.ZOOM);
        list.add(VolumeKey.VOLUME);
        list.add(VolumeKey.HW_CAMERA_KEY);
        return list.toArray(new VolumeKey[0]);
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
        return UserSettingKey.VOLUME_KEY;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690252;
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
