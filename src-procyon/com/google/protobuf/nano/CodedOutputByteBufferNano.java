// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.util.Map;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteOrder;
import com.google.protobuf.CodedOutputStream;
import java.nio.ByteBuffer;

public final class CodedOutputByteBufferNano
{
    public static final int LITTLE_ENDIAN_32_SIZE = 4;
    public static final int LITTLE_ENDIAN_64_SIZE = 8;
    private static final int MAX_UTF8_EXPANSION = 3;
    private final ByteBuffer buffer;
    private CodedOutputStream codedOutputStream;
    private int codedOutputStreamPosition;
    
    private CodedOutputByteBufferNano(final ByteBuffer buffer) {
        (this.buffer = buffer).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    private CodedOutputByteBufferNano(final byte[] array, final int offset, final int length) {
        this(ByteBuffer.wrap(array, offset, length));
    }
    
    public static int computeBoolSize(final int n, final boolean b) {
        return computeTagSize(n) + computeBoolSizeNoTag(b);
    }
    
    public static int computeBoolSizeNoTag(final boolean b) {
        return 1;
    }
    
    public static int computeBytesSize(final int n, final byte[] array) {
        return computeTagSize(n) + computeBytesSizeNoTag(array);
    }
    
    public static int computeBytesSizeNoTag(final byte[] array) {
        return computeRawVarint32Size(array.length) + array.length;
    }
    
    public static int computeDoubleSize(final int n, final double n2) {
        return computeTagSize(n) + computeDoubleSizeNoTag(n2);
    }
    
    public static int computeDoubleSizeNoTag(final double n) {
        return 8;
    }
    
    public static int computeEnumSize(final int n, final int n2) {
        return computeTagSize(n) + computeEnumSizeNoTag(n2);
    }
    
    public static int computeEnumSizeNoTag(final int n) {
        return computeRawVarint32Size(n);
    }
    
    public static int computeFixed32Size(final int n, final int n2) {
        return computeTagSize(n) + computeFixed32SizeNoTag(n2);
    }
    
    public static int computeFixed32SizeNoTag(final int n) {
        return 4;
    }
    
    public static int computeFixed64Size(final int n, final long n2) {
        return computeTagSize(n) + computeFixed64SizeNoTag(n2);
    }
    
    public static int computeFixed64SizeNoTag(final long n) {
        return 8;
    }
    
    public static int computeFloatSize(final int n, final float n2) {
        return computeTagSize(n) + computeFloatSizeNoTag(n2);
    }
    
    public static int computeFloatSizeNoTag(final float n) {
        return 4;
    }
    
    public static int computeGroupSize(final int n, final MessageNano messageNano) {
        return computeTagSize(n) * 2 + computeGroupSizeNoTag(messageNano);
    }
    
    public static int computeGroupSizeNoTag(final MessageNano messageNano) {
        return messageNano.getSerializedSize();
    }
    
    public static int computeInt32Size(final int n, final int n2) {
        return computeTagSize(n) + computeInt32SizeNoTag(n2);
    }
    
    public static int computeInt32SizeNoTag(final int n) {
        if (n >= 0) {
            return computeRawVarint32Size(n);
        }
        return 10;
    }
    
    public static int computeInt64Size(final int n, final long n2) {
        return computeTagSize(n) + computeInt64SizeNoTag(n2);
    }
    
    public static int computeInt64SizeNoTag(final long n) {
        return computeRawVarint64Size(n);
    }
    
    public static int computeMessageSetExtensionSize(final int n, final MessageNano messageNano) {
        return computeTagSize(1) * 2 + computeUInt32Size(2, n) + computeMessageSize(3, messageNano);
    }
    
    public static int computeMessageSize(final int n, final MessageNano messageNano) {
        return computeTagSize(n) + computeMessageSizeNoTag(messageNano);
    }
    
    public static int computeMessageSizeNoTag(final MessageNano messageNano) {
        final int serializedSize = messageNano.getSerializedSize();
        return computeRawVarint32Size(serializedSize) + serializedSize;
    }
    
    public static int computeRawMessageSetExtensionSize(final int n, final byte[] array) {
        return computeTagSize(1) * 2 + computeUInt32Size(2, n) + computeTagSize(3) + array.length;
    }
    
    public static int computeRawVarint32Size(final int n) {
        if ((n & 0xFFFFFF80) == 0x0) {
            return 1;
        }
        if ((n & 0xFFFFC000) == 0x0) {
            return 2;
        }
        if ((0xFFE00000 & n) == 0x0) {
            return 3;
        }
        if ((n & 0xF0000000) == 0x0) {
            return 4;
        }
        return 5;
    }
    
    public static int computeRawVarint64Size(final long n) {
        if ((0xFFFFFFFFFFFFFF80L & n) == 0x0L) {
            return 1;
        }
        if ((0xFFFFFFFFFFFFC000L & n) == 0x0L) {
            return 2;
        }
        if ((0xFFFFFFFFFFE00000L & n) == 0x0L) {
            return 3;
        }
        if ((0xFFFFFFFFF0000000L & n) == 0x0L) {
            return 4;
        }
        if ((0xFFFFFFF800000000L & n) == 0x0L) {
            return 5;
        }
        if ((0xFFFFFC0000000000L & n) == 0x0L) {
            return 6;
        }
        if ((0xFFFE000000000000L & n) == 0x0L) {
            return 7;
        }
        if ((0xFF00000000000000L & n) == 0x0L) {
            return 8;
        }
        if ((n & Long.MIN_VALUE) == 0x0L) {
            return 9;
        }
        return 10;
    }
    
    public static int computeSFixed32Size(final int n, final int n2) {
        return computeTagSize(n) + computeSFixed32SizeNoTag(n2);
    }
    
    public static int computeSFixed32SizeNoTag(final int n) {
        return 4;
    }
    
    public static int computeSFixed64Size(final int n, final long n2) {
        return computeTagSize(n) + computeSFixed64SizeNoTag(n2);
    }
    
    public static int computeSFixed64SizeNoTag(final long n) {
        return 8;
    }
    
    public static int computeSInt32Size(final int n, final int n2) {
        return computeTagSize(n) + computeSInt32SizeNoTag(n2);
    }
    
    public static int computeSInt32SizeNoTag(final int n) {
        return computeRawVarint32Size(encodeZigZag32(n));
    }
    
    public static int computeSInt64Size(final int n, final long n2) {
        return computeTagSize(n) + computeSInt64SizeNoTag(n2);
    }
    
    public static int computeSInt64SizeNoTag(final long n) {
        return computeRawVarint64Size(encodeZigZag64(n));
    }
    
    public static int computeStringSize(final int n, final String s) {
        return computeTagSize(n) + computeStringSizeNoTag(s);
    }
    
    public static int computeStringSizeNoTag(final String s) {
        final int encodedLength = encodedLength(s);
        return computeRawVarint32Size(encodedLength) + encodedLength;
    }
    
    public static int computeTagSize(final int n) {
        return computeRawVarint32Size(WireFormatNano.makeTag(n, 0));
    }
    
    public static int computeUInt32Size(final int n, final int n2) {
        return computeTagSize(n) + computeUInt32SizeNoTag(n2);
    }
    
    public static int computeUInt32SizeNoTag(final int n) {
        return computeRawVarint32Size(n);
    }
    
    public static int computeUInt64Size(final int n, final long n2) {
        return computeTagSize(n) + computeUInt64SizeNoTag(n2);
    }
    
    public static int computeUInt64SizeNoTag(final long n) {
        return computeRawVarint64Size(n);
    }
    
    private static int encode(final CharSequence charSequence, final byte[] array, int i, int j) {
        final int length = charSequence.length();
        final int n = j + i;
        int n2;
        char char1;
        for (j = 0; j < length; ++j) {
            n2 = j + i;
            if (n2 >= n) {
                break;
            }
            char1 = charSequence.charAt(j);
            if (char1 >= '\u0080') {
                break;
            }
            array[n2] = (byte)char1;
        }
        if (j == length) {
            return i + length;
        }
        int k = i + j;
        char char2;
        int n3;
        int n4;
        char char3;
        int n5;
        int n6;
        int n7;
        int n8;
        StringBuilder sb;
        StringBuilder sb2;
        for (i = j; i < length; ++i, k = j) {
            char2 = charSequence.charAt(i);
            if (char2 < '\u0080' && k < n) {
                j = k + 1;
                array[k] = (byte)char2;
            }
            else if (char2 < '\u0800' && k <= n - 2) {
                n3 = k + 1;
                array[k] = (byte)(0x3C0 | char2 >>> 6);
                j = n3 + 1;
                array[n3] = (byte)((char2 & '?') | 0x80);
            }
            else if ((char2 < '\ud800' || '\udfff' < char2) && k <= n - 3) {
                j = k + 1;
                array[k] = (byte)(0x1E0 | char2 >>> 12);
                n4 = j + 1;
                array[j] = (byte)((char2 >>> 6 & 0x3F) | 0x80);
                j = n4 + 1;
                array[n4] = (byte)((char2 & '?') | 0x80);
            }
            else {
                if (k <= n - 4) {
                    j = i + 1;
                    if (j != charSequence.length()) {
                        char3 = charSequence.charAt(j);
                        if (Character.isSurrogatePair(char2, char3)) {
                            i = Character.toCodePoint(char2, char3);
                            n5 = k + 1;
                            array[k] = (byte)(0xF0 | i >>> 18);
                            n6 = n5 + 1;
                            array[n5] = (byte)((i >>> 12 & 0x3F) | 0x80);
                            n7 = n6 + 1;
                            array[n6] = (byte)((i >>> 6 & 0x3F) | 0x80);
                            n8 = n7 + 1;
                            array[n7] = (byte)((i & 0x3F) | 0x80);
                            i = j;
                            j = n8;
                            continue;
                        }
                        i = j;
                    }
                    sb = new StringBuilder(39);
                    sb.append("Unpaired surrogate at index ");
                    sb.append(i - 1);
                    throw new IllegalArgumentException(sb.toString());
                }
                sb2 = new StringBuilder(37);
                sb2.append("Failed writing ");
                sb2.append(char2);
                sb2.append(" at index ");
                sb2.append(k);
                throw new ArrayIndexOutOfBoundsException(sb2.toString());
            }
        }
        return k;
    }
    
    private static void encode(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer.hasArray()) {
            try {
                byteBuffer.position();
                return;
            }
            catch (final ArrayIndexOutOfBoundsException cause) {
                final BufferOverflowException ex = new BufferOverflowException();
                ex.initCause(cause);
                throw ex;
            }
        }
        encodeDirect(charSequence, byteBuffer);
    }
    
    private static void encodeDirect(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            final char char1 = charSequence.charAt(i);
            if (char1 < '\u0080') {
                byteBuffer.put((byte)char1);
            }
            else if (char1 < '\u0800') {
                byteBuffer.put((byte)(0x3C0 | char1 >>> 6));
                byteBuffer.put((byte)((char1 & '?') | 0x80));
            }
            else {
                if (char1 >= '\ud800' && '\udfff' >= char1) {
                    final int n = i + 1;
                    if (n != charSequence.length()) {
                        final char char2 = charSequence.charAt(n);
                        if (Character.isSurrogatePair(char1, char2)) {
                            final int codePoint = Character.toCodePoint(char1, char2);
                            byteBuffer.put((byte)(0xF0 | codePoint >>> 18));
                            byteBuffer.put((byte)((codePoint >>> 12 & 0x3F) | 0x80));
                            byteBuffer.put((byte)((codePoint >>> 6 & 0x3F) | 0x80));
                            byteBuffer.put((byte)((codePoint & 0x3F) | 0x80));
                            i = n;
                            continue;
                        }
                        i = n;
                    }
                    final StringBuilder sb = new StringBuilder(39);
                    sb.append("Unpaired surrogate at index ");
                    sb.append(i - 1);
                    throw new IllegalArgumentException(sb.toString());
                }
                byteBuffer.put((byte)(0x1E0 | char1 >>> 12));
                byteBuffer.put((byte)((char1 >>> 6 & 0x3F) | 0x80));
                byteBuffer.put((byte)((char1 & '?') | 0x80));
            }
        }
    }
    
    public static int encodeZigZag32(final int n) {
        return n >> 31 ^ n << 1;
    }
    
    public static long encodeZigZag64(final long n) {
        return n >> 63 ^ n << 1;
    }
    
    private static int encodedLength(final CharSequence charSequence) {
        int length;
        int n;
        for (length = charSequence.length(), n = 0; n < length && charSequence.charAt(n) < '\u0080'; ++n) {}
        int n2 = length;
        int n3;
        while (true) {
            n3 = n2;
            if (n >= length) {
                break;
            }
            final char char1 = charSequence.charAt(n);
            if (char1 >= '\u0800') {
                n3 = n2 + encodedLengthGeneral(charSequence, n);
                break;
            }
            n2 += '\u007f' - char1 >>> 31;
            ++n;
        }
        if (n3 < length) {
            final long n4 = n3;
            final StringBuilder sb = new StringBuilder(54);
            sb.append("UTF-8 length does not fit in int: ");
            sb.append(n4 + 4294967296L);
            throw new IllegalArgumentException(sb.toString());
        }
        return n3;
    }
    
    private static int encodedLengthGeneral(final CharSequence seq, int i) {
        final int length = seq.length();
        int n = 0;
        while (i < length) {
            final char char1 = seq.charAt(i);
            int n2;
            if (char1 < '\u0800') {
                n += '\u007f' - char1 >>> 31;
                n2 = i;
            }
            else {
                final int n3 = n += 2;
                n2 = i;
                if ('\ud800' <= char1) {
                    n = n3;
                    n2 = i;
                    if (char1 <= '\udfff') {
                        if (Character.codePointAt(seq, i) < 65536) {
                            final StringBuilder sb = new StringBuilder(39);
                            sb.append("Unpaired surrogate at index ");
                            sb.append(i);
                            throw new IllegalArgumentException(sb.toString());
                        }
                        n2 = i + 1;
                        n = n3;
                    }
                }
            }
            i = n2 + 1;
        }
        return n;
    }
    
    private CodedOutputStream getCodedOutputStream() throws IOException {
        if (this.codedOutputStream == null) {
            this.codedOutputStream = CodedOutputStream.newInstance(this.buffer);
            this.codedOutputStreamPosition = this.buffer.position();
        }
        else if (this.codedOutputStreamPosition != this.buffer.position()) {
            this.codedOutputStream.write(this.buffer.array(), this.codedOutputStreamPosition, this.buffer.position() - this.codedOutputStreamPosition);
            this.codedOutputStreamPosition = this.buffer.position();
        }
        return this.codedOutputStream;
    }
    
    public static CodedOutputByteBufferNano newInstance(final byte[] array) {
        return newInstance(array, 0, array.length);
    }
    
    public static CodedOutputByteBufferNano newInstance(final byte[] array, final int n, final int n2) {
        return new CodedOutputByteBufferNano(array, n, n2);
    }
    
    public void checkNoSpaceLeft() {
        if (this.spaceLeft() != 0) {
            throw new IllegalStateException(String.format("Did not write as much data as expected, %s bytes remaining.", this.spaceLeft()));
        }
    }
    
    public int position() {
        return this.buffer.position();
    }
    
    public void reset() {
        this.buffer.clear();
    }
    
    public int spaceLeft() {
        return this.buffer.remaining();
    }
    
    public void writeBool(final int n, final boolean b) throws IOException {
        this.writeTag(n, 0);
        this.writeBoolNoTag(b);
    }
    
    public void writeBoolNoTag(final boolean b) throws IOException {
        this.writeRawByte(b ? 1 : 0);
    }
    
    public void writeBytes(final int n, final byte[] array) throws IOException {
        this.writeTag(n, 2);
        this.writeBytesNoTag(array);
    }
    
    public void writeBytesNoTag(final byte[] array) throws IOException {
        this.writeRawVarint32(array.length);
        this.writeRawBytes(array);
    }
    
    public void writeDouble(final int n, final double n2) throws IOException {
        this.writeTag(n, 1);
        this.writeDoubleNoTag(n2);
    }
    
    public void writeDoubleNoTag(final double value) throws IOException {
        this.writeRawLittleEndian64(Double.doubleToLongBits(value));
    }
    
    public void writeEnum(final int n, final int n2) throws IOException {
        this.writeTag(n, 0);
        this.writeEnumNoTag(n2);
    }
    
    public void writeEnumNoTag(final int n) throws IOException {
        this.writeRawVarint32(n);
    }
    
    public void writeFixed32(final int n, final int n2) throws IOException {
        this.writeTag(n, 5);
        this.writeFixed32NoTag(n2);
    }
    
    public void writeFixed32NoTag(final int n) throws IOException {
        this.writeRawLittleEndian32(n);
    }
    
    public void writeFixed64(final int n, final long n2) throws IOException {
        this.writeTag(n, 1);
        this.writeFixed64NoTag(n2);
    }
    
    public void writeFixed64NoTag(final long n) throws IOException {
        this.writeRawLittleEndian64(n);
    }
    
    public void writeFloat(final int n, final float n2) throws IOException {
        this.writeTag(n, 5);
        this.writeFloatNoTag(n2);
    }
    
    public void writeFloatNoTag(final float value) throws IOException {
        this.writeRawLittleEndian32(Float.floatToIntBits(value));
    }
    
    public void writeGroup(final int n, final MessageNano messageNano) throws IOException {
        this.writeTag(n, 3);
        this.writeGroupNoTag(messageNano);
        this.writeTag(n, 4);
    }
    
    public void writeGroupLite(final int n, final MessageLite messageLite) throws IOException {
        final CodedOutputStream codedOutputStream = this.getCodedOutputStream();
        codedOutputStream.writeGroup(n, messageLite);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }
    
    public void writeGroupNoTag(final MessageLite messageLite) throws IOException {
        final CodedOutputStream codedOutputStream = this.getCodedOutputStream();
        messageLite.writeTo(codedOutputStream);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }
    
    public void writeGroupNoTag(final MessageNano messageNano) throws IOException {
        messageNano.writeTo(this);
    }
    
    public void writeInt32(final int n, final int n2) throws IOException {
        this.writeTag(n, 0);
        this.writeInt32NoTag(n2);
    }
    
    public void writeInt32NoTag(final int n) throws IOException {
        if (n >= 0) {
            this.writeRawVarint32(n);
        }
        else {
            this.writeRawVarint64(n);
        }
    }
    
    public void writeInt64(final int n, final long n2) throws IOException {
        this.writeTag(n, 0);
        this.writeInt64NoTag(n2);
    }
    
    public void writeInt64NoTag(final long n) throws IOException {
        this.writeRawVarint64(n);
    }
    
    public <K, V> void writeMapEntry(final int n, final MapEntryLite<K, V> mapEntryLite, final Map.Entry<K, V> entry) throws IOException {
        final CodedOutputStream codedOutputStream = this.getCodedOutputStream();
        mapEntryLite.serializeTo(codedOutputStream, n, (Object)entry.getKey(), (Object)entry.getValue());
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }
    
    public void writeMessage(final int n, final MessageNano messageNano) throws IOException {
        this.writeTag(n, 2);
        this.writeMessageNoTag(messageNano);
    }
    
    public void writeMessageLite(final int n, final MessageLite messageLite) throws IOException {
        final CodedOutputStream codedOutputStream = this.getCodedOutputStream();
        codedOutputStream.writeMessage(n, messageLite);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }
    
    public void writeMessageNoTag(final MessageLite messageLite) throws IOException {
        this.writeRawVarint32(messageLite.getSerializedSize());
        final CodedOutputStream codedOutputStream = this.getCodedOutputStream();
        messageLite.writeTo(codedOutputStream);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }
    
    public void writeMessageNoTag(final MessageNano messageNano) throws IOException {
        this.writeRawVarint32(messageNano.getCachedSize());
        messageNano.writeTo(this);
    }
    
    public void writeMessageSetExtension(final int n, final MessageNano messageNano) throws IOException {
        this.writeTag(1, 3);
        this.writeUInt32(2, n);
        this.writeMessage(3, messageNano);
        this.writeTag(1, 4);
    }
    
    public void writeRawByte(final byte b) throws IOException {
        if (!this.buffer.hasRemaining()) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.put(b);
    }
    
    public void writeRawByte(final int n) throws IOException {
        this.writeRawByte((byte)n);
    }
    
    public void writeRawBytes(final byte[] array) throws IOException {
        this.writeRawBytes(array, 0, array.length);
    }
    
    public void writeRawBytes(final byte[] src, final int offset, final int length) throws IOException {
        if (this.buffer.remaining() >= length) {
            this.buffer.put(src, offset, length);
            return;
        }
        throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
    }
    
    public void writeRawLittleEndian32(final int n) throws IOException {
        if (this.buffer.remaining() < 4) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.putInt(n);
    }
    
    public void writeRawLittleEndian64(final long n) throws IOException {
        if (this.buffer.remaining() < 8) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.putLong(n);
    }
    
    public void writeRawMessageSetExtension(final int n, final byte[] array) throws IOException {
        this.writeTag(1, 3);
        this.writeUInt32(2, n);
        this.writeTag(3, 2);
        this.writeRawBytes(array);
        this.writeTag(1, 4);
    }
    
    public void writeRawVarint32(int n) throws IOException {
        while ((n & 0xFFFFFF80) != 0x0) {
            this.writeRawByte((n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.writeRawByte(n);
    }
    
    public void writeRawVarint64(long n) throws IOException {
        while ((0xFFFFFFFFFFFFFF80L & n) != 0x0L) {
            this.writeRawByte(((int)n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.writeRawByte((int)n);
    }
    
    public void writeSFixed32(final int n, final int n2) throws IOException {
        this.writeTag(n, 5);
        this.writeSFixed32NoTag(n2);
    }
    
    public void writeSFixed32NoTag(final int n) throws IOException {
        this.writeRawLittleEndian32(n);
    }
    
    public void writeSFixed64(final int n, final long n2) throws IOException {
        this.writeTag(n, 1);
        this.writeSFixed64NoTag(n2);
    }
    
    public void writeSFixed64NoTag(final long n) throws IOException {
        this.writeRawLittleEndian64(n);
    }
    
    public void writeSInt32(final int n, final int n2) throws IOException {
        this.writeTag(n, 0);
        this.writeSInt32NoTag(n2);
    }
    
    public void writeSInt32NoTag(final int n) throws IOException {
        this.writeRawVarint32(encodeZigZag32(n));
    }
    
    public void writeSInt64(final int n, final long n2) throws IOException {
        this.writeTag(n, 0);
        this.writeSInt64NoTag(n2);
    }
    
    public void writeSInt64NoTag(final long n) throws IOException {
        this.writeRawVarint64(encodeZigZag64(n));
    }
    
    public void writeString(final int n, final String s) throws IOException {
        this.writeTag(n, 2);
        this.writeStringNoTag(s);
    }
    
    public void writeStringNoTag(final String s) throws IOException {
        try {
            final int computeRawVarint32Size = computeRawVarint32Size(s.length());
            if (computeRawVarint32Size == computeRawVarint32Size(s.length() * 3)) {
                final int position = this.buffer.position();
                if (this.buffer.remaining() < computeRawVarint32Size) {
                    throw new OutOfSpaceException(position + computeRawVarint32Size, this.buffer.limit());
                }
                this.buffer.position();
                encode(s, this.buffer);
                final int position2 = this.buffer.position();
                this.buffer.position();
                this.writeRawVarint32(position2 - position - computeRawVarint32Size);
                this.buffer.position();
            }
            else {
                this.writeRawVarint32(encodedLength(s));
                encode(s, this.buffer);
            }
        }
        catch (final BufferOverflowException cause) {
            final OutOfSpaceException ex = new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
            ex.initCause(cause);
            throw ex;
        }
    }
    
    public void writeTag(final int n, final int n2) throws IOException {
        this.writeRawVarint32(WireFormatNano.makeTag(n, n2));
    }
    
    public void writeUInt32(final int n, final int n2) throws IOException {
        this.writeTag(n, 0);
        this.writeUInt32NoTag(n2);
    }
    
    public void writeUInt32NoTag(final int n) throws IOException {
        this.writeRawVarint32(n);
    }
    
    public void writeUInt64(final int n, final long n2) throws IOException {
        this.writeTag(n, 0);
        this.writeUInt64NoTag(n2);
    }
    
    public void writeUInt64NoTag(final long n) throws IOException {
        this.writeRawVarint64(n);
    }
    
    public static class OutOfSpaceException extends IOException
    {
        private static final long serialVersionUID = -6947486886997889499L;
        
        OutOfSpaceException(final int i, final int j) {
            final StringBuilder sb = new StringBuilder(108);
            sb.append("CodedOutputStream was writing to a flat byte array and ran out of space (pos ");
            sb.append(i);
            sb.append(" limit ");
            sb.append(j);
            sb.append(").");
            super(sb.toString());
        }
    }
}
