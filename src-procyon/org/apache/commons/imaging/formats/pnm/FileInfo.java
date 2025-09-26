// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import java.io.IOException;
import java.io.InputStream;

abstract class FileInfo
{
    protected final int height;
    protected final boolean rawbits;
    protected final int width;
    
    public FileInfo(final int width, final int height, final boolean rawbits) {
        this.width = width;
        this.height = height;
        this.rawbits = rawbits;
    }
    
    protected static int readSample(final InputStream inputStream, final int n) throws IOException {
        int i = 0;
        int n2 = 0;
        while (i < n) {
            final int read = inputStream.read();
            if (read < 0) {
                throw new IOException("PNM: Unexpected EOF");
            }
            n2 = (n2 << 8 | read);
            ++i;
        }
        return n2;
    }
    
    protected static int scaleSample(final int n, final float n2, final int n3) throws IOException {
        if (n < 0) {
            throw new IOException("Negative pixel values are invalid in PNM files");
        }
        int n4;
        if ((n4 = n) > n3) {
            n4 = 0;
        }
        return (int)(n4 * n2 / n3 + 0.5f);
    }
    
    public abstract int getBitDepth();
    
    public abstract ImageInfo.ColorType getColorType();
    
    public abstract ImageFormat getImageType();
    
    public abstract String getImageTypeDescription();
    
    public abstract String getMIMEType();
    
    public abstract int getNumComponents();
    
    public abstract int getRGB(final InputStream p0) throws IOException;
    
    public abstract int getRGB(final WhiteSpaceReader p0) throws IOException;
    
    public abstract boolean hasAlpha();
    
    protected void newline() {
    }
    
    public void readImage(final ImageBuilder imageBuilder, final InputStream inputStream) throws IOException {
        if (!this.rawbits) {
            final WhiteSpaceReader whiteSpaceReader = new WhiteSpaceReader(inputStream);
            for (int i = 0; i < this.height; ++i) {
                for (int j = 0; j < this.width; ++j) {
                    imageBuilder.setRGB(j, i, this.getRGB(whiteSpaceReader));
                }
                this.newline();
            }
        }
        else {
            for (int k = 0; k < this.height; ++k) {
                for (int l = 0; l < this.width; ++l) {
                    imageBuilder.setRGB(l, k, this.getRGB(inputStream));
                }
                this.newline();
            }
        }
    }
}
