package org.apache.commons.imaging.formats.bmp;

import android.support.v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class BmpWriterRgb implements BmpWriter {
    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public int getBitsPerPixel() {
        return 24;
    }

    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public int getPaletteSize() {
        return 0;
    }

    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public void writePalette(BinaryOutputStream binaryOutputStream) throws IOException {
    }

    BmpWriterRgb() {
    }

    @Override // org.apache.commons.imaging.formats.bmp.BmpWriter
    public byte[] getImageData(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        for (int i2 = height - 1; i2 >= 0; i2--) {
            int i3 = i;
            for (int i4 = 0; i4 < width; i4++) {
                int rgb = bufferedImage.getRGB(i4, i2) & ViewCompat.MEASURED_SIZE_MASK;
                byteArrayOutputStream.write((rgb >> 0) & 255);
                byteArrayOutputStream.write((rgb >> 8) & 255);
                byteArrayOutputStream.write((rgb >> 16) & 255);
                i3 += 3;
            }
            i = i3;
            while (i % 4 != 0) {
                byteArrayOutputStream.write(0);
                i++;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
