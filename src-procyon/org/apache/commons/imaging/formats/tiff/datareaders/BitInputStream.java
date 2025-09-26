// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.datareaders;

import java.io.IOException;
import java.nio.ByteOrder;
import java.io.InputStream;

class BitInputStream extends InputStream
{
    private final ByteOrder byteOrder;
    private long bytesRead;
    private int cache;
    private int cacheBitsRemaining;
    private final InputStream is;
    
    public BitInputStream(final InputStream is, final ByteOrder byteOrder) {
        this.is = is;
        this.byteOrder = byteOrder;
    }
    
    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }
    
    public long getBytesRead() {
        return this.bytesRead;
    }
    
    @Override
    public int read() throws IOException {
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        }
        return this.is.read();
    }
    
    public final int readBits(int n) throws IOException {
        if (n < 8) {
            if (this.cacheBitsRemaining == 0) {
                this.cache = this.is.read();
                this.cacheBitsRemaining = 8;
                ++this.bytesRead;
            }
            if (n > this.cacheBitsRemaining) {
                throw new IOException("BitInputStream: can't read bit fields across bytes");
            }
            this.cacheBitsRemaining -= n;
            final int n2 = this.cache >> this.cacheBitsRemaining;
            switch (n) {
                case 7: {
                    return n2 & 0x7F;
                }
                case 6: {
                    return n2 & 0x3F;
                }
                case 5: {
                    return n2 & 0x1F;
                }
                case 4: {
                    return n2 & 0xF;
                }
                case 3: {
                    return n2 & 0x7;
                }
                case 2: {
                    return n2 & 0x3;
                }
                case 1: {
                    return n2 & 0x1;
                }
            }
        }
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        }
        if (n == 8) {
            ++this.bytesRead;
            return this.is.read();
        }
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            if (n == 16) {
                this.bytesRead += 2L;
                n = this.is.read();
                return this.is.read() << 0 | n << 8;
            }
            if (n == 24) {
                this.bytesRead += 3L;
                final int read = this.is.read();
                n = this.is.read();
                return this.is.read() << 0 | (read << 16 | n << 8);
            }
            if (n == 32) {
                this.bytesRead += 4L;
                final int read2 = this.is.read();
                n = this.is.read();
                return this.is.read() << 0 | (read2 << 24 | n << 16 | this.is.read() << 8);
            }
        }
        else {
            if (n == 16) {
                this.bytesRead += 2L;
                n = this.is.read();
                return this.is.read() << 8 | n << 0;
            }
            if (n == 24) {
                this.bytesRead += 3L;
                n = this.is.read();
                return this.is.read() << 16 | (n << 0 | this.is.read() << 8);
            }
            if (n == 32) {
                this.bytesRead += 4L;
                n = this.is.read();
                return this.is.read() << 24 | (n << 0 | this.is.read() << 8 | this.is.read() << 16);
            }
        }
        throw new IOException("BitInputStream: unknown error");
    }
}
