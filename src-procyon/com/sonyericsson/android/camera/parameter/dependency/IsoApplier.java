// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.Iso;

public class IsoApplier extends DependencyApplier
{
    public static final String TAG = "IsoApplier";
    private final Iso mValue;
    
    public IsoApplier(final Iso mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (this.mValue == Iso.ISO_AUTO) {
            if (PlatformCapability.isStillHdrSupportedWith(capturingModeParams.mCapturingMode.get().getCameraId(), capturingModeParams.mResolution.get())) {
                ParameterUtil.reset(capturingModeParams.mHdr);
            }
        }
        else {
            if (!PlatformCapability.getSupportedAeModes(capturingModeParams.mCapturingMode.get().getCameraId()).contains("semi-auto")) {
                ParameterUtil.applyRecommendedValue(capturingModeParams.mShutterSpeed, ShutterSpeed.AUTO);
            }
            if (PlatformCapability.isStillHdrSupportedWith(capturingModeParams.mCapturingMode.get().getCameraId(), capturingModeParams.mResolution.get())) {
                ParameterUtil.reset(capturingModeParams.mHdr);
                if (capturingModeParams.mIso.get() != Iso.ISO_AUTO && capturingModeParams.mHdr.get() == Hdr.HDR_ON) {
                    ParameterUtil.applyRecommendedValue(capturingModeParams.mHdr, Hdr.HDR_OFF);
                }
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if (this.mValue != Iso.ISO_AUTO) {
            return;
        }
        if (PlatformCapability.isStillHdrSupportedWith(capturingModeParams.mCapturingMode.get().getCameraId(), capturingModeParams.mResolution.get())) {
            ParameterUtil.reset(capturingModeParams.mHdr);
        }
    }
}
