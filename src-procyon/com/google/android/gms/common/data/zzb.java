// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import java.util.NoSuchElementException;
import com.google.android.gms.common.internal.zzx;
import java.util.Iterator;

public class zzb<T> implements Iterator<T>
{
    protected final DataBuffer<T> zzadi;
    protected int zzadj;
    
    public zzb(final DataBuffer<T> dataBuffer) {
        this.zzadi = zzx.zzw(dataBuffer);
        this.zzadj = -1;
    }
    
    @Override
    public boolean hasNext() {
        return this.zzadj < this.zzadi.getCount() - 1;
    }
    
    @Override
    public T next() {
        if (!this.hasNext()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot advance the iterator beyond ");
            sb.append(this.zzadj);
            throw new NoSuchElementException(sb.toString());
        }
        final DataBuffer<T> zzadi = this.zzadi;
        final int zzadj = this.zzadj + 1;
        this.zzadj = zzadj;
        return zzadi.get(zzadj);
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
