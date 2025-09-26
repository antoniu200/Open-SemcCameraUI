package com.sonyericsson.android.camera.parameter.dependency;

import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.parameter.ParameterUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HdrApplier extends DependencyApplier {
    public static final String TAG = "HdrApplier";
    private final Hdr mValue;

    public HdrApplier(@NonNull Hdr hdr) {
        this.mValue = hdr;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
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
            DependencyApplier dependencyApplierCreate = DependencyApplier.create(capturingModeParams.mFusionMode.get());
            if (dependencyApplierCreate != null) {
                dependencyApplierCreate.apply(capturingModeParams);
            }
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        if (this.mValue != Hdr.HDR_OFF) {
            return;
        }
        if (capturingModeParams.mCapturingMode.get() != CapturingMode.FRONT_PHOTO) {
            ParameterUtil.reset(capturingModeParams.mIso);
        }
        UserSettingSelectability selectability = ((FusionMode) capturingModeParams.mFusionMode.get()).getKey().getSelectability();
        if (selectability == UserSettingSelectability.FIXED || selectability == UserSettingSelectability.UNAVAILABLE) {
            return;
        }
        ParameterUtil.reset(capturingModeParams.mFusionMode);
    }
}
