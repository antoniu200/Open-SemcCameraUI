package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class MainParameters extends Parameters {
    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SoftSkin softSkin) {
    }

    public MainParameters(Context context, CapturingMode capturingMode, boolean z, ModeIndependentParams modeIndependentParams) {
        super(capturingMode, z, context, modeIndependentParams);
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public void updatePhotoLight() {
        this.mIndependentParams.mPhotoLight.setDefaultValue();
        if (this.mIndependentParams.mFlash.get() == Flash.LED_ON) {
            this.mIndependentParams.mFlash.setDefaultValue();
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public SoftSkin getSoftSkin() {
        return SoftSkin.OFF;
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public void updateFocusParameters() {
        ParameterUtil.applyCurrentValue(this.mCapturingModeParams.mFocusMode, FocusMode.getDefaultValue(this.capturingMode));
    }
}
