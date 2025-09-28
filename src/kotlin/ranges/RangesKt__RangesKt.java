package kotlin.ranges;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: Ranges.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\u001a0\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\u0002H\b2\u0006\u0010\n\u001a\u0002H\bH\u0086\u0002¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\r0\f*\u00020\r2\u0006\u0010\n\u001a\u00020\rH\u0087\u0002¨\u0006\u000e"}, d2 = {"checkStepIsPositive", "", "isPositive", "", "step", "", "rangeTo", "Lkotlin/ranges/ClosedRange;", "T", "", "that", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Lkotlin/ranges/ClosedRange;", "Lkotlin/ranges/ClosedFloatingPointRange;", "", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/ranges/RangesKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class RangesKt__RangesKt extends RangesKt__RangesJVMKt {
    @NotNull
    public static final <T extends Comparable<? super T>> ClosedRange<T> rangeTo(@NotNull T receiver, @NotNull T that) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(that, "that");
        return new ComparableRange(receiver, that);
    }

    @SinceKotlin(version = "1.1")
    @NotNull
    public static final ClosedFloatingPointRange<Double> rangeTo(double d, double d2) {
        return new ClosedDoubleRange(d, d2);
    }

    public static final void checkStepIsPositive(boolean z, @NotNull Number step) {
        Intrinsics.checkParameterIsNotNull(step, "step");
        if (z) {
            return;
        }
        throw new IllegalArgumentException("Step must be positive, was: " + step + '.');
    }
}
