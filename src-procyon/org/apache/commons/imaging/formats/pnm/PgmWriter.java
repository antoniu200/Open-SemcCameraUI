// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Map;
import java.io.OutputStream;
import java.awt.image.BufferedImage;

class PgmWriter implements PnmWriter
{
    private boolean rawbits;
    
    public PgmWriter(final boolean rawbits) {
        this.rawbits = rawbits;
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.write(80);
        int n;
        if (this.rawbits) {
            n = 53;
        }
        else {
            n = 50;
        }
        outputStream.write(n);
        outputStream.write(32);
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        outputStream.write(Integer.toString(width).getBytes("US-ASCII"));
        outputStream.write(32);
        outputStream.write(Integer.toString(height).getBytes("US-ASCII"));
        outputStream.write(32);
        outputStream.write(Integer.toString(255).getBytes("US-ASCII"));
        outputStream.write(10);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                final int rgb = bufferedImage.getRGB(j, i);
                final int k = ((rgb >> 16 & 0xFF) + (rgb >> 8 & 0xFF) + (rgb >> 0 & 0xFF)) / 3;
                if (this.rawbits) {
                    outputStream.write((byte)k);
                }
                else {
                    outputStream.write(Integer.toString(k).getBytes("US-ASCII"));
                    outputStream.write(32);
                }
            }
        }
    }
}
