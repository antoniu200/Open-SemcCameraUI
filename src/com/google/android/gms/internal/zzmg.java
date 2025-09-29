package com.google.android.gms.internal;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzmg<K, V> {
    private int size;
    private final LinkedHashMap<K, V> zzagB;
    private int zzagC;
    private int zzagD;
    private int zzagE;
    private int zzagF;
    private int zzagG;
    private int zzagH;

    public zzmg(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.zzagC = i;
        this.zzagB = new LinkedHashMap<>(0, 0.75f, true);
    }

    private int zzc(K k, V v) {
        int iSizeOf = sizeOf(k, v);
        if (iSizeOf >= 0) {
            return iSizeOf;
        }
        throw new IllegalStateException("Negative size: " + k + "=" + v);
    }

    protected V create(K k) {
        return null;
    }

    protected void entryRemoved(boolean z, K k, V v, V v2) {
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final V get(K k) {
        V v;
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v2 = this.zzagB.get(k);
            if (v2 != null) {
                this.zzagG++;
                return v2;
            }
            this.zzagH++;
            V vCreate = create(k);
            if (vCreate == null) {
                return null;
            }
            synchronized (this) {
                this.zzagE++;
                v = (V) this.zzagB.put(k, vCreate);
                if (v != null) {
                    this.zzagB.put(k, v);
                } else {
                    this.size += zzc(k, vCreate);
                }
            }
            if (v != null) {
                entryRemoved(false, k, vCreate, v);
                return v;
            }
            trimToSize(this.zzagC);
            return vCreate;
        }
    }

    public final V put(K k, V v) {
        V vPut;
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.zzagD++;
            this.size += zzc(k, v);
            vPut = this.zzagB.put(k, v);
            if (vPut != null) {
                this.size -= zzc(k, vPut);
            }
        }
        if (vPut != null) {
            entryRemoved(false, k, vPut, v);
        }
        trimToSize(this.zzagC);
        return vPut;
    }

    public final synchronized int size() {
        return this.size;
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public final synchronized String toString() {
        int i;
        i = this.zzagG + this.zzagH;
        return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.zzagC), Integer.valueOf(this.zzagG), Integer.valueOf(this.zzagH), Integer.valueOf(i != 0 ? (100 * this.zzagG) / i : 0));
    }

    public void trimToSize(int maxSize) {
        for (;;) {
            K key;
            V value;
            synchronized (this) {
                if (size < 0 || (zzagB.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }
                if (size <= maxSize || zzagB.isEmpty()) {
                    return;
                }
                // Evict the eldest entry (LinkedHashMap preserves order appropriate for this cache)
                Entry<K,V> toEvict = zzagB.entrySet().iterator().next();
                key = toEvict.getKey();
                value = toEvict.getValue();
                zzagB.remove(key);
                size -= zzc(key, value); // per-class size accounting
                zzagF++;                 // eviction/modification count
            }
            // Notify outside the monitor
            entryRemoved(true, key, value, null);
        }
    }
}
