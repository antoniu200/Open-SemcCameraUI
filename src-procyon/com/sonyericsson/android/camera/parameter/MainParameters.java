// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public abstract class MainParameters extends Parameters
{
    public MainParameters(final Context context, final CapturingMode capturingMode, final boolean b, final ModeIndependentParams modeIndependentParams) {
        super(capturingMode, b, context, modeIndependentParams);
    }
    
    @Override
    public SoftSkin getSoftSkin() {
        return SoftSkin.OFF;
    }
    
    @Override
    public void set(final SoftSkin softSkin) {
    }
    
    public void updateFocusParameters() {
        ParameterUtil.applyCurrentValue(this.mCapturingModeParams.mFocusMode, FocusMode.getDefaultValue(this.capturingMode));
    }
    
    public void updatePhotoLight() {
        this.mIndependentParams.mPhotoLight.setDefaultValue();
        if (this.mIndependentParams.mFlash.get() == Flash.LED_ON) {
            this.mIndependentParams.mFlash.setDefaultValue();
        }
    }
}
