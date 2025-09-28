package org.apache.commons.imaging.formats.psd.dataparsers;

import org.apache.commons.imaging.color.ColorConversions;
import org.apache.commons.imaging.formats.psd.ImageContents;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DataParserCmyk extends DataParser {
    @Override // org.apache.commons.imaging.formats.psd.dataparsers.DataParser
    public int getBasicChannelsCount() {
        return 4;
    }

    @Override // org.apache.commons.imaging.formats.psd.dataparsers.DataParser
    protected int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents) {
        return ColorConversions.convertCMYKtoRGB(255 - (iArr[0][i2][i] & 255), 255 - (iArr[1][i2][i] & 255), 255 - (iArr[2][i2][i] & 255), 255 - (iArr[3][i2][i] & 255));
    }
}
