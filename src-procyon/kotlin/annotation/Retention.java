// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.annotation;

import kotlin.Metadata;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0087\u0002\u0018\u00002\u00020\u0001B\n\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000¨\u0006\u0004" }, d2 = { "Lkotlin/annotation/Retention;", "", "value", "Lkotlin/annotation/AnnotationRetention;", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@kotlin.annotation.Target(allowedTargets = { AnnotationTarget.ANNOTATION_CLASS })
public @interface Retention {
    AnnotationRetention value() default AnnotationRetention.RUNTIME;
}
