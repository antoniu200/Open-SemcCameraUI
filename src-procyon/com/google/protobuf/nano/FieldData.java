// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.Collections;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FieldData implements Cloneable
{
    private Extension<?, ?> cachedExtension;
    private List<UnknownFieldData> unknownFieldData;
    private Object value;
    
    FieldData() {
        this.unknownFieldData = new ArrayList<UnknownFieldData>();
    }
    
     <T> FieldData(final Extension<?, T> cachedExtension, final T value) {
        this.cachedExtension = cachedExtension;
        this.value = value;
    }
    
    private byte[] toByteArray() throws IOException {
        final byte[] array = new byte[this.computeSerializedSize()];
        this.writeTo(CodedOutputByteBufferNano.newInstance(array));
        return array;
    }
    
    void addUnknownField(final UnknownFieldData unknownFieldData) throws IOException {
        if (this.unknownFieldData != null) {
            this.unknownFieldData.add(unknownFieldData);
        }
        else {
            Object o;
            if (this.value instanceof MessageNano) {
                final byte[] bytes = unknownFieldData.bytes;
                final CodedInputByteBufferNano instance = CodedInputByteBufferNano.newInstance(bytes, 0, bytes.length);
                final int int32 = instance.readInt32();
                if (int32 != bytes.length - CodedOutputByteBufferNano.computeInt32SizeNoTag(int32)) {
                    throw InvalidProtocolBufferNanoException.truncatedMessage();
                }
                o = ((MessageNano)this.value).mergeFrom(instance);
            }
            else if (this.value instanceof MessageNano[]) {
                final MessageNano[] array = (Object)this.cachedExtension.getValueFrom(Collections.singletonList(unknownFieldData));
                final MessageNano[] original = (MessageNano[])this.value;
                o = Arrays.copyOf(original, original.length + array.length);
                System.arraycopy(array, 0, o, original.length, array.length);
            }
            else {
                o = this.cachedExtension.getValueFrom(Collections.singletonList(unknownFieldData));
            }
            this.setValue(this.cachedExtension, o);
        }
    }
    
    public final FieldData clone() {
        final FieldData fieldData = new FieldData();
        try {
            fieldData.cachedExtension = this.cachedExtension;
            if (this.unknownFieldData == null) {
                fieldData.unknownFieldData = null;
            }
            else {
                fieldData.unknownFieldData.addAll(this.unknownFieldData);
            }
            if (this.value != null) {
                if (this.value instanceof MessageNano) {
                    fieldData.value = ((MessageNano)this.value).clone();
                }
                else if (this.value instanceof byte[]) {
                    fieldData.value = ((byte[])this.value).clone();
                }
                else {
                    final boolean b = this.value instanceof byte[][];
                    int i = 0;
                    final int n = 0;
                    if (b) {
                        final byte[][] array = (byte[][])this.value;
                        final byte[][] value = new byte[array.length][];
                        fieldData.value = value;
                        for (int j = n; j < array.length; ++j) {
                            value[j] = array[j].clone();
                        }
                    }
                    else if (this.value instanceof boolean[]) {
                        fieldData.value = ((boolean[])this.value).clone();
                    }
                    else if (this.value instanceof int[]) {
                        fieldData.value = ((int[])this.value).clone();
                    }
                    else if (this.value instanceof long[]) {
                        fieldData.value = ((long[])this.value).clone();
                    }
                    else if (this.value instanceof float[]) {
                        fieldData.value = ((float[])this.value).clone();
                    }
                    else if (this.value instanceof double[]) {
                        fieldData.value = ((double[])this.value).clone();
                    }
                    else if (this.value instanceof MessageNano[]) {
                        final MessageNano[] array2 = (MessageNano[])this.value;
                        final MessageNano[] value2 = new MessageNano[array2.length];
                        fieldData.value = value2;
                        while (i < array2.length) {
                            value2[i] = array2[i].clone();
                            ++i;
                        }
                    }
                }
            }
            return fieldData;
        }
        catch (final CloneNotSupportedException detailMessage) {
            throw new AssertionError((Object)detailMessage);
        }
    }
    
    int computeSerializedSize() {
        int computeSerializedSize;
        if (this.value != null) {
            computeSerializedSize = this.cachedExtension.computeSerializedSize(this.value);
        }
        else {
            final Iterator<UnknownFieldData> iterator = this.unknownFieldData.iterator();
            computeSerializedSize = 0;
            while (iterator.hasNext()) {
                computeSerializedSize += iterator.next().computeSerializedSize();
            }
        }
        return computeSerializedSize;
    }
    
    int computeSerializedSizeAsMessageSet() {
        int computeSerializedSizeAsMessageSet;
        if (this.value != null) {
            computeSerializedSizeAsMessageSet = this.cachedExtension.computeSerializedSizeAsMessageSet(this.value);
        }
        else {
            final Iterator<UnknownFieldData> iterator = this.unknownFieldData.iterator();
            computeSerializedSizeAsMessageSet = 0;
            while (iterator.hasNext()) {
                computeSerializedSizeAsMessageSet += iterator.next().computeSerializedSizeAsMessageSet();
            }
        }
        return computeSerializedSizeAsMessageSet;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldData)) {
            return false;
        }
        final FieldData fieldData = (FieldData)o;
        if (this.value != null && fieldData.value != null) {
            if (this.cachedExtension != fieldData.cachedExtension) {
                return false;
            }
            if (!this.cachedExtension.clazz.isArray()) {
                return this.value.equals(fieldData.value);
            }
            if (this.value instanceof byte[]) {
                return Arrays.equals((byte[])this.value, (byte[])fieldData.value);
            }
            if (this.value instanceof int[]) {
                return Arrays.equals((int[])this.value, (int[])fieldData.value);
            }
            if (this.value instanceof long[]) {
                return Arrays.equals((long[])this.value, (long[])fieldData.value);
            }
            if (this.value instanceof float[]) {
                return Arrays.equals((float[])this.value, (float[])fieldData.value);
            }
            if (this.value instanceof double[]) {
                return Arrays.equals((double[])this.value, (double[])fieldData.value);
            }
            if (this.value instanceof boolean[]) {
                return Arrays.equals((boolean[])this.value, (boolean[])fieldData.value);
            }
            return Arrays.deepEquals((Object[])this.value, (Object[])fieldData.value);
        }
        else {
            if (this.unknownFieldData != null && fieldData.unknownFieldData != null) {
                return this.unknownFieldData.equals(fieldData.unknownFieldData);
            }
            try {
                return Arrays.equals(this.toByteArray(), fieldData.toByteArray());
            }
            catch (final IOException cause) {
                throw new IllegalStateException(cause);
            }
        }
    }
    
    UnknownFieldData getUnknownField(final int n) {
        if (this.unknownFieldData == null) {
            return null;
        }
        if (n < this.unknownFieldData.size()) {
            return this.unknownFieldData.get(n);
        }
        return null;
    }
    
    int getUnknownFieldSize() {
        if (this.unknownFieldData == null) {
            return 0;
        }
        return this.unknownFieldData.size();
    }
    
     <T> T getValue(final Extension<?, T> cachedExtension) {
        if (this.value != null) {
            if (!this.cachedExtension.equals(cachedExtension)) {
                throw new IllegalStateException("Tried to getExtension with a different Extension.");
            }
        }
        else {
            this.cachedExtension = cachedExtension;
            this.value = cachedExtension.getValueFrom(this.unknownFieldData);
            this.unknownFieldData = null;
        }
        return (T)this.value;
    }
    
    @Override
    public int hashCode() {
        try {
            return 527 + Arrays.hashCode(this.toByteArray());
        }
        catch (final IOException cause) {
            throw new IllegalStateException(cause);
        }
    }
    
     <T> void setValue(final Extension<?, T> cachedExtension, final T value) {
        this.cachedExtension = cachedExtension;
        this.value = value;
        this.unknownFieldData = null;
    }
    
    void writeAsMessageSetTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.value != null) {
            this.cachedExtension.writeAsMessageSetTo(this.value, codedOutputByteBufferNano);
        }
        else {
            final Iterator<UnknownFieldData> iterator = this.unknownFieldData.iterator();
            while (iterator.hasNext()) {
                iterator.next().writeAsMessageSetTo(codedOutputByteBufferNano);
            }
        }
    }
    
    void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.value != null) {
            this.cachedExtension.writeTo(this.value, codedOutputByteBufferNano);
        }
        else {
            final Iterator<UnknownFieldData> iterator = this.unknownFieldData.iterator();
            while (iterator.hasNext()) {
                iterator.next().writeTo(codedOutputByteBufferNano);
            }
        }
    }
}
