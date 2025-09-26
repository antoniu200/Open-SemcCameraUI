// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilter;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPlte;
import java.awt.image.BufferedImage;
import java.io.InputStream;

class ScanExpediterSimple extends ScanExpediter
{
    public ScanExpediterSimple(final int n, final int n2, final InputStream inputStream, final BufferedImage bufferedImage, final PngColorType pngColorType, final int n3, final int n4, final PngChunkPlte pngChunkPlte, final GammaCorrection gammaCorrection, final TransparencyFilter transparencyFilter) {
        super(n, n2, inputStream, bufferedImage, pngColorType, n3, n4, pngChunkPlte, gammaCorrection, transparencyFilter);
    }
    
    @Override
    public void drive() throws ImageReadException, IOException {
        final int bitsToBytesRoundingUp = this.getBitsToBytesRoundingUp(this.bitsPerPixel * this.width);
        byte[] nextScanline = null;
        for (int i = 0; i < this.height; ++i) {
            nextScanline = this.getNextScanline(this.is, bitsToBytesRoundingUp, nextScanline, this.bytesPerPixel);
            final BitParser bitParser = new BitParser(nextScanline, this.bitsPerPixel, this.bitDepth);
            for (int j = 0; j < this.width; ++j) {
                this.bi.setRGB(j, i, this.getRGB(bitParser, j));
            }
        }
    }
}
