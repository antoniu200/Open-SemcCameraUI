// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.formats.psd.PsdHeaderInfo;
import java.awt.image.DataBuffer;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.formats.psd.ImageContents;

public abstract class DataParser
{
    public abstract int getBasicChannelsCount();
    
    protected abstract int getRGB(final int[][][] p0, final int p1, final int p2, final ImageContents p3);
    
    public final void parseData(final int[][][] array, final BufferedImage bufferedImage, final ImageContents imageContents) {
        final DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        final PsdHeaderInfo header = imageContents.header;
        final int columns = header.columns;
        for (int rows = header.rows, i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                dataBuffer.setElem(i * columns + j, this.getRGB(array, j, i, imageContents));
            }
        }
    }
}
