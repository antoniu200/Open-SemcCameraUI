package org.apache.commons.imaging.common;

import java.awt.image.BufferedImage;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface BufferedImageFactory {
    BufferedImage getColorBufferedImage(int i, int i2, boolean z);

    BufferedImage getGrayscaleBufferedImage(int i, int i2, boolean z);
}
