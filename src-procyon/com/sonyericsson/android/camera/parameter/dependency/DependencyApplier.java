// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import android.support.annotation.Nullable;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;

public abstract class DependencyApplier
{
    public static final String TAG = "DependencyApplier";
    
    @Nullable
    public static DependencyApplier create(@NonNull final UserSettingValue userSettingValue) {
        switch (DependencyApplier$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingValue.getKey().ordinal()]) {
            default: {
                return null;
            }
            case 13: {
                return new AspectRatioApplier((AspectRatio)userSettingValue);
            }
            case 12: {
                return new FusionModeApplier((FusionMode)userSettingValue);
            }
            case 11: {
                return new VideoHdrApplier((VideoHdr)userSettingValue);
            }
            case 10: {
                return new TouchIntentionApplier((TouchIntention)userSettingValue);
            }
            case 9: {
                return new ShutterSpeedApplier((ShutterSpeed)userSettingValue);
            }
            case 8: {
                return new ResolutionApplier((Resolution)userSettingValue);
            }
            case 7: {
                return new VideoSizeApplier((VideoSize)userSettingValue);
            }
            case 6: {
                return new ObjectTrackingApplier((ObjectTracking)userSettingValue);
            }
            case 5: {
                return new FocusModeApplier((FocusMode)userSettingValue);
            }
            case 4: {
                return new VideoShutterTriggerApplier((VideoShutterTrigger)userSettingValue);
            }
            case 3: {
                return new ShutterTriggerApplier((ShutterTrigger)userSettingValue);
            }
            case 2: {
                return new IsoApplier((Iso)userSettingValue);
            }
            case 1: {
                return new HdrApplier((Hdr)userSettingValue);
            }
        }
    }
    
    public abstract void apply(final CapturingModeParams p0);
    
    public abstract void reset(final CapturingModeParams p0);
}
