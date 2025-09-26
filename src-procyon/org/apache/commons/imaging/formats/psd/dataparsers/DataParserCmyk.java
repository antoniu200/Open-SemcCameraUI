// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.color.ColorConversions;
import org.apache.commons.imaging.formats.psd.ImageContents;

public class DataParserCmyk extends DataParser
{
    @Override
    public int getBasicChannelsCount() {
        return 4;
    }
    
    @Override
    protected int getRGB(final int[][][] array, final int n, final int n2, final ImageContents imageContents) {
        return ColorConversions.convertCMYKtoRGB(255 - (array[0][n2][n] & 0xFF), 255 - (array[1][n2][n] & 0xFF), 255 - (array[2][n2][n] & 0xFF), 255 - (array[3][n2][n] & 0xFF));
    }
}
