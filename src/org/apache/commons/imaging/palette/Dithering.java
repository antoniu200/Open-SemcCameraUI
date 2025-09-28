package org.apache.commons.imaging.palette;

import java.awt.image.BufferedImage;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class Dithering {
    private Dithering() {
    }

    public static void applyFloydSteinbergDithering(BufferedImage bufferedImage, Palette palette) throws ImageWriteException {
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            int i2 = 0;
            while (i2 < bufferedImage.getWidth()) {
                int rgb = bufferedImage.getRGB(i2, i);
                int entry = palette.getEntry(palette.getPaletteIndex(rgb));
                bufferedImage.setRGB(i2, i, entry);
                int i3 = ((rgb >> 24) & 255) - ((entry >> 24) & 255);
                int i4 = ((rgb >> 16) & 255) - ((entry >> 16) & 255);
                int i5 = ((rgb >> 8) & 255) - ((entry >> 8) & 255);
                int i6 = (rgb & 255) - (entry & 255);
                int i7 = i2 + 1;
                if (i7 < bufferedImage.getWidth()) {
                    bufferedImage.setRGB(i7, i, adjustPixel(bufferedImage.getRGB(i7, i), i3, i4, i5, i6, 7));
                    int i8 = i + 1;
                    if (i8 < bufferedImage.getHeight()) {
                        bufferedImage.setRGB(i7, i8, adjustPixel(bufferedImage.getRGB(i7, i8), i3, i4, i5, i6, 1));
                    }
                }
                int i9 = i + 1;
                if (i9 < bufferedImage.getHeight()) {
                    bufferedImage.setRGB(i2, i9, adjustPixel(bufferedImage.getRGB(i2, i9), i3, i4, i5, i6, 5));
                    int i10 = i2 - 1;
                    if (i10 >= 0) {
                        bufferedImage.setRGB(i10, i9, adjustPixel(bufferedImage.getRGB(i10, i9), i3, i4, i5, i6, 3));
                    }
                }
                i2 = i7;
            }
        }
    }

    private static int adjustPixel(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = ((i2 * i6) / 16) + ((i >> 24) & 255);
        int i8 = ((i3 * i6) / 16) + ((i >> 16) & 255);
        int i9 = ((i4 * i6) / 16) + ((i >> 8) & 255);
        int i10 = (i & 255) + ((i5 * i6) / 16);
        if (i7 < 0) {
            i7 = 0;
        } else if (i7 > 255) {
            i7 = 255;
        }
        if (i8 < 0) {
            i8 = 0;
        } else if (i8 > 255) {
            i8 = 255;
        }
        if (i9 < 0) {
            i9 = 0;
        } else if (i9 > 255) {
            i9 = 255;
        }
        if (i10 < 0) {
            i10 = 0;
        } else if (i10 > 255) {
            i10 = 255;
        }
        return i10 | (i7 << 24) | (i8 << 16) | (i9 << 8);
    }
}
