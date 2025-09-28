package com.google.protobuf.nano;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.nano.ExtendableMessageNano;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Extension<M extends ExtendableMessageNano<M>, T> {
    public static final int TYPE_BOOL = 8;
    public static final int TYPE_BYTES = 12;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_ENUM = 14;
    public static final int TYPE_FIXED32 = 7;
    public static final int TYPE_FIXED64 = 6;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_GROUP = 10;
    public static final int TYPE_INT32 = 5;
    public static final int TYPE_INT64 = 3;
    public static final int TYPE_MESSAGE = 11;
    public static final int TYPE_SFIXED32 = 15;
    public static final int TYPE_SFIXED64 = 16;
    public static final int TYPE_SINT32 = 17;
    public static final int TYPE_SINT64 = 18;
    public static final int TYPE_STRING = 9;
    public static final int TYPE_UINT32 = 13;
    public static final int TYPE_UINT64 = 4;
    protected final Class<T> clazz;
    protected final GeneratedMessageLite<?, ?> defaultInstance;
    protected final boolean repeated;
    public final int tag;
    protected final int type;

    @Deprecated
    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int type, Class<T> clazz, int tag) {
        return new Extension<>(type, clazz, tag, false);
    }

    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int type, Class<T> clazz, long tag) {
        return new Extension<>(type, clazz, (int) tag, false);
    }

    /* JADX WARN: Incorrect types in method signature: <M:Lcom/google/protobuf/nano/ExtendableMessageNano<TM;>;T:Lcom/google/protobuf/GeneratedMessageLite<**>;>(ILjava/lang/Class<TT;>;TT;J)Lcom/google/protobuf/nano/Extension<TM;TT;>; */
    public static Extension createMessageLiteTyped(int type, Class clazz, GeneratedMessageLite defaultInstance, long tag) {
        return new Extension(type, clazz, (GeneratedMessageLite<?, ?>) defaultInstance, (int) tag, false);
    }

    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T[]> createRepeatedMessageTyped(int type, Class<T[]> clazz, long tag) {
        return new Extension<>(type, clazz, (int) tag, true);
    }

    /* JADX WARN: Incorrect types in method signature: <M:Lcom/google/protobuf/nano/ExtendableMessageNano<TM;>;T:Lcom/google/protobuf/GeneratedMessageLite<**>;>(ILjava/lang/Class<[TT;>;TT;J)Lcom/google/protobuf/nano/Extension<TM;[TT;>; */
    public static Extension createRepeatedMessageLiteTyped(int type, Class clazz, GeneratedMessageLite defaultInstance, long tag) {
        return new Extension(type, clazz, (GeneratedMessageLite<?, ?>) defaultInstance, (int) tag, true);
    }

    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createPrimitiveTyped(int type, Class<T> clazz, long tag) {
        return new PrimitiveExtension(type, clazz, (int) tag, false, 0, 0);
    }

    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createRepeatedPrimitiveTyped(int type, Class<T> clazz, long tag, long nonPackedTag, long packedTag) {
        return new PrimitiveExtension(type, clazz, (int) tag, true, (int) nonPackedTag, (int) packedTag);
    }

    private Extension(int type, Class<T> clazz, int tag, boolean repeated) {
        this(type, clazz, (GeneratedMessageLite<?, ?>) null, tag, repeated);
    }

    private Extension(int type, Class<T> clazz, GeneratedMessageLite<?, ?> defaultInstance, int tag, boolean repeated) {
        this.type = type;
        this.clazz = clazz;
        this.tag = tag;
        this.repeated = repeated;
        this.defaultInstance = defaultInstance;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Extension)) {
            return false;
        }
        Extension extension = (Extension) other;
        return this.type == extension.type && this.clazz == extension.clazz && this.tag == extension.tag && this.repeated == extension.repeated;
    }

    public int hashCode() {
        return (31 * (((((1147 + this.type) * 31) + this.clazz.hashCode()) * 31) + this.tag)) + (this.repeated ? 1 : 0);
    }

    final T getValueFrom(List<UnknownFieldData> unknownFields) {
        if (unknownFields == null) {
            return null;
        }
        return this.repeated ? getRepeatedValueFrom(unknownFields) : getSingularValueFrom(unknownFields);
    }

    private T getRepeatedValueFrom(List<UnknownFieldData> unknownFields) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < unknownFields.size(); i++) {
            UnknownFieldData unknownFieldData = unknownFields.get(i);
            if (unknownFieldData.bytes.length != 0) {
                readDataInto(unknownFieldData, arrayList);
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            return null;
        }
        T tCast = this.clazz.cast(Array.newInstance(this.clazz.getComponentType(), size));
        for (int i2 = 0; i2 < size; i2++) {
            Array.set(tCast, i2, arrayList.get(i2));
        }
        return tCast;
    }

    private T getSingularValueFrom(List<UnknownFieldData> unknownFields) {
        if (unknownFields.isEmpty()) {
            return null;
        }
        return this.clazz.cast(readData(CodedInputByteBufferNano.newInstance(unknownFields.get(unknownFields.size() - 1).bytes)));
    }

    protected Object readData(CodedInputByteBufferNano input) {
        Class componentType = this.repeated ? this.clazz.getComponentType() : this.clazz;
        try {
            switch (this.type) {
                case 10:
                    MessageNano messageNano = (MessageNano) componentType.newInstance();
                    input.readGroup(messageNano, WireFormatNano.getTagFieldNumber(this.tag));
                    return messageNano;
                case 11:
                    if (this.defaultInstance != null) {
                        return input.readMessageLite(this.defaultInstance.getParserForType());
                    }
                    MessageNano messageNano2 = (MessageNano) componentType.newInstance();
                    input.readMessage(messageNano2);
                    return messageNano2;
                default:
                    int i = this.type;
                    StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading extension field", e);
        } catch (IllegalAccessException e2) {
            String strValueOf = String.valueOf(componentType);
            StringBuilder sb2 = new StringBuilder(33 + String.valueOf(strValueOf).length());
            sb2.append("Error creating instance of class ");
            sb2.append(strValueOf);
            throw new IllegalArgumentException(sb2.toString(), e2);
        } catch (InstantiationException e3) {
            String strValueOf2 = String.valueOf(componentType);
            StringBuilder sb3 = new StringBuilder(33 + String.valueOf(strValueOf2).length());
            sb3.append("Error creating instance of class ");
            sb3.append(strValueOf2);
            throw new IllegalArgumentException(sb3.toString(), e3);
        }
    }

    protected void readDataInto(UnknownFieldData data, List<Object> resultList) {
        resultList.add(readData(CodedInputByteBufferNano.newInstance(data.bytes)));
    }

    void writeTo(Object value, CodedOutputByteBufferNano output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.repeated) {
            writeRepeatedData(value, output);
        } else {
            writeSingularData(value, output);
        }
    }

    void writeAsMessageSetTo(Object value, CodedOutputByteBufferNano output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.repeated) {
            writeRepeatedDataAsMessageSet(value, output);
        } else {
            writeSingularDataAsMessageSet(value, output);
        }
    }

    protected void writeSingularData(Object value, CodedOutputByteBufferNano out) {
        try {
            out.writeRawVarint32(this.tag);
            switch (this.type) {
                case 10:
                    int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
                    if (this.defaultInstance == null) {
                        out.writeGroupNoTag((MessageNano) value);
                    } else {
                        out.writeGroupNoTag((MessageLite) value);
                    }
                    out.writeTag(tagFieldNumber, 4);
                    return;
                case 11:
                    if (this.defaultInstance == null) {
                        out.writeMessageNoTag((MessageNano) value);
                        return;
                    } else {
                        out.writeMessageNoTag((MessageLite) value);
                        return;
                    }
                default:
                    int i = this.type;
                    StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void writeSingularDataAsMessageSet(Object value, CodedOutputByteBufferNano out) throws IOException {
        out.writeMessageSetExtension(WireFormatNano.getTagFieldNumber(this.tag), (MessageNano) value);
    }

    protected void writeRepeatedData(Object array, CodedOutputByteBufferNano output) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object obj = Array.get(array, i);
            if (obj != null) {
                writeSingularData(obj, output);
            }
        }
    }

    protected void writeRepeatedDataAsMessageSet(Object array, CodedOutputByteBufferNano output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object obj = Array.get(array, i);
            if (obj != null) {
                writeSingularDataAsMessageSet(obj, output);
            }
        }
    }

    int computeSerializedSize(Object value) {
        if (this.repeated) {
            return computeRepeatedSerializedSize(value);
        }
        return computeSingularSerializedSize(value);
    }

    int computeSerializedSizeAsMessageSet(Object value) {
        if (this.repeated) {
            return computeRepeatedSerializedSizeAsMessageSet(value);
        }
        return computeSingularSerializedSizeAsMessageSet(value);
    }

    protected int computeRepeatedSerializedSize(Object array) {
        int length = Array.getLength(array);
        int iComputeSingularSerializedSize = 0;
        for (int i = 0; i < length; i++) {
            if (Array.get(array, i) != null) {
                iComputeSingularSerializedSize += computeSingularSerializedSize(Array.get(array, i));
            }
        }
        return iComputeSingularSerializedSize;
    }

    protected int computeSingularSerializedSize(Object value) {
        int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
        switch (this.type) {
            case 10:
                if (this.defaultInstance == null) {
                    return CodedOutputByteBufferNano.computeGroupSize(tagFieldNumber, (MessageNano) value);
                }
                return CodedOutputStream.computeGroupSize(tagFieldNumber, (MessageLite) value);
            case 11:
                if (this.defaultInstance == null) {
                    return CodedOutputByteBufferNano.computeMessageSize(tagFieldNumber, (MessageNano) value);
                }
                return CodedOutputStream.computeMessageSize(tagFieldNumber, (MessageLite) value);
            default:
                int i = this.type;
                StringBuilder sb = new StringBuilder(24);
                sb.append("Unknown type ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
        }
    }

    protected int computeRepeatedSerializedSizeAsMessageSet(Object array) {
        int length = Array.getLength(array);
        int iComputeSingularSerializedSizeAsMessageSet = 0;
        for (int i = 0; i < length; i++) {
            if (Array.get(array, i) != null) {
                iComputeSingularSerializedSizeAsMessageSet += computeSingularSerializedSizeAsMessageSet(Array.get(array, i));
            }
        }
        return iComputeSingularSerializedSizeAsMessageSet;
    }

    protected int computeSingularSerializedSizeAsMessageSet(Object value) {
        return CodedOutputByteBufferNano.computeMessageSetExtensionSize(WireFormatNano.getTagFieldNumber(this.tag), (MessageNano) value);
    }

    private static class PrimitiveExtension<M extends ExtendableMessageNano<M>, T> extends Extension<M, T> {
        private final int nonPackedTag;
        private final int packedTag;

        public PrimitiveExtension(int type, Class<T> clazz, int tag, boolean repeated, int nonPackedTag, int packedTag) {
            super(type, clazz, tag, repeated);
            this.nonPackedTag = nonPackedTag;
            this.packedTag = packedTag;
        }

        @Override // com.google.protobuf.nano.Extension
        protected Object readData(CodedInputByteBufferNano input) {
            try {
                switch (this.type) {
                    case 1:
                        return Double.valueOf(input.readDouble());
                    case 2:
                        return Float.valueOf(input.readFloat());
                    case 3:
                        return Long.valueOf(input.readInt64());
                    case 4:
                        return Long.valueOf(input.readUInt64());
                    case 5:
                        return Integer.valueOf(input.readInt32());
                    case 6:
                        return Long.valueOf(input.readFixed64());
                    case 7:
                        return Integer.valueOf(input.readFixed32());
                    case 8:
                        return Boolean.valueOf(input.readBool());
                    case 9:
                        return input.readString();
                    case 10:
                    case 11:
                    default:
                        int i = this.type;
                        StringBuilder sb = new StringBuilder(24);
                        sb.append("Unknown type ");
                        sb.append(i);
                        throw new IllegalArgumentException(sb.toString());
                    case 12:
                        return input.readBytes();
                    case 13:
                        return Integer.valueOf(input.readUInt32());
                    case 14:
                        return Integer.valueOf(input.readEnum());
                    case 15:
                        return Integer.valueOf(input.readSFixed32());
                    case 16:
                        return Long.valueOf(input.readSFixed64());
                    case 17:
                        return Integer.valueOf(input.readSInt32());
                    case 18:
                        return Long.valueOf(input.readSInt64());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading extension field", e);
            }
        }

        @Override // com.google.protobuf.nano.Extension
        protected void readDataInto(UnknownFieldData data, List<Object> resultList) {
            if (data.tag == this.nonPackedTag) {
                resultList.add(readData(CodedInputByteBufferNano.newInstance(data.bytes)));
                return;
            }
            CodedInputByteBufferNano codedInputByteBufferNanoNewInstance = CodedInputByteBufferNano.newInstance(data.bytes);
            try {
                codedInputByteBufferNanoNewInstance.pushLimit(codedInputByteBufferNanoNewInstance.readRawVarint32());
                while (!codedInputByteBufferNanoNewInstance.isAtEnd()) {
                    resultList.add(readData(codedInputByteBufferNanoNewInstance));
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading extension field", e);
            }
        }

        @Override // com.google.protobuf.nano.Extension
        protected final void writeSingularData(Object value, CodedOutputByteBufferNano output) {
            try {
                output.writeRawVarint32(this.tag);
                switch (this.type) {
                    case 1:
                        output.writeDoubleNoTag(((Double) value).doubleValue());
                        return;
                    case 2:
                        output.writeFloatNoTag(((Float) value).floatValue());
                        return;
                    case 3:
                        output.writeInt64NoTag(((Long) value).longValue());
                        return;
                    case 4:
                        output.writeUInt64NoTag(((Long) value).longValue());
                        return;
                    case 5:
                        output.writeInt32NoTag(((Integer) value).intValue());
                        return;
                    case 6:
                        output.writeFixed64NoTag(((Long) value).longValue());
                        return;
                    case 7:
                        output.writeFixed32NoTag(((Integer) value).intValue());
                        return;
                    case 8:
                        output.writeBoolNoTag(((Boolean) value).booleanValue());
                        return;
                    case 9:
                        output.writeStringNoTag((String) value);
                        return;
                    case 10:
                    case 11:
                    default:
                        int i = this.type;
                        StringBuilder sb = new StringBuilder(24);
                        sb.append("Unknown type ");
                        sb.append(i);
                        throw new IllegalArgumentException(sb.toString());
                    case 12:
                        output.writeBytesNoTag((byte[]) value);
                        return;
                    case 13:
                        output.writeUInt32NoTag(((Integer) value).intValue());
                        return;
                    case 14:
                        output.writeEnumNoTag(((Integer) value).intValue());
                        return;
                    case 15:
                        output.writeSFixed32NoTag(((Integer) value).intValue());
                        return;
                    case 16:
                        output.writeSFixed64NoTag(((Long) value).longValue());
                        return;
                    case 17:
                        output.writeSInt32NoTag(((Integer) value).intValue());
                        return;
                    case 18:
                        output.writeSInt64NoTag(((Long) value).longValue());
                        return;
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.google.protobuf.nano.Extension
        protected void writeRepeatedData(Object array, CodedOutputByteBufferNano output) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (this.tag == this.nonPackedTag) {
                super.writeRepeatedData(array, output);
                return;
            }
            if (this.tag == this.packedTag) {
                int length = Array.getLength(array);
                int iComputePackedDataSize = computePackedDataSize(array);
                try {
                    output.writeRawVarint32(this.tag);
                    output.writeRawVarint32(iComputePackedDataSize);
                    int i = this.type;
                    int i2 = 0;
                    switch (i) {
                        case 1:
                            while (i2 < length) {
                                output.writeDoubleNoTag(Array.getDouble(array, i2));
                                i2++;
                            }
                            return;
                        case 2:
                            while (i2 < length) {
                                output.writeFloatNoTag(Array.getFloat(array, i2));
                                i2++;
                            }
                            return;
                        case 3:
                            while (i2 < length) {
                                output.writeInt64NoTag(Array.getLong(array, i2));
                                i2++;
                            }
                            return;
                        case 4:
                            while (i2 < length) {
                                output.writeUInt64NoTag(Array.getLong(array, i2));
                                i2++;
                            }
                            return;
                        case 5:
                            while (i2 < length) {
                                output.writeInt32NoTag(Array.getInt(array, i2));
                                i2++;
                            }
                            return;
                        case 6:
                            while (i2 < length) {
                                output.writeFixed64NoTag(Array.getLong(array, i2));
                                i2++;
                            }
                            return;
                        case 7:
                            while (i2 < length) {
                                output.writeFixed32NoTag(Array.getInt(array, i2));
                                i2++;
                            }
                            return;
                        case 8:
                            while (i2 < length) {
                                output.writeBoolNoTag(Array.getBoolean(array, i2));
                                i2++;
                            }
                            return;
                        default:
                            switch (i) {
                                case 13:
                                    while (i2 < length) {
                                        output.writeUInt32NoTag(Array.getInt(array, i2));
                                        i2++;
                                    }
                                    return;
                                case 14:
                                    while (i2 < length) {
                                        output.writeEnumNoTag(Array.getInt(array, i2));
                                        i2++;
                                    }
                                    return;
                                case 15:
                                    while (i2 < length) {
                                        output.writeSFixed32NoTag(Array.getInt(array, i2));
                                        i2++;
                                    }
                                    return;
                                case 16:
                                    while (i2 < length) {
                                        output.writeSFixed64NoTag(Array.getLong(array, i2));
                                        i2++;
                                    }
                                    return;
                                case 17:
                                    while (i2 < length) {
                                        output.writeSInt32NoTag(Array.getInt(array, i2));
                                        i2++;
                                    }
                                    return;
                                case 18:
                                    while (i2 < length) {
                                        output.writeSInt64NoTag(Array.getLong(array, i2));
                                        i2++;
                                    }
                                    return;
                                default:
                                    int i3 = this.type;
                                    StringBuilder sb = new StringBuilder(27);
                                    sb.append("Unpackable type ");
                                    sb.append(i3);
                                    throw new IllegalArgumentException(sb.toString());
                            }
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            int i4 = this.tag;
            int i5 = this.nonPackedTag;
            int i6 = this.packedTag;
            StringBuilder sb2 = new StringBuilder(124);
            sb2.append("Unexpected repeated extension tag ");
            sb2.append(i4);
            sb2.append(", unequal to both non-packed variant ");
            sb2.append(i5);
            sb2.append(" and packed variant ");
            sb2.append(i6);
            throw new IllegalArgumentException(sb2.toString());
        }

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.processFallThroughCases(SwitchRegionMaker.java:105)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:64)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
            */
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        private int computePackedDataSize(java.lang.Object r6) {
            /*
                r5 = this;
                int r0 = java.lang.reflect.Array.getLength(r6)
                int r1 = r5.type
                r2 = 0
                switch(r1) {
                    case 1: goto L94;
                    case 2: goto L91;
                    case 3: goto L82;
                    case 4: goto L73;
                    case 5: goto L64;
                    case 6: goto L94;
                    case 7: goto L91;
                    case 8: goto L96;
                    default: goto La;
                }
            La:
                switch(r1) {
                    case 13: goto L55;
                    case 14: goto L46;
                    case 15: goto L91;
                    case 16: goto L94;
                    case 17: goto L37;
                    case 18: goto L28;
                    default: goto Ld;
                }
            Ld:
                java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
                int r5 = r5.type
                r0 = 40
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>(r0)
                java.lang.String r0 = "Unexpected non-packable type "
                r1.append(r0)
                r1.append(r5)
                java.lang.String r5 = r1.toString()
                r6.<init>(r5)
                throw r6
            L28:
                r5 = r2
            L29:
                if (r2 >= r0) goto L97
                long r3 = java.lang.reflect.Array.getLong(r6, r2)
                int r1 = com.google.protobuf.nano.CodedOutputByteBufferNano.computeSInt64SizeNoTag(r3)
                int r5 = r5 + r1
                int r2 = r2 + 1
                goto L29
            L37:
                r5 = r2
            L38:
                if (r2 >= r0) goto L97
                int r1 = java.lang.reflect.Array.getInt(r6, r2)
                int r1 = com.google.protobuf.nano.CodedOutputByteBufferNano.computeSInt32SizeNoTag(r1)
                int r5 = r5 + r1
                int r2 = r2 + 1
                goto L38
            L46:
                r5 = r2
            L47:
                if (r2 >= r0) goto L97
                int r1 = java.lang.reflect.Array.getInt(r6, r2)
                int r1 = com.google.protobuf.nano.CodedOutputByteBufferNano.computeEnumSizeNoTag(r1)
                int r5 = r5 + r1
                int r2 = r2 + 1
                goto L47
            L55:
                r5 = r2
            L56:
                if (r2 >= r0) goto L97
                int r1 = java.lang.reflect.Array.getInt(r6, r2)
                int r1 = com.google.protobuf.nano.CodedOutputByteBufferNano.computeUInt32SizeNoTag(r1)
                int r5 = r5 + r1
                int r2 = r2 + 1
                goto L56
            L64:
                r5 = r2
            L65:
                if (r2 >= r0) goto L97
                int r1 = java.lang.reflect.Array.getInt(r6, r2)
                int r1 = com.google.protobuf.nano.CodedOutputByteBufferNano.computeInt32SizeNoTag(r1)
                int r5 = r5 + r1
                int r2 = r2 + 1
                goto L65
            L73:
                r5 = r2
            L74:
                if (r2 >= r0) goto L97
                long r3 = java.lang.reflect.Array.getLong(r6, r2)
                int r1 = com.google.protobuf.nano.CodedOutputByteBufferNano.computeUInt64SizeNoTag(r3)
                int r5 = r5 + r1
                int r2 = r2 + 1
                goto L74
            L82:
                r5 = r2
            L83:
                if (r2 >= r0) goto L97
                long r3 = java.lang.reflect.Array.getLong(r6, r2)
                int r1 = com.google.protobuf.nano.CodedOutputByteBufferNano.computeInt64SizeNoTag(r3)
                int r5 = r5 + r1
                int r2 = r2 + 1
                goto L83
            L91:
                int r0 = r0 * 4
                goto L96
            L94:
                int r0 = r0 * 8
            L96:
                r5 = r0
            L97:
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.nano.Extension.PrimitiveExtension.computePackedDataSize(java.lang.Object):int");
        }

        @Override // com.google.protobuf.nano.Extension
        protected int computeRepeatedSerializedSize(Object array) {
            if (this.tag == this.nonPackedTag) {
                return super.computeRepeatedSerializedSize(array);
            }
            if (this.tag == this.packedTag) {
                int iComputePackedDataSize = computePackedDataSize(array);
                return iComputePackedDataSize + CodedOutputByteBufferNano.computeRawVarint32Size(iComputePackedDataSize) + CodedOutputByteBufferNano.computeRawVarint32Size(this.tag);
            }
            int i = this.tag;
            int i2 = this.nonPackedTag;
            int i3 = this.packedTag;
            StringBuilder sb = new StringBuilder(124);
            sb.append("Unexpected repeated extension tag ");
            sb.append(i);
            sb.append(", unequal to both non-packed variant ");
            sb.append(i2);
            sb.append(" and packed variant ");
            sb.append(i3);
            throw new IllegalArgumentException(sb.toString());
        }

        @Override // com.google.protobuf.nano.Extension
        protected final int computeSingularSerializedSize(Object value) {
            int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
            switch (this.type) {
                case 1:
                    return CodedOutputByteBufferNano.computeDoubleSize(tagFieldNumber, ((Double) value).doubleValue());
                case 2:
                    return CodedOutputByteBufferNano.computeFloatSize(tagFieldNumber, ((Float) value).floatValue());
                case 3:
                    return CodedOutputByteBufferNano.computeInt64Size(tagFieldNumber, ((Long) value).longValue());
                case 4:
                    return CodedOutputByteBufferNano.computeUInt64Size(tagFieldNumber, ((Long) value).longValue());
                case 5:
                    return CodedOutputByteBufferNano.computeInt32Size(tagFieldNumber, ((Integer) value).intValue());
                case 6:
                    return CodedOutputByteBufferNano.computeFixed64Size(tagFieldNumber, ((Long) value).longValue());
                case 7:
                    return CodedOutputByteBufferNano.computeFixed32Size(tagFieldNumber, ((Integer) value).intValue());
                case 8:
                    return CodedOutputByteBufferNano.computeBoolSize(tagFieldNumber, ((Boolean) value).booleanValue());
                case 9:
                    return CodedOutputByteBufferNano.computeStringSize(tagFieldNumber, (String) value);
                case 10:
                case 11:
                default:
                    int i = this.type;
                    StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
                case 12:
                    return CodedOutputByteBufferNano.computeBytesSize(tagFieldNumber, (byte[]) value);
                case 13:
                    return CodedOutputByteBufferNano.computeUInt32Size(tagFieldNumber, ((Integer) value).intValue());
                case 14:
                    return CodedOutputByteBufferNano.computeEnumSize(tagFieldNumber, ((Integer) value).intValue());
                case 15:
                    return CodedOutputByteBufferNano.computeSFixed32Size(tagFieldNumber, ((Integer) value).intValue());
                case 16:
                    return CodedOutputByteBufferNano.computeSFixed64Size(tagFieldNumber, ((Long) value).longValue());
                case 17:
                    return CodedOutputByteBufferNano.computeSInt32Size(tagFieldNumber, ((Integer) value).intValue());
                case 18:
                    return CodedOutputByteBufferNano.computeSInt64Size(tagFieldNumber, ((Long) value).longValue());
            }
        }
    }
}
