// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;

public class ShutterTriggerApplier extends DependencyApplier
{
    private final ShutterTrigger mValue;
    
    public ShutterTriggerApplier(final ShutterTrigger mValue) {
        this.mValue = mValue;
    }
    
    private void applyGestureShutterOff(final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }
    
    private void applyGestureShutterOn(final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        }
    }
    
    private void applySmileCaptureOff(final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }
    
    private void applySmileCaptureOn(final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        }
    }
    
    private void resetSmileShutterOn(final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.getDefaultValue() == CapturingMode.NORMAL) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        switch (ShutterTriggerApplier$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$ShutterTrigger[this.mValue.ordinal()]) {
            default: {
                this.applySmileCaptureOff(capturingModeParams);
                this.applyGestureShutterOff(capturingModeParams);
                break;
            }
            case 2: {
                this.applySmileCaptureOff(capturingModeParams);
                this.applyGestureShutterOn(capturingModeParams);
                break;
            }
            case 1: {
                this.applySmileCaptureOn(capturingModeParams);
                this.applyGestureShutterOff(capturingModeParams);
                break;
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        switch (ShutterTriggerApplier$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$ShutterTrigger[this.mValue.ordinal()]) {
            case 2: {
                this.applySmileCaptureOff(capturingModeParams);
                break;
            }
            case 1: {
                this.resetSmileShutterOn(capturingModeParams);
                break;
            }
        }
    }
}
