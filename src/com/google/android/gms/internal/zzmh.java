package com.google.android.gms.internal;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class zzmh<K, V> {
    zzmh<K, V>.zzb zzagI;
    zzmh<K, V>.zzc zzagJ;
    zzmh<K, V>.zze zzagK;

    final class zza<T> implements Iterator<T> {
        boolean mCanRemove = false;
        int mIndex;
        final int mOffset;
        int mSize;

        zza(int i) {
            this.mOffset = i;
            this.mSize = zzmh.this.colGetSize();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }

        @Override // java.util.Iterator
        public T next() {
            T t = (T) zzmh.this.colGetEntry(this.mIndex, this.mOffset);
            this.mIndex++;
            this.mCanRemove = true;
            return t;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.mCanRemove) {
                throw new IllegalStateException();
            }
            this.mIndex--;
            this.mSize--;
            this.mCanRemove = false;
            zzmh.this.colRemoveAt(this.mIndex);
        }
    }

    final class zzb implements Set<Map.Entry<K, V>> {
        zzb() {
        }

        @Override // java.util.Set, java.util.Collection
        public boolean add(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            int iColGetSize = zzmh.this.colGetSize();
            for (Map.Entry<K, V> entry : collection) {
                zzmh.this.colPut(entry.getKey(), entry.getValue());
            }
            return iColGetSize != zzmh.this.colGetSize();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            zzmh.this.colClear();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            int iColIndexOfKey = zzmh.this.colIndexOfKey(entry.getKey());
            if (iColIndexOfKey < 0) {
                return false;
            }
            return zzmf.equal(zzmh.this.colGetEntry(iColIndexOfKey, 1), entry.getValue());
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return zzmh.equalsSetHelper(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int iHashCode = 0;
            for (int iColGetSize = zzmh.this.colGetSize() - 1; iColGetSize >= 0; iColGetSize--) {
                Object objColGetEntry = zzmh.this.colGetEntry(iColGetSize, 0);
                Object objColGetEntry2 = zzmh.this.colGetEntry(iColGetSize, 1);
                iHashCode += (objColGetEntry == null ? 0 : objColGetEntry.hashCode()) ^ (objColGetEntry2 == null ? 0 : objColGetEntry2.hashCode());
            }
            return iHashCode;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return zzmh.this.colGetSize() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return new zzd();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return zzmh.this.colGetSize();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            throw new UnsupportedOperationException();
        }
    }

    final class zzc implements Set<K> {
        zzc() {
        }

        @Override // java.util.Set, java.util.Collection
        public boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            zzmh.this.colClear();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            return zzmh.this.colIndexOfKey(obj) >= 0;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return zzmh.containsAllHelper(zzmh.this.colGetMap(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return zzmh.equalsSetHelper(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int iHashCode = 0;
            for (int iColGetSize = zzmh.this.colGetSize() - 1; iColGetSize >= 0; iColGetSize--) {
                Object objColGetEntry = zzmh.this.colGetEntry(iColGetSize, 0);
                iHashCode += objColGetEntry == null ? 0 : objColGetEntry.hashCode();
            }
            return iHashCode;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return zzmh.this.colGetSize() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<K> iterator() {
            return new zza(0);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            int iColIndexOfKey = zzmh.this.colIndexOfKey(obj);
            if (iColIndexOfKey < 0) {
                return false;
            }
            zzmh.this.colRemoveAt(iColIndexOfKey);
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            return zzmh.removeAllHelper(zzmh.this.colGetMap(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            return zzmh.retainAllHelper(zzmh.this.colGetMap(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return zzmh.this.colGetSize();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            return zzmh.this.toArrayHelper(0);
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) zzmh.this.toArrayHelper(tArr, 0);
        }
    }

    final class zzd implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {
        int mEnd;
        boolean mEntryValid = false;
        int mIndex = -1;

        zzd() {
            this.mEnd = zzmh.this.colGetSize() - 1;
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object obj) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return zzmf.equal(entry.getKey(), zzmh.this.colGetEntry(this.mIndex, 0)) && zzmf.equal(entry.getValue(), zzmh.this.colGetEntry(this.mIndex, 1));
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            if (this.mEntryValid) {
                return (K) zzmh.this.colGetEntry(this.mIndex, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            if (this.mEntryValid) {
                return (V) zzmh.this.colGetEntry(this.mIndex, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            Object objColGetEntry = zzmh.this.colGetEntry(this.mIndex, 0);
            Object objColGetEntry2 = zzmh.this.colGetEntry(this.mIndex, 1);
            return (objColGetEntry == null ? 0 : objColGetEntry.hashCode()) ^ (objColGetEntry2 != null ? objColGetEntry2.hashCode() : 0);
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            this.mIndex++;
            this.mEntryValid = true;
            return this;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.mEntryValid) {
                throw new IllegalStateException();
            }
            zzmh.this.colRemoveAt(this.mIndex);
            this.mIndex--;
            this.mEnd--;
            this.mEntryValid = false;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            if (this.mEntryValid) {
                return (V) zzmh.this.colSetValue(this.mIndex, v);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }

    final class zze implements Collection<V> {
        zze() {
        }

        @Override // java.util.Collection
        public boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public void clear() {
            zzmh.this.colClear();
        }

        @Override // java.util.Collection
        public boolean contains(Object obj) {
            return zzmh.this.colIndexOfValue(obj) >= 0;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return zzmh.this.colGetSize() == 0;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new zza(1);
        }

        @Override // java.util.Collection
        public boolean remove(Object obj) {
            int iColIndexOfValue = zzmh.this.colIndexOfValue(obj);
            if (iColIndexOfValue < 0) {
                return false;
            }
            zzmh.this.colRemoveAt(iColIndexOfValue);
            return true;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            int iColGetSize = zzmh.this.colGetSize();
            int i = 0;
            boolean z = false;
            while (i < iColGetSize) {
                if (collection.contains(zzmh.this.colGetEntry(i, 1))) {
                    zzmh.this.colRemoveAt(i);
                    i--;
                    iColGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            int iColGetSize = zzmh.this.colGetSize();
            int i = 0;
            boolean z = false;
            while (i < iColGetSize) {
                if (!collection.contains(zzmh.this.colGetEntry(i, 1))) {
                    zzmh.this.colRemoveAt(i);
                    i--;
                    iColGetSize--;
                    z = true;
                }
                i++;
            }
            return z;
        }

        @Override // java.util.Collection
        public int size() {
            return zzmh.this.colGetSize();
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            return zzmh.this.toArrayHelper(1);
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) zzmh.this.toArrayHelper(tArr, 1);
        }
    }

    zzmh() {
    }

    public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!map.containsKey(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equalsSetHelper(Set<T> set, Object obj) {
        if (set == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set2 = (Set) obj;
            try {
                if (set.size() == set2.size()) {
                    if (set.containsAll(set2)) {
                        return true;
                    }
                }
                return false;
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            map.remove(it.next());
        }
        return size != map.size();
    }

    public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<K> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    protected abstract void colClear();

    protected abstract Object colGetEntry(int i, int i2);

    protected abstract Map<K, V> colGetMap();

    protected abstract int colGetSize();

    protected abstract int colIndexOfKey(Object obj);

    protected abstract int colIndexOfValue(Object obj);

    protected abstract void colPut(K k, V v);

    protected abstract void colRemoveAt(int i);

    protected abstract V colSetValue(int i, V v);

    public Set<Map.Entry<K, V>> getEntrySet() {
        if (this.zzagI == null) {
            this.zzagI = new zzb();
        }
        return this.zzagI;
    }

    public Set<K> getKeySet() {
        if (this.zzagJ == null) {
            this.zzagJ = new zzc();
        }
        return this.zzagJ;
    }

    public Collection<V> getValues() {
        if (this.zzagK == null) {
            this.zzagK = new zze();
        }
        return this.zzagK;
    }

    public Object[] toArrayHelper(int i) {
        int iColGetSize = colGetSize();
        Object[] objArr = new Object[iColGetSize];
        for (int i2 = 0; i2 < iColGetSize; i2++) {
            objArr[i2] = colGetEntry(i2, i);
        }
        return objArr;
    }

    public <T> T[] toArrayHelper(T[] tArr, int i) {
        int iColGetSize = colGetSize();
        if (tArr.length < iColGetSize) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), iColGetSize));
        }
        for (int i2 = 0; i2 < iColGetSize; i2++) {
            tArr[i2] = colGetEntry(i2, i);
        }
        if (tArr.length > iColGetSize) {
            tArr[iColGetSize] = null;
        }
        return tArr;
    }
}
