// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;

public enum PredictiveLaunch implements UserSettingValue
{
    private static final PredictiveLaunch[] $VALUES;
    
    OFF(-1, 2131690115, "0", false);
    
    private static final String TAG = "PredictiveLaunch";
    
    TOUCH_TO_LAUNCH(-1, 2131690012, "1", false), 
    TOUCH_TO_LAUNCH_AND_CAPTURE(-1, 2131690011, "1", true);
    
    private final boolean mDoCapture;
    private final int mIconId;
    private final String mSecureValue;
    private final int mTextId;
    
    static {
        $VALUES = new PredictiveLaunch[] { PredictiveLaunch.TOUCH_TO_LAUNCH, PredictiveLaunch.TOUCH_TO_LAUNCH_AND_CAPTURE, PredictiveLaunch.OFF };
    }
    
    private PredictiveLaunch(final int mIconId, final int mTextId, final String mSecureValue, final boolean mDoCapture) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mSecureValue = mSecureValue;
        this.mDoCapture = mDoCapture;
    }
    
    public static PredictiveLaunch getDefaultValue() {
        return PredictiveLaunch.OFF;
    }
    
    public static PredictiveLaunch[] getOptions() {
        return values();
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public boolean doCapture() {
        return this.mDoCapture;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.PREDICTIVE_LAUNCH;
    }
    
    @Override
    public int getKeyTextId() {
        return this.getKey().getTitleTextId();
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public String getSecureValue() {
        return this.mSecureValue;
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
