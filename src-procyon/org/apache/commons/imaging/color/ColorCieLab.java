// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorCieLab
{
    public static final ColorCieLab BLACK;
    public static final ColorCieLab BLUE;
    public static final ColorCieLab GREEN;
    public static final ColorCieLab RED;
    public static final ColorCieLab WHITE;
    public final double L;
    public final double a;
    public final double b;
    
    static {
        BLACK = new ColorCieLab(0.0, 0.0, 0.0);
        WHITE = new ColorCieLab(100.0, 0.0, 0.0);
        RED = new ColorCieLab(53.0, 80.0, 67.0);
        GREEN = new ColorCieLab(88.0, -86.0, 83.0);
        BLUE = new ColorCieLab(32.0, 79.0, -108.0);
    }
    
    public ColorCieLab(final double l, final double a, final double b) {
        this.L = l;
        this.a = a;
        this.b = b;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final ColorCieLab colorCieLab = (ColorCieLab)o;
            return Double.compare(colorCieLab.L, this.L) == 0 && Double.compare(colorCieLab.a, this.a) == 0 && Double.compare(colorCieLab.b, this.b) == 0;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.L);
        final int n = (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.a);
        final int n2 = (int)(doubleToLongBits2 ^ doubleToLongBits2 >>> 32);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.b);
        return 31 * (n * 31 + n2) + (int)(doubleToLongBits3 ^ doubleToLongBits3 >>> 32);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{L: ");
        sb.append(this.L);
        sb.append(", a: ");
        sb.append(this.a);
        sb.append(", b: ");
        sb.append(this.b);
        sb.append("}");
        return sb.toString();
    }
}
