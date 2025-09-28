package com.google.android.gms.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class zzme<K, V> extends zzmi<K, V> implements Map<K, V> {
    zzmh<K, V> zzagz;

    private zzmh<K, V> zzpx() {
        if (this.zzagz == null) {
            this.zzagz = new zzmh<K, V>() { // from class: com.google.android.gms.internal.zzme.1
                @Override // com.google.android.gms.internal.zzmh
                protected void colClear() {
                    zzme.this.clear();
                }

                @Override // com.google.android.gms.internal.zzmh
                protected Object colGetEntry(int i, int i2) {
                    return zzme.this.mArray[(i << 1) + i2];
                }

                @Override // com.google.android.gms.internal.zzmh
                protected Map<K, V> colGetMap() {
                    return zzme.this;
                }

                @Override // com.google.android.gms.internal.zzmh
                protected int colGetSize() {
                    return zzme.this.mSize;
                }

                @Override // com.google.android.gms.internal.zzmh
                protected int colIndexOfKey(Object obj) {
                    return obj == null ? zzme.this.indexOfNull() : zzme.this.indexOf(obj, obj.hashCode());
                }

                @Override // com.google.android.gms.internal.zzmh
                protected int colIndexOfValue(Object obj) {
                    return zzme.this.indexOfValue(obj);
                }

                @Override // com.google.android.gms.internal.zzmh
                protected void colPut(K k, V v) {
                    zzme.this.put(k, v);
                }

                @Override // com.google.android.gms.internal.zzmh
                protected void colRemoveAt(int i) {
                    zzme.this.removeAt(i);
                }

                @Override // com.google.android.gms.internal.zzmh
                protected V colSetValue(int i, V v) {
                    return zzme.this.setValueAt(i, v);
                }
            };
        }
        return this.zzagz;
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return zzpx().getEntrySet();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return zzpx().getKeySet();
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        ensureCapacity(this.mSize + map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return zzpx().getValues();
    }
}
