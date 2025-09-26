// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.coroutines.experimental.intrinsics;

import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.functions.Function1;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import kotlin.coroutines.experimental.Continuation;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0014\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001c\b\u0004\u0010\u0002\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003H\u0087H\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0006\u001a5\u0010\u0007\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001c\b\u0004\u0010\u0002\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003H\u0087H\u00f8\u0001\u0000¢\u0006\u0002\u0010\u0006\u001a\u001f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004\"\u0004\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b\u0082\u0002\u0004\n\u0002\b\t¨\u0006\t" }, d2 = { "suspendCoroutineOrReturn", "T", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;", "suspendCoroutineUninterceptedOrReturn", "intercepted", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/coroutines/experimental/intrinsics/IntrinsicsKt")
class IntrinsicsKt__IntrinsicsKt extends IntrinsicsKt__IntrinsicsJvmKt
{
    public IntrinsicsKt__IntrinsicsKt() {
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> Continuation<T> intercepted(@NotNull final Continuation<? super T> continuation) {
        throw new NotImplementedError("Implementation of intercepted is intrinsic");
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final <T> Object suspendCoroutineOrReturn(final Function1<? super Continuation<? super T>, ?> function1, final Continuation<? super T> continuation) {
        InlineMarker.mark(0);
        final Object invoke = function1.invoke(CoroutineIntrinsics.normalizeContinuation((Continuation<? super Object>)continuation));
        InlineMarker.mark(1);
        return invoke;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T> Object suspendCoroutineUninterceptedOrReturn(final Function1<? super Continuation<? super T>, ?> function1, final Continuation<? super T> continuation) {
        throw new NotImplementedError("Implementation of suspendCoroutineUninterceptedOrReturn is intrinsic");
    }
}
