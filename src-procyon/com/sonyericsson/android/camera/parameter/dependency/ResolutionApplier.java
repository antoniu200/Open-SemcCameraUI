// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;

public class ResolutionApplier extends DependencyApplier
{
    public static final String TAG = "ResolutionApplier";
    private final boolean mIsCorrectionSize;
    private final Resolution mValue;
    
    public ResolutionApplier(final Resolution mValue) {
        this.mIsCorrectionSize = (mValue.getPictureRect().width() > 3840);
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        final CapturingMode capturingMode = capturingModeParams.mCapturingMode.get();
        if (capturingMode != CapturingMode.FRONT_PHOTO) {
            if (this.mIsCorrectionSize) {
                if (!Iso.canBeManuallySetWith(capturingMode, capturingModeParams.mResolution.get())) {
                    capturingModeParams.mIso.setOptions(Iso.getOptions(capturingMode, capturingModeParams.mResolution.get(), capturingModeParams.mFusionMode.get()));
                    if (capturingModeParams.mIso.get() == Iso.ISO_1600 || capturingModeParams.mIso.get() == Iso.ISO_3200 || capturingModeParams.mIso.get() == Iso.ISO_6400) {
                        ParameterUtil.applyRecommendedValue(capturingModeParams.mIso, Iso.ISO_AUTO);
                    }
                }
            }
            else if (!Iso.canBeManuallySetWith(capturingMode, capturingModeParams.mResolution.get())) {
                ParameterUtil.unavailable(capturingModeParams.mIso, Iso.ISO_AUTO);
            }
            else {
                capturingModeParams.mIso.setOptions(Iso.getOptions(capturingMode, capturingModeParams.mResolution.get(), capturingModeParams.mFusionMode.get()));
            }
            if (capturingModeParams.mFusionMode.get().getKey().getSelectability() != UserSettingSelectability.FIXED) {
                if (!PlatformCapability.isFusionSupportedWith(capturingModeParams.getActionMode().mCameraId, capturingModeParams.mResolution.get())) {
                    ParameterUtil.unavailable(capturingModeParams.mFusionMode, FusionMode.OFF);
                }
                else {
                    ParameterUtil.reset(capturingModeParams.mFusionMode);
                }
                final DependencyApplier create = DependencyApplier.create(capturingModeParams.mFusionMode.get());
                if (create != null) {
                    create.apply(capturingModeParams);
                }
            }
        }
        final AspectRatio aspectRatio = AspectRatio.getAspectRatio(this.mValue.getPictureRect().width(), this.mValue.getPictureRect().height());
        if (aspectRatio != null && aspectRatio != capturingModeParams.mAspectRatio.get()) {
            capturingModeParams.mAspectRatio.applyRecommendedValue(aspectRatio);
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if (capturingModeParams.mCapturingMode.get() != CapturingMode.FRONT_PHOTO) {
            if (this.mIsCorrectionSize) {
                ParameterUtil.reset(capturingModeParams.mHdr);
            }
            if (!Iso.canBeManuallySetWith(capturingModeParams.mCapturingMode.get(), capturingModeParams.mResolution.get())) {
                ParameterUtil.reset(capturingModeParams.mIso);
            }
        }
        ParameterUtil.reset(capturingModeParams.mAspectRatio);
    }
}
