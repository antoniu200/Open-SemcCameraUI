// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

class ImageContents
{
    final BmpHeaderInfo bhi;
    final byte[] colorTable;
    final byte[] imageData;
    final PixelParser pixelParser;
    
    public ImageContents(final BmpHeaderInfo bhi, final byte[] colorTable, final byte[] imageData, final PixelParser pixelParser) {
        this.bhi = bhi;
        this.colorTable = colorTable;
        this.imageData = imageData;
        this.pixelParser = pixelParser;
    }
}
