// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

public enum VideoSmileCapture implements UserSettingValue
{
    private static final VideoSmileCapture[] $VALUES;
    
    HIGH(SmileCapture.HIGH, 2131231407), 
    LOW(SmileCapture.LOW, 2131231408), 
    MIDDLE(SmileCapture.MIDDLE, 2131231409), 
    OFF(SmileCapture.OFF, -1);
    
    public static final String TAG = "VideoSmileCapture";
    private static final int sParameterTextId = 2131690249;
    private final int mNotificationIconId;
    private final SmileCapture mSmile;
    private final String mValue;
    
    static {
        $VALUES = new VideoSmileCapture[] { VideoSmileCapture.HIGH, VideoSmileCapture.MIDDLE, VideoSmileCapture.LOW, VideoSmileCapture.OFF };
    }
    
    private VideoSmileCapture(final SmileCapture mSmile, final int mNotificationIconId) {
        this.mSmile = mSmile;
        if (this.mSmile.isSmileCaptureOn()) {
            this.mValue = "on";
        }
        else {
            this.mValue = "off";
        }
        this.mNotificationIconId = mNotificationIconId;
    }
    
    public static VideoSmileCapture[] getOptions(final boolean b, final CapturingMode capturingMode) {
        if (b) {
            return new VideoSmileCapture[] { VideoSmileCapture.OFF };
        }
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).SMILE_DETECTION.get()) {
            return values();
        }
        return new VideoSmileCapture[0];
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
    }
    
    public int getDimenId() {
        return this.mSmile.getDimenId();
    }
    
    @Override
    public int getIconId() {
        return this.mSmile.getIconId();
    }
    
    public int getIntValue() {
        return this.mSmile.getIntValue();
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.VIDEO_SMILE_CAPTURE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690249;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public int getNotificationIconId() {
        return this.mNotificationIconId;
    }
    
    @Override
    public int getTextId() {
        return this.mSmile.getTextId();
    }
    
    @Override
    public String getValue() {
        return this.mValue;
    }
    
    public boolean isSmileCaptureOn() {
        return this.mSmile.isSmileCaptureOn();
    }
}
