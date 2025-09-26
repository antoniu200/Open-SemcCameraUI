// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.internal;

import org.jetbrains.annotations.Nullable;
import kotlin.text.MatchGroup;
import java.util.regex.MatchResult;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0010\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\u001a\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016¨\u0006\u000e" }, d2 = { "Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public class PlatformImplementations
{
    public void addSuppressed(@NotNull final Throwable t, @NotNull final Throwable t2) {
        Intrinsics.checkParameterIsNotNull(t, "cause");
        Intrinsics.checkParameterIsNotNull(t2, "exception");
    }
    
    @Nullable
    public MatchGroup getMatchResultNamedGroup(@NotNull final MatchResult matchResult, @NotNull final String s) {
        Intrinsics.checkParameterIsNotNull(matchResult, "matchResult");
        Intrinsics.checkParameterIsNotNull(s, "name");
        throw new UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }
}
