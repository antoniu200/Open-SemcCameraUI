// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import java.math.MathContext;
import java.math.RoundingMode;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u0010H\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00102\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u0001*\u00020\u0001H\u0087\n¨\u0006\u0012" }, d2 = { "dec", "Ljava/math/BigDecimal;", "div", "other", "inc", "minus", "mod", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/MathKt")
class MathKt__BigDecimalsKt
{
    public MathKt__BigDecimalsKt() {
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal dec(@NotNull BigDecimal subtract) {
        Intrinsics.checkParameterIsNotNull(subtract, "$receiver");
        subtract = subtract.subtract(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(BigDecimal.ONE)");
        return subtract;
    }
    
    @InlineOnly
    private static final BigDecimal div(@NotNull BigDecimal divide, final BigDecimal divisor) {
        Intrinsics.checkParameterIsNotNull(divide, "$receiver");
        divide = divide.divide(divisor, RoundingMode.HALF_EVEN);
        Intrinsics.checkExpressionValueIsNotNull(divide, "this.divide(other, RoundingMode.HALF_EVEN)");
        return divide;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal inc(@NotNull BigDecimal add) {
        Intrinsics.checkParameterIsNotNull(add, "$receiver");
        add = add.add(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(BigDecimal.ONE)");
        return add;
    }
    
    @InlineOnly
    private static final BigDecimal minus(@NotNull BigDecimal subtract, final BigDecimal subtrahend) {
        Intrinsics.checkParameterIsNotNull(subtract, "$receiver");
        subtract = subtract.subtract(subtrahend);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(other)");
        return subtract;
    }
    
    @Deprecated(level = DeprecationLevel.WARNING, message = "Use rem(other) instead", replaceWith = @ReplaceWith(expression = "rem(other)", imports = {}))
    @InlineOnly
    private static final BigDecimal mod(@NotNull BigDecimal remainder, final BigDecimal divisor) {
        Intrinsics.checkParameterIsNotNull(remainder, "$receiver");
        remainder = remainder.remainder(divisor);
        Intrinsics.checkExpressionValueIsNotNull(remainder, "this.remainder(other)");
        return remainder;
    }
    
    @InlineOnly
    private static final BigDecimal plus(@NotNull BigDecimal add, final BigDecimal augend) {
        Intrinsics.checkParameterIsNotNull(add, "$receiver");
        add = add.add(augend);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(other)");
        return add;
    }
    
    @InlineOnly
    private static final BigDecimal rem(@NotNull BigDecimal remainder, final BigDecimal divisor) {
        Intrinsics.checkParameterIsNotNull(remainder, "$receiver");
        remainder = remainder.remainder(divisor);
        Intrinsics.checkExpressionValueIsNotNull(remainder, "this.remainder(other)");
        return remainder;
    }
    
    @InlineOnly
    private static final BigDecimal times(@NotNull BigDecimal multiply, final BigDecimal multiplicand) {
        Intrinsics.checkParameterIsNotNull(multiply, "$receiver");
        multiply = multiply.multiply(multiplicand);
        Intrinsics.checkExpressionValueIsNotNull(multiply, "this.multiply(other)");
        return multiply;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final double d) {
        return new BigDecimal(String.valueOf(d));
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final double d, final MathContext mc) {
        return new BigDecimal(String.valueOf(d), mc);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final float f) {
        return new BigDecimal(String.valueOf(f));
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final float f, final MathContext mc) {
        return new BigDecimal(String.valueOf(f), mc);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final int n) {
        final BigDecimal value = BigDecimal.valueOf(n);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigDecimal.valueOf(this.toLong())");
        return value;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final int val, final MathContext mc) {
        return new BigDecimal(val, mc);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final long val) {
        final BigDecimal value = BigDecimal.valueOf(val);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigDecimal.valueOf(this)");
        return value;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(final long val, final MathContext mc) {
        return new BigDecimal(val, mc);
    }
    
    @InlineOnly
    private static final BigDecimal unaryMinus(@NotNull BigDecimal negate) {
        Intrinsics.checkParameterIsNotNull(negate, "$receiver");
        negate = negate.negate();
        Intrinsics.checkExpressionValueIsNotNull(negate, "this.negate()");
        return negate;
    }
}
