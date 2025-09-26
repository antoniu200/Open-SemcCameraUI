// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.CameraCapabilityList;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;

public enum TouchIntention implements UserSettingValue
{
    private static final TouchIntention[] $VALUES;
    
    FOCUS_AND_EXPOSURE(-1, 2131690196), 
    FOCUS_ONLY(-1, 2131690197), 
    OBJECT_TRACKING(-1, 2131689847);
    
    public static final String TAG = "TouchIntention";
    private static final int sParameterTextId = 2131690198;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new TouchIntention[] { TouchIntention.OBJECT_TRACKING, TouchIntention.FOCUS_ONLY, TouchIntention.FOCUS_AND_EXPOSURE };
    }
    
    private TouchIntention(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static TouchIntention getDefaultValue(final CapturingMode capturingMode) {
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).METERING.get().contains("user")) {
            return TouchIntention.FOCUS_AND_EXPOSURE;
        }
        return TouchIntention.FOCUS_ONLY;
    }
    
    public static TouchIntention[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        if (capturingMode != CapturingMode.SCENE_RECOGNITION && capturingMode != CapturingMode.NORMAL) {
            return list.toArray(new TouchIntention[0]);
        }
        final CameraCapabilityList cameraCapability = PlatformCapability.getCameraCapability(capturingMode.getCameraId());
        if (cameraCapability.METERING.get().contains("user")) {
            if (capturingMode == CapturingMode.NORMAL && cameraCapability.OBJECT_TRACKING.get()) {
                list.add(TouchIntention.OBJECT_TRACKING);
            }
            list.add(TouchIntention.FOCUS_ONLY);
            list.add(TouchIntention.FOCUS_AND_EXPOSURE);
        }
        return list.toArray(new TouchIntention[0]);
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
        return UserSettingKey.TOUCH_INTENTION;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690198;
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
