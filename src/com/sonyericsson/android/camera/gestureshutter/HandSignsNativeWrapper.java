package com.sonyericsson.android.camera.gestureshutter;

import android.util.Log;
import com.sonyericsson.android.camera.gestureshutter.HandSignsDetector;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class HandSignsNativeWrapper {
    private static final String TAG = "HandSignsNativeWrapper";
    private long mNativeHandle = 0;

    private native long nativeCreate();

    private native boolean nativeDetect(long j, int i, int i2, ByteBuffer byteBuffer, int i3, HandSignsDetector.DetectResult detectResult);

    private native boolean nativeDetect(long j, int i, int i2, byte[] bArr, int i3, HandSignsDetector.DetectResult detectResult);

    private native void nativeRelease(long j);

    private static final native int nativeShrinkByteArrayYvu420Sp(byte[] bArr, int i, int i2, byte[] bArr2, int i3);

    static {
        try {
            System.loadLibrary("handsigns_jni");
        } catch (UnsatisfiedLinkError unused) {
            Log.i(TAG, "GestureShutter is not supported.");
        }
    }

    public HandSignsNativeWrapper() {
        create();
    }

    private synchronized void create() {
        if (this.mNativeHandle == 0) {
            this.mNativeHandle = nativeCreate();
        }
    }

    public synchronized boolean detect(int i, int i2, byte[] bArr, int i3, HandSignsDetector.DetectResult detectResult) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return nativeDetect(this.mNativeHandle, i, i2, bArr, i3, detectResult);
    }

    public synchronized boolean detect(int i, int i2, ByteBuffer byteBuffer, int i3, HandSignsDetector.DetectResult detectResult) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return nativeDetect(this.mNativeHandle, i, i2, byteBuffer, i3, detectResult);
    }

    public synchronized void release() {
        if (this.mNativeHandle != 0) {
            nativeRelease(this.mNativeHandle);
            this.mNativeHandle = 0L;
        }
    }

    public enum ShrinkRatio {
        ONE(1),
        HALF(2),
        QUARTER(4),
        ONE_EIGHTH(8);

        public final int shrinkSize;

        ShrinkRatio(int i) {
            this.shrinkSize = i;
        }
    }

    public static final void shrinkYvu420Sp(byte[] bArr, int i, int i2, byte[] bArr2, ShrinkRatio shrinkRatio) {
        int iNativeShrinkByteArrayYvu420Sp = nativeShrinkByteArrayYvu420Sp(bArr, i, i2, bArr2, shrinkRatio.shrinkSize);
        if (iNativeShrinkByteArrayYvu420Sp != 0) {
            throw new RuntimeException("Error Code Returned : " + iNativeShrinkByteArrayYvu420Sp);
        }
    }
}
