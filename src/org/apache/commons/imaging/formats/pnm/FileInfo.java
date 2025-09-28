package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.common.ImageBuilder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class FileInfo {
    protected final int height;
    protected final boolean rawbits;
    protected final int width;

    public abstract int getBitDepth();

    public abstract ImageInfo.ColorType getColorType();

    public abstract ImageFormat getImageType();

    public abstract String getImageTypeDescription();

    public abstract String getMIMEType();

    public abstract int getNumComponents();

    public abstract int getRGB(InputStream inputStream) throws IOException;

    public abstract int getRGB(WhiteSpaceReader whiteSpaceReader) throws IOException;

    public abstract boolean hasAlpha();

    protected void newline() {
    }

    public FileInfo(int i, int i2, boolean z) {
        this.width = i;
        this.height = i2;
        this.rawbits = z;
    }

    protected static int readSample(InputStream inputStream, int i) throws IOException {
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            int i4 = inputStream.read();
            if (i4 < 0) {
                throw new IOException("PNM: Unexpected EOF");
            }
            i2 = (i2 << 8) | i4;
        }
        return i2;
    }

    protected static int scaleSample(int i, float f, int i2) throws IOException {
        if (i < 0) {
            throw new IOException("Negative pixel values are invalid in PNM files");
        }
        if (i > i2) {
            i = 0;
        }
        return (int) (((i * f) / i2) + 0.5f);
    }

    public void readImage(ImageBuilder imageBuilder, InputStream inputStream) throws IOException {
        if (!this.rawbits) {
            WhiteSpaceReader whiteSpaceReader = new WhiteSpaceReader(inputStream);
            for (int i = 0; i < this.height; i++) {
                for (int i2 = 0; i2 < this.width; i2++) {
                    imageBuilder.setRGB(i2, i, getRGB(whiteSpaceReader));
                }
                newline();
            }
            return;
        }
        for (int i3 = 0; i3 < this.height; i3++) {
            for (int i4 = 0; i4 < this.width; i4++) {
                imageBuilder.setRGB(i4, i3, getRGB(inputStream));
            }
            newline();
        }
    }
}
