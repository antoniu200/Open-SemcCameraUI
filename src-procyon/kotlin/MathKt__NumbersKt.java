// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.DoubleCompanionObject;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0001H\u0087\b\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0005H\u0087\b\u001a\r\u0010\n\u001a\u00020\t*\u00020\u0001H\u0087\b\u001a\r\u0010\n\u001a\u00020\t*\u00020\u0005H\u0087\b\u001a\r\u0010\u000b\u001a\u00020\t*\u00020\u0001H\u0087\b\u001a\r\u0010\u000b\u001a\u00020\t*\u00020\u0005H\u0087\b\u001a\r\u0010\f\u001a\u00020\u0004*\u00020\u0001H\u0087\b\u001a\r\u0010\f\u001a\u00020\u0007*\u00020\u0005H\u0087\b\u001a\r\u0010\r\u001a\u00020\u0004*\u00020\u0001H\u0087\b\u001a\r\u0010\r\u001a\u00020\u0007*\u00020\u0005H\u0087\b¨\u0006\u000e" }, d2 = { "fromBits", "", "Lkotlin/Double$Companion;", "bits", "", "", "Lkotlin/Float$Companion;", "", "isFinite", "", "isInfinite", "isNaN", "toBits", "toRawBits", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/MathKt")
class MathKt__NumbersKt extends MathKt__BigIntegersKt
{
    public MathKt__NumbersKt() {
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double fromBits(@NotNull final DoubleCompanionObject doubleCompanionObject, final long n) {
        return Double.longBitsToDouble(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float fromBits(@NotNull final FloatCompanionObject floatCompanionObject, final int n) {
        return Float.intBitsToFloat(n);
    }
    
    @InlineOnly
    private static final boolean isFinite(final double n) {
        return !Double.isInfinite(n) && !Double.isNaN(n);
    }
    
    @InlineOnly
    private static final boolean isFinite(final float n) {
        return !Float.isInfinite(n) && !Float.isNaN(n);
    }
    
    @InlineOnly
    private static final boolean isInfinite(final double v) {
        return Double.isInfinite(v);
    }
    
    @InlineOnly
    private static final boolean isInfinite(final float v) {
        return Float.isInfinite(v);
    }
    
    @InlineOnly
    private static final boolean isNaN(final double v) {
        return Double.isNaN(v);
    }
    
    @InlineOnly
    private static final boolean isNaN(final float v) {
        return Float.isNaN(v);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final int toBits(final float value) {
        return Float.floatToIntBits(value);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final long toBits(final double value) {
        return Double.doubleToLongBits(value);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final int toRawBits(final float n) {
        return Float.floatToRawIntBits(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final long toRawBits(final double n) {
        return Double.doubleToRawLongBits(n);
    }
}
