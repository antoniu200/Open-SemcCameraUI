// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.search.shared.service.proto;

import com.google.protobuf.GeneratedMessageLite$Builder;
import com.google.protobuf.MessageLite;
import com.google.protobuf.GeneratedMessageLite$DefaultInstanceBasedParser;
import com.google.protobuf.GeneratedMessageLite$MethodToInvoke;
import java.nio.ByteBuffer;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ByteString;
import com.google.protobuf.ExtensionRegistryLite;
import java.io.IOException;
import java.io.InputStream;
import com.google.protobuf.ProtoPresenceCheckedField;
import com.google.protobuf.FieldType;
import com.google.protobuf.ProtoField;
import com.google.protobuf.ProtoPresenceBits;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtoSyntax;
import com.google.protobuf.ProtoMessage;
import com.google.protobuf.GeneratedMessageLite;

@ProtoMessage(checkInitialized = {}, messageSetWireFormat = false, protoSyntax = ProtoSyntax.PROTO2)
public final class LensServiceEventData extends GeneratedMessageLite<LensServiceEventData, Builder> implements LensServiceEventDataOrBuilder
{
    private static final LensServiceEventData DEFAULT_INSTANCE;
    private static volatile Parser<LensServiceEventData> PARSER;
    public static final int SERVICE_API_VERSION_FIELD_NUMBER = 1;
    @ProtoPresenceBits(id = 0)
    private int bitField0_;
    @ProtoField(fieldNumber = 1, isRequired = false, type = FieldType.INT32)
    @ProtoPresenceCheckedField(mask = 1, presenceBitsId = 0)
    private int serviceApiVersion_;
    
    static {
        GeneratedMessageLite.registerDefaultInstance((Class)LensServiceEventData.class, (GeneratedMessageLite)(DEFAULT_INSTANCE = new LensServiceEventData()));
    }
    
    private LensServiceEventData() {
    }
    
    private void clearServiceApiVersion() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.serviceApiVersion_ = 0;
    }
    
    public static LensServiceEventData getDefaultInstance() {
        return LensServiceEventData.DEFAULT_INSTANCE;
    }
    
    public static Builder newBuilder() {
        return (Builder)LensServiceEventData.DEFAULT_INSTANCE.createBuilder();
    }
    
    public static Builder newBuilder(final LensServiceEventData lensServiceEventData) {
        return (Builder)LensServiceEventData.DEFAULT_INSTANCE.createBuilder((GeneratedMessageLite)lensServiceEventData);
    }
    
    public static LensServiceEventData parseDelimitedFrom(final InputStream inputStream) throws IOException {
        return (LensServiceEventData)parseDelimitedFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, inputStream);
    }
    
    public static LensServiceEventData parseDelimitedFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (LensServiceEventData)parseDelimitedFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
    }
    
    public static LensServiceEventData parseFrom(final ByteString byteString) throws InvalidProtocolBufferException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, byteString);
    }
    
    public static LensServiceEventData parseFrom(final ByteString byteString, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, byteString, extensionRegistryLite);
    }
    
    public static LensServiceEventData parseFrom(final CodedInputStream codedInputStream) throws IOException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, codedInputStream);
    }
    
    public static LensServiceEventData parseFrom(final CodedInputStream codedInputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
    }
    
    public static LensServiceEventData parseFrom(final InputStream inputStream) throws IOException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, inputStream);
    }
    
    public static LensServiceEventData parseFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
    }
    
    public static LensServiceEventData parseFrom(final ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, byteBuffer);
    }
    
    public static LensServiceEventData parseFrom(final ByteBuffer byteBuffer, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
    }
    
    public static LensServiceEventData parseFrom(final byte[] array) throws InvalidProtocolBufferException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, array);
    }
    
    public static LensServiceEventData parseFrom(final byte[] array, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (LensServiceEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE, array, extensionRegistryLite);
    }
    
    public static Parser<LensServiceEventData> parser() {
        return (Parser<LensServiceEventData>)LensServiceEventData.DEFAULT_INSTANCE.getParserForType();
    }
    
    private void setServiceApiVersion(final int serviceApiVersion_) {
        this.bitField0_ |= 0x1;
        this.serviceApiVersion_ = serviceApiVersion_;
    }
    
    protected final Object dynamicMethod(final GeneratedMessageLite$MethodToInvoke generatedMessageLite$MethodToInvoke, final Object o, final Object o2) {
        switch (LensServiceEventData$1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[generatedMessageLite$MethodToInvoke.ordinal()]) {
            default: {
                throw new UnsupportedOperationException();
            }
            case 7: {
                return null;
            }
            case 6: {
                return 1;
            }
            case 5: {
                final Parser<LensServiceEventData> parser;
                if ((parser = LensServiceEventData.PARSER) == null) {
                    synchronized (LensServiceEventData.class) {
                        if (LensServiceEventData.PARSER == null) {
                            LensServiceEventData.PARSER = (Parser<LensServiceEventData>)new GeneratedMessageLite$DefaultInstanceBasedParser((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE);
                        }
                    }
                }
                return parser;
            }
            case 4: {
                return LensServiceEventData.DEFAULT_INSTANCE;
            }
            case 3: {
                return newMessageInfo((MessageLite)LensServiceEventData.DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0002\u0000\u0000\u0000\u0001\u0004\u0000", new Object[] { "bitField0_", "serviceApiVersion_" });
            }
            case 2: {
                return new Builder();
            }
            case 1: {
                return new LensServiceEventData();
            }
        }
    }
    
    public int getServiceApiVersion() {
        return this.serviceApiVersion_;
    }
    
    public boolean hasServiceApiVersion() {
        final int bitField0_ = this.bitField0_;
        boolean b = true;
        if ((bitField0_ & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public static final class Builder extends GeneratedMessageLite$Builder<LensServiceEventData, Builder> implements LensServiceEventDataOrBuilder
    {
        private Builder() {
            super((GeneratedMessageLite)LensServiceEventData.DEFAULT_INSTANCE);
        }
        
        public Builder clearServiceApiVersion() {
            this.copyOnWrite();
            ((LensServiceEventData)this.instance).clearServiceApiVersion();
            return this;
        }
        
        public int getServiceApiVersion() {
            return ((LensServiceEventData)this.instance).getServiceApiVersion();
        }
        
        public boolean hasServiceApiVersion() {
            return ((LensServiceEventData)this.instance).hasServiceApiVersion();
        }
        
        public Builder setServiceApiVersion(final int n) {
            this.copyOnWrite();
            ((LensServiceEventData)this.instance).setServiceApiVersion(n);
            return this;
        }
    }
}
