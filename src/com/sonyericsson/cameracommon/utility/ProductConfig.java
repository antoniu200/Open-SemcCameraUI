package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ProductConfig {
    public static final String TAG = "ProductConfig";

    private ProductConfig() {
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.is_tablet);
    }

    public static boolean isPhone(Context context) {
        return !isTablet(context);
    }

    public static int getMountAngle(Context context) {
        int i;
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case 0:
            default:
                i = 0;
                break;
            case 1:
                i = 270;
                break;
            case 2:
                i = 180;
                break;
            case 3:
                i = 90;
                break;
        }
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point.x > point.y ? i : i + 270;
    }

    public static boolean shouldReverseZoomDirection(Context context, boolean z) {
        return !isTablet(context) && z;
    }
}
