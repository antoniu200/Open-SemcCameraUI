// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.mylzw;

import java.io.IOException;
import java.nio.ByteOrder;
import java.io.InputStream;

public class MyBitInputStream extends InputStream
{
    private int bitCache;
    private int bitsInCache;
    private final ByteOrder byteOrder;
    private long bytesRead;
    private final InputStream is;
    private boolean tiffLZWMode;
    
    public MyBitInputStream(final InputStream is, final ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        this.is = is;
    }
    
    public void flushCache() {
        this.bitsInCache = 0;
        this.bitCache = 0;
    }
    
    public long getBytesRead() {
        return this.bytesRead;
    }
    
    @Override
    public int read() throws IOException {
        return this.readBits(8);
    }
    
    public int readBits(final int n) throws IOException {
        while (this.bitsInCache < n) {
            final int read = this.is.read();
            if (read < 0) {
                if (this.tiffLZWMode) {
                    return 257;
                }
                return -1;
            }
            else {
                final int n2 = read & 0xFF;
                if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                    this.bitCache = (n2 | this.bitCache << 8);
                }
                else {
                    this.bitCache |= n2 << this.bitsInCache;
                }
                ++this.bytesRead;
                this.bitsInCache += 8;
            }
        }
        final int n3 = (1 << n) - 1;
        int n4;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            n4 = (n3 & this.bitCache >> this.bitsInCache - n);
        }
        else {
            n4 = (n3 & this.bitCache);
            this.bitCache >>= n;
        }
        this.bitsInCache -= n;
        this.bitCache &= (1 << this.bitsInCache) - 1;
        return n4;
    }
    
    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }
}
