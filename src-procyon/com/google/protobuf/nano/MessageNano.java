// 
// Decompiled by Procyon v0.6.0
// 

package com.google.protobuf.nano;

import java.util.Arrays;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class MessageNano
{
    public static final int UNSET_ENUM_VALUE = Integer.MIN_VALUE;
    protected volatile int cachedSize;
    
    public MessageNano() {
        this.cachedSize = -1;
    }
    
    public static final <T extends MessageNano> T cloneUsingSerialization(final T t) {
        try {
            return mergeFrom((T)t.getClass().getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]), toByteArray(t));
        }
        catch (final InvalidProtocolBufferNanoException cause) {
            throw new IllegalStateException(cause);
        }
        catch (final IllegalAccessException cause2) {
            throw new IllegalStateException(cause2);
        }
        catch (final InvocationTargetException cause3) {
            throw new IllegalStateException(cause3);
        }
        catch (final InstantiationException cause4) {
            throw new IllegalStateException(cause4);
        }
        catch (final NoSuchMethodException cause5) {
            throw new IllegalStateException(cause5);
        }
    }
    
    public static final <T extends MessageNano> T mergeFrom(final T t, final byte[] array) throws InvalidProtocolBufferNanoException {
        return mergeFrom(t, array, 0, array.length);
    }
    
    public static final <T extends MessageNano> T mergeFrom(final T t, final byte[] array, final int n, final int n2) throws InvalidProtocolBufferNanoException {
        try {
            final CodedInputByteBufferNano instance = CodedInputByteBufferNano.newInstance(array, n, n2);
            t.mergeFrom(instance);
            instance.checkLastTagWas(0);
            return t;
        }
        catch (final IOException cause) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", cause);
        }
        catch (final InvalidProtocolBufferNanoException ex) {
            throw ex;
        }
    }
    
    public static final boolean messageNanoEquals(final MessageNano messageNano, final MessageNano messageNano2) {
        if (messageNano == messageNano2) {
            return true;
        }
        if (messageNano == null || messageNano2 == null) {
            return false;
        }
        if (messageNano.getClass() != messageNano2.getClass()) {
            return false;
        }
        final int serializedSize = messageNano.getSerializedSize();
        if (messageNano2.getSerializedSize() != serializedSize) {
            return false;
        }
        final byte[] a = new byte[serializedSize];
        final byte[] a2 = new byte[serializedSize];
        toByteArray(messageNano, a, 0, serializedSize);
        toByteArray(messageNano2, a2, 0, serializedSize);
        return Arrays.equals(a, a2);
    }
    
    public static final void toByteArray(final MessageNano messageNano, final byte[] array, final int n, final int n2) {
        try {
            final CodedOutputByteBufferNano instance = CodedOutputByteBufferNano.newInstance(array, n, n2);
            messageNano.writeTo(instance);
            instance.checkNoSpaceLeft();
        }
        catch (final IOException cause) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", cause);
        }
    }
    
    public static final byte[] toByteArray(final MessageNano messageNano) {
        final byte[] array = new byte[messageNano.getSerializedSize()];
        toByteArray(messageNano, array, 0, array.length);
        return array;
    }
    
    public MessageNano clone() throws CloneNotSupportedException {
        return (MessageNano)super.clone();
    }
    
    protected int computeSerializedSize() {
        return 0;
    }
    
    public int getCachedSize() {
        if (this.cachedSize < 0) {
            this.getSerializedSize();
        }
        return this.cachedSize;
    }
    
    public int getSerializedSize() {
        return this.cachedSize = this.computeSerializedSize();
    }
    
    public abstract MessageNano mergeFrom(final CodedInputByteBufferNano p0) throws IOException;
    
    @Override
    public String toString() {
        return MessageNanoPrinter.print(this);
    }
    
    public void writeTo(final CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
    }
    
    public interface GeneratedMapEntry
    {
    }
}
