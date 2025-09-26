// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u0001*\u0006\b\u0001\u0010\u0002 \u0001*\u0006\b\u0002\u0010\u0003 \u00012\u00060\u0004j\u0002`\u0005B\u001d\u0012\u0006\u0010\u0006\u001a\u00028\u0000\u0012\u0006\u0010\u0007\u001a\u00028\u0001\u0012\u0006\u0010\b\u001a\u00028\u0002¢\u0006\u0002\u0010\tJ\u000e\u0010\u000f\u001a\u00028\u0000H\u00c6\u0003¢\u0006\u0002\u0010\u000bJ\u000e\u0010\u0010\u001a\u00028\u0001H\u00c6\u0003¢\u0006\u0002\u0010\u000bJ\u000e\u0010\u0011\u001a\u00028\u0002H\u00c6\u0003¢\u0006\u0002\u0010\u000bJ>\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00020\u00002\b\b\u0002\u0010\u0006\u001a\u00028\u00002\b\b\u0002\u0010\u0007\u001a\u00028\u00012\b\b\u0002\u0010\b\u001a\u00028\u0002H\u00c6\u0001¢\u0006\u0002\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0013\u0010\u0006\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0007\u001a\u00028\u0001¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\r\u0010\u000bR\u0013\u0010\b\u001a\u00028\u0002¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\u000e\u0010\u000b¨\u0006\u001c" }, d2 = { "Lkotlin/Triple;", "A", "B", "C", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "first", "second", "third", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", "getFirst", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getSecond", "getThird", "component1", "component2", "component3", "copy", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Triple;", "equals", "", "other", "", "hashCode", "", "toString", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class Triple<A, B, C> implements Serializable
{
    private final A first;
    private final B second;
    private final C third;
    
    public Triple(final A first, final B second, final C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    public final A component1() {
        return this.first;
    }
    
    public final B component2() {
        return this.second;
    }
    
    public final C component3() {
        return this.third;
    }
    
    @NotNull
    public final Triple<A, B, C> copy(final A a, final B b, final C c) {
        return new Triple<A, B, C>(a, b, c);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof Triple) {
                final Triple triple = (Triple)o;
                if (Intrinsics.areEqual(this.first, triple.first) && Intrinsics.areEqual(this.second, triple.second) && Intrinsics.areEqual(this.third, triple.third)) {
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
    
    public final C getThird() {
        return this.third;
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
        int hashCode3;
        if (second != null) {
            hashCode3 = second.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        final C third = this.third;
        if (third != null) {
            hashCode = third.hashCode();
        }
        return (hashCode2 * 31 + hashCode3) * 31 + hashCode;
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(this.first);
        sb.append(", ");
        sb.append(this.second);
        sb.append(", ");
        sb.append(this.third);
        sb.append(')');
        return sb.toString();
    }
}
