// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.ActionMode;

public enum CameraKey implements UserSettingValue
{
    private static final CameraKey[] $VALUES;
    
    BURST_SHOT(2131690116);
    
    public static final String TAG = "CameraKey";
    
    TAKE_PHOTO(2131690115);
    
    private static final int sParameterTextId = 2131689663;
    private final int mTextId;
    
    static {
        $VALUES = new CameraKey[] { CameraKey.BURST_SHOT, CameraKey.TAKE_PHOTO };
    }
    
    private CameraKey(final int mTextId) {
        this.mTextId = mTextId;
    }
    
    public static CameraKey getDefaultValue() {
        return CameraKey.TAKE_PHOTO;
    }
    
    public static CameraKey[] getOptions(final ActionMode actionMode) {
        if (actionMode.mIsOneShot) {
            return new CameraKey[] { CameraKey.TAKE_PHOTO };
        }
        return values();
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return -1;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.CAMERA_KEY;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689663;
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
