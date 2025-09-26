// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorCieLuv
{
    public static final ColorCieLuv BLACK;
    public static final ColorCieLuv BLUE;
    public static final ColorCieLuv GREEN;
    public static final ColorCieLuv RED;
    public static final ColorCieLuv WHITE;
    public final double L;
    public final double u;
    public final double v;
    
    static {
        BLACK = new ColorCieLuv(0.0, 0.0, 0.0);
        WHITE = new ColorCieLuv(100.0, 0.0, -0.017);
        RED = new ColorCieLuv(53.233, 175.053, 37.751);
        GREEN = new ColorCieLuv(87.737, -83.08, 107.401);
        BLUE = new ColorCieLuv(32.303, -9.4, -130.358);
    }
    
    public ColorCieLuv(final double l, final double u, final double v) {
        this.L = l;
        this.u = u;
        this.v = v;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorCieLuv colorCieLuv = (ColorCieLuv)o;
            return Double.compare(colorCieLuv.L, this.L) == 0 && Double.compare(colorCieLuv.u, this.u) == 0 && Double.compare(colorCieLuv.v, this.v) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.L);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.u);
        final int n2 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.v);
        return 31 * (n * 31 + n2) + (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{L: ");
        sb.append(this.L);
        sb.append(", u: ");
        sb.append(this.u);
        sb.append(", v: ");
        sb.append(this.v);
        sb.append("}");
        return sb.toString();
    }
}
