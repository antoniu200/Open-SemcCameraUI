package org.apache.commons.imaging.common;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class FastByteArrayOutputStream extends OutputStream {
    private final byte[] bytes;
    private int count;

    public FastByteArrayOutputStream(int i) {
        this.bytes = new byte[i];
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        if (this.count >= this.bytes.length) {
            throw new IOException("Write exceeded expected length (" + this.count + ", " + this.bytes.length + ")");
        }
        this.bytes[this.count] = (byte) i;
        this.count++;
    }

    public byte[] toByteArray() {
        if (this.count < this.bytes.length) {
            byte[] bArr = new byte[this.count];
            System.arraycopy(this.bytes, 0, bArr, 0, this.count);
            return bArr;
        }
        return this.bytes;
    }

    public int getBytesWritten() {
        return this.count;
    }
}
