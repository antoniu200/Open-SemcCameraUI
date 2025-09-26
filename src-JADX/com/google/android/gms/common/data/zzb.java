package com.google.android.gms.common.data;

import com.google.android.gms.common.internal.zzx;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzb<T> implements Iterator<T> {
    protected final DataBuffer<T> zzadi;
    protected int zzadj = -1;

    public zzb(DataBuffer<T> dataBuffer) {
        this.zzadi = (DataBuffer) zzx.zzw(dataBuffer);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.zzadj < this.zzadi.getCount() - 1;
    }

    @Override // java.util.Iterator
    public T next() {
        if (hasNext()) {
            DataBuffer<T> dataBuffer = this.zzadi;
            int i = this.zzadj + 1;
            this.zzadj = i;
            return dataBuffer.get(i);
        }
        throw new NoSuchElementException("Cannot advance the iterator beyond " + this.zzadj);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
