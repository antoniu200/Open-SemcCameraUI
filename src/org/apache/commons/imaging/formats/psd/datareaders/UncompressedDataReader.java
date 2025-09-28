package org.apache.commons.imaging.formats.psd.datareaders;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.mylzw.BitsToByteInputStream;
import org.apache.commons.imaging.common.mylzw.MyBitInputStream;
import org.apache.commons.imaging.formats.psd.ImageContents;
import org.apache.commons.imaging.formats.psd.PsdHeaderInfo;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParser;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class UncompressedDataReader implements DataReader {
    private DataParser dataParser;

    public UncompressedDataReader(DataParser dataParser) {
        this.dataParser = dataParser;
    }

    @Override // org.apache.commons.imaging.formats.psd.datareaders.DataReader
    public void readData(InputStream inputStream, BufferedImage bufferedImage, ImageContents imageContents, BinaryFileParser binaryFileParser) throws Throwable {
        BitsToByteInputStream bitsToByteInputStream;
        PsdHeaderInfo psdHeaderInfo = imageContents.header;
        int i = psdHeaderInfo.columns;
        int i2 = psdHeaderInfo.rows;
        binaryFileParser.setDebug(false);
        int basicChannelsCount = this.dataParser.getBasicChannelsCount();
        int i3 = psdHeaderInfo.depth;
        try {
            bitsToByteInputStream = new BitsToByteInputStream(new MyBitInputStream(inputStream, ByteOrder.BIG_ENDIAN), 8);
        } catch (Throwable th) {
            th = th;
            bitsToByteInputStream = null;
        }
        try {
            int[][][] iArr = (int[][][]) Array.newInstance((Class<?>) int.class, basicChannelsCount, i2, i);
            for (int i4 = 0; i4 < basicChannelsCount; i4++) {
                for (int i5 = 0; i5 < i2; i5++) {
                    for (int i6 = 0; i6 < i; i6++) {
                        iArr[i4][i5][i6] = (byte) bitsToByteInputStream.readBits(i3);
                    }
                }
            }
            this.dataParser.parseData(iArr, bufferedImage, imageContents);
            IoUtils.closeQuietly(true, bitsToByteInputStream);
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, bitsToByteInputStream);
            throw th;
        }
    }
}
