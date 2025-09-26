package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PixelParserRle extends PixelParser {
    public PixelParserRle(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        super(bmpHeaderInfo, bArr, bArr2);
    }

    private int getSamplesPerByte() throws ImageReadException {
        if (this.bhi.bitsPerPixel == 8) {
            return 1;
        }
        if (this.bhi.bitsPerPixel == 4) {
            return 2;
        }
        throw new ImageReadException("BMP RLE: bad BitsPerPixel: " + this.bhi.bitsPerPixel);
    }

    private int[] convertDataToSamples(int i) throws ImageReadException {
        if (this.bhi.bitsPerPixel == 8) {
            return new int[]{getColorTableRGB(i)};
        }
        if (this.bhi.bitsPerPixel == 4) {
            return new int[]{getColorTableRGB(i >> 4), getColorTableRGB(i & 15)};
        }
        throw new ImageReadException("BMP RLE: bad BitsPerPixel: " + this.bhi.bitsPerPixel);
    }

    private int processByteOfData(int[] iArr, int i, int i2, int i3, int i4, int i5, ImageBuilder imageBuilder) {
        int i6 = 0;
        for (int i7 = 0; i7 < i; i7++) {
            if (i2 >= 0 && i2 < i4 && i3 >= 0 && i3 < i5) {
                imageBuilder.setRGB(i2, i3, iArr[i7 % iArr.length]);
            } else {
                System.out.println("skipping bad pixel (" + i2 + "," + i3 + ")");
            }
            i2++;
            i6++;
        }
        return i6;
    }

    @Override // org.apache.commons.imaging.formats.bmp.PixelParser
    public void processImage(ImageBuilder imageBuilder) throws IOException, ImageReadException {
        int iProcessByteOfData;
        int i = this.bhi.width;
        int i2 = this.bhi.height;
        int i3 = i2 - 1;
        boolean z = false;
        int i4 = 0;
        while (!z) {
            int i5 = 255 & BinaryFunctions.readByte("RLE (" + i4 + "," + i3 + ") a", this.is, "BMP: Bad RLE");
            int i6 = BinaryFunctions.readByte("RLE (" + i4 + "," + i3 + ") b", this.is, "BMP: Bad RLE") & 255;
            if (i5 == 0) {
                switch (i6) {
                    case 0:
                        i3--;
                        i4 = 0;
                        continue;
                    case 1:
                        z = true;
                        continue;
                    case 2:
                        iProcessByteOfData = (BinaryFunctions.readByte("RLE deltaX", this.is, "BMP: Bad RLE") & 255) + i4;
                        i3 -= BinaryFunctions.readByte("RLE deltaY", this.is, "BMP: Bad RLE") & 255;
                        break;
                    default:
                        int samplesPerByte = getSamplesPerByte();
                        int i7 = i6 / samplesPerByte;
                        if (i6 % samplesPerByte > 0) {
                            i7++;
                        }
                        if (i7 % 2 != 0) {
                            i7++;
                        }
                        byte[] bytes = BinaryFunctions.readBytes("bytes", this.is, i7, "RLE: Absolute Mode");
                        int i8 = 0;
                        int i9 = i4;
                        int i10 = i6;
                        while (i10 > 0) {
                            int iProcessByteOfData2 = processByteOfData(convertDataToSamples(bytes[i8] & 255), Math.min(i10, samplesPerByte), i9, i3, i, i2, imageBuilder);
                            i9 += iProcessByteOfData2;
                            i10 -= iProcessByteOfData2;
                            i8++;
                            samplesPerByte = samplesPerByte;
                        }
                        i4 = i9;
                        continue;
                }
            } else {
                iProcessByteOfData = processByteOfData(convertDataToSamples(i6), i5, i4, i3, i, i2, imageBuilder) + i4;
            }
            i4 = iProcessByteOfData;
        }
    }
}
