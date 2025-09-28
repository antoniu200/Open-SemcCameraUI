package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoShutterTriggerApplier extends DependencyApplier {
    public static final String TAG = "VideoShutterTriggerApplier";
    private final VideoShutterTrigger mValue;

    public VideoShutterTriggerApplier(VideoShutterTrigger videoShutterTrigger) {
        this.mValue = videoShutterTrigger;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        if (this.mValue != VideoShutterTrigger.OFF) {
            if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
            }
        } else if (capturingModeParams.mCapturingMode.get() == CapturingMode.VIDEO) {
            ParameterUtil.reset(capturingModeParams.mFocusMode);
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if (this.mValue == VideoShutterTrigger.OFF || capturingModeParams.mCapturingMode.get() != CapturingMode.VIDEO) {
            return;
        }
        ParameterUtil.reset(capturingModeParams.mFocusMode);
    }
}
