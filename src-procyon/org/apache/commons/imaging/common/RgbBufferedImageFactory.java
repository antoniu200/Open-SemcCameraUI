// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.awt.image.BufferedImage;

public class RgbBufferedImageFactory implements BufferedImageFactory
{
    @Override
    public BufferedImage getColorBufferedImage(final int n, final int n2, final boolean b) {
        if (b) {
            return new BufferedImage(n, n2, 2);
        }
        return new BufferedImage(n, n2, 1);
    }
    
    @Override
    public BufferedImage getGrayscaleBufferedImage(final int n, final int n2, final boolean b) {
        return this.getColorBufferedImage(n, n2, b);
    }
}
