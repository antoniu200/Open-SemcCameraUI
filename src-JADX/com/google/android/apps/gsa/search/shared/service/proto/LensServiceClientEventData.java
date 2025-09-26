package com.google.android.apps.gsa.search.shared.service.proto;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.FieldType;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtoField;
import com.google.protobuf.ProtoMessage;
import com.google.protobuf.ProtoPresenceBits;
import com.google.protobuf.ProtoPresenceCheckedField;
import com.google.protobuf.ProtoSyntax;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@ProtoMessage(checkInitialized = {}, messageSetWireFormat = false, protoSyntax = ProtoSyntax.PROTO2)
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class LensServiceClientEventData extends GeneratedMessageLite<LensServiceClientEventData, Builder> implements LensServiceClientEventDataOrBuilder {
    private static final LensServiceClientEventData DEFAULT_INSTANCE = new LensServiceClientEventData();
    private static volatile Parser<LensServiceClientEventData> PARSER = null;
    public static final int TARGET_SERVICE_API_VERSION_FIELD_NUMBER = 1;

    @ProtoPresenceBits(id = 0)
    private int bitField0_;

    @ProtoField(fieldNumber = 1, isRequired = false, type = FieldType.INT32)
    @ProtoPresenceCheckedField(mask = 1, presenceBitsId = 0)
    private int targetServiceApiVersion_;

    private LensServiceClientEventData() {
    }

    @Override // com.google.android.apps.gsa.search.shared.service.proto.LensServiceClientEventDataOrBuilder
    public boolean hasTargetServiceApiVersion() {
        return (this.bitField0_ & 1) == 1;
    }

    @Override // com.google.android.apps.gsa.search.shared.service.proto.LensServiceClientEventDataOrBuilder
    public int getTargetServiceApiVersion() {
        return this.targetServiceApiVersion_;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTargetServiceApiVersion(int value) {
        this.bitField0_ |= 1;
        this.targetServiceApiVersion_ = value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearTargetServiceApiVersion() {
        this.bitField0_ &= -2;
        this.targetServiceApiVersion_ = 0;
    }

    public static LensServiceClientEventData parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static LensServiceClientEventData parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static LensServiceClientEventData parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static LensServiceClientEventData parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static LensServiceClientEventData parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
    }

    public static LensServiceClientEventData parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
    }

    public static LensServiceClientEventData parseFrom(InputStream input) throws IOException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static LensServiceClientEventData parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static LensServiceClientEventData parseDelimitedFrom(InputStream input) throws IOException {
        return (LensServiceClientEventData) parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }

    public static LensServiceClientEventData parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (LensServiceClientEventData) parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static LensServiceClientEventData parseFrom(CodedInputStream input) throws IOException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
    }

    public static LensServiceClientEventData parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (LensServiceClientEventData) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return (Builder) DEFAULT_INSTANCE.createBuilder();
    }

    public static Builder newBuilder(LensServiceClientEventData prototype) {
        return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
    }

    public static final class Builder extends GeneratedMessageLite.Builder<LensServiceClientEventData, Builder> implements LensServiceClientEventDataOrBuilder {
        /* synthetic */ Builder(AnonymousClass1 anonymousClass1) {
            this();
        }

        private Builder() {
            super(LensServiceClientEventData.DEFAULT_INSTANCE);
        }

        @Override // com.google.android.apps.gsa.search.shared.service.proto.LensServiceClientEventDataOrBuilder
        public boolean hasTargetServiceApiVersion() {
            return ((LensServiceClientEventData) this.instance).hasTargetServiceApiVersion();
        }

        @Override // com.google.android.apps.gsa.search.shared.service.proto.LensServiceClientEventDataOrBuilder
        public int getTargetServiceApiVersion() {
            return ((LensServiceClientEventData) this.instance).getTargetServiceApiVersion();
        }

        public Builder setTargetServiceApiVersion(int value) {
            copyOnWrite();
            ((LensServiceClientEventData) this.instance).setTargetServiceApiVersion(value);
            return this;
        }

        public Builder clearTargetServiceApiVersion() {
            copyOnWrite();
            ((LensServiceClientEventData) this.instance).clearTargetServiceApiVersion();
            return this;
        }
    }

    /* renamed from: com.google.android.apps.gsa.search.shared.service.proto.LensServiceClientEventData$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke = new int[GeneratedMessageLite.MethodToInvoke.values().length];

        static {
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.NEW_MUTABLE_INSTANCE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.NEW_BUILDER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.BUILD_MESSAGE_INFO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_DEFAULT_INSTANCE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_PARSER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.GET_MEMOIZED_IS_INITIALIZED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[GeneratedMessageLite.MethodToInvoke.SET_MEMOIZED_IS_INITIALIZED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
        AnonymousClass1 anonymousClass1 = null;
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[method.ordinal()]) {
            case 1:
                return new LensServiceClientEventData();
            case 2:
                return new Builder(anonymousClass1);
            case 3:
                return newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0002\u0000\u0000\u0000\u0001\u0004\u0000", new Object[]{"bitField0_", "targetServiceApiVersion_"});
            case 4:
                return DEFAULT_INSTANCE;
            case 5:
                GeneratedMessageLite.DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                if (defaultInstanceBasedParser == null) {
                    synchronized (LensServiceClientEventData.class) {
                        defaultInstanceBasedParser = PARSER;
                        if (defaultInstanceBasedParser == null) {
                            defaultInstanceBasedParser = new GeneratedMessageLite.DefaultInstanceBasedParser(DEFAULT_INSTANCE);
                            PARSER = defaultInstanceBasedParser;
                        }
                    }
                }
                return defaultInstanceBasedParser;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }

    static {
        GeneratedMessageLite.registerDefaultInstance(LensServiceClientEventData.class, DEFAULT_INSTANCE);
    }

    public static LensServiceClientEventData getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<LensServiceClientEventData> parser() {
        return DEFAULT_INSTANCE.getParserForType();
    }
}
