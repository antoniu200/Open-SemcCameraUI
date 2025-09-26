package org.apache.commons.imaging.formats.bmp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
interface BmpWriter {
    int getBitsPerPixel();

    byte[] getImageData(BufferedImage bufferedImage);

    int getPaletteSize();

    void writePalette(BinaryOutputStream binaryOutputStream) throws IOException;
}
