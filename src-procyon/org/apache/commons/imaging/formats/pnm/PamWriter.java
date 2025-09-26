// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Map;
import java.io.OutputStream;
import java.awt.image.BufferedImage;

class PamWriter implements PnmWriter
{
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.write(80);
        outputStream.write(55);
        outputStream.write(10);
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final StringBuilder sb = new StringBuilder();
        sb.append("WIDTH ");
        sb.append(width);
        outputStream.write(sb.toString().getBytes("US-ASCII"));
        outputStream.write(10);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("HEIGHT ");
        sb2.append(height);
        outputStream.write(sb2.toString().getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("DEPTH 4".getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("MAXVAL 255".getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("TUPLTYPE RGB_ALPHA".getBytes("US-ASCII"));
        outputStream.write(10);
        outputStream.write("ENDHDR".getBytes("US-ASCII"));
        outputStream.write(10);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                final int rgb = bufferedImage.getRGB(j, i);
                outputStream.write((byte)(rgb >> 16 & 0xFF));
                outputStream.write((byte)(rgb >> 8 & 0xFF));
                outputStream.write((byte)(rgb >> 0 & 0xFF));
                outputStream.write((byte)(rgb >> 24 & 0xFF));
            }
        }
    }
}
