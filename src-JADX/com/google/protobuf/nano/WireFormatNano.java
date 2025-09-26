package com.google.protobuf.nano;

import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class WireFormatNano {
    static final int MESSAGE_SET_ITEM = 1;
    static final int MESSAGE_SET_MESSAGE = 3;
    static final int MESSAGE_SET_TYPE_ID = 2;
    static final int TAG_TYPE_BITS = 3;
    static final int TAG_TYPE_MASK = 7;
    public static final int WIRETYPE_END_GROUP = 4;
    public static final int WIRETYPE_FIXED32 = 5;
    public static final int WIRETYPE_FIXED64 = 1;
    public static final int WIRETYPE_LENGTH_DELIMITED = 2;
    public static final int WIRETYPE_START_GROUP = 3;
    public static final int WIRETYPE_VARINT = 0;
    static final int MESSAGE_SET_ITEM_TAG = makeTag(1, 3);
    static final int MESSAGE_SET_ITEM_END_TAG = makeTag(1, 4);
    static final int MESSAGE_SET_TYPE_ID_TAG = makeTag(2, 0);
    static final int MESSAGE_SET_MESSAGE_TAG = makeTag(3, 2);
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final byte[][] EMPTY_BYTES_ARRAY = new byte[0][];
    public static final byte[] EMPTY_BYTES = new byte[0];

    public static int getTagFieldNumber(final int tag) {
        return tag >>> 3;
    }

    static int getTagWireType(final int tag) {
        return tag & 7;
    }

    public static int makeTag(final int fieldNumber, final int wireType) {
        return (fieldNumber << 3) | wireType;
    }

    private WireFormatNano() {
    }

    public static boolean parseUnknownField(final CodedInputByteBufferNano input, final int tag) throws IOException {
        return input.skipField(tag);
    }

    public static final int getRepeatedFieldArrayLength(final CodedInputByteBufferNano input, final int tag) throws IOException {
        int position = input.getPosition();
        input.skipField(tag);
        int i = 1;
        while (input.readTag() == tag) {
            input.skipField(tag);
            i++;
        }
        input.rewindToPositionAndTag(position, tag);
        return i;
    }
}
