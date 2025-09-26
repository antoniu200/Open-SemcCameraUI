// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.ranges;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u00142\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002:\u0001\u0014B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006J\u0011\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003H\u0096\u0002J\u0013\u0010\r\u001a\u00020\u000b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\b\u0010\u0010\u001a\u00020\u0003H\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016R\u0014\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0004\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\b¨\u0006\u0015" }, d2 = { "Lkotlin/ranges/IntRange;", "Lkotlin/ranges/IntProgression;", "Lkotlin/ranges/ClosedRange;", "", "start", "endInclusive", "(II)V", "getEndInclusive", "()Ljava/lang/Integer;", "getStart", "contains", "", "value", "equals", "other", "", "hashCode", "isEmpty", "toString", "", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class IntRange extends IntProgression implements ClosedRange<Integer>
{
    public static final Companion Companion;
    @NotNull
    private static final IntRange EMPTY;
    
    static {
        Companion = new Companion(null);
        EMPTY = new IntRange(1, 0);
    }
    
    public IntRange(final int n, final int n2) {
        super(n, n2, 1);
    }
    
    @NotNull
    public static final /* synthetic */ IntRange access$getEMPTY$cp() {
        return IntRange.EMPTY;
    }
    
    public boolean contains(final int n) {
        return this.getFirst() <= n && n <= this.getLast();
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof IntRange) {
            if (!this.isEmpty() || !((IntRange)o).isEmpty()) {
                final int first = this.getFirst();
                final IntRange intRange = (IntRange)o;
                if (first != intRange.getFirst() || this.getLast() != intRange.getLast()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @NotNull
    @Override
    public Integer getEndInclusive() {
        return this.getLast();
    }
    
    @NotNull
    @Override
    public Integer getStart() {
        return this.getFirst();
    }
    
    @Override
    public int hashCode() {
        int n;
        if (this.isEmpty()) {
            n = -1;
        }
        else {
            n = this.getLast() + 31 * this.getFirst();
        }
        return n;
    }
    
    @Override
    public boolean isEmpty() {
        return this.getFirst() > this.getLast();
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getFirst());
        sb.append("..");
        sb.append(this.getLast());
        return sb.toString();
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Lkotlin/ranges/IntRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/IntRange;", "getEMPTY", "()Lkotlin/ranges/IntRange;", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final IntRange getEMPTY() {
            return IntRange.access$getEMPTY$cp();
        }
    }
}
