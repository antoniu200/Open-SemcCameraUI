package com.google.protobuf.nano;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class FieldData implements Cloneable {
    private Extension<?, ?> cachedExtension;
    private List<UnknownFieldData> unknownFieldData;
    private Object value;

    /* JADX WARN: Multi-variable type inference failed */
    <T> FieldData(Extension<?, T> extension, T newValue) {
        this.cachedExtension = extension;
        this.value = newValue;
    }

    FieldData() {
        this.unknownFieldData = new ArrayList();
    }

    void addUnknownField(UnknownFieldData unknownField) throws IOException {
        Object valueFrom;
        if (this.unknownFieldData != null) {
            this.unknownFieldData.add(unknownField);
            return;
        }
        if (this.value instanceof MessageNano) {
            byte[] bArr = unknownField.bytes;
            CodedInputByteBufferNano codedInputByteBufferNanoNewInstance = CodedInputByteBufferNano.newInstance(bArr, 0, bArr.length);
            int int32 = codedInputByteBufferNanoNewInstance.readInt32();
            if (int32 != bArr.length - CodedOutputByteBufferNano.computeInt32SizeNoTag(int32)) {
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            }
            valueFrom = ((MessageNano) this.value).mergeFrom(codedInputByteBufferNanoNewInstance);
        } else if (this.value instanceof MessageNano[]) {
            MessageNano[] messageNanoArr = (MessageNano[]) this.cachedExtension.getValueFrom(Collections.singletonList(unknownField));
            MessageNano[] messageNanoArr2 = (MessageNano[]) this.value;
            Object obj = (MessageNano[]) Arrays.copyOf(messageNanoArr2, messageNanoArr2.length + messageNanoArr.length);
            System.arraycopy(messageNanoArr, 0, obj, messageNanoArr2.length, messageNanoArr.length);
            valueFrom = obj;
        } else {
            valueFrom = this.cachedExtension.getValueFrom(Collections.singletonList(unknownField));
        }
        setValue(this.cachedExtension, valueFrom);
    }

    UnknownFieldData getUnknownField(int index) {
        if (this.unknownFieldData != null && index < this.unknownFieldData.size()) {
            return this.unknownFieldData.get(index);
        }
        return null;
    }

    int getUnknownFieldSize() {
        if (this.unknownFieldData == null) {
            return 0;
        }
        return this.unknownFieldData.size();
    }

    /* JADX WARN: Multi-variable type inference failed */
    <T> T getValue(Extension<?, T> extension) {
        if (this.value != null) {
            if (!this.cachedExtension.equals(extension)) {
                throw new IllegalStateException("Tried to getExtension with a different Extension.");
            }
        } else {
            this.cachedExtension = extension;
            this.value = extension.getValueFrom(this.unknownFieldData);
            this.unknownFieldData = null;
        }
        return (T) this.value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    <T> void setValue(Extension<?, T> extension, T newValue) {
        this.cachedExtension = extension;
        this.value = newValue;
        this.unknownFieldData = null;
    }

    int computeSerializedSize() {
        if (this.value != null) {
            return this.cachedExtension.computeSerializedSize(this.value);
        }
        Iterator<UnknownFieldData> it = this.unknownFieldData.iterator();
        int iComputeSerializedSize = 0;
        while (it.hasNext()) {
            iComputeSerializedSize += it.next().computeSerializedSize();
        }
        return iComputeSerializedSize;
    }

    int computeSerializedSizeAsMessageSet() {
        if (this.value != null) {
            return this.cachedExtension.computeSerializedSizeAsMessageSet(this.value);
        }
        Iterator<UnknownFieldData> it = this.unknownFieldData.iterator();
        int iComputeSerializedSizeAsMessageSet = 0;
        while (it.hasNext()) {
            iComputeSerializedSizeAsMessageSet += it.next().computeSerializedSizeAsMessageSet();
        }
        return iComputeSerializedSizeAsMessageSet;
    }

    void writeTo(CodedOutputByteBufferNano output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.value != null) {
            this.cachedExtension.writeTo(this.value, output);
            return;
        }
        Iterator<UnknownFieldData> it = this.unknownFieldData.iterator();
        while (it.hasNext()) {
            it.next().writeTo(output);
        }
    }

    void writeAsMessageSetTo(CodedOutputByteBufferNano output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.value != null) {
            this.cachedExtension.writeAsMessageSetTo(this.value, output);
            return;
        }
        Iterator<UnknownFieldData> it = this.unknownFieldData.iterator();
        while (it.hasNext()) {
            it.next().writeAsMessageSetTo(output);
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldData)) {
            return false;
        }
        FieldData fieldData = (FieldData) o;
        if (this.value != null && fieldData.value != null) {
            if (this.cachedExtension != fieldData.cachedExtension) {
                return false;
            }
            if (!this.cachedExtension.clazz.isArray()) {
                return this.value.equals(fieldData.value);
            }
            if (this.value instanceof byte[]) {
                return Arrays.equals((byte[]) this.value, (byte[]) fieldData.value);
            }
            if (this.value instanceof int[]) {
                return Arrays.equals((int[]) this.value, (int[]) fieldData.value);
            }
            if (this.value instanceof long[]) {
                return Arrays.equals((long[]) this.value, (long[]) fieldData.value);
            }
            if (this.value instanceof float[]) {
                return Arrays.equals((float[]) this.value, (float[]) fieldData.value);
            }
            if (this.value instanceof double[]) {
                return Arrays.equals((double[]) this.value, (double[]) fieldData.value);
            }
            if (this.value instanceof boolean[]) {
                return Arrays.equals((boolean[]) this.value, (boolean[]) fieldData.value);
            }
            return Arrays.deepEquals((Object[]) this.value, (Object[]) fieldData.value);
        }
        if (this.unknownFieldData != null && fieldData.unknownFieldData != null) {
            return this.unknownFieldData.equals(fieldData.unknownFieldData);
        }
        try {
            return Arrays.equals(toByteArray(), fieldData.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public int hashCode() {
        try {
            return 527 + Arrays.hashCode(toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] toByteArray() throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        byte[] bArr = new byte[computeSerializedSize()];
        writeTo(CodedOutputByteBufferNano.newInstance(bArr));
        return bArr;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public final FieldData m10clone() {
        FieldData fieldData = new FieldData();
        try {
            fieldData.cachedExtension = this.cachedExtension;
            if (this.unknownFieldData == null) {
                fieldData.unknownFieldData = null;
            } else {
                fieldData.unknownFieldData.addAll(this.unknownFieldData);
            }
            if (this.value != null) {
                if (this.value instanceof MessageNano) {
                    fieldData.value = ((MessageNano) this.value).mo6clone();
                } else if (this.value instanceof byte[]) {
                    fieldData.value = ((byte[]) this.value).clone();
                } else {
                    int i = 0;
                    if (this.value instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.value;
                        byte[][] bArr2 = new byte[bArr.length][];
                        fieldData.value = bArr2;
                        while (i < bArr.length) {
                            bArr2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (this.value instanceof boolean[]) {
                        fieldData.value = ((boolean[]) this.value).clone();
                    } else if (this.value instanceof int[]) {
                        fieldData.value = ((int[]) this.value).clone();
                    } else if (this.value instanceof long[]) {
                        fieldData.value = ((long[]) this.value).clone();
                    } else if (this.value instanceof float[]) {
                        fieldData.value = ((float[]) this.value).clone();
                    } else if (this.value instanceof double[]) {
                        fieldData.value = ((double[]) this.value).clone();
                    } else if (this.value instanceof MessageNano[]) {
                        MessageNano[] messageNanoArr = (MessageNano[]) this.value;
                        MessageNano[] messageNanoArr2 = new MessageNano[messageNanoArr.length];
                        fieldData.value = messageNanoArr2;
                        while (i < messageNanoArr.length) {
                            messageNanoArr2[i] = messageNanoArr[i].mo6clone();
                            i++;
                        }
                    }
                }
            }
            return fieldData;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
