// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

public class PredictiveCaptureStoreInfo
{
    private final int mCaptureNum;
    private final String mCaptureTime;
    
    public PredictiveCaptureStoreInfo(final int mCaptureNum, final String mCaptureTime) {
        this.mCaptureNum = mCaptureNum;
        this.mCaptureTime = mCaptureTime;
    }
    
    public int getCaptureNum() {
        return this.mCaptureNum;
    }
    
    public String getCaptureTime() {
        return this.mCaptureTime;
    }
}
