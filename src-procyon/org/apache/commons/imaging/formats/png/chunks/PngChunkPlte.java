// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import org.apache.commons.imaging.formats.png.GammaCorrection;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.ImageReadException;
import java.io.ByteArrayInputStream;

public class PngChunkPlte extends PngChunk
{
    private final int[] rgb;
    
    public PngChunkPlte(int i, int n, int byte1, final byte[] buf) throws ImageReadException, IOException {
        super(i, n, byte1, buf);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
        if (i % 3 != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PLTE: wrong length: ");
            sb.append(i);
            throw new ImageReadException(sb.toString());
        }
        n = i / 3;
        this.rgb = new int[n];
        StringBuilder sb2;
        byte byte2;
        StringBuilder sb3;
        StringBuilder sb4;
        for (i = 0; i < n; ++i) {
            sb2 = new StringBuilder();
            sb2.append("red[");
            sb2.append(i);
            sb2.append("]");
            byte2 = BinaryFunctions.readByte(sb2.toString(), byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt");
            sb3 = new StringBuilder();
            sb3.append("green[");
            sb3.append(i);
            sb3.append("]");
            byte1 = BinaryFunctions.readByte(sb3.toString(), byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt");
            sb4 = new StringBuilder();
            sb4.append("blue[");
            sb4.append(i);
            sb4.append("]");
            this.rgb[i] = ((byte2 & 0xFF) << 16 | 0xFF000000 | (byte1 & 0xFF) << 8 | (0xFF & BinaryFunctions.readByte(sb4.toString(), byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt")) << 0);
        }
    }
    
    public void correct(final GammaCorrection gammaCorrection) {
        for (int i = 0; i < this.rgb.length; ++i) {
            this.rgb[i] = gammaCorrection.correctARGB(this.rgb[i]);
        }
    }
    
    public int getRGB(final int i) throws ImageReadException {
        if (i >= 0 && i < this.rgb.length) {
            return this.rgb[i];
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("PNG: unknown Palette reference: ");
        sb.append(i);
        throw new ImageReadException(sb.toString());
    }
    
    public int[] getRgb() {
        return this.rgb;
    }
}
