// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import java.nio.ByteBuffer;

public class ByteBufferUtil
{
    public static final String TAG = "ByteBufferUtil";
    
    public static byte[] array(final ByteBuffer byteBuffer) {
        final ByteBuffer duplicate = byteBuffer.duplicate();
        final byte[] dst = new byte[duplicate.limit()];
        duplicate.rewind();
        duplicate.get(dst);
        return dst;
    }
}
