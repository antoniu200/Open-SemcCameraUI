package com.sonyericsson.android.camera.parameter;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.EnumMap;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FrontVideoParameters extends FrontParameters {
    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void prepare() {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(AspectRatio aspectRatio) {
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

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(SoftSkin softSkin) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(TouchIntention touchIntention) {
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(VideoCodec videoCodec) {
    }

    public FrontVideoParameters(Context context, CapturingMode capturingMode, boolean z, ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, z, modeIndependentParams);
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    protected void updateSelectability() {
        set((Hdr) this.mCapturingModeParams.mHdr.get());
        DependencyApplier dependencyApplierCreate = DependencyApplier.create(this.mCapturingModeParams.mVideoSize.get());
        if (dependencyApplierCreate != null) {
            dependencyApplierCreate.apply(this.mCapturingModeParams);
        }
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters, com.sonyericsson.android.camera.configuration.parameters.UserSettingApplicable
    public void set(CapturingMode capturingMode) {
        if (!PlatformCapability.isSceneRecognitionSupported(this.capturingMode.getCameraId())) {
            ParameterUtil.forceChange(this.mCapturingModeParams.mEv, Ev.ZERO);
            ParameterUtil.forceChange(this.mCapturingModeParams.mWhiteBalance, WhiteBalance.AUTO);
        }
        if (PlatformCapability.isFocusSupported(this.capturingMode.getCameraId())) {
            ParameterUtil.forceChange(this.mCapturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        } else {
            ParameterUtil.forceChange(this.mCapturingModeParams.mFocusMode, FocusMode.FIXED);
        }
        ParameterUtil.forceChange(this.mCapturingModeParams.mIso, Iso.ISO_AUTO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mMetering, Metering.getDefaultValue((CapturingMode) this.mCapturingModeParams.mCapturingMode.get()));
    }

    @Override // com.sonyericsson.android.camera.parameter.Parameters
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        EnumMap<UserSettingKey, UserSettingValue> enumMap = new EnumMap<>(UserSettingKey.class);
        for (UserSettingKey userSettingKey : this.mHolders.keySet()) {
            UserSettingValue userSettingValue = this.mHolders.get(userSettingKey).get();
            if (AnonymousClass1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] != 1) {
                if (userSettingValue == null) {
                    CamLog.d("[" + getClass().getSimpleName() + "] getTargetParameters() invalid value of key: " + userSettingKey);
                }
                enumMap.put((EnumMap<UserSettingKey, UserSettingValue>) userSettingKey, (UserSettingKey) userSettingValue);
            }
        }
        return enumMap;
    }

    /* renamed from: com.sonyericsson.android.camera.parameter.FrontVideoParameters$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey = new int[UserSettingKey.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.VIDEO_SHUTTER_TRIGGER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }
}
