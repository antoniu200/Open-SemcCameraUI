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

class ScanExpediterInterlaced extends ScanExpediter
{
    private static final int[] COL_INCREMENT;
    private static final int[] ROW_INCREMENT;
    private static final int[] STARTING_COL;
    private static final int[] STARTING_ROW;
    
    static {
        STARTING_ROW = new int[] { 0, 0, 4, 0, 2, 0, 1 };
        STARTING_COL = new int[] { 0, 4, 0, 2, 0, 1, 0 };
        ROW_INCREMENT = new int[] { 8, 8, 8, 4, 4, 2, 2 };
        COL_INCREMENT = new int[] { 8, 8, 4, 4, 2, 2, 1 };
    }
    
    public ScanExpediterInterlaced(final int n, final int n2, final InputStream inputStream, final BufferedImage bufferedImage, final PngColorType pngColorType, final int n3, final int n4, final PngChunkPlte pngChunkPlte, final GammaCorrection gammaCorrection, final TransparencyFilter transparencyFilter) {
        super(n, n2, inputStream, bufferedImage, pngColorType, n3, n4, pngChunkPlte, gammaCorrection, transparencyFilter);
    }
    
    private void visit(final int x, final int y, final BufferedImage bufferedImage, final BitParser bitParser, final int n) throws ImageReadException, IOException {
        bufferedImage.setRGB(x, y, this.getRGB(bitParser, n));
    }
    
    @Override
    public void drive() throws ImageReadException, IOException {
        for (int i = 1; i <= 7; ++i) {
            byte[] array = null;
            final int[] starting_ROW = ScanExpediterInterlaced.STARTING_ROW;
            byte[] array2;
            for (int n = i - 1, j = starting_ROW[n]; j < this.height; j += ScanExpediterInterlaced.ROW_INCREMENT[n], array = array2) {
                int n2 = ScanExpediterInterlaced.STARTING_COL[n];
                array2 = array;
                if (n2 < this.width) {
                    final byte[] nextScanline = this.getNextScanline(this.is, this.getBitsToBytesRoundingUp(this.bitsPerPixel * ((this.width - ScanExpediterInterlaced.STARTING_COL[n] - 1) / ScanExpediterInterlaced.COL_INCREMENT[n] + 1)), array, this.bytesPerPixel);
                    final BitParser bitParser = new BitParser(nextScanline, this.bitsPerPixel, this.bitDepth);
                    int n3 = 0;
                    while (true) {
                        array2 = nextScanline;
                        if (n2 >= this.width) {
                            break;
                        }
                        this.visit(n2, j, this.bi, bitParser, n3);
                        n2 += ScanExpediterInterlaced.COL_INCREMENT[n];
                        ++n3;
                    }
                }
            }
        }
    }
}
