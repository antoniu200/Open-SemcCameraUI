// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.util;

import java.util.Iterator;
import java.util.Set;
import android.support.annotation.NonNull;
import java.util.Collection;
import android.support.annotation.Nullable;
import java.util.Map;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V>
{
    @Nullable
    MapCollections<K, V> mCollections;
    
    public ArrayMap() {
    }
    
    public ArrayMap(final int n) {
        super(n);
    }
    
    public ArrayMap(final SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }
    
    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>(this) {
                final ArrayMap this$0;
                
                @Override
                protected void colClear() {
                    this.this$0.clear();
                }
                
                @Override
                protected Object colGetEntry(final int n, final int n2) {
                    return this.this$0.mArray[(n << 1) + n2];
                }
                
                @Override
                protected Map<K, V> colGetMap() {
                    return this.this$0;
                }
                
                @Override
                protected int colGetSize() {
                    return this.this$0.mSize;
                }
                
                @Override
                protected int colIndexOfKey(final Object o) {
                    return this.this$0.indexOfKey(o);
                }
                
                @Override
                protected int colIndexOfValue(final Object o) {
                    return this.this$0.indexOfValue(o);
                }
                
                @Override
                protected void colPut(final K k, final V v) {
                    this.this$0.put(k, v);
                }
                
                @Override
                protected void colRemoveAt(final int n) {
                    this.this$0.removeAt(n);
                }
                
                @Override
                protected V colSetValue(final int n, final V v) {
                    return this.this$0.setValueAt(n, v);
                }
            };
        }
        return this.mCollections;
    }
    
    public boolean containsAll(@NonNull final Collection<?> collection) {
        return MapCollections.containsAllHelper((Map<Object, Object>)this, collection);
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.getCollection().getEntrySet();
    }
    
    @Override
    public Set<K> keySet() {
        return this.getCollection().getKeySet();
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.ensureCapacity(this.mSize + map.size());
        for (final Entry<K, V> entry : map.entrySet()) {
            this.put(entry.getKey(), (V)entry.getValue());
        }
    }
    
    public boolean removeAll(@NonNull final Collection<?> collection) {
        return MapCollections.removeAllHelper((Map<Object, Object>)this, collection);
    }
    
    public boolean retainAll(@NonNull final Collection<?> collection) {
        return MapCollections.retainAllHelper((Map<Object, Object>)this, collection);
    }
    
    @Override
    public Collection<V> values() {
        return this.getCollection().getValues();
    }
}
