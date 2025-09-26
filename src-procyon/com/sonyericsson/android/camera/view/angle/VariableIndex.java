// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.angle;

public class VariableIndex
{
    protected int mIndex;
    protected final int mMaxIndex;
    protected final int mMinIndex;
    
    public VariableIndex(final int mMaxIndex, final int mMinIndex, final int mIndex) {
        this.mMaxIndex = mMaxIndex;
        this.mMinIndex = mMinIndex;
        this.mIndex = mIndex;
    }
    
    public int getIndex() {
        return this.mIndex;
    }
    
    public float getRatio() {
        return this.mIndex / (float)this.mMaxIndex;
    }
    
    void setIndex(final int b) {
        this.mIndex = Math.max(this.mMinIndex, Math.min(this.mMaxIndex, b));
    }
    
    public interface Calculator
    {
        VariableIndex calculate(final VariableIndex p0, final Object... p1);
    }
}
