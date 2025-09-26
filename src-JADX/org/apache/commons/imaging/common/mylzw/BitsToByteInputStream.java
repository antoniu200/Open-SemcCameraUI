package org.apache.commons.imaging.common.mylzw;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BitsToByteInputStream extends InputStream {
    private final int desiredDepth;
    private final MyBitInputStream is;

    public BitsToByteInputStream(MyBitInputStream myBitInputStream, int i) {
        this.is = myBitInputStream;
        this.desiredDepth = i;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return readBits(8);
    }

    public int readBits(int i) throws IOException {
        int bits = this.is.readBits(i);
        if (i < this.desiredDepth) {
            return bits << (this.desiredDepth - i);
        }
        return i > this.desiredDepth ? bits >> (i - this.desiredDepth) : bits;
    }

    public int[] readBitsArray(int i, int i2) throws IOException {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = readBits(i);
        }
        return iArr;
    }
}
