package com.sonyericsson.android.camera.view.angle;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VariableIndex {
    protected int mIndex;
    protected final int mMaxIndex;
    protected final int mMinIndex;

    public interface Calculator {
        VariableIndex calculate(VariableIndex variableIndex, Object... objArr);
    }

    public VariableIndex(int i, int i2, int i3) {
        this.mMaxIndex = i;
        this.mMinIndex = i2;
        this.mIndex = i3;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public float getRatio() {
        return this.mIndex / this.mMaxIndex;
    }

    void setIndex(int i) {
        this.mIndex = Math.max(this.mMinIndex, Math.min(this.mMaxIndex, i));
    }
}
