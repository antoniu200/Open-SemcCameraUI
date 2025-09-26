// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.datareaders;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.psd.PsdHeaderInfo;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.mylzw.BitsToByteInputStream;
import org.apache.commons.imaging.common.mylzw.MyBitInputStream;
import java.nio.ByteOrder;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.PackBits;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.formats.psd.ImageContents;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParser;

public class CompressedDataReader implements DataReader
{
    private DataParser dataParser;
    
    public CompressedDataReader(final DataParser dataParser) {
        this.dataParser = dataParser;
    }
    
    @Override
    public void readData(final InputStream inputStream, BufferedImage bufferedImage, final ImageContents imageContents, BinaryFileParser binaryFileParser) throws ImageReadException, IOException {
        final PsdHeaderInfo header = imageContents.header;
        final int columns = header.columns;
        final int rows = header.rows;
        final int n = header.channels * rows;
        final int[] array = new int[n];
        final int n2 = 0;
        for (int i = 0; i < n; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("scanline_bytecount[");
            sb.append(i);
            sb.append("]");
            array[i] = BinaryFunctions.read2Bytes(sb.toString(), inputStream, "PSD: bad Image Data", binaryFileParser.getByteOrder());
        }
        binaryFileParser.setDebug(false);
        final int depth = header.depth;
        final int basicChannelsCount = this.dataParser.getBasicChannelsCount();
        final int[][][] array2 = new int[basicChannelsCount][rows][];
        int j = 0;
        int n3 = n2;
        while (j < basicChannelsCount) {
            int k = n3;
            while (k < rows) {
                final MyBitInputStream myBitInputStream = new MyBitInputStream(new ByteArrayInputStream(new PackBits().decompress(BinaryFunctions.readBytes("scanline", inputStream, array[j * rows + k], "PSD: Missing Image Data"), columns)), ByteOrder.BIG_ENDIAN);
                try {
                    binaryFileParser = (BinaryFileParser)new BitsToByteInputStream(myBitInputStream, 8);
                    try {
                        array2[j][k] = ((BitsToByteInputStream)binaryFileParser).readBitsArray(depth, columns);
                        IoUtils.closeQuietly(true, (Closeable)binaryFileParser);
                        ++k;
                        n3 = 0;
                    }
                    finally {}
                }
                finally {
                    bufferedImage = null;
                }
                IoUtils.closeQuietly(false, (Closeable)bufferedImage);
            }
            ++j;
        }
        this.dataParser.parseData(array2, bufferedImage, imageContents);
    }
}
