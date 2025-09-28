package org.apache.commons.imaging.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MyLzwCompressor {
    private final ByteOrder byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final boolean earlyLimit;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final Map<ByteArray, Integer> map;

    public interface Listener {
        void clearCode(int i);

        void dataCode(int i);

        void eoiCode(int i);

        void init(int i, int i2);
    }

    public MyLzwCompressor(int i, ByteOrder byteOrder, boolean z) {
        this(i, byteOrder, z, null);
    }

    public MyLzwCompressor(int i, ByteOrder byteOrder, boolean z, Listener listener) {
        this.codes = -1;
        this.map = new HashMap();
        this.listener = listener;
        this.byteOrder = byteOrder;
        this.earlyLimit = z;
        this.initialCodeSize = i;
        this.clearCode = 1 << i;
        this.eoiCode = this.clearCode + 1;
        if (listener != null) {
            listener.init(this.clearCode, this.eoiCode);
        }
        initializeStringTable();
    }

    private void initializeStringTable() {
        this.codeSize = this.initialCodeSize;
        int i = (1 << this.codeSize) + 2;
        this.map.clear();
        int i2 = 0;
        while (true) {
            this.codes = i2;
            if (this.codes >= i) {
                return;
            }
            if (this.codes != this.clearCode && this.codes != this.eoiCode) {
                this.map.put(arrayToKey((byte) this.codes), Integer.valueOf(this.codes));
            }
            i2 = this.codes + 1;
        }
    }

    private void clearTable() {
        initializeStringTable();
        incrementCodeSize();
    }

    private void incrementCodeSize() {
        if (this.codeSize != 12) {
            this.codeSize++;
        }
    }

    private ByteArray arrayToKey(byte b) {
        return arrayToKey(new byte[]{b}, 0, 1);
    }

    private static final class ByteArray {
        private final byte[] bytes;
        private final int hash;
        private final int length;
        private final int start;

        public ByteArray(byte[] bArr, int i, int i2) {
            this.bytes = bArr;
            this.start = i;
            this.length = i2;
            int i3 = i2;
            for (int i4 = 0; i4 < i2; i4++) {
                i3 = ((i3 + (i3 << 8)) ^ (255 & bArr[i4 + i])) ^ i4;
            }
            this.hash = i3;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ByteArray)) {
                return false;
            }
            ByteArray byteArray = (ByteArray) obj;
            if (byteArray.hash != this.hash || byteArray.length != this.length) {
                return false;
            }
            for (int i = 0; i < this.length; i++) {
                if (byteArray.bytes[byteArray.start + i] != this.bytes[this.start + i]) {
                    return false;
                }
            }
            return true;
        }
    }

    private ByteArray arrayToKey(byte[] bArr, int i, int i2) {
        return new ByteArray(bArr, i, i2);
    }

    private void writeDataCode(MyBitOutputStream myBitOutputStream, int i) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(i);
        }
        writeCode(myBitOutputStream, i);
    }

    private void writeClearCode(MyBitOutputStream myBitOutputStream) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(this.clearCode);
        }
        writeCode(myBitOutputStream, this.clearCode);
    }

    private void writeEoiCode(MyBitOutputStream myBitOutputStream) throws IOException {
        if (this.listener != null) {
            this.listener.eoiCode(this.eoiCode);
        }
        writeCode(myBitOutputStream, this.eoiCode);
    }

    private void writeCode(MyBitOutputStream myBitOutputStream, int i) throws IOException {
        myBitOutputStream.writeBits(i, this.codeSize);
    }

    private boolean isInTable(byte[] bArr, int i, int i2) {
        return this.map.containsKey(arrayToKey(bArr, i, i2));
    }

    private int codeFromString(byte[] bArr, int i, int i2) throws IOException {
        Integer num = this.map.get(arrayToKey(bArr, i, i2));
        if (num == null) {
            throw new IOException("CodeFromString");
        }
        return num.intValue();
    }

    private boolean addTableEntry(MyBitOutputStream myBitOutputStream, byte[] bArr, int i, int i2) throws IOException {
        return addTableEntry(myBitOutputStream, arrayToKey(bArr, i, i2));
    }

    private boolean addTableEntry(MyBitOutputStream myBitOutputStream, ByteArray byteArray) throws IOException {
        boolean z;
        int i = 1 << this.codeSize;
        if (this.earlyLimit) {
            i--;
        }
        if (this.codes != i) {
            z = false;
        } else if (this.codeSize < 12) {
            incrementCodeSize();
            z = false;
        } else {
            writeClearCode(myBitOutputStream);
            clearTable();
            z = true;
        }
        if (!z) {
            this.map.put(byteArray, Integer.valueOf(this.codes));
            this.codes++;
        }
        return z;
    }

    public byte[] compress(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        MyBitOutputStream myBitOutputStream = new MyBitOutputStream(byteArrayOutputStream, this.byteOrder);
        initializeStringTable();
        clearTable();
        writeClearCode(myBitOutputStream);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            int i4 = i2 + 1;
            if (isInTable(bArr, i, i4)) {
                i2 = i4;
            } else {
                writeDataCode(myBitOutputStream, codeFromString(bArr, i, i2));
                addTableEntry(myBitOutputStream, bArr, i, i4);
                i2 = 1;
                i = i3;
            }
        }
        writeDataCode(myBitOutputStream, codeFromString(bArr, i, i2));
        writeEoiCode(myBitOutputStream);
        myBitOutputStream.flushCache();
        return byteArrayOutputStream.toByteArray();
    }
}
