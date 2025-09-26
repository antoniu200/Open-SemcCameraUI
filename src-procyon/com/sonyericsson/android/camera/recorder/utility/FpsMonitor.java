// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class FpsMonitor
{
    private long mHeadSampleTime;
    private final int mIntervalCount;
    private final List<Double> mResult;
    private int mSampleCount;
    
    public FpsMonitor(final int mIntervalCount) {
        this.mResult = new ArrayList<Double>();
        this.mIntervalCount = mIntervalCount;
        this.mSampleCount = 0;
        this.mHeadSampleTime = 0L;
    }
    
    private void addResult(final long n) {
        this.mResult.add((this.mSampleCount - 1) / ((n - this.mHeadSampleTime) / 1.0E9));
    }
    
    public void addSample(final long mHeadSampleTime) {
        ++this.mSampleCount;
        if (this.mSampleCount < this.mIntervalCount) {
            if (this.mSampleCount == 1) {
                this.mHeadSampleTime = mHeadSampleTime;
            }
        }
        else {
            this.addResult(mHeadSampleTime);
            this.mSampleCount = 0;
        }
    }
    
    public void addSampleMillis(final long n) {
        this.addSample(n * 1000L * 1000L);
    }
    
    public String dump() {
        final StringBuilder sb = new StringBuilder();
        for (final double doubleValue : this.mResult) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(doubleValue);
            sb2.append(",");
            sb.append(sb2.toString());
        }
        return sb.toString();
    }
    
    public void reset() {
        this.mResult.clear();
        this.mSampleCount = 0;
        this.mHeadSampleTime = 0L;
    }
}
