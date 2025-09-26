// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder;

import android.view.WindowManager;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Point;

public class LayoutDependencyResolver
{
    private static final int BASIC_SYSTEM_UI_FLAGS = 7936;
    public static final String TAG = "LayoutDependencyResolver";
    private static float VIEWFINDER_ASPECT_RATIO = 1.7777778f;
    
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
    
    public static Rect getSurfaceRect(final Context context, final float n) {
        final Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(getViewFinderSize(context));
        if (n > rectAccordingToLayoutOrientation.width() / (float)rectAccordingToLayoutOrientation.height()) {
            return new Rect(0, 0, rectAccordingToLayoutOrientation.width(), (int)(rectAccordingToLayoutOrientation.width() / n));
        }
        return new Rect(0, 0, (int)(rectAccordingToLayoutOrientation.height() * n), rectAccordingToLayoutOrientation.height());
    }
    
    public static int getSystemBarMargin(final Context context) {
        switch (LayoutDependencyResolver$1.$SwitchMap$com$sonyericsson$cameracommon$viewfinder$LayoutDependencyResolver$SystemBarStatus[getCurrentSystemBarStatus(context).ordinal()]) {
            default: {
                throw new IllegalStateException("getSystemBarMargin(): Unknown system bar status");
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
    
    public static Rect getViewFinderSize(final Context context) {
        final Point point = new Point(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels);
        final Point point2 = new Point();
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getRealSize(point2);
        switch (LayoutDependencyResolver$1.$SwitchMap$com$sonyericsson$cameracommon$viewfinder$LayoutDependencyResolver$SystemBarStatus[getCurrentSystemBarStatus(context).ordinal()]) {
            default: {
                throw new IllegalStateException("getViewFinderSize(): Unknown system bar status");
            }
            case 2: {
                return cropWithAspectRatio(point2, LayoutDependencyResolver.VIEWFINDER_ASPECT_RATIO);
            }
            case 1: {
                if (isTablet(context)) {
                    return cropWithAspectRatio(point2, LayoutDependencyResolver.VIEWFINDER_ASPECT_RATIO);
                }
                return cropWithAspectRatio(point, LayoutDependencyResolver.VIEWFINDER_ASPECT_RATIO);
            }
        }
    }
    
    public static boolean isTablet(final Context context) {
        return context.getResources().getBoolean(2131034120);
    }
    
    public static boolean isTenInch(final Context context) {
        return context.getResources().getBoolean(2131034121);
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
