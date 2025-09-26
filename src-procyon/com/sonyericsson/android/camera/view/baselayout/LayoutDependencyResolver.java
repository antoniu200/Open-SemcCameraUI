// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import android.util.DisplayMetrics;
import com.sonyericsson.cameracommon.rotatableview.RotatableToast;
import android.widget.FrameLayout$LayoutParams;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Point;

public class LayoutDependencyResolver
{
    private static final int BASIC_SYSTEM_UI_FLAGS = 1792;
    private static final float SCREEN_ASPECT_EIGHTEEN_NINE = 2.0f;
    private static final float SCREEN_ASPECT_NOT_DEFINED = -1.0f;
    private static final float SCREEN_ASPECT_SIXTEEN_NINE = 1.7777778f;
    private static final String TAG = "LayoutDependencyResolver";
    
    private static Rect cropWithAspectRatio(final Point point, final float n) {
        final float n2 = (float)Math.max(point.x, point.y);
        final float n3 = (float)Math.min(point.x, point.y);
        if (n2 / n3 < n) {
            return new Rect(0, 0, (int)Math.ceil(n2), (int)Math.ceil(n2 / n));
        }
        return new Rect(0, 0, (int)Math.ceil(n * n3), (int)Math.ceil(n3));
    }
    
    public static SystemBarStatus getCurrentSystemBarStatus(final Context context) {
        if (isTablet(context)) {
            return SystemBarStatus.ALWAYS_CANCELED;
        }
        return SystemBarStatus.REGION_OVERLAID;
    }
    
    public static int getLeftItemCount(final Context context) {
        return context.getResources().getInteger(2131361811);
    }
    
    public static int getNavigationBarMargin(final Context context) {
        switch (LayoutDependencyResolver$1.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$LayoutDependencyResolver$SystemBarStatus[getCurrentSystemBarStatus(context).ordinal()]) {
            default: {
                throw new IllegalStateException("getNavigationBarMargin(): Unknown navigation bar status");
            }
            case 2: {
                final int identifier = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (identifier > 0) {
                    return context.getResources().getDimensionPixelSize(identifier);
                }
                return context.getResources().getDimensionPixelSize(2131165455);
            }
            case 1: {
                return 0;
            }
        }
    }
    
    public static Rect getSurfaceViewRect(final Context context, final float n, final ScreenAspect screenAspect) {
        final Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(getViewFinderSize(context));
        if (n > rectAccordingToLayoutOrientation.width() / (float)rectAccordingToLayoutOrientation.height()) {
            if (screenAspect == ScreenAspect.EIGHTEEN_NINE && Math.abs(n - 0.5625f) < 0.01f) {
                final int n2 = rectAccordingToLayoutOrientation.height() - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), 2131165456) - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), 2131165428);
                return new Rect(0, 0, (int)(n2 * n), n2);
            }
            if (Math.abs(n - 1.0f) < 0.01f) {
                return new Rect(0, rectAccordingToLayoutOrientation.width() / 3, rectAccordingToLayoutOrientation.width(), rectAccordingToLayoutOrientation.width() * 4 / 3);
            }
            return new Rect(0, 0, rectAccordingToLayoutOrientation.width(), (int)(rectAccordingToLayoutOrientation.width() / n));
        }
        else {
            if (screenAspect == ScreenAspect.EIGHTEEN_NINE && Math.abs(n - 1.7777778f) < 0.01f) {
                final int n3 = rectAccordingToLayoutOrientation.width() - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), 2131165456) - ResourceUtil.getDimensionPixelSize(context, context.getPackageName(), 2131165428);
                return new Rect(0, 0, n3, (int)(n3 / n));
            }
            if (Math.abs(n - 1.0f) < 0.01f) {
                return new Rect(rectAccordingToLayoutOrientation.height() / 3, 0, rectAccordingToLayoutOrientation.height() * 4 / 3, rectAccordingToLayoutOrientation.height());
            }
            return new Rect(0, 0, (int)(rectAccordingToLayoutOrientation.height() * n), rectAccordingToLayoutOrientation.height());
        }
    }
    
    public static Rect getViewFinderSize(final Context context) {
        final Point point = new Point(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
        final Point point2 = new Point();
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getRealSize(point2);
        final float n = Math.max(point2.x, point2.y) * 1.0f / Math.min(point2.x, point2.y);
        switch (LayoutDependencyResolver$1.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$LayoutDependencyResolver$SystemBarStatus[getCurrentSystemBarStatus(context).ordinal()]) {
            default: {
                throw new IllegalStateException("getViewFinderSize(): Unknown system bar status");
            }
            case 2: {
                return cropWithAspectRatio(point2, n);
            }
            case 1: {
                if (isTablet(context)) {
                    return cropWithAspectRatio(point2, n);
                }
                return cropWithAspectRatio(point, n);
            }
        }
    }
    
    public static boolean isTablet(final Context context) {
        return context.getResources().getBoolean(2131034120);
    }
    
    public static boolean isTenInch(final Context context) {
        return context.getResources().getBoolean(2131034121);
    }
    
    public static void requestToDimSystemUi(final View view) {
        if (view == null) {
            return;
        }
        final int basic_SYSTEM_UI_FLAGS = LayoutDependencyResolver.BASIC_SYSTEM_UI_FLAGS;
        switch (LayoutDependencyResolver$1.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$LayoutDependencyResolver$SystemBarStatus[getCurrentSystemBarStatus(view.getContext()).ordinal()]) {
            case 2: {
                view.setSystemUiVisibility(basic_SYSTEM_UI_FLAGS | 0x1);
            }
            default:
            case 1: {
                view.requestLayout();
            }
        }
    }
    
    public static void requestToRecoverSystemUi(final View view) {
        if (view == null) {
            return;
        }
        final int basic_SYSTEM_UI_FLAGS = LayoutDependencyResolver.BASIC_SYSTEM_UI_FLAGS;
        switch (LayoutDependencyResolver$1.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$LayoutDependencyResolver$SystemBarStatus[getCurrentSystemBarStatus(view.getContext()).ordinal()]) {
            case 2: {
                view.setSystemUiVisibility(basic_SYSTEM_UI_FLAGS);
            }
            default:
            case 1: {
                view.requestLayout();
            }
        }
    }
    
    public static void requestToRemoveSystemUi(final View view) {
        if (view == null) {
            return;
        }
        final int basic_SYSTEM_UI_FLAGS = LayoutDependencyResolver.BASIC_SYSTEM_UI_FLAGS;
        switch (LayoutDependencyResolver$1.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$LayoutDependencyResolver$SystemBarStatus[getCurrentSystemBarStatus(view.getContext()).ordinal()]) {
            case 2: {
                view.setSystemUiVisibility(basic_SYSTEM_UI_FLAGS);
            }
            default:
            case 1: {
                view.requestLayout();
            }
        }
    }
    
    public static void resolveLayoutDependencyOnDevice(final Activity activity, final View view) {
        final Rect viewFinderSize = getViewFinderSize((Context)activity);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)view.getLayoutParams();
        frameLayout$LayoutParams.width = viewFinderSize.width();
        frameLayout$LayoutParams.height = viewFinderSize.height();
        frameLayout$LayoutParams.gravity = 80;
        setupRotatableToast(activity);
    }
    
    public static void setupRotatableToast(final Activity activity) {
        final DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        final int dimensionPixelSize = activity.getResources().getDimensionPixelSize(2131165425);
        final int n = activity.getResources().getDimensionPixelSize(2131165534) + getNavigationBarMargin((Context)activity);
        final Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(getViewFinderSize((Context)activity));
        final LayoutOrientationResolver.LayoutOrientationType orientation = LayoutOrientationResolver.getInstance().getOrientation();
        int n2;
        int n3;
        int n4;
        if (orientation == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            n2 = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            n3 = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
            rectAccordingToLayoutOrientation.offset(0, n2 - rectAccordingToLayoutOrientation.width());
            n4 = rectAccordingToLayoutOrientation.width() / getLeftItemCount((Context)activity);
        }
        else {
            n2 = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
            n3 = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            rectAccordingToLayoutOrientation.offset(0, n3 - rectAccordingToLayoutOrientation.height());
            n4 = rectAccordingToLayoutOrientation.height() / getLeftItemCount((Context)activity);
        }
        Rect rect;
        Rect rect2;
        Rect rect3;
        Rect rect4;
        if (orientation == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            rect = new Rect(rectAccordingToLayoutOrientation.right - n4, rectAccordingToLayoutOrientation.top + dimensionPixelSize, rectAccordingToLayoutOrientation.right, rectAccordingToLayoutOrientation.bottom - n);
            rect2 = new Rect(rectAccordingToLayoutOrientation.left, rectAccordingToLayoutOrientation.top + dimensionPixelSize, n4, rectAccordingToLayoutOrientation.bottom - n);
            rect3 = new Rect(rectAccordingToLayoutOrientation.left, rectAccordingToLayoutOrientation.top + dimensionPixelSize, rectAccordingToLayoutOrientation.right, rectAccordingToLayoutOrientation.top + dimensionPixelSize + n4);
            rect4 = new Rect(rectAccordingToLayoutOrientation.left, rectAccordingToLayoutOrientation.bottom - n - n4, rectAccordingToLayoutOrientation.right, rectAccordingToLayoutOrientation.bottom);
        }
        else {
            rect = new Rect(rectAccordingToLayoutOrientation.left + dimensionPixelSize, rectAccordingToLayoutOrientation.top, rectAccordingToLayoutOrientation.right - n, rectAccordingToLayoutOrientation.top + n4);
            rect2 = new Rect(rectAccordingToLayoutOrientation.left + dimensionPixelSize, rectAccordingToLayoutOrientation.bottom - n4, rectAccordingToLayoutOrientation.right - n, rectAccordingToLayoutOrientation.bottom);
            rect3 = new Rect(rectAccordingToLayoutOrientation.left + dimensionPixelSize, rectAccordingToLayoutOrientation.top, rectAccordingToLayoutOrientation.left + dimensionPixelSize + n4, rectAccordingToLayoutOrientation.bottom);
            rect4 = new Rect(rectAccordingToLayoutOrientation.right - n - n4, rectAccordingToLayoutOrientation.top, rectAccordingToLayoutOrientation.right - n, rectAccordingToLayoutOrientation.bottom);
        }
        RotatableToast.setToastLayoutParams(new RotatableToast.ToastLayoutParams(n2, n3, rect, rect2), new RotatableToast.ToastLayoutParams(n2, n3, rect3, rect4));
    }
    
    public enum ScreenAspect
    {
        private static final ScreenAspect[] $VALUES;
        
        EIGHTEEN_NINE(2.0f), 
        NOT_DEFINED(-1.0f), 
        SIXTEEN_NINE(1.7777778f);
        
        private final float mScreenAspectRatio;
        
        static {
            $VALUES = new ScreenAspect[] { ScreenAspect.NOT_DEFINED, ScreenAspect.SIXTEEN_NINE, ScreenAspect.EIGHTEEN_NINE };
        }
        
        private ScreenAspect(final float mScreenAspectRatio) {
            this.mScreenAspectRatio = mScreenAspectRatio;
        }
        
        public float getScreenAspectRatio() {
            return this.mScreenAspectRatio;
        }
    }
    
    public enum SystemBarStatus
    {
        private static final SystemBarStatus[] $VALUES;
        
        ALWAYS_CANCELED, 
        REGION_OVERLAID;
        
        static {
            $VALUES = new SystemBarStatus[] { SystemBarStatus.ALWAYS_CANCELED, SystemBarStatus.REGION_OVERLAID };
        }
    }
}
