// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Set;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b'\u0018\u0000 \u000b*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001\u000bB\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016¨\u0006\f" }, d2 = { "Lkotlin/collections/AbstractSet;", "E", "Lkotlin/collections/AbstractCollection;", "", "()V", "equals", "", "other", "", "hashCode", "", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@SinceKotlin(version = "1.1")
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E>, KMappedMarker
{
    public static final Companion Companion;
    
    static {
        Companion = new Companion(null);
    }
    
    protected AbstractSet() {
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o == this || (o instanceof Set && AbstractSet.Companion.setEquals$kotlin_stdlib(this, (Set<?>)o));
    }
    
    @Override
    public int hashCode() {
        return AbstractSet.Companion.unorderedHashCode$kotlin_stdlib(this);
    }
    
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\u0010\u001e\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u00062\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\bJ\u0019\u0010\t\u001a\u00020\n2\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u000bH\u0000¢\u0006\u0002\b\f¨\u0006\r" }, d2 = { "Lkotlin/collections/AbstractSet$Companion;", "", "()V", "setEquals", "", "c", "", "other", "setEquals$kotlin_stdlib", "unorderedHashCode", "", "", "unorderedHashCode$kotlin_stdlib", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
        
        public final boolean setEquals$kotlin_stdlib(@NotNull final Set<?> set, @NotNull final Set<?> set2) {
            Intrinsics.checkParameterIsNotNull(set, "c");
            Intrinsics.checkParameterIsNotNull(set2, "other");
            return set.size() == set2.size() && set.containsAll(set2);
        }
        
        public final int unorderedHashCode$kotlin_stdlib(@NotNull final Collection<?> collection) {
            Intrinsics.checkParameterIsNotNull(collection, "c");
            final Iterator<?> iterator = collection.iterator();
            int n = 0;
            while (iterator.hasNext()) {
                final Object next = iterator.next();
                int hashCode;
                if (next != null) {
                    hashCode = next.hashCode();
                }
                else {
                    hashCode = 0;
                }
                n += hashCode;
            }
            return n;
        }
    }
}
