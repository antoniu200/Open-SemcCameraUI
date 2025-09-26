package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.EnumMap;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class NormalParameters extends MainParameters {
    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void prepare() {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(PredictiveCapture predictiveCapture) {
    }

    @Override // com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SlowMotion slowMotion) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoCodec videoCodec) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoShutterTrigger videoShutterTrigger) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoSize videoSize) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoStabilizer videoStabilizer) {
    }

    public NormalParameters(Context context, CapturingMode capturingMode, boolean z, ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, z, modeIndependentParams);
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void updateSelectability() {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mResolution.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.apply(this.mCapturingModeParams);
        }
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(this.mCapturingModeParams.mShutterTrigger.get());
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
        DependencyApplier dependencyApplierCreate3 = DependencyApplier.create(this.mCapturingModeParams.mFusionMode.get());
        if (dependencyApplierCreate3 != null) {
            dependencyApplierCreate3.apply(this.mCapturingModeParams);
        }
        if (CamLog.VERBOSE) {
            for (UserSettingKey userSettingKey : UserSettingKey.values()) {
                CamLog.d("key = " + userSettingKey + " , Selectability = " + userSettingKey.getSelectability());
            }
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        EnumMap<UserSettingKey, UserSettingValue> enumMap = new EnumMap<>(UserSettingKey.class);
        for (UserSettingKey userSettingKey : this.mHolders.keySet()) {
            UserSettingValue userSettingValue = this.mHolders.get(userSettingKey).get();
            if (userSettingKey != UserSettingKey.VIDEO_SIZE && userSettingKey != UserSettingKey.VIDEO_SHUTTER_TRIGGER) {
                if (userSettingValue == null) {
                    CamLog.d("[" + getClass().getSimpleName() + "] getTargetParameters() invalid value of key: " + userSettingKey);
                }
                enumMap.put((EnumMap<UserSettingKey, UserSettingValue>) userSettingKey, (UserSettingKey) userSettingValue);
            }
        }
        return enumMap;
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Resolution resolution) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mResolution.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        super.set(resolution);
        DependencyApplier dependencyApplierCreate2 = DependencyApplier.create(resolution);
        if (dependencyApplierCreate2 != null) {
            dependencyApplierCreate2.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FocusRange focusRange) {
        super.set(focusRange);
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ShutterSpeed shutterSpeed) {
        super.set(shutterSpeed);
    }
}
