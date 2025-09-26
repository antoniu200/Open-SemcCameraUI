package org.apache.commons.imaging.color;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ColorHsv {
    public final double H;
    public final double S;
    public final double V;
    public static final ColorHsv BLACK = new ColorHsv(0.0d, 0.0d, 0.0d);
    public static final ColorHsv WHITE = new ColorHsv(0.0d, 0.0d, 100.0d);
    public static final ColorHsv RED = new ColorHsv(0.0d, 100.0d, 100.0d);
    public static final ColorHsv GREEN = new ColorHsv(120.0d, 100.0d, 100.0d);
    public static final ColorHsv BLUE = new ColorHsv(240.0d, 100.0d, 100.0d);

    public ColorHsv(double d, double d2, double d3) {
        this.H = d;
        this.S = d2;
        this.V = d3;
    }

    public String toString() {
        return "{H: " + this.H + ", S: " + this.S + ", V: " + this.V + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ColorHsv colorHsv = (ColorHsv) obj;
        return Double.compare(colorHsv.H, this.H) == 0 && Double.compare(colorHsv.S, this.S) == 0 && Double.compare(colorHsv.V, this.V) == 0;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.H);
        long jDoubleToLongBits2 = Double.doubleToLongBits(this.S);
        int i = (((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32))) * 31) + ((int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32)));
        long jDoubleToLongBits3 = Double.doubleToLongBits(this.V);
        return (31 * i) + ((int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32)));
    }
}
