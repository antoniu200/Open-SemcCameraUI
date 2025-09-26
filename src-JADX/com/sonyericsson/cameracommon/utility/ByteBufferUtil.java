package com.sonyericsson.cameracommon.utility;

import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ByteBufferUtil {
    public static final String TAG = "ByteBufferUtil";

    public static byte[] array(ByteBuffer byteBuffer) {
        ByteBuffer byteBufferDuplicate = byteBuffer.duplicate();
        byte[] bArr = new byte[byteBufferDuplicate.limit()];
        byteBufferDuplicate.rewind();
        byteBufferDuplicate.get(bArr);
        return bArr;
    }
}
