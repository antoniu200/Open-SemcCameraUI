// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

public class zzmw
{
    public static int zza(final byte[] array, int i, final int n, int n2) {
        int n3;
        for (n3 = (n & 0xFFFFFFFC) + i; i < n3; i += 4) {
            final int n4 = ((array[i] & 0xFF) | (array[i + 1] & 0xFF) << 8 | (array[i + 2] & 0xFF) << 16 | array[i + 3] << 24) * -862048943;
            n2 ^= (n4 << 15 | n4 >>> 17) * 461845907;
            n2 = (n2 >>> 19 | n2 << 13) * 5 - 430675100;
        }
        int n5 = 0;
        i = 0;
        switch (n & 0x3) {
            case 3: {
                i = (array[n3 + 2] & 0xFF) << 16;
            }
            case 2: {
                n5 = (i | (array[n3 + 1] & 0xFF) << 8);
            }
            case 1: {
                i = ((array[n3] & 0xFF) | n5) * -862048943;
                n2 ^= (i >>> 17 | i << 15) * 461845907;
                break;
            }
        }
        i = (n2 ^ n);
        i = (i ^ i >>> 16) * -2048144789;
        i = (i ^ i >>> 13) * -1028477387;
        return i ^ i >>> 16;
    }
}
