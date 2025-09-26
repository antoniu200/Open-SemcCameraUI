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
public final class LensServiceClientEventData extends GeneratedMessageLite<LensServiceClientEventData, Builder> implements LensServiceClientEventDataOrBuilder
{
    private static final LensServiceClientEventData DEFAULT_INSTANCE;
    private static volatile Parser<LensServiceClientEventData> PARSER;
    public static final int TARGET_SERVICE_API_VERSION_FIELD_NUMBER = 1;
    @ProtoPresenceBits(id = 0)
    private int bitField0_;
    @ProtoField(fieldNumber = 1, isRequired = false, type = FieldType.INT32)
    @ProtoPresenceCheckedField(mask = 1, presenceBitsId = 0)
    private int targetServiceApiVersion_;
    
    static {
        GeneratedMessageLite.registerDefaultInstance((Class)LensServiceClientEventData.class, (GeneratedMessageLite)(DEFAULT_INSTANCE = new LensServiceClientEventData()));
    }
    
    private LensServiceClientEventData() {
    }
    
    private void clearTargetServiceApiVersion() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.targetServiceApiVersion_ = 0;
    }
    
    public static LensServiceClientEventData getDefaultInstance() {
        return LensServiceClientEventData.DEFAULT_INSTANCE;
    }
    
    public static Builder newBuilder() {
        return (Builder)LensServiceClientEventData.DEFAULT_INSTANCE.createBuilder();
    }
    
    public static Builder newBuilder(final LensServiceClientEventData lensServiceClientEventData) {
        return (Builder)LensServiceClientEventData.DEFAULT_INSTANCE.createBuilder((GeneratedMessageLite)lensServiceClientEventData);
    }
    
    public static LensServiceClientEventData parseDelimitedFrom(final InputStream inputStream) throws IOException {
        return (LensServiceClientEventData)parseDelimitedFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, inputStream);
    }
    
    public static LensServiceClientEventData parseDelimitedFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (LensServiceClientEventData)parseDelimitedFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
    }
    
    public static LensServiceClientEventData parseFrom(final ByteString byteString) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, byteString);
    }
    
    public static LensServiceClientEventData parseFrom(final ByteString byteString, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, byteString, extensionRegistryLite);
    }
    
    public static LensServiceClientEventData parseFrom(final CodedInputStream codedInputStream) throws IOException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, codedInputStream);
    }
    
    public static LensServiceClientEventData parseFrom(final CodedInputStream codedInputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
    }
    
    public static LensServiceClientEventData parseFrom(final InputStream inputStream) throws IOException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, inputStream);
    }
    
    public static LensServiceClientEventData parseFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
    }
    
    public static LensServiceClientEventData parseFrom(final ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, byteBuffer);
    }
    
    public static LensServiceClientEventData parseFrom(final ByteBuffer byteBuffer, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
    }
    
    public static LensServiceClientEventData parseFrom(final byte[] array) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, array);
    }
    
    public static LensServiceClientEventData parseFrom(final byte[] array, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, array, extensionRegistryLite);
    }
    
    public static Parser<LensServiceClientEventData> parser() {
        return (Parser<LensServiceClientEventData>)LensServiceClientEventData.DEFAULT_INSTANCE.getParserForType();
    }
    
    private void setTargetServiceApiVersion(final int targetServiceApiVersion_) {
        this.bitField0_ |= 0x1;
        this.targetServiceApiVersion_ = targetServiceApiVersion_;
    }
    
    protected final Object dynamicMethod(final GeneratedMessageLite$MethodToInvoke generatedMessageLite$MethodToInvoke, final Object o, final Object o2) {
        switch (LensServiceClientEventData$1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[generatedMessageLite$MethodToInvoke.ordinal()]) {
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
                final Parser<LensServiceClientEventData> parser;
                if ((parser = LensServiceClientEventData.PARSER) == null) {
                    synchronized (LensServiceClientEventData.class) {
                        if (LensServiceClientEventData.PARSER == null) {
                            LensServiceClientEventData.PARSER = (Parser<LensServiceClientEventData>)new GeneratedMessageLite$DefaultInstanceBasedParser((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE);
                        }
                    }
                }
                return parser;
            }
            case 4: {
                return LensServiceClientEventData.DEFAULT_INSTANCE;
            }
            case 3: {
                return newMessageInfo((MessageLite)LensServiceClientEventData.DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0002\u0000\u0000\u0000\u0001\u0004\u0000", new Object[] { "bitField0_", "targetServiceApiVersion_" });
            }
            case 2: {
                return new Builder();
            }
            case 1: {
                return new LensServiceClientEventData();
            }
        }
    }
    
    public int getTargetServiceApiVersion() {
        return this.targetServiceApiVersion_;
    }
    
    public boolean hasTargetServiceApiVersion() {
        final int bitField0_ = this.bitField0_;
        boolean b = true;
        if ((bitField0_ & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public static final class Builder extends GeneratedMessageLite$Builder<LensServiceClientEventData, Builder> implements LensServiceClientEventDataOrBuilder
    {
        private Builder() {
            super((GeneratedMessageLite)LensServiceClientEventData.DEFAULT_INSTANCE);
        }
        
        public Builder clearTargetServiceApiVersion() {
            this.copyOnWrite();
            ((LensServiceClientEventData)this.instance).clearTargetServiceApiVersion();
            return this;
        }
        
        public int getTargetServiceApiVersion() {
            return ((LensServiceClientEventData)this.instance).getTargetServiceApiVersion();
        }
        
        public boolean hasTargetServiceApiVersion() {
            return ((LensServiceClientEventData)this.instance).hasTargetServiceApiVersion();
        }
        
        public Builder setTargetServiceApiVersion(final int n) {
            this.copyOnWrite();
            ((LensServiceClientEventData)this.instance).setTargetServiceApiVersion(n);
            return this;
        }
    }
}
