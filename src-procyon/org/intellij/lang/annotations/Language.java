// 
// Decompiled by Procyon v0.6.0
// 

package org.intellij.lang.annotations;

import org.jetbrains.annotations.NonNls;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
public @interface Language {
    @NonNls
    String prefix() default "";
    
    @NonNls
    String suffix() default "";
    
    @NonNls
    String value();
}
