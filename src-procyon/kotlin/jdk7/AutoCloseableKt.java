// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jdk7;

import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.functions.Function1;
import kotlin.SinceKotlin;
import kotlin.PublishedApi;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0001\u001a8\u0010\u0005\u001a\u0002H\u0006\"\n\b\u0000\u0010\u0007*\u0004\u0018\u00010\u0002\"\u0004\b\u0001\u0010\u0006*\u0002H\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\u00060\tH\u0087\b¢\u0006\u0002\u0010\n¨\u0006\u000b" }, d2 = { "closeFinally", "", "Ljava/lang/AutoCloseable;", "cause", "", "use", "R", "T", "block", "Lkotlin/Function1;", "(Ljava/lang/AutoCloseable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib-jdk7" }, k = 2, mv = { 1, 1, 10 }, pn = "kotlin")
@JvmName(name = "AutoCloseableKt")
public final class AutoCloseableKt
{
    @PublishedApi
    @SinceKotlin(version = "1.2")
    public static final void closeFinally(@Nullable final AutoCloseable autoCloseable, @Nullable final Throwable t) {
        if (autoCloseable != null) {
            if (t == null) {
                autoCloseable.close();
            }
            else {
                try {
                    autoCloseable.close();
                }
                catch (final Throwable exception) {
                    t.addSuppressed(exception);
                }
            }
        }
    }
    
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <T extends AutoCloseable, R> R use(final T t, final Function1<? super T, ? extends R> function1) {
        final Throwable t2 = null;
        try {
            try {
                final R invoke = (R)function1.invoke(t);
                InlineMarker.finallyStart(1);
                closeFinally(t, t2);
                InlineMarker.finallyEnd(1);
                return invoke;
            }
            finally {}
        }
        catch (final Throwable t3) {
            throw t3;
        }
        InlineMarker.finallyStart(1);
        closeFinally(t, t2);
        InlineMarker.finallyEnd(1);
    }
}
