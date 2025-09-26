// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.TypeCastException;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import java.util.RandomAccess;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\f\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0006H\u0096\u0002¢\u0006\u0002\u0010\u0019J\u0006\u0010\u001a\u001a\u00020\u001bJ\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u001dH\u0096\u0002J\u000e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u001f\u001a\u00020\u0006J\u0015\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014¢\u0006\u0002\u0010!J'\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014¢\u0006\u0002\u0010#J9\u0010$\u001a\u00020\u0014\"\u0004\b\u0001\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\t2\u0006\u0010\u0015\u001a\u0002H\u00012\b\b\u0002\u0010%\u001a\u00020\u00062\b\b\u0002\u0010&\u001a\u00020\u0006H\u0002¢\u0006\u0002\u0010'J\u0015\u0010(\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR$\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u0007R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006)" }, d2 = { "Lkotlin/collections/RingBuffer;", "T", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "[Ljava/lang/Object;", "getCapacity", "()I", "<set-?>", "size", "getSize", "setSize", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "get", "index", "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "fill", "fromIndex", "toIndex", "([Ljava/lang/Object;Ljava/lang/Object;II)V", "forward", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class RingBuffer<T> extends AbstractList<T> implements RandomAccess
{
    private final Object[] buffer;
    private final int capacity;
    private int size;
    private int startIndex;
    
    public RingBuffer(int capacity) {
        this.capacity = capacity;
        if (this.capacity >= 0) {
            capacity = 1;
        }
        else {
            capacity = 0;
        }
        if (capacity == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ring buffer capacity should not be negative but it is ");
            sb.append(this.capacity);
            throw new IllegalArgumentException(sb.toString().toString());
        }
        this.buffer = new Object[this.capacity];
    }
    
    private final <T> void fill(@NotNull final T[] array, final T t, int i, final int n) {
        while (i < n) {
            array[i] = t;
            ++i;
        }
    }
    
    private final int forward(final int n, final int n2) {
        return (n + n2) % this.getCapacity();
    }
    
    private void setSize(final int size) {
        this.size = size;
    }
    
    public final void add(final T t) {
        if (this.isFull()) {
            throw new IllegalStateException("ring buffer is full");
        }
        this.buffer[(this.startIndex + this.size()) % this.getCapacity()] = t;
        this.setSize(this.size() + 1);
    }
    
    @Override
    public T get(final int n) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(n, this.size());
        return (T)this.buffer[(this.startIndex + n) % this.getCapacity()];
    }
    
    public final int getCapacity() {
        return this.capacity;
    }
    
    @Override
    public int getSize() {
        return this.size;
    }
    
    public final boolean isFull() {
        return this.size() == this.capacity;
    }
    
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)new RingBuffer$iterator.RingBuffer$iterator$1(this);
    }
    
    public final void removeFirst(final int n) {
        final int n2 = 1;
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("n shouldn't be negative but it is ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString().toString());
        }
        int n3;
        if (n <= this.size()) {
            n3 = n2;
        }
        else {
            n3 = 0;
        }
        if (n3 == 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("n shouldn't be greater than the buffer size: n = ");
            sb2.append(n);
            sb2.append(", size = ");
            sb2.append(this.size());
            throw new IllegalArgumentException(sb2.toString().toString());
        }
        if (n > 0) {
            final int startIndex = this.startIndex;
            final int startIndex2 = (startIndex + n) % this.getCapacity();
            if (startIndex > startIndex2) {
                this.fill(this.buffer, null, startIndex, this.capacity);
                this.fill(this.buffer, null, 0, startIndex2);
            }
            else {
                this.fill(this.buffer, null, startIndex, startIndex2);
            }
            this.startIndex = startIndex2;
            this.setSize(this.size() - n);
        }
    }
    
    @NotNull
    @Override
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }
    
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull final T[] original) {
        Intrinsics.checkParameterIsNotNull(original, "array");
        T[] copy = original;
        if (original.length < this.size()) {
            copy = Arrays.copyOf(original, this.size());
            Intrinsics.checkExpressionValueIsNotNull(copy, "java.util.Arrays.copyOf(this, newSize)");
        }
        final int size = this.size();
        int startIndex = this.startIndex;
        final int n = 0;
        int n2 = 0;
        int i;
        int n3;
        while (true) {
            i = n2;
            n3 = n;
            if (n2 >= size) {
                break;
            }
            i = n2;
            n3 = n;
            if (startIndex >= this.capacity) {
                break;
            }
            copy[n2] = (T)this.buffer[startIndex];
            ++n2;
            ++startIndex;
        }
        while (i < size) {
            copy[i] = (T)this.buffer[n3];
            ++i;
            ++n3;
        }
        if (copy.length > this.size()) {
            copy[this.size()] = null;
        }
        if (copy == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return copy;
    }
}
