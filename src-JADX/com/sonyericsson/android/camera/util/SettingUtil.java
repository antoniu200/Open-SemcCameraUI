package com.sonyericsson.android.camera.util;

import android.provider.Settings;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingUtil {
    private static final String SYSTEM_SETTING_SIDE_TOUCH = "somc.side_sense";
    public static final String TAG = "SettingUtil";

    public static boolean isSideSenseEnabled(boolean z) {
        return (!z || PlatformCapability.isSideTouchSupported()) && Settings.System.getInt(CameraApplication.getContext().getContentResolver(), SYSTEM_SETTING_SIDE_TOUCH, 0) == 1;
    }
}
