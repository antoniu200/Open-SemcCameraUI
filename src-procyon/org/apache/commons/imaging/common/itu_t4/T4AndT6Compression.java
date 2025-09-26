// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.itu_t4;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.imaging.ImageWriteException;

public final class T4AndT6Compression
{
    public static final int BLACK = 1;
    private static final HuffmanTree<Integer> BLACK_RUN_LENGTHS;
    private static final HuffmanTree<T4_T6_Tables.Entry> CONTROL_CODES;
    public static final int WHITE = 0;
    private static final HuffmanTree<Integer> WHITE_RUN_LENGTHS;
    
    static {
        WHITE_RUN_LENGTHS = new HuffmanTree<Integer>();
        BLACK_RUN_LENGTHS = new HuffmanTree<Integer>();
        CONTROL_CODES = new HuffmanTree<T4_T6_Tables.Entry>();
        try {
            final T4_T6_Tables.Entry[] white_TERMINATING_CODES = T4_T6_Tables.WHITE_TERMINATING_CODES;
            final int length = white_TERMINATING_CODES.length;
            final int n = 0;
            for (final T4_T6_Tables.Entry entry : white_TERMINATING_CODES) {
                T4AndT6Compression.WHITE_RUN_LENGTHS.insert(entry.bitString, entry.value);
            }
            for (final T4_T6_Tables.Entry entry2 : T4_T6_Tables.WHITE_MAKE_UP_CODES) {
                T4AndT6Compression.WHITE_RUN_LENGTHS.insert(entry2.bitString, entry2.value);
            }
            for (final T4_T6_Tables.Entry entry3 : T4_T6_Tables.BLACK_TERMINATING_CODES) {
                T4AndT6Compression.BLACK_RUN_LENGTHS.insert(entry3.bitString, entry3.value);
            }
            for (final T4_T6_Tables.Entry entry4 : T4_T6_Tables.BLACK_MAKE_UP_CODES) {
                T4AndT6Compression.BLACK_RUN_LENGTHS.insert(entry4.bitString, entry4.value);
            }
            final T4_T6_Tables.Entry[] additional_MAKE_UP_CODES = T4_T6_Tables.ADDITIONAL_MAKE_UP_CODES;
            for (int length5 = additional_MAKE_UP_CODES.length, n2 = n; n2 < length5; ++n2) {
                final T4_T6_Tables.Entry entry5 = additional_MAKE_UP_CODES[n2];
                T4AndT6Compression.WHITE_RUN_LENGTHS.insert(entry5.bitString, entry5.value);
                T4AndT6Compression.BLACK_RUN_LENGTHS.insert(entry5.bitString, entry5.value);
            }
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL.bitString, T4_T6_Tables.EOL);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL13.bitString, T4_T6_Tables.EOL13);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL14.bitString, T4_T6_Tables.EOL14);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL15.bitString, T4_T6_Tables.EOL15);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL16.bitString, T4_T6_Tables.EOL16);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL17.bitString, T4_T6_Tables.EOL17);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL18.bitString, T4_T6_Tables.EOL18);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.EOL19.bitString, T4_T6_Tables.EOL19);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.P.bitString, T4_T6_Tables.P);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.H.bitString, T4_T6_Tables.H);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.V0.bitString, T4_T6_Tables.V0);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.VL1.bitString, T4_T6_Tables.VL1);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.VL2.bitString, T4_T6_Tables.VL2);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.VL3.bitString, T4_T6_Tables.VL3);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.VR1.bitString, T4_T6_Tables.VR1);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.VR2.bitString, T4_T6_Tables.VR2);
            T4AndT6Compression.CONTROL_CODES.insert(T4_T6_Tables.VR3.bitString, T4_T6_Tables.VR3);
        }
        catch (final HuffmanTreeException cause) {
            throw new Error(cause);
        }
    }
    
    private T4AndT6Compression() {
    }
    
    private static int changingElementAt(final int[] array, final int n) {
        if (n >= 0 && n < array.length) {
            return array[n];
        }
        return 0;
    }
    
    private static void compress1DLine(final BitInputStreamFlexible bitInputStreamFlexible, final BitArrayOutputStream bitArrayOutputStream, final int[] array, final int n) throws ImageWriteException {
        int i = 0;
        int n2 = 0;
        int n3 = 0;
        while (i < n) {
            try {
                final int bits = bitInputStreamFlexible.readBits(1);
                if (array != null) {
                    array[i] = bits;
                }
                if (n3 == bits) {
                    ++n2;
                }
                else {
                    writeRunLength(bitArrayOutputStream, n2, n3);
                    n2 = 1;
                    n3 = bits;
                }
                ++i;
                continue;
            }
            catch (final IOException ex) {
                throw new ImageWriteException("Error reading image to compress", ex);
            }
            break;
        }
        writeRunLength(bitArrayOutputStream, n2, n3);
    }
    
    public static byte[] compressModifiedHuffman(final byte[] buf, final int n, final int n2) throws ImageWriteException {
        final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(buf));
        final BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        for (int i = 0; i < n2; ++i) {
            compress1DLine(bitInputStreamFlexible, bitArrayOutputStream, null, n);
            bitInputStreamFlexible.flushCache();
            bitArrayOutputStream.flush();
        }
        return bitArrayOutputStream.toByteArray();
    }
    
    public static byte[] compressT4_1D(final byte[] buf, final int n, final int n2, final boolean b) throws ImageWriteException {
        final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(buf));
        final BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        if (b) {
            T4_T6_Tables.EOL16.writeBits(bitArrayOutputStream);
        }
        else {
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
        }
        for (int i = 0; i < n2; ++i) {
            compress1DLine(bitInputStreamFlexible, bitArrayOutputStream, null, n);
            if (b) {
                int j;
                if ((j = bitArrayOutputStream.getBitsAvailableInCurrentByte()) < 4) {
                    bitArrayOutputStream.flush();
                    j = 8;
                }
                while (j > 4) {
                    bitArrayOutputStream.writeBit(0);
                    --j;
                }
            }
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
            bitInputStreamFlexible.flushCache();
        }
        return bitArrayOutputStream.toByteArray();
    }
    
    public static byte[] compressT4_2D(final byte[] buf, final int n, final int n2, final boolean b, final int n3) throws ImageWriteException {
        final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(buf));
        final BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
        int[] array = new int[n];
        int[] array2 = new int[n];
        if (b) {
            T4_T6_Tables.EOL16.writeBits(bitArrayOutputStream);
        }
        else {
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
        }
        int n4 = 0;
        int n5 = 0;
        while (true) {
            int n6 = 0;
            if (n4 >= n2) {
                break;
            }
            int[] array3;
            if (n5 > 0) {
                bitArrayOutputStream.writeBit(0);
                int i = 0;
                while (i < n) {
                    try {
                        array2[i] = bitInputStreamFlexible.readBits(1);
                        ++i;
                        continue;
                    }
                    catch (final IOException ex) {
                        throw new ImageWriteException("Error reading image to compress", ex);
                    }
                    break;
                }
                int nextChangingElement = nextChangingElement(array2, 0, 0);
                int nextChangingElement2 = nextChangingElement(array, 0, 0);
                int nextChangingElement3 = nextChangingElement(array, 1, nextChangingElement2 + 1);
                int nextChangingElement5;
                for (int j = 0; j < n; j = nextChangingElement, nextChangingElement = nextChangingElement5) {
                    if (nextChangingElement3 < nextChangingElement) {
                        T4_T6_Tables.P.writeBits(bitArrayOutputStream);
                        nextChangingElement = nextChangingElement3;
                    }
                    else {
                        final int n7 = nextChangingElement - nextChangingElement2;
                        if (-3 <= n7 && n7 <= 3) {
                            T4_T6_Tables.Entry entry;
                            if (n7 == -3) {
                                entry = T4_T6_Tables.VL3;
                            }
                            else if (n7 == -2) {
                                entry = T4_T6_Tables.VL2;
                            }
                            else if (n7 == -1) {
                                entry = T4_T6_Tables.VL1;
                            }
                            else if (n7 == 0) {
                                entry = T4_T6_Tables.V0;
                            }
                            else if (n7 == 1) {
                                entry = T4_T6_Tables.VR1;
                            }
                            else if (n7 == 2) {
                                entry = T4_T6_Tables.VR2;
                            }
                            else {
                                entry = T4_T6_Tables.VR3;
                            }
                            entry.writeBits(bitArrayOutputStream);
                            n6 = 1 - n6;
                        }
                        else {
                            final int n8 = 1 - n6;
                            final int nextChangingElement4 = nextChangingElement(array2, n8, nextChangingElement + 1);
                            T4_T6_Tables.H.writeBits(bitArrayOutputStream);
                            writeRunLength(bitArrayOutputStream, nextChangingElement - j, n6);
                            writeRunLength(bitArrayOutputStream, nextChangingElement4 - nextChangingElement, n8);
                            nextChangingElement = nextChangingElement4;
                        }
                    }
                    final int changingElement = changingElementAt(array, nextChangingElement);
                    final int n9 = nextChangingElement + 1;
                    nextChangingElement5 = nextChangingElement(array2, n6, n9);
                    int n10;
                    if (n6 == changingElement) {
                        n10 = nextChangingElement(array, changingElement, n9);
                    }
                    else {
                        n10 = nextChangingElement(array, 1 - changingElement, nextChangingElement(array, changingElement, n9) + 1);
                    }
                    final int nextChangingElement6 = nextChangingElement(array, 1 - n6, n10 + 1);
                    nextChangingElement2 = n10;
                    nextChangingElement3 = nextChangingElement6;
                }
                array3 = array2;
                array2 = array;
            }
            else {
                bitArrayOutputStream.writeBit(1);
                compress1DLine(bitInputStreamFlexible, bitArrayOutputStream, array, n);
                array3 = array;
            }
            if (b) {
                int k;
                if ((k = bitArrayOutputStream.getBitsAvailableInCurrentByte()) < 4) {
                    bitArrayOutputStream.flush();
                    k = 8;
                }
                while (k > 4) {
                    bitArrayOutputStream.writeBit(0);
                    --k;
                }
            }
            T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
            int n11;
            if ((n11 = n5 + 1) == n3) {
                n11 = 0;
            }
            bitInputStreamFlexible.flushCache();
            ++n4;
            n5 = n11;
            array = array3;
        }
        return bitArrayOutputStream.toByteArray();
    }
    
    public static byte[] compressT6(byte[] byteArray, final int n, final int n2) throws ImageWriteException {
        Closeable closeable;
        try {
            final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(byteArray));
            try {
                final BitArrayOutputStream bitArrayOutputStream = new BitArrayOutputStream();
                int[] array = new int[n];
                int[] array2 = new int[n];
                int n3 = 0;
                while (true) {
                    final int[] array3 = array2;
                    if (n3 >= n2) {
                        break;
                    }
                    int i = 0;
                    while (i < n) {
                        try {
                            array3[i] = bitInputStreamFlexible.readBits(1);
                            ++i;
                            continue;
                        }
                        catch (final IOException ex) {
                            throw new ImageWriteException("Error reading image to compress", ex);
                        }
                        break;
                    }
                    int nextChangingElement = nextChangingElement(array3, 0, 0);
                    int nextChangingElement2 = nextChangingElement(array, 0, 0);
                    int nextChangingElement3 = nextChangingElement(array, 1, nextChangingElement2 + 1);
                    int n4 = 0;
                    int nextChangingElement6;
                    int n9;
                    for (int j = 0; j < n; j = nextChangingElement, nextChangingElement = n9, nextChangingElement3 = nextChangingElement6) {
                        if (nextChangingElement3 < nextChangingElement) {
                            T4_T6_Tables.P.writeBits(bitArrayOutputStream);
                            nextChangingElement = nextChangingElement3;
                        }
                        else {
                            final int n5 = nextChangingElement - nextChangingElement2;
                            if (-3 <= n5 && n5 <= 3) {
                                T4_T6_Tables.Entry entry;
                                if (n5 == -3) {
                                    entry = T4_T6_Tables.VL3;
                                }
                                else if (n5 == -2) {
                                    entry = T4_T6_Tables.VL2;
                                }
                                else if (n5 == -1) {
                                    entry = T4_T6_Tables.VL1;
                                }
                                else if (n5 == 0) {
                                    entry = T4_T6_Tables.V0;
                                }
                                else if (n5 == 1) {
                                    entry = T4_T6_Tables.VR1;
                                }
                                else if (n5 == 2) {
                                    entry = T4_T6_Tables.VR2;
                                }
                                else {
                                    entry = T4_T6_Tables.VR3;
                                }
                                entry.writeBits(bitArrayOutputStream);
                                n4 = 1 - n4;
                            }
                            else {
                                final int n6 = 1 - n4;
                                final int nextChangingElement4 = nextChangingElement(array3, n6, nextChangingElement + 1);
                                T4_T6_Tables.H.writeBits(bitArrayOutputStream);
                                writeRunLength(bitArrayOutputStream, nextChangingElement - j, n4);
                                writeRunLength(bitArrayOutputStream, nextChangingElement4 - nextChangingElement, n6);
                                nextChangingElement = nextChangingElement4;
                            }
                        }
                        final int changingElement = changingElementAt(array, nextChangingElement);
                        final int n7 = nextChangingElement + 1;
                        final int nextChangingElement5 = nextChangingElement(array3, n4, n7);
                        int n8;
                        if (n4 == changingElement) {
                            n8 = nextChangingElement(array, changingElement, n7);
                        }
                        else {
                            n8 = nextChangingElement(array, 1 - changingElement, nextChangingElement(array, changingElement, n7) + 1);
                        }
                        nextChangingElement6 = nextChangingElement(array, 1 - n4, n8 + 1);
                        nextChangingElement2 = n8;
                        n9 = nextChangingElement5;
                    }
                    bitInputStreamFlexible.flushCache();
                    ++n3;
                    array2 = array;
                    array = array3;
                }
                T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
                T4_T6_Tables.EOL.writeBits(bitArrayOutputStream);
                byteArray = bitArrayOutputStream.toByteArray();
                try {
                    IoUtils.closeQuietly(true, bitInputStreamFlexible);
                    return byteArray;
                }
                catch (final IOException ex2) {
                    throw new ImageWriteException("I/O error", ex2);
                }
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        try {
            IoUtils.closeQuietly(false, closeable);
            throw;
        }
        catch (final IOException ex3) {
            throw new ImageWriteException("I/O error", ex3);
        }
    }
    
    public static byte[] decompressModifiedHuffman(byte[] buf, final int n, final int n2) throws ImageReadException {
        final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream((byte[])(Object)buf));
        Closeable closeable = null;
        try {
            buf = (IOException)new BitArrayOutputStream();
            int i = 0;
        Label_0038_Outer:
            while (true) {
                Label_0164: {
                    if (i >= n2) {
                        break Label_0164;
                    }
                    int n3 = 0;
                    int n4 = 0;
                    int totalRunLength;
                    StringBuilder sb;
                    byte[] byteArray;
                    Block_8_Outer:Block_9_Outer:
                    while (true) {
                        Label_0091: {
                            if (n3 >= n) {
                                break Label_0091;
                            }
                            try {
                                totalRunLength = readTotalRunLength(bitInputStreamFlexible, n4);
                                for (int j = 0; j < totalRunLength; ++j) {
                                    ((BitArrayOutputStream)buf).writeBit(n4);
                                }
                                n4 = 1 - n4;
                                n3 += totalRunLength;
                                continue Block_8_Outer;
                                while (true) {
                                    while (true) {
                                        bitInputStreamFlexible.flushCache();
                                        ((BitArrayOutputStream)buf).flush();
                                        Label_0158: {
                                            ++i;
                                        }
                                        continue Label_0038_Outer;
                                        sb = new StringBuilder();
                                        sb.append("Unrecoverable row length error in image row ");
                                        sb.append(i);
                                        throw new ImageReadException(sb.toString());
                                        iftrue(Label_0109:)(n3 != n);
                                        continue Block_9_Outer;
                                    }
                                    Label_0109: {
                                        iftrue(Label_0158:)(n3 <= n);
                                    }
                                    continue;
                                }
                                byteArray = ((BitArrayOutputStream)buf).toByteArray();
                                try {
                                    IoUtils.closeQuietly(true, (Closeable)buf);
                                    return byteArray;
                                }
                                catch (final IOException buf) {
                                    throw new ImageReadException("I/O error", buf);
                                }
                            }
                            finally {}
                        }
                        break;
                    }
                }
            }
        }
        finally {
            closeable = null;
        }
        try {
            IoUtils.closeQuietly(false, closeable);
            throw;
        }
        catch (final IOException ex) {
            throw new ImageReadException("I/O error", ex);
        }
    }
    
    public static byte[] decompressT4_1D(byte[] byteArray, final int n, final int n2, final boolean b) throws ImageReadException {
        final BitInputStreamFlexible bitInputStreamFlexible = new BitInputStreamFlexible(new ByteArrayInputStream(byteArray));
        BitArrayOutputStream bitArrayOutputStream;
        ImageReadException ex2;
        try {
            bitArrayOutputStream = new BitArrayOutputStream();
            int i = 0;
            while (i < n2) {
                try {
                    try {
                        if (!isEOL(T4AndT6Compression.CONTROL_CODES.decode(bitInputStreamFlexible), b)) {
                            throw new ImageReadException("Expected EOL not found");
                        }
                        int j = 0;
                        int n3 = 0;
                        while (j < n) {
                            final int totalRunLength = readTotalRunLength(bitInputStreamFlexible, n3);
                            for (int k = 0; k < totalRunLength; ++k) {
                                bitArrayOutputStream.writeBit(n3);
                            }
                            n3 = 1 - n3;
                            j += totalRunLength;
                        }
                        if (j == n) {
                            bitArrayOutputStream.flush();
                        }
                        else if (j > n) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unrecoverable row length error in image row ");
                            sb.append(i);
                            throw new ImageReadException(sb.toString());
                        }
                        ++i;
                        continue;
                    }
                    finally {}
                }
                catch (final HuffmanTreeException ex) {
                    ex2 = new ImageReadException("Decompression error", ex);
                }
                break;
            }
            byteArray = bitArrayOutputStream.toByteArray();
            try {
                IoUtils.closeQuietly(true, bitArrayOutputStream);
                return byteArray;
            }
            catch (final IOException ex3) {
                throw new ImageReadException("I/O error", ex3);
            }
        }
        finally {
            bitArrayOutputStream = null;
        }
        try {
            IoUtils.closeQuietly(false, bitArrayOutputStream);
            throw ex2;
        }
        catch (final IOException ex4) {
            throw new ImageReadException("I/O error", ex4);
        }
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
    
    private static void fillRange(final BitArrayOutputStream bitArrayOutputStream, final int[] array, int i, final int n, final int n2) {
        while (i < n) {
            bitArrayOutputStream.writeBit(array[i] = n2);
            ++i;
        }
    }
    
    private static boolean isEOL(final T4_T6_Tables.Entry entry, final boolean b) {
        if (entry == T4_T6_Tables.EOL) {
            return true;
        }
        final boolean b2 = false;
        if (b) {
            if (entry != T4_T6_Tables.EOL13 && entry != T4_T6_Tables.EOL14 && entry != T4_T6_Tables.EOL15 && entry != T4_T6_Tables.EOL16 && entry != T4_T6_Tables.EOL17 && entry != T4_T6_Tables.EOL18) {
                final boolean b3 = b2;
                if (entry != T4_T6_Tables.EOL19) {
                    return b3;
                }
            }
            return true;
        }
        return false;
    }
    
    private static T4_T6_Tables.Entry lowerBound(final T4_T6_Tables.Entry[] array, final int n) {
        int n2 = array.length - 1;
        int n3 = 0;
        int i;
        int n4;
        do {
            n4 = n3 + n2 >>> 1;
            if (array[n4].value <= n) {
                final int n5 = n4 + 1;
                if (n5 >= array.length || n < array[n5].value) {
                    return array[n4];
                }
            }
            if (array[n4].value > n) {
                --n4;
                i = n3;
            }
            else {
                i = n4 + 1;
                n4 = n2;
            }
            n2 = n4;
            n3 = i;
        } while (i < n4);
        return array[i];
    }
    
    private static int nextChangingElement(final int[] array, final int n, int length) {
        while (length < array.length && array[length] == n) {
            ++length;
        }
        if (length >= array.length) {
            length = array.length;
        }
        return length;
    }
    
    private static int readTotalRunLength(final BitInputStreamFlexible bitInputStreamFlexible, final int n) throws ImageReadException {
        int n2 = 0;
        int i;
        int n4;
        do {
            Integer n3 = null;
            Label_0037: {
                if (n == 0) {
                    try {
                        n3 = T4AndT6Compression.WHITE_RUN_LENGTHS.decode(bitInputStreamFlexible);
                        break Label_0037;
                    }
                    catch (final HuffmanTreeException ex) {
                        throw new ImageReadException("Decompression error", ex);
                    }
                }
                n3 = T4AndT6Compression.BLACK_RUN_LENGTHS.decode(bitInputStreamFlexible);
            }
            n4 = n2 + n3;
            i = n3;
            n2 = n4;
        } while (i > 63);
        return n4;
    }
    
    private static void writeRunLength(final BitArrayOutputStream bitArrayOutputStream, int n, int i) {
        T4_T6_Tables.Entry[] array;
        T4_T6_Tables.Entry[] array2;
        if (i == 0) {
            array = T4_T6_Tables.WHITE_MAKE_UP_CODES;
            array2 = T4_T6_Tables.WHITE_TERMINATING_CODES;
        }
        else {
            array = T4_T6_Tables.BLACK_MAKE_UP_CODES;
            array2 = T4_T6_Tables.BLACK_TERMINATING_CODES;
        }
        while (true) {
            i = n;
            if (n < 1792) {
                break;
            }
            final T4_T6_Tables.Entry lowerBound = lowerBound(T4_T6_Tables.ADDITIONAL_MAKE_UP_CODES, n);
            lowerBound.writeBits(bitArrayOutputStream);
            n -= lowerBound.value;
        }
        while (i >= 64) {
            final T4_T6_Tables.Entry lowerBound2 = lowerBound(array, i);
            lowerBound2.writeBits(bitArrayOutputStream);
            i -= lowerBound2.value;
        }
        array2[i].writeBits(bitArrayOutputStream);
    }
}
