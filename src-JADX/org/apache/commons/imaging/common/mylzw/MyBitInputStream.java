package org.apache.commons.imaging.common.mylzw;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MyBitInputStream extends InputStream {
    private int bitCache;
    private int bitsInCache;
    private final ByteOrder byteOrder;
    private long bytesRead;
    private final InputStream is;
    private boolean tiffLZWMode;

    public MyBitInputStream(InputStream inputStream, ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        this.is = inputStream;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return readBits(8);
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }

    public int readBits(int i) throws IOException {
        int i2;
        while (this.bitsInCache < i) {
            int i3 = this.is.read();
            if (i3 < 0) {
                return this.tiffLZWMode ? 257 : -1;
            }
            int i4 = i3 & 255;
            if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                this.bitCache = i4 | (this.bitCache << 8);
            } else {
                this.bitCache = (i4 << this.bitsInCache) | this.bitCache;
            }
            this.bytesRead++;
            this.bitsInCache += 8;
        }
        int i5 = (1 << i) - 1;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            i2 = i5 & (this.bitCache >> (this.bitsInCache - i));
        } else {
            i2 = i5 & this.bitCache;
            this.bitCache >>= i;
        }
        this.bitsInCache -= i;
        this.bitCache = ((1 << this.bitsInCache) - 1) & this.bitCache;
        return i2;
    }

    public void flushCache() {
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }
}
