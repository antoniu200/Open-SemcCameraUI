// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.MustBeDocumented;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\u0002\u0018\u00002\u00020\u0001B\b\u0012\u0006\u0010\u0002\u001a\u00020\u0003R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000¨\u0006\u0004" }, d2 = { "Lkotlin/ParameterName;", "", "name", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@MustBeDocumented
@kotlin.annotation.Target(allowedTargets = { AnnotationTarget.TYPE })
@SinceKotlin(version = "1.1")
public @interface ParameterName {
    String name();
}
