// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Map;
import java.io.OutputStream;
import java.awt.image.BufferedImage;

class PbmWriter implements PnmWriter
{
    private boolean rawbits;
    
    public PbmWriter(final boolean rawbits) {
        this.rawbits = rawbits;
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.write(80);
        int n;
        if (this.rawbits) {
            n = 52;
        }
        else {
            n = 49;
        }
        outputStream.write(n);
        outputStream.write(32);
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        outputStream.write(Integer.toString(width).getBytes("US-ASCII"));
        outputStream.write(32);
        outputStream.write(Integer.toString(height).getBytes("US-ASCII"));
        outputStream.write(10);
        int i = 0;
        int n3;
        int n2 = n3 = 0;
        while (i < height) {
            for (int j = 0; j < width; ++j) {
                final int rgb = bufferedImage.getRGB(j, i);
                int k;
                if (((rgb >> 16 & 0xFF) + (rgb >> 8 & 0xFF) + (rgb >> 0 & 0xFF)) / 3 > 127) {
                    k = 0;
                }
                else {
                    k = 1;
                }
                if (this.rawbits) {
                    final int n4 = n2 << 1 | (k & 0x1);
                    final int n5 = ++n3;
                    n2 = n4;
                    if (n5 >= 8) {
                        outputStream.write((byte)n4);
                        n3 = 0;
                        n2 = 0;
                    }
                }
                else {
                    outputStream.write(Integer.toString(k).getBytes("US-ASCII"));
                    outputStream.write(32);
                }
            }
            if (this.rawbits && n3 > 0) {
                outputStream.write((byte)(n2 << 8 - n3));
                n2 = 0;
                n3 = 0;
            }
            ++i;
        }
    }
}
