// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.io.IOException;
import com.google.protobuf.Parser;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.MessageLite;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.google.protobuf.GeneratedMessageLite;

public class Extension<M extends ExtendableMessageNano<M>, T>
{
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
    
    private Extension(final int n, final Class<T> clazz, final int n2, final boolean b) {
        this(n, clazz, null, n2, b);
    }
    
    private Extension(final int type, final Class<T> clazz, final GeneratedMessageLite<?, ?> defaultInstance, final int tag, final boolean repeated) {
        this.type = type;
        this.clazz = clazz;
        this.tag = tag;
        this.repeated = repeated;
        this.defaultInstance = defaultInstance;
    }
    
    public static <M extends ExtendableMessageNano<M>, T extends GeneratedMessageLite<?, ?>> Extension<M, T> createMessageLiteTyped(final int n, final Class<T> clazz, final T t, final long n2) {
        return new Extension<M, T>(n, clazz, t, (int)n2, false);
    }
    
    @Deprecated
    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(final int n, final Class<T> clazz, final int n2) {
        return new Extension<M, T>(n, clazz, n2, false);
    }
    
    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(final int n, final Class<T> clazz, final long n2) {
        return new Extension<M, T>(n, clazz, (int)n2, false);
    }
    
    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createPrimitiveTyped(final int n, final Class<T> clazz, final long n2) {
        return new PrimitiveExtension<M, T>(n, clazz, (int)n2, false, 0, 0);
    }
    
    public static <M extends ExtendableMessageNano<M>, T extends GeneratedMessageLite<?, ?>> Extension<M, T[]> createRepeatedMessageLiteTyped(final int n, final Class<T[]> clazz, final T t, final long n2) {
        return new Extension<M, T[]>(n, clazz, t, (int)n2, true);
    }
    
    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T[]> createRepeatedMessageTyped(final int n, final Class<T[]> clazz, final long n2) {
        return new Extension<M, T[]>(n, clazz, (int)n2, true);
    }
    
    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createRepeatedPrimitiveTyped(final int n, final Class<T> clazz, final long n2, final long n3, final long n4) {
        return new PrimitiveExtension<M, T>(n, clazz, (int)n2, true, (int)n3, (int)n4);
    }
    
    private T getRepeatedValueFrom(final List<UnknownFieldData> list) {
        final ArrayList list2 = new ArrayList();
        final int n = 0;
        for (int i = 0; i < list.size(); ++i) {
            final UnknownFieldData unknownFieldData = list.get(i);
            if (unknownFieldData.bytes.length != 0) {
                this.readDataInto(unknownFieldData, list2);
            }
        }
        final int size = list2.size();
        if (size == 0) {
            return null;
        }
        final T cast = this.clazz.cast(Array.newInstance(this.clazz.getComponentType(), size));
        for (int j = n; j < size; ++j) {
            Array.set(cast, j, list2.get(j));
        }
        return cast;
    }
    
    private T getSingularValueFrom(final List<UnknownFieldData> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.clazz.cast(this.readData(CodedInputByteBufferNano.newInstance(list.get(list.size() - 1).bytes)));
    }
    
    protected int computeRepeatedSerializedSize(final Object o) {
        final int length = Array.getLength(o);
        int i = 0;
        int n = 0;
        while (i < length) {
            int n2 = n;
            if (Array.get(o, i) != null) {
                n2 = n + this.computeSingularSerializedSize(Array.get(o, i));
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    protected int computeRepeatedSerializedSizeAsMessageSet(final Object o) {
        final int length = Array.getLength(o);
        int i = 0;
        int n = 0;
        while (i < length) {
            int n2 = n;
            if (Array.get(o, i) != null) {
                n2 = n + this.computeSingularSerializedSizeAsMessageSet(Array.get(o, i));
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    int computeSerializedSize(final Object o) {
        if (this.repeated) {
            return this.computeRepeatedSerializedSize(o);
        }
        return this.computeSingularSerializedSize(o);
    }
    
    int computeSerializedSizeAsMessageSet(final Object o) {
        if (this.repeated) {
            return this.computeRepeatedSerializedSizeAsMessageSet(o);
        }
        return this.computeSingularSerializedSizeAsMessageSet(o);
    }
    
    protected int computeSingularSerializedSize(final Object o) {
        final int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
        switch (this.type) {
            default: {
                final int type = this.type;
                final StringBuilder sb = new StringBuilder(24);
                sb.append("Unknown type ");
                sb.append(type);
                throw new IllegalArgumentException(sb.toString());
            }
            case 11: {
                if (this.defaultInstance == null) {
                    return CodedOutputByteBufferNano.computeMessageSize(tagFieldNumber, (MessageNano)o);
                }
                return CodedOutputStream.computeMessageSize(tagFieldNumber, (MessageLite)o);
            }
            case 10: {
                if (this.defaultInstance == null) {
                    return CodedOutputByteBufferNano.computeGroupSize(tagFieldNumber, (MessageNano)o);
                }
                return CodedOutputStream.computeGroupSize(tagFieldNumber, (MessageLite)o);
            }
        }
    }
    
    protected int computeSingularSerializedSizeAsMessageSet(final Object o) {
        return CodedOutputByteBufferNano.computeMessageSetExtensionSize(WireFormatNano.getTagFieldNumber(this.tag), (MessageNano)o);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extension)) {
            return false;
        }
        final Extension extension = (Extension)o;
        if (this.type != extension.type || this.clazz != extension.clazz || this.tag != extension.tag || this.repeated != extension.repeated) {
            b = false;
        }
        return b;
    }
    
    final T getValueFrom(final List<UnknownFieldData> list) {
        if (list == null) {
            return null;
        }
        T t;
        if (this.repeated) {
            t = this.getRepeatedValueFrom(list);
        }
        else {
            t = this.getSingularValueFrom(list);
        }
        return t;
    }
    
    @Override
    public int hashCode() {
        return 31 * (((1147 + this.type) * 31 + this.clazz.hashCode()) * 31 + this.tag) + (this.repeated ? 1 : 0);
    }
    
    protected Object readData(final CodedInputByteBufferNano codedInputByteBufferNano) {
        Class<?> clazz;
        if (this.repeated) {
            clazz = this.clazz.getComponentType();
        }
        else {
            clazz = this.clazz;
        }
        try {
            switch (this.type) {
                default: {
                    final int type = this.type;
                    final StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(type);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 11: {
                    if (this.defaultInstance != null) {
                        return codedInputByteBufferNano.readMessageLite((com.google.protobuf.Parser<Object>)this.defaultInstance.getParserForType());
                    }
                    final MessageNano messageNano = (MessageNano)clazz.newInstance();
                    codedInputByteBufferNano.readMessage(messageNano);
                    return messageNano;
                }
                case 10: {
                    final MessageNano messageNano2 = (MessageNano)clazz.newInstance();
                    codedInputByteBufferNano.readGroup(messageNano2, WireFormatNano.getTagFieldNumber(this.tag));
                    return messageNano2;
                }
            }
        }
        catch (final IOException cause) {
            throw new IllegalArgumentException("Error reading extension field", cause);
        }
        catch (final IllegalAccessException cause2) {
            final String value = String.valueOf(clazz);
            final StringBuilder sb2 = new StringBuilder(33 + String.valueOf(value).length());
            sb2.append("Error creating instance of class ");
            sb2.append(value);
            throw new IllegalArgumentException(sb2.toString(), cause2);
        }
        catch (final InstantiationException cause3) {
            final String value2 = String.valueOf(clazz);
            final StringBuilder sb3 = new StringBuilder(33 + String.valueOf(value2).length());
            sb3.append("Error creating instance of class ");
            sb3.append(value2);
            throw new IllegalArgumentException(sb3.toString(), cause3);
        }
    }
    
    protected void readDataInto(final UnknownFieldData unknownFieldData, final List<Object> list) {
        list.add(this.readData(CodedInputByteBufferNano.newInstance(unknownFieldData.bytes)));
    }
    
    void writeAsMessageSetTo(final Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.repeated) {
            this.writeRepeatedDataAsMessageSet(o, codedOutputByteBufferNano);
        }
        else {
            this.writeSingularDataAsMessageSet(o, codedOutputByteBufferNano);
        }
    }
    
    protected void writeRepeatedData(final Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) {
        for (int length = Array.getLength(o), i = 0; i < length; ++i) {
            final Object value = Array.get(o, i);
            if (value != null) {
                this.writeSingularData(value, codedOutputByteBufferNano);
            }
        }
    }
    
    protected void writeRepeatedDataAsMessageSet(final Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        for (int length = Array.getLength(o), i = 0; i < length; ++i) {
            final Object value = Array.get(o, i);
            if (value != null) {
                this.writeSingularDataAsMessageSet(value, codedOutputByteBufferNano);
            }
        }
    }
    
    protected void writeSingularData(Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) {
        try {
            codedOutputByteBufferNano.writeRawVarint32(this.tag);
            switch (this.type) {
                default: {
                    final int type = this.type;
                    final StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(type);
                    o = new IllegalArgumentException(sb.toString());
                    throw o;
                }
                case 11: {
                    if (this.defaultInstance == null) {
                        codedOutputByteBufferNano.writeMessageNoTag((MessageNano)o);
                        break;
                    }
                    codedOutputByteBufferNano.writeMessageNoTag((MessageLite)o);
                    break;
                }
                case 10: {
                    final int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
                    if (this.defaultInstance == null) {
                        codedOutputByteBufferNano.writeGroupNoTag((MessageNano)o);
                    }
                    else {
                        codedOutputByteBufferNano.writeGroupNoTag((MessageLite)o);
                    }
                    codedOutputByteBufferNano.writeTag(tagFieldNumber, 4);
                    break;
                }
            }
        }
        catch (final IOException cause) {
            throw new IllegalStateException(cause);
        }
    }
    
    protected void writeSingularDataAsMessageSet(final Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        codedOutputByteBufferNano.writeMessageSetExtension(WireFormatNano.getTagFieldNumber(this.tag), (MessageNano)o);
    }
    
    void writeTo(final Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.repeated) {
            this.writeRepeatedData(o, codedOutputByteBufferNano);
        }
        else {
            this.writeSingularData(o, codedOutputByteBufferNano);
        }
    }
    
    private static class PrimitiveExtension<M extends ExtendableMessageNano<M>, T> extends Extension<M, T>
    {
        private final int nonPackedTag;
        private final int packedTag;
        
        public PrimitiveExtension(final int n, final Class<T> clazz, final int n2, final boolean b, final int nonPackedTag, final int packedTag) {
            super(n, clazz, n2, b, null);
            this.nonPackedTag = nonPackedTag;
            this.packedTag = packedTag;
        }
        
        private int computePackedDataSize(final Object o) {
            final int length = Array.getLength(o);
            final int type = this.type;
            final int n = 0;
            final int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            final int n5 = 0;
            int n6 = 0;
            final int n7 = 0;
            int n8 = length;
            switch (type) {
                default: {
                    switch (type) {
                        default: {
                            final int type2 = this.type;
                            final StringBuilder sb = new StringBuilder(40);
                            sb.append("Unexpected non-packable type ");
                            sb.append(type2);
                            throw new IllegalArgumentException(sb.toString());
                        }
                        case 18: {
                            int n9 = 0;
                            int n10 = n7;
                            while (true) {
                                n8 = n9;
                                if (n10 >= length) {
                                    return n8;
                                }
                                n9 += CodedOutputByteBufferNano.computeSInt64SizeNoTag(Array.getLong(o, n10));
                                ++n10;
                            }
                            break;
                        }
                        case 17: {
                            int n11 = 0;
                            int n12 = n;
                            while (true) {
                                n8 = n11;
                                if (n12 >= length) {
                                    return n8;
                                }
                                n11 += CodedOutputByteBufferNano.computeSInt32SizeNoTag(Array.getInt(o, n12));
                                ++n12;
                            }
                            break;
                        }
                        case 14: {
                            int n13 = 0;
                            int n14 = n2;
                            while (true) {
                                n8 = n13;
                                if (n14 >= length) {
                                    return n8;
                                }
                                n13 += CodedOutputByteBufferNano.computeEnumSizeNoTag(Array.getInt(o, n14));
                                ++n14;
                            }
                            break;
                        }
                        case 13: {
                            int n15 = 0;
                            int n16 = n3;
                            while (true) {
                                n8 = n15;
                                if (n16 >= length) {
                                    return n8;
                                }
                                n15 += CodedOutputByteBufferNano.computeUInt32SizeNoTag(Array.getInt(o, n16));
                                ++n16;
                            }
                            break;
                        }
                        case 15: {
                            return length * 4;
                        }
                        case 16: {
                            return length * 8;
                        }
                    }
                    break;
                }
                case 5: {
                    int n17 = 0;
                    int n18 = n4;
                    while (true) {
                        n8 = n17;
                        if (n18 >= length) {
                            return n8;
                        }
                        n17 += CodedOutputByteBufferNano.computeInt32SizeNoTag(Array.getInt(o, n18));
                        ++n18;
                    }
                    break;
                }
                case 4: {
                    int n19 = 0;
                    int n20 = n5;
                    while (true) {
                        n8 = n19;
                        if (n20 >= length) {
                            return n8;
                        }
                        n19 += CodedOutputByteBufferNano.computeUInt64SizeNoTag(Array.getLong(o, n20));
                        ++n20;
                    }
                    break;
                }
                case 3: {
                    int n21 = 0;
                    while (true) {
                        n8 = n21;
                        if (n6 >= length) {
                            return n8;
                        }
                        n21 += CodedOutputByteBufferNano.computeInt64SizeNoTag(Array.getLong(o, n6));
                        ++n6;
                    }
                    break;
                }
                case 8: {
                    return n8;
                }
                case 2:
                case 7: {
                    n8 = length * 4;
                    return n8;
                }
                case 1:
                case 6: {
                    n8 = length * 8;
                    return n8;
                }
            }
        }
        
        @Override
        protected int computeRepeatedSerializedSize(final Object o) {
            if (this.tag == this.nonPackedTag) {
                return super.computeRepeatedSerializedSize(o);
            }
            if (this.tag == this.packedTag) {
                final int computePackedDataSize = this.computePackedDataSize(o);
                return computePackedDataSize + CodedOutputByteBufferNano.computeRawVarint32Size(computePackedDataSize) + CodedOutputByteBufferNano.computeRawVarint32Size(this.tag);
            }
            final int tag = this.tag;
            final int nonPackedTag = this.nonPackedTag;
            final int packedTag = this.packedTag;
            final StringBuilder sb = new StringBuilder(124);
            sb.append("Unexpected repeated extension tag ");
            sb.append(tag);
            sb.append(", unequal to both non-packed variant ");
            sb.append(nonPackedTag);
            sb.append(" and packed variant ");
            sb.append(packedTag);
            throw new IllegalArgumentException(sb.toString());
        }
        
        @Override
        protected final int computeSingularSerializedSize(final Object o) {
            final int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
            switch (this.type) {
                default: {
                    final int type = this.type;
                    final StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(type);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 18: {
                    return CodedOutputByteBufferNano.computeSInt64Size(tagFieldNumber, (long)o);
                }
                case 17: {
                    return CodedOutputByteBufferNano.computeSInt32Size(tagFieldNumber, (int)o);
                }
                case 16: {
                    return CodedOutputByteBufferNano.computeSFixed64Size(tagFieldNumber, (long)o);
                }
                case 15: {
                    return CodedOutputByteBufferNano.computeSFixed32Size(tagFieldNumber, (int)o);
                }
                case 14: {
                    return CodedOutputByteBufferNano.computeEnumSize(tagFieldNumber, (int)o);
                }
                case 13: {
                    return CodedOutputByteBufferNano.computeUInt32Size(tagFieldNumber, (int)o);
                }
                case 12: {
                    return CodedOutputByteBufferNano.computeBytesSize(tagFieldNumber, (byte[])o);
                }
                case 9: {
                    return CodedOutputByteBufferNano.computeStringSize(tagFieldNumber, (String)o);
                }
                case 8: {
                    return CodedOutputByteBufferNano.computeBoolSize(tagFieldNumber, (boolean)o);
                }
                case 7: {
                    return CodedOutputByteBufferNano.computeFixed32Size(tagFieldNumber, (int)o);
                }
                case 6: {
                    return CodedOutputByteBufferNano.computeFixed64Size(tagFieldNumber, (long)o);
                }
                case 5: {
                    return CodedOutputByteBufferNano.computeInt32Size(tagFieldNumber, (int)o);
                }
                case 4: {
                    return CodedOutputByteBufferNano.computeUInt64Size(tagFieldNumber, (long)o);
                }
                case 3: {
                    return CodedOutputByteBufferNano.computeInt64Size(tagFieldNumber, (long)o);
                }
                case 2: {
                    return CodedOutputByteBufferNano.computeFloatSize(tagFieldNumber, (float)o);
                }
                case 1: {
                    return CodedOutputByteBufferNano.computeDoubleSize(tagFieldNumber, (double)o);
                }
            }
        }
        
        @Override
        protected Object readData(final CodedInputByteBufferNano codedInputByteBufferNano) {
            try {
                switch (this.type) {
                    default: {
                        final int type = this.type;
                        final StringBuilder sb = new StringBuilder(24);
                        sb.append("Unknown type ");
                        sb.append(type);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    case 18: {
                        return codedInputByteBufferNano.readSInt64();
                    }
                    case 17: {
                        return codedInputByteBufferNano.readSInt32();
                    }
                    case 16: {
                        return codedInputByteBufferNano.readSFixed64();
                    }
                    case 15: {
                        return codedInputByteBufferNano.readSFixed32();
                    }
                    case 14: {
                        return codedInputByteBufferNano.readEnum();
                    }
                    case 13: {
                        return codedInputByteBufferNano.readUInt32();
                    }
                    case 12: {
                        return codedInputByteBufferNano.readBytes();
                    }
                    case 9: {
                        return codedInputByteBufferNano.readString();
                    }
                    case 8: {
                        return codedInputByteBufferNano.readBool();
                    }
                    case 7: {
                        return codedInputByteBufferNano.readFixed32();
                    }
                    case 6: {
                        return codedInputByteBufferNano.readFixed64();
                    }
                    case 5: {
                        return codedInputByteBufferNano.readInt32();
                    }
                    case 4: {
                        return codedInputByteBufferNano.readUInt64();
                    }
                    case 3: {
                        return codedInputByteBufferNano.readInt64();
                    }
                    case 2: {
                        return codedInputByteBufferNano.readFloat();
                    }
                    case 1: {
                        return codedInputByteBufferNano.readDouble();
                    }
                }
            }
            catch (final IOException cause) {
                throw new IllegalArgumentException("Error reading extension field", cause);
            }
        }
        
        @Override
        protected void readDataInto(final UnknownFieldData unknownFieldData, final List<Object> list) {
            if (unknownFieldData.tag == this.nonPackedTag) {
                list.add(this.readData(CodedInputByteBufferNano.newInstance(unknownFieldData.bytes)));
                return;
            }
            final CodedInputByteBufferNano instance = CodedInputByteBufferNano.newInstance(unknownFieldData.bytes);
            try {
                instance.pushLimit(instance.readRawVarint32());
                while (!instance.isAtEnd()) {
                    list.add(this.readData(instance));
                }
            }
            catch (final IOException cause) {
                throw new IllegalArgumentException("Error reading extension field", cause);
            }
        }
        
        @Override
        protected void writeRepeatedData(Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) {
            if (this.tag == this.nonPackedTag) {
                super.writeRepeatedData(o, codedOutputByteBufferNano);
                return;
            }
            Label_0588: {
                if (this.tag != this.packedTag) {
                    break Label_0588;
                }
                final int length = Array.getLength(o);
                final int computePackedDataSize = this.computePackedDataSize(o);
                try {
                    codedOutputByteBufferNano.writeRawVarint32(this.tag);
                    codedOutputByteBufferNano.writeRawVarint32(computePackedDataSize);
                    final int type = this.type;
                    final int n = 0;
                    final int n2 = 0;
                    final int n3 = 0;
                    final int n4 = 0;
                    final int n5 = 0;
                    int i = 0;
                    int j = 0;
                    int k = 0;
                    int l = 0;
                    int n6 = 0;
                    int n7 = 0;
                    int n8 = 0;
                    int n9 = 0;
                    final int n10 = 0;
                    Label_0577: {
                        switch (type) {
                            default: {
                                int n11 = n10;
                                int n12 = n;
                                int n13 = n2;
                                int n14 = n3;
                                int n15 = n4;
                                int n16 = n5;
                                switch (type) {
                                    default: {
                                        final int type2 = this.type;
                                        o = new StringBuilder(27);
                                        ((StringBuilder)o).append("Unpackable type ");
                                        ((StringBuilder)o).append(type2);
                                        throw new IllegalArgumentException(((StringBuilder)o).toString());
                                    }
                                    case 18: {
                                        while (n11 < length) {
                                            codedOutputByteBufferNano.writeSInt64NoTag(Array.getLong(o, n11));
                                            ++n11;
                                        }
                                        break Label_0577;
                                    }
                                    case 17: {
                                        while (n12 < length) {
                                            codedOutputByteBufferNano.writeSInt32NoTag(Array.getInt(o, n12));
                                            ++n12;
                                        }
                                        break Label_0577;
                                    }
                                    case 16: {
                                        while (n13 < length) {
                                            codedOutputByteBufferNano.writeSFixed64NoTag(Array.getLong(o, n13));
                                            ++n13;
                                        }
                                        break Label_0577;
                                    }
                                    case 15: {
                                        while (n14 < length) {
                                            codedOutputByteBufferNano.writeSFixed32NoTag(Array.getInt(o, n14));
                                            ++n14;
                                        }
                                        break Label_0577;
                                    }
                                    case 14: {
                                        while (n15 < length) {
                                            codedOutputByteBufferNano.writeEnumNoTag(Array.getInt(o, n15));
                                            ++n15;
                                        }
                                        break Label_0577;
                                    }
                                    case 13: {
                                        while (n16 < length) {
                                            codedOutputByteBufferNano.writeUInt32NoTag(Array.getInt(o, n16));
                                            ++n16;
                                        }
                                        break Label_0577;
                                    }
                                }
                                break;
                            }
                            case 8: {
                                while (i < length) {
                                    codedOutputByteBufferNano.writeBoolNoTag(Array.getBoolean(o, i));
                                    ++i;
                                }
                                break;
                            }
                            case 7: {
                                while (j < length) {
                                    codedOutputByteBufferNano.writeFixed32NoTag(Array.getInt(o, j));
                                    ++j;
                                }
                                break;
                            }
                            case 6: {
                                while (k < length) {
                                    codedOutputByteBufferNano.writeFixed64NoTag(Array.getLong(o, k));
                                    ++k;
                                }
                                break;
                            }
                            case 5: {
                                while (l < length) {
                                    codedOutputByteBufferNano.writeInt32NoTag(Array.getInt(o, l));
                                    ++l;
                                }
                                break;
                            }
                            case 4: {
                                while (n6 < length) {
                                    codedOutputByteBufferNano.writeUInt64NoTag(Array.getLong(o, n6));
                                    ++n6;
                                }
                                break;
                            }
                            case 3: {
                                while (n7 < length) {
                                    codedOutputByteBufferNano.writeInt64NoTag(Array.getLong(o, n7));
                                    ++n7;
                                }
                                break;
                            }
                            case 2: {
                                while (n8 < length) {
                                    codedOutputByteBufferNano.writeFloatNoTag(Array.getFloat(o, n8));
                                    ++n8;
                                }
                                break;
                            }
                            case 1: {
                                while (n9 < length) {
                                    codedOutputByteBufferNano.writeDoubleNoTag(Array.getDouble(o, n9));
                                    ++n9;
                                }
                                break;
                            }
                        }
                    }
                    return;
                }
                catch (final IOException cause) {
                    throw new IllegalStateException(cause);
                }
            }
            final int tag = this.tag;
            final int nonPackedTag = this.nonPackedTag;
            final int packedTag = this.packedTag;
            final StringBuilder sb = new StringBuilder(124);
            sb.append("Unexpected repeated extension tag ");
            sb.append(tag);
            sb.append(", unequal to both non-packed variant ");
            sb.append(nonPackedTag);
            sb.append(" and packed variant ");
            sb.append(packedTag);
            throw new IllegalArgumentException(sb.toString());
        }
        
        @Override
        protected final void writeSingularData(Object o, final CodedOutputByteBufferNano codedOutputByteBufferNano) {
            try {
                codedOutputByteBufferNano.writeRawVarint32(this.tag);
                switch (this.type) {
                    default: {
                        final int type = this.type;
                        o = new StringBuilder(24);
                        ((StringBuilder)o).append("Unknown type ");
                        ((StringBuilder)o).append(type);
                        throw new IllegalArgumentException(((StringBuilder)o).toString());
                    }
                    case 18: {
                        codedOutputByteBufferNano.writeSInt64NoTag((long)o);
                        break;
                    }
                    case 17: {
                        codedOutputByteBufferNano.writeSInt32NoTag((int)o);
                        break;
                    }
                    case 16: {
                        codedOutputByteBufferNano.writeSFixed64NoTag((long)o);
                        break;
                    }
                    case 15: {
                        codedOutputByteBufferNano.writeSFixed32NoTag((int)o);
                        break;
                    }
                    case 14: {
                        codedOutputByteBufferNano.writeEnumNoTag((int)o);
                        break;
                    }
                    case 13: {
                        codedOutputByteBufferNano.writeUInt32NoTag((int)o);
                        break;
                    }
                    case 12: {
                        codedOutputByteBufferNano.writeBytesNoTag((byte[])o);
                        break;
                    }
                    case 9: {
                        codedOutputByteBufferNano.writeStringNoTag((String)o);
                        break;
                    }
                    case 8: {
                        codedOutputByteBufferNano.writeBoolNoTag((boolean)o);
                        break;
                    }
                    case 7: {
                        codedOutputByteBufferNano.writeFixed32NoTag((int)o);
                        break;
                    }
                    case 6: {
                        codedOutputByteBufferNano.writeFixed64NoTag((long)o);
                        break;
                    }
                    case 5: {
                        codedOutputByteBufferNano.writeInt32NoTag((int)o);
                        break;
                    }
                    case 4: {
                        codedOutputByteBufferNano.writeUInt64NoTag((long)o);
                        break;
                    }
                    case 3: {
                        codedOutputByteBufferNano.writeInt64NoTag((long)o);
                        break;
                    }
                    case 2: {
                        codedOutputByteBufferNano.writeFloatNoTag((float)o);
                        break;
                    }
                    case 1: {
                        codedOutputByteBufferNano.writeDoubleNoTag((double)o);
                        break;
                    }
                }
            }
            catch (final IOException cause) {
                throw new IllegalStateException(cause);
            }
        }
    }
}
