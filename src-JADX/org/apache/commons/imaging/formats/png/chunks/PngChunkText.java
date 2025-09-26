package org.apache.commons.imaging.formats.png.chunks;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.png.PngText;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngChunkText extends PngTextChunk {
    public final String keyword;
    public final String text;

    public PngChunkText(int i, int i2, int i3, byte[] bArr) throws IOException, ImageReadException {
        super(i, i2, i3, bArr);
        int iFindNull = BinaryFunctions.findNull(bArr);
        if (iFindNull < 0) {
            throw new ImageReadException("PNG tEXt chunk keyword is not terminated.");
        }
        this.keyword = new String(bArr, 0, iFindNull, "ISO-8859-1");
        int i4 = iFindNull + 1;
        this.text = new String(bArr, i4, bArr.length - i4, "ISO-8859-1");
        if (getDebug()) {
            System.out.println("Keyword: " + this.keyword);
            System.out.println("Text: " + this.text);
        }
    }

    @Override // org.apache.commons.imaging.formats.png.chunks.PngTextChunk
    public String getKeyword() {
        return this.keyword;
    }

    @Override // org.apache.commons.imaging.formats.png.chunks.PngTextChunk
    public String getText() {
        return this.text;
    }

    @Override // org.apache.commons.imaging.formats.png.chunks.PngTextChunk
    public PngText getContents() {
        return new PngText.Text(this.keyword, this.text);
    }
}
