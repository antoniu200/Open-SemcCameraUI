// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import java.util.List;
import java.util.regex.Pattern;
import kotlin.jvm.functions.Function1;
import java.util.Iterator;
import kotlin.collections.IntIterator;
import java.util.Collection;
import java.util.Comparator;
import kotlin.jvm.internal.StringCompanionObject;
import java.util.Arrays;
import java.util.Locale;
import org.jetbrains.annotations.Nullable;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.nio.charset.Charset;
import kotlin.internal.InlineOnly;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000x\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\bH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\u0019\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a)\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000eH\u0087\b\u001a\u0011\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a!\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0087\b\u001a\n\u0010\u0016\u001a\u00020\u0002*\u00020\u0002\u001a\u0015\u0010\u0017\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010\u001a\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\u001c\u0010\u001d\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0015\u0010!\u001a\u00020 *\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0087\b\u001a\u0015\u0010!\u001a\u00020 *\u00020\u00022\u0006\u0010\"\u001a\u00020#H\u0087\b\u001a\n\u0010$\u001a\u00020\u0002*\u00020\u0002\u001a\u001c\u0010%\u001a\u00020 *\u00020\u00022\u0006\u0010&\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a \u0010'\u001a\u00020 *\u0004\u0018\u00010\u00022\b\u0010\u001e\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a2\u0010(\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*2\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u0010.\u001a*\u0010(\u001a\u00020\u0002*\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u0010/\u001a:\u0010(\u001a\u00020\u0002*\u00020\u00032\u0006\u0010)\u001a\u00020*2\u0006\u0010(\u001a\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u00100\u001a2\u0010(\u001a\u00020\u0002*\u00020\u00032\u0006\u0010(\u001a\u00020\u00022\u0016\u0010+\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010-0,\"\u0004\u0018\u00010-H\u0087\b¢\u0006\u0002\u00101\u001a\r\u00102\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u00103\u001a\u00020 *\u00020#\u001a\u001d\u00104\u001a\u00020\u0010*\u00020\u00022\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00104\u001a\u00020\u0010*\u00020\u00022\u0006\u00108\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00109\u001a\u00020\u0010*\u00020\u00022\u0006\u00105\u001a\u0002062\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u00109\u001a\u00020\u0010*\u00020\u00022\u0006\u00108\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u0010H\u0081\b\u001a\u001d\u0010:\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00102\u0006\u0010;\u001a\u00020\u0010H\u0087\b\u001a4\u0010<\u001a\u00020 *\u00020#2\u0006\u0010=\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020#2\u0006\u0010>\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a4\u0010<\u001a\u00020 *\u00020\u00022\u0006\u0010=\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u00022\u0006\u0010>\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0012\u0010?\u001a\u00020\u0002*\u00020#2\u0006\u0010@\u001a\u00020\u0010\u001a$\u0010A\u001a\u00020\u0002*\u00020\u00022\u0006\u0010B\u001a\u0002062\u0006\u0010C\u001a\u0002062\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010A\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010F\u001a\u00020\u0002*\u00020\u00022\u0006\u0010B\u001a\u0002062\u0006\u0010C\u001a\u0002062\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010F\u001a\u00020\u0002*\u00020\u00022\u0006\u0010D\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\"\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00020H*\u00020#2\u0006\u0010I\u001a\u00020J2\b\b\u0002\u0010K\u001a\u00020\u0010\u001a\u001c\u0010L\u001a\u00020 *\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\b\b\u0002\u0010\u001f\u001a\u00020 \u001a$\u0010L\u001a\u00020 *\u00020\u00022\u0006\u0010M\u001a\u00020\u00022\u0006\u0010N\u001a\u00020\u00102\b\b\u0002\u0010\u001f\u001a\u00020 \u001a\u0015\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010N\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010N\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\u0017\u0010P\u001a\u00020\f*\u00020\u00022\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0087\b\u001a\r\u0010Q\u001a\u00020\u0013*\u00020\u0002H\u0087\b\u001a3\u0010Q\u001a\u00020\u0013*\u00020\u00022\u0006\u0010R\u001a\u00020\u00132\b\b\u0002\u0010S\u001a\u00020\u00102\b\b\u0002\u0010N\u001a\u00020\u00102\b\b\u0002\u0010\u001c\u001a\u00020\u0010H\u0087\b\u001a\r\u0010T\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010T\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*H\u0087\b\u001a\u0017\u0010U\u001a\u00020J*\u00020\u00022\b\b\u0002\u0010V\u001a\u00020\u0010H\u0087\b\u001a\r\u0010W\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010W\u001a\u00020\u0002*\u00020\u00022\u0006\u0010)\u001a\u00020*H\u0087\b\"\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006X" }, d2 = { "CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", "codePointAt", "index", "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "contentEquals", "charSequence", "", "decapitalize", "endsWith", "suffix", "equals", "format", "locale", "Ljava/util/Locale;", "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "startIndex", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt
{
    public StringsKt__StringsJVMKt() {
    }
    
    @InlineOnly
    private static final String String(final StringBuffer buffer) {
        return new String(buffer);
    }
    
    @InlineOnly
    private static final String String(final StringBuilder builder) {
        return new String(builder);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_8);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes, final int offset, final int length) {
        return new String(bytes, offset, length, Charsets.UTF_8);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes, final int offset, final int length, final Charset charset) {
        return new String(bytes, offset, length, charset);
    }
    
    @InlineOnly
    private static final String String(final byte[] bytes, final Charset charset) {
        return new String(bytes, charset);
    }
    
    @InlineOnly
    private static final String String(final char[] value) {
        return new String(value);
    }
    
    @InlineOnly
    private static final String String(final char[] value, final int offset, final int count) {
        return new String(value, offset, count);
    }
    
    @InlineOnly
    private static final String String(final int[] codePoints, final int offset, final int count) {
        return new String(codePoints, offset, count);
    }
    
    @NotNull
    public static final String capitalize(@NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        final boolean b = substring.length() > 0;
        String string = substring;
        if (b) {
            string = substring;
            if (Character.isLowerCase(substring.charAt(0))) {
                final StringBuilder sb = new StringBuilder();
                final String substring2 = substring.substring(0, 1);
                Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                if (substring2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String upperCase = substring2.toUpperCase();
                Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase()");
                sb.append(upperCase);
                substring = substring.substring(1);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                sb.append(substring);
                string = sb.toString();
            }
        }
        return string;
    }
    
    @InlineOnly
    private static final int codePointAt(@NotNull final String s, final int index) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.codePointAt(index);
    }
    
    @InlineOnly
    private static final int codePointBefore(@NotNull final String s, final int index) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.codePointBefore(index);
    }
    
    @InlineOnly
    private static final int codePointCount(@NotNull final String s, final int beginIndex, final int endIndex) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.codePointCount(beginIndex, endIndex);
    }
    
    public static final int compareTo(@NotNull final String s, @NotNull final String s2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "other");
        if (b) {
            return s.compareToIgnoreCase(s2);
        }
        return s.compareTo(s2);
    }
    
    @InlineOnly
    private static final boolean contentEquals(@NotNull final String s, final CharSequence cs) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.contentEquals(cs);
    }
    
    @InlineOnly
    private static final boolean contentEquals(@NotNull final String s, final StringBuffer sb) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.contentEquals(sb);
    }
    
    @NotNull
    public static final String decapitalize(@NotNull String substring) {
        Intrinsics.checkParameterIsNotNull(substring, "$receiver");
        final boolean b = substring.length() > 0;
        String string = substring;
        if (b) {
            string = substring;
            if (Character.isUpperCase(substring.charAt(0))) {
                final StringBuilder sb = new StringBuilder();
                final String substring2 = substring.substring(0, 1);
                Intrinsics.checkExpressionValueIsNotNull(substring2, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                if (substring2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                final String lowerCase = substring2.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
                sb.append(lowerCase);
                substring = substring.substring(1);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                sb.append(substring);
                string = sb.toString();
            }
        }
        return string;
    }
    
    public static final boolean endsWith(@NotNull final String s, @NotNull final String suffix, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (!b) {
            return s.endsWith(suffix);
        }
        return regionMatches(s, s.length() - suffix.length(), suffix, 0, suffix.length(), true);
    }
    
    public static final boolean equals(@Nullable final String s, @Nullable final String s2, final boolean b) {
        if (s == null) {
            return s2 == null;
        }
        boolean b2;
        if (!b) {
            b2 = s.equals(s2);
        }
        else {
            b2 = s.equalsIgnoreCase(s2);
        }
        return b2;
    }
    
    @InlineOnly
    private static final String format(@NotNull String format, final Locale l, final Object... original) {
        format = String.format(l, format, Arrays.copyOf(original, original.length));
        Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(locale, this, *args)");
        return format;
    }
    
    @InlineOnly
    private static final String format(@NotNull String format, final Object... original) {
        format = String.format(format, Arrays.copyOf(original, original.length));
        Intrinsics.checkExpressionValueIsNotNull(format, "java.lang.String.format(this, *args)");
        return format;
    }
    
    @InlineOnly
    private static final String format(@NotNull final StringCompanionObject stringCompanionObject, final String format, final Object... original) {
        final String format2 = String.format(format, Arrays.copyOf(original, original.length));
        Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(format, *args)");
        return format2;
    }
    
    @InlineOnly
    private static final String format(@NotNull final StringCompanionObject stringCompanionObject, final Locale l, final String format, final Object... original) {
        final String format2 = String.format(l, format, Arrays.copyOf(original, original.length));
        Intrinsics.checkExpressionValueIsNotNull(format2, "java.lang.String.format(locale, format, *args)");
        return format2;
    }
    
    @NotNull
    public static final Comparator<String> getCASE_INSENSITIVE_ORDER(@NotNull final StringCompanionObject stringCompanionObject) {
        Intrinsics.checkParameterIsNotNull(stringCompanionObject, "$receiver");
        final Comparator<String> case_INSENSITIVE_ORDER = String.CASE_INSENSITIVE_ORDER;
        Intrinsics.checkExpressionValueIsNotNull(case_INSENSITIVE_ORDER, "java.lang.String.CASE_INSENSITIVE_ORDER");
        return case_INSENSITIVE_ORDER;
    }
    
    @InlineOnly
    private static final String intern(@NotNull String intern) {
        if (intern == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        intern = intern.intern();
        Intrinsics.checkExpressionValueIsNotNull(intern, "(this as java.lang.String).intern()");
        return intern;
    }
    
    public static final boolean isBlank(@NotNull final CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        final int length = charSequence.length();
        boolean b = false;
        if (length != 0) {
            final Iterable iterable = StringsKt__StringsKt.getIndices(charSequence);
            boolean b2 = false;
            Label_0088: {
                if (!(iterable instanceof Collection) || !((Collection)iterable).isEmpty()) {
                    final Iterator iterator = iterable.iterator();
                    while (iterator.hasNext()) {
                        if (!CharsKt__CharJVMKt.isWhitespace(charSequence.charAt(((IntIterator)iterator).nextInt()))) {
                            b2 = false;
                            break Label_0088;
                        }
                    }
                }
                b2 = true;
            }
            if (!b2) {
                return b;
            }
        }
        b = true;
        return b;
    }
    
    @InlineOnly
    private static final int nativeIndexOf(@NotNull final String s, final char ch, final int fromIndex) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.indexOf(ch, fromIndex);
    }
    
    @InlineOnly
    private static final int nativeIndexOf(@NotNull final String s, final String str, final int fromIndex) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.indexOf(str, fromIndex);
    }
    
    @InlineOnly
    private static final int nativeLastIndexOf(@NotNull final String s, final char ch, final int fromIndex) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.lastIndexOf(ch, fromIndex);
    }
    
    @InlineOnly
    private static final int nativeLastIndexOf(@NotNull final String s, final String str, final int fromIndex) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.lastIndexOf(str, fromIndex);
    }
    
    @InlineOnly
    private static final int offsetByCodePoints(@NotNull final String s, final int index, final int codePointOffset) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        return s.offsetByCodePoints(index, codePointOffset);
    }
    
    public static final boolean regionMatches(@NotNull final CharSequence charSequence, final int n, @NotNull final CharSequence charSequence2, final int n2, final int n3, final boolean b) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        Intrinsics.checkParameterIsNotNull(charSequence2, "other");
        if (charSequence instanceof String && charSequence2 instanceof String) {
            return regionMatches((String)charSequence, n, (String)charSequence2, n2, n3, b);
        }
        return StringsKt__StringsKt.regionMatchesImpl(charSequence, n, charSequence2, n2, n3, b);
    }
    
    public static final boolean regionMatches(@NotNull final String s, final int n, @NotNull final String s2, final int n2, final int n3, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "other");
        boolean b;
        if (!ignoreCase) {
            b = s.regionMatches(n, s2, n2, n3);
        }
        else {
            b = s.regionMatches(ignoreCase, n, s2, n2, n3);
        }
        return b;
    }
    
    @NotNull
    public static final String repeat(@NotNull final CharSequence s, int i) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        final int n = 0;
        final int n2 = 1;
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Count 'n' must be non-negative, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        String s2 = null;
        Label_0250: {
            switch (i) {
                default: {
                    switch (s.length()) {
                        default: {
                            final StringBuilder sb2 = new StringBuilder(s.length() * i);
                            if (1 <= i) {
                                int n3 = n2;
                                while (true) {
                                    sb2.append(s);
                                    if (n3 == i) {
                                        break;
                                    }
                                    ++n3;
                                }
                            }
                            s2 = sb2.toString();
                            Intrinsics.checkExpressionValueIsNotNull(s2, "sb.toString()");
                            break Label_0250;
                        }
                        case 1: {
                            final char char1 = s.charAt(0);
                            final char[] value = new char[i];
                            int length;
                            for (length = value.length, i = n; i < length; ++i) {
                                value[i] = char1;
                            }
                            s2 = new String(value);
                            break Label_0250;
                        }
                        case 0: {
                            s2 = "";
                            break Label_0250;
                        }
                    }
                    break;
                }
                case 1: {
                    s2 = s.toString();
                    break;
                }
                case 0: {
                    s2 = "";
                    break;
                }
            }
        }
        return s2;
    }
    
    @NotNull
    public static final String replace(@NotNull String replace, final char oldChar, final char c, final boolean b) {
        Intrinsics.checkParameterIsNotNull(replace, "$receiver");
        if (!b) {
            replace = replace.replace(oldChar, c);
            Intrinsics.checkExpressionValueIsNotNull(replace, "(this as java.lang.Strin\u2026replace(oldChar, newChar)");
            return replace;
        }
        return SequencesKt___SequencesKt.joinToString$default(StringsKt__StringsKt.splitToSequence$default((CharSequence)replace, new char[] { oldChar }, b, 0, 4, (Object)null), (CharSequence)String.valueOf(c), (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null);
    }
    
    @NotNull
    public static final String replace(@NotNull final String s, @NotNull final String s2, @NotNull final String s3, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(s2, "oldValue");
        Intrinsics.checkParameterIsNotNull(s3, "newValue");
        return SequencesKt___SequencesKt.joinToString$default(StringsKt__StringsKt.splitToSequence$default((CharSequence)s, new String[] { s2 }, b, 0, 4, (Object)null), (CharSequence)s3, (CharSequence)null, (CharSequence)null, 0, (CharSequence)null, (Function1)null, 62, (Object)null);
    }
    
    @NotNull
    public static final String replaceFirst(@NotNull String string, final char c, final char c2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(string, "$receiver");
        final CharSequence charSequence = string;
        final int indexOf$default = StringsKt__StringsKt.indexOf$default(charSequence, c, 0, b, 2, (Object)null);
        if (indexOf$default >= 0) {
            string = StringsKt__StringsKt.replaceRange(charSequence, indexOf$default, indexOf$default + 1, String.valueOf(c2)).toString();
        }
        return string;
    }
    
    @NotNull
    public static final String replaceFirst(@NotNull String string, @NotNull final String s, @NotNull final String s2, final boolean b) {
        Intrinsics.checkParameterIsNotNull(string, "$receiver");
        Intrinsics.checkParameterIsNotNull(s, "oldValue");
        Intrinsics.checkParameterIsNotNull(s2, "newValue");
        final CharSequence charSequence = string;
        final int indexOf$default = StringsKt__StringsKt.indexOf$default(charSequence, s, 0, b, 2, (Object)null);
        if (indexOf$default >= 0) {
            string = StringsKt__StringsKt.replaceRange(charSequence, indexOf$default, s.length() + indexOf$default, s2).toString();
        }
        return string;
    }
    
    @NotNull
    public static final List<String> split(@NotNull final CharSequence input, @NotNull final Pattern pattern, final int i) {
        Intrinsics.checkParameterIsNotNull(input, "$receiver");
        Intrinsics.checkParameterIsNotNull(pattern, "regex");
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Limit must be non-negative, but was ");
            sb.append(i);
            sb.append('.');
            throw new IllegalArgumentException(sb.toString().toString());
        }
        int limit;
        if ((limit = i) == 0) {
            limit = -1;
        }
        final String[] split = pattern.split(input, limit);
        Intrinsics.checkExpressionValueIsNotNull(split, "regex.split(this, if (limit == 0) -1 else limit)");
        return ArraysKt___ArraysJvmKt.asList(split);
    }
    
    public static final boolean startsWith(@NotNull final String s, @NotNull final String prefix, final int toffset, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!b) {
            return s.startsWith(prefix, toffset);
        }
        return regionMatches(s, toffset, prefix, 0, prefix.length(), b);
    }
    
    public static final boolean startsWith(@NotNull final String s, @NotNull final String prefix, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "$receiver");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!b) {
            return s.startsWith(prefix);
        }
        return regionMatches(s, 0, prefix, 0, prefix.length(), b);
    }
    
    @InlineOnly
    private static final String substring(@NotNull String substring, final int beginIndex) {
        if (substring == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        substring = substring.substring(beginIndex);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
    
    @InlineOnly
    private static final String substring(@NotNull String substring, final int beginIndex, final int endIndex) {
        if (substring == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        substring = substring.substring(beginIndex, endIndex);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        return substring;
    }
    
    @InlineOnly
    private static final byte[] toByteArray(@NotNull final String s, final Charset charset) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final byte[] bytes = s.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }
    
    @InlineOnly
    private static final char[] toCharArray(@NotNull final String s) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        final char[] charArray = s.toCharArray();
        Intrinsics.checkExpressionValueIsNotNull(charArray, "(this as java.lang.String).toCharArray()");
        return charArray;
    }
    
    @InlineOnly
    private static final char[] toCharArray(@NotNull final String s, final char[] dst, final int dstBegin, final int srcBegin, final int srcEnd) {
        if (s == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        s.getChars(srcBegin, srcEnd, dst, dstBegin);
        return dst;
    }
    
    @InlineOnly
    private static final String toLowerCase(@NotNull String lowerCase) {
        if (lowerCase == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        lowerCase = lowerCase.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
        return lowerCase;
    }
    
    @InlineOnly
    private static final String toLowerCase(@NotNull String lowerCase, final Locale locale) {
        if (lowerCase == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        lowerCase = lowerCase.toLowerCase(locale);
        Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
        return lowerCase;
    }
    
    @InlineOnly
    private static final Pattern toPattern(@NotNull final String regex, final int flags) {
        final Pattern compile = Pattern.compile(regex, flags);
        Intrinsics.checkExpressionValueIsNotNull(compile, "java.util.regex.Pattern.compile(this, flags)");
        return compile;
    }
    
    @InlineOnly
    private static final String toUpperCase(@NotNull String upperCase) {
        if (upperCase == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        upperCase = upperCase.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase()");
        return upperCase;
    }
    
    @InlineOnly
    private static final String toUpperCase(@NotNull String upperCase, final Locale locale) {
        if (upperCase == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        upperCase = upperCase.toUpperCase(locale);
        Intrinsics.checkExpressionValueIsNotNull(upperCase, "(this as java.lang.String).toUpperCase(locale)");
        return upperCase;
    }
}
