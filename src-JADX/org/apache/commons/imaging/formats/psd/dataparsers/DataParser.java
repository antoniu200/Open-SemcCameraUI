package org.apache.commons.imaging.formats.psd.dataparsers;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import org.apache.commons.imaging.formats.psd.ImageContents;
import org.apache.commons.imaging.formats.psd.PsdHeaderInfo;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class DataParser {
    public abstract int getBasicChannelsCount();

    protected abstract int getRGB(int[][][] iArr, int i, int i2, ImageContents imageContents);

    public final void parseData(int[][][] iArr, BufferedImage bufferedImage, ImageContents imageContents) {
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        PsdHeaderInfo psdHeaderInfo = imageContents.header;
        int i = psdHeaderInfo.columns;
        int i2 = psdHeaderInfo.rows;
        for (int i3 = 0; i3 < i2; i3++) {
            for (int i4 = 0; i4 < i; i4++) {
                dataBuffer.setElem((i3 * i) + i4, getRGB(iArr, i4, i3, imageContents));
            }
        }
    }
}
