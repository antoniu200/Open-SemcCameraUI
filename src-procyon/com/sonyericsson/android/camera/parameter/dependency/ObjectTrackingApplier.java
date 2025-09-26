// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;

public class ObjectTrackingApplier extends DependencyApplier
{
    public static final String TAG = "ObjectTrackingApplier";
    private final ObjectTracking mValue;
    
    public ObjectTrackingApplier(final ObjectTracking mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (this.mValue == ObjectTracking.ON) {
            if ((capturingModeParams.mCapturingMode.get() == CapturingMode.SCENE_RECOGNITION || capturingModeParams.mCapturingMode.get() == CapturingMode.SUPERIOR_FRONT) && capturingModeParams.mTouchIntention.get() == TouchIntention.FOCUS_AND_EXPOSURE) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mTouchIntention, TouchIntention.FOCUS_ONLY);
            }
        }
        else {
            final CapturingMode capturingMode = capturingModeParams.mCapturingMode.get();
            final CameraInfo.CameraId cameraId = capturingMode.getCameraId();
            if (capturingMode == CapturingMode.SCENE_RECOGNITION && PlatformCapability.isTouchFocusSupported(cameraId) && PlatformCapability.isTouchAeSupported(cameraId)) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mTouchIntention, TouchIntention.FOCUS_AND_EXPOSURE);
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if (this.mValue == ObjectTracking.OFF) {
            return;
        }
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.SCENE_RECOGNITION || capturingModeParams.mCapturingMode.get() == CapturingMode.SUPERIOR_FRONT) {
            ParameterUtil.reset(capturingModeParams.mTouchIntention);
        }
    }
}
