package org.apache.commons.imaging.formats.png.chunks;

import android.support.v4.view.ViewCompat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.png.GammaCorrection;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngChunkPlte extends PngChunk {
    private final int[] rgb;

    public PngChunkPlte(int i, int i2, int i3, byte[] bArr) throws IOException, ImageReadException {
        super(i, i2, i3, bArr);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        if (i % 3 != 0) {
            throw new ImageReadException("PLTE: wrong length: " + i);
        }
        int i4 = i / 3;
        this.rgb = new int[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            this.rgb[i5] = ((BinaryFunctions.readByte("red[" + i5 + "]", byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt") & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | ((BinaryFunctions.readByte("green[" + i5 + "]", byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt") & 255) << 8) | ((255 & BinaryFunctions.readByte("blue[" + i5 + "]", byteArrayInputStream, "Not a Valid Png File: PLTE Corrupt")) << 0);
        }
    }

    public int[] getRgb() {
        return this.rgb;
    }

    public int getRGB(int i) throws ImageReadException {
        if (i < 0 || i >= this.rgb.length) {
            throw new ImageReadException("PNG: unknown Palette reference: " + i);
        }
        return this.rgb[i];
    }

    public void correct(GammaCorrection gammaCorrection) {
        for (int i = 0; i < this.rgb.length; i++) {
            this.rgb[i] = gammaCorrection.correctARGB(this.rgb[i]);
        }
    }
}
