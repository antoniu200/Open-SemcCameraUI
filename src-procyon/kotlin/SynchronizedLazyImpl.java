// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function0;
import java.io.Serializable;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u001f\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0002\u0010\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\bH\u0002R\u0014\u0010\n\u001a\u0004\u0018\u00010\b8\u0002@\u0002X\u0083\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0006X\u0088\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00028\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0013" }, d2 = { "Lkotlin/SynchronizedLazyImpl;", "T", "Lkotlin/Lazy;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "initializer", "Lkotlin/Function0;", "lock", "", "(Lkotlin/jvm/functions/Function0;Ljava/lang/Object;)V", "_value", "value", "getValue", "()Ljava/lang/Object;", "isInitialized", "", "toString", "", "writeReplace", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class SynchronizedLazyImpl<T> implements Lazy<T>, Serializable
{
    private volatile Object _value;
    private Function0<? extends T> initializer;
    private final Object lock;
    
    public SynchronizedLazyImpl(@NotNull final Function0<? extends T> initializer, @Nullable final Object o) {
        Intrinsics.checkParameterIsNotNull(initializer, "initializer");
        this.initializer = initializer;
        this._value = UNINITIALIZED_VALUE.INSTANCE;
        Object lock;
        if (o != null) {
            lock = o;
        }
        else {
            lock = this;
        }
        this.lock = lock;
    }
    
    private final Object writeReplace() {
        return new InitializedLazyImpl(this.getValue());
    }
    
    @Override
    public T getValue() {
        final Object value = this._value;
        if (value != UNINITIALIZED_VALUE.INSTANCE) {
            return (T)value;
        }
        synchronized (this.lock) {
            Object value2 = this._value;
            if (value2 == UNINITIALIZED_VALUE.INSTANCE) {
                final Function0<? extends T> initializer = this.initializer;
                if (initializer == null) {
                    Intrinsics.throwNpe();
                }
                value2 = initializer.invoke();
                this._value = value2;
                this.initializer = null;
            }
            return (T)value2;
        }
    }
    
    @Override
    public boolean isInitialized() {
        return this._value != UNINITIALIZED_VALUE.INSTANCE;
    }
    
    @NotNull
    @Override
    public String toString() {
        String value;
        if (this.isInitialized()) {
            value = String.valueOf(this.getValue());
        }
        else {
            value = "Lazy value not initialized yet.";
        }
        return value;
    }
}
