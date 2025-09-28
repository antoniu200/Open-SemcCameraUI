package com.google.android.gms.internal;

import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class zzsh {
    public static final int[] zzbix = new int[0];
    public static final long[] zzbiy = new long[0];
    public static final float[] zzbiz = new float[0];
    public static final double[] zzbiA = new double[0];
    public static final boolean[] zzbiB = new boolean[0];
    public static final String[] zzbiC = new String[0];
    public static final byte[][] zzbiD = new byte[0][];
    public static final byte[] zzbiE = new byte[0];

    static int zzD(int i, int i2) {
        return (i << 3) | i2;
    }

    public static boolean zzb(zzrw zzrwVar, int i) throws IOException {
        return zzrwVar.zzlA(i);
    }

    public static final int zzc(zzrw zzrwVar, int i) throws IOException {
        int position = zzrwVar.getPosition();
        zzrwVar.zzlA(i);
        int i2 = 1;
        while (zzrwVar.zzFo() == i) {
            zzrwVar.zzlA(i);
            i2++;
        }
        zzrwVar.zzlE(position);
        return i2;
    }

    static int zzlU(int i) {
        return i & 7;
    }

    public static int zzlV(int i) {
        return i >>> 3;
    }
}
