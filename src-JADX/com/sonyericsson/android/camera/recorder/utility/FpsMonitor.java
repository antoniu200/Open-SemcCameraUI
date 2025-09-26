package com.sonyericsson.android.camera.recorder.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FpsMonitor {
    private final int mIntervalCount;
    private final List<Double> mResult = new ArrayList();
    private int mSampleCount = 0;
    private long mHeadSampleTime = 0;

    public FpsMonitor(int i) {
        this.mIntervalCount = i;
    }

    public void addSampleMillis(long j) {
        addSample(j * 1000 * 1000);
    }

    public void addSample(long j) {
        this.mSampleCount++;
        if (this.mSampleCount < this.mIntervalCount) {
            if (this.mSampleCount == 1) {
                this.mHeadSampleTime = j;
            }
        } else {
            addResult(j);
            this.mSampleCount = 0;
        }
    }

    public void reset() {
        this.mResult.clear();
        this.mSampleCount = 0;
        this.mHeadSampleTime = 0L;
    }

    private void addResult(long j) {
        this.mResult.add(Double.valueOf((this.mSampleCount - 1) / ((j - this.mHeadSampleTime) / 1.0E9d)));
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
        Iterator<Double> it = this.mResult.iterator();
        while (it.hasNext()) {
            sb.append(it.next().doubleValue() + ",");
        }
        return sb.toString();
    }
}
