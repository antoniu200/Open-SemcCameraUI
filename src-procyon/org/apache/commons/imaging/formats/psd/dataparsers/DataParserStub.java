// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.formats.psd.ImageContents;

public class DataParserStub extends DataParser
{
    @Override
    public int getBasicChannelsCount() {
        return 1;
    }
    
    @Override
    protected int getRGB(final int[][][] array, final int n, final int n2, final ImageContents imageContents) {
        return 0;
    }
}
