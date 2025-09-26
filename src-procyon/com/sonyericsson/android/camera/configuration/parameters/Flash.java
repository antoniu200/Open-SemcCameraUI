// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;
import java.util.ArrayList;
import com.sonyericsson.android.camera.ActionMode;

public enum Flash implements UserSettingValue
{
    private static final Flash[] $VALUES;
    
    AUTO(2131231047, 2131690114, "auto", true), 
    LED_OFF(2131231052, 2131690115, "off", false), 
    LED_ON(2131231051, 2131689841, "torch", false), 
    OFF(2131231049, 2131690115, "off", false), 
    ON(2131231048, 2131689836, "on", false), 
    PHOTO_LIGHT_ON_AS_FLASH(PhotoLight.ON.getIconId(), PhotoLight.ON.getTextId(), PhotoLight.ON.getValue(), false), 
    RED_EYE(2131231050, 2131689839, "red-eye", true);
    
    public static final String TAG = "Flash";
    private final int mIconId;
    private final boolean mIsSceneDependent;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new Flash[] { Flash.AUTO, Flash.ON, Flash.RED_EYE, Flash.OFF, Flash.LED_ON, Flash.LED_OFF, Flash.PHOTO_LIGHT_ON_AS_FLASH };
    }
    
    private Flash(final int mIconId, final int mTextId, final String mValue, final boolean mIsSceneDependent) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
        this.mIsSceneDependent = mIsSceneDependent;
    }
    
    public static Flash getDefaultValue() {
        return LedOptionsResolver.getInstance().getDefaultFlash();
    }
    
    public static Flash getFlashFromParameterString(final String anObject) {
        for (final Flash flash : values()) {
            if (flash.getValue().equals(anObject)) {
                return flash;
            }
        }
        return null;
    }
    
    public static Flash[] getOptions(final ActionMode actionMode) {
        final ArrayList list = new ArrayList();
        if (actionMode.mType == 1) {
            final List list2 = PlatformCapability.getCameraCapability(actionMode.mCameraId).FLASH.get();
            if (!list2.isEmpty()) {
                for (final Flash e : LedOptionsResolver.getInstance().getFlashOptions(actionMode, list2)) {
                    final Iterator iterator = list2.iterator();
                    while (iterator.hasNext()) {
                        if (e.getValue().equals(iterator.next())) {
                            list.add(e);
                            break;
                        }
                    }
                }
            }
        }
        return list.toArray(new Flash[0]);
    }
    
    public static int getParameterKeyTitleTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTitleTextId();
    }
    
    public static boolean isSupported(final CameraInfo.CameraId cameraId) {
        return PlatformCapability.isFlashModeSupported(cameraId);
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
        return UserSettingKey.FLASH;
    }
    
    @Override
    public int getKeyTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTextId();
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
    
    public boolean isSceneDependent() {
        return this.mIsSceneDependent;
    }
}
