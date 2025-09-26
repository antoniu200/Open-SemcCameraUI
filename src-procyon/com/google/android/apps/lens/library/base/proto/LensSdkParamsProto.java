// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.lens.library.base.proto;

import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Internal$EnumLiteMap;
import com.google.protobuf.Internal$EnumLite;
import com.google.protobuf.GeneratedMessageLite$Builder;
import com.google.protobuf.MessageLite;
import com.google.protobuf.GeneratedMessageLite$DefaultInstanceBasedParser;
import com.google.protobuf.GeneratedMessageLite$MethodToInvoke;
import java.nio.ByteBuffer;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.io.InputStream;
import com.google.protobuf.ByteString;
import com.google.protobuf.ProtoPresenceBits;
import com.google.protobuf.ProtoPresenceCheckedField;
import com.google.protobuf.FieldType;
import com.google.protobuf.ProtoField;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtoSyntax;
import com.google.protobuf.ProtoMessage;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.ExtensionRegistryLite;

public final class LensSdkParamsProto
{
    private LensSdkParamsProto() {
    }
    
    public static void registerAllExtensions(final ExtensionRegistryLite extensionRegistryLite) {
    }
    
    @ProtoMessage(checkInitialized = {}, messageSetWireFormat = false, protoSyntax = ProtoSyntax.PROTO2)
    public static final class LensSdkParams extends GeneratedMessageLite<LensSdkParams, Builder> implements LensSdkParamsOrBuilder
    {
        public static final int AGSA_VERSION_NAME_FIELD_NUMBER = 2;
        public static final int AR_STICKERS_AVAILABILITY_STATUS_FIELD_NUMBER = 4;
        private static final LensSdkParams DEFAULT_INSTANCE;
        public static final int LENS_AVAILABILITY_STATUS_FIELD_NUMBER = 3;
        public static final int LENS_SDK_VERSION_FIELD_NUMBER = 1;
        private static volatile Parser<LensSdkParams> PARSER;
        @ProtoField(fieldNumber = 2, isEnforceUtf8 = false, isRequired = false, type = FieldType.STRING)
        @ProtoPresenceCheckedField(mask = 2, presenceBitsId = 0)
        private String agsaVersionName_;
        @ProtoField(fieldNumber = 4, isRequired = false, type = FieldType.ENUM)
        @ProtoPresenceCheckedField(mask = 8, presenceBitsId = 0)
        private int arStickersAvailabilityStatus_;
        @ProtoPresenceBits(id = 0)
        private int bitField0_;
        @ProtoField(fieldNumber = 3, isRequired = false, type = FieldType.ENUM)
        @ProtoPresenceCheckedField(mask = 4, presenceBitsId = 0)
        private int lensAvailabilityStatus_;
        @ProtoField(fieldNumber = 1, isEnforceUtf8 = false, isRequired = false, type = FieldType.STRING)
        @ProtoPresenceCheckedField(mask = 1, presenceBitsId = 0)
        private String lensSdkVersion_;
        
        static {
            GeneratedMessageLite.registerDefaultInstance((Class)LensSdkParams.class, (GeneratedMessageLite)(DEFAULT_INSTANCE = new LensSdkParams()));
        }
        
        private LensSdkParams() {
            this.lensSdkVersion_ = "";
            this.agsaVersionName_ = "";
            this.lensAvailabilityStatus_ = -1;
            this.arStickersAvailabilityStatus_ = -1;
        }
        
        private void clearAgsaVersionName() {
            this.bitField0_ &= 0xFFFFFFFD;
            this.agsaVersionName_ = getDefaultInstance().getAgsaVersionName();
        }
        
        private void clearArStickersAvailabilityStatus() {
            this.bitField0_ &= 0xFFFFFFF7;
            this.arStickersAvailabilityStatus_ = -1;
        }
        
        private void clearLensAvailabilityStatus() {
            this.bitField0_ &= 0xFFFFFFFB;
            this.lensAvailabilityStatus_ = -1;
        }
        
        private void clearLensSdkVersion() {
            this.bitField0_ &= 0xFFFFFFFE;
            this.lensSdkVersion_ = getDefaultInstance().getLensSdkVersion();
        }
        
        public static LensSdkParams getDefaultInstance() {
            return LensSdkParams.DEFAULT_INSTANCE;
        }
        
        public static Builder newBuilder() {
            return (Builder)LensSdkParams.DEFAULT_INSTANCE.createBuilder();
        }
        
        public static Builder newBuilder(final LensSdkParams lensSdkParams) {
            return (Builder)LensSdkParams.DEFAULT_INSTANCE.createBuilder((GeneratedMessageLite)lensSdkParams);
        }
        
        public static LensSdkParams parseDelimitedFrom(final InputStream inputStream) throws IOException {
            return (LensSdkParams)parseDelimitedFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, inputStream);
        }
        
        public static LensSdkParams parseDelimitedFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (LensSdkParams)parseDelimitedFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }
        
        public static LensSdkParams parseFrom(final ByteString byteString) throws InvalidProtocolBufferException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, byteString);
        }
        
        public static LensSdkParams parseFrom(final ByteString byteString, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, byteString, extensionRegistryLite);
        }
        
        public static LensSdkParams parseFrom(final CodedInputStream codedInputStream) throws IOException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, codedInputStream);
        }
        
        public static LensSdkParams parseFrom(final CodedInputStream codedInputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, codedInputStream, extensionRegistryLite);
        }
        
        public static LensSdkParams parseFrom(final InputStream inputStream) throws IOException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, inputStream);
        }
        
        public static LensSdkParams parseFrom(final InputStream inputStream, final ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, inputStream, extensionRegistryLite);
        }
        
        public static LensSdkParams parseFrom(final ByteBuffer byteBuffer) throws InvalidProtocolBufferException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, byteBuffer);
        }
        
        public static LensSdkParams parseFrom(final ByteBuffer byteBuffer, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, byteBuffer, extensionRegistryLite);
        }
        
        public static LensSdkParams parseFrom(final byte[] array) throws InvalidProtocolBufferException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, array);
        }
        
        public static LensSdkParams parseFrom(final byte[] array, final ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return (LensSdkParams)GeneratedMessageLite.parseFrom((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE, array, extensionRegistryLite);
        }
        
        public static Parser<LensSdkParams> parser() {
            return (Parser<LensSdkParams>)LensSdkParams.DEFAULT_INSTANCE.getParserForType();
        }
        
        private void setAgsaVersionName(final String agsaVersionName_) {
            if (agsaVersionName_ == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 0x2;
            this.agsaVersionName_ = agsaVersionName_;
        }
        
        private void setAgsaVersionNameBytes(final ByteString byteString) {
            if (byteString == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 0x2;
            this.agsaVersionName_ = byteString.toStringUtf8();
        }
        
        private void setArStickersAvailabilityStatus(final LensAvailabilityStatus lensAvailabilityStatus) {
            if (lensAvailabilityStatus == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 0x8;
            this.arStickersAvailabilityStatus_ = lensAvailabilityStatus.getNumber();
        }
        
        private void setLensAvailabilityStatus(final LensAvailabilityStatus lensAvailabilityStatus) {
            if (lensAvailabilityStatus == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 0x4;
            this.lensAvailabilityStatus_ = lensAvailabilityStatus.getNumber();
        }
        
        private void setLensSdkVersion(final String lensSdkVersion_) {
            if (lensSdkVersion_ == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 0x1;
            this.lensSdkVersion_ = lensSdkVersion_;
        }
        
        private void setLensSdkVersionBytes(final ByteString byteString) {
            if (byteString == null) {
                throw new NullPointerException();
            }
            this.bitField0_ |= 0x1;
            this.lensSdkVersion_ = byteString.toStringUtf8();
        }
        
        protected final Object dynamicMethod(final GeneratedMessageLite$MethodToInvoke generatedMessageLite$MethodToInvoke, final Object o, final Object o2) {
            switch (LensSdkParamsProto$1.$SwitchMap$com$google$protobuf$GeneratedMessageLite$MethodToInvoke[generatedMessageLite$MethodToInvoke.ordinal()]) {
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
                    final Parser<LensSdkParams> parser;
                    if ((parser = LensSdkParams.PARSER) == null) {
                        synchronized (LensSdkParams.class) {
                            if (LensSdkParams.PARSER == null) {
                                LensSdkParams.PARSER = (Parser<LensSdkParams>)new GeneratedMessageLite$DefaultInstanceBasedParser((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE);
                            }
                        }
                    }
                    return parser;
                }
                case 4: {
                    return LensSdkParams.DEFAULT_INSTANCE;
                }
                case 3: {
                    return newMessageInfo((MessageLite)LensSdkParams.DEFAULT_INSTANCE, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0005\u0000\u0000\u0000\u0001\b\u0000\u0002\b\u0001\u0003\f\u0002\u0004\f\u0003", new Object[] { "bitField0_", "lensSdkVersion_", "agsaVersionName_", "lensAvailabilityStatus_", LensAvailabilityStatus.internalGetValueMap(), "arStickersAvailabilityStatus_", LensAvailabilityStatus.internalGetValueMap() });
                }
                case 2: {
                    return new Builder();
                }
                case 1: {
                    return new LensSdkParams();
                }
            }
        }
        
        public String getAgsaVersionName() {
            return this.agsaVersionName_;
        }
        
        public ByteString getAgsaVersionNameBytes() {
            return ByteString.copyFromUtf8(this.agsaVersionName_);
        }
        
        public LensAvailabilityStatus getArStickersAvailabilityStatus() {
            LensAvailabilityStatus lensAvailabilityStatus;
            if ((lensAvailabilityStatus = LensAvailabilityStatus.forNumber(this.arStickersAvailabilityStatus_)) == null) {
                lensAvailabilityStatus = LensAvailabilityStatus.LENS_AVAILABILITY_UNKNOWN;
            }
            return lensAvailabilityStatus;
        }
        
        public LensAvailabilityStatus getLensAvailabilityStatus() {
            LensAvailabilityStatus lensAvailabilityStatus;
            if ((lensAvailabilityStatus = LensAvailabilityStatus.forNumber(this.lensAvailabilityStatus_)) == null) {
                lensAvailabilityStatus = LensAvailabilityStatus.LENS_AVAILABILITY_UNKNOWN;
            }
            return lensAvailabilityStatus;
        }
        
        public String getLensSdkVersion() {
            return this.lensSdkVersion_;
        }
        
        public ByteString getLensSdkVersionBytes() {
            return ByteString.copyFromUtf8(this.lensSdkVersion_);
        }
        
        public boolean hasAgsaVersionName() {
            return (this.bitField0_ & 0x2) == 0x2;
        }
        
        public boolean hasArStickersAvailabilityStatus() {
            return (this.bitField0_ & 0x8) == 0x8;
        }
        
        public boolean hasLensAvailabilityStatus() {
            return (this.bitField0_ & 0x4) == 0x4;
        }
        
        public boolean hasLensSdkVersion() {
            final int bitField0_ = this.bitField0_;
            boolean b = true;
            if ((bitField0_ & 0x1) != 0x1) {
                b = false;
            }
            return b;
        }
        
        public static final class Builder extends GeneratedMessageLite$Builder<LensSdkParams, Builder> implements LensSdkParamsOrBuilder
        {
            private Builder() {
                super((GeneratedMessageLite)LensSdkParams.DEFAULT_INSTANCE);
            }
            
            public Builder clearAgsaVersionName() {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).clearAgsaVersionName();
                return this;
            }
            
            public Builder clearArStickersAvailabilityStatus() {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).clearArStickersAvailabilityStatus();
                return this;
            }
            
            public Builder clearLensAvailabilityStatus() {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).clearLensAvailabilityStatus();
                return this;
            }
            
            public Builder clearLensSdkVersion() {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).clearLensSdkVersion();
                return this;
            }
            
            public String getAgsaVersionName() {
                return ((LensSdkParams)this.instance).getAgsaVersionName();
            }
            
            public ByteString getAgsaVersionNameBytes() {
                return ((LensSdkParams)this.instance).getAgsaVersionNameBytes();
            }
            
            public LensAvailabilityStatus getArStickersAvailabilityStatus() {
                return ((LensSdkParams)this.instance).getArStickersAvailabilityStatus();
            }
            
            public LensAvailabilityStatus getLensAvailabilityStatus() {
                return ((LensSdkParams)this.instance).getLensAvailabilityStatus();
            }
            
            public String getLensSdkVersion() {
                return ((LensSdkParams)this.instance).getLensSdkVersion();
            }
            
            public ByteString getLensSdkVersionBytes() {
                return ((LensSdkParams)this.instance).getLensSdkVersionBytes();
            }
            
            public boolean hasAgsaVersionName() {
                return ((LensSdkParams)this.instance).hasAgsaVersionName();
            }
            
            public boolean hasArStickersAvailabilityStatus() {
                return ((LensSdkParams)this.instance).hasArStickersAvailabilityStatus();
            }
            
            public boolean hasLensAvailabilityStatus() {
                return ((LensSdkParams)this.instance).hasLensAvailabilityStatus();
            }
            
            public boolean hasLensSdkVersion() {
                return ((LensSdkParams)this.instance).hasLensSdkVersion();
            }
            
            public Builder setAgsaVersionName(final String s) {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).setAgsaVersionName(s);
                return this;
            }
            
            public Builder setAgsaVersionNameBytes(final ByteString byteString) {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).setAgsaVersionNameBytes(byteString);
                return this;
            }
            
            public Builder setArStickersAvailabilityStatus(final LensAvailabilityStatus lensAvailabilityStatus) {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).setArStickersAvailabilityStatus(lensAvailabilityStatus);
                return this;
            }
            
            public Builder setLensAvailabilityStatus(final LensAvailabilityStatus lensAvailabilityStatus) {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).setLensAvailabilityStatus(lensAvailabilityStatus);
                return this;
            }
            
            public Builder setLensSdkVersion(final String s) {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).setLensSdkVersion(s);
                return this;
            }
            
            public Builder setLensSdkVersionBytes(final ByteString byteString) {
                this.copyOnWrite();
                ((LensSdkParams)this.instance).setLensSdkVersionBytes(byteString);
                return this;
            }
        }
        
        public enum LensAvailabilityStatus implements Internal$EnumLite
        {
            private static final LensAvailabilityStatus[] $VALUES;
            
            LENS_AVAILABILITY_UNKNOWN(-1);
            
            public static final int LENS_AVAILABILITY_UNKNOWN_VALUE = -1;
            
            LENS_READY(0);
            
            public static final int LENS_READY_VALUE = 0;
            
            LENS_UNAVAILABLE(1), 
            LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE(3);
            
            public static final int LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE_VALUE = 3;
            
            LENS_UNAVAILABLE_DEVICE_LOCKED(5);
            
            public static final int LENS_UNAVAILABLE_DEVICE_LOCKED_VALUE = 5;
            
            LENS_UNAVAILABLE_INVALID_CURSOR(4);
            
            public static final int LENS_UNAVAILABLE_INVALID_CURSOR_VALUE = 4;
            
            LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED(2);
            
            public static final int LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED_VALUE = 2;
            
            LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE(6);
            
            public static final int LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE_VALUE = 6;
            public static final int LENS_UNAVAILABLE_VALUE = 1;
            private static final Internal$EnumLiteMap<LensAvailabilityStatus> internalValueMap;
            private final int value;
            
            static {
                $VALUES = new LensAvailabilityStatus[] { LensAvailabilityStatus.LENS_AVAILABILITY_UNKNOWN, LensAvailabilityStatus.LENS_READY, LensAvailabilityStatus.LENS_UNAVAILABLE, LensAvailabilityStatus.LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED, LensAvailabilityStatus.LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE, LensAvailabilityStatus.LENS_UNAVAILABLE_INVALID_CURSOR, LensAvailabilityStatus.LENS_UNAVAILABLE_DEVICE_LOCKED, LensAvailabilityStatus.LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE };
                internalValueMap = (Internal$EnumLiteMap)new Internal$EnumLiteMap<LensAvailabilityStatus>() {
                    public LensAvailabilityStatus findValueByNumber(final int n) {
                        return LensAvailabilityStatus.forNumber(n);
                    }
                };
            }
            
            private LensAvailabilityStatus(final int value) {
                this.value = value;
            }
            
            public static LensAvailabilityStatus forNumber(final int n) {
                switch (n) {
                    default: {
                        return null;
                    }
                    case 6: {
                        return LensAvailabilityStatus.LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE;
                    }
                    case 5: {
                        return LensAvailabilityStatus.LENS_UNAVAILABLE_DEVICE_LOCKED;
                    }
                    case 4: {
                        return LensAvailabilityStatus.LENS_UNAVAILABLE_INVALID_CURSOR;
                    }
                    case 3: {
                        return LensAvailabilityStatus.LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE;
                    }
                    case 2: {
                        return LensAvailabilityStatus.LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED;
                    }
                    case 1: {
                        return LensAvailabilityStatus.LENS_UNAVAILABLE;
                    }
                    case 0: {
                        return LensAvailabilityStatus.LENS_READY;
                    }
                    case -1: {
                        return LensAvailabilityStatus.LENS_AVAILABILITY_UNKNOWN;
                    }
                }
            }
            
            public static Internal$EnumLiteMap<LensAvailabilityStatus> internalGetValueMap() {
                return LensAvailabilityStatus.internalValueMap;
            }
            
            public final int getNumber() {
                return this.value;
            }
        }
    }
    
    public interface LensSdkParamsOrBuilder extends MessageLiteOrBuilder
    {
        String getAgsaVersionName();
        
        ByteString getAgsaVersionNameBytes();
        
        LensAvailabilityStatus getArStickersAvailabilityStatus();
        
        LensAvailabilityStatus getLensAvailabilityStatus();
        
        String getLensSdkVersion();
        
        ByteString getLensSdkVersionBytes();
        
        boolean hasAgsaVersionName();
        
        boolean hasArStickersAvailabilityStatus();
        
        boolean hasLensAvailabilityStatus();
        
        boolean hasLensSdkVersion();
    }
}
