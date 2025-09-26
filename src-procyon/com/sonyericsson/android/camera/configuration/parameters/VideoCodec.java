// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.CamLog;

public enum VideoCodec implements UserSettingValue
{
    private static final VideoCodec[] $VALUES;
    
    H264(-1, 2131690209), 
    H265(-1, 2131690210);
    
    private static final String TAG = "VideoCodec";
    private static final int sParameterTextId = 2131690228;
    private final int mIconId;
    private final int mTextId;
    
    static {
        $VALUES = new VideoCodec[] { VideoCodec.H264, VideoCodec.H265 };
    }
    
    private VideoCodec(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static VideoCodec getDefaultValue() {
        return VideoCodec.H264;
    }
    
    public static VideoCodec[] getOptions(final CapturingMode capturingMode) {
        if (VideoCodec$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()] != 1) {
            return new VideoCodec[0];
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getOptions : H264, H265");
        }
        return new VideoCodec[] { VideoCodec.H264, VideoCodec.H265 };
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.VIDEO_CODEC;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690228;
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
}
