// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

public enum ObjectTracking implements UserSettingValue
{
    private static final ObjectTracking[] $VALUES;
    
    OFF(-1, 2131690115, FocusMode.FACE_DETECTION), 
    ON(-1, 2131690116, FocusMode.OBJECT_TRACKING);
    
    public static final String TAG = "ObjectTracking";
    private static final int sParameterTextId = 2131689847;
    private final FocusMode mFocusMode;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new ObjectTracking[] { ObjectTracking.ON, ObjectTracking.OFF };
    }
    
    private ObjectTracking(final int mIconId, final int mTextId, final FocusMode mFocusMode) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mFocusMode = mFocusMode;
    }
    
    public static ObjectTracking getDefault(final CapturingMode capturingMode) {
        if (!PlatformCapability.getCameraCapability(capturingMode.getCameraId()).OBJECT_TRACKING.get()) {
            return ObjectTracking.OFF;
        }
        if (capturingMode == CapturingMode.VIDEO) {
            return ObjectTracking.ON;
        }
        return ObjectTracking.OFF;
    }
    
    public static ObjectTracking[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).OBJECT_TRACKING.get()) {
            list.add(ObjectTracking.ON);
        }
        list.add(ObjectTracking.OFF);
        return list.toArray(new ObjectTracking[0]);
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public FocusMode getFocusMode() {
        return this.mFocusMode;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.OBJECT_TRACKING;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689847;
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
