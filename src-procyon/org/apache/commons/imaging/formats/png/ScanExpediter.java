// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterNone;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterSub;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterUp;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterAverage;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterPaeth;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilter;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilter;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPlte;
import java.io.InputStream;
import java.awt.image.BufferedImage;

abstract class ScanExpediter
{
    protected final BufferedImage bi;
    protected final int bitDepth;
    protected final int bitsPerPixel;
    protected final int bytesPerPixel;
    protected final GammaCorrection gammaCorrection;
    protected final int height;
    protected final InputStream is;
    protected final PngChunkPlte pngChunkPLTE;
    protected final PngColorType pngColorType;
    protected final TransparencyFilter transparencyFilter;
    protected final int width;
    
    public ScanExpediter(final int width, final int height, final InputStream is, final BufferedImage bi, final PngColorType pngColorType, final int bitDepth, final int bitsPerPixel, final PngChunkPlte pngChunkPLTE, final GammaCorrection gammaCorrection, final TransparencyFilter transparencyFilter) {
        this.width = width;
        this.height = height;
        this.is = is;
        this.bi = bi;
        this.pngColorType = pngColorType;
        this.bitDepth = bitDepth;
        this.bytesPerPixel = this.getBitsToBytesRoundingUp(bitsPerPixel);
        this.bitsPerPixel = bitsPerPixel;
        this.pngChunkPLTE = pngChunkPLTE;
        this.gammaCorrection = gammaCorrection;
        this.transparencyFilter = transparencyFilter;
    }
    
    public abstract void drive() throws ImageReadException, IOException;
    
    protected final int getBitsToBytesRoundingUp(final int n) {
        return (n + 7) / 8;
    }
    
    protected byte[] getNextScanline(final InputStream inputStream, final int n, final byte[] array, final int n2) throws ImageReadException, IOException {
        final int read = inputStream.read();
        if (read < 0) {
            throw new ImageReadException("PNG: missing filter type");
        }
        if (read >= FilterType.values().length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PNG: unknown filterType: ");
            sb.append(read);
            throw new ImageReadException(sb.toString());
        }
        return this.unfilterScanline(FilterType.values()[read], BinaryFunctions.readBytes("scanline", inputStream, n, "PNG: missing image data"), array, n2);
    }
    
    protected final int getPixelARGB(final int n, final int n2, final int n3, final int n4) {
        return (0xFF & n4) << 0 | ((n & 0xFF) << 24 | (n2 & 0xFF) << 16 | (0xFF & n3) << 8);
    }
    
    protected final int getPixelRGB(final int n, final int n2, final int n3) {
        return this.getPixelARGB(255, n, n2, n3);
    }
    
    protected int getRGB(final BitParser bitParser, int n) throws ImageReadException, IOException {
        switch (ScanExpediter$1.$SwitchMap$org$apache$commons$imaging$formats$png$PngColorType[this.pngColorType.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("PNG: unknown color type: ");
                sb.append(this.pngColorType);
                throw new ImageReadException(sb.toString());
            }
            case 5: {
                final int sampleAsByte = bitParser.getSampleAsByte(n, 0);
                final int sampleAsByte2 = bitParser.getSampleAsByte(n, 1);
                final int sampleAsByte3 = bitParser.getSampleAsByte(n, 2);
                final int sampleAsByte4 = bitParser.getSampleAsByte(n, 3);
                int correctSample = sampleAsByte;
                int correctSample2 = sampleAsByte3;
                n = sampleAsByte2;
                if (this.gammaCorrection != null) {
                    correctSample = this.gammaCorrection.correctSample(sampleAsByte);
                    n = this.gammaCorrection.correctSample(sampleAsByte2);
                    correctSample2 = this.gammaCorrection.correctSample(sampleAsByte3);
                }
                return this.getPixelARGB(sampleAsByte4, correctSample, n, correctSample2);
            }
            case 4: {
                final int sampleAsByte5 = bitParser.getSampleAsByte(n, 0);
                final int sampleAsByte6 = bitParser.getSampleAsByte(n, 1);
                n = sampleAsByte5;
                if (this.gammaCorrection != null) {
                    n = this.gammaCorrection.correctSample(sampleAsByte5);
                }
                return this.getPixelARGB(sampleAsByte6, n, n, n);
            }
            case 3: {
                final int sample = bitParser.getSample(n, 0);
                final int n2 = n = this.pngChunkPLTE.getRGB(sample);
                if (this.transparencyFilter != null) {
                    n = this.transparencyFilter.filter(n2, sample);
                }
                return n;
            }
            case 2: {
                final int sampleAsByte7 = bitParser.getSampleAsByte(n, 0);
                final int sampleAsByte8 = bitParser.getSampleAsByte(n, 1);
                final int sampleAsByte9 = bitParser.getSampleAsByte(n, 2);
                final int n3 = n = this.getPixelRGB(sampleAsByte7, sampleAsByte8, sampleAsByte9);
                if (this.transparencyFilter != null) {
                    n = this.transparencyFilter.filter(n3, -1);
                }
                int pixelARGB = n;
                if (this.gammaCorrection != null) {
                    pixelARGB = this.getPixelARGB((n & 0xFF000000) >> 24, this.gammaCorrection.correctSample(sampleAsByte7), this.gammaCorrection.correctSample(sampleAsByte8), this.gammaCorrection.correctSample(sampleAsByte9));
                }
                return pixelARGB;
            }
            case 1: {
                final int n4 = n = bitParser.getSampleAsByte(n, 0);
                if (this.gammaCorrection != null) {
                    n = this.gammaCorrection.correctSample(n4);
                }
                int n5 = this.getPixelRGB(n, n, n);
                if (this.transparencyFilter != null) {
                    n5 = this.transparencyFilter.filter(n5, n);
                }
                return n5;
            }
        }
    }
    
    protected ScanlineFilter getScanlineFilter(final FilterType filterType, final int n) throws ImageReadException {
        switch (ScanExpediter$1.$SwitchMap$org$apache$commons$imaging$formats$png$FilterType[filterType.ordinal()]) {
            default: {
                return null;
            }
            case 5: {
                return new ScanlineFilterPaeth(n);
            }
            case 4: {
                return new ScanlineFilterAverage(n);
            }
            case 3: {
                return new ScanlineFilterUp();
            }
            case 2: {
                return new ScanlineFilterSub(n);
            }
            case 1: {
                return new ScanlineFilterNone();
            }
        }
    }
    
    protected byte[] unfilterScanline(final FilterType filterType, final byte[] array, final byte[] array2, final int n) throws ImageReadException, IOException {
        final ScanlineFilter scanlineFilter = this.getScanlineFilter(filterType, n);
        final byte[] array3 = new byte[array.length];
        scanlineFilter.unfilter(array, array3, array2);
        return array3;
    }
}
