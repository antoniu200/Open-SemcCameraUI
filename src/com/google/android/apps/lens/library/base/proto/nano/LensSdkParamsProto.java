package com.google.android.apps.lens.library.base.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.ExtendableMessageNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.NanoEnumValue;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class LensSdkParamsProto {
    private LensSdkParamsProto() {
    }

    public static final class LensSdkParams extends ExtendableMessageNano<LensSdkParams> implements Cloneable {
        private static volatile LensSdkParams[] _emptyArray;
        public String agsaVersionName;

        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public int arStickersAvailabilityStatus;

        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public int lensAvailabilityStatus;
        public String lensSdkVersion;

        public interface LensAvailabilityStatus {

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

        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public static int checkLensAvailabilityStatusOrThrow(int value) {
            if (value >= -1 && value <= 6) {
                return value;
            }
            StringBuilder sb = new StringBuilder(54);
            sb.append(value);
            sb.append(" is not a valid enum LensAvailabilityStatus");
            throw new IllegalArgumentException(sb.toString());
        }

        @NanoEnumValue(legacy = false, value = LensAvailabilityStatus.class)
        public static int[] checkLensAvailabilityStatusOrThrow(int[] values) {
            int[] iArr = (int[]) values.clone();
            for (int i : iArr) {
                checkLensAvailabilityStatusOrThrow(i);
            }
            return iArr;
        }

        public static LensSdkParams[] emptyArray() {
            if (_emptyArray == null) {
                synchronized (InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new LensSdkParams[0];
                    }
                }
            }
            return _emptyArray;
        }

        public LensSdkParams() {
            clear();
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

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        /* renamed from: clone */
        public LensSdkParams mo6clone() {
            try {
                return (LensSdkParams) super.mo6clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        public void writeTo(CodedOutputByteBufferNano output) throws IOException {
            if (this.lensSdkVersion != null && !this.lensSdkVersion.equals("")) {
                output.writeString(1, this.lensSdkVersion);
            }
            if (this.agsaVersionName != null && !this.agsaVersionName.equals("")) {
                output.writeString(2, this.agsaVersionName);
            }
            if (this.lensAvailabilityStatus != -1) {
                output.writeInt32(3, this.lensAvailabilityStatus);
            }
            if (this.arStickersAvailabilityStatus != -1) {
                output.writeInt32(4, this.arStickersAvailabilityStatus);
            }
            super.writeTo(output);
        }

        @Override // com.google.protobuf.nano.ExtendableMessageNano, com.google.protobuf.nano.MessageNano
        protected int computeSerializedSize() {
            int iComputeSerializedSize = super.computeSerializedSize();
            if (this.lensSdkVersion != null && !this.lensSdkVersion.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(1, this.lensSdkVersion);
            }
            if (this.agsaVersionName != null && !this.agsaVersionName.equals("")) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.agsaVersionName);
            }
            if (this.lensAvailabilityStatus != -1) {
                iComputeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, this.lensAvailabilityStatus);
            }
            return this.arStickersAvailabilityStatus != -1 ? iComputeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(4, this.arStickersAvailabilityStatus) : iComputeSerializedSize;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public LensSdkParams mergeFrom(CodedInputByteBufferNano input) throws IOException {
            while (true) {
                int tag = input.readTag();
                if (tag == 0) {
                    return this;
                }
                if (tag == 10) {
                    this.lensSdkVersion = input.readString();
                } else if (tag == 18) {
                    this.agsaVersionName = input.readString();
                } else if (tag == 24) {
                    int position = input.getPosition();
                    try {
                        this.lensAvailabilityStatus = checkLensAvailabilityStatusOrThrow(input.readInt32());
                    } catch (IllegalArgumentException unused) {
                        input.rewindToPosition(position);
                        storeUnknownField(input, tag);
                    }
                } else if (tag != 32) {
                    if (!super.storeUnknownField(input, tag)) {
                        return this;
                    }
                } else {
                    int position2 = input.getPosition();
                    try {
                        this.arStickersAvailabilityStatus = checkLensAvailabilityStatusOrThrow(input.readInt32());
                    } catch (IllegalArgumentException unused2) {
                        input.rewindToPosition(position2);
                        storeUnknownField(input, tag);
                    }
                }
            }
        }

        public static LensSdkParams parseFrom(byte[] data) throws InvalidProtocolBufferNanoException {
            return (LensSdkParams) MessageNano.mergeFrom(new LensSdkParams(), data);
        }

        public static LensSdkParams parseFrom(CodedInputByteBufferNano input) throws IOException {
            return new LensSdkParams().mergeFrom(input);
        }
    }
}
