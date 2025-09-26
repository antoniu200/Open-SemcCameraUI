// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving;

import java.io.IOException;
import java.io.OutputStream;

public class ThreadSafeOutputStream extends OutputStream
{
    boolean mClosed;
    private OutputStream mDelegateStream;
    
    public ThreadSafeOutputStream(final OutputStream mDelegateStream) {
        this.mDelegateStream = mDelegateStream;
    }
    
    @Override
    public void close() throws IOException {
        synchronized (this) {
            this.mClosed = true;
            this.mDelegateStream.close();
        }
    }
    
    @Override
    public void flush() throws IOException {
        synchronized (this) {
            super.flush();
        }
    }
    
    @Override
    public void write(final int n) throws IOException {
        synchronized (this) {
            if (this.mClosed) {
                return;
            }
            this.mDelegateStream.write(n);
        }
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        monitorenter(this);
        if (len > 0) {
            try {
                if (!this.mClosed) {
                    this.mDelegateStream.write(b, off, len);
                    return;
                }
            }
            finally {
                monitorexit(this);
            }
        }
        monitorexit(this);
    }
}
