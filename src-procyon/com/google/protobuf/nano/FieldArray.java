// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

public final class FieldArray implements Cloneable
{
    private static final FieldData DELETED;
    private FieldData[] mData;
    private int[] mFieldNumbers;
    private boolean mGarbage;
    private int mSize;
    
    static {
        DELETED = new FieldData();
    }
    
    FieldArray() {
        this(10);
    }
    
    FieldArray(int idealIntArraySize) {
        this.mGarbage = false;
        idealIntArraySize = this.idealIntArraySize(idealIntArraySize);
        this.mFieldNumbers = new int[idealIntArraySize];
        this.mData = new FieldData[idealIntArraySize];
        this.mSize = 0;
    }
    
    private boolean arrayEquals(final int[] array, final int[] array2, final int n) {
        for (int i = 0; i < n; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean arrayEquals(final FieldData[] array, final FieldData[] array2, final int n) {
        for (int i = 0; i < n; ++i) {
            if (!array[i].equals(array2[i])) {
                return false;
            }
        }
        return true;
    }
    
    private int binarySearch(final int n) {
        int n2 = this.mSize - 1;
        int i = 0;
        while (i <= n2) {
            final int n3 = i + n2 >>> 1;
            final int n4 = this.mFieldNumbers[n3];
            if (n4 < n) {
                i = n3 + 1;
            }
            else {
                if (n4 <= n) {
                    return n3;
                }
                n2 = n3 - 1;
            }
        }
        return ~i;
    }
    
    private void gc() {
        final int mSize = this.mSize;
        final int[] mFieldNumbers = this.mFieldNumbers;
        final FieldData[] mData = this.mData;
        int i = 0;
        int mSize2 = 0;
        while (i < mSize) {
            final FieldData fieldData = mData[i];
            int n = mSize2;
            if (fieldData != FieldArray.DELETED) {
                if (i != mSize2) {
                    mFieldNumbers[mSize2] = mFieldNumbers[i];
                    mData[mSize2] = fieldData;
                    mData[i] = null;
                }
                n = mSize2 + 1;
            }
            ++i;
            mSize2 = n;
        }
        this.mGarbage = false;
        this.mSize = mSize2;
    }
    
    private int idealByteArraySize(final int n) {
        for (int i = 4; i < 32; ++i) {
            final int n2 = (1 << i) - 12;
            if (n <= n2) {
                return n2;
            }
        }
        return n;
    }
    
    private int idealIntArraySize(final int n) {
        return this.idealByteArraySize(n * 4) / 4;
    }
    
    public final FieldArray clone() {
        final int size = this.size();
        final FieldArray fieldArray = new FieldArray(size);
        final int[] mFieldNumbers = this.mFieldNumbers;
        final int[] mFieldNumbers2 = fieldArray.mFieldNumbers;
        int i = 0;
        System.arraycopy(mFieldNumbers, 0, mFieldNumbers2, 0, size);
        while (i < size) {
            if (this.mData[i] != null) {
                fieldArray.mData[i] = this.mData[i].clone();
            }
            ++i;
        }
        fieldArray.mSize = size;
        return fieldArray;
    }
    
    FieldData dataAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mData[n];
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldArray)) {
            return false;
        }
        final FieldArray fieldArray = (FieldArray)o;
        if (this.size() != fieldArray.size()) {
            return false;
        }
        if (!this.arrayEquals(this.mFieldNumbers, fieldArray.mFieldNumbers, this.mSize) || !this.arrayEquals(this.mData, fieldArray.mData, this.mSize)) {
            b = false;
        }
        return b;
    }
    
    FieldData get(int binarySearch) {
        binarySearch = this.binarySearch(binarySearch);
        if (binarySearch >= 0 && this.mData[binarySearch] != FieldArray.DELETED) {
            return this.mData[binarySearch];
        }
        return null;
    }
    
    public final int getFieldNumberAt(final int n) {
        return this.mFieldNumbers[n];
    }
    
    public final int getFieldNumbersSize() {
        return this.mFieldNumbers.length;
    }
    
    @Override
    public int hashCode() {
        if (this.mGarbage) {
            this.gc();
        }
        int n = 17;
        for (int i = 0; i < this.mSize; ++i) {
            n = this.mData[i].hashCode() + 31 * (n * 31 + this.mFieldNumbers[i]);
        }
        return n;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    void put(final int n, final FieldData fieldData) {
        final int binarySearch = this.binarySearch(n);
        if (binarySearch >= 0) {
            this.mData[binarySearch] = fieldData;
        }
        else {
            final int n2 = ~binarySearch;
            if (n2 < this.mSize && this.mData[n2] == FieldArray.DELETED) {
                this.mFieldNumbers[n2] = n;
                this.mData[n2] = fieldData;
                return;
            }
            int n3 = n2;
            if (this.mGarbage) {
                n3 = n2;
                if (this.mSize >= this.mFieldNumbers.length) {
                    this.gc();
                    n3 = ~this.binarySearch(n);
                }
            }
            if (this.mSize >= this.mFieldNumbers.length) {
                final int idealIntArraySize = this.idealIntArraySize(this.mSize + 1);
                final int[] mFieldNumbers = new int[idealIntArraySize];
                final FieldData[] mData = new FieldData[idealIntArraySize];
                System.arraycopy(this.mFieldNumbers, 0, mFieldNumbers, 0, this.mFieldNumbers.length);
                System.arraycopy(this.mData, 0, mData, 0, this.mData.length);
                this.mFieldNumbers = mFieldNumbers;
                this.mData = mData;
            }
            if (this.mSize - n3 != 0) {
                final int[] mFieldNumbers2 = this.mFieldNumbers;
                final int[] mFieldNumbers3 = this.mFieldNumbers;
                final int n4 = n3 + 1;
                System.arraycopy(mFieldNumbers2, n3, mFieldNumbers3, n4, this.mSize - n3);
                System.arraycopy(this.mData, n3, this.mData, n4, this.mSize - n3);
            }
            this.mFieldNumbers[n3] = n;
            this.mData[n3] = fieldData;
            ++this.mSize;
        }
    }
    
    void remove(int binarySearch) {
        binarySearch = this.binarySearch(binarySearch);
        if (binarySearch >= 0 && this.mData[binarySearch] != FieldArray.DELETED) {
            this.mData[binarySearch] = FieldArray.DELETED;
            this.mGarbage = true;
        }
    }
    
    int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }
}
