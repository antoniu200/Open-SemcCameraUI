package org.apache.commons.imaging.palette;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Comparator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ColorSpaceSubset {
    public static final RgbComparator RGB_COMPARATOR = new RgbComparator();
    private int index;
    final int[] maxs;
    final int[] mins;
    final int precision;
    final int precisionMask;
    int rgb;
    final int total;

    ColorSpaceSubset(int i, int i2) {
        this.total = i;
        this.precision = i2;
        this.precisionMask = (1 << i2) - 1;
        this.mins = new int[3];
        this.maxs = new int[3];
        for (int i3 = 0; i3 < 3; i3++) {
            this.mins[i3] = 0;
            this.maxs[i3] = this.precisionMask;
        }
        this.rgb = -1;
    }

    ColorSpaceSubset(int i, int i2, int[] iArr, int[] iArr2) {
        this.total = i;
        this.precision = i2;
        this.mins = iArr;
        this.maxs = iArr2;
        this.precisionMask = (1 << i2) - 1;
        this.rgb = -1;
    }

    public final boolean contains(int i, int i2, int i3) {
        int i4;
        int i5;
        int i6 = i >> (8 - this.precision);
        return this.mins[0] <= i6 && this.maxs[0] >= i6 && this.mins[1] <= (i4 = i2 >> (8 - this.precision)) && this.maxs[1] >= i4 && this.mins[2] <= (i5 = i3 >> (8 - this.precision)) && this.maxs[2] >= i5;
    }

    public void dump(String str) {
        int i = (this.maxs[0] - this.mins[0]) + 1;
        int i2 = (this.maxs[1] - this.mins[1]) + 1;
        int i3 = (this.maxs[2] - this.mins[2]) + 1;
        System.out.println(str + ": [" + Integer.toHexString(this.rgb) + "] total : " + this.total);
        System.out.println("\trgb: " + Integer.toHexString(this.rgb) + ", red: " + Integer.toHexString(this.mins[0] << (8 - this.precision)) + ", " + Integer.toHexString(this.maxs[0] << (8 - this.precision)) + ", green: " + Integer.toHexString(this.mins[1] << (8 - this.precision)) + ", " + Integer.toHexString(this.maxs[1] << (8 - this.precision)) + ", blue: " + Integer.toHexString(this.mins[2] << (8 - this.precision)) + ", " + Integer.toHexString(this.maxs[2] << (8 - this.precision)));
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("\tred: ");
        sb.append(this.mins[0]);
        sb.append(", ");
        sb.append(this.maxs[0]);
        sb.append(", ");
        sb.append("green: ");
        sb.append(this.mins[1]);
        sb.append(", ");
        sb.append(this.maxs[1]);
        sb.append(", ");
        sb.append("blue: ");
        sb.append(this.mins[2]);
        sb.append(", ");
        sb.append(this.maxs[2]);
        printStream.println(sb.toString());
        System.out.println("\trdiff: " + i + ", gdiff: " + i2 + ", bdiff: " + i3 + ", colorArea: " + (i * i2 * i3));
    }

    public void dumpJustRGB(String str) {
        System.out.println("\trgb: " + Integer.toHexString(this.rgb) + ", red: " + Integer.toHexString(this.mins[0] << (8 - this.precision)) + ", " + Integer.toHexString(this.maxs[0] << (8 - this.precision)) + ", green: " + Integer.toHexString(this.mins[1] << (8 - this.precision)) + ", " + Integer.toHexString(this.maxs[1] << (8 - this.precision)) + ", blue: " + Integer.toHexString(this.mins[2] << (8 - this.precision)) + ", " + Integer.toHexString(this.maxs[2] << (8 - this.precision)));
    }

    public int getArea() {
        return ((this.maxs[0] - this.mins[0]) + 1) * ((this.maxs[1] - this.mins[1]) + 1) * ((this.maxs[2] - this.mins[2]) + 1);
    }

    public void setAverageRGB(int[] iArr) {
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        for (int i = this.mins[0]; i <= this.maxs[0]; i++) {
            int i2 = 1;
            int i3 = this.mins[1];
            while (i3 <= this.maxs[i2]) {
                int i4 = this.mins[2];
                for (int i5 = 2; i4 <= this.maxs[i5]; i5 = 2) {
                    int i6 = iArr[(i4 << (this.precision * i5)) | (i3 << (this.precision * i2)) | (i << (this.precision * 0))];
                    j += (i << (8 - this.precision)) * i6;
                    j2 += i6 * (i3 << (8 - this.precision));
                    j3 += i6 * (i4 << (8 - this.precision));
                    i4++;
                    i2 = 1;
                }
                i3++;
                i2 = 1;
            }
        }
        this.rgb = (int) ((((j3 / this.total) & 255) << 0) | (((j / this.total) & 255) << 16) | (((j2 / this.total) & 255) << 8));
    }

    public final int getIndex() {
        return this.index;
    }

    public final void setIndex(int i) {
        this.index = i;
    }

    public static class RgbComparator implements Comparator<ColorSpaceSubset>, Serializable {
        private static final long serialVersionUID = 509214838111679029L;

        @Override // java.util.Comparator
        public int compare(ColorSpaceSubset colorSpaceSubset, ColorSpaceSubset colorSpaceSubset2) {
            return colorSpaceSubset.rgb - colorSpaceSubset2.rgb;
        }
    }
}
