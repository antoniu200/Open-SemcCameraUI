// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;

public enum FocusRange implements UserSettingIntValue
{
    private static final FocusRange[] $VALUES;
    
    AF(-1, 2131689890), 
    DEFAULT(-1, 2131689891), 
    MF(-1, 2131689891);
    
    public static final int NUMBER_OF_DIVISION_FOR_THE_WHOLE_RANGE = 99;
    public static final int RATIO_OF_THE_RANGE_FROM_1M_TO_INFINITY = 1;
    public static final int RATIO_OF_THE_RANGE_FROM_MACRO_TO_1M = 2;
    private final int mIconId;
    private final int mTextId;
    private int mValue;
    
    static {
        $VALUES = new FocusRange[] { FocusRange.AF, FocusRange.MF, FocusRange.DEFAULT };
    }
    
    private FocusRange(final int mIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static FocusRange[] getOptions(final CameraInfo.CameraId cameraId) {
        if (PlatformCapability.isManualFocusSupported(cameraId) && CameraInfo.CameraId.BACK == cameraId) {
            return values();
        }
        return new FocusRange[0];
    }
    
    public static float getThreshold() {
        return 33.0f;
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public float getFocusRange(final CameraInfo.CameraId cameraId) {
        final float floatValue = PlatformCapability.getCameraCapability(cameraId).MACRO_FOCUS_RANGE.get();
        final float floatValue2 = CameraParameters.MANUAL_FOCUS_INFINITY;
        final float floatValue3 = CameraParameters.MANUAL_FOCUS_1M;
        final float n = (float)this.mValue;
        final float threshold = getThreshold();
        float n2;
        if (0.0f <= n && n < threshold) {
            n2 = floatValue2 + Math.abs(floatValue3 - floatValue2) / threshold * n;
        }
        else if (threshold <= n && n <= 99.0f) {
            n2 = floatValue3 + Math.abs(floatValue3 - floatValue) / (99.0f - threshold) * (n - threshold);
        }
        else {
            n2 = 1.0f;
        }
        return n2;
    }
    
    @Override
    public int getIconId() {
        return this.mIconId;
    }
    
    @Override
    public int getInt() {
        return this.mValue;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.FOCUS_RANGE;
    }
    
    @Override
    public int getKeyTextId() {
        return 0;
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
    
    @Override
    public void setInt(final int mValue) {
        this.mValue = mValue;
    }
}
