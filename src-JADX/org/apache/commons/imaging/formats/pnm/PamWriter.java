package org.apache.commons.imaging.formats.pnm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PamWriter implements PnmWriter {
    PamWriter() {
    }

    @Override // org.apache.commons.imaging.formats.pnm.PnmWriter
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.write(80);
        outputStream.write(55);
        outputStream.write(10);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        outputStream.write(("WIDTH " + width).getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write(("HEIGHT " + height).getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("DEPTH 4".getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("MAXVAL 255".getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("TUPLTYPE RGB_ALPHA".getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("ENDHDR".getBytes("US-ASCII"));
        outputStream.write(10);
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int rgb = bufferedImage.getRGB(i2, i);
                outputStream.write((byte) ((rgb >> 16) & 255));
                outputStream.write((byte) ((rgb >> 8) & 255));
                outputStream.write((byte) ((rgb >> 0) & 255));
                outputStream.write((byte) ((rgb >> 24) & 255));
            }
        }
    }
}
