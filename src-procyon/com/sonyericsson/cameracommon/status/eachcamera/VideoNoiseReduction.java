// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

public class VideoNoiseReduction extends EnumValue<Value> implements EachCameraStatusValue
{
    public static final Value DEFAULT_VALUE;
    public static final String KEY = "video_noise_reduction";
    private static int REQUIRED_PROVIDER_VERSION = 10;
    
    static {
        DEFAULT_VALUE = Value.OFF;
    }
    
    public VideoNoiseReduction(final Value value) {
        super(value);
    }
    
    @Override
    public String getKey() {
        return "video_noise_reduction";
    }
    
    @Override
    public int minRequiredVersion() {
        return VideoNoiseReduction.REQUIRED_PROVIDER_VERSION;
    }
    
    public enum Value
    {
        private static final Value[] $VALUES;
        
        OFF("off"), 
        ON("on");
        
        private final String mStringExpression;
        
        static {
            $VALUES = new Value[] { Value.ON, Value.OFF };
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
