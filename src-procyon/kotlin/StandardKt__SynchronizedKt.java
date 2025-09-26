// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.functions.Function0;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0005H\u0087\b¢\u0006\u0002\u0010\u0006¨\u0006\u0007" }, d2 = { "synchronized", "R", "lock", "", "block", "Lkotlin/Function0;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/StandardKt")
class StandardKt__SynchronizedKt extends StandardKt__StandardKt
{
    public StandardKt__SynchronizedKt() {
    }
    
    @InlineOnly
    private static final <R> R synchronized(final Object o, final Function0<? extends R> function0) {
        monitorenter(o);
        try {
            return (R)function0.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            monitorexit(o);
            InlineMarker.finallyEnd(1);
        }
    }
}
