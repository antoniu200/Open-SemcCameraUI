// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.coroutines.experimental;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u0000 \u00062\u00020\u0001:\u0001\u0006J\"\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003H&¨\u0006\u0007" }, d2 = { "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "interceptContinuation", "Lkotlin/coroutines/experimental/Continuation;", "T", "continuation", "Key", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@SinceKotlin(version = "1.1")
public interface ContinuationInterceptor extends Element
{
    public static final Key Key = ContinuationInterceptor.Key.$$INSTANCE;
    
    @NotNull
     <T> Continuation<T> interceptContinuation(@NotNull final Continuation<? super T> p0);
    
    @Metadata(bv = { 1, 0, 2 }, k = 3, mv = { 1, 1, 10 })
    public static final class DefaultImpls
    {
        public static <R> R fold(final ContinuationInterceptor continuationInterceptor, final R r, @NotNull final Function2<? super R, ? super Element, ? extends R> function2) {
            Intrinsics.checkParameterIsNotNull(function2, "operation");
            return Element.DefaultImpls.fold((Element)continuationInterceptor, r, function2);
        }
        
        @Nullable
        public static <E extends Element> E get(final ContinuationInterceptor continuationInterceptor, @NotNull final CoroutineContext.Key<E> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return Element.DefaultImpls.get((Element)continuationInterceptor, key);
        }
        
        @NotNull
        public static CoroutineContext minusKey(final ContinuationInterceptor continuationInterceptor, @NotNull final CoroutineContext.Key<?> key) {
            Intrinsics.checkParameterIsNotNull(key, "key");
            return Element.DefaultImpls.minusKey((Element)continuationInterceptor, key);
        }
        
        @NotNull
        public static CoroutineContext plus(final ContinuationInterceptor continuationInterceptor, @NotNull final CoroutineContext coroutineContext) {
            Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
            return Element.DefaultImpls.plus((Element)continuationInterceptor, coroutineContext);
        }
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004" }, d2 = { "Lkotlin/coroutines/experimental/ContinuationInterceptor$Key;", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "Lkotlin/coroutines/experimental/ContinuationInterceptor;", "()V", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Key implements CoroutineContext.Key<ContinuationInterceptor>
    {
        static final Key $$INSTANCE;
        
        static {
            $$INSTANCE = new Key();
        }
        
        private Key() {
        }
    }
}
