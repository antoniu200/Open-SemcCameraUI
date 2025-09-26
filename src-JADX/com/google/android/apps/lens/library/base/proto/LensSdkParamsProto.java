package com.google.android.apps.lens.library.base.proto;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.FieldType;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtoField;
import com.google.protobuf.ProtoMessage;
import com.google.protobuf.ProtoPresenceBits;
import com.google.protobuf.ProtoPresenceCheckedField;
import com.google.protobuf.ProtoSyntax;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class LensSdkParamsProto {

    public interface LensSdkParamsOrBuilder extends MessageLiteOrBuilder {
        String getAgsaVersionName();

        ByteString getAgsaVersionNameBytes();

        LensSdkParams.LensAvailabilityStatus getArStickersAvailabilityStatus();

        LensSdkParams.LensAvailabilityStatus getLensAvailabilityStatus();

        String getLensSdkVersion();

        ByteString getLensSdkVersionBytes();

        boolean hasAgsaVersionName();

        boolean hasArStickersAvailabilityStatus();

        boolean hasLensAvailabilityStatus();

        boolean hasLensSdkVersion();
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    private LensSdkParamsProto() {
    }

    @ProtoMessage(checkInitialized = {}, messageSetWireFormat = false, protoSyntax = ProtoSyntax.PROTO2)
    public static final class LensSdkParams extends GeneratedMessageLite<LensSdkParams, Builder> implements LensSdkParamsOrBuilder {
        public static final int AGSA_VERSION_NAME_FIELD_NUMBER = 2;
        public static final int AR_STICKERS_AVAILABILITY_STATUS_FIELD_NUMBER = 4;
        private static final LensSdkParams DEFAULT_INSTANCE = new LensSdkParams();
        public static final int LENS_AVAILABILITY_STATUS_FIELD_NUMBER = 3;
        public static final int LENS_SDK_VERSION_FIELD_NUMBER = 1;
        private static volatile Parser<LensSdkParams> PARSER;

        @ProtoPresenceBits(id = 0)
        private int bitField0_;

        @ProtoField(fieldNumber = 1, isEnforceUtf8 = false, isRequired = false, type = FieldType.STRING)
        @ProtoPresenceCheckedField(mask = 1, presenceBitsId = 0)
        private String lensSdkVersion_ = "";

        @ProtoField(fieldNumber = 2, isEnforceUtf8 = false, isRequired = false, type = FieldType.STRING)
        @ProtoPresenceCheckedField(mask = 2, presenceBitsId = 0)
        private String agsaVersionName_ = "";

        @ProtoField(fieldNumber = 3, isRequired = false, type = FieldType.ENUM)
        @ProtoPresenceCheckedField(mask = 4, presenceBitsId = 0)
        private int lensAvailabilityStatus_ = -1;

        @ProtoField(fieldNumber = 4, isRequired = false, type = FieldType.ENUM)
        @ProtoPresenceCheckedField(mask = 8, presenceBitsId = 0)
        private int arStickersAvailabilityStatus_ = -1;

        private LensSdkParams() {
        }

        public enum LensAvailabilityStatus implements Internal.EnumLite {
            LENS_AVAILABILITY_UNKNOWN(-1),
            LENS_READY(0),
            LENS_UNAVAILABLE(1),
            LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED(2),
            LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE(3),
            LENS_UNAVAILABLE_INVALID_CURSOR(4),
            LENS_UNAVAILABLE_DEVICE_LOCKED(5),
            LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE(6);

            public static final int LENS_AVAILABILITY_UNKNOWN_VALUE = -1;
            public static final int LENS_READY_VALUE = 0;
            public static final int LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE_VALUE = 3;
            public static final int LENS_UNAVAILABLE_DEVICE_LOCKED_VALUE = 5;
            public static final int LENS_UNAVAILABLE_INVALID_CURSOR_VALUE = 4;
            public static final int LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED_VALUE = 2;
            public static final int LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE_VALUE = 6;
            public static final int LENS_UNAVAILABLE_VALUE = 1;
            private static final Internal.EnumLiteMap<LensAvailabilityStatus> internalValueMap = new Internal.EnumLiteMap<LensAvailabilityStatus>() { // from class: com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParams.LensAvailabilityStatus.1
                /* renamed from: findValueByNumber, reason: merged with bridge method [inline-methods] */
                public LensAvailabilityStatus m7findValueByNumber(int number) {
                    return LensAvailabilityStatus.forNumber(number);
                }
            };
            private final int value;

            public final int getNumber() {
                return this.value;
            }

            public static LensAvailabilityStatus forNumber(int value) {
                switch (value) {
                    case -1:
                        return LENS_AVAILABILITY_UNKNOWN;
                    case 0:
                        return LENS_READY;
                    case 1:
                        return LENS_UNAVAILABLE;
                    case 2:
                        return LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED;
                    case 3:
                        return LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE;
                    case 4:
                        return LENS_UNAVAILABLE_INVALID_CURSOR;
                    case 5:
                        return LENS_UNAVAILABLE_DEVICE_LOCKED;
                    case 6:
                        return LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<LensAvailabilityStatus> internalGetValueMap() {
                return internalValueMap;
            }

            LensAvailabilityStatus(int value) {
                this.value = value;
            }
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public boolean hasLensSdkVersion() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public String getLensSdkVersion() {
            return this.lensSdkVersion_;
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public ByteString getLensSdkVersionBytes() {
            return ByteString.copyFromUtf8(this.lensSdkVersion_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLensSdkVersion(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 1;
            this.lensSdkVersion_ = value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearLensSdkVersion() {
            this.bitField0_ &= -2;
            this.lensSdkVersion_ = getDefaultInstance().getLensSdkVersion();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLensSdkVersionBytes(ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 1;
            this.lensSdkVersion_ = value.toStringUtf8();
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public boolean hasAgsaVersionName() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public String getAgsaVersionName() {
            return this.agsaVersionName_;
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public ByteString getAgsaVersionNameBytes() {
            return ByteString.copyFromUtf8(this.agsaVersionName_);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAgsaVersionName(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 2;
            this.agsaVersionName_ = value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearAgsaVersionName() {
            this.bitField0_ &= -3;
            this.agsaVersionName_ = getDefaultInstance().getAgsaVersionName();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAgsaVersionNameBytes(ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 2;
            this.agsaVersionName_ = value.toStringUtf8();
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public boolean hasLensAvailabilityStatus() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public LensAvailabilityStatus getLensAvailabilityStatus() {
            LensAvailabilityStatus lensAvailabilityStatusForNumber = LensAvailabilityStatus.forNumber(this.lensAvailabilityStatus_);
            return lensAvailabilityStatusForNumber == null ? LensAvailabilityStatus.LENS_AVAILABILITY_UNKNOWN : lensAvailabilityStatusForNumber;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLensAvailabilityStatus(LensAvailabilityStatus value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 4;
            this.lensAvailabilityStatus_ = value.getNumber();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearLensAvailabilityStatus() {
            this.bitField0_ &= -5;
            this.lensAvailabilityStatus_ = -1;
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public boolean hasArStickersAvailabilityStatus() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
        public LensAvailabilityStatus getArStickersAvailabilityStatus() {
            LensAvailabilityStatus lensAvailabilityStatusForNumber = LensAvailabilityStatus.forNumber(this.arStickersAvailabilityStatus_);
            return lensAvailabilityStatusForNumber == null ? LensAvailabilityStatus.LENS_AVAILABILITY_UNKNOWN : lensAvailabilityStatusForNumber;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setArStickersAvailabilityStatus(LensAvailabilityStatus value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 8;
            this.arStickersAvailabilityStatus_ = value.getNumber();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearArStickersAvailabilityStatus() {
            this.bitField0_ &= -9;
            this.arStickersAvailabilityStatus_ = -1;
        }

        public static LensSdkParams parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static LensSdkParams parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static LensSdkParams parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static LensSdkParams parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static LensSdkParams parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data);
        }

        public static LensSdkParams parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, data, extensionRegistry);
        }

        public static LensSdkParams parseFrom(InputStream input) throws IOException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static LensSdkParams parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static LensSdkParams parseDelimitedFrom(InputStream input) throws IOException {
            return (LensSdkParams) parseDelimitedFrom(DEFAULT_INSTANCE, input);
        }

        public static LensSdkParams parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (LensSdkParams) parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static LensSdkParams parseFrom(CodedInputStream input) throws IOException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input);
        }

        public static LensSdkParams parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (LensSdkParams) GeneratedMessageLite.parseFrom(DEFAULT_INSTANCE, input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return (Builder) DEFAULT_INSTANCE.createBuilder();
        }

        public static Builder newBuilder(LensSdkParams prototype) {
            return (Builder) DEFAULT_INSTANCE.createBuilder(prototype);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<LensSdkParams, Builder> implements LensSdkParamsOrBuilder {
            /* synthetic */ Builder(AnonymousClass1 anonymousClass1) {
                this();
            }

            private Builder() {
                super(LensSdkParams.DEFAULT_INSTANCE);
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public boolean hasLensSdkVersion() {
                return ((LensSdkParams) this.instance).hasLensSdkVersion();
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public String getLensSdkVersion() {
                return ((LensSdkParams) this.instance).getLensSdkVersion();
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public ByteString getLensSdkVersionBytes() {
                return ((LensSdkParams) this.instance).getLensSdkVersionBytes();
            }

            public Builder setLensSdkVersion(String value) {
                copyOnWrite();
                ((LensSdkParams) this.instance).setLensSdkVersion(value);
                return this;
            }

            public Builder clearLensSdkVersion() {
                copyOnWrite();
                ((LensSdkParams) this.instance).clearLensSdkVersion();
                return this;
            }

            public Builder setLensSdkVersionBytes(ByteString value) {
                copyOnWrite();
                ((LensSdkParams) this.instance).setLensSdkVersionBytes(value);
                return this;
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public boolean hasAgsaVersionName() {
                return ((LensSdkParams) this.instance).hasAgsaVersionName();
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public String getAgsaVersionName() {
                return ((LensSdkParams) this.instance).getAgsaVersionName();
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public ByteString getAgsaVersionNameBytes() {
                return ((LensSdkParams) this.instance).getAgsaVersionNameBytes();
            }

            public Builder setAgsaVersionName(String value) {
                copyOnWrite();
                ((LensSdkParams) this.instance).setAgsaVersionName(value);
                return this;
            }

            public Builder clearAgsaVersionName() {
                copyOnWrite();
                ((LensSdkParams) this.instance).clearAgsaVersionName();
                return this;
            }

            public Builder setAgsaVersionNameBytes(ByteString value) {
                copyOnWrite();
                ((LensSdkParams) this.instance).setAgsaVersionNameBytes(value);
                return this;
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public boolean hasLensAvailabilityStatus() {
                return ((LensSdkParams) this.instance).hasLensAvailabilityStatus();
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public LensAvailabilityStatus getLensAvailabilityStatus() {
                return ((LensSdkParams) this.instance).getLensAvailabilityStatus();
            }

            public Builder setLensAvailabilityStatus(LensAvailabilityStatus value) {
                copyOnWrite();
                ((LensSdkParams) this.instance).setLensAvailabilityStatus(value);
                return this;
            }

            public Builder clearLensAvailabilityStatus() {
                copyOnWrite();
                ((LensSdkParams) this.instance).clearLensAvailabilityStatus();
                return this;
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public boolean hasArStickersAvailabilityStatus() {
                return ((LensSdkParams) this.instance).hasArStickersAvailabilityStatus();
            }

            @Override // com.google.android.apps.lens.library.base.proto.LensSdkParamsProto.LensSdkParamsOrBuilder
            public LensAvailabilityStatus getArStickersAvailabilityStatus() {
                return ((LensSdkParams) this.instance).getArStickersAvailabilityStatus();
            }

            public Builder setArStickersAvailabilityStatus(LensAvailabilityStatus value) {
                copyOnWrite();
                ((LensSdkParams) this.instance).setArStickersAvailabilityStatus(value);
                return this;
            }

            public Builder clearArStickersAvailabilityStatus() {
                copyOnWrite();
                ((LensSdkParams) this.instance).clearArStickersAvailabilityStatus();
                return this;
            }
        }

        protected final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke method, Object arg0, Object arg1) {
            AnonymousClass1 anonymousClass1 = null;
            switch (AnonymousClass1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[method.ordinal()]) {
                case 1:
                    return new LensSdkParams();
                case 2:
                    return new Builder(anonymousClass1);
                case 3:
                    return newMessageInfo(DEFAULT_INSTANCE, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0005\u0000\u0000\u0000\u0001\b\u0000\u0002\b\u0001\u0003\f\u0002\u0004\f\u0003", new Object[]{"bitField0_", "lensSdkVersion_", "agsaVersionName_", "lensAvailabilityStatus_", LensAvailabilityStatus.internalGetValueMap(), "arStickersAvailabilityStatus_", LensAvailabilityStatus.internalGetValueMap()});
                case 4:
                    return DEFAULT_INSTANCE;
                case 5:
                    GeneratedMessageLite.DefaultInstanceBasedParser defaultInstanceBasedParser = PARSER;
                    if (defaultInstanceBasedParser == null) {
                        synchronized (LensSdkParams.class) {
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
            GeneratedMessageLite.registerDefaultInstance(LensSdkParams.class, DEFAULT_INSTANCE);
        }

        public static LensSdkParams getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<LensSdkParams> parser() {
            return DEFAULT_INSTANCE.getParserForType();
        }
    }

    /* renamed from: com.google.android.apps.lens.library.base.proto.LensSdkParamsProto$1, reason: invalid class name */
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
}
