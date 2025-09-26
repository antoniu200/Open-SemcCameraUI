// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.gestureshutter;

import java.nio.ByteBuffer;
import android.util.Log;

public class HandSignsNativeWrapper
{
    private static final String TAG = "HandSignsNativeWrapper";
    private long mNativeHandle;
    
    static {
        try {
            System.loadLibrary("handsigns_jni");
        }
        catch (final UnsatisfiedLinkError unsatisfiedLinkError) {
            Log.i("HandSignsNativeWrapper", "GestureShutter is not supported.");
        }
    }
    
    public HandSignsNativeWrapper() {
        this.mNativeHandle = 0L;
        this.create();
    }
    
    private void create() {
        synchronized (this) {
            if (this.mNativeHandle == 0L) {
                this.mNativeHandle = this.nativeCreate();
            }
        }
    }
    
    private native long nativeCreate();
    
    private native boolean nativeDetect(final long p0, final int p1, final int p2, final ByteBuffer p3, final int p4, final HandSignsDetector.DetectResult p5);
    
    private native boolean nativeDetect(final long p0, final int p1, final int p2, final byte[] p3, final int p4, final HandSignsDetector.DetectResult p5);
    
    private native void nativeRelease(final long p0);
    
    private static final native int nativeShrinkByteArrayYvu420Sp(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4);
    
    public static final void shrinkYvu420Sp(final byte[] array, int nativeShrinkByteArrayYvu420Sp, final int n, final byte[] array2, final ShrinkRatio shrinkRatio) {
        nativeShrinkByteArrayYvu420Sp = nativeShrinkByteArrayYvu420Sp(array, nativeShrinkByteArrayYvu420Sp, n, array2, shrinkRatio.shrinkSize);
        if (nativeShrinkByteArrayYvu420Sp != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error Code Returned : ");
            sb.append(nativeShrinkByteArrayYvu420Sp);
            throw new RuntimeException(sb.toString());
        }
    }
    
    public boolean detect(final int n, final int n2, final ByteBuffer byteBuffer, final int n3, final HandSignsDetector.DetectResult detectResult) {
        synchronized (this) {
            return this.mNativeHandle != 0L && this.nativeDetect(this.mNativeHandle, n, n2, byteBuffer, n3, detectResult);
        }
    }
    
    public boolean detect(final int n, final int n2, final byte[] array, final int n3, final HandSignsDetector.DetectResult detectResult) {
        synchronized (this) {
            return this.mNativeHandle != 0L && this.nativeDetect(this.mNativeHandle, n, n2, array, n3, detectResult);
        }
    }
    
    public void release() {
        synchronized (this) {
            if (this.mNativeHandle != 0L) {
                this.nativeRelease(this.mNativeHandle);
                this.mNativeHandle = 0L;
            }
        }
    }
    
    public enum ShrinkRatio
    {
        private static final ShrinkRatio[] $VALUES;
        
        HALF(2), 
        ONE(1), 
        ONE_EIGHTH(8), 
        QUARTER(4);
        
        public final int shrinkSize;
        
        static {
            $VALUES = new ShrinkRatio[] { ShrinkRatio.ONE, ShrinkRatio.HALF, ShrinkRatio.QUARTER, ShrinkRatio.ONE_EIGHTH };
        }
        
        private ShrinkRatio(final int shrinkSize) {
            this.shrinkSize = shrinkSize;
        }
    }
}
