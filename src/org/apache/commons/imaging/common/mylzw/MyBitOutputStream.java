package org.apache.commons.imaging.common.mylzw;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MyBitOutputStream extends OutputStream {
    private int bitCache;
    private int bitsInCache;
    private final ByteOrder byteOrder;
    private int bytesWritten;
    private final OutputStream os;

    public MyBitOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        this.os = outputStream;
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        writeBits(i, 8);
    }

    public void writeBits(int i, int i2) throws IOException {
        int i3 = i & ((1 << i2) - 1);
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            this.bitCache = i3 | (this.bitCache << i2);
        } else {
            this.bitCache = (i3 << this.bitsInCache) | this.bitCache;
        }
        this.bitsInCache += i2;
        while (this.bitsInCache >= 8) {
            if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                actualWrite((this.bitCache >> (this.bitsInCache - 8)) & 255);
                this.bitsInCache -= 8;
            } else {
                actualWrite(this.bitCache & 255);
                this.bitCache >>= 8;
                this.bitsInCache -= 8;
            }
            this.bitCache = ((1 << this.bitsInCache) - 1) & this.bitCache;
        }
    }

    private void actualWrite(int i) throws IOException {
        this.os.write(i);
        this.bytesWritten++;
    }

    public void flushCache() throws IOException {
        if (this.bitsInCache > 0) {
            int i = ((1 << this.bitsInCache) - 1) & this.bitCache;
            if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                this.os.write(i << (8 - this.bitsInCache));
            } else {
                this.os.write(i);
            }
        }
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public int getBytesWritten() {
        return this.bytesWritten + (this.bitsInCache > 0 ? 1 : 0);
    }
}
