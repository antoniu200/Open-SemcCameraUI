package org.apache.commons.imaging.common.bytesource;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ByteSource {
    protected final String filename;

    public abstract byte[] getAll() throws IOException;

    public abstract byte[] getBlock(long j, int i) throws IOException;

    public abstract String getDescription();

    public abstract InputStream getInputStream() throws IOException;

    public abstract long getLength() throws IOException;

    public ByteSource(String str) {
        this.filename = str;
    }

    public final InputStream getInputStream(long j) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            BinaryFunctions.skipBytes(inputStream, j);
            return inputStream;
        } catch (Throwable th2) {
            th = th2;
            if (inputStream != null) {
                inputStream.close();
            }
            throw th;
        }
    }

    public byte[] getBlock(int i, int i2) throws IOException {
        return getBlock(i & 4294967295L, i2);
    }

    public final String getFilename() {
        return this.filename;
    }
}
