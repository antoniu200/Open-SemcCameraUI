// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.stream;

public enum JsonToken
{
    private static final JsonToken[] $VALUES;
    
    BEGIN_ARRAY, 
    BEGIN_OBJECT, 
    BOOLEAN, 
    END_ARRAY, 
    END_DOCUMENT, 
    END_OBJECT, 
    NAME, 
    NULL, 
    NUMBER, 
    STRING;
    
    static {
        $VALUES = new JsonToken[] { JsonToken.BEGIN_ARRAY, JsonToken.END_ARRAY, JsonToken.BEGIN_OBJECT, JsonToken.END_OBJECT, JsonToken.NAME, JsonToken.STRING, JsonToken.NUMBER, JsonToken.BOOLEAN, JsonToken.NULL, JsonToken.END_DOCUMENT };
    }
}
