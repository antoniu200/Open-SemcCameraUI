// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.jvm.functions.Function1;
import kotlin.TypeCastException;
import kotlin.jvm.internal.CollectionToArray;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0007\b\u0004¢\u0006\u0002\u0010\u0003J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\t2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002H\u0016J\b\u0010\u000e\u001a\u00020\tH\u0016J\u000f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H¦\u0002J\u0015\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0012H\u0014¢\u0006\u0002\u0010\u0014J'\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0012\"\u0004\b\u0001\u0010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00150\u0012H\u0014¢\u0006\u0002\u0010\u0017J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u0012\u0010\u0004\u001a\u00020\u0005X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u001a" }, d2 = { "Lkotlin/collections/AbstractCollection;", "E", "", "()V", "size", "", "getSize", "()I", "contains", "", "element", "(Ljava/lang/Object;)Z", "containsAll", "elements", "isEmpty", "iterator", "", "toArray", "", "", "()[Ljava/lang/Object;", "T", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@SinceKotlin(version = "1.1")
public abstract class AbstractCollection<E> implements Collection<E>, KMappedMarker
{
    protected AbstractCollection() {
    }
    
    @Override
    public boolean add(final E e) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean contains(final Object o) {
        final boolean b = this instanceof Collection;
        final boolean b2 = false;
        boolean b3;
        if (b && this.isEmpty()) {
            b3 = b2;
        }
        else {
            final Iterator<E> iterator = this.iterator();
            do {
                b3 = b2;
                if (iterator.hasNext()) {
                    continue;
                }
                return b3;
            } while (!Intrinsics.areEqual(iterator.next(), o));
            b3 = true;
        }
        return b3;
    }
    
    @Override
    public boolean containsAll(@NotNull final Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        final Iterable iterable = collection;
        final boolean empty = ((Collection)iterable).isEmpty();
        final boolean b = true;
        boolean b2;
        if (empty) {
            b2 = b;
        }
        else {
            final Iterator iterator = iterable.iterator();
            do {
                b2 = b;
                if (iterator.hasNext()) {
                    continue;
                }
                return b2;
            } while (this.contains(iterator.next()));
            b2 = false;
        }
        return b2;
    }
    
    public abstract int getSize();
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @NotNull
    @Override
    public abstract Iterator<E> iterator();
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    @NotNull
    @Override
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }
    
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        final Object[] array2 = CollectionToArray.toArray(this, array);
        if (array2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (T[])array2;
    }
    
    @NotNull
    @Override
    public String toString() {
        return CollectionsKt___CollectionsKt.joinToString$default((Iterable)this, (CharSequence)", ", (CharSequence)"[", (CharSequence)"]", 0, (CharSequence)null, (Function1)new AbstractCollection$toString.AbstractCollection$toString$1(this), 24, (Object)null);
    }
}
