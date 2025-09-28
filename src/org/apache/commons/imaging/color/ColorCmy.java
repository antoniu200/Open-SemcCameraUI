package org.apache.commons.imaging.color;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ColorCmy {
    public final double C;
    public final double M;
    public final double Y;
    public static final ColorCmy CYAN = new ColorCmy(100.0d, 0.0d, 0.0d);
    public static final ColorCmy MAGENTA = new ColorCmy(0.0d, 100.0d, 0.0d);
    public static final ColorCmy YELLOW = new ColorCmy(0.0d, 0.0d, 100.0d);
    public static final ColorCmy BLACK = new ColorCmy(100.0d, 100.0d, 100.0d);
    public static final ColorCmy WHITE = new ColorCmy(0.0d, 0.0d, 0.0d);
    public static final ColorCmy RED = new ColorCmy(0.0d, 100.0d, 100.0d);
    public static final ColorCmy GREEN = new ColorCmy(100.0d, 0.0d, 100.0d);
    public static final ColorCmy BLUE = new ColorCmy(100.0d, 100.0d, 0.0d);

    public ColorCmy(double d, double d2, double d3) {
        this.C = d;
        this.M = d2;
        this.Y = d3;
    }

    public String toString() {
        return "{C: " + this.C + ", M: " + this.M + ", Y: " + this.Y + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ColorCmy colorCmy = (ColorCmy) obj;
        return Double.compare(colorCmy.C, this.C) == 0 && Double.compare(colorCmy.M, this.M) == 0 && Double.compare(colorCmy.Y, this.Y) == 0;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.C);
        long jDoubleToLongBits2 = Double.doubleToLongBits(this.M);
        int i = (((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32))) * 31) + ((int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32)));
        long jDoubleToLongBits3 = Double.doubleToLongBits(this.Y);
        return (31 * i) + ((int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32)));
    }
}
