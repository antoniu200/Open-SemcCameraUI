// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.coroutines.experimental.jvm.internal;

import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0000\u001a \u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001¨\u0006\u0007" }, d2 = { "interceptContinuationIfNeeded", "Lkotlin/coroutines/experimental/Continuation;", "T", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "continuation", "normalizeContinuation", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "CoroutineIntrinsics")
public final class CoroutineIntrinsics
{
    @NotNull
    public static final <T> Continuation<T> interceptContinuationIfNeeded(@NotNull final CoroutineContext coroutineContext, @NotNull final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        final ContinuationInterceptor continuationInterceptor = coroutineContext.get((CoroutineContext.Key<ContinuationInterceptor>)ContinuationInterceptor.Key);
        if (continuationInterceptor != null) {
            final Continuation<? super Object> interceptContinuation = continuationInterceptor.interceptContinuation((Continuation<? super Object>)continuation);
            if (interceptContinuation != null) {
                return (Continuation<T>)interceptContinuation;
            }
        }
        final Continuation<? super Object> interceptContinuation = (Continuation<? super Object>)continuation;
        return (Continuation<T>)interceptContinuation;
    }
    
    @NotNull
    public static final <T> Continuation<T> normalizeContinuation(@NotNull final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        Object o;
        if (!(continuation instanceof CoroutineImpl)) {
            o = null;
        }
        else {
            o = continuation;
        }
        final CoroutineImpl coroutineImpl = (CoroutineImpl)o;
        Object o2 = continuation;
        if (coroutineImpl != null) {
            final Continuation<Object> facade = coroutineImpl.getFacade();
            o2 = continuation;
            if (facade != null) {
                o2 = facade;
            }
        }
        return (Continuation<T>)o2;
    }
}
