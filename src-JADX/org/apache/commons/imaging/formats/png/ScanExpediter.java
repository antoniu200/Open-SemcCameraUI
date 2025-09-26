package org.apache.commons.imaging.formats.png;

import android.support.v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPlte;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilter;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterAverage;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterNone;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterPaeth;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterSub;
import org.apache.commons.imaging.formats.png.scanlinefilters.ScanlineFilterUp;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class ScanExpediter {
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

    public abstract void drive() throws IOException, ImageReadException;

    protected final int getPixelARGB(int i, int i2, int i3, int i4) {
        return ((255 & i4) << 0) | ((i & 255) << 24) | ((i2 & 255) << 16) | ((255 & i3) << 8);
    }

    public ScanExpediter(int i, int i2, InputStream inputStream, BufferedImage bufferedImage, PngColorType pngColorType, int i3, int i4, PngChunkPlte pngChunkPlte, GammaCorrection gammaCorrection, TransparencyFilter transparencyFilter) {
        this.width = i;
        this.height = i2;
        this.is = inputStream;
        this.bi = bufferedImage;
        this.pngColorType = pngColorType;
        this.bitDepth = i3;
        this.bytesPerPixel = getBitsToBytesRoundingUp(i4);
        this.bitsPerPixel = i4;
        this.pngChunkPLTE = pngChunkPlte;
        this.gammaCorrection = gammaCorrection;
        this.transparencyFilter = transparencyFilter;
    }

    protected final int getBitsToBytesRoundingUp(int i) {
        return (i + 7) / 8;
    }

    protected final int getPixelRGB(int i, int i2, int i3) {
        return getPixelARGB(255, i, i2, i3);
    }

    protected int getRGB(BitParser bitParser, int i) throws IOException, ImageReadException {
        switch (this.pngColorType) {
            case GREYSCALE:
                int sampleAsByte = bitParser.getSampleAsByte(i, 0);
                if (this.gammaCorrection != null) {
                    sampleAsByte = this.gammaCorrection.correctSample(sampleAsByte);
                }
                int pixelRGB = getPixelRGB(sampleAsByte, sampleAsByte, sampleAsByte);
                return this.transparencyFilter != null ? this.transparencyFilter.filter(pixelRGB, sampleAsByte) : pixelRGB;
            case TRUE_COLOR:
                int sampleAsByte2 = bitParser.getSampleAsByte(i, 0);
                int sampleAsByte3 = bitParser.getSampleAsByte(i, 1);
                int sampleAsByte4 = bitParser.getSampleAsByte(i, 2);
                int pixelRGB2 = getPixelRGB(sampleAsByte2, sampleAsByte3, sampleAsByte4);
                if (this.transparencyFilter != null) {
                    pixelRGB2 = this.transparencyFilter.filter(pixelRGB2, -1);
                }
                return this.gammaCorrection != null ? getPixelARGB((pixelRGB2 & ViewCompat.MEASURED_STATE_MASK) >> 24, this.gammaCorrection.correctSample(sampleAsByte2), this.gammaCorrection.correctSample(sampleAsByte3), this.gammaCorrection.correctSample(sampleAsByte4)) : pixelRGB2;
            case INDEXED_COLOR:
                int sample = bitParser.getSample(i, 0);
                int rgb = this.pngChunkPLTE.getRGB(sample);
                return this.transparencyFilter != null ? this.transparencyFilter.filter(rgb, sample) : rgb;
            case GREYSCALE_WITH_ALPHA:
                int sampleAsByte5 = bitParser.getSampleAsByte(i, 0);
                int sampleAsByte6 = bitParser.getSampleAsByte(i, 1);
                if (this.gammaCorrection != null) {
                    sampleAsByte5 = this.gammaCorrection.correctSample(sampleAsByte5);
                }
                return getPixelARGB(sampleAsByte6, sampleAsByte5, sampleAsByte5, sampleAsByte5);
            case TRUE_COLOR_WITH_ALPHA:
                int sampleAsByte7 = bitParser.getSampleAsByte(i, 0);
                int sampleAsByte8 = bitParser.getSampleAsByte(i, 1);
                int sampleAsByte9 = bitParser.getSampleAsByte(i, 2);
                int sampleAsByte10 = bitParser.getSampleAsByte(i, 3);
                if (this.gammaCorrection != null) {
                    sampleAsByte7 = this.gammaCorrection.correctSample(sampleAsByte7);
                    sampleAsByte8 = this.gammaCorrection.correctSample(sampleAsByte8);
                    sampleAsByte9 = this.gammaCorrection.correctSample(sampleAsByte9);
                }
                return getPixelARGB(sampleAsByte10, sampleAsByte7, sampleAsByte8, sampleAsByte9);
            default:
                throw new ImageReadException("PNG: unknown color type: " + this.pngColorType);
        }
    }

    protected ScanlineFilter getScanlineFilter(FilterType filterType, int i) throws ImageReadException {
        switch (filterType) {
            case NONE:
                return new ScanlineFilterNone();
            case SUB:
                return new ScanlineFilterSub(i);
            case UP:
                return new ScanlineFilterUp();
            case AVERAGE:
                return new ScanlineFilterAverage(i);
            case PAETH:
                return new ScanlineFilterPaeth(i);
            default:
                return null;
        }
    }

    protected byte[] unfilterScanline(FilterType filterType, byte[] bArr, byte[] bArr2, int i) throws IOException, ImageReadException {
        ScanlineFilter scanlineFilter = getScanlineFilter(filterType, i);
        byte[] bArr3 = new byte[bArr.length];
        scanlineFilter.unfilter(bArr, bArr3, bArr2);
        return bArr3;
    }

    protected byte[] getNextScanline(InputStream inputStream, int i, byte[] bArr, int i2) throws IOException, ImageReadException {
        int i3 = inputStream.read();
        if (i3 < 0) {
            throw new ImageReadException("PNG: missing filter type");
        }
        if (i3 >= FilterType.values().length) {
            throw new ImageReadException("PNG: unknown filterType: " + i3);
        }
        return unfilterScanline(FilterType.values()[i3], BinaryFunctions.readBytes("scanline", inputStream, i, "PNG: missing image data"), bArr, i2);
    }
}
