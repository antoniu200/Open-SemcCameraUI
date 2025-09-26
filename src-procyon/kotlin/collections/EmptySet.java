// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.jvm.internal.CollectionToArray;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.io.Serializable;
import java.util.Set;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0002\u0010\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u00c0\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004B\u0007\b\u0002¢\u0006\u0002\u0010\u0005J\u0011\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0002H\u0096\u0002J\u0016\u0010\u000f\u001a\u00020\r2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00020\u0011H\u0016J\u0013\u0010\u0012\u001a\u00020\r2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0096\u0002J\b\u0010\u0015\u001a\u00020\tH\u0016J\b\u0010\u0016\u001a\u00020\rH\u0016J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0018H\u0096\u0002J\b\u0010\u0019\u001a\u00020\u0014H\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u001c" }, d2 = { "Lkotlin/collections/EmptySet;", "", "", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "serialVersionUID", "", "size", "", "getSize", "()I", "contains", "", "element", "containsAll", "elements", "", "equals", "other", "", "hashCode", "isEmpty", "iterator", "", "readResolve", "toString", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class EmptySet implements Set, Serializable, KMappedMarker
{
    public static final EmptySet INSTANCE;
    private static final long serialVersionUID = 3406603774387020532L;
    
    static {
        INSTANCE = new EmptySet();
    }
    
    private EmptySet() {
    }
    
    private final Object readResolve() {
        return EmptySet.INSTANCE;
    }
    
    public boolean add(final Void void1) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean addAll(final Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final /* bridge */ boolean contains(final Object o) {
        return o instanceof Void && this.contains((Void)o);
    }
    
    public boolean contains(@NotNull final Void void1) {
        Intrinsics.checkParameterIsNotNull(void1, "element");
        return false;
    }
    
    @Override
    public boolean containsAll(@NotNull final Collection collection) {
        Intrinsics.checkParameterIsNotNull(collection, "elements");
        return collection.isEmpty();
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof Set && ((Set)o).isEmpty();
    }
    
    public int getSize() {
        return 0;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @NotNull
    @Override
    public Iterator iterator() {
        return EmptyIterator.INSTANCE;
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean removeAll(final Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public boolean retainAll(final Collection collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    @Override
    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return (T[])CollectionToArray.toArray(this, array);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "[]";
    }
}
