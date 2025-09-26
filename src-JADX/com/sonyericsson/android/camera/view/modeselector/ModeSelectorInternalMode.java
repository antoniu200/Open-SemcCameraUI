package com.sonyericsson.android.camera.view.modeselector;

import android.content.Context;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ModeSelectorInternalMode {
    MANUAL(CapturingMode.NORMAL),
    SLOW_MOTION(CapturingMode.SLOW_MOTION),
    PORTRAIT_SELFIE(R.drawable.cam_cap_mode_beauty_portrait_icn, R.string.cam_strings_capturing_mode_portraitselfie_txt),
    DUAL_BACKGROUND_DEFOCUS(R.drawable.cam_cap_mode_bokeh_icn, R.string.cam_strings_bokeh_title_txt),
    DUAL_MONOCHROME(R.drawable.cam_cap_mode_monochrome_icn, R.string.cam_strings_monochrome_title_txt);

    public final int iconId;
    public final boolean isExternalApp;
    public final Object tag;
    public final int textId;

    ModeSelectorInternalMode(CapturingMode capturingMode) {
        this.tag = capturingMode;
        this.isExternalApp = false;
        this.iconId = capturingMode.getIconId();
        this.textId = capturingMode.getTextId();
    }

    ModeSelectorInternalMode(int i, int i2) {
        this.tag = null;
        this.isExternalApp = true;
        this.iconId = i;
        this.textId = i2;
    }

    public boolean isSupported(Context context) {
        if (this.isExternalApp) {
            switch (this) {
                case PORTRAIT_SELFIE:
                    return ApplicationLauncher.isPortraitSelfieAvailable(context);
                case DUAL_BACKGROUND_DEFOCUS:
                    return ApplicationLauncher.isBokehSupported();
                case DUAL_MONOCHROME:
                    return ApplicationLauncher.isMonochromeSupported();
                default:
                    return false;
            }
        }
        return CapturingMode.getValidOptions().contains(this.tag);
    }

    public static boolean exists(CapturingMode capturingMode) {
        for (ModeSelectorInternalMode modeSelectorInternalMode : values()) {
            if (capturingMode.equals(modeSelectorInternalMode.tag)) {
                return true;
            }
        }
        return false;
    }
}
