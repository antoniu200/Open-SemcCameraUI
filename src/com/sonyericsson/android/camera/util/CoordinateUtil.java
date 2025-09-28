package com.sonyericsson.android.camera.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class CoordinateUtil {
    private static final float ROUNDING = 0.5f;
    public static final String TAG = "CoordinateUtil";

    public static Rect scale(Rect rect, Rect rect2, Rect rect3) {
        int iHeight = rect2.height();
        int iWidth = rect2.width();
        int iHeight2 = rect3.height();
        int iWidth2 = rect3.width();
        if (CamLog.VERBOSE) {
            CamLog.d("scale: origWidth: " + iWidth + ", showWidth: " + iWidth2);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Original position:top,left,right,bottom   :" + rect.top + "," + rect.left + "," + rect.right + "," + rect.bottom + ")");
        }
        int i = (rect.top * iHeight2) / iHeight;
        int i2 = (rect.left * iWidth2) / iWidth;
        int i3 = (rect.bottom * iHeight2) / iHeight;
        int i4 = (rect.right * iWidth2) / iWidth;
        if (CamLog.VERBOSE) {
            CamLog.d("Display position:top,left,right,bottom   :" + i + "," + i2 + "," + i4 + "," + i3 + ")");
        }
        return new Rect(i2, i, i4, i3);
    }

    public static Rect scale2Dev(Rect rect, Rect rect2, Rect rect3) {
        int iHeight = rect2.height();
        int iWidth = rect2.width();
        int iHeight2 = rect3.height();
        int iWidth2 = rect3.width();
        if (CamLog.VERBOSE) {
            CamLog.d("scale2Dev: origWidth: " + iWidth + ", showWidth: " + iWidth2);
        }
        double d = iWidth / iWidth2;
        double d2 = iHeight / iHeight2;
        if (CamLog.VERBOSE) {
            CamLog.d("scale2Dev: ratio: width: " + d + ", height: " + d2);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("scale2Dev: Display position:top,left,right,bottom   :" + rect.top + "," + rect.left + "," + rect.right + "," + rect.bottom + ")");
        }
        double d3 = rect.top * d2;
        double d4 = rect.left * d;
        double d5 = rect.bottom * d2;
        double d6 = rect.right * d;
        if (CamLog.VERBOSE) {
            CamLog.d("scale2Dev: Device position:top,left,right,bottom   :" + d3 + "," + d4 + "," + d6 + "," + d5 + ")");
        }
        return new Rect((int) d4, (int) d3, (int) d6, (int) d5);
    }

    public static Rect convertDev2View(Rect rect, Rect rect2, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("###### convertDev2View: srcArea: left, top, right, bottom, previewW, previewH: " + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ", " + rect2.width() + ", " + rect2.height());
        }
        Rect rect3 = new Rect(rect);
        if (i == 1) {
            rect3.left = rect2.height() - rect.bottom;
            rect3.top = rect.left;
            rect3.right = rect2.height() - rect.top;
            rect3.bottom = rect.right;
        }
        return rect3;
    }

    public static Rect[] convertPositionToSurface(RectF[] rectFArr, int i, int i2, int i3) {
        Rect[] rectArr = new Rect[rectFArr.length];
        for (int i4 = 0; i4 < rectFArr.length; i4++) {
            float f = i;
            int iCenterX = (int) (rectFArr[i4].centerX() * f);
            float f2 = i2;
            int iCenterY = (int) (rectFArr[i4].centerY() * f2);
            int iWidth = ((int) (rectFArr[i4].width() * f)) / 2;
            int iHeight = ((int) (rectFArr[i4].height() * f2)) / 2;
            rectArr[i4] = new Rect(iCenterX - iWidth, iCenterY - iHeight, iCenterX + iWidth, iCenterY + iHeight);
            if (CamLog.VERBOSE) {
                CamLog.d("###### convertDev2SurfaceView: convertedRect [px]: left: " + rectArr[i4].left + ", top: " + rectArr[i4].top + ", right: " + rectArr[i4].right + ", bottom: " + rectArr[i4].bottom);
            }
        }
        return rectArr;
    }

    public static Rect convertView2Dev(Rect rect, Rect rect2, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("###### convertView2Dev: srcArea: left, top, right, bottom, previewW, previewH: " + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ", " + rect2.width() + ", " + rect2.height());
        }
        Rect rect3 = new Rect(rect);
        if (i == 1) {
            rect3.left = rect.top;
            rect3.top = rect2.height() - rect.right;
            rect3.right = rect.bottom;
            rect3.bottom = rect2.height() - rect.left;
        }
        return rect3;
    }

    public static int convertAbsolutePosition2Relative(int i, int i2) {
        int i3 = (i * 100) / i2;
        if (CamLog.VERBOSE) {
            CamLog.d("###### convertAbsolutePosition2Relative: from " + i + " to " + i3);
        }
        return i3;
    }

    public static int convertDip2Px(Context context, int i) {
        return (int) ((i * context.getResources().getDisplayMetrics().density) + ROUNDING);
    }

    public static Point convertDip2Px(Context context, Point point) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Point((int) ((point.x * f) + ROUNDING), (int) ((point.y * f) + ROUNDING));
    }

    public static Rect convertDip2Px(Context context, Rect rect) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Rect((int) ((rect.left * f) + ROUNDING), (int) ((rect.top * f) + ROUNDING), (int) ((rect.right * f) + ROUNDING), (int) ((rect.bottom * f) + ROUNDING));
    }

    public static int convertPx2Dip(Context context, int i) {
        return (int) ((i / context.getResources().getDisplayMetrics().density) + ROUNDING);
    }

    public static Point convertPx2Dip(Context context, Point point) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Point((int) ((point.x / f) + ROUNDING), (int) ((point.y / f) + ROUNDING));
    }

    public static Rect convertPx2Dip(Context context, Rect rect) {
        float f = context.getResources().getDisplayMetrics().density;
        return new Rect((int) ((rect.left / f) + ROUNDING), (int) ((rect.top / f) + ROUNDING), (int) ((rect.right / f) + ROUNDING), (int) ((rect.bottom / f) + ROUNDING));
    }

    public static Rect convertPositionToAligned(int i, int i2, Rect rect, Rect rect2, int i3, int i4) {
        if (rect == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("surface is null");
            }
            return new Rect();
        }
        if (rect2 == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("Touch area is null.");
            }
            return new Rect();
        }
        if (rect.contains(i, i2)) {
            int i5 = rect.left;
            int i6 = rect.top;
            Rect rect3 = new Rect(rect2.left - i5, rect2.top - i6, (rect2.right - i5) - i3, (rect2.bottom - i6) - i4);
            int iMax = Math.max(rect3.left, Math.min(rect3.right, (i - i5) - (i3 / 2)));
            int iMax2 = Math.max(rect3.top, Math.min(rect3.bottom, (i2 - i6) - (i4 / 2)));
            if (CamLog.VERBOSE) {
                CamLog.d("surface-left = " + rect.left);
                CamLog.d("surface-top  = " + rect.top);
                CamLog.d("surface-right = " + rect.right);
                CamLog.d("surface-bottom = " + rect.bottom);
                CamLog.d("positionBound-left = " + rect3.left);
                CamLog.d("positionBound-top  = " + rect3.top);
                CamLog.d("positionBound-right = " + rect3.right);
                CamLog.d("positionBound-bottom = " + rect3.bottom);
                CamLog.d("aligned-left = " + iMax);
                CamLog.d("aligned-top  = " + iMax2);
            }
            return new Rect(iMax, iMax2, i3 + iMax, i4 + iMax2);
        }
        return new Rect();
    }
}
