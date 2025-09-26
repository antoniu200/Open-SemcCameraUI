// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.search.shared.service.proto.nano;

import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.ExtendableMessageNano;

public final class LensServiceClientEventData extends ExtendableMessageNano<LensServiceClientEventData> implements Cloneable
{
    private static volatile LensServiceClientEventData[] _emptyArray;
    private int bitField0_;
    private int targetServiceApiVersion_;
    
    public LensServiceClientEventData() {
        this.clear();
    }
    
    public static LensServiceClientEventData[] emptyArray() {
        if (LensServiceClientEventData._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (LensServiceClientEventData._emptyArray == null) {
                    LensServiceClientEventData._emptyArray = new LensServiceClientEventData[0];
                }
            }
        }
        return LensServiceClientEventData._emptyArray;
    }
    
    public static LensServiceClientEventData parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new LensServiceClientEventData().mergeFrom(codedInputByteBufferNano);
    }
    
    public static LensServiceClientEventData parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
        return MessageNano.mergeFrom(new LensServiceClientEventData(), array);
    }
    
    public LensServiceClientEventData clear() {
        this.bitField0_ = 0;
        this.targetServiceApiVersion_ = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }
    
    public LensServiceClientEventData clearTargetServiceApiVersion() {
        this.targetServiceApiVersion_ = 0;
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
    }
    
    @Override
    public LensServiceClientEventData clone() {
        try {
            return super.clone();
        }
        catch (final CloneNotSupportedException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    @Override
    protected int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if ((this.bitField0_ & 0x1) != 0x0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.targetServiceApiVersion_);
        }
        return computeSerializedSize;
    }
    
    public int getTargetServiceApiVersion() {
        return this.targetServiceApiVersion_;
    }
    
    public boolean hasTargetServiceApiVersion() {
        final int bitField0_ = this.bitField0_;
        boolean b = true;
        if ((bitField0_ & 0x1) == 0x0) {
            b = false;
        }
        return b;
    }
    
    @Override
    public LensServiceClientEventData mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            final int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                return this;
            }
            if (tag != 8) {
                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                    return this;
                }
                continue;
            }
            else {
                this.targetServiceApiVersion_ = codedInputByteBufferNano.readInt32();
                this.bitField0_ |= 0x1;
            }
        }
    }
    
    public LensServiceClientEventData setTargetServiceApiVersion(final int targetServiceApiVersion_) {
        this.bitField0_ |= 0x1;
        this.targetServiceApiVersion_ = targetServiceApiVersion_;
        return this;
    }
    
    @Override
    public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if ((this.bitField0_ & 0x1) != 0x0) {
            codedOutputByteBufferNano.writeInt32(1, this.targetServiceApiVersion_);
        }
        super.writeTo(codedOutputByteBufferNano);
    }
}
