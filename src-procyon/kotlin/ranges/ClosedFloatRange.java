// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.ranges;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002¢\u0006\u0002\u0010\u0005J\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0002H\u0096\u0002J\u0013\u0010\u000e\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\fH\u0016J\u0018\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u0002H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0006\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0003\u001a\u00020\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\t¨\u0006\u0019" }, d2 = { "Lkotlin/ranges/ClosedFloatRange;", "Lkotlin/ranges/ClosedFloatingPointRange;", "", "start", "endInclusive", "(FF)V", "_endInclusive", "_start", "getEndInclusive", "()Ljava/lang/Float;", "getStart", "contains", "", "value", "equals", "other", "", "hashCode", "", "isEmpty", "lessThanOrEquals", "a", "b", "toString", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class ClosedFloatRange implements ClosedFloatingPointRange<Float>
{
    private final float _endInclusive;
    private final float _start;
    
    public ClosedFloatRange(final float start, final float endInclusive) {
        this._start = start;
        this._endInclusive = endInclusive;
    }
    
    public boolean contains(final float n) {
        return n >= this._start && n <= this._endInclusive;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof ClosedFloatRange) {
            if (!this.isEmpty() || !((ClosedFloatRange)o).isEmpty()) {
                final float start = this._start;
                final ClosedFloatRange closedFloatRange = (ClosedFloatRange)o;
                if (start != closedFloatRange._start || this._endInclusive != closedFloatRange._endInclusive) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @NotNull
    @Override
    public Float getEndInclusive() {
        return this._endInclusive;
    }
    
    @NotNull
    @Override
    public Float getStart() {
        return this._start;
    }
    
    @Override
    public int hashCode() {
        int n;
        if (this.isEmpty()) {
            n = -1;
        }
        else {
            n = Float.valueOf(this._endInclusive).hashCode() + 31 * Float.valueOf(this._start).hashCode();
        }
        return n;
    }
    
    @Override
    public boolean isEmpty() {
        return this._start > this._endInclusive;
    }
    
    public boolean lessThanOrEquals(final float n, final float n2) {
        return n <= n2;
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this._start);
        sb.append("..");
        sb.append(this._endInclusive);
        return sb.toString();
    }
}
