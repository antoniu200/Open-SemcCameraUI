// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import org.apache.commons.imaging.formats.png.PngText;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;

public class PngChunkText extends PngTextChunk
{
    public final String keyword;
    public final String text;
    
    public PngChunkText(int length, int null, final int n, final byte[] array) throws ImageReadException, IOException {
        super(length, null, n, array);
        null = BinaryFunctions.findNull(array);
        if (null < 0) {
            throw new ImageReadException("PNG tEXt chunk keyword is not terminated.");
        }
        this.keyword = new String(array, 0, null, "ISO-8859-1");
        length = array.length;
        ++null;
        this.text = new String(array, null, length - null, "ISO-8859-1");
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Keyword: ");
            sb.append(this.keyword);
            out.println(sb.toString());
            final PrintStream out2 = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Text: ");
            sb2.append(this.text);
            out2.println(sb2.toString());
        }
    }
    
    @Override
    public PngText getContents() {
        return new PngText.Text(this.keyword, this.text);
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
