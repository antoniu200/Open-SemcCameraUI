package org.apache.commons.imaging.common.itu_t4;

import java.io.OutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BitArrayOutputStream extends OutputStream {
    private byte[] buffer;
    private int bytesWritten;
    private int cache;
    private int cacheMask;

    public BitArrayOutputStream() {
        this.cacheMask = 128;
        this.buffer = new byte[16];
    }

    public BitArrayOutputStream(int i) {
        this.cacheMask = 128;
        this.buffer = new byte[i];
    }

    public int size() {
        return this.bytesWritten;
    }

    public byte[] toByteArray() {
        flush();
        if (this.bytesWritten == this.buffer.length) {
            return this.buffer;
        }
        byte[] bArr = new byte[this.bytesWritten];
        System.arraycopy(this.buffer, 0, bArr, 0, this.bytesWritten);
        return bArr;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        flush();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() {
        if (this.cacheMask != 128) {
            writeByte(this.cache);
            this.cache = 0;
            this.cacheMask = 128;
        }
    }

    @Override // java.io.OutputStream
    public void write(int i) {
        flush();
        writeByte(i);
    }

    public void writeBit(int i) {
        if (i != 0) {
            this.cache |= this.cacheMask;
        }
        this.cacheMask >>>= 1;
        if (this.cacheMask == 0) {
            flush();
        }
    }

    public int getBitsAvailableInCurrentByte() {
        int i = 0;
        for (int i2 = this.cacheMask; i2 != 0; i2 >>>= 1) {
            i++;
        }
        return i;
    }

    private void writeByte(int i) {
        if (this.bytesWritten >= this.buffer.length) {
            byte[] bArr = new byte[this.buffer.length * 2];
            System.arraycopy(this.buffer, 0, bArr, 0, this.bytesWritten);
            this.buffer = bArr;
        }
        byte[] bArr2 = this.buffer;
        int i2 = this.bytesWritten;
        this.bytesWritten = i2 + 1;
        bArr2[i2] = (byte) i;
    }
}
