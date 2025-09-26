// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;

public enum SmileCapture implements UserSettingValue
{
    private static final SmileCapture[] $VALUES;
    
    HIGH(-1, 2131690159, 2131231404, 70, 2131165647, true), 
    LOW(-1, 2131690160, 2131231405, 40, 2131165648, true), 
    MIDDLE(-1, 2131690161, 2131231406, 55, 2131165649, true), 
    OFF(-1, 2131690115, -1, 999, -1, false);
    
    public static final String TAG = "SmileCapture";
    private static final int sParameterTextId = 2131690163;
    private final int mDimenId;
    private final int mIconId;
    private final boolean mIsSmileCaptureOn;
    private final int mNotificationIconId;
    private final int mScoreThreshold;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new SmileCapture[] { SmileCapture.HIGH, SmileCapture.MIDDLE, SmileCapture.LOW, SmileCapture.OFF };
    }
    
    private SmileCapture(final int mIconId, final int mTextId, final int mNotificationIconId, final int mScoreThreshold, final int mDimenId, final boolean mIsSmileCaptureOn) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mNotificationIconId = mNotificationIconId;
        this.mScoreThreshold = mScoreThreshold;
        this.mDimenId = mDimenId;
        this.mIsSmileCaptureOn = mIsSmileCaptureOn;
        if (mIsSmileCaptureOn) {
            this.mValue = "on";
        }
        else {
            this.mValue = "off";
        }
    }
    
    public static SmileCapture[] getOptions(final CapturingMode capturingMode) {
        if (PlatformCapability.getCameraCapability(capturingMode.getCameraId()).SMILE_DETECTION.get()) {
            return values();
        }
        return new SmileCapture[0];
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
    }
    
    public int getDimenId() {
        return this.mDimenId;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    public int getIntValue() {
        return this.mScoreThreshold;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.SMILE_CAPTURE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690163;
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
        return this.mTextId;
    }
    
    @Override
    public String getValue() {
        return this.mValue;
    }
    
    public boolean isSmileCaptureOn() {
        return this.mIsSmileCaptureOn;
    }
}
