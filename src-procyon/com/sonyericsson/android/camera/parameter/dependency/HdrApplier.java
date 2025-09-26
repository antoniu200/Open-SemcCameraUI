// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;

public class HdrApplier extends DependencyApplier
{
    public static final String TAG = "HdrApplier";
    private final Hdr mValue;
    
    public HdrApplier(@NonNull final Hdr mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (this.mValue == Hdr.HDR_OFF) {
            return;
        }
        if (capturingModeParams.mCapturingMode.get() != CapturingMode.FRONT_PHOTO) {
            ParameterUtil.reset(capturingModeParams.mIso);
            ParameterUtil.applyRecommendedValue(capturingModeParams.mIso, Iso.ISO_AUTO);
        }
        if (capturingModeParams.mHdr.get() == Hdr.HDR_ON && capturingModeParams.mFusionMode.get() == FusionMode.ON) {
            ParameterUtil.reset(capturingModeParams.mFusionMode);
            ParameterUtil.applyRecommendedValue(capturingModeParams.mFusionMode, FusionMode.OFF);
            final DependencyApplier create = DependencyApplier.create(capturingModeParams.mFusionMode.get());
            if (create != null) {
                create.apply(capturingModeParams);
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        if (this.mValue != Hdr.HDR_OFF) {
            return;
        }
        if (capturingModeParams.mCapturingMode.get() != CapturingMode.FRONT_PHOTO) {
            ParameterUtil.reset(capturingModeParams.mIso);
        }
        final UserSettingSelectability selectability = capturingModeParams.mFusionMode.get().getKey().getSelectability();
        if (selectability != UserSettingSelectability.FIXED && selectability != UserSettingSelectability.UNAVAILABLE) {
            ParameterUtil.reset(capturingModeParams.mFusionMode);
        }
    }
}
