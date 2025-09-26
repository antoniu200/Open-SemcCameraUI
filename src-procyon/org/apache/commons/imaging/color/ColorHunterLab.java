// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorHunterLab
{
    public static final ColorHunterLab BLACK;
    public static final ColorHunterLab BLUE;
    public static final ColorHunterLab GREEN;
    public static final ColorHunterLab RED;
    public static final ColorHunterLab WHITE;
    public final double L;
    public final double a;
    public final double b;
    
    static {
        BLACK = new ColorHunterLab(0.0, 0.0, 0.0);
        WHITE = new ColorHunterLab(100.0, -5.336, 5.433);
        RED = new ColorHunterLab(46.109, 78.962, 29.794);
        GREEN = new ColorHunterLab(84.569, -72.518, 50.842);
        BLUE = new ColorHunterLab(26.87, 72.885, -190.923);
    }
    
    public ColorHunterLab(final double l, final double a, final double b) {
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
            final ColorHunterLab colorHunterLab = (ColorHunterLab)o;
            return Double.compare(colorHunterLab.L, this.L) == 0 && Double.compare(colorHunterLab.a, this.a) == 0 && Double.compare(colorHunterLab.b, this.b) == 0;
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
