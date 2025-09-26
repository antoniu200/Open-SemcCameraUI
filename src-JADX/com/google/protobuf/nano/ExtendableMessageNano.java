package com.google.protobuf.nano;

import com.google.protobuf.nano.ExtendableMessageNano;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano {
    protected FieldArray unknownFieldData;

    @Override // com.google.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        if (this.unknownFieldData == null) {
            return 0;
        }
        int iComputeSerializedSize = 0;
        for (int i = 0; i < this.unknownFieldData.size(); i++) {
            iComputeSerializedSize += this.unknownFieldData.dataAt(i).computeSerializedSize();
        }
        return iComputeSerializedSize;
    }

    protected int computeSerializedSizeAsMessageSet() {
        if (this.unknownFieldData == null) {
            return 0;
        }
        int iComputeSerializedSizeAsMessageSet = 0;
        for (int i = 0; i < this.unknownFieldData.size(); i++) {
            iComputeSerializedSizeAsMessageSet += this.unknownFieldData.dataAt(i).computeSerializedSizeAsMessageSet();
        }
        return iComputeSerializedSizeAsMessageSet;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano output) throws IOException {
        if (this.unknownFieldData == null) {
            return;
        }
        for (int i = 0; i < this.unknownFieldData.size(); i++) {
            this.unknownFieldData.dataAt(i).writeTo(output);
        }
    }

    protected void writeAsMessageSetTo(CodedOutputByteBufferNano output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.unknownFieldData == null) {
            return;
        }
        for (int i = 0; i < this.unknownFieldData.size(); i++) {
            this.unknownFieldData.dataAt(i).writeAsMessageSetTo(output);
        }
    }

    public final boolean hasExtension(Extension<M, ?> extension) {
        return (this.unknownFieldData == null || this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag)) == null) ? false : true;
    }

    public final <T> T getExtension(Extension<M, T> extension) {
        FieldData fieldData;
        if (this.unknownFieldData == null || (fieldData = this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag))) == null) {
            return null;
        }
        return (T) fieldData.getValue(extension);
    }

    public final <T> M setExtension(Extension<M, T> extension, T value) {
        int tagFieldNumber = WireFormatNano.getTagFieldNumber(extension.tag);
        FieldData fieldData = null;
        if (value == null) {
            if (this.unknownFieldData != null) {
                this.unknownFieldData.remove(tagFieldNumber);
                if (this.unknownFieldData.isEmpty()) {
                    this.unknownFieldData = null;
                }
            }
        } else {
            if (this.unknownFieldData == null) {
                this.unknownFieldData = new FieldArray();
            } else {
                fieldData = this.unknownFieldData.get(tagFieldNumber);
            }
            if (fieldData == null) {
                this.unknownFieldData.put(tagFieldNumber, new FieldData(extension, value));
            } else {
                fieldData.setValue(extension, value);
            }
        }
        return this;
    }

    protected final boolean storeUnknownField(CodedInputByteBufferNano input, int tag) throws IOException {
        int position = input.getPosition();
        if (!input.skipField(tag)) {
            return false;
        }
        storeUnknownFieldData(WireFormatNano.getTagFieldNumber(tag), new UnknownFieldData(tag, input.getData(position, input.getPosition() - position)));
        return true;
    }

    private void storeUnknownFieldData(int fieldNumber, UnknownFieldData unknownField) throws IOException {
        FieldData fieldData;
        if (this.unknownFieldData == null) {
            this.unknownFieldData = new FieldArray();
            fieldData = null;
        } else {
            fieldData = this.unknownFieldData.get(fieldNumber);
        }
        if (fieldData == null) {
            fieldData = new FieldData();
            this.unknownFieldData.put(fieldNumber, fieldData);
        }
        fieldData.addUnknownField(unknownField);
    }

    protected final boolean storeUnknownFieldAsMessageSet(CodedInputByteBufferNano input, int maybeMessageSetItemTag) throws IOException {
        if (maybeMessageSetItemTag != WireFormatNano.MESSAGE_SET_ITEM_TAG) {
            return storeUnknownField(input, maybeMessageSetItemTag);
        }
        int uInt32 = 0;
        byte[] data = null;
        while (true) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == WireFormatNano.MESSAGE_SET_TYPE_ID_TAG) {
                uInt32 = input.readUInt32();
            } else if (tag == WireFormatNano.MESSAGE_SET_MESSAGE_TAG) {
                int position = input.getPosition();
                input.skipField(tag);
                data = input.getData(position, input.getPosition() - position);
            } else if (!input.skipField(tag)) {
                break;
            }
        }
        input.checkLastTagWas(WireFormatNano.MESSAGE_SET_ITEM_END_TAG);
        if (data == null || uInt32 == 0) {
            return true;
        }
        storeUnknownFieldData(uInt32, new UnknownFieldData(uInt32, data));
        return true;
    }

    @Override // com.google.protobuf.nano.MessageNano
    /* renamed from: clone */
    public M mo6clone() throws CloneNotSupportedException {
        M m = (M) super.mo6clone();
        InternalNano.cloneUnknownFieldData(this, m);
        return m;
    }

    public final FieldArray getUnknownFieldArray() {
        return this.unknownFieldData;
    }
}
