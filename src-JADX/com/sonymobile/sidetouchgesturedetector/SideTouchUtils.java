package com.sonymobile.sidetouchgesturedetector;

import android.graphics.PointF;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SideTouchUtils {
    public static final int SIDE_BOTTOM = 8;
    public static final int SIDE_LEFT = 1;
    public static final int SIDE_RIGHT = 2;
    public static final int SIDE_TOP = 4;
    public static final int SIDE_UNKNOWN = 0;
    public static final int SOURCE_SIDETOUCH = 536870912;

    private static float clip(float f, float f2, float f3) {
        return f < f2 ? f2 : f > f3 ? f3 : f;
    }

    public static int getLogicalScreenSide(MotionEvent motionEvent) {
        return getLogicalScreenSide(motionEvent, motionEvent.getActionIndex());
    }

    public static int getLogicalScreenSide(MotionEvent motionEvent, int i) {
        InputDevice device = motionEvent.getDevice();
        float x = motionEvent.getX(i);
        float y = motionEvent.getY(i);
        InputDevice.MotionRange motionRange = device.getMotionRange(0, SOURCE_SIDETOUCH);
        InputDevice.MotionRange motionRange2 = device.getMotionRange(1, SOURCE_SIDETOUCH);
        if (motionRange == null || motionRange2 == null) {
            return 0;
        }
        int i2 = x <= motionRange.getMin() ? 1 : 0;
        if (x >= motionRange.getMax()) {
            i2 |= 2;
        }
        if (y <= motionRange2.getMin()) {
            i2 |= 4;
        }
        if (y >= motionRange2.getMax()) {
            i2 |= 8;
        }
        if (Integer.bitCount(i2) > 1) {
            return 0;
        }
        return i2;
    }

    public static PointF getLocalPos(View view, float f, float f2, boolean z) {
        PointF pointF = new PointF();
        getLocalPos(pointF, view, f, f2, z);
        return pointF;
    }

    public static void getLocalPos(PointF pointF, View view, float f, float f2, boolean z) {
        view.getLocationOnScreen(new int[2]);
        float fClip = f - r0[0];
        float fClip2 = f2 - r0[1];
        if (z) {
            fClip = clip(fClip, 0.0f, view.getWidth());
            fClip2 = clip(fClip2, 0.0f, view.getHeight());
        }
        pointF.set(fClip, fClip2);
    }
}
