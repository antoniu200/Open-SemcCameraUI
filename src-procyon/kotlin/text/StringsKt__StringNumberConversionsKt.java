// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import org.jetbrains.annotations.Nullable;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0000\n\u0002\u0010\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0013\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0003\u001a\u001b\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\u0006\u001a\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005*\u00020\u0002H\u0007¢\u0006\u0002\u0010\b\u001a\u001b\u0010\u0007\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b*\u00020\u0002H\u0007¢\u0006\u0002\u0010\f\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\u000b*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\r\u001a\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0010\u001a\u001b\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\u0011¨\u0006\u0012" }, d2 = { "toByteOrNull", "", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt
{
    public StringsKt__StringNumberConversionsKt() {
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return toByteOrNull(s, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull final String s, int intValue) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        final Integer intOrNull = toIntOrNull(s, intValue);
        if (intOrNull == null) {
            return null;
        }
        intValue = intOrNull;
        if (intValue >= -128 && intValue <= 127) {
            return (byte)intValue;
        }
        return null;
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return toIntOrNull(s, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull final String s, final int n) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        CharsKt__CharJVMKt.checkRadix(n);
        int length = s.length();
        if (length == 0) {
            return null;
        }
        int i = 0;
        int n2 = 0;
        final char char1 = s.charAt(0);
        int n3 = -2147483647;
        int index = 0;
        int n4 = 0;
        Label_0088: {
            if (char1 < '0') {
                if (length == 1) {
                    return null;
                }
                if (char1 == '-') {
                    n3 = Integer.MIN_VALUE;
                    index = 1;
                }
                else {
                    if (char1 == '+') {
                        n4 = 0;
                        index = 1;
                        break Label_0088;
                    }
                    return null;
                }
            }
            else {
                index = 0;
            }
            n4 = index;
        }
        final int n5 = n3 / n;
        --length;
        if (index <= length) {
            while (true) {
                final int digit = CharsKt__CharJVMKt.digitOf(s.charAt(index), n);
                if (digit < 0) {
                    return null;
                }
                if (n2 < n5) {
                    return null;
                }
                final int n6 = n2 * n;
                if (n6 < n3 + digit) {
                    return null;
                }
                n2 = (i = n6 - digit);
                if (index == length) {
                    break;
                }
                ++index;
            }
        }
        Integer n7;
        if (n4 != 0) {
            n7 = i;
        }
        else {
            n7 = -i;
        }
        return n7;
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return toLongOrNull(s, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull final String s, final int n) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        CharsKt__CharJVMKt.checkRadix(n);
        int length = s.length();
        if (length == 0) {
            return null;
        }
        int index = 0;
        final char char1 = s.charAt(0);
        long n3;
        final long n2 = n3 = -9223372036854775807L;
        int n4 = 0;
        Label_0092: {
            if (char1 < '0') {
                if (length == 1) {
                    return null;
                }
                if (char1 == '-') {
                    n3 = Long.MIN_VALUE;
                    index = 1;
                }
                else {
                    if (char1 == '+') {
                        n4 = 0;
                        index = 1;
                        n3 = n2;
                        break Label_0092;
                    }
                    return null;
                }
            }
            n4 = index;
        }
        final long n5 = n;
        final long n6 = n3 / n5;
        long n7 = 0L;
        --length;
        long l = n7;
        if (index <= length) {
            while (true) {
                final int digit = CharsKt__CharJVMKt.digitOf(s.charAt(index), n);
                if (digit < 0) {
                    return null;
                }
                if (n7 < n6) {
                    return null;
                }
                final long n8 = n7 * n5;
                final long n9 = digit;
                if (n8 < n3 + n9) {
                    return null;
                }
                n7 = (l = n8 - n9);
                if (index == length) {
                    break;
                }
                ++index;
            }
        }
        Long n10;
        if (n4 != 0) {
            n10 = l;
        }
        else {
            n10 = -l;
        }
        return n10;
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        return toShortOrNull(s, 10);
    }
    
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull final String s, int intValue) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        final Integer intOrNull = toIntOrNull(s, intValue);
        if (intOrNull == null) {
            return null;
        }
        intValue = intOrNull;
        if (intValue >= -32768 && intValue <= 32767) {
            return (short)intValue;
        }
        return null;
    }
}
