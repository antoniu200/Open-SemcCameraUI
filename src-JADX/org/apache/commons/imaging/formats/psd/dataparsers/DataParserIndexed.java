package org.apache.commons.imaging.formats.psd.dataparsers;

import android.support.v4.view.ViewCompat;
import org.apache.commons.imaging.formats.psd.ImageContents;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DataParserIndexed extends DataParser {
    private final int[] colorTable = new int[256];

    @Override // org.apache.commons.imaging.formats.psd.dataparsers.DataParser
    public int getBasicChannelsCount() {
        return 1;
    }

    public DataParserIndexed(byte[] bArr) {
        for (int i = 0; i < 256; i++) {
            this.colorTable[i] = (((bArr[0 + i] & 255) & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | (((bArr[256 + i] & 255) & 255) << 8) | ((255 & (bArr[512 + i] & 255)) << 0);
        }
    }

    @Override // org.apache.commons.imaging.formats.psd.dataparsers.DataParser
    protected int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents) {
        return this.colorTable[iArr[0][i2][i] & 255];
    }
}
