// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function0;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.io.Serializable;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0002\u0018\u0000 \u0013*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004:\u0001\u0013B\u0013\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\tH\u0002R\u0014\u0010\b\u001a\u0004\u0018\u00010\t8\u0002@\u0002X\u0083\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00068\b@\bX\u0089\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00028\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0014" }, d2 = { "Lkotlin/SafePublicationLazyImpl;", "T", "Lkotlin/Lazy;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "initializer", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)V", "_value", "", "final", "value", "getValue", "()Ljava/lang/Object;", "isInitialized", "", "toString", "", "writeReplace", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class SafePublicationLazyImpl<T> implements Lazy<T>, Serializable
{
    public static final Companion Companion;
    private static final AtomicReferenceFieldUpdater<SafePublicationLazyImpl<?>, Object> valueUpdater;
    private volatile Object _value;
    private final Object final;
    private volatile Function0<? extends T> initializer;
    
    static {
        Companion = new Companion(null);
        valueUpdater = AtomicReferenceFieldUpdater.newUpdater(SafePublicationLazyImpl.class, Object.class, "_value");
    }
    
    public SafePublicationLazyImpl(@NotNull final Function0<? extends T> initializer) {
        Intrinsics.checkParameterIsNotNull(initializer, "initializer");
        this.initializer = initializer;
        this._value = UNINITIALIZED_VALUE.INSTANCE;
        this.final = UNINITIALIZED_VALUE.INSTANCE;
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
        final Function0<? extends T> initializer = this.initializer;
        if (initializer != null) {
            final T invoke = (T)initializer.invoke();
            if (SafePublicationLazyImpl.valueUpdater.compareAndSet(this, UNINITIALIZED_VALUE.INSTANCE, invoke)) {
                this.initializer = null;
                return invoke;
            }
        }
        return (T)this._value;
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
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R^\u0010\u0003\u001aR\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001 \u0006*(\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007" }, d2 = { "Lkotlin/SafePublicationLazyImpl$Companion;", "", "()V", "valueUpdater", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlin/SafePublicationLazyImpl;", "kotlin.jvm.PlatformType", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
