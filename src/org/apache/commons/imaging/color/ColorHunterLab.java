package org.apache.commons.imaging.color;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class ColorHunterLab {
    public final double L;
    public final double a;
    public final double b;
    public static final ColorHunterLab BLACK = new ColorHunterLab(0.0d, 0.0d, 0.0d);
    public static final ColorHunterLab WHITE = new ColorHunterLab(100.0d, -5.336d, 5.433d);
    public static final ColorHunterLab RED = new ColorHunterLab(46.109d, 78.962d, 29.794d);
    public static final ColorHunterLab GREEN = new ColorHunterLab(84.569d, -72.518d, 50.842d);
    public static final ColorHunterLab BLUE = new ColorHunterLab(26.87d, 72.885d, -190.923d);

    public ColorHunterLab(double d, double d2, double d3) {
        this.L = d;
        this.a = d2;
        this.b = d3;
    }

    public String toString() {
        return "{L: " + this.L + ", a: " + this.a + ", b: " + this.b + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ColorHunterLab colorHunterLab = (ColorHunterLab) obj;
        return Double.compare(colorHunterLab.L, this.L) == 0 && Double.compare(colorHunterLab.a, this.a) == 0 && Double.compare(colorHunterLab.b, this.b) == 0;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.L);
        long jDoubleToLongBits2 = Double.doubleToLongBits(this.a);
        int i = (((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32))) * 31) + ((int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32)));
        long jDoubleToLongBits3 = Double.doubleToLongBits(this.b);
        return (31 * i) + ((int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32)));
    }
}
