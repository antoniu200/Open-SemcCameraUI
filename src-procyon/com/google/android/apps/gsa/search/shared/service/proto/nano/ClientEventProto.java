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

public final class ClientEventProto extends ExtendableMessageNano<ClientEventProto> implements Cloneable
{
    private static volatile ClientEventProto[] _emptyArray;
    private int bitField0_;
    private int eventId_;
    
    public ClientEventProto() {
        this.clear();
    }
    
    public static ClientEventProto[] emptyArray() {
        if (ClientEventProto._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (ClientEventProto._emptyArray == null) {
                    ClientEventProto._emptyArray = new ClientEventProto[0];
                }
            }
        }
        return ClientEventProto._emptyArray;
    }
    
    public static ClientEventProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new ClientEventProto().mergeFrom(codedInputByteBufferNano);
    }
    
    public static ClientEventProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
        return MessageNano.mergeFrom(new ClientEventProto(), array);
    }
    
    public ClientEventProto clear() {
        this.bitField0_ = 0;
        this.eventId_ = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }
    
    public ClientEventProto clearEventId() {
        this.eventId_ = 0;
        this.bitField0_ &= 0xFFFFFFFE;
        return this;
    }
    
    @Override
    public ClientEventProto clone() {
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
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.eventId_);
        }
        return computeSerializedSize;
    }
    
    public int getEventId() {
        return this.eventId_;
    }
    
    public boolean hasEventId() {
        final int bitField0_ = this.bitField0_;
        boolean b = true;
        if ((bitField0_ & 0x1) == 0x0) {
            b = false;
        }
        return b;
    }
    
    @Override
    public ClientEventProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
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
                this.eventId_ = codedInputByteBufferNano.readInt32();
                this.bitField0_ |= 0x1;
            }
        }
    }
    
    public ClientEventProto setEventId(final int eventId_) {
        this.bitField0_ |= 0x1;
        this.eventId_ = eventId_;
        return this;
    }
    
    @Override
    public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if ((this.bitField0_ & 0x1) != 0x0) {
            codedOutputByteBufferNano.writeInt32(1, this.eventId_);
        }
        super.writeTo(codedOutputByteBufferNano);
    }
}
