// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;

public class FocusModeApplier extends DependencyApplier
{
    public static final String TAG = "FocusModeApplier";
    private final FocusMode mValue;
    
    public FocusModeApplier(final FocusMode mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (this.mValue == FocusMode.FACE_DETECTION) {
            return;
        }
        if (capturingModeParams.mShutterTrigger.get() == ShutterTrigger.SMILE_SHUTTER) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mShutterTrigger, ShutterTrigger.OFF);
        }
        ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if (this.mValue == FocusMode.FACE_DETECTION) {
            return;
        }
        ParameterUtil.reset(capturingModeParams.mShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
    }
}
