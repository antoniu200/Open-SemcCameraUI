// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.angle;

public class FrontAngleChangeCalculator implements Calculator
{
    public static final int ZOOM_CHANGE_ANGLE_LOOP_COUNT = 1;
    private int mStepInterval;
    
    public FrontAngleChangeCalculator() {
        this.mStepInterval = -1;
    }
    
    @Override
    public VariableIndex calculate(final VariableIndex variableIndex, final Object... array) {
        boolean b = false;
        final int intValue = (int)array[0];
        if (this.mStepInterval == -1) {
            if (intValue == variableIndex.mMinIndex) {
                this.mStepInterval = variableIndex.mIndex / 1;
            }
            else {
                this.mStepInterval = intValue / 1;
            }
        }
        final int mIndex = variableIndex.mIndex;
        if (mIndex - intValue < variableIndex.mMinIndex) {
            b = true;
        }
        int mMinIndex;
        if (b) {
            mMinIndex = mIndex + this.mStepInterval;
        }
        else {
            mMinIndex = mIndex - this.mStepInterval;
        }
        if (b && mMinIndex > intValue) {
            mMinIndex = intValue;
        }
        else if (!b && variableIndex.mMinIndex > mMinIndex) {
            mMinIndex = variableIndex.mMinIndex;
        }
        variableIndex.mIndex = mMinIndex;
        return variableIndex;
    }
}
