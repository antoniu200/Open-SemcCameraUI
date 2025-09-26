// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;
import com.sonyericsson.android.camera.controller.GestureShutter;

public enum ShutterTrigger implements UserSettingValue
{
    private static final ShutterTrigger[] $VALUES;
    
    GESTURE_SHUTTER(2131689868, SmileCapture.OFF, true), 
    OFF(2131690115, SmileCapture.OFF, false), 
    SMILE_SHUTTER(2131690163, SmileCapture.MIDDLE, false);
    
    public static final String TAG = "ShutterTrigger";
    private static final int sParameterTextId = 2131690121;
    private boolean mGestureShutter;
    private SmileCapture mSmileCapture;
    private final int mTextId;
    
    static {
        $VALUES = new ShutterTrigger[] { ShutterTrigger.SMILE_SHUTTER, ShutterTrigger.GESTURE_SHUTTER, ShutterTrigger.OFF };
    }
    
    private ShutterTrigger(final int mTextId, final SmileCapture mSmileCapture, final boolean mGestureShutter) {
        this.mSmileCapture = mSmileCapture;
        this.mTextId = mTextId;
        this.mGestureShutter = mGestureShutter;
    }
    
    public static ShutterTrigger getDefaultValue(final CapturingMode capturingMode) {
        if ((capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT) && GestureShutter.isGestureShutterSupported()) {
            return ShutterTrigger.GESTURE_SHUTTER;
        }
        return ShutterTrigger.OFF;
    }
    
    public static ShutterTrigger[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).SMILE_DETECTION.get()) {
            list.add(ShutterTrigger.SMILE_SHUTTER);
        }
        if ((capturingMode == CapturingMode.FRONT_PHOTO || capturingMode == CapturingMode.SUPERIOR_FRONT) && GestureShutter.isGestureShutterSupported()) {
            list.add(ShutterTrigger.GESTURE_SHUTTER);
        }
        if (list.size() != 0) {
            list.add(ShutterTrigger.OFF);
            final ShutterTrigger[] a = new ShutterTrigger[list.size()];
            list.toArray(a);
            return a;
        }
        return new ShutterTrigger[0];
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
        return UserSettingKey.SHUTTER_TRIGGER;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690121;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public SmileCapture getSmileCapture() {
        return this.mSmileCapture;
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
    
    public boolean isGestureShutterOn() {
        return this.mGestureShutter;
    }
}
