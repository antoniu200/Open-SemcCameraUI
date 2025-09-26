// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.io.IOException;
import java.util.Arrays;

final class UnknownFieldData
{
    final byte[] bytes;
    final int tag;
    
    UnknownFieldData(final int tag, final byte[] bytes) {
        this.tag = tag;
        this.bytes = bytes;
    }
    
    int computeSerializedSize() {
        return 0 + CodedOutputByteBufferNano.computeRawVarint32Size(this.tag) + this.bytes.length;
    }
    
    int computeSerializedSizeAsMessageSet() {
        return CodedOutputByteBufferNano.computeRawMessageSetExtensionSize(this.tag, this.bytes);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof UnknownFieldData)) {
            return false;
        }
        final UnknownFieldData unknownFieldData = (UnknownFieldData)o;
        if (this.tag != unknownFieldData.tag || !Arrays.equals(this.bytes, unknownFieldData.bytes)) {
            b = false;
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        return 31 * (527 + this.tag) + Arrays.hashCode(this.bytes);
    }
    
    void writeAsMessageSetTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        codedOutputByteBufferNano.writeRawMessageSetExtension(this.tag, this.bytes);
    }
    
    void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        codedOutputByteBufferNano.writeRawVarint32(this.tag);
        codedOutputByteBufferNano.writeRawBytes(this.bytes);
    }
}
