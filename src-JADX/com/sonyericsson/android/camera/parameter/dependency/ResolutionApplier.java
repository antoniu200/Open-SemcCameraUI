package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResolutionApplier extends DependencyApplier {
    public static final String TAG = "ResolutionApplier";
    private final boolean mIsCorrectionSize;
    private final Resolution mValue;

    public ResolutionApplier(Resolution resolution) {
        this.mIsCorrectionSize = resolution.getPictureRect().width() > 3840;
        this.mValue = resolution;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        CapturingMode capturingMode = (CapturingMode) capturingModeParams.mCapturingMode.get();
        if (capturingMode != CapturingMode.FRONT_PHOTO) {
            if (this.mIsCorrectionSize) {
                if (!Iso.canBeManuallySetWith(capturingMode, (Resolution) capturingModeParams.mResolution.get())) {
                    capturingModeParams.mIso.setOptions(Iso.getOptions(capturingMode, (Resolution) capturingModeParams.mResolution.get(), (FusionMode) capturingModeParams.mFusionMode.get()));
                    if (capturingModeParams.mIso.get() == Iso.ISO_1600 || capturingModeParams.mIso.get() == Iso.ISO_3200 || capturingModeParams.mIso.get() == Iso.ISO_6400) {
                        ParameterUtil.applyRecommendedValue(capturingModeParams.mIso, Iso.ISO_AUTO);
                    }
                }
            } else if (!Iso.canBeManuallySetWith(capturingMode, (Resolution) capturingModeParams.mResolution.get())) {
                ParameterUtil.unavailable(capturingModeParams.mIso, Iso.ISO_AUTO);
            } else {
                capturingModeParams.mIso.setOptions(Iso.getOptions(capturingMode, (Resolution) capturingModeParams.mResolution.get(), (FusionMode) capturingModeParams.mFusionMode.get()));
            }
            if (((FusionMode) capturingModeParams.mFusionMode.get()).getKey().getSelectability() != UserSettingSelectability.FIXED) {
                if (!PlatformCapability.isFusionSupportedWith(capturingModeParams.getActionMode().mCameraId, (Resolution) capturingModeParams.mResolution.get())) {
                    ParameterUtil.unavailable(capturingModeParams.mFusionMode, FusionMode.OFF);
                } else {
                    ParameterUtil.reset(capturingModeParams.mFusionMode);
                }
                DependencyApplier dependencyApplierCreate = DependencyApplier.create(capturingModeParams.mFusionMode.get());
                if (dependencyApplierCreate != null) {
                    dependencyApplierCreate.apply(capturingModeParams);
                }
            }
        }
        AspectRatio aspectRatio = AspectRatio.getAspectRatio(this.mValue.getPictureRect().width(), this.mValue.getPictureRect().height());
        if (aspectRatio == null || aspectRatio == capturingModeParams.mAspectRatio.get()) {
            return;
        }
        capturingModeParams.mAspectRatio.applyRecommendedValue(aspectRatio);
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if (((CapturingMode) capturingModeParams.mCapturingMode.get()) != CapturingMode.FRONT_PHOTO) {
            if (this.mIsCorrectionSize) {
                ParameterUtil.reset(capturingModeParams.mHdr);
            }
            if (!Iso.canBeManuallySetWith((CapturingMode) capturingModeParams.mCapturingMode.get(), (Resolution) capturingModeParams.mResolution.get())) {
                ParameterUtil.reset(capturingModeParams.mIso);
            }
        }
        ParameterUtil.reset(capturingModeParams.mAspectRatio);
    }
}
