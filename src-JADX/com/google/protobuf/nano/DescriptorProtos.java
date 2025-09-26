package com.google.protobuf.nano;

import java.io.IOException;
import java.util.Arrays;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class DescriptorProtos {
    private DescriptorProtos() {
    }

    public static final class FileDescriptorSet extends ExtendableMessageNano<FileDescriptorSet> {
        private static volatile FileDescriptorSet[] _emptyArray;
        public FileDescriptorProto[] file;

        public static FileDescriptorSet[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new FileDescriptorSet[0];
                    }
                }
            }
            return _emptyArray;
        }

        public FileDescriptorSet() {
            clear();
        }

        public FileDescriptorSet clear() {
            this.file = FileDescriptorProto.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.file != null && this.file.length > 0) {
                for (int i = 0; i < this.file.length; i++) {
                    FileDescriptorProto fileDescriptorProto = this.file[i];
                    if (fileDescriptorProto != null) {
                        output.writeMessage(1, fileDescriptorProto);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.file != null && this.file.length > 0) {
                for (int i = 0; i < this.file.length; i++) {
                    FileDescriptorProto fileDescriptorProto = this.file[i];
                    if (fileDescriptorProto != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, fileDescriptorProto);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public FileDescriptorSet mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    int length = this.file == null ? 0 : this.file.length;
                    FileDescriptorProto[] fileDescriptorProtoArr = new FileDescriptorProto[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.file, 0, fileDescriptorProtoArr, 0, length);
                    }
                    while (length < fileDescriptorProtoArr.length - 1) {
                        fileDescriptorProtoArr[length] = new FileDescriptorProto();
                        input.readMessage(fileDescriptorProtoArr[length]);
                        input.readTag();
                        length++;
                    }
                    fileDescriptorProtoArr[length] = new FileDescriptorProto();
                    input.readMessage(fileDescriptorProtoArr[length]);
                    this.file = fileDescriptorProtoArr;
                }
            }
        }

        public static FileDescriptorSet parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (FileDescriptorSet) MessageNano.mergeFrom(new FileDescriptorSet(), data);
        }

        public static FileDescriptorSet parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new FileDescriptorSet().mergeFrom(input);
        }
    }

    public static final class FileDescriptorProto extends ExtendableMessageNano<FileDescriptorProto> {
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

        public static FileDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new FileDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public FileDescriptorProto() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.package_ != null && !this.package_.equals("")) {
                output.writeString(2, this.package_);
            }
            if (this.dependency != null && this.dependency.length > 0) {
                for (int i = 0; i < this.dependency.length; i++) {
                    String str = this.dependency[i];
                    if (str != null) {
                        output.writeString(3, str);
                    }
                }
            }
            if (this.messageType != null && this.messageType.length > 0) {
                for (int i2 = 0; i2 < this.messageType.length; i2++) {
                    DescriptorProto descriptorProto = this.messageType[i2];
                    if (descriptorProto != null) {
                        output.writeMessage(4, descriptorProto);
                    }
                }
            }
            if (this.enumType != null && this.enumType.length > 0) {
                for (int i3 = 0; i3 < this.enumType.length; i3++) {
                    EnumDescriptorProto enumDescriptorProto = this.enumType[i3];
                    if (enumDescriptorProto != null) {
                        output.writeMessage(5, enumDescriptorProto);
                    }
                }
            }
            if (this.service != null && this.service.length > 0) {
                for (int i4 = 0; i4 < this.service.length; i4++) {
                    ServiceDescriptorProto serviceDescriptorProto = this.service[i4];
                    if (serviceDescriptorProto != null) {
                        output.writeMessage(6, serviceDescriptorProto);
                    }
                }
            }
            if (this.extension != null && this.extension.length > 0) {
                for (int i5 = 0; i5 < this.extension.length; i5++) {
                    FieldDescriptorProto fieldDescriptorProto = this.extension[i5];
                    if (fieldDescriptorProto != null) {
                        output.writeMessage(7, fieldDescriptorProto);
                    }
                }
            }
            if (this.options != null) {
                output.writeMessage(8, this.options);
            }
            if (this.sourceCodeInfo != null) {
                output.writeMessage(9, this.sourceCodeInfo);
            }
            if (this.publicDependency != null && this.publicDependency.length > 0) {
                for (int i6 = 0; i6 < this.publicDependency.length; i6++) {
                    output.writeInt32(10, this.publicDependency[i6]);
                }
            }
            if (this.weakDependency != null && this.weakDependency.length > 0) {
                for (int i7 = 0; i7 < this.weakDependency.length; i7++) {
                    output.writeInt32(11, this.weakDependency[i7]);
                }
            }
            if (this.syntax != null && !this.syntax.equals("")) {
                output.writeString(12, this.syntax);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.package_ != null && !this.package_.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.package_);
            }
            if (this.dependency != null && this.dependency.length > 0) {
                int iComputeStringSizeNoTag = 0;
                int i = 0;
                for (int i2 = 0; i2 < this.dependency.length; i2++) {
                    String str = this.dependency[i2];
                    if (str != null) {
                        i++;
                        iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                    }
                }
                iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag + (i * 1);
            }
            if (this.messageType != null && this.messageType.length > 0) {
                int iComputeMessageSize = iComputeSerializedSize;
                for (int i3 = 0; i3 < this.messageType.length; i3++) {
                    DescriptorProto descriptorProto = this.messageType[i3];
                    if (descriptorProto != null) {
                        iComputeMessageSize += CodedOutputByteBufferNano.computeMessageSize(4, descriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize;
            }
            if (this.enumType != null && this.enumType.length > 0) {
                int iComputeMessageSize2 = iComputeSerializedSize;
                for (int i4 = 0; i4 < this.enumType.length; i4++) {
                    EnumDescriptorProto enumDescriptorProto = this.enumType[i4];
                    if (enumDescriptorProto != null) {
                        iComputeMessageSize2 += CodedOutputByteBufferNano.computeMessageSize(5, enumDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize2;
            }
            if (this.service != null && this.service.length > 0) {
                int iComputeMessageSize3 = iComputeSerializedSize;
                for (int i5 = 0; i5 < this.service.length; i5++) {
                    ServiceDescriptorProto serviceDescriptorProto = this.service[i5];
                    if (serviceDescriptorProto != null) {
                        iComputeMessageSize3 += CodedOutputByteBufferNano.computeMessageSize(6, serviceDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize3;
            }
            if (this.extension != null && this.extension.length > 0) {
                int iComputeMessageSize4 = iComputeSerializedSize;
                for (int i6 = 0; i6 < this.extension.length; i6++) {
                    FieldDescriptorProto fieldDescriptorProto = this.extension[i6];
                    if (fieldDescriptorProto != null) {
                        iComputeMessageSize4 += CodedOutputByteBufferNano.computeMessageSize(7, fieldDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize4;
            }
            if (this.options != null) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(8, this.options);
            }
            if (this.sourceCodeInfo != null) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(9, this.sourceCodeInfo);
            }
            if (this.publicDependency != null && this.publicDependency.length > 0) {
                int iComputeInt32SizeNoTag = 0;
                for (int i7 = 0; i7 < this.publicDependency.length; i7++) {
                    iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.publicDependency[i7]);
                }
                iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + (this.publicDependency.length * 1);
            }
            if (this.weakDependency != null && this.weakDependency.length > 0) {
                int iComputeInt32SizeNoTag2 = 0;
                for (int i8 = 0; i8 < this.weakDependency.length; i8++) {
                    iComputeInt32SizeNoTag2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.weakDependency[i8]);
                }
                iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag2 + (1 * this.weakDependency.length);
            }
            return (this.syntax == null || this.syntax.equals("")) ? iComputeSerializedSize : iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(12, this.syntax);
        }

        @Override // com.google.protobuf.nano.MessageNano
        public FileDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        this.name = input.readString();
                        break;
                    case 18:
                        this.package_ = input.readString();
                        break;
                    case 26:
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                        int length = this.dependency == null ? 0 : this.dependency.length;
                        String[] strArr = new String[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.dependency, 0, strArr, 0, length);
                        }
                        while (length < strArr.length - 1) {
                            strArr[length] = input.readString();
                            input.readTag();
                            length++;
                        }
                        strArr[length] = input.readString();
                        this.dependency = strArr;
                        break;
                    case 34:
                        int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                        int length2 = this.messageType == null ? 0 : this.messageType.length;
                        DescriptorProto[] descriptorProtoArr = new DescriptorProto[repeatedFieldArrayLength2 + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.messageType, 0, descriptorProtoArr, 0, length2);
                        }
                        while (length2 < descriptorProtoArr.length - 1) {
                            descriptorProtoArr[length2] = new DescriptorProto();
                            input.readMessage(descriptorProtoArr[length2]);
                            input.readTag();
                            length2++;
                        }
                        descriptorProtoArr[length2] = new DescriptorProto();
                        input.readMessage(descriptorProtoArr[length2]);
                        this.messageType = descriptorProtoArr;
                        break;
                    case 42:
                        int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                        int length3 = this.enumType == null ? 0 : this.enumType.length;
                        EnumDescriptorProto[] enumDescriptorProtoArr = new EnumDescriptorProto[repeatedFieldArrayLength3 + length3];
                        if (length3 != 0) {
                            System.arraycopy(this.enumType, 0, enumDescriptorProtoArr, 0, length3);
                        }
                        while (length3 < enumDescriptorProtoArr.length - 1) {
                            enumDescriptorProtoArr[length3] = new EnumDescriptorProto();
                            input.readMessage(enumDescriptorProtoArr[length3]);
                            input.readTag();
                            length3++;
                        }
                        enumDescriptorProtoArr[length3] = new EnumDescriptorProto();
                        input.readMessage(enumDescriptorProtoArr[length3]);
                        this.enumType = enumDescriptorProtoArr;
                        break;
                    case 50:
                        int repeatedFieldArrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                        int length4 = this.service == null ? 0 : this.service.length;
                        ServiceDescriptorProto[] serviceDescriptorProtoArr = new ServiceDescriptorProto[repeatedFieldArrayLength4 + length4];
                        if (length4 != 0) {
                            System.arraycopy(this.service, 0, serviceDescriptorProtoArr, 0, length4);
                        }
                        while (length4 < serviceDescriptorProtoArr.length - 1) {
                            serviceDescriptorProtoArr[length4] = new ServiceDescriptorProto();
                            input.readMessage(serviceDescriptorProtoArr[length4]);
                            input.readTag();
                            length4++;
                        }
                        serviceDescriptorProtoArr[length4] = new ServiceDescriptorProto();
                        input.readMessage(serviceDescriptorProtoArr[length4]);
                        this.service = serviceDescriptorProtoArr;
                        break;
                    case 58:
                        int repeatedFieldArrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(input, 58);
                        int length5 = this.extension == null ? 0 : this.extension.length;
                        FieldDescriptorProto[] fieldDescriptorProtoArr = new FieldDescriptorProto[repeatedFieldArrayLength5 + length5];
                        if (length5 != 0) {
                            System.arraycopy(this.extension, 0, fieldDescriptorProtoArr, 0, length5);
                        }
                        while (length5 < fieldDescriptorProtoArr.length - 1) {
                            fieldDescriptorProtoArr[length5] = new FieldDescriptorProto();
                            input.readMessage(fieldDescriptorProtoArr[length5]);
                            input.readTag();
                            length5++;
                        }
                        fieldDescriptorProtoArr[length5] = new FieldDescriptorProto();
                        input.readMessage(fieldDescriptorProtoArr[length5]);
                        this.extension = fieldDescriptorProtoArr;
                        break;
                    case 66:
                        if (this.options == null) {
                            this.options = new FileOptions();
                        }
                        input.readMessage(this.options);
                        break;
                    case 74:
                        if (this.sourceCodeInfo == null) {
                            this.sourceCodeInfo = new SourceCodeInfo();
                        }
                        input.readMessage(this.sourceCodeInfo);
                        break;
                    case 80:
                        int repeatedFieldArrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(input, 80);
                        int length6 = this.publicDependency == null ? 0 : this.publicDependency.length;
                        int[] iArr = new int[repeatedFieldArrayLength6 + length6];
                        if (length6 != 0) {
                            System.arraycopy(this.publicDependency, 0, iArr, 0, length6);
                        }
                        while (length6 < iArr.length - 1) {
                            iArr[length6] = input.readInt32();
                            input.readTag();
                            length6++;
                        }
                        iArr[length6] = input.readInt32();
                        this.publicDependency = iArr;
                        break;
                    case 82:
                        int iPushLimit = input.pushLimit(input.readRawVarint32());
                        int position = input.getPosition();
                        int i = 0;
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            i++;
                        }
                        input.rewindToPosition(position);
                        int length7 = this.publicDependency == null ? 0 : this.publicDependency.length;
                        int[] iArr2 = new int[i + length7];
                        if (length7 != 0) {
                            System.arraycopy(this.publicDependency, 0, iArr2, 0, length7);
                        }
                        while (length7 < iArr2.length) {
                            iArr2[length7] = input.readInt32();
                            length7++;
                        }
                        this.publicDependency = iArr2;
                        input.popLimit(iPushLimit);
                        break;
                    case 88:
                        int repeatedFieldArrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(input, 88);
                        int length8 = this.weakDependency == null ? 0 : this.weakDependency.length;
                        int[] iArr3 = new int[repeatedFieldArrayLength7 + length8];
                        if (length8 != 0) {
                            System.arraycopy(this.weakDependency, 0, iArr3, 0, length8);
                        }
                        while (length8 < iArr3.length - 1) {
                            iArr3[length8] = input.readInt32();
                            input.readTag();
                            length8++;
                        }
                        iArr3[length8] = input.readInt32();
                        this.weakDependency = iArr3;
                        break;
                    case 90:
                        int iPushLimit2 = input.pushLimit(input.readRawVarint32());
                        int position2 = input.getPosition();
                        int i2 = 0;
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            i2++;
                        }
                        input.rewindToPosition(position2);
                        int length9 = this.weakDependency == null ? 0 : this.weakDependency.length;
                        int[] iArr4 = new int[i2 + length9];
                        if (length9 != 0) {
                            System.arraycopy(this.weakDependency, 0, iArr4, 0, length9);
                        }
                        while (length9 < iArr4.length) {
                            iArr4[length9] = input.readInt32();
                            length9++;
                        }
                        this.weakDependency = iArr4;
                        input.popLimit(iPushLimit2);
                        break;
                    case 98:
                        this.syntax = input.readString();
                        break;
                    default:
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static FileDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (FileDescriptorProto) MessageNano.mergeFrom(new FileDescriptorProto(), data);
        }

        public static FileDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new FileDescriptorProto().mergeFrom(input);
        }
    }

    public static final class DescriptorProto extends ExtendableMessageNano<DescriptorProto> {
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

        public static final class ExtensionRange extends ExtendableMessageNano<ExtensionRange> {
            private static volatile ExtensionRange[] _emptyArray;
            public int end;
            public ExtensionRangeOptions options;
            public int start;

            public static ExtensionRange[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ExtensionRange[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ExtensionRange() {
                clear();
            }

            public ExtensionRange clear() {
                this.start = 0;
                this.end = 0;
                this.options = null;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.start != 0) {
                    output.writeInt32(1, this.start);
                }
                if (this.end != 0) {
                    output.writeInt32(2, this.end);
                }
                if (this.options != null) {
                    output.writeMessage(3, this.options);
                }
                super.writeTo(output);
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            protected int computeSerializedSize() {
                int iComputeSerializedSize = super.computeSerializedSize();
                if (this.start != 0) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.start);
                }
                if (this.end != 0) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, this.end);
                }
                return this.options != null ? iComputeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(3, this.options) : iComputeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public ExtensionRange mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.start = input.readInt32();
                    } else if (tag == 16) {
                        this.end = input.readInt32();
                    } else if (tag != 26) {
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        if (this.options == null) {
                            this.options = new ExtensionRangeOptions();
                        }
                        input.readMessage(this.options);
                    }
                }
            }

            public static ExtensionRange parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ExtensionRange) MessageNano.mergeFrom(new ExtensionRange(), data);
            }

            public static ExtensionRange parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ExtensionRange().mergeFrom(input);
            }
        }

        public static final class ReservedRange extends ExtendableMessageNano<ReservedRange> {
            private static volatile ReservedRange[] _emptyArray;
            public int end;
            public int start;

            public static ReservedRange[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new ReservedRange[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public ReservedRange() {
                clear();
            }

            public ReservedRange clear() {
                this.start = 0;
                this.end = 0;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.start != 0) {
                    output.writeInt32(1, this.start);
                }
                if (this.end != 0) {
                    output.writeInt32(2, this.end);
                }
                super.writeTo(output);
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            protected int computeSerializedSize() {
                int iComputeSerializedSize = super.computeSerializedSize();
                if (this.start != 0) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.start);
                }
                return this.end != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, this.end) : iComputeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public ReservedRange mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.start = input.readInt32();
                    } else if (tag != 16) {
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        this.end = input.readInt32();
                    }
                }
            }

            public static ReservedRange parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (ReservedRange) MessageNano.mergeFrom(new ReservedRange(), data);
            }

            public static ReservedRange parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new ReservedRange().mergeFrom(input);
            }
        }

        public static DescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new DescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public DescriptorProto() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.field != null && this.field.length > 0) {
                for (int i = 0; i < this.field.length; i++) {
                    FieldDescriptorProto fieldDescriptorProto = this.field[i];
                    if (fieldDescriptorProto != null) {
                        output.writeMessage(2, fieldDescriptorProto);
                    }
                }
            }
            if (this.nestedType != null && this.nestedType.length > 0) {
                for (int i2 = 0; i2 < this.nestedType.length; i2++) {
                    DescriptorProto descriptorProto = this.nestedType[i2];
                    if (descriptorProto != null) {
                        output.writeMessage(3, descriptorProto);
                    }
                }
            }
            if (this.enumType != null && this.enumType.length > 0) {
                for (int i3 = 0; i3 < this.enumType.length; i3++) {
                    EnumDescriptorProto enumDescriptorProto = this.enumType[i3];
                    if (enumDescriptorProto != null) {
                        output.writeMessage(4, enumDescriptorProto);
                    }
                }
            }
            if (this.extensionRange != null && this.extensionRange.length > 0) {
                for (int i4 = 0; i4 < this.extensionRange.length; i4++) {
                    ExtensionRange extensionRange = this.extensionRange[i4];
                    if (extensionRange != null) {
                        output.writeMessage(5, extensionRange);
                    }
                }
            }
            if (this.extension != null && this.extension.length > 0) {
                for (int i5 = 0; i5 < this.extension.length; i5++) {
                    FieldDescriptorProto fieldDescriptorProto2 = this.extension[i5];
                    if (fieldDescriptorProto2 != null) {
                        output.writeMessage(6, fieldDescriptorProto2);
                    }
                }
            }
            if (this.options != null) {
                output.writeMessage(7, this.options);
            }
            if (this.oneofDecl != null && this.oneofDecl.length > 0) {
                for (int i6 = 0; i6 < this.oneofDecl.length; i6++) {
                    OneofDescriptorProto oneofDescriptorProto = this.oneofDecl[i6];
                    if (oneofDescriptorProto != null) {
                        output.writeMessage(8, oneofDescriptorProto);
                    }
                }
            }
            if (this.reservedRange != null && this.reservedRange.length > 0) {
                for (int i7 = 0; i7 < this.reservedRange.length; i7++) {
                    ReservedRange reservedRange = this.reservedRange[i7];
                    if (reservedRange != null) {
                        output.writeMessage(9, reservedRange);
                    }
                }
            }
            if (this.reservedName != null && this.reservedName.length > 0) {
                for (int i8 = 0; i8 < this.reservedName.length; i8++) {
                    String str = this.reservedName[i8];
                    if (str != null) {
                        output.writeString(10, str);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.field != null && this.field.length > 0) {
                int iComputeMessageSize = iComputeSerializedSize;
                for (int i = 0; i < this.field.length; i++) {
                    FieldDescriptorProto fieldDescriptorProto = this.field[i];
                    if (fieldDescriptorProto != null) {
                        iComputeMessageSize += CodedOutputByteBufferNano.computeMessageSize(2, fieldDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize;
            }
            if (this.nestedType != null && this.nestedType.length > 0) {
                int iComputeMessageSize2 = iComputeSerializedSize;
                for (int i2 = 0; i2 < this.nestedType.length; i2++) {
                    DescriptorProto descriptorProto = this.nestedType[i2];
                    if (descriptorProto != null) {
                        iComputeMessageSize2 += CodedOutputByteBufferNano.computeMessageSize(3, descriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize2;
            }
            if (this.enumType != null && this.enumType.length > 0) {
                int iComputeMessageSize3 = iComputeSerializedSize;
                for (int i3 = 0; i3 < this.enumType.length; i3++) {
                    EnumDescriptorProto enumDescriptorProto = this.enumType[i3];
                    if (enumDescriptorProto != null) {
                        iComputeMessageSize3 += CodedOutputByteBufferNano.computeMessageSize(4, enumDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize3;
            }
            if (this.extensionRange != null && this.extensionRange.length > 0) {
                int iComputeMessageSize4 = iComputeSerializedSize;
                for (int i4 = 0; i4 < this.extensionRange.length; i4++) {
                    ExtensionRange extensionRange = this.extensionRange[i4];
                    if (extensionRange != null) {
                        iComputeMessageSize4 += CodedOutputByteBufferNano.computeMessageSize(5, extensionRange);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize4;
            }
            if (this.extension != null && this.extension.length > 0) {
                int iComputeMessageSize5 = iComputeSerializedSize;
                for (int i5 = 0; i5 < this.extension.length; i5++) {
                    FieldDescriptorProto fieldDescriptorProto2 = this.extension[i5];
                    if (fieldDescriptorProto2 != null) {
                        iComputeMessageSize5 += CodedOutputByteBufferNano.computeMessageSize(6, fieldDescriptorProto2);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize5;
            }
            if (this.options != null) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(7, this.options);
            }
            if (this.oneofDecl != null && this.oneofDecl.length > 0) {
                int iComputeMessageSize6 = iComputeSerializedSize;
                for (int i6 = 0; i6 < this.oneofDecl.length; i6++) {
                    OneofDescriptorProto oneofDescriptorProto = this.oneofDecl[i6];
                    if (oneofDescriptorProto != null) {
                        iComputeMessageSize6 += CodedOutputByteBufferNano.computeMessageSize(8, oneofDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize6;
            }
            if (this.reservedRange != null && this.reservedRange.length > 0) {
                int iComputeMessageSize7 = iComputeSerializedSize;
                for (int i7 = 0; i7 < this.reservedRange.length; i7++) {
                    ReservedRange reservedRange = this.reservedRange[i7];
                    if (reservedRange != null) {
                        iComputeMessageSize7 += CodedOutputByteBufferNano.computeMessageSize(9, reservedRange);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize7;
            }
            if (this.reservedName == null || this.reservedName.length <= 0) {
                return iComputeSerializedSize;
            }
            int iComputeStringSizeNoTag = 0;
            int i8 = 0;
            for (int i9 = 0; i9 < this.reservedName.length; i9++) {
                String str = this.reservedName[i9];
                if (str != null) {
                    i8++;
                    iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                }
            }
            return iComputeSerializedSize + iComputeStringSizeNoTag + (1 * i8);
        }

        @Override // com.google.protobuf.nano.MessageNano
        public DescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        this.name = input.readString();
                        break;
                    case 18:
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                        int length = this.field == null ? 0 : this.field.length;
                        FieldDescriptorProto[] fieldDescriptorProtoArr = new FieldDescriptorProto[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.field, 0, fieldDescriptorProtoArr, 0, length);
                        }
                        while (length < fieldDescriptorProtoArr.length - 1) {
                            fieldDescriptorProtoArr[length] = new FieldDescriptorProto();
                            input.readMessage(fieldDescriptorProtoArr[length]);
                            input.readTag();
                            length++;
                        }
                        fieldDescriptorProtoArr[length] = new FieldDescriptorProto();
                        input.readMessage(fieldDescriptorProtoArr[length]);
                        this.field = fieldDescriptorProtoArr;
                        break;
                    case 26:
                        int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 26);
                        int length2 = this.nestedType == null ? 0 : this.nestedType.length;
                        DescriptorProto[] descriptorProtoArr = new DescriptorProto[repeatedFieldArrayLength2 + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.nestedType, 0, descriptorProtoArr, 0, length2);
                        }
                        while (length2 < descriptorProtoArr.length - 1) {
                            descriptorProtoArr[length2] = new DescriptorProto();
                            input.readMessage(descriptorProtoArr[length2]);
                            input.readTag();
                            length2++;
                        }
                        descriptorProtoArr[length2] = new DescriptorProto();
                        input.readMessage(descriptorProtoArr[length2]);
                        this.nestedType = descriptorProtoArr;
                        break;
                    case 34:
                        int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                        int length3 = this.enumType == null ? 0 : this.enumType.length;
                        EnumDescriptorProto[] enumDescriptorProtoArr = new EnumDescriptorProto[repeatedFieldArrayLength3 + length3];
                        if (length3 != 0) {
                            System.arraycopy(this.enumType, 0, enumDescriptorProtoArr, 0, length3);
                        }
                        while (length3 < enumDescriptorProtoArr.length - 1) {
                            enumDescriptorProtoArr[length3] = new EnumDescriptorProto();
                            input.readMessage(enumDescriptorProtoArr[length3]);
                            input.readTag();
                            length3++;
                        }
                        enumDescriptorProtoArr[length3] = new EnumDescriptorProto();
                        input.readMessage(enumDescriptorProtoArr[length3]);
                        this.enumType = enumDescriptorProtoArr;
                        break;
                    case 42:
                        int repeatedFieldArrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                        int length4 = this.extensionRange == null ? 0 : this.extensionRange.length;
                        ExtensionRange[] extensionRangeArr = new ExtensionRange[repeatedFieldArrayLength4 + length4];
                        if (length4 != 0) {
                            System.arraycopy(this.extensionRange, 0, extensionRangeArr, 0, length4);
                        }
                        while (length4 < extensionRangeArr.length - 1) {
                            extensionRangeArr[length4] = new ExtensionRange();
                            input.readMessage(extensionRangeArr[length4]);
                            input.readTag();
                            length4++;
                        }
                        extensionRangeArr[length4] = new ExtensionRange();
                        input.readMessage(extensionRangeArr[length4]);
                        this.extensionRange = extensionRangeArr;
                        break;
                    case 50:
                        int repeatedFieldArrayLength5 = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                        int length5 = this.extension == null ? 0 : this.extension.length;
                        FieldDescriptorProto[] fieldDescriptorProtoArr2 = new FieldDescriptorProto[repeatedFieldArrayLength5 + length5];
                        if (length5 != 0) {
                            System.arraycopy(this.extension, 0, fieldDescriptorProtoArr2, 0, length5);
                        }
                        while (length5 < fieldDescriptorProtoArr2.length - 1) {
                            fieldDescriptorProtoArr2[length5] = new FieldDescriptorProto();
                            input.readMessage(fieldDescriptorProtoArr2[length5]);
                            input.readTag();
                            length5++;
                        }
                        fieldDescriptorProtoArr2[length5] = new FieldDescriptorProto();
                        input.readMessage(fieldDescriptorProtoArr2[length5]);
                        this.extension = fieldDescriptorProtoArr2;
                        break;
                    case 58:
                        if (this.options == null) {
                            this.options = new MessageOptions();
                        }
                        input.readMessage(this.options);
                        break;
                    case 66:
                        int repeatedFieldArrayLength6 = WireFormatNano.getRepeatedFieldArrayLength(input, 66);
                        int length6 = this.oneofDecl == null ? 0 : this.oneofDecl.length;
                        OneofDescriptorProto[] oneofDescriptorProtoArr = new OneofDescriptorProto[repeatedFieldArrayLength6 + length6];
                        if (length6 != 0) {
                            System.arraycopy(this.oneofDecl, 0, oneofDescriptorProtoArr, 0, length6);
                        }
                        while (length6 < oneofDescriptorProtoArr.length - 1) {
                            oneofDescriptorProtoArr[length6] = new OneofDescriptorProto();
                            input.readMessage(oneofDescriptorProtoArr[length6]);
                            input.readTag();
                            length6++;
                        }
                        oneofDescriptorProtoArr[length6] = new OneofDescriptorProto();
                        input.readMessage(oneofDescriptorProtoArr[length6]);
                        this.oneofDecl = oneofDescriptorProtoArr;
                        break;
                    case 74:
                        int repeatedFieldArrayLength7 = WireFormatNano.getRepeatedFieldArrayLength(input, 74);
                        int length7 = this.reservedRange == null ? 0 : this.reservedRange.length;
                        ReservedRange[] reservedRangeArr = new ReservedRange[repeatedFieldArrayLength7 + length7];
                        if (length7 != 0) {
                            System.arraycopy(this.reservedRange, 0, reservedRangeArr, 0, length7);
                        }
                        while (length7 < reservedRangeArr.length - 1) {
                            reservedRangeArr[length7] = new ReservedRange();
                            input.readMessage(reservedRangeArr[length7]);
                            input.readTag();
                            length7++;
                        }
                        reservedRangeArr[length7] = new ReservedRange();
                        input.readMessage(reservedRangeArr[length7]);
                        this.reservedRange = reservedRangeArr;
                        break;
                    case 82:
                        int repeatedFieldArrayLength8 = WireFormatNano.getRepeatedFieldArrayLength(input, 82);
                        int length8 = this.reservedName == null ? 0 : this.reservedName.length;
                        String[] strArr = new String[repeatedFieldArrayLength8 + length8];
                        if (length8 != 0) {
                            System.arraycopy(this.reservedName, 0, strArr, 0, length8);
                        }
                        while (length8 < strArr.length - 1) {
                            strArr[length8] = input.readString();
                            input.readTag();
                            length8++;
                        }
                        strArr[length8] = input.readString();
                        this.reservedName = strArr;
                        break;
                    default:
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static DescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (DescriptorProto) MessageNano.mergeFrom(new DescriptorProto(), data);
        }

        public static DescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new DescriptorProto().mergeFrom(input);
        }
    }

    public static final class ExtensionRangeOptions extends ExtendableMessageNano<ExtensionRangeOptions> {
        private static volatile ExtensionRangeOptions[] _emptyArray;
        public UninterpretedOption[] uninterpretedOption;

        public static ExtensionRangeOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ExtensionRangeOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ExtensionRangeOptions() {
            clear();
        }

        public ExtensionRangeOptions clear() {
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public ExtensionRangeOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 7994) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                    int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                    UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                    }
                    while (length < uninterpretedOptionArr.length - 1) {
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        input.readTag();
                        length++;
                    }
                    uninterpretedOptionArr[length] = new UninterpretedOption();
                    input.readMessage(uninterpretedOptionArr[length]);
                    this.uninterpretedOption = uninterpretedOptionArr;
                }
            }
        }

        public static ExtensionRangeOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ExtensionRangeOptions) MessageNano.mergeFrom(new ExtensionRangeOptions(), data);
        }

        public static ExtensionRangeOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ExtensionRangeOptions().mergeFrom(input);
        }
    }

    public static final class FieldDescriptorProto extends ExtendableMessageNano<FieldDescriptorProto> {
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

        public interface Label {

            @NanoEnumValue(legacy = false, value = Label.class)
            public static final int LABEL_OPTIONAL = 1;

            @NanoEnumValue(legacy = false, value = Label.class)
            public static final int LABEL_REPEATED = 3;

            @NanoEnumValue(legacy = false, value = Label.class)
            public static final int LABEL_REQUIRED = 2;
        }

        public interface Type {

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

        @NanoEnumValue(legacy = false, value = Type.class)
        public static int checkTypeOrThrow(int value) {
            if (value >= 1 && value <= 18) {
                return value;
            }
            StringBuilder sb = new StringBuilder(36);
            sb.append(value);
            sb.append(" is not a valid enum Type");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = Type.class)
        public static int[] checkTypeOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkTypeOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = Label.class)
        public static int checkLabelOrThrow(int value) {
            if (value >= 1 && value <= 3) {
                return value;
            }
            StringBuilder sb = new StringBuilder(37);
            sb.append(value);
            sb.append(" is not a valid enum Label");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = Label.class)
        public static int[] checkLabelOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkLabelOrThrow(i);
            }
            return iArr;
        }

        public static FieldDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new FieldDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public FieldDescriptorProto() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.extendee != null && !this.extendee.equals("")) {
                output.writeString(2, this.extendee);
            }
            if (this.number != 0) {
                output.writeInt32(3, this.number);
            }
            if (this.label != 1) {
                output.writeInt32(4, this.label);
            }
            if (this.type != 1) {
                output.writeInt32(5, this.type);
            }
            if (this.typeName != null && !this.typeName.equals("")) {
                output.writeString(6, this.typeName);
            }
            if (this.defaultValue != null && !this.defaultValue.equals("")) {
                output.writeString(7, this.defaultValue);
            }
            if (this.options != null) {
                output.writeMessage(8, this.options);
            }
            if (this.oneofIndex != 0) {
                output.writeInt32(9, this.oneofIndex);
            }
            if (this.jsonName != null && !this.jsonName.equals("")) {
                output.writeString(10, this.jsonName);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.extendee != null && !this.extendee.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.extendee);
            }
            if (this.number != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, this.number);
            }
            if (this.label != 1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, this.label);
            }
            if (this.type != 1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, this.type);
            }
            if (this.typeName != null && !this.typeName.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(6, this.typeName);
            }
            if (this.defaultValue != null && !this.defaultValue.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(7, this.defaultValue);
            }
            if (this.options != null) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(8, this.options);
            }
            if (this.oneofIndex != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, this.oneofIndex);
            }
            return (this.jsonName == null || this.jsonName.equals("")) ? iComputeSerializedSize : iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(10, this.jsonName);
        }

        @Override // com.google.protobuf.nano.MessageNano
        public FieldDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        this.name = input.readString();
                        break;
                    case 18:
                        this.extendee = input.readString();
                        break;
                    case 24:
                        this.number = input.readInt32();
                        break;
                    case 32:
                        int position = input.getPosition();
                        try {
                            this.label = checkLabelOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused) {
                            input.rewindToPosition(position);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 40:
                        int position2 = input.getPosition();
                        try {
                            this.type = checkTypeOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused2) {
                            input.rewindToPosition(position2);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 50:
                        this.typeName = input.readString();
                        break;
                    case 58:
                        this.defaultValue = input.readString();
                        break;
                    case 66:
                        if (this.options == null) {
                            this.options = new FieldOptions();
                        }
                        input.readMessage(this.options);
                        break;
                    case 72:
                        this.oneofIndex = input.readInt32();
                        break;
                    case 82:
                        this.jsonName = input.readString();
                        break;
                    default:
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static FieldDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (FieldDescriptorProto) MessageNano.mergeFrom(new FieldDescriptorProto(), data);
        }

        public static FieldDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new FieldDescriptorProto().mergeFrom(input);
        }
    }

    public static final class OneofDescriptorProto extends ExtendableMessageNano<OneofDescriptorProto> {
        private static volatile OneofDescriptorProto[] _emptyArray;
        public String name;
        public OneofOptions options;

        public static OneofDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new OneofDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public OneofDescriptorProto() {
            clear();
        }

        public OneofDescriptorProto clear() {
            this.name = "";
            this.options = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.options != null) {
                output.writeMessage(2, this.options);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            return this.options != null ? iComputeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, this.options) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public OneofDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.name = input.readString();
                } else if (tag != 18) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    if (this.options == null) {
                        this.options = new OneofOptions();
                    }
                    input.readMessage(this.options);
                }
            }
        }

        public static OneofDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (OneofDescriptorProto) MessageNano.mergeFrom(new OneofDescriptorProto(), data);
        }

        public static OneofDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new OneofDescriptorProto().mergeFrom(input);
        }
    }

    public static final class EnumDescriptorProto extends ExtendableMessageNano<EnumDescriptorProto> {
        private static volatile EnumDescriptorProto[] _emptyArray;
        public String name;
        public EnumOptions options;
        public String[] reservedName;
        public EnumReservedRange[] reservedRange;
        public EnumValueDescriptorProto[] value;

        public static final class EnumReservedRange extends ExtendableMessageNano<EnumReservedRange> {
            private static volatile EnumReservedRange[] _emptyArray;
            public int end;
            public int start;

            public static EnumReservedRange[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new EnumReservedRange[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public EnumReservedRange() {
                clear();
            }

            public EnumReservedRange clear() {
                this.start = 0;
                this.end = 0;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.start != 0) {
                    output.writeInt32(1, this.start);
                }
                if (this.end != 0) {
                    output.writeInt32(2, this.end);
                }
                super.writeTo(output);
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            protected int computeSerializedSize() {
                int iComputeSerializedSize = super.computeSerializedSize();
                if (this.start != 0) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.start);
                }
                return this.end != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(2, this.end) : iComputeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public EnumReservedRange mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        this.start = input.readInt32();
                    } else if (tag != 16) {
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        this.end = input.readInt32();
                    }
                }
            }

            public static EnumReservedRange parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (EnumReservedRange) MessageNano.mergeFrom(new EnumReservedRange(), data);
            }

            public static EnumReservedRange parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new EnumReservedRange().mergeFrom(input);
            }
        }

        public static EnumDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new EnumDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public EnumDescriptorProto() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.value != null && this.value.length > 0) {
                for (int i = 0; i < this.value.length; i++) {
                    EnumValueDescriptorProto enumValueDescriptorProto = this.value[i];
                    if (enumValueDescriptorProto != null) {
                        output.writeMessage(2, enumValueDescriptorProto);
                    }
                }
            }
            if (this.options != null) {
                output.writeMessage(3, this.options);
            }
            if (this.reservedRange != null && this.reservedRange.length > 0) {
                for (int i2 = 0; i2 < this.reservedRange.length; i2++) {
                    EnumReservedRange enumReservedRange = this.reservedRange[i2];
                    if (enumReservedRange != null) {
                        output.writeMessage(4, enumReservedRange);
                    }
                }
            }
            if (this.reservedName != null && this.reservedName.length > 0) {
                for (int i3 = 0; i3 < this.reservedName.length; i3++) {
                    String str = this.reservedName[i3];
                    if (str != null) {
                        output.writeString(5, str);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.value != null && this.value.length > 0) {
                int iComputeMessageSize = iComputeSerializedSize;
                for (int i = 0; i < this.value.length; i++) {
                    EnumValueDescriptorProto enumValueDescriptorProto = this.value[i];
                    if (enumValueDescriptorProto != null) {
                        iComputeMessageSize += CodedOutputByteBufferNano.computeMessageSize(2, enumValueDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize;
            }
            if (this.options != null) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(3, this.options);
            }
            if (this.reservedRange != null && this.reservedRange.length > 0) {
                int iComputeMessageSize2 = iComputeSerializedSize;
                for (int i2 = 0; i2 < this.reservedRange.length; i2++) {
                    EnumReservedRange enumReservedRange = this.reservedRange[i2];
                    if (enumReservedRange != null) {
                        iComputeMessageSize2 += CodedOutputByteBufferNano.computeMessageSize(4, enumReservedRange);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize2;
            }
            if (this.reservedName == null || this.reservedName.length <= 0) {
                return iComputeSerializedSize;
            }
            int iComputeStringSizeNoTag = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < this.reservedName.length; i4++) {
                String str = this.reservedName[i4];
                if (str != null) {
                    i3++;
                    iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                }
            }
            return iComputeSerializedSize + iComputeStringSizeNoTag + (1 * i3);
        }

        @Override // com.google.protobuf.nano.MessageNano
        public EnumDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.name = input.readString();
                } else if (tag == 18) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int length = this.value == null ? 0 : this.value.length;
                    EnumValueDescriptorProto[] enumValueDescriptorProtoArr = new EnumValueDescriptorProto[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.value, 0, enumValueDescriptorProtoArr, 0, length);
                    }
                    while (length < enumValueDescriptorProtoArr.length - 1) {
                        enumValueDescriptorProtoArr[length] = new EnumValueDescriptorProto();
                        input.readMessage(enumValueDescriptorProtoArr[length]);
                        input.readTag();
                        length++;
                    }
                    enumValueDescriptorProtoArr[length] = new EnumValueDescriptorProto();
                    input.readMessage(enumValueDescriptorProtoArr[length]);
                    this.value = enumValueDescriptorProtoArr;
                } else if (tag == 26) {
                    if (this.options == null) {
                        this.options = new EnumOptions();
                    }
                    input.readMessage(this.options);
                } else if (tag == 34) {
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int length2 = this.reservedRange == null ? 0 : this.reservedRange.length;
                    EnumReservedRange[] enumReservedRangeArr = new EnumReservedRange[repeatedFieldArrayLength2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.reservedRange, 0, enumReservedRangeArr, 0, length2);
                    }
                    while (length2 < enumReservedRangeArr.length - 1) {
                        enumReservedRangeArr[length2] = new EnumReservedRange();
                        input.readMessage(enumReservedRangeArr[length2]);
                        input.readTag();
                        length2++;
                    }
                    enumReservedRangeArr[length2] = new EnumReservedRange();
                    input.readMessage(enumReservedRangeArr[length2]);
                    this.reservedRange = enumReservedRangeArr;
                } else if (tag != 42) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    int length3 = this.reservedName == null ? 0 : this.reservedName.length;
                    String[] strArr = new String[repeatedFieldArrayLength3 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.reservedName, 0, strArr, 0, length3);
                    }
                    while (length3 < strArr.length - 1) {
                        strArr[length3] = input.readString();
                        input.readTag();
                        length3++;
                    }
                    strArr[length3] = input.readString();
                    this.reservedName = strArr;
                }
            }
        }

        public static EnumDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (EnumDescriptorProto) MessageNano.mergeFrom(new EnumDescriptorProto(), data);
        }

        public static EnumDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new EnumDescriptorProto().mergeFrom(input);
        }
    }

    public static final class EnumValueDescriptorProto extends ExtendableMessageNano<EnumValueDescriptorProto> {
        private static volatile EnumValueDescriptorProto[] _emptyArray;
        public String name;
        public int number;
        public EnumValueOptions options;

        public static EnumValueDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new EnumValueDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public EnumValueDescriptorProto() {
            clear();
        }

        public EnumValueDescriptorProto clear() {
            this.name = "";
            this.number = 0;
            this.options = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.number != 0) {
                output.writeInt32(2, this.number);
            }
            if (this.options != null) {
                output.writeMessage(3, this.options);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.number != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, this.number);
            }
            return this.options != null ? iComputeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(3, this.options) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public EnumValueDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.name = input.readString();
                } else if (tag == 16) {
                    this.number = input.readInt32();
                } else if (tag != 26) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    if (this.options == null) {
                        this.options = new EnumValueOptions();
                    }
                    input.readMessage(this.options);
                }
            }
        }

        public static EnumValueDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (EnumValueDescriptorProto) MessageNano.mergeFrom(new EnumValueDescriptorProto(), data);
        }

        public static EnumValueDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new EnumValueDescriptorProto().mergeFrom(input);
        }
    }

    public static final class ServiceDescriptorProto extends ExtendableMessageNano<ServiceDescriptorProto> {
        private static volatile ServiceDescriptorProto[] _emptyArray;
        public MethodDescriptorProto[] method;
        public String name;
        public ServiceOptions options;
        public StreamDescriptorProto[] stream;

        public static ServiceDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ServiceDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ServiceDescriptorProto() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.method != null && this.method.length > 0) {
                for (int i = 0; i < this.method.length; i++) {
                    MethodDescriptorProto methodDescriptorProto = this.method[i];
                    if (methodDescriptorProto != null) {
                        output.writeMessage(2, methodDescriptorProto);
                    }
                }
            }
            if (this.options != null) {
                output.writeMessage(3, this.options);
            }
            if (this.stream != null && this.stream.length > 0) {
                for (int i2 = 0; i2 < this.stream.length; i2++) {
                    StreamDescriptorProto streamDescriptorProto = this.stream[i2];
                    if (streamDescriptorProto != null) {
                        output.writeMessage(4, streamDescriptorProto);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.method != null && this.method.length > 0) {
                int iComputeMessageSize = iComputeSerializedSize;
                for (int i = 0; i < this.method.length; i++) {
                    MethodDescriptorProto methodDescriptorProto = this.method[i];
                    if (methodDescriptorProto != null) {
                        iComputeMessageSize += CodedOutputByteBufferNano.computeMessageSize(2, methodDescriptorProto);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize;
            }
            if (this.options != null) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(3, this.options);
            }
            if (this.stream != null && this.stream.length > 0) {
                for (int i2 = 0; i2 < this.stream.length; i2++) {
                    StreamDescriptorProto streamDescriptorProto = this.stream[i2];
                    if (streamDescriptorProto != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(4, streamDescriptorProto);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public ServiceDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.name = input.readString();
                } else if (tag == 18) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int length = this.method == null ? 0 : this.method.length;
                    MethodDescriptorProto[] methodDescriptorProtoArr = new MethodDescriptorProto[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.method, 0, methodDescriptorProtoArr, 0, length);
                    }
                    while (length < methodDescriptorProtoArr.length - 1) {
                        methodDescriptorProtoArr[length] = new MethodDescriptorProto();
                        input.readMessage(methodDescriptorProtoArr[length]);
                        input.readTag();
                        length++;
                    }
                    methodDescriptorProtoArr[length] = new MethodDescriptorProto();
                    input.readMessage(methodDescriptorProtoArr[length]);
                    this.method = methodDescriptorProtoArr;
                } else if (tag == 26) {
                    if (this.options == null) {
                        this.options = new ServiceOptions();
                    }
                    input.readMessage(this.options);
                } else if (tag != 34) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int length2 = this.stream == null ? 0 : this.stream.length;
                    StreamDescriptorProto[] streamDescriptorProtoArr = new StreamDescriptorProto[repeatedFieldArrayLength2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.stream, 0, streamDescriptorProtoArr, 0, length2);
                    }
                    while (length2 < streamDescriptorProtoArr.length - 1) {
                        streamDescriptorProtoArr[length2] = new StreamDescriptorProto();
                        input.readMessage(streamDescriptorProtoArr[length2]);
                        input.readTag();
                        length2++;
                    }
                    streamDescriptorProtoArr[length2] = new StreamDescriptorProto();
                    input.readMessage(streamDescriptorProtoArr[length2]);
                    this.stream = streamDescriptorProtoArr;
                }
            }
        }

        public static ServiceDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ServiceDescriptorProto) MessageNano.mergeFrom(new ServiceDescriptorProto(), data);
        }

        public static ServiceDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ServiceDescriptorProto().mergeFrom(input);
        }
    }

    public static final class MethodDescriptorProto extends ExtendableMessageNano<MethodDescriptorProto> {
        private static volatile MethodDescriptorProto[] _emptyArray;
        public boolean clientStreaming;
        public String inputType;
        public String name;
        public MethodOptions options;
        public String outputType;
        public boolean serverStreaming;

        public static MethodDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new MethodDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public MethodDescriptorProto() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.inputType != null && !this.inputType.equals("")) {
                output.writeString(2, this.inputType);
            }
            if (this.outputType != null && !this.outputType.equals("")) {
                output.writeString(3, this.outputType);
            }
            if (this.options != null) {
                output.writeMessage(4, this.options);
            }
            if (this.clientStreaming) {
                output.writeBool(5, this.clientStreaming);
            }
            if (this.serverStreaming) {
                output.writeBool(6, this.serverStreaming);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.inputType != null && !this.inputType.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.inputType);
            }
            if (this.outputType != null && !this.outputType.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.outputType);
            }
            if (this.options != null) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(4, this.options);
            }
            if (this.clientStreaming) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, this.clientStreaming);
            }
            return this.serverStreaming ? iComputeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(6, this.serverStreaming) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public MethodDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.name = input.readString();
                } else if (tag == 18) {
                    this.inputType = input.readString();
                } else if (tag == 26) {
                    this.outputType = input.readString();
                } else if (tag == 34) {
                    if (this.options == null) {
                        this.options = new MethodOptions();
                    }
                    input.readMessage(this.options);
                } else if (tag == 40) {
                    this.clientStreaming = input.readBool();
                } else if (tag != 48) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    this.serverStreaming = input.readBool();
                }
            }
        }

        public static MethodDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (MethodDescriptorProto) MessageNano.mergeFrom(new MethodDescriptorProto(), data);
        }

        public static MethodDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new MethodDescriptorProto().mergeFrom(input);
        }
    }

    public static final class StreamDescriptorProto extends ExtendableMessageNano<StreamDescriptorProto> {
        private static volatile StreamDescriptorProto[] _emptyArray;
        public String clientMessageType;
        public String name;
        public StreamOptions options;
        public String serverMessageType;

        public static StreamDescriptorProto[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new StreamDescriptorProto[0];
                    }
                }
            }
            return _emptyArray;
        }

        public StreamDescriptorProto() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && !this.name.equals("")) {
                output.writeString(1, this.name);
            }
            if (this.clientMessageType != null && !this.clientMessageType.equals("")) {
                output.writeString(2, this.clientMessageType);
            }
            if (this.serverMessageType != null && !this.serverMessageType.equals("")) {
                output.writeString(3, this.serverMessageType);
            }
            if (this.options != null) {
                output.writeMessage(4, this.options);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && !this.name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
            }
            if (this.clientMessageType != null && !this.clientMessageType.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.clientMessageType);
            }
            if (this.serverMessageType != null && !this.serverMessageType.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.serverMessageType);
            }
            return this.options != null ? iComputeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(4, this.options) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public StreamDescriptorProto mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.name = input.readString();
                } else if (tag == 18) {
                    this.clientMessageType = input.readString();
                } else if (tag == 26) {
                    this.serverMessageType = input.readString();
                } else if (tag != 34) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    if (this.options == null) {
                        this.options = new StreamOptions();
                    }
                    input.readMessage(this.options);
                }
            }
        }

        public static StreamDescriptorProto parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (StreamDescriptorProto) MessageNano.mergeFrom(new StreamDescriptorProto(), data);
        }

        public static StreamDescriptorProto parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new StreamDescriptorProto().mergeFrom(input);
        }
    }

    public static final class FileOptions extends ExtendableMessageNano<FileOptions> {
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

        public interface CompatibilityLevel {

            @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
            public static final int NO_COMPATIBILITY = 0;

            @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
            public static final int PROTO1_COMPATIBLE = 100;
        }

        public interface OptimizeMode {

            @NanoEnumValue(legacy = false, value = OptimizeMode.class)
            public static final int CODE_SIZE = 2;

            @NanoEnumValue(legacy = false, value = OptimizeMode.class)
            public static final int LITE_RUNTIME = 3;

            @NanoEnumValue(legacy = false, value = OptimizeMode.class)
            public static final int SPEED = 1;
        }

        @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
        public static int checkCompatibilityLevelOrThrow(int value) {
            if (value >= 0 && value <= 0) {
                return value;
            }
            if (value >= 100 && value <= 100) {
                return value;
            }
            StringBuilder sb = new StringBuilder(50);
            sb.append(value);
            sb.append(" is not a valid enum CompatibilityLevel");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = CompatibilityLevel.class)
        public static int[] checkCompatibilityLevelOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkCompatibilityLevelOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = OptimizeMode.class)
        public static int checkOptimizeModeOrThrow(int value) {
            if (value >= 1 && value <= 3) {
                return value;
            }
            StringBuilder sb = new StringBuilder(44);
            sb.append(value);
            sb.append(" is not a valid enum OptimizeMode");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = OptimizeMode.class)
        public static int[] checkOptimizeModeOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkOptimizeModeOrThrow(i);
            }
            return iArr;
        }

        public static FileOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new FileOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public FileOptions() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.javaPackage != null && !this.javaPackage.equals("")) {
                output.writeString(1, this.javaPackage);
            }
            if (this.ccApiVersion != 2) {
                output.writeInt32(2, this.ccApiVersion);
            }
            if (this.pyApiVersion != 2) {
                output.writeInt32(4, this.pyApiVersion);
            }
            if (this.javaApiVersion != 2) {
                output.writeInt32(5, this.javaApiVersion);
            }
            if (!this.javaUseJavaproto2) {
                output.writeBool(6, this.javaUseJavaproto2);
            }
            if (!this.javaJava5Enums) {
                output.writeBool(7, this.javaJava5Enums);
            }
            if (this.javaOuterClassname != null && !this.javaOuterClassname.equals("")) {
                output.writeString(8, this.javaOuterClassname);
            }
            if (this.optimizeFor != 1) {
                output.writeInt32(9, this.optimizeFor);
            }
            if (this.javaMultipleFiles) {
                output.writeBool(10, this.javaMultipleFiles);
            }
            if (this.goPackage != null && !this.goPackage.equals("")) {
                output.writeString(11, this.goPackage);
            }
            if (this.javascriptPackage != null && !this.javascriptPackage.equals("")) {
                output.writeString(12, this.javascriptPackage);
            }
            if (this.szlApiVersion != 1) {
                output.writeInt32(14, this.szlApiVersion);
            }
            if (this.ccGenericServices) {
                output.writeBool(16, this.ccGenericServices);
            }
            if (this.javaGenericServices) {
                output.writeBool(17, this.javaGenericServices);
            }
            if (this.pyGenericServices) {
                output.writeBool(18, this.pyGenericServices);
            }
            if (this.javaAltApiPackage != null && !this.javaAltApiPackage.equals("")) {
                output.writeString(19, this.javaAltApiPackage);
            }
            if (this.javaUseJavastrings) {
                output.writeBool(21, this.javaUseJavastrings);
            }
            if (this.deprecated) {
                output.writeBool(23, this.deprecated);
            }
            if (!this.ccUtf8Verification) {
                output.writeBool(24, this.ccUtf8Verification);
            }
            if (this.javaEnableDualGenerateMutableApi) {
                output.writeBool(26, this.javaEnableDualGenerateMutableApi);
            }
            if (this.javaStringCheckUtf8) {
                output.writeBool(27, this.javaStringCheckUtf8);
            }
            if (this.javaMutableApi) {
                output.writeBool(28, this.javaMutableApi);
            }
            if (this.javaMultipleFilesMutablePackage != null && !this.javaMultipleFilesMutablePackage.equals("")) {
                output.writeString(29, this.javaMultipleFilesMutablePackage);
            }
            if (this.ccEnableArenas) {
                output.writeBool(31, this.ccEnableArenas);
            }
            if (this.objcClassPrefix != null && !this.objcClassPrefix.equals("")) {
                output.writeString(36, this.objcClassPrefix);
            }
            if (this.csharpNamespace != null && !this.csharpNamespace.equals("")) {
                output.writeString(37, this.csharpNamespace);
            }
            if (this.swiftPrefix != null && !this.swiftPrefix.equals("")) {
                output.writeString(39, this.swiftPrefix);
            }
            if (this.phpClassPrefix != null && !this.phpClassPrefix.equals("")) {
                output.writeString(40, this.phpClassPrefix);
            }
            if (this.phpNamespace != null && !this.phpNamespace.equals("")) {
                output.writeString(41, this.phpNamespace);
            }
            if (this.phpGenericServices) {
                output.writeBool(42, this.phpGenericServices);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.javaPackage != null && !this.javaPackage.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.javaPackage);
            }
            if (this.ccApiVersion != 2) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, this.ccApiVersion);
            }
            if (this.pyApiVersion != 2) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, this.pyApiVersion);
            }
            if (this.javaApiVersion != 2) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(5, this.javaApiVersion);
            }
            if (!this.javaUseJavaproto2) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(6, this.javaUseJavaproto2);
            }
            if (!this.javaJava5Enums) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(7, this.javaJava5Enums);
            }
            if (this.javaOuterClassname != null && !this.javaOuterClassname.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(8, this.javaOuterClassname);
            }
            if (this.optimizeFor != 1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(9, this.optimizeFor);
            }
            if (this.javaMultipleFiles) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(10, this.javaMultipleFiles);
            }
            if (this.goPackage != null && !this.goPackage.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(11, this.goPackage);
            }
            if (this.javascriptPackage != null && !this.javascriptPackage.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(12, this.javascriptPackage);
            }
            if (this.szlApiVersion != 1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(14, this.szlApiVersion);
            }
            if (this.ccGenericServices) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(16, this.ccGenericServices);
            }
            if (this.javaGenericServices) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(17, this.javaGenericServices);
            }
            if (this.pyGenericServices) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(18, this.pyGenericServices);
            }
            if (this.javaAltApiPackage != null && !this.javaAltApiPackage.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(19, this.javaAltApiPackage);
            }
            if (this.javaUseJavastrings) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(21, this.javaUseJavastrings);
            }
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(23, this.deprecated);
            }
            if (!this.ccUtf8Verification) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(24, this.ccUtf8Verification);
            }
            if (this.javaEnableDualGenerateMutableApi) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(26, this.javaEnableDualGenerateMutableApi);
            }
            if (this.javaStringCheckUtf8) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(27, this.javaStringCheckUtf8);
            }
            if (this.javaMutableApi) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(28, this.javaMutableApi);
            }
            if (this.javaMultipleFilesMutablePackage != null && !this.javaMultipleFilesMutablePackage.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(29, this.javaMultipleFilesMutablePackage);
            }
            if (this.ccEnableArenas) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(31, this.ccEnableArenas);
            }
            if (this.objcClassPrefix != null && !this.objcClassPrefix.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(36, this.objcClassPrefix);
            }
            if (this.csharpNamespace != null && !this.csharpNamespace.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(37, this.csharpNamespace);
            }
            if (this.swiftPrefix != null && !this.swiftPrefix.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(39, this.swiftPrefix);
            }
            if (this.phpClassPrefix != null && !this.phpClassPrefix.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(40, this.phpClassPrefix);
            }
            if (this.phpNamespace != null && !this.phpNamespace.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(41, this.phpNamespace);
            }
            if (this.phpGenericServices) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(42, this.phpGenericServices);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public FileOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 10:
                        this.javaPackage = input.readString();
                        break;
                    case 16:
                        this.ccApiVersion = input.readInt32();
                        break;
                    case 32:
                        this.pyApiVersion = input.readInt32();
                        break;
                    case 40:
                        this.javaApiVersion = input.readInt32();
                        break;
                    case 48:
                        this.javaUseJavaproto2 = input.readBool();
                        break;
                    case 56:
                        this.javaJava5Enums = input.readBool();
                        break;
                    case 66:
                        this.javaOuterClassname = input.readString();
                        break;
                    case 72:
                        int position = input.getPosition();
                        try {
                            this.optimizeFor = checkOptimizeModeOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused) {
                            input.rewindToPosition(position);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 80:
                        this.javaMultipleFiles = input.readBool();
                        break;
                    case 90:
                        this.goPackage = input.readString();
                        break;
                    case 98:
                        this.javascriptPackage = input.readString();
                        break;
                    case 112:
                        this.szlApiVersion = input.readInt32();
                        break;
                    case 128:
                        this.ccGenericServices = input.readBool();
                        break;
                    case 136:
                        this.javaGenericServices = input.readBool();
                        break;
                    case 144:
                        this.pyGenericServices = input.readBool();
                        break;
                    case 154:
                        this.javaAltApiPackage = input.readString();
                        break;
                    case 168:
                        this.javaUseJavastrings = input.readBool();
                        break;
                    case 184:
                        this.deprecated = input.readBool();
                        break;
                    case 192:
                        this.ccUtf8Verification = input.readBool();
                        break;
                    case 208:
                        this.javaEnableDualGenerateMutableApi = input.readBool();
                        break;
                    case 216:
                        this.javaStringCheckUtf8 = input.readBool();
                        break;
                    case 224:
                        this.javaMutableApi = input.readBool();
                        break;
                    case 234:
                        this.javaMultipleFilesMutablePackage = input.readString();
                        break;
                    case 248:
                        this.ccEnableArenas = input.readBool();
                        break;
                    case 290:
                        this.objcClassPrefix = input.readString();
                        break;
                    case 298:
                        this.csharpNamespace = input.readString();
                        break;
                    case 314:
                        this.swiftPrefix = input.readString();
                        break;
                    case 322:
                        this.phpClassPrefix = input.readString();
                        break;
                    case 330:
                        this.phpNamespace = input.readString();
                        break;
                    case 336:
                        this.phpGenericServices = input.readBool();
                        break;
                    case 7994:
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                        int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                        UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                        }
                        while (length < uninterpretedOptionArr.length - 1) {
                            uninterpretedOptionArr[length] = new UninterpretedOption();
                            input.readMessage(uninterpretedOptionArr[length]);
                            input.readTag();
                            length++;
                        }
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        this.uninterpretedOption = uninterpretedOptionArr;
                        break;
                    default:
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static FileOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (FileOptions) MessageNano.mergeFrom(new FileOptions(), data);
        }

        public static FileOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new FileOptions().mergeFrom(input);
        }
    }

    public static final class MessageOptions extends ExtendableMessageNano<MessageOptions> {
        private static volatile MessageOptions[] _emptyArray;
        public boolean deprecated;
        public String[] experimentalJavaBuilderInterface;
        public String[] experimentalJavaInterfaceExtends;
        public String[] experimentalJavaMessageInterface;
        public boolean mapEntry;
        public boolean messageSetWireFormat;
        public boolean noStandardDescriptorAccessor;
        public UninterpretedOption[] uninterpretedOption;

        public static MessageOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new MessageOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public MessageOptions() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.messageSetWireFormat) {
                output.writeBool(1, this.messageSetWireFormat);
            }
            if (this.noStandardDescriptorAccessor) {
                output.writeBool(2, this.noStandardDescriptorAccessor);
            }
            if (this.deprecated) {
                output.writeBool(3, this.deprecated);
            }
            if (this.experimentalJavaMessageInterface != null && this.experimentalJavaMessageInterface.length > 0) {
                for (int i = 0; i < this.experimentalJavaMessageInterface.length; i++) {
                    String str = this.experimentalJavaMessageInterface[i];
                    if (str != null) {
                        output.writeString(4, str);
                    }
                }
            }
            if (this.experimentalJavaBuilderInterface != null && this.experimentalJavaBuilderInterface.length > 0) {
                for (int i2 = 0; i2 < this.experimentalJavaBuilderInterface.length; i2++) {
                    String str2 = this.experimentalJavaBuilderInterface[i2];
                    if (str2 != null) {
                        output.writeString(5, str2);
                    }
                }
            }
            if (this.experimentalJavaInterfaceExtends != null && this.experimentalJavaInterfaceExtends.length > 0) {
                for (int i3 = 0; i3 < this.experimentalJavaInterfaceExtends.length; i3++) {
                    String str3 = this.experimentalJavaInterfaceExtends[i3];
                    if (str3 != null) {
                        output.writeString(6, str3);
                    }
                }
            }
            if (this.mapEntry) {
                output.writeBool(7, this.mapEntry);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i4 = 0; i4 < this.uninterpretedOption.length; i4++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i4];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.messageSetWireFormat) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(1, this.messageSetWireFormat);
            }
            if (this.noStandardDescriptorAccessor) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(2, this.noStandardDescriptorAccessor);
            }
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(3, this.deprecated);
            }
            if (this.experimentalJavaMessageInterface != null && this.experimentalJavaMessageInterface.length > 0) {
                int iComputeStringSizeNoTag = 0;
                int i = 0;
                for (int i2 = 0; i2 < this.experimentalJavaMessageInterface.length; i2++) {
                    String str = this.experimentalJavaMessageInterface[i2];
                    if (str != null) {
                        i++;
                        iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                    }
                }
                iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag + (i * 1);
            }
            if (this.experimentalJavaBuilderInterface != null && this.experimentalJavaBuilderInterface.length > 0) {
                int iComputeStringSizeNoTag2 = 0;
                int i3 = 0;
                for (int i4 = 0; i4 < this.experimentalJavaBuilderInterface.length; i4++) {
                    String str2 = this.experimentalJavaBuilderInterface[i4];
                    if (str2 != null) {
                        i3++;
                        iComputeStringSizeNoTag2 += CodedOutputByteBufferNano.computeStringSizeNoTag(str2);
                    }
                }
                iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag2 + (i3 * 1);
            }
            if (this.experimentalJavaInterfaceExtends != null && this.experimentalJavaInterfaceExtends.length > 0) {
                int iComputeStringSizeNoTag3 = 0;
                int i5 = 0;
                for (int i6 = 0; i6 < this.experimentalJavaInterfaceExtends.length; i6++) {
                    String str3 = this.experimentalJavaInterfaceExtends[i6];
                    if (str3 != null) {
                        i5++;
                        iComputeStringSizeNoTag3 += CodedOutputByteBufferNano.computeStringSizeNoTag(str3);
                    }
                }
                iComputeSerializedSize = iComputeSerializedSize + iComputeStringSizeNoTag3 + (1 * i5);
            }
            if (this.mapEntry) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(7, this.mapEntry);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i7 = 0; i7 < this.uninterpretedOption.length; i7++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i7];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public MessageOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.messageSetWireFormat = input.readBool();
                } else if (tag == 16) {
                    this.noStandardDescriptorAccessor = input.readBool();
                } else if (tag == 24) {
                    this.deprecated = input.readBool();
                } else if (tag == 34) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 34);
                    int length = this.experimentalJavaMessageInterface == null ? 0 : this.experimentalJavaMessageInterface.length;
                    String[] strArr = new String[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.experimentalJavaMessageInterface, 0, strArr, 0, length);
                    }
                    while (length < strArr.length - 1) {
                        strArr[length] = input.readString();
                        input.readTag();
                        length++;
                    }
                    strArr[length] = input.readString();
                    this.experimentalJavaMessageInterface = strArr;
                } else if (tag == 42) {
                    int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 42);
                    int length2 = this.experimentalJavaBuilderInterface == null ? 0 : this.experimentalJavaBuilderInterface.length;
                    String[] strArr2 = new String[repeatedFieldArrayLength2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.experimentalJavaBuilderInterface, 0, strArr2, 0, length2);
                    }
                    while (length2 < strArr2.length - 1) {
                        strArr2[length2] = input.readString();
                        input.readTag();
                        length2++;
                    }
                    strArr2[length2] = input.readString();
                    this.experimentalJavaBuilderInterface = strArr2;
                } else if (tag == 50) {
                    int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                    int length3 = this.experimentalJavaInterfaceExtends == null ? 0 : this.experimentalJavaInterfaceExtends.length;
                    String[] strArr3 = new String[repeatedFieldArrayLength3 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.experimentalJavaInterfaceExtends, 0, strArr3, 0, length3);
                    }
                    while (length3 < strArr3.length - 1) {
                        strArr3[length3] = input.readString();
                        input.readTag();
                        length3++;
                    }
                    strArr3[length3] = input.readString();
                    this.experimentalJavaInterfaceExtends = strArr3;
                } else if (tag == 56) {
                    this.mapEntry = input.readBool();
                } else if (tag != 7994) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength4 = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                    int length4 = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                    UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength4 + length4];
                    if (length4 != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length4);
                    }
                    while (length4 < uninterpretedOptionArr.length - 1) {
                        uninterpretedOptionArr[length4] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length4]);
                        input.readTag();
                        length4++;
                    }
                    uninterpretedOptionArr[length4] = new UninterpretedOption();
                    input.readMessage(uninterpretedOptionArr[length4]);
                    this.uninterpretedOption = uninterpretedOptionArr;
                }
            }
        }

        public static MessageOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (MessageOptions) MessageNano.mergeFrom(new MessageOptions(), data);
        }

        public static MessageOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new MessageOptions().mergeFrom(input);
        }
    }

    public static final class FieldOptions extends ExtendableMessageNano<FieldOptions> {
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

        public interface CType {

            @NanoEnumValue(legacy = false, value = CType.class)
            public static final int CORD = 1;

            @NanoEnumValue(legacy = false, value = CType.class)
            public static final int STRING = 0;

            @NanoEnumValue(legacy = false, value = CType.class)
            public static final int STRING_PIECE = 2;
        }

        public interface JSType {

            @NanoEnumValue(legacy = false, value = JSType.class)
            public static final int JS_NORMAL = 0;

            @NanoEnumValue(legacy = false, value = JSType.class)
            public static final int JS_NUMBER = 2;

            @NanoEnumValue(legacy = false, value = JSType.class)
            public static final int JS_STRING = 1;
        }

        @NanoEnumValue(legacy = false, value = CType.class)
        public static int checkCTypeOrThrow(int value) {
            if (value >= 0 && value <= 2) {
                return value;
            }
            StringBuilder sb = new StringBuilder(37);
            sb.append(value);
            sb.append(" is not a valid enum CType");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = CType.class)
        public static int[] checkCTypeOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkCTypeOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = JSType.class)
        public static int checkJSTypeOrThrow(int value) {
            if (value >= 0 && value <= 2) {
                return value;
            }
            StringBuilder sb = new StringBuilder(38);
            sb.append(value);
            sb.append(" is not a valid enum JSType");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = JSType.class)
        public static int[] checkJSTypeOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkJSTypeOrThrow(i);
            }
            return iArr;
        }

        public static final class UpgradedOption extends ExtendableMessageNano<UpgradedOption> {
            private static volatile UpgradedOption[] _emptyArray;
            public String name;
            public String value;

            public static UpgradedOption[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new UpgradedOption[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public UpgradedOption() {
                clear();
            }

            public UpgradedOption clear() {
                this.name = "";
                this.value = "";
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.name != null && !this.name.equals("")) {
                    output.writeString(1, this.name);
                }
                if (this.value != null && !this.value.equals("")) {
                    output.writeString(2, this.value);
                }
                super.writeTo(output);
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            protected int computeSerializedSize() {
                int iComputeSerializedSize = super.computeSerializedSize();
                if (this.name != null && !this.name.equals("")) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.name);
                }
                return (this.value == null || this.value.equals("")) ? iComputeSerializedSize : iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.value);
            }

            @Override // com.google.protobuf.nano.MessageNano
            public UpgradedOption mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 10) {
                        this.name = input.readString();
                    } else if (tag != 18) {
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        this.value = input.readString();
                    }
                }
            }

            public static UpgradedOption parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (UpgradedOption) MessageNano.mergeFrom(new UpgradedOption(), data);
            }

            public static UpgradedOption parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new UpgradedOption().mergeFrom(input);
            }
        }

        public static FieldOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new FieldOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public FieldOptions() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.ctype != 0) {
                output.writeInt32(1, this.ctype);
            }
            if (this.packed) {
                output.writeBool(2, this.packed);
            }
            if (this.deprecated) {
                output.writeBool(3, this.deprecated);
            }
            if (this.lazy) {
                output.writeBool(5, this.lazy);
            }
            if (this.jstype != 0) {
                output.writeInt32(6, this.jstype);
            }
            if (this.weak) {
                output.writeBool(10, this.weak);
            }
            if (this.upgradedOption != null && this.upgradedOption.length > 0) {
                for (int i = 0; i < this.upgradedOption.length; i++) {
                    UpgradedOption upgradedOption = this.upgradedOption[i];
                    if (upgradedOption != null) {
                        output.writeMessage(11, upgradedOption);
                    }
                }
            }
            if (this.deprecatedRawMessage) {
                output.writeBool(12, this.deprecatedRawMessage);
            }
            if (!this.enforceUtf8) {
                output.writeBool(13, this.enforceUtf8);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i2 = 0; i2 < this.uninterpretedOption.length; i2++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i2];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.ctype != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, this.ctype);
            }
            if (this.packed) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(2, this.packed);
            }
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(3, this.deprecated);
            }
            if (this.lazy) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, this.lazy);
            }
            if (this.jstype != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, this.jstype);
            }
            if (this.weak) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(10, this.weak);
            }
            if (this.upgradedOption != null && this.upgradedOption.length > 0) {
                int iComputeMessageSize = iComputeSerializedSize;
                for (int i = 0; i < this.upgradedOption.length; i++) {
                    UpgradedOption upgradedOption = this.upgradedOption[i];
                    if (upgradedOption != null) {
                        iComputeMessageSize += CodedOutputByteBufferNano.computeMessageSize(11, upgradedOption);
                    }
                }
                iComputeSerializedSize = iComputeMessageSize;
            }
            if (this.deprecatedRawMessage) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(12, this.deprecatedRawMessage);
            }
            if (!this.enforceUtf8) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(13, this.enforceUtf8);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i2 = 0; i2 < this.uninterpretedOption.length; i2++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i2];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public FieldOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        int position = input.getPosition();
                        try {
                            this.ctype = checkCTypeOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused) {
                            input.rewindToPosition(position);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 16:
                        this.packed = input.readBool();
                        break;
                    case 24:
                        this.deprecated = input.readBool();
                        break;
                    case 40:
                        this.lazy = input.readBool();
                        break;
                    case 48:
                        int position2 = input.getPosition();
                        try {
                            this.jstype = checkJSTypeOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused2) {
                            input.rewindToPosition(position2);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 80:
                        this.weak = input.readBool();
                        break;
                    case 90:
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 90);
                        int length = this.upgradedOption == null ? 0 : this.upgradedOption.length;
                        UpgradedOption[] upgradedOptionArr = new UpgradedOption[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.upgradedOption, 0, upgradedOptionArr, 0, length);
                        }
                        while (length < upgradedOptionArr.length - 1) {
                            upgradedOptionArr[length] = new UpgradedOption();
                            input.readMessage(upgradedOptionArr[length]);
                            input.readTag();
                            length++;
                        }
                        upgradedOptionArr[length] = new UpgradedOption();
                        input.readMessage(upgradedOptionArr[length]);
                        this.upgradedOption = upgradedOptionArr;
                        break;
                    case 96:
                        this.deprecatedRawMessage = input.readBool();
                        break;
                    case 104:
                        this.enforceUtf8 = input.readBool();
                        break;
                    case 7994:
                        int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                        int length2 = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                        UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength2 + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length2);
                        }
                        while (length2 < uninterpretedOptionArr.length - 1) {
                            uninterpretedOptionArr[length2] = new UninterpretedOption();
                            input.readMessage(uninterpretedOptionArr[length2]);
                            input.readTag();
                            length2++;
                        }
                        uninterpretedOptionArr[length2] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length2]);
                        this.uninterpretedOption = uninterpretedOptionArr;
                        break;
                    default:
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static FieldOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (FieldOptions) MessageNano.mergeFrom(new FieldOptions(), data);
        }

        public static FieldOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new FieldOptions().mergeFrom(input);
        }
    }

    public static final class OneofOptions extends ExtendableMessageNano<OneofOptions> {
        private static volatile OneofOptions[] _emptyArray;
        public UninterpretedOption[] uninterpretedOption;

        public static OneofOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new OneofOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public OneofOptions() {
            clear();
        }

        public OneofOptions clear() {
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public OneofOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 7994) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                    int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                    UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                    }
                    while (length < uninterpretedOptionArr.length - 1) {
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        input.readTag();
                        length++;
                    }
                    uninterpretedOptionArr[length] = new UninterpretedOption();
                    input.readMessage(uninterpretedOptionArr[length]);
                    this.uninterpretedOption = uninterpretedOptionArr;
                }
            }
        }

        public static OneofOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (OneofOptions) MessageNano.mergeFrom(new OneofOptions(), data);
        }

        public static OneofOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new OneofOptions().mergeFrom(input);
        }
    }

    public static final class EnumOptions extends ExtendableMessageNano<EnumOptions> {
        private static volatile EnumOptions[] _emptyArray;
        public boolean allowAlias;
        public boolean deprecated;
        public String proto1Name;
        public UninterpretedOption[] uninterpretedOption;

        public static EnumOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new EnumOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public EnumOptions() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.proto1Name != null && !this.proto1Name.equals("")) {
                output.writeString(1, this.proto1Name);
            }
            if (this.allowAlias) {
                output.writeBool(2, this.allowAlias);
            }
            if (this.deprecated) {
                output.writeBool(3, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.proto1Name != null && !this.proto1Name.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.proto1Name);
            }
            if (this.allowAlias) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(2, this.allowAlias);
            }
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(3, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public EnumOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.proto1Name = input.readString();
                } else if (tag == 16) {
                    this.allowAlias = input.readBool();
                } else if (tag == 24) {
                    this.deprecated = input.readBool();
                } else if (tag != 7994) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                    int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                    UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                    }
                    while (length < uninterpretedOptionArr.length - 1) {
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        input.readTag();
                        length++;
                    }
                    uninterpretedOptionArr[length] = new UninterpretedOption();
                    input.readMessage(uninterpretedOptionArr[length]);
                    this.uninterpretedOption = uninterpretedOptionArr;
                }
            }
        }

        public static EnumOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (EnumOptions) MessageNano.mergeFrom(new EnumOptions(), data);
        }

        public static EnumOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new EnumOptions().mergeFrom(input);
        }
    }

    public static final class EnumValueOptions extends ExtendableMessageNano<EnumValueOptions> {
        private static volatile EnumValueOptions[] _emptyArray;
        public boolean deprecated;
        public UninterpretedOption[] uninterpretedOption;

        public static EnumValueOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new EnumValueOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public EnumValueOptions() {
            clear();
        }

        public EnumValueOptions clear() {
            this.deprecated = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.deprecated) {
                output.writeBool(1, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(1, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public EnumValueOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 8) {
                    this.deprecated = input.readBool();
                } else if (tag != 7994) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                    int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                    UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                    }
                    while (length < uninterpretedOptionArr.length - 1) {
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        input.readTag();
                        length++;
                    }
                    uninterpretedOptionArr[length] = new UninterpretedOption();
                    input.readMessage(uninterpretedOptionArr[length]);
                    this.uninterpretedOption = uninterpretedOptionArr;
                }
            }
        }

        public static EnumValueOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (EnumValueOptions) MessageNano.mergeFrom(new EnumValueOptions(), data);
        }

        public static EnumValueOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new EnumValueOptions().mergeFrom(input);
        }
    }

    public static final class ServiceOptions extends ExtendableMessageNano<ServiceOptions> {
        private static volatile ServiceOptions[] _emptyArray;
        public boolean deprecated;
        public double failureDetectionDelay;
        public boolean multicastStub;
        public UninterpretedOption[] uninterpretedOption;

        public static ServiceOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ServiceOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public ServiceOptions() {
            clear();
        }

        public ServiceOptions clear() {
            this.multicastStub = false;
            this.failureDetectionDelay = -1.0d;
            this.deprecated = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (Double.doubleToLongBits(this.failureDetectionDelay) != Double.doubleToLongBits(-1.0d)) {
                output.writeDouble(16, this.failureDetectionDelay);
            }
            if (this.multicastStub) {
                output.writeBool(20, this.multicastStub);
            }
            if (this.deprecated) {
                output.writeBool(33, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (Double.doubleToLongBits(this.failureDetectionDelay) != Double.doubleToLongBits(-1.0d)) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeDoubleSize(16, this.failureDetectionDelay);
            }
            if (this.multicastStub) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(20, this.multicastStub);
            }
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(33, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public ServiceOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 129) {
                    this.failureDetectionDelay = input.readDouble();
                } else if (tag == 160) {
                    this.multicastStub = input.readBool();
                } else if (tag == 264) {
                    this.deprecated = input.readBool();
                } else if (tag != 7994) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                    int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                    UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                    }
                    while (length < uninterpretedOptionArr.length - 1) {
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        input.readTag();
                        length++;
                    }
                    uninterpretedOptionArr[length] = new UninterpretedOption();
                    input.readMessage(uninterpretedOptionArr[length]);
                    this.uninterpretedOption = uninterpretedOptionArr;
                }
            }
        }

        public static ServiceOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (ServiceOptions) MessageNano.mergeFrom(new ServiceOptions(), data);
        }

        public static ServiceOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new ServiceOptions().mergeFrom(input);
        }
    }

    public static final class MethodOptions extends ExtendableMessageNano<MethodOptions> {
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

        public interface Format {

            @NanoEnumValue(legacy = false, value = Format.class)
            public static final int UNCOMPRESSED = 0;

            @NanoEnumValue(legacy = false, value = Format.class)
            public static final int ZIPPY_COMPRESSED = 1;
        }

        public interface IdempotencyLevel {

            @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
            public static final int IDEMPOTENCY_UNKNOWN = 0;

            @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
            public static final int IDEMPOTENT = 2;

            @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
            public static final int NO_SIDE_EFFECTS = 1;
        }

        public interface LogLevel {

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

        public interface Protocol {

            @NanoEnumValue(legacy = false, value = Protocol.class)
            public static final int TCP = 0;

            @NanoEnumValue(legacy = false, value = Protocol.class)
            public static final int UDP = 1;
        }

        public interface SecurityLevel {

            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int INTEGRITY = 1;

            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int NONE = 0;

            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int PRIVACY_AND_INTEGRITY = 2;

            @NanoEnumValue(legacy = false, value = SecurityLevel.class)
            public static final int STRONG_PRIVACY_AND_INTEGRITY = 3;
        }

        public interface TokenUnit {

            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int BYTE = 1;

            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int MESSAGE = 0;
        }

        @NanoEnumValue(legacy = false, value = Protocol.class)
        public static int checkProtocolOrThrow(int value) {
            if (value >= 0 && value <= 1) {
                return value;
            }
            StringBuilder sb = new StringBuilder(40);
            sb.append(value);
            sb.append(" is not a valid enum Protocol");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = Protocol.class)
        public static int[] checkProtocolOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkProtocolOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = SecurityLevel.class)
        public static int checkSecurityLevelOrThrow(int value) {
            if (value >= 0 && value <= 3) {
                return value;
            }
            StringBuilder sb = new StringBuilder(45);
            sb.append(value);
            sb.append(" is not a valid enum SecurityLevel");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = SecurityLevel.class)
        public static int[] checkSecurityLevelOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkSecurityLevelOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = Format.class)
        public static int checkFormatOrThrow(int value) {
            if (value >= 0 && value <= 1) {
                return value;
            }
            StringBuilder sb = new StringBuilder(38);
            sb.append(value);
            sb.append(" is not a valid enum Format");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = Format.class)
        public static int[] checkFormatOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkFormatOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = LogLevel.class)
        public static int checkLogLevelOrThrow(int value) {
            if (value >= 0 && value <= 4) {
                return value;
            }
            StringBuilder sb = new StringBuilder(40);
            sb.append(value);
            sb.append(" is not a valid enum LogLevel");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = LogLevel.class)
        public static int[] checkLogLevelOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkLogLevelOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int checkTokenUnitOrThrow(int value) {
            if (value >= 0 && value <= 1) {
                return value;
            }
            StringBuilder sb = new StringBuilder(41);
            sb.append(value);
            sb.append(" is not a valid enum TokenUnit");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int[] checkTokenUnitOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkTokenUnitOrThrow(i);
            }
            return iArr;
        }

        @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
        public static int checkIdempotencyLevelOrThrow(int value) {
            if (value >= 0 && value <= 2) {
                return value;
            }
            StringBuilder sb = new StringBuilder(48);
            sb.append(value);
            sb.append(" is not a valid enum IdempotencyLevel");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = IdempotencyLevel.class)
        public static int[] checkIdempotencyLevelOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkIdempotencyLevelOrThrow(i);
            }
            return iArr;
        }

        public static MethodOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new MethodOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public MethodOptions() {
            clear();
        }

        public MethodOptions clear() {
            this.protocol = 0;
            this.deadline = -1.0d;
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.protocol != 0) {
                output.writeInt32(7, this.protocol);
            }
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0d)) {
                output.writeDouble(8, this.deadline);
            }
            if (this.duplicateSuppression) {
                output.writeBool(9, this.duplicateSuppression);
            }
            if (this.failFast) {
                output.writeBool(10, this.failFast);
            }
            if (this.clientLogging != 256) {
                output.writeSInt32(11, this.clientLogging);
            }
            if (this.serverLogging != 256) {
                output.writeSInt32(12, this.serverLogging);
            }
            if (this.securityLevel != 0) {
                output.writeInt32(13, this.securityLevel);
            }
            if (this.responseFormat != 0) {
                output.writeInt32(15, this.responseFormat);
            }
            if (this.requestFormat != 0) {
                output.writeInt32(17, this.requestFormat);
            }
            if (this.streamType != null && !this.streamType.equals("")) {
                output.writeString(18, this.streamType);
            }
            if (this.securityLabel != null && !this.securityLabel.equals("")) {
                output.writeString(19, this.securityLabel);
            }
            if (this.clientStreaming) {
                output.writeBool(20, this.clientStreaming);
            }
            if (this.serverStreaming) {
                output.writeBool(21, this.serverStreaming);
            }
            if (this.legacyStreamType != null && !this.legacyStreamType.equals("")) {
                output.writeString(22, this.legacyStreamType);
            }
            if (this.legacyResultType != null && !this.legacyResultType.equals("")) {
                output.writeString(23, this.legacyResultType);
            }
            if (this.legacyClientInitialTokens != -1) {
                output.writeInt64(24, this.legacyClientInitialTokens);
            }
            if (this.legacyServerInitialTokens != -1) {
                output.writeInt64(25, this.legacyServerInitialTokens);
            }
            if (this.endUserCredsRequested) {
                output.writeBool(26, this.endUserCredsRequested);
            }
            if (this.logLevel != 2) {
                output.writeInt32(27, this.logLevel);
            }
            if (this.legacyTokenUnit != 1) {
                output.writeInt32(28, this.legacyTokenUnit);
            }
            if (this.goLegacyChannelApi) {
                output.writeBool(29, this.goLegacyChannelApi);
            }
            if (this.deprecated) {
                output.writeBool(33, this.deprecated);
            }
            if (this.idempotencyLevel != 0) {
                output.writeInt32(34, this.idempotencyLevel);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.protocol != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, this.protocol);
            }
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0d)) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeDoubleSize(8, this.deadline);
            }
            if (this.duplicateSuppression) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(9, this.duplicateSuppression);
            }
            if (this.failFast) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(10, this.failFast);
            }
            if (this.clientLogging != 256) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeSInt32Size(11, this.clientLogging);
            }
            if (this.serverLogging != 256) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeSInt32Size(12, this.serverLogging);
            }
            if (this.securityLevel != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(13, this.securityLevel);
            }
            if (this.responseFormat != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(15, this.responseFormat);
            }
            if (this.requestFormat != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(17, this.requestFormat);
            }
            if (this.streamType != null && !this.streamType.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(18, this.streamType);
            }
            if (this.securityLabel != null && !this.securityLabel.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(19, this.securityLabel);
            }
            if (this.clientStreaming) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(20, this.clientStreaming);
            }
            if (this.serverStreaming) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(21, this.serverStreaming);
            }
            if (this.legacyStreamType != null && !this.legacyStreamType.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(22, this.legacyStreamType);
            }
            if (this.legacyResultType != null && !this.legacyResultType.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(23, this.legacyResultType);
            }
            if (this.legacyClientInitialTokens != -1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(24, this.legacyClientInitialTokens);
            }
            if (this.legacyServerInitialTokens != -1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(25, this.legacyServerInitialTokens);
            }
            if (this.endUserCredsRequested) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(26, this.endUserCredsRequested);
            }
            if (this.logLevel != 2) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(27, this.logLevel);
            }
            if (this.legacyTokenUnit != 1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(28, this.legacyTokenUnit);
            }
            if (this.goLegacyChannelApi) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(29, this.goLegacyChannelApi);
            }
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(33, this.deprecated);
            }
            if (this.idempotencyLevel != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(34, this.idempotencyLevel);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public MethodOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 56:
                        int position = input.getPosition();
                        try {
                            this.protocol = checkProtocolOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused) {
                            input.rewindToPosition(position);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 65:
                        this.deadline = input.readDouble();
                        break;
                    case 72:
                        this.duplicateSuppression = input.readBool();
                        break;
                    case 80:
                        this.failFast = input.readBool();
                        break;
                    case 88:
                        this.clientLogging = input.readSInt32();
                        break;
                    case 96:
                        this.serverLogging = input.readSInt32();
                        break;
                    case 104:
                        int position2 = input.getPosition();
                        try {
                            this.securityLevel = checkSecurityLevelOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused2) {
                            input.rewindToPosition(position2);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 120:
                        int position3 = input.getPosition();
                        try {
                            this.responseFormat = checkFormatOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused3) {
                            input.rewindToPosition(position3);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 136:
                        int position4 = input.getPosition();
                        try {
                            this.requestFormat = checkFormatOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused4) {
                            input.rewindToPosition(position4);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 146:
                        this.streamType = input.readString();
                        break;
                    case 154:
                        this.securityLabel = input.readString();
                        break;
                    case 160:
                        this.clientStreaming = input.readBool();
                        break;
                    case 168:
                        this.serverStreaming = input.readBool();
                        break;
                    case 178:
                        this.legacyStreamType = input.readString();
                        break;
                    case 186:
                        this.legacyResultType = input.readString();
                        break;
                    case 192:
                        this.legacyClientInitialTokens = input.readInt64();
                        break;
                    case 200:
                        this.legacyServerInitialTokens = input.readInt64();
                        break;
                    case 208:
                        this.endUserCredsRequested = input.readBool();
                        break;
                    case 216:
                        int position5 = input.getPosition();
                        try {
                            this.logLevel = checkLogLevelOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused5) {
                            input.rewindToPosition(position5);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 224:
                        int position6 = input.getPosition();
                        try {
                            this.legacyTokenUnit = checkTokenUnitOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused6) {
                            input.rewindToPosition(position6);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 232:
                        this.goLegacyChannelApi = input.readBool();
                        break;
                    case 264:
                        this.deprecated = input.readBool();
                        break;
                    case 272:
                        int position7 = input.getPosition();
                        try {
                            this.idempotencyLevel = checkIdempotencyLevelOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused7) {
                            input.rewindToPosition(position7);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 7994:
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                        int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                        UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                        }
                        while (length < uninterpretedOptionArr.length - 1) {
                            uninterpretedOptionArr[length] = new UninterpretedOption();
                            input.readMessage(uninterpretedOptionArr[length]);
                            input.readTag();
                            length++;
                        }
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        this.uninterpretedOption = uninterpretedOptionArr;
                        break;
                    default:
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static MethodOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (MethodOptions) MessageNano.mergeFrom(new MethodOptions(), data);
        }

        public static MethodOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new MethodOptions().mergeFrom(input);
        }
    }

    public static final class StreamOptions extends ExtendableMessageNano<StreamOptions> {
        private static volatile StreamOptions[] _emptyArray;
        public long clientInitialTokens;
        public int clientLogging;
        public double deadline;
        public boolean deprecated;
        public boolean endUserCredsRequested;
        public boolean failFast;

        @NanoEnumValue(legacy = false, value = MethodOptions.LogLevel.class)
        public int logLevel;
        public String securityLabel;

        @NanoEnumValue(legacy = false, value = MethodOptions.SecurityLevel.class)
        public int securityLevel;
        public long serverInitialTokens;
        public int serverLogging;

        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public int tokenUnit;
        public UninterpretedOption[] uninterpretedOption;

        public interface TokenUnit {

            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int BYTE = 1;

            @NanoEnumValue(legacy = false, value = TokenUnit.class)
            public static final int MESSAGE = 0;
        }

        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int checkTokenUnitOrThrow(int value) {
            if (value >= 0 && value <= 1) {
                return value;
            }
            StringBuilder sb = new StringBuilder(41);
            sb.append(value);
            sb.append(" is not a valid enum TokenUnit");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = TokenUnit.class)
        public static int[] checkTokenUnitOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkTokenUnitOrThrow(i);
            }
            return iArr;
        }

        public static StreamOptions[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new StreamOptions[0];
                    }
                }
            }
            return _emptyArray;
        }

        public StreamOptions() {
            clear();
        }

        public StreamOptions clear() {
            this.clientInitialTokens = -1L;
            this.serverInitialTokens = -1L;
            this.tokenUnit = 0;
            this.securityLevel = 0;
            this.securityLabel = "";
            this.clientLogging = 256;
            this.serverLogging = 256;
            this.deadline = -1.0d;
            this.failFast = false;
            this.endUserCredsRequested = false;
            this.logLevel = 2;
            this.deprecated = false;
            this.uninterpretedOption = UninterpretedOption.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.clientInitialTokens != -1) {
                output.writeInt64(1, this.clientInitialTokens);
            }
            if (this.serverInitialTokens != -1) {
                output.writeInt64(2, this.serverInitialTokens);
            }
            if (this.tokenUnit != 0) {
                output.writeInt32(3, this.tokenUnit);
            }
            if (this.securityLevel != 0) {
                output.writeInt32(4, this.securityLevel);
            }
            if (this.securityLabel != null && !this.securityLabel.equals("")) {
                output.writeString(5, this.securityLabel);
            }
            if (this.clientLogging != 256) {
                output.writeInt32(6, this.clientLogging);
            }
            if (this.serverLogging != 256) {
                output.writeInt32(7, this.serverLogging);
            }
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0d)) {
                output.writeDouble(8, this.deadline);
            }
            if (this.failFast) {
                output.writeBool(9, this.failFast);
            }
            if (this.endUserCredsRequested) {
                output.writeBool(10, this.endUserCredsRequested);
            }
            if (this.logLevel != 2) {
                output.writeInt32(11, this.logLevel);
            }
            if (this.deprecated) {
                output.writeBool(33, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        output.writeMessage(999, uninterpretedOption);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.clientInitialTokens != -1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(1, this.clientInitialTokens);
            }
            if (this.serverInitialTokens != -1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(2, this.serverInitialTokens);
            }
            if (this.tokenUnit != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, this.tokenUnit);
            }
            if (this.securityLevel != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, this.securityLevel);
            }
            if (this.securityLabel != null && !this.securityLabel.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(5, this.securityLabel);
            }
            if (this.clientLogging != 256) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(6, this.clientLogging);
            }
            if (this.serverLogging != 256) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(7, this.serverLogging);
            }
            if (Double.doubleToLongBits(this.deadline) != Double.doubleToLongBits(-1.0d)) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeDoubleSize(8, this.deadline);
            }
            if (this.failFast) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(9, this.failFast);
            }
            if (this.endUserCredsRequested) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(10, this.endUserCredsRequested);
            }
            if (this.logLevel != 2) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(11, this.logLevel);
            }
            if (this.deprecated) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(33, this.deprecated);
            }
            if (this.uninterpretedOption != null && this.uninterpretedOption.length > 0) {
                for (int i = 0; i < this.uninterpretedOption.length; i++) {
                    UninterpretedOption uninterpretedOption = this.uninterpretedOption[i];
                    if (uninterpretedOption != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(999, uninterpretedOption);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public StreamOptions mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    case 8:
                        this.clientInitialTokens = input.readInt64();
                        break;
                    case 16:
                        this.serverInitialTokens = input.readInt64();
                        break;
                    case 24:
                        int position = input.getPosition();
                        try {
                            this.tokenUnit = checkTokenUnitOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused) {
                            input.rewindToPosition(position);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 32:
                        int position2 = input.getPosition();
                        try {
                            this.securityLevel = MethodOptions.checkSecurityLevelOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused2) {
                            input.rewindToPosition(position2);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 42:
                        this.securityLabel = input.readString();
                        break;
                    case 48:
                        this.clientLogging = input.readInt32();
                        break;
                    case 56:
                        this.serverLogging = input.readInt32();
                        break;
                    case 65:
                        this.deadline = input.readDouble();
                        break;
                    case 72:
                        this.failFast = input.readBool();
                        break;
                    case 80:
                        this.endUserCredsRequested = input.readBool();
                        break;
                    case 88:
                        int position3 = input.getPosition();
                        try {
                            this.logLevel = MethodOptions.checkLogLevelOrThrow(input.readInt32());
                            break;
                        } catch (IllegalArgumentException unused3) {
                            input.rewindToPosition(position3);
                            storeUnknownField(input, tag);
                            break;
                        }
                    case 264:
                        this.deprecated = input.readBool();
                        break;
                    case 7994:
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 7994);
                        int length = this.uninterpretedOption == null ? 0 : this.uninterpretedOption.length;
                        UninterpretedOption[] uninterpretedOptionArr = new UninterpretedOption[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.uninterpretedOption, 0, uninterpretedOptionArr, 0, length);
                        }
                        while (length < uninterpretedOptionArr.length - 1) {
                            uninterpretedOptionArr[length] = new UninterpretedOption();
                            input.readMessage(uninterpretedOptionArr[length]);
                            input.readTag();
                            length++;
                        }
                        uninterpretedOptionArr[length] = new UninterpretedOption();
                        input.readMessage(uninterpretedOptionArr[length]);
                        this.uninterpretedOption = uninterpretedOptionArr;
                        break;
                    default:
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                }
            }
        }

        public static StreamOptions parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (StreamOptions) MessageNano.mergeFrom(new StreamOptions(), data);
        }

        public static StreamOptions parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new StreamOptions().mergeFrom(input);
        }
    }

    public static final class UninterpretedOption extends ExtendableMessageNano<UninterpretedOption> {
        private static volatile UninterpretedOption[] _emptyArray;
        public String aggregateValue;
        public double doubleValue;
        public String identifierValue;
        public NamePart[] name;
        public long negativeIntValue;
        public long positiveIntValue;
        public byte[] stringValue;

        public static final class NamePart extends ExtendableMessageNano<NamePart> {
            private static volatile NamePart[] _emptyArray;
            public boolean isExtension;
            public String namePart;

            public static NamePart[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new NamePart[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public NamePart() {
                clear();
            }

            public NamePart clear() {
                this.namePart = "";
                this.isExtension = false;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                output.writeString(1, this.namePart);
                output.writeBool(2, this.isExtension);
                super.writeTo(output);
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            protected int computeSerializedSize() {
                return super.computeSerializedSize() + CodedOutputByteBufferNano.computeStringSize(1, this.namePart) + CodedOutputByteBufferNano.computeBoolSize(2, this.isExtension);
            }

            @Override // com.google.protobuf.nano.MessageNano
            public NamePart mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 10) {
                        this.namePart = input.readString();
                    } else if (tag != 16) {
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        this.isExtension = input.readBool();
                    }
                }
            }

            public static NamePart parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (NamePart) MessageNano.mergeFrom(new NamePart(), data);
            }

            public static NamePart parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new NamePart().mergeFrom(input);
            }
        }

        public static UninterpretedOption[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new UninterpretedOption[0];
                    }
                }
            }
            return _emptyArray;
        }

        public UninterpretedOption() {
            clear();
        }

        public UninterpretedOption clear() {
            this.name = NamePart.emptyArray();
            this.identifierValue = "";
            this.positiveIntValue = 0L;
            this.negativeIntValue = 0L;
            this.doubleValue = 0.0d;
            this.stringValue = WireFormatNano.EMPTY_BYTES;
            this.aggregateValue = "";
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.name != null && this.name.length > 0) {
                for (int i = 0; i < this.name.length; i++) {
                    NamePart namePart = this.name[i];
                    if (namePart != null) {
                        output.writeMessage(2, namePart);
                    }
                }
            }
            if (this.identifierValue != null && !this.identifierValue.equals("")) {
                output.writeString(3, this.identifierValue);
            }
            if (this.positiveIntValue != 0) {
                output.writeUInt64(4, this.positiveIntValue);
            }
            if (this.negativeIntValue != 0) {
                output.writeInt64(5, this.negativeIntValue);
            }
            if (Double.doubleToLongBits(this.doubleValue) != Double.doubleToLongBits(0.0d)) {
                output.writeDouble(6, this.doubleValue);
            }
            if (!Arrays.equals(this.stringValue, WireFormatNano.EMPTY_BYTES)) {
                output.writeBytes(7, this.stringValue);
            }
            if (this.aggregateValue != null && !this.aggregateValue.equals("")) {
                output.writeString(8, this.aggregateValue);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.name != null && this.name.length > 0) {
                for (int i = 0; i < this.name.length; i++) {
                    NamePart namePart = this.name[i];
                    if (namePart != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, namePart);
                    }
                }
            }
            if (this.identifierValue != null && !this.identifierValue.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.identifierValue);
            }
            if (this.positiveIntValue != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeUInt64Size(4, this.positiveIntValue);
            }
            if (this.negativeIntValue != 0) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(5, this.negativeIntValue);
            }
            if (Double.doubleToLongBits(this.doubleValue) != Double.doubleToLongBits(0.0d)) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeDoubleSize(6, this.doubleValue);
            }
            if (!Arrays.equals(this.stringValue, WireFormatNano.EMPTY_BYTES)) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeBytesSize(7, this.stringValue);
            }
            return (this.aggregateValue == null || this.aggregateValue.equals("")) ? iComputeSerializedSize : iComputeSerializedSize + CodedOutputByteBufferNano.computeStringSize(8, this.aggregateValue);
        }

        @Override // com.google.protobuf.nano.MessageNano
        public UninterpretedOption mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 18) {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 18);
                    int length = this.name == null ? 0 : this.name.length;
                    NamePart[] namePartArr = new NamePart[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.name, 0, namePartArr, 0, length);
                    }
                    while (length < namePartArr.length - 1) {
                        namePartArr[length] = new NamePart();
                        input.readMessage(namePartArr[length]);
                        input.readTag();
                        length++;
                    }
                    namePartArr[length] = new NamePart();
                    input.readMessage(namePartArr[length]);
                    this.name = namePartArr;
                } else if (tag == 26) {
                    this.identifierValue = input.readString();
                } else if (tag == 32) {
                    this.positiveIntValue = input.readUInt64();
                } else if (tag == 40) {
                    this.negativeIntValue = input.readInt64();
                } else if (tag == 49) {
                    this.doubleValue = input.readDouble();
                } else if (tag == 58) {
                    this.stringValue = input.readBytes();
                } else if (tag != 66) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    this.aggregateValue = input.readString();
                }
            }
        }

        public static UninterpretedOption parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (UninterpretedOption) MessageNano.mergeFrom(new UninterpretedOption(), data);
        }

        public static UninterpretedOption parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new UninterpretedOption().mergeFrom(input);
        }
    }

    public static final class SourceCodeInfo extends ExtendableMessageNano<SourceCodeInfo> {
        private static volatile SourceCodeInfo[] _emptyArray;
        public Location[] location;

        public static final class Location extends ExtendableMessageNano<Location> {
            private static volatile Location[] _emptyArray;
            public String leadingComments;
            public String[] leadingDetachedComments;
            public int[] path;
            public int[] span;
            public String trailingComments;

            public static Location[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new Location[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public Location() {
                clear();
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

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.path != null && this.path.length > 0) {
                    int iComputeInt32SizeNoTag = 0;
                    for (int i = 0; i < this.path.length; i++) {
                        iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                    }
                    output.writeRawVarint32(10);
                    output.writeRawVarint32(iComputeInt32SizeNoTag);
                    for (int i2 = 0; i2 < this.path.length; i2++) {
                        output.writeInt32NoTag(this.path[i2]);
                    }
                }
                if (this.span != null && this.span.length > 0) {
                    int iComputeInt32SizeNoTag2 = 0;
                    for (int i3 = 0; i3 < this.span.length; i3++) {
                        iComputeInt32SizeNoTag2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.span[i3]);
                    }
                    output.writeRawVarint32(18);
                    output.writeRawVarint32(iComputeInt32SizeNoTag2);
                    for (int i4 = 0; i4 < this.span.length; i4++) {
                        output.writeInt32NoTag(this.span[i4]);
                    }
                }
                if (this.leadingComments != null && !this.leadingComments.equals("")) {
                    output.writeString(3, this.leadingComments);
                }
                if (this.trailingComments != null && !this.trailingComments.equals("")) {
                    output.writeString(4, this.trailingComments);
                }
                if (this.leadingDetachedComments != null && this.leadingDetachedComments.length > 0) {
                    for (int i5 = 0; i5 < this.leadingDetachedComments.length; i5++) {
                        String str = this.leadingDetachedComments[i5];
                        if (str != null) {
                            output.writeString(6, str);
                        }
                    }
                }
                super.writeTo(output);
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            protected int computeSerializedSize() {
                int iComputeSerializedSize = super.computeSerializedSize();
                if (this.path != null && this.path.length > 0) {
                    int iComputeInt32SizeNoTag = 0;
                    for (int i = 0; i < this.path.length; i++) {
                        iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                    }
                    iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + 1 + CodedOutputByteBufferNano.computeRawVarint32Size(iComputeInt32SizeNoTag);
                }
                if (this.span != null && this.span.length > 0) {
                    int iComputeInt32SizeNoTag2 = 0;
                    for (int i2 = 0; i2 < this.span.length; i2++) {
                        iComputeInt32SizeNoTag2 += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.span[i2]);
                    }
                    iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag2 + 1 + CodedOutputByteBufferNano.computeRawVarint32Size(iComputeInt32SizeNoTag2);
                }
                if (this.leadingComments != null && !this.leadingComments.equals("")) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(3, this.leadingComments);
                }
                if (this.trailingComments != null && !this.trailingComments.equals("")) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(4, this.trailingComments);
                }
                if (this.leadingDetachedComments == null || this.leadingDetachedComments.length <= 0) {
                    return iComputeSerializedSize;
                }
                int iComputeStringSizeNoTag = 0;
                int i3 = 0;
                for (int i4 = 0; i4 < this.leadingDetachedComments.length; i4++) {
                    String str = this.leadingDetachedComments[i4];
                    if (str != null) {
                        i3++;
                        iComputeStringSizeNoTag += CodedOutputByteBufferNano.computeStringSizeNoTag(str);
                    }
                }
                return iComputeSerializedSize + iComputeStringSizeNoTag + (1 * i3);
            }

            @Override // com.google.protobuf.nano.MessageNano
            public Location mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 8);
                        int length = this.path == null ? 0 : this.path.length;
                        int[] iArr = new int[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.path, 0, iArr, 0, length);
                        }
                        while (length < iArr.length - 1) {
                            iArr[length] = input.readInt32();
                            input.readTag();
                            length++;
                        }
                        iArr[length] = input.readInt32();
                        this.path = iArr;
                    } else if (tag == 10) {
                        int iPushLimit = input.pushLimit(input.readRawVarint32());
                        int position = input.getPosition();
                        int i = 0;
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            i++;
                        }
                        input.rewindToPosition(position);
                        int length2 = this.path == null ? 0 : this.path.length;
                        int[] iArr2 = new int[i + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.path, 0, iArr2, 0, length2);
                        }
                        while (length2 < iArr2.length) {
                            iArr2[length2] = input.readInt32();
                            length2++;
                        }
                        this.path = iArr2;
                        input.popLimit(iPushLimit);
                    } else if (tag == 16) {
                        int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(input, 16);
                        int length3 = this.span == null ? 0 : this.span.length;
                        int[] iArr3 = new int[repeatedFieldArrayLength2 + length3];
                        if (length3 != 0) {
                            System.arraycopy(this.span, 0, iArr3, 0, length3);
                        }
                        while (length3 < iArr3.length - 1) {
                            iArr3[length3] = input.readInt32();
                            input.readTag();
                            length3++;
                        }
                        iArr3[length3] = input.readInt32();
                        this.span = iArr3;
                    } else if (tag == 18) {
                        int iPushLimit2 = input.pushLimit(input.readRawVarint32());
                        int position2 = input.getPosition();
                        int i2 = 0;
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            i2++;
                        }
                        input.rewindToPosition(position2);
                        int length4 = this.span == null ? 0 : this.span.length;
                        int[] iArr4 = new int[i2 + length4];
                        if (length4 != 0) {
                            System.arraycopy(this.span, 0, iArr4, 0, length4);
                        }
                        while (length4 < iArr4.length) {
                            iArr4[length4] = input.readInt32();
                            length4++;
                        }
                        this.span = iArr4;
                        input.popLimit(iPushLimit2);
                    } else if (tag == 26) {
                        this.leadingComments = input.readString();
                    } else if (tag == 34) {
                        this.trailingComments = input.readString();
                    } else if (tag != 50) {
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        int repeatedFieldArrayLength3 = WireFormatNano.getRepeatedFieldArrayLength(input, 50);
                        int length5 = this.leadingDetachedComments == null ? 0 : this.leadingDetachedComments.length;
                        String[] strArr = new String[repeatedFieldArrayLength3 + length5];
                        if (length5 != 0) {
                            System.arraycopy(this.leadingDetachedComments, 0, strArr, 0, length5);
                        }
                        while (length5 < strArr.length - 1) {
                            strArr[length5] = input.readString();
                            input.readTag();
                            length5++;
                        }
                        strArr[length5] = input.readString();
                        this.leadingDetachedComments = strArr;
                    }
                }
            }

            public static Location parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (Location) MessageNano.mergeFrom(new Location(), data);
            }

            public static Location parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new Location().mergeFrom(input);
            }
        }

        public static SourceCodeInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new SourceCodeInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public SourceCodeInfo() {
            clear();
        }

        public SourceCodeInfo clear() {
            this.location = Location.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.location != null && this.location.length > 0) {
                for (int i = 0; i < this.location.length; i++) {
                    Location location = this.location[i];
                    if (location != null) {
                        output.writeMessage(1, location);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.location != null && this.location.length > 0) {
                for (int i = 0; i < this.location.length; i++) {
                    Location location = this.location[i];
                    if (location != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, location);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public SourceCodeInfo mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    int length = this.location == null ? 0 : this.location.length;
                    Location[] locationArr = new Location[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.location, 0, locationArr, 0, length);
                    }
                    while (length < locationArr.length - 1) {
                        locationArr[length] = new Location();
                        input.readMessage(locationArr[length]);
                        input.readTag();
                        length++;
                    }
                    locationArr[length] = new Location();
                    input.readMessage(locationArr[length]);
                    this.location = locationArr;
                }
            }
        }

        public static SourceCodeInfo parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (SourceCodeInfo) MessageNano.mergeFrom(new SourceCodeInfo(), data);
        }

        public static SourceCodeInfo parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new SourceCodeInfo().mergeFrom(input);
        }
    }

    public static final class GeneratedCodeInfo extends ExtendableMessageNano<GeneratedCodeInfo> {
        private static volatile GeneratedCodeInfo[] _emptyArray;
        public Annotation[] annotation;

        public static final class Annotation extends ExtendableMessageNano<Annotation> {
            private static volatile Annotation[] _emptyArray;
            public int begin;
            public int end;
            public int[] path;
            public String sourceFile;

            public static Annotation[] emptyArray() {
                if (_emptyArray == null) {
                    synchronized (InternalNano.LAZY_INIT_LOCK) {
                        if (_emptyArray == null) {
                            _emptyArray = new Annotation[0];
                        }
                    }
                }
                return _emptyArray;
            }

            public Annotation() {
                clear();
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

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            public void writeTo(CodedOutputByteBufferNano output) throws IOException {
                if (this.path != null && this.path.length > 0) {
                    int iComputeInt32SizeNoTag = 0;
                    for (int i = 0; i < this.path.length; i++) {
                        iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                    }
                    output.writeRawVarint32(10);
                    output.writeRawVarint32(iComputeInt32SizeNoTag);
                    for (int i2 = 0; i2 < this.path.length; i2++) {
                        output.writeInt32NoTag(this.path[i2]);
                    }
                }
                if (this.sourceFile != null && !this.sourceFile.equals("")) {
                    output.writeString(2, this.sourceFile);
                }
                if (this.begin != 0) {
                    output.writeInt32(3, this.begin);
                }
                if (this.end != 0) {
                    output.writeInt32(4, this.end);
                }
                super.writeTo(output);
            }

            @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
            protected int computeSerializedSize() {
                int iComputeSerializedSize = super.computeSerializedSize();
                if (this.path != null && this.path.length > 0) {
                    int iComputeInt32SizeNoTag = 0;
                    for (int i = 0; i < this.path.length; i++) {
                        iComputeInt32SizeNoTag += CodedOutputByteBufferNano.computeInt32SizeNoTag(this.path[i]);
                    }
                    iComputeSerializedSize = iComputeSerializedSize + iComputeInt32SizeNoTag + 1 + CodedOutputByteBufferNano.computeRawVarint32Size(iComputeInt32SizeNoTag);
                }
                if (this.sourceFile != null && !this.sourceFile.equals("")) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.sourceFile);
                }
                if (this.begin != 0) {
                    iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, this.begin);
                }
                return this.end != 0 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(4, this.end) : iComputeSerializedSize;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public Annotation mergeFrom(CodedInputByteBufferNano input) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    if (tag == 0) {
                        return this;
                    }
                    if (tag == 8) {
                        int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 8);
                        int length = this.path == null ? 0 : this.path.length;
                        int[] iArr = new int[repeatedFieldArrayLength + length];
                        if (length != 0) {
                            System.arraycopy(this.path, 0, iArr, 0, length);
                        }
                        while (length < iArr.length - 1) {
                            iArr[length] = input.readInt32();
                            input.readTag();
                            length++;
                        }
                        iArr[length] = input.readInt32();
                        this.path = iArr;
                    } else if (tag == 10) {
                        int iPushLimit = input.pushLimit(input.readRawVarint32());
                        int position = input.getPosition();
                        int i = 0;
                        while (input.getBytesUntilLimit() > 0) {
                            input.readInt32();
                            i++;
                        }
                        input.rewindToPosition(position);
                        int length2 = this.path == null ? 0 : this.path.length;
                        int[] iArr2 = new int[i + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.path, 0, iArr2, 0, length2);
                        }
                        while (length2 < iArr2.length) {
                            iArr2[length2] = input.readInt32();
                            length2++;
                        }
                        this.path = iArr2;
                        input.popLimit(iPushLimit);
                    } else if (tag == 18) {
                        this.sourceFile = input.readString();
                    } else if (tag == 24) {
                        this.begin = input.readInt32();
                    } else if (tag != 32) {
                        if (!super.storeUnknownField(input, tag)) {
                            return this;
                        }
                    } else {
                        this.end = input.readInt32();
                    }
                }
            }

            public static Annotation parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
                return (Annotation) MessageNano.mergeFrom(new Annotation(), data);
            }

            public static Annotation parseFrom(CodedInputByteBufferNano input) throws IOException {
                return new Annotation().mergeFrom(input);
            }
        }

        public static GeneratedCodeInfo[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new GeneratedCodeInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        public GeneratedCodeInfo() {
            clear();
        }

        public GeneratedCodeInfo clear() {
            this.annotation = Annotation.emptyArray();
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.annotation != null && this.annotation.length > 0) {
                for (int i = 0; i < this.annotation.length; i++) {
                    Annotation annotation = this.annotation[i];
                    if (annotation != null) {
                        output.writeMessage(1, annotation);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.annotation != null && this.annotation.length > 0) {
                for (int i = 0; i < this.annotation.length; i++) {
                    Annotation annotation = this.annotation[i];
                    if (annotation != null) {
                        iComputeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, annotation);
                    }
                }
            }
            return iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public GeneratedCodeInfo mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag != 10) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 10);
                    int length = this.annotation == null ? 0 : this.annotation.length;
                    Annotation[] annotationArr = new Annotation[repeatedFieldArrayLength + length];
                    if (length != 0) {
                        System.arraycopy(this.annotation, 0, annotationArr, 0, length);
                    }
                    while (length < annotationArr.length - 1) {
                        annotationArr[length] = new Annotation();
                        input.readMessage(annotationArr[length]);
                        input.readTag();
                        length++;
                    }
                    annotationArr[length] = new Annotation();
                    input.readMessage(annotationArr[length]);
                    this.annotation = annotationArr;
                }
            }
        }

        public static GeneratedCodeInfo parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (GeneratedCodeInfo) MessageNano.mergeFrom(new GeneratedCodeInfo(), data);
        }

        public static GeneratedCodeInfo parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new GeneratedCodeInfo().mergeFrom(input);
        }
    }
}
