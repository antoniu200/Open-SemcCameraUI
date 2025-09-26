// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.view.MotionEvent;
import android.view.View;
import android.view.Display;
import android.view.WindowManager;
import android.graphics.Rect;
import android.content.Context;
import android.graphics.Point;

public class ViewUtility
{
    private static final float ASPECT_TOLERANCE = 0.001f;
    public static final String TAG = "ViewUtility";
    
    public static Point getCenter(final Point point, final Point point2) {
        return new Point((point.x + point2.x) / 2, (point.y + point2.y) / 2);
    }
    
    public static Rect getEstimatedRealScreenRect(final Context context) {
        final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
        final Point point = new Point();
        defaultDisplay.getSize(point);
        int n;
        int n2;
        if (point.y < point.x) {
            n = point.x;
            n2 = point.y;
        }
        else {
            n = point.y;
            n2 = point.x;
        }
        ScreenSize screenSize = null;
        int n3 = n + n2;
        final ScreenSize[] values = ScreenSize.values();
        int n5;
        for (int length = values.length, i = 0; i < length; ++i, n3 = n5) {
            final ScreenSize screenSize2 = values[i];
            final int n4 = Math.abs(n - screenSize2.getWidth()) + Math.abs(n2 - screenSize2.getHeight());
            if (n4 < (n5 = n3)) {
                screenSize = screenSize2;
                n5 = n4;
            }
        }
        if (screenSize == null) {
            throw new RuntimeException("getEstimatedRealScreenRect():[Not supported screen size.]");
        }
        return screenSize.getAsRect();
    }
    
    public static int getPixel(final Context context, final int n) {
        return context.getResources().getDimensionPixelSize(n);
    }
    
    public static boolean hitTest(final View view, final MotionEvent motionEvent) {
        final int[] array = new int[2];
        view.getLocationOnScreen(array);
        return new Rect(array[0], array[1], array[0] + view.getWidth(), array[1] + view.getHeight()).contains((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
    }
    
    public static boolean isSimilarAspect(final float n, final float n2) {
        return Math.abs(n - n2) <= 0.001f;
    }
    
    public static boolean isSimilarAspect(final int n, final int n2, final int n3, final int n4) {
        boolean b = false;
        if (n >= 1 && n2 >= 1 && n3 >= 1 && n4 >= 1) {
            if (Math.abs(n / (float)n2 - n3 / (float)n4) <= 0.001f) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    public static boolean isSimilarAspectRect(final Rect rect, final Rect rect2) {
        return isSimilarAspect(rect.width(), rect.height(), rect2.width(), rect2.height());
    }
    
    private enum ScreenSize
    {
        private static final ScreenSize[] $VALUES;
        
        FULL_HD(1920, 1080), 
        HD(1280, 720), 
        WUXGA(1920, 1200);
        
        private final int mHeight;
        private final int mWidth;
        
        static {
            $VALUES = new ScreenSize[] { ScreenSize.WUXGA, ScreenSize.FULL_HD, ScreenSize.HD };
        }
        
        private ScreenSize(final int mWidth, final int mHeight) {
            this.mWidth = mWidth;
            this.mHeight = mHeight;
        }
        
        public Rect getAsRect() {
            return new Rect(0, 0, this.mWidth, this.mHeight);
        }
        
        public int getHeight() {
            return this.mHeight;
        }
        
        public int getWidth() {
            return this.mWidth;
        }
    }
}
