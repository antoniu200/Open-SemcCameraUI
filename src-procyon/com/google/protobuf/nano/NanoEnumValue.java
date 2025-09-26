// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.CLASS)
public @interface NanoEnumValue {
    boolean keepAsInt() default false;
    
    boolean legacy() default false;
    
    Class<?> value();
}
