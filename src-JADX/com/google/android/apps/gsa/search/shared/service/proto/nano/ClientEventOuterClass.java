package com.google.android.apps.gsa.search.shared.service.proto.nano;

import com.google.protobuf.nano.NanoEnumValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ClientEventOuterClass {
    private ClientEventOuterClass() {
    }

    @NanoEnumValue(legacy = false, value = ClientEventId.class)
    public static int checkClientEventIdOrThrow(int value) {
        if (value >= 0 && value <= 7) {
            return value;
        }
        if (value >= 9 && value <= 12) {
            return value;
        }
        if (value >= 14 && value <= 18) {
            return value;
        }
        if (value >= 23 && value <= 25) {
            return value;
        }
        if (value >= 27 && value <= 27) {
            return value;
        }
        if (value >= 29 && value <= 29) {
            return value;
        }
        if (value >= 31 && value <= 49) {
            return value;
        }
        if (value >= 51 && value <= 55) {
            return value;
        }
        if (value >= 57 && value <= 90) {
            return value;
        }
        if (value >= 92 && value <= 100) {
            return value;
        }
        if (value >= 103 && value <= 103) {
            return value;
        }
        if (value >= 105 && value <= 108) {
            return value;
        }
        if (value >= 110 && value <= 158) {
            return value;
        }
        if (value >= 160 && value <= 163) {
            return value;
        }
        if (value >= 166 && value <= 171) {
            return value;
        }
        if (value >= 173 && value <= 175) {
            return value;
        }
        if (value >= 178 && value <= 192) {
            return value;
        }
        if (value >= 194 && value <= 200) {
            return value;
        }
        if (value >= 203 && value <= 216) {
            return value;
        }
        if (value >= 218 && value <= 228) {
            return value;
        }
        if (value >= 230 && value <= 233) {
            return value;
        }
        if (value >= 235 && value <= 251) {
            return value;
        }
        if (value >= 253 && value <= 265) {
            return value;
        }
        if (value >= 267 && value <= 309) {
            return value;
        }
        if (value >= 311 && value <= 348) {
            return value;
        }
        StringBuilder sb = new StringBuilder(45);
        sb.append(value);
        sb.append(" is not a valid enum ClientEventId");
        throw new IllegalArgumentException(sb.toString());
    }

    @NanoEnumValue(legacy = false, value = ClientEventId.class)
    public static int[] checkClientEventIdOrThrow(int[] values) {
        int[] iArr = (int[]) values.clone();
        for (int i : iArr) {
            checkClientEventIdOrThrow(i);
        }
        return iArr;
    }
}
