package org.apache.commons.imaging.formats.jpeg.decoder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class Dct {
    private static final float[] DCT_SCALING_FACTORS = {(float) (0.5d / Math.sqrt(2.0d)), (float) (0.25d / Math.cos(0.19634954084936207d)), (float) (0.25d / Math.cos(0.39269908169872414d)), (float) (0.25d / Math.cos(0.5890486225480862d)), (float) (0.25d / Math.cos(0.7853981633974483d)), (float) (0.25d / Math.cos(0.9817477042468103d)), (float) (0.25d / Math.cos(1.1780972450961724d)), (float) (0.25d / Math.cos(1.3744467859455345d))};
    private static final float[] IDCT_SCALING_FACTORS = {(float) ((8.0d / Math.sqrt(2.0d)) * 0.0625d), (float) ((Math.cos(0.19634954084936207d) * 4.0d) * 0.125d), (float) ((Math.cos(0.39269908169872414d) * 4.0d) * 0.125d), (float) ((Math.cos(0.5890486225480862d) * 4.0d) * 0.125d), (float) ((Math.cos(0.7853981633974483d) * 4.0d) * 0.125d), (float) ((Math.cos(0.9817477042468103d) * 4.0d) * 0.125d), (float) ((Math.cos(1.1780972450961724d) * 4.0d) * 0.125d), (float) ((4.0d * Math.cos(1.3744467859455345d)) * 0.125d)};
    private static final float A1 = (float) Math.cos(0.7853981633974483d);
    private static final float A2 = (float) (Math.cos(0.39269908169872414d) - Math.cos(1.1780972450961724d));
    private static final float A3 = A1;
    private static final float A4 = (float) (Math.cos(0.39269908169872414d) + Math.cos(1.1780972450961724d));
    private static final float A5 = (float) Math.cos(1.1780972450961724d);
    private static final float C2 = (float) (Math.cos(0.39269908169872414d) * 2.0d);
    private static final float C4 = (float) (Math.cos(0.7853981633974483d) * 2.0d);
    private static final float C6 = (float) (2.0d * Math.cos(1.1780972450961724d));
    private static final float Q = C2 - C6;
    private static final float R = C2 + C6;

    private Dct() {
    }

    public static void scaleQuantizationVector(float[] fArr) {
        for (int i = 0; i < 8; i++) {
            fArr[i] = fArr[i] * DCT_SCALING_FACTORS[i];
        }
    }

    public static void scaleDequantizationVector(float[] fArr) {
        for (int i = 0; i < 8; i++) {
            fArr[i] = fArr[i] * IDCT_SCALING_FACTORS[i];
        }
    }

    public static void scaleQuantizationMatrix(float[] fArr) {
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                int i3 = (8 * i) + i2;
                fArr[i3] = fArr[i3] * DCT_SCALING_FACTORS[i] * DCT_SCALING_FACTORS[i2];
            }
        }
    }

    public static void scaleDequantizationMatrix(float[] fArr) {
        for (int i = 0; i < 8; i++) {
            for (int i2 = 0; i2 < 8; i2++) {
                int i3 = (8 * i) + i2;
                fArr[i3] = fArr[i3] * IDCT_SCALING_FACTORS[i] * IDCT_SCALING_FACTORS[i2];
            }
        }
    }

    public static void forwardDCT8(float[] fArr) {
        float f = fArr[0] + fArr[7];
        float f2 = fArr[1] + fArr[6];
        float f3 = fArr[2] + fArr[5];
        float f4 = fArr[3] + fArr[4];
        float f5 = fArr[3] - fArr[4];
        float f6 = fArr[2] - fArr[5];
        float f7 = fArr[1] - fArr[6];
        float f8 = fArr[0] - fArr[7];
        float f9 = f + f4;
        float f10 = f2 + f3;
        float f11 = f - f4;
        float f12 = f5 + f6;
        float f13 = f6 + f7;
        float f14 = f7 + f8;
        float f15 = ((f2 - f3) + f11) * A1;
        float f16 = (f14 - f12) * A5;
        float f17 = (f12 * A2) - f16;
        float f18 = f13 * A3;
        float f19 = (f14 * A4) - f16;
        float f20 = f8 + f18;
        float f21 = f8 - f18;
        fArr[0] = f9 + f10;
        fArr[4] = f9 - f10;
        fArr[2] = f11 + f15;
        fArr[6] = f11 - f15;
        fArr[5] = f21 + f17;
        fArr[1] = f20 + f19;
        fArr[7] = f20 - f19;
        fArr[3] = f21 - f17;
    }

    public static void forwardDCT8x8(float[] fArr) {
        for (int i = 0; i < 8; i++) {
            int i2 = 8 * i;
            int i3 = i2 + 7;
            float f = fArr[i2] + fArr[i3];
            int i4 = i2 + 1;
            int i5 = i2 + 6;
            float f2 = fArr[i4] + fArr[i5];
            int i6 = i2 + 2;
            int i7 = i2 + 5;
            float f3 = fArr[i6] + fArr[i7];
            int i8 = i2 + 3;
            int i9 = i2 + 4;
            float f4 = fArr[i8] + fArr[i9];
            float f5 = fArr[i8] - fArr[i9];
            float f6 = fArr[i6] - fArr[i7];
            float f7 = fArr[i4] - fArr[i5];
            float f8 = fArr[i2] - fArr[i3];
            float f9 = f + f4;
            float f10 = f2 + f3;
            float f11 = f - f4;
            float f12 = f5 + f6;
            float f13 = f6 + f7;
            float f14 = f7 + f8;
            float f15 = ((f2 - f3) + f11) * A1;
            float f16 = (f14 - f12) * A5;
            float f17 = (f12 * A2) - f16;
            float f18 = f13 * A3;
            float f19 = (f14 * A4) - f16;
            float f20 = f8 + f18;
            float f21 = f8 - f18;
            fArr[i2] = f9 + f10;
            fArr[i9] = f9 - f10;
            fArr[i6] = f11 + f15;
            fArr[i5] = f11 - f15;
            fArr[i7] = f21 + f17;
            fArr[i4] = f20 + f19;
            fArr[i3] = f20 - f19;
            fArr[i8] = f21 - f17;
        }
        for (int i10 = 0; i10 < 8; i10++) {
            int i11 = 56 + i10;
            float f22 = fArr[i10] + fArr[i11];
            int i12 = 8 + i10;
            int i13 = 48 + i10;
            float f23 = fArr[i12] + fArr[i13];
            int i14 = 16 + i10;
            int i15 = 40 + i10;
            float f24 = fArr[i14] + fArr[i15];
            int i16 = 24 + i10;
            int i17 = 32 + i10;
            float f25 = fArr[i16] + fArr[i17];
            float f26 = fArr[i16] - fArr[i17];
            float f27 = fArr[i14] - fArr[i15];
            float f28 = fArr[i12] - fArr[i13];
            float f29 = fArr[i10] - fArr[i11];
            float f30 = f22 + f25;
            float f31 = f23 + f24;
            float f32 = f22 - f25;
            float f33 = f26 + f27;
            float f34 = f27 + f28;
            float f35 = f28 + f29;
            float f36 = ((f23 - f24) + f32) * A1;
            float f37 = (f35 - f33) * A5;
            float f38 = (f33 * A2) - f37;
            float f39 = f34 * A3;
            float f40 = (f35 * A4) - f37;
            float f41 = f29 + f39;
            float f42 = f29 - f39;
            fArr[i10] = f30 + f31;
            fArr[i17] = f30 - f31;
            fArr[i14] = f32 + f36;
            fArr[i13] = f32 - f36;
            fArr[i15] = f42 + f38;
            fArr[i12] = f41 + f40;
            fArr[i11] = f41 - f40;
            fArr[i16] = f42 - f38;
        }
    }

    public static void inverseDCT8(float[] fArr) {
        float f = fArr[2] - fArr[6];
        float f2 = fArr[2] + fArr[6];
        float f3 = fArr[5] - fArr[3];
        float f4 = fArr[1] + fArr[7];
        float f5 = fArr[3] + fArr[5];
        float f6 = f4 - f5;
        float f7 = fArr[1] - fArr[7];
        float f8 = f4 + f5;
        float f9 = C6 * (f3 + f7);
        float f10 = (Q * f3) + f9;
        float f11 = (R * f7) - f9;
        float f12 = f * C4;
        float f13 = f11 - f8;
        float f14 = f13 - (f6 * C4);
        float f15 = fArr[0] - fArr[4];
        float f16 = f12 - f2;
        float f17 = fArr[0] + fArr[4];
        float f18 = f15 + f16;
        float f19 = f17 + f2;
        float f20 = f15 - f16;
        float f21 = f17 - f2;
        float f22 = f10 + f14;
        fArr[0] = f19 + f8;
        fArr[1] = f18 + f13;
        fArr[2] = f20 - f14;
        fArr[3] = f21 + f22;
        fArr[4] = f21 - f22;
        fArr[5] = f20 + f14;
        fArr[6] = f18 - f13;
        fArr[7] = f19 - f8;
    }

    public static void inverseDCT8x8(float[] fArr) {
        for (int i = 0; i < 8; i++) {
            int i2 = 8 * i;
            int i3 = i2 + 2;
            int i4 = i2 + 6;
            float f = fArr[i3] - fArr[i4];
            float f2 = fArr[i3] + fArr[i4];
            int i5 = i2 + 5;
            int i6 = i2 + 3;
            float f3 = fArr[i5] - fArr[i6];
            int i7 = i2 + 1;
            int i8 = i2 + 7;
            float f4 = fArr[i7] + fArr[i8];
            float f5 = fArr[i6] + fArr[i5];
            float f6 = f4 - f5;
            float f7 = fArr[i7] - fArr[i8];
            float f8 = f4 + f5;
            float f9 = C6 * (f3 + f7);
            float f10 = (Q * f3) + f9;
            float f11 = (R * f7) - f9;
            float f12 = f * C4;
            float f13 = f11 - f8;
            float f14 = f13 - (f6 * C4);
            int i9 = i2 + 4;
            float f15 = fArr[i2] - fArr[i9];
            float f16 = f12 - f2;
            float f17 = fArr[i2] + fArr[i9];
            float f18 = f15 + f16;
            float f19 = f17 + f2;
            float f20 = f15 - f16;
            float f21 = f17 - f2;
            float f22 = f10 + f14;
            fArr[i2] = f19 + f8;
            fArr[i7] = f18 + f13;
            fArr[i3] = f20 - f14;
            fArr[i6] = f21 + f22;
            fArr[i9] = f21 - f22;
            fArr[i5] = f20 + f14;
            fArr[i4] = f18 - f13;
            fArr[i8] = f19 - f8;
        }
        for (int i10 = 0; i10 < 8; i10++) {
            int i11 = 16 + i10;
            int i12 = 48 + i10;
            float f23 = fArr[i11] - fArr[i12];
            float f24 = fArr[i11] + fArr[i12];
            int i13 = 40 + i10;
            int i14 = 24 + i10;
            float f25 = fArr[i13] - fArr[i14];
            int i15 = 8 + i10;
            int i16 = 56 + i10;
            float f26 = fArr[i15] + fArr[i16];
            float f27 = fArr[i14] + fArr[i13];
            float f28 = f26 - f27;
            float f29 = fArr[i15] - fArr[i16];
            float f30 = f26 + f27;
            float f31 = C6 * (f25 + f29);
            float f32 = (Q * f25) + f31;
            float f33 = (R * f29) - f31;
            float f34 = f23 * C4;
            float f35 = f33 - f30;
            float f36 = f35 - (f28 * C4);
            int i17 = 32 + i10;
            float f37 = fArr[i10] - fArr[i17];
            float f38 = f34 - f24;
            float f39 = fArr[i10] + fArr[i17];
            float f40 = f37 + f38;
            float f41 = f39 + f24;
            float f42 = f37 - f38;
            float f43 = f39 - f24;
            float f44 = f32 + f36;
            fArr[i10] = f41 + f30;
            fArr[i15] = f40 + f35;
            fArr[i11] = f42 - f36;
            fArr[i14] = f43 + f44;
            fArr[i17] = f43 - f44;
            fArr[i13] = f42 + f36;
            fArr[i12] = f40 - f35;
            fArr[i16] = f41 - f30;
        }
    }
}
