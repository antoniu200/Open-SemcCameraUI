// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import java.math.MathContext;
import java.math.BigDecimal;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.math.BigInteger;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0010\u001a\u00020\u0011*\u00020\u0001H\u0087\b\u001a!\u0010\u0010\u001a\u00020\u0011*\u00020\u00012\b\b\u0002\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\u0016H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0018\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f¨\u0006\u0019" }, d2 = { "and", "Ljava/math/BigInteger;", "other", "dec", "div", "inc", "inv", "minus", "or", "plus", "rem", "shl", "n", "", "shr", "times", "toBigDecimal", "Ljava/math/BigDecimal;", "scale", "mathContext", "Ljava/math/MathContext;", "toBigInteger", "", "unaryMinus", "xor", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/MathKt")
class MathKt__BigIntegersKt extends MathKt__BigDecimalsKt
{
    public MathKt__BigIntegersKt() {
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger and(@NotNull BigInteger and, final BigInteger val) {
        and = and.and(val);
        Intrinsics.checkExpressionValueIsNotNull(and, "this.and(other)");
        return and;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger dec(@NotNull BigInteger subtract) {
        Intrinsics.checkParameterIsNotNull(subtract, "$receiver");
        subtract = subtract.subtract(BigInteger.ONE);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(BigInteger.ONE)");
        return subtract;
    }
    
    @InlineOnly
    private static final BigInteger div(@NotNull BigInteger divide, final BigInteger val) {
        Intrinsics.checkParameterIsNotNull(divide, "$receiver");
        divide = divide.divide(val);
        Intrinsics.checkExpressionValueIsNotNull(divide, "this.divide(other)");
        return divide;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger inc(@NotNull BigInteger add) {
        Intrinsics.checkParameterIsNotNull(add, "$receiver");
        add = add.add(BigInteger.ONE);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(BigInteger.ONE)");
        return add;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger inv(@NotNull BigInteger not) {
        not = not.not();
        Intrinsics.checkExpressionValueIsNotNull(not, "this.not()");
        return not;
    }
    
    @InlineOnly
    private static final BigInteger minus(@NotNull BigInteger subtract, final BigInteger val) {
        Intrinsics.checkParameterIsNotNull(subtract, "$receiver");
        subtract = subtract.subtract(val);
        Intrinsics.checkExpressionValueIsNotNull(subtract, "this.subtract(other)");
        return subtract;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger or(@NotNull BigInteger or, final BigInteger val) {
        or = or.or(val);
        Intrinsics.checkExpressionValueIsNotNull(or, "this.or(other)");
        return or;
    }
    
    @InlineOnly
    private static final BigInteger plus(@NotNull BigInteger add, final BigInteger val) {
        Intrinsics.checkParameterIsNotNull(add, "$receiver");
        add = add.add(val);
        Intrinsics.checkExpressionValueIsNotNull(add, "this.add(other)");
        return add;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final BigInteger rem(@NotNull BigInteger remainder, final BigInteger val) {
        Intrinsics.checkParameterIsNotNull(remainder, "$receiver");
        remainder = remainder.remainder(val);
        Intrinsics.checkExpressionValueIsNotNull(remainder, "this.remainder(other)");
        return remainder;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger shl(@NotNull BigInteger shiftLeft, final int n) {
        shiftLeft = shiftLeft.shiftLeft(n);
        Intrinsics.checkExpressionValueIsNotNull(shiftLeft, "this.shiftLeft(n)");
        return shiftLeft;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger shr(@NotNull BigInteger shiftRight, final int n) {
        shiftRight = shiftRight.shiftRight(n);
        Intrinsics.checkExpressionValueIsNotNull(shiftRight, "this.shiftRight(n)");
        return shiftRight;
    }
    
    @InlineOnly
    private static final BigInteger times(@NotNull BigInteger multiply, final BigInteger val) {
        Intrinsics.checkParameterIsNotNull(multiply, "$receiver");
        multiply = multiply.multiply(val);
        Intrinsics.checkExpressionValueIsNotNull(multiply, "this.multiply(other)");
        return multiply;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final BigInteger val) {
        return new BigDecimal(val);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final BigInteger unscaledVal, final int scale, final MathContext mc) {
        return new BigDecimal(unscaledVal, scale, mc);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(final int n) {
        final BigInteger value = BigInteger.valueOf(n);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigInteger.valueOf(this.toLong())");
        return value;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(final long val) {
        final BigInteger value = BigInteger.valueOf(val);
        Intrinsics.checkExpressionValueIsNotNull(value, "BigInteger.valueOf(this)");
        return value;
    }
    
    @InlineOnly
    private static final BigInteger unaryMinus(@NotNull BigInteger negate) {
        Intrinsics.checkParameterIsNotNull(negate, "$receiver");
        negate = negate.negate();
        Intrinsics.checkExpressionValueIsNotNull(negate, "this.negate()");
        return negate;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger xor(@NotNull BigInteger xor, final BigInteger val) {
        xor = xor.xor(val);
        Intrinsics.checkExpressionValueIsNotNull(xor, "this.xor(other)");
        return xor;
    }
}
