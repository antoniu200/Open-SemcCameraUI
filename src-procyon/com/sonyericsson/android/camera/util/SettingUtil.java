// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import android.provider.Settings$System;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

public class SettingUtil
{
    private static final String SYSTEM_SETTING_SIDE_TOUCH = "somc.side_sense";
    public static final String TAG = "SettingUtil";
    
    public static boolean isSideSenseEnabled(final boolean b) {
        final boolean b2 = false;
        if (b && !PlatformCapability.isSideTouchSupported()) {
            return false;
        }
        boolean b3 = b2;
        if (Settings$System.getInt(CameraApplication.getContext().getContentResolver(), "somc.side_sense", 0) == 1) {
            b3 = true;
        }
        return b3;
    }
}
