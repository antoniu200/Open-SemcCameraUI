package com.sonyericsson.android.camera.view.angle;

import com.sonyericsson.android.camera.view.angle.VariableIndex;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FrontAngleChangeCalculator implements VariableIndex.Calculator {
    public static final int ZOOM_CHANGE_ANGLE_LOOP_COUNT = 1;
    private int mStepInterval = -1;

    @Override // com.sonyericsson.android.camera.view.angle.VariableIndex.Calculator
    public VariableIndex calculate(VariableIndex variableIndex, Object... objArr) {
        int iIntValue = ((Integer) objArr[0]).intValue();
        if (this.mStepInterval == -1) {
            if (iIntValue == variableIndex.mMinIndex) {
                this.mStepInterval = variableIndex.mIndex / 1;
            } else {
                this.mStepInterval = iIntValue / 1;
            }
        }
        int i = variableIndex.mIndex;
        boolean z = i - iIntValue < variableIndex.mMinIndex;
        int i2 = z ? i + this.mStepInterval : i - this.mStepInterval;
        if (!z || i2 <= iIntValue) {
            iIntValue = (z || variableIndex.mMinIndex <= i2) ? i2 : variableIndex.mMinIndex;
        }
        variableIndex.mIndex = iIntValue;
        return variableIndex;
    }
}
