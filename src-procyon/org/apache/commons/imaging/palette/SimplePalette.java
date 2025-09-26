// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

public class SimplePalette implements Palette
{
    private final int[] palette;
    
    public SimplePalette(final int[] palette) {
        this.palette = palette;
    }
    
    @Override
    public int getEntry(final int n) {
        return this.palette[n];
    }
    
    @Override
    public int getPaletteIndex(final int n) {
        for (int i = 0; i < this.palette.length; ++i) {
            if (this.palette[i] == n) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int length() {
        return this.palette.length;
    }
}
