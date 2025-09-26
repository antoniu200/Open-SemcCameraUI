// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

public class zzmf
{
    public static final int[] EMPTY_INTS;
    public static final long[] EMPTY_LONGS;
    public static final Object[] EMPTY_OBJECTS;
    
    static {
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EMPTY_OBJECTS = new Object[0];
    }
    
    public static int binarySearch(final int[] array, int n, final int n2) {
        --n;
        int i = 0;
        while (i <= n) {
            final int n3 = i + n >>> 1;
            final int n4 = array[n3];
            if (n4 < n2) {
                i = n3 + 1;
            }
            else {
                if (n4 <= n2) {
                    return n3;
                }
                n = n3 - 1;
            }
        }
        return ~i;
    }
    
    public static boolean equal(final Object o, final Object obj) {
        return o == obj || (o != null && o.equals(obj));
    }
}
