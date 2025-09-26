// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.datareaders;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterRgb;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import java.nio.ByteOrder;

public final class DataReaderStrips extends DataReader
{
    private final int bitsPerPixel;
    private final ByteOrder byteOrder;
    private final int compression;
    private final TiffImageData.Strips imageData;
    private final int rowsPerStrip;
    private int x;
    private int y;
    
    public DataReaderStrips(final TiffDirectory tiffDirectory, final PhotometricInterpreter photometricInterpreter, final int bitsPerPixel, final int[] array, final int n, final int n2, final int n3, final int n4, final int compression, final ByteOrder byteOrder, final int rowsPerStrip, final TiffImageData.Strips imageData) {
        super(tiffDirectory, photometricInterpreter, array, n, n2, n3, n4);
        this.bitsPerPixel = bitsPerPixel;
        this.compression = compression;
        this.rowsPerStrip = rowsPerStrip;
        this.imageData = imageData;
        this.byteOrder = byteOrder;
    }
    
    private void interpretStrip(final ImageBuilder imageBuilder, final byte[] buf, int n, int i) throws ImageReadException, IOException {
        if (this.y >= i) {
            return;
        }
        final boolean homogenous = this.isHomogenous(8);
        if (this.predictor != 2 && this.bitsPerPixel == 8 && homogenous) {
            int n2;
            n = (n2 = n / this.width);
            if (this.y + n > i) {
                n2 = i - this.y;
            }
            i = this.y;
            final int y = this.y;
            this.x = 0;
            this.y += n2;
            final int[] array = { 0 };
            n = 0;
            while (i < y + n2) {
                for (int j = 0; j < this.width; ++j, ++n) {
                    array[0] = (buf[n] & 0xFF);
                    this.photometricInterpreter.interpretPixel(imageBuilder, array, j, i);
                }
                ++i;
            }
            return;
        }
        if (this.predictor != 2 && this.bitsPerPixel == 24 && homogenous) {
            if (this.y + (n /= this.width) > i) {
                n = i - this.y;
            }
            i = this.y;
            final int n3 = this.y + n;
            this.x = 0;
            this.y += n;
            if (this.photometricInterpreter instanceof PhotometricInterpreterRgb) {
                n = 0;
                while (i < n3) {
                    for (int k = 0; k < this.width; ++k, n += 3) {
                        imageBuilder.setRGB(k, i, 0xFF000000 | (buf[n] << 8 | (buf[n + 1] & 0xFF)) << 8 | (buf[n + 2] & 0xFF));
                    }
                    ++i;
                }
            }
            else {
                final int[] array2 = new int[3];
                n = 0;
                while (i < n3) {
                    for (int l = 0; l < this.width; ++l, ++n) {
                        final int n4 = n + 1;
                        array2[0] = (buf[n] & 0xFF);
                        n = n4 + 1;
                        array2[1] = (buf[n4] & 0xFF);
                        array2[2] = (buf[n] & 0xFF);
                        this.photometricInterpreter.interpretPixel(imageBuilder, array2, l, i);
                    }
                    ++i;
                }
            }
            return;
        }
        final BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(buf), this.byteOrder);
        int[] array3 = new int[this.bitsPerSampleLength];
        this.resetPredictor();
        int[] applyPredictor;
        for (int n5 = 0; n5 < n; ++n5, array3 = applyPredictor) {
            this.getSamplesAsBytes(bitInputStream, array3);
            applyPredictor = array3;
            if (this.x < this.width) {
                applyPredictor = this.applyPredictor(array3);
                this.photometricInterpreter.interpretPixel(imageBuilder, applyPredictor, this.x, this.y);
            }
            ++this.x;
            if (this.x >= this.width) {
                this.x = 0;
                this.resetPredictor();
                ++this.y;
                bitInputStream.flushCache();
                if (this.y >= i) {
                    break;
                }
            }
        }
    }
    
    @Override
    public BufferedImage readImageData(final Rectangle rectangle) throws ImageReadException, IOException {
        int i = rectangle.y / this.rowsPerStrip;
        final int n = (rectangle.y + rectangle.height - 1) / this.rowsPerStrip;
        final int n2 = (n - i + 1) * this.rowsPerStrip;
        final int n3 = i * this.rowsPerStrip;
        final int y = rectangle.y;
        final int height = rectangle.height;
        final ImageBuilder imageBuilder = new ImageBuilder(this.width, n2, false);
        while (i <= n) {
            final long b = 0xFFFFFFFFL & (long)this.rowsPerStrip;
            final long min = Math.min(this.height - i * b, b);
            this.interpretStrip(imageBuilder, this.decompress(this.imageData.getImageData(i).getData(), this.compression, (int)((this.bitsPerPixel * this.width + 7) / 8 * min), this.width, (int)min), (int)(min * this.width), y - n3 + height);
            ++i;
        }
        if (rectangle.x == 0 && rectangle.y == n3 && rectangle.width == this.width && rectangle.height == n2) {
            return imageBuilder.getBufferedImage();
        }
        return imageBuilder.getSubimage(rectangle.x, rectangle.y - n3, rectangle.width, rectangle.height);
    }
    
    @Override
    public void readImageData(final ImageBuilder imageBuilder) throws ImageReadException, IOException {
        for (int i = 0; i < this.imageData.getImageDataLength(); ++i) {
            final long b = 0xFFFFFFFFL & (long)this.rowsPerStrip;
            final long min = Math.min(this.height - i * b, b);
            this.interpretStrip(imageBuilder, this.decompress(this.imageData.getImageData(i).getData(), this.compression, (int)((this.bitsPerPixel * this.width + 7) / 8 * min), this.width, (int)min), (int)(this.width * min), this.height);
        }
    }
}
