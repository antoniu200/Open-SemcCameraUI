// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import kotlin.collections.IntIterator;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\b\u0010\t\u001a\u00020\u0006H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\n" }, d2 = { "Lkotlin/jvm/internal/ArrayIntIterator;", "Lkotlin/collections/IntIterator;", "array", "", "([I)V", "index", "", "hasNext", "", "nextInt", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class ArrayIntIterator extends IntIterator
{
    private final int[] array;
    private int index;
    
    public ArrayIntIterator(@NotNull final int[] array) {
        Intrinsics.checkParameterIsNotNull(array, "array");
        this.array = array;
    }
    
    @Override
    public boolean hasNext() {
        return this.index < this.array.length;
    }
    
    @Override
    public int nextInt() {
        try {
            return this.array[this.index++];
        }
        catch (final ArrayIndexOutOfBoundsException ex) {
            --this.index;
            throw new NoSuchElementException(ex.getMessage());
        }
    }
}
