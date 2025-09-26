// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¨\u0006\t" }, d2 = { "lazy", "Lkotlin/Lazy;", "T", "initializer", "Lkotlin/Function0;", "lock", "", "mode", "Lkotlin/LazyThreadSafetyMode;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/LazyKt")
class LazyKt__LazyJVMKt
{
    public LazyKt__LazyJVMKt() {
    }
    
    @NotNull
    public static final <T> Lazy<T> lazy(@Nullable final Object o, @NotNull final Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "initializer");
        return new SynchronizedLazyImpl<T>(function0, o);
    }
    
    @NotNull
    public static final <T> Lazy<T> lazy(@NotNull final LazyThreadSafetyMode lazyThreadSafetyMode, @NotNull final Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(lazyThreadSafetyMode, "mode");
        Intrinsics.checkParameterIsNotNull(function0, "initializer");
        Lazy lazy = null;
        switch (LazyKt$WhenMappings.$EnumSwitchMapping$0[lazyThreadSafetyMode.ordinal()]) {
            default: {
                throw new NoWhenBranchMatchedException();
            }
            case 3: {
                lazy = new UnsafeLazyImpl(function0);
                break;
            }
            case 2: {
                lazy = new SafePublicationLazyImpl(function0);
                break;
            }
            case 1: {
                lazy = new SynchronizedLazyImpl(function0, null, 2, null);
                break;
            }
        }
        return lazy;
    }
    
    @NotNull
    public static final <T> Lazy<T> lazy(@NotNull final Function0<? extends T> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "initializer");
        return new SynchronizedLazyImpl<T>(function0, null, 2, null);
    }
}
