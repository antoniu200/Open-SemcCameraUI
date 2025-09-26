// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.awt.image.BufferedImage;

interface BmpWriter
{
    int getBitsPerPixel();
    
    byte[] getImageData(final BufferedImage p0);
    
    int getPaletteSize();
    
    void writePalette(final BinaryOutputStream p0) throws IOException;
}
