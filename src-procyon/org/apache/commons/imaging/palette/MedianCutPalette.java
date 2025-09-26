// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

class MedianCutPalette extends SimplePalette
{
    private final ColorGroup root;
    
    public MedianCutPalette(final ColorGroup root, final int[] array) {
        super(array);
        this.root = root;
    }
    
    @Override
    public int getPaletteIndex(final int n) {
        ColorGroup colorGroup;
        for (colorGroup = this.root; colorGroup.cut != null; colorGroup = colorGroup.cut.getColorGroup(n)) {}
        return colorGroup.paletteIndex;
    }
}
