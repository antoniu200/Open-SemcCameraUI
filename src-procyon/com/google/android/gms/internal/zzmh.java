// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.lang.reflect.Array;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;

abstract class zzmh<K, V>
{
    zzb zzagI;
    zzc zzagJ;
    zze zzagK;
    
    public static <K, V> boolean containsAllHelper(final Map<K, V> map, final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!map.containsKey(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public static <T> boolean equalsSetHelper(final Set<T> set, final Object o) {
        if (set == o) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        final Set set2 = (Set)o;
        try {
            return set.size() == set2.size() && set.containsAll(set2);
        }
        catch (final NullPointerException | ClassCastException ex) {
            return false;
        }
    }
    
    public static <K, V> boolean removeAllHelper(final Map<K, V> map, final Collection<?> collection) {
        final int size = map.size();
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            map.remove(iterator.next());
        }
        return size != map.size();
    }
    
    public static <K, V> boolean retainAllHelper(final Map<K, V> map, final Collection<?> collection) {
        final int size = map.size();
        final Iterator<K> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            if (!collection.contains(iterator.next())) {
                iterator.remove();
            }
        }
        return size != map.size();
    }
    
    protected abstract void colClear();
    
    protected abstract Object colGetEntry(final int p0, final int p1);
    
    protected abstract Map<K, V> colGetMap();
    
    protected abstract int colGetSize();
    
    protected abstract int colIndexOfKey(final Object p0);
    
    protected abstract int colIndexOfValue(final Object p0);
    
    protected abstract void colPut(final K p0, final V p1);
    
    protected abstract void colRemoveAt(final int p0);
    
    protected abstract V colSetValue(final int p0, final V p1);
    
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
    
    public Object[] toArrayHelper(final int n) {
        final int colGetSize = this.colGetSize();
        final Object[] array = new Object[colGetSize];
        for (int i = 0; i < colGetSize; ++i) {
            array[i] = this.colGetEntry(i, n);
        }
        return array;
    }
    
    public <T> T[] toArrayHelper(final T[] array, final int n) {
        final int colGetSize = this.colGetSize();
        Object[] array2 = array;
        if (array.length < colGetSize) {
            array2 = (Object[])Array.newInstance(array.getClass().getComponentType(), colGetSize);
        }
        for (int i = 0; i < colGetSize; ++i) {
            array2[i] = this.colGetEntry(i, n);
        }
        if (array2.length > colGetSize) {
            array2[colGetSize] = null;
        }
        return (T[])array2;
    }
    
    final class zza<T> implements Iterator<T>
    {
        boolean mCanRemove;
        int mIndex;
        final int mOffset;
        int mSize;
        final zzmh zzagL;
        
        zza(final zzmh zzagL, final int mOffset) {
            this.zzagL = zzagL;
            this.mCanRemove = false;
            this.mOffset = mOffset;
            this.mSize = zzagL.colGetSize();
        }
        
        @Override
        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }
        
        @Override
        public T next() {
            final Object colGetEntry = this.zzagL.colGetEntry(this.mIndex, this.mOffset);
            ++this.mIndex;
            this.mCanRemove = true;
            return (T)colGetEntry;
        }
        
        @Override
        public void remove() {
            if (!this.mCanRemove) {
                throw new IllegalStateException();
            }
            --this.mIndex;
            --this.mSize;
            this.mCanRemove = false;
            this.zzagL.colRemoveAt(this.mIndex);
        }
    }
    
    final class zzb implements Set<Map.Entry<K, V>>
    {
        final zzmh zzagL;
        
        zzb(final zzmh zzagL) {
            this.zzagL = zzagL;
        }
        
        @Override
        public boolean add(final Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Map.Entry<K, V>> collection) {
            final int colGetSize = this.zzagL.colGetSize();
            for (final Map.Entry<K, V> entry : collection) {
                this.zzagL.colPut(entry.getKey(), entry.getValue());
            }
            return colGetSize != this.zzagL.colGetSize();
        }
        
        @Override
        public void clear() {
            this.zzagL.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry entry = (Map.Entry)o;
            final int colIndexOfKey = this.zzagL.colIndexOfKey(entry.getKey());
            return colIndexOfKey >= 0 && zzmf.equal(this.zzagL.colGetEntry(colIndexOfKey, 1), entry.getValue());
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            final Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (!this.contains(iterator.next())) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean equals(final Object o) {
            return zzmh.equalsSetHelper((Set<Object>)this, o);
        }
        
        @Override
        public int hashCode() {
            int i = this.zzagL.colGetSize() - 1;
            int n = 0;
            while (i >= 0) {
                final Object colGetEntry = this.zzagL.colGetEntry(i, 0);
                final Object colGetEntry2 = this.zzagL.colGetEntry(i, 1);
                int hashCode;
                if (colGetEntry == null) {
                    hashCode = 0;
                }
                else {
                    hashCode = colGetEntry.hashCode();
                }
                int hashCode2;
                if (colGetEntry2 == null) {
                    hashCode2 = 0;
                }
                else {
                    hashCode2 = colGetEntry2.hashCode();
                }
                n += (hashCode ^ hashCode2);
                --i;
            }
            return n;
        }
        
        @Override
        public boolean isEmpty() {
            return this.zzagL.colGetSize() == 0;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return this.zzagL.new zzd();
        }
        
        @Override
        public boolean remove(final Object o) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int size() {
            return this.zzagL.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            throw new UnsupportedOperationException();
        }
    }
    
    final class zzc implements Set<K>
    {
        final zzmh zzagL;
        
        zzc(final zzmh zzagL) {
            this.zzagL = zzagL;
        }
        
        @Override
        public boolean add(final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            this.zzagL.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.zzagL.colIndexOfKey(o) >= 0;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            return zzmh.containsAllHelper(this.zzagL.colGetMap(), collection);
        }
        
        @Override
        public boolean equals(final Object o) {
            return zzmh.equalsSetHelper((Set<Object>)this, o);
        }
        
        @Override
        public int hashCode() {
            int i = this.zzagL.colGetSize() - 1;
            int n = 0;
            while (i >= 0) {
                final Object colGetEntry = this.zzagL.colGetEntry(i, 0);
                int hashCode;
                if (colGetEntry == null) {
                    hashCode = 0;
                }
                else {
                    hashCode = colGetEntry.hashCode();
                }
                n += hashCode;
                --i;
            }
            return n;
        }
        
        @Override
        public boolean isEmpty() {
            return this.zzagL.colGetSize() == 0;
        }
        
        @Override
        public Iterator<K> iterator() {
            return (Iterator<K>)this.zzagL.new zza(0);
        }
        
        @Override
        public boolean remove(final Object o) {
            final int colIndexOfKey = this.zzagL.colIndexOfKey(o);
            if (colIndexOfKey >= 0) {
                this.zzagL.colRemoveAt(colIndexOfKey);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            return zzmh.removeAllHelper(this.zzagL.colGetMap(), collection);
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            return zzmh.retainAllHelper(this.zzagL.colGetMap(), collection);
        }
        
        @Override
        public int size() {
            return this.zzagL.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            return this.zzagL.toArrayHelper(0);
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.zzagL.toArrayHelper(array, 0);
        }
    }
    
    final class zzd implements Iterator<Entry<K, V>>, Entry<K, V>
    {
        int mEnd;
        boolean mEntryValid;
        int mIndex;
        final zzmh zzagL;
        
        zzd(final zzmh zzagL) {
            this.zzagL = zzagL;
            this.mEntryValid = false;
            this.mEnd = zzagL.colGetSize() - 1;
            this.mIndex = -1;
        }
        
        @Override
        public final boolean equals(final Object o) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            final boolean b = o instanceof Entry;
            final boolean b2 = false;
            if (!b) {
                return false;
            }
            final Entry entry = (Entry)o;
            boolean b3 = b2;
            if (zzmf.equal(entry.getKey(), this.zzagL.colGetEntry(this.mIndex, 0))) {
                b3 = b2;
                if (zzmf.equal(entry.getValue(), this.zzagL.colGetEntry(this.mIndex, 1))) {
                    b3 = true;
                }
            }
            return b3;
        }
        
        @Override
        public K getKey() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return (K)this.zzagL.colGetEntry(this.mIndex, 0);
        }
        
        @Override
        public V getValue() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return (V)this.zzagL.colGetEntry(this.mIndex, 1);
        }
        
        @Override
        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }
        
        @Override
        public final int hashCode() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            final zzmh zzagL = this.zzagL;
            final int mIndex = this.mIndex;
            int hashCode = 0;
            final Object colGetEntry = zzagL.colGetEntry(mIndex, 0);
            final Object colGetEntry2 = this.zzagL.colGetEntry(this.mIndex, 1);
            int hashCode2;
            if (colGetEntry == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = colGetEntry.hashCode();
            }
            if (colGetEntry2 != null) {
                hashCode = colGetEntry2.hashCode();
            }
            return hashCode2 ^ hashCode;
        }
        
        @Override
        public Entry<K, V> next() {
            ++this.mIndex;
            this.mEntryValid = true;
            return this;
        }
        
        @Override
        public void remove() {
            if (!this.mEntryValid) {
                throw new IllegalStateException();
            }
            this.zzagL.colRemoveAt(this.mIndex);
            --this.mIndex;
            --this.mEnd;
            this.mEntryValid = false;
        }
        
        @Override
        public V setValue(final V v) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return this.zzagL.colSetValue(this.mIndex, v);
        }
        
        @Override
        public final String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getKey());
            sb.append("=");
            sb.append(this.getValue());
            return sb.toString();
        }
    }
    
    final class zze implements Collection<V>
    {
        final zzmh zzagL;
        
        zze(final zzmh zzagL) {
            this.zzagL = zzagL;
        }
        
        @Override
        public boolean add(final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            this.zzagL.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.zzagL.colIndexOfValue(o) >= 0;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            final Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (!this.contains(iterator.next())) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean isEmpty() {
            return this.zzagL.colGetSize() == 0;
        }
        
        @Override
        public Iterator<V> iterator() {
            return (Iterator<V>)this.zzagL.new zza(1);
        }
        
        @Override
        public boolean remove(final Object o) {
            final int colIndexOfValue = this.zzagL.colIndexOfValue(o);
            if (colIndexOfValue >= 0) {
                this.zzagL.colRemoveAt(colIndexOfValue);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            int colGetSize = this.zzagL.colGetSize();
            int i = 0;
            boolean b = false;
            while (i < colGetSize) {
                int n = colGetSize;
                int n2 = i;
                if (collection.contains(this.zzagL.colGetEntry(i, 1))) {
                    this.zzagL.colRemoveAt(i);
                    n2 = i - 1;
                    n = colGetSize - 1;
                    b = true;
                }
                i = n2 + 1;
                colGetSize = n;
            }
            return b;
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            int colGetSize = this.zzagL.colGetSize();
            int i = 0;
            boolean b = false;
            while (i < colGetSize) {
                int n = colGetSize;
                int n2 = i;
                if (!collection.contains(this.zzagL.colGetEntry(i, 1))) {
                    this.zzagL.colRemoveAt(i);
                    n2 = i - 1;
                    n = colGetSize - 1;
                    b = true;
                }
                i = n2 + 1;
                colGetSize = n;
            }
            return b;
        }
        
        @Override
        public int size() {
            return this.zzagL.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            return this.zzagL.toArrayHelper(1);
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.zzagL.toArrayHelper(array, 1);
        }
    }
}
