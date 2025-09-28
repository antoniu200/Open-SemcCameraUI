package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FocusModeApplier extends DependencyApplier {
    public static final String TAG = "FocusModeApplier";
    private final FocusMode mValue;

    public FocusModeApplier(FocusMode focusMode) {
        this.mValue = focusMode;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        if (this.mValue == FocusMode.FACE_DETECTION) {
            return;
        }
        if (capturingModeParams.mShutterTrigger.get() == ShutterTrigger.SMILE_SHUTTER) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mShutterTrigger, ShutterTrigger.OFF);
        }
        ParameterUtil.applyRecommendedValue(capturingModeParams.mVideoShutterTrigger, VideoShutterTrigger.OFF);
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if (this.mValue == FocusMode.FACE_DETECTION) {
            return;
        }
        ParameterUtil.reset(capturingModeParams.mShutterTrigger);
        ParameterUtil.reset(capturingModeParams.mVideoShutterTrigger);
    }
}
