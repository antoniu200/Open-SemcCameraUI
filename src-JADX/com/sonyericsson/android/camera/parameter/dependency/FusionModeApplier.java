package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FusionModeApplier extends DependencyApplier {
    private static final String TAG = "FusionModeApplier";
    private final FusionMode mValue;

    public FusionModeApplier(FusionMode fusionMode) {
        this.mValue = fusionMode;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        if (this.mValue == FusionMode.ON && PlatformCapability.isStillHdrSupportedWith(((CapturingMode) capturingModeParams.mCapturingMode.get()).getCameraId(), (Resolution) capturingModeParams.mResolution.get()) && capturingModeParams.mHdr.get() == Hdr.HDR_ON) {
            ParameterUtil.reset(capturingModeParams.mHdr);
            ParameterUtil.applyRecommendedValue(capturingModeParams.mHdr, Hdr.HDR_OFF);
        }
        capturingModeParams.mIso.setOptions(Iso.getOptions((CapturingMode) capturingModeParams.mCapturingMode.get(), (Resolution) capturingModeParams.mResolution.get(), (FusionMode) capturingModeParams.mFusionMode.get()));
        capturingModeParams.mIso.set(Iso.adjustToSupportedValue((Iso) capturingModeParams.mIso.get(), (Iso[]) capturingModeParams.mIso.getOptions()));
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if (this.mValue == FusionMode.OFF && PlatformCapability.isStillHdrSupportedWith(((CapturingMode) capturingModeParams.mCapturingMode.get()).getCameraId(), (Resolution) capturingModeParams.mResolution.get())) {
            ParameterUtil.reset(capturingModeParams.mHdr);
        }
    }
}
