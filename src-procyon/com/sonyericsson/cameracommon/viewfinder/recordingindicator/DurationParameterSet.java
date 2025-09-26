// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder.recordingindicator;

import com.sonyericsson.android.camera.util.CamLog;

public class DurationParameterSet
{
    public static final String TAG = "DurationParameterSet";
    public int hour;
    public int min;
    public int sec;
    
    public DurationParameterSet() {
        this.hour = 0;
        this.min = 0;
        this.sec = 0;
    }
    
    public void update(int n) {
        n /= 1000;
        this.sec = n % 60;
        n = (n - this.sec) / 60;
        this.min = n % 60;
        this.hour = (n - this.min) / 60;
        if (this.hour > 9) {
            this.sec = 59;
            this.min = 59;
            this.hour = 9;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getTime() ");
            sb.append(this.hour);
            sb.append(" : ");
            sb.append(this.min);
            sb.append(" : ");
            sb.append(this.sec);
            CamLog.d(sb.toString());
        }
    }
}
