// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.util;

import java.util.Locale;
import java.util.Map;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.util.LinkedHashMap;

public class LruCache<K, V>
{
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;
    
    public LruCache(final int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }
    
    private int safeSizeOf(final K obj, final V obj2) {
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
    
    @Nullable
    protected V create(@NonNull final K k) {
        return null;
    }
    
    public final int createCount() {
        synchronized (this) {
            return this.createCount;
        }
    }
    
    protected void entryRemoved(final boolean b, @NonNull final K k, @NonNull final V v, @Nullable final V v2) {
    }
    
    public final void evictAll() {
        this.trimToSize(-1);
    }
    
    public final int evictionCount() {
        synchronized (this) {
            return this.evictionCount;
        }
    }
    
    @Nullable
    public final V get(@NonNull final K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            final V value = this.map.get(key);
            if (value != null) {
                ++this.hitCount;
                return value;
            }
            ++this.missCount;
            monitorexit(this);
            final V create = this.create(key);
            if (create == null) {
                return null;
            }
            synchronized (this) {
                ++this.createCount;
                final V put = this.map.put(key, create);
                if (put != null) {
                    this.map.put(key, put);
                }
                else {
                    this.size += this.safeSizeOf(key, create);
                }
                monitorexit(this);
                if (put != null) {
                    this.entryRemoved(false, key, create, put);
                    return put;
                }
                this.trimToSize(this.maxSize);
                return create;
            }
        }
    }
    
    public final int hitCount() {
        synchronized (this) {
            return this.hitCount;
        }
    }
    
    public final int maxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public final int missCount() {
        synchronized (this) {
            return this.missCount;
        }
    }
    
    @Nullable
    public final V put(@NonNull final K key, @NonNull final V value) {
        if (key != null) {
            if (value != null) {
                synchronized (this) {
                    ++this.putCount;
                    this.size += this.safeSizeOf(key, value);
                    final V put = this.map.put(key, value);
                    if (put != null) {
                        this.size -= this.safeSizeOf(key, put);
                    }
                    monitorexit(this);
                    if (put != null) {
                        this.entryRemoved(false, key, put, value);
                    }
                    this.trimToSize(this.maxSize);
                    return put;
                }
            }
        }
        throw new NullPointerException("key == null || value == null");
    }
    
    public final int putCount() {
        synchronized (this) {
            return this.putCount;
        }
    }
    
    @Nullable
    public final V remove(@NonNull final K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            final V remove = this.map.remove(key);
            if (remove != null) {
                this.size -= this.safeSizeOf(key, remove);
            }
            monitorexit(this);
            if (remove != null) {
                this.entryRemoved(false, key, remove, null);
            }
            return remove;
        }
    }
    
    public void resize(final int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        synchronized (this) {
            this.maxSize = maxSize;
            monitorexit(this);
            this.trimToSize(maxSize);
        }
    }
    
    public final int size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    protected int sizeOf(@NonNull final K k, @NonNull final V v) {
        return 1;
    }
    
    public final Map<K, V> snapshot() {
        synchronized (this) {
            return new LinkedHashMap<K, V>((Map<? extends K, ? extends V>)this.map);
        }
    }
    
    @Override
    public final String toString() {
        synchronized (this) {
            final int n = this.hitCount + this.missCount;
            int i;
            if (n != 0) {
                i = 100 * this.hitCount / n;
            }
            else {
                i = 0;
            }
            return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", this.maxSize, this.hitCount, this.missCount, i);
        }
    }
    
    public void trimToSize(final int n) {
        while (true) {
            synchronized (this) {
                if (this.size < 0 || (this.map.isEmpty() && this.size != 0)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.getClass().getName());
                    sb.append(".sizeOf() is reporting inconsistent results!");
                    throw new IllegalStateException(sb.toString());
                }
                if (this.size <= n || this.map.isEmpty()) {
                    return;
                }
                final Map.Entry entry = (Map.Entry)this.map.entrySet().iterator().next();
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                this.map.remove(key);
                this.size -= this.safeSizeOf((K)key, (V)value);
                ++this.evictionCount;
                monitorexit(this);
                this.entryRemoved(true, (K)key, (V)value, null);
            }
        }
    }
}
