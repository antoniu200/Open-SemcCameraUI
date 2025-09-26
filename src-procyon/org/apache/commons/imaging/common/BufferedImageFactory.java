// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.awt.image.BufferedImage;

public interface BufferedImageFactory
{
    BufferedImage getColorBufferedImage(final int p0, final int p1, final boolean p2);
    
    BufferedImage getGrayscaleBufferedImage(final int p0, final int p1, final boolean p2);
}
