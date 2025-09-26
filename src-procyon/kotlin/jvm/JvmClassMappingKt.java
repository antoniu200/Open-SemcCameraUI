// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm;

import kotlin.ReplaceWith;
import kotlin.DeprecationLevel;
import kotlin.Deprecated;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.Nullable;
import java.io.Serializable;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import java.lang.annotation.Annotation;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a\u001f\u0010\u0018\u001a\u00020\u0019\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\r*\u0006\u0012\u0002\b\u00030\u001a¢\u0006\u0002\u0010\u001b\"'\u0010\u0000\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\u0002H\u00028F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"0\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00018GX\u0087\u0004¢\u0006\f\u0012\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b\"&\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\u0002H\u00028\u00c6\u0002¢\u0006\u0006\u001a\u0004\b\n\u0010\u000e\";\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018\u00c7\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u000f\u0010\t\u001a\u0004\b\u0010\u0010\u000b\"+\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u000b\"-\u0010\u0013\u001a\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u000b\"+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00078G¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u001c" }, d2 = { "annotationClass", "Lkotlin/reflect/KClass;", "T", "", "getAnnotationClass", "(Ljava/lang/annotation/Annotation;)Lkotlin/reflect/KClass;", "java", "Ljava/lang/Class;", "java$annotations", "(Lkotlin/reflect/KClass;)V", "getJavaClass", "(Lkotlin/reflect/KClass;)Ljava/lang/Class;", "javaClass", "", "(Ljava/lang/Object;)Ljava/lang/Class;", "javaClass$annotations", "getRuntimeClassOfKClassInstance", "javaObjectType", "getJavaObjectType", "javaPrimitiveType", "getJavaPrimitiveType", "kotlin", "getKotlinClass", "(Ljava/lang/Class;)Lkotlin/reflect/KClass;", "isArrayOf", "", "", "([Ljava/lang/Object;)Z", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "JvmClassMappingKt")
public final class JvmClassMappingKt
{
    @NotNull
    public static final <T extends Annotation> KClass<? extends T> getAnnotationClass(@NotNull final T t) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        final Class<? extends Annotation> annotationType = t.annotationType();
        Intrinsics.checkExpressionValueIsNotNull(annotationType, "(this as java.lang.annot\u2026otation).annotationType()");
        final KClass<Object> kotlinClass = getKotlinClass((Class<Object>)annotationType);
        if (kotlinClass == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.reflect.KClass<out T>");
        }
        return (KClass<? extends T>)kotlinClass;
    }
    
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull final T t) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        final Class<?> class1 = t.getClass();
        if (class1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
        return (Class<T>)class1;
    }
    
    @JvmName(name = "getJavaClass")
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull final KClass<T> kClass) {
        Intrinsics.checkParameterIsNotNull(kClass, "$receiver");
        final Class<?> jClass = ((ClassBasedDeclarationContainer)kClass).getJClass();
        if (jClass == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
        return (Class<T>)jClass;
    }
    
    @NotNull
    public static final <T> Class<T> getJavaObjectType(@NotNull final KClass<T> kClass) {
        Intrinsics.checkParameterIsNotNull(kClass, "$receiver");
        Class<?> jClass = ((ClassBasedDeclarationContainer)kClass).getJClass();
        if (!jClass.isPrimitive()) {
            if (jClass == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
            }
            return (Class<T>)jClass;
        }
        else {
            final String name = jClass.getName();
            if (name != null) {
                switch (name.hashCode()) {
                    case 109413500: {
                        if (name.equals("short")) {
                            jClass = Short.class;
                            break;
                        }
                        break;
                    }
                    case 97526364: {
                        if (name.equals("float")) {
                            jClass = Float.class;
                            break;
                        }
                        break;
                    }
                    case 64711720: {
                        if (name.equals("boolean")) {
                            jClass = Boolean.class;
                            break;
                        }
                        break;
                    }
                    case 3625364: {
                        if (name.equals("void")) {
                            jClass = Void.class;
                            break;
                        }
                        break;
                    }
                    case 3327612: {
                        if (name.equals("long")) {
                            jClass = Long.class;
                            break;
                        }
                        break;
                    }
                    case 3052374: {
                        if (name.equals("char")) {
                            jClass = Character.class;
                            break;
                        }
                        break;
                    }
                    case 3039496: {
                        if (name.equals("byte")) {
                            jClass = Byte.class;
                            break;
                        }
                        break;
                    }
                    case 104431: {
                        if (name.equals("int")) {
                            jClass = Integer.class;
                            break;
                        }
                        break;
                    }
                    case -1325958191: {
                        if (name.equals("double")) {
                            jClass = Double.class;
                            break;
                        }
                        break;
                    }
                }
            }
            if (jClass == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
            }
            return (Class<T>)jClass;
        }
    }
    
    @Nullable
    public static final <T> Class<T> getJavaPrimitiveType(@NotNull final KClass<T> kClass) {
        Intrinsics.checkParameterIsNotNull(kClass, "$receiver");
        final Class<?> jClass = ((ClassBasedDeclarationContainer)kClass).getJClass();
        if (!jClass.isPrimitive()) {
            final String name = jClass.getName();
            if (name != null) {
                switch (name.hashCode()) {
                    case 761287205: {
                        if (name.equals("java.lang.Double")) {
                            final Serializable s = Double.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case 399092968: {
                        if (name.equals("java.lang.Void")) {
                            final Serializable s = Void.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case 398795216: {
                        if (name.equals("java.lang.Long")) {
                            final Serializable s = Long.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case 398507100: {
                        if (name.equals("java.lang.Byte")) {
                            final Serializable s = Byte.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case 344809556: {
                        if (name.equals("java.lang.Boolean")) {
                            final Serializable s = Boolean.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case 155276373: {
                        if (name.equals("java.lang.Character")) {
                            final Serializable s = Character.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case -515992664: {
                        if (name.equals("java.lang.Short")) {
                            final Serializable s = Short.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case -527879800: {
                        if (name.equals("java.lang.Float")) {
                            final Serializable s = Float.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                    case -2056817302: {
                        if (name.equals("java.lang.Integer")) {
                            final Serializable s = Integer.TYPE;
                            return (Class<T>)s;
                        }
                        break;
                    }
                }
            }
            final Serializable s = null;
            return (Class<T>)s;
        }
        if (jClass == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<T>");
        }
        return (Class<T>)jClass;
    }
    
    @JvmName(name = "getKotlinClass")
    @NotNull
    public static final <T> KClass<T> getKotlinClass(@NotNull final Class<T> clazz) {
        Intrinsics.checkParameterIsNotNull(clazz, "$receiver");
        return Reflection.getOrCreateKotlinClass(clazz);
    }
    
    @JvmName(name = "getRuntimeClassOfKClassInstance")
    @NotNull
    public static final <T> Class<KClass<T>> getRuntimeClassOfKClassInstance(@NotNull final KClass<T> kClass) {
        Intrinsics.checkParameterIsNotNull(kClass, "$receiver");
        final Class<?> class1 = kClass.getClass();
        if (class1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Class<kotlin.reflect.KClass<T>>");
        }
        return (Class<KClass<T>>)class1;
    }
    
    private static final <T> boolean isArrayOf(@NotNull final Object[] array) {
        Intrinsics.reifiedOperationMarker(4, "T");
        return Object.class.isAssignableFrom(array.getClass().getComponentType());
    }
}
