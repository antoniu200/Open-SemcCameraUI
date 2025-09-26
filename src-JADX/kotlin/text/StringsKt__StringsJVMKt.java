package kotlin.text;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StringsJVM.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000x\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\u0019\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a)\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a\n\u0010\u0016\u001a\u00020\u0002*\u00020\u0002\u001a\u0015\u0010\u0017\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010\u001a\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\u001c\u0010\u001d\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0015\u0010!\u001a\u00020 *\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0087\b\u001a\u0015\u0010!\u001a\u00020 *\u00020\u00022\u0006\u0010\"\u001a\u00020#H\u0087\b\u001a\n\u0010$\u001a\u00020\u0002*\u00020\u0002\u001a\u001c\u0010%\u001a\u00020 *\u00020\u00022\u0006\u0010&\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a \u0010'\u001a\u00020 *\u0004\u0018\u00010\u00022\b\u0010\u001e\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a2\u0010(\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*2\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u0010.\u001a*\u0010(\u001a\u00020\u0002*\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u0010/\u001a:\u0010(\u001a\u00020\u0002*\u00020\u00032\u0006\u0010)\u001a\u00020*2\u0006\u0010(\u001a\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u00100\u001a2\u0010(\u001a\u00020\u0002*\u00020\u00032\u0006\u0010(\u001a\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u00101\u001a\r\u00102\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u00103\u001a\u00020 *\u00020#\u001a\u001d\u00104\u001a\u00020\u0010*\u00020\u00022\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00104\u001a\u00020\u0010*\u00020\u00022\u0006\u00108\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00109\u001a\u00020\u0010*\u00020\u00022\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00109\u001a\u00020\u0010*\u00020\u00022\u0006\u00108\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u0010:\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00102\u0006\u0010;\u001a\u00020\u0010H\u0087\b\u001a4\u0010<\u001a\u00020 *\u00020#2\u0006\u0010=\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020#2\u0006\u0010>\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a4\u0010<\u001a\u00020 *\u00020\u00022\u0006\u0010=\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u00022\u0006\u0010>\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0012\u0010?\u001a\u00020\u0002*\u00020#2\u0006\u0010@\u001a\u00020\u0010\u001a$\u0010A\u001a\u00020\u0002*\u00020\u00022\u0006\u0010B\u001a\u0002062\u0006\u0010C\u001a\u0002062\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010A\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010F\u001a\u00020\u0002*\u00020\u00022\u0006\u0010B\u001a\u0002062\u0006\u0010C\u001a\u0002062\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010F\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\"\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00020H*\u00020#2\u0006\u0010I\u001a\u00020J2\b\b\u0002\u0010K\u001a\u00020\u0010\u001a\u001c\u0010L\u001a\u00020 *\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010L\u001a\u00020 *\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\u0006\u0010N\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0015\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010N\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010N\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\u0017\u0010P\u001a\u00020\f*\u00020\u00022\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0087\b\u001a\r\u0010Q\u001a\u00020\u0013*\u00020\u0002H\u0087\b\u001a3\u0010Q\u001a\u00020\u0013*\u00020\u00022\u0006\u0010R\u001a\u00020\u00132\b\b\u0002\u0010S\u001a\u00020\u00102\b\b\u0002\u0010N\u001a\u00020\u00102\b\b\u0002\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\r\u0010T\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010T\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*H\u0087\b\u001a\u0017\u0010U\u001a\u00020J*\u00020\u00022\b\b\u0002\u0010V\u001a\u00020\u0010H\u0087\b\u001a\r\u0010W\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010W\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*H\u0087\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006X"}, d2 = {"CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", "codePointAt", "index", "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "contentEquals", "charSequence", "", "decapitalize", "endsWith", "suffix", "equals", "format", "locale", "Ljava/util/Locale;", "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "startIndex", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt {
    @InlineOnly
    private static final int nativeIndexOf(@NotNull String str, char c, int i) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.indexOf(c, i);
    }

    @InlineOnly
    private static final int nativeIndexOf(@NotNull String str, String str2, int i) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.indexOf(str2, i);
    }

    @InlineOnly
    private static final int nativeLastIndexOf(@NotNull String str, char c, int i) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.lastIndexOf(c, i);
    }

    @InlineOnly
    private static final int nativeLastIndexOf(@NotNull String str, String str2, int i) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.lastIndexOf(str2, i);
    }

    public static final boolean equals(@Nullable String str, @Nullable String str2, boolean z) {
        if (str == null) {
            return str2 == null;
        }
        if (!z) {
            return str.equals(str2);
        }
        return str.equalsIgnoreCase(str2);
    }

    @NotNull
    public static final String replace(@NotNull String receiver, char c, char c2, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!z) {
            String strReplace = receiver.replace(c, c2);
            Intrinsics.checkExpressionValueIsNotNull(strReplace, "(this as java.lang.Strin…replace(oldChar, newChar)");
            return strReplace;
        }
        return SequencesKt.joinToString$default(StringsKt.splitToSequence$default(receiver, new char[]{c}, z, 0, 4, (Object) null), String.valueOf(c2), null, null, 0, null, null, 62, null);
    }

    @NotNull
    public static final String replace(@NotNull String receiver, @NotNull String oldValue, @NotNull String newValue, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(oldValue, "oldValue");
        Intrinsics.checkParameterIsNotNull(newValue, "newValue");
        return SequencesKt.joinToString$default(StringsKt.splitToSequence$default(receiver, new String[]{oldValue}, z, 0, 4, (Object) null), newValue, null, null, 0, null, null, 62, null);
    }

    @NotNull
    public static final String replaceFirst(@NotNull String receiver, char c, char c2, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        String str = receiver;
        int iIndexOf$default = StringsKt.indexOf$default(str, c, 0, z, 2, (Object) null);
        return iIndexOf$default < 0 ? receiver : StringsKt.replaceRange((CharSequence) str, iIndexOf$default, iIndexOf$default + 1, (CharSequence) String.valueOf(c2)).toString();
    }

    @NotNull
    public static final String replaceFirst(@NotNull String receiver, @NotNull String oldValue, @NotNull String newValue, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(oldValue, "oldValue");
        Intrinsics.checkParameterIsNotNull(newValue, "newValue");
        String str = receiver;
        int iIndexOf$default = StringsKt.indexOf$default(str, oldValue, 0, z, 2, (Object) null);
        return iIndexOf$default < 0 ? receiver : StringsKt.replaceRange((CharSequence) str, iIndexOf$default, oldValue.length() + iIndexOf$default, (CharSequence) newValue).toString();
    }

    @InlineOnly
    private static final String toUpperCase(@NotNull String str) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String upperCase = str.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase()");
        return upperCase;
    }

    @InlineOnly
    private static final String toLowerCase(@NotNull String str) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String lowerCase = str.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
        return lowerCase;
    }

    @InlineOnly
    private static final char[] toCharArray(@NotNull String str) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        char[] charArray = str.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(charArray, "(this as java.lang.String).toCharArray()");
        return charArray;
    }

    @NotNull
    public static /* bridge */ /* synthetic */ String replace$default(String str, char c, char c2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return StringsKt.replace(str, c, c2, z);
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ char[] toCharArray$default(String str, char[] cArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = str.length();
        }
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        str.getChars(i2, i3, cArr, i);
        return cArr;
    }

    @InlineOnly
    private static final char[] toCharArray(@NotNull String str, char[] cArr, int i, int i2, int i3) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        str.getChars(i2, i3, cArr, i);
        return cArr;
    }

    @NotNull
    public static /* bridge */ /* synthetic */ String replace$default(String str, String str2, String str3, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return StringsKt.replace(str, str2, str3, z);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ String replaceFirst$default(String str, char c, char c2, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return StringsKt.replaceFirst(str, c, c2, z);
    }

    @InlineOnly
    private static final String format(@NotNull String str, Object... objArr) {
        String str2 = String.format(str, Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkExpressionValueIsNotNull(str2, "java.lang.String.format(this, *args)");
        return str2;
    }

    @NotNull
    public static /* bridge */ /* synthetic */ String replaceFirst$default(String str, String str2, String str3, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return StringsKt.replaceFirst(str, str2, str3, z);
    }

    @InlineOnly
    private static final String format(@NotNull StringCompanionObject stringCompanionObject, String str, Object... objArr) {
        String str2 = String.format(str, Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkExpressionValueIsNotNull(str2, "java.lang.String.format(format, *args)");
        return str2;
    }

    public static /* bridge */ /* synthetic */ boolean equals$default(String str, String str2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.equals(str, str2, z);
    }

    @InlineOnly
    private static final String format(@NotNull String str, Locale locale, Object... objArr) {
        String str2 = String.format(locale, str, Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkExpressionValueIsNotNull(str2, "java.lang.String.format(locale, this, *args)");
        return str2;
    }

    @InlineOnly
    private static final String format(@NotNull StringCompanionObject stringCompanionObject, Locale locale, String str, Object... objArr) {
        String str2 = String.format(locale, str, Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkExpressionValueIsNotNull(str2, "java.lang.String.format(locale, format, *args)");
        return str2;
    }

    @NotNull
    public static /* bridge */ /* synthetic */ List split$default(CharSequence charSequence, Pattern pattern, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return StringsKt.split(charSequence, pattern, i);
    }

    @NotNull
    public static final List<String> split(@NotNull CharSequence receiver, @NotNull Pattern regex, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(regex, "regex");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Limit must be non-negative, but was " + i + '.').toString());
        }
        if (i == 0) {
            i = -1;
        }
        String[] strArrSplit = regex.split(receiver, i);
        Intrinsics.checkExpressionValueIsNotNull(strArrSplit, "regex.split(this, if (limit == 0) -1 else limit)");
        return ArraysKt.asList(strArrSplit);
    }

    public static /* bridge */ /* synthetic */ boolean regionMatches$default(CharSequence charSequence, int i, CharSequence charSequence2, int i2, int i3, boolean z, int i4, Object obj) {
        if ((i4 & 16) != 0) {
            z = false;
        }
        return StringsKt.regionMatches(charSequence, i, charSequence2, i2, i3, z);
    }

    @InlineOnly
    private static final String substring(@NotNull String str, int i) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = str.substring(i);
        Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.String).substring(startIndex)");
        return strSubstring;
    }

    @InlineOnly
    private static final String substring(@NotNull String str, int i, int i2) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = str.substring(i, i2);
        Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    public static /* bridge */ /* synthetic */ boolean startsWith$default(String str, String str2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.startsWith(str, str2, z);
    }

    public static final boolean startsWith(@NotNull String receiver, @NotNull String prefix, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!z) {
            return receiver.startsWith(prefix);
        }
        return StringsKt.regionMatches(receiver, 0, prefix, 0, prefix.length(), z);
    }

    public static /* bridge */ /* synthetic */ boolean startsWith$default(String str, String str2, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.startsWith(str, str2, i, z);
    }

    public static final boolean startsWith(@NotNull String receiver, @NotNull String prefix, int i, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!z) {
            return receiver.startsWith(prefix, i);
        }
        return StringsKt.regionMatches(receiver, i, prefix, 0, prefix.length(), z);
    }

    public static /* bridge */ /* synthetic */ boolean endsWith$default(String str, String str2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.endsWith(str, str2, z);
    }

    public static final boolean endsWith(@NotNull String receiver, @NotNull String suffix, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (!z) {
            return receiver.endsWith(suffix);
        }
        return StringsKt.regionMatches(receiver, receiver.length() - suffix.length(), suffix, 0, suffix.length(), true);
    }

    @InlineOnly
    private static final String String(byte[] bArr, int i, int i2, Charset charset) {
        return new String(bArr, i, i2, charset);
    }

    @InlineOnly
    private static final String String(byte[] bArr, Charset charset) {
        return new String(bArr, charset);
    }

    @InlineOnly
    private static final String String(byte[] bArr, int i, int i2) {
        return new String(bArr, i, i2, Charsets.UTF_8);
    }

    @InlineOnly
    private static final String String(byte[] bArr) {
        return new String(bArr, Charsets.UTF_8);
    }

    @InlineOnly
    private static final String String(char[] cArr) {
        return new String(cArr);
    }

    @InlineOnly
    private static final String String(char[] cArr, int i, int i2) {
        return new String(cArr, i, i2);
    }

    @InlineOnly
    private static final String String(int[] iArr, int i, int i2) {
        return new String(iArr, i, i2);
    }

    @InlineOnly
    private static final String String(StringBuffer stringBuffer) {
        return new String(stringBuffer);
    }

    @InlineOnly
    private static final String String(StringBuilder sb) {
        return new String(sb);
    }

    @InlineOnly
    private static final int codePointAt(@NotNull String str, int i) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.codePointAt(i);
    }

    @InlineOnly
    private static final int codePointBefore(@NotNull String str, int i) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.codePointBefore(i);
    }

    @InlineOnly
    private static final int codePointCount(@NotNull String str, int i, int i2) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.codePointCount(i, i2);
    }

    public static /* bridge */ /* synthetic */ int compareTo$default(String str, String str2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.compareTo(str, str2, z);
    }

    public static final int compareTo(@NotNull String receiver, @NotNull String other, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (z) {
            return receiver.compareToIgnoreCase(other);
        }
        return receiver.compareTo(other);
    }

    @InlineOnly
    private static final boolean contentEquals(@NotNull String str, CharSequence charSequence) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.contentEquals(charSequence);
    }

    @InlineOnly
    private static final boolean contentEquals(@NotNull String str, StringBuffer stringBuffer) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.contentEquals(stringBuffer);
    }

    @InlineOnly
    private static final String intern(@NotNull String str) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String strIntern = str.intern();
        Intrinsics.checkExpressionValueIsNotNull(strIntern, "(this as java.lang.String).intern()");
        return strIntern;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final boolean isBlank(@org.jetbrains.annotations.NotNull java.lang.CharSequence r4) {
        /*
            java.lang.String r0 = "$receiver"
            kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r4, r0)
            int r0 = r4.length()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L40
            kotlin.ranges.IntRange r0 = kotlin.text.StringsKt.getIndices(r4)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            boolean r3 = r0 instanceof java.util.Collection
            if (r3 == 0) goto L22
            r3 = r0
            java.util.Collection r3 = (java.util.Collection) r3
            boolean r3 = r3.isEmpty()
            if (r3 == 0) goto L22
        L20:
            r4 = r2
            goto L3e
        L22:
            java.util.Iterator r0 = r0.iterator()
        L26:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L20
            r3 = r0
            kotlin.collections.IntIterator r3 = (kotlin.collections.IntIterator) r3
            int r3 = r3.nextInt()
            char r3 = r4.charAt(r3)
            boolean r3 = kotlin.text.CharsKt.isWhitespace(r3)
            if (r3 != 0) goto L26
            r4 = r1
        L3e:
            if (r4 == 0) goto L41
        L40:
            r1 = r2
        L41:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringsJVMKt.isBlank(java.lang.CharSequence):boolean");
    }

    @InlineOnly
    private static final int offsetByCodePoints(@NotNull String str, int i, int i2) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return str.offsetByCodePoints(i, i2);
    }

    public static final boolean regionMatches(@NotNull CharSequence receiver, int i, @NotNull CharSequence other, int i2, int i3, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if ((receiver instanceof String) && (other instanceof String)) {
            return StringsKt.regionMatches((String) receiver, i, (String) other, i2, i3, z);
        }
        return StringsKt.regionMatchesImpl(receiver, i, other, i2, i3, z);
    }

    public static /* bridge */ /* synthetic */ boolean regionMatches$default(String str, int i, String str2, int i2, int i3, boolean z, int i4, Object obj) {
        if ((i4 & 16) != 0) {
            z = false;
        }
        return StringsKt.regionMatches(str, i, str2, i2, i3, z);
    }

    public static final boolean regionMatches(@NotNull String receiver, int i, @NotNull String other, int i2, int i3, boolean z) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (!z) {
            return receiver.regionMatches(i, other, i2, i3);
        }
        return receiver.regionMatches(z, i, other, i2, i3);
    }

    @InlineOnly
    private static final String toLowerCase(@NotNull String str, Locale locale) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String lowerCase = str.toLowerCase(locale);
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
        return lowerCase;
    }

    @InlineOnly
    private static final String toUpperCase(@NotNull String str, Locale locale) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String upperCase = str.toUpperCase(locale);
        Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase(locale)");
        return upperCase;
    }

    @InlineOnly
    private static final byte[] toByteArray(@NotNull String str, Charset charset) {
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        byte[] bytes = str.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ byte[] toByteArray$default(String str, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        if (str == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        byte[] bytes = str.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }

    @InlineOnly
    private static final Pattern toPattern(@NotNull String str, int i) {
        Pattern patternCompile = Pattern.compile(str, i);
        Intrinsics.checkExpressionValueIsNotNull(patternCompile, "java.util.regex.Pattern.compile(this, flags)");
        return patternCompile;
    }

    @NotNull
    public static final String capitalize(@NotNull String receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(receiver.length() > 0) || !Character.isLowerCase(receiver.charAt(0))) {
            return receiver;
        }
        StringBuilder sb = new StringBuilder();
        String strSubstring = receiver.substring(0, 1);
        Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        if (strSubstring == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String upperCase = strSubstring.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase()");
        sb.append(upperCase);
        String strSubstring2 = receiver.substring(1);
        Intrinsics.checkExpressionValueIsNotNull(strSubstring2, "(this as java.lang.String).substring(startIndex)");
        sb.append(strSubstring2);
        return sb.toString();
    }

    @NotNull
    public static final String decapitalize(@NotNull String receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        if (!(receiver.length() > 0) || !Character.isUpperCase(receiver.charAt(0))) {
            return receiver;
        }
        StringBuilder sb = new StringBuilder();
        String strSubstring = receiver.substring(0, 1);
        Intrinsics.checkExpressionValueIsNotNull(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        if (strSubstring == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String lowerCase = strSubstring.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
        sb.append(lowerCase);
        String strSubstring2 = receiver.substring(1);
        Intrinsics.checkExpressionValueIsNotNull(strSubstring2, "(this as java.lang.String).substring(startIndex)");
        sb.append(strSubstring2);
        return sb.toString();
    }

    @NotNull
    public static final String repeat(@NotNull CharSequence receiver, int i) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        int i2 = 1;
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Count 'n' must be non-negative, but was " + i + '.').toString());
        }
        switch (i) {
            case 0:
                return "";
            case 1:
                return receiver.toString();
            default:
                switch (receiver.length()) {
                    case 0:
                        return "";
                    case 1:
                        char cCharAt = receiver.charAt(0);
                        char[] cArr = new char[i];
                        int length = cArr.length;
                        for (int i3 = 0; i3 < length; i3++) {
                            cArr[i3] = cCharAt;
                        }
                        return new String(cArr);
                    default:
                        StringBuilder sb = new StringBuilder(receiver.length() * i);
                        if (1 <= i) {
                            while (true) {
                                sb.append(receiver);
                                if (i2 != i) {
                                    i2++;
                                }
                            }
                        }
                        String string = sb.toString();
                        Intrinsics.checkExpressionValueIsNotNull(string, "sb.toString()");
                        return string;
                }
        }
    }

    @NotNull
    public static final Comparator<String> getCASE_INSENSITIVE_ORDER(@NotNull StringCompanionObject receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        Intrinsics.checkExpressionValueIsNotNull(comparator, "java.lang.String.CASE_INSENSITIVE_ORDER");
        return comparator;
    }

    @InlineOnly
    static /* bridge */ /* synthetic */ Pattern toPattern$default(String str, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        Pattern patternCompile = Pattern.compile(str, i);
        Intrinsics.checkExpressionValueIsNotNull(patternCompile, "java.util.regex.Pattern.compile(this, flags)");
        return patternCompile;
    }
}
