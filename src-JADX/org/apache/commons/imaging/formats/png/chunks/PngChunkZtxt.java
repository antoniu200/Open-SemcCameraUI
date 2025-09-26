package org.apache.commons.imaging.formats.png.chunks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.png.PngText;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngChunkZtxt extends PngTextChunk {
    public final String keyword;
    public final String text;

    public PngChunkZtxt(int i, int i2, int i3, byte[] bArr) throws IOException, ImageReadException {
        super(i, i2, i3, bArr);
        int iFindNull = BinaryFunctions.findNull(bArr);
        if (iFindNull < 0) {
            throw new ImageReadException("PNG zTXt chunk keyword is unterminated.");
        }
        this.keyword = new String(bArr, 0, iFindNull, "ISO-8859-1");
        int i4 = iFindNull + 1;
        int i5 = i4 + 1;
        byte b = bArr[i4];
        if (b != 0) {
            throw new ImageReadException("PNG zTXt chunk has unexpected compression method: " + ((int) b));
        }
        int length = bArr.length - i5;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, i5, bArr2, 0, length);
        this.text = new String(BinaryFunctions.getStreamBytes(new InflaterInputStream(new ByteArrayInputStream(bArr2))), "ISO-8859-1");
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
        return new PngText.Ztxt(this.keyword, this.text);
    }
}
