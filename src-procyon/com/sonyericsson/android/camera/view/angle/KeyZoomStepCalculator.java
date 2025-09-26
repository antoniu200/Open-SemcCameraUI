// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.angle;

public class KeyZoomStepCalculator implements Calculator
{
    private static final int ZOOM_STEP_THRESHOLD_FOR_ZOOM_LEVER_1_MILLIS = 500;
    private static final int ZOOM_STEP_THRESHOLD_FOR_ZOOM_LEVER_2_MILLIS = 750;
    private static final int ZOOM_STEP_THRESHOLD_FOR_ZOOM_LEVER_3_MILLIS = 1000;
    final boolean mIsZoomIn;
    final long mZoomStartTimeMillis;
    
    public KeyZoomStepCalculator(final boolean mIsZoomIn) {
        this.mZoomStartTimeMillis = System.currentTimeMillis();
        this.mIsZoomIn = mIsZoomIn;
    }
    
    @Override
    public VariableIndex calculate(final VariableIndex variableIndex, final Object... array) {
        boolean b = false;
        final long longValue = (long)array[0];
        int mIndex;
        if (this.mIsZoomIn) {
            mIndex = variableIndex.mMaxIndex;
        }
        else {
            mIndex = variableIndex.mMinIndex;
        }
        if (variableIndex.mIndex == mIndex) {
            return variableIndex;
        }
        final long n = longValue - this.mZoomStartTimeMillis;
        int mIndex2 = variableIndex.mIndex;
        if (variableIndex.mIndex - mIndex < 0) {
            b = true;
        }
        if (0L <= n && n < 500L) {
            if (b) {
                ++mIndex2;
            }
            else {
                --mIndex2;
            }
        }
        else if (500L <= n && n < 750L) {
            if (b) {
                mIndex2 += 2;
            }
            else {
                mIndex2 -= 2;
            }
        }
        else if (750L <= n && n < 1000L) {
            if (b) {
                mIndex2 += 3;
            }
            else {
                mIndex2 -= 3;
            }
        }
        else if (b) {
            mIndex2 += 4;
        }
        else {
            mIndex2 -= 4;
        }
        if (!b || mIndex2 <= mIndex) {
            if (!b && variableIndex.mMinIndex > mIndex2) {
                mIndex = variableIndex.mMinIndex;
            }
            else {
                mIndex = mIndex2;
            }
        }
        variableIndex.mIndex = mIndex;
        return variableIndex;
    }
}
