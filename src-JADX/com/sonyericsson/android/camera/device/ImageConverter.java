package com.sonyericsson.android.camera.device;

import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ImageConverter {
    public static native void convertFromYuv420_888ToNv21(byte[] bArr, int i, int i2, ByteBuffer byteBuffer, int i3, int i4, ByteBuffer byteBuffer2, int i5, int i6, ByteBuffer byteBuffer3, int i7, int i8);

    ImageConverter() {
    }

    static {
        System.loadLibrary("image_converter");
    }
}
