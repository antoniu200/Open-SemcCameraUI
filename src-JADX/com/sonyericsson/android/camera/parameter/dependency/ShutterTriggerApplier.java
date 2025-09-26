package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ShutterTriggerApplier extends DependencyApplier {
    private final ShutterTrigger mValue;

    public ShutterTriggerApplier(ShutterTrigger shutterTrigger) {
        this.mValue = shutterTrigger;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        switch (this.mValue) {
            case SMILE_SHUTTER:
                applySmileCaptureOn(capturingModeParams);
                applyGestureShutterOff(capturingModeParams);
                break;
            case GESTURE_SHUTTER:
                applySmileCaptureOff(capturingModeParams);
                applyGestureShutterOn(capturingModeParams);
                break;
            default:
                applySmileCaptureOff(capturingModeParams);
                applyGestureShutterOff(capturingModeParams);
                break;
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        switch (this.mValue) {
            case SMILE_SHUTTER:
                resetSmileShutterOn(capturingModeParams);
                break;
            case GESTURE_SHUTTER:
                applySmileCaptureOff(capturingModeParams);
                break;
        }
    }

    private void applySmileCaptureOn(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        }
    }

    private void applySmileCaptureOff(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }

    private void applyGestureShutterOn(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        }
    }

    private void applyGestureShutterOff(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }

    private void resetSmileShutterOn(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.getDefaultValue() == CapturingMode.NORMAL) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }
}
