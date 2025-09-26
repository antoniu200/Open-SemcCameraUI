package kotlin.text;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StringNumberConversions.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0013\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0003\u001a\u001b\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\u0006\u001a\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005*\u00020\u0002H\u0007¢\u0006\u0002\u0010\b\u001a\u001b\u0010\u0007\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b*\u00020\u0002H\u0007¢\u0006\u0002\u0010\f\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\u000b*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\r\u001a\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0010\u001a\u001b\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\u0010\u0011¨\u0006\u0012"}, d2 = {"toByteOrNull", "", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {
    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull String receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.toByteOrNull(receiver, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Byte toByteOrNull(@NotNull String receiver, int i) {
        int iIntValue;
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Integer intOrNull = StringsKt.toIntOrNull(receiver, i);
        if (intOrNull == null || (iIntValue = intOrNull.intValue()) < -128 || iIntValue > 127) {
            return null;
        }
        return Byte.valueOf((byte) iIntValue);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull String receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.toShortOrNull(receiver, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Short toShortOrNull(@NotNull String receiver, int i) {
        int iIntValue;
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Integer intOrNull = StringsKt.toIntOrNull(receiver, i);
        if (intOrNull == null || (iIntValue = intOrNull.intValue()) < -32768 || iIntValue > 32767) {
            return null;
        }
        return Short.valueOf((short) iIntValue);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Integer toIntOrNull(@NotNull String receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.toIntOrNull(receiver, 10);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0037 A[LOOP:0: B:20:0x0037->B:30:0x004e, LOOP_START, PHI: r2 r3
  0x0037: PHI (r2v2 int) = (r2v0 int), (r2v4 int) binds: [B:19:0x0035, B:30:0x004e] A[DONT_GENERATE, DONT_INLINE]
  0x0037: PHI (r3v3 int) = (r3v2 int), (r3v4 int) binds: [B:19:0x0035, B:30:0x004e] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0058  */
    @kotlin.SinceKotlin(version = "1.1")
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final java.lang.Integer toIntOrNull(@org.jetbrains.annotations.NotNull java.lang.String r9, int r10) {
        /*
            java.lang.String r0 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r9, r0)
            kotlin.text.CharsKt.checkRadix(r10)
            int r0 = r9.length()
            r1 = 0
            if (r0 != 0) goto L10
            return r1
        L10:
            r2 = 0
            char r3 = r9.charAt(r2)
            r4 = 48
            r5 = -2147483647(0xffffffff80000001, float:-1.4E-45)
            r6 = 1
            if (r3 >= r4) goto L30
            if (r0 != r6) goto L20
            return r1
        L20:
            r4 = 45
            if (r3 != r4) goto L28
            r5 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r6
            goto L31
        L28:
            r4 = 43
            if (r3 != r4) goto L2f
            r4 = r2
            r3 = r6
            goto L32
        L2f:
            return r1
        L30:
            r3 = r2
        L31:
            r4 = r3
        L32:
            int r7 = r5 / r10
            int r0 = r0 - r6
            if (r3 > r0) goto L51
        L37:
            char r6 = r9.charAt(r3)
            int r6 = kotlin.text.CharsKt.digitOf(r6, r10)
            if (r6 >= 0) goto L42
            return r1
        L42:
            if (r2 >= r7) goto L45
            return r1
        L45:
            int r2 = r2 * r10
            int r8 = r5 + r6
            if (r2 >= r8) goto L4b
            return r1
        L4b:
            int r2 = r2 - r6
            if (r3 == r0) goto L51
            int r3 = r3 + 1
            goto L37
        L51:
            if (r4 == 0) goto L58
            java.lang.Integer r9 = java.lang.Integer.valueOf(r2)
            goto L5d
        L58:
            int r9 = -r2
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
        L5d:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringNumberConversionsKt.toIntOrNull(java.lang.String, int):java.lang.Integer");
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull String receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        return StringsKt.toLongOrNull(receiver, 10);
    }

    @SinceKotlin(version = "1.1")
    @Nullable
    public static final Long toLongOrNull(@NotNull String receiver, int i) {
        int i2;
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        CharsKt.checkRadix(i);
        int length = receiver.length();
        Long l = null;
        if (length == 0) {
            return null;
        }
        int i3 = 0;
        char cCharAt = receiver.charAt(0);
        long j = -9223372036854775807L;
        if (cCharAt >= '0') {
            i2 = i3;
        } else {
            if (length == 1) {
                return null;
            }
            if (cCharAt == '-') {
                j = Long.MIN_VALUE;
                i3 = 1;
                i2 = i3;
            } else {
                if (cCharAt != '+') {
                    return null;
                }
                i2 = 0;
                i3 = 1;
            }
        }
        long j2 = i;
        long j3 = j / j2;
        long j4 = 0;
        int i4 = length - 1;
        if (i3 <= i4) {
            while (true) {
                int iDigitOf = CharsKt.digitOf(receiver.charAt(i3), i);
                if (iDigitOf < 0 || j4 < j3) {
                    return l;
                }
                long j5 = j4 * j2;
                int i5 = i3;
                long j6 = iDigitOf;
                if (j5 >= j + j6) {
                    j4 = j5 - j6;
                    if (i5 == i4) {
                        break;
                    }
                    i3 = i5 + 1;
                    l = null;
                } else {
                    return null;
                }
            }
        }
        return i2 != 0 ? Long.valueOf(j4) : Long.valueOf(-j4);
    }
}
