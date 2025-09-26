// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import java.util.Iterator;
import java.util.HashSet;

public final class DataBufferObserverSet implements DataBufferObserver, Observable
{
    private HashSet<DataBufferObserver> zzadk;
    
    public DataBufferObserverSet() {
        this.zzadk = new HashSet<DataBufferObserver>();
    }
    
    @Override
    public void addObserver(final DataBufferObserver e) {
        this.zzadk.add(e);
    }
    
    public void clear() {
        this.zzadk.clear();
    }
    
    public boolean hasObservers() {
        return this.zzadk.isEmpty() ^ true;
    }
    
    @Override
    public void onDataChanged() {
        final Iterator<DataBufferObserver> iterator = this.zzadk.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDataChanged();
        }
    }
    
    @Override
    public void onDataRangeChanged(final int n, final int n2) {
        final Iterator<DataBufferObserver> iterator = this.zzadk.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDataRangeChanged(n, n2);
        }
    }
    
    @Override
    public void onDataRangeInserted(final int n, final int n2) {
        final Iterator<DataBufferObserver> iterator = this.zzadk.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDataRangeInserted(n, n2);
        }
    }
    
    @Override
    public void onDataRangeMoved(final int n, final int n2, final int n3) {
        final Iterator<DataBufferObserver> iterator = this.zzadk.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDataRangeMoved(n, n2, n3);
        }
    }
    
    @Override
    public void onDataRangeRemoved(final int n, final int n2) {
        final Iterator<DataBufferObserver> iterator = this.zzadk.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDataRangeRemoved(n, n2);
        }
    }
    
    @Override
    public void removeObserver(final DataBufferObserver o) {
        this.zzadk.remove(o);
    }
}
