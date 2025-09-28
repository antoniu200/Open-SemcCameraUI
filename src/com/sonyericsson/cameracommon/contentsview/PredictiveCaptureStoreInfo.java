package com.sonyericsson.cameracommon.contentsview;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PredictiveCaptureStoreInfo {
    private final int mCaptureNum;
    private final String mCaptureTime;

    public PredictiveCaptureStoreInfo(int i, String str) {
        this.mCaptureNum = i;
        this.mCaptureTime = str;
    }

    public int getCaptureNum() {
        return this.mCaptureNum;
    }

    public String getCaptureTime() {
        return this.mCaptureTime;
    }
}
