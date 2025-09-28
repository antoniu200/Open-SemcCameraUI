package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SuperiorFrontParameters extends FrontParameters {
    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void prepare() {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(FocusRange focusRange) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(ShutterSpeed shutterSpeed) {
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

    public SuperiorFrontParameters(Context context, CapturingMode capturingMode, boolean z, ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, z, modeIndependentParams);
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void updateSelectability() {
        ParameterUtil.forceChange(this.mCapturingModeParams.mEv, Ev.ZERO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mWhiteBalance, WhiteBalance.AUTO);
        Resolution resolution = (Resolution) this.mCapturingModeParams.mResolution.get();
        AspectRatio aspectRatio = AspectRatio.getAspectRatio(resolution.getPictureRect().width(), resolution.getPictureRect().height());
        if (aspectRatio != null && aspectRatio != this.mCapturingModeParams.mAspectRatio.get()) {
            this.mCapturingModeParams.mAspectRatio.applyRecommendedValue(aspectRatio);
        }
        if (PlatformCapability.isFocusSupported(this.capturingMode.getCameraId())) {
            ParameterUtil.forceChange(this.mCapturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        } else {
            ParameterUtil.forceChange(this.mCapturingModeParams.mFocusMode, FocusMode.FIXED);
        }
        ParameterUtil.forceChange(this.mCapturingModeParams.mHdr, Hdr.HDR_AUTO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mIso, Iso.ISO_AUTO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mMetering, Metering.getDefaultValue((CapturingMode) this.mCapturingModeParams.mCapturingMode.get()));
        ParameterUtil.forceChange(this.mCapturingModeParams.mVideoSize, VideoSize.getDefaultValue(this.mCapturingModeParams.getActionMode(), this.mCapturingModeParams.getConfig(), null, ((DestinationToSave) this.mIndependentParams.mDestinationToSave.get()).getType()));
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mVideoSize.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(CapturingMode capturingMode) {
        updateSelectability();
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public List<UserSettingValue> getChangedValues() {
        ArrayList arrayList = new ArrayList();
        for (UserSettingKey userSettingKey : this.mHolders.keySet()) {
            if (this.mHolders.get(userSettingKey).hasChanged()) {
                switch (userSettingKey) {
                    case VIDEO_SHUTTER_TRIGGER:
                    case VIDEO_SIZE:
                        break;
                    default:
                        arrayList.add(this.mHolders.get(userSettingKey).get());
                        break;
                }
            }
        }
        return arrayList;
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        EnumMap<UserSettingKey, UserSettingValue> enumMap = new EnumMap<>(UserSettingKey.class);
        for (UserSettingKey userSettingKey : this.mHolders.keySet()) {
            UserSettingValue userSettingValue = this.mHolders.get(userSettingKey).get();
            switch (userSettingKey) {
                case VIDEO_SHUTTER_TRIGGER:
                case VIDEO_SIZE:
                    break;
                default:
                    if (userSettingValue == null) {
                        CamLog.d("[" + getClass().getSimpleName() + "] getTargetParameters() invalid value of key: " + userSettingKey);
                    }
                    enumMap.put((EnumMap<UserSettingKey, UserSettingValue>) userSettingKey, (UserSettingKey) userSettingValue);
                    break;
            }
        }
        return enumMap;
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(Resolution resolution) {
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(resolution);
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.reset(this.mCapturingModeParams);
        }
        super.set(resolution);
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.apply(this.mCapturingModeParams);
        }
    }
}
