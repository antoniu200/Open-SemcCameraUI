// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KVisibility;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KType;
import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;
import java.util.Collection;
import java.lang.annotation.Annotation;
import java.util.List;
import kotlin.jvm.JvmClassMappingKt;
import org.jetbrains.annotations.Nullable;
import kotlin.SinceKotlin;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import kotlin.reflect.KClass;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\u0011\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010?\u001a\u00020\u00122\b\u0010@\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010A\u001a\u00020BH\u0002J\b\u0010C\u001a\u00020DH\u0016J\u0012\u0010E\u001a\u00020\u00122\b\u0010F\u001a\u0004\u0018\u00010\u0002H\u0017J\b\u0010G\u001a\u00020-H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u000e0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0017\u0010\u0014\u001a\u0004\b\u0016\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0015R\u001a\u0010\u001a\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001b\u0010\u0014\u001a\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u0014\u001a\u0004\b\u001c\u0010\u0015R\u001a\u0010\u001e\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001e\u0010\u0015R\u001a\u0010 \u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b!\u0010\u0014\u001a\u0004\b \u0010\u0015R\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u001e\u0010$\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030%0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b&\u0010\u0010R\u001e\u0010'\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b(\u0010\u0010R\u0016\u0010)\u001a\u0004\u0018\u00010\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b*\u0010+R\u0016\u0010,\u001a\u0004\u0018\u00010-8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b.\u0010/R\u0016\u00100\u001a\u0004\u0018\u00010-8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b1\u0010/R \u00102\u001a\b\u0012\u0004\u0012\u0002030\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b4\u0010\u0014\u001a\u0004\b5\u0010\u000bR \u00106\u001a\b\u0012\u0004\u0012\u0002070\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b8\u0010\u0014\u001a\u0004\b9\u0010\u000bR\u001c\u0010:\u001a\u0004\u0018\u00010;8VX\u0097\u0004¢\u0006\f\u0012\u0004\b<\u0010\u0014\u001a\u0004\b=\u0010>¨\u0006H" }, d2 = { "Lkotlin/jvm/internal/ClassReference;", "Lkotlin/reflect/KClass;", "", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)V", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "constructors", "", "Lkotlin/reflect/KFunction;", "getConstructors", "()Ljava/util/Collection;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isCompanion", "isCompanion$annotations", "isData", "isData$annotations", "isFinal", "isFinal$annotations", "isInner", "isInner$annotations", "isOpen", "isOpen$annotations", "isSealed", "isSealed$annotations", "getJClass", "()Ljava/lang/Class;", "members", "Lkotlin/reflect/KCallable;", "getMembers", "nestedClasses", "getNestedClasses", "objectInstance", "getObjectInstance", "()Ljava/lang/Object;", "qualifiedName", "", "getQualifiedName", "()Ljava/lang/String;", "simpleName", "getSimpleName", "supertypes", "Lkotlin/reflect/KType;", "supertypes$annotations", "getSupertypes", "typeParameters", "Lkotlin/reflect/KTypeParameter;", "typeParameters$annotations", "getTypeParameters", "visibility", "Lkotlin/reflect/KVisibility;", "visibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "equals", "other", "error", "", "hashCode", "", "isInstance", "value", "toString", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class ClassReference implements KClass<Object>, ClassBasedDeclarationContainer
{
    @NotNull
    private final Class<?> jClass;
    
    public ClassReference(@NotNull final Class<?> jClass) {
        Intrinsics.checkParameterIsNotNull(jClass, "jClass");
        this.jClass = jClass;
    }
    
    private final Void error() {
        throw new KotlinReflectionNotSupportedError();
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof ClassReference && Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType((KClass<Object>)this), JvmClassMappingKt.getJavaObjectType((KClass<Object>)o));
    }
    
    @NotNull
    @Override
    public List<Annotation> getAnnotations() {
        this.error();
        throw null;
    }
    
    @NotNull
    @Override
    public Collection<KFunction<Object>> getConstructors() {
        this.error();
        throw null;
    }
    
    @NotNull
    @Override
    public Class<?> getJClass() {
        return this.jClass;
    }
    
    @NotNull
    @Override
    public Collection<KCallable<?>> getMembers() {
        this.error();
        throw null;
    }
    
    @NotNull
    @Override
    public Collection<KClass<?>> getNestedClasses() {
        this.error();
        throw null;
    }
    
    @Nullable
    @Override
    public Object getObjectInstance() {
        this.error();
        throw null;
    }
    
    @Nullable
    @Override
    public String getQualifiedName() {
        this.error();
        throw null;
    }
    
    @Nullable
    @Override
    public String getSimpleName() {
        this.error();
        throw null;
    }
    
    @NotNull
    @Override
    public List<KType> getSupertypes() {
        this.error();
        throw null;
    }
    
    @NotNull
    @Override
    public List<KTypeParameter> getTypeParameters() {
        this.error();
        throw null;
    }
    
    @Nullable
    @Override
    public KVisibility getVisibility() {
        this.error();
        throw null;
    }
    
    @Override
    public int hashCode() {
        return JvmClassMappingKt.getJavaObjectType((KClass<Object>)this).hashCode();
    }
    
    @Override
    public boolean isAbstract() {
        this.error();
        throw null;
    }
    
    @Override
    public boolean isCompanion() {
        this.error();
        throw null;
    }
    
    @Override
    public boolean isData() {
        this.error();
        throw null;
    }
    
    @Override
    public boolean isFinal() {
        this.error();
        throw null;
    }
    
    @Override
    public boolean isInner() {
        this.error();
        throw null;
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isInstance(@Nullable final Object o) {
        this.error();
        throw null;
    }
    
    @Override
    public boolean isOpen() {
        this.error();
        throw null;
    }
    
    @Override
    public boolean isSealed() {
        this.error();
        throw null;
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getJClass().toString());
        sb.append(" (Kotlin reflection is not available)");
        return sb.toString();
    }
}
