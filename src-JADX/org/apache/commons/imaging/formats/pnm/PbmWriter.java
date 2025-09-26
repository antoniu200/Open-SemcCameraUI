package org.apache.commons.imaging.formats.pnm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PbmWriter implements PnmWriter {
    private boolean rawbits;

    public PbmWriter(boolean z) {
        this.rawbits = z;
    }

    @Override // org.apache.commons.imaging.formats.pnm.PnmWriter
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.write(80);
        outputStream.write(this.rawbits ? 52 : 49);
        outputStream.write(32);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        outputStream.write(Integer.toString(width).getBytes("US-ASCII"));
        outputStream.write(32);
        outputStream.write(Integer.toString(height).getBytes("US-ASCII"));
        outputStream.write(10);
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < height; i3++) {
            int i4 = i;
            for (int i5 = 0; i5 < width; i5++) {
                int rgb = bufferedImage.getRGB(i5, i3);
                int i6 = ((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + ((rgb >> 0) & 255)) / 3 > 127 ? 0 : 1;
                if (this.rawbits) {
                    i4 = (i4 << 1) | (i6 & 1);
                    i2++;
                    if (i2 >= 8) {
                        outputStream.write((byte) i4);
                        i2 = 0;
                        i4 = 0;
                    }
                } else {
                    outputStream.write(Integer.toString(i6).getBytes("US-ASCII"));
                    outputStream.write(32);
                }
            }
            if (!this.rawbits || i2 <= 0) {
                i = i4;
            } else {
                outputStream.write((byte) (i4 << (8 - i2)));
                i = 0;
                i2 = 0;
            }
        }
    }
}
