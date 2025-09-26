// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.parameter.dependency.DependencyCheckUtil;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.FocusMode;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.EnumMap;
import java.util.Iterator;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.util.List;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public class SuperiorParameters extends MainParameters
{
    public SuperiorParameters(final Context context, final CapturingMode capturingMode, final boolean b, final ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, b, modeIndependentParams);
    }
    
    @Override
    public List<UserSettingValue> getChangedValues() {
        final ArrayList list = new ArrayList();
        for (final UserSettingKey userSettingKey : this.mHolders.keySet()) {
            if (this.mHolders.get(userSettingKey).hasChanged()) {
                switch (SuperiorParameters$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
                    case 1:
                    case 2: {
                        continue;
                    }
                    default: {
                        list.add(((UserSettingValueHolder<Object>)this.mHolders.get(userSettingKey)).get());
                        continue;
                    }
                }
            }
        }
        return list;
    }
    
    @Override
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        final EnumMap enumMap = new EnumMap((Class<K>)UserSettingKey.class);
        for (final UserSettingKey userSettingKey : this.mHolders.keySet()) {
            final UserSettingValue value = this.mHolders.get(userSettingKey).get();
            switch (SuperiorParameters$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
                case 1:
                case 2: {
                    continue;
                }
                default: {
                    if (value == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("[");
                        sb.append(this.getClass().getSimpleName());
                        sb.append("] getTargetParameters() invalid value of key: ");
                        sb.append(userSettingKey);
                        CamLog.d(sb.toString());
                    }
                    enumMap.put(userSettingKey, value);
                    continue;
                }
            }
        }
        return enumMap;
    }
    
    @Override
    protected void prepare() {
    }
    
    @Override
    public void set(final CapturingMode capturingMode) {
        this.updateSelectability();
    }
    
    @Override
    public void set(final FocusRange focusRange) {
    }
    
    @Override
    public void set(final Resolution resolution) {
        final DependencyApplier create = DependencyApplier.create(resolution);
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        super.set(resolution);
        if (create != null) {
            create.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final ShutterSpeed shutterSpeed) {
    }
    
    @Override
    public void set(final SlowMotion slowMotion) {
    }
    
    @Override
    public void set(final VideoCodec videoCodec) {
    }
    
    @Override
    public void set(final VideoShutterTrigger videoShutterTrigger) {
    }
    
    @Override
    protected void updateSelectability() {
        ParameterUtil.forceChange(this.mCapturingModeParams.mEv, Ev.ZERO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mWhiteBalance, WhiteBalance.AUTO);
        final Resolution resolution = this.mCapturingModeParams.mResolution.get();
        final AspectRatio aspectRatio = AspectRatio.getAspectRatio(resolution.getPictureRect().width(), resolution.getPictureRect().height());
        if (aspectRatio != null && aspectRatio != this.mCapturingModeParams.mAspectRatio.get()) {
            this.mCapturingModeParams.mAspectRatio.applyRecommendedValue(aspectRatio);
        }
        ParameterUtil.forceChange(this.mCapturingModeParams.mFocusMode, FocusMode.FACE_DETECTION);
        ParameterUtil.forceChange(this.mCapturingModeParams.mHdr, Hdr.HDR_AUTO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mIso, Iso.ISO_AUTO);
        ParameterUtil.forceChange(this.mCapturingModeParams.mMetering, Metering.getDefaultValue(this.mCapturingModeParams.mCapturingMode.get()));
        ParameterUtil.forceChange(this.mCapturingModeParams.mVideoSize, VideoSize.getDefaultValue(this.mCapturingModeParams.getActionMode(), this.mCapturingModeParams.getConfig(), null, this.mIndependentParams.mDestinationToSave.get().getType()));
        final boolean steadyShotSupported = VideoStabilizer.isSteadyShotSupported(this.mCapturingModeParams.getActionMode().mCameraId, this.mCapturingModeParams.mVideoSize.get());
        final boolean intelligentActiveAvailable = DependencyCheckUtil.isIntelligentActiveAvailable(this.mCapturingModeParams.getActionMode().mCameraId, this.mCapturingModeParams.mVideoSize.get(), this.mCapturingModeParams.mVideoHdr.get());
        if (!steadyShotSupported && !intelligentActiveAvailable) {
            ParameterUtil.unavailable(this.mCapturingModeParams.mVideoStabilizer, VideoStabilizer.OFF);
        }
        else {
            ParameterUtil.reset(this.mCapturingModeParams.mVideoStabilizer);
        }
    }
}
