package kotlin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: BigDecimals.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u0010H\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00102\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u0001*\u00020\u0001H\u0087\n¨\u0006\u0012"}, d2 = {"dec", "Ljava/math/BigDecimal;", "div", "other", "inc", "minus", "mod", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/MathKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class MathKt__BigDecimalsKt {
    @InlineOnly
    private static final BigDecimal plus(@NotNull BigDecimal receiver, BigDecimal bigDecimal) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalAdd = receiver.add(bigDecimal);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalAdd, "this.add(other)");
        return bigDecimalAdd;
    }

    @InlineOnly
    private static final BigDecimal minus(@NotNull BigDecimal receiver, BigDecimal bigDecimal) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalSubtract = receiver.subtract(bigDecimal);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalSubtract, "this.subtract(other)");
        return bigDecimalSubtract;
    }

    @InlineOnly
    private static final BigDecimal times(@NotNull BigDecimal receiver, BigDecimal bigDecimal) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalMultiply = receiver.multiply(bigDecimal);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalMultiply, "this.multiply(other)");
        return bigDecimalMultiply;
    }

    @InlineOnly
    private static final BigDecimal div(@NotNull BigDecimal receiver, BigDecimal bigDecimal) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalDivide = receiver.divide(bigDecimal, RoundingMode.HALF_EVEN);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalDivide, "this.divide(other, RoundingMode.HALF_EVEN)");
        return bigDecimalDivide;
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Use rem(other) instead", replaceWith = @ReplaceWith(expression = "rem(other)", imports = {}))
    @InlineOnly
    private static final BigDecimal mod(@NotNull BigDecimal receiver, BigDecimal bigDecimal) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalRemainder = receiver.remainder(bigDecimal);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalRemainder, "this.remainder(other)");
        return bigDecimalRemainder;
    }

    @InlineOnly
    private static final BigDecimal rem(@NotNull BigDecimal receiver, BigDecimal bigDecimal) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalRemainder = receiver.remainder(bigDecimal);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalRemainder, "this.remainder(other)");
        return bigDecimalRemainder;
    }

    @InlineOnly
    private static final BigDecimal unaryMinus(@NotNull BigDecimal receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalNegate = receiver.negate();
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalNegate, "this.negate()");
        return bigDecimalNegate;
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal inc(@NotNull BigDecimal receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalAdd = receiver.add(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalAdd, "this.add(BigDecimal.ONE)");
        return bigDecimalAdd;
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal dec(@NotNull BigDecimal receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        BigDecimal bigDecimalSubtract = receiver.subtract(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalSubtract, "this.subtract(BigDecimal.ONE)");
        return bigDecimalSubtract;
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(int i) {
        BigDecimal bigDecimalValueOf = BigDecimal.valueOf(i);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalValueOf, "BigDecimal.valueOf(this.toLong())");
        return bigDecimalValueOf;
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(int i, MathContext mathContext) {
        return new BigDecimal(i, mathContext);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(long j) {
        BigDecimal bigDecimalValueOf = BigDecimal.valueOf(j);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimalValueOf, "BigDecimal.valueOf(this)");
        return bigDecimalValueOf;
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(long j, MathContext mathContext) {
        return new BigDecimal(j, mathContext);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(float f) {
        return new BigDecimal(String.valueOf(f));
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(float f, MathContext mathContext) {
        return new BigDecimal(String.valueOf(f), mathContext);
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(double d) {
        return new BigDecimal(String.valueOf(d));
    }

    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(double d, MathContext mathContext) {
        return new BigDecimal(String.valueOf(d), mathContext);
    }
}
