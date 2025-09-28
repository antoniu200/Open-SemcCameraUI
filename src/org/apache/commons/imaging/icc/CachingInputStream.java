package org.apache.commons.imaging.icc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class CachingInputStream extends InputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final InputStream is;

    public CachingInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public byte[] getCache() {
        return this.baos.toByteArray();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int i = this.is.read();
        this.baos.write(i);
        return i;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.is.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.is.close();
    }
}
