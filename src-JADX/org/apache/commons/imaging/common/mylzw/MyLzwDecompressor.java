package org.apache.commons.imaging.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class MyLzwDecompressor {
    private static final int MAX_TABLE_SIZE = 4096;
    private final ByteOrder byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final byte[][] table;
    private boolean tiffLZWMode;
    private int written;

    public interface Listener {
        void code(int i);

        void init(int i, int i2);
    }

    public MyLzwDecompressor(int i, ByteOrder byteOrder) {
        this(i, byteOrder, null);
    }

    public MyLzwDecompressor(int i, ByteOrder byteOrder, Listener listener) {
        this.codes = -1;
        this.listener = listener;
        this.byteOrder = byteOrder;
        this.initialCodeSize = i;
        this.table = new byte[4096][];
        this.clearCode = 1 << i;
        this.eoiCode = this.clearCode + 1;
        if (listener != null) {
            listener.init(this.clearCode, this.eoiCode);
        }
        initializeTable();
    }

    private void initializeTable() {
        this.codeSize = this.initialCodeSize;
        int i = 1 << (this.codeSize + 2);
        for (int i2 = 0; i2 < i; i2++) {
            this.table[i2] = new byte[]{(byte) i2};
        }
    }

    private void clearTable() {
        this.codes = (1 << this.initialCodeSize) + 2;
        this.codeSize = this.initialCodeSize;
        incrementCodeSize();
    }

    private int getNextCode(MyBitInputStream myBitInputStream) throws IOException {
        int bits = myBitInputStream.readBits(this.codeSize);
        if (this.listener != null) {
            this.listener.code(bits);
        }
        return bits;
    }

    private byte[] stringFromCode(int i) throws IOException {
        if (i >= this.codes || i < 0) {
            throw new IOException("Bad Code: " + i + " codes: " + this.codes + " code_size: " + this.codeSize + ", table: " + this.table.length);
        }
        return this.table[i];
    }

    private boolean isInTable(int i) {
        return i < this.codes;
    }

    private byte firstChar(byte[] bArr) {
        return bArr[0];
    }

    private void addStringToTable(byte[] bArr) throws IOException {
        if (this.codes < (1 << this.codeSize)) {
            this.table[this.codes] = bArr;
            this.codes++;
            checkCodeSize();
        } else {
            throw new IOException("AddStringToTable: codes: " + this.codes + " code_size: " + this.codeSize);
        }
    }

    private byte[] appendBytes(byte[] bArr, byte b) {
        byte[] bArr2 = new byte[bArr.length + 1];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        bArr2[bArr2.length - 1] = b;
        return bArr2;
    }

    private void writeToResult(OutputStream outputStream, byte[] bArr) throws IOException {
        outputStream.write(bArr);
        this.written += bArr.length;
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }

    public byte[] decompress(InputStream inputStream, int i) throws IOException {
        MyBitInputStream myBitInputStream = new MyBitInputStream(inputStream, this.byteOrder);
        if (this.tiffLZWMode) {
            myBitInputStream.setTiffLZWMode();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        clearTable();
        int nextCode = -1;
        do {
            int nextCode2 = getNextCode(myBitInputStream);
            if (nextCode2 == this.eoiCode) {
                break;
            }
            if (nextCode2 == this.clearCode) {
                clearTable();
                if (this.written >= i || (nextCode = getNextCode(myBitInputStream)) == this.eoiCode) {
                    break;
                }
                writeToResult(byteArrayOutputStream, stringFromCode(nextCode));
            } else {
                if (isInTable(nextCode2)) {
                    writeToResult(byteArrayOutputStream, stringFromCode(nextCode2));
                    addStringToTable(appendBytes(stringFromCode(nextCode), firstChar(stringFromCode(nextCode2))));
                } else {
                    byte[] bArrAppendBytes = appendBytes(stringFromCode(nextCode), firstChar(stringFromCode(nextCode)));
                    writeToResult(byteArrayOutputStream, bArrAppendBytes);
                    addStringToTable(bArrAppendBytes);
                }
                nextCode = nextCode2;
            }
        } while (this.written < i);
        return byteArrayOutputStream.toByteArray();
    }

    private void checkCodeSize() {
        int i = 1 << this.codeSize;
        if (this.tiffLZWMode) {
            i--;
        }
        if (this.codes == i) {
            incrementCodeSize();
        }
    }

    private void incrementCodeSize() {
        if (this.codeSize != 12) {
            this.codeSize++;
        }
    }
}
