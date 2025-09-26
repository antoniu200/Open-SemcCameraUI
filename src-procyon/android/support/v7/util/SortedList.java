// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.util;

import android.support.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Arrays;
import java.lang.reflect.Array;
import android.support.annotation.NonNull;

public class SortedList<T>
{
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mNewDataStart;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;
    
    public SortedList(@NonNull final Class<T> clazz, @NonNull final Callback<T> callback) {
        this(clazz, callback, 10);
    }
    
    public SortedList(@NonNull final Class<T> clazz, @NonNull final Callback<T> mCallback, final int length) {
        this.mTClass = clazz;
        this.mData = (T[])Array.newInstance(clazz, length);
        this.mCallback = mCallback;
        this.mSize = 0;
    }
    
    private int add(final T t, final boolean b) {
        final int index = this.findIndexOf(t, this.mData, 0, this.mSize, 1);
        int n;
        if (index == -1) {
            n = 0;
        }
        else if ((n = index) < this.mSize) {
            final T t2 = this.mData[index];
            n = index;
            if (this.mCallback.areItemsTheSame(t2, t)) {
                if (this.mCallback.areContentsTheSame(t2, t)) {
                    this.mData[index] = t;
                    return index;
                }
                this.mData[index] = t;
                this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(t2, t));
                return index;
            }
        }
        this.addToData(n, t);
        if (b) {
            this.mCallback.onInserted(n, 1);
        }
        return n;
    }
    
    private void addAllInternal(final T[] mData) {
        if (mData.length < 1) {
            return;
        }
        final int sortAndDedup = this.sortAndDedup(mData);
        if (this.mSize == 0) {
            this.mData = mData;
            this.mSize = sortAndDedup;
            this.mCallback.onInserted(0, sortAndDedup);
        }
        else {
            this.merge(mData, sortAndDedup);
        }
    }
    
    private void addToData(final int i, final T t) {
        if (i > this.mSize) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot add item to ");
            sb.append(i);
            sb.append(" because size is ");
            sb.append(this.mSize);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        if (this.mSize == this.mData.length) {
            final Object[] mData = (Object[])Array.newInstance(this.mTClass, this.mData.length + 10);
            System.arraycopy(this.mData, 0, mData, 0, i);
            mData[i] = t;
            System.arraycopy(this.mData, i, mData, i + 1, this.mSize - i);
            this.mData = (T[])mData;
        }
        else {
            System.arraycopy(this.mData, i, this.mData, i + 1, this.mSize - i);
            this.mData[i] = t;
        }
        ++this.mSize;
    }
    
    private T[] copyArray(final T[] array) {
        final Object[] array2 = (Object[])Array.newInstance(this.mTClass, array.length);
        System.arraycopy(array, 0, array2, 0, array.length);
        return (T[])array2;
    }
    
    private int findIndexOf(final T t, final T[] array, int i, int linearEqualitySearch, final int n) {
        while (i < linearEqualitySearch) {
            final int n2 = (i + linearEqualitySearch) / 2;
            final T t2 = array[n2];
            final int compare = this.mCallback.compare(t2, t);
            if (compare < 0) {
                i = n2 + 1;
            }
            else if (compare == 0) {
                if (this.mCallback.areItemsTheSame(t2, t)) {
                    return n2;
                }
                linearEqualitySearch = this.linearEqualitySearch(t, n2, i, linearEqualitySearch);
                if (n == 1) {
                    if ((i = linearEqualitySearch) == -1) {
                        i = n2;
                    }
                    return i;
                }
                return linearEqualitySearch;
            }
            else {
                linearEqualitySearch = n2;
            }
        }
        if (n != 1) {
            i = -1;
        }
        return i;
    }
    
    private int findSameItem(final T t, final T[] array, int i, final int n) {
        while (i < n) {
            if (this.mCallback.areItemsTheSame(array[i], t)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    private int linearEqualitySearch(final T t, int n, final int n2, final int n3) {
        int n4 = n - 1;
        int n5;
        while (true) {
            n5 = n;
            if (n4 < n2) {
                break;
            }
            final T t2 = this.mData[n4];
            if (this.mCallback.compare(t2, t) != 0) {
                n5 = n;
                break;
            }
            if (this.mCallback.areItemsTheSame(t2, t)) {
                return n4;
            }
            --n4;
        }
        T t3;
        do {
            n = n5 + 1;
            if (n < n3) {
                t3 = this.mData[n];
                if (this.mCallback.compare(t3, t) == 0) {
                    n5 = n;
                    continue;
                }
            }
            return -1;
        } while (!this.mCallback.areItemsTheSame(t3, t));
        return n;
    }
    
    private void merge(final T[] array, int n) {
        final boolean b = this.mCallback instanceof BatchedCallback;
        int n2 = 0;
        final boolean b2 = !b;
        if (b2) {
            this.beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mData = (T[])Array.newInstance(this.mTClass, this.mSize + n + 10);
        this.mNewDataStart = 0;
        while (this.mOldDataStart < this.mOldDataSize || n2 < n) {
            if (this.mOldDataStart == this.mOldDataSize) {
                n -= n2;
                System.arraycopy(array, n2, this.mData, this.mNewDataStart, n);
                this.mNewDataStart += n;
                this.mSize += n;
                this.mCallback.onInserted(this.mNewDataStart - n, n);
                break;
            }
            if (n2 == n) {
                n = this.mOldDataSize - this.mOldDataStart;
                System.arraycopy(this.mOldData, this.mOldDataStart, this.mData, this.mNewDataStart, n);
                this.mNewDataStart += n;
                break;
            }
            final T t = this.mOldData[this.mOldDataStart];
            final T t2 = array[n2];
            final int compare = this.mCallback.compare(t, t2);
            if (compare > 0) {
                this.mData[this.mNewDataStart++] = t2;
                ++this.mSize;
                ++n2;
                this.mCallback.onInserted(this.mNewDataStart - 1, 1);
            }
            else if (compare == 0 && this.mCallback.areItemsTheSame(t, t2)) {
                this.mData[this.mNewDataStart++] = t2;
                final int n3 = n2 + 1;
                ++this.mOldDataStart;
                n2 = n3;
                if (this.mCallback.areContentsTheSame(t, t2)) {
                    continue;
                }
                this.mCallback.onChanged(this.mNewDataStart - 1, 1, this.mCallback.getChangePayload(t, t2));
                n2 = n3;
            }
            else {
                this.mData[this.mNewDataStart++] = t;
                ++this.mOldDataStart;
            }
        }
        this.mOldData = null;
        if (b2) {
            this.endBatchedUpdates();
        }
    }
    
    private boolean remove(final T t, final boolean b) {
        final int index = this.findIndexOf(t, this.mData, 0, this.mSize, 2);
        if (index == -1) {
            return false;
        }
        this.removeItemAtIndex(index, b);
        return true;
    }
    
    private void removeItemAtIndex(final int n, final boolean b) {
        System.arraycopy(this.mData, n + 1, this.mData, n, this.mSize - n - 1);
        --this.mSize;
        this.mData[this.mSize] = null;
        if (b) {
            this.mCallback.onRemoved(n, 1);
        }
    }
    
    private void replaceAllInsert(final T t) {
        this.mData[this.mNewDataStart] = t;
        ++this.mNewDataStart;
        ++this.mSize;
        this.mCallback.onInserted(this.mNewDataStart - 1, 1);
    }
    
    private void replaceAllInternal(@NonNull final T[] array) {
        final boolean b = !(this.mCallback instanceof BatchedCallback);
        if (b) {
            this.beginBatchedUpdates();
        }
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mOldData = this.mData;
        this.mNewDataStart = 0;
        final int sortAndDedup = this.sortAndDedup(array);
        this.mData = (T[])Array.newInstance(this.mTClass, sortAndDedup);
        while (this.mNewDataStart < sortAndDedup || this.mOldDataStart < this.mOldDataSize) {
            if (this.mOldDataStart >= this.mOldDataSize) {
                final int mNewDataStart = this.mNewDataStart;
                final int n = sortAndDedup - this.mNewDataStart;
                System.arraycopy(array, mNewDataStart, this.mData, mNewDataStart, n);
                this.mNewDataStart += n;
                this.mSize += n;
                this.mCallback.onInserted(mNewDataStart, n);
                break;
            }
            if (this.mNewDataStart >= sortAndDedup) {
                final int n2 = this.mOldDataSize - this.mOldDataStart;
                this.mSize -= n2;
                this.mCallback.onRemoved(this.mNewDataStart, n2);
                break;
            }
            final T t = this.mOldData[this.mOldDataStart];
            final T t2 = array[this.mNewDataStart];
            final int compare = this.mCallback.compare(t, t2);
            if (compare < 0) {
                this.replaceAllRemove();
            }
            else if (compare > 0) {
                this.replaceAllInsert(t2);
            }
            else if (!this.mCallback.areItemsTheSame(t, t2)) {
                this.replaceAllRemove();
                this.replaceAllInsert(t2);
            }
            else {
                this.mData[this.mNewDataStart] = t2;
                ++this.mOldDataStart;
                ++this.mNewDataStart;
                if (this.mCallback.areContentsTheSame(t, t2)) {
                    continue;
                }
                this.mCallback.onChanged(this.mNewDataStart - 1, 1, this.mCallback.getChangePayload(t, t2));
            }
        }
        this.mOldData = null;
        if (b) {
            this.endBatchedUpdates();
        }
    }
    
    private void replaceAllRemove() {
        --this.mSize;
        ++this.mOldDataStart;
        this.mCallback.onRemoved(this.mNewDataStart, 1);
    }
    
    private int sortAndDedup(@NonNull final T[] a) {
        final int length = a.length;
        int n = 0;
        if (length == 0) {
            return 0;
        }
        Arrays.sort(a, this.mCallback);
        int i = 1;
        int n2 = 1;
        while (i < a.length) {
            final T t = a[i];
            if (this.mCallback.compare(a[n], t) == 0) {
                final int sameItem = this.findSameItem(t, a, n, n2);
                if (sameItem != -1) {
                    a[sameItem] = t;
                }
                else {
                    if (n2 != i) {
                        a[n2] = t;
                    }
                    ++n2;
                }
            }
            else {
                if (n2 != i) {
                    a[n2] = t;
                }
                final int n3 = n2 + 1;
                n = n2;
                n2 = n3;
            }
            ++i;
        }
        return n2;
    }
    
    private void throwIfInMutationOperation() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
        }
    }
    
    public int add(final T t) {
        this.throwIfInMutationOperation();
        return this.add(t, true);
    }
    
    public void addAll(@NonNull final Collection<T> collection) {
        this.addAll(collection.toArray((T[])Array.newInstance(this.mTClass, collection.size())), true);
    }
    
    public void addAll(@NonNull final T... array) {
        this.addAll(array, false);
    }
    
    public void addAll(@NonNull final T[] array, final boolean b) {
        this.throwIfInMutationOperation();
        if (array.length == 0) {
            return;
        }
        if (b) {
            this.addAllInternal(array);
        }
        else {
            this.addAllInternal(this.copyArray(array));
        }
    }
    
    public void beginBatchedUpdates() {
        this.throwIfInMutationOperation();
        if (this.mCallback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(this.mCallback);
        }
        this.mCallback = (Callback)this.mBatchedCallback;
    }
    
    public void clear() {
        this.throwIfInMutationOperation();
        if (this.mSize == 0) {
            return;
        }
        final int mSize = this.mSize;
        Arrays.fill(this.mData, 0, mSize, null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, mSize);
    }
    
    public void endBatchedUpdates() {
        this.throwIfInMutationOperation();
        if (this.mCallback instanceof BatchedCallback) {
            ((BatchedCallback)this.mCallback).dispatchLastEvent();
        }
        if (this.mCallback == this.mBatchedCallback) {
            this.mCallback = (Callback)this.mBatchedCallback.mWrappedCallback;
        }
    }
    
    public T get(final int i) throws IndexOutOfBoundsException {
        if (i >= this.mSize || i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Asked to get item at ");
            sb.append(i);
            sb.append(" but size is ");
            sb.append(this.mSize);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        if (this.mOldData != null && i >= this.mNewDataStart) {
            return this.mOldData[i - this.mNewDataStart + this.mOldDataStart];
        }
        return this.mData[i];
    }
    
    public int indexOf(final T t) {
        if (this.mOldData == null) {
            return this.findIndexOf(t, this.mData, 0, this.mSize, 4);
        }
        final int index = this.findIndexOf(t, this.mData, 0, this.mNewDataStart, 4);
        if (index != -1) {
            return index;
        }
        final int index2 = this.findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
        if (index2 != -1) {
            return index2 - this.mOldDataStart + this.mNewDataStart;
        }
        return -1;
    }
    
    public void recalculatePositionOfItemAt(final int n) {
        this.throwIfInMutationOperation();
        final T value = this.get(n);
        this.removeItemAtIndex(n, false);
        final int add = this.add(value, false);
        if (n != add) {
            this.mCallback.onMoved(n, add);
        }
    }
    
    public boolean remove(final T t) {
        this.throwIfInMutationOperation();
        return this.remove(t, true);
    }
    
    public T removeItemAt(final int n) {
        this.throwIfInMutationOperation();
        final T value = this.get(n);
        this.removeItemAtIndex(n, true);
        return value;
    }
    
    public void replaceAll(@NonNull final Collection<T> collection) {
        this.replaceAll(collection.toArray((T[])Array.newInstance(this.mTClass, collection.size())), true);
    }
    
    public void replaceAll(@NonNull final T... array) {
        this.replaceAll(array, false);
    }
    
    public void replaceAll(@NonNull final T[] array, final boolean b) {
        this.throwIfInMutationOperation();
        if (b) {
            this.replaceAllInternal(array);
        }
        else {
            this.replaceAllInternal(this.copyArray(array));
        }
    }
    
    public int size() {
        return this.mSize;
    }
    
    public void updateItemAt(final int n, final T t) {
        this.throwIfInMutationOperation();
        final T value = this.get(n);
        final boolean b = value == t || !this.mCallback.areContentsTheSame(value, t);
        if (value != t && this.mCallback.compare(value, t) == 0) {
            this.mData[n] = t;
            if (b) {
                this.mCallback.onChanged(n, 1, this.mCallback.getChangePayload(value, t));
            }
            return;
        }
        if (b) {
            this.mCallback.onChanged(n, 1, this.mCallback.getChangePayload(value, t));
        }
        this.removeItemAtIndex(n, false);
        final int add = this.add(t, false);
        if (n != add) {
            this.mCallback.onMoved(n, add);
        }
    }
    
    public static class BatchedCallback<T2> extends Callback<T2>
    {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback;
        final Callback<T2> mWrappedCallback;
        
        public BatchedCallback(final Callback<T2> mWrappedCallback) {
            this.mWrappedCallback = mWrappedCallback;
            this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
        }
        
        @Override
        public boolean areContentsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areContentsTheSame(t2, t3);
        }
        
        @Override
        public boolean areItemsTheSame(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.areItemsTheSame(t2, t3);
        }
        
        @Override
        public int compare(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.compare(t2, t3);
        }
        
        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }
        
        @Nullable
        @Override
        public Object getChangePayload(final T2 t2, final T2 t3) {
            return this.mWrappedCallback.getChangePayload(t2, t3);
        }
        
        @Override
        public void onChanged(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onChanged(n, n2, null);
        }
        
        @Override
        public void onChanged(final int n, final int n2, final Object o) {
            this.mBatchingListUpdateCallback.onChanged(n, n2, o);
        }
        
        @Override
        public void onInserted(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onInserted(n, n2);
        }
        
        @Override
        public void onMoved(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onMoved(n, n2);
        }
        
        @Override
        public void onRemoved(final int n, final int n2) {
            this.mBatchingListUpdateCallback.onRemoved(n, n2);
        }
    }
    
    public abstract static class Callback<T2> implements Comparator<T2>, ListUpdateCallback
    {
        public abstract boolean areContentsTheSame(final T2 p0, final T2 p1);
        
        public abstract boolean areItemsTheSame(final T2 p0, final T2 p1);
        
        @Override
        public abstract int compare(final T2 p0, final T2 p1);
        
        @Nullable
        public Object getChangePayload(final T2 t2, final T2 t3) {
            return null;
        }
        
        public abstract void onChanged(final int p0, final int p1);
        
        @Override
        public void onChanged(final int n, final int n2, final Object o) {
            this.onChanged(n, n2);
        }
    }
}
