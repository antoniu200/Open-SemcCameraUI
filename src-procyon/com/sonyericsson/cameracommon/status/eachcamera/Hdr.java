// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

public class Hdr extends EnumValue<Value> implements EachCameraStatusValue
{
    public static final Value DEFAULT_VALUE;
    public static final String KEY = "hdr";
    private static int REQUIRED_PROVIDER_VERSION = 12;
    
    static {
        DEFAULT_VALUE = Value.OFF;
    }
    
    public Hdr(final Value value) {
        super(value);
    }
    
    public static Value fromCameraParameter(final VideoHdr videoHdr) {
        if (videoHdr == VideoHdr.HDR_ON) {
            return Value.ON;
        }
        return Value.OFF;
    }
    
    @Override
    public String getKey() {
        return "hdr";
    }
    
    @Override
    public int minRequiredVersion() {
        return Hdr.REQUIRED_PROVIDER_VERSION;
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
