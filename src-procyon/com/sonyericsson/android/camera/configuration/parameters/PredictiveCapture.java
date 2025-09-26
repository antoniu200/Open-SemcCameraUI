// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.configuration.parameters;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import java.util.List;

public enum PredictiveCapture implements UserSettingValue
{
    private static final PredictiveCapture[] $VALUES;
    
    AUTO(2131690114, "auto", 4), 
    OFF(2131690115, "off", 1), 
    ON(2131690116, "on", 4);
    
    public static final String TAG = "PredictiveCapture";
    private static final int sParameterTextId = 2131690005;
    private final int mCaptureNum;
    private final int mTextId;
    private final String mValue;
    
    static {
        $VALUES = new PredictiveCapture[] { PredictiveCapture.AUTO, PredictiveCapture.OFF, PredictiveCapture.ON };
    }
    
    private PredictiveCapture(final int mTextId, final String mValue, final int mCaptureNum) {
        this.mTextId = mTextId;
        this.mValue = mValue;
        this.mCaptureNum = mCaptureNum;
    }
    
    public static PredictiveCapture getDefaultValue(final boolean b, final CapturingMode capturingMode) {
        if (!b && capturingMode == CapturingMode.SCENE_RECOGNITION) {
            return PredictiveCapture.AUTO;
        }
        return PredictiveCapture.OFF;
    }
    
    public static PredictiveCapture[] getOptions(final boolean b, final CapturingMode capturingMode) {
        if (!b && capturingMode == CapturingMode.SCENE_RECOGNITION) {
            final List list = PlatformCapability.getCameraCapability(capturingMode.getCameraId()).PREDICTIVE_CAPTURE.get();
            if (list.size() != 0) {
                final Iterator iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (((String)iterator.next()).equals(PredictiveCapture.AUTO.getValue())) {
                        return new PredictiveCapture[] { PredictiveCapture.AUTO, PredictiveCapture.OFF };
                    }
                }
                return new PredictiveCapture[] { PredictiveCapture.OFF };
            }
        }
        return new PredictiveCapture[] { PredictiveCapture.OFF };
    }
    
    @Override
    public void apply(final UserSettingApplicable userSettingApplicable) {
        userSettingApplicable.set(this);
    }
    
    public int getCaptureNum() {
        return this.mCaptureNum;
    }
    
    @Override
    public int getIconId() {
        return -1;
    }
    
    @Override
    public UserSettingKey getKey() {
        return UserSettingKey.PREDICTIVE_CAPTURE;
    }
    
    @Override
    public int getKeyTextId() {
        return 2131690005;
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
