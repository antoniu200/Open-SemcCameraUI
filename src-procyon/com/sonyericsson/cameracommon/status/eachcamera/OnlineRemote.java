// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status.eachcamera;

import com.sonyericsson.cameracommon.status.EachCameraStatusValue;
import com.sonyericsson.cameracommon.status.EnumValue;

public class OnlineRemote extends EnumValue<Value> implements EachCameraStatusValue
{
    public static final Value DEFAULT_VALUE;
    public static final String KEY = "online_remote";
    private static final int REQUIRED_VERSION = 10;
    
    static {
        DEFAULT_VALUE = Value.OFF;
    }
    
    public OnlineRemote(final Value value) {
        super(value);
    }
    
    @Override
    public String getKey() {
        return "online_remote";
    }
    
    @Override
    public int minRequiredVersion() {
        return 10;
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
