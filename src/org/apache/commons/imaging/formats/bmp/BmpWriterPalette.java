package org.apache.commons.imaging.formats.bmp;

import android.support.v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.palette.SimplePalette;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BmpWriterPalette implements BmpWriter {
    private final int bitsPerSample;
    private final SimplePalette palette;

    public BmpWriterPalette(SimplePalette simplePalette) {
        this.palette = simplePalette;
        if (simplePalette.length() <= 2) {
            this.bitsPerSample = 1;
        } else if (simplePalette.length() <= 16) {
            this.bitsPerSample = 4;
        } else {
            this.bitsPerSample = 8;
        }
    }

    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public int getPaletteSize() {
        return this.palette.length();
    }

    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public int getBitsPerPixel() {
        return this.bitsPerSample;
    }

    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public void writePalette(BinaryOutputStream binaryOutputStream) throws IOException {
        for (int i = 0; i < this.palette.length(); i++) {
            int entry = this.palette.getEntry(i);
            binaryOutputStream.write((entry >> 0) & 255);
            binaryOutputStream.write((entry >> 8) & 255);
            binaryOutputStream.write((entry >> 16) & 255);
            binaryOutputStream.write(0);
        }
    }

    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public byte[] getImageData(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = height - 1; i4 >= 0; i4--) {
            int i5 = i;
            for (int i6 = 0; i6 < width; i6++) {
                int paletteIndex = this.palette.getPaletteIndex(bufferedImage.getRGB(i6, i4) & ViewCompat.MEASURED_SIZE_MASK);
                if (this.bitsPerSample == 8) {
                    byteArrayOutputStream.write(255 & paletteIndex);
                    i3++;
                } else {
                    i5 = (i5 << this.bitsPerSample) | paletteIndex;
                    i2 += this.bitsPerSample;
                    if (i2 >= 8) {
                        byteArrayOutputStream.write(255 & i5);
                        i3++;
                        i2 = 0;
                        i5 = 0;
                    }
                }
            }
            if (i2 > 0) {
                byteArrayOutputStream.write((i5 << (8 - i2)) & 255);
                i3++;
                i = 0;
                i2 = 0;
            } else {
                i = i5;
            }
            while (i3 % 4 != 0) {
                byteArrayOutputStream.write(0);
                i3++;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
