// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import org.apache.commons.imaging.common.ImageBuilder;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

abstract class PixelParserSimple extends PixelParser
{
    public PixelParserSimple(final BmpHeaderInfo bmpHeaderInfo, final byte[] array, final byte[] array2) {
        super(bmpHeaderInfo, array, array2);
    }
    
    public abstract int getNextRGB() throws ImageReadException, IOException;
    
    public abstract void newline() throws ImageReadException, IOException;
    
    @Override
    public void processImage(final ImageBuilder imageBuilder) throws ImageReadException, IOException {
        for (int i = this.bhi.height - 1; i >= 0; --i) {
            for (int j = 0; j < this.bhi.width; ++j) {
                imageBuilder.setRGB(j, i, this.getNextRGB());
            }
            this.newline();
        }
    }
}
