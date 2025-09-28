package com.sonyericsson.android.camera.util.capability;

import android.content.Context;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResolutionDependence {
    public static final String TAG = "ResolutionDependence";

    public static boolean isDependOnAspect(Context context) {
        return context.getResources().getBoolean(R.bool.use_max_still_capture_resolution_as_default_regardless_of_display_aspect);
    }
}
