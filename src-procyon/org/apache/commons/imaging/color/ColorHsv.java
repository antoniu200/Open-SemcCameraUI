// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorHsv
{
    public static final ColorHsv BLACK;
    public static final ColorHsv BLUE;
    public static final ColorHsv GREEN;
    public static final ColorHsv RED;
    public static final ColorHsv WHITE;
    public final double H;
    public final double S;
    public final double V;
    
    static {
        BLACK = new ColorHsv(0.0, 0.0, 0.0);
        WHITE = new ColorHsv(0.0, 0.0, 100.0);
        RED = new ColorHsv(0.0, 100.0, 100.0);
        GREEN = new ColorHsv(120.0, 100.0, 100.0);
        BLUE = new ColorHsv(240.0, 100.0, 100.0);
    }
    
    public ColorHsv(final double h, final double s, final double v) {
        this.H = h;
        this.S = s;
        this.V = v;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorHsv colorHsv = (ColorHsv)o;
            return Double.compare(colorHsv.H, this.H) == 0 && Double.compare(colorHsv.S, this.S) == 0 && Double.compare(colorHsv.V, this.V) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.H);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.S);
        final int n2 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.V);
        return 31 * (n * 31 + n2) + (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{H: ");
        sb.append(this.H);
        sb.append(", S: ");
        sb.append(this.S);
        sb.append(", V: ");
        sb.append(this.V);
        sb.append("}");
        return sb.toString();
    }
}
