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

    /* JADX WARN: Removed duplicated region for block: B:43:0x00a2 A[Catch: HuffmanTreeException -> 0x0118, IOException -> 0x0121, TryCatch #2 {IOException -> 0x0121, HuffmanTreeException -> 0x0118, blocks: (B:4:0x0015, B:6:0x0023, B:7:0x002a, B:8:0x002b, B:10:0x0032, B:12:0x0043, B:14:0x004f, B:41:0x009c, B:43:0x00a2, B:46:0x00b8, B:45:0x00aa, B:16:0x0054, B:18:0x0058, B:19:0x006b, B:40:0x0094, B:22:0x0071, B:25:0x0077, B:28:0x007d, B:31:0x0083, B:34:0x0089, B:37:0x008f, B:47:0x00c3, B:48:0x00db, B:51:0x00e0, B:53:0x00e7), top: B:70:0x0015 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00aa A[Catch: HuffmanTreeException -> 0x0118, IOException -> 0x0121, TryCatch #2 {IOException -> 0x0121, HuffmanTreeException -> 0x0118, blocks: (B:4:0x0015, B:6:0x0023, B:7:0x002a, B:8:0x002b, B:10:0x0032, B:12:0x0043, B:14:0x004f, B:41:0x009c, B:43:0x00a2, B:46:0x00b8, B:45:0x00aa, B:16:0x0054, B:18:0x0058, B:19:0x006b, B:40:0x0094, B:22:0x0071, B:25:0x0077, B:28:0x007d, B:31:0x0083, B:34:0x0089, B:37:0x008f, B:47:0x00c3, B:48:0x00db, B:51:0x00e0, B:53:0x00e7), top: B:70:0x0015 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decompressT4_2D(byte[] r11, int r12, int r13, boolean r14) throws org.apache.commons.imaging.ImageReadException {
        /*
            Method dump skipped, instructions count: 303
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.common.itu_t4.T4AndT6Compression.decompressT4_2D(byte[], int, int, boolean):byte[]");
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

    /* JADX WARN: Removed duplicated region for block: B:37:0x0086 A[Catch: HuffmanTreeException -> 0x00e3, TryCatch #0 {HuffmanTreeException -> 0x00e3, blocks: (B:4:0x0015, B:6:0x0027, B:8:0x0033, B:35:0x0080, B:37:0x0086, B:40:0x009c, B:39:0x008e, B:10:0x0038, B:12:0x003c, B:13:0x004f, B:34:0x0078, B:16:0x0055, B:19:0x005b, B:22:0x0061, B:25:0x0067, B:28:0x006d, B:31:0x0073, B:41:0x00a7, B:42:0x00bf), top: B:54:0x0015 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x008e A[Catch: HuffmanTreeException -> 0x00e3, TryCatch #0 {HuffmanTreeException -> 0x00e3, blocks: (B:4:0x0015, B:6:0x0027, B:8:0x0033, B:35:0x0080, B:37:0x0086, B:40:0x009c, B:39:0x008e, B:10:0x0038, B:12:0x003c, B:13:0x004f, B:34:0x0078, B:16:0x0055, B:19:0x005b, B:22:0x0061, B:25:0x0067, B:28:0x006d, B:31:0x0073, B:41:0x00a7, B:42:0x00bf), top: B:54:0x0015 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] decompressT6(byte[] r11, int r12, int r13) throws org.apache.commons.imaging.ImageReadException {
        /*
            Method dump skipped, instructions count: 241
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.common.itu_t4.T4AndT6Compression.decompressT6(byte[], int, int):byte[]");
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
