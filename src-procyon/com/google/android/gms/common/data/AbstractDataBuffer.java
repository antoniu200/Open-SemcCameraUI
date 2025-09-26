// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import android.os.Bundle;
import java.util.Iterator;

public abstract class AbstractDataBuffer<T> implements DataBuffer<T>
{
    protected final DataHolder zzabq;
    
    protected AbstractDataBuffer(final DataHolder zzabq) {
        this.zzabq = zzabq;
        if (this.zzabq != null) {
            this.zzabq.zzr(this);
        }
    }
    
    @Deprecated
    @Override
    public final void close() {
        this.release();
    }
    
    @Override
    public abstract T get(final int p0);
    
    @Override
    public int getCount() {
        if (this.zzabq == null) {
            return 0;
        }
        return this.zzabq.getCount();
    }
    
    @Deprecated
    @Override
    public boolean isClosed() {
        return this.zzabq == null || this.zzabq.isClosed();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new zzb<T>(this);
    }
    
    @Override
    public void release() {
        if (this.zzabq != null) {
            this.zzabq.close();
        }
    }
    
    @Override
    public Iterator<T> singleRefIterator() {
        return new zzg<T>(this);
    }
    
    @Override
    public Bundle zzor() {
        return this.zzabq.zzor();
    }
}
