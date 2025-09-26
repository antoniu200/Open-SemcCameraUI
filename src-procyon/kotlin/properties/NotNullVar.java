// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.properties;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u0002H\u00010\u0003B\u0005¢\u0006\u0002\u0010\u0004J$\u0010\u0007\u001a\u00028\u00002\b\u0010\b\u001a\u0004\u0018\u00010\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\nH\u0096\u0002¢\u0006\u0002\u0010\u000bJ,\u0010\f\u001a\u00020\r2\b\u0010\b\u001a\u0004\u0018\u00010\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n2\u0006\u0010\u0005\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000eR\u0012\u0010\u0005\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0006¨\u0006\u000f" }, d2 = { "Lkotlin/properties/NotNullVar;", "T", "", "Lkotlin/properties/ReadWriteProperty;", "()V", "value", "Ljava/lang/Object;", "getValue", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class NotNullVar<T> implements ReadWriteProperty<Object, T>
{
    private T value;
    
    public NotNullVar() {
    }
    
    @NotNull
    @Override
    public T getValue(@Nullable Object value, @NotNull final KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        value = this.value;
        if (value != null) {
            return (T)value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Property ");
        sb.append(kProperty.getName());
        sb.append(" should be initialized before get.");
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void setValue(@Nullable final Object o, @NotNull final KProperty<?> kProperty, @NotNull final T value) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        Intrinsics.checkParameterIsNotNull(value, "value");
        this.value = value;
    }
}
