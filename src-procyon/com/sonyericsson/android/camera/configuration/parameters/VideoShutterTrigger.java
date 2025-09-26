// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum VideoShutterTrigger implements UserSettingValue
{
    private static final VideoShutterTrigger[] $VALUES;
    
    OFF(2131690115, VideoSmileCapture.OFF), 
    SMILE_SHUTTER(2131690163, VideoSmileCapture.MIDDLE);
    
    public static final String TAG = "VideoShutterTrigger";
    private static final int sParameterTextId = 2131690244;
    private final int mTextId;
    private VideoSmileCapture mVideoSmileCapture;
    
    static {
        $VALUES = new VideoShutterTrigger[] { VideoShutterTrigger.SMILE_SHUTTER, VideoShutterTrigger.OFF };
    }
    
    private VideoShutterTrigger(final int mTextId, final VideoSmileCapture mVideoSmileCapture) {
        this.mVideoSmileCapture = mVideoSmileCapture;
        this.mTextId = mTextId;
    }
    
    public static VideoShutterTrigger getDefaultValue(final CapturingMode capturingMode, final boolean b) {
        return getOptions(capturingMode, b)[0];
    }
    
    public static VideoShutterTrigger[] getOptions(final CapturingMode capturingMode, final boolean b) {
        if (b) {
            return new VideoShutterTrigger[] { VideoShutterTrigger.OFF };
        }
        return values();
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return -1;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.VIDEO_SHUTTER_TRIGGER;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690244;
    }
    
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int getTextId() {
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.toString();
    }
    
    public VideoSmileCapture getVideoSmileCapture() {
        return this.mVideoSmileCapture;
    }
}
