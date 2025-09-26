// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorCmy
{
    public static final ColorCmy BLACK;
    public static final ColorCmy BLUE;
    public static final ColorCmy CYAN;
    public static final ColorCmy GREEN;
    public static final ColorCmy MAGENTA;
    public static final ColorCmy RED;
    public static final ColorCmy WHITE;
    public static final ColorCmy YELLOW;
    public final double C;
    public final double M;
    public final double Y;
    
    static {
        CYAN = new ColorCmy(100.0, 0.0, 0.0);
        MAGENTA = new ColorCmy(0.0, 100.0, 0.0);
        YELLOW = new ColorCmy(0.0, 0.0, 100.0);
        BLACK = new ColorCmy(100.0, 100.0, 100.0);
        WHITE = new ColorCmy(0.0, 0.0, 0.0);
        RED = new ColorCmy(0.0, 100.0, 100.0);
        GREEN = new ColorCmy(100.0, 0.0, 100.0);
        BLUE = new ColorCmy(100.0, 100.0, 0.0);
    }
    
    public ColorCmy(final double c, final double m, final double y) {
        this.C = c;
        this.M = m;
        this.Y = y;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorCmy colorCmy = (ColorCmy)o;
            return Double.compare(colorCmy.C, this.C) == 0 && Double.compare(colorCmy.M, this.M) == 0 && Double.compare(colorCmy.Y, this.Y) == 0;
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
        return 31 * (n * 31 + n2) + (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
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
        sb.append("}");
        return sb.toString();
    }
}
