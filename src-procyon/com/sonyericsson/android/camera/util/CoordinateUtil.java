// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import android.graphics.RectF;
import android.graphics.Point;
import android.content.Context;
import android.graphics.Rect;

public final class CoordinateUtil
{
    private static final float ROUNDING = 0.5f;
    public static final String TAG = "CoordinateUtil";
    
    public static int convertAbsolutePosition2Relative(final int i, int j) {
        j = i * 100 / j;
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("###### convertAbsolutePosition2Relative: from ");
            sb.append(i);
            sb.append(" to ");
            sb.append(j);
            CamLog.d(sb.toString());
        }
        return j;
    }
    
    public static Rect convertDev2View(final Rect rect, final Rect rect2, final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("###### convertDev2View: srcArea: left, top, right, bottom, previewW, previewH: ");
            sb.append(rect.left);
            sb.append(", ");
            sb.append(rect.top);
            sb.append(", ");
            sb.append(rect.right);
            sb.append(", ");
            sb.append(rect.bottom);
            sb.append(", ");
            sb.append(rect2.width());
            sb.append(", ");
            sb.append(rect2.height());
            CamLog.d(sb.toString());
        }
        final Rect rect3 = new Rect(rect);
        if (n == 1) {
            rect3.left = rect2.height() - rect.bottom;
            rect3.top = rect.left;
            rect3.right = rect2.height() - rect.top;
            rect3.bottom = rect.right;
        }
        return rect3;
    }
    
    public static int convertDip2Px(final Context context, final int n) {
        return (int)(n * context.getResources().getDisplayMetrics().density + 0.5f);
    }
    
    public static Point convertDip2Px(final Context context, final Point point) {
        final float density = context.getResources().getDisplayMetrics().density;
        return new Point((int)(point.x * density + 0.5f), (int)(point.y * density + 0.5f));
    }
    
    public static Rect convertDip2Px(final Context context, final Rect rect) {
        final float density = context.getResources().getDisplayMetrics().density;
        return new Rect((int)(rect.left * density + 0.5f), (int)(rect.top * density + 0.5f), (int)(rect.right * density + 0.5f), (int)(rect.bottom * density + 0.5f));
    }
    
    public static Rect convertPositionToAligned(int max, int max2, final Rect rect, Rect rect2, final int n, final int n2) {
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
        if (rect.contains(max, max2)) {
            final int left = rect.left;
            final int top = rect.top;
            final int n3 = n / 2;
            final int n4 = n2 / 2;
            rect2 = new Rect(rect2.left - left, rect2.top - top, rect2.right - left - n, rect2.bottom - top - n2);
            max = Math.max(rect2.left, Math.min(rect2.right, max - left - n3));
            max2 = Math.max(rect2.top, Math.min(rect2.bottom, max2 - top - n4));
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("surface-left = ");
                sb.append(rect.left);
                CamLog.d(sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("surface-top  = ");
                sb2.append(rect.top);
                CamLog.d(sb2.toString());
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("surface-right = ");
                sb3.append(rect.right);
                CamLog.d(sb3.toString());
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("surface-bottom = ");
                sb4.append(rect.bottom);
                CamLog.d(sb4.toString());
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("positionBound-left = ");
                sb5.append(rect2.left);
                CamLog.d(sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("positionBound-top  = ");
                sb6.append(rect2.top);
                CamLog.d(sb6.toString());
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("positionBound-right = ");
                sb7.append(rect2.right);
                CamLog.d(sb7.toString());
                final StringBuilder sb8 = new StringBuilder();
                sb8.append("positionBound-bottom = ");
                sb8.append(rect2.bottom);
                CamLog.d(sb8.toString());
                final StringBuilder sb9 = new StringBuilder();
                sb9.append("aligned-left = ");
                sb9.append(max);
                CamLog.d(sb9.toString());
                final StringBuilder sb10 = new StringBuilder();
                sb10.append("aligned-top  = ");
                sb10.append(max2);
                CamLog.d(sb10.toString());
            }
            return new Rect(max, max2, n + max, n2 + max2);
        }
        return new Rect();
    }
    
    public static Rect[] convertPositionToSurface(final RectF[] array, final int n, final int n2, int i) {
        final Rect[] array2 = new Rect[array.length];
        float centerX;
        float n3;
        int n4;
        float centerY;
        float n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        StringBuilder sb;
        for (i = 0; i < array.length; ++i) {
            centerX = array[i].centerX();
            n3 = (float)n;
            n4 = (int)(centerX * n3);
            centerY = array[i].centerY();
            n5 = (float)n2;
            n6 = (int)(centerY * n5);
            n7 = (int)(array[i].width() * n3);
            n8 = (int)(array[i].height() * n5);
            n9 = n7 / 2;
            n10 = n8 / 2;
            array2[i] = new Rect(n4 - n9, n6 - n10, n4 + n9, n6 + n10);
            if (CamLog.VERBOSE) {
                sb = new StringBuilder();
                sb.append("###### convertDev2SurfaceView: convertedRect [px]: left: ");
                sb.append(array2[i].left);
                sb.append(", top: ");
                sb.append(array2[i].top);
                sb.append(", right: ");
                sb.append(array2[i].right);
                sb.append(", bottom: ");
                sb.append(array2[i].bottom);
                CamLog.d(sb.toString());
            }
        }
        return array2;
    }
    
    public static int convertPx2Dip(final Context context, final int n) {
        return (int)(n / context.getResources().getDisplayMetrics().density + 0.5f);
    }
    
    public static Point convertPx2Dip(final Context context, final Point point) {
        final float density = context.getResources().getDisplayMetrics().density;
        return new Point((int)(point.x / density + 0.5f), (int)(point.y / density + 0.5f));
    }
    
    public static Rect convertPx2Dip(final Context context, final Rect rect) {
        final float density = context.getResources().getDisplayMetrics().density;
        return new Rect((int)(rect.left / density + 0.5f), (int)(rect.top / density + 0.5f), (int)(rect.right / density + 0.5f), (int)(rect.bottom / density + 0.5f));
    }
    
    public static Rect convertView2Dev(final Rect rect, final Rect rect2, final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("###### convertView2Dev: srcArea: left, top, right, bottom, previewW, previewH: ");
            sb.append(rect.left);
            sb.append(", ");
            sb.append(rect.top);
            sb.append(", ");
            sb.append(rect.right);
            sb.append(", ");
            sb.append(rect.bottom);
            sb.append(", ");
            sb.append(rect2.width());
            sb.append(", ");
            sb.append(rect2.height());
            CamLog.d(sb.toString());
        }
        final Rect rect3 = new Rect(rect);
        if (n == 1) {
            rect3.left = rect.top;
            rect3.top = rect2.height() - rect.right;
            rect3.right = rect.bottom;
            rect3.bottom = rect2.height() - rect.left;
        }
        return rect3;
    }
    
    public static Rect scale(final Rect rect, final Rect rect2, final Rect rect3) {
        final int height = rect2.height();
        final int width = rect2.width();
        final int height2 = rect3.height();
        final int width2 = rect3.width();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("scale: origWidth: ");
            sb.append(width);
            sb.append(", showWidth: ");
            sb.append(width2);
            CamLog.d(sb.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Original position:top,left,right,bottom   :");
            sb2.append(rect.top);
            sb2.append(",");
            sb2.append(rect.left);
            sb2.append(",");
            sb2.append(rect.right);
            sb2.append(",");
            sb2.append(rect.bottom);
            sb2.append(")");
            CamLog.d(sb2.toString());
        }
        final int i = rect.top * height2 / height;
        final int j = rect.left * width2 / width;
        final int k = rect.bottom * height2 / height;
        final int l = rect.right * width2 / width;
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Display position:top,left,right,bottom   :");
            sb3.append(i);
            sb3.append(",");
            sb3.append(j);
            sb3.append(",");
            sb3.append(l);
            sb3.append(",");
            sb3.append(k);
            sb3.append(")");
            CamLog.d(sb3.toString());
        }
        return new Rect(j, i, l, k);
    }
    
    public static Rect scale2Dev(final Rect rect, final Rect rect2, final Rect rect3) {
        final int height = rect2.height();
        final int width = rect2.width();
        final int height2 = rect3.height();
        final int width2 = rect3.width();
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("scale2Dev: origWidth: ");
            sb.append(width);
            sb.append(", showWidth: ");
            sb.append(width2);
            CamLog.d(sb.toString());
        }
        final double d = width / (double)width2;
        final double d2 = height / (double)height2;
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("scale2Dev: ratio: width: ");
            sb2.append(d);
            sb2.append(", height: ");
            sb2.append(d2);
            CamLog.d(sb2.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("scale2Dev: Display position:top,left,right,bottom   :");
            sb3.append(rect.top);
            sb3.append(",");
            sb3.append(rect.left);
            sb3.append(",");
            sb3.append(rect.right);
            sb3.append(",");
            sb3.append(rect.bottom);
            sb3.append(")");
            CamLog.d(sb3.toString());
        }
        final double d3 = rect.top * d2;
        final double d4 = rect.left * d;
        final double d5 = rect.bottom * d2;
        final double d6 = rect.right * d;
        if (CamLog.VERBOSE) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("scale2Dev: Device position:top,left,right,bottom   :");
            sb4.append(d3);
            sb4.append(",");
            sb4.append(d4);
            sb4.append(",");
            sb4.append(d6);
            sb4.append(",");
            sb4.append(d5);
            sb4.append(")");
            CamLog.d(sb4.toString());
        }
        return new Rect((int)d4, (int)d3, (int)d6, (int)d5);
    }
}
