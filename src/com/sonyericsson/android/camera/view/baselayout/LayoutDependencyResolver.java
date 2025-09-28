package com.sonyericsson.android.camera.view.baselayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.ResourceUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LayoutDependencyResolver {
    private static final int BASIC_SYSTEM_UI_FLAGS = 1792;
    private static final float SCREEN_ASPECT_EIGHTEEN_NINE = 2.0f;
    private static final float SCREEN_ASPECT_NOT_DEFINED = -1.0f;
    private static final float SCREEN_ASPECT_SIXTEEN_NINE = 1.7777778f;
    private static final String TAG = "LayoutDependencyResolver";

    public enum SystemBarStatus {
        ALWAYS_CANCELED,
        REGION_OVERLAID
    }

    public enum ScreenAspect {
        NOT_DEFINED(LayoutDependencyResolver.SCREEN_ASPECT_NOT_DEFINED),
        SIXTEEN_NINE(LayoutDependencyResolver.SCREEN_ASPECT_SIXTEEN_NINE),
        EIGHTEEN_NINE(LayoutDependencyResolver.SCREEN_ASPECT_EIGHTEEN_NINE);

        private final float mScreenAspectRatio;

        ScreenAspect(float f) {
            this.mScreenAspectRatio = f;
        }

        public float getScreenAspectRatio() {
            return this.mScreenAspectRatio;
        }
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

    public static int getNavigationBarMargin(Context context) {
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
                throw new IllegalStateException("getNavigationBarMargin(): Unknown navigation bar status");
        }
    }

    public static Rect getViewFinderSize(Context context) {
        Point point = new Point(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
        Point point2 = new Point();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRealSize(point2);
        float fMax = (Math.max(point2.x, point2.y) * 1.0f) / Math.min(point2.x, point2.y);
        switch (getCurrentSystemBarStatus(context)) {
            case ALWAYS_CANCELED:
                if (isTablet(context)) {
                    return cropWithAspectRatio(point2, fMax);
                }
                return cropWithAspectRatio(point, fMax);
            case REGION_OVERLAID:
                return cropWithAspectRatio(point2, fMax);
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

    public static Rect getSurfaceViewRect(Context context, float f, ScreenAspect screenAspect) {
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(getViewFinderSize(context));
        if (f > rectAccordingToLayoutOrientation.width() / rectAccordingToLayoutOrientation.height()) {
            if (screenAspect == ScreenAspect.EIGHTEEN_NINE && Math.abs(f - 0.5625f) < 0.01f) {
                int iHeight = (rectAccordingToLayoutOrientation.height() - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), R.dimen.navigator_container_width)) - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), R.dimen.left_icon_area_height);
                return new Rect(0, 0, (int) (iHeight * f), iHeight);
            }
            if (Math.abs(f - 1.0f) < 0.01f) {
                return new Rect(0, rectAccordingToLayoutOrientation.width() / 3, rectAccordingToLayoutOrientation.width(), (rectAccordingToLayoutOrientation.width() * 4) / 3);
            }
            return new Rect(0, 0, rectAccordingToLayoutOrientation.width(), (int) (rectAccordingToLayoutOrientation.width() / f));
        }
        if (screenAspect == ScreenAspect.EIGHTEEN_NINE && Math.abs(f - SCREEN_ASPECT_SIXTEEN_NINE) < 0.01f) {
            int iWidth = (rectAccordingToLayoutOrientation.width() - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), R.dimen.navigator_container_width)) - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), R.dimen.left_icon_area_height);
            return new Rect(0, 0, iWidth, (int) (iWidth / f));
        }
        if (Math.abs(f - 1.0f) < 0.01f) {
            return new Rect(rectAccordingToLayoutOrientation.height() / 3, 0, (rectAccordingToLayoutOrientation.height() * 4) / 3, rectAccordingToLayoutOrientation.height());
        }
        return new Rect(0, 0, (int) (rectAccordingToLayoutOrientation.height() * f), rectAccordingToLayoutOrientation.height());
    }

    public static void resolveLayoutDependencyOnDevice(Activity activity, View view) throws Resources.NotFoundException {
        Rect viewFinderSize = getViewFinderSize(activity);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = viewFinderSize.width();
        layoutParams.height = viewFinderSize.height();
        layoutParams.gravity = 80;
        setupRotatableToast(activity);
    }

    public static void requestToRemoveSystemUi(View view) {
        if (view == null) {
            return;
        }
        int i = BASIC_SYSTEM_UI_FLAGS;
        switch (getCurrentSystemBarStatus(view.getContext())) {
            case REGION_OVERLAID:
                view.setSystemUiVisibility(i);
                break;
        }
        view.requestLayout();
    }

    public static void requestToDimSystemUi(View view) {
        if (view == null) {
            return;
        }
        int i = BASIC_SYSTEM_UI_FLAGS;
        switch (getCurrentSystemBarStatus(view.getContext())) {
            case REGION_OVERLAID:
                view.setSystemUiVisibility(i | 1);
                break;
        }
        view.requestLayout();
    }

    public static void requestToRecoverSystemUi(View view) {
        if (view == null) {
            return;
        }
        int i = BASIC_SYSTEM_UI_FLAGS;
        switch (getCurrentSystemBarStatus(view.getContext())) {
            case REGION_OVERLAID:
                view.setSystemUiVisibility(i);
                break;
        }
        view.requestLayout();
    }

    public static void setupRotatableToast(Activity activity) throws Resources.NotFoundException {
        int iMax;
        int iMin;
        int iHeight;
        Rect rect;
        Rect rect2;
        Rect rect3;
        Rect rect4;
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int dimensionPixelSize = activity.getResources().getDimensionPixelSize(R.dimen.left_container_width);
        int dimensionPixelSize2 = activity.getResources().getDimensionPixelSize(R.dimen.right_container_width) + getNavigationBarMargin(activity);
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(getViewFinderSize(activity));
        LayoutOrientationResolver.LayoutOrientationType orientation = LayoutOrientationResolver.getInstance().getOrientation();
        if (orientation == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            iMax = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            iMin = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
            rectAccordingToLayoutOrientation.offset(0, iMax - rectAccordingToLayoutOrientation.width());
            iHeight = rectAccordingToLayoutOrientation.width() / getLeftItemCount(activity);
        } else {
            iMax = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
            iMin = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            rectAccordingToLayoutOrientation.offset(0, iMin - rectAccordingToLayoutOrientation.height());
            iHeight = rectAccordingToLayoutOrientation.height() / getLeftItemCount(activity);
        }
        if (orientation == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            rect = new Rect(rectAccordingToLayoutOrientation.right - iHeight, rectAccordingToLayoutOrientation.top + dimensionPixelSize, rectAccordingToLayoutOrientation.right, rectAccordingToLayoutOrientation.bottom - dimensionPixelSize2);
            rect2 = new Rect(rectAccordingToLayoutOrientation.left, rectAccordingToLayoutOrientation.top + dimensionPixelSize, iHeight, rectAccordingToLayoutOrientation.bottom - dimensionPixelSize2);
            rect3 = new Rect(rectAccordingToLayoutOrientation.left, rectAccordingToLayoutOrientation.top + dimensionPixelSize, rectAccordingToLayoutOrientation.right, rectAccordingToLayoutOrientation.top + dimensionPixelSize + iHeight);
            rect4 = new Rect(rectAccordingToLayoutOrientation.left, (rectAccordingToLayoutOrientation.bottom - dimensionPixelSize2) - iHeight, rectAccordingToLayoutOrientation.right, rectAccordingToLayoutOrientation.bottom);
        } else {
            rect = new Rect(rectAccordingToLayoutOrientation.left + dimensionPixelSize, rectAccordingToLayoutOrientation.top, rectAccordingToLayoutOrientation.right - dimensionPixelSize2, rectAccordingToLayoutOrientation.top + iHeight);
            rect2 = new Rect(rectAccordingToLayoutOrientation.left + dimensionPixelSize, rectAccordingToLayoutOrientation.bottom - iHeight, rectAccordingToLayoutOrientation.right - dimensionPixelSize2, rectAccordingToLayoutOrientation.bottom);
            rect3 = new Rect(rectAccordingToLayoutOrientation.left + dimensionPixelSize, rectAccordingToLayoutOrientation.top, rectAccordingToLayoutOrientation.left + dimensionPixelSize + iHeight, rectAccordingToLayoutOrientation.bottom);
            rect4 = new Rect((rectAccordingToLayoutOrientation.right - dimensionPixelSize2) - iHeight, rectAccordingToLayoutOrientation.top, rectAccordingToLayoutOrientation.right - dimensionPixelSize2, rectAccordingToLayoutOrientation.bottom);
        }
        RotatableToast.setToastLayoutParams(new RotatableToast.ToastLayoutParams(iMax, iMin, rect, rect2), new RotatableToast.ToastLayoutParams(iMax, iMin, rect3, rect4));
    }
}
