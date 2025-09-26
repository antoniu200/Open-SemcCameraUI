package org.apache.commons.imaging.formats.pnm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PgmWriter implements PnmWriter {
    private boolean rawbits;

    public PgmWriter(boolean z) {
        this.rawbits = z;
    }

    @Override // org.apache.commons.imaging.formats.pnm.PnmWriter
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.write(80);
        outputStream.write(this.rawbits ? 53 : 50);
        outputStream.write(32);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        outputStream.write(Integer.toString(width).getBytes("US-ASCII"));
        outputStream.write(32);
        outputStream.write(Integer.toString(height).getBytes("US-ASCII"));
        outputStream.write(32);
        outputStream.write(Integer.toString(255).getBytes("US-ASCII"));
        outputStream.write(10);
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int rgb = bufferedImage.getRGB(i2, i);
                int i3 = ((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + ((rgb >> 0) & 255)) / 3;
                if (this.rawbits) {
                    outputStream.write((byte) i3);
                } else {
                    outputStream.write(Integer.toString(i3).getBytes("US-ASCII"));
                    outputStream.write(32);
                }
            }
        }
    }
}
