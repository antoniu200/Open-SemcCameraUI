// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

public class PhotometricInterpreterBiLevel extends PhotometricInterpreter
{
    private final boolean invert;
    
    public PhotometricInterpreterBiLevel(final int n, final int[] array, final int n2, final int n3, final int n4, final boolean invert) {
        super(n, array, n2, n3, n4);
        this.invert = invert;
    }
    
    @Override
    public void interpretPixel(final ImageBuilder imageBuilder, final int[] array, final int n, final int n2) throws ImageReadException, IOException {
        int n4;
        final int n3 = n4 = array[0];
        if (this.invert) {
            n4 = 255 - n3;
        }
        imageBuilder.setRGB(n, n2, 0xFF000000 | n4 << 16 | n4 << 8 | n4 << 0);
    }
}
