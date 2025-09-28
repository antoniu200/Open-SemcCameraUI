package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public @interface IntDef {
    boolean flag() default false;

    int[] value() default {};
}
