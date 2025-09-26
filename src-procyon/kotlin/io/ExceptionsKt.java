// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.io;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001H\u0002¨\u0006\u0006" }, d2 = { "constructMessage", "", "file", "Ljava/io/File;", "other", "reason", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
public final class ExceptionsKt
{
    private static final String constructMessage(final File file, final File obj, final String str) {
        final StringBuilder sb = new StringBuilder(file.toString());
        if (obj != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" -> ");
            sb2.append(obj);
            sb.append(sb2.toString());
        }
        if (str != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(": ");
            sb3.append(str);
            sb.append(sb3.toString());
        }
        final String string = sb.toString();
        Intrinsics.checkExpressionValueIsNotNull(string, "sb.toString()");
        return string;
    }
}
