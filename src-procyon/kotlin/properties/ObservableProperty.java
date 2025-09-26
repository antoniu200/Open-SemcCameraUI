// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.properties;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.reflect.KProperty;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000¢\u0006\u0002\u0010\u0005J)\u0010\b\u001a\u00020\t2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\f\u001a\u00028\u00002\u0006\u0010\r\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u000eJ)\u0010\u000f\u001a\u00020\u00102\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\f\u001a\u00028\u00002\u0006\u0010\r\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u0011J$\u0010\u0012\u001a\u00028\u00002\b\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000bH\u0096\u0002¢\u0006\u0002\u0010\u0014J,\u0010\u0015\u001a\u00020\t2\b\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\u0006\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0016R\u0010\u0010\u0006\u001a\u00028\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\u0017" }, d2 = { "Lkotlin/properties/ObservableProperty;", "T", "Lkotlin/properties/ReadWriteProperty;", "", "initialValue", "(Ljava/lang/Object;)V", "value", "Ljava/lang/Object;", "afterChange", "", "property", "Lkotlin/reflect/KProperty;", "oldValue", "newValue", "(Lkotlin/reflect/KProperty;Ljava/lang/Object;Ljava/lang/Object;)V", "beforeChange", "", "(Lkotlin/reflect/KProperty;Ljava/lang/Object;Ljava/lang/Object;)Z", "getValue", "thisRef", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public abstract class ObservableProperty<T> implements ReadWriteProperty<Object, T>
{
    private T value;
    
    public ObservableProperty(final T value) {
        this.value = value;
    }
    
    protected void afterChange(@NotNull final KProperty<?> kProperty, final T t, final T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
    }
    
    protected boolean beforeChange(@NotNull final KProperty<?> kProperty, final T t, final T t2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return true;
    }
    
    @Override
    public T getValue(@Nullable final Object o, @NotNull final KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return this.value;
    }
    
    @Override
    public void setValue(@Nullable Object value, @NotNull final KProperty<?> kProperty, final T value2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        value = this.value;
        if (!this.beforeChange(kProperty, value, value2)) {
            return;
        }
        this.afterChange(kProperty, value, this.value = value2);
    }
}
