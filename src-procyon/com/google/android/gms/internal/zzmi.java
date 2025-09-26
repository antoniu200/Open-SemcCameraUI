// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Map;

public class zzmi<K, V>
{
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;
    
    public zzmi() {
        this.mHashes = zzmf.EMPTY_INTS;
        this.mArray = zzmf.EMPTY_OBJECTS;
        this.mSize = 0;
    }
    
    public zzmi(final int n) {
        if (n == 0) {
            this.mHashes = zzmf.EMPTY_INTS;
            this.mArray = zzmf.EMPTY_OBJECTS;
        }
        else {
            this.zzbO(n);
        }
        this.mSize = 0;
    }
    
    private static void zza(final int[] array, final Object[] array2, int i) {
        if (array.length == 8) {
            synchronized (zzme.class) {
                if (zzmi.mTwiceBaseCacheSize < 10) {
                    array2[0] = zzmi.mTwiceBaseCache;
                    array2[1] = array;
                    for (i = (i << 1) - 1; i >= 2; --i) {
                        array2[i] = null;
                    }
                    zzmi.mTwiceBaseCache = array2;
                    ++zzmi.mTwiceBaseCacheSize;
                }
                return;
            }
        }
        if (array.length == 4) {
            synchronized (zzme.class) {
                if (zzmi.mBaseCacheSize < 10) {
                    array2[0] = zzmi.mBaseCache;
                    array2[1] = array;
                    for (i = (i << 1) - 1; i >= 2; --i) {
                        array2[i] = null;
                    }
                    zzmi.mBaseCache = array2;
                    ++zzmi.mBaseCacheSize;
                }
            }
        }
    }
    
    private void zzbO(final int n) {
        Label_0149: {
            if (n == 8) {
                synchronized (zzme.class) {
                    if (zzmi.mTwiceBaseCache != null) {
                        final Object[] mTwiceBaseCache = zzmi.mTwiceBaseCache;
                        this.mArray = mTwiceBaseCache;
                        zzmi.mTwiceBaseCache = (Object[])mTwiceBaseCache[0];
                        this.mHashes = (int[])mTwiceBaseCache[1];
                        mTwiceBaseCache[0] = (mTwiceBaseCache[1] = null);
                        --zzmi.mTwiceBaseCacheSize;
                        return;
                    }
                    break Label_0149;
                }
            }
            if (n == 4) {
                synchronized (zzme.class) {
                    if (zzmi.mBaseCache != null) {
                        final Object[] mBaseCache = zzmi.mBaseCache;
                        this.mArray = mBaseCache;
                        zzmi.mBaseCache = (Object[])mBaseCache[0];
                        this.mHashes = (int[])mBaseCache[1];
                        mBaseCache[0] = (mBaseCache[1] = null);
                        --zzmi.mBaseCacheSize;
                        return;
                    }
                }
            }
        }
        this.mHashes = new int[n];
        this.mArray = new Object[n << 1];
    }
    
    public void clear() {
        if (this.mSize != 0) {
            zza(this.mHashes, this.mArray, this.mSize);
            this.mHashes = zzmf.EMPTY_INTS;
            this.mArray = zzmf.EMPTY_OBJECTS;
            this.mSize = 0;
        }
    }
    
    public boolean containsKey(final Object o) {
        if (o == null) {
            if (this.indexOfNull() < 0) {
                return false;
            }
        }
        else if (this.indexOf(o, o.hashCode()) < 0) {
            return false;
        }
        return true;
    }
    
    public boolean containsValue(final Object o) {
        return this.indexOfValue(o) >= 0;
    }
    
    public void ensureCapacity(final int n) {
        if (this.mHashes.length < n) {
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.zzbO(n);
            if (this.mSize > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(mArray, 0, this.mArray, 0, this.mSize << 1);
            }
            zza(mHashes, mArray, this.mSize);
        }
    }
    
    @Override
    public boolean equals(Object key) {
        if (this == key) {
            return true;
        }
        if (!(key instanceof Map)) {
            return false;
        }
        final Map map = (Map)key;
        if (this.size() != map.size()) {
            return false;
        }
        int i = 0;
        try {
            while (i < this.mSize) {
                key = this.keyAt(i);
                final V value = this.valueAt(i);
                final Object value2 = map.get(key);
                if (value == null) {
                    if (value2 != null || !map.containsKey(key)) {
                        return false;
                    }
                }
                else if (!value.equals(value2)) {
                    return false;
                }
                ++i;
            }
            return true;
        }
        catch (final NullPointerException | ClassCastException ex) {
            return false;
        }
    }
    
    public V get(final Object o) {
        int n;
        if (o == null) {
            n = this.indexOfNull();
        }
        else {
            n = this.indexOf(o, o.hashCode());
        }
        if (n >= 0) {
            return (V)this.mArray[(n << 1) + 1];
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        final int[] mHashes = this.mHashes;
        final Object[] mArray = this.mArray;
        final int mSize = this.mSize;
        int n = 1;
        int i = 0;
        int n2 = 0;
        while (i < mSize) {
            final Object o = mArray[n];
            final int n3 = mHashes[i];
            int hashCode;
            if (o == null) {
                hashCode = 0;
            }
            else {
                hashCode = o.hashCode();
            }
            n2 += (hashCode ^ n3);
            ++i;
            n += 2;
        }
        return n2;
    }
    
    int indexOf(final Object o, final int n) {
        final int mSize = this.mSize;
        if (mSize == 0) {
            return -1;
        }
        int binarySearch = zzmf.binarySearch(this.mHashes, mSize, n);
        if (binarySearch < 0) {
            return binarySearch;
        }
        if (o.equals(this.mArray[binarySearch << 1])) {
            return binarySearch;
        }
        int n2;
        for (n2 = binarySearch + 1; n2 < mSize && this.mHashes[n2] == n; ++n2) {
            if (o.equals(this.mArray[n2 << 1])) {
                return n2;
            }
        }
        --binarySearch;
        while (binarySearch >= 0 && this.mHashes[binarySearch] == n) {
            if (o.equals(this.mArray[binarySearch << 1])) {
                return binarySearch;
            }
            --binarySearch;
        }
        return ~n2;
    }
    
    int indexOfNull() {
        final int mSize = this.mSize;
        if (mSize == 0) {
            return -1;
        }
        int binarySearch = zzmf.binarySearch(this.mHashes, mSize, 0);
        if (binarySearch < 0) {
            return binarySearch;
        }
        if (this.mArray[binarySearch << 1] == null) {
            return binarySearch;
        }
        int n;
        for (n = binarySearch + 1; n < mSize && this.mHashes[n] == 0; ++n) {
            if (this.mArray[n << 1] == null) {
                return n;
            }
        }
        --binarySearch;
        while (binarySearch >= 0 && this.mHashes[binarySearch] == 0) {
            if (this.mArray[binarySearch << 1] == null) {
                return binarySearch;
            }
            --binarySearch;
        }
        return ~n;
    }
    
    int indexOfValue(final Object o) {
        final int n = this.mSize * 2;
        final Object[] mArray = this.mArray;
        if (o == null) {
            for (int i = 1; i < n; i += 2) {
                if (mArray[i] == null) {
                    return i >> 1;
                }
            }
        }
        else {
            for (int j = 1; j < n; j += 2) {
                if (o.equals(mArray[j])) {
                    return j >> 1;
                }
            }
        }
        return -1;
    }
    
    public boolean isEmpty() {
        return this.mSize <= 0;
    }
    
    public K keyAt(final int n) {
        return (K)this.mArray[n << 1];
    }
    
    public V put(final K k, final V v) {
        int n;
        int hashCode;
        if (k == null) {
            n = this.indexOfNull();
            hashCode = 0;
        }
        else {
            hashCode = k.hashCode();
            n = this.indexOf(k, hashCode);
        }
        if (n >= 0) {
            final int n2 = (n << 1) + 1;
            final Object o = this.mArray[n2];
            this.mArray[n2] = v;
            return (V)o;
        }
        final int n3 = ~n;
        if (this.mSize >= this.mHashes.length) {
            final int mSize = this.mSize;
            int n4 = 4;
            if (mSize >= 8) {
                n4 = (this.mSize >> 1) + this.mSize;
            }
            else if (this.mSize >= 4) {
                n4 = 8;
            }
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.zzbO(n4);
            if (this.mHashes.length > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, mHashes.length);
                System.arraycopy(mArray, 0, this.mArray, 0, mArray.length);
            }
            zza(mHashes, mArray, this.mSize);
        }
        if (n3 < this.mSize) {
            final int[] mHashes2 = this.mHashes;
            final int[] mHashes3 = this.mHashes;
            final int n5 = n3 + 1;
            System.arraycopy(mHashes2, n3, mHashes3, n5, this.mSize - n3);
            System.arraycopy(this.mArray, n3 << 1, this.mArray, n5 << 1, this.mSize - n3 << 1);
        }
        this.mHashes[n3] = hashCode;
        final Object[] mArray2 = this.mArray;
        final int n6 = n3 << 1;
        mArray2[n6] = k;
        this.mArray[n6 + 1] = v;
        ++this.mSize;
        return null;
    }
    
    public V remove(final Object o) {
        int n;
        if (o == null) {
            n = this.indexOfNull();
        }
        else {
            n = this.indexOf(o, o.hashCode());
        }
        if (n >= 0) {
            return this.removeAt(n);
        }
        return null;
    }
    
    public V removeAt(final int n) {
        final Object[] mArray = this.mArray;
        final int n2 = n << 1;
        final Object o = mArray[n2 + 1];
        if (this.mSize <= 1) {
            zza(this.mHashes, this.mArray, this.mSize);
            this.mHashes = zzmf.EMPTY_INTS;
            this.mArray = zzmf.EMPTY_OBJECTS;
            this.mSize = 0;
            return (V)o;
        }
        final int length = this.mHashes.length;
        int n3 = 8;
        if (length > 8 && this.mSize < this.mHashes.length / 3) {
            if (this.mSize > 8) {
                n3 = (this.mSize >> 1) + this.mSize;
            }
            final int[] mHashes = this.mHashes;
            final Object[] mArray2 = this.mArray;
            this.zzbO(n3);
            --this.mSize;
            if (n > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, n);
                System.arraycopy(mArray2, 0, this.mArray, 0, n2);
            }
            if (n < this.mSize) {
                final int n4 = n + 1;
                System.arraycopy(mHashes, n4, this.mHashes, n, this.mSize - n);
                System.arraycopy(mArray2, n4 << 1, this.mArray, n2, this.mSize - n << 1);
                return (V)o;
            }
        }
        else {
            --this.mSize;
            if (n < this.mSize) {
                final int[] mHashes2 = this.mHashes;
                final int n5 = n + 1;
                System.arraycopy(mHashes2, n5, this.mHashes, n, this.mSize - n);
                System.arraycopy(this.mArray, n5 << 1, this.mArray, n2, this.mSize - n << 1);
            }
            this.mArray[this.mSize << 1] = null;
            this.mArray[(this.mSize << 1) + 1] = null;
        }
        return (V)o;
    }
    
    public V setValueAt(int n, final V v) {
        n = (n << 1) + 1;
        final Object o = this.mArray[n];
        this.mArray[n] = v;
        return (V)o;
    }
    
    public int size() {
        return this.mSize;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            final K key = this.keyAt(i);
            if (key != this) {
                sb.append(key);
            }
            else {
                sb.append("(this Map)");
            }
            sb.append('=');
            final V value = this.valueAt(i);
            if (value != this) {
                sb.append(value);
            }
            else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public V valueAt(final int n) {
        return (V)this.mArray[(n << 1) + 1];
    }
}
