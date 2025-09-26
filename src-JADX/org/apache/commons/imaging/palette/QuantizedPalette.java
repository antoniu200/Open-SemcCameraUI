package org.apache.commons.imaging.palette;

import java.util.List;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class QuantizedPalette implements Palette {
    private final int precision;
    private final ColorSpaceSubset[] straight;
    private final List<ColorSpaceSubset> subsets;

    public QuantizedPalette(List<ColorSpaceSubset> list, int i) {
        this.subsets = list;
        this.precision = i;
        this.straight = new ColorSpaceSubset[1 << (i * 3)];
        for (int i2 = 0; i2 < list.size(); i2++) {
            ColorSpaceSubset colorSpaceSubset = list.get(i2);
            colorSpaceSubset.setIndex(i2);
            for (int i3 = colorSpaceSubset.mins[0]; i3 <= colorSpaceSubset.maxs[0]; i3++) {
                for (int i4 = colorSpaceSubset.mins[1]; i4 <= colorSpaceSubset.maxs[1]; i4++) {
                    for (int i5 = colorSpaceSubset.mins[2]; i5 <= colorSpaceSubset.maxs[2]; i5++) {
                        this.straight[(i3 << (i * 2)) | (i4 << (i * 1)) | (i5 << (i * 0))] = colorSpaceSubset;
                    }
                }
            }
        }
    }

    @Override // org.apache.commons.imaging.palette.Palette
    public int getPaletteIndex(int i) throws ImageWriteException {
        int i2 = (1 << this.precision) - 1;
        return this.straight[((i >> (8 - this.precision)) & i2) | ((i2 << (this.precision << 1)) & (i >> (24 - (3 * this.precision)))) | ((i >> (16 - (2 * this.precision))) & (i2 << this.precision))].getIndex();
    }

    @Override // org.apache.commons.imaging.palette.Palette
    public int getEntry(int i) {
        return this.subsets.get(i).rgb;
    }

    @Override // org.apache.commons.imaging.palette.Palette
    public int length() {
        return this.subsets.size();
    }
}
