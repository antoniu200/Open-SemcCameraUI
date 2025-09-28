package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class PixelParserSimple extends PixelParser {
    public abstract int getNextRGB() throws IOException, ImageReadException;

    public abstract void newline() throws IOException, ImageReadException;

    public PixelParserSimple(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        super(bmpHeaderInfo, bArr, bArr2);
    }

    @Override // org.apache.commons.imaging.formats.bmp.PixelParser
    public void processImage(ImageBuilder imageBuilder) throws IOException, ImageReadException {
        for (int i = this.bhi.height - 1; i >= 0; i--) {
            for (int i2 = 0; i2 < this.bhi.width; i2++) {
                imageBuilder.setRGB(i2, i, getNextRGB());
            }
            newline();
        }
    }
}
