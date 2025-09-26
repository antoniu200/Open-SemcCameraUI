// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;

public class TouchIntentionApplier extends DependencyApplier
{
    public static final String TAG = "TouchIntentionApplier";
    private final TouchIntention mValue;
    
    public TouchIntentionApplier(final TouchIntention mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() == CapturingMode.SCENE_RECOGNITION) {
            if (capturingModeParams.mTouchIntention.get() == TouchIntention.FOCUS_AND_EXPOSURE && capturingModeParams.mObjectTracking.get() == ObjectTracking.ON) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
            }
        }
        else if (capturingModeParams.mCapturingMode.get() == CapturingMode.NORMAL) {
            if (capturingModeParams.mTouchIntention.get() == TouchIntention.OBJECT_TRACKING) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mObjectTracking, ObjectTracking.ON);
            }
            else {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mObjectTracking, ObjectTracking.OFF);
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        ParameterUtil.reset(capturingModeParams.mObjectTracking);
    }
}
