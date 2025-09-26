// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.angle;

import android.util.DisplayMetrics;
import android.content.res.Resources;

public class PinchZoomStepCalculator implements Calculator
{
    private static final float PINCH_ZOOM_COEFFICIENT = 0.7f;
    
    @Override
    public VariableIndex calculate(final VariableIndex variableIndex, final Object... array) {
        final float floatValue = (float)array[0];
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        final float n = variableIndex.mIndex + floatValue * variableIndex.mMaxIndex / (Math.min(displayMetrics.heightPixels, displayMetrics.widthPixels) * 0.7f);
        float a;
        if (n < variableIndex.mMinIndex) {
            a = (float)variableIndex.mMinIndex;
        }
        else {
            a = n;
            if (n > variableIndex.mMaxIndex) {
                a = (float)variableIndex.mMaxIndex;
            }
        }
        variableIndex.mIndex = Math.round(a);
        return variableIndex;
    }
}
