package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ObjectTrackingApplier extends DependencyApplier {
    public static final String TAG = "ObjectTrackingApplier";
    private final ObjectTracking mValue;

    public ObjectTrackingApplier(ObjectTracking objectTracking) {
        this.mValue = objectTracking;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        if (this.mValue == ObjectTracking.ON) {
            if ((capturingModeParams.mCapturingMode.get() == CapturingMode.SCENE_RECOGNITION || capturingModeParams.mCapturingMode.get() == CapturingMode.SUPERIOR_FRONT) && capturingModeParams.mTouchIntention.get() == TouchIntention.FOCUS_AND_EXPOSURE) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mTouchIntention, TouchIntention.FOCUS_ONLY);
                return;
            }
            return;
        }
        CapturingMode capturingMode = (CapturingMode) capturingModeParams.mCapturingMode.get();
        CameraInfo.CameraId cameraId = capturingMode.getCameraId();
        if (capturingMode == CapturingMode.SCENE_RECOGNITION && PlatformCapability.isTouchFocusSupported(cameraId) && PlatformCapability.isTouchAeSupported(cameraId)) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mTouchIntention, TouchIntention.FOCUS_AND_EXPOSURE);
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if (this.mValue == ObjectTracking.OFF) {
            return;
        }
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.SCENE_RECOGNITION || capturingModeParams.mCapturingMode.get() == CapturingMode.SUPERIOR_FRONT) {
            ParameterUtil.reset(capturingModeParams.mTouchIntention);
        }
    }
}
