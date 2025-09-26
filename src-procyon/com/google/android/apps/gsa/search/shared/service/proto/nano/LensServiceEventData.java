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

public final class LensServiceEventData extends ExtendableMessageNano<LensServiceEventData> implements Cloneable
{
    private static volatile LensServiceEventData[] _emptyArray;
    private int bitField0_;
    private int serviceApiVersion_;
    
    public LensServiceEventData() {
        this.clear();
    }
    
    public static LensServiceEventData[] emptyArray() {
        if (LensServiceEventData._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (LensServiceEventData._emptyArray == null) {
                    LensServiceEventData._emptyArray = new LensServiceEventData[0];
                }
            }
        }
        return LensServiceEventData._emptyArray;
    }
    
    public static LensServiceEventData parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new LensServiceEventData().mergeFrom(codedInputByteBufferNano);
    }
    
    public static LensServiceEventData parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
        return MessageNano.mergeFrom(new LensServiceEventData(), array);
    }
    
    public LensServiceEventData clear() {
        this.bitField0_ = 0;
        this.serviceApiVersion_ = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }
    
    public LensServiceEventData clearServiceApiVersion() {
        this.serviceApiVersion_ = 0;
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
    }
    
    @Override
    public LensServiceEventData clone() {
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
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.serviceApiVersion_);
        }
        return computeSerializedSize;
    }
    
    public int getServiceApiVersion() {
        return this.serviceApiVersion_;
    }
    
    public boolean hasServiceApiVersion() {
        final int bitField0_ = this.bitField0_;
        boolean b = true;
        if ((bitField0_ & 0x1) == 0x0) {
            b = false;
        }
        return b;
    }
    
    @Override
    public LensServiceEventData mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
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
                this.serviceApiVersion_ = codedInputByteBufferNano.readInt32();
                this.bitField0_ |= 0x1;
            }
        }
    }
    
    public LensServiceEventData setServiceApiVersion(final int serviceApiVersion_) {
        this.bitField0_ |= 0x1;
        this.serviceApiVersion_ = serviceApiVersion_;
        return this;
    }
    
    @Override
    public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if ((this.bitField0_ & 0x1) != 0x0) {
            codedOutputByteBufferNano.writeInt32(1, this.serviceApiVersion_);
        }
        super.writeTo(codedOutputByteBufferNano);
    }
}
