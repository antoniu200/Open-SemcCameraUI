// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.graphics.Point;
import android.view.WindowManager;
import android.content.Context;

public class ProductConfig
{
    public static final String TAG = "ProductConfig";
    
    private ProductConfig() {
    }
    
    public static int getMountAngle(final Context context) {
        final WindowManager windowManager = (WindowManager)context.getSystemService("window");
        final int rotation = windowManager.getDefaultDisplay().getRotation();
        boolean b = false;
        int n = 0;
        switch (rotation) {
            default: {
                n = 0;
                break;
            }
            case 3: {
                n = 90;
                break;
            }
            case 2: {
                n = 180;
                break;
            }
            case 1: {
                n = 270;
                break;
            }
        }
        final Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        if (point.x > point.y) {
            b = true;
        }
        if (b) {
            return n;
        }
        return n + 270;
    }
    
    public static boolean isPhone(final Context context) {
        return isTablet(context) ^ true;
    }
    
    public static boolean isTablet(final Context context) {
        return context.getResources().getBoolean(2131034120);
    }
    
    public static boolean shouldReverseZoomDirection(final Context context, final boolean b) {
        return !isTablet(context) && b;
    }
}
