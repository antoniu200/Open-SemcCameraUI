package com.google.protobuf.nano;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class CodedOutputByteBufferNano {
    public static final int LITTLE_ENDIAN_32_SIZE = 4;
    public static final int LITTLE_ENDIAN_64_SIZE = 8;
    private static final int MAX_UTF8_EXPANSION = 3;
    private final ByteBuffer buffer;
    private CodedOutputStream codedOutputStream;
    private int codedOutputStreamPosition;

    public static int computeBoolSizeNoTag(final boolean value) {
        return 1;
    }

    public static int computeDoubleSizeNoTag(final double value) {
        return 8;
    }

    public static int computeFixed32SizeNoTag(final int value) {
        return 4;
    }

    public static int computeFixed64SizeNoTag(final long value) {
        return 8;
    }

    public static int computeFloatSizeNoTag(final float value) {
        return 4;
    }

    public static int computeRawVarint32Size(final int value) {
        if ((value & (-128)) == 0) {
            return 1;
        }
        if ((value & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & value) == 0) {
            return 3;
        }
        return (value & (-268435456)) == 0 ? 4 : 5;
    }

    public static int computeRawVarint64Size(final long value) {
        if (((-128) & value) == 0) {
            return 1;
        }
        if (((-16384) & value) == 0) {
            return 2;
        }
        if (((-2097152) & value) == 0) {
            return 3;
        }
        if (((-268435456) & value) == 0) {
            return 4;
        }
        if (((-34359738368L) & value) == 0) {
            return 5;
        }
        if (((-4398046511104L) & value) == 0) {
            return 6;
        }
        if (((-562949953421312L) & value) == 0) {
            return 7;
        }
        if (((-72057594037927936L) & value) == 0) {
            return 8;
        }
        return (value & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    public static int computeSFixed32SizeNoTag(final int value) {
        return 4;
    }

    public static int computeSFixed64SizeNoTag(final long value) {
        return 8;
    }

    public static int encodeZigZag32(final int n) {
        return (n >> 31) ^ (n << 1);
    }

    public static long encodeZigZag64(final long n) {
        return (n >> 63) ^ (n << 1);
    }

    private CodedOutputByteBufferNano(final byte[] buffer, final int offset, final int length) {
        this(ByteBuffer.wrap(buffer, offset, length));
    }

    private CodedOutputByteBufferNano(final ByteBuffer buffer) {
        this.buffer = buffer;
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static CodedOutputByteBufferNano newInstance(final byte[] flatArray) {
        return newInstance(flatArray, 0, flatArray.length);
    }

    public static CodedOutputByteBufferNano newInstance(final byte[] flatArray, final int offset, final int length) {
        return new CodedOutputByteBufferNano(flatArray, offset, length);
    }

    private CodedOutputStream getCodedOutputStream() throws IOException {
        if (this.codedOutputStream == null) {
            this.codedOutputStream = CodedOutputStream.newInstance(this.buffer);
            this.codedOutputStreamPosition = this.buffer.position();
        } else if (this.codedOutputStreamPosition != this.buffer.position()) {
            this.codedOutputStream.write(this.buffer.array(), this.codedOutputStreamPosition, this.buffer.position() - this.codedOutputStreamPosition);
            this.codedOutputStreamPosition = this.buffer.position();
        }
        return this.codedOutputStream;
    }

    public void writeDouble(final int fieldNumber, final double value) throws IOException {
        writeTag(fieldNumber, 1);
        writeDoubleNoTag(value);
    }

    public void writeFloat(final int fieldNumber, final float value) throws IOException {
        writeTag(fieldNumber, 5);
        writeFloatNoTag(value);
    }

    public void writeUInt64(final int fieldNumber, final long value) throws IOException {
        writeTag(fieldNumber, 0);
        writeUInt64NoTag(value);
    }

    public void writeInt64(final int fieldNumber, final long value) throws IOException {
        writeTag(fieldNumber, 0);
        writeInt64NoTag(value);
    }

    public void writeInt32(final int fieldNumber, final int value) throws IOException {
        writeTag(fieldNumber, 0);
        writeInt32NoTag(value);
    }

    public void writeFixed64(final int fieldNumber, final long value) throws IOException {
        writeTag(fieldNumber, 1);
        writeFixed64NoTag(value);
    }

    public void writeFixed32(final int fieldNumber, final int value) throws IOException {
        writeTag(fieldNumber, 5);
        writeFixed32NoTag(value);
    }

    public void writeBool(final int fieldNumber, final boolean value) throws IOException {
        writeTag(fieldNumber, 0);
        writeBoolNoTag(value);
    }

    public void writeString(final int fieldNumber, final String value) throws IOException {
        writeTag(fieldNumber, 2);
        writeStringNoTag(value);
    }

    public void writeGroup(final int fieldNumber, final MessageNano value) throws IOException {
        writeTag(fieldNumber, 3);
        writeGroupNoTag(value);
        writeTag(fieldNumber, 4);
    }

    public void writeGroupLite(final int fieldNumber, final MessageLite value) throws IOException {
        CodedOutputStream codedOutputStream = getCodedOutputStream();
        codedOutputStream.writeGroup(fieldNumber, value);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }

    public void writeMessage(final int fieldNumber, final MessageNano value) throws IOException {
        writeTag(fieldNumber, 2);
        writeMessageNoTag(value);
    }

    public void writeMessageLite(final int fieldNumber, final MessageLite value) throws IOException {
        CodedOutputStream codedOutputStream = getCodedOutputStream();
        codedOutputStream.writeMessage(fieldNumber, value);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }

    public <K, V> void writeMapEntry(final int fieldNumber, MapEntryLite<K, V> defaultEntry, Map.Entry<K, V> entry) throws IOException {
        CodedOutputStream codedOutputStream = getCodedOutputStream();
        defaultEntry.serializeTo(codedOutputStream, fieldNumber, entry.getKey(), entry.getValue());
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }

    public void writeBytes(final int fieldNumber, final byte[] value) throws IOException {
        writeTag(fieldNumber, 2);
        writeBytesNoTag(value);
    }

    public void writeUInt32(final int fieldNumber, final int value) throws IOException {
        writeTag(fieldNumber, 0);
        writeUInt32NoTag(value);
    }

    public void writeEnum(final int fieldNumber, final int value) throws IOException {
        writeTag(fieldNumber, 0);
        writeEnumNoTag(value);
    }

    public void writeSFixed32(final int fieldNumber, final int value) throws IOException {
        writeTag(fieldNumber, 5);
        writeSFixed32NoTag(value);
    }

    public void writeSFixed64(final int fieldNumber, final long value) throws IOException {
        writeTag(fieldNumber, 1);
        writeSFixed64NoTag(value);
    }

    public void writeSInt32(final int fieldNumber, final int value) throws IOException {
        writeTag(fieldNumber, 0);
        writeSInt32NoTag(value);
    }

    public void writeSInt64(final int fieldNumber, final long value) throws IOException {
        writeTag(fieldNumber, 0);
        writeSInt64NoTag(value);
    }

    public void writeMessageSetExtension(final int fieldNumber, final MessageNano value) throws IOException {
        writeTag(1, 3);
        writeUInt32(2, fieldNumber);
        writeMessage(3, value);
        writeTag(1, 4);
    }

    public void writeRawMessageSetExtension(final int fieldNumber, final byte[] value) throws IOException {
        writeTag(1, 3);
        writeUInt32(2, fieldNumber);
        writeTag(3, 2);
        writeRawBytes(value);
        writeTag(1, 4);
    }

    public void writeDoubleNoTag(final double value) throws IOException {
        writeRawLittleEndian64(Double.doubleToLongBits(value));
    }

    public void writeFloatNoTag(final float value) throws IOException {
        writeRawLittleEndian32(Float.floatToIntBits(value));
    }

    public void writeUInt64NoTag(final long value) throws IOException {
        writeRawVarint64(value);
    }

    public void writeInt64NoTag(final long value) throws IOException {
        writeRawVarint64(value);
    }

    public void writeInt32NoTag(final int value) throws IOException {
        if (value >= 0) {
            writeRawVarint32(value);
        } else {
            writeRawVarint64(value);
        }
    }

    public void writeFixed64NoTag(final long value) throws IOException {
        writeRawLittleEndian64(value);
    }

    public void writeFixed32NoTag(final int value) throws IOException {
        writeRawLittleEndian32(value);
    }

    public void writeBoolNoTag(boolean z) throws IOException {
        writeRawByte(z ? 1 : 0);
    }

    public void writeStringNoTag(final String value) throws IOException {
        try {
            int iComputeRawVarint32Size = computeRawVarint32Size(value.length());
            if (iComputeRawVarint32Size == computeRawVarint32Size(value.length() * 3)) {
                int iPosition = this.buffer.position();
                if (this.buffer.remaining() < iComputeRawVarint32Size) {
                    throw new OutOfSpaceException(iPosition + iComputeRawVarint32Size, this.buffer.limit());
                }
                this.buffer.position(iPosition + iComputeRawVarint32Size);
                encode(value, this.buffer);
                int iPosition2 = this.buffer.position();
                this.buffer.position(iPosition);
                writeRawVarint32((iPosition2 - iPosition) - iComputeRawVarint32Size);
                this.buffer.position(iPosition2);
                return;
            }
            writeRawVarint32(encodedLength(value));
            encode(value, this.buffer);
        } catch (BufferOverflowException e) {
            OutOfSpaceException outOfSpaceException = new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
            outOfSpaceException.initCause(e);
            throw outOfSpaceException;
        }
    }

    private static int encodedLength(CharSequence sequence) {
        int length = sequence.length();
        int i = 0;
        while (i < length && sequence.charAt(i) < 128) {
            i++;
        }
        int iEncodedLengthGeneral = length;
        while (true) {
            if (i < length) {
                char cCharAt = sequence.charAt(i);
                if (cCharAt >= 2048) {
                    iEncodedLengthGeneral += encodedLengthGeneral(sequence, i);
                    break;
                }
                iEncodedLengthGeneral += (127 - cCharAt) >>> 31;
                i++;
            } else {
                break;
            }
        }
        if (iEncodedLengthGeneral >= length) {
            return iEncodedLengthGeneral;
        }
        StringBuilder sb = new StringBuilder(54);
        sb.append("UTF-8 length does not fit in int: ");
        sb.append(iEncodedLengthGeneral + 4294967296L);
        throw new IllegalArgumentException(sb.toString());
    }

    private static int encodedLengthGeneral(CharSequence sequence, int start) {
        int length = sequence.length();
        int i = 0;
        while (start < length) {
            char cCharAt = sequence.charAt(start);
            if (cCharAt < 2048) {
                i += (127 - cCharAt) >>> 31;
            } else {
                i += 2;
                if (55296 <= cCharAt && cCharAt <= 57343) {
                    if (Character.codePointAt(sequence, start) < 65536) {
                        StringBuilder sb = new StringBuilder(39);
                        sb.append("Unpaired surrogate at index ");
                        sb.append(start);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    start++;
                }
            }
            start++;
        }
        return i;
    }

    private static void encode(CharSequence sequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer.hasArray()) {
            try {
                byteBuffer.position(encode(sequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
                return;
            } catch (ArrayIndexOutOfBoundsException e) {
                BufferOverflowException bufferOverflowException = new BufferOverflowException();
                bufferOverflowException.initCause(e);
                throw bufferOverflowException;
            }
        }
        encodeDirect(sequence, byteBuffer);
    }

    private static void encodeDirect(CharSequence sequence, ByteBuffer byteBuffer) {
        int length = sequence.length();
        int i = 0;
        while (i < length) {
            char cCharAt = sequence.charAt(i);
            if (cCharAt < 128) {
                byteBuffer.put((byte) cCharAt);
            } else if (cCharAt < 2048) {
                byteBuffer.put((byte) (960 | (cCharAt >>> 6)));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            } else if (cCharAt < 55296 || 57343 < cCharAt) {
                byteBuffer.put((byte) (480 | (cCharAt >>> '\f')));
                byteBuffer.put((byte) (((cCharAt >>> 6) & 63) | 128));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            } else {
                int i2 = i + 1;
                if (i2 != sequence.length()) {
                    char cCharAt2 = sequence.charAt(i2);
                    if (Character.isSurrogatePair(cCharAt, cCharAt2)) {
                        int codePoint = Character.toCodePoint(cCharAt, cCharAt2);
                        byteBuffer.put((byte) (240 | (codePoint >>> 18)));
                        byteBuffer.put((byte) (((codePoint >>> 12) & 63) | 128));
                        byteBuffer.put((byte) (((codePoint >>> 6) & 63) | 128));
                        byteBuffer.put((byte) ((codePoint & 63) | 128));
                        i = i2;
                    } else {
                        i = i2;
                    }
                }
                StringBuilder sb = new StringBuilder(39);
                sb.append("Unpaired surrogate at index ");
                sb.append(i - 1);
                throw new IllegalArgumentException(sb.toString());
            }
            i++;
        }
    }

    private static int encode(CharSequence sequence, byte[] bytes, int offset, int length) {
        int i;
        int i2;
        char cCharAt;
        int length2 = sequence.length();
        int i3 = length + offset;
        int i4 = 0;
        while (i4 < length2 && (i2 = i4 + offset) < i3 && (cCharAt = sequence.charAt(i4)) < 128) {
            bytes[i2] = (byte) cCharAt;
            i4++;
        }
        if (i4 == length2) {
            return offset + length2;
        }
        int i5 = offset + i4;
        while (i4 < length2) {
            char cCharAt2 = sequence.charAt(i4);
            if (cCharAt2 >= 128 || i5 >= i3) {
                if (cCharAt2 < 2048 && i5 <= i3 - 2) {
                    int i6 = i5 + 1;
                    bytes[i5] = (byte) (960 | (cCharAt2 >>> 6));
                    i5 = i6 + 1;
                    bytes[i6] = (byte) ((cCharAt2 & '?') | 128);
                } else {
                    if ((cCharAt2 >= 55296 && 57343 >= cCharAt2) || i5 > i3 - 3) {
                        if (i5 <= i3 - 4) {
                            int i7 = i4 + 1;
                            if (i7 != sequence.length()) {
                                char cCharAt3 = sequence.charAt(i7);
                                if (Character.isSurrogatePair(cCharAt2, cCharAt3)) {
                                    int codePoint = Character.toCodePoint(cCharAt2, cCharAt3);
                                    int i8 = i5 + 1;
                                    bytes[i5] = (byte) (240 | (codePoint >>> 18));
                                    int i9 = i8 + 1;
                                    bytes[i8] = (byte) (((codePoint >>> 12) & 63) | 128);
                                    int i10 = i9 + 1;
                                    bytes[i9] = (byte) (((codePoint >>> 6) & 63) | 128);
                                    i5 = i10 + 1;
                                    bytes[i10] = (byte) ((codePoint & 63) | 128);
                                    i4 = i7;
                                } else {
                                    i4 = i7;
                                }
                            }
                            StringBuilder sb = new StringBuilder(39);
                            sb.append("Unpaired surrogate at index ");
                            sb.append(i4 - 1);
                            throw new IllegalArgumentException(sb.toString());
                        }
                        StringBuilder sb2 = new StringBuilder(37);
                        sb2.append("Failed writing ");
                        sb2.append(cCharAt2);
                        sb2.append(" at index ");
                        sb2.append(i5);
                        throw new ArrayIndexOutOfBoundsException(sb2.toString());
                    }
                    int i11 = i5 + 1;
                    bytes[i5] = (byte) (480 | (cCharAt2 >>> '\f'));
                    int i12 = i11 + 1;
                    bytes[i11] = (byte) (((cCharAt2 >>> 6) & 63) | 128);
                    i = i12 + 1;
                    bytes[i12] = (byte) ((cCharAt2 & '?') | 128);
                }
                i4++;
            } else {
                i = i5 + 1;
                bytes[i5] = (byte) cCharAt2;
            }
            i5 = i;
            i4++;
        }
        return i5;
    }

    public void writeGroupNoTag(final MessageNano value) throws IOException {
        value.writeTo(this);
    }

    public void writeGroupNoTag(final MessageLite value) throws IOException {
        CodedOutputStream codedOutputStream = getCodedOutputStream();
        value.writeTo(codedOutputStream);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }

    public void writeMessageNoTag(final MessageNano value) throws IOException {
        writeRawVarint32(value.getCachedSize());
        value.writeTo(this);
    }

    public void writeMessageNoTag(final MessageLite value) throws IOException {
        writeRawVarint32(value.getSerializedSize());
        CodedOutputStream codedOutputStream = getCodedOutputStream();
        value.writeTo(codedOutputStream);
        codedOutputStream.flush();
        this.codedOutputStreamPosition = this.buffer.position();
    }

    public void writeBytesNoTag(final byte[] value) throws IOException {
        writeRawVarint32(value.length);
        writeRawBytes(value);
    }

    public void writeUInt32NoTag(final int value) throws IOException {
        writeRawVarint32(value);
    }

    public void writeEnumNoTag(final int value) throws IOException {
        writeRawVarint32(value);
    }

    public void writeSFixed32NoTag(final int value) throws IOException {
        writeRawLittleEndian32(value);
    }

    public void writeSFixed64NoTag(final long value) throws IOException {
        writeRawLittleEndian64(value);
    }

    public void writeSInt32NoTag(final int value) throws IOException {
        writeRawVarint32(encodeZigZag32(value));
    }

    public void writeSInt64NoTag(final long value) throws IOException {
        writeRawVarint64(encodeZigZag64(value));
    }

    public static int computeDoubleSize(final int fieldNumber, final double value) {
        return computeTagSize(fieldNumber) + computeDoubleSizeNoTag(value);
    }

    public static int computeFloatSize(final int fieldNumber, final float value) {
        return computeTagSize(fieldNumber) + computeFloatSizeNoTag(value);
    }

    public static int computeUInt64Size(final int fieldNumber, final long value) {
        return computeTagSize(fieldNumber) + computeUInt64SizeNoTag(value);
    }

    public static int computeInt64Size(final int fieldNumber, final long value) {
        return computeTagSize(fieldNumber) + computeInt64SizeNoTag(value);
    }

    public static int computeInt32Size(final int fieldNumber, final int value) {
        return computeTagSize(fieldNumber) + computeInt32SizeNoTag(value);
    }

    public static int computeFixed64Size(final int fieldNumber, final long value) {
        return computeTagSize(fieldNumber) + computeFixed64SizeNoTag(value);
    }

    public static int computeFixed32Size(final int fieldNumber, final int value) {
        return computeTagSize(fieldNumber) + computeFixed32SizeNoTag(value);
    }

    public static int computeBoolSize(final int fieldNumber, final boolean value) {
        return computeTagSize(fieldNumber) + computeBoolSizeNoTag(value);
    }

    public static int computeStringSize(final int fieldNumber, final String value) {
        return computeTagSize(fieldNumber) + computeStringSizeNoTag(value);
    }

    public static int computeGroupSize(final int fieldNumber, final MessageNano value) {
        return (computeTagSize(fieldNumber) * 2) + computeGroupSizeNoTag(value);
    }

    public static int computeMessageSize(final int fieldNumber, final MessageNano value) {
        return computeTagSize(fieldNumber) + computeMessageSizeNoTag(value);
    }

    public static int computeBytesSize(final int fieldNumber, final byte[] value) {
        return computeTagSize(fieldNumber) + computeBytesSizeNoTag(value);
    }

    public static int computeUInt32Size(final int fieldNumber, final int value) {
        return computeTagSize(fieldNumber) + computeUInt32SizeNoTag(value);
    }

    public static int computeEnumSize(final int fieldNumber, final int value) {
        return computeTagSize(fieldNumber) + computeEnumSizeNoTag(value);
    }

    public static int computeSFixed32Size(final int fieldNumber, final int value) {
        return computeTagSize(fieldNumber) + computeSFixed32SizeNoTag(value);
    }

    public static int computeSFixed64Size(final int fieldNumber, final long value) {
        return computeTagSize(fieldNumber) + computeSFixed64SizeNoTag(value);
    }

    public static int computeSInt32Size(final int fieldNumber, final int value) {
        return computeTagSize(fieldNumber) + computeSInt32SizeNoTag(value);
    }

    public static int computeSInt64Size(final int fieldNumber, final long value) {
        return computeTagSize(fieldNumber) + computeSInt64SizeNoTag(value);
    }

    public static int computeMessageSetExtensionSize(final int fieldNumber, final MessageNano value) {
        return (computeTagSize(1) * 2) + computeUInt32Size(2, fieldNumber) + computeMessageSize(3, value);
    }

    public static int computeRawMessageSetExtensionSize(final int fieldNumber, final byte[] value) {
        return (computeTagSize(1) * 2) + computeUInt32Size(2, fieldNumber) + computeTagSize(3) + value.length;
    }

    public static int computeUInt64SizeNoTag(final long value) {
        return computeRawVarint64Size(value);
    }

    public static int computeInt64SizeNoTag(final long value) {
        return computeRawVarint64Size(value);
    }

    public static int computeInt32SizeNoTag(final int value) {
        if (value >= 0) {
            return computeRawVarint32Size(value);
        }
        return 10;
    }

    public static int computeStringSizeNoTag(final String value) {
        int iEncodedLength = encodedLength(value);
        return computeRawVarint32Size(iEncodedLength) + iEncodedLength;
    }

    public static int computeGroupSizeNoTag(final MessageNano value) {
        return value.getSerializedSize();
    }

    public static int computeMessageSizeNoTag(final MessageNano value) {
        int serializedSize = value.getSerializedSize();
        return computeRawVarint32Size(serializedSize) + serializedSize;
    }

    public static int computeBytesSizeNoTag(final byte[] value) {
        return computeRawVarint32Size(value.length) + value.length;
    }

    public static int computeUInt32SizeNoTag(final int value) {
        return computeRawVarint32Size(value);
    }

    public static int computeEnumSizeNoTag(final int value) {
        return computeRawVarint32Size(value);
    }

    public static int computeSInt32SizeNoTag(final int value) {
        return computeRawVarint32Size(encodeZigZag32(value));
    }

    public static int computeSInt64SizeNoTag(final long value) {
        return computeRawVarint64Size(encodeZigZag64(value));
    }

    public int spaceLeft() {
        return this.buffer.remaining();
    }

    public void checkNoSpaceLeft() {
        if (spaceLeft() != 0) {
            throw new IllegalStateException(String.format("Did not write as much data as expected, %s bytes remaining.", Integer.valueOf(spaceLeft())));
        }
    }

    public int position() {
        return this.buffer.position();
    }

    public void reset() {
        this.buffer.clear();
    }

    public static class OutOfSpaceException extends IOException {
        private static final long serialVersionUID = -6947486886997889499L;

        OutOfSpaceException(int position, int limit) {
            StringBuilder sb = new StringBuilder(108);
            sb.append("CodedOutputStream was writing to a flat byte array and ran out of space (pos ");
            sb.append(position);
            sb.append(" limit ");
            sb.append(limit);
            sb.append(").");
            super(sb.toString());
        }
    }

    public void writeRawByte(final byte value) throws IOException {
        if (!this.buffer.hasRemaining()) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.put(value);
    }

    public void writeRawByte(final int value) throws IOException {
        writeRawByte((byte) value);
    }

    public void writeRawBytes(final byte[] value) throws IOException {
        writeRawBytes(value, 0, value.length);
    }

    public void writeRawBytes(final byte[] value, int offset, int length) throws IOException {
        if (this.buffer.remaining() >= length) {
            this.buffer.put(value, offset, length);
            return;
        }
        throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
    }

    public void writeTag(final int fieldNumber, final int wireType) throws IOException {
        writeRawVarint32(WireFormatNano.makeTag(fieldNumber, wireType));
    }

    public static int computeTagSize(final int fieldNumber) {
        return computeRawVarint32Size(WireFormatNano.makeTag(fieldNumber, 0));
    }

    public void writeRawVarint32(int value) throws IOException {
        while ((value & (-128)) != 0) {
            writeRawByte((value & 127) | 128);
            value >>>= 7;
        }
        writeRawByte(value);
    }

    public void writeRawVarint64(long value) throws IOException {
        while (((-128) & value) != 0) {
            writeRawByte((((int) value) & 127) | 128);
            value >>>= 7;
        }
        writeRawByte((int) value);
    }

    public void writeRawLittleEndian32(final int value) throws IOException {
        if (this.buffer.remaining() < 4) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.putInt(value);
    }

    public void writeRawLittleEndian64(final long value) throws IOException {
        if (this.buffer.remaining() < 8) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.putLong(value);
    }
}
