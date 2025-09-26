package com.sonyericsson.android.camera.parameter.dependency;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class DependencyApplier {
    public static final String TAG = "DependencyApplier";

    public abstract void apply(CapturingModeParams capturingModeParams);

    public abstract void reset(CapturingModeParams capturingModeParams);

    @Nullable
    public static DependencyApplier create(@NonNull UserSettingValue userSettingValue) {
        switch (userSettingValue.getKey()) {
            case HDR:
                return new HdrApplier((Hdr) userSettingValue);
            case ISO:
                return new IsoApplier((Iso) userSettingValue);
            case SHUTTER_TRIGGER:
                return new ShutterTriggerApplier((ShutterTrigger) userSettingValue);
            case VIDEO_SHUTTER_TRIGGER:
                return new VideoShutterTriggerApplier((VideoShutterTrigger) userSettingValue);
            case FOCUS_MODE:
                return new FocusModeApplier((FocusMode) userSettingValue);
            case OBJECT_TRACKING:
                return new ObjectTrackingApplier((ObjectTracking) userSettingValue);
            case VIDEO_SIZE:
                return new VideoSizeApplier((VideoSize) userSettingValue);
            case RESOLUTION:
                return new ResolutionApplier((Resolution) userSettingValue);
            case SHUTTER_SPEED:
                return new ShutterSpeedApplier((ShutterSpeed) userSettingValue);
            case TOUCH_INTENTION:
                return new TouchIntentionApplier((TouchIntention) userSettingValue);
            case VIDEO_HDR:
                return new VideoHdrApplier((VideoHdr) userSettingValue);
            case FUSION_MODE:
                return new FusionModeApplier((FusionMode) userSettingValue);
            case ASPECT_RATIO:
                return new AspectRatioApplier((AspectRatio) userSettingValue);
            default:
                return null;
        }
    }
}
