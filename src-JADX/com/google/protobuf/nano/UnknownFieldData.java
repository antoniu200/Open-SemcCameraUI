package com.google.protobuf.nano;

import java.io.IOException;
import java.util.Arrays;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class UnknownFieldData {
    final byte[] bytes;
    final int tag;

    UnknownFieldData(int tag, byte[] bytes) {
        this.tag = tag;
        this.bytes = bytes;
    }

    int computeSerializedSize() {
        return 0 + CodedOutputByteBufferNano.computeRawVarint32Size(this.tag) + this.bytes.length;
    }

    void writeTo(CodedOutputByteBufferNano output) throws IOException {
        output.writeRawVarint32(this.tag);
        output.writeRawBytes(this.bytes);
    }

    int computeSerializedSizeAsMessageSet() {
        return CodedOutputByteBufferNano.computeRawMessageSetExtensionSize(this.tag, this.bytes);
    }

    void writeAsMessageSetTo(CodedOutputByteBufferNano output) throws IOException {
        output.writeRawMessageSetExtension(this.tag, this.bytes);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UnknownFieldData)) {
            return false;
        }
        UnknownFieldData unknownFieldData = (UnknownFieldData) o;
        return this.tag == unknownFieldData.tag && Arrays.equals(this.bytes, unknownFieldData.bytes);
    }

    public int hashCode() {
        return (31 * (527 + this.tag)) + Arrays.hashCode(this.bytes);
    }
}
