// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import java.math.BigInteger;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import java.math.MathContext;
import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function1;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000X\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a4\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0005H\u0082\b¢\u0006\u0004\b\u0006\u0010\u0007\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0003H\u0087\b\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u0003H\u0007\u001a\u0016\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\r\u0010\r\u001a\u00020\u000e*\u00020\u0003H\u0087\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007\u001a\u0016\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\r\u0010\u0012\u001a\u00020\u0013*\u00020\u0003H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0015*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u0017*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u0018\u001a\u0004\u0018\u00010\u0017*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0019\u001a\r\u0010\u001a\u001a\u00020\u001b*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u001c\u001a\u0004\u0018\u00010\u001b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u001d\u001a\r\u0010\u001e\u001a\u00020\u0010*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001e\u001a\u00020\u0010*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u001f\u001a\u00020 *\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010!\u001a\u00020\"*\u00020\u0003H\u0087\b\u001a\u0015\u0010!\u001a\u00020\"*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020 2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\"2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b¨\u0006$" }, d2 = { "screenFloatValue", "T", "str", "", "parse", "Lkotlin/Function1;", "screenFloatValue$StringsKt__StringNumberConversionsJVMKt", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "toBigDecimal", "Ljava/math/BigDecimal;", "mathContext", "Ljava/math/MathContext;", "toBigDecimalOrNull", "toBigInteger", "Ljava/math/BigInteger;", "radix", "", "toBigIntegerOrNull", "toBoolean", "", "toByte", "", "toDouble", "", "toDoubleOrNull", "(Ljava/lang/String;)Ljava/lang/Double;", "toFloat", "", "toFloatOrNull", "(Ljava/lang/String;)Ljava/lang/Float;", "toInt", "toLong", "", "toShort", "", "toString", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsJVMKt extends StringsKt__StringBuilderKt
{
    public StringsKt__StringNumberConversionsJVMKt() {
    }
    
    private static final <T> T screenFloatValue$StringsKt__StringNumberConversionsJVMKt(final String s, final Function1<? super String, ? extends T> function1) {
        T invoke = null;
        try {
            if (ScreenFloatValueRegEx.value.matches(s)) {
                invoke = (T)function1.invoke(s);
            }
            return invoke;
        }
        catch (final NumberFormatException ex) {
            invoke = invoke;
            return invoke;
        }
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final String val) {
        return new BigDecimal(val);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull final String val, final MathContext mc) {
        return new BigDecimal(val, mc);
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigDecimal toBigDecimalOrNull(@NotNull final String val) {
        Intrinsics.checkParameterIsNotNull(val, "$receiver");
        BigDecimal bigDecimal2;
        final BigDecimal bigDecimal = bigDecimal2 = null;
        try {
            if (ScreenFloatValueRegEx.value.matches(val)) {
                bigDecimal2 = new BigDecimal(val);
            }
            return bigDecimal2;
        }
        catch (final NumberFormatException ex) {
            bigDecimal2 = bigDecimal;
            return bigDecimal2;
        }
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigDecimal toBigDecimalOrNull(@NotNull final String val, @NotNull final MathContext mc) {
        Intrinsics.checkParameterIsNotNull(val, "$receiver");
        Intrinsics.checkParameterIsNotNull(mc, "mathContext");
        BigDecimal bigDecimal2;
        final BigDecimal bigDecimal = bigDecimal2 = null;
        try {
            if (ScreenFloatValueRegEx.value.matches(val)) {
                bigDecimal2 = new BigDecimal(val, mc);
            }
            return bigDecimal2;
        }
        catch (final NumberFormatException ex) {
            bigDecimal2 = bigDecimal;
            return bigDecimal2;
        }
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(@NotNull final String val) {
        return new BigInteger(val);
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(@NotNull final String val, final int n) {
        return new BigInteger(val, CharsKt__CharJVMKt.checkRadix(n));
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return toBigIntegerOrNull(s, 10);
    }
    
    @SinceKotlin(version = "1.2")
    @Nullable
    public static final BigInteger toBigIntegerOrNull(@NotNull final String val, final int n) {
        Intrinsics.checkParameterIsNotNull(val, "$receiver");
        CharsKt__CharJVMKt.checkRadix(n);
        final int length = val.length();
        int i = 0;
        switch (length) {
            default: {
                if (val.charAt(0) == '-') {
                    i = 1;
                }
                while (i < length) {
                    if (CharsKt__CharJVMKt.digitOf(val.charAt(i), n) < 0) {
                        return null;
                    }
                    ++i;
                }
                break;
            }
            case 1: {
                if (CharsKt__CharJVMKt.digitOf(val.charAt(0), n) < 0) {
                    return null;
                }
                break;
            }
            case 0: {
                return null;
            }
        }
        return new BigInteger(val, CharsKt__CharJVMKt.checkRadix(n));
    }
    
    @InlineOnly
    private static final boolean toBoolean(@NotNull final String s) {
        return Boolean.parseBoolean(s);
    }
    
    @InlineOnly
    private static final byte toByte(@NotNull final String s) {
        return Byte.parseByte(s);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final byte toByte(@NotNull final String s, final int n) {
        return Byte.parseByte(s, CharsKt__CharJVMKt.checkRadix(n));
    }
    
    @InlineOnly
    private static final double toDouble(@NotNull final String s) {
        return Double.parseDouble(s);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Double toDoubleOrNull(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Double value = null;
        try {
            if (ScreenFloatValueRegEx.value.matches(s)) {
                value = Double.parseDouble(s);
            }
            return value;
        }
        catch (final NumberFormatException ex) {
            value = value;
            return value;
        }
    }
    
    @InlineOnly
    private static final float toFloat(@NotNull final String s) {
        return Float.parseFloat(s);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Float toFloatOrNull(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Float value = null;
        try {
            if (ScreenFloatValueRegEx.value.matches(s)) {
                value = Float.parseFloat(s);
            }
            return value;
        }
        catch (final NumberFormatException ex) {
            value = value;
            return value;
        }
    }
    
    @InlineOnly
    private static final int toInt(@NotNull final String s) {
        return Integer.parseInt(s);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final int toInt(@NotNull final String s, final int n) {
        return Integer.parseInt(s, CharsKt__CharJVMKt.checkRadix(n));
    }
    
    @InlineOnly
    private static final long toLong(@NotNull final String s) {
        return Long.parseLong(s);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final long toLong(@NotNull final String s, final int n) {
        return Long.parseLong(s, CharsKt__CharJVMKt.checkRadix(n));
    }
    
    @InlineOnly
    private static final short toShort(@NotNull final String s) {
        return Short.parseShort(s);
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final short toShort(@NotNull final String s, final int n) {
        return Short.parseShort(s, CharsKt__CharJVMKt.checkRadix(n));
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final byte i, final int n) {
        final String string = Integer.toString(i, CharsKt__CharJVMKt.checkRadix(CharsKt__CharJVMKt.checkRadix(n)));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final int i, final int n) {
        final String string = Integer.toString(i, CharsKt__CharJVMKt.checkRadix(n));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final long i, final int n) {
        final String string = Long.toString(i, CharsKt__CharJVMKt.checkRadix(n));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Long.toString(this, checkRadix(radix))");
        return string;
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String toString(final short i, final int n) {
        final String string = Integer.toString(i, CharsKt__CharJVMKt.checkRadix(CharsKt__CharJVMKt.checkRadix(n)));
        Intrinsics.checkExpressionValueIsNotNull(string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string;
    }
}
