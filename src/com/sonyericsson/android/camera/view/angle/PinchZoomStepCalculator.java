package com.sonyericsson.android.camera.view.angle;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.sonyericsson.android.camera.view.angle.VariableIndex;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PinchZoomStepCalculator implements VariableIndex.Calculator {
    private static final float PINCH_ZOOM_COEFFICIENT = 0.7f;

    @Override // com.sonyericsson.android.camera.view.angle.VariableIndex.Calculator
    public VariableIndex calculate(VariableIndex variableIndex, Object... objArr) {
        float fFloatValue = ((Float) objArr[0]).floatValue();
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        float fMin = variableIndex.mIndex + ((fFloatValue * variableIndex.mMaxIndex) / (Math.min(displayMetrics.heightPixels, displayMetrics.widthPixels) * PINCH_ZOOM_COEFFICIENT));
        if (fMin < variableIndex.mMinIndex) {
            fMin = variableIndex.mMinIndex;
        } else if (fMin > variableIndex.mMaxIndex) {
            fMin = variableIndex.mMaxIndex;
        }
        variableIndex.mIndex = Math.round(fMin);
        return variableIndex;
    }
}
