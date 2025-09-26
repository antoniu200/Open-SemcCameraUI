// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

public class DeviceStatus extends EnumValue<Value> implements EachCameraStatusValue
{
    public static final Value DEFAULT_VALUE;
    public static final String KEY = "device_status";
    private static int REQUIRED_PROVIDER_VERSION = 1;
    
    static {
        DEFAULT_VALUE = Value.POWER_OFF;
    }
    
    public DeviceStatus(final Value value) {
        super(value);
    }
    
    @Override
    public String getKey() {
        return "device_status";
    }
    
    @Override
    public int minRequiredVersion() {
        return DeviceStatus.REQUIRED_PROVIDER_VERSION;
    }
    
    public enum Value
    {
        private static final Value[] $VALUES;
        
        PICTURE_TAKING("picture_taking"), 
        PICTURE_TAKING_DURING_VIDEO_RECORDING("picture_taking_during_video_recording"), 
        POWER_OFF("power_off"), 
        POWER_ON("power_on"), 
        STILL_PREVIEW("still_preview"), 
        VIDEO_PREVIEW("video_preview"), 
        VIDEO_RECORDING("video_recording");
        
        private final String mStringExpression;
        
        static {
            $VALUES = new Value[] { Value.POWER_ON, Value.POWER_OFF, Value.STILL_PREVIEW, Value.VIDEO_PREVIEW, Value.PICTURE_TAKING, Value.VIDEO_RECORDING, Value.PICTURE_TAKING_DURING_VIDEO_RECORDING };
        }
        
        private Value(final String mStringExpression) {
            this.mStringExpression = mStringExpression;
        }
        
        @Override
        public String toString() {
            return this.mStringExpression;
        }
    }
}
