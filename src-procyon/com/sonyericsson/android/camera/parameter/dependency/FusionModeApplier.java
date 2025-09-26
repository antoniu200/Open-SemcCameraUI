// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;

public class FusionModeApplier extends DependencyApplier
{
    private static final String TAG = "FusionModeApplier";
    private final FusionMode mValue;
    
    public FusionModeApplier(final FusionMode mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (this.mValue == FusionMode.ON && PlatformCapability.isStillHdrSupportedWith(capturingModeParams.mCapturingMode.get().getCameraId(), capturingModeParams.mResolution.get()) && capturingModeParams.mHdr.get() == Hdr.HDR_ON) {
            ParameterUtil.reset(capturingModeParams.mHdr);
            ParameterUtil.applyRecommendedValue(capturingModeParams.mHdr, Hdr.HDR_OFF);
        }
        capturingModeParams.mIso.setOptions(Iso.getOptions(capturingModeParams.mCapturingMode.get(), capturingModeParams.mResolution.get(), capturingModeParams.mFusionMode.get()));
        capturingModeParams.mIso.set(Iso.adjustToSupportedValue(capturingModeParams.mIso.get(), capturingModeParams.mIso.getOptions()));
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if (this.mValue == FusionMode.OFF && PlatformCapability.isStillHdrSupportedWith(capturingModeParams.mCapturingMode.get().getCameraId(), capturingModeParams.mResolution.get())) {
            ParameterUtil.reset(capturingModeParams.mHdr);
        }
    }
}
