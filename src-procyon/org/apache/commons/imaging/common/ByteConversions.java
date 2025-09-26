// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.nio.ByteOrder;

public final class ByteConversions
{
    private ByteConversions() {
    }
    
    private static void toBytes(final double n, final ByteOrder byteOrder, final byte[] array, final int n2) {
        final long doubleToRawLongBits = Double.doubleToRawLongBits(n);
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            array[n2 + 0] = (byte)(doubleToRawLongBits >> 0 & 0xFFL);
            array[n2 + 1] = (byte)(doubleToRawLongBits >> 8 & 0xFFL);
            array[n2 + 2] = (byte)(doubleToRawLongBits >> 16 & 0xFFL);
            array[n2 + 3] = (byte)(doubleToRawLongBits >> 24 & 0xFFL);
            array[n2 + 4] = (byte)(doubleToRawLongBits >> 32 & 0xFFL);
            array[n2 + 5] = (byte)(doubleToRawLongBits >> 40 & 0xFFL);
            array[n2 + 6] = (byte)(doubleToRawLongBits >> 48 & 0xFFL);
            array[n2 + 7] = (byte)(doubleToRawLongBits >> 56 & 0xFFL);
        }
        else {
            array[n2 + 7] = (byte)(doubleToRawLongBits >> 0 & 0xFFL);
            array[n2 + 6] = (byte)(doubleToRawLongBits >> 8 & 0xFFL);
            array[n2 + 5] = (byte)(doubleToRawLongBits >> 16 & 0xFFL);
            array[n2 + 4] = (byte)(doubleToRawLongBits >> 24 & 0xFFL);
            array[n2 + 3] = (byte)(doubleToRawLongBits >> 32 & 0xFFL);
            array[n2 + 2] = (byte)(doubleToRawLongBits >> 40 & 0xFFL);
            array[n2 + 1] = (byte)(doubleToRawLongBits >> 48 & 0xFFL);
            array[n2 + 0] = (byte)(doubleToRawLongBits >> 56 & 0xFFL);
        }
    }
    
    private static void toBytes(final float n, final ByteOrder byteOrder, final byte[] array, final int n2) {
        final int floatToRawIntBits = Float.floatToRawIntBits(n);
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            array[n2 + 0] = (byte)(floatToRawIntBits >> 0 & 0xFF);
            array[n2 + 1] = (byte)(floatToRawIntBits >> 8 & 0xFF);
            array[n2 + 2] = (byte)(floatToRawIntBits >> 16 & 0xFF);
            array[n2 + 3] = (byte)(floatToRawIntBits >> 24 & 0xFF);
        }
        else {
            array[n2 + 3] = (byte)(floatToRawIntBits >> 0 & 0xFF);
            array[n2 + 2] = (byte)(floatToRawIntBits >> 8 & 0xFF);
            array[n2 + 1] = (byte)(floatToRawIntBits >> 16 & 0xFF);
            array[n2 + 0] = (byte)(floatToRawIntBits >> 24 & 0xFF);
        }
    }
    
    private static void toBytes(final int n, final ByteOrder byteOrder, final byte[] array, final int n2) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            array[n2 + 0] = (byte)(n >> 24);
            array[n2 + 1] = (byte)(n >> 16);
            array[n2 + 2] = (byte)(n >> 8);
            array[n2 + 3] = (byte)(n >> 0);
        }
        else {
            array[n2 + 3] = (byte)(n >> 24);
            array[n2 + 2] = (byte)(n >> 16);
            array[n2 + 1] = (byte)(n >> 8);
            array[n2 + 0] = (byte)(n >> 0);
        }
    }
    
    private static void toBytes(final RationalNumber rationalNumber, final ByteOrder byteOrder, final byte[] array, final int n) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            array[n + 0] = (byte)(rationalNumber.numerator >> 24);
            array[n + 1] = (byte)(rationalNumber.numerator >> 16);
            array[n + 2] = (byte)(rationalNumber.numerator >> 8);
            array[n + 3] = (byte)(rationalNumber.numerator >> 0);
            array[n + 4] = (byte)(rationalNumber.divisor >> 24);
            array[n + 5] = (byte)(rationalNumber.divisor >> 16);
            array[n + 6] = (byte)(rationalNumber.divisor >> 8);
            array[n + 7] = (byte)(rationalNumber.divisor >> 0);
        }
        else {
            array[n + 3] = (byte)(rationalNumber.numerator >> 24);
            array[n + 2] = (byte)(rationalNumber.numerator >> 16);
            array[n + 1] = (byte)(rationalNumber.numerator >> 8);
            array[n + 0] = (byte)(rationalNumber.numerator >> 0);
            array[n + 7] = (byte)(rationalNumber.divisor >> 24);
            array[n + 6] = (byte)(rationalNumber.divisor >> 16);
            array[n + 5] = (byte)(rationalNumber.divisor >> 8);
            array[n + 4] = (byte)(rationalNumber.divisor >> 0);
        }
    }
    
    private static void toBytes(final short n, final ByteOrder byteOrder, final byte[] array, final int n2) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            array[n2 + 0] = (byte)(n >> 8);
            array[n2 + 1] = (byte)(n >> 0);
        }
        else {
            array[n2 + 1] = (byte)(n >> 8);
            array[n2 + 0] = (byte)(n >> 0);
        }
    }
    
    public static byte[] toBytes(final double n, final ByteOrder byteOrder) {
        final byte[] array = new byte[8];
        toBytes(n, byteOrder, array, 0);
        return array;
    }
    
    public static byte[] toBytes(final float n, final ByteOrder byteOrder) {
        final byte[] array = new byte[4];
        toBytes(n, byteOrder, array, 0);
        return array;
    }
    
    public static byte[] toBytes(final int n, final ByteOrder byteOrder) {
        final byte[] array = new byte[4];
        toBytes(n, byteOrder, array, 0);
        return array;
    }
    
    public static byte[] toBytes(final RationalNumber rationalNumber, final ByteOrder byteOrder) {
        final byte[] array = new byte[8];
        toBytes(rationalNumber, byteOrder, array, 0);
        return array;
    }
    
    public static byte[] toBytes(final short n, final ByteOrder byteOrder) {
        final byte[] array = new byte[2];
        toBytes(n, byteOrder, array, 0);
        return array;
    }
    
    private static byte[] toBytes(final double[] array, final int n, final int n2, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[n2 * 8];
        for (int i = 0; i < n2; ++i) {
            toBytes(array[n + i], byteOrder, array2, i * 8);
        }
        return array2;
    }
    
    public static byte[] toBytes(final double[] array, final ByteOrder byteOrder) {
        return toBytes(array, 0, array.length, byteOrder);
    }
    
    private static byte[] toBytes(final float[] array, final int n, final int n2, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[n2 * 4];
        for (int i = 0; i < n2; ++i) {
            toBytes(array[n + i], byteOrder, array2, i * 4);
        }
        return array2;
    }
    
    public static byte[] toBytes(final float[] array, final ByteOrder byteOrder) {
        return toBytes(array, 0, array.length, byteOrder);
    }
    
    private static byte[] toBytes(final int[] array, final int n, final int n2, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[n2 * 4];
        for (int i = 0; i < n2; ++i) {
            toBytes(array[n + i], byteOrder, array2, i * 4);
        }
        return array2;
    }
    
    public static byte[] toBytes(final int[] array, final ByteOrder byteOrder) {
        return toBytes(array, 0, array.length, byteOrder);
    }
    
    private static byte[] toBytes(final RationalNumber[] array, final int n, final int n2, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[n2 * 8];
        for (int i = 0; i < n2; ++i) {
            toBytes(array[n + i], byteOrder, array2, i * 8);
        }
        return array2;
    }
    
    public static byte[] toBytes(final RationalNumber[] array, final ByteOrder byteOrder) {
        return toBytes(array, 0, array.length, byteOrder);
    }
    
    private static byte[] toBytes(final short[] array, final int n, final int n2, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[n2 * 2];
        for (int i = 0; i < n2; ++i) {
            toBytes(array[n + i], byteOrder, array2, i * 2);
        }
        return array2;
    }
    
    public static byte[] toBytes(final short[] array, final ByteOrder byteOrder) {
        return toBytes(array, 0, array.length, byteOrder);
    }
    
    private static double toDouble(final byte[] array, final int n, final ByteOrder byteOrder) {
        final long n2 = (long)array[n + 0] & 0xFFL;
        final long n3 = (long)array[n + 1] & 0xFFL;
        final long n4 = (long)array[n + 2] & 0xFFL;
        final long n5 = (long)array[n + 3] & 0xFFL;
        final long n6 = (long)array[n + 4] & 0xFFL;
        final long n7 = (long)array[n + 5] & 0xFFL;
        final long n8 = (long)array[n + 6] & 0xFFL;
        final long n9 = (long)array[n + 7] & 0xFFL;
        long n10;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            n10 = (n9 << 0 | (n2 << 56 | n3 << 48 | n4 << 40 | n5 << 32 | n6 << 24 | n7 << 16 | n8 << 8));
        }
        else {
            n10 = (n9 << 56 | n8 << 48 | n7 << 40 | n6 << 32 | n5 << 24 | n4 << 16 | n3 << 8 | n2 << 0);
        }
        return Double.longBitsToDouble(n10);
    }
    
    public static double toDouble(final byte[] array, final ByteOrder byteOrder) {
        return toDouble(array, 0, byteOrder);
    }
    
    private static double[] toDoubles(final byte[] array, final int n, int i, final ByteOrder byteOrder) {
        double[] array2;
        for (array2 = new double[i / 8], i = 0; i < array2.length; ++i) {
            array2[i] = toDouble(array, 8 * i + n, byteOrder);
        }
        return array2;
    }
    
    public static double[] toDoubles(final byte[] array, final ByteOrder byteOrder) {
        return toDoubles(array, 0, array.length, byteOrder);
    }
    
    private static float toFloat(final byte[] array, int n, final ByteOrder byteOrder) {
        final int n2 = array[n + 0] & 0xFF;
        final int n3 = array[n + 1] & 0xFF;
        final int n4 = array[n + 2] & 0xFF;
        n = (array[n + 3] & 0xFF);
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            n = (n << 0 | (n2 << 24 | n3 << 16 | n4 << 8));
        }
        else {
            n = (n << 24 | n4 << 16 | n3 << 8 | n2 << 0);
        }
        return Float.intBitsToFloat(n);
    }
    
    public static float toFloat(final byte[] array, final ByteOrder byteOrder) {
        return toFloat(array, 0, byteOrder);
    }
    
    private static float[] toFloats(final byte[] array, final int n, int i, final ByteOrder byteOrder) {
        float[] array2;
        for (array2 = new float[i / 4], i = 0; i < array2.length; ++i) {
            array2[i] = toFloat(array, 4 * i + n, byteOrder);
        }
        return array2;
    }
    
    public static float[] toFloats(final byte[] array, final ByteOrder byteOrder) {
        return toFloats(array, 0, array.length, byteOrder);
    }
    
    public static int toInt(final byte[] array, int n, final ByteOrder byteOrder) {
        final int n2 = array[n + 0] & 0xFF;
        final int n3 = array[n + 1] & 0xFF;
        final int n4 = array[n + 2] & 0xFF;
        n = (array[n + 3] & 0xFF);
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return n | (n2 << 24 | n3 << 16 | n4 << 8);
        }
        return n << 24 | n4 << 16 | n3 << 8 | n2;
    }
    
    public static int toInt(final byte[] array, final ByteOrder byteOrder) {
        return toInt(array, 0, byteOrder);
    }
    
    private static int[] toInts(final byte[] array, final int n, int i, final ByteOrder byteOrder) {
        int[] array2;
        for (array2 = new int[i / 4], i = 0; i < array2.length; ++i) {
            array2[i] = toInt(array, 4 * i + n, byteOrder);
        }
        return array2;
    }
    
    public static int[] toInts(final byte[] array, final ByteOrder byteOrder) {
        return toInts(array, 0, array.length, byteOrder);
    }
    
    private static RationalNumber toRational(final byte[] array, int n, final ByteOrder byteOrder) {
        final int n2 = array[n + 0] & 0xFF;
        final int n3 = array[n + 1] & 0xFF;
        final int n4 = array[n + 2] & 0xFF;
        final int n5 = array[n + 3] & 0xFF;
        final int n6 = array[n + 4] & 0xFF;
        final int n7 = array[n + 5] & 0xFF;
        final int n8 = array[n + 6] & 0xFF;
        final int n9 = array[n + 7] & 0xFF;
        int n10;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            n = (n2 << 24 | n3 << 16 | n4 << 8 | n5);
            n10 = (n9 | (n6 << 24 | n7 << 16 | n8 << 8));
        }
        else {
            n = (n5 << 24 | n4 << 16 | n3 << 8 | n2);
            n10 = (n9 << 24 | n8 << 16 | n7 << 8 | n6);
        }
        return new RationalNumber(n, n10);
    }
    
    public static RationalNumber toRational(final byte[] array, final ByteOrder byteOrder) {
        return toRational(array, 0, byteOrder);
    }
    
    private static RationalNumber[] toRationals(final byte[] array, final int n, int i, final ByteOrder byteOrder) {
        RationalNumber[] array2;
        for (array2 = new RationalNumber[i / 8], i = 0; i < array2.length; ++i) {
            array2[i] = toRational(array, 8 * i + n, byteOrder);
        }
        return array2;
    }
    
    public static RationalNumber[] toRationals(final byte[] array, final ByteOrder byteOrder) {
        return toRationals(array, 0, array.length, byteOrder);
    }
    
    private static short toShort(final byte[] array, final int n, final ByteOrder byteOrder) {
        return (short)toUInt16(array, n, byteOrder);
    }
    
    public static short toShort(final byte[] array, final ByteOrder byteOrder) {
        return toShort(array, 0, byteOrder);
    }
    
    private static short[] toShorts(final byte[] array, final int n, int i, final ByteOrder byteOrder) {
        short[] array2;
        for (array2 = new short[i / 2], i = 0; i < array2.length; ++i) {
            array2[i] = toShort(array, 2 * i + n, byteOrder);
        }
        return array2;
    }
    
    public static short[] toShorts(final byte[] array, final ByteOrder byteOrder) {
        return toShorts(array, 0, array.length, byteOrder);
    }
    
    public static int toUInt16(final byte[] array, int n, final ByteOrder byteOrder) {
        final int n2 = array[n + 0] & 0xFF;
        n = (array[n + 1] & 0xFF);
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return n | n2 << 8;
        }
        return n << 8 | n2;
    }
    
    public static int toUInt16(final byte[] array, final ByteOrder byteOrder) {
        return toUInt16(array, 0, byteOrder);
    }
    
    private static int[] toUInt16s(final byte[] array, final int n, int i, final ByteOrder byteOrder) {
        int[] array2;
        for (array2 = new int[i / 2], i = 0; i < array2.length; ++i) {
            array2[i] = toUInt16(array, 2 * i + n, byteOrder);
        }
        return array2;
    }
    
    public static int[] toUInt16s(final byte[] array, final ByteOrder byteOrder) {
        return toUInt16s(array, 0, array.length, byteOrder);
    }
}
