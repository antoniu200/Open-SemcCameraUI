// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u0001*\u0006\b\u0001\u0010\u0002 \u00012\u00060\u0003j\u0002`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\u0006\u0010\u0006\u001a\u00028\u0001¢\u0006\u0002\u0010\u0007J\u000e\u0010\f\u001a\u00028\u0000H\u00c6\u0003¢\u0006\u0002\u0010\tJ\u000e\u0010\r\u001a\u00028\u0001H\u00c6\u0003¢\u0006\u0002\u0010\tJ.\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\b\b\u0002\u0010\u0005\u001a\u00028\u00002\b\b\u0002\u0010\u0006\u001a\u00028\u0001H\u00c6\u0001¢\u0006\u0002\u0010\u000fJ\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u0013\u0010\u0005\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0006\u001a\u00028\u0001¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\u000b\u0010\t¨\u0006\u0018" }, d2 = { "Lkotlin/Pair;", "A", "B", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "first", "second", "(Ljava/lang/Object;Ljava/lang/Object;)V", "getFirst", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getSecond", "component1", "component2", "copy", "(Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;", "equals", "", "other", "", "hashCode", "", "toString", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class Pair<A, B> implements Serializable
{
    private final A first;
    private final B second;
    
    public Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }
    
    public final A component1() {
        return this.first;
    }
    
    public final B component2() {
        return this.second;
    }
    
    @NotNull
    public final Pair<A, B> copy(final A a, final B b) {
        return new Pair<A, B>(a, b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof Pair) {
                final Pair pair = (Pair)o;
                if (Intrinsics.areEqual(this.first, pair.first) && Intrinsics.areEqual(this.second, pair.second)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public final A getFirst() {
        return this.first;
    }
    
    public final B getSecond() {
        return this.second;
    }
    
    @Override
    public int hashCode() {
        final A first = this.first;
        int hashCode = 0;
        int hashCode2;
        if (first != null) {
            hashCode2 = first.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        final B second = this.second;
        if (second != null) {
            hashCode = second.hashCode();
        }
        return hashCode2 * 31 + hashCode;
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(this.first);
        sb.append(", ");
        sb.append(this.second);
        sb.append(')');
        return sb.toString();
    }
}
