package com.sonyericsson.android.camera.view.angle;

import com.sonyericsson.android.camera.view.angle.VariableIndex;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class KeyZoomStepCalculator implements VariableIndex.Calculator {
    private static final int ZOOM_STEP_THRESHOLD_FOR_ZOOM_LEVER_1_MILLIS = 500;
    private static final int ZOOM_STEP_THRESHOLD_FOR_ZOOM_LEVER_2_MILLIS = 750;
    private static final int ZOOM_STEP_THRESHOLD_FOR_ZOOM_LEVER_3_MILLIS = 1000;
    final boolean mIsZoomIn;
    final long mZoomStartTimeMillis = System.currentTimeMillis();

    public KeyZoomStepCalculator(boolean z) {
        this.mIsZoomIn = z;
    }

    @Override // com.sonyericsson.android.camera.view.angle.VariableIndex.Calculator
    public VariableIndex calculate(VariableIndex variableIndex, Object... objArr) {
        long jLongValue = ((Long) objArr[0]).longValue();
        int i = this.mIsZoomIn ? variableIndex.mMaxIndex : variableIndex.mMinIndex;
        if (variableIndex.mIndex == i) {
            return variableIndex;
        }
        long j = jLongValue - this.mZoomStartTimeMillis;
        int i2 = variableIndex.mIndex;
        boolean z = variableIndex.mIndex - i < 0;
        int i3 = (0 > j || j >= 500) ? (500 > j || j >= 750) ? (750 > j || j >= 1000) ? z ? i2 + 4 : i2 - 4 : z ? i2 + 3 : i2 - 3 : z ? i2 + 2 : i2 - 2 : z ? i2 + 1 : i2 - 1;
        if (!z || i3 <= i) {
            i = (z || variableIndex.mMinIndex <= i3) ? i3 : variableIndex.mMinIndex;
        }
        variableIndex.mIndex = i;
        return variableIndex;
    }
}
