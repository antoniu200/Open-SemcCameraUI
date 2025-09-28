package org.apache.commons.imaging.color;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ColorCieLch {
    public final double C;
    public final double H;
    public final double L;
    public static final ColorCieLch BLACK = new ColorCieLch(0.0d, 0.0d, 0.0d);
    public static final ColorCieLch WHITE = new ColorCieLch(100.0d, 0.0d, 297.0d);
    public static final ColorCieLch RED = new ColorCieLch(53.0d, 80.0d, 67.0d);
    public static final ColorCieLch GREEN = new ColorCieLch(88.0d, -86.0d, 83.0d);
    public static final ColorCieLch BLUE = new ColorCieLch(32.0d, 79.0d, -108.0d);

    public ColorCieLch(double d, double d2, double d3) {
        this.L = d;
        this.C = d2;
        this.H = d3;
    }

    public String toString() {
        return "{L: " + this.L + ", C: " + this.C + ", H: " + this.H + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ColorCieLch colorCieLch = (ColorCieLch) obj;
        return Double.compare(colorCieLch.C, this.C) == 0 && Double.compare(colorCieLch.H, this.H) == 0 && Double.compare(colorCieLch.L, this.L) == 0;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.L);
        long jDoubleToLongBits2 = Double.doubleToLongBits(this.C);
        int i = (((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32))) * 31) + ((int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32)));
        long jDoubleToLongBits3 = Double.doubleToLongBits(this.H);
        return (31 * i) + ((int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32)));
    }
}
