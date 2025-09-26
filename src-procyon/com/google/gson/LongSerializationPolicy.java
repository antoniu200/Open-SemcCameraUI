// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson;

public enum LongSerializationPolicy
{
    private static final LongSerializationPolicy[] $VALUES;
    
    DEFAULT {
        @Override
        public JsonElement serialize(final Long n) {
            return new JsonPrimitive(n);
        }
    }, 
    STRING {
        @Override
        public JsonElement serialize(final Long obj) {
            return new JsonPrimitive(String.valueOf(obj));
        }
    };
    
    static {
        $VALUES = new LongSerializationPolicy[] { LongSerializationPolicy.DEFAULT, LongSerializationPolicy.STRING };
    }
    
    public abstract JsonElement serialize(final Long p0);
}
