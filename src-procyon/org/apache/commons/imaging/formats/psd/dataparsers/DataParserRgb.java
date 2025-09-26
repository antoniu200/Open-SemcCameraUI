// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.formats.psd.ImageContents;

public class DataParserRgb extends DataParser
{
    @Override
    public int getBasicChannelsCount() {
        return 3;
    }
    
    @Override
    protected int getRGB(final int[][][] array, final int n, final int n2, final ImageContents imageContents) {
        return (array[2][n2][n] & 0xFF & 0xFF) << 0 | ((0xFF & (array[0][n2][n] & 0xFF)) << 16 | 0xFF000000 | (0xFF & (array[1][n2][n] & 0xFF)) << 8);
    }
}
