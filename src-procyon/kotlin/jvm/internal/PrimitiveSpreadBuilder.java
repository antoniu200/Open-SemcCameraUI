// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\t\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0004H\u0004J\u001d\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\u0004*\u00028\u0000H$¢\u0006\u0002\u0010\u0018R\u001a\u0010\u0006\u001a\u00020\u0004X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\u0005R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000b8\u0002X\u0083\u0004¢\u0006\n\n\u0002\u0010\u000e\u0012\u0004\b\f\u0010\r¨\u0006\u0019" }, d2 = { "Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "T", "", "size", "", "(I)V", "position", "getPosition", "()I", "setPosition", "spreads", "", "spreads$annotations", "()V", "[Ljava/lang/Object;", "addSpread", "", "spreadArgument", "(Ljava/lang/Object;)V", "toArray", "values", "result", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getSize", "(Ljava/lang/Object;)I", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public abstract class PrimitiveSpreadBuilder<T>
{
    private int position;
    private final int size;
    private final T[] spreads;
    
    public PrimitiveSpreadBuilder(final int size) {
        this.size = size;
        this.spreads = (T[])new Object[this.size];
    }
    
    public final void addSpread(@NotNull final T t) {
        Intrinsics.checkParameterIsNotNull(t, "spreadArgument");
        this.spreads[this.position++] = t;
    }
    
    protected final int getPosition() {
        return this.position;
    }
    
    protected abstract int getSize(@NotNull final T p0);
    
    protected final void setPosition(final int position) {
        this.position = position;
    }
    
    protected final int size() {
        final int n = this.size - 1;
        int n2 = 0;
        final int n3 = 0;
        if (n >= 0) {
            int n4 = 0;
            int n5 = n3;
            while (true) {
                final T t = this.spreads[n4];
                int size;
                if (t != null) {
                    size = this.getSize(t);
                }
                else {
                    size = 1;
                }
                n2 = n5 + size;
                if (n4 == n) {
                    break;
                }
                ++n4;
                n5 = n2;
            }
        }
        return n2;
    }
    
    @NotNull
    protected final T toArray(@NotNull final T t, @NotNull final T t2) {
        Intrinsics.checkParameterIsNotNull(t, "values");
        Intrinsics.checkParameterIsNotNull(t2, "result");
        final int n = this.size - 1;
        int n2 = 0;
        int n7;
        if (n >= 0) {
            int n3 = 0;
            int n5;
            int n4 = n5 = 0;
            int n6;
            while (true) {
                final T t3 = this.spreads[n3];
                n6 = n4;
                n7 = n5;
                if (t3 != null) {
                    int n8 = n5;
                    if (n4 < n3) {
                        final int n9 = n3 - n4;
                        System.arraycopy(t, n4, t2, n5, n9);
                        n8 = n5 + n9;
                    }
                    final int size = this.getSize(t3);
                    System.arraycopy(t3, 0, t2, n8, size);
                    n7 = n8 + size;
                    n6 = n3 + 1;
                }
                if (n3 == n) {
                    break;
                }
                ++n3;
                n4 = n6;
                n5 = n7;
            }
            n2 = n6;
        }
        else {
            n7 = 0;
        }
        if (n2 < this.size) {
            System.arraycopy(t, n2, t2, n7, this.size - n2);
        }
        return t2;
    }
}
