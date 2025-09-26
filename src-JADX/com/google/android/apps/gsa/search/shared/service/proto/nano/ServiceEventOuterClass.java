package com.google.android.apps.gsa.search.shared.service.proto.nano;

import com.google.protobuf.nano.NanoEnumValue;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ServiceEventOuterClass {
    private ServiceEventOuterClass() {
    }

    @NanoEnumValue(legacy = false, value = ServiceEventId.class)
    public static int checkServiceEventIdOrThrow(int value) {
        if (value >= 1 && value <= 10) {
            return value;
        }
        if (value >= 12 && value <= 18) {
            return value;
        }
        if (value >= 20 && value <= 22) {
            return value;
        }
        if (value >= 24 && value <= 27) {
            return value;
        }
        if (value >= 29 && value <= 39) {
            return value;
        }
        if (value >= 42 && value <= 51) {
            return value;
        }
        if (value >= 56 && value <= 85) {
            return value;
        }
        if (value >= 87 && value <= 89) {
            return value;
        }
        if (value >= 91 && value <= 91) {
            return value;
        }
        if (value >= 94 && value <= 108) {
            return value;
        }
        if (value >= 110 && value <= 111) {
            return value;
        }
        if (value >= 113 && value <= 113) {
            return value;
        }
        if (value >= 115 && value <= 123) {
            return value;
        }
        if (value >= 126 && value <= 146) {
            return value;
        }
        if (value >= 148 && value <= 158) {
            return value;
        }
        if (value >= 160 && value <= 160) {
            return value;
        }
        if (value >= 162 && value <= 168) {
            return value;
        }
        if (value >= 170 && value <= 172) {
            return value;
        }
        if (value >= 174 && value <= 174) {
            return value;
        }
        if (value >= 176 && value <= 176) {
            return value;
        }
        if (value >= 178 && value <= 189) {
            return value;
        }
        if (value >= 191 && value <= 207) {
            return value;
        }
        if (value >= 210 && value <= 240) {
            return value;
        }
        StringBuilder sb = new StringBuilder(46);
        sb.append(value);
        sb.append(" is not a valid enum ServiceEventId");
        throw new IllegalArgumentException(sb.toString());
    }

    @NanoEnumValue(legacy = false, value = ServiceEventId.class)
    public static int[] checkServiceEventIdOrThrow(int[] values) {
        int[] iArr = (int[]) values.clone();
        for (int i : iArr) {
            checkServiceEventIdOrThrow(i);
        }
        return iArr;
    }
}
