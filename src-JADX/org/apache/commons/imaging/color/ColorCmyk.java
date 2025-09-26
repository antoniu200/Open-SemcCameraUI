package org.apache.commons.imaging.color;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ColorCmyk {
    public final double C;
    public final double K;
    public final double M;
    public final double Y;
    public static final ColorCmyk CYAN = new ColorCmyk(100.0d, 0.0d, 0.0d, 0.0d);
    public static final ColorCmyk MAGENTA = new ColorCmyk(0.0d, 100.0d, 0.0d, 0.0d);
    public static final ColorCmyk YELLOW = new ColorCmyk(0.0d, 0.0d, 100.0d, 0.0d);
    public static final ColorCmyk BLACK = new ColorCmyk(0.0d, 0.0d, 0.0d, 100.0d);
    public static final ColorCmyk WHITE = new ColorCmyk(0.0d, 0.0d, 0.0d, 0.0d);
    public static final ColorCmyk RED = new ColorCmyk(0.0d, 100.0d, 100.0d, 0.0d);
    public static final ColorCmyk GREEN = new ColorCmyk(100.0d, 0.0d, 100.0d, 0.0d);
    public static final ColorCmyk BLUE = new ColorCmyk(100.0d, 100.0d, 0.0d, 0.0d);

    public ColorCmyk(double d, double d2, double d3, double d4) {
        this.C = d;
        this.M = d2;
        this.Y = d3;
        this.K = d4;
    }

    public String toString() {
        return "{C: " + this.C + ", M: " + this.M + ", Y: " + this.Y + ", K: " + this.K + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ColorCmyk colorCmyk = (ColorCmyk) obj;
        return Double.compare(colorCmyk.C, this.C) == 0 && Double.compare(colorCmyk.K, this.K) == 0 && Double.compare(colorCmyk.M, this.M) == 0 && Double.compare(colorCmyk.Y, this.Y) == 0;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.C);
        long jDoubleToLongBits2 = Double.doubleToLongBits(this.M);
        int i = (((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32))) * 31) + ((int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32)));
        long jDoubleToLongBits3 = Double.doubleToLongBits(this.Y);
        int i2 = (i * 31) + ((int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32)));
        long jDoubleToLongBits4 = Double.doubleToLongBits(this.K);
        return (31 * i2) + ((int) (jDoubleToLongBits4 ^ (jDoubleToLongBits4 >>> 32)));
    }
}
