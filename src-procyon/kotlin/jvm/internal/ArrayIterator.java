// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Iterator;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\u000b\u001a\u00020\fH\u0096\u0002J\u000e\u0010\r\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000eR\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f" }, d2 = { "Lkotlin/jvm/internal/ArrayIterator;", "T", "", "array", "", "([Ljava/lang/Object;)V", "getArray", "()[Ljava/lang/Object;", "[Ljava/lang/Object;", "index", "", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class ArrayIterator<T> implements Iterator<T>, KMappedMarker
{
    @NotNull
    private final T[] array;
    private int index;
    
    public ArrayIterator(@NotNull final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        this.array = array;
    }
    
    @NotNull
    public final T[] getArray() {
        return this.array;
    }
    
    @Override
    public boolean hasNext() {
        return this.index < this.array.length;
    }
    
    @Override
    public T next() {
        try {
            return this.array[this.index++];
        }
        catch (final ArrayIndexOutOfBoundsException ex) {
            --this.index;
            throw new NoSuchElementException(ex.getMessage());
        }
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
}
