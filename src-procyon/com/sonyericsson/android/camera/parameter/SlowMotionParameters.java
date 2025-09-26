// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.SoftSkin;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.ShutterTrigger;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Hdr;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.EnumMap;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public class SlowMotionParameters extends MainParameters
{
    public SlowMotionParameters(final Context context, final CapturingMode capturingMode, final boolean b, final ModeIndependentParams modeIndependentParams) {
        super(context, capturingMode, b, modeIndependentParams);
    }
    
    @Override
    public EnumMap<UserSettingKey, UserSettingValue> getTargetParameters() {
        final EnumMap enumMap = new EnumMap((Class<K>)UserSettingKey.class);
        for (final UserSettingKey userSettingKey : this.mHolders.keySet()) {
            final UserSettingValue value = this.mHolders.get(userSettingKey).get();
            if (userSettingKey != UserSettingKey.RESOLUTION && userSettingKey != UserSettingKey.VIDEO_SIZE) {
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
    public void set(final Flash flash) {
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
        this.mCapturingModeParams.mSlowMotion.set(slowMotion);
    }
    
    @Override
    public void set(final SoftSkin softSkin) {
    }
    
    @Override
    public void set(final TouchIntention touchIntention) {
    }
    
    @Override
    public void set(final VideoCodec videoCodec) {
        this.mCapturingModeParams.mVideoCodec.set(videoCodec);
    }
    
    @Override
    protected void updateSelectability() {
    }
}
