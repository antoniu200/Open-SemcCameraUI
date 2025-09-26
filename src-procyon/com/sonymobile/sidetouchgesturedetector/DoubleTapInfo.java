// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.sidetouchgesturedetector;

import android.view.MotionEvent;

public class DoubleTapInfo
{
    public int doubleTapMinTime;
    public int doubleTapSlopSquare;
    public int doubleTapTimeout;
    public int doubleTapTimeoutReal;
    public MotionEvent firstDown;
    public long firstDownTime;
    public float firstDownX;
    public float firstDownY;
    public long firstUpTime;
    public boolean isLearningMode;
    public MotionEvent secondDown;
    public long secondDownTime;
    public float secondDownX;
    public float secondDownY;
    
    DoubleTapInfo doubleTapMinTime(final int doubleTapMinTime) {
        this.doubleTapMinTime = doubleTapMinTime;
        return this;
    }
    
    DoubleTapInfo doubleTapSlopSquare(final int doubleTapSlopSquare) {
        this.doubleTapSlopSquare = doubleTapSlopSquare;
        return this;
    }
    
    DoubleTapInfo doubleTapTimeout(final int doubleTapTimeout) {
        this.doubleTapTimeout = doubleTapTimeout;
        return this;
    }
    
    DoubleTapInfo doubleTapTimeoutReal(final int doubleTapTimeoutReal) {
        this.doubleTapTimeoutReal = doubleTapTimeoutReal;
        return this;
    }
    
    DoubleTapInfo firstDown(final MotionEvent firstDown) {
        this.firstDown = firstDown;
        this.firstDownTime = firstDown.getEventTime();
        this.firstDownX = firstDown.getX(firstDown.getActionIndex());
        this.firstDownY = firstDown.getY(firstDown.getActionIndex());
        return this;
    }
    
    DoubleTapInfo firstUpTime(final long firstUpTime) {
        this.firstUpTime = firstUpTime;
        return this;
    }
    
    public long getDeltaTime() {
        return this.secondDownTime - this.firstUpTime;
    }
    
    public long getDeltaX() {
        return (int)this.firstDownX - (int)this.secondDownX;
    }
    
    public long getDeltaY() {
        return (int)this.firstDownY - (int)this.secondDownY;
    }
    
    public long getDoubleTapTimeout() {
        if (this.isLearningMode) {
            return this.doubleTapTimeoutReal;
        }
        return this.doubleTapTimeout;
    }
    
    public Status getStatus() {
        if (this.getDeltaTime() > this.getDoubleTapTimeout()) {
            return Status.SlowTap;
        }
        if (this.getDeltaTime() < this.doubleTapMinTime) {
            return Status.FastTap;
        }
        final long deltaX = this.getDeltaX();
        final long deltaY = this.getDeltaY();
        if (deltaX * deltaX + deltaY * deltaY >= this.doubleTapSlopSquare) {
            return Status.FarPosition;
        }
        return Status.Success;
    }
    
    DoubleTapInfo isLearningMode(final boolean isLearningMode) {
        this.isLearningMode = isLearningMode;
        return this;
    }
    
    DoubleTapInfo secondDown(final MotionEvent secondDown) {
        this.secondDown = secondDown;
        this.secondDownTime = secondDown.getEventTime();
        this.secondDownX = secondDown.getX(secondDown.getActionIndex());
        this.secondDownY = secondDown.getY(secondDown.getActionIndex());
        return this;
    }
    
    public enum Status
    {
        private static final Status[] $VALUES;
        
        FarPosition, 
        FastTap, 
        SlowTap, 
        Success;
        
        static {
            $VALUES = new Status[] { Status.Success, Status.FastTap, Status.SlowTap, Status.FarPosition };
        }
    }
}
