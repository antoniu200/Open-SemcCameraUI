// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

public enum ModeSelectorInternalMode
{
    private static final ModeSelectorInternalMode[] $VALUES;
    
    DUAL_BACKGROUND_DEFOCUS(2131230876, 2131689650), 
    DUAL_MONOCHROME(2131230882, 2131689951), 
    MANUAL(CapturingMode.NORMAL), 
    PORTRAIT_SELFIE(2131230875, 2131689680), 
    SLOW_MOTION(CapturingMode.SLOW_MOTION);
    
    public final int iconId;
    public final boolean isExternalApp;
    public final Object tag;
    public final int textId;
    
    static {
        $VALUES = new ModeSelectorInternalMode[] { ModeSelectorInternalMode.MANUAL, ModeSelectorInternalMode.SLOW_MOTION, ModeSelectorInternalMode.PORTRAIT_SELFIE, ModeSelectorInternalMode.DUAL_BACKGROUND_DEFOCUS, ModeSelectorInternalMode.DUAL_MONOCHROME };
    }
    
    private ModeSelectorInternalMode(final int iconId, final int textId) {
        this.tag = null;
        this.isExternalApp = true;
        this.iconId = iconId;
        this.textId = textId;
    }
    
    private ModeSelectorInternalMode(final CapturingMode tag) {
        this.tag = tag;
        this.isExternalApp = false;
        this.iconId = tag.getIconId();
        this.textId = tag.getTextId();
    }
    
    public static boolean exists(final CapturingMode capturingMode) {
        final ModeSelectorInternalMode[] values = values();
        for (int length = values.length, i = 0; i < length; ++i) {
            if (capturingMode.equals(values[i].tag)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSupported(final Context context) {
        if (!this.isExternalApp) {
            return CapturingMode.getValidOptions().contains(this.tag);
        }
        switch (ModeSelectorInternalMode$1.$SwitchMap$com$sonyericsson$android$camera$view$modeselector$ModeSelectorInternalMode[this.ordinal()]) {
            default: {
                return false;
            }
            case 3: {
                return ApplicationLauncher.isMonochromeSupported();
            }
            case 2: {
                return ApplicationLauncher.isBokehSupported();
            }
            case 1: {
                return ApplicationLauncher.isPortraitSelfieAvailable(context);
            }
        }
    }
}
