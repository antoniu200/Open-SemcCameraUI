package org.apache.commons.imaging.common.itu_t4;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BitInputStreamFlexible extends InputStream {
    private long bytesRead;
    private int cache;
    private int cacheBitsRemaining;
    private final InputStream is;

    public BitInputStreamFlexible(InputStream inputStream) {
        this.is = inputStream;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        }
        return this.is.read();
    }

    public final int readBits(int i) throws IOException {
        int i2;
        if (i <= 32) {
            if (this.cacheBitsRemaining <= 0) {
                i2 = 0;
            } else if (i >= this.cacheBitsRemaining) {
                i2 = ((1 << this.cacheBitsRemaining) - 1) & this.cache;
                i -= this.cacheBitsRemaining;
                this.cacheBitsRemaining = 0;
            } else {
                this.cacheBitsRemaining -= i;
                i2 = ((1 << i) - 1) & (this.cache >> this.cacheBitsRemaining);
                i = 0;
            }
            while (i >= 8) {
                this.cache = this.is.read();
                if (this.cache < 0) {
                    throw new IOException("couldn't read bits");
                }
                this.bytesRead++;
                i2 = (i2 << 8) | (255 & this.cache);
                i -= 8;
            }
            if (i <= 0) {
                return i2;
            }
            this.cache = this.is.read();
            if (this.cache < 0) {
                throw new IOException("couldn't read bits");
            }
            this.bytesRead++;
            this.cacheBitsRemaining = 8 - i;
            return (i2 << i) | ((this.cache >> this.cacheBitsRemaining) & ((1 << i) - 1));
        }
        throw new IOException("BitInputStream: unknown error");
    }

    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }
}
