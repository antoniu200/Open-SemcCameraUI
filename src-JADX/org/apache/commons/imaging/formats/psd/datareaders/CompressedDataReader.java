package org.apache.commons.imaging.formats.psd.datareaders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.PackBits;
import org.apache.commons.imaging.common.mylzw.BitsToByteInputStream;
import org.apache.commons.imaging.common.mylzw.MyBitInputStream;
import org.apache.commons.imaging.formats.psd.ImageContents;
import org.apache.commons.imaging.formats.psd.PsdHeaderInfo;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParser;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CompressedDataReader implements DataReader {
    private DataParser dataParser;

    public CompressedDataReader(DataParser dataParser) {
        this.dataParser = dataParser;
    }

    @Override // org.apache.commons.imaging.formats.psd.datareaders.DataReader
    public void readData(InputStream inputStream, BufferedImage bufferedImage, ImageContents imageContents, BinaryFileParser binaryFileParser) throws Throwable {
        Throwable th;
        BitsToByteInputStream bitsToByteInputStream;
        PsdHeaderInfo psdHeaderInfo = imageContents.header;
        int i = psdHeaderInfo.columns;
        int i2 = psdHeaderInfo.rows;
        int i3 = psdHeaderInfo.channels * i2;
        int[] iArr = new int[i3];
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            iArr[i5] = BinaryFunctions.read2Bytes("scanline_bytecount[" + i5 + "]", inputStream, "PSD: bad Image Data", binaryFileParser.getByteOrder());
        }
        binaryFileParser.setDebug(false);
        int i6 = psdHeaderInfo.depth;
        int basicChannelsCount = this.dataParser.getBasicChannelsCount();
        int[][][] iArr2 = (int[][][]) Array.newInstance((Class<?>) int[].class, basicChannelsCount, i2);
        for (int i7 = 0; i7 < basicChannelsCount; i7++) {
            int i8 = i4;
            while (i8 < i2) {
                try {
                    bitsToByteInputStream = new BitsToByteInputStream(new MyBitInputStream(new ByteArrayInputStream(new PackBits().decompress(BinaryFunctions.readBytes("scanline", inputStream, iArr[(i7 * i2) + i8], "PSD: Missing Image Data"), i)), ByteOrder.BIG_ENDIAN), 8);
                } catch (Throwable th2) {
                    th = th2;
                    bitsToByteInputStream = null;
                }
                try {
                    iArr2[i7][i8] = bitsToByteInputStream.readBitsArray(i6, i);
                    IoUtils.closeQuietly(true, bitsToByteInputStream);
                    i8++;
                    i4 = 0;
                } catch (Throwable th3) {
                    th = th3;
                    IoUtils.closeQuietly(false, bitsToByteInputStream);
                    throw th;
                }
            }
        }
        this.dataParser.parseData(iArr2, bufferedImage, imageContents);
    }
}
