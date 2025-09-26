// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.util;

import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import android.support.annotation.Nullable;

abstract class MapCollections<K, V>
{
    @Nullable
    EntrySet mEntrySet;
    @Nullable
    KeySet mKeySet;
    @Nullable
    ValuesCollection mValues;
    
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
        boolean b = true;
        if (set == o) {
            return true;
        }
        if (o instanceof Set) {
            final Set set2 = (Set)o;
            try {
                if (set.size() != set2.size() || !set.containsAll(set2)) {
                    b = false;
                }
                return b;
            }
            catch (final ClassCastException ex) {
                return false;
            }
            catch (final NullPointerException ex2) {
                return false;
            }
        }
        return false;
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
        if (this.mEntrySet == null) {
            this.mEntrySet = new EntrySet();
        }
        return this.mEntrySet;
    }
    
    public Set<K> getKeySet() {
        if (this.mKeySet == null) {
            this.mKeySet = new KeySet();
        }
        return this.mKeySet;
    }
    
    public Collection<V> getValues() {
        if (this.mValues == null) {
            this.mValues = new ValuesCollection();
        }
        return this.mValues;
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
    
    final class ArrayIterator<T> implements Iterator<T>
    {
        boolean mCanRemove;
        int mIndex;
        final int mOffset;
        int mSize;
        final MapCollections this$0;
        
        ArrayIterator(final MapCollections this$0, final int mOffset) {
            this.this$0 = this$0;
            this.mCanRemove = false;
            this.mOffset = mOffset;
            this.mSize = this$0.colGetSize();
        }
        
        @Override
        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }
        
        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Object colGetEntry = this.this$0.colGetEntry(this.mIndex, this.mOffset);
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
            this.this$0.colRemoveAt(this.mIndex);
        }
    }
    
    final class EntrySet implements Set<Map.Entry<K, V>>
    {
        final MapCollections this$0;
        
        EntrySet(final MapCollections this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public boolean add(final Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Map.Entry<K, V>> collection) {
            final int colGetSize = this.this$0.colGetSize();
            for (final Map.Entry<K, V> entry : collection) {
                this.this$0.colPut(entry.getKey(), entry.getValue());
            }
            return colGetSize != this.this$0.colGetSize();
        }
        
        @Override
        public void clear() {
            this.this$0.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry entry = (Map.Entry)o;
            final int colIndexOfKey = this.this$0.colIndexOfKey(entry.getKey());
            return colIndexOfKey >= 0 && ContainerHelpers.equal(this.this$0.colGetEntry(colIndexOfKey, 1), entry.getValue());
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
            return MapCollections.equalsSetHelper((Set<Object>)this, o);
        }
        
        @Override
        public int hashCode() {
            int i = this.this$0.colGetSize() - 1;
            int n = 0;
            while (i >= 0) {
                final Object colGetEntry = this.this$0.colGetEntry(i, 0);
                final Object colGetEntry2 = this.this$0.colGetEntry(i, 1);
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
            return this.this$0.colGetSize() == 0;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return this.this$0.new MapIterator();
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
            return this.this$0.colGetSize();
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
    
    final class KeySet implements Set<K>
    {
        final MapCollections this$0;
        
        KeySet(final MapCollections this$0) {
            this.this$0 = this$0;
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
            this.this$0.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.this$0.colIndexOfKey(o) >= 0;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            return MapCollections.containsAllHelper(this.this$0.colGetMap(), collection);
        }
        
        @Override
        public boolean equals(final Object o) {
            return MapCollections.equalsSetHelper((Set<Object>)this, o);
        }
        
        @Override
        public int hashCode() {
            int i = this.this$0.colGetSize() - 1;
            int n = 0;
            while (i >= 0) {
                final Object colGetEntry = this.this$0.colGetEntry(i, 0);
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
            return this.this$0.colGetSize() == 0;
        }
        
        @Override
        public Iterator<K> iterator() {
            return (Iterator<K>)this.this$0.new ArrayIterator(0);
        }
        
        @Override
        public boolean remove(final Object o) {
            final int colIndexOfKey = this.this$0.colIndexOfKey(o);
            if (colIndexOfKey >= 0) {
                this.this$0.colRemoveAt(colIndexOfKey);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            return MapCollections.removeAllHelper(this.this$0.colGetMap(), collection);
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            return MapCollections.retainAllHelper(this.this$0.colGetMap(), collection);
        }
        
        @Override
        public int size() {
            return this.this$0.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            return this.this$0.toArrayHelper(0);
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.this$0.toArrayHelper(array, 0);
        }
    }
    
    final class MapIterator implements Iterator<Entry<K, V>>, Entry<K, V>
    {
        int mEnd;
        boolean mEntryValid;
        int mIndex;
        final MapCollections this$0;
        
        MapIterator(final MapCollections this$0) {
            this.this$0 = this$0;
            this.mEntryValid = false;
            this.mEnd = this$0.colGetSize() - 1;
            this.mIndex = -1;
        }
        
        @Override
        public boolean equals(final Object o) {
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
            if (ContainerHelpers.equal(entry.getKey(), this.this$0.colGetEntry(this.mIndex, 0))) {
                b3 = b2;
                if (ContainerHelpers.equal(entry.getValue(), this.this$0.colGetEntry(this.mIndex, 1))) {
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
            return (K)this.this$0.colGetEntry(this.mIndex, 0);
        }
        
        @Override
        public V getValue() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return (V)this.this$0.colGetEntry(this.mIndex, 1);
        }
        
        @Override
        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }
        
        @Override
        public int hashCode() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            final MapCollections this$0 = this.this$0;
            final int mIndex = this.mIndex;
            int hashCode = 0;
            final Object colGetEntry = this$0.colGetEntry(mIndex, 0);
            final Object colGetEntry2 = this.this$0.colGetEntry(this.mIndex, 1);
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
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            ++this.mIndex;
            this.mEntryValid = true;
            return this;
        }
        
        @Override
        public void remove() {
            if (!this.mEntryValid) {
                throw new IllegalStateException();
            }
            this.this$0.colRemoveAt(this.mIndex);
            --this.mIndex;
            --this.mEnd;
            this.mEntryValid = false;
        }
        
        @Override
        public V setValue(final V v) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            return this.this$0.colSetValue(this.mIndex, v);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getKey());
            sb.append("=");
            sb.append(this.getValue());
            return sb.toString();
        }
    }
    
    final class ValuesCollection implements Collection<V>
    {
        final MapCollections this$0;
        
        ValuesCollection(final MapCollections this$0) {
            this.this$0 = this$0;
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
            this.this$0.colClear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.this$0.colIndexOfValue(o) >= 0;
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
            return this.this$0.colGetSize() == 0;
        }
        
        @Override
        public Iterator<V> iterator() {
            return (Iterator<V>)this.this$0.new ArrayIterator(1);
        }
        
        @Override
        public boolean remove(final Object o) {
            final int colIndexOfValue = this.this$0.colIndexOfValue(o);
            if (colIndexOfValue >= 0) {
                this.this$0.colRemoveAt(colIndexOfValue);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            int colGetSize = this.this$0.colGetSize();
            int i = 0;
            boolean b = false;
            while (i < colGetSize) {
                int n = colGetSize;
                int n2 = i;
                if (collection.contains(this.this$0.colGetEntry(i, 1))) {
                    this.this$0.colRemoveAt(i);
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
            int colGetSize = this.this$0.colGetSize();
            int i = 0;
            boolean b = false;
            while (i < colGetSize) {
                int n = colGetSize;
                int n2 = i;
                if (!collection.contains(this.this$0.colGetEntry(i, 1))) {
                    this.this$0.colRemoveAt(i);
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
            return this.this$0.colGetSize();
        }
        
        @Override
        public Object[] toArray() {
            return this.this$0.toArrayHelper(1);
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.this$0.toArrayHelper(array, 1);
        }
    }
}
