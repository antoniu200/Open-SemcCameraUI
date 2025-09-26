package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.EnumMap;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoParameters extends MainParameters {
    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void prepare() {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(AspectRatio aspectRatio) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Flash flash) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FocusRange focusRange) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Hdr hdr) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Iso iso) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(PredictiveCapture predictiveCapture) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Resolution resolution) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SelfTimer selfTimer) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ShutterSpeed shutterSpeed) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ShutterTrigger shutterTrigger) {
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SlowMotion slowMotion) {
    }

    @Override // com.sonyericsson.android.camera.parameter.MainParameters, com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SoftSkin softSkin) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(TouchIntention touchIntention) {
    }

    public VideoParameters(Context context, CapturingMode capturingMode, boolean z, ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, z, modeIndependentParams);
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void updateSelectability() {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mVideoSize.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.apply(this.mCapturingModeParams);
        }
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(this.mCapturingModeParams.mVideoHdr.get());
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        EnumMap<UserSettingKey, UserSettingValue> enumMap = new EnumMap<>(UserSettingKey.class);
        for (UserSettingKey userSettingKey : this.mHolders.keySet()) {
            UserSettingValue userSettingValue = this.mHolders.get(userSettingKey).get();
            if (userSettingKey != UserSettingKey.RESOLUTION && userSettingKey != UserSettingKey.VIDEO_SHUTTER_TRIGGER) {
                if (userSettingValue == null) {
                    CamLog.d("[" + getClass().getSimpleName() + "] getTargetParameters() invalid value of key: " + userSettingKey);
                }
                enumMap.put((EnumMap<UserSettingKey, UserSettingValue>) userSettingKey, (UserSettingKey) userSettingValue);
            }
        }
        return enumMap;
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoCodec videoCodec) {
        this.mCapturingModeParams.mVideoCodec.set(videoCodec);
    }
}
