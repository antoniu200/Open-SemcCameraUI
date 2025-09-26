// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.itu_t4;

import java.io.IOException;
import java.io.InputStream;

class BitInputStreamFlexible extends InputStream
{
    private long bytesRead;
    private int cache;
    private int cacheBitsRemaining;
    private final InputStream is;
    
    public BitInputStreamFlexible(final InputStream is) {
        this.is = is;
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
        if (n <= 32) {
            int i;
            if (this.cacheBitsRemaining > 0) {
                if (n >= this.cacheBitsRemaining) {
                    final int n2 = (1 << this.cacheBitsRemaining) - 1 & this.cache;
                    final int n3 = n - this.cacheBitsRemaining;
                    this.cacheBitsRemaining = 0;
                    n = n2;
                    i = n3;
                }
                else {
                    this.cacheBitsRemaining -= n;
                    n = ((1 << n) - 1 & this.cache >> this.cacheBitsRemaining);
                    i = 0;
                }
            }
            else {
                final int n4 = 0;
                i = n;
                n = n4;
            }
            while (i >= 8) {
                this.cache = this.is.read();
                if (this.cache < 0) {
                    throw new IOException("couldn't read bits");
                }
                ++this.bytesRead;
                n = (n << 8 | (0xFF & this.cache));
                i -= 8;
            }
            int n5 = n;
            if (i > 0) {
                this.cache = this.is.read();
                if (this.cache < 0) {
                    throw new IOException("couldn't read bits");
                }
                ++this.bytesRead;
                this.cacheBitsRemaining = 8 - i;
                n5 = (n << i | (this.cache >> this.cacheBitsRemaining & (1 << i) - 1));
            }
            return n5;
        }
        throw new IOException("BitInputStream: unknown error");
    }
}
