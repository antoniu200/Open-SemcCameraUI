package com.sonyericsson.cameracommon.viewfinder.recordingindicator;

import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DurationParameterSet {
    public static final String TAG = "DurationParameterSet";
    public int hour = 0;
    public int min = 0;
    public int sec = 0;

    public void update(int i) {
        int i2 = i / 1000;
        this.sec = i2 % 60;
        int i3 = (i2 - this.sec) / 60;
        this.min = i3 % 60;
        this.hour = (i3 - this.min) / 60;
        if (this.hour > 9) {
            this.sec = 59;
            this.min = 59;
            this.hour = 9;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getTime() " + this.hour + " : " + this.min + " : " + this.sec);
        }
    }
}
