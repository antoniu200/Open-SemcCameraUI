// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.decoder;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.io.InputStream;

class JpegInputStream
{
    private int b;
    private int cnt;
    private final InputStream is;
    
    public JpegInputStream(final InputStream is) {
        this.is = is;
    }
    
    public int nextBit() throws IOException, ImageReadException {
        if (this.cnt == 0) {
            this.b = this.is.read();
            if (this.b < 0) {
                throw new ImageReadException("Premature End of File");
            }
            this.cnt = 8;
            if (this.b == 255) {
                final int read = this.is.read();
                if (read < 0) {
                    throw new ImageReadException("Premature End of File");
                }
                if (read != 0) {
                    if (read == 220) {
                        throw new ImageReadException("DNL not yet supported");
                    }
                    throw new ImageReadException("Invalid marker found in entropy data");
                }
            }
        }
        final int b = this.b;
        --this.cnt;
        this.b <<= 1;
        return b >> 7 & 0x1;
    }
}
