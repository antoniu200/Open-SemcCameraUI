// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import com.sonyericsson.android.camera.util.CamLog;

public class RotationUtil
{
    public static final String TAG = "RotationUtil";
    
    public static float getAngle(final int n) {
        if (n == 1) {
            return -90.0f;
        }
        return 0.0f;
    }
    
    public static int getNormalizedRotation(int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ORIENTATION: sensor value ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        n %= 360;
        if (45 < n && n <= 135) {
            n = 90;
        }
        else if (135 <= n && n <= 225) {
            n = 180;
        }
        else if (225 <= n && n <= 315) {
            n = 270;
        }
        else {
            n = 0;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("ORIENTATION: normalized value is ");
            sb2.append(n);
            CamLog.d("RotationUtil", sb2.toString());
        }
        return n;
    }
    
    public static String orientationToString(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 2: {
                return "LANDSCAPE";
            }
            case 1: {
                return "PORTRAIT";
            }
        }
    }
}
