// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.photometricinterpreters;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.color.ColorConversions;
import org.apache.commons.imaging.common.ImageBuilder;

public class PhotometricInterpreterCmyk extends PhotometricInterpreter
{
    public PhotometricInterpreterCmyk(final int n, final int[] array, final int n2, final int n3, final int n4) {
        super(n, array, n2, n3, n4);
    }
    
    @Override
    public void interpretPixel(final ImageBuilder imageBuilder, final int[] array, final int n, final int n2) throws ImageReadException, IOException {
        imageBuilder.setRGB(n, n2, ColorConversions.convertCMYKtoRGB(array[0], array[1], array[2], array[3]));
    }
}
