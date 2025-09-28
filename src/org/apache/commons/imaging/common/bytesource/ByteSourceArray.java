package org.apache.commons.imaging.common.bytesource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ByteSourceArray extends ByteSource {
    private final byte[] bytes;

    public ByteSourceArray(String str, byte[] bArr) {
        super(str);
        this.bytes = bArr;
    }

    public ByteSourceArray(byte[] bArr) {
        super(null);
        this.bytes = bArr;
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public byte[] getBlock(long j, int i) throws IOException {
        int i2;
        int i3 = (int) j;
        if (i3 < 0 || i < 0 || (i2 = i3 + i) < 0 || i2 > this.bytes.length) {
            throw new IOException("Could not read block (block start: " + i3 + ", block length: " + i + ", data length: " + this.bytes.length + ").");
        }
        byte[] bArr = new byte[i];
        System.arraycopy(this.bytes, i3, bArr, 0, i);
        return bArr;
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public long getLength() {
        return this.bytes.length;
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public byte[] getAll() throws IOException {
        return this.bytes;
    }

    @Override // org.apache.commons.imaging.common.bytesource.ByteSource
    public String getDescription() {
        return this.bytes.length + " byte array";
    }
}
