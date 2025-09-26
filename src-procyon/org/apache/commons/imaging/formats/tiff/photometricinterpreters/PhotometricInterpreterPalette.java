// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

public class PhotometricInterpreterPalette extends PhotometricInterpreter
{
    private final int[] indexColorMap;
    
    public PhotometricInterpreterPalette(int i, final int[] array, int n, int n2, int n3, final int[] array2) {
        super(i, array, n, n2, n3);
        i = 0;
        n = 1 << this.getBitsPerSample(0);
        this.indexColorMap = new int[n];
        while (i < n) {
            final int n4 = array2[i];
            n3 = array2[i + n];
            n2 = array2[2 * n + i];
            this.indexColorMap[i] = ((n4 >> 8 & 0xFF) << 16 | 0xFF000000 | (n3 >> 8 & 0xFF) << 8 | (n2 >> 8 & 0xFF));
            ++i;
        }
    }
    
    @Override
    public void interpretPixel(final ImageBuilder imageBuilder, final int[] array, final int n, final int n2) throws ImageReadException, IOException {
        imageBuilder.setRGB(n, n2, this.indexColorMap[array[0]]);
    }
}
