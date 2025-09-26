// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;
import java.util.ArrayList;
import com.sonyericsson.android.camera.ActionMode;

public enum PhotoLight implements UserSettingValue
{
    private static final PhotoLight[] $VALUES;
    
    OFF(2131231052, 2131690115, "off", false), 
    ON(2131231051, 2131690116, "torch", true);
    
    public static final String TAG = "PhotoLight";
    private static final int sParameterTextId = 2131689841;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new PhotoLight[] { PhotoLight.ON, PhotoLight.OFF };
    }
    
    private PhotoLight(final int mIconId, final int mTextId, final String mValue, final boolean mBooleanValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
        this.mBooleanValue = mBooleanValue;
    }
    
    public static PhotoLight[] getOptions(final ActionMode actionMode) {
        final ArrayList list = new ArrayList();
        final List list2 = PlatformCapability.getCameraCapability(actionMode.mCameraId).FLASH.get();
        if (!list2.isEmpty()) {
            for (final PhotoLight e : LedOptionsResolver.getInstance().getPhotoLightOptions(actionMode, list2)) {
                final Iterator iterator = list2.iterator();
                while (iterator.hasNext()) {
                    if (e.getValue().equals(iterator.next())) {
                        list.add(e);
                        break;
                    }
                }
            }
        }
        return list.toArray(new PhotoLight[0]);
    }
    
    public static PhotoLight getPhotoLightFromParameterString(final String anObject) {
        for (final PhotoLight photoLight : values()) {
            if (photoLight.getValue().equals(anObject)) {
                return photoLight;
            }
        }
        return null;
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public boolean getBooleanValue() {
        return this.mBooleanValue;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.PHOTO_LIGHT;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689841;
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
