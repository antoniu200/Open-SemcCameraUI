// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.math;

import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\"\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b7\u001a\u0011\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\fH\u0087\b\u001a\u0011\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010 \u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0011\u0010 \u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010!\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010!\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\"\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\"\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010#\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010#\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010$\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010$\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010%\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010%\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010&\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010&\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010'\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010'\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010(\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010(\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010)\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010)\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0018\u0010*\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010+\u001a\u00020\u0001H\u0007\u001a\u0018\u0010*\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010+\u001a\u00020\u0006H\u0007\u001a\u0011\u0010,\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010,\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u0010-\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0010\u0010-\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0007\u001a\u0019\u0010.\u001a\u00020\u00012\u0006\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u00020\u0001H\u0087\b\u001a\u0019\u0010.\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u0006H\u0087\b\u001a\u0019\u0010.\u001a\u00020\t2\u0006\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\tH\u0087\b\u001a\u0019\u0010.\u001a\u00020\f2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\fH\u0087\b\u001a\u0019\u00101\u001a\u00020\u00012\u0006\u0010/\u001a\u00020\u00012\u0006\u00100\u001a\u00020\u0001H\u0087\b\u001a\u0019\u00101\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u00062\u0006\u00100\u001a\u00020\u0006H\u0087\b\u001a\u0019\u00101\u001a\u00020\t2\u0006\u0010/\u001a\u00020\t2\u0006\u00100\u001a\u00020\tH\u0087\b\u001a\u0019\u00101\u001a\u00020\f2\u0006\u0010/\u001a\u00020\f2\u0006\u00100\u001a\u00020\fH\u0087\b\u001a\u0011\u00102\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00102\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00103\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00103\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00104\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00104\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00105\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00105\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00106\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00106\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0011\u00107\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0011\u00107\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0010\u00108\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0007\u001a\u0010\u00108\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0007\u001a\u0015\u00109\u001a\u00020\u0001*\u00020\u00012\u0006\u0010:\u001a\u00020\u0001H\u0087\b\u001a\u0015\u00109\u001a\u00020\u0006*\u00020\u00062\u0006\u0010:\u001a\u00020\u0006H\u0087\b\u001a\r\u0010;\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010;\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010<\u001a\u00020\u0001*\u00020\u00012\u0006\u0010=\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010<\u001a\u00020\u0006*\u00020\u00062\u0006\u0010=\u001a\u00020\u0006H\u0087\b\u001a\r\u0010>\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010>\u001a\u00020\u0006*\u00020\u0006H\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010?\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0018\u001a\u00020\tH\u0087\b\u001a\f\u0010@\u001a\u00020\t*\u00020\u0001H\u0007\u001a\f\u0010@\u001a\u00020\t*\u00020\u0006H\u0007\u001a\f\u0010A\u001a\u00020\f*\u00020\u0001H\u0007\u001a\f\u0010A\u001a\u00020\f*\u00020\u0006H\u0007\u001a\u0015\u0010B\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010B\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\tH\u0087\b\u001a\u0015\u0010B\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010B\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u000f\u001a\u00020\tH\u0087\b\"\u001f\u0010\u0000\u001a\u00020\u0001*\u00020\u00018\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u001f\u0010\u0000\u001a\u00020\u0006*\u00020\u00068\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\u0007\u001a\u0004\b\u0004\u0010\b\"\u001f\u0010\u0000\u001a\u00020\t*\u00020\t8\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\n\u001a\u0004\b\u0004\u0010\u000b\"\u001f\u0010\u0000\u001a\u00020\f*\u00020\f8\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\r\u001a\u0004\b\u0004\u0010\u000e\"\u001f\u0010\u000f\u001a\u00020\u0001*\u00020\u00018\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\u0003\u001a\u0004\b\u0011\u0010\u0005\"\u001f\u0010\u000f\u001a\u00020\u0006*\u00020\u00068\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\u0007\u001a\u0004\b\u0011\u0010\b\"\u001e\u0010\u000f\u001a\u00020\t*\u00020\t8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\n\u001a\u0004\b\u0011\u0010\u000b\"\u001e\u0010\u000f\u001a\u00020\t*\u00020\f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\r\u001a\u0004\b\u0011\u0010\u0012\"\u001f\u0010\u0013\u001a\u00020\u0001*\u00020\u00018\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0014\u0010\u0003\u001a\u0004\b\u0015\u0010\u0005\"\u001f\u0010\u0013\u001a\u00020\u0006*\u00020\u00068\u00c6\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0014\u0010\u0007\u001a\u0004\b\u0015\u0010\b¨\u0006C" }, d2 = { "absoluteValue", "", "absoluteValue$annotations", "(D)V", "getAbsoluteValue", "(D)D", "", "(F)V", "(F)F", "", "(I)V", "(I)I", "", "(J)V", "(J)J", "sign", "sign$annotations", "getSign", "(J)I", "ulp", "ulp$annotations", "getUlp", "abs", "x", "n", "acos", "acosh", "asin", "asinh", "atan", "atan2", "y", "atanh", "ceil", "cos", "cosh", "exp", "expm1", "floor", "hypot", "ln", "ln1p", "log", "base", "log10", "log2", "max", "a", "b", "min", "round", "sin", "sinh", "sqrt", "tan", "tanh", "truncate", "IEEErem", "divisor", "nextDown", "nextTowards", "to", "nextUp", "pow", "roundToInt", "roundToLong", "withSign", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/math/MathKt")
class MathKt__MathJVMKt extends MathKt__MathHKt
{
    public MathKt__MathJVMKt() {
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double IEEErem(final double f1, final double f2) {
        return Math.IEEEremainder(f1, f2);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float IEEErem(final float n, final float n2) {
        return (float)Math.IEEEremainder(n, n2);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double abs(final double a) {
        return Math.abs(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float abs(final float a) {
        return Math.abs(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final int abs(final int a) {
        return Math.abs(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final long abs(final long a) {
        return Math.abs(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double acos(final double a) {
        return Math.acos(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float acos(final float n) {
        return (float)Math.acos(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final double acosh(double a) {
        final double n = 1;
        if (a < n) {
            a = DoubleCompanionObject.INSTANCE.getNaN();
        }
        else if (a > Constants.upper_taylor_2_bound) {
            a = Math.log(a) + Constants.LN2;
        }
        else {
            final double a2 = a - n;
            if (a2 >= Constants.taylor_n_bound) {
                a = Math.log(a + Math.sqrt(a * a - n));
            }
            else {
                final double n2 = a = Math.sqrt(a2);
                if (n2 >= Constants.taylor_2_bound) {
                    a = n2 - n2 * n2 * n2 / 12;
                }
                a *= Math.sqrt(2.0);
            }
        }
        return a;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float acosh(final float n) {
        return (float)acosh((double)n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double asin(final double a) {
        return Math.asin(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float asin(final float n) {
        return (float)Math.asin(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final double asinh(double n) {
        double n2;
        if (n >= Constants.taylor_n_bound) {
            if (n > Constants.upper_taylor_n_bound) {
                if (n > Constants.upper_taylor_2_bound) {
                    n2 = Math.log(n) + Constants.LN2;
                }
                else {
                    n *= 2;
                    n2 = Math.log(n + 1 / n);
                }
            }
            else {
                n2 = Math.log(n + Math.sqrt(n * n + 1));
            }
        }
        else if (n <= -Constants.taylor_n_bound) {
            n2 = -asinh(-n);
        }
        else {
            n2 = n;
            if (Math.abs(n) >= Constants.taylor_2_bound) {
                n2 = n - n * n * n / 6;
            }
        }
        return n2;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float asinh(final float n) {
        return (float)asinh((double)n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double atan(final double a) {
        return Math.atan(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float atan(final float n) {
        return (float)Math.atan(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double atan2(final double y, final double x) {
        return Math.atan2(y, x);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float atan2(final float n, final float n2) {
        return (float)Math.atan2(n, n2);
    }
    
    @SinceKotlin(version = "1.2")
    public static final double atanh(final double n) {
        if (Math.abs(n) < Constants.taylor_n_bound) {
            double n2 = n;
            if (Math.abs(n) > Constants.taylor_2_bound) {
                n2 = n + n * n * n / 3;
            }
            return n2;
        }
        final double n3 = 1;
        return Math.log((n3 + n) / (n3 - n)) / 2;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float atanh(final float n) {
        return (float)atanh((double)n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double ceil(final double a) {
        return Math.ceil(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float ceil(final float n) {
        return (float)Math.ceil(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double cos(final double a) {
        return Math.cos(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float cos(final float n) {
        return (float)Math.cos(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double cosh(final double x) {
        return Math.cosh(x);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float cosh(final float n) {
        return (float)Math.cosh(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double exp(final double a) {
        return Math.exp(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float exp(final float n) {
        return (float)Math.exp(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double expm1(final double x) {
        return Math.expm1(x);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float expm1(final float n) {
        return (float)Math.expm1(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double floor(final double a) {
        return Math.floor(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float floor(final float n) {
        return (float)Math.floor(n);
    }
    
    private static final double getAbsoluteValue(final double a) {
        return Math.abs(a);
    }
    
    private static final float getAbsoluteValue(final float a) {
        return Math.abs(a);
    }
    
    private static final int getAbsoluteValue(final int a) {
        return Math.abs(a);
    }
    
    private static final long getAbsoluteValue(final long a) {
        return Math.abs(a);
    }
    
    private static final double getSign(final double d) {
        return Math.signum(d);
    }
    
    private static final float getSign(final float f) {
        return Math.signum(f);
    }
    
    public static final int getSign(int n) {
        if (n < 0) {
            n = -1;
        }
        else if (n > 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    public static final int getSign(final long n) {
        final long n2 = lcmp(n, 0L);
        int n3;
        if (n2 < 0) {
            n3 = -1;
        }
        else if (n2 > 0) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        return n3;
    }
    
    private static final double getUlp(final double d) {
        return Math.ulp(d);
    }
    
    private static final float getUlp(final float f) {
        return Math.ulp(f);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double hypot(final double x, final double y) {
        return Math.hypot(x, y);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float hypot(final float n, final float n2) {
        return (float)Math.hypot(n, n2);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double ln(final double a) {
        return Math.log(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float ln(final float n) {
        return (float)Math.log(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double ln1p(final double x) {
        return Math.log1p(x);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float ln1p(final float n) {
        return (float)Math.log1p(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final double log(final double a, final double a2) {
        if (a2 > 0.0 && a2 != 1.0) {
            return Math.log(a) / Math.log(a2);
        }
        return DoubleCompanionObject.INSTANCE.getNaN();
    }
    
    @SinceKotlin(version = "1.2")
    public static final float log(final float n, final float n2) {
        if (n2 > 0.0f && n2 != 1.0f) {
            return (float)(Math.log(n) / Math.log(n2));
        }
        return FloatCompanionObject.INSTANCE.getNaN();
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double log10(final double a) {
        return Math.log10(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float log10(final float n) {
        return (float)Math.log10(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final double log2(final double a) {
        return Math.log(a) / Constants.LN2;
    }
    
    @SinceKotlin(version = "1.2")
    public static final float log2(final float n) {
        return (float)(Math.log(n) / Constants.LN2);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double max(final double a, final double b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float max(final float a, final float b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final int max(final int a, final int b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final long max(final long a, final long b) {
        return Math.max(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double min(final double a, final double b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float min(final float a, final float b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final int min(final int a, final int b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final long min(final long a, final long b) {
        return Math.min(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double nextDown(final double start) {
        return Math.nextAfter(start, DoubleCompanionObject.INSTANCE.getNEGATIVE_INFINITY());
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float nextDown(final float start) {
        return Math.nextAfter(start, DoubleCompanionObject.INSTANCE.getNEGATIVE_INFINITY());
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double nextTowards(final double start, final double direction) {
        return Math.nextAfter(start, direction);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float nextTowards(final float start, final float n) {
        return Math.nextAfter(start, n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double nextUp(final double d) {
        return Math.nextUp(d);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float nextUp(final float f) {
        return Math.nextUp(f);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double pow(final double a, final double b) {
        return Math.pow(a, b);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double pow(final double a, final int n) {
        return Math.pow(a, n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float pow(final float n, final float n2) {
        return (float)Math.pow(n, n2);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float pow(final float n, final int n2) {
        return (float)Math.pow(n, n2);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double round(final double a) {
        return Math.rint(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float round(final float n) {
        return (float)Math.rint(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final int roundToInt(final double n) {
        if (Double.isNaN(n)) {
            throw new IllegalArgumentException("Cannot round NaN value.");
        }
        int n2 = Integer.MAX_VALUE;
        if (n <= Integer.MAX_VALUE) {
            if (n < Integer.MIN_VALUE) {
                n2 = Integer.MIN_VALUE;
            }
            else {
                n2 = (int)Math.round(n);
            }
        }
        return n2;
    }
    
    @SinceKotlin(version = "1.2")
    public static final int roundToInt(final float n) {
        if (Float.isNaN(n)) {
            throw new IllegalArgumentException("Cannot round NaN value.");
        }
        return Math.round(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final long roundToLong(final double n) {
        if (Double.isNaN(n)) {
            throw new IllegalArgumentException("Cannot round NaN value.");
        }
        return Math.round(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final long roundToLong(final float n) {
        return roundToLong((double)n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double sign(final double d) {
        return Math.signum(d);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float sign(final float f) {
        return Math.signum(f);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double sin(final double a) {
        return Math.sin(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float sin(final float n) {
        return (float)Math.sin(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double sinh(final double x) {
        return Math.sinh(x);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float sinh(final float n) {
        return (float)Math.sinh(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double sqrt(final double a) {
        return Math.sqrt(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float sqrt(final float n) {
        return (float)Math.sqrt(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double tan(final double a) {
        return Math.tan(a);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float tan(final float n) {
        return (float)Math.tan(n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double tanh(final double x) {
        return Math.tanh(x);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float tanh(final float n) {
        return (float)Math.tanh(n);
    }
    
    @SinceKotlin(version = "1.2")
    public static final double truncate(final double n) {
        double n2 = n;
        if (!Double.isNaN(n)) {
            if (Double.isInfinite(n)) {
                n2 = n;
            }
            else if (n > 0) {
                n2 = Math.floor(n);
            }
            else {
                n2 = Math.ceil(n);
            }
        }
        return n2;
    }
    
    @SinceKotlin(version = "1.2")
    public static final float truncate(final float n) {
        float n2 = n;
        if (!Float.isNaN(n)) {
            if (Float.isInfinite(n)) {
                n2 = n;
            }
            else if (n > 0) {
                n2 = (float)Math.floor(n);
            }
            else {
                n2 = (float)Math.ceil(n);
            }
        }
        return n2;
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double withSign(final double magnitude, final double sign) {
        return Math.copySign(magnitude, sign);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final double withSign(final double magnitude, final int n) {
        return Math.copySign(magnitude, n);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float withSign(final float magnitude, final float sign) {
        return Math.copySign(magnitude, sign);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final float withSign(final float magnitude, final int n) {
        return Math.copySign(magnitude, (float)n);
    }
}
