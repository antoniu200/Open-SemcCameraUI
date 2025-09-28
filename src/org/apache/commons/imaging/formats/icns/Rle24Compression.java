package org.apache.commons.imaging.formats.icns;

import kotlin.jvm.internal.ByteCompanionObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class Rle24Compression {
    private Rle24Compression() {
    }

    public static byte[] decompress(int i, int i2, byte[] bArr) {
        int i3 = i * i2;
        byte[] bArr2 = new byte[4 * i3];
        int i4 = (i < 128 || i2 < 128) ? 0 : 4;
        int i5 = 1;
        while (i5 <= 3) {
            int i6 = 0;
            int i7 = i4;
            int i8 = i3;
            while (i8 > 0) {
                if ((bArr[i7] & ByteCompanionObject.MIN_VALUE) != 0) {
                    int i9 = (bArr[i7] & 255) - 125;
                    int i10 = i6;
                    int i11 = 0;
                    while (i11 < i9) {
                        bArr2[(i10 * 4) + i5] = bArr[i7 + 1];
                        i11++;
                        i10++;
                    }
                    i7 += 2;
                    i8 -= i9;
                    i6 = i10;
                } else {
                    int i12 = (bArr[i7] & 255) + 1;
                    int i13 = i7 + 1;
                    int i14 = 0;
                    while (i14 < i12) {
                        bArr2[(i6 * 4) + i5] = bArr[i13];
                        i14++;
                        i6++;
                        i13++;
                    }
                    i8 -= i12;
                    i7 = i13;
                }
            }
            i5++;
            i4 = i7;
        }
        return bArr2;
    }
}
