// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import org.apache.commons.imaging.formats.png.PngText;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;

public class PngChunkZtxt extends PngTextChunk
{
    public final String keyword;
    public final String text;
    
    public PngChunkZtxt(int null, int i, final int n, final byte[] bytes) throws ImageReadException, IOException {
        super(null, i, n, bytes);
        null = BinaryFunctions.findNull(bytes);
        if (null < 0) {
            throw new ImageReadException("PNG zTXt chunk keyword is unterminated.");
        }
        this.keyword = new String(bytes, 0, null, "ISO-8859-1");
        i = null + 1;
        null = i + 1;
        i = bytes[i];
        if (i != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PNG zTXt chunk has unexpected compression method: ");
            sb.append(i);
            throw new ImageReadException(sb.toString());
        }
        i = bytes.length - null;
        final byte[] buf = new byte[i];
        System.arraycopy(bytes, null, buf, 0, i);
        this.text = new String(BinaryFunctions.getStreamBytes(new InflaterInputStream(new ByteArrayInputStream(buf))), "ISO-8859-1");
    }
    
    @Override
    public PngText getContents() {
        return new PngText.Ztxt(this.keyword, this.text);
    }
    
    @Override
    public String getKeyword() {
        return this.keyword;
    }
    
    @Override
    public String getText() {
        return this.text;
    }
}
