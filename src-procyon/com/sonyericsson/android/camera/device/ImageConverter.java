// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

import java.nio.ByteBuffer;

class ImageConverter
{
    static {
        System.loadLibrary("image_converter");
    }
    
    public static native void convertFromYuv420_888ToNv21(final byte[] p0, final int p1, final int p2, final ByteBuffer p3, final int p4, final int p5, final ByteBuffer p6, final int p7, final int p8, final ByteBuffer p9, final int p10, final int p11);
}
