// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;

public class VideoShutterTriggerApplier extends DependencyApplier
{
    public static final String TAG = "VideoShutterTriggerApplier";
    private final VideoShutterTrigger mValue;
    
    public VideoShutterTriggerApplier(final VideoShutterTrigger mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (this.mValue != VideoShutterTrigger.OFF) {
            if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
            }
        }
        else if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if (this.mValue != VideoShutterTrigger.OFF && capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }
}
