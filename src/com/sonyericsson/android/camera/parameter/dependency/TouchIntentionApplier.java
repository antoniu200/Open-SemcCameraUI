package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TouchIntentionApplier extends DependencyApplier {
    public static final String TAG = "TouchIntentionApplier";
    private final TouchIntention mValue;

    public TouchIntentionApplier(TouchIntention touchIntention) {
        this.mValue = touchIntention;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.SCENE_RECOGNITION) {
            if (capturingModeParams.mTouchIntention.get() == TouchIntention.FOCUS_AND_EXPOSURE && capturingModeParams.mObjectTracking.get() == ObjectTracking.ON) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
                return;
            }
            return;
        }
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            if (capturingModeParams.mTouchIntention.get() == TouchIntention.OBJECT_TRACKING) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mObjectTracking, ObjectTracking.ON);
            } else {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
            }
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }
}
