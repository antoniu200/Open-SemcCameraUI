// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;

class BmpWriterRgb implements BmpWriter
{
    @Override
    public int getBitsPerPixel() {
        return 24;
    }
    
    @Override
    public byte[] getImageData(final BufferedImage bufferedImage) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = height - 1;
        int n = 0;
        while (i >= 0) {
            for (int j = 0; j < width; ++j) {
                final int n2 = bufferedImage.getRGB(j, i) & 0xFFFFFF;
                byteArrayOutputStream.write(n2 >> 0 & 0xFF);
                byteArrayOutputStream.write(n2 >> 8 & 0xFF);
                byteArrayOutputStream.write(n2 >> 16 & 0xFF);
                n += 3;
            }
            while (n % 4 != 0) {
                byteArrayOutputStream.write(0);
                ++n;
            }
            --i;
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public int getPaletteSize() {
        return 0;
    }
    
    @Override
    public void writePalette(final BinaryOutputStream binaryOutputStream) throws IOException {
    }
}
