package org.apache.commons.imaging.formats.bmp;

import android.support.v4.view.ViewCompat;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.io.IOException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PixelParserRgb extends PixelParserSimple {
    private int bytecount;
    private int cachedBitCount;
    private int cachedByte;

    public PixelParserRgb(BmpHeaderInfo bmpHeaderInfo, byte[] bArr, byte[] bArr2) {
        super(bmpHeaderInfo, bArr, bArr2);
    }

    @Override // org.apache.commons.imaging.formats.bmp.PixelParserSimple
    public int getNextRGB() throws IOException, ImageReadException {
        if (this.bhi.bitsPerPixel == 1 || this.bhi.bitsPerPixel == 4) {
            if (this.cachedBitCount < this.bhi.bitsPerPixel) {
                if (this.cachedBitCount != 0) {
                    throw new ImageReadException("Unexpected leftover bits: " + this.cachedBitCount + SharedPrefsTranslator.CONNECTOR_SLASH + this.bhi.bitsPerPixel);
                }
                this.cachedBitCount += 8;
                this.cachedByte = this.imageData[this.bytecount] & 255;
                this.bytecount++;
            }
            int i = ((1 << this.bhi.bitsPerPixel) - 1) & (this.cachedByte >> (8 - this.bhi.bitsPerPixel));
            this.cachedByte = (this.cachedByte << this.bhi.bitsPerPixel) & 255;
            this.cachedBitCount -= this.bhi.bitsPerPixel;
            return getColorTableRGB(i);
        }
        if (this.bhi.bitsPerPixel == 8) {
            int colorTableRGB = getColorTableRGB(this.imageData[this.bytecount + 0] & 255);
            this.bytecount++;
            return colorTableRGB;
        }
        if (this.bhi.bitsPerPixel == 16) {
            int i2 = BinaryFunctions.read2Bytes("Pixel", this.is, "BMP Image Data", ByteOrder.LITTLE_ENDIAN);
            int i3 = ((((i2 >> 10) & 31) << 3) << 16) | ViewCompat.MEASURED_STATE_MASK | ((((i2 >> 5) & 31) << 3) << 8) | ((((i2 >> 0) & 31) << 3) << 0);
            this.bytecount += 2;
            return i3;
        }
        if (this.bhi.bitsPerPixel == 24) {
            int i4 = ((this.imageData[this.bytecount + 0] & 255) << 0) | ((this.imageData[this.bytecount + 1] & 255) << 8) | ((255 & this.imageData[this.bytecount + 2]) << 16) | ViewCompat.MEASURED_STATE_MASK;
            this.bytecount += 3;
            return i4;
        }
        if (this.bhi.bitsPerPixel == 32) {
            int i5 = ((this.imageData[this.bytecount + 0] & 255) << 0) | ((this.imageData[this.bytecount + 1] & 255) << 8) | ((255 & this.imageData[this.bytecount + 2]) << 16) | ViewCompat.MEASURED_STATE_MASK;
            this.bytecount += 4;
            return i5;
        }
        throw new ImageReadException("Unknown BitsPerPixel: " + this.bhi.bitsPerPixel);
    }

    @Override // org.apache.commons.imaging.formats.bmp.PixelParserSimple
    public void newline() throws IOException, ImageReadException {
        this.cachedBitCount = 0;
        while (this.bytecount % 4 != 0) {
            BinaryFunctions.readByte("Pixel", this.is, "BMP Image Data");
            this.bytecount++;
        }
    }
}
