// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.mylzw;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public final class MyLzwDecompressor
{
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
    
    public MyLzwDecompressor(final int n, final ByteOrder byteOrder) {
        this(n, byteOrder, null);
    }
    
    public MyLzwDecompressor(final int initialCodeSize, final ByteOrder byteOrder, final Listener listener) {
        this.codes = -1;
        this.listener = listener;
        this.byteOrder = byteOrder;
        this.initialCodeSize = initialCodeSize;
        this.table = new byte[4096][];
        this.clearCode = 1 << initialCodeSize;
        this.eoiCode = this.clearCode + 1;
        if (listener != null) {
            listener.init(this.clearCode, this.eoiCode);
        }
        this.initializeTable();
    }
    
    private void addStringToTable(final byte[] array) throws IOException {
        if (this.codes < 1 << this.codeSize) {
            this.table[this.codes] = array;
            ++this.codes;
            this.checkCodeSize();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("AddStringToTable: codes: ");
        sb.append(this.codes);
        sb.append(" code_size: ");
        sb.append(this.codeSize);
        throw new IOException(sb.toString());
    }
    
    private byte[] appendBytes(final byte[] array, final byte b) {
        final byte[] array2 = new byte[array.length + 1];
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[array2.length - 1] = b;
        return array2;
    }
    
    private void checkCodeSize() {
        int n = 1 << this.codeSize;
        if (this.tiffLZWMode) {
            --n;
        }
        if (this.codes == n) {
            this.incrementCodeSize();
        }
    }
    
    private void clearTable() {
        this.codes = (1 << this.initialCodeSize) + 2;
        this.codeSize = this.initialCodeSize;
        this.incrementCodeSize();
    }
    
    private byte firstChar(final byte[] array) {
        return array[0];
    }
    
    private int getNextCode(final MyBitInputStream myBitInputStream) throws IOException {
        final int bits = myBitInputStream.readBits(this.codeSize);
        if (this.listener != null) {
            this.listener.code(bits);
        }
        return bits;
    }
    
    private void incrementCodeSize() {
        if (this.codeSize != 12) {
            ++this.codeSize;
        }
    }
    
    private void initializeTable() {
        this.codeSize = this.initialCodeSize;
        for (int codeSize = this.codeSize, i = 0; i < 1 << codeSize + 2; ++i) {
            this.table[i] = new byte[] { (byte)i };
        }
    }
    
    private boolean isInTable(final int n) {
        return n < this.codes;
    }
    
    private byte[] stringFromCode(final int i) throws IOException {
        if (i < this.codes && i >= 0) {
            return this.table[i];
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad Code: ");
        sb.append(i);
        sb.append(" codes: ");
        sb.append(this.codes);
        sb.append(" code_size: ");
        sb.append(this.codeSize);
        sb.append(", table: ");
        sb.append(this.table.length);
        throw new IOException(sb.toString());
    }
    
    private void writeToResult(final OutputStream outputStream, final byte[] b) throws IOException {
        outputStream.write(b);
        this.written += b.length;
    }
    
    public byte[] decompress(final InputStream inputStream, final int size) throws IOException {
        final MyBitInputStream myBitInputStream = new MyBitInputStream(inputStream, this.byteOrder);
        if (this.tiffLZWMode) {
            myBitInputStream.setTiffLZWMode();
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(size);
        this.clearTable();
        int nextCode = -1;
        do {
            final int nextCode2 = this.getNextCode(myBitInputStream);
            if (nextCode2 == this.eoiCode) {
                break;
            }
            if (nextCode2 == this.clearCode) {
                this.clearTable();
                if (this.written >= size) {
                    break;
                }
                nextCode = this.getNextCode(myBitInputStream);
                if (nextCode == this.eoiCode) {
                    break;
                }
                this.writeToResult(byteArrayOutputStream, this.stringFromCode(nextCode));
            }
            else {
                if (this.isInTable(nextCode2)) {
                    this.writeToResult(byteArrayOutputStream, this.stringFromCode(nextCode2));
                    this.addStringToTable(this.appendBytes(this.stringFromCode(nextCode), this.firstChar(this.stringFromCode(nextCode2))));
                }
                else {
                    final byte[] appendBytes = this.appendBytes(this.stringFromCode(nextCode), this.firstChar(this.stringFromCode(nextCode)));
                    this.writeToResult(byteArrayOutputStream, appendBytes);
                    this.addStringToTable(appendBytes);
                }
                nextCode = nextCode2;
            }
        } while (this.written < size);
        return byteArrayOutputStream.toByteArray();
    }
    
    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }
    
    public interface Listener
    {
        void code(final int p0);
        
        void init(final int p0, final int p1);
    }
}
