// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.io.IOException;

public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano
{
    protected FieldArray unknownFieldData;
    
    private void storeUnknownFieldData(final int n, final UnknownFieldData unknownFieldData) throws IOException {
        FieldData value;
        if (this.unknownFieldData == null) {
            this.unknownFieldData = new FieldArray();
            value = null;
        }
        else {
            value = this.unknownFieldData.get(n);
        }
        FieldData fieldData = value;
        if (value == null) {
            fieldData = new FieldData();
            this.unknownFieldData.put(n, fieldData);
        }
        fieldData.addUnknownField(unknownFieldData);
    }
    
    @Override
    public M clone() throws CloneNotSupportedException {
        final ExtendableMessageNano extendableMessageNano = (ExtendableMessageNano)super.clone();
        InternalNano.cloneUnknownFieldData(this, extendableMessageNano);
        return (M)extendableMessageNano;
    }
    
    @Override
    protected int computeSerializedSize() {
        final FieldArray unknownFieldData = this.unknownFieldData;
        int n = 0;
        int n3;
        if (unknownFieldData != null) {
            int n2 = 0;
            while (true) {
                n3 = n2;
                if (n >= this.unknownFieldData.size()) {
                    break;
                }
                n2 += this.unknownFieldData.dataAt(n).computeSerializedSize();
                ++n;
            }
        }
        else {
            n3 = 0;
        }
        return n3;
    }
    
    protected int computeSerializedSizeAsMessageSet() {
        final FieldArray unknownFieldData = this.unknownFieldData;
        int n = 0;
        int n3;
        if (unknownFieldData != null) {
            int n2 = 0;
            while (true) {
                n3 = n2;
                if (n >= this.unknownFieldData.size()) {
                    break;
                }
                n2 += this.unknownFieldData.dataAt(n).computeSerializedSizeAsMessageSet();
                ++n;
            }
        }
        else {
            n3 = 0;
        }
        return n3;
    }
    
    public final <T> T getExtension(final Extension<M, T> extension) {
        final FieldArray unknownFieldData = this.unknownFieldData;
        final T t = null;
        if (unknownFieldData == null) {
            return null;
        }
        final FieldData value = this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag));
        T value2;
        if (value == null) {
            value2 = t;
        }
        else {
            value2 = value.getValue(extension);
        }
        return value2;
    }
    
    public final FieldArray getUnknownFieldArray() {
        return this.unknownFieldData;
    }
    
    public final boolean hasExtension(final Extension<M, ?> extension) {
        final FieldArray unknownFieldData = this.unknownFieldData;
        boolean b = false;
        if (unknownFieldData == null) {
            return false;
        }
        if (this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag)) != null) {
            b = true;
        }
        return b;
    }
    
    public final <T> M setExtension(final Extension<M, T> extension, final T t) {
        final int tagFieldNumber = WireFormatNano.getTagFieldNumber(extension.tag);
        FieldData value = null;
        if (t == null) {
            if (this.unknownFieldData != null) {
                this.unknownFieldData.remove(tagFieldNumber);
                if (this.unknownFieldData.isEmpty()) {
                    this.unknownFieldData = null;
                }
            }
        }
        else {
            if (this.unknownFieldData == null) {
                this.unknownFieldData = new FieldArray();
            }
            else {
                value = this.unknownFieldData.get(tagFieldNumber);
            }
            if (value == null) {
                this.unknownFieldData.put(tagFieldNumber, new FieldData((Extension<?, T>)extension, (T)t));
            }
            else {
                value.setValue(extension, t);
            }
        }
        return (M)this;
    }
    
    protected final boolean storeUnknownField(final CodedInputByteBufferNano codedInputByteBufferNano, final int n) throws IOException {
        final int position = codedInputByteBufferNano.getPosition();
        if (!codedInputByteBufferNano.skipField(n)) {
            return false;
        }
        this.storeUnknownFieldData(WireFormatNano.getTagFieldNumber(n), new UnknownFieldData(n, codedInputByteBufferNano.getData(position, codedInputByteBufferNano.getPosition() - position)));
        return true;
    }
    
    protected final boolean storeUnknownFieldAsMessageSet(final CodedInputByteBufferNano codedInputByteBufferNano, int uInt32) throws IOException {
        if (uInt32 != WireFormatNano.MESSAGE_SET_ITEM_TAG) {
            return this.storeUnknownField(codedInputByteBufferNano, uInt32);
        }
        uInt32 = 0;
        byte[] data = null;
        while (true) {
            final int tag = codedInputByteBufferNano.readTag();
            if (tag == 0) {
                break;
            }
            if (tag == WireFormatNano.MESSAGE_SET_TYPE_ID_TAG) {
                uInt32 = codedInputByteBufferNano.readUInt32();
            }
            else if (tag == WireFormatNano.MESSAGE_SET_MESSAGE_TAG) {
                final int position = codedInputByteBufferNano.getPosition();
                codedInputByteBufferNano.skipField(tag);
                data = codedInputByteBufferNano.getData(position, codedInputByteBufferNano.getPosition() - position);
            }
            else {
                if (!codedInputByteBufferNano.skipField(tag)) {
                    break;
                }
                continue;
            }
        }
        codedInputByteBufferNano.checkLastTagWas(WireFormatNano.MESSAGE_SET_ITEM_END_TAG);
        if (data != null && uInt32 != 0) {
            this.storeUnknownFieldData(uInt32, new UnknownFieldData(uInt32, data));
        }
        return true;
    }
    
    protected void writeAsMessageSetTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.unknownFieldData == null) {
            return;
        }
        for (int i = 0; i < this.unknownFieldData.size(); ++i) {
            this.unknownFieldData.dataAt(i).writeAsMessageSetTo(codedOutputByteBufferNano);
        }
    }
    
    @Override
    public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.unknownFieldData == null) {
            return;
        }
        for (int i = 0; i < this.unknownFieldData.size(); ++i) {
            this.unknownFieldData.dataAt(i).writeTo(codedOutputByteBufferNano);
        }
    }
}
