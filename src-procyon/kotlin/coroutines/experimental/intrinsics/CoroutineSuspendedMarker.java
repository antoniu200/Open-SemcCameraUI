// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.coroutines.experimental.intrinsics;

import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003" }, d2 = { "Lkotlin/coroutines/experimental/intrinsics/CoroutineSuspendedMarker;", "", "()V", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class CoroutineSuspendedMarker
{
    public static final CoroutineSuspendedMarker INSTANCE;
    
    static {
        INSTANCE = new CoroutineSuspendedMarker();
    }
    
    private CoroutineSuspendedMarker() {
    }
}
