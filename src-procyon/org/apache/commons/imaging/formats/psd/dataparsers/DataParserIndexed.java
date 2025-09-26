// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.formats.psd.ImageContents;

public class DataParserIndexed extends DataParser
{
    private final int[] colorTable;
    
    public DataParserIndexed(final byte[] array) {
        this.colorTable = new int[256];
        for (int i = 0; i < 256; ++i) {
            this.colorTable[i] = ((array[0 + i] & 0xFF & 0xFF) << 16 | 0xFF000000 | (array[256 + i] & 0xFF & 0xFF) << 8 | (0xFF & (array[512 + i] & 0xFF)) << 0);
        }
    }
    
    @Override
    public int getBasicChannelsCount() {
        return 1;
    }
    
    @Override
    protected int getRGB(final int[][][] array, int n, final int n2, final ImageContents imageContents) {
        n = array[0][n2][n];
        return this.colorTable[n & 0xFF];
    }
}
