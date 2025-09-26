// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

abstract class PixelParser
{
    final BmpHeaderInfo bhi;
    final byte[] colorTable;
    final byte[] imageData;
    final InputStream is;
    
    public PixelParser(final BmpHeaderInfo bhi, final byte[] colorTable, final byte[] array) {
        this.bhi = bhi;
        this.colorTable = colorTable;
        this.imageData = array;
        this.is = new ByteArrayInputStream(array);
    }
    
    int getColorTableRGB(int n) {
        n *= 4;
        return (this.colorTable[n + 2] & 0xFF) << 16 | 0xFF000000 | (this.colorTable[n + 1] & 0xFF) << 8 | (this.colorTable[n + 0] & 0xFF) << 0;
    }
    
    public abstract void processImage(final ImageBuilder p0) throws ImageReadException, IOException;
}
