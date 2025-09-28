package com.sonyericsson.cameracommon.viewfinder;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.WindowManager;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LayoutDependencyResolver {
    private static final int BASIC_SYSTEM_UI_FLAGS = 7936;
    public static final String TAG = "LayoutDependencyResolver";
    private static float VIEWFINDER_ASPECT_RATIO = 1.7777778f;

    public enum SystemBarStatus {
        ALWAYS_CANCELED,
        REGION_OVERLAID
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.is_tablet);
    }

    public static boolean isTenInch(Context context) {
        return context.getResources().getBoolean(R.bool.is_ten_inch);
    }

    public static SystemBarStatus getCurrentSystemBarStatus(Context context) {
        if (isTablet(context)) {
            return SystemBarStatus.ALWAYS_CANCELED;
        }
        return SystemBarStatus.REGION_OVERLAID;
    }

    public static int getSystemBarMargin(Context context) {
        switch (getCurrentSystemBarStatus(context)) {
            case ALWAYS_CANCELED:
                return 0;
            case REGION_OVERLAID:
                int identifier = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (identifier > 0) {
                    return context.getResources().getDimensionPixelSize(identifier);
                }
                return context.getResources().getDimensionPixelSize(R.dimen.navigationbar_width);
            default:
                throw new IllegalStateException("getSystemBarMargin(): Unknown system bar status");
        }
    }

    public static Rect getViewFinderSize(Context context) {
        Point point = new Point(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
        Point point2 = new Point();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealSize(point2);
        switch (getCurrentSystemBarStatus(context)) {
            case ALWAYS_CANCELED:
                if (isTablet(context)) {
                    return cropWithAspectRatio(point2, VIEWFINDER_ASPECT_RATIO);
                }
                return cropWithAspectRatio(point, VIEWFINDER_ASPECT_RATIO);
            case REGION_OVERLAID:
                return cropWithAspectRatio(point2, VIEWFINDER_ASPECT_RATIO);
            default:
                throw new IllegalStateException("getViewFinderSize(): Unknown system bar status");
        }
    }

    private static Rect cropWithAspectRatio(Point point, float f) {
        float fMax = Math.max(point.x, point.y);
        float fMin = Math.min(point.x, point.y);
        if (fMax / fMin < f) {
            return new Rect(0, 0, (int) Math.ceil(fMax), (int) Math.ceil(fMax / f));
        }
        return new Rect(0, 0, (int) Math.ceil(f * fMin), (int) Math.ceil(fMin));
    }

    public static int getLeftItemCount(Context context) {
        return context.getResources().getInteger(R.integer.shortcut_icon_count);
    }

    public static Rect getSurfaceRect(Context context, float f) {
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(getViewFinderSize(context));
        if (f > rectAccordingToLayoutOrientation.width() / rectAccordingToLayoutOrientation.height()) {
            return new Rect(0, 0, rectAccordingToLayoutOrientation.width(), (int) (rectAccordingToLayoutOrientation.width() / f));
        }
        return new Rect(0, 0, (int) (rectAccordingToLayoutOrientation.height() * f), rectAccordingToLayoutOrientation.height());
    }
}
