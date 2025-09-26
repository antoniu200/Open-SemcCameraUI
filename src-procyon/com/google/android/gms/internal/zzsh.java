// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.IOException;

public final class zzsh
{
    public static final double[] zzbiA;
    public static final boolean[] zzbiB;
    public static final String[] zzbiC;
    public static final byte[][] zzbiD;
    public static final byte[] zzbiE;
    public static final int[] zzbix;
    public static final long[] zzbiy;
    public static final float[] zzbiz;
    
    static {
        zzbix = new int[0];
        zzbiy = new long[0];
        zzbiz = new float[0];
        zzbiA = new double[0];
        zzbiB = new boolean[0];
        zzbiC = new String[0];
        zzbiD = new byte[0][];
        zzbiE = new byte[0];
    }
    
    static int zzD(final int n, final int n2) {
        return n << 3 | n2;
    }
    
    public static boolean zzb(final zzrw zzrw, final int n) throws IOException {
        return zzrw.zzlA(n);
    }
    
    public static final int zzc(final zzrw zzrw, final int n) throws IOException {
        final int position = zzrw.getPosition();
        zzrw.zzlA(n);
        int n2 = 1;
        while (zzrw.zzFo() == n) {
            zzrw.zzlA(n);
            ++n2;
        }
        zzrw.zzlE(position);
        return n2;
    }
    
    static int zzlU(final int n) {
        return n & 0x7;
    }
    
    public static int zzlV(final int n) {
        return n >>> 3;
    }
}
