package com.google.protobuf.nano;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.MapFieldLite;
import com.google.protobuf.Parser;
import java.io.IOException;
import kotlin.jvm.internal.ByteCompanionObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class CodedInputByteBufferNano {
    private static final int DEFAULT_RECURSION_LIMIT = 64;
    private static final int DEFAULT_SIZE_LIMIT = 67108864;
    private final byte[] buffer;
    private int bufferPos;
    private final int bufferSize;
    private int bufferSizeAfterLimit;
    private final int bufferStart;
    private CodedInputStream codedInputStream;
    private int lastTag;
    private int maybeLimitedBufferSize;
    private int recursionDepth;
    private int currentLimit = Integer.MAX_VALUE;
    private int recursionLimit = 64;
    private int sizeLimit = DEFAULT_SIZE_LIMIT;

    public static int decodeZigZag32(final int n) {
        return (-(n & 1)) ^ (n >>> 1);
    }

    public static long decodeZigZag64(final long n) {
        return (-(n & 1)) ^ (n >>> 1);
    }

    public void resetSizeCounter() {
    }

    public static CodedInputByteBufferNano newInstance(final byte[] buf) {
        return newInstance(buf, 0, buf.length);
    }

    public static CodedInputByteBufferNano newInstance(final byte[] buf, final int off, final int len) {
        return new CodedInputByteBufferNano(buf, off, len);
    }

    public int readTag() throws IOException {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readRawVarint32();
        if (this.lastTag == 0) {
            throw InvalidProtocolBufferNanoException.invalidTag();
        }
        return this.lastTag;
    }

    public void checkLastTagWas(final int value) throws InvalidProtocolBufferNanoException {
        if (this.lastTag != value) {
            throw InvalidProtocolBufferNanoException.invalidEndTag();
        }
    }

    public boolean skipField(final int tag) throws IOException {
        switch (WireFormatNano.getTagWireType(tag)) {
            case 0:
                readInt32();
                return true;
            case 1:
                readRawLittleEndian64();
                return true;
            case 2:
                skipRawBytes(readRawVarint32());
                return true;
            case 3:
                skipMessage();
                checkLastTagWas(WireFormatNano.makeTag(WireFormatNano.getTagFieldNumber(tag), 4));
                return true;
            case 4:
                return false;
            case 5:
                readRawLittleEndian32();
                return true;
            default:
                throw InvalidProtocolBufferNanoException.invalidWireType();
        }
    }

    public void skipMessage() throws IOException {
        int tag;
        do {
            tag = readTag();
            if (tag == 0) {
                return;
            }
        } while (skipField(tag));
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readRawLittleEndian64());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readRawLittleEndian32());
    }

    public long readUInt64() throws IOException {
        return readRawVarint64();
    }

    public long readInt64() throws IOException {
        return readRawVarint64();
    }

    public int readInt32() throws IOException {
        return readRawVarint32();
    }

    public long readFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public boolean readBool() throws IOException {
        return readRawVarint32() != 0;
    }

    public String readString() throws IOException {
        int rawVarint32 = readRawVarint32();
        if (rawVarint32 < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        if (rawVarint32 > this.maybeLimitedBufferSize - this.bufferPos) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        String str = new String(this.buffer, this.bufferPos, rawVarint32, InternalNano.UTF_8);
        this.bufferPos += rawVarint32;
        return str;
    }

    public void readGroup(final MessageNano msg, final int fieldNumber) throws IOException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        msg.mergeFrom(this);
        checkLastTagWas(WireFormatNano.makeTag(fieldNumber, 4));
        this.recursionDepth--;
    }

    public void readMessage(final MessageNano msg) throws IOException {
        int rawVarint32 = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
        }
        int iPushLimit = pushLimit(rawVarint32);
        this.recursionDepth++;
        msg.mergeFrom(this);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(iPushLimit);
    }

    public byte[] readBytes() throws IOException {
        int rawVarint32 = readRawVarint32();
        if (rawVarint32 < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        if (rawVarint32 == 0) {
            return WireFormatNano.EMPTY_BYTES;
        }
        if (rawVarint32 > this.maybeLimitedBufferSize - this.bufferPos) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        byte[] bArr = new byte[rawVarint32];
        System.arraycopy(this.buffer, this.bufferPos, bArr, 0, rawVarint32);
        this.bufferPos += rawVarint32;
        return bArr;
    }

    public int readUInt32() throws IOException {
        return readRawVarint32();
    }

    public int readEnum() throws IOException {
        return readRawVarint32();
    }

    public int readSFixed32() throws IOException {
        return readRawLittleEndian32();
    }

    public long readSFixed64() throws IOException {
        return readRawLittleEndian64();
    }

    public int readSInt32() throws IOException {
        return decodeZigZag32(readRawVarint32());
    }

    public long readSInt64() throws IOException {
        return decodeZigZag64(readRawVarint64());
    }

    public int readRawVarint32() throws IOException {
        byte rawByte = readRawByte();
        if (rawByte >= 0) {
            return rawByte;
        }
        int i = rawByte & ByteCompanionObject.MAX_VALUE;
        byte rawByte2 = readRawByte();
        if (rawByte2 >= 0) {
            return (rawByte2 << 7) | i;
        }
        int i2 = i | ((rawByte2 & ByteCompanionObject.MAX_VALUE) << 7);
        byte rawByte3 = readRawByte();
        if (rawByte3 >= 0) {
            return (rawByte3 << 14) | i2;
        }
        int i3 = i2 | ((rawByte3 & ByteCompanionObject.MAX_VALUE) << 14);
        byte rawByte4 = readRawByte();
        if (rawByte4 >= 0) {
            return (rawByte4 << 21) | i3;
        }
        int i4 = i3 | ((rawByte4 & ByteCompanionObject.MAX_VALUE) << 21);
        byte rawByte5 = readRawByte();
        int i5 = i4 | (rawByte5 << 28);
        if (rawByte5 >= 0) {
            return i5;
        }
        for (int i6 = 0; i6 < 5; i6++) {
            if (readRawByte() >= 0) {
                return i5;
            }
        }
        throw InvalidProtocolBufferNanoException.malformedVarint();
    }

    public long readRawVarint64() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            j |= (r3 & ByteCompanionObject.MAX_VALUE) << i;
            if ((readRawByte() & ByteCompanionObject.MIN_VALUE) == 0) {
                return j;
            }
        }
        throw InvalidProtocolBufferNanoException.malformedVarint();
    }

    public int readRawLittleEndian32() throws IOException {
        byte rawByte = readRawByte();
        byte rawByte2 = readRawByte();
        byte rawByte3 = readRawByte();
        return ((readRawByte() & 255) << 24) | (rawByte & 255) | ((rawByte2 & 255) << 8) | ((rawByte3 & 255) << 16);
    }

    public long readRawLittleEndian64() throws IOException {
        return ((readRawByte() & 255) << 8) | (readRawByte() & 255) | ((readRawByte() & 255) << 16) | ((readRawByte() & 255) << 24) | ((readRawByte() & 255) << 32) | ((readRawByte() & 255) << 40) | ((readRawByte() & 255) << 48) | ((readRawByte() & 255) << 56);
    }

    private CodedInputByteBufferNano(final byte[] buffer, final int off, final int len) {
        this.buffer = buffer;
        this.bufferStart = off;
        int i = len + off;
        this.maybeLimitedBufferSize = i;
        this.bufferSize = i;
        this.bufferPos = off;
    }

    private CodedInputStream getCodedInputStream() throws IOException {
        if (this.codedInputStream == null) {
            this.codedInputStream = CodedInputStream.newInstance(this.buffer, this.bufferStart, this.bufferSize);
        }
        int totalBytesRead = this.codedInputStream.getTotalBytesRead();
        int i = this.bufferPos - this.bufferStart;
        if (totalBytesRead > i) {
            throw new IOException(String.format("CodedInputStream read ahead of CodedInputByteBufferNano: %s > %s", Integer.valueOf(totalBytesRead), Integer.valueOf(i)));
        }
        this.codedInputStream.skipRawBytes(i - totalBytesRead);
        this.codedInputStream.setRecursionLimit(this.recursionLimit - this.recursionDepth);
        return this.codedInputStream;
    }

    public <T extends GeneratedMessageLite<T, ?>> T readMessageLite(Parser<T> parser) throws IOException {
        T message = getCodedInputStream().readMessage(parser, ExtensionRegistryLite.getGeneratedRegistry());
        skipField(this.lastTag);
        return message;
    }

    public <T extends GeneratedMessageLite<T, ?>> T readGroupLite(final Parser<T> parser, final int fieldNumber) throws IOException {
        T group = getCodedInputStream().readGroup(fieldNumber, parser, ExtensionRegistryLite.getGeneratedRegistry());
        skipField(this.lastTag);
        checkLastTagWas(WireFormatNano.makeTag(fieldNumber, 4));
        return group;
    }

    public <K, V> void readMapEntryInto(MapFieldLite<K, V> map, MapEntryLite<K, V> defaultEntry) throws IOException {
        defaultEntry.parseInto(map, getCodedInputStream(), ExtensionRegistryLite.getGeneratedRegistry());
        skipField(this.lastTag);
    }

    public int setRecursionLimit(final int limit) {
        if (limit < 0) {
            StringBuilder sb = new StringBuilder(47);
            sb.append("Recursion limit cannot be negative: ");
            sb.append(limit);
            throw new IllegalArgumentException(sb.toString());
        }
        int i = this.recursionLimit;
        this.recursionLimit = limit;
        return i;
    }

    public int setSizeLimit(final int limit) {
        if (limit < 0) {
            StringBuilder sb = new StringBuilder(42);
            sb.append("Size limit cannot be negative: ");
            sb.append(limit);
            throw new IllegalArgumentException(sb.toString());
        }
        int i = this.sizeLimit;
        this.sizeLimit = limit;
        return i;
    }

    public int pushLimit(int byteLimit) throws InvalidProtocolBufferNanoException {
        if (byteLimit < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        int i = byteLimit + this.bufferPos;
        int i2 = this.currentLimit;
        if (i > i2) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        this.currentLimit = i;
        recomputeBufferSizeAfterLimit();
        return i2;
    }

    private void recomputeBufferSizeAfterLimit() {
        this.maybeLimitedBufferSize += this.bufferSizeAfterLimit;
        int i = this.maybeLimitedBufferSize;
        if (i > this.currentLimit) {
            this.bufferSizeAfterLimit = i - this.currentLimit;
            this.maybeLimitedBufferSize -= this.bufferSizeAfterLimit;
        } else {
            this.bufferSizeAfterLimit = 0;
        }
    }

    public void popLimit(final int oldLimit) {
        this.currentLimit = oldLimit;
        recomputeBufferSizeAfterLimit();
    }

    public int getBytesUntilLimit() {
        if (this.currentLimit == Integer.MAX_VALUE) {
            return -1;
        }
        return this.currentLimit - this.bufferPos;
    }

    public boolean isAtEnd() {
        return this.bufferPos == this.maybeLimitedBufferSize;
    }

    public int getPosition() {
        return this.bufferPos - this.bufferStart;
    }

    public byte[] getData(int offset, int length) {
        if (length == 0) {
            return WireFormatNano.EMPTY_BYTES;
        }
        byte[] bArr = new byte[length];
        System.arraycopy(this.buffer, this.bufferStart + offset, bArr, 0, length);
        return bArr;
    }

    public void rewindToPosition(int position) {
        rewindToPositionAndTag(position, this.lastTag);
    }

    void rewindToPositionAndTag(int position, int tag) {
        if (position > this.bufferPos - this.bufferStart) {
            int i = this.bufferPos - this.bufferStart;
            StringBuilder sb = new StringBuilder(50);
            sb.append("Position ");
            sb.append(position);
            sb.append(" is beyond current ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        if (position < 0) {
            StringBuilder sb2 = new StringBuilder(24);
            sb2.append("Bad position ");
            sb2.append(position);
            throw new IllegalArgumentException(sb2.toString());
        }
        this.bufferPos = this.bufferStart + position;
        this.lastTag = tag;
    }

    public byte readRawByte() throws IOException {
        if (this.bufferPos == this.maybeLimitedBufferSize) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        byte[] bArr = this.buffer;
        int i = this.bufferPos;
        this.bufferPos = i + 1;
        return bArr[i];
    }

    public byte[] readRawBytes(final int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        if (this.bufferPos + size > this.currentLimit) {
            skipRawBytes(this.currentLimit - this.bufferPos);
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        if (size > this.maybeLimitedBufferSize - this.bufferPos) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        byte[] bArr = new byte[size];
        System.arraycopy(this.buffer, this.bufferPos, bArr, 0, size);
        this.bufferPos += size;
        return bArr;
    }

    public void skipRawBytes(final int size) throws IOException {
        if (size < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        if (this.bufferPos + size > this.currentLimit) {
            skipRawBytes(this.currentLimit - this.bufferPos);
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        if (size <= this.maybeLimitedBufferSize - this.bufferPos) {
            this.bufferPos += size;
            return;
        }
        throw InvalidProtocolBufferNanoException.truncatedMessage();
    }
}
