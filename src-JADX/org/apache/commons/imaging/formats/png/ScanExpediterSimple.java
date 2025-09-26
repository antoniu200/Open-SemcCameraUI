package org.apache.commons.imaging.formats.png;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPlte;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ScanExpediterSimple extends ScanExpediter {
    public ScanExpediterSimple(int i, int i2, InputStream inputStream, BufferedImage bufferedImage, PngColorType pngColorType, int i3, int i4, PngChunkPlte pngChunkPlte, GammaCorrection gammaCorrection, TransparencyFilter transparencyFilter) {
        super(i, i2, inputStream, bufferedImage, pngColorType, i3, i4, pngChunkPlte, gammaCorrection, transparencyFilter);
    }

    @Override // org.apache.commons.imaging.formats.png.ScanExpediter
    public void drive() throws IOException, ImageReadException {
        int bitsToBytesRoundingUp = getBitsToBytesRoundingUp(this.bitsPerPixel * this.width);
        byte[] nextScanline = null;
        for (int i = 0; i < this.height; i++) {
            nextScanline = getNextScanline(this.is, bitsToBytesRoundingUp, nextScanline, this.bytesPerPixel);
            BitParser bitParser = new BitParser(nextScanline, this.bitsPerPixel, this.bitDepth);
            for (int i2 = 0; i2 < this.width; i2++) {
                this.bi.setRGB(i2, i, getRGB(bitParser, i2));
            }
        }
    }
}
