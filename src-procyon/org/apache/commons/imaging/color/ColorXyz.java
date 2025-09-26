// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorXyz
{
    public static final ColorXyz BLACK;
    public static final ColorXyz BLUE;
    public static final ColorXyz GREEN;
    public static final ColorXyz RED;
    public static final ColorXyz WHITE;
    public final double X;
    public final double Y;
    public final double Z;
    
    static {
        BLACK = new ColorXyz(0.0, 0.0, 0.0);
        WHITE = new ColorXyz(95.05, 100.0, 108.9);
        RED = new ColorXyz(41.24, 21.26, 1.93);
        GREEN = new ColorXyz(35.76, 71.52, 11.92);
        BLUE = new ColorXyz(18.05, 7.22, 95.05);
    }
    
    public ColorXyz(final double x, final double y, final double z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorXyz colorXyz = (ColorXyz)o;
            return Double.compare(colorXyz.X, this.X) == 0 && Double.compare(colorXyz.Y, this.Y) == 0 && Double.compare(colorXyz.Z, this.Z) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.X);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.Y);
        final int n2 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.Z);
        return 31 * (n * 31 + n2) + (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{X: ");
        sb.append(this.X);
        sb.append(", Y: ");
        sb.append(this.Y);
        sb.append(", Z: ");
        sb.append(this.Z);
        sb.append("}");
        return sb.toString();
    }
}
