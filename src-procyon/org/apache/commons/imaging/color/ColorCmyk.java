// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorCmyk
{
    public static final ColorCmyk BLACK;
    public static final ColorCmyk BLUE;
    public static final ColorCmyk CYAN;
    public static final ColorCmyk GREEN;
    public static final ColorCmyk MAGENTA;
    public static final ColorCmyk RED;
    public static final ColorCmyk WHITE;
    public static final ColorCmyk YELLOW;
    public final double C;
    public final double K;
    public final double M;
    public final double Y;
    
    static {
        CYAN = new ColorCmyk(100.0, 0.0, 0.0, 0.0);
        MAGENTA = new ColorCmyk(0.0, 100.0, 0.0, 0.0);
        YELLOW = new ColorCmyk(0.0, 0.0, 100.0, 0.0);
        BLACK = new ColorCmyk(0.0, 0.0, 0.0, 100.0);
        WHITE = new ColorCmyk(0.0, 0.0, 0.0, 0.0);
        RED = new ColorCmyk(0.0, 100.0, 100.0, 0.0);
        GREEN = new ColorCmyk(100.0, 0.0, 100.0, 0.0);
        BLUE = new ColorCmyk(100.0, 100.0, 0.0, 0.0);
    }
    
    public ColorCmyk(final double c, final double m, final double y, final double k) {
        this.C = c;
        this.M = m;
        this.Y = y;
        this.K = k;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorCmyk colorCmyk = (ColorCmyk)o;
            return Double.compare(colorCmyk.C, this.C) == 0 && Double.compare(colorCmyk.K, this.K) == 0 && Double.compare(colorCmyk.M, this.M) == 0 && Double.compare(colorCmyk.Y, this.Y) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.C);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.M);
        final int n2 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.Y);
        final int n3 = (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
        final long doubleToLongBits4 = Double.doubleToLongBits(this.K);
        return 31 * ((n * 31 + n2) * 31 + n3) + (int)(doubleToLongBits4 ^ doubleToLongBits4 >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{C: ");
        sb.append(this.C);
        sb.append(", M: ");
        sb.append(this.M);
        sb.append(", Y: ");
        sb.append(this.Y);
        sb.append(", K: ");
        sb.append(this.K);
        sb.append("}");
        return sb.toString();
    }
}
