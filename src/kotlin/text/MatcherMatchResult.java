package kotlin.text;

import java.util.List;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.MatchResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Regex.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\n\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\f\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000eX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n \u0013*\u0004\u0018\u00010\u00120\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0018\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a¨\u0006\u001c"}, d2 = {"Lkotlin/text/MatcherMatchResult;", "Lkotlin/text/MatchResult;", "matcher", "Ljava/util/regex/Matcher;", "input", "", "(Ljava/util/regex/Matcher;Ljava/lang/CharSequence;)V", "groupValues", "", "", "getGroupValues", "()Ljava/util/List;", "groupValues_", "groups", "Lkotlin/text/MatchGroupCollection;", "getGroups", "()Lkotlin/text/MatchGroupCollection;", "matchResult", "Ljava/util/regex/MatchResult;", "kotlin.jvm.PlatformType", "range", "Lkotlin/ranges/IntRange;", "getRange", "()Lkotlin/ranges/IntRange;", "value", "getValue", "()Ljava/lang/String;", "next", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class MatcherMatchResult implements MatchResult {
    private List<String> groupValues_;

    @NotNull
    private final MatchGroupCollection groups;
    private final CharSequence input;
    private final java.util.regex.MatchResult matchResult;
    private final Matcher matcher;

    public MatcherMatchResult(@NotNull Matcher matcher, @NotNull CharSequence input) {
        Intrinsics.checkParameterIsNotNull(matcher, "matcher");
        Intrinsics.checkParameterIsNotNull(input, "input");
        this.matcher = matcher;
        this.input = input;
        this.matchResult = this.matcher.toMatchResult();
        this.groups = new MatcherMatchResult$groups$1(this);
    }

    @Override // kotlin.text.MatchResult
    @NotNull
    public MatchResult.Destructured getDestructured() {
        return MatchResult.DefaultImpls.getDestructured(this);
    }

    @Override // kotlin.text.MatchResult
    @NotNull
    public IntRange getRange() {
        java.util.regex.MatchResult matchResult = this.matchResult;
        Intrinsics.checkExpressionValueIsNotNull(matchResult, "matchResult");
        return RegexKt.range(matchResult);
    }

    @Override // kotlin.text.MatchResult
    @NotNull
    public String getValue() {
        String strGroup = this.matchResult.group();
        Intrinsics.checkExpressionValueIsNotNull(strGroup, "matchResult.group()");
        return strGroup;
    }

    @Override // kotlin.text.MatchResult
    @NotNull
    public MatchGroupCollection getGroups() {
        return this.groups;
    }

    @Override // kotlin.text.MatchResult
    @NotNull
    public List<String> getGroupValues() {
        if (this.groupValues_ == null) {
            this.groupValues_ = new AbstractList<String>() { // from class: kotlin.text.MatcherMatchResult$groupValues$1
                @Override // kotlin.collections.AbstractCollection, java.util.Collection
                public final /* bridge */ boolean contains(Object obj) {
                    if (obj instanceof String) {
                        return contains((String) obj);
                    }
                    return false;
                }

                public /* bridge */ boolean contains(String str) {
                    return super.contains((Object) str);
                }

                @Override // kotlin.collections.AbstractList, java.util.List
                public final /* bridge */ int indexOf(Object obj) {
                    if (obj instanceof String) {
                        return indexOf((String) obj);
                    }
                    return -1;
                }

                public /* bridge */ int indexOf(String str) {
                    return super.indexOf((Object) str);
                }

                @Override // kotlin.collections.AbstractList, java.util.List
                public final /* bridge */ int lastIndexOf(Object obj) {
                    if (obj instanceof String) {
                        return lastIndexOf((String) obj);
                    }
                    return -1;
                }

                public /* bridge */ int lastIndexOf(String str) {
                    return super.lastIndexOf((Object) str);
                }

                @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
                public int getSize() {
                    return this.this$0.matchResult.groupCount() + 1;
                }

                @Override // kotlin.collections.AbstractList, java.util.List
                @NotNull
                public String get(int index) {
                    String strGroup = this.this$0.matchResult.group(index);
                    return strGroup != null ? strGroup : "";
                }
            };
        }
        List<String> list = this.groupValues_;
        if (list == null) {
            Intrinsics.throwNpe();
        }
        return list;
    }

    @Override // kotlin.text.MatchResult
    @Nullable
    public MatchResult next() {
        int iEnd = this.matchResult.end() + (this.matchResult.end() == this.matchResult.start() ? 1 : 0);
        if (iEnd <= this.input.length()) {
            return RegexKt.findNext(this.matcher, iEnd, this.input);
        }
        return null;
    }
}
