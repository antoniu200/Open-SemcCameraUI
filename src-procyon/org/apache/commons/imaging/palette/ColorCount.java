// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

class ColorCount
{
    public final int alpha;
    public final int argb;
    public final int blue;
    public int count;
    public final int green;
    public final int red;
    
    public ColorCount(final int argb) {
        this.argb = argb;
        this.alpha = (argb >> 24 & 0xFF);
        this.red = (argb >> 16 & 0xFF);
        this.green = (argb >> 8 & 0xFF);
        this.blue = (argb >> 0 & 0xFF);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ColorCount;
        boolean b2 = false;
        if (b) {
            if (((ColorCount)o).argb == this.argb) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.argb;
    }
}
