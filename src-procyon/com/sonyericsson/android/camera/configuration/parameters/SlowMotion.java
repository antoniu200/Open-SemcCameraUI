// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import android.content.Context;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

public enum SlowMotion implements UserSettingValue
{
    private static final SlowMotion[] $VALUES;
    
    OFF(-1, 2131690115, 2131690115, VideoSize.HD), 
    STANDARD_SLOW_MOTION(-1, 2131690134, 2131690143, VideoSize.HD_120FPS), 
    SUPER_SLOW_MOTION(-1, 2131690135, 2131690150, VideoSize.HD), 
    SUPER_SLOW_SHOT(-1, 2131690133, 2131690139, VideoSize.HD);
    
    public static final String TAG = "SlowMotion";
    private static final int sParameterTextId = 2131690158;
    private final int mDescriptionTextId;
    private final int mIconId;
    private final int mTextId;
    private final VideoSize mVideoSize;
    
    static {
        $VALUES = new SlowMotion[] { SlowMotion.SUPER_SLOW_MOTION, SlowMotion.SUPER_SLOW_SHOT, SlowMotion.STANDARD_SLOW_MOTION, SlowMotion.OFF };
    }
    
    private SlowMotion(final int mIconId, final int mTextId, final int mDescriptionTextId, final VideoSize mVideoSize) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mDescriptionTextId = mDescriptionTextId;
        this.mVideoSize = mVideoSize;
    }
    
    public static SlowMotion getDefaultValue(final CapturingMode capturingMode) {
        if (capturingMode != CapturingMode.SLOW_MOTION) {
            return SlowMotion.OFF;
        }
        if (PlatformCapability.isSuperSlowMotionSupported(capturingMode.getCameraId())) {
            return SlowMotion.SUPER_SLOW_MOTION;
        }
        return SlowMotion.STANDARD_SLOW_MOTION;
    }
    
    public static SlowMotion[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        if (capturingMode == CapturingMode.SLOW_MOTION) {
            if (PlatformCapability.isSuperSlowMotionSupported(capturingMode.getCameraId())) {
                list.add(SlowMotion.SUPER_SLOW_MOTION);
                list.add(SlowMotion.SUPER_SLOW_SHOT);
            }
            list.add(SlowMotion.STANDARD_SLOW_MOTION);
        }
        else {
            list.add(SlowMotion.OFF);
        }
        return list.toArray(new SlowMotion[0]);
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public String getDescriptionText(final Context context) {
        switch (SlowMotion$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[this.ordinal()]) {
            default: {
                throw new IllegalStateException("This value is not supported.");
            }
            case 3: {
                return context.getString(this.mDescriptionTextId, new Object[] { "120" });
            }
            case 1:
            case 2: {
                return context.getString(this.mDescriptionTextId, new Object[] { "960" });
            }
        }
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.SLOW_MOTION;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690158;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
    
    public VideoSize getVideoSize() {
        return this.mVideoSize;
    }
}
