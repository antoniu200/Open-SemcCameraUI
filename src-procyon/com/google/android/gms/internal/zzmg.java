// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Map;
import java.util.LinkedHashMap;

public class zzmg<K, V>
{
    private int size;
    private final LinkedHashMap<K, V> zzagB;
    private int zzagC;
    private int zzagD;
    private int zzagE;
    private int zzagF;
    private int zzagG;
    private int zzagH;
    
    public zzmg(final int zzagC) {
        if (zzagC <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.zzagC = zzagC;
        this.zzagB = new LinkedHashMap<K, V>(0, 0.75f, true);
    }
    
    private int zzc(final K obj, final V obj2) {
        final int size = this.sizeOf(obj, obj2);
        if (size < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Negative size: ");
            sb.append(obj);
            sb.append("=");
            sb.append(obj2);
            throw new IllegalStateException(sb.toString());
        }
        return size;
    }
    
    protected V create(final K k) {
        return null;
    }
    
    protected void entryRemoved(final boolean b, final K k, final V v, final V v2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final V get(final K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            final V value = this.zzagB.get(key);
            if (value != null) {
                ++this.zzagG;
                return value;
            }
            ++this.zzagH;
            monitorexit(this);
            final V create = this.create(key);
            if (create == null) {
                return null;
            }
            synchronized (this) {
                ++this.zzagE;
                final V put = this.zzagB.put(key, create);
                if (put != null) {
                    this.zzagB.put(key, put);
                }
                else {
                    this.size += this.zzc(key, create);
                }
                monitorexit(this);
                if (put != null) {
                    this.entryRemoved(false, key, create, put);
                    return put;
                }
                this.trimToSize(this.zzagC);
                return create;
            }
        }
    }
    
    public final V put(final K key, final V value) {
        if (key != null) {
            if (value != null) {
                synchronized (this) {
                    ++this.zzagD;
                    this.size += this.zzc(key, value);
                    final V put = this.zzagB.put(key, value);
                    if (put != null) {
                        this.size -= this.zzc(key, put);
                    }
                    monitorexit(this);
                    if (put != null) {
                        this.entryRemoved(false, key, put, value);
                    }
                    this.trimToSize(this.zzagC);
                    return put;
                }
            }
        }
        throw new NullPointerException("key == null || value == null");
    }
    
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    protected int sizeOf(final K k, final V v) {
        return 1;
    }
    
    @Override
    public final String toString() {
        synchronized (this) {
            final int n = this.zzagG + this.zzagH;
            int i;
            if (n != 0) {
                i = 100 * this.zzagG / n;
            }
            else {
                i = 0;
            }
            return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.zzagC, this.zzagG, this.zzagH, i);
        }
    }
    
    public void trimToSize(final int n) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.zzagB.isEmpty() && this.size != 0)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.getClass().getName());
                    sb.append(".sizeOf() is reporting inconsistent results!");
                    throw new IllegalStateException(sb.toString());
                }
                if (this.size <= n || this.zzagB.isEmpty()) {
                    return;
                }
                final Map.Entry entry = (Map.Entry)this.zzagB.entrySet().iterator().next();
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                this.zzagB.remove(key);
                this.size -= this.zzc((K)key, (V)value);
                ++this.zzagF;
                monitorexit(this);
                this.entryRemoved(true, (K)key, (V)value, null);
            }
        }
    }
}
