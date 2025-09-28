package com.google.protobuf.nano;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class MessageNano {
    public static final int UNSET_ENUM_VALUE = Integer.MIN_VALUE;
    protected volatile int cachedSize = -1;

    public interface GeneratedMapEntry {
    }

    protected int computeSerializedSize() {
        return 0;
    }

    public abstract MessageNano mergeFrom(CodedInputByteBufferNano input) throws IOException;

    public void writeTo(CodedOutputByteBufferNano output) throws IOException {
    }

    public int getCachedSize() {
        if (this.cachedSize < 0) {
            getSerializedSize();
        }
        return this.cachedSize;
    }

    public int getSerializedSize() {
        int iComputeSerializedSize = computeSerializedSize();
        this.cachedSize = iComputeSerializedSize;
        return iComputeSerializedSize;
    }

    public static final byte[] toByteArray(MessageNano msg) {
        byte[] bArr = new byte[msg.getSerializedSize()];
        toByteArray(msg, bArr, 0, bArr.length);
        return bArr;
    }

    public static final void toByteArray(MessageNano msg, byte[] data, int offset, int length) {
        try {
            CodedOutputByteBufferNano codedOutputByteBufferNanoNewInstance = CodedOutputByteBufferNano.newInstance(data, offset, length);
            msg.writeTo(codedOutputByteBufferNanoNewInstance);
            codedOutputByteBufferNanoNewInstance.checkNoSpaceLeft();
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public static final <T extends MessageNano> T mergeFrom(T t, byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (T) mergeFrom(t, bArr, 0, bArr.length);
    }

    public static final <T extends MessageNano> T mergeFrom(T msg, final byte[] data, final int off, final int len) throws InvalidProtocolBufferNanoException {
        try {
            CodedInputByteBufferNano codedInputByteBufferNanoNewInstance = CodedInputByteBufferNano.newInstance(data, off, len);
            msg.mergeFrom(codedInputByteBufferNanoNewInstance);
            codedInputByteBufferNanoNewInstance.checkLastTagWas(0);
            return msg;
        } catch (InvalidProtocolBufferNanoException e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
        }
    }

    public static final <T extends MessageNano> T cloneUsingSerialization(T t) {
        try {
            return (T) mergeFrom((MessageNano) t.getClass().getConstructor(new Class[0]).newInstance(new Object[0]), toByteArray(t));
        } catch (InvalidProtocolBufferNanoException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException(e2);
        } catch (InstantiationException e3) {
            throw new IllegalStateException(e3);
        } catch (NoSuchMethodException e4) {
            throw new IllegalStateException(e4);
        } catch (InvocationTargetException e5) {
            throw new IllegalStateException(e5);
        }
    }

    public static final boolean messageNanoEquals(MessageNano a, MessageNano b) {
        int serializedSize;
        if (a == b) {
            return true;
        }
        if (a == null || b == null || a.getClass() != b.getClass() || b.getSerializedSize() != (serializedSize = a.getSerializedSize())) {
            return false;
        }
        byte[] bArr = new byte[serializedSize];
        byte[] bArr2 = new byte[serializedSize];
        toByteArray(a, bArr, 0, serializedSize);
        toByteArray(b, bArr2, 0, serializedSize);
        return Arrays.equals(bArr, bArr2);
    }

    public String toString() {
        return MessageNanoPrinter.print(this);
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public MessageNano mo6clone() throws CloneNotSupportedException {
        return (MessageNano) super.clone();
    }
}
