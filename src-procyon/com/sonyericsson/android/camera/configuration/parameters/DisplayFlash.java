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

public enum DisplayFlash implements UserSettingValue
{
    private static final DisplayFlash[] $VALUES;
    
    DISPLAY_AUTO(2131231047, 2131690114, "display-auto"), 
    DISPLAY_OFF(2131231049, 2131690115, "off"), 
    DISPLAY_ON(2131231048, 2131689836, "display-on");
    
    public static final String TAG = "DisplayFlash";
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new DisplayFlash[] { DisplayFlash.DISPLAY_AUTO, DisplayFlash.DISPLAY_ON, DisplayFlash.DISPLAY_OFF };
    }
    
    private DisplayFlash(final int mIconId, final int mTextId, final String mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static DisplayFlash getDefaultValue() {
        return LedOptionsResolver.getInstance().getDefaultDisplayFlash();
    }
    
    public static DisplayFlash getDisplayFlashFromParameterString(final String anObject) {
        for (final DisplayFlash displayFlash : values()) {
            if (displayFlash.getValue().equals(anObject)) {
                return displayFlash;
            }
        }
        return null;
    }
    
    public static DisplayFlash[] getOptions(final ActionMode actionMode) {
        final ArrayList list = new ArrayList();
        if (actionMode.mType == 1) {
            final List list2 = PlatformCapability.getCameraCapability(actionMode.mCameraId).FLASH.get();
            if (!list2.isEmpty()) {
                for (final DisplayFlash e : LedOptionsResolver.getInstance().getDisplayFlashOptions(actionMode, list2)) {
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
        return list.toArray(new DisplayFlash[0]);
    }
    
    public static int getParameterKeyTitleTextId() {
        return LedOptionsResolver.getInstance().getParameterKeyTitleTextId();
    }
    
    public static boolean isSupported(final CameraInfo.CameraId cameraId) {
        return PlatformCapability.isDisplayFlashModeSupported(cameraId);
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
        return UserSettingKey.DISPLAY_FLASH;
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
}
