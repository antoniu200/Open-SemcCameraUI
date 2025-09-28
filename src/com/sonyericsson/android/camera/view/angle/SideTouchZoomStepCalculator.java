package com.sonyericsson.android.camera.view.angle;

import com.sonyericsson.android.camera.view.angle.VariableIndex;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SideTouchZoomStepCalculator implements VariableIndex.Calculator {
    private static final int ZOOM_STEP_THRESHOLD_FOR_SIDE_TOUCH_1_DISTANCE = 100;
    private static final int ZOOM_STEP_THRESHOLD_FOR_SIDE_TOUCH_2_DISTANCE = 200;
    private static final int ZOOM_STEP_THRESHOLD_FOR_SIDE_TOUCH_3_DISTANCE = 300;

    private int getIncrementDirection(int i) {
        return i > 0 ? 1 : -1;
    }

    @Override // com.sonyericsson.android.camera.view.angle.VariableIndex.Calculator
    public VariableIndex calculate(VariableIndex variableIndex, Object... objArr) {
        int iIntValue = ((Integer) objArr[0]).intValue();
        variableIndex.setIndex(variableIndex.mIndex + (getIncrementDirection(iIntValue) * getIncrementLength(iIntValue)));
        return variableIndex;
    }

    private int getIncrementLength(int i) {
        if (Math.abs(i) < 100) {
            return 0;
        }
        if (100 > Math.abs(i) || Math.abs(i) >= 200) {
            return (200 > Math.abs(i) || Math.abs(i) >= 300) ? 5 : 3;
        }
        return 1;
    }
}
