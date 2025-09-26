// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.AnnotationRetention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.ANNOTATION_TYPE })
@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\u0002\u0018\u00002\u00020\u0001:\u0001\u0004B\n\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003R\t\u0010\u0002\u001a\u00020\u0003¢\u0006\u0000\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009120\u0001¨\u0006\u0005" }, d2 = { "Lkotlin/Experimental;", "", "level", "Lkotlin/Experimental$Level;", "Level", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
@kotlin.annotation.Target(allowedTargets = { AnnotationTarget.ANNOTATION_CLASS })
@SinceKotlin(version = "1.2")
public @interface Experimental {
    Level level() default Level.ERROR;
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005" }, d2 = { "Lkotlin/Experimental$Level;", "", "(Ljava/lang/String;I)V", "WARNING", "ERROR", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public enum Level
    {
        private static final Level[] $VALUES;
        
        ERROR, 
        WARNING;
    }
}
