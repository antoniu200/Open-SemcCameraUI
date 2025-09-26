package org.apache.commons.imaging.color;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ColorXyz {
    public final double X;
    public final double Y;
    public final double Z;
    public static final ColorXyz BLACK = new ColorXyz(0.0d, 0.0d, 0.0d);
    public static final ColorXyz WHITE = new ColorXyz(95.05d, 100.0d, 108.9d);
    public static final ColorXyz RED = new ColorXyz(41.24d, 21.26d, 1.93d);
    public static final ColorXyz GREEN = new ColorXyz(35.76d, 71.52d, 11.92d);
    public static final ColorXyz BLUE = new ColorXyz(18.05d, 7.22d, 95.05d);

    public ColorXyz(double d, double d2, double d3) {
        this.X = d;
        this.Y = d2;
        this.Z = d3;
    }

    public String toString() {
        return "{X: " + this.X + ", Y: " + this.Y + ", Z: " + this.Z + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ColorXyz colorXyz = (ColorXyz) obj;
        return Double.compare(colorXyz.X, this.X) == 0 && Double.compare(colorXyz.Y, this.Y) == 0 && Double.compare(colorXyz.Z, this.Z) == 0;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.X);
        long jDoubleToLongBits2 = Double.doubleToLongBits(this.Y);
        int i = (((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32))) * 31) + ((int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32)));
        long jDoubleToLongBits3 = Double.doubleToLongBits(this.Z);
        return (31 * i) + ((int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32)));
    }
}
