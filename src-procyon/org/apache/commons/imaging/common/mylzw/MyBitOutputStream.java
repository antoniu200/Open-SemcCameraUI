// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.mylzw;

import java.io.IOException;
import java.nio.ByteOrder;
import java.io.OutputStream;

public class MyBitOutputStream extends OutputStream
{
    private int bitCache;
    private int bitsInCache;
    private final ByteOrder byteOrder;
    private int bytesWritten;
    private final OutputStream os;
    
    public MyBitOutputStream(final OutputStream os, final ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        this.os = os;
    }
    
    private void actualWrite(final int n) throws IOException {
        this.os.write(n);
        ++this.bytesWritten;
    }
    
    public void flushCache() throws IOException {
        if (this.bitsInCache > 0) {
            final int n = (1 << this.bitsInCache) - 1 & this.bitCache;
            if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                this.os.write(n << 8 - this.bitsInCache);
            }
            else {
                this.os.write(n);
            }
        }
        this.bitsInCache = 0;
        this.bitCache = 0;
    }
    
    public int getBytesWritten() {
        final int bytesWritten = this.bytesWritten;
        int n;
        if (this.bitsInCache > 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        return bytesWritten + n;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.writeBits(n, 8);
    }
    
    public void writeBits(int bitCache, final int n) throws IOException {
        final int n2 = bitCache & (1 << n) - 1;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.bitCache = (n2 | this.bitCache << n);
        }
        else {
            bitCache = this.bitCache;
            this.bitCache = (n2 << this.bitsInCache | bitCache);
        }
        this.bitsInCache += n;
        while (this.bitsInCache >= 8) {
            if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                this.actualWrite(this.bitCache >> this.bitsInCache - 8 & 0xFF);
                this.bitsInCache -= 8;
            }
            else {
                this.actualWrite(this.bitCache & 0xFF);
                this.bitCache >>= 8;
                this.bitsInCache -= 8;
            }
            this.bitCache &= (1 << this.bitsInCache) - 1;
        }
    }
}
