// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorCieLch
{
    public static final ColorCieLch BLACK;
    public static final ColorCieLch BLUE;
    public static final ColorCieLch GREEN;
    public static final ColorCieLch RED;
    public static final ColorCieLch WHITE;
    public final double C;
    public final double H;
    public final double L;
    
    static {
        BLACK = new ColorCieLch(0.0, 0.0, 0.0);
        WHITE = new ColorCieLch(100.0, 0.0, 297.0);
        RED = new ColorCieLch(53.0, 80.0, 67.0);
        GREEN = new ColorCieLch(88.0, -86.0, 83.0);
        BLUE = new ColorCieLch(32.0, 79.0, -108.0);
    }
    
    public ColorCieLch(final double l, final double c, final double h) {
        this.L = l;
        this.C = c;
        this.H = h;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorCieLch colorCieLch = (ColorCieLch)o;
            return Double.compare(colorCieLch.C, this.C) == 0 && Double.compare(colorCieLch.H, this.H) == 0 && Double.compare(colorCieLch.L, this.L) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.L);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.C);
        final int n2 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.H);
        return 31 * (n * 31 + n2) + (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{L: ");
        sb.append(this.L);
        sb.append(", C: ");
        sb.append(this.C);
        sb.append(", H: ");
        sb.append(this.H);
        sb.append("}");
        return sb.toString();
    }
}
