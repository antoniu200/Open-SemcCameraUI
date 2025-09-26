// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

public class VideoStabilizerStatus extends EnumValue<Value> implements EachCameraStatusValue
{
    public static final Value DEFAULT_VALUE;
    public static final String KEY = "video_stabilizer";
    private static int REQUIRED_PROVIDER_VERSION = 1;
    
    static {
        DEFAULT_VALUE = Value.OFF;
    }
    
    public VideoStabilizerStatus(final Value value) {
        super(value);
    }
    
    public static VideoStabilizerStatus fromCameraParameter(final String anObject) {
        if ("on".equals(anObject)) {
            return new VideoStabilizerStatus(Value.ON);
        }
        if ("on".equals(anObject)) {
            return new VideoStabilizerStatus(Value.ON);
        }
        if ("intelligent_active".equals(anObject)) {
            return new VideoStabilizerStatus(Value.INTELLIGENT_ACTIVE);
        }
        return new VideoStabilizerStatus(Value.OFF);
    }
    
    @Override
    public String getKey() {
        return "video_stabilizer";
    }
    
    @Override
    public int minRequiredVersion() {
        return VideoStabilizerStatus.REQUIRED_PROVIDER_VERSION;
    }
    
    public enum Value
    {
        private static final Value[] $VALUES;
        
        INTELLIGENT_ACTIVE("intelligent_active"), 
        OFF("off"), 
        ON("on");
        
        private final String mStringExpression;
        
        static {
            $VALUES = new Value[] { Value.ON, Value.OFF, Value.INTELLIGENT_ACTIVE };
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
