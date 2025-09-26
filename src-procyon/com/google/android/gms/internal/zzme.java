// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

public class zzme<K, V> extends zzmi<K, V> implements Map<K, V>
{
    zzmh<K, V> zzagz;
    
    private zzmh<K, V> zzpx() {
        if (this.zzagz == null) {
            this.zzagz = new zzmh<K, V>(this) {
                final zzme zzagA;
                
                @Override
                protected void colClear() {
                    this.zzagA.clear();
                }
                
                @Override
                protected Object colGetEntry(final int n, final int n2) {
                    return this.zzagA.mArray[(n << 1) + n2];
                }
                
                @Override
                protected Map<K, V> colGetMap() {
                    return this.zzagA;
                }
                
                @Override
                protected int colGetSize() {
                    return this.zzagA.mSize;
                }
                
                @Override
                protected int colIndexOfKey(final Object o) {
                    if (o == null) {
                        return this.zzagA.indexOfNull();
                    }
                    return this.zzagA.indexOf(o, o.hashCode());
                }
                
                @Override
                protected int colIndexOfValue(final Object o) {
                    return this.zzagA.indexOfValue(o);
                }
                
                @Override
                protected void colPut(final K k, final V v) {
                    this.zzagA.put(k, v);
                }
                
                @Override
                protected void colRemoveAt(final int n) {
                    this.zzagA.removeAt(n);
                }
                
                @Override
                protected V colSetValue(final int n, final V v) {
                    return this.zzagA.setValueAt(n, v);
                }
            };
        }
        return this.zzagz;
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.zzpx().getEntrySet();
    }
    
    @Override
    public Set<K> keySet() {
        return this.zzpx().getKeySet();
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.ensureCapacity(this.mSize + map.size());
        for (final Entry<K, V> entry : map.entrySet()) {
            this.put(entry.getKey(), (V)entry.getValue());
        }
    }
    
    @Override
    public Collection<V> values() {
        return this.zzpx().getValues();
    }
}
