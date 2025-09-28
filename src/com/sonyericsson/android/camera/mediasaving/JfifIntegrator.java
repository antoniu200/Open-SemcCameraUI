package com.sonyericsson.android.camera.mediasaving;

import com.sonyericsson.cameracommon.mediasaving.yuv2jpeg.IntegrationMakerException;
import java.math.BigInteger;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JfifIntegrator {
    private static final int M_DHT = 196;
    private static final int M_DQT = 219;
    private static final int M_MARKER = 255;
    private static final int M_SOI = 216;
    private static final int M_SOS = 218;
    public static final String TAG = "JfifIntegrator";

    public static int integrate(byte[] bArr, byte[] bArr2) throws IntegrationMakerException {
        int length = bArr.length;
        byte[] bArr3 = new byte[2];
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (true) {
            int i7 = i2 + 1;
            if (i7 >= length) {
                break;
            }
            bArr3[0] = bArr[i2];
            bArr3[1] = bArr[i7];
            i2 += 2;
            if (-1 != bArr3[0]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == bArr3[1]) {
                break;
            }
            if (-40 != bArr3[1]) {
                int iIntValue = new BigInteger(new byte[]{bArr[i2], bArr[i2 + 1]}).intValue();
                if (-60 == bArr3[1]) {
                    i5 += i5 == 0 ? iIntValue : iIntValue - 2;
                    i3 += iIntValue + 2;
                } else if (-37 == bArr3[1]) {
                    i4 += i4 == 0 ? iIntValue : iIntValue - 2;
                    i6 += iIntValue + 2;
                }
                i2 += iIntValue;
            }
        }
        int i8 = i4 + 2;
        byte[] bArr4 = new byte[i8];
        int i9 = i5 + 2;
        byte[] bArr5 = new byte[i9];
        bArr4[0] = -1;
        bArr4[1] = -37;
        bArr5[0] = -1;
        bArr5[1] = -60;
        String strValueOf = String.valueOf(i4);
        String strValueOf2 = String.valueOf(i5);
        byte[] byteArray = new BigInteger(strValueOf).toByteArray();
        bArr4[2] = byteArray[0];
        bArr4[3] = byteArray[1];
        byte[] byteArray2 = new BigInteger(strValueOf2).toByteArray();
        bArr5[2] = byteArray2[0];
        bArr5[3] = byteArray2[1];
        int i10 = ((length - i6) - i3) + i4 + 2 + i5 + 2;
        int i11 = 0;
        int i12 = 4;
        int i13 = 4;
        int i14 = 0;
        while (true) {
            int i15 = i14 + 1;
            if (i15 >= length) {
                break;
            }
            bArr3[i] = bArr[i14];
            bArr3[1] = bArr[i15];
            i14 += 2;
            if (-1 != bArr3[i]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == bArr3[1]) {
                System.arraycopy(bArr4, i, bArr2, i11, i8);
                int i16 = i11 + i8;
                System.arraycopy(bArr5, i, bArr2, i16, i9);
                int i17 = i14 - 2;
                System.arraycopy(bArr, i17, bArr2, i16 + i9, length - i17);
                break;
            }
            if (-40 == bArr3[1]) {
                bArr2[i14 - 2] = bArr3[i];
                bArr2[i14 - 1] = bArr3[1];
                i11 += 2;
            } else {
                byte[] bArr6 = new byte[2];
                bArr6[i] = bArr[i14];
                bArr6[1] = bArr[i14 + 1];
                int iIntValue2 = new BigInteger(bArr6).intValue();
                if (-60 == bArr3[1]) {
                    int i18 = iIntValue2 - 2;
                    System.arraycopy(bArr, i14 + 2, bArr5, i12, i18);
                    i12 += i18;
                } else if (-37 == bArr3[1]) {
                    int i19 = iIntValue2 - 2;
                    System.arraycopy(bArr, i14 + 2, bArr4, i13, i19);
                    i13 += i19;
                } else {
                    int i20 = iIntValue2 + 2;
                    System.arraycopy(bArr, i14 - 2, bArr2, i11, i20);
                    i11 += i20;
                }
                i14 += iIntValue2;
                i = 0;
            }
        }
        return i10;
    }
}
