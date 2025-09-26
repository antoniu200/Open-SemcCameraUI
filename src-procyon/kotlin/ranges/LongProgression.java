// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.ranges;

import org.jetbrains.annotations.NotNull;
import kotlin.collections.LongIterator;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import kotlin.internal.ProgressionUtilKt;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00182\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0018B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0002¢\u0006\u0002\u0010\u0006J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u000eH\u0016J\t\u0010\u0014\u001a\u00020\u0015H\u0096\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u0011\u0010\u0007\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0019" }, d2 = { "Lkotlin/ranges/LongProgression;", "", "", "start", "endInclusive", "step", "(JJJ)V", "first", "getFirst", "()J", "last", "getLast", "getStep", "equals", "", "other", "", "hashCode", "", "isEmpty", "iterator", "Lkotlin/collections/LongIterator;", "toString", "", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public class LongProgression implements Iterable<Long>, KMappedMarker
{
    public static final Companion Companion;
    private final long first;
    private final long last;
    private final long step;
    
    static {
        Companion = new Companion(null);
    }
    
    public LongProgression(final long first, final long n, final long step) {
        if (step == 0L) {
            throw new IllegalArgumentException("Step must be non-zero");
        }
        this.first = first;
        this.last = ProgressionUtilKt.getProgressionLastElement(first, n, step);
        this.step = step;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof LongProgression) {
            if (!this.isEmpty() || !((LongProgression)o).isEmpty()) {
                final long first = this.first;
                final LongProgression longProgression = (LongProgression)o;
                if (first != longProgression.first || this.last != longProgression.last || this.step != longProgression.step) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public final long getFirst() {
        return this.first;
    }
    
    public final long getLast() {
        return this.last;
    }
    
    public final long getStep() {
        return this.step;
    }
    
    @Override
    public int hashCode() {
        int n;
        if (this.isEmpty()) {
            n = -1;
        }
        else {
            final long n2 = 31;
            n = (int)(n2 * ((this.first ^ this.first >>> 32) * n2 + (this.last ^ this.last >>> 32)) + (this.step ^ this.step >>> 32));
        }
        return n;
    }
    
    public boolean isEmpty() {
        final long step = this.step;
        boolean b = false;
        if (step > 0L) {
            if (this.first <= this.last) {
                return b;
            }
        }
        else if (this.first >= this.last) {
            return b;
        }
        b = true;
        return b;
    }
    
    @NotNull
    @Override
    public LongIterator iterator() {
        return new LongProgressionIterator(this.first, this.last, this.step);
    }
    
    @NotNull
    @Override
    public String toString() {
        StringBuilder sb;
        long step;
        if (this.step > 0L) {
            sb = new StringBuilder();
            sb.append(this.first);
            sb.append("..");
            sb.append(this.last);
            sb.append(" step ");
            step = this.step;
        }
        else {
            sb = new StringBuilder();
            sb.append(this.first);
            sb.append(" downTo ");
            sb.append(this.last);
            sb.append(" step ");
            step = -this.step;
        }
        sb.append(step);
        return sb.toString();
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006¨\u0006\t" }, d2 = { "Lkotlin/ranges/LongProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/LongProgression;", "rangeStart", "", "rangeEnd", "step", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final LongProgression fromClosedRange(final long n, final long n2, final long n3) {
            return new LongProgression(n, n2, n3);
        }
    }
}
