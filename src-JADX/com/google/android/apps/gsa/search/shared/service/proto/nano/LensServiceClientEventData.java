package com.google.android.apps.gsa.search.shared.service.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.ExtendableMessageNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class LensServiceClientEventData extends ExtendableMessageNano<LensServiceClientEventData> implements Cloneable {
    private static volatile LensServiceClientEventData[] _emptyArray;
    private int bitField0_;
    private int targetServiceApiVersion_;

    public static LensServiceClientEventData[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new LensServiceClientEventData[0];
                }
            }
        }
        return _emptyArray;
    }

    public int getTargetServiceApiVersion() {
        return this.targetServiceApiVersion_;
    }

    public boolean hasTargetServiceApiVersion() {
        return (this.bitField0_ & 1) != 0;
    }

    public LensServiceClientEventData clearTargetServiceApiVersion() {
        this.targetServiceApiVersion_ = 0;
        this.bitField0_ &= -2;
        return this;
    }

    public LensServiceClientEventData setTargetServiceApiVersion(int value) {
        this.bitField0_ |= 1;
        this.targetServiceApiVersion_ = value;
        return this;
    }

    public LensServiceClientEventData() {
        clear();
    }

    public LensServiceClientEventData clear() {
        this.bitField0_ = 0;
        this.targetServiceApiVersion_ = 0;
        this.unknownFieldData = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
    /* renamed from: clone */
    public LensServiceClientEventData mo6clone() {
        try {
            return (LensServiceClientEventData) super.mo6clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano output) throws IOException {
        if ((this.bitField0_ & 1) != 0) {
            output.writeInt32(1, this.targetServiceApiVersion_);
        }
        super.writeTo(output);
    }

    @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        int iComputeSerializedSize = super.computeSerializedSize();
        return (this.bitField0_ & 1) != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(1, this.targetServiceApiVersion_) : iComputeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public LensServiceClientEventData mergeFrom(CodedInputByteBufferNano input) throws IOException {
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
                this.targetServiceApiVersion_ = input.readInt32();
                this.bitField0_ |= 1;
            }
        }
    }

    public static LensServiceClientEventData parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
        return (LensServiceClientEventData) MessageNano.mergeFrom(new LensServiceClientEventData(), data);
    }

    public static LensServiceClientEventData parseFrom(CodedInputByteBufferNano input) throws IOException {
        return new LensServiceClientEventData().mergeFrom(input);
    }
}
