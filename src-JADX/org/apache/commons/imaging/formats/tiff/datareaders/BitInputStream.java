package org.apache.commons.imaging.formats.tiff.datareaders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BitInputStream extends InputStream {
    private final ByteOrder byteOrder;
    private long bytesRead;
    private int cache;
    private int cacheBitsRemaining;
    private final InputStream is;

    public BitInputStream(InputStream inputStream, ByteOrder byteOrder) {
        this.is = inputStream;
        this.byteOrder = byteOrder;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        }
        return this.is.read();
    }

    public final int readBits(int i) throws IOException {
        if (i < 8) {
            if (this.cacheBitsRemaining == 0) {
                this.cache = this.is.read();
                this.cacheBitsRemaining = 8;
                this.bytesRead++;
            }
            if (i > this.cacheBitsRemaining) {
                throw new IOException("BitInputStream: can't read bit fields across bytes");
            }
            this.cacheBitsRemaining -= i;
            int i2 = this.cache >> this.cacheBitsRemaining;
            switch (i) {
                case 1:
                    return i2 & 1;
                case 2:
                    return i2 & 3;
                case 3:
                    return i2 & 7;
                case 4:
                    return i2 & 15;
                case 5:
                    return i2 & 31;
                case 6:
                    return i2 & 63;
                case 7:
                    return i2 & 127;
            }
        }
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        }
        if (i == 8) {
            this.bytesRead++;
            return this.is.read();
        }
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            if (i == 16) {
                this.bytesRead += 2;
                return (this.is.read() << 0) | (this.is.read() << 8);
            }
            if (i == 24) {
                this.bytesRead += 3;
                return (this.is.read() << 0) | (this.is.read() << 16) | (this.is.read() << 8);
            }
            if (i == 32) {
                this.bytesRead += 4;
                return (this.is.read() << 0) | (this.is.read() << 24) | (this.is.read() << 16) | (this.is.read() << 8);
            }
        } else {
            if (i == 16) {
                this.bytesRead += 2;
                return (this.is.read() << 8) | (this.is.read() << 0);
            }
            if (i == 24) {
                this.bytesRead += 3;
                return (this.is.read() << 16) | (this.is.read() << 0) | (this.is.read() << 8);
            }
            if (i == 32) {
                this.bytesRead += 4;
                return (this.is.read() << 24) | (this.is.read() << 0) | (this.is.read() << 8) | (this.is.read() << 16);
            }
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
