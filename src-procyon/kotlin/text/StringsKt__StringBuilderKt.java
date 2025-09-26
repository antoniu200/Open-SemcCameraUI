// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.internal.InlineOnly;
import kotlin.SinceKotlin;
import kotlin.Unit;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000B\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0007\u001a.\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u001b\u0010\u0004\u001a\u0017\u0012\b\u0012\u00060\u0006j\u0002`\u0007\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\u0002\b\tH\u0087\b\u001a&\u0010\u0000\u001a\u00020\u00012\u001b\u0010\u0004\u001a\u0017\u0012\b\u0012\u00060\u0006j\u0002`\u0007\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\u0002\b\tH\u0087\b\u001a5\u0010\n\u001a\u0002H\u000b\"\f\b\u0000\u0010\u000b*\u00060\fj\u0002`\r*\u0002H\u000b2\u0016\u0010\u000e\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00100\u000f\"\u0004\u0018\u00010\u0010¢\u0006\u0002\u0010\u0011\u001a/\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0016\u0010\u000e\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00120\u000f\"\u0004\u0018\u00010\u0012¢\u0006\u0002\u0010\u0013\u001a/\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0016\u0010\u000e\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00010\u000f\"\u0004\u0018\u00010\u0001¢\u0006\u0002\u0010\u0014\u001a9\u0010\u0015\u001a\u00020\b\"\u0004\b\u0000\u0010\u000b*\u00060\fj\u0002`\r2\u0006\u0010\u0016\u001a\u0002H\u000b2\u0014\u0010\u0017\u001a\u0010\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u0005H\u0000¢\u0006\u0002\u0010\u0018¨\u0006\u0019" }, d2 = { "buildString", "", "capacity", "", "builderAction", "Lkotlin/Function1;", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "", "Lkotlin/ExtensionFunctionType;", "append", "T", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "value", "", "", "(Ljava/lang/Appendable;[Ljava/lang/CharSequence;)Ljava/lang/Appendable;", "", "(Ljava/lang/StringBuilder;[Ljava/lang/Object;)Ljava/lang/StringBuilder;", "(Ljava/lang/StringBuilder;[Ljava/lang/String;)Ljava/lang/StringBuilder;", "appendElement", "element", "transform", "(Ljava/lang/Appendable;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "kotlin-stdlib" }, k = 5, mv = { 1, 1, 10 }, xi = 1, xs = "kotlin/text/StringsKt")
class StringsKt__StringBuilderKt extends StringsKt__StringBuilderJVMKt
{
    public StringsKt__StringBuilderKt() {
    }
    
    @NotNull
    public static final <T extends Appendable> T append(@NotNull final T t, @NotNull final CharSequence... array) {
        Intrinsics.checkParameterIsNotNull(t, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "value");
        for (int length = array.length, i = 0; i < length; ++i) {
            t.append(array[i]);
        }
        return t;
    }
    
    @NotNull
    public static final StringBuilder append(@NotNull final StringBuilder sb, @NotNull final Object... array) {
        Intrinsics.checkParameterIsNotNull(sb, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "value");
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(array[i]);
        }
        return sb;
    }
    
    @NotNull
    public static final StringBuilder append(@NotNull final StringBuilder sb, @NotNull final String... array) {
        Intrinsics.checkParameterIsNotNull(sb, "$receiver");
        Intrinsics.checkParameterIsNotNull(array, "value");
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(array[i]);
        }
        return sb;
    }
    
    public static final <T> void appendElement(@NotNull final Appendable appendable, final T obj, @Nullable final Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkParameterIsNotNull(appendable, "$receiver");
        if (function1 != null) {
            appendable.append((CharSequence)function1.invoke(obj));
        }
        else if (obj == null || obj instanceof CharSequence) {
            appendable.append((CharSequence)obj);
        }
        else if (obj instanceof Character) {
            appendable.append((char)obj);
        }
        else {
            appendable.append(String.valueOf(obj));
        }
    }
    
    @SinceKotlin(version = "1.1")
    @InlineOnly
    private static final String buildString(final int capacity, final Function1<? super StringBuilder, Unit> function1) {
        final StringBuilder sb = new StringBuilder(capacity);
        function1.invoke(sb);
        final String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "StringBuilder(capacity).\u2026builderAction).toString()");
        return string;
    }
    
    @InlineOnly
    private static final String buildString(final Function1<? super StringBuilder, Unit> function1) {
        final StringBuilder sb = new StringBuilder();
        function1.invoke(sb);
        final String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }
}
