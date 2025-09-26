// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.device;

import java.util.Comparator;
import java.util.Locale;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.List;
import android.graphics.Rect;

public class CommonPlatformDependencyResolver
{
    private static final double ASPECT_TOLERANCE = 0.05;
    public static final String TAG = "CommonPlatformDependencyResolver";
    
    private static boolean equalsRatio(final Rect rect, final Rect rect2) {
        return Math.abs(rect.width() / (double)rect.height() - rect2.width() / (double)rect2.height()) <= 0.05;
    }
    
    private static Rect getOptimalPreviewRect(final OptimalPreviewSizeComparator optimalPreviewSizeComparator, final Rect rect, final Rect rect2, final List<Rect> list) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("E: captureSize:");
            sb.append(toString(rect));
            sb.append(", preferredPreviewSize:");
            sb.append(toString(rect2));
            CamLog.d(sb.toString());
        }
        final Rect rect3 = null;
        final Iterator<Rect> iterator = list.iterator();
        Rect rect4 = rect3;
        while (iterator.hasNext()) {
            final Rect rect5 = iterator.next();
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("previewSize:");
                sb2.append(toString(rect5));
                CamLog.d(sb2.toString());
            }
            if (rect5.height() <= rect2.height() && equalsRatio(rect5, rect)) {
                if (rect4 != null) {
                    if (optimalPreviewSizeComparator.compare(rect5, rect4) >= 0) {
                        continue;
                    }
                }
                rect4 = rect5;
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("X: result:");
            sb3.append(toString(rect4));
            CamLog.d(sb3.toString());
        }
        return rect4;
    }
    
    public static Rect getOptimalStillPreviewRect(final Rect rect, final Rect rect2, final List<Rect> list) {
        return getOptimalPreviewRect(new OptimalPreviewSizeComparator(rect2), rect, rect2, list);
    }
    
    public static Rect getOptimalVideoPreviewRect(final Rect rect, final Rect rect2, final List<Rect> list) {
        return getOptimalPreviewRect(new OptimalPreviewSizeComparator(rect), rect, rect2, list);
    }
    
    protected static String toString(final Rect rect) {
        if (rect == null) {
            return "null";
        }
        return String.format(Locale.US, "(%d,%d,%d,%d)", rect.left, rect.top, rect.right, rect.bottom);
    }
    
    private static class OptimalPreviewSizeComparator implements Comparator<Rect>
    {
        private final Rect mTarget;
        
        public OptimalPreviewSizeComparator(final Rect mTarget) {
            this.mTarget = mTarget;
        }
        
        @Override
        public int compare(final Rect rect, final Rect rect2) {
            return Math.abs(rect.height() - this.mTarget.height()) - Math.abs(rect2.height() - this.mTarget.height());
        }
    }
}
