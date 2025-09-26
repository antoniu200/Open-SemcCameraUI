// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.datareaders;

import org.apache.commons.imaging.formats.tiff.TiffElement;
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

public final class DataReaderTiled extends DataReader
{
    private final int bitsPerPixel;
    private final ByteOrder byteOrder;
    private final int compression;
    private final TiffImageData.Tiles imageData;
    private final int tileLength;
    private final int tileWidth;
    
    public DataReaderTiled(final TiffDirectory tiffDirectory, final PhotometricInterpreter photometricInterpreter, final int tileWidth, final int tileLength, final int bitsPerPixel, final int[] array, final int n, final int n2, final int n3, final int n4, final int compression, final ByteOrder byteOrder, final TiffImageData.Tiles imageData) {
        super(tiffDirectory, photometricInterpreter, array, n, n2, n3, n4);
        this.tileWidth = tileWidth;
        this.tileLength = tileLength;
        this.bitsPerPixel = bitsPerPixel;
        this.compression = compression;
        this.imageData = imageData;
        this.byteOrder = byteOrder;
    }
    
    private void interpretTile(final ImageBuilder imageBuilder, final byte[] buf, final int n, final int n2, int n3, int n4) throws ImageReadException, IOException {
        final boolean homogenous = this.isHomogenous(8);
        if (this.predictor != 2 && this.bitsPerPixel == 24 && homogenous) {
            final int n5 = this.tileLength + n2;
            if (n5 <= n4) {
                n4 = n5;
            }
            final int n6 = this.tileWidth + n;
            if (n6 <= n3) {
                n3 = n6;
            }
            if (this.photometricInterpreter instanceof PhotometricInterpreterRgb) {
                for (int i = n2; i < n4; ++i) {
                    for (int n7 = (i - n2) * this.tileWidth * 3, j = n; j < n3; ++j, n7 += 3) {
                        imageBuilder.setRGB(j, i, 0xFF000000 | (buf[n7] << 8 | (buf[n7 + 1] & 0xFF)) << 8 | (buf[n7 + 2] & 0xFF));
                    }
                }
            }
            else {
                final int[] array = new int[3];
                for (int k = n2; k < n4; ++k) {
                    for (int n8 = (k - n2) * this.tileWidth * 3, l = n; l < n3; ++l, ++n8) {
                        final int n9 = n8 + 1;
                        array[0] = (buf[n8] & 0xFF);
                        n8 = n9 + 1;
                        array[1] = (buf[n9] & 0xFF);
                        array[2] = (buf[n8] & 0xFF);
                        this.photometricInterpreter.interpretPixel(imageBuilder, array, l, k);
                    }
                }
            }
            return;
        }
        final BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(buf), this.byteOrder);
        final int tileWidth = this.tileWidth;
        final int tileLength = this.tileLength;
        int[] array2 = new int[this.bitsPerSampleLength];
        this.resetPredictor();
        int n10 = 0;
        int n12;
        int n11 = n12 = 0;
        while (n10 < tileWidth * tileLength) {
            final int n13 = n11 + n;
            final int n14 = n12 + n2;
            this.getSamplesAsBytes(bitInputStream, array2);
            int[] applyPredictor = array2;
            if (n13 < n3) {
                applyPredictor = array2;
                if (n14 < n4) {
                    applyPredictor = this.applyPredictor(array2);
                    this.photometricInterpreter.interpretPixel(imageBuilder, applyPredictor, n13, n14);
                }
            }
            final int n15 = ++n11;
            int n16 = n12;
            if (n15 >= this.tileWidth) {
                this.resetPredictor();
                n16 = n12 + 1;
                bitInputStream.flushCache();
                if (n16 >= this.tileLength) {
                    break;
                }
                n11 = 0;
            }
            ++n10;
            n12 = n16;
            array2 = applyPredictor;
        }
    }
    
    @Override
    public BufferedImage readImageData(final Rectangle rectangle) throws ImageReadException, IOException {
        final int n = (this.tileWidth * this.bitsPerPixel + 7) / 8 * this.tileLength;
        final int n2 = rectangle.x / this.tileWidth;
        final int n3 = (rectangle.x + rectangle.width - 1) / this.tileWidth;
        int i = rectangle.y / this.tileLength;
        final int n4 = (rectangle.y + rectangle.height - 1) / this.tileLength;
        final int n5 = (n3 - n2 + 1) * this.tileWidth;
        final int n6 = (n4 - i + 1) * this.tileLength;
        final int n7 = (this.width + this.tileWidth - 1) / this.tileWidth;
        final int n8 = n2 * this.tileWidth;
        final int n9 = i * this.tileLength;
        final ImageBuilder imageBuilder = new ImageBuilder(n5, n6, false);
        while (i <= n4) {
            for (int j = n2; j <= n3; ++j) {
                this.interpretTile(imageBuilder, this.decompress(this.imageData.tiles[i * n7 + j].getData(), this.compression, n, this.tileWidth, this.tileLength), this.tileWidth * j - n8, this.tileLength * i - n9, n5, n6);
            }
            ++i;
        }
        if (rectangle.x == n8 && rectangle.y == n9 && rectangle.width == n5 && rectangle.height == n6) {
            return imageBuilder.getBufferedImage();
        }
        return imageBuilder.getSubimage(rectangle.x - n8, rectangle.y - n9, rectangle.width, rectangle.height);
    }
    
    @Override
    public void readImageData(final ImageBuilder imageBuilder) throws ImageReadException, IOException {
        final int n = (this.tileWidth * this.bitsPerPixel + 7) / 8;
        final int tileLength = this.tileLength;
        final TiffElement.DataElement[] tiles = this.imageData.tiles;
        final int length = tiles.length;
        int i = 0;
        int n3;
        int n2 = n3 = 0;
        while (i < length) {
            this.interpretTile(imageBuilder, this.decompress(tiles[i].getData(), this.compression, n * tileLength, this.tileWidth, this.tileLength), n2, n3, this.width, this.height);
            final int n4 = this.tileWidth + n2;
            int n5;
            int n6;
            if (n4 >= this.width) {
                n5 = n3 + this.tileLength;
                if (n5 >= this.height) {
                    break;
                }
                n6 = 0;
            }
            else {
                n5 = n3;
                n6 = n4;
            }
            ++i;
            n2 = n6;
            n3 = n5;
        }
    }
}
