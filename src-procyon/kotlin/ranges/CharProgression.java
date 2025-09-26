// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.ranges;

import org.jetbrains.annotations.NotNull;
import kotlin.collections.CharIterator;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import kotlin.internal.ProgressionUtilKt;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00192\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0019B\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0006H\u0016J\b\u0010\u0014\u001a\u00020\u0010H\u0016J\t\u0010\u0015\u001a\u00020\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0011\u0010\b\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u0002¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a" }, d2 = { "Lkotlin/ranges/CharProgression;", "", "", "start", "endInclusive", "step", "", "(CCI)V", "first", "getFirst", "()C", "last", "getLast", "getStep", "()I", "equals", "", "other", "", "hashCode", "isEmpty", "iterator", "Lkotlin/collections/CharIterator;", "toString", "", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public class CharProgression implements Iterable<Character>, KMappedMarker
{
    public static final Companion Companion;
    private final char first;
    private final char last;
    private final int step;
    
    static {
        Companion = new Companion(null);
    }
    
    public CharProgression(final char first, final char c, final int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step must be non-zero");
        }
        this.first = first;
        this.last = (char)ProgressionUtilKt.getProgressionLastElement(first, c, step);
        this.step = step;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof CharProgression) {
            if (!this.isEmpty() || !((CharProgression)o).isEmpty()) {
                final char first = this.first;
                final CharProgression charProgression = (CharProgression)o;
                if (first != charProgression.first || this.last != charProgression.last || this.step != charProgression.step) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public final char getFirst() {
        return this.first;
    }
    
    public final char getLast() {
        return this.last;
    }
    
    public final int getStep() {
        return this.step;
    }
    
    @Override
    public int hashCode() {
        int n;
        if (this.isEmpty()) {
            n = -1;
        }
        else {
            n = this.step + 31 * (this.first * '\u001f' + this.last);
        }
        return n;
    }
    
    public boolean isEmpty() {
        final int step = this.step;
        boolean b = false;
        if (step > 0) {
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
    public CharIterator iterator() {
        return new CharProgressionIterator(this.first, this.last, this.step);
    }
    
    @NotNull
    @Override
    public String toString() {
        StringBuilder sb;
        int step;
        if (this.step > 0) {
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
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¨\u0006\n" }, d2 = { "Lkotlin/ranges/CharProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/CharProgression;", "rangeStart", "", "rangeEnd", "step", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final CharProgression fromClosedRange(final char c, final char c2, final int n) {
            return new CharProgression(c, c2, n);
        }
    }
}
