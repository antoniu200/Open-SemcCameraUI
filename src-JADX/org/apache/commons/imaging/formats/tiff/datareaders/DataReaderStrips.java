package org.apache.commons.imaging.formats.tiff.datareaders;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterRgb;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class DataReaderStrips extends DataReader {
    private final int bitsPerPixel;
    private final ByteOrder byteOrder;
    private final int compression;
    private final TiffImageData.Strips imageData;
    private final int rowsPerStrip;
    private int x;
    private int y;

    public DataReaderStrips(TiffDirectory tiffDirectory, PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6, ByteOrder byteOrder, int i7, TiffImageData.Strips strips) {
        super(tiffDirectory, photometricInterpreter, iArr, i2, i3, i4, i5);
        this.bitsPerPixel = i;
        this.compression = i6;
        this.rowsPerStrip = i7;
        this.imageData = strips;
        this.byteOrder = byteOrder;
    }

    private void interpretStrip(ImageBuilder imageBuilder, byte[] bArr, int i, int i2) throws IOException, ImageReadException {
        if (this.y >= i2) {
            return;
        }
        boolean zIsHomogenous = isHomogenous(8);
        if (this.predictor != 2 && this.bitsPerPixel == 8 && zIsHomogenous) {
            int i3 = i / this.width;
            if (this.y + i3 > i2) {
                i3 = i2 - this.y;
            }
            int i4 = this.y;
            int i5 = this.y + i3;
            this.x = 0;
            this.y += i3;
            int[] iArr = new int[1];
            int i6 = 0;
            while (i4 < i5) {
                int i7 = i6;
                int i8 = 0;
                while (i8 < this.width) {
                    iArr[0] = bArr[i7] & 255;
                    this.photometricInterpreter.interpretPixel(imageBuilder, iArr, i8, i4);
                    i8++;
                    i7++;
                }
                i4++;
                i6 = i7;
            }
            return;
        }
        if (this.predictor != 2 && this.bitsPerPixel == 24 && zIsHomogenous) {
            int i9 = i / this.width;
            if (this.y + i9 > i2) {
                i9 = i2 - this.y;
            }
            int i10 = this.y;
            int i11 = this.y + i9;
            this.x = 0;
            this.y += i9;
            if (this.photometricInterpreter instanceof PhotometricInterpreterRgb) {
                int i12 = 0;
                while (i10 < i11) {
                    int i13 = i12;
                    int i14 = 0;
                    while (i14 < this.width) {
                        imageBuilder.setRGB(i14, i10, (-16777216) | (((bArr[i13] << 8) | (bArr[i13 + 1] & 255)) << 8) | (bArr[i13 + 2] & 255));
                        i14++;
                        i13 += 3;
                    }
                    i10++;
                    i12 = i13;
                }
                return;
            }
            int[] iArr2 = new int[3];
            int i15 = 0;
            while (i10 < i11) {
                int i16 = i15;
                int i17 = 0;
                while (i17 < this.width) {
                    int i18 = i16 + 1;
                    iArr2[0] = bArr[i16] & 255;
                    int i19 = i18 + 1;
                    iArr2[1] = bArr[i18] & 255;
                    iArr2[2] = bArr[i19] & 255;
                    this.photometricInterpreter.interpretPixel(imageBuilder, iArr2, i17, i10);
                    i17++;
                    i16 = i19 + 1;
                }
                i10++;
                i15 = i16;
            }
            return;
        }
        BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(bArr), this.byteOrder);
        int[] iArr3 = new int[this.bitsPerSampleLength];
        resetPredictor();
        int[] iArrApplyPredictor = iArr3;
        for (int i20 = 0; i20 < i; i20++) {
            getSamplesAsBytes(bitInputStream, iArrApplyPredictor);
            if (this.x < this.width) {
                iArrApplyPredictor = applyPredictor(iArrApplyPredictor);
                this.photometricInterpreter.interpretPixel(imageBuilder, iArrApplyPredictor, this.x, this.y);
            }
            this.x++;
            if (this.x >= this.width) {
                this.x = 0;
                resetPredictor();
                this.y++;
                bitInputStream.flushCache();
                if (this.y >= i2) {
                    return;
                }
            }
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.datareaders.DataReader
    public void readImageData(ImageBuilder imageBuilder) throws IOException, ImageReadException {
        for (int i = 0; i < this.imageData.getImageDataLength(); i++) {
            long j = 4294967295L & this.rowsPerStrip;
            long jMin = Math.min(this.height - (i * j), j);
            interpretStrip(imageBuilder, decompress(this.imageData.getImageData(i).getData(), this.compression, (int) ((((this.bitsPerPixel * this.width) + 7) / 8) * jMin), this.width, (int) jMin), (int) (this.width * jMin), this.height);
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.datareaders.DataReader
    public BufferedImage readImageData(Rectangle rectangle) throws IOException, ImageReadException {
        int i = rectangle.y / this.rowsPerStrip;
        int i2 = ((rectangle.y + rectangle.height) - 1) / this.rowsPerStrip;
        int i3 = ((i2 - i) + 1) * this.rowsPerStrip;
        int i4 = i * this.rowsPerStrip;
        int i5 = (rectangle.y - i4) + rectangle.height;
        ImageBuilder imageBuilder = new ImageBuilder(this.width, i3, false);
        for (int i6 = i; i6 <= i2; i6++) {
            long j = 4294967295L & this.rowsPerStrip;
            long jMin = Math.min(this.height - (i6 * j), j);
            interpretStrip(imageBuilder, decompress(this.imageData.getImageData(i6).getData(), this.compression, (int) ((((this.bitsPerPixel * this.width) + 7) / 8) * jMin), this.width, (int) jMin), (int) (jMin * this.width), i5);
        }
        if (rectangle.x == 0 && rectangle.y == i4 && rectangle.width == this.width && rectangle.height == i3) {
            return imageBuilder.getBufferedImage();
        }
        return imageBuilder.getSubimage(rectangle.x, rectangle.y - i4, rectangle.width, rectangle.height);
    }
}
