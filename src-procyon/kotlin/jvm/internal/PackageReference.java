// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.reflect.KCallable;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0019\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0005H\u0016R\u0018\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b0\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015" }, d2 = { "Lkotlin/jvm/internal/PackageReference;", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "moduleName", "", "(Ljava/lang/Class;Ljava/lang/String;)V", "getJClass", "()Ljava/lang/Class;", "members", "", "Lkotlin/reflect/KCallable;", "getMembers", "()Ljava/util/Collection;", "equals", "", "other", "", "hashCode", "", "toString", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@SinceKotlin(version = "1.1")
public final class PackageReference implements ClassBasedDeclarationContainer
{
    @NotNull
    private final Class<?> jClass;
    private final String moduleName;
    
    public PackageReference(@NotNull final Class<?> jClass, @NotNull final String moduleName) {
        Intrinsics.checkParameterIsNotNull(jClass, "jClass");
        Intrinsics.checkParameterIsNotNull(moduleName, "moduleName");
        this.jClass = jClass;
        this.moduleName = moduleName;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof PackageReference && Intrinsics.areEqual(this.getJClass(), ((PackageReference)o).getJClass());
    }
    
    @NotNull
    @Override
    public Class<?> getJClass() {
        return this.jClass;
    }
    
    @NotNull
    @Override
    public Collection<KCallable<?>> getMembers() {
        throw new KotlinReflectionNotSupportedError();
    }
    
    @Override
    public int hashCode() {
        return this.getJClass().hashCode();
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
