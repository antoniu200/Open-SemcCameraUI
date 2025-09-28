package org.apache.commons.imaging.formats.tiff.datareaders;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffElement;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterRgb;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DataReaderTiled extends DataReader {
    private final int bitsPerPixel;
    private final ByteOrder byteOrder;
    private final int compression;
    private final TiffImageData.Tiles imageData;
    private final int tileLength;
    private final int tileWidth;

    public DataReaderTiled(TiffDirectory tiffDirectory, PhotometricInterpreter photometricInterpreter, int i, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7, int i8, ByteOrder byteOrder, TiffImageData.Tiles tiles) {
        super(tiffDirectory, photometricInterpreter, iArr, i4, i5, i6, i7);
        this.tileWidth = i;
        this.tileLength = i2;
        this.bitsPerPixel = i3;
        this.compression = i8;
        this.imageData = tiles;
        this.byteOrder = byteOrder;
    }

    private void interpretTile(ImageBuilder imageBuilder, byte[] bArr, int i, int i2, int i3, int i4) throws IOException, ImageReadException {
        boolean zIsHomogenous = isHomogenous(8);
        if (this.predictor != 2 && this.bitsPerPixel == 24 && zIsHomogenous) {
            int i5 = this.tileLength + i2;
            if (i5 <= i4) {
                i4 = i5;
            }
            int i6 = this.tileWidth + i;
            if (i6 <= i3) {
                i3 = i6;
            }
            if (this.photometricInterpreter instanceof PhotometricInterpreterRgb) {
                for (int i7 = i2; i7 < i4; i7++) {
                    int i8 = (i7 - i2) * this.tileWidth * 3;
                    int i9 = i;
                    while (i9 < i3) {
                        imageBuilder.setRGB(i9, i7, (-16777216) | (((bArr[i8] << 8) | (bArr[i8 + 1] & 255)) << 8) | (bArr[i8 + 2] & 255));
                        i9++;
                        i8 += 3;
                    }
                }
                return;
            }
            int[] iArr = new int[3];
            for (int i10 = i2; i10 < i4; i10++) {
                int i11 = (i10 - i2) * this.tileWidth * 3;
                int i12 = i;
                while (i12 < i3) {
                    int i13 = i11 + 1;
                    iArr[0] = bArr[i11] & 255;
                    int i14 = i13 + 1;
                    iArr[1] = bArr[i13] & 255;
                    iArr[2] = bArr[i14] & 255;
                    this.photometricInterpreter.interpretPixel(imageBuilder, iArr, i12, i10);
                    i12++;
                    i11 = i14 + 1;
                }
            }
            return;
        }
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bArr), this.byteOrder);
        int i15 = this.tileWidth * this.tileLength;
        int[] iArr2 = new int[this.bitsPerSampleLength];
        resetPredictor();
        int[] iArrApplyPredictor = iArr2;
        int i16 = 0;
        int i17 = 0;
        for (int i18 = 0; i18 < i15; i18++) {
            int i19 = i16 + i;
            int i20 = i17 + i2;
            getSamplesAsBytes(bitInputStream, iArrApplyPredictor);
            if (i19 < i3 && i20 < i4) {
                iArrApplyPredictor = applyPredictor(iArrApplyPredictor);
                this.photometricInterpreter.interpretPixel(imageBuilder, iArrApplyPredictor, i19, i20);
            }
            i16++;
            if (i16 >= this.tileWidth) {
                resetPredictor();
                i17++;
                bitInputStream.flushCache();
                if (i17 >= this.tileLength) {
                    return;
                } else {
                    i16 = 0;
                }
            }
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.datareaders.DataReader
    public void readImageData(ImageBuilder imageBuilder) throws IOException, ImageReadException {
        int i = (((this.tileWidth * this.bitsPerPixel) + 7) / 8) * this.tileLength;
        int i2 = 0;
        int i3 = 0;
        for (TiffElement.DataElement dataElement : this.imageData.tiles) {
            interpretTile(imageBuilder, decompress(dataElement.getData(), this.compression, i, this.tileWidth, this.tileLength), i2, i3, this.width, this.height);
            int i4 = this.tileWidth + i2;
            if (i4 >= this.width) {
                i3 += this.tileLength;
                if (i3 >= this.height) {
                    return;
                } else {
                    i2 = 0;
                }
            } else {
                i2 = i4;
            }
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.datareaders.DataReader
    public BufferedImage readImageData(Rectangle rectangle) throws IOException, ImageReadException {
        DataReaderTiled dataReaderTiled = this;
        int i = (((dataReaderTiled.tileWidth * dataReaderTiled.bitsPerPixel) + 7) / 8) * dataReaderTiled.tileLength;
        int i2 = rectangle.x / dataReaderTiled.tileWidth;
        int i3 = ((rectangle.x + rectangle.width) - 1) / dataReaderTiled.tileWidth;
        int i4 = rectangle.y / dataReaderTiled.tileLength;
        int i5 = ((rectangle.y + rectangle.height) - 1) / dataReaderTiled.tileLength;
        int i6 = ((i3 - i2) + 1) * dataReaderTiled.tileWidth;
        int i7 = ((i5 - i4) + 1) * dataReaderTiled.tileLength;
        int i8 = ((dataReaderTiled.width + dataReaderTiled.tileWidth) - 1) / dataReaderTiled.tileWidth;
        int i9 = i2 * dataReaderTiled.tileWidth;
        int i10 = i4 * dataReaderTiled.tileLength;
        ImageBuilder imageBuilder = new ImageBuilder(i6, i7, false);
        int i11 = i4;
        while (i11 <= i5) {
            int i12 = i2;
            while (i12 <= i3) {
                ImageBuilder imageBuilder2 = imageBuilder;
                int i13 = i10;
                int i14 = i12;
                int i15 = i11;
                DataReaderTiled dataReaderTiled2 = dataReaderTiled;
                dataReaderTiled2.interpretTile(imageBuilder2, dataReaderTiled.decompress(dataReaderTiled.imageData.tiles[(i11 * i8) + i12].getData(), dataReaderTiled.compression, i, dataReaderTiled.tileWidth, dataReaderTiled.tileLength), (dataReaderTiled.tileWidth * i14) - i9, (dataReaderTiled.tileLength * i15) - i13, i6, i7);
                i12 = i14 + 1;
                i9 = i9;
                i10 = i13;
                i11 = i15;
                imageBuilder = imageBuilder2;
                i = i;
                dataReaderTiled = this;
            }
            i11++;
            i = i;
            dataReaderTiled = this;
        }
        ImageBuilder imageBuilder3 = imageBuilder;
        int i16 = i10;
        int i17 = i9;
        if (rectangle.x == i17 && rectangle.y == i16 && rectangle.width == i6 && rectangle.height == i7) {
            return imageBuilder3.getBufferedImage();
        }
        return imageBuilder3.getSubimage(rectangle.x - i17, rectangle.y - i16, rectangle.width, rectangle.height);
    }
}
