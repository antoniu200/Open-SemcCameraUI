// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.ArrayList;

public enum VideoHdr implements UserSettingValue
{
    private static final VideoHdr[] $VALUES;
    
    HDR_OFF(2131231053, 2131690115, "off"), 
    HDR_ON(2131231054, 2131690116, "on-video-hdr");
    
    private static final int sParameterTextId = 2131690222;
    private final int mIconId;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new VideoHdr[] { VideoHdr.HDR_ON, VideoHdr.HDR_OFF };
    }
    
    private VideoHdr(final int mIconId, final int mTextId, final String mValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mValue = mValue;
    }
    
    public static VideoHdr getDefault() {
        return VideoHdr.HDR_OFF;
    }
    
    public static VideoHdr[] getOptions(final CapturingMode capturingMode) {
        final ArrayList list = new ArrayList();
        if (PlatformCapability.isVideoHdrSupported(capturingMode.getCameraId()) && CapturingMode.VIDEO == capturingMode) {
            list.add(VideoHdr.HDR_ON);
            list.add(VideoHdr.HDR_OFF);
        }
        return (VideoHdr[])list.toArray(new VideoHdr[0]);
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
        return UserSettingKey.VIDEO_HDR;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690222;
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
        return this.mValue;
    }
}
