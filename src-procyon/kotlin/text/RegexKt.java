// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import java.util.Iterator;
import java.util.Collections;
import kotlin.jvm.functions.Function1;
import java.util.EnumSet;
import kotlin.jvm.internal.Intrinsics;
import java.util.Set;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Matcher;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002¨\u0006\u0014" }, d2 = { "fromInt", "", "T", "Lkotlin/text/FlagEnum;", "", "value", "", "findNext", "Lkotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", "input", "", "matchEntire", "range", "Lkotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
public final class RegexKt
{
    private static final MatchResult findNext(@NotNull final Matcher matcher, final int start, final CharSequence charSequence) {
        MatchResult matchResult;
        if (!matcher.find(start)) {
            matchResult = null;
        }
        else {
            matchResult = new MatcherMatchResult(matcher, charSequence);
        }
        return matchResult;
    }
    
    private static final <T extends Enum<T> & FlagEnum> Set<T> fromInt(final int n) {
        Intrinsics.reifiedOperationMarker(4, "T");
        final EnumSet<Enum> all = EnumSet.allOf(Enum.class);
        CollectionsKt__MutableCollectionsKt.retainAll((Iterable<?>)all, (Function1<? super Object, Boolean>)new RegexKt$fromInt$$inlined$apply$lambda.RegexKt$fromInt$$inlined$apply$lambda$1(n));
        final Set<Object> unmodifiableSet = Collections.unmodifiableSet((Set<?>)all);
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableSet, "Collections.unmodifiable\u2026mask == it.value }\n    })");
        return (Set<T>)unmodifiableSet;
    }
    
    private static final MatchResult matchEntire(@NotNull final Matcher matcher, final CharSequence charSequence) {
        MatchResult matchResult;
        if (!matcher.matches()) {
            matchResult = null;
        }
        else {
            matchResult = new MatcherMatchResult(matcher, charSequence);
        }
        return matchResult;
    }
    
    private static final IntRange range(@NotNull final java.util.regex.MatchResult matchResult) {
        return RangesKt___RangesKt.until(matchResult.start(), matchResult.end());
    }
    
    private static final IntRange range(@NotNull final java.util.regex.MatchResult matchResult, final int n) {
        return RangesKt___RangesKt.until(matchResult.start(n), matchResult.end(n));
    }
    
    private static final int toInt(@NotNull final Iterable<? extends FlagEnum> iterable) {
        final Iterator<? extends FlagEnum> iterator = iterable.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            n |= ((FlagEnum)iterator.next()).getValue();
        }
        return n;
    }
}
