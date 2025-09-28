package org.apache.commons.imaging.formats.jpeg.decoder;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageReadException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class JpegInputStream {
    private int b;
    private int cnt;
    private final InputStream is;

    public JpegInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public int nextBit() throws IOException, ImageReadException {
        if (this.cnt == 0) {
            this.b = this.is.read();
            if (this.b < 0) {
                throw new ImageReadException("Premature End of File");
            }
            this.cnt = 8;
            if (this.b == 255) {
                int i = this.is.read();
                if (i < 0) {
                    throw new ImageReadException("Premature End of File");
                }
                if (i != 0) {
                    if (i == 220) {
                        throw new ImageReadException("DNL not yet supported");
                    }
                    throw new ImageReadException("Invalid marker found in entropy data");
                }
            }
        }
        int i2 = (this.b >> 7) & 1;
        this.cnt--;
        this.b <<= 1;
        return i2;
    }
}
