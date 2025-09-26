// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.MustBeDocumented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.ANNOTATION_TYPE })
@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0087\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002" }, d2 = { "Lkotlin/DslMarker;", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
@kotlin.annotation.Target(allowedTargets = { AnnotationTarget.ANNOTATION_CLASS })
@SinceKotlin(version = "1.1")
public @interface DslMarker {
}
