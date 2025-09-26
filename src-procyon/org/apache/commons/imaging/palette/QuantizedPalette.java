// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;
import java.util.List;

public class QuantizedPalette implements Palette
{
    private final int precision;
    private final ColorSpaceSubset[] straight;
    private final List<ColorSpaceSubset> subsets;
    
    public QuantizedPalette(final List<ColorSpaceSubset> subsets, final int precision) {
        this.subsets = subsets;
        this.precision = precision;
        this.straight = new ColorSpaceSubset[1 << precision * 3];
        for (int i = 0; i < subsets.size(); ++i) {
            final ColorSpaceSubset colorSpaceSubset = subsets.get(i);
            colorSpaceSubset.setIndex(i);
            for (int j = colorSpaceSubset.mins[0]; j <= colorSpaceSubset.maxs[0]; ++j) {
                for (int k = colorSpaceSubset.mins[1]; k <= colorSpaceSubset.maxs[1]; ++k) {
                    for (int l = colorSpaceSubset.mins[2]; l <= colorSpaceSubset.maxs[2]; ++l) {
                        this.straight[j << precision * 2 | k << precision * 1 | l << precision * 0] = colorSpaceSubset;
                    }
                }
            }
        }
    }
    
    @Override
    public int getEntry(final int n) {
        return this.subsets.get(n).rgb;
    }
    
    @Override
    public int getPaletteIndex(final int n) throws ImageWriteException {
        final int n2 = (1 << this.precision) - 1;
        return this.straight[(n >> 8 - this.precision & n2) | ((n2 << (this.precision << 1) & n >> 24 - 3 * this.precision) | (n >> 16 - 2 * this.precision & n2 << this.precision))].getIndex();
    }
    
    @Override
    public int length() {
        return this.subsets.size();
    }
}
