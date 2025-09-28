package com.sonyericsson.cameracommon.mediasaving;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ThreadSafeOutputStream extends OutputStream {
    boolean mClosed;
    private OutputStream mDelegateStream;

    public ThreadSafeOutputStream(OutputStream outputStream) {
        this.mDelegateStream = outputStream;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        this.mClosed = true;
        this.mDelegateStream.close();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public synchronized void flush() throws IOException {
        super.flush();
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        if (i2 > 0) {
            if (!this.mClosed) {
                this.mDelegateStream.write(bArr, i, i2);
            }
        }
    }

    @Override // java.io.OutputStream
    public synchronized void write(int i) throws IOException {
        if (this.mClosed) {
            return;
        }
        this.mDelegateStream.write(i);
    }
}
