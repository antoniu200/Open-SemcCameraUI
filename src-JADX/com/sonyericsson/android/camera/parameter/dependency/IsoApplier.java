package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IsoApplier extends DependencyApplier {
    public static final String TAG = "IsoApplier";
    private final Iso mValue;

    public IsoApplier(Iso iso) {
        this.mValue = iso;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        if (this.mValue == Iso.ISO_AUTO) {
            if (PlatformCapability.isStillHdrSupportedWith(((CapturingMode) capturingModeParams.mCapturingMode.get()).getCameraId(), (Resolution) capturingModeParams.mResolution.get())) {
                ParameterUtil.reset(capturingModeParams.mHdr);
                return;
            }
            return;
        }
        if (!PlatformCapability.getSupportedAeModes(((CapturingMode) capturingModeParams.mCapturingMode.get()).getCameraId()).contains(CameraParameters.AE_MODE_SEMI_AUTO)) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mShutterSpeed, ShutterSpeed.AUTO);
        }
        if (PlatformCapability.isStillHdrSupportedWith(((CapturingMode) capturingModeParams.mCapturingMode.get()).getCameraId(), (Resolution) capturingModeParams.mResolution.get())) {
            ParameterUtil.reset(capturingModeParams.mHdr);
            if (capturingModeParams.mIso.get() == Iso.ISO_AUTO || capturingModeParams.mHdr.get() != Hdr.HDR_ON) {
                return;
            }
            ParameterUtil.applyRecommendedValue(capturingModeParams.mHdr, Hdr.HDR_OFF);
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if (this.mValue == Iso.ISO_AUTO && PlatformCapability.isStillHdrSupportedWith(((CapturingMode) capturingModeParams.mCapturingMode.get()).getCameraId(), (Resolution) capturingModeParams.mResolution.get())) {
            ParameterUtil.reset(capturingModeParams.mHdr);
        }
    }
}
