// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.ranges.IntRange;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014" }, d2 = { "Lkotlin/text/MatchGroup;", "", "value", "", "range", "Lkotlin/ranges/IntRange;", "(Ljava/lang/String;Lkotlin/ranges/IntRange;)V", "getRange", "()Lkotlin/ranges/IntRange;", "getValue", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class MatchGroup
{
    @NotNull
    private final IntRange range;
    @NotNull
    private final String value;
    
    public MatchGroup(@NotNull final String value, @NotNull final IntRange range) {
        Intrinsics.checkParameterIsNotNull(value, "value");
        Intrinsics.checkParameterIsNotNull(range, "range");
        this.value = value;
        this.range = range;
    }
    
    @NotNull
    public final String component1() {
        return this.value;
    }
    
    @NotNull
    public final IntRange component2() {
        return this.range;
    }
    
    @NotNull
    public final MatchGroup copy(@NotNull final String s, @NotNull final IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(s, "value");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        return new MatchGroup(s, intRange);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof MatchGroup) {
                final MatchGroup matchGroup = (MatchGroup)o;
                if (Intrinsics.areEqual(this.value, matchGroup.value) && Intrinsics.areEqual(this.range, matchGroup.range)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @NotNull
    public final IntRange getRange() {
        return this.range;
    }
    
    @NotNull
    public final String getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        final String value = this.value;
        int hashCode = 0;
        int hashCode2;
        if (value != null) {
            hashCode2 = value.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final IntRange range = this.range;
        if (range != null) {
            hashCode = range.hashCode();
        }
        return hashCode2 * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MatchGroup(value=");
        sb.append(this.value);
        sb.append(", range=");
        sb.append(this.range);
        sb.append(")");
        return sb.toString();
    }
}
