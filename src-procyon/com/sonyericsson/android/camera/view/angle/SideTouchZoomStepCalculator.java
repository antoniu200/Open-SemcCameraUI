// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.angle;

public class SideTouchZoomStepCalculator implements Calculator
{
    private static final int ZOOM_STEP_THRESHOLD_FOR_SIDE_TOUCH_1_DISTANCE = 100;
    private static final int ZOOM_STEP_THRESHOLD_FOR_SIDE_TOUCH_2_DISTANCE = 200;
    private static final int ZOOM_STEP_THRESHOLD_FOR_SIDE_TOUCH_3_DISTANCE = 300;
    
    private int getIncrementDirection(int n) {
        if (n > 0) {
            n = 1;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    private int getIncrementLength(final int a) {
        if (Math.abs(a) < 100) {
            return 0;
        }
        if (100 <= Math.abs(a) && Math.abs(a) < 200) {
            return 1;
        }
        if (200 <= Math.abs(a) && Math.abs(a) < 300) {
            return 3;
        }
        return 5;
    }
    
    @Override
    public VariableIndex calculate(final VariableIndex variableIndex, final Object... array) {
        final int intValue = (int)array[0];
        variableIndex.setIndex(variableIndex.mIndex + this.getIncrementDirection(intValue) * this.getIncrementLength(intValue));
        return variableIndex;
    }
}
