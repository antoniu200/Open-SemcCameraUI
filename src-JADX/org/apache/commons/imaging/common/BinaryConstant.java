package org.apache.commons.imaging.common;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BinaryConstant implements Cloneable {
    private final byte[] value;

    public BinaryConstant(byte[] bArr) {
        this.value = (byte[]) bArr.clone();
    }

    public BinaryConstant clone() throws CloneNotSupportedException {
        return (BinaryConstant) super.clone();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof BinaryConstant)) {
            return equals(((BinaryConstant) obj).value);
        }
        return false;
    }

    public boolean equals(byte[] bArr) {
        return Arrays.equals(this.value, bArr);
    }

    public boolean equals(byte[] bArr, int i, int i2) {
        if (this.value.length != i2) {
            return false;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (this.value[i3] != bArr[i + i3]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    public byte get(int i) {
        return this.value[i];
    }

    public int size() {
        return this.value.length;
    }

    public byte[] toByteArray() {
        return (byte[]) this.value.clone();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        for (byte b : this.value) {
            outputStream.write(b);
        }
    }
}
