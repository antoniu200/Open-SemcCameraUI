// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.search.shared.service.proto;

import com.google.protobuf.GeneratedMessageLite$ExtendableBuilder;
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
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.ProtoPresenceCheckedField;
import com.google.protobuf.FieldType;
import com.google.protobuf.ProtoField;
import com.google.protobuf.ProtoPresenceBits;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtoSyntax;
import com.google.protobuf.ProtoMessage;
import com.google.protobuf.GeneratedMessageLite$ExtendableMessage;

@ProtoMessage(checkInitialized = {}, messageSetWireFormat = false, protoSyntax = ProtoSyntax.PROTO2)
public final class ServiceEventProto extends GeneratedMessageLite$ExtendableMessage<ServiceEventProto, Builder> implements ServiceEventProtoOrBuilder
{
    private static final ServiceEventProto DEFAULT_INSTANCE;
    public static final int EVENT_ID_FIELD_NUMBER = 1;
    private static volatile Parser<ServiceEventProto> PARSER;
    @ProtoPresenceBits(id = 0)
    private int bitField0_;
    @ProtoField(fieldNumber = 1, isRequired = false, type = FieldType.INT32)
    @ProtoPresenceCheckedField(mask = 1, presenceBitsId = 0)
    private int eventId_;
    private byte memoizedIsInitialized;
    
    static {
        GeneratedMessageLite.registerDefaultInstance((Class)ServiceEventProto.class, (GeneratedMessageLite)(DEFAULT_INSTANCE = new ServiceEventProto()));
    }
    
    private ServiceEventProto() {
        this.memoizedIsInitialized = 2;
    }
    
    private void clearEventId() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.eventId_ = 0;
    }
    
    public static ServiceEventProto getDefaultInstance() {
        return ServiceEventProto.DEFAULT_INSTANCE;
    }
    
    public static Builder newBuilder() {
        return (Builder)ServiceEventProto.DEFAULT_INSTANCE.createBuilder();
    }
    
    public static Builder newBuilder(final ServiceEventProto serviceEventProto) {
        return (Builder)ServiceEventProto.DEFAULT_INSTANCE.createBuilder((GeneratedMessageLite)serviceEventProto);
    }
    
    public static ServiceEventProto parseDelimitedFrom(final InputStream inputStream) throws IOException {
        return (ServiceEventProto)parseDelimitedFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, inputStream);
    }
    
    public static ServiceEventProto parseDelimitedFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (ServiceEventProto)parseDelimitedFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
    }
    
    public static ServiceEventProto parseFrom(final ByteString byteString) throws InvalidProtocolBufferException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, byteString);
    }
    
    public static ServiceEventProto parseFrom(final ByteString byteString, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, byteString, extensionRegistryLite);
    }
    
    public static ServiceEventProto parseFrom(final CodedInputStream codedInputStream) throws IOException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, codedInputStream);
    }
    
    public static ServiceEventProto parseFrom(final CodedInputStream codedInputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
    }
    
    public static ServiceEventProto parseFrom(final InputStream inputStream) throws IOException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, inputStream);
    }
    
    public static ServiceEventProto parseFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
    }
    
    public static ServiceEventProto parseFrom(final ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, byteBuffer);
    }
    
    public static ServiceEventProto parseFrom(final ByteBuffer byteBuffer, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
    }
    
    public static ServiceEventProto parseFrom(final byte[] array) throws InvalidProtocolBufferException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, array);
    }
    
    public static ServiceEventProto parseFrom(final byte[] array, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (ServiceEventProto)GeneratedMessageLite.parseFrom((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE, array, extensionRegistryLite);
    }
    
    public static Parser<ServiceEventProto> parser() {
        return (Parser<ServiceEventProto>)ServiceEventProto.DEFAULT_INSTANCE.getParserForType();
    }
    
    private void setEventId(final int eventId_) {
        this.bitField0_ |= 0x1;
        this.eventId_ = eventId_;
    }
    
    protected final Object dynamicMethod(final GeneratedMessageLite$MethodToInvoke generatedMessageLite$MethodToInvoke, final Object o, final Object o2) {
        final int n = ServiceEventProto$1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[generatedMessageLite$MethodToInvoke.ordinal()];
        boolean b = true;
        switch (n) {
            default: {
                throw new UnsupportedOperationException();
            }
            case 7: {
                if (o == null) {
                    b = false;
                }
                this.memoizedIsInitialized = (byte)(b ? 1 : 0);
                return null;
            }
            case 6: {
                return this.memoizedIsInitialized;
            }
            case 5: {
                final Parser<ServiceEventProto> parser;
                if ((parser = ServiceEventProto.PARSER) == null) {
                    synchronized (ServiceEventProto.class) {
                        if (ServiceEventProto.PARSER == null) {
                            ServiceEventProto.PARSER = (Parser<ServiceEventProto>)new GeneratedMessageLite$DefaultInstanceBasedParser((GeneratedMessageLite)ServiceEventProto.DEFAULT_INSTANCE);
                        }
                    }
                }
                return parser;
            }
            case 4: {
                return ServiceEventProto.DEFAULT_INSTANCE;
            }
            case 3: {
                return newMessageInfo((MessageLite)ServiceEventProto.DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0002\u0000\u0000\u0000\u0001\u0004\u0000", new Object[] { "bitField0_", "eventId_" });
            }
            case 2: {
                return new Builder();
            }
            case 1: {
                return new ServiceEventProto();
            }
        }
    }
    
    public int getEventId() {
        return this.eventId_;
    }
    
    public boolean hasEventId() {
        final int bitField0_ = this.bitField0_;
        boolean b = true;
        if ((bitField0_ & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public static final class Builder extends GeneratedMessageLite$ExtendableBuilder<ServiceEventProto, Builder> implements ServiceEventProtoOrBuilder
    {
        private Builder() {
            super((GeneratedMessageLite$ExtendableMessage)ServiceEventProto.DEFAULT_INSTANCE);
        }
        
        public Builder clearEventId() {
            this.copyOnWrite();
            ((ServiceEventProto)this.instance).clearEventId();
            return this;
        }
        
        public int getEventId() {
            return ((ServiceEventProto)this.instance).getEventId();
        }
        
        public boolean hasEventId() {
            return ((ServiceEventProto)this.instance).hasEventId();
        }
        
        public Builder setEventId(final int n) {
            this.copyOnWrite();
            ((ServiceEventProto)this.instance).setEventId(n);
            return this;
        }
    }
}
