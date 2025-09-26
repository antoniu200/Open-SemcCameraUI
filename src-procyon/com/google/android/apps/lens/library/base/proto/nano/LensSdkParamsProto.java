// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.lens.library.base.proto.nano;

import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.NanoEnumValue;
import com.google.protobuf.nano.ExtendableMessageNano;

public abstract class LensSdkParamsProto
{
    private LensSdkParamsProto() {
    }
    
    public static final class LensSdkParams extends ExtendableMessageNano<LensSdkParams> implements Cloneable
    {
        private static volatile LensSdkParams[] _emptyArray;
        public String agsaVersionName;
        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public int arStickersAvailabilityStatus;
        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public int lensAvailabilityStatus;
        public String lensSdkVersion;
        
        public LensSdkParams() {
            this.clear();
        }
        
        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public static int checkLensAvailabilityStatusOrThrow(final int i) {
            if (i >= -1 && i <= 6) {
                return i;
            }
            final StringBuilder sb = new StringBuilder(54);
            sb.append(i);
            sb.append(" is not a valid enum LensAvailabilityStatus");
            throw new IllegalArgumentException(sb.toString());
        }
        
        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public static int[] checkLensAvailabilityStatusOrThrow(int[] array) {
            array = array.clone();
            for (int length = array.length, i = 0; i < length; ++i) {
                checkLensAvailabilityStatusOrThrow(array[i]);
            }
            return array;
        }
        
        public static LensSdkParams[] emptyArray() {
            if (LensSdkParams._emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (LensSdkParams._emptyArray == null) {
                        LensSdkParams._emptyArray = new LensSdkParams[0];
                    }
                }
            }
            return LensSdkParams._emptyArray;
        }
        
        public static LensSdkParams parseFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            return new LensSdkParams().mergeFrom(codedInputByteBufferNano);
        }
        
        public static LensSdkParams parseFrom(final byte[] array) throws InvalidProtocolBufferNanoException {
            return MessageNano.mergeFrom(new LensSdkParams(), array);
        }
        
        public LensSdkParams clear() {
            this.lensSdkVersion = "";
            this.agsaVersionName = "";
            this.lensAvailabilityStatus = -1;
            this.arStickersAvailabilityStatus = -1;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }
        
        @Override
        public LensSdkParams clone() {
            try {
                return super.clone();
            }
            catch (final CloneNotSupportedException detailMessage) {
                throw new AssertionError((Object)detailMessage);
            }
        }
        
        @Override
        protected int computeSerializedSize() {
            int computeSerializedSize;
            final int n = computeSerializedSize = super.computeSerializedSize();
            if (this.lensSdkVersion != null) {
                computeSerializedSize = n;
                if (!this.lensSdkVersion.equals("")) {
                    computeSerializedSize = n + CodedOutputByteBufferNano.computeStringSize(1, this.lensSdkVersion);
                }
            }
            int n2 = computeSerializedSize;
            if (this.agsaVersionName != null) {
                n2 = computeSerializedSize;
                if (!this.agsaVersionName.equals("")) {
                    n2 = computeSerializedSize + CodedOutputByteBufferNano.computeStringSize(2, this.agsaVersionName);
                }
            }
            int n3 = n2;
            if (this.lensAvailabilityStatus != -1) {
                n3 = n2 + CodedOutputByteBufferNano.computeInt32Size(3, this.lensAvailabilityStatus);
            }
            int n4 = n3;
            if (this.arStickersAvailabilityStatus != -1) {
                n4 = n3 + CodedOutputByteBufferNano.computeInt32Size(4, this.arStickersAvailabilityStatus);
            }
            return n4;
        }
        
        @Override
        public LensSdkParams mergeFrom(final CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                final int tag = codedInputByteBufferNano.readTag();
                if (tag == 0) {
                    return this;
                }
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
                                final int position = codedInputByteBufferNano.getPosition();
                                try {
                                    this.arStickersAvailabilityStatus = checkLensAvailabilityStatusOrThrow(codedInputByteBufferNano.readInt32());
                                }
                                catch (final IllegalArgumentException ex) {
                                    codedInputByteBufferNano.rewindToPosition(position);
                                    this.storeUnknownField(codedInputByteBufferNano, tag);
                                }
                            }
                        }
                        else {
                            final int position2 = codedInputByteBufferNano.getPosition();
                            try {
                                this.lensAvailabilityStatus = checkLensAvailabilityStatusOrThrow(codedInputByteBufferNano.readInt32());
                            }
                            catch (final IllegalArgumentException ex2) {
                                codedInputByteBufferNano.rewindToPosition(position2);
                                this.storeUnknownField(codedInputByteBufferNano, tag);
                            }
                        }
                    }
                    else {
                        this.agsaVersionName = codedInputByteBufferNano.readString();
                    }
                }
                else {
                    this.lensSdkVersion = codedInputByteBufferNano.readString();
                }
            }
        }
        
        @Override
        public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            if (this.lensSdkVersion != null && !this.lensSdkVersion.equals("")) {
                codedOutputByteBufferNano.writeString(1, this.lensSdkVersion);
            }
            if (this.agsaVersionName != null && !this.agsaVersionName.equals("")) {
                codedOutputByteBufferNano.writeString(2, this.agsaVersionName);
            }
            if (this.lensAvailabilityStatus != -1) {
                codedOutputByteBufferNano.writeInt32(3, this.lensAvailabilityStatus);
            }
            if (this.arStickersAvailabilityStatus != -1) {
                codedOutputByteBufferNano.writeInt32(4, this.arStickersAvailabilityStatus);
            }
            super.writeTo(codedOutputByteBufferNano);
        }
        
        public interface LensAvailabilityStatus
        {
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_AVAILABILITY_UNKNOWN = -1;
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_READY = 0;
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_UNAVAILABLE = 1;
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE = 3;
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_UNAVAILABLE_DEVICE_LOCKED = 5;
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_UNAVAILABLE_INVALID_CURSOR = 4;
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED = 2;
            @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
            public static final int LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE = 6;
        }
    }
}
