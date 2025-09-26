// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000R\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0005H\u0087\b\u001a\u0012\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u0007\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\bH\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\tH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000bH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\fH\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\u0005H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\rH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000eH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0011H\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\u0012H\u0087\b\u001a%\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u000e\u0010\u0003\u001a\n\u0018\u00010\u0006j\u0004\u0018\u0001`\u0007H\u0087\b\u001a!\u0010\u0013\u001a\u00020\u0014*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\n¨\u0006\u0016" }, d2 = { "appendln", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "value", "", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "Ljava/lang/StringBuffer;", "", "", "", "", "", "", "", "", "", "", "set", "", "index", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__StringBuilderJVMKt extends StringsKt__RegexExtensionsKt
{
    public StringsKt__StringBuilderJVMKt() {
    }
    
    @NotNull
    public static final Appendable appendln(@NotNull Appendable append) {
        Intrinsics.checkParameterIsNotNull(append, "$receiver");
        append = append.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkExpressionValueIsNotNull(append, "append(SystemProperties.LINE_SEPARATOR)");
        return append;
    }
    
    @InlineOnly
    private static final Appendable appendln(@NotNull Appendable append, final char c) {
        append = append.append(c);
        Intrinsics.checkExpressionValueIsNotNull(append, "append(value)");
        return appendln(append);
    }
    
    @InlineOnly
    private static final Appendable appendln(@NotNull Appendable append, final CharSequence charSequence) {
        append = append.append(charSequence);
        Intrinsics.checkExpressionValueIsNotNull(append, "append(value)");
        return appendln(append);
    }
    
    @NotNull
    public static final StringBuilder appendln(@NotNull final StringBuilder sb) {
        Intrinsics.checkParameterIsNotNull(sb, "$receiver");
        sb.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(SystemProperties.LINE_SEPARATOR)");
        return sb;
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final byte i) {
        sb.append(i);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value.toInt())");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final char c) {
        sb.append(c);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final double d) {
        sb.append(d);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final float f) {
        sb.append(f);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final int i) {
        sb.append(i);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final long lng) {
        sb.append(lng);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final CharSequence s) {
        sb.append(s);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final Object obj) {
        sb.append(obj);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final String str) {
        sb.append(str);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final StringBuffer sb2) {
        sb.append(sb2);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final StringBuilder sb2) {
        sb.append((CharSequence)sb2);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final short i) {
        sb.append(i);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value.toInt())");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final boolean b) {
        sb.append(b);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final StringBuilder appendln(@NotNull final StringBuilder sb, final char[] str) {
        sb.append(str);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return appendln(sb);
    }
    
    @InlineOnly
    private static final void set(@NotNull final StringBuilder sb, final int n, final char c) {
        Intrinsics.checkParameterIsNotNull(sb, "$receiver");
        sb.setCharAt();
    }
}
