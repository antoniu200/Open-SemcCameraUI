package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class FrontParameters extends Parameters {
    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Metering metering) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(PredictiveCapture predictiveCapture) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public void updateFocusParameters() {
    }

    public FrontParameters(Context context, CapturingMode capturingMode, boolean z, ModeIndependentParams modeIndependentParams) {
        super(capturingMode, z, context, modeIndependentParams);
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void updatePhotoLight() {
        this.mIndependentParams.mPhotoLight.setDefaultValue();
        if (this.mIndependentParams.mFlash.get() == Flash.LED_ON) {
            this.mIndependentParams.mFlash.setDefaultValue();
        }
    }
}
