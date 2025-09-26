// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.util.Arrays;
import java.io.IOException;

public abstract class DescriptorProtos
{
    private DescriptorProtos() {
    }
    
    public static final class DescriptorProto extends ExtendableMessageNano<DescriptorProto>
    {
        private static volatile DescriptorProto[] _emptyArray;
        public EnumDescriptorProto[] enumType;
        public FieldDescriptorProto[] extension;
        public ExtensionRange[] extensionRange;
        public FieldDescriptorProto[] field;
        public String name;
        public DescriptorProto[] nestedType;
        public OneofDescriptorProto[] oneofDecl;
        public MessageOptions options;
        public String[] reservedName;
        public ReservedRange[] reservedRange;
        
        public DescriptorProto() {
            this.clear();
        }
        
        public static DescriptorProto[] emptyArray() {
            if (DescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (DescriptorProto._emptyArray == null) {
                        DescriptorProto._emptyArray = new DescriptorProto[0];
                    }
                }
            }
            return DescriptorProto._emptyArray;
        }
        
        public static DescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new DescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static DescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new DescriptorProto(), array);
        }
        
        public DescriptorProto clear() {
            this.name = "";
            this.field = FieldDescriptorProto.emptyArray();
            this.extension = FieldDescriptorProto.emptyArray();
            this.nestedType = emptyArray();
            this.enumType = EnumDescriptorProto.emptyArray();
            this.extensionRange = ExtensionRange.emptyArray();
            this.oneofDecl = OneofDescriptorProto.emptyArray();
            this.options = null;
            this.reservedRange = ReservedRange.emptyArray();
            this.reservedName = WireFormatNano.EMPTY_STRING_ARRAY;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            final FieldDescriptorProto[] field = this.field;
            final int n2 = 0;
            int n3 = computeSerializedSize;
            if (field != null) {
                n3 = computeSerializedSize;
                if (this.field.length > 0) {
                    n3 = computeSerializedSize;
                    int n4;
                    for (int i = 0; i < this.field.length; ++i, n3 = n4) {
                        final FieldDescriptorProto fieldDescriptorProto = this.field[i];
                        n4 = n3;
                        if (fieldDescriptorProto != null) {
                            n4 = n3 + CodedOutputByteBufferNano.computeMessageSize(2, fieldDescriptorProto);
                        }
                    }
                }
            }
            int n5 = n3;
            if (this.nestedType != null) {
                n5 = n3;
                if (this.nestedType.length > 0) {
                    int n6;
                    for (int j = 0; j < this.nestedType.length; ++j, n3 = n6) {
                        final DescriptorProto descriptorProto = this.nestedType[j];
                        n6 = n3;
                        if (descriptorProto != null) {
                            n6 = n3 + CodedOutputByteBufferNano.computeMessageSize(3, descriptorProto);
                        }
                    }
                    n5 = n3;
                }
            }
            int n7 = n5;
            if (this.enumType != null) {
                n7 = n5;
                if (this.enumType.length > 0) {
                    int n8 = n5;
                    int n9;
                    for (int k = 0; k < this.enumType.length; ++k, n8 = n9) {
                        final EnumDescriptorProto enumDescriptorProto = this.enumType[k];
                        n9 = n8;
                        if (enumDescriptorProto != null) {
                            n9 = n8 + CodedOutputByteBufferNano.computeMessageSize(4, enumDescriptorProto);
                        }
                    }
                    n7 = n8;
                }
            }
            int n10 = n7;
            if (this.extensionRange != null) {
                n10 = n7;
                if (this.extensionRange.length > 0) {
                    n10 = n7;
                    int n11;
                    for (int l = 0; l < this.extensionRange.length; ++l, n10 = n11) {
                        final ExtensionRange extensionRange = this.extensionRange[l];
                        n11 = n10;
                        if (extensionRange != null) {
                            n11 = n10 + CodedOutputByteBufferNano.computeMessageSize(5, extensionRange);
                        }
                    }
                }
            }
            int n12 = n10;
            if (this.extension != null) {
                n12 = n10;
                if (this.extension.length > 0) {
                    int n14;
                    for (int n13 = 0; n13 < this.extension.length; ++n13, n10 = n14) {
                        final FieldDescriptorProto fieldDescriptorProto2 = this.extension[n13];
                        n14 = n10;
                        if (fieldDescriptorProto2 != null) {
                            n14 = n10 + CodedOutputByteBufferNano.computeMessageSize(6, fieldDescriptorProto2);
                        }
                    }
                    n12 = n10;
                }
            }
            int n15 = n12;
            if (this.options != null) {
                n15 = n12 + CodedOutputByteBufferNano.computeMessageSize(7, this.options);
            }
            int n16 = n15;
            if (this.oneofDecl != null) {
                n16 = n15;
                if (this.oneofDecl.length > 0) {
                    int n18;
                    for (int n17 = 0; n17 < this.oneofDecl.length; ++n17, n15 = n18) {
                        final OneofDescriptorProto oneofDescriptorProto = this.oneofDecl[n17];
                        n18 = n15;
                        if (oneofDescriptorProto != null) {
                            n18 = n15 + CodedOutputByteBufferNano.computeMessageSize(8, oneofDescriptorProto);
                        }
                    }
                    n16 = n15;
                }
            }
            int n19 = n16;
            if (this.reservedRange != null) {
                n19 = n16;
                if (this.reservedRange.length > 0) {
                    n19 = n16;
                    int n21;
                    for (int n20 = 0; n20 < this.reservedRange.length; ++n20, n19 = n21) {
                        final ReservedRange reservedRange = this.reservedRange[n20];
                        n21 = n19;
                        if (reservedRange != null) {
                            n21 = n19 + CodedOutputByteBufferNano.computeMessageSize(9, reservedRange);
                        }
                    }
                }
            }
            int n22 = n19;
            if (this.reservedName != null) {
                n22 = n19;
                if (this.reservedName.length > 0) {
                    int n23 = 0;
                    int n24 = 0;
                    int n26;
                    int n27;
                    for (int n25 = n2; n25 < this.reservedName.length; ++n25, n23 = n26, n24 = n27) {
                        final String s = this.reservedName[n25];
                        n26 = n23;
                        n27 = n24;
                        if (s != null) {
                            n27 = n24 + 1;
                            n26 = n23 + CodedOutputByteBufferNano.computeStringSizeNoTag(s);
                        }
                    }
                    n22 = n19 + n23 + 1 * n24;
                }
            }
            return n22;
        }
        
        @Override
        public DescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    default: {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    case 82: {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 82);
                        int length;
                        if (this.reservedName == null) {
                            length = 0;
                        }
                        else {
                            length = this.reservedName.length;
                        }
                        final String[] reservedName = new String[repeatedFieldArrayLength + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.reservedName, 0, reservedName, 0, length);
                            i = length;
                        }
                        while (i < reservedName.length - 1) {
                            reservedName[i] = codedInputByteBufferNano.readString();
                            codedInputByteBufferNano.readTag();
                            ++i;
                        }
                        reservedName[i] = codedInputByteBufferNano.readString();
                        this.reservedName = reservedName;
                        continue;
                    }
                    case 74: {
                        final int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 74);
                        int length2;
                        if (this.reservedRange == null) {
                            length2 = 0;
                        }
                        else {
                            length2 = this.reservedRange.length;
                        }
                        final ReservedRange[] reservedRange = new ReservedRange[repeatedFieldArrayLength2 + length2];
                        int j = length2;
                        if (length2 != 0) {
                            System.arraycopy(this.reservedRange, 0, reservedRange, 0, length2);
                            j = length2;
                        }
                        while (j < reservedRange.length - 1) {
                            codedInputByteBufferNano.readMessage(reservedRange[j] = new ReservedRange());
                            codedInputByteBufferNano.readTag();
                            ++j;
                        }
                        codedInputByteBufferNano.readMessage(reservedRange[j] = new ReservedRange());
                        this.reservedRange = reservedRange;
                        continue;
                    }
                    case 66: {
                        final int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 66);
                        int length3;
                        if (this.oneofDecl == null) {
                            length3 = 0;
                        }
                        else {
                            length3 = this.oneofDecl.length;
                        }
                        final OneofDescriptorProto[] oneofDecl = new OneofDescriptorProto[repeatedFieldArrayLength3 + length3];
                        int k = length3;
                        if (length3 != 0) {
                            System.arraycopy(this.oneofDecl, 0, oneofDecl, 0, length3);
                            k = length3;
                        }
                        while (k < oneofDecl.length - 1) {
                            codedInputByteBufferNano.readMessage(oneofDecl[k] = new OneofDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++k;
                        }
                        codedInputByteBufferNano.readMessage(oneofDecl[k] = new OneofDescriptorProto());
                        this.oneofDecl = oneofDecl;
                        continue;
                    }
                    case 58: {
                        if (this.options == null) {
                            this.options = new MessageOptions();
                        }
                        codedInputByteBufferNano.readMessage(this.options);
                        continue;
                    }
                    case 50: {
                        final int repeatedFieldArrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 50);
                        int length4;
                        if (this.extension == null) {
                            length4 = 0;
                        }
                        else {
                            length4 = this.extension.length;
                        }
                        final FieldDescriptorProto[] extension = new FieldDescriptorProto[repeatedFieldArrayLength4 + length4];
                        int l = length4;
                        if (length4 != 0) {
                            System.arraycopy(this.extension, 0, extension, 0, length4);
                            l = length4;
                        }
                        while (l < extension.length - 1) {
                            codedInputByteBufferNano.readMessage(extension[l] = new FieldDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++l;
                        }
                        codedInputByteBufferNano.readMessage(extension[l] = new FieldDescriptorProto());
                        this.extension = extension;
                        continue;
                    }
                    case 42: {
                        final int repeatedFieldArrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 42);
                        int length5;
                        if (this.extensionRange == null) {
                            length5 = 0;
                        }
                        else {
                            length5 = this.extensionRange.length;
                        }
                        final ExtensionRange[] extensionRange = new ExtensionRange[repeatedFieldArrayLength5 + length5];
                        int n = length5;
                        if (length5 != 0) {
                            System.arraycopy(this.extensionRange, 0, extensionRange, 0, length5);
                            n = length5;
                        }
                        while (n < extensionRange.length - 1) {
                            codedInputByteBufferNano.readMessage(extensionRange[n] = new ExtensionRange());
                            codedInputByteBufferNano.readTag();
                            ++n;
                        }
                        codedInputByteBufferNano.readMessage(extensionRange[n] = new ExtensionRange());
                        this.extensionRange = extensionRange;
                        continue;
                    }
                    case 34: {
                        final int repeatedFieldArrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                        int length6;
                        if (this.enumType == null) {
                            length6 = 0;
                        }
                        else {
                            length6 = this.enumType.length;
                        }
                        final EnumDescriptorProto[] enumType = new EnumDescriptorProto[repeatedFieldArrayLength6 + length6];
                        int n2 = length6;
                        if (length6 != 0) {
                            System.arraycopy(this.enumType, 0, enumType, 0, length6);
                            n2 = length6;
                        }
                        while (n2 < enumType.length - 1) {
                            codedInputByteBufferNano.readMessage(enumType[n2] = new EnumDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++n2;
                        }
                        codedInputByteBufferNano.readMessage(enumType[n2] = new EnumDescriptorProto());
                        this.enumType = enumType;
                        continue;
                    }
                    case 26: {
                        final int repeatedFieldArrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 26);
                        int length7;
                        if (this.nestedType == null) {
                            length7 = 0;
                        }
                        else {
                            length7 = this.nestedType.length;
                        }
                        final DescriptorProto[] nestedType = new DescriptorProto[repeatedFieldArrayLength7 + length7];
                        int n3 = length7;
                        if (length7 != 0) {
                            System.arraycopy(this.nestedType, 0, nestedType, 0, length7);
                            n3 = length7;
                        }
                        while (n3 < nestedType.length - 1) {
                            codedInputByteBufferNano.readMessage(nestedType[n3] = new DescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++n3;
                        }
                        codedInputByteBufferNano.readMessage(nestedType[n3] = new DescriptorProto());
                        this.nestedType = nestedType;
                        continue;
                    }
                    case 18: {
                        final int repeatedFieldArrayLength8 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                        int length8;
                        if (this.field == null) {
                            length8 = 0;
                        }
                        else {
                            length8 = this.field.length;
                        }
                        final FieldDescriptorProto[] field = new FieldDescriptorProto[repeatedFieldArrayLength8 + length8];
                        int n4 = length8;
                        if (length8 != 0) {
                            System.arraycopy(this.field, 0, field, 0, length8);
                            n4 = length8;
                        }
                        while (n4 < field.length - 1) {
                            codedInputByteBufferNano.readMessage(field[n4] = new FieldDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++n4;
                        }
                        codedInputByteBufferNano.readMessage(field[n4] = new FieldDescriptorProto());
                        this.field = field;
                        continue;
                    }
                    case 10: {
                        this.name = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            final FieldDescriptorProto[] field = this.field;
            final int n = 0;
            if (field != null && this.field.length > 0) {
                for (int i = 0; i < this.field.length; ++i) {
                    final FieldDescriptorProto fieldDescriptorProto = this.field[i];
                    if (fieldDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(2, fieldDescriptorProto);
                    }
                }
            }
            if (this.nestedType != null && this.nestedType.length > 0) {
                for (int j = 0; j < this.nestedType.length; ++j) {
                    final DescriptorProto descriptorProto = this.nestedType[j];
                    if (descriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(3, descriptorProto);
                    }
                }
            }
            if (this.enumType != null && this.enumType.length > 0) {
                for (int k = 0; k < this.enumType.length; ++k) {
                    final EnumDescriptorProto enumDescriptorProto = this.enumType[k];
                    if (enumDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(4, enumDescriptorProto);
                    }
                }
            }
            if (this.extensionRange != null && this.extensionRange.length > 0) {
                for (int l = 0; l < this.extensionRange.length; ++l) {
                    final ExtensionRange extensionRange = this.extensionRange[l];
                    if (extensionRange != null) {
                        codedOutputByteBufferNano.writeMessage(5, extensionRange);
                    }
                }
            }
            if (this.extension != null && this.extension.length > 0) {
                for (int n2 = 0; n2 < this.extension.length; ++n2) {
                    final FieldDescriptorProto fieldDescriptorProto2 = this.extension[n2];
                    if (fieldDescriptorProto2 != null) {
                        codedOutputByteBufferNano.writeMessage(6, fieldDescriptorProto2);
                    }
                }
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(7, this.options);
            }
            if (this.oneofDecl != null && this.oneofDecl.length > 0) {
                for (int n3 = 0; n3 < this.oneofDecl.length; ++n3) {
                    final OneofDescriptorProto oneofDescriptorProto = this.oneofDecl[n3];
                    if (oneofDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(8, oneofDescriptorProto);
                    }
                }
            }
            if (this.reservedRange != null && this.reservedRange.length > 0) {
                for (int n4 = 0; n4 < this.reservedRange.length; ++n4) {
                    final ReservedRange reservedRange = this.reservedRange[n4];
                    if (reservedRange != null) {
                        codedOutputByteBufferNano.writeMessage(9, reservedRange);
                    }
                }
            }
            if (this.reservedName != null && this.reservedName.length > 0) {
                for (int n5 = n; n5 < this.reservedName.length; ++n5) {
                    final String s = this.reservedName[n5];
                    if (s != null) {
                        codedOutputByteBufferNano.writeString(10, s);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public static final class ExtensionRange extends ExtendableMessageNano<ExtensionRange>
        {
            private static volatile ExtensionRange[] _emptyArray;
            public int end;
            public ExtensionRangeOptions options;
            public int start;
            
            public ExtensionRange() {
                this.clear();
            }
            
            public static ExtensionRange[] emptyArray() {
                if (ExtensionRange._emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (ExtensionRange._emptyArray == null) {
                            ExtensionRange._emptyArray = new ExtensionRange[0];
                        }
                    }
                }
                return ExtensionRange._emptyArray;
            }
            
            public static ExtensionRange parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                return new ExtensionRange().mergeFrom(codedInputByteBufferNano);
            }
            
            public static ExtensionRange parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
                return MessageNano.mergeFrom(new ExtensionRange(), array);
            }
            
            public ExtensionRange clear() {
                this.start = 0;
                this.end = 0;
                this.options = null;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }
            
            @Override
            protected int computeSerializedSize() {
                int computeSerializedSize;
                final int n = computeSerializedSize = super.computeSerializedSize();
                if (this.start != 0) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeInt32Size(1, this.start);
                }
                int n2 = computeSerializedSize;
                if (this.end != 0) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, this.end);
                }
                int n3 = n2;
                if (this.options != null) {
                    n3 = n2 + CodedOutputByteBufferNano.computeMessageSize(3, this.options);
                }
                return n3;
            }
            
            @Override
            public ExtensionRange mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    final int tag = codedInputByteBufferNano.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag != 8) {
                        if (tag != 16) {
                            if (tag != 26) {
                                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                    return this;
                                }
                                continue;
                            }
                            else {
                                if (this.options == null) {
                                    this.options = new ExtensionRangeOptions();
                                }
                                codedInputByteBufferNano.readMessage(this.options);
                            }
                        }
                        else {
                            this.end = codedInputByteBufferNano.readInt32();
                        }
                    }
                    else {
                        this.start = codedInputByteBufferNano.readInt32();
                    }
                }
            }
            
            @Override
            public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (this.start != 0) {
                    codedOutputByteBufferNano.writeInt32(1, this.start);
                }
                if (this.end != 0) {
                    codedOutputByteBufferNano.writeInt32(2, this.end);
                }
                if (this.options != null) {
                    codedOutputByteBufferNano.writeMessage(3, this.options);
                }
                super.writeTo(codedOutputByteBufferNano);
            }
        }
        
        public static final class ReservedRange extends ExtendableMessageNano<ReservedRange>
        {
            private static volatile ReservedRange[] _emptyArray;
            public int end;
            public int start;
            
            public ReservedRange() {
                this.clear();
            }
            
            public static ReservedRange[] emptyArray() {
                if (ReservedRange._emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (ReservedRange._emptyArray == null) {
                            ReservedRange._emptyArray = new ReservedRange[0];
                        }
                    }
                }
                return ReservedRange._emptyArray;
            }
            
            public static ReservedRange parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                return new ReservedRange().mergeFrom(codedInputByteBufferNano);
            }
            
            public static ReservedRange parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
                return MessageNano.mergeFrom(new ReservedRange(), array);
            }
            
            public ReservedRange clear() {
                this.start = 0;
                this.end = 0;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }
            
            @Override
            protected int computeSerializedSize() {
                int computeSerializedSize;
                final int n = computeSerializedSize = super.computeSerializedSize();
                if (this.start != 0) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeInt32Size(1, this.start);
                }
                int n2 = computeSerializedSize;
                if (this.end != 0) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, this.end);
                }
                return n2;
            }
            
            @Override
            public ReservedRange mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    final int tag = codedInputByteBufferNano.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag != 8) {
                        if (tag != 16) {
                            if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            this.end = codedInputByteBufferNano.readInt32();
                        }
                    }
                    else {
                        this.start = codedInputByteBufferNano.readInt32();
                    }
                }
            }
            
            @Override
            public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (this.start != 0) {
                    codedOutputByteBufferNano.writeInt32(1, this.start);
                }
                if (this.end != 0) {
                    codedOutputByteBufferNano.writeInt32(2, this.end);
                }
                super.writeTo(codedOutputByteBufferNano);
            }
        }
    }
    
    public static final class EnumDescriptorProto extends ExtendableMessageNano<EnumDescriptorProto>
    {
        private static volatile EnumDescriptorProto[] _emptyArray;
        public String name;
        public EnumOptions options;
        public String[] reservedName;
        public EnumReservedRange[] reservedRange;
        public EnumValueDescriptorProto[] value;
        
        public EnumDescriptorProto() {
            this.clear();
        }
        
        public static EnumDescriptorProto[] emptyArray() {
            if (EnumDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (EnumDescriptorProto._emptyArray == null) {
                        EnumDescriptorProto._emptyArray = new EnumDescriptorProto[0];
                    }
                }
            }
            return EnumDescriptorProto._emptyArray;
        }
        
        public static EnumDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new EnumDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static EnumDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new EnumDescriptorProto(), array);
        }
        
        public EnumDescriptorProto clear() {
            this.name = "";
            this.value = EnumValueDescriptorProto.emptyArray();
            this.options = null;
            this.reservedRange = EnumReservedRange.emptyArray();
            this.reservedName = WireFormatNano.EMPTY_STRING_ARRAY;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            final EnumValueDescriptorProto[] value = this.value;
            final int n2 = 0;
            int n3 = computeSerializedSize;
            if (value != null) {
                n3 = computeSerializedSize;
                if (this.value.length > 0) {
                    int n4;
                    for (int i = 0; i < this.value.length; ++i, computeSerializedSize = n4) {
                        final EnumValueDescriptorProto enumValueDescriptorProto = this.value[i];
                        n4 = computeSerializedSize;
                        if (enumValueDescriptorProto != null) {
                            n4 = computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, enumValueDescriptorProto);
                        }
                    }
                    n3 = computeSerializedSize;
                }
            }
            int n5 = n3;
            if (this.options != null) {
                n5 = n3 + CodedOutputByteBufferNano.computeMessageSize(3, this.options);
            }
            int n6 = n5;
            if (this.reservedRange != null) {
                n6 = n5;
                if (this.reservedRange.length > 0) {
                    int n7;
                    for (int j = 0; j < this.reservedRange.length; ++j, n5 = n7) {
                        final EnumReservedRange enumReservedRange = this.reservedRange[j];
                        n7 = n5;
                        if (enumReservedRange != null) {
                            n7 = n5 + CodedOutputByteBufferNano.computeMessageSize(4, enumReservedRange);
                        }
                    }
                    n6 = n5;
                }
            }
            int n8 = n6;
            if (this.reservedName != null) {
                n8 = n6;
                if (this.reservedName.length > 0) {
                    final int n9 = 0;
                    int n10 = 0;
                    int k = n2;
                    int n11 = n9;
                    while (k < this.reservedName.length) {
                        final String s = this.reservedName[k];
                        int n12 = n11;
                        int n13 = n10;
                        if (s != null) {
                            n13 = n10 + 1;
                            n12 = n11 + CodedOutputByteBufferNano.computeStringSizeNoTag(s);
                        }
                        ++k;
                        n11 = n12;
                        n10 = n13;
                    }
                    n8 = n6 + n11 + 1 * n10;
                }
            }
            return n8;
        }
        
        @Override
        public EnumDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (tag != 18) {
                        if (tag != 26) {
                            if (tag != 34) {
                                if (tag != 42) {
                                    if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                        return this;
                                    }
                                    continue;
                                }
                                else {
                                    final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 42);
                                    int length;
                                    if (this.reservedName == null) {
                                        length = 0;
                                    }
                                    else {
                                        length = this.reservedName.length;
                                    }
                                    final String[] reservedName = new String[repeatedFieldArrayLength + length];
                                    int i = length;
                                    if (length != 0) {
                                        System.arraycopy(this.reservedName, 0, reservedName, 0, length);
                                        i = length;
                                    }
                                    while (i < reservedName.length - 1) {
                                        reservedName[i] = codedInputByteBufferNano.readString();
                                        codedInputByteBufferNano.readTag();
                                        ++i;
                                    }
                                    reservedName[i] = codedInputByteBufferNano.readString();
                                    this.reservedName = reservedName;
                                }
                            }
                            else {
                                final int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                                int length2;
                                if (this.reservedRange == null) {
                                    length2 = 0;
                                }
                                else {
                                    length2 = this.reservedRange.length;
                                }
                                final EnumReservedRange[] reservedRange = new EnumReservedRange[repeatedFieldArrayLength2 + length2];
                                int j = length2;
                                if (length2 != 0) {
                                    System.arraycopy(this.reservedRange, 0, reservedRange, 0, length2);
                                    j = length2;
                                }
                                while (j < reservedRange.length - 1) {
                                    codedInputByteBufferNano.readMessage(reservedRange[j] = new EnumReservedRange());
                                    codedInputByteBufferNano.readTag();
                                    ++j;
                                }
                                codedInputByteBufferNano.readMessage(reservedRange[j] = new EnumReservedRange());
                                this.reservedRange = reservedRange;
                            }
                        }
                        else {
                            if (this.options == null) {
                                this.options = new EnumOptions();
                            }
                            codedInputByteBufferNano.readMessage(this.options);
                        }
                    }
                    else {
                        final int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                        int length3;
                        if (this.value == null) {
                            length3 = 0;
                        }
                        else {
                            length3 = this.value.length;
                        }
                        final EnumValueDescriptorProto[] value = new EnumValueDescriptorProto[repeatedFieldArrayLength3 + length3];
                        int k = length3;
                        if (length3 != 0) {
                            System.arraycopy(this.value, 0, value, 0, length3);
                            k = length3;
                        }
                        while (k < value.length - 1) {
                            codedInputByteBufferNano.readMessage(value[k] = new EnumValueDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++k;
                        }
                        codedInputByteBufferNano.readMessage(value[k] = new EnumValueDescriptorProto());
                        this.value = value;
                    }
                }
                else {
                    this.name = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            final EnumValueDescriptorProto[] value = this.value;
            final int n = 0;
            if (value != null && this.value.length > 0) {
                for (int i = 0; i < this.value.length; ++i) {
                    final EnumValueDescriptorProto enumValueDescriptorProto = this.value[i];
                    if (enumValueDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(2, enumValueDescriptorProto);
                    }
                }
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(3, this.options);
            }
            if (this.reservedRange != null && this.reservedRange.length > 0) {
                for (int j = 0; j < this.reservedRange.length; ++j) {
                    final EnumReservedRange enumReservedRange = this.reservedRange[j];
                    if (enumReservedRange != null) {
                        codedOutputByteBufferNano.writeMessage(4, enumReservedRange);
                    }
                }
            }
            if (this.reservedName != null && this.reservedName.length > 0) {
                for (int k = n; k < this.reservedName.length; ++k) {
                    final String s = this.reservedName[k];
                    if (s != null) {
                        codedOutputByteBufferNano.writeString(5, s);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public static final class EnumReservedRange extends ExtendableMessageNano<EnumReservedRange>
        {
            private static volatile EnumReservedRange[] _emptyArray;
            public int end;
            public int start;
            
            public EnumReservedRange() {
                this.clear();
            }
            
            public static EnumReservedRange[] emptyArray() {
                if (EnumReservedRange._emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (EnumReservedRange._emptyArray == null) {
                            EnumReservedRange._emptyArray = new EnumReservedRange[0];
                        }
                    }
                }
                return EnumReservedRange._emptyArray;
            }
            
            public static EnumReservedRange parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                return new EnumReservedRange().mergeFrom(codedInputByteBufferNano);
            }
            
            public static EnumReservedRange parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
                return MessageNano.mergeFrom(new EnumReservedRange(), array);
            }
            
            public EnumReservedRange clear() {
                this.start = 0;
                this.end = 0;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }
            
            @Override
            protected int computeSerializedSize() {
                int computeSerializedSize;
                final int n = computeSerializedSize = super.computeSerializedSize();
                if (this.start != 0) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeInt32Size(1, this.start);
                }
                int n2 = computeSerializedSize;
                if (this.end != 0) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, this.end);
                }
                return n2;
            }
            
            @Override
            public EnumReservedRange mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    final int tag = codedInputByteBufferNano.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag != 8) {
                        if (tag != 16) {
                            if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            this.end = codedInputByteBufferNano.readInt32();
                        }
                    }
                    else {
                        this.start = codedInputByteBufferNano.readInt32();
                    }
                }
            }
            
            @Override
            public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (this.start != 0) {
                    codedOutputByteBufferNano.writeInt32(1, this.start);
                }
                if (this.end != 0) {
                    codedOutputByteBufferNano.writeInt32(2, this.end);
                }
                super.writeTo(codedOutputByteBufferNano);
            }
        }
    }
    
    public static final class EnumOptions extends ExtendableMessageNano<EnumOptions>
    {
        private static volatile EnumOptions[] _emptyArray;
        public boolean allowAlias;
        public boolean deprecated;
        public String proto1Name;
        public UninterpretedOption[] uninterpretedOption;
        
        public EnumOptions() {
            this.clear();
        }
        
        public static EnumOptions[] emptyArray() {
            if (EnumOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (EnumOptions._emptyArray == null) {
                        EnumOptions._emptyArray = new EnumOptions[0];
                    }
                }
            }
            return EnumOptions._emptyArray;
        }
        
        public static EnumOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new EnumOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static EnumOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new EnumOptions(), array);
        }
        
        public EnumOptions clear() {
            this.proto1Name = "";
            this.allowAlias = false;
            this.deprecated = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.proto1Name != null) {
                computeSerializedSize = n;
                if (!this.proto1Name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.proto1Name);
                }
            }
            int n2 = computeSerializedSize;
            if (this.allowAlias) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(2, this.allowAlias);
            }
            int n3 = n2;
            if (this.deprecated) {
                n3 = n2 + CodedOutputByteBufferNano.computeBoolSize(3, this.deprecated);
            }
            int n4 = n3;
            if (this.uninterpretedOption != null) {
                n4 = n3;
                if (this.uninterpretedOption.length > 0) {
                    int n5 = 0;
                    while (true) {
                        n4 = n3;
                        if (n5 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n5];
                        int n6 = n3;
                        if (uninterpretedOption != null) {
                            n6 = n3 + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n5;
                        n3 = n6;
                    }
                }
            }
            return n4;
        }
        
        @Override
        public EnumOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (tag != 16) {
                        if (tag != 24) {
                            if (tag != 7994) {
                                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                    return this;
                                }
                                continue;
                            }
                            else {
                                final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                                int length;
                                if (this.uninterpretedOption == null) {
                                    length = 0;
                                }
                                else {
                                    length = this.uninterpretedOption.length;
                                }
                                final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                                int i = length;
                                if (length != 0) {
                                    System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                                    i = length;
                                }
                                while (i < uninterpretedOption.length - 1) {
                                    codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                                    codedInputByteBufferNano.readTag();
                                    ++i;
                                }
                                codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                                this.uninterpretedOption = uninterpretedOption;
                            }
                        }
                        else {
                            this.deprecated = codedInputByteBufferNano.readBool();
                        }
                    }
                    else {
                        this.allowAlias = codedInputByteBufferNano.readBool();
                    }
                }
                else {
                    this.proto1Name = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.proto1Name != null && !this.proto1Name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.proto1Name);
            }
            if (this.allowAlias) {
                codedOutputByteBufferNano.writeBool(2, this.allowAlias);
            }
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(3, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class EnumValueDescriptorProto extends ExtendableMessageNano<EnumValueDescriptorProto>
    {
        private static volatile EnumValueDescriptorProto[] _emptyArray;
        public String name;
        public int number;
        public EnumValueOptions options;
        
        public EnumValueDescriptorProto() {
            this.clear();
        }
        
        public static EnumValueDescriptorProto[] emptyArray() {
            if (EnumValueDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (EnumValueDescriptorProto._emptyArray == null) {
                        EnumValueDescriptorProto._emptyArray = new EnumValueDescriptorProto[0];
                    }
                }
            }
            return EnumValueDescriptorProto._emptyArray;
        }
        
        public static EnumValueDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new EnumValueDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static EnumValueDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new EnumValueDescriptorProto(), array);
        }
        
        public EnumValueDescriptorProto clear() {
            this.name = "";
            this.number = 0;
            this.options = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            int n2 = computeSerializedSize;
            if (this.number != 0) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, this.number);
            }
            int n3 = n2;
            if (this.options != null) {
                n3 = n2 + CodedOutputByteBufferNano.computeMessageSize(3, this.options);
            }
            return n3;
        }
        
        @Override
        public EnumValueDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (tag != 16) {
                        if (tag != 26) {
                            if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            if (this.options == null) {
                                this.options = new EnumValueOptions();
                            }
                            codedInputByteBufferNano.readMessage(this.options);
                        }
                    }
                    else {
                        this.number = codedInputByteBufferNano.readInt32();
                    }
                }
                else {
                    this.name = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            if (this.number != 0) {
                codedOutputByteBufferNano.writeInt32(2, this.number);
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(3, this.options);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class EnumValueOptions extends ExtendableMessageNano<EnumValueOptions>
    {
        private static volatile EnumValueOptions[] _emptyArray;
        public boolean deprecated;
        public UninterpretedOption[] uninterpretedOption;
        
        public EnumValueOptions() {
            this.clear();
        }
        
        public static EnumValueOptions[] emptyArray() {
            if (EnumValueOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (EnumValueOptions._emptyArray == null) {
                        EnumValueOptions._emptyArray = new EnumValueOptions[0];
                    }
                }
            }
            return EnumValueOptions._emptyArray;
        }
        
        public static EnumValueOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new EnumValueOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static EnumValueOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new EnumValueOptions(), array);
        }
        
        public EnumValueOptions clear() {
            this.deprecated = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.deprecated) {
                computeSerializedSize = n + CodedOutputByteBufferNano.computeBoolSize(1, this.deprecated);
            }
            int n2 = computeSerializedSize;
            if (this.uninterpretedOption != null) {
                n2 = computeSerializedSize;
                if (this.uninterpretedOption.length > 0) {
                    int n3 = 0;
                    while (true) {
                        n2 = computeSerializedSize;
                        if (n3 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n3];
                        int n4 = computeSerializedSize;
                        if (uninterpretedOption != null) {
                            n4 = computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n3;
                        computeSerializedSize = n4;
                    }
                }
            }
            return n2;
        }
        
        @Override
        public EnumValueOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 8) {
                    if (tag != 7994) {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    else {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                        int length;
                        if (this.uninterpretedOption == null) {
                            length = 0;
                        }
                        else {
                            length = this.uninterpretedOption.length;
                        }
                        final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                            i = length;
                        }
                        while (i < uninterpretedOption.length - 1) {
                            codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                            codedInputByteBufferNano.readTag();
                            ++i;
                        }
                        codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                        this.uninterpretedOption = uninterpretedOption;
                    }
                }
                else {
                    this.deprecated = codedInputByteBufferNano.readBool();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(1, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class ExtensionRangeOptions extends ExtendableMessageNano<ExtensionRangeOptions>
    {
        private static volatile ExtensionRangeOptions[] _emptyArray;
        public UninterpretedOption[] uninterpretedOption;
        
        public ExtensionRangeOptions() {
            this.clear();
        }
        
        public static ExtensionRangeOptions[] emptyArray() {
            if (ExtensionRangeOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (ExtensionRangeOptions._emptyArray == null) {
                        ExtensionRangeOptions._emptyArray = new ExtensionRangeOptions[0];
                    }
                }
            }
            return ExtensionRangeOptions._emptyArray;
        }
        
        public static ExtensionRangeOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new ExtensionRangeOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static ExtensionRangeOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new ExtensionRangeOptions(), array);
        }
        
        public ExtensionRangeOptions clear() {
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            int n = computeSerializedSize = super.computeSerializedSize();
            if (this.uninterpretedOption != null) {
                computeSerializedSize = n;
                if (this.uninterpretedOption.length > 0) {
                    int n2 = 0;
                    while (true) {
                        computeSerializedSize = n;
                        if (n2 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n2];
                        int n3 = n;
                        if (uninterpretedOption != null) {
                            n3 = n + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n2;
                        n = n3;
                    }
                }
            }
            return computeSerializedSize;
        }
        
        @Override
        public ExtensionRangeOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 7994) {
                    if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                    continue;
                }
                else {
                    final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                    int length;
                    if (this.uninterpretedOption == null) {
                        length = 0;
                    }
                    else {
                        length = this.uninterpretedOption.length;
                    }
                    final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                    int i = length;
                    if (length != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                        i = length;
                    }
                    while (i < uninterpretedOption.length - 1) {
                        codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                        codedInputByteBufferNano.readTag();
                        ++i;
                    }
                    codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                    this.uninterpretedOption = uninterpretedOption;
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class FieldDescriptorProto extends ExtendableMessageNano<FieldDescriptorProto>
    {
        private static volatile FieldDescriptorProto[] _emptyArray;
        public String defaultValue;
        public String extendee;
        public String jsonName;
        @NanoEnumValue(legacy = false, value = Label.class)
        public int label;
        public String name;
        public int number;
        public int oneofIndex;
        public FieldOptions options;
        @NanoEnumValue(legacy = false, value = Type.class)
        public int type;
        public String typeName;
        
        public FieldDescriptorProto() {
            this.clear();
        }
        
        @NanoEnumValue(legacy = false, value = Label.class)
        public static int checkLabelOrThrow(final int i) {
            if (i >= 1 && i <= 3) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(37);
            sb.append(i);
            sb.append(" is not a valid enum Label");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = Label.class)
        public static int[] checkLabelOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkLabelOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = Type.class)
        public static int checkTypeOrThrow(final int i) {
            if (i >= 1 && i <= 18) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(36);
            sb.append(i);
            sb.append(" is not a valid enum Type");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = Type.class)
        public static int[] checkTypeOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkTypeOrThrow(array[i]);
            }
            return array;
        }
        
        public static FieldDescriptorProto[] emptyArray() {
            if (FieldDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (FieldDescriptorProto._emptyArray == null) {
                        FieldDescriptorProto._emptyArray = new FieldDescriptorProto[0];
                    }
                }
            }
            return FieldDescriptorProto._emptyArray;
        }
        
        public static FieldDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new FieldDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static FieldDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new FieldDescriptorProto(), array);
        }
        
        public FieldDescriptorProto clear() {
            this.name = "";
            this.number = 0;
            this.label = 1;
            this.type = 1;
            this.typeName = "";
            this.extendee = "";
            this.defaultValue = "";
            this.oneofIndex = 0;
            this.jsonName = "";
            this.options = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            int n2 = computeSerializedSize;
            if (this.extendee != null) {
                n2 = computeSerializedSize;
                if (!this.extendee.equals("")) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.extendee);
                }
            }
            int n3 = n2;
            if (this.number != 0) {
                n3 = n2 + CodedOutputByteBufferNano.computeInt32Size(3, this.number);
            }
            int n4 = n3;
            if (this.label != 1) {
                n4 = n3 + CodedOutputByteBufferNano.computeInt32Size(4, this.label);
            }
            int n5 = n4;
            if (this.type != 1) {
                n5 = n4 + CodedOutputByteBufferNano.computeInt32Size(5, this.type);
            }
            int n6 = n5;
            if (this.typeName != null) {
                n6 = n5;
                if (!this.typeName.equals("")) {
                    n6 = n5 + CodedOutputByteBufferNano.computeStringSize(6, this.typeName);
                }
            }
            int n7 = n6;
            if (this.defaultValue != null) {
                n7 = n6;
                if (!this.defaultValue.equals("")) {
                    n7 = n6 + CodedOutputByteBufferNano.computeStringSize(7, this.defaultValue);
                }
            }
            int n8 = n7;
            if (this.options != null) {
                n8 = n7 + CodedOutputByteBufferNano.computeMessageSize(8, this.options);
            }
            int n9 = n8;
            if (this.oneofIndex != 0) {
                n9 = n8 + CodedOutputByteBufferNano.computeInt32Size(9, this.oneofIndex);
            }
            int n10 = n9;
            if (this.jsonName != null) {
                n10 = n9;
                if (!this.jsonName.equals("")) {
                    n10 = n9 + CodedOutputByteBufferNano.computeStringSize(10, this.jsonName);
                }
            }
            return n10;
        }
        
        @Override
        public FieldDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    default: {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    case 82: {
                        this.jsonName = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 72: {
                        this.oneofIndex = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 66: {
                        if (this.options == null) {
                            this.options = new FieldOptions();
                        }
                        codedInputByteBufferNano.readMessage(this.options);
                        continue;
                    }
                    case 58: {
                        this.defaultValue = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 50: {
                        this.typeName = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 40: {
                        final int position = codedInputByteBufferNano.getPosition();
                        try {
                            this.type = checkTypeOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex) {
                            codedInputByteBufferNano.rewindToPosition(position);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 32: {
                        final int position2 = codedInputByteBufferNano.getPosition();
                        try {
                            this.label = checkLabelOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex2) {
                            codedInputByteBufferNano.rewindToPosition(position2);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 24: {
                        this.number = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 18: {
                        this.extendee = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 10: {
                        this.name = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            if (this.extendee != null && !this.extendee.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.extendee);
            }
            if (this.number != 0) {
                codedOutputByteBufferNano.writeInt32(3, this.number);
            }
            if (this.label != 1) {
                codedOutputByteBufferNano.writeInt32(4, this.label);
            }
            if (this.type != 1) {
                codedOutputByteBufferNano.writeInt32(5, this.type);
            }
            if (this.typeName != null && !this.typeName.equals("")) {
                codedOutputByteBufferNano.writeString(6, this.typeName);
            }
            if (this.defaultValue != null && !this.defaultValue.equals("")) {
                codedOutputByteBufferNano.writeString(7, this.defaultValue);
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(8, this.options);
            }
            if (this.oneofIndex != 0) {
                codedOutputByteBufferNano.writeInt32(9, this.oneofIndex);
            }
            if (this.jsonName != null && !this.jsonName.equals("")) {
                codedOutputByteBufferNano.writeString(10, this.jsonName);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public interface Label
        {
            @NanoEnumValue(legacy = false, value = Label.class)
            public static final int LABEL_OPTIONAL = 1;
            @NanoEnumValue(legacy = false, value = Label.class)
            public static final int LABEL_REPEATED = 3;
            @NanoEnumValue(legacy = false, value = Label.class)
            public static final int LABEL_REQUIRED = 2;
        }
        
        public interface Type
        {
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_BOOL = 8;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_BYTES = 12;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_DOUBLE = 1;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_ENUM = 14;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_FIXED32 = 7;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_FIXED64 = 6;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_FLOAT = 2;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_GROUP = 10;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_INT32 = 5;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_INT64 = 3;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_MESSAGE = 11;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_SFIXED32 = 15;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_SFIXED64 = 16;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_SINT32 = 17;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_SINT64 = 18;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_STRING = 9;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_UINT32 = 13;
            @NanoEnumValue(legacy = false, value = Type.class)
            public static final int TYPE_UINT64 = 4;
        }
    }
    
    public static final class FieldOptions extends ExtendableMessageNano<FieldOptions>
    {
        private static volatile FieldOptions[] _emptyArray;
        @NanoEnumValue(legacy = false, value = CType.class)
        public int ctype;
        public boolean deprecated;
        public boolean deprecatedRawMessage;
        public boolean enforceUtf8;
        @NanoEnumValue(legacy = false, value = JSType.class)
        public int jstype;
        public boolean lazy;
        public boolean packed;
        public UninterpretedOption[] uninterpretedOption;
        public UpgradedOption[] upgradedOption;
        public boolean weak;
        
        public FieldOptions() {
            this.clear();
        }
        
        @NanoEnumValue(legacy = false, value = CType.class)
        public static int checkCTypeOrThrow(final int i) {
            if (i >= 0 && i <= 2) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(37);
            sb.append(i);
            sb.append(" is not a valid enum CType");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = CType.class)
        public static int[] checkCTypeOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkCTypeOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = JSType.class)
        public static int checkJSTypeOrThrow(final int i) {
            if (i >= 0 && i <= 2) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(38);
            sb.append(i);
            sb.append(" is not a valid enum JSType");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = JSType.class)
        public static int[] checkJSTypeOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkJSTypeOrThrow(array[i]);
            }
            return array;
        }
        
        public static FieldOptions[] emptyArray() {
            if (FieldOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (FieldOptions._emptyArray == null) {
                        FieldOptions._emptyArray = new FieldOptions[0];
                    }
                }
            }
            return FieldOptions._emptyArray;
        }
        
        public static FieldOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new FieldOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static FieldOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new FieldOptions(), array);
        }
        
        public FieldOptions clear() {
            this.ctype = 0;
            this.packed = false;
            this.jstype = 0;
            this.lazy = false;
            this.deprecated = false;
            this.weak = false;
            this.upgradedOption = UpgradedOption.emptyArray();
            this.deprecatedRawMessage = false;
            this.enforceUtf8 = true;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.ctype != 0) {
                computeSerializedSize = n + CodedOutputByteBufferNano.computeInt32Size(1, this.ctype);
            }
            int n2 = computeSerializedSize;
            if (this.packed) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(2, this.packed);
            }
            int n3 = n2;
            if (this.deprecated) {
                n3 = n2 + CodedOutputByteBufferNano.computeBoolSize(3, this.deprecated);
            }
            int n4 = n3;
            if (this.lazy) {
                n4 = n3 + CodedOutputByteBufferNano.computeBoolSize(5, this.lazy);
            }
            int n5 = n4;
            if (this.jstype != 0) {
                n5 = n4 + CodedOutputByteBufferNano.computeInt32Size(6, this.jstype);
            }
            int n6 = n5;
            if (this.weak) {
                n6 = n5 + CodedOutputByteBufferNano.computeBoolSize(10, this.weak);
            }
            final UpgradedOption[] upgradedOption = this.upgradedOption;
            final int n7 = 0;
            int n8 = n6;
            if (upgradedOption != null) {
                n8 = n6;
                if (this.upgradedOption.length > 0) {
                    int n9;
                    for (int i = 0; i < this.upgradedOption.length; ++i, n6 = n9) {
                        final UpgradedOption upgradedOption2 = this.upgradedOption[i];
                        n9 = n6;
                        if (upgradedOption2 != null) {
                            n9 = n6 + CodedOutputByteBufferNano.computeMessageSize(11, upgradedOption2);
                        }
                    }
                    n8 = n6;
                }
            }
            int n10 = n8;
            if (this.deprecatedRawMessage) {
                n10 = n8 + CodedOutputByteBufferNano.computeBoolSize(12, this.deprecatedRawMessage);
            }
            int n11 = n10;
            if (!this.enforceUtf8) {
                n11 = n10 + CodedOutputByteBufferNano.computeBoolSize(13, this.enforceUtf8);
            }
            int n12 = n11;
            if (this.uninterpretedOption != null) {
                n12 = n11;
                if (this.uninterpretedOption.length > 0) {
                    int n13 = n7;
                    while (true) {
                        n12 = n11;
                        if (n13 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n13];
                        int n14 = n11;
                        if (uninterpretedOption != null) {
                            n14 = n11 + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n13;
                        n11 = n14;
                    }
                }
            }
            return n12;
        }
        
        @Override
        public FieldOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    default: {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    case 7994: {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                        int length;
                        if (this.uninterpretedOption == null) {
                            length = 0;
                        }
                        else {
                            length = this.uninterpretedOption.length;
                        }
                        final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                            i = length;
                        }
                        while (i < uninterpretedOption.length - 1) {
                            codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                            codedInputByteBufferNano.readTag();
                            ++i;
                        }
                        codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                        this.uninterpretedOption = uninterpretedOption;
                        continue;
                    }
                    case 104: {
                        this.enforceUtf8 = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 96: {
                        this.deprecatedRawMessage = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 90: {
                        final int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 90);
                        int length2;
                        if (this.upgradedOption == null) {
                            length2 = 0;
                        }
                        else {
                            length2 = this.upgradedOption.length;
                        }
                        final UpgradedOption[] upgradedOption = new UpgradedOption[repeatedFieldArrayLength2 + length2];
                        int j = length2;
                        if (length2 != 0) {
                            System.arraycopy(this.upgradedOption, 0, upgradedOption, 0, length2);
                            j = length2;
                        }
                        while (j < upgradedOption.length - 1) {
                            codedInputByteBufferNano.readMessage(upgradedOption[j] = new UpgradedOption());
                            codedInputByteBufferNano.readTag();
                            ++j;
                        }
                        codedInputByteBufferNano.readMessage(upgradedOption[j] = new UpgradedOption());
                        this.upgradedOption = upgradedOption;
                        continue;
                    }
                    case 80: {
                        this.weak = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 48: {
                        final int position = codedInputByteBufferNano.getPosition();
                        try {
                            this.jstype = checkJSTypeOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex) {
                            codedInputByteBufferNano.rewindToPosition(position);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 40: {
                        this.lazy = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 24: {
                        this.deprecated = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 16: {
                        this.packed = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 8: {
                        final int position2 = codedInputByteBufferNano.getPosition();
                        try {
                            this.ctype = checkCTypeOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex2) {
                            codedInputByteBufferNano.rewindToPosition(position2);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.ctype != 0) {
                codedOutputByteBufferNano.writeInt32(1, this.ctype);
            }
            if (this.packed) {
                codedOutputByteBufferNano.writeBool(2, this.packed);
            }
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(3, this.deprecated);
            }
            if (this.lazy) {
                codedOutputByteBufferNano.writeBool(5, this.lazy);
            }
            if (this.jstype != 0) {
                codedOutputByteBufferNano.writeInt32(6, this.jstype);
            }
            if (this.weak) {
                codedOutputByteBufferNano.writeBool(10, this.weak);
            }
            final UpgradedOption[] upgradedOption = this.upgradedOption;
            final int n = 0;
            if (upgradedOption != null && this.upgradedOption.length > 0) {
                for (int i = 0; i < this.upgradedOption.length; ++i) {
                    final UpgradedOption upgradedOption2 = this.upgradedOption[i];
                    if (upgradedOption2 != null) {
                        codedOutputByteBufferNano.writeMessage(11, upgradedOption2);
                    }
                }
            }
            if (this.deprecatedRawMessage) {
                codedOutputByteBufferNano.writeBool(12, this.deprecatedRawMessage);
            }
            if (!this.enforceUtf8) {
                codedOutputByteBufferNano.writeBool(13, this.enforceUtf8);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int j = n; j < this.uninterpretedOption.length; ++j) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[j];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public interface CType
        {
            @NanoEnumValue(legacy = false, value = CType.class)
            public static final int CORD = 1;
            @NanoEnumValue(legacy = false, value = CType.class)
            public static final int STRING = 0;
            @NanoEnumValue(legacy = false, value = CType.class)
            public static final int STRING_PIECE = 2;
        }
        
        public interface JSType
        {
            @NanoEnumValue(legacy = false, value = JSType.class)
            public static final int JS_NORMAL = 0;
            @NanoEnumValue(legacy = false, value = JSType.class)
            public static final int JS_NUMBER = 2;
            @NanoEnumValue(legacy = false, value = JSType.class)
            public static final int JS_STRING = 1;
        }
        
        public static final class UpgradedOption extends ExtendableMessageNano<UpgradedOption>
        {
            private static volatile UpgradedOption[] _emptyArray;
            public String name;
            public String value;
            
            public UpgradedOption() {
                this.clear();
            }
            
            public static UpgradedOption[] emptyArray() {
                if (UpgradedOption._emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (UpgradedOption._emptyArray == null) {
                            UpgradedOption._emptyArray = new UpgradedOption[0];
                        }
                    }
                }
                return UpgradedOption._emptyArray;
            }
            
            public static UpgradedOption parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                return new UpgradedOption().mergeFrom(codedInputByteBufferNano);
            }
            
            public static UpgradedOption parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
                return MessageNano.mergeFrom(new UpgradedOption(), array);
            }
            
            public UpgradedOption clear() {
                this.name = "";
                this.value = "";
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }
            
            @Override
            protected int computeSerializedSize() {
                int computeSerializedSize;
                final int n = computeSerializedSize = super.computeSerializedSize();
                if (this.name != null) {
                    computeSerializedSize = n;
                    if (!this.name.equals("")) {
                        computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                    }
                }
                int n2 = computeSerializedSize;
                if (this.value != null) {
                    n2 = computeSerializedSize;
                    if (!this.value.equals("")) {
                        n2 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.value);
                    }
                }
                return n2;
            }
            
            @Override
            public UpgradedOption mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    final int tag = codedInputByteBufferNano.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag != 10) {
                        if (tag != 18) {
                            if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            this.value = codedInputByteBufferNano.readString();
                        }
                    }
                    else {
                        this.name = codedInputByteBufferNano.readString();
                    }
                }
            }
            
            @Override
            public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (this.name != null && !this.name.equals("")) {
                    codedOutputByteBufferNano.writeString(1, this.name);
                }
                if (this.value != null && !this.value.equals("")) {
                    codedOutputByteBufferNano.writeString(2, this.value);
                }
                super.writeTo(codedOutputByteBufferNano);
            }
        }
    }
    
    public static final class FileDescriptorProto extends ExtendableMessageNano<FileDescriptorProto>
    {
        private static volatile FileDescriptorProto[] _emptyArray;
        public String[] dependency;
        public EnumDescriptorProto[] enumType;
        public FieldDescriptorProto[] extension;
        public DescriptorProto[] messageType;
        public String name;
        public FileOptions options;
        public String package_;
        public int[] publicDependency;
        public ServiceDescriptorProto[] service;
        public SourceCodeInfo sourceCodeInfo;
        public String syntax;
        public int[] weakDependency;
        
        public FileDescriptorProto() {
            this.clear();
        }
        
        public static FileDescriptorProto[] emptyArray() {
            if (FileDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (FileDescriptorProto._emptyArray == null) {
                        FileDescriptorProto._emptyArray = new FileDescriptorProto[0];
                    }
                }
            }
            return FileDescriptorProto._emptyArray;
        }
        
        public static FileDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new FileDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static FileDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new FileDescriptorProto(), array);
        }
        
        public FileDescriptorProto clear() {
            this.name = "";
            this.package_ = "";
            this.dependency = WireFormatNano.EMPTY_STRING_ARRAY;
            this.publicDependency = WireFormatNano.EMPTY_INT_ARRAY;
            this.weakDependency = WireFormatNano.EMPTY_INT_ARRAY;
            this.messageType = DescriptorProto.emptyArray();
            this.enumType = EnumDescriptorProto.emptyArray();
            this.service = ServiceDescriptorProto.emptyArray();
            this.extension = FieldDescriptorProto.emptyArray();
            this.options = null;
            this.sourceCodeInfo = null;
            this.syntax = "";
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            int n2 = computeSerializedSize;
            if (this.package_ != null) {
                n2 = computeSerializedSize;
                if (!this.package_.equals("")) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.package_);
                }
            }
            final String[] dependency = this.dependency;
            final int n3 = 0;
            int n4 = n2;
            if (dependency != null) {
                n4 = n2;
                if (this.dependency.length > 0) {
                    int i = 0;
                    int n6;
                    int n5 = n6 = 0;
                    while (i < this.dependency.length) {
                        final String s = this.dependency[i];
                        int n7 = n5;
                        int n8 = n6;
                        if (s != null) {
                            n8 = n6 + 1;
                            n7 = n5 + CodedOutputByteBufferNano.computeStringSizeNoTag(s);
                        }
                        ++i;
                        n5 = n7;
                        n6 = n8;
                    }
                    n4 = n2 + n5 + n6 * 1;
                }
            }
            int n9 = n4;
            if (this.messageType != null) {
                n9 = n4;
                if (this.messageType.length > 0) {
                    int n10;
                    for (int j = 0; j < this.messageType.length; ++j, n4 = n10) {
                        final DescriptorProto descriptorProto = this.messageType[j];
                        n10 = n4;
                        if (descriptorProto != null) {
                            n10 = n4 + CodedOutputByteBufferNano.computeMessageSize(4, descriptorProto);
                        }
                    }
                    n9 = n4;
                }
            }
            int n11 = n9;
            if (this.enumType != null) {
                n11 = n9;
                if (this.enumType.length > 0) {
                    n11 = n9;
                    int n12;
                    for (int k = 0; k < this.enumType.length; ++k, n11 = n12) {
                        final EnumDescriptorProto enumDescriptorProto = this.enumType[k];
                        n12 = n11;
                        if (enumDescriptorProto != null) {
                            n12 = n11 + CodedOutputByteBufferNano.computeMessageSize(5, enumDescriptorProto);
                        }
                    }
                }
            }
            int n13 = n11;
            if (this.service != null) {
                n13 = n11;
                if (this.service.length > 0) {
                    int n14;
                    for (int l = 0; l < this.service.length; ++l, n11 = n14) {
                        final ServiceDescriptorProto serviceDescriptorProto = this.service[l];
                        n14 = n11;
                        if (serviceDescriptorProto != null) {
                            n14 = n11 + CodedOutputByteBufferNano.computeMessageSize(6, serviceDescriptorProto);
                        }
                    }
                    n13 = n11;
                }
            }
            int n15 = n13;
            if (this.extension != null) {
                n15 = n13;
                if (this.extension.length > 0) {
                    n15 = n13;
                    int n17;
                    for (int n16 = 0; n16 < this.extension.length; ++n16, n15 = n17) {
                        final FieldDescriptorProto fieldDescriptorProto = this.extension[n16];
                        n17 = n15;
                        if (fieldDescriptorProto != null) {
                            n17 = n15 + CodedOutputByteBufferNano.computeMessageSize(7, fieldDescriptorProto);
                        }
                    }
                }
            }
            int n18 = n15;
            if (this.options != null) {
                n18 = n15 + CodedOutputByteBufferNano.computeMessageSize(8, this.options);
            }
            int n19 = n18;
            if (this.sourceCodeInfo != null) {
                n19 = n18 + CodedOutputByteBufferNano.computeMessageSize(9, this.sourceCodeInfo);
            }
            int n20 = n19;
            if (this.publicDependency != null) {
                n20 = n19;
                if (this.publicDependency.length > 0) {
                    int n21 = 0;
                    int n22 = 0;
                    while (n21 < this.publicDependency.length) {
                        n22 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.publicDependency[n21]);
                        ++n21;
                    }
                    n20 = n19 + n22 + this.publicDependency.length * 1;
                }
            }
            int n23 = n20;
            if (this.weakDependency != null) {
                n23 = n20;
                if (this.weakDependency.length > 0) {
                    int n24 = 0;
                    for (int n25 = n3; n25 < this.weakDependency.length; ++n25) {
                        n24 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.weakDependency[n25]);
                    }
                    n23 = n20 + n24 + 1 * this.weakDependency.length;
                }
            }
            int n26 = n23;
            if (this.syntax != null) {
                n26 = n23;
                if (!this.syntax.equals("")) {
                    n26 = n23 + CodedOutputByteBufferNano.computeStringSize(12, this.syntax);
                }
            }
            return n26;
        }
        
        @Override
        public FileDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    default: {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    case 98: {
                        this.syntax = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 90: {
                        final int pushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                        final int position = codedInputByteBufferNano.getPosition();
                        int n = 0;
                        while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                            codedInputByteBufferNano.readInt32();
                            ++n;
                        }
                        codedInputByteBufferNano.rewindToPosition(position);
                        int length;
                        if (this.weakDependency == null) {
                            length = 0;
                        }
                        else {
                            length = this.weakDependency.length;
                        }
                        final int[] weakDependency = new int[n + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.weakDependency, 0, weakDependency, 0, length);
                            i = length;
                        }
                        while (i < weakDependency.length) {
                            weakDependency[i] = codedInputByteBufferNano.readInt32();
                            ++i;
                        }
                        this.weakDependency = weakDependency;
                        codedInputByteBufferNano.popLimit(pushLimit);
                        continue;
                    }
                    case 88: {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 88);
                        int length2;
                        if (this.weakDependency == null) {
                            length2 = 0;
                        }
                        else {
                            length2 = this.weakDependency.length;
                        }
                        final int[] weakDependency2 = new int[repeatedFieldArrayLength + length2];
                        int j = length2;
                        if (length2 != 0) {
                            System.arraycopy(this.weakDependency, 0, weakDependency2, 0, length2);
                            j = length2;
                        }
                        while (j < weakDependency2.length - 1) {
                            weakDependency2[j] = codedInputByteBufferNano.readInt32();
                            codedInputByteBufferNano.readTag();
                            ++j;
                        }
                        weakDependency2[j] = codedInputByteBufferNano.readInt32();
                        this.weakDependency = weakDependency2;
                        continue;
                    }
                    case 82: {
                        final int pushLimit2 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                        final int position2 = codedInputByteBufferNano.getPosition();
                        int n2 = 0;
                        while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                            codedInputByteBufferNano.readInt32();
                            ++n2;
                        }
                        codedInputByteBufferNano.rewindToPosition(position2);
                        int length3;
                        if (this.publicDependency == null) {
                            length3 = 0;
                        }
                        else {
                            length3 = this.publicDependency.length;
                        }
                        final int[] publicDependency = new int[n2 + length3];
                        int k = length3;
                        if (length3 != 0) {
                            System.arraycopy(this.publicDependency, 0, publicDependency, 0, length3);
                            k = length3;
                        }
                        while (k < publicDependency.length) {
                            publicDependency[k] = codedInputByteBufferNano.readInt32();
                            ++k;
                        }
                        this.publicDependency = publicDependency;
                        codedInputByteBufferNano.popLimit(pushLimit2);
                        continue;
                    }
                    case 80: {
                        final int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 80);
                        int length4;
                        if (this.publicDependency == null) {
                            length4 = 0;
                        }
                        else {
                            length4 = this.publicDependency.length;
                        }
                        final int[] publicDependency2 = new int[repeatedFieldArrayLength2 + length4];
                        int l = length4;
                        if (length4 != 0) {
                            System.arraycopy(this.publicDependency, 0, publicDependency2, 0, length4);
                            l = length4;
                        }
                        while (l < publicDependency2.length - 1) {
                            publicDependency2[l] = codedInputByteBufferNano.readInt32();
                            codedInputByteBufferNano.readTag();
                            ++l;
                        }
                        publicDependency2[l] = codedInputByteBufferNano.readInt32();
                        this.publicDependency = publicDependency2;
                        continue;
                    }
                    case 74: {
                        if (this.sourceCodeInfo == null) {
                            this.sourceCodeInfo = new SourceCodeInfo();
                        }
                        codedInputByteBufferNano.readMessage(this.sourceCodeInfo);
                        continue;
                    }
                    case 66: {
                        if (this.options == null) {
                            this.options = new FileOptions();
                        }
                        codedInputByteBufferNano.readMessage(this.options);
                        continue;
                    }
                    case 58: {
                        final int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 58);
                        int length5;
                        if (this.extension == null) {
                            length5 = 0;
                        }
                        else {
                            length5 = this.extension.length;
                        }
                        final FieldDescriptorProto[] extension = new FieldDescriptorProto[repeatedFieldArrayLength3 + length5];
                        int n3 = length5;
                        if (length5 != 0) {
                            System.arraycopy(this.extension, 0, extension, 0, length5);
                            n3 = length5;
                        }
                        while (n3 < extension.length - 1) {
                            codedInputByteBufferNano.readMessage(extension[n3] = new FieldDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++n3;
                        }
                        codedInputByteBufferNano.readMessage(extension[n3] = new FieldDescriptorProto());
                        this.extension = extension;
                        continue;
                    }
                    case 50: {
                        final int repeatedFieldArrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 50);
                        int length6;
                        if (this.service == null) {
                            length6 = 0;
                        }
                        else {
                            length6 = this.service.length;
                        }
                        final ServiceDescriptorProto[] service = new ServiceDescriptorProto[repeatedFieldArrayLength4 + length6];
                        int n4 = length6;
                        if (length6 != 0) {
                            System.arraycopy(this.service, 0, service, 0, length6);
                            n4 = length6;
                        }
                        while (n4 < service.length - 1) {
                            codedInputByteBufferNano.readMessage(service[n4] = new ServiceDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++n4;
                        }
                        codedInputByteBufferNano.readMessage(service[n4] = new ServiceDescriptorProto());
                        this.service = service;
                        continue;
                    }
                    case 42: {
                        final int repeatedFieldArrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 42);
                        int length7;
                        if (this.enumType == null) {
                            length7 = 0;
                        }
                        else {
                            length7 = this.enumType.length;
                        }
                        final EnumDescriptorProto[] enumType = new EnumDescriptorProto[repeatedFieldArrayLength5 + length7];
                        int n5 = length7;
                        if (length7 != 0) {
                            System.arraycopy(this.enumType, 0, enumType, 0, length7);
                            n5 = length7;
                        }
                        while (n5 < enumType.length - 1) {
                            codedInputByteBufferNano.readMessage(enumType[n5] = new EnumDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++n5;
                        }
                        codedInputByteBufferNano.readMessage(enumType[n5] = new EnumDescriptorProto());
                        this.enumType = enumType;
                        continue;
                    }
                    case 34: {
                        final int repeatedFieldArrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                        int length8;
                        if (this.messageType == null) {
                            length8 = 0;
                        }
                        else {
                            length8 = this.messageType.length;
                        }
                        final DescriptorProto[] messageType = new DescriptorProto[repeatedFieldArrayLength6 + length8];
                        int n6 = length8;
                        if (length8 != 0) {
                            System.arraycopy(this.messageType, 0, messageType, 0, length8);
                            n6 = length8;
                        }
                        while (n6 < messageType.length - 1) {
                            codedInputByteBufferNano.readMessage(messageType[n6] = new DescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++n6;
                        }
                        codedInputByteBufferNano.readMessage(messageType[n6] = new DescriptorProto());
                        this.messageType = messageType;
                        continue;
                    }
                    case 26: {
                        final int repeatedFieldArrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 26);
                        int length9;
                        if (this.dependency == null) {
                            length9 = 0;
                        }
                        else {
                            length9 = this.dependency.length;
                        }
                        final String[] dependency = new String[repeatedFieldArrayLength7 + length9];
                        int n7 = length9;
                        if (length9 != 0) {
                            System.arraycopy(this.dependency, 0, dependency, 0, length9);
                            n7 = length9;
                        }
                        while (n7 < dependency.length - 1) {
                            dependency[n7] = codedInputByteBufferNano.readString();
                            codedInputByteBufferNano.readTag();
                            ++n7;
                        }
                        dependency[n7] = codedInputByteBufferNano.readString();
                        this.dependency = dependency;
                        continue;
                    }
                    case 18: {
                        this.package_ = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 10: {
                        this.name = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            if (this.package_ != null && !this.package_.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.package_);
            }
            final String[] dependency = this.dependency;
            final int n = 0;
            if (dependency != null && this.dependency.length > 0) {
                for (int i = 0; i < this.dependency.length; ++i) {
                    final String s = this.dependency[i];
                    if (s != null) {
                        codedOutputByteBufferNano.writeString(3, s);
                    }
                }
            }
            if (this.messageType != null && this.messageType.length > 0) {
                for (int j = 0; j < this.messageType.length; ++j) {
                    final DescriptorProto descriptorProto = this.messageType[j];
                    if (descriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(4, descriptorProto);
                    }
                }
            }
            if (this.enumType != null && this.enumType.length > 0) {
                for (int k = 0; k < this.enumType.length; ++k) {
                    final EnumDescriptorProto enumDescriptorProto = this.enumType[k];
                    if (enumDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(5, enumDescriptorProto);
                    }
                }
            }
            if (this.service != null && this.service.length > 0) {
                for (int l = 0; l < this.service.length; ++l) {
                    final ServiceDescriptorProto serviceDescriptorProto = this.service[l];
                    if (serviceDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(6, serviceDescriptorProto);
                    }
                }
            }
            if (this.extension != null && this.extension.length > 0) {
                for (int n2 = 0; n2 < this.extension.length; ++n2) {
                    final FieldDescriptorProto fieldDescriptorProto = this.extension[n2];
                    if (fieldDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(7, fieldDescriptorProto);
                    }
                }
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(8, this.options);
            }
            if (this.sourceCodeInfo != null) {
                codedOutputByteBufferNano.writeMessage(9, this.sourceCodeInfo);
            }
            if (this.publicDependency != null && this.publicDependency.length > 0) {
                for (int n3 = 0; n3 < this.publicDependency.length; ++n3) {
                    codedOutputByteBufferNano.writeInt32(10, this.publicDependency[n3]);
                }
            }
            if (this.weakDependency != null && this.weakDependency.length > 0) {
                for (int n4 = n; n4 < this.weakDependency.length; ++n4) {
                    codedOutputByteBufferNano.writeInt32(11, this.weakDependency[n4]);
                }
            }
            if (this.syntax != null && !this.syntax.equals("")) {
                codedOutputByteBufferNano.writeString(12, this.syntax);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class FileDescriptorSet extends ExtendableMessageNano<FileDescriptorSet>
    {
        private static volatile FileDescriptorSet[] _emptyArray;
        public FileDescriptorProto[] file;
        
        public FileDescriptorSet() {
            this.clear();
        }
        
        public static FileDescriptorSet[] emptyArray() {
            if (FileDescriptorSet._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (FileDescriptorSet._emptyArray == null) {
                        FileDescriptorSet._emptyArray = new FileDescriptorSet[0];
                    }
                }
            }
            return FileDescriptorSet._emptyArray;
        }
        
        public static FileDescriptorSet parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new FileDescriptorSet().mergeFrom(codedInputByteBufferNano);
        }
        
        public static FileDescriptorSet parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new FileDescriptorSet(), array);
        }
        
        public FileDescriptorSet clear() {
            this.file = FileDescriptorProto.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            int n = computeSerializedSize = super.computeSerializedSize();
            if (this.file != null) {
                computeSerializedSize = n;
                if (this.file.length > 0) {
                    int n2 = 0;
                    while (true) {
                        computeSerializedSize = n;
                        if (n2 >= this.file.length) {
                            break;
                        }
                        final FileDescriptorProto fileDescriptorProto = this.file[n2];
                        int n3 = n;
                        if (fileDescriptorProto != null) {
                            n3 = n + CodedOutputByteBufferNano.computeMessageSize(1, fileDescriptorProto);
                        }
                        ++n2;
                        n = n3;
                    }
                }
            }
            return computeSerializedSize;
        }
        
        @Override
        public FileDescriptorSet mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                    continue;
                }
                else {
                    final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                    int length;
                    if (this.file == null) {
                        length = 0;
                    }
                    else {
                        length = this.file.length;
                    }
                    final FileDescriptorProto[] file = new FileDescriptorProto[repeatedFieldArrayLength + length];
                    int i = length;
                    if (length != 0) {
                        System.arraycopy(this.file, 0, file, 0, length);
                        i = length;
                    }
                    while (i < file.length - 1) {
                        codedInputByteBufferNano.readMessage(file[i] = new FileDescriptorProto());
                        codedInputByteBufferNano.readTag();
                        ++i;
                    }
                    codedInputByteBufferNano.readMessage(file[i] = new FileDescriptorProto());
                    this.file = file;
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.file != null && this.file.length > 0) {
                for (int i = 0; i < this.file.length; ++i) {
                    final FileDescriptorProto fileDescriptorProto = this.file[i];
                    if (fileDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(1, fileDescriptorProto);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class FileOptions extends ExtendableMessageNano<FileOptions>
    {
        private static volatile FileOptions[] _emptyArray;
        public int ccApiVersion;
        public boolean ccEnableArenas;
        public boolean ccGenericServices;
        public boolean ccUtf8Verification;
        public String csharpNamespace;
        public boolean deprecated;
        public String goPackage;
        public String javaAltApiPackage;
        public int javaApiVersion;
        public boolean javaEnableDualGenerateMutableApi;
        public boolean javaGenericServices;
        public boolean javaJava5Enums;
        public boolean javaMultipleFiles;
        public String javaMultipleFilesMutablePackage;
        public boolean javaMutableApi;
        public String javaOuterClassname;
        public String javaPackage;
        public boolean javaStringCheckUtf8;
        public boolean javaUseJavaproto2;
        public boolean javaUseJavastrings;
        public String javascriptPackage;
        public String objcClassPrefix;
        @NanoEnumValue(legacy = false, value = OptimizeMode.class)
        public int optimizeFor;
        public String phpClassPrefix;
        public boolean phpGenericServices;
        public String phpNamespace;
        public int pyApiVersion;
        public boolean pyGenericServices;
        public String swiftPrefix;
        public int szlApiVersion;
        public UninterpretedOption[] uninterpretedOption;
        
        public FileOptions() {
            this.clear();
        }
        
        @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
        public static int checkCompatibilityLevelOrThrow(final int i) {
            if (i >= 0 && i <= 0) {
                return i;
            }
            if (i >= 100 && i <= 100) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(50);
            sb.append(i);
            sb.append(" is not a valid enum CompatibilityLevel");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
        public static int[] checkCompatibilityLevelOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkCompatibilityLevelOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = OptimizeMode.class)
        public static int checkOptimizeModeOrThrow(final int i) {
            if (i >= 1 && i <= 3) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(44);
            sb.append(i);
            sb.append(" is not a valid enum OptimizeMode");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = OptimizeMode.class)
        public static int[] checkOptimizeModeOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkOptimizeModeOrThrow(array[i]);
            }
            return array;
        }
        
        public static FileOptions[] emptyArray() {
            if (FileOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (FileOptions._emptyArray == null) {
                        FileOptions._emptyArray = new FileOptions[0];
                    }
                }
            }
            return FileOptions._emptyArray;
        }
        
        public static FileOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new FileOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static FileOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new FileOptions(), array);
        }
        
        public FileOptions clear() {
            this.ccApiVersion = 2;
            this.ccUtf8Verification = true;
            this.javaPackage = "";
            this.pyApiVersion = 2;
            this.javaApiVersion = 2;
            this.javaUseJavaproto2 = true;
            this.javaJava5Enums = true;
            this.javaUseJavastrings = false;
            this.javaAltApiPackage = "";
            this.javaEnableDualGenerateMutableApi = false;
            this.javaOuterClassname = "";
            this.javaMultipleFiles = false;
            this.javaStringCheckUtf8 = false;
            this.javaMutableApi = false;
            this.javaMultipleFilesMutablePackage = "";
            this.optimizeFor = 1;
            this.goPackage = "";
            this.javascriptPackage = "";
            this.szlApiVersion = 1;
            this.ccGenericServices = false;
            this.javaGenericServices = false;
            this.pyGenericServices = false;
            this.phpGenericServices = false;
            this.deprecated = false;
            this.ccEnableArenas = false;
            this.objcClassPrefix = "";
            this.csharpNamespace = "";
            this.swiftPrefix = "";
            this.phpClassPrefix = "";
            this.phpNamespace = "";
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.javaPackage != null) {
                computeSerializedSize = n;
                if (!this.javaPackage.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.javaPackage);
                }
            }
            int n2 = computeSerializedSize;
            if (this.ccApiVersion != 2) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, this.ccApiVersion);
            }
            int n3 = n2;
            if (this.pyApiVersion != 2) {
                n3 = n2 + CodedOutputByteBufferNano.computeInt32Size(4, this.pyApiVersion);
            }
            int n4 = n3;
            if (this.javaApiVersion != 2) {
                n4 = n3 + CodedOutputByteBufferNano.computeInt32Size(5, this.javaApiVersion);
            }
            int n5 = n4;
            if (!this.javaUseJavaproto2) {
                n5 = n4 + CodedOutputByteBufferNano.computeBoolSize(6, this.javaUseJavaproto2);
            }
            int n6 = n5;
            if (!this.javaJava5Enums) {
                n6 = n5 + CodedOutputByteBufferNano.computeBoolSize(7, this.javaJava5Enums);
            }
            int n7 = n6;
            if (this.javaOuterClassname != null) {
                n7 = n6;
                if (!this.javaOuterClassname.equals("")) {
                    n7 = n6 + CodedOutputByteBufferNano.computeStringSize(8, this.javaOuterClassname);
                }
            }
            int n8 = n7;
            if (this.optimizeFor != 1) {
                n8 = n7 + CodedOutputByteBufferNano.computeInt32Size(9, this.optimizeFor);
            }
            int n9 = n8;
            if (this.javaMultipleFiles) {
                n9 = n8 + CodedOutputByteBufferNano.computeBoolSize(10, this.javaMultipleFiles);
            }
            int n10 = n9;
            if (this.goPackage != null) {
                n10 = n9;
                if (!this.goPackage.equals("")) {
                    n10 = n9 + CodedOutputByteBufferNano.computeStringSize(11, this.goPackage);
                }
            }
            int n11 = n10;
            if (this.javascriptPackage != null) {
                n11 = n10;
                if (!this.javascriptPackage.equals("")) {
                    n11 = n10 + CodedOutputByteBufferNano.computeStringSize(12, this.javascriptPackage);
                }
            }
            int n12 = n11;
            if (this.szlApiVersion != 1) {
                n12 = n11 + CodedOutputByteBufferNano.computeInt32Size(14, this.szlApiVersion);
            }
            int n13 = n12;
            if (this.ccGenericServices) {
                n13 = n12 + CodedOutputByteBufferNano.computeBoolSize(16, this.ccGenericServices);
            }
            int n14 = n13;
            if (this.javaGenericServices) {
                n14 = n13 + CodedOutputByteBufferNano.computeBoolSize(17, this.javaGenericServices);
            }
            int n15 = n14;
            if (this.pyGenericServices) {
                n15 = n14 + CodedOutputByteBufferNano.computeBoolSize(18, this.pyGenericServices);
            }
            int n16 = n15;
            if (this.javaAltApiPackage != null) {
                n16 = n15;
                if (!this.javaAltApiPackage.equals("")) {
                    n16 = n15 + CodedOutputByteBufferNano.computeStringSize(19, this.javaAltApiPackage);
                }
            }
            int n17 = n16;
            if (this.javaUseJavastrings) {
                n17 = n16 + CodedOutputByteBufferNano.computeBoolSize(21, this.javaUseJavastrings);
            }
            int n18 = n17;
            if (this.deprecated) {
                n18 = n17 + CodedOutputByteBufferNano.computeBoolSize(23, this.deprecated);
            }
            int n19 = n18;
            if (!this.ccUtf8Verification) {
                n19 = n18 + CodedOutputByteBufferNano.computeBoolSize(24, this.ccUtf8Verification);
            }
            int n20 = n19;
            if (this.javaEnableDualGenerateMutableApi) {
                n20 = n19 + CodedOutputByteBufferNano.computeBoolSize(26, this.javaEnableDualGenerateMutableApi);
            }
            int n21 = n20;
            if (this.javaStringCheckUtf8) {
                n21 = n20 + CodedOutputByteBufferNano.computeBoolSize(27, this.javaStringCheckUtf8);
            }
            int n22 = n21;
            if (this.javaMutableApi) {
                n22 = n21 + CodedOutputByteBufferNano.computeBoolSize(28, this.javaMutableApi);
            }
            int n23 = n22;
            if (this.javaMultipleFilesMutablePackage != null) {
                n23 = n22;
                if (!this.javaMultipleFilesMutablePackage.equals("")) {
                    n23 = n22 + CodedOutputByteBufferNano.computeStringSize(29, this.javaMultipleFilesMutablePackage);
                }
            }
            int n24 = n23;
            if (this.ccEnableArenas) {
                n24 = n23 + CodedOutputByteBufferNano.computeBoolSize(31, this.ccEnableArenas);
            }
            int n25 = n24;
            if (this.objcClassPrefix != null) {
                n25 = n24;
                if (!this.objcClassPrefix.equals("")) {
                    n25 = n24 + CodedOutputByteBufferNano.computeStringSize(36, this.objcClassPrefix);
                }
            }
            int n26 = n25;
            if (this.csharpNamespace != null) {
                n26 = n25;
                if (!this.csharpNamespace.equals("")) {
                    n26 = n25 + CodedOutputByteBufferNano.computeStringSize(37, this.csharpNamespace);
                }
            }
            int n27 = n26;
            if (this.swiftPrefix != null) {
                n27 = n26;
                if (!this.swiftPrefix.equals("")) {
                    n27 = n26 + CodedOutputByteBufferNano.computeStringSize(39, this.swiftPrefix);
                }
            }
            int n28 = n27;
            if (this.phpClassPrefix != null) {
                n28 = n27;
                if (!this.phpClassPrefix.equals("")) {
                    n28 = n27 + CodedOutputByteBufferNano.computeStringSize(40, this.phpClassPrefix);
                }
            }
            int n29 = n28;
            if (this.phpNamespace != null) {
                n29 = n28;
                if (!this.phpNamespace.equals("")) {
                    n29 = n28 + CodedOutputByteBufferNano.computeStringSize(41, this.phpNamespace);
                }
            }
            int n30 = n29;
            if (this.phpGenericServices) {
                n30 = n29 + CodedOutputByteBufferNano.computeBoolSize(42, this.phpGenericServices);
            }
            int n31 = n30;
            if (this.uninterpretedOption != null) {
                n31 = n30;
                if (this.uninterpretedOption.length > 0) {
                    int n32 = 0;
                    while (true) {
                        n31 = n30;
                        if (n32 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n32];
                        int n33 = n30;
                        if (uninterpretedOption != null) {
                            n33 = n30 + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n32;
                        n30 = n33;
                    }
                }
            }
            return n31;
        }
        
        @Override
        public FileOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    default: {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    case 7994: {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                        int length;
                        if (this.uninterpretedOption == null) {
                            length = 0;
                        }
                        else {
                            length = this.uninterpretedOption.length;
                        }
                        final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                            i = length;
                        }
                        while (i < uninterpretedOption.length - 1) {
                            codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                            codedInputByteBufferNano.readTag();
                            ++i;
                        }
                        codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                        this.uninterpretedOption = uninterpretedOption;
                        continue;
                    }
                    case 336: {
                        this.phpGenericServices = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 330: {
                        this.phpNamespace = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 322: {
                        this.phpClassPrefix = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 314: {
                        this.swiftPrefix = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 298: {
                        this.csharpNamespace = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 290: {
                        this.objcClassPrefix = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 248: {
                        this.ccEnableArenas = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 234: {
                        this.javaMultipleFilesMutablePackage = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 224: {
                        this.javaMutableApi = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 216: {
                        this.javaStringCheckUtf8 = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 208: {
                        this.javaEnableDualGenerateMutableApi = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 192: {
                        this.ccUtf8Verification = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 184: {
                        this.deprecated = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 168: {
                        this.javaUseJavastrings = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 154: {
                        this.javaAltApiPackage = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 144: {
                        this.pyGenericServices = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 136: {
                        this.javaGenericServices = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 128: {
                        this.ccGenericServices = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 112: {
                        this.szlApiVersion = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 98: {
                        this.javascriptPackage = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 90: {
                        this.goPackage = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 80: {
                        this.javaMultipleFiles = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 72: {
                        final int position = codedInputByteBufferNano.getPosition();
                        try {
                            this.optimizeFor = checkOptimizeModeOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex) {
                            codedInputByteBufferNano.rewindToPosition(position);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 66: {
                        this.javaOuterClassname = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 56: {
                        this.javaJava5Enums = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 48: {
                        this.javaUseJavaproto2 = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 40: {
                        this.javaApiVersion = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 32: {
                        this.pyApiVersion = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 16: {
                        this.ccApiVersion = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 10: {
                        this.javaPackage = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.javaPackage != null && !this.javaPackage.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.javaPackage);
            }
            if (this.ccApiVersion != 2) {
                codedOutputByteBufferNano.writeInt32(2, this.ccApiVersion);
            }
            if (this.pyApiVersion != 2) {
                codedOutputByteBufferNano.writeInt32(4, this.pyApiVersion);
            }
            if (this.javaApiVersion != 2) {
                codedOutputByteBufferNano.writeInt32(5, this.javaApiVersion);
            }
            if (!this.javaUseJavaproto2) {
                codedOutputByteBufferNano.writeBool(6, this.javaUseJavaproto2);
            }
            if (!this.javaJava5Enums) {
                codedOutputByteBufferNano.writeBool(7, this.javaJava5Enums);
            }
            if (this.javaOuterClassname != null && !this.javaOuterClassname.equals("")) {
                codedOutputByteBufferNano.writeString(8, this.javaOuterClassname);
            }
            if (this.optimizeFor != 1) {
                codedOutputByteBufferNano.writeInt32(9, this.optimizeFor);
            }
            if (this.javaMultipleFiles) {
                codedOutputByteBufferNano.writeBool(10, this.javaMultipleFiles);
            }
            if (this.goPackage != null && !this.goPackage.equals("")) {
                codedOutputByteBufferNano.writeString(11, this.goPackage);
            }
            if (this.javascriptPackage != null && !this.javascriptPackage.equals("")) {
                codedOutputByteBufferNano.writeString(12, this.javascriptPackage);
            }
            if (this.szlApiVersion != 1) {
                codedOutputByteBufferNano.writeInt32(14, this.szlApiVersion);
            }
            if (this.ccGenericServices) {
                codedOutputByteBufferNano.writeBool(16, this.ccGenericServices);
            }
            if (this.javaGenericServices) {
                codedOutputByteBufferNano.writeBool(17, this.javaGenericServices);
            }
            if (this.pyGenericServices) {
                codedOutputByteBufferNano.writeBool(18, this.pyGenericServices);
            }
            if (this.javaAltApiPackage != null && !this.javaAltApiPackage.equals("")) {
                codedOutputByteBufferNano.writeString(19, this.javaAltApiPackage);
            }
            if (this.javaUseJavastrings) {
                codedOutputByteBufferNano.writeBool(21, this.javaUseJavastrings);
            }
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(23, this.deprecated);
            }
            if (!this.ccUtf8Verification) {
                codedOutputByteBufferNano.writeBool(24, this.ccUtf8Verification);
            }
            if (this.javaEnableDualGenerateMutableApi) {
                codedOutputByteBufferNano.writeBool(26, this.javaEnableDualGenerateMutableApi);
            }
            if (this.javaStringCheckUtf8) {
                codedOutputByteBufferNano.writeBool(27, this.javaStringCheckUtf8);
            }
            if (this.javaMutableApi) {
                codedOutputByteBufferNano.writeBool(28, this.javaMutableApi);
            }
            if (this.javaMultipleFilesMutablePackage != null && !this.javaMultipleFilesMutablePackage.equals("")) {
                codedOutputByteBufferNano.writeString(29, this.javaMultipleFilesMutablePackage);
            }
            if (this.ccEnableArenas) {
                codedOutputByteBufferNano.writeBool(31, this.ccEnableArenas);
            }
            if (this.objcClassPrefix != null && !this.objcClassPrefix.equals("")) {
                codedOutputByteBufferNano.writeString(36, this.objcClassPrefix);
            }
            if (this.csharpNamespace != null && !this.csharpNamespace.equals("")) {
                codedOutputByteBufferNano.writeString(37, this.csharpNamespace);
            }
            if (this.swiftPrefix != null && !this.swiftPrefix.equals("")) {
                codedOutputByteBufferNano.writeString(39, this.swiftPrefix);
            }
            if (this.phpClassPrefix != null && !this.phpClassPrefix.equals("")) {
                codedOutputByteBufferNano.writeString(40, this.phpClassPrefix);
            }
            if (this.phpNamespace != null && !this.phpNamespace.equals("")) {
                codedOutputByteBufferNano.writeString(41, this.phpNamespace);
            }
            if (this.phpGenericServices) {
                codedOutputByteBufferNano.writeBool(42, this.phpGenericServices);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public interface CompatibilityLevel
        {
            @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
            public static final int NO_COMPATIBILITY = 0;
            @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
            public static final int PROTO1_COMPATIBLE = 100;
        }
        
        public interface OptimizeMode
        {
            @NanoEnumValue(legacy = false, value = OptimizeMode.class)
            public static final int CODE_SIZE = 2;
            @NanoEnumValue(legacy = false, value = OptimizeMode.class)
            public static final int LITE_RUNTIME = 3;
            @NanoEnumValue(legacy = false, value = OptimizeMode.class)
            public static final int SPEED = 1;
        }
    }
    
    public static final class GeneratedCodeInfo extends ExtendableMessageNano<GeneratedCodeInfo>
    {
        private static volatile GeneratedCodeInfo[] _emptyArray;
        public Annotation[] annotation;
        
        public GeneratedCodeInfo() {
            this.clear();
        }
        
        public static GeneratedCodeInfo[] emptyArray() {
            if (GeneratedCodeInfo._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (GeneratedCodeInfo._emptyArray == null) {
                        GeneratedCodeInfo._emptyArray = new GeneratedCodeInfo[0];
                    }
                }
            }
            return GeneratedCodeInfo._emptyArray;
        }
        
        public static GeneratedCodeInfo parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new GeneratedCodeInfo().mergeFrom(codedInputByteBufferNano);
        }
        
        public static GeneratedCodeInfo parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new GeneratedCodeInfo(), array);
        }
        
        public GeneratedCodeInfo clear() {
            this.annotation = Annotation.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            int n = computeSerializedSize = super.computeSerializedSize();
            if (this.annotation != null) {
                computeSerializedSize = n;
                if (this.annotation.length > 0) {
                    int n2 = 0;
                    while (true) {
                        computeSerializedSize = n;
                        if (n2 >= this.annotation.length) {
                            break;
                        }
                        final Annotation annotation = this.annotation[n2];
                        int n3 = n;
                        if (annotation != null) {
                            n3 = n + CodedOutputByteBufferNano.computeMessageSize(1, annotation);
                        }
                        ++n2;
                        n = n3;
                    }
                }
            }
            return computeSerializedSize;
        }
        
        @Override
        public GeneratedCodeInfo mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                    continue;
                }
                else {
                    final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                    int length;
                    if (this.annotation == null) {
                        length = 0;
                    }
                    else {
                        length = this.annotation.length;
                    }
                    final Annotation[] annotation = new Annotation[repeatedFieldArrayLength + length];
                    int i = length;
                    if (length != 0) {
                        System.arraycopy(this.annotation, 0, annotation, 0, length);
                        i = length;
                    }
                    while (i < annotation.length - 1) {
                        codedInputByteBufferNano.readMessage(annotation[i] = new Annotation());
                        codedInputByteBufferNano.readTag();
                        ++i;
                    }
                    codedInputByteBufferNano.readMessage(annotation[i] = new Annotation());
                    this.annotation = annotation;
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.annotation != null && this.annotation.length > 0) {
                for (int i = 0; i < this.annotation.length; ++i) {
                    final Annotation annotation = this.annotation[i];
                    if (annotation != null) {
                        codedOutputByteBufferNano.writeMessage(1, annotation);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public static final class Annotation extends ExtendableMessageNano<Annotation>
        {
            private static volatile Annotation[] _emptyArray;
            public int begin;
            public int end;
            public int[] path;
            public String sourceFile;
            
            public Annotation() {
                this.clear();
            }
            
            public static Annotation[] emptyArray() {
                if (Annotation._emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (Annotation._emptyArray == null) {
                            Annotation._emptyArray = new Annotation[0];
                        }
                    }
                }
                return Annotation._emptyArray;
            }
            
            public static Annotation parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                return new Annotation().mergeFrom(codedInputByteBufferNano);
            }
            
            public static Annotation parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
                return MessageNano.mergeFrom(new Annotation(), array);
            }
            
            public Annotation clear() {
                this.path = WireFormatNano.EMPTY_INT_ARRAY;
                this.sourceFile = "";
                this.begin = 0;
                this.end = 0;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }
            
            @Override
            protected int computeSerializedSize() {
                int computeSerializedSize;
                final int n = computeSerializedSize = super.computeSerializedSize();
                if (this.path != null) {
                    computeSerializedSize = n;
                    if (this.path.length > 0) {
                        int i = 0;
                        int n2 = 0;
                        while (i < this.path.length) {
                            n2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                            ++i;
                        }
                        computeSerializedSize = n + n2 + 1 + CodedOutputByteBufferNano.computeRawVarint32Size(n2);
                    }
                }
                int n3 = computeSerializedSize;
                if (this.sourceFile != null) {
                    n3 = computeSerializedSize;
                    if (!this.sourceFile.equals("")) {
                        n3 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.sourceFile);
                    }
                }
                int n4 = n3;
                if (this.begin != 0) {
                    n4 = n3 + CodedOutputByteBufferNano.computeInt32Size(3, this.begin);
                }
                int n5 = n4;
                if (this.end != 0) {
                    n5 = n4 + CodedOutputByteBufferNano.computeInt32Size(4, this.end);
                }
                return n5;
            }
            
            @Override
            public Annotation mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    final int tag = codedInputByteBufferNano.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag != 8) {
                        if (tag != 10) {
                            if (tag != 18) {
                                if (tag != 24) {
                                    if (tag != 32) {
                                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                            return this;
                                        }
                                        continue;
                                    }
                                    else {
                                        this.end = codedInputByteBufferNano.readInt32();
                                    }
                                }
                                else {
                                    this.begin = codedInputByteBufferNano.readInt32();
                                }
                            }
                            else {
                                this.sourceFile = codedInputByteBufferNano.readString();
                            }
                        }
                        else {
                            final int pushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                            final int position = codedInputByteBufferNano.getPosition();
                            int n = 0;
                            while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                                codedInputByteBufferNano.readInt32();
                                ++n;
                            }
                            codedInputByteBufferNano.rewindToPosition(position);
                            int length;
                            if (this.path == null) {
                                length = 0;
                            }
                            else {
                                length = this.path.length;
                            }
                            final int[] path = new int[n + length];
                            int i = length;
                            if (length != 0) {
                                System.arraycopy(this.path, 0, path, 0, length);
                                i = length;
                            }
                            while (i < path.length) {
                                path[i] = codedInputByteBufferNano.readInt32();
                                ++i;
                            }
                            this.path = path;
                            codedInputByteBufferNano.popLimit(pushLimit);
                        }
                    }
                    else {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 8);
                        int length2;
                        if (this.path == null) {
                            length2 = 0;
                        }
                        else {
                            length2 = this.path.length;
                        }
                        final int[] path2 = new int[repeatedFieldArrayLength + length2];
                        int j = length2;
                        if (length2 != 0) {
                            System.arraycopy(this.path, 0, path2, 0, length2);
                            j = length2;
                        }
                        while (j < path2.length - 1) {
                            path2[j] = codedInputByteBufferNano.readInt32();
                            codedInputByteBufferNano.readTag();
                            ++j;
                        }
                        path2[j] = codedInputByteBufferNano.readInt32();
                        this.path = path2;
                    }
                }
            }
            
            @Override
            public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (this.path != null && this.path.length > 0) {
                    final int n = 0;
                    int i = 0;
                    int n2 = 0;
                    while (i < this.path.length) {
                        n2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                        ++i;
                    }
                    codedOutputByteBufferNano.writeRawVarint32(10);
                    codedOutputByteBufferNano.writeRawVarint32(n2);
                    for (int j = n; j < this.path.length; ++j) {
                        codedOutputByteBufferNano.writeInt32NoTag(this.path[j]);
                    }
                }
                if (this.sourceFile != null && !this.sourceFile.equals("")) {
                    codedOutputByteBufferNano.writeString(2, this.sourceFile);
                }
                if (this.begin != 0) {
                    codedOutputByteBufferNano.writeInt32(3, this.begin);
                }
                if (this.end != 0) {
                    codedOutputByteBufferNano.writeInt32(4, this.end);
                }
                super.writeTo(codedOutputByteBufferNano);
            }
        }
    }
    
    public static final class MessageOptions extends ExtendableMessageNano<MessageOptions>
    {
        private static volatile MessageOptions[] _emptyArray;
        public boolean deprecated;
        public String[] experimentalJavaBuilderInterface;
        public String[] experimentalJavaInterfaceExtends;
        public String[] experimentalJavaMessageInterface;
        public boolean mapEntry;
        public boolean messageSetWireFormat;
        public boolean noStandardDescriptorAccessor;
        public UninterpretedOption[] uninterpretedOption;
        
        public MessageOptions() {
            this.clear();
        }
        
        public static MessageOptions[] emptyArray() {
            if (MessageOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (MessageOptions._emptyArray == null) {
                        MessageOptions._emptyArray = new MessageOptions[0];
                    }
                }
            }
            return MessageOptions._emptyArray;
        }
        
        public static MessageOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new MessageOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static MessageOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new MessageOptions(), array);
        }
        
        public MessageOptions clear() {
            this.experimentalJavaMessageInterface = WireFormatNano.EMPTY_STRING_ARRAY;
            this.experimentalJavaBuilderInterface = WireFormatNano.EMPTY_STRING_ARRAY;
            this.experimentalJavaInterfaceExtends = WireFormatNano.EMPTY_STRING_ARRAY;
            this.messageSetWireFormat = false;
            this.noStandardDescriptorAccessor = false;
            this.deprecated = false;
            this.mapEntry = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.messageSetWireFormat) {
                computeSerializedSize = n + CodedOutputByteBufferNano.computeBoolSize(1, this.messageSetWireFormat);
            }
            int n2 = computeSerializedSize;
            if (this.noStandardDescriptorAccessor) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(2, this.noStandardDescriptorAccessor);
            }
            int n3 = n2;
            if (this.deprecated) {
                n3 = n2 + CodedOutputByteBufferNano.computeBoolSize(3, this.deprecated);
            }
            final String[] experimentalJavaMessageInterface = this.experimentalJavaMessageInterface;
            final int n4 = 0;
            int n5 = n3;
            if (experimentalJavaMessageInterface != null) {
                n5 = n3;
                if (this.experimentalJavaMessageInterface.length > 0) {
                    int i = 0;
                    int n7;
                    int n6 = n7 = 0;
                    while (i < this.experimentalJavaMessageInterface.length) {
                        final String s = this.experimentalJavaMessageInterface[i];
                        int n8 = n6;
                        int n9 = n7;
                        if (s != null) {
                            n9 = n7 + 1;
                            n8 = n6 + CodedOutputByteBufferNano.computeStringSizeNoTag(s);
                        }
                        ++i;
                        n6 = n8;
                        n7 = n9;
                    }
                    n5 = n3 + n6 + n7 * 1;
                }
            }
            int n10 = n5;
            if (this.experimentalJavaBuilderInterface != null) {
                n10 = n5;
                if (this.experimentalJavaBuilderInterface.length > 0) {
                    int j = 0;
                    int n12;
                    int n11 = n12 = 0;
                    while (j < this.experimentalJavaBuilderInterface.length) {
                        final String s2 = this.experimentalJavaBuilderInterface[j];
                        int n13 = n11;
                        int n14 = n12;
                        if (s2 != null) {
                            n14 = n12 + 1;
                            n13 = n11 + CodedOutputByteBufferNano.computeStringSizeNoTag(s2);
                        }
                        ++j;
                        n11 = n13;
                        n12 = n14;
                    }
                    n10 = n5 + n11 + n12 * 1;
                }
            }
            int n15 = n10;
            if (this.experimentalJavaInterfaceExtends != null) {
                n15 = n10;
                if (this.experimentalJavaInterfaceExtends.length > 0) {
                    int k = 0;
                    int n17;
                    int n16 = n17 = 0;
                    while (k < this.experimentalJavaInterfaceExtends.length) {
                        final String s3 = this.experimentalJavaInterfaceExtends[k];
                        int n18 = n16;
                        int n19 = n17;
                        if (s3 != null) {
                            n19 = n17 + 1;
                            n18 = n16 + CodedOutputByteBufferNano.computeStringSizeNoTag(s3);
                        }
                        ++k;
                        n16 = n18;
                        n17 = n19;
                    }
                    n15 = n10 + n16 + 1 * n17;
                }
            }
            int n20 = n15;
            if (this.mapEntry) {
                n20 = n15 + CodedOutputByteBufferNano.computeBoolSize(7, this.mapEntry);
            }
            int n21 = n20;
            if (this.uninterpretedOption != null) {
                n21 = n20;
                if (this.uninterpretedOption.length > 0) {
                    int n22 = n4;
                    while (true) {
                        n21 = n20;
                        if (n22 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n22];
                        int n23 = n20;
                        if (uninterpretedOption != null) {
                            n23 = n20 + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n22;
                        n20 = n23;
                    }
                }
            }
            return n21;
        }
        
        @Override
        public MessageOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 8) {
                    if (tag != 16) {
                        if (tag != 24) {
                            if (tag != 34) {
                                if (tag != 42) {
                                    if (tag != 50) {
                                        if (tag != 56) {
                                            if (tag != 7994) {
                                                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                                    return this;
                                                }
                                                continue;
                                            }
                                            else {
                                                final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                                                int length;
                                                if (this.uninterpretedOption == null) {
                                                    length = 0;
                                                }
                                                else {
                                                    length = this.uninterpretedOption.length;
                                                }
                                                final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                                                int i = length;
                                                if (length != 0) {
                                                    System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                                                    i = length;
                                                }
                                                while (i < uninterpretedOption.length - 1) {
                                                    codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                                                    codedInputByteBufferNano.readTag();
                                                    ++i;
                                                }
                                                codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                                                this.uninterpretedOption = uninterpretedOption;
                                            }
                                        }
                                        else {
                                            this.mapEntry = codedInputByteBufferNano.readBool();
                                        }
                                    }
                                    else {
                                        final int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 50);
                                        int length2;
                                        if (this.experimentalJavaInterfaceExtends == null) {
                                            length2 = 0;
                                        }
                                        else {
                                            length2 = this.experimentalJavaInterfaceExtends.length;
                                        }
                                        final String[] experimentalJavaInterfaceExtends = new String[repeatedFieldArrayLength2 + length2];
                                        int j = length2;
                                        if (length2 != 0) {
                                            System.arraycopy(this.experimentalJavaInterfaceExtends, 0, experimentalJavaInterfaceExtends, 0, length2);
                                            j = length2;
                                        }
                                        while (j < experimentalJavaInterfaceExtends.length - 1) {
                                            experimentalJavaInterfaceExtends[j] = codedInputByteBufferNano.readString();
                                            codedInputByteBufferNano.readTag();
                                            ++j;
                                        }
                                        experimentalJavaInterfaceExtends[j] = codedInputByteBufferNano.readString();
                                        this.experimentalJavaInterfaceExtends = experimentalJavaInterfaceExtends;
                                    }
                                }
                                else {
                                    final int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 42);
                                    int length3;
                                    if (this.experimentalJavaBuilderInterface == null) {
                                        length3 = 0;
                                    }
                                    else {
                                        length3 = this.experimentalJavaBuilderInterface.length;
                                    }
                                    final String[] experimentalJavaBuilderInterface = new String[repeatedFieldArrayLength3 + length3];
                                    int k = length3;
                                    if (length3 != 0) {
                                        System.arraycopy(this.experimentalJavaBuilderInterface, 0, experimentalJavaBuilderInterface, 0, length3);
                                        k = length3;
                                    }
                                    while (k < experimentalJavaBuilderInterface.length - 1) {
                                        experimentalJavaBuilderInterface[k] = codedInputByteBufferNano.readString();
                                        codedInputByteBufferNano.readTag();
                                        ++k;
                                    }
                                    experimentalJavaBuilderInterface[k] = codedInputByteBufferNano.readString();
                                    this.experimentalJavaBuilderInterface = experimentalJavaBuilderInterface;
                                }
                            }
                            else {
                                final int repeatedFieldArrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                                int length4;
                                if (this.experimentalJavaMessageInterface == null) {
                                    length4 = 0;
                                }
                                else {
                                    length4 = this.experimentalJavaMessageInterface.length;
                                }
                                final String[] experimentalJavaMessageInterface = new String[repeatedFieldArrayLength4 + length4];
                                int l = length4;
                                if (length4 != 0) {
                                    System.arraycopy(this.experimentalJavaMessageInterface, 0, experimentalJavaMessageInterface, 0, length4);
                                    l = length4;
                                }
                                while (l < experimentalJavaMessageInterface.length - 1) {
                                    experimentalJavaMessageInterface[l] = codedInputByteBufferNano.readString();
                                    codedInputByteBufferNano.readTag();
                                    ++l;
                                }
                                experimentalJavaMessageInterface[l] = codedInputByteBufferNano.readString();
                                this.experimentalJavaMessageInterface = experimentalJavaMessageInterface;
                            }
                        }
                        else {
                            this.deprecated = codedInputByteBufferNano.readBool();
                        }
                    }
                    else {
                        this.noStandardDescriptorAccessor = codedInputByteBufferNano.readBool();
                    }
                }
                else {
                    this.messageSetWireFormat = codedInputByteBufferNano.readBool();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.messageSetWireFormat) {
                codedOutputByteBufferNano.writeBool(1, this.messageSetWireFormat);
            }
            if (this.noStandardDescriptorAccessor) {
                codedOutputByteBufferNano.writeBool(2, this.noStandardDescriptorAccessor);
            }
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(3, this.deprecated);
            }
            final String[] experimentalJavaMessageInterface = this.experimentalJavaMessageInterface;
            final int n = 0;
            if (experimentalJavaMessageInterface != null && this.experimentalJavaMessageInterface.length > 0) {
                for (int i = 0; i < this.experimentalJavaMessageInterface.length; ++i) {
                    final String s = this.experimentalJavaMessageInterface[i];
                    if (s != null) {
                        codedOutputByteBufferNano.writeString(4, s);
                    }
                }
            }
            if (this.experimentalJavaBuilderInterface != null && this.experimentalJavaBuilderInterface.length > 0) {
                for (int j = 0; j < this.experimentalJavaBuilderInterface.length; ++j) {
                    final String s2 = this.experimentalJavaBuilderInterface[j];
                    if (s2 != null) {
                        codedOutputByteBufferNano.writeString(5, s2);
                    }
                }
            }
            if (this.experimentalJavaInterfaceExtends != null && this.experimentalJavaInterfaceExtends.length > 0) {
                for (int k = 0; k < this.experimentalJavaInterfaceExtends.length; ++k) {
                    final String s3 = this.experimentalJavaInterfaceExtends[k];
                    if (s3 != null) {
                        codedOutputByteBufferNano.writeString(6, s3);
                    }
                }
            }
            if (this.mapEntry) {
                codedOutputByteBufferNano.writeBool(7, this.mapEntry);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int l = n; l < this.uninterpretedOption.length; ++l) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[l];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class MethodDescriptorProto extends ExtendableMessageNano<MethodDescriptorProto>
    {
        private static volatile MethodDescriptorProto[] _emptyArray;
        public boolean clientStreaming;
        public String inputType;
        public String name;
        public MethodOptions options;
        public String outputType;
        public boolean serverStreaming;
        
        public MethodDescriptorProto() {
            this.clear();
        }
        
        public static MethodDescriptorProto[] emptyArray() {
            if (MethodDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (MethodDescriptorProto._emptyArray == null) {
                        MethodDescriptorProto._emptyArray = new MethodDescriptorProto[0];
                    }
                }
            }
            return MethodDescriptorProto._emptyArray;
        }
        
        public static MethodDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new MethodDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static MethodDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new MethodDescriptorProto(), array);
        }
        
        public MethodDescriptorProto clear() {
            this.name = "";
            this.inputType = "";
            this.outputType = "";
            this.options = null;
            this.clientStreaming = false;
            this.serverStreaming = false;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            int n2 = computeSerializedSize;
            if (this.inputType != null) {
                n2 = computeSerializedSize;
                if (!this.inputType.equals("")) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.inputType);
                }
            }
            int n3 = n2;
            if (this.outputType != null) {
                n3 = n2;
                if (!this.outputType.equals("")) {
                    n3 = n2 + CodedOutputByteBufferNano.computeStringSize(3, this.outputType);
                }
            }
            int n4 = n3;
            if (this.options != null) {
                n4 = n3 + CodedOutputByteBufferNano.computeMessageSize(4, this.options);
            }
            int n5 = n4;
            if (this.clientStreaming) {
                n5 = n4 + CodedOutputByteBufferNano.computeBoolSize(5, this.clientStreaming);
            }
            int n6 = n5;
            if (this.serverStreaming) {
                n6 = n5 + CodedOutputByteBufferNano.computeBoolSize(6, this.serverStreaming);
            }
            return n6;
        }
        
        @Override
        public MethodDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (tag != 18) {
                        if (tag != 26) {
                            if (tag != 34) {
                                if (tag != 40) {
                                    if (tag != 48) {
                                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                            return this;
                                        }
                                        continue;
                                    }
                                    else {
                                        this.serverStreaming = codedInputByteBufferNano.readBool();
                                    }
                                }
                                else {
                                    this.clientStreaming = codedInputByteBufferNano.readBool();
                                }
                            }
                            else {
                                if (this.options == null) {
                                    this.options = new MethodOptions();
                                }
                                codedInputByteBufferNano.readMessage(this.options);
                            }
                        }
                        else {
                            this.outputType = codedInputByteBufferNano.readString();
                        }
                    }
                    else {
                        this.inputType = codedInputByteBufferNano.readString();
                    }
                }
                else {
                    this.name = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            if (this.inputType != null && !this.inputType.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.inputType);
            }
            if (this.outputType != null && !this.outputType.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.outputType);
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(4, this.options);
            }
            if (this.clientStreaming) {
                codedOutputByteBufferNano.writeBool(5, this.clientStreaming);
            }
            if (this.serverStreaming) {
                codedOutputByteBufferNano.writeBool(6, this.serverStreaming);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class MethodOptions extends ExtendableMessageNano<MethodOptions>
    {
        private static volatile MethodOptions[] _emptyArray;
        public int clientLogging;
        public boolean clientStreaming;
        public double deadline;
        public boolean deprecated;
        public boolean duplicateSuppression;
        public boolean endUserCredsRequested;
        public boolean failFast;
        public boolean goLegacyChannelApi;
        @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
        public int idempotencyLevel;
        public long legacyClientInitialTokens;
        public String legacyResultType;
        public long legacyServerInitialTokens;
        public String legacyStreamType;
        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public int legacyTokenUnit;
        @NanoEnumValue(legacy = false, value = LogLevel.class)
        public int logLevel;
        @NanoEnumValue(legacy = false, value = Protocol.class)
        public int protocol;
        @NanoEnumValue(legacy = false, value = Format.class)
        public int requestFormat;
        @NanoEnumValue(legacy = false, value = Format.class)
        public int responseFormat;
        public String securityLabel;
        @NanoEnumValue(legacy = false, value = SecurityLevel.class)
        public int securityLevel;
        public int serverLogging;
        public boolean serverStreaming;
        public String streamType;
        public UninterpretedOption[] uninterpretedOption;
        
        public MethodOptions() {
            this.clear();
        }
        
        @NanoEnumValue(legacy = false, value = Format.class)
        public static int checkFormatOrThrow(final int i) {
            if (i >= 0 && i <= 1) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(38);
            sb.append(i);
            sb.append(" is not a valid enum Format");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = Format.class)
        public static int[] checkFormatOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkFormatOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
        public static int checkIdempotencyLevelOrThrow(final int i) {
            if (i >= 0 && i <= 2) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(48);
            sb.append(i);
            sb.append(" is not a valid enum IdempotencyLevel");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
        public static int[] checkIdempotencyLevelOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkIdempotencyLevelOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = LogLevel.class)
        public static int checkLogLevelOrThrow(final int i) {
            if (i >= 0 && i <= 4) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(40);
            sb.append(i);
            sb.append(" is not a valid enum LogLevel");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = LogLevel.class)
        public static int[] checkLogLevelOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkLogLevelOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = Protocol.class)
        public static int checkProtocolOrThrow(final int i) {
            if (i >= 0 && i <= 1) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(40);
            sb.append(i);
            sb.append(" is not a valid enum Protocol");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = Protocol.class)
        public static int[] checkProtocolOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkProtocolOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = SecurityLevel.class)
        public static int checkSecurityLevelOrThrow(final int i) {
            if (i >= 0 && i <= 3) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(45);
            sb.append(i);
            sb.append(" is not a valid enum SecurityLevel");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = SecurityLevel.class)
        public static int[] checkSecurityLevelOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkSecurityLevelOrThrow(array[i]);
            }
            return array;
        }
        
        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int checkTokenUnitOrThrow(final int i) {
            if (i >= 0 && i <= 1) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(41);
            sb.append(i);
            sb.append(" is not a valid enum TokenUnit");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int[] checkTokenUnitOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkTokenUnitOrThrow(array[i]);
            }
            return array;
        }
        
        public static MethodOptions[] emptyArray() {
            if (MethodOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (MethodOptions._emptyArray == null) {
                        MethodOptions._emptyArray = new MethodOptions[0];
                    }
                }
            }
            return MethodOptions._emptyArray;
        }
        
        public static MethodOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new MethodOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static MethodOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new MethodOptions(), array);
        }
        
        public MethodOptions clear() {
            this.protocol = 0;
            this.deadline = -1.0;
            this.duplicateSuppression = false;
            this.failFast = false;
            this.endUserCredsRequested = false;
            this.clientLogging = 256;
            this.serverLogging = 256;
            this.securityLevel = 0;
            this.responseFormat = 0;
            this.requestFormat = 0;
            this.streamType = "";
            this.securityLabel = "";
            this.clientStreaming = false;
            this.serverStreaming = false;
            this.legacyStreamType = "";
            this.legacyResultType = "";
            this.goLegacyChannelApi = false;
            this.legacyClientInitialTokens = -1L;
            this.legacyServerInitialTokens = -1L;
            this.legacyTokenUnit = 1;
            this.logLevel = 2;
            this.deprecated = false;
            this.idempotencyLevel = 0;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.protocol != 0) {
                computeSerializedSize = n + CodedOutputByteBufferNano.computeInt32Size(7, this.protocol);
            }
            int n2 = computeSerializedSize;
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0)) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeDoubleSize(8, this.deadline);
            }
            int n3 = n2;
            if (this.duplicateSuppression) {
                n3 = n2 + CodedOutputByteBufferNano.computeBoolSize(9, this.duplicateSuppression);
            }
            int n4 = n3;
            if (this.failFast) {
                n4 = n3 + CodedOutputByteBufferNano.computeBoolSize(10, this.failFast);
            }
            int n5 = n4;
            if (this.clientLogging != 256) {
                n5 = n4 + CodedOutputByteBufferNano.computeSInt32Size(11, this.clientLogging);
            }
            int n6 = n5;
            if (this.serverLogging != 256) {
                n6 = n5 + CodedOutputByteBufferNano.computeSInt32Size(12, this.serverLogging);
            }
            int n7 = n6;
            if (this.securityLevel != 0) {
                n7 = n6 + CodedOutputByteBufferNano.computeInt32Size(13, this.securityLevel);
            }
            int n8 = n7;
            if (this.responseFormat != 0) {
                n8 = n7 + CodedOutputByteBufferNano.computeInt32Size(15, this.responseFormat);
            }
            int n9 = n8;
            if (this.requestFormat != 0) {
                n9 = n8 + CodedOutputByteBufferNano.computeInt32Size(17, this.requestFormat);
            }
            int n10 = n9;
            if (this.streamType != null) {
                n10 = n9;
                if (!this.streamType.equals("")) {
                    n10 = n9 + CodedOutputByteBufferNano.computeStringSize(18, this.streamType);
                }
            }
            int n11 = n10;
            if (this.securityLabel != null) {
                n11 = n10;
                if (!this.securityLabel.equals("")) {
                    n11 = n10 + CodedOutputByteBufferNano.computeStringSize(19, this.securityLabel);
                }
            }
            int n12 = n11;
            if (this.clientStreaming) {
                n12 = n11 + CodedOutputByteBufferNano.computeBoolSize(20, this.clientStreaming);
            }
            int n13 = n12;
            if (this.serverStreaming) {
                n13 = n12 + CodedOutputByteBufferNano.computeBoolSize(21, this.serverStreaming);
            }
            int n14 = n13;
            if (this.legacyStreamType != null) {
                n14 = n13;
                if (!this.legacyStreamType.equals("")) {
                    n14 = n13 + CodedOutputByteBufferNano.computeStringSize(22, this.legacyStreamType);
                }
            }
            int n15 = n14;
            if (this.legacyResultType != null) {
                n15 = n14;
                if (!this.legacyResultType.equals("")) {
                    n15 = n14 + CodedOutputByteBufferNano.computeStringSize(23, this.legacyResultType);
                }
            }
            int n16 = n15;
            if (this.legacyClientInitialTokens != -1L) {
                n16 = n15 + CodedOutputByteBufferNano.computeInt64Size(24, this.legacyClientInitialTokens);
            }
            int n17 = n16;
            if (this.legacyServerInitialTokens != -1L) {
                n17 = n16 + CodedOutputByteBufferNano.computeInt64Size(25, this.legacyServerInitialTokens);
            }
            int n18 = n17;
            if (this.endUserCredsRequested) {
                n18 = n17 + CodedOutputByteBufferNano.computeBoolSize(26, this.endUserCredsRequested);
            }
            int n19 = n18;
            if (this.logLevel != 2) {
                n19 = n18 + CodedOutputByteBufferNano.computeInt32Size(27, this.logLevel);
            }
            int n20 = n19;
            if (this.legacyTokenUnit != 1) {
                n20 = n19 + CodedOutputByteBufferNano.computeInt32Size(28, this.legacyTokenUnit);
            }
            int n21 = n20;
            if (this.goLegacyChannelApi) {
                n21 = n20 + CodedOutputByteBufferNano.computeBoolSize(29, this.goLegacyChannelApi);
            }
            int n22 = n21;
            if (this.deprecated) {
                n22 = n21 + CodedOutputByteBufferNano.computeBoolSize(33, this.deprecated);
            }
            int n23 = n22;
            if (this.idempotencyLevel != 0) {
                n23 = n22 + CodedOutputByteBufferNano.computeInt32Size(34, this.idempotencyLevel);
            }
            int n24 = n23;
            if (this.uninterpretedOption != null) {
                n24 = n23;
                if (this.uninterpretedOption.length > 0) {
                    int n25 = 0;
                    while (true) {
                        n24 = n23;
                        if (n25 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n25];
                        int n26 = n23;
                        if (uninterpretedOption != null) {
                            n26 = n23 + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n25;
                        n23 = n26;
                    }
                }
            }
            return n24;
        }
        
        @Override
        public MethodOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    default: {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    case 7994: {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                        int length;
                        if (this.uninterpretedOption == null) {
                            length = 0;
                        }
                        else {
                            length = this.uninterpretedOption.length;
                        }
                        final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                            i = length;
                        }
                        while (i < uninterpretedOption.length - 1) {
                            codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                            codedInputByteBufferNano.readTag();
                            ++i;
                        }
                        codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                        this.uninterpretedOption = uninterpretedOption;
                        continue;
                    }
                    case 272: {
                        final int position = codedInputByteBufferNano.getPosition();
                        try {
                            this.idempotencyLevel = checkIdempotencyLevelOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex) {
                            codedInputByteBufferNano.rewindToPosition(position);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 264: {
                        this.deprecated = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 232: {
                        this.goLegacyChannelApi = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 224: {
                        final int position2 = codedInputByteBufferNano.getPosition();
                        try {
                            this.legacyTokenUnit = checkTokenUnitOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex2) {
                            codedInputByteBufferNano.rewindToPosition(position2);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 216: {
                        final int position3 = codedInputByteBufferNano.getPosition();
                        try {
                            this.logLevel = checkLogLevelOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex3) {
                            codedInputByteBufferNano.rewindToPosition(position3);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 208: {
                        this.endUserCredsRequested = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 200: {
                        this.legacyServerInitialTokens = codedInputByteBufferNano.readInt64();
                        continue;
                    }
                    case 192: {
                        this.legacyClientInitialTokens = codedInputByteBufferNano.readInt64();
                        continue;
                    }
                    case 186: {
                        this.legacyResultType = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 178: {
                        this.legacyStreamType = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 168: {
                        this.serverStreaming = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 160: {
                        this.clientStreaming = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 154: {
                        this.securityLabel = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 146: {
                        this.streamType = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 136: {
                        final int position4 = codedInputByteBufferNano.getPosition();
                        try {
                            this.requestFormat = checkFormatOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex4) {
                            codedInputByteBufferNano.rewindToPosition(position4);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 120: {
                        final int position5 = codedInputByteBufferNano.getPosition();
                        try {
                            this.responseFormat = checkFormatOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex5) {
                            codedInputByteBufferNano.rewindToPosition(position5);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 104: {
                        final int position6 = codedInputByteBufferNano.getPosition();
                        try {
                            this.securityLevel = checkSecurityLevelOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex6) {
                            codedInputByteBufferNano.rewindToPosition(position6);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 96: {
                        this.serverLogging = codedInputByteBufferNano.readSInt32();
                        continue;
                    }
                    case 88: {
                        this.clientLogging = codedInputByteBufferNano.readSInt32();
                        continue;
                    }
                    case 80: {
                        this.failFast = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 72: {
                        this.duplicateSuppression = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 65: {
                        this.deadline = codedInputByteBufferNano.readDouble();
                        continue;
                    }
                    case 56: {
                        final int position7 = codedInputByteBufferNano.getPosition();
                        try {
                            this.protocol = checkProtocolOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex7) {
                            codedInputByteBufferNano.rewindToPosition(position7);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.protocol != 0) {
                codedOutputByteBufferNano.writeInt32(7, this.protocol);
            }
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0)) {
                codedOutputByteBufferNano.writeDouble(8, this.deadline);
            }
            if (this.duplicateSuppression) {
                codedOutputByteBufferNano.writeBool(9, this.duplicateSuppression);
            }
            if (this.failFast) {
                codedOutputByteBufferNano.writeBool(10, this.failFast);
            }
            if (this.clientLogging != 256) {
                codedOutputByteBufferNano.writeSInt32(11, this.clientLogging);
            }
            if (this.serverLogging != 256) {
                codedOutputByteBufferNano.writeSInt32(12, this.serverLogging);
            }
            if (this.securityLevel != 0) {
                codedOutputByteBufferNano.writeInt32(13, this.securityLevel);
            }
            if (this.responseFormat != 0) {
                codedOutputByteBufferNano.writeInt32(15, this.responseFormat);
            }
            if (this.requestFormat != 0) {
                codedOutputByteBufferNano.writeInt32(17, this.requestFormat);
            }
            if (this.streamType != null && !this.streamType.equals("")) {
                codedOutputByteBufferNano.writeString(18, this.streamType);
            }
            if (this.securityLabel != null && !this.securityLabel.equals("")) {
                codedOutputByteBufferNano.writeString(19, this.securityLabel);
            }
            if (this.clientStreaming) {
                codedOutputByteBufferNano.writeBool(20, this.clientStreaming);
            }
            if (this.serverStreaming) {
                codedOutputByteBufferNano.writeBool(21, this.serverStreaming);
            }
            if (this.legacyStreamType != null && !this.legacyStreamType.equals("")) {
                codedOutputByteBufferNano.writeString(22, this.legacyStreamType);
            }
            if (this.legacyResultType != null && !this.legacyResultType.equals("")) {
                codedOutputByteBufferNano.writeString(23, this.legacyResultType);
            }
            if (this.legacyClientInitialTokens != -1L) {
                codedOutputByteBufferNano.writeInt64(24, this.legacyClientInitialTokens);
            }
            if (this.legacyServerInitialTokens != -1L) {
                codedOutputByteBufferNano.writeInt64(25, this.legacyServerInitialTokens);
            }
            if (this.endUserCredsRequested) {
                codedOutputByteBufferNano.writeBool(26, this.endUserCredsRequested);
            }
            if (this.logLevel != 2) {
                codedOutputByteBufferNano.writeInt32(27, this.logLevel);
            }
            if (this.legacyTokenUnit != 1) {
                codedOutputByteBufferNano.writeInt32(28, this.legacyTokenUnit);
            }
            if (this.goLegacyChannelApi) {
                codedOutputByteBufferNano.writeBool(29, this.goLegacyChannelApi);
            }
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(33, this.deprecated);
            }
            if (this.idempotencyLevel != 0) {
                codedOutputByteBufferNano.writeInt32(34, this.idempotencyLevel);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public interface Format
        {
            @NanoEnumValue(legacy = false, value = Format.class)
            public static final int UNCOMPRESSED = 0;
            @NanoEnumValue(legacy = false, value = Format.class)
            public static final int ZIPPY_COMPRESSED = 1;
        }
        
        public interface IdempotencyLevel
        {
            @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
            public static final int IDEMPOTENCY_UNKNOWN = 0;
            @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
            public static final int IDEMPOTENT = 2;
            @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
            public static final int NO_SIDE_EFFECTS = 1;
        }
        
        public interface LogLevel
        {
            @NanoEnumValue(legacy = false, value = LogLevel.class)
            public static final int LOG_HEADER_AND_FILTERED_PAYLOAD = 3;
            @NanoEnumValue(legacy = false, value = LogLevel.class)
            public static final int LOG_HEADER_AND_NON_PRIVATE_PAYLOAD_INTERNAL = 2;
            @NanoEnumValue(legacy = false, value = LogLevel.class)
            public static final int LOG_HEADER_AND_PAYLOAD = 4;
            @NanoEnumValue(legacy = false, value = LogLevel.class)
            public static final int LOG_HEADER_ONLY = 1;
            @NanoEnumValue(legacy = false, value = LogLevel.class)
            public static final int LOG_NONE = 0;
        }
        
        public interface Protocol
        {
            @NanoEnumValue(legacy = false, value = Protocol.class)
            public static final int TCP = 0;
            @NanoEnumValue(legacy = false, value = Protocol.class)
            public static final int UDP = 1;
        }
        
        public interface SecurityLevel
        {
            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int INTEGRITY = 1;
            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int NONE = 0;
            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int PRIVACY_AND_INTEGRITY = 2;
            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int STRONG_PRIVACY_AND_INTEGRITY = 3;
        }
        
        public interface TokenUnit
        {
            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int BYTE = 1;
            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int MESSAGE = 0;
        }
    }
    
    public static final class OneofDescriptorProto extends ExtendableMessageNano<OneofDescriptorProto>
    {
        private static volatile OneofDescriptorProto[] _emptyArray;
        public String name;
        public OneofOptions options;
        
        public OneofDescriptorProto() {
            this.clear();
        }
        
        public static OneofDescriptorProto[] emptyArray() {
            if (OneofDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (OneofDescriptorProto._emptyArray == null) {
                        OneofDescriptorProto._emptyArray = new OneofDescriptorProto[0];
                    }
                }
            }
            return OneofDescriptorProto._emptyArray;
        }
        
        public static OneofDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new OneofDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static OneofDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new OneofDescriptorProto(), array);
        }
        
        public OneofDescriptorProto clear() {
            this.name = "";
            this.options = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            int n2 = computeSerializedSize;
            if (this.options != null) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, this.options);
            }
            return n2;
        }
        
        @Override
        public OneofDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (tag != 18) {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    else {
                        if (this.options == null) {
                            this.options = new OneofOptions();
                        }
                        codedInputByteBufferNano.readMessage(this.options);
                    }
                }
                else {
                    this.name = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(2, this.options);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class OneofOptions extends ExtendableMessageNano<OneofOptions>
    {
        private static volatile OneofOptions[] _emptyArray;
        public UninterpretedOption[] uninterpretedOption;
        
        public OneofOptions() {
            this.clear();
        }
        
        public static OneofOptions[] emptyArray() {
            if (OneofOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (OneofOptions._emptyArray == null) {
                        OneofOptions._emptyArray = new OneofOptions[0];
                    }
                }
            }
            return OneofOptions._emptyArray;
        }
        
        public static OneofOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new OneofOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static OneofOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new OneofOptions(), array);
        }
        
        public OneofOptions clear() {
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            int n = computeSerializedSize = super.computeSerializedSize();
            if (this.uninterpretedOption != null) {
                computeSerializedSize = n;
                if (this.uninterpretedOption.length > 0) {
                    int n2 = 0;
                    while (true) {
                        computeSerializedSize = n;
                        if (n2 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n2];
                        int n3 = n;
                        if (uninterpretedOption != null) {
                            n3 = n + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n2;
                        n = n3;
                    }
                }
            }
            return computeSerializedSize;
        }
        
        @Override
        public OneofOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 7994) {
                    if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                    continue;
                }
                else {
                    final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                    int length;
                    if (this.uninterpretedOption == null) {
                        length = 0;
                    }
                    else {
                        length = this.uninterpretedOption.length;
                    }
                    final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                    int i = length;
                    if (length != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                        i = length;
                    }
                    while (i < uninterpretedOption.length - 1) {
                        codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                        codedInputByteBufferNano.readTag();
                        ++i;
                    }
                    codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                    this.uninterpretedOption = uninterpretedOption;
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class ServiceDescriptorProto extends ExtendableMessageNano<ServiceDescriptorProto>
    {
        private static volatile ServiceDescriptorProto[] _emptyArray;
        public MethodDescriptorProto[] method;
        public String name;
        public ServiceOptions options;
        public StreamDescriptorProto[] stream;
        
        public ServiceDescriptorProto() {
            this.clear();
        }
        
        public static ServiceDescriptorProto[] emptyArray() {
            if (ServiceDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (ServiceDescriptorProto._emptyArray == null) {
                        ServiceDescriptorProto._emptyArray = new ServiceDescriptorProto[0];
                    }
                }
            }
            return ServiceDescriptorProto._emptyArray;
        }
        
        public static ServiceDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new ServiceDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static ServiceDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new ServiceDescriptorProto(), array);
        }
        
        public ServiceDescriptorProto clear() {
            this.name = "";
            this.method = MethodDescriptorProto.emptyArray();
            this.stream = StreamDescriptorProto.emptyArray();
            this.options = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            final MethodDescriptorProto[] method = this.method;
            final int n2 = 0;
            int n3 = computeSerializedSize;
            if (method != null) {
                n3 = computeSerializedSize;
                if (this.method.length > 0) {
                    int n4;
                    for (int i = 0; i < this.method.length; ++i, computeSerializedSize = n4) {
                        final MethodDescriptorProto methodDescriptorProto = this.method[i];
                        n4 = computeSerializedSize;
                        if (methodDescriptorProto != null) {
                            n4 = computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, methodDescriptorProto);
                        }
                    }
                    n3 = computeSerializedSize;
                }
            }
            int n5 = n3;
            if (this.options != null) {
                n5 = n3 + CodedOutputByteBufferNano.computeMessageSize(3, this.options);
            }
            int n6 = n5;
            if (this.stream != null) {
                n6 = n5;
                if (this.stream.length > 0) {
                    int n7 = n2;
                    while (true) {
                        n6 = n5;
                        if (n7 >= this.stream.length) {
                            break;
                        }
                        final StreamDescriptorProto streamDescriptorProto = this.stream[n7];
                        int n8 = n5;
                        if (streamDescriptorProto != null) {
                            n8 = n5 + CodedOutputByteBufferNano.computeMessageSize(4, streamDescriptorProto);
                        }
                        ++n7;
                        n5 = n8;
                    }
                }
            }
            return n6;
        }
        
        @Override
        public ServiceDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (tag != 18) {
                        if (tag != 26) {
                            if (tag != 34) {
                                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                    return this;
                                }
                                continue;
                            }
                            else {
                                final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                                int length;
                                if (this.stream == null) {
                                    length = 0;
                                }
                                else {
                                    length = this.stream.length;
                                }
                                final StreamDescriptorProto[] stream = new StreamDescriptorProto[repeatedFieldArrayLength + length];
                                int i = length;
                                if (length != 0) {
                                    System.arraycopy(this.stream, 0, stream, 0, length);
                                    i = length;
                                }
                                while (i < stream.length - 1) {
                                    codedInputByteBufferNano.readMessage(stream[i] = new StreamDescriptorProto());
                                    codedInputByteBufferNano.readTag();
                                    ++i;
                                }
                                codedInputByteBufferNano.readMessage(stream[i] = new StreamDescriptorProto());
                                this.stream = stream;
                            }
                        }
                        else {
                            if (this.options == null) {
                                this.options = new ServiceOptions();
                            }
                            codedInputByteBufferNano.readMessage(this.options);
                        }
                    }
                    else {
                        final int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                        int length2;
                        if (this.method == null) {
                            length2 = 0;
                        }
                        else {
                            length2 = this.method.length;
                        }
                        final MethodDescriptorProto[] method = new MethodDescriptorProto[repeatedFieldArrayLength2 + length2];
                        int j = length2;
                        if (length2 != 0) {
                            System.arraycopy(this.method, 0, method, 0, length2);
                            j = length2;
                        }
                        while (j < method.length - 1) {
                            codedInputByteBufferNano.readMessage(method[j] = new MethodDescriptorProto());
                            codedInputByteBufferNano.readTag();
                            ++j;
                        }
                        codedInputByteBufferNano.readMessage(method[j] = new MethodDescriptorProto());
                        this.method = method;
                    }
                }
                else {
                    this.name = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            final MethodDescriptorProto[] method = this.method;
            final int n = 0;
            if (method != null && this.method.length > 0) {
                for (int i = 0; i < this.method.length; ++i) {
                    final MethodDescriptorProto methodDescriptorProto = this.method[i];
                    if (methodDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(2, methodDescriptorProto);
                    }
                }
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(3, this.options);
            }
            if (this.stream != null && this.stream.length > 0) {
                for (int j = n; j < this.stream.length; ++j) {
                    final StreamDescriptorProto streamDescriptorProto = this.stream[j];
                    if (streamDescriptorProto != null) {
                        codedOutputByteBufferNano.writeMessage(4, streamDescriptorProto);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class ServiceOptions extends ExtendableMessageNano<ServiceOptions>
    {
        private static volatile ServiceOptions[] _emptyArray;
        public boolean deprecated;
        public double failureDetectionDelay;
        public boolean multicastStub;
        public UninterpretedOption[] uninterpretedOption;
        
        public ServiceOptions() {
            this.clear();
        }
        
        public static ServiceOptions[] emptyArray() {
            if (ServiceOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (ServiceOptions._emptyArray == null) {
                        ServiceOptions._emptyArray = new ServiceOptions[0];
                    }
                }
            }
            return ServiceOptions._emptyArray;
        }
        
        public static ServiceOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new ServiceOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static ServiceOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new ServiceOptions(), array);
        }
        
        public ServiceOptions clear() {
            this.multicastStub = false;
            this.failureDetectionDelay = -1.0;
            this.deprecated = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (Double.doubleToLongBits(this.failureDetectionDelay) != Double.doubleToLongBits(-1.0)) {
                computeSerializedSize = n + CodedOutputByteBufferNano.computeDoubleSize(16, this.failureDetectionDelay);
            }
            int n2 = computeSerializedSize;
            if (this.multicastStub) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(20, this.multicastStub);
            }
            int n3 = n2;
            if (this.deprecated) {
                n3 = n2 + CodedOutputByteBufferNano.computeBoolSize(33, this.deprecated);
            }
            int n4 = n3;
            if (this.uninterpretedOption != null) {
                n4 = n3;
                if (this.uninterpretedOption.length > 0) {
                    int n5 = 0;
                    while (true) {
                        n4 = n3;
                        if (n5 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n5];
                        int n6 = n3;
                        if (uninterpretedOption != null) {
                            n6 = n3 + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n5;
                        n3 = n6;
                    }
                }
            }
            return n4;
        }
        
        @Override
        public ServiceOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 129) {
                    if (tag != 160) {
                        if (tag != 264) {
                            if (tag != 7994) {
                                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                    return this;
                                }
                                continue;
                            }
                            else {
                                final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                                int length;
                                if (this.uninterpretedOption == null) {
                                    length = 0;
                                }
                                else {
                                    length = this.uninterpretedOption.length;
                                }
                                final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                                int i = length;
                                if (length != 0) {
                                    System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                                    i = length;
                                }
                                while (i < uninterpretedOption.length - 1) {
                                    codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                                    codedInputByteBufferNano.readTag();
                                    ++i;
                                }
                                codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                                this.uninterpretedOption = uninterpretedOption;
                            }
                        }
                        else {
                            this.deprecated = codedInputByteBufferNano.readBool();
                        }
                    }
                    else {
                        this.multicastStub = codedInputByteBufferNano.readBool();
                    }
                }
                else {
                    this.failureDetectionDelay = codedInputByteBufferNano.readDouble();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (Double.doubleToLongBits(this.failureDetectionDelay) != Double.doubleToLongBits(-1.0)) {
                codedOutputByteBufferNano.writeDouble(16, this.failureDetectionDelay);
            }
            if (this.multicastStub) {
                codedOutputByteBufferNano.writeBool(20, this.multicastStub);
            }
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(33, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class SourceCodeInfo extends ExtendableMessageNano<SourceCodeInfo>
    {
        private static volatile SourceCodeInfo[] _emptyArray;
        public Location[] location;
        
        public SourceCodeInfo() {
            this.clear();
        }
        
        public static SourceCodeInfo[] emptyArray() {
            if (SourceCodeInfo._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (SourceCodeInfo._emptyArray == null) {
                        SourceCodeInfo._emptyArray = new SourceCodeInfo[0];
                    }
                }
            }
            return SourceCodeInfo._emptyArray;
        }
        
        public static SourceCodeInfo parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new SourceCodeInfo().mergeFrom(codedInputByteBufferNano);
        }
        
        public static SourceCodeInfo parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new SourceCodeInfo(), array);
        }
        
        public SourceCodeInfo clear() {
            this.location = Location.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            int n = computeSerializedSize = super.computeSerializedSize();
            if (this.location != null) {
                computeSerializedSize = n;
                if (this.location.length > 0) {
                    int n2 = 0;
                    while (true) {
                        computeSerializedSize = n;
                        if (n2 >= this.location.length) {
                            break;
                        }
                        final Location location = this.location[n2];
                        int n3 = n;
                        if (location != null) {
                            n3 = n + CodedOutputByteBufferNano.computeMessageSize(1, location);
                        }
                        ++n2;
                        n = n3;
                    }
                }
            }
            return computeSerializedSize;
        }
        
        @Override
        public SourceCodeInfo mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                        return this;
                    }
                    continue;
                }
                else {
                    final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                    int length;
                    if (this.location == null) {
                        length = 0;
                    }
                    else {
                        length = this.location.length;
                    }
                    final Location[] location = new Location[repeatedFieldArrayLength + length];
                    int i = length;
                    if (length != 0) {
                        System.arraycopy(this.location, 0, location, 0, length);
                        i = length;
                    }
                    while (i < location.length - 1) {
                        codedInputByteBufferNano.readMessage(location[i] = new Location());
                        codedInputByteBufferNano.readTag();
                        ++i;
                    }
                    codedInputByteBufferNano.readMessage(location[i] = new Location());
                    this.location = location;
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.location != null && this.location.length > 0) {
                for (int i = 0; i < this.location.length; ++i) {
                    final Location location = this.location[i];
                    if (location != null) {
                        codedOutputByteBufferNano.writeMessage(1, location);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public static final class Location extends ExtendableMessageNano<Location>
        {
            private static volatile Location[] _emptyArray;
            public String leadingComments;
            public String[] leadingDetachedComments;
            public int[] path;
            public int[] span;
            public String trailingComments;
            
            public Location() {
                this.clear();
            }
            
            public static Location[] emptyArray() {
                if (Location._emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (Location._emptyArray == null) {
                            Location._emptyArray = new Location[0];
                        }
                    }
                }
                return Location._emptyArray;
            }
            
            public static Location parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                return new Location().mergeFrom(codedInputByteBufferNano);
            }
            
            public static Location parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
                return MessageNano.mergeFrom(new Location(), array);
            }
            
            public Location clear() {
                this.path = WireFormatNano.EMPTY_INT_ARRAY;
                this.span = WireFormatNano.EMPTY_INT_ARRAY;
                this.leadingComments = "";
                this.trailingComments = "";
                this.leadingDetachedComments = WireFormatNano.EMPTY_STRING_ARRAY;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }
            
            @Override
            protected int computeSerializedSize() {
                final int computeSerializedSize = super.computeSerializedSize();
                final int[] path = this.path;
                final int n = 0;
                int n2 = computeSerializedSize;
                if (path != null) {
                    n2 = computeSerializedSize;
                    if (this.path.length > 0) {
                        int i = 0;
                        int n3 = 0;
                        while (i < this.path.length) {
                            n3 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                            ++i;
                        }
                        n2 = computeSerializedSize + n3 + 1 + CodedOutputByteBufferNano.computeRawVarint32Size(n3);
                    }
                }
                int n4 = n2;
                if (this.span != null) {
                    n4 = n2;
                    if (this.span.length > 0) {
                        int j = 0;
                        int n5 = 0;
                        while (j < this.span.length) {
                            n5 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.span[j]);
                            ++j;
                        }
                        n4 = n2 + n5 + 1 + CodedOutputByteBufferNano.computeRawVarint32Size(n5);
                    }
                }
                int n6 = n4;
                if (this.leadingComments != null) {
                    n6 = n4;
                    if (!this.leadingComments.equals("")) {
                        n6 = n4 + CodedOutputByteBufferNano.computeStringSize(3, this.leadingComments);
                    }
                }
                int n7 = n6;
                if (this.trailingComments != null) {
                    n7 = n6;
                    if (!this.trailingComments.equals("")) {
                        n7 = n6 + CodedOutputByteBufferNano.computeStringSize(4, this.trailingComments);
                    }
                }
                int n8 = n7;
                if (this.leadingDetachedComments != null) {
                    n8 = n7;
                    if (this.leadingDetachedComments.length > 0) {
                        int n9 = 0;
                        int n10 = 0;
                        int n11;
                        int n12;
                        for (int k = n; k < this.leadingDetachedComments.length; ++k, n9 = n11, n10 = n12) {
                            final String s = this.leadingDetachedComments[k];
                            n11 = n9;
                            n12 = n10;
                            if (s != null) {
                                n12 = n10 + 1;
                                n11 = n9 + CodedOutputByteBufferNano.computeStringSizeNoTag(s);
                            }
                        }
                        n8 = n7 + n9 + 1 * n10;
                    }
                }
                return n8;
            }
            
            @Override
            public Location mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    final int tag = codedInputByteBufferNano.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag != 8) {
                        if (tag != 10) {
                            if (tag != 16) {
                                if (tag != 18) {
                                    if (tag != 26) {
                                        if (tag != 34) {
                                            if (tag != 50) {
                                                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                                    return this;
                                                }
                                                continue;
                                            }
                                            else {
                                                final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 50);
                                                int length;
                                                if (this.leadingDetachedComments == null) {
                                                    length = 0;
                                                }
                                                else {
                                                    length = this.leadingDetachedComments.length;
                                                }
                                                final String[] leadingDetachedComments = new String[repeatedFieldArrayLength + length];
                                                int i = length;
                                                if (length != 0) {
                                                    System.arraycopy(this.leadingDetachedComments, 0, leadingDetachedComments, 0, length);
                                                    i = length;
                                                }
                                                while (i < leadingDetachedComments.length - 1) {
                                                    leadingDetachedComments[i] = codedInputByteBufferNano.readString();
                                                    codedInputByteBufferNano.readTag();
                                                    ++i;
                                                }
                                                leadingDetachedComments[i] = codedInputByteBufferNano.readString();
                                                this.leadingDetachedComments = leadingDetachedComments;
                                            }
                                        }
                                        else {
                                            this.trailingComments = codedInputByteBufferNano.readString();
                                        }
                                    }
                                    else {
                                        this.leadingComments = codedInputByteBufferNano.readString();
                                    }
                                }
                                else {
                                    final int pushLimit = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                                    final int position = codedInputByteBufferNano.getPosition();
                                    int n = 0;
                                    while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                                        codedInputByteBufferNano.readInt32();
                                        ++n;
                                    }
                                    codedInputByteBufferNano.rewindToPosition(position);
                                    int length2;
                                    if (this.span == null) {
                                        length2 = 0;
                                    }
                                    else {
                                        length2 = this.span.length;
                                    }
                                    final int[] span = new int[n + length2];
                                    int j = length2;
                                    if (length2 != 0) {
                                        System.arraycopy(this.span, 0, span, 0, length2);
                                        j = length2;
                                    }
                                    while (j < span.length) {
                                        span[j] = codedInputByteBufferNano.readInt32();
                                        ++j;
                                    }
                                    this.span = span;
                                    codedInputByteBufferNano.popLimit(pushLimit);
                                }
                            }
                            else {
                                final int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 16);
                                int length3;
                                if (this.span == null) {
                                    length3 = 0;
                                }
                                else {
                                    length3 = this.span.length;
                                }
                                final int[] span2 = new int[repeatedFieldArrayLength2 + length3];
                                int k = length3;
                                if (length3 != 0) {
                                    System.arraycopy(this.span, 0, span2, 0, length3);
                                    k = length3;
                                }
                                while (k < span2.length - 1) {
                                    span2[k] = codedInputByteBufferNano.readInt32();
                                    codedInputByteBufferNano.readTag();
                                    ++k;
                                }
                                span2[k] = codedInputByteBufferNano.readInt32();
                                this.span = span2;
                            }
                        }
                        else {
                            final int pushLimit2 = codedInputByteBufferNano.pushLimit(codedInputByteBufferNano.readRawVarint32());
                            final int position2 = codedInputByteBufferNano.getPosition();
                            int n2 = 0;
                            while (codedInputByteBufferNano.getBytesUntilLimit() > 0) {
                                codedInputByteBufferNano.readInt32();
                                ++n2;
                            }
                            codedInputByteBufferNano.rewindToPosition(position2);
                            int length4;
                            if (this.path == null) {
                                length4 = 0;
                            }
                            else {
                                length4 = this.path.length;
                            }
                            final int[] path = new int[n2 + length4];
                            int l = length4;
                            if (length4 != 0) {
                                System.arraycopy(this.path, 0, path, 0, length4);
                                l = length4;
                            }
                            while (l < path.length) {
                                path[l] = codedInputByteBufferNano.readInt32();
                                ++l;
                            }
                            this.path = path;
                            codedInputByteBufferNano.popLimit(pushLimit2);
                        }
                    }
                    else {
                        final int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 8);
                        int length5;
                        if (this.path == null) {
                            length5 = 0;
                        }
                        else {
                            length5 = this.path.length;
                        }
                        final int[] path2 = new int[repeatedFieldArrayLength3 + length5];
                        int n3 = length5;
                        if (length5 != 0) {
                            System.arraycopy(this.path, 0, path2, 0, length5);
                            n3 = length5;
                        }
                        while (n3 < path2.length - 1) {
                            path2[n3] = codedInputByteBufferNano.readInt32();
                            codedInputByteBufferNano.readTag();
                            ++n3;
                        }
                        path2[n3] = codedInputByteBufferNano.readInt32();
                        this.path = path2;
                    }
                }
            }
            
            @Override
            public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                final int[] path = this.path;
                final int n = 0;
                if (path != null && this.path.length > 0) {
                    int i = 0;
                    int n2 = 0;
                    while (i < this.path.length) {
                        n2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                        ++i;
                    }
                    codedOutputByteBufferNano.writeRawVarint32(10);
                    codedOutputByteBufferNano.writeRawVarint32(n2);
                    for (int j = 0; j < this.path.length; ++j) {
                        codedOutputByteBufferNano.writeInt32NoTag(this.path[j]);
                    }
                }
                if (this.span != null && this.span.length > 0) {
                    int k = 0;
                    int n3 = 0;
                    while (k < this.span.length) {
                        n3 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.span[k]);
                        ++k;
                    }
                    codedOutputByteBufferNano.writeRawVarint32(18);
                    codedOutputByteBufferNano.writeRawVarint32(n3);
                    for (int l = 0; l < this.span.length; ++l) {
                        codedOutputByteBufferNano.writeInt32NoTag(this.span[l]);
                    }
                }
                if (this.leadingComments != null && !this.leadingComments.equals("")) {
                    codedOutputByteBufferNano.writeString(3, this.leadingComments);
                }
                if (this.trailingComments != null && !this.trailingComments.equals("")) {
                    codedOutputByteBufferNano.writeString(4, this.trailingComments);
                }
                if (this.leadingDetachedComments != null && this.leadingDetachedComments.length > 0) {
                    for (int n4 = n; n4 < this.leadingDetachedComments.length; ++n4) {
                        final String s = this.leadingDetachedComments[n4];
                        if (s != null) {
                            codedOutputByteBufferNano.writeString(6, s);
                        }
                    }
                }
                super.writeTo(codedOutputByteBufferNano);
            }
        }
    }
    
    public static final class StreamDescriptorProto extends ExtendableMessageNano<StreamDescriptorProto>
    {
        private static volatile StreamDescriptorProto[] _emptyArray;
        public String clientMessageType;
        public String name;
        public StreamOptions options;
        public String serverMessageType;
        
        public StreamDescriptorProto() {
            this.clear();
        }
        
        public static StreamDescriptorProto[] emptyArray() {
            if (StreamDescriptorProto._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (StreamDescriptorProto._emptyArray == null) {
                        StreamDescriptorProto._emptyArray = new StreamDescriptorProto[0];
                    }
                }
            }
            return StreamDescriptorProto._emptyArray;
        }
        
        public static StreamDescriptorProto parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new StreamDescriptorProto().mergeFrom(codedInputByteBufferNano);
        }
        
        public static StreamDescriptorProto parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new StreamDescriptorProto(), array);
        }
        
        public StreamDescriptorProto clear() {
            this.name = "";
            this.clientMessageType = "";
            this.serverMessageType = "";
            this.options = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (!this.name.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
            }
            int n2 = computeSerializedSize;
            if (this.clientMessageType != null) {
                n2 = computeSerializedSize;
                if (!this.clientMessageType.equals("")) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.clientMessageType);
                }
            }
            int n3 = n2;
            if (this.serverMessageType != null) {
                n3 = n2;
                if (!this.serverMessageType.equals("")) {
                    n3 = n2 + CodedOutputByteBufferNano.computeStringSize(3, this.serverMessageType);
                }
            }
            int n4 = n3;
            if (this.options != null) {
                n4 = n3 + CodedOutputByteBufferNano.computeMessageSize(4, this.options);
            }
            return n4;
        }
        
        @Override
        public StreamDescriptorProto mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (tag != 18) {
                        if (tag != 26) {
                            if (tag != 34) {
                                if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                    return this;
                                }
                                continue;
                            }
                            else {
                                if (this.options == null) {
                                    this.options = new StreamOptions();
                                }
                                codedInputByteBufferNano.readMessage(this.options);
                            }
                        }
                        else {
                            this.serverMessageType = codedInputByteBufferNano.readString();
                        }
                    }
                    else {
                        this.clientMessageType = codedInputByteBufferNano.readString();
                    }
                }
                else {
                    this.name = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.name);
            }
            if (this.clientMessageType != null && !this.clientMessageType.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.clientMessageType);
            }
            if (this.serverMessageType != null && !this.serverMessageType.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.serverMessageType);
            }
            if (this.options != null) {
                codedOutputByteBufferNano.writeMessage(4, this.options);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
    }
    
    public static final class StreamOptions extends ExtendableMessageNano<StreamOptions>
    {
        private static volatile StreamOptions[] _emptyArray;
        public long clientInitialTokens;
        public int clientLogging;
        public double deadline;
        public boolean deprecated;
        public boolean endUserCredsRequested;
        public boolean failFast;
        @NanoEnumValue(legacy = false, value = LogLevel.class)
        public int logLevel;
        public String securityLabel;
        @NanoEnumValue(legacy = false, value = SecurityLevel.class)
        public int securityLevel;
        public long serverInitialTokens;
        public int serverLogging;
        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public int tokenUnit;
        public UninterpretedOption[] uninterpretedOption;
        
        public StreamOptions() {
            this.clear();
        }
        
        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int checkTokenUnitOrThrow(final int i) {
            if (i >= 0 && i <= 1) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(41);
            sb.append(i);
            sb.append(" is not a valid enum TokenUnit");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int[] checkTokenUnitOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkTokenUnitOrThrow(array[i]);
            }
            return array;
        }
        
        public static StreamOptions[] emptyArray() {
            if (StreamOptions._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (StreamOptions._emptyArray == null) {
                        StreamOptions._emptyArray = new StreamOptions[0];
                    }
                }
            }
            return StreamOptions._emptyArray;
        }
        
        public static StreamOptions parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new StreamOptions().mergeFrom(codedInputByteBufferNano);
        }
        
        public static StreamOptions parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new StreamOptions(), array);
        }
        
        public StreamOptions clear() {
            this.clientInitialTokens = -1L;
            this.serverInitialTokens = -1L;
            this.tokenUnit = 0;
            this.securityLevel = 0;
            this.securityLabel = "";
            this.clientLogging = 256;
            this.serverLogging = 256;
            this.deadline = -1.0;
            this.failFast = false;
            this.endUserCredsRequested = false;
            this.logLevel = 2;
            this.deprecated = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.clientInitialTokens != -1L) {
                computeSerializedSize = n + CodedOutputByteBufferNano.computeInt64Size(1, this.clientInitialTokens);
            }
            int n2 = computeSerializedSize;
            if (this.serverInitialTokens != -1L) {
                n2 = computeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(2, this.serverInitialTokens);
            }
            int n3 = n2;
            if (this.tokenUnit != 0) {
                n3 = n2 + CodedOutputByteBufferNano.computeInt32Size(3, this.tokenUnit);
            }
            int n4 = n3;
            if (this.securityLevel != 0) {
                n4 = n3 + CodedOutputByteBufferNano.computeInt32Size(4, this.securityLevel);
            }
            int n5 = n4;
            if (this.securityLabel != null) {
                n5 = n4;
                if (!this.securityLabel.equals("")) {
                    n5 = n4 + CodedOutputByteBufferNano.computeStringSize(5, this.securityLabel);
                }
            }
            int n6 = n5;
            if (this.clientLogging != 256) {
                n6 = n5 + CodedOutputByteBufferNano.computeInt32Size(6, this.clientLogging);
            }
            int n7 = n6;
            if (this.serverLogging != 256) {
                n7 = n6 + CodedOutputByteBufferNano.computeInt32Size(7, this.serverLogging);
            }
            int n8 = n7;
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0)) {
                n8 = n7 + CodedOutputByteBufferNano.computeDoubleSize(8, this.deadline);
            }
            int n9 = n8;
            if (this.failFast) {
                n9 = n8 + CodedOutputByteBufferNano.computeBoolSize(9, this.failFast);
            }
            int n10 = n9;
            if (this.endUserCredsRequested) {
                n10 = n9 + CodedOutputByteBufferNano.computeBoolSize(10, this.endUserCredsRequested);
            }
            int n11 = n10;
            if (this.logLevel != 2) {
                n11 = n10 + CodedOutputByteBufferNano.computeInt32Size(11, this.logLevel);
            }
            int n12 = n11;
            if (this.deprecated) {
                n12 = n11 + CodedOutputByteBufferNano.computeBoolSize(33, this.deprecated);
            }
            int n13 = n12;
            if (this.uninterpretedOption != null) {
                n13 = n12;
                if (this.uninterpretedOption.length > 0) {
                    int n14 = 0;
                    while (true) {
                        n13 = n12;
                        if (n14 >= this.uninterpretedOption.length) {
                            break;
                        }
                        final UninterpretedOption uninterpretedOption = this.uninterpretedOption[n14];
                        int n15 = n12;
                        if (uninterpretedOption != null) {
                            n15 = n12 + CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                        }
                        ++n14;
                        n12 = n15;
                    }
                }
            }
            return n13;
        }
        
        @Override
        public StreamOptions mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                switch (tag) {
                    default: {
                        if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                            return this;
                        }
                        continue;
                    }
                    case 7994: {
                        final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 7994);
                        int length;
                        if (this.uninterpretedOption == null) {
                            length = 0;
                        }
                        else {
                            length = this.uninterpretedOption.length;
                        }
                        final UninterpretedOption[] uninterpretedOption = new UninterpretedOption[repeatedFieldArrayLength + length];
                        int i = length;
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOption, 0, length);
                            i = length;
                        }
                        while (i < uninterpretedOption.length - 1) {
                            codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                            codedInputByteBufferNano.readTag();
                            ++i;
                        }
                        codedInputByteBufferNano.readMessage(uninterpretedOption[i] = new UninterpretedOption());
                        this.uninterpretedOption = uninterpretedOption;
                        continue;
                    }
                    case 264: {
                        this.deprecated = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 88: {
                        final int position = codedInputByteBufferNano.getPosition();
                        try {
                            this.logLevel = MethodOptions.checkLogLevelOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex) {
                            codedInputByteBufferNano.rewindToPosition(position);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 80: {
                        this.endUserCredsRequested = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 72: {
                        this.failFast = codedInputByteBufferNano.readBool();
                        continue;
                    }
                    case 65: {
                        this.deadline = codedInputByteBufferNano.readDouble();
                        continue;
                    }
                    case 56: {
                        this.serverLogging = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 48: {
                        this.clientLogging = codedInputByteBufferNano.readInt32();
                        continue;
                    }
                    case 42: {
                        this.securityLabel = codedInputByteBufferNano.readString();
                        continue;
                    }
                    case 32: {
                        final int position2 = codedInputByteBufferNano.getPosition();
                        try {
                            this.securityLevel = MethodOptions.checkSecurityLevelOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex2) {
                            codedInputByteBufferNano.rewindToPosition(position2);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 24: {
                        final int position3 = codedInputByteBufferNano.getPosition();
                        try {
                            this.tokenUnit = checkTokenUnitOrThrow(codedInputByteBufferNano.readInt32());
                        }
                        catch (final IllegalArgumentException ex3) {
                            codedInputByteBufferNano.rewindToPosition(position3);
                            this.storeUnknownField(codedInputByteBufferNano, tag);
                        }
                        continue;
                    }
                    case 16: {
                        this.serverInitialTokens = codedInputByteBufferNano.readInt64();
                        continue;
                    }
                    case 8: {
                        this.clientInitialTokens = codedInputByteBufferNano.readInt64();
                        continue;
                    }
                    case 0: {
                        return this;
                    }
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.clientInitialTokens != -1L) {
                codedOutputByteBufferNano.writeInt64(1, this.clientInitialTokens);
            }
            if (this.serverInitialTokens != -1L) {
                codedOutputByteBufferNano.writeInt64(2, this.serverInitialTokens);
            }
            if (this.tokenUnit != 0) {
                codedOutputByteBufferNano.writeInt32(3, this.tokenUnit);
            }
            if (this.securityLevel != 0) {
                codedOutputByteBufferNano.writeInt32(4, this.securityLevel);
            }
            if (this.securityLabel != null && !this.securityLabel.equals("")) {
                codedOutputByteBufferNano.writeString(5, this.securityLabel);
            }
            if (this.clientLogging != 256) {
                codedOutputByteBufferNano.writeInt32(6, this.clientLogging);
            }
            if (this.serverLogging != 256) {
                codedOutputByteBufferNano.writeInt32(7, this.serverLogging);
            }
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0)) {
                codedOutputByteBufferNano.writeDouble(8, this.deadline);
            }
            if (this.failFast) {
                codedOutputByteBufferNano.writeBool(9, this.failFast);
            }
            if (this.endUserCredsRequested) {
                codedOutputByteBufferNano.writeBool(10, this.endUserCredsRequested);
            }
            if (this.logLevel != 2) {
                codedOutputByteBufferNano.writeInt32(11, this.logLevel);
            }
            if (this.deprecated) {
                codedOutputByteBufferNano.writeBool(33, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; ++i) {
                    final UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        codedOutputByteBufferNano.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public interface TokenUnit
        {
            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int BYTE = 1;
            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int MESSAGE = 0;
        }
    }
    
    public static final class UninterpretedOption extends ExtendableMessageNano<UninterpretedOption>
    {
        private static volatile UninterpretedOption[] _emptyArray;
        public String aggregateValue;
        public double doubleValue;
        public String identifierValue;
        public NamePart[] name;
        public long negativeIntValue;
        public long positiveIntValue;
        public byte[] stringValue;
        
        public UninterpretedOption() {
            this.clear();
        }
        
        public static UninterpretedOption[] emptyArray() {
            if (UninterpretedOption._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (UninterpretedOption._emptyArray == null) {
                        UninterpretedOption._emptyArray = new UninterpretedOption[0];
                    }
                }
            }
            return UninterpretedOption._emptyArray;
        }
        
        public static UninterpretedOption parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new UninterpretedOption().mergeFrom(codedInputByteBufferNano);
        }
        
        public static UninterpretedOption parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new UninterpretedOption(), array);
        }
        
        public UninterpretedOption clear() {
            this.name = NamePart.emptyArray();
            this.identifierValue = "";
            this.positiveIntValue = 0L;
            this.negativeIntValue = 0L;
            this.doubleValue = 0.0;
            this.stringValue = WireFormatNano.EMPTY_BYTES;
            this.aggregateValue = "";
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            int n = computeSerializedSize = super.computeSerializedSize();
            if (this.name != null) {
                computeSerializedSize = n;
                if (this.name.length > 0) {
                    int n2 = 0;
                    while (true) {
                        computeSerializedSize = n;
                        if (n2 >= this.name.length) {
                            break;
                        }
                        final NamePart namePart = this.name[n2];
                        int n3 = n;
                        if (namePart != null) {
                            n3 = n + CodedOutputByteBufferNano.computeMessageSize(2, namePart);
                        }
                        ++n2;
                        n = n3;
                    }
                }
            }
            int n4 = computeSerializedSize;
            if (this.identifierValue != null) {
                n4 = computeSerializedSize;
                if (!this.identifierValue.equals("")) {
                    n4 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(3, this.identifierValue);
                }
            }
            int n5 = n4;
            if (this.positiveIntValue != 0L) {
                n5 = n4 + CodedOutputByteBufferNano.computeUInt64Size(4, this.positiveIntValue);
            }
            int n6 = n5;
            if (this.negativeIntValue != 0L) {
                n6 = n5 + CodedOutputByteBufferNano.computeInt64Size(5, this.negativeIntValue);
            }
            int n7 = n6;
            if (Double.doubleToLongBits(this.doubleValue) != Double.doubleToLongBits(0.0)) {
                n7 = n6 + CodedOutputByteBufferNano.computeDoubleSize(6, this.doubleValue);
            }
            int n8 = n7;
            if (!Arrays.equals(this.stringValue, WireFormatNano.EMPTY_BYTES)) {
                n8 = n7 + CodedOutputByteBufferNano.computeBytesSize(7, this.stringValue);
            }
            int n9 = n8;
            if (this.aggregateValue != null) {
                n9 = n8;
                if (!this.aggregateValue.equals("")) {
                    n9 = n8 + CodedOutputByteBufferNano.computeStringSize(8, this.aggregateValue);
                }
            }
            return n9;
        }
        
        @Override
        public UninterpretedOption mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 18) {
                    if (tag != 26) {
                        if (tag != 32) {
                            if (tag != 40) {
                                if (tag != 49) {
                                    if (tag != 58) {
                                        if (tag != 66) {
                                            if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                                return this;
                                            }
                                            continue;
                                        }
                                        else {
                                            this.aggregateValue = codedInputByteBufferNano.readString();
                                        }
                                    }
                                    else {
                                        this.stringValue = codedInputByteBufferNano.readBytes();
                                    }
                                }
                                else {
                                    this.doubleValue = codedInputByteBufferNano.readDouble();
                                }
                            }
                            else {
                                this.negativeIntValue = codedInputByteBufferNano.readInt64();
                            }
                        }
                        else {
                            this.positiveIntValue = codedInputByteBufferNano.readUInt64();
                        }
                    }
                    else {
                        this.identifierValue = codedInputByteBufferNano.readString();
                    }
                }
                else {
                    final int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                    int length;
                    if (this.name == null) {
                        length = 0;
                    }
                    else {
                        length = this.name.length;
                    }
                    final NamePart[] name = new NamePart[repeatedFieldArrayLength + length];
                    int i = length;
                    if (length != 0) {
                        System.arraycopy(this.name, 0, name, 0, length);
                        i = length;
                    }
                    while (i < name.length - 1) {
                        codedInputByteBufferNano.readMessage(name[i] = new NamePart());
                        codedInputByteBufferNano.readTag();
                        ++i;
                    }
                    codedInputByteBufferNano.readMessage(name[i] = new NamePart());
                    this.name = name;
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.name != null && this.name.length > 0) {
                for (int i = 0; i < this.name.length; ++i) {
                    final NamePart namePart = this.name[i];
                    if (namePart != null) {
                        codedOutputByteBufferNano.writeMessage(2, namePart);
                    }
                }
            }
            if (this.identifierValue != null && !this.identifierValue.equals("")) {
                codedOutputByteBufferNano.writeString(3, this.identifierValue);
            }
            if (this.positiveIntValue != 0L) {
                codedOutputByteBufferNano.writeUInt64(4, this.positiveIntValue);
            }
            if (this.negativeIntValue != 0L) {
                codedOutputByteBufferNano.writeInt64(5, this.negativeIntValue);
            }
            if (Double.doubleToLongBits(this.doubleValue) != Double.doubleToLongBits(0.0)) {
                codedOutputByteBufferNano.writeDouble(6, this.doubleValue);
            }
            if (!Arrays.equals(this.stringValue, WireFormatNano.EMPTY_BYTES)) {
                codedOutputByteBufferNano.writeBytes(7, this.stringValue);
            }
            if (this.aggregateValue != null && !this.aggregateValue.equals("")) {
                codedOutputByteBufferNano.writeString(8, this.aggregateValue);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public static final class NamePart extends ExtendableMessageNano<NamePart>
        {
            private static volatile NamePart[] _emptyArray;
            public boolean isExtension;
            public String namePart;
            
            public NamePart() {
                this.clear();
            }
            
            public static NamePart[] emptyArray() {
                if (NamePart._emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (NamePart._emptyArray == null) {
                            NamePart._emptyArray = new NamePart[0];
                        }
                    }
                }
                return NamePart._emptyArray;
            }
            
            public static NamePart parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                return new NamePart().mergeFrom(codedInputByteBufferNano);
            }
            
            public static NamePart parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
                return MessageNano.mergeFrom(new NamePart(), array);
            }
            
            public NamePart clear() {
                this.namePart = "";
                this.isExtension = false;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }
            
            @Override
            protected int computeSerializedSize() {
                return super.computeSerializedSize() + CodedOutputByteBufferNano.computeStringSize(1, this.namePart) + CodedOutputByteBufferNano.computeBoolSize(2, this.isExtension);
            }
            
            @Override
            public NamePart mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    final int tag = codedInputByteBufferNano.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag != 10) {
                        if (tag != 16) {
                            if (!super.storeUnknownField(codedInputByteBufferNano, tag)) {
                                return this;
                            }
                            continue;
                        }
                        else {
                            this.isExtension = codedInputByteBufferNano.readBool();
                        }
                    }
                    else {
                        this.namePart = codedInputByteBufferNano.readString();
                    }
                }
            }
            
            @Override
            public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                codedOutputByteBufferNano.writeString(1, this.namePart);
                codedOutputByteBufferNano.writeBool(2, this.isExtension);
                super.writeTo(codedOutputByteBufferNano);
            }
        }
    }
}
