// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.view;

import com.sonyericsson.android.camera.device.CameraParameters;

public class RecognizedCondition
{
    private final CameraParameters.DeviceStabilityCondition mCondition;
    private final int mIconId;
    private final int mTextId;
    
    private RecognizedCondition(final CameraParameters.DeviceStabilityCondition mCondition, final int mIconId, final int mTextId) {
        this.mCondition = mCondition;
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static RecognizedCondition create(final CameraParameters.DeviceStabilityCondition deviceStabilityCondition) {
        if (deviceStabilityCondition == null) {
            return new RecognizedCondition(null, -1, -1);
        }
        switch (RecognizedCondition$1.$SwitchMap$com$sonyericsson$android$camera$device$CameraParameters$DeviceStabilityCondition[deviceStabilityCondition.ordinal()]) {
            default: {
                return new RecognizedCondition(deviceStabilityCondition, -1, -1);
            }
            case 4: {
                return new RecognizedCondition(deviceStabilityCondition, 2131230927, -1);
            }
            case 3: {
                return new RecognizedCondition(deviceStabilityCondition, 2131230926, -1);
            }
            case 2: {
                return new RecognizedCondition(deviceStabilityCondition, 2131230925, -1);
            }
            case 1: {
                return new RecognizedCondition(deviceStabilityCondition, -1, -1);
            }
        }
    }
    
    public CameraParameters.DeviceStabilityCondition getCondition() {
        return this.mCondition;
    }
    
    public int getIconId() {
        return this.mIconId;
    }
    
    public int getTextId() {
        return this.mTextId;
    }
}
