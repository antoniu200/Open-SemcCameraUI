package kotlin.text;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: StringBuilderJVM.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000R\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u0005\n\u0002\u0010\u0019\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0005H\u0087\b\u001a\u0012\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u0007\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\bH\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\tH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000bH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\fH\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\u0005H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\rH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000eH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u000fH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0010H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0003\u001a\u00020\u0011H\u0087\b\u001a\u001f\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u0003\u001a\u0004\u0018\u00010\u0012H\u0087\b\u001a%\u0010\u0000\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u000e\u0010\u0003\u001a\n\u0018\u00010\u0006j\u0004\u0018\u0001`\u0007H\u0087\b\u001a!\u0010\u0013\u001a\u00020\u0014*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\n¨\u0006\u0016"}, d2 = {"appendln", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "value", "", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "Ljava/lang/StringBuffer;", "", "", "", "", "", "", "", "", "", "", "set", "", "index", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class StringsKt__StringBuilderJVMKt extends StringsKt__RegexExtensionsKt {
    @InlineOnly
    private static final void set(@NotNull StringBuilder receiver, int i, char c) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        receiver.setCharAt(i, c);
    }

    @NotNull
    public static final Appendable appendln(@NotNull Appendable receiver) throws IOException {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        Appendable appendableAppend = receiver.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkExpressionValueIsNotNull(appendableAppend, "append(SystemProperties.LINE_SEPARATOR)");
        return appendableAppend;
    }

    @InlineOnly
    private static final Appendable appendln(@NotNull Appendable appendable, CharSequence charSequence) throws IOException {
        Appendable appendableAppend = appendable.append(charSequence);
        Intrinsics.checkExpressionValueIsNotNull(appendableAppend, "append(value)");
        return StringsKt.appendln(appendableAppend);
    }

    @InlineOnly
    private static final Appendable appendln(@NotNull Appendable appendable, char c) throws IOException {
        Appendable appendableAppend = appendable.append(c);
        Intrinsics.checkExpressionValueIsNotNull(appendableAppend, "append(value)");
        return StringsKt.appendln(appendableAppend);
    }

    @NotNull
    public static final StringBuilder appendln(@NotNull StringBuilder receiver) {
        Intrinsics.checkParameterIsNotNull(receiver, "$receiver");
        receiver.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkExpressionValueIsNotNull(receiver, "append(SystemProperties.LINE_SEPARATOR)");
        return receiver;
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, StringBuffer stringBuffer) {
        sb.append(stringBuffer);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, CharSequence charSequence) {
        sb.append(charSequence);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, String str) {
        sb.append(str);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, Object obj) {
        sb.append(obj);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, StringBuilder sb2) {
        sb.append((CharSequence) sb2);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, char[] cArr) {
        sb.append(cArr);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, char c) {
        sb.append(c);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, boolean z) {
        sb.append(z);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, int i) {
        sb.append(i);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, short s) {
        sb.append((int) s);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value.toInt())");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, byte b) {
        sb.append((int) b);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value.toInt())");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, long j) {
        sb.append(j);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, float f) {
        sb.append(f);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }

    @InlineOnly
    private static final StringBuilder appendln(@NotNull StringBuilder sb, double d) {
        sb.append(d);
        Intrinsics.checkExpressionValueIsNotNull(sb, "append(value)");
        return StringsKt.appendln(sb);
    }
}
