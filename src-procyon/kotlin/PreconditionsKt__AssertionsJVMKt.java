// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.jvm.functions.Function0;
import kotlin.internal.InlineOnly;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\b¨\u0006\u0007" }, d2 = { "assert", "", "value", "", "lazyMessage", "Lkotlin/Function0;", "", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/PreconditionsKt")
class PreconditionsKt__AssertionsJVMKt
{
    public PreconditionsKt__AssertionsJVMKt() {
    }
    
    @InlineOnly
    private static final void assert(final boolean b) {
        if (_Assertions.ENABLED && !b) {
            throw new AssertionError((Object)"Assertion failed");
        }
    }
    
    @InlineOnly
    private static final void assert(final boolean b, final Function0<?> function0) {
        if (_Assertions.ENABLED && !b) {
            throw new AssertionError(function0.invoke());
        }
    }
}
