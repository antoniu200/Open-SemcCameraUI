// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.util.Arrays;

public final class zzsc
{
    public static final Object zzbiu;
    
    static {
        zzbiu = new Object();
    }
    
    public static boolean equals(final float[] a, final float[] a2) {
        if (a != null && a.length != 0) {
            return Arrays.equals(a, a2);
        }
        return a2 == null || a2.length == 0;
    }
    
    public static boolean equals(final int[] a, final int[] a2) {
        if (a != null && a.length != 0) {
            return Arrays.equals(a, a2);
        }
        return a2 == null || a2.length == 0;
    }
    
    public static boolean equals(final long[] a, final long[] a2) {
        if (a != null && a.length != 0) {
            return Arrays.equals(a, a2);
        }
        return a2 == null || a2.length == 0;
    }
    
    public static boolean equals(final Object[] array, final Object[] array2) {
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        int length2;
        if (array2 == null) {
            length2 = 0;
        }
        else {
            length2 = array2.length;
        }
        int n = 0;
        int n2 = 0;
        while (true) {
            int n3 = n2;
            if (n < length) {
                n3 = n2;
                if (array[n] == null) {
                    ++n;
                    continue;
                }
            }
            while (n3 < length2 && array2[n3] == null) {
                ++n3;
            }
            final boolean b = n >= length;
            final boolean b2 = n3 >= length2;
            if (b && b2) {
                return true;
            }
            if (b != b2) {
                return false;
            }
            if (!array[n].equals(array2[n3])) {
                return false;
            }
            ++n;
            n2 = n3 + 1;
        }
    }
    
    public static int hashCode(final float[] a) {
        if (a != null && a.length != 0) {
            return Arrays.hashCode(a);
        }
        return 0;
    }
    
    public static int hashCode(final int[] a) {
        if (a != null && a.length != 0) {
            return Arrays.hashCode(a);
        }
        return 0;
    }
    
    public static int hashCode(final long[] a) {
        if (a != null && a.length != 0) {
            return Arrays.hashCode(a);
        }
        return 0;
    }
    
    public static int hashCode(final Object[] array) {
        int i = 0;
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        int n = 0;
        while (i < length) {
            final Object o = array[i];
            int n2 = n;
            if (o != null) {
                n2 = 31 * n + o.hashCode();
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    public static int zza(final byte[][] array) {
        int i = 0;
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        int n = 0;
        while (i < length) {
            final byte[] a = array[i];
            int n2 = n;
            if (a != null) {
                n2 = 31 * n + Arrays.hashCode(a);
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    public static void zza(final zzry zzry, final zzry zzry2) {
        if (zzry.zzbik != null) {
            zzry2.zzbik = zzry.zzbik.zzFH();
        }
    }
    
    public static boolean zza(final byte[][] array, final byte[][] array2) {
        int length;
        if (array == null) {
            length = 0;
        }
        else {
            length = array.length;
        }
        int length2;
        if (array2 == null) {
            length2 = 0;
        }
        else {
            length2 = array2.length;
        }
        int n = 0;
        int n2 = 0;
        while (true) {
            int n3 = n2;
            if (n < length) {
                n3 = n2;
                if (array[n] == null) {
                    ++n;
                    continue;
                }
            }
            while (n3 < length2 && array2[n3] == null) {
                ++n3;
            }
            final boolean b = n >= length;
            final boolean b2 = n3 >= length2;
            if (b && b2) {
                return true;
            }
            if (b != b2) {
                return false;
            }
            if (!Arrays.equals(array[n], array2[n3])) {
                return false;
            }
            ++n;
            n2 = n3 + 1;
        }
    }
}
