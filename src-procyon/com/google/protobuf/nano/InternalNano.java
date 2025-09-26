// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.util.Arrays;
import java.nio.charset.Charset;

public final class InternalNano
{
    protected static final Charset ISO_8859_1;
    public static final Object LAZY_INIT_LOCK;
    protected static final Charset UTF_8;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        LAZY_INIT_LOCK = new Object();
    }
    
    private InternalNano() {
    }
    
    public static byte[] bytesDefaultValue(final String s) {
        return s.getBytes(InternalNano.ISO_8859_1);
    }
    
    public static void cloneUnknownFieldData(final ExtendableMessageNano extendableMessageNano, final ExtendableMessageNano extendableMessageNano2) {
        if (extendableMessageNano.unknownFieldData != null) {
            extendableMessageNano2.unknownFieldData = extendableMessageNano.unknownFieldData.clone();
        }
    }
    
    public static byte[] copyFromUtf8(final String s) {
        return s.getBytes(InternalNano.UTF_8);
    }
    
    public static boolean equals(final double[] a, final double[] a2) {
        if (a != null && a.length != 0) {
            return Arrays.equals(a, a2);
        }
        return a2 == null || a2.length == 0;
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
    
    public static boolean equals(final boolean[] a, final boolean[] a2) {
        if (a != null && a.length != 0) {
            return Arrays.equals(a, a2);
        }
        return a2 == null || a2.length == 0;
    }
    
    public static boolean equals(final byte[][] array, final byte[][] array2) {
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
    
    public static int hashCode(final double[] a) {
        int hashCode;
        if (a != null && a.length != 0) {
            hashCode = Arrays.hashCode(a);
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    public static int hashCode(final float[] a) {
        int hashCode;
        if (a != null && a.length != 0) {
            hashCode = Arrays.hashCode(a);
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    public static int hashCode(final int[] a) {
        int hashCode;
        if (a != null && a.length != 0) {
            hashCode = Arrays.hashCode(a);
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    public static int hashCode(final long[] a) {
        int hashCode;
        if (a != null && a.length != 0) {
            hashCode = Arrays.hashCode(a);
        }
        else {
            hashCode = 0;
        }
        return hashCode;
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
    
    public static int hashCode(final boolean[] a) {
        int hashCode;
        if (a != null && a.length != 0) {
            hashCode = Arrays.hashCode(a);
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    public static int hashCode(final byte[][] array) {
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
    
    public static String stringDefaultValue(final String s) {
        return new String(s.getBytes(InternalNano.ISO_8859_1), InternalNano.UTF_8);
    }
}
