// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.ArrayList;

public enum FastCapture implements UserSettingValue
{
    private static final FastCapture[] $VALUES;
    
    LAUNCH_AND_CAPTURE(-1, 2131689820, 1, true), 
    LAUNCH_ONLY(-1, 2131689822, 1, true), 
    OFF(-1, 2131690115, 0, false);
    
    public static final String TAG = "FastCapture";
    private static final int sParameterTextId = 2131689828;
    private final boolean mBooleanValue;
    private final int mIconId;
    private final int mTextId;
    private final int mType;
    private String mValue;
    
    static {
        $VALUES = new FastCapture[] { FastCapture.LAUNCH_AND_CAPTURE, FastCapture.LAUNCH_ONLY, FastCapture.OFF };
    }
    
    private FastCapture(final int mIconId, final int mTextId, final int mType, final boolean mBooleanValue) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
        this.mType = mType;
        this.mBooleanValue = mBooleanValue;
    }
    
    public static FastCapture getDefault() {
        return FastCapture.LAUNCH_ONLY;
    }
    
    public static FastCapture[] getOptions() {
        final ArrayList list = new ArrayList();
        list.add(FastCapture.LAUNCH_ONLY);
        list.add(FastCapture.LAUNCH_AND_CAPTURE);
        list.add(FastCapture.OFF);
        return list.toArray(new FastCapture[0]);
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public boolean getBooleanValue() {
        return this.mBooleanValue;
    }
    
    public int getCameraType() {
        return this.mType;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.FAST_CAPTURE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131689828;
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    public int getParameterkeyTitleTextId() {
        return 2131689827;
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
