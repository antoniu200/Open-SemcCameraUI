package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.sonyericsson.cameracommon.device.SizeConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ViewUtility {
    private static final float ASPECT_TOLERANCE = 0.001f;
    public static final String TAG = "ViewUtility";

    public static boolean hitTest(View view, MotionEvent motionEvent) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight()).contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
    }

    private enum ScreenSize {
        WUXGA(SizeConstants.WIDTH_PREVIEW_FULL_HD, 1200),
        FULL_HD(SizeConstants.WIDTH_PREVIEW_FULL_HD, SizeConstants.HEIGHT_PREVIEW_FULL_HD),
        HD(SizeConstants.WIDTH_PREVIEW_HD, SizeConstants.HEIGHT_PREVIEW_HD);

        private final int mHeight;
        private final int mWidth;

        ScreenSize(int i, int i2) {
            this.mWidth = i;
            this.mHeight = i2;
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public Rect getAsRect() {
            return new Rect(0, 0, this.mWidth, this.mHeight);
        }
    }

    public static Rect getEstimatedRealScreenRect(Context context) {
        int i;
        int i2;
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        if (point.y < point.x) {
            i = point.x;
            i2 = point.y;
        } else {
            i = point.y;
            i2 = point.x;
        }
        ScreenSize screenSize = null;
        int i3 = i + i2;
        for (ScreenSize screenSize2 : ScreenSize.values()) {
            int iAbs = Math.abs(i - screenSize2.getWidth()) + Math.abs(i2 - screenSize2.getHeight());
            if (iAbs < i3) {
                screenSize = screenSize2;
                i3 = iAbs;
            }
        }
        if (screenSize == null) {
            throw new RuntimeException("getEstimatedRealScreenRect():[Not supported screen size.]");
        }
        return screenSize.getAsRect();
    }

    public static int getPixel(Context context, int i) {
        return context.getResources().getDimensionPixelSize(i);
    }

    public static boolean isSimilarAspect(int i, int i2, int i3, int i4) {
        return i >= 1 && i2 >= 1 && i3 >= 1 && i4 >= 1 && Math.abs((((float) i) / ((float) i2)) - (((float) i3) / ((float) i4))) <= ASPECT_TOLERANCE;
    }

    public static boolean isSimilarAspect(float f, float f2) {
        return Math.abs(f - f2) <= ASPECT_TOLERANCE;
    }

    public static boolean isSimilarAspectRect(Rect rect, Rect rect2) {
        return isSimilarAspect(rect.width(), rect.height(), rect2.width(), rect2.height());
    }

    public static Point getCenter(Point point, Point point2) {
        return new Point((point.x + point2.x) / 2, (point.y + point2.y) / 2);
    }
}
