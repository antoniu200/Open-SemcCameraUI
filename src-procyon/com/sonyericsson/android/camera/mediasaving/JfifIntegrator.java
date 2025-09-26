// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.mediasaving;

import java.math.BigInteger;
import com.sonyericsson.cameracommon.mediasaving.yuv2jpeg.IntegrationMakerException;

public class JfifIntegrator
{
    private static final int M_DHT = 196;
    private static final int M_DQT = 219;
    private static final int M_MARKER = 255;
    private static final int M_SOI = 216;
    private static final int M_SOS = 218;
    public static final String TAG = "JfifIntegrator";
    
    public static int integrate(final byte[] array, final byte[] array2) throws IntegrationMakerException {
        final int length = array.length;
        final byte[] array3 = new byte[2];
        final int n = 0;
        final int n3;
        int n2 = n3 = 0;
        int i;
        int n4 = i = n3;
        int j = n3;
        int n5 = n;
        while (true) {
            final int n6 = n5 + 1;
            if (n6 >= length) {
                break;
            }
            array3[0] = array[n5];
            array3[1] = array[n6];
            final int n7 = n5 + 2;
            if (-1 != array3[0]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == array3[1]) {
                break;
            }
            if (-40 == array3[1]) {
                n5 = n7;
            }
            else {
                final int intValue = new BigInteger(new byte[] { array[n7], array[n7 + 1] }).intValue();
                int n9;
                int n10;
                int n11;
                int n12;
                if (-60 == array3[1]) {
                    int n8;
                    if (i == 0) {
                        n8 = intValue;
                    }
                    else {
                        n8 = intValue - 2;
                    }
                    n9 = i + n8;
                    n10 = n2 + (intValue + 2);
                    n11 = j;
                    n12 = n4;
                }
                else {
                    n10 = n2;
                    n11 = j;
                    n9 = i;
                    n12 = n4;
                    if (-37 == array3[1]) {
                        int n13;
                        if (j == 0) {
                            n13 = intValue;
                        }
                        else {
                            n13 = intValue - 2;
                        }
                        n11 = j + n13;
                        n12 = n4 + (intValue + 2);
                        n9 = i;
                        n10 = n2;
                    }
                }
                n5 = n7 + intValue;
                n2 = n10;
                j = n11;
                i = n9;
                n4 = n12;
            }
        }
        final int n14 = j + 2;
        final byte[] array4 = new byte[n14];
        final int n15 = i + 2;
        final byte[] array5 = new byte[n15];
        array4[0] = -1;
        array4[1] = -37;
        array5[0] = -1;
        array5[1] = -60;
        final byte[] byteArray = new BigInteger(String.valueOf(j)).toByteArray();
        array4[2] = byteArray[0];
        array4[3] = byteArray[1];
        final byte[] byteArray2 = new BigInteger(String.valueOf(i)).toByteArray();
        array5[2] = byteArray2[0];
        array5[3] = byteArray2[1];
        int n16 = 0;
        int n17 = 4;
        int n18 = 4;
        int n19 = 0;
        while (true) {
            final int n20 = n19 + 1;
            if (n20 >= length) {
                break;
            }
            array3[0] = array[n19];
            array3[1] = array[n20];
            n19 += 2;
            if (-1 != array3[0]) {
                throw new IntegrationMakerException("No 'FF' marker.");
            }
            if (-38 == array3[1]) {
                System.arraycopy(array4, 0, array2, n16, n14);
                final int n21 = n16 + n14;
                System.arraycopy(array5, 0, array2, n21, n15);
                n19 -= 2;
                System.arraycopy(array, n19, array2, n21 + n15, length - n19);
                break;
            }
            if (-40 == array3[1]) {
                array2[n19 - 2] = array3[0];
                array2[n19 - 1] = array3[1];
                n16 += 2;
            }
            else {
                final int intValue2 = new BigInteger(new byte[] { array[n19], array[n19 + 1] }).intValue();
                if (-60 == array3[1]) {
                    final int n22 = intValue2 - 2;
                    System.arraycopy(array, n19 + 2, array5, n17, n22);
                    n17 += n22;
                }
                else if (-37 == array3[1]) {
                    final int n23 = intValue2 - 2;
                    System.arraycopy(array, n19 + 2, array4, n18, n23);
                    n18 += n23;
                }
                else {
                    final int n24 = intValue2 + 2;
                    System.arraycopy(array, n19 - 2, array2, n16, n24);
                    n16 += n24;
                }
                n19 += intValue2;
            }
        }
        return length - n4 - n2 + j + 2 + i + 2;
    }
}
