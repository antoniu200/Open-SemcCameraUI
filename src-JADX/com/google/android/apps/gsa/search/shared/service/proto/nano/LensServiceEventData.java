package com.google.android.apps.gsa.search.shared.service.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.ExtendableMessageNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class LensServiceEventData extends ExtendableMessageNano<LensServiceEventData> implements Cloneable {
    private static volatile LensServiceEventData[] _emptyArray;
    private int bitField0_;
    private int serviceApiVersion_;

    public static LensServiceEventData[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new LensServiceEventData[0];
                }
            }
        }
        return _emptyArray;
    }

    public int getServiceApiVersion() {
        return this.serviceApiVersion_;
    }

    public boolean hasServiceApiVersion() {
        return (this.bitField0_ & 1) != 0;
    }

    public LensServiceEventData clearServiceApiVersion() {
        this.serviceApiVersion_ = 0;
        this.bitField0_ &= -2;
        return this;
    }

    public LensServiceEventData setServiceApiVersion(int value) {
        this.bitField0_ |= 1;
        this.serviceApiVersion_ = value;
        return this;
    }

    public LensServiceEventData() {
        clear();
    }

    public LensServiceEventData clear() {
        this.bitField0_ = 0;
        this.serviceApiVersion_ = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
    /* renamed from: clone */
    public LensServiceEventData mo6clone() {
        try {
            return (LensServiceEventData) super.mo6clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano output) throws IOException {
        if ((this.bitField0_ & 1) != 0) {
            output.writeInt32(1, this.serviceApiVersion_);
        }
        super.writeTo(output);
    }

    @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        return (this.bitField0_ & 1) != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(1, this.serviceApiVersion_) : iComputeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public LensServiceEventData mergeFrom(CodedInputByteBufferNano input) throws IOException {
        while (true) {
            int tag = input.readTag();
            if (tag == 0) {
                return this;
            }
            if (tag != 8) {
                if (!super.storeUnknownField(input, tag)) {
                    return this;
                }
            } else {
                this.serviceApiVersion_ = input.readInt32();
                this.bitField0_ |= 1;
            }
        }
    }

    public static LensServiceEventData parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
        return (LensServiceEventData) MessageNano.mergeFrom(new LensServiceEventData(), data);
    }

    public static LensServiceEventData parseFrom(CodedInputByteBufferNano input) throws IOException {
        return new LensServiceEventData().mergeFrom(input);
    }
}
