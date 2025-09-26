// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.color.ColorConversions;
import org.apache.commons.imaging.formats.psd.ImageContents;

public class DataParserLab extends DataParser
{
    @Override
    public int getBasicChannelsCount() {
        return 3;
    }
    
    @Override
    protected int getRGB(final int[][][] array, final int n, final int n2, final ImageContents imageContents) {
        return ColorConversions.convertCIELabtoARGBTest(array[0][n2][n] & 0xFF, (array[1][n2][n] & 0xFF) - 128, (array[2][n2][n] & 0xFF) - 128);
    }
}
