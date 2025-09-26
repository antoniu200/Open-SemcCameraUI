// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.parameter.dependency.DependencyApplier;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.PredictiveCapture;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.EnumMap;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public class NormalParameters extends MainParameters
{
    public NormalParameters(final Context context, final CapturingMode capturingMode, final boolean b, final ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, b, modeIndependentParams);
    }
    
    @Override
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        final EnumMap enumMap = new EnumMap((Class<K>)UserSettingKey.class);
        for (final UserSettingKey userSettingKey : this.mHolders.keySet()) {
            final UserSettingValue value = this.mHolders.get(userSettingKey).get();
            if (userSettingKey != UserSettingKey.VIDEO_SIZE && userSettingKey != UserSettingKey.VIDEO_SHUTTER_TRIGGER) {
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
    public void set(final FocusRange focusRange) {
        super.set(focusRange);
    }
    
    @Override
    public void set(final PredictiveCapture predictiveCapture) {
    }
    
    @Override
    public void set(final Resolution resolution) {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mResolution.get());
        if (create != null) {
            create.reset(this.mCapturingModeParams);
        }
        super.set(resolution);
        final DependencyApplier create2 = DependencyApplier.create(resolution);
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
    }
    
    @Override
    public void set(final ShutterSpeed shutterSpeed) {
        super.set(shutterSpeed);
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
    public void set(final VideoSize videoSize) {
    }
    
    @Override
    public void set(final VideoStabilizer videoStabilizer) {
    }
    
    @Override
    protected void updateSelectability() {
        final DependencyApplier create = DependencyApplier.create(this.mCapturingModeParams.mResolution.get());
        if (create != null) {
            create.apply(this.mCapturingModeParams);
        }
        final DependencyApplier create2 = DependencyApplier.create(this.mCapturingModeParams.mShutterTrigger.get());
        if (create2 != null) {
            create2.apply(this.mCapturingModeParams);
        }
        final DependencyApplier create3 = DependencyApplier.create(this.mCapturingModeParams.mFusionMode.get());
        if (create3 != null) {
            create3.apply(this.mCapturingModeParams);
        }
        if (CamLog.VERBOSE) {
            for (final UserSettingKey obj : UserSettingKey.values()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("key = ");
                sb.append(obj);
                sb.append(" , Selectability = ");
                sb.append(obj.getSelectability());
                CamLog.d(sb.toString());
            }
        }
    }
}
