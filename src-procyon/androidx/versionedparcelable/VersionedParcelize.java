// 
// Decompiled by Procyon v0.6.0
// 

package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public @interface VersionedParcelize {
    boolean allowSerialization() default false;
    
    int[] deprecatedIds() default {};
    
    boolean ignoreParcelables() default false;
    
    boolean isCustom() default false;
    
    String jetifyAs() default "";
}
