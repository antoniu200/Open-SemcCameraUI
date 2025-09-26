// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.internal;

import kotlin.PublishedApi;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u001a \u0010\u0000\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a \u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001H\u0001\u001a \u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005H\u0001\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0002\u001a\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0002¨\u0006\u000b" }, d2 = { "differenceModulo", "", "a", "b", "c", "", "getProgressionLastElement", "start", "end", "step", "mod", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
public final class ProgressionUtilKt
{
    private static final int differenceModulo(final int n, final int n2, final int n3) {
        return mod(mod(n, n3) - mod(n2, n3), n3);
    }
    
    private static final long differenceModulo(final long n, final long n2, final long n3) {
        return mod(mod(n, n3) - mod(n2, n3), n3);
    }
    
    @PublishedApi
    public static final int getProgressionLastElement(final int n, int n2, final int n3) {
        if (n3 > 0) {
            if (n < n2) {
                n2 -= differenceModulo(n2, n, n3);
            }
        }
        else {
            if (n3 >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            if (n > n2) {
                n2 += differenceModulo(n, n2, -n3);
            }
        }
        return n2;
    }
    
    @PublishedApi
    public static final long getProgressionLastElement(final long n, long n2, final long n3) {
        final long n4 = lcmp(n3, 0L);
        if (n4 > 0) {
            if (n < n2) {
                n2 -= differenceModulo(n2, n, n3);
            }
        }
        else {
            if (n4 >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            if (n > n2) {
                n2 += differenceModulo(n, n2, -n3);
            }
        }
        return n2;
    }
    
    private static final int mod(int n, final int n2) {
        n %= n2;
        if (n < 0) {
            n += n2;
        }
        return n;
    }
    
    private static final long mod(long n, final long n2) {
        n %= n2;
        if (n < 0L) {
            n += n2;
        }
        return n;
    }
}
