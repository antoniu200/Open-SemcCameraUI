// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

public class ArtFilter extends EnumValue<Value> implements EachCameraStatusValue
{
    public static final Value DEFAULT_VALUE;
    public static final String KEY = "art_filter";
    private static final int REQUIRED_VERSION = 1;
    
    static {
        DEFAULT_VALUE = Value.OFF;
    }
    
    public ArtFilter(final Value value) {
        super(value);
    }
    
    @Override
    public String getKey() {
        return "art_filter";
    }
    
    @Override
    public int minRequiredVersion() {
        return 1;
    }
    
    public enum Value
    {
        private static final Value[] $VALUES;
        
        MULTI("multi"), 
        OFF("off"), 
        SINGLE("single");
        
        private final String mStringExpression;
        
        static {
            $VALUES = new Value[] { Value.SINGLE, Value.MULTI, Value.OFF };
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
