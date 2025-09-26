// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.formats.psd.ImageContents;

public class DataParserGrayscale extends DataParser
{
    @Override
    public int getBasicChannelsCount() {
        return 1;
    }
    
    @Override
    protected int getRGB(final int[][][] array, int n, final int n2, final ImageContents imageContents) {
        n = (array[0][n2][n] & 0xFF & 0xFF);
        return n << 0 | (n << 16 | 0xFF000000 | n << 8);
    }
}
