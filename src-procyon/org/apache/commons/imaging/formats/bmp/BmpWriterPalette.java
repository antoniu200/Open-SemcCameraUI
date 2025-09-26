// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.palette.SimplePalette;

class BmpWriterPalette implements BmpWriter
{
    private final int bitsPerSample;
    private final SimplePalette palette;
    
    public BmpWriterPalette(final SimplePalette palette) {
        this.palette = palette;
        if (palette.length() <= 2) {
            this.bitsPerSample = 1;
        }
        else if (palette.length() <= 16) {
            this.bitsPerSample = 4;
        }
        else {
            this.bitsPerSample = 8;
        }
    }
    
    @Override
    public int getBitsPerPixel() {
        return this.bitsPerSample;
    }
    
    @Override
    public byte[] getImageData(final BufferedImage bufferedImage) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = height - 1;
        int n = 0;
        int n3;
        int n2 = n3 = 0;
        while (i >= 0) {
            int n4;
            for (int j = 0; j < width; ++j, n3 = n4) {
                final int paletteIndex = this.palette.getPaletteIndex(bufferedImage.getRGB(j, i) & 0xFFFFFF);
                if (this.bitsPerSample == 8) {
                    byteArrayOutputStream.write(0xFF & paletteIndex);
                    n4 = n3 + 1;
                }
                else {
                    final int n5 = n << this.bitsPerSample | paletteIndex;
                    final int n6 = n2 += this.bitsPerSample;
                    n4 = n3;
                    n = n5;
                    if (n6 >= 8) {
                        byteArrayOutputStream.write(0xFF & n5);
                        n4 = n3 + 1;
                        n2 = 0;
                        n = 0;
                    }
                }
            }
            if (n2 > 0) {
                byteArrayOutputStream.write(n << 8 - n2 & 0xFF);
                ++n3;
                n = 0;
                n2 = 0;
            }
            while (n3 % 4 != 0) {
                byteArrayOutputStream.write(0);
                ++n3;
            }
            --i;
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public int getPaletteSize() {
        return this.palette.length();
    }
    
    @Override
    public void writePalette(final BinaryOutputStream binaryOutputStream) throws IOException {
        for (int i = 0; i < this.palette.length(); ++i) {
            final int entry = this.palette.getEntry(i);
            binaryOutputStream.write(entry >> 0 & 0xFF);
            binaryOutputStream.write(entry >> 8 & 0xFF);
            binaryOutputStream.write(entry >> 16 & 0xFF);
            binaryOutputStream.write(0);
        }
    }
}
