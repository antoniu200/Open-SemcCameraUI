// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.LongIterator;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0002\u0010\u0006J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\b\u0010\r\u001a\u00020\u0003H\u0016R\u000e\u0010\u0007\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u000e" }, d2 = { "Lkotlin/ranges/LongProgressionIterator;", "Lkotlin/collections/LongIterator;", "first", "", "last", "step", "(JJJ)V", "finalElement", "hasNext", "", "next", "getStep", "()J", "nextLong", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class LongProgressionIterator extends LongIterator
{
    private final long finalElement;
    private boolean hasNext;
    private long next;
    private final long step;
    
    public LongProgressionIterator(long finalElement, final long finalElement2, long step) {
        this.step = step;
        this.finalElement = finalElement2;
        step = this.step;
        boolean hasNext = false;
        Label_0052: {
            if (step > 0L) {
                if (finalElement > finalElement2) {
                    break Label_0052;
                }
            }
            else if (finalElement < finalElement2) {
                break Label_0052;
            }
            hasNext = true;
        }
        this.hasNext = hasNext;
        if (!this.hasNext) {
            finalElement = this.finalElement;
        }
        this.next = finalElement;
    }
    
    public final long getStep() {
        return this.step;
    }
    
    @Override
    public boolean hasNext() {
        return this.hasNext;
    }
    
    @Override
    public long nextLong() {
        final long next = this.next;
        if (next == this.finalElement) {
            if (!this.hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
        }
        else {
            this.next += this.step;
        }
        return next;
    }
}
