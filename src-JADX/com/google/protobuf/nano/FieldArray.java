package com.google.protobuf.nano;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class FieldArray implements Cloneable {
    private static final FieldData DELETED = new FieldData();
    private FieldData[] mData;
    private int[] mFieldNumbers;
    private boolean mGarbage;
    private int mSize;

    private int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            int i2 = (1 << i) - 12;
            if (need <= i2) {
                return i2;
            }
        }
        return need;
    }

    FieldArray() {
        this(10);
    }

    FieldArray(int initialCapacity) {
        this.mGarbage = false;
        int iIdealIntArraySize = idealIntArraySize(initialCapacity);
        this.mFieldNumbers = new int[iIdealIntArraySize];
        this.mData = new FieldData[iIdealIntArraySize];
        this.mSize = 0;
    }

    FieldData get(int fieldNumber) {
        int iBinarySearch = binarySearch(fieldNumber);
        if (iBinarySearch < 0 || this.mData[iBinarySearch] == DELETED) {
            return null;
        }
        return this.mData[iBinarySearch];
    }

    public final int getFieldNumbersSize() {
        return this.mFieldNumbers.length;
    }

    public final int getFieldNumberAt(int index) {
        return this.mFieldNumbers[index];
    }

    void remove(int fieldNumber) {
        int iBinarySearch = binarySearch(fieldNumber);
        if (iBinarySearch < 0 || this.mData[iBinarySearch] == DELETED) {
            return;
        }
        this.mData[iBinarySearch] = DELETED;
        this.mGarbage = true;
    }

    private void gc() {
        int i = this.mSize;
        int[] iArr = this.mFieldNumbers;
        FieldData[] fieldDataArr = this.mData;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            FieldData fieldData = fieldDataArr[i3];
            if (fieldData != DELETED) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    fieldDataArr[i2] = fieldData;
                    fieldDataArr[i3] = null;
                }
                i2++;
            }
        }
        this.mGarbage = false;
        this.mSize = i2;
    }

    void put(int fieldNumber, FieldData data) {
        int iBinarySearch = binarySearch(fieldNumber);
        if (iBinarySearch >= 0) {
            this.mData[iBinarySearch] = data;
            return;
        }
        int i = ~iBinarySearch;
        if (i < this.mSize && this.mData[i] == DELETED) {
            this.mFieldNumbers[i] = fieldNumber;
            this.mData[i] = data;
            return;
        }
        if (this.mGarbage && this.mSize >= this.mFieldNumbers.length) {
            gc();
            i = ~binarySearch(fieldNumber);
        }
        if (this.mSize >= this.mFieldNumbers.length) {
            int iIdealIntArraySize = idealIntArraySize(this.mSize + 1);
            int[] iArr = new int[iIdealIntArraySize];
            FieldData[] fieldDataArr = new FieldData[iIdealIntArraySize];
            System.arraycopy(this.mFieldNumbers, 0, iArr, 0, this.mFieldNumbers.length);
            System.arraycopy(this.mData, 0, fieldDataArr, 0, this.mData.length);
            this.mFieldNumbers = iArr;
            this.mData = fieldDataArr;
        }
        if (this.mSize - i != 0) {
            int i2 = i + 1;
            System.arraycopy(this.mFieldNumbers, i, this.mFieldNumbers, i2, this.mSize - i);
            System.arraycopy(this.mData, i, this.mData, i2, this.mSize - i);
        }
        this.mFieldNumbers[i] = fieldNumber;
        this.mData[i] = data;
        this.mSize++;
    }

    int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    FieldData dataAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mData[index];
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldArray)) {
            return false;
        }
        FieldArray fieldArray = (FieldArray) o;
        if (size() != fieldArray.size()) {
            return false;
        }
        return arrayEquals(this.mFieldNumbers, fieldArray.mFieldNumbers, this.mSize) && arrayEquals(this.mData, fieldArray.mData, this.mSize);
    }

    public int hashCode() {
        if (this.mGarbage) {
            gc();
        }
        int iHashCode = 17;
        for (int i = 0; i < this.mSize; i++) {
            iHashCode = this.mData[i].hashCode() + (31 * ((iHashCode * 31) + this.mFieldNumbers[i]));
        }
        return iHashCode;
    }

    private int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    private int binarySearch(int value) {
        int i = this.mSize - 1;
        int i2 = 0;
        while (i2 <= i) {
            int i3 = (i2 + i) >>> 1;
            int i4 = this.mFieldNumbers[i3];
            if (i4 < value) {
                i2 = i3 + 1;
            } else {
                if (i4 <= value) {
                    return i3;
                }
                i = i3 - 1;
            }
        }
        return ~i2;
    }

    private boolean arrayEquals(int[] a, int[] b, int size) {
        for (int i = 0; i < size; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean arrayEquals(FieldData[] a, FieldData[] b, int size) {
        for (int i = 0; i < size; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public final FieldArray m9clone() {
        int size = size();
        FieldArray fieldArray = new FieldArray(size);
        System.arraycopy(this.mFieldNumbers, 0, fieldArray.mFieldNumbers, 0, size);
        for (int i = 0; i < size; i++) {
            if (this.mData[i] != null) {
                fieldArray.mData[i] = this.mData[i].m10clone();
            }
        }
        fieldArray.mSize = size;
        return fieldArray;
    }
}
