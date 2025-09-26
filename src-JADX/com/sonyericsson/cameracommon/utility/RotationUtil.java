package com.sonyericsson.cameracommon.utility;

import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RotationUtil {
    public static final String TAG = "RotationUtil";

    public static float getAngle(int i) {
        return i == 1 ? -90.0f : 0.0f;
    }

    public static String orientationToString(int i) {
        switch (i) {
            case 1:
                return "PORTRAIT";
            case 2:
                return "LANDSCAPE";
            default:
                return "UNKNOWN";
        }
    }

    public static int getNormalizedRotation(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("ORIENTATION: sensor value " + i);
        }
        int i2 = i % 360;
        int i3 = (45 >= i2 || i2 > 135) ? (135 > i2 || i2 > 225) ? (225 > i2 || i2 > 315) ? 0 : 270 : 180 : 90;
        if (CamLog.VERBOSE) {
            CamLog.d(TAG, "ORIENTATION: normalized value is " + i3);
        }
        return i3;
    }
}
