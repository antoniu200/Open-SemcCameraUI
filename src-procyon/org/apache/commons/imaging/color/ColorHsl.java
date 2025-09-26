// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorHsl
{
    public static final ColorHsl BLACK;
    public static final ColorHsl BLUE;
    public static final ColorHsl GREEN;
    public static final ColorHsl RED;
    public static final ColorHsl WHITE;
    public final double H;
    public final double L;
    public final double S;
    
    static {
        BLACK = new ColorHsl(0.0, 0.0, 0.0);
        WHITE = new ColorHsl(0.0, 0.0, 100.0);
        RED = new ColorHsl(0.0, 100.0, 100.0);
        GREEN = new ColorHsl(120.0, 100.0, 100.0);
        BLUE = new ColorHsl(240.0, 100.0, 100.0);
    }
    
    public ColorHsl(final double h, final double s, final double l) {
        this.H = h;
        this.S = s;
        this.L = l;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorHsl colorHsl = (ColorHsl)o;
            return Double.compare(colorHsl.H, this.H) == 0 && Double.compare(colorHsl.L, this.L) == 0 && Double.compare(colorHsl.S, this.S) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.H);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.S);
        final int n2 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.L);
        return 31 * (n * 31 + n2) + (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{H: ");
        sb.append(this.H);
        sb.append(", S: ");
        sb.append(this.S);
        sb.append(", L: ");
        sb.append(this.L);
        sb.append("}");
        return sb.toString();
    }
}
