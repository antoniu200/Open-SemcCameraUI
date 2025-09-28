package org.apache.commons.imaging.color;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ColorHsl {
    public final double H;
    public final double L;
    public final double S;
    public static final ColorHsl BLACK = new ColorHsl(0.0d, 0.0d, 0.0d);
    public static final ColorHsl WHITE = new ColorHsl(0.0d, 0.0d, 100.0d);
    public static final ColorHsl RED = new ColorHsl(0.0d, 100.0d, 100.0d);
    public static final ColorHsl GREEN = new ColorHsl(120.0d, 100.0d, 100.0d);
    public static final ColorHsl BLUE = new ColorHsl(240.0d, 100.0d, 100.0d);

    public ColorHsl(double d, double d2, double d3) {
        this.H = d;
        this.S = d2;
        this.L = d3;
    }

    public String toString() {
        return "{H: " + this.H + ", S: " + this.S + ", L: " + this.L + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ColorHsl colorHsl = (ColorHsl) obj;
        return Double.compare(colorHsl.H, this.H) == 0 && Double.compare(colorHsl.L, this.L) == 0 && Double.compare(colorHsl.S, this.S) == 0;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.H);
        long jDoubleToLongBits2 = Double.doubleToLongBits(this.S);
        int i = (((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32))) * 31) + ((int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32)));
        long jDoubleToLongBits3 = Double.doubleToLongBits(this.L);
        return (31 * i) + ((int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32)));
    }
}
