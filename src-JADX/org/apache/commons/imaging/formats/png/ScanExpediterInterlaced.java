package org.apache.commons.imaging.formats.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPlte;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ScanExpediterInterlaced extends ScanExpediter {
    private static final int[] STARTING_ROW = {0, 0, 4, 0, 2, 0, 1};
    private static final int[] STARTING_COL = {0, 4, 0, 2, 0, 1, 0};
    private static final int[] ROW_INCREMENT = {8, 8, 8, 4, 4, 2, 2};
    private static final int[] COL_INCREMENT = {8, 8, 4, 4, 2, 2, 1};

    public ScanExpediterInterlaced(int i, int i2, InputStream inputStream, BufferedImage bufferedImage, PngColorType pngColorType, int i3, int i4, PngChunkPlte pngChunkPlte, GammaCorrection gammaCorrection, TransparencyFilter transparencyFilter) {
        super(i, i2, inputStream, bufferedImage, pngColorType, i3, i4, pngChunkPlte, gammaCorrection, transparencyFilter);
    }

    private void visit(int i, int i2, BufferedImage bufferedImage, BitParser bitParser, int i3) throws IOException, ImageReadException {
        bufferedImage.setRGB(i, i2, getRGB(bitParser, i3));
    }

    @Override // org.apache.commons.imaging.formats.png.ScanExpediter
    public void drive() throws IOException, ImageReadException {
        for (int i = 1; i <= 7; i++) {
            byte[] nextScanline = null;
            int i2 = i - 1;
            for (int i3 = STARTING_ROW[i2]; i3 < this.height; i3 += ROW_INCREMENT[i2]) {
                int i4 = STARTING_COL[i2];
                if (i4 < this.width) {
                    nextScanline = getNextScanline(this.is, getBitsToBytesRoundingUp(this.bitsPerPixel * ((((this.width - STARTING_COL[i2]) - 1) / COL_INCREMENT[i2]) + 1)), nextScanline, this.bytesPerPixel);
                    BitParser bitParser = new BitParser(nextScanline, this.bitsPerPixel, this.bitDepth);
                    int i5 = i4;
                    int i6 = 0;
                    while (i5 < this.width) {
                        visit(i5, i3, this.bi, bitParser, i6);
                        i5 += COL_INCREMENT[i2];
                        i6++;
                    }
                }
            }
        }
    }
}
