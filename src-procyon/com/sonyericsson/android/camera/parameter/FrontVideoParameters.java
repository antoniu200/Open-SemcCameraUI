// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.EnumMap;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public class FrontVideoParameters extends FrontParameters
{
    public FrontVideoParameters(final Context context, final CapturingMode capturingMode, final boolean b, final ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, b, modeIndependentParams);
    }
    
    @Override
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        final EnumMap enumMap = new EnumMap((Class<K>)UserSettingKey.class);
        for (final UserSettingKey userSettingKey : this.mHolders.keySet()) {
            final UserSettingValue value = this.mHolders.get(userSettingKey).get();
            if (FrontVideoParameters$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] != 1) {
                if (value == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    sb.append(this.getClass().getSimpleName());
                    sb.append("] getTargetParameters() invalid value of key: ");
                    sb.append(userSettingKey);
                    CamLog.d(sb.toString());
                }
                enumMap.put(userSettingKey, value);
            }
        }
        return enumMap;
    }
    
    @Override
    protected void prepare() {
    }
    
    @Override
    public void set(final AspectRatio aspectRatio) {
    }
    
    @Override
    public void set(final CapturingMode capturingMode) {
        if (!PlatformCapability.isSceneRecognitionSupported(this.capturingMode.getCameraId())) {
            ParameterUtil.forceChange(this.mCapturingModeParams.mEv, Ev.ZERO);
            ParameterUtil.forceChange(this.mCapturingModeParams.mWhiteBalance, WhiteBalance.AUTO);
        }
        if (PlatformCapability.isFocusSupported(this.capturingMode.getCameraId())) {
            ParameterUtil.forceChange(this.mCapturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        }
        else {
            ParameterUtil.forceChange(this.mCapturingModeParams.mFocusMode, FocusMode.FIXED);
        }
        ParameterUtil.forceChange(this.mCapturingModeParams.mIso, Iso.ISO_AUTO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mMetering, Metering.getDefaultValue(this.mCapturingModeParams.mCapturingMode.get()));
    }
    
    @Override
    public void set(final FocusRange focusRange) {
    }
    
    @Override
    public void set(final Hdr hdr) {
    }
    
    @Override
    public void set(final Iso iso) {
    }
    
    @Override
    public void set(final Resolution resolution) {
    }
    
    @Override
    public void set(final SelfTimer selfTimer) {
    }
    
    @Override
    public void set(final ShutterSpeed shutterSpeed) {
    }
    
    @Override
    public void set(final ShutterTrigger shutterTrigger) {
    }
    
    @Override
    public void set(final SlowMotion slowMotion) {
    }
    
    @Override
    public void set(final SoftSkin softSkin) {
    }
    
    @Override
    public void set(final TouchIntention touchIntention) {
    }
    
    @Override
    public void set(final VideoCodec videoCodec) {
    }
    
    @Override
    protected void updateSelectability() {
        this.set(this.mCapturingModeParams.mHdr.get());
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mVideoSize.get());
        if (create != null) {
            create.apply(this.mCapturingModeParams);
        }
    }
}
