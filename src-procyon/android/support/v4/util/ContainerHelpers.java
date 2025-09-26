// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.util;

class ContainerHelpers
{
    static final int[] EMPTY_INTS;
    static final long[] EMPTY_LONGS;
    static final Object[] EMPTY_OBJECTS;
    
    static {
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EMPTY_OBJECTS = new Object[0];
    }
    
    private ContainerHelpers() {
    }
    
    static int binarySearch(final int[] array, int n, final int n2) {
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
    
    static int binarySearch(final long[] array, int n, final long n2) {
        --n;
        int i = 0;
        while (i <= n) {
            final int n3 = i + n >>> 1;
            final long n4 = lcmp(array[n3], n2);
            if (n4 < 0) {
                i = n3 + 1;
            }
            else {
                if (n4 <= 0) {
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
    
    public static int idealByteArraySize(final int n) {
        for (int i = 4; i < 32; ++i) {
            final int n2 = (1 << i) - 12;
            if (n <= n2) {
                return n2;
            }
        }
        return n;
    }
    
    public static int idealIntArraySize(final int n) {
        return idealByteArraySize(n * 4) / 4;
    }
    
    public static int idealLongArraySize(final int n) {
        return idealByteArraySize(n * 8) / 8;
    }
}
