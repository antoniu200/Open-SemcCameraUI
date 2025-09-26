// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.parameter.ParameterUtil;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;

public class ShutterSpeedApplier extends DependencyApplier
{
    public static final String TAG = "ShutterSpeedApplier";
    private final ShutterSpeed mValue;
    
    public ShutterSpeedApplier(final ShutterSpeed mValue) {
        this.mValue = mValue;
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        if (this.mValue == ShutterSpeed.AUTO) {
            return;
        }
        if (!PlatformCapability.getSupportedAeModes(capturingModeParams.mCapturingMode.get().getCameraId()).contains("semi-auto")) {
            ParameterUtil.applyRecommendedValue(capturingModeParams.mIso, Iso.ISO_AUTO);
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
    }
}
