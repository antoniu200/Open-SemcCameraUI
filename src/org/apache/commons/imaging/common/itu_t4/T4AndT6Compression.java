package org.apache.commons.imaging.common.itu_t4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.itu_t4.T4_T6_Tables;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class T4AndT6Compression {
    public static final int BLACK = 1;
    public static final int WHITE = 0;
    private static final HuffmanTree<Integer> WHITE_RUN_LENGTHS = new HuffmanTree<>();
    private static final HuffmanTree<Integer> BLACK_RUN_LENGTHS = new HuffmanTree<>();
    private static final HuffmanTree<T4_T6_Tables.Entry> CONTROL_CODES = new HuffmanTree<>();

    static {
        try {
            for (T4_T6_Tables.Entry entry : T4_T6_Tables.WHITE_TERMINATING_CODES) {
                WHITE_RUN_LENGTHS.insert(entry.bitString, entry.value);
            }
            for (T4_T6_Tables.Entry entry2 : T4_T6_Tables.WHITE_MAKE_UP_CODES) {
                WHITE_RUN_LENGTHS.insert(entry2.bitString, entry2.value);
            }
            for (T4_T6_Tables.Entry entry3 : T4_T6_Tables.BLACK_TERMINATING_CODES) {
                BLACK_RUN_LENGTHS.insert(entry3.bitString, entry3.value);
            }
            for (T4_T6_Tables.Entry entry4 : T4_T6_Tables.BLACK_MAKE_UP_CODES) {
                BLACK_RUN_LENGTHS.insert(entry4.bitString, entry4.value);
            }
            for (T4_T6_Tables.Entry entry5 : T4_T6_Tables.ADDITIONAL_MAKE_UP_CODES) {
                WHITE_RUN_LENGTHS.insert(entry5.bitString, entry5.value);
                BLACK_RUN_LENGTHS.insert(entry5.bitString, entry5.value);
            }
            CONTROL_CODES.insert(T4_T6_Tables.EOL.bitString, T4_T6_Tables.EOL);
            CONTROL_CODES.insert(T4_T6_Tables.EOL13.bitString, T4_T6_Tables.EOL13);
            CONTROL_CODES.insert(T4_T6_Tables.EOL14.bitString, T4_T6_Tables.EOL14);
            CONTROL_CODES.insert(T4_T6_Tables.EOL15.bitString, T4_T6_Tables.EOL15);
            CONTROL_CODES.insert(T4_T6_Tables.EOL16.bitString, T4_T6_Tables.EOL16);
            CONTROL_CODES.insert(T4_T6_Tables.EOL17.bitString, T4_T6_Tables.EOL17);
            CONTROL_CODES.insert(T4_T6_Tables.EOL18.bitString, T4_T6_Tables.EOL18);
            CONTROL_CODES.insert(T4_T6_Tables.EOL19.bitString, T4_T6_Tables.EOL19);
            CONTROL_CODES.insert(T4_T6_Tables.P.bitString, T4_T6_Tables.P);
            CONTROL_CODES.insert(T4_T6_Tables.H.bitString, T4_T6_Tables.H);
            CONTROL_CODES.insert(T4_T6_Tables.V0.bitString, T4_T6_Tables.V0);
            CONTROL_CODES.insert(T4_T6_Tables.VL1.bitString, T4_T6_Tables.VL1);
            CONTROL_CODES.insert(T4_T6_Tables.VL2.bitString, T4_T6_Tables.VL2);
            CONTROL_CODES.insert(T4_T6_Tables.VL3.bitString, T4_T6_Tables.VL3);
            CONTROL_CODES.insert(T4_T6_Tables.VR1.bitString, T4_T6_Tables.VR1);
            CONTROL_CODES.insert(T4_T6_Tables.VR2.bitString, T4_T6_Tables.VR2);
            CONTROL_CODES.insert(T4_T6_Tables.VR3.bitString, T4_T6_Tables.VR3);
        } catch (HuffmanTreeException e) {
            throw new Error(e);
        }
    }

    private T4AndT6Compression() {
    }

    private static void compress1DLine(BitInputStreamFlexible bitInputStreamFlexible, BitArrayOutputStream bitArrayOutputStream, int[] iArr, int i) throws ImageWriteException {
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            try {
                int bits = bitInputStreamFlexible.readBits(1);
                if (iArr != null) {
                    iArr[i4] = bits;
                }
                if (i3 == bits) {
                    i2++;
                } else {
                    writeRunLength(bitArrayOutputStream, i2, i3);
                    i2 = 1;
                    i3 = bits;
                }
            } catch (IOException e) {
                throw new ImageWriteException("Error reading image to compress", (Throwable) e);
            }
        }
        writeRunLength(bitArrayOutputStream, i2, i3);
    }

    public static byte[] compressModifiedHuffman(byte[] bArr, int i, int i2) throws ImageWriteException {
        BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(bArr));
        BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        for (int i3 = 0; i3 < i2; i3++) {
            compress1DLine(bitInputStreamFlexible, bitArrayOutputStream, null, i);
            bitInputStreamFlexible.flushCache();
            bitArrayOutputStream.flush();
        }
        return bitArrayOutputStream.toByteArray();
    }

    public static byte[] decompressModifiedHuffman(byte[] bArr, int i, int i2) throws Throwable {
        BitArrayOutputStream bitArrayOutputStream;
        BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(bArr));
        try {
            bitArrayOutputStream = new BitArrayOutputStream();
            for (int i3 = 0; i3 < i2; i3++) {
                int i4 = 0;
                int i5 = 0;
                while (i4 < i) {
                    try {
                        int totalRunLength = readTotalRunLength(bitInputStreamFlexible, i5);
                        for (int i6 = 0; i6 < totalRunLength; i6++) {
                            bitArrayOutputStream.writeBit(i5);
                        }
                        i5 = 1 - i5;
                        i4 += totalRunLength;
                    } catch (Throwable th) {
                        th = th;
                        try {
                            IoUtils.closeQuietly(false, bitArrayOutputStream);
                            throw th;
                        } catch (IOException e) {
                            throw new ImageReadException("I/O error", e);
                        }
                    }
                }
                if (i4 == i) {
                    bitInputStreamFlexible.flushCache();
                    bitArrayOutputStream.flush();
                } else if (i4 > i) {
                    throw new ImageReadException("Unrecoverable row length error in image row " + i3);
                }
            }
            byte[] byteArray = bitArrayOutputStream.toByteArray();
            try {
                IoUtils.closeQuietly(true, bitArrayOutputStream);
                return byteArray;
            } catch (IOException e2) {
                throw new ImageReadException("I/O error", e2);
            }
        } catch (Throwable th2) {
            th = th2;
            bitArrayOutputStream = null;
        }
    }

    public static byte[] compressT4_1D(byte[] bArr, int i, int i2, boolean z) throws ImageWriteException {
        BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(bArr));
        BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        if (z) {
            T4_T6_Tables.EOL16.writeBits(bitArrayOutputStream);
        } else {
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
        }
        for (int i3 = 0; i3 < i2; i3++) {
            compress1DLine(bitInputStreamFlexible, bitArrayOutputStream, null, i);
            if (z) {
                int bitsAvailableInCurrentByte = bitArrayOutputStream.getBitsAvailableInCurrentByte();
                if (bitsAvailableInCurrentByte < 4) {
                    bitArrayOutputStream.flush();
                    bitsAvailableInCurrentByte = 8;
                }
                while (bitsAvailableInCurrentByte > 4) {
                    bitArrayOutputStream.writeBit(0);
                    bitsAvailableInCurrentByte--;
                }
            }
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
            bitInputStreamFlexible.flushCache();
        }
        return bitArrayOutputStream.toByteArray();
    }

    public static byte[] decompressT4_1D(byte[] bArr, int i, int i2, boolean z) throws Throwable {
        BitArrayOutputStream bitArrayOutputStream;
        BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(bArr));
        try {
            bitArrayOutputStream = new BitArrayOutputStream();
            for (int i3 = 0; i3 < i2; i3++) {
                try {
                    try {
                        if (!isEOL(CONTROL_CODES.decode(bitInputStreamFlexible), z)) {
                            throw new ImageReadException("Expected EOL not found");
                        }
                        int i4 = 0;
                        int i5 = 0;
                        while (i4 < i) {
                            int totalRunLength = readTotalRunLength(bitInputStreamFlexible, i5);
                            for (int i6 = 0; i6 < totalRunLength; i6++) {
                                bitArrayOutputStream.writeBit(i5);
                            }
                            i5 = 1 - i5;
                            i4 += totalRunLength;
                        }
                        if (i4 == i) {
                            bitArrayOutputStream.flush();
                        } else if (i4 > i) {
                            throw new ImageReadException("Unrecoverable row length error in image row " + i3);
                        }
                    } catch (HuffmanTreeException e) {
                        throw new ImageReadException("Decompression error", e);
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        IoUtils.closeQuietly(false, bitArrayOutputStream);
                        throw th;
                    } catch (IOException e2) {
                        throw new ImageReadException("I/O error", e2);
                    }
                }
            }
            byte[] byteArray = bitArrayOutputStream.toByteArray();
            try {
                IoUtils.closeQuietly(true, bitArrayOutputStream);
                return byteArray;
            } catch (IOException e3) {
                throw new ImageReadException("I/O error", e3);
            }
        } catch (Throwable th2) {
            th = th2;
            bitArrayOutputStream = null;
        }
    }

    public static byte[] compressT4_2D(byte[] bArr, int i, int i2, boolean z, int i3) throws ImageWriteException {
        T4_T6_Tables.Entry entry;
        T4_T6_Tables.Entry entry2;
        int iNextChangingElement;
        BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(bArr));
        BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        int[] iArr = new int[i];
        int[] iArr2 = new int[i];
        if (z) {
            T4_T6_Tables.EOL16.writeBits(bitArrayOutputStream);
        } else {
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
        }
        int i4 = 0;
        int[] iArr3 = iArr;
        int[] iArr4 = iArr2;
        int i5 = 0;
        int i6 = 0;
        while (i5 < i2) {
            if (i6 > 0) {
                bitArrayOutputStream.writeBit(i4);
                for (int i7 = i4; i7 < i; i7++) {
                    try {
                        iArr4[i7] = bitInputStreamFlexible.readBits(1);
                    } catch (IOException e) {
                        throw new ImageWriteException("Error reading image to compress", (Throwable) e);
                    }
                }
                int iNextChangingElement2 = nextChangingElement(iArr4, i4, i4);
                int iNextChangingElement3 = nextChangingElement(iArr3, i4, i4);
                int iNextChangingElement4 = nextChangingElement(iArr3, 1, iNextChangingElement3 + 1);
                int i8 = iNextChangingElement3;
                int i9 = iNextChangingElement2;
                int i10 = i4;
                while (i10 < i) {
                    if (iNextChangingElement4 < i9) {
                        T4_T6_Tables.P.writeBits(bitArrayOutputStream);
                        i10 = iNextChangingElement4;
                    } else {
                        int i11 = i9 - i8;
                        if (-3 <= i11 && i11 <= 3) {
                            if (i11 == -3) {
                                entry2 = T4_T6_Tables.VL3;
                            } else if (i11 == -2) {
                                entry2 = T4_T6_Tables.VL2;
                            } else if (i11 == -1) {
                                entry2 = T4_T6_Tables.VL1;
                            } else if (i11 == 0) {
                                entry2 = T4_T6_Tables.V0;
                            } else {
                                if (i11 == 1) {
                                    entry = T4_T6_Tables.VR1;
                                } else if (i11 == 2) {
                                    entry = T4_T6_Tables.VR2;
                                } else {
                                    entry = T4_T6_Tables.VR3;
                                }
                                entry.writeBits(bitArrayOutputStream);
                                i4 = 1 - i4;
                                i10 = i9;
                            }
                            entry = entry2;
                            entry.writeBits(bitArrayOutputStream);
                            i4 = 1 - i4;
                            i10 = i9;
                        } else {
                            int i12 = 1 - i4;
                            int iNextChangingElement5 = nextChangingElement(iArr4, i12, i9 + 1);
                            T4_T6_Tables.H.writeBits(bitArrayOutputStream);
                            writeRunLength(bitArrayOutputStream, i9 - i10, i4);
                            writeRunLength(bitArrayOutputStream, iNextChangingElement5 - i9, i12);
                            i10 = iNextChangingElement5;
                        }
                    }
                    int iChangingElementAt = changingElementAt(iArr3, i10);
                    int i13 = i10 + 1;
                    int iNextChangingElement6 = nextChangingElement(iArr4, i4, i13);
                    if (i4 == iChangingElementAt) {
                        iNextChangingElement = nextChangingElement(iArr3, iChangingElementAt, i13);
                    } else {
                        iNextChangingElement = nextChangingElement(iArr3, 1 - iChangingElementAt, nextChangingElement(iArr3, iChangingElementAt, i13) + 1);
                    }
                    i8 = iNextChangingElement;
                    iNextChangingElement4 = nextChangingElement(iArr3, 1 - i4, iNextChangingElement + 1);
                    i9 = iNextChangingElement6;
                }
                int[] iArr5 = iArr4;
                iArr4 = iArr3;
                iArr3 = iArr5;
            } else {
                bitArrayOutputStream.writeBit(1);
                compress1DLine(bitInputStreamFlexible, bitArrayOutputStream, iArr3, i);
            }
            if (z) {
                int bitsAvailableInCurrentByte = bitArrayOutputStream.getBitsAvailableInCurrentByte();
                if (bitsAvailableInCurrentByte < 4) {
                    bitArrayOutputStream.flush();
                    bitsAvailableInCurrentByte = 8;
                }
                while (bitsAvailableInCurrentByte > 4) {
                    bitArrayOutputStream.writeBit(0);
                    bitsAvailableInCurrentByte--;
                }
            }
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
            i6++;
            if (i6 == i3) {
                i6 = 0;
            }
            bitInputStreamFlexible.flushCache();
            i5++;
            i4 = 0;
        }
        return bitArrayOutputStream.toByteArray();
    }

    public static byte[] decompressT4_2D(final byte[] buf, final int n, final int n2, final boolean b) throws ImageReadException {
        final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(buf));
        final BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        final int[] array = new int[n];
        int i = 0;
        while (i < n2) {
            try {
                if (!isEOL(T4AndT6Compression.CONTROL_CODES.decode(bitInputStreamFlexible), b)) {
                    throw new ImageReadException("Expected EOL not found");
                }
                int n6;
                if (bitInputStreamFlexible.readBits(1) == 0) {
                    int nextChangingElement = nextChangingElement(array, 0, 0);
                    int nextChangingElement2 = nextChangingElement(array, 1, nextChangingElement + 1);
                    int n3 = 0;
                    int n5;
                    int n4 = n5 = 0;
                    while (true) {
                        n6 = n5;
                        if (n4 >= n) {
                            break;
                        }
                        final T4_T6_Tables.Entry entry = T4AndT6Compression.CONTROL_CODES.decode(bitInputStreamFlexible);
                        if (entry == T4_T6_Tables.P) {
                            fillRange(bitArrayOutputStream, array, n4, nextChangingElement2, n3);
                        }
                        else if (entry == T4_T6_Tables.H) {
                            final int n7 = readTotalRunLength(bitInputStreamFlexible, n3) + n4;
                            fillRange(bitArrayOutputStream, array, n4, n7, n3);
                            final int n8 = 1 - n3;
                            nextChangingElement2 = readTotalRunLength(bitInputStreamFlexible, n8) + n7;
                            fillRange(bitArrayOutputStream, array, n7, nextChangingElement2, n8);
                        }
                        else {
                            int n9;
                            if (entry == T4_T6_Tables.V0) {
                                n9 = 0;
                            }
                            else if (entry == T4_T6_Tables.VL1) {
                                n9 = -1;
                            }
                            else if (entry == T4_T6_Tables.VL2) {
                                n9 = -2;
                            }
                            else if (entry == T4_T6_Tables.VL3) {
                                n9 = -3;
                            }
                            else if (entry == T4_T6_Tables.VR1) {
                                n9 = 1;
                            }
                            else if (entry == T4_T6_Tables.VR2) {
                                n9 = 2;
                            }
                            else {
                                if (entry != T4_T6_Tables.VR3) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Invalid/unknown T.4 control code ");
                                    sb.append(entry.bitString);
                                    throw new ImageReadException(sb.toString());
                                }
                                n9 = 3;
                            }
                            nextChangingElement2 = nextChangingElement + n9;
                            fillRange(bitArrayOutputStream, array, n4, nextChangingElement2, n3);
                            n3 = 1 - n3;
                        }
                        final int changingElement = changingElementAt(array, nextChangingElement2);
                        int n10;
                        if (n3 == changingElement) {
                            n10 = nextChangingElement(array, changingElement, nextChangingElement2 + 1);
                        }
                        else {
                            n10 = nextChangingElement(array, 1 - changingElement, nextChangingElement(array, changingElement, nextChangingElement2 + 1) + 1);
                        }
                        nextChangingElement = n10;
                        final int nextChangingElement3 = nextChangingElement(array, 1 - n3, nextChangingElement + 1);
                        n4 = nextChangingElement2;
                        n5 = nextChangingElement2;
                        nextChangingElement2 = nextChangingElement3;
                    }
                }
                else {
                    int n11 = 0;
                    int n12 = 0;
                    while (true) {
                        n6 = n12;
                        if (n12 >= n) {
                            break;
                        }
                        final int totalRunLength = readTotalRunLength(bitInputStreamFlexible, n11);
                        for (int j = 0; j < totalRunLength; ++j) {
                            bitArrayOutputStream.writeBit(n11);
                            array[n12 + j] = n11;
                        }
                        n11 = 1 - n11;
                        n12 += totalRunLength;
                    }
                }
                if (n6 == n) {
                    bitArrayOutputStream.flush();
                }
                else if (n6 > n) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unrecoverable row length error in image row ");
                    sb2.append(i);
                    throw new ImageReadException(sb2.toString());
                }
                ++i;
                continue;
            }
            catch (final HuffmanTreeException ex) {
                throw new ImageReadException("Decompression error", ex);
            }
            catch (final IOException ex2) {
                throw new ImageReadException("Decompression error", ex2);
            }
            break;
        }
        return bitArrayOutputStream.toByteArray();
    }

    public static byte[] compressT6(byte[] bArr, int i, int i2) throws Throwable {
        BitInputStreamFlexible bitInputStreamFlexible;
        T4_T6_Tables.Entry entry;
        int iNextChangingElement;
        try {
            bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(bArr));
        } catch (Throwable th) {
            th = th;
            bitInputStreamFlexible = null;
        }
        try {
            BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
            int[] iArr = new int[i];
            int[] iArr2 = new int[i];
            int i3 = 0;
            while (i3 < i2) {
                for (int i4 = 0; i4 < i; i4++) {
                    try {
                        iArr[i4] = bitInputStreamFlexible.readBits(1);
                    } catch (IOException e) {
                        throw new ImageWriteException("Error reading image to compress", (Throwable) e);
                    }
                }
                int iNextChangingElement2 = nextChangingElement(iArr, 0, 0);
                int iNextChangingElement3 = nextChangingElement(iArr2, 0, 0);
                int iNextChangingElement4 = nextChangingElement(iArr2, 1, iNextChangingElement3 + 1);
                int i5 = 0;
                int i6 = iNextChangingElement3;
                int i7 = iNextChangingElement2;
                int i8 = 0;
                while (i8 < i) {
                    if (iNextChangingElement4 < i7) {
                        T4_T6_Tables.P.writeBits(bitArrayOutputStream);
                        i8 = iNextChangingElement4;
                    } else {
                        int i9 = i7 - i6;
                        if (-3 <= i9 && i9 <= 3) {
                            if (i9 == -3) {
                                entry = T4_T6_Tables.VL3;
                            } else if (i9 == -2) {
                                entry = T4_T6_Tables.VL2;
                            } else if (i9 == -1) {
                                entry = T4_T6_Tables.VL1;
                            } else if (i9 == 0) {
                                entry = T4_T6_Tables.V0;
                            } else if (i9 == 1) {
                                entry = T4_T6_Tables.VR1;
                            } else if (i9 == 2) {
                                entry = T4_T6_Tables.VR2;
                            } else {
                                entry = T4_T6_Tables.VR3;
                            }
                            entry.writeBits(bitArrayOutputStream);
                            i5 = 1 - i5;
                            i8 = i7;
                        } else {
                            int i10 = 1 - i5;
                            int iNextChangingElement5 = nextChangingElement(iArr, i10, i7 + 1);
                            T4_T6_Tables.H.writeBits(bitArrayOutputStream);
                            writeRunLength(bitArrayOutputStream, i7 - i8, i5);
                            writeRunLength(bitArrayOutputStream, iNextChangingElement5 - i7, i10);
                            i8 = iNextChangingElement5;
                        }
                    }
                    int iChangingElementAt = changingElementAt(iArr2, i8);
                    int i11 = i8 + 1;
                    int iNextChangingElement6 = nextChangingElement(iArr, i5, i11);
                    if (i5 == iChangingElementAt) {
                        iNextChangingElement = nextChangingElement(iArr2, iChangingElementAt, i11);
                    } else {
                        iNextChangingElement = nextChangingElement(iArr2, 1 - iChangingElementAt, nextChangingElement(iArr2, iChangingElementAt, i11) + 1);
                    }
                    iNextChangingElement4 = nextChangingElement(iArr2, 1 - i5, iNextChangingElement + 1);
                    i6 = iNextChangingElement;
                    i7 = iNextChangingElement6;
                }
                bitInputStreamFlexible.flushCache();
                i3++;
                int[] iArr3 = iArr;
                iArr = iArr2;
                iArr2 = iArr3;
            }
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
            byte[] byteArray = bitArrayOutputStream.toByteArray();
            try {
                IoUtils.closeQuietly(true, bitInputStreamFlexible);
                return byteArray;
            } catch (IOException e2) {
                throw new ImageWriteException("I/O error", (Throwable) e2);
            }
        } catch (Throwable th2) {
            th = th2;
            try {
                IoUtils.closeQuietly(false, bitInputStreamFlexible);
                throw th;
            } catch (IOException e3) {
                throw new ImageWriteException("I/O error", (Throwable) e3);
            }
        }
    }

    public static byte[] decompressT6(final byte[] buf, final int n, final int n2) throws ImageReadException {
        final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(buf));
        final BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        final int[] array = new int[n];
        int i = 0;
        while (i < n2) {
            try {
                int nextChangingElement = nextChangingElement(array, 0, 0);
                int nextChangingElement2 = nextChangingElement(array, 1, nextChangingElement + 1);
                int n3 = 0;
                int n4;
                int n6 = 0;
                int n10;
                int nextChangingElement3;
                for (int j = n4 = 0; j < n; j = nextChangingElement2, n4 = nextChangingElement2, nextChangingElement2 = nextChangingElement3, n3 = n6, nextChangingElement = n10) {
                    final T4_T6_Tables.Entry entry = T4AndT6Compression.CONTROL_CODES.decode(bitInputStreamFlexible);
                    Label_0284: {
                        if (entry == T4_T6_Tables.P) {
                            fillRange(bitArrayOutputStream, array, j, nextChangingElement2, n3);
                        }
                        else {
                            if (entry != T4_T6_Tables.H) {
                                int n5;
                                if (entry == T4_T6_Tables.V0) {
                                    n5 = 0;
                                }
                                else if (entry == T4_T6_Tables.VL1) {
                                    n5 = -1;
                                }
                                else if (entry == T4_T6_Tables.VL2) {
                                    n5 = -2;
                                }
                                else if (entry == T4_T6_Tables.VL3) {
                                    n5 = -3;
                                }
                                else if (entry == T4_T6_Tables.VR1) {
                                    n5 = 1;
                                }
                                else if (entry == T4_T6_Tables.VR2) {
                                    n5 = 2;
                                }
                                else {
                                    if (entry != T4_T6_Tables.VR3) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("Invalid/unknown T.6 control code ");
                                        sb.append(entry.bitString);
                                        throw new ImageReadException(sb.toString());
                                    }
                                    n5 = 3;
                                }
                                nextChangingElement2 = nextChangingElement + n5;
                                fillRange(bitArrayOutputStream, array, j, nextChangingElement2, n3);
                                n6 = 1 - n3;
                                break Label_0284;
                            }
                            final int n7 = readTotalRunLength(bitInputStreamFlexible, n3) + j;
                            fillRange(bitArrayOutputStream, array, j, n7, n3);
                            final int n8 = 1 - n3;
                            nextChangingElement2 = readTotalRunLength(bitInputStreamFlexible, n8) + n7;
                            fillRange(bitArrayOutputStream, array, n7, nextChangingElement2, n8);
                        }
                        n6 = n3;
                    }
                    final int changingElement = changingElementAt(array, nextChangingElement2);
                    int n9;
                    if (n6 == changingElement) {
                        n9 = nextChangingElement(array, changingElement, nextChangingElement2 + 1);
                    }
                    else {
                        n9 = nextChangingElement(array, 1 - changingElement, nextChangingElement(array, changingElement, nextChangingElement2 + 1) + 1);
                    }
                    n10 = n9;
                    nextChangingElement3 = nextChangingElement(array, 1 - n6, n10 + 1);
                }
                if (n4 == n) {
                    bitArrayOutputStream.flush();
                }
                else if (n4 > n) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unrecoverable row length error in image row ");
                    sb2.append(i);
                    throw new ImageReadException(sb2.toString());
                }
                ++i;
                continue;
            }
            catch (final HuffmanTreeException ex) {
                throw new ImageReadException("Decompression error", ex);
            }
            break;
        }
        return bitArrayOutputStream.toByteArray();
    }

    private static boolean isEOL(T4_T6_Tables.Entry entry, boolean z) {
        if (entry == T4_T6_Tables.EOL) {
            return true;
        }
        if (z) {
            return entry == T4_T6_Tables.EOL13 || entry == T4_T6_Tables.EOL14 || entry == T4_T6_Tables.EOL15 || entry == T4_T6_Tables.EOL16 || entry == T4_T6_Tables.EOL17 || entry == T4_T6_Tables.EOL18 || entry == T4_T6_Tables.EOL19;
        }
        return false;
    }

    private static void writeRunLength(BitArrayOutputStream bitArrayOutputStream, int i, int i2) {
        T4_T6_Tables.Entry[] entryArr;
        T4_T6_Tables.Entry[] entryArr2;
        if (i2 == 0) {
            entryArr = T4_T6_Tables.WHITE_MAKE_UP_CODES;
            entryArr2 = T4_T6_Tables.WHITE_TERMINATING_CODES;
        } else {
            entryArr = T4_T6_Tables.BLACK_MAKE_UP_CODES;
            entryArr2 = T4_T6_Tables.BLACK_TERMINATING_CODES;
        }
        while (i >= 1792) {
            T4_T6_Tables.Entry entryLowerBound = lowerBound(T4_T6_Tables.ADDITIONAL_MAKE_UP_CODES, i);
            entryLowerBound.writeBits(bitArrayOutputStream);
            i -= entryLowerBound.value.intValue();
        }
        while (i >= 64) {
            T4_T6_Tables.Entry entryLowerBound2 = lowerBound(entryArr, i);
            entryLowerBound2.writeBits(bitArrayOutputStream);
            i -= entryLowerBound2.value.intValue();
        }
        entryArr2[i].writeBits(bitArrayOutputStream);
    }

    private static T4_T6_Tables.Entry lowerBound(T4_T6_Tables.Entry[] entryArr, int i) {
        int i2;
        int length = entryArr.length - 1;
        int i3 = 0;
        do {
            int i4 = (i3 + length) >>> 1;
            if (entryArr[i4].value.intValue() <= i && ((i2 = i4 + 1) >= entryArr.length || i < entryArr[i2].value.intValue())) {
                return entryArr[i4];
            }
            if (entryArr[i4].value.intValue() > i) {
                length = i4 - 1;
            } else {
                i3 = i4 + 1;
            }
        } while (i3 < length);
        return entryArr[i3];
    }

    private static int readTotalRunLength(BitInputStreamFlexible bitInputStreamFlexible, int i) throws ImageReadException {
        Integer numDecode;
        int iIntValue = 0;
        do {
            if (i == 0) {
                try {
                    numDecode = WHITE_RUN_LENGTHS.decode(bitInputStreamFlexible);
                } catch (HuffmanTreeException e) {
                    throw new ImageReadException("Decompression error", e);
                }
            } else {
                numDecode = BLACK_RUN_LENGTHS.decode(bitInputStreamFlexible);
            }
            iIntValue += numDecode.intValue();
        } while (numDecode.intValue() > 63);
        return iIntValue;
    }

    private static int changingElementAt(int[] iArr, int i) {
        if (i < 0 || i >= iArr.length) {
            return 0;
        }
        return iArr[i];
    }

    private static int nextChangingElement(int[] iArr, int i, int i2) {
        while (i2 < iArr.length && iArr[i2] == i) {
            i2++;
        }
        return i2 < iArr.length ? i2 : iArr.length;
    }

    private static void fillRange(BitArrayOutputStream bitArrayOutputStream, int[] iArr, int i, int i2, int i3) {
        while (i < i2) {
            iArr[i] = i3;
            bitArrayOutputStream.writeBit(i3);
            i++;
        }
    }
}
