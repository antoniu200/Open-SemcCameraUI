// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.mylzw;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.nio.ByteOrder;

public class MyLzwCompressor
{
    private final ByteOrder byteOrder;
    private final int clearCode;
    private int codeSize;
    private int codes;
    private final boolean earlyLimit;
    private final int eoiCode;
    private final int initialCodeSize;
    private final Listener listener;
    private final Map<ByteArray, Integer> map;
    
    public MyLzwCompressor(final int n, final ByteOrder byteOrder, final boolean b) {
        this(n, byteOrder, b, null);
    }
    
    public MyLzwCompressor(final int initialCodeSize, final ByteOrder byteOrder, final boolean earlyLimit, final Listener listener) {
        this.codes = -1;
        this.map = new HashMap<ByteArray, Integer>();
        this.listener = listener;
        this.byteOrder = byteOrder;
        this.earlyLimit = earlyLimit;
        this.initialCodeSize = initialCodeSize;
        this.clearCode = 1 << initialCodeSize;
        this.eoiCode = this.clearCode + 1;
        if (listener != null) {
            listener.init(this.clearCode, this.eoiCode);
        }
        this.initializeStringTable();
    }
    
    private boolean addTableEntry(final MyBitOutputStream myBitOutputStream, final ByteArray byteArray) throws IOException {
        int n = 1 << this.codeSize;
        if (this.earlyLimit) {
            --n;
        }
        boolean b = false;
        Label_0065: {
            if (this.codes == n) {
                if (this.codeSize >= 12) {
                    this.writeClearCode(myBitOutputStream);
                    this.clearTable();
                    b = true;
                    break Label_0065;
                }
                this.incrementCodeSize();
            }
            b = false;
        }
        if (!b) {
            this.map.put(byteArray, this.codes);
            ++this.codes;
        }
        return b;
    }
    
    private boolean addTableEntry(final MyBitOutputStream myBitOutputStream, final byte[] array, final int n, final int n2) throws IOException {
        return this.addTableEntry(myBitOutputStream, this.arrayToKey(array, n, n2));
    }
    
    private ByteArray arrayToKey(final byte b) {
        return this.arrayToKey(new byte[] { b }, 0, 1);
    }
    
    private ByteArray arrayToKey(final byte[] array, final int n, final int n2) {
        return new ByteArray(array, n, n2);
    }
    
    private void clearTable() {
        this.initializeStringTable();
        this.incrementCodeSize();
    }
    
    private int codeFromString(final byte[] array, final int n, final int n2) throws IOException {
        final Integer n3 = this.map.get(this.arrayToKey(array, n, n2));
        if (n3 == null) {
            throw new IOException("CodeFromString");
        }
        return n3;
    }
    
    private void incrementCodeSize() {
        if (this.codeSize != 12) {
            ++this.codeSize;
        }
    }
    
    private void initializeStringTable() {
        this.codeSize = this.initialCodeSize;
        final int codeSize = this.codeSize;
        this.map.clear();
        int codes = 0;
        while (true) {
            this.codes = codes;
            if (this.codes >= (1 << codeSize) + 2) {
                break;
            }
            if (this.codes != this.clearCode && this.codes != this.eoiCode) {
                this.map.put(this.arrayToKey((byte)this.codes), this.codes);
            }
            codes = this.codes + 1;
        }
    }
    
    private boolean isInTable(final byte[] array, final int n, final int n2) {
        return this.map.containsKey(this.arrayToKey(array, n, n2));
    }
    
    private void writeClearCode(final MyBitOutputStream myBitOutputStream) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(this.clearCode);
        }
        this.writeCode(myBitOutputStream, this.clearCode);
    }
    
    private void writeCode(final MyBitOutputStream myBitOutputStream, final int n) throws IOException {
        myBitOutputStream.writeBits(n, this.codeSize);
    }
    
    private void writeDataCode(final MyBitOutputStream myBitOutputStream, final int n) throws IOException {
        if (this.listener != null) {
            this.listener.dataCode(n);
        }
        this.writeCode(myBitOutputStream, n);
    }
    
    private void writeEoiCode(final MyBitOutputStream myBitOutputStream) throws IOException {
        if (this.listener != null) {
            this.listener.eoiCode(this.eoiCode);
        }
        this.writeCode(myBitOutputStream, this.eoiCode);
    }
    
    public byte[] compress(final byte[] array) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(array.length);
        final MyBitOutputStream myBitOutputStream = new MyBitOutputStream(byteArrayOutputStream, this.byteOrder);
        this.initializeStringTable();
        this.clearTable();
        this.writeClearCode(myBitOutputStream);
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < array.length) {
            final int n3 = n2 + 1;
            if (this.isInTable(array, n, n3)) {
                n2 = n3;
            }
            else {
                this.writeDataCode(myBitOutputStream, this.codeFromString(array, n, n2));
                this.addTableEntry(myBitOutputStream, array, n, n3);
                n2 = 1;
                n = i;
            }
            ++i;
        }
        this.writeDataCode(myBitOutputStream, this.codeFromString(array, n, n2));
        this.writeEoiCode(myBitOutputStream);
        myBitOutputStream.flushCache();
        return byteArrayOutputStream.toByteArray();
    }
    
    private static final class ByteArray
    {
        private final byte[] bytes;
        private final int hash;
        private final int length;
        private final int start;
        
        public ByteArray(final byte[] bytes, final int start, final int length) {
            this.bytes = bytes;
            this.start = start;
            this.length = length;
            int i = 0;
            int hash = length;
            while (i < length) {
                hash = (hash + (hash << 8) ^ (0xFF & bytes[i + start]) ^ i);
                ++i;
            }
            this.hash = hash;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof ByteArray)) {
                return false;
            }
            final ByteArray byteArray = (ByteArray)o;
            if (byteArray.hash != this.hash) {
                return false;
            }
            if (byteArray.length != this.length) {
                return false;
            }
            for (int i = 0; i < this.length; ++i) {
                if (byteArray.bytes[byteArray.start + i] != this.bytes[this.start + i]) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.hash;
        }
    }
    
    public interface Listener
    {
        void clearCode(final int p0);
        
        void dataCode(final int p0);
        
        void eoiCode(final int p0);
        
        void init(final int p0, final int p1);
    }
}
