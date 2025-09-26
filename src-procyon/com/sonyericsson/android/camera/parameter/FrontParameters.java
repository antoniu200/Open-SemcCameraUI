// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public abstract class FrontParameters extends Parameters
{
    public FrontParameters(final Context context, final CapturingMode capturingMode, final boolean b, final ModeIndependentParams modeIndependentParams) {
        super(capturingMode, b, context, modeIndependentParams);
    }
    
    @Override
    public void set(final Metering metering) {
    }
    
    @Override
    public void set(final PredictiveCapture predictiveCapture) {
    }
    
    public void updateFocusParameters() {
    }
    
    @Override
    protected void updatePhotoLight() {
        this.mIndependentParams.mPhotoLight.setDefaultValue();
        if (this.mIndependentParams.mFlash.get() == Flash.LED_ON) {
            this.mIndependentParams.mFlash.setDefaultValue();
        }
    }
}
