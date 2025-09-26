// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;
import java.awt.image.BufferedImage;

public final class Dithering
{
    private Dithering() {
    }
    
    private static int adjustPixel(int n, int n2, int n3, int n4, int n5, final int n6) {
        n2 = n2 * n6 / 16 + (n >> 24 & 0xFF);
        n3 = n3 * n6 / 16 + (n >> 16 & 0xFF);
        n4 = n4 * n6 / 16 + (n >> 8 & 0xFF);
        n5 = (n & 0xFF) + n5 * n6 / 16;
        if (n2 < 0) {
            n = 0;
        }
        else if ((n = n2) > 255) {
            n = 255;
        }
        if (n3 < 0) {
            n2 = 0;
        }
        else if ((n2 = n3) > 255) {
            n2 = 255;
        }
        if (n4 < 0) {
            n3 = 0;
        }
        else if ((n3 = n4) > 255) {
            n3 = 255;
        }
        if (n5 < 0) {
            n4 = 0;
        }
        else if ((n4 = n5) > 255) {
            n4 = 255;
        }
        return n4 | (n << 24 | n2 << 16 | n3 << 8);
    }
    
    public static void applyFloydSteinbergDithering(final BufferedImage bufferedImage, final Palette palette) throws ImageWriteException {
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            int n5;
            for (int j = 0; j < bufferedImage.getWidth(); j = n5) {
                final int rgb = bufferedImage.getRGB(j, i);
                final int entry = palette.getEntry(palette.getPaletteIndex(rgb));
                bufferedImage.setRGB(j, i, entry);
                final int n = (rgb >> 24 & 0xFF) - (entry >> 24 & 0xFF);
                final int n2 = (rgb >> 16 & 0xFF) - (entry >> 16 & 0xFF);
                final int n3 = (rgb >> 8 & 0xFF) - (entry >> 8 & 0xFF);
                final int n4 = (rgb & 0xFF) - (entry & 0xFF);
                n5 = j + 1;
                if (n5 < bufferedImage.getWidth()) {
                    bufferedImage.setRGB(n5, i, adjustPixel(bufferedImage.getRGB(n5, i), n, n2, n3, n4, 7));
                    final int n6 = i + 1;
                    if (n6 < bufferedImage.getHeight()) {
                        bufferedImage.setRGB(n5, n6, adjustPixel(bufferedImage.getRGB(n5, n6), n, n2, n3, n4, 1));
                    }
                }
                final int n7 = i + 1;
                if (n7 < bufferedImage.getHeight()) {
                    bufferedImage.setRGB(j, n7, adjustPixel(bufferedImage.getRGB(j, n7), n, n2, n3, n4, 5));
                    if (--j >= 0) {
                        bufferedImage.setRGB(j, n7, adjustPixel(bufferedImage.getRGB(j, n7), n, n2, n3, n4, 3));
                    }
                }
            }
        }
    }
}
