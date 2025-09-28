package com.sonyericsson.cameracommon.device;

import android.graphics.Rect;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CommonPlatformDependencyResolver {
    private static final double ASPECT_TOLERANCE = 0.05d;
    public static final String TAG = "CommonPlatformDependencyResolver";

    public static Rect getOptimalStillPreviewRect(Rect rect, Rect rect2, List<Rect> list) {
        return getOptimalPreviewRect(new OptimalPreviewSizeComparator(rect2), rect, rect2, list);
    }

    public static Rect getOptimalVideoPreviewRect(Rect rect, Rect rect2, List<Rect> list) {
        return getOptimalPreviewRect(new OptimalPreviewSizeComparator(rect), rect, rect2, list);
    }

    private static Rect getOptimalPreviewRect(OptimalPreviewSizeComparator optimalPreviewSizeComparator, Rect rect, Rect rect2, List<Rect> list) {
        if (CamLog.VERBOSE) {
            CamLog.d("E: captureSize:" + toString(rect) + ", preferredPreviewSize:" + toString(rect2));
        }
        Rect rect3 = null;
        for (Rect rect4 : list) {
            if (CamLog.VERBOSE) {
                CamLog.d("previewSize:" + toString(rect4));
            }
            if (rect4.height() <= rect2.height() && equalsRatio(rect4, rect) && (rect3 == null || optimalPreviewSizeComparator.compare(rect4, rect3) < 0)) {
                rect3 = rect4;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("X: result:" + toString(rect3));
        }
        return rect3;
    }

    protected static String toString(Rect rect) {
        return rect == null ? "null" : String.format(Locale.US, "(%d,%d,%d,%d)", Integer.valueOf(rect.left), Integer.valueOf(rect.top), Integer.valueOf(rect.right), Integer.valueOf(rect.bottom));
    }

    private static boolean equalsRatio(Rect rect, Rect rect2) {
        return Math.abs((((double) rect.width()) / ((double) rect.height())) - (((double) rect2.width()) / ((double) rect2.height()))) <= ASPECT_TOLERANCE;
    }

    private static class OptimalPreviewSizeComparator implements Comparator<Rect> {
        private final Rect mTarget;

        public OptimalPreviewSizeComparator(Rect rect) {
            this.mTarget = rect;
        }

        @Override // java.util.Comparator
        public int compare(Rect rect, Rect rect2) {
            return Math.abs(rect.height() - this.mTarget.height()) - Math.abs(rect2.height() - this.mTarget.height());
        }
    }
}
