// 
// Decompiled by Procyon v0.6.0
// 

package org.intellij.lang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD })
public @interface MagicConstant {
    long[] flags() default {};
    
    Class flagsFromClass() default void.class;
    
    long[] intValues() default {};
    
    String[] stringValues() default {};
    
    Class valuesFromClass() default void.class;
}
