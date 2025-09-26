// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.sidetouchgesturedetector;

import android.view.InputDevice$MotionRange;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.graphics.PointF;
import android.view.View;

public class SideTouchUtils
{
    public static final int SIDE_BOTTOM = 8;
    public static final int SIDE_LEFT = 1;
    public static final int SIDE_RIGHT = 2;
    public static final int SIDE_TOP = 4;
    public static final int SIDE_UNKNOWN = 0;
    public static final int SOURCE_SIDETOUCH = 536870912;
    
    private static float clip(final float n, final float n2, final float n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    public static PointF getLocalPos(final View view, final float n, final float n2, final boolean b) {
        final PointF pointF = new PointF();
        getLocalPos(pointF, view, n, n2, b);
        return pointF;
    }
    
    public static void getLocalPos(final PointF pointF, final View view, float clip, float clip2, final boolean b) {
        final int[] array = new int[2];
        view.getLocationOnScreen(array);
        final float n = clip - array[0];
        final float n2 = clip2 - array[1];
        clip2 = n;
        clip = n2;
        if (b) {
            clip2 = clip(n, 0.0f, (float)view.getWidth());
            clip = clip(n2, 0.0f, (float)view.getHeight());
        }
        pointF.set(clip2, clip);
    }
    
    public static int getLogicalScreenSide(final MotionEvent motionEvent) {
        return getLogicalScreenSide(motionEvent, motionEvent.getActionIndex());
    }
    
    public static int getLogicalScreenSide(final MotionEvent motionEvent, int n) {
        final InputDevice device = motionEvent.getDevice();
        final float x = motionEvent.getX(n);
        final float y = motionEvent.getY(n);
        final InputDevice$MotionRange motionRange = device.getMotionRange(0, 536870912);
        final InputDevice$MotionRange motionRange2 = device.getMotionRange(1, 536870912);
        if (motionRange == null || motionRange2 == null) {
            return 0;
        }
        if (x <= motionRange.getMin()) {
            n = 1;
        }
        else {
            n = 0;
        }
        int n2 = n;
        if (x >= motionRange.getMax()) {
            n2 = (n | 0x2);
        }
        n = n2;
        if (y <= motionRange2.getMin()) {
            n = (n2 | 0x4);
        }
        int i = n;
        if (y >= motionRange2.getMax()) {
            i = (n | 0x8);
        }
        if (Integer.bitCount(i) > 1) {
            return 0;
        }
        return i;
    }
}
