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
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.formats.psd.ImageContents;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParser;

public class UncompressedDataReader implements DataReader
{
    private DataParser dataParser;
    
    public UncompressedDataReader(final DataParser dataParser) {
        this.dataParser = dataParser;
    }
    
    @Override
    public void readData(final InputStream inputStream, final BufferedImage bufferedImage, final ImageContents imageContents, final BinaryFileParser binaryFileParser) throws ImageReadException, IOException {
        final PsdHeaderInfo header = imageContents.header;
        final int columns = header.columns;
        final int rows = header.rows;
        binaryFileParser.setDebug(false);
        final int basicChannelsCount = this.dataParser.getBasicChannelsCount();
        final int depth = header.depth;
        final MyBitInputStream myBitInputStream = new MyBitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        Closeable closeable;
        try {
            final BitsToByteInputStream bitsToByteInputStream = new BitsToByteInputStream(myBitInputStream, 8);
            try {
                final int[][][] array = new int[basicChannelsCount][rows][columns];
                for (int i = 0; i < basicChannelsCount; ++i) {
                    for (int j = 0; j < rows; ++j) {
                        for (int k = 0; k < columns; ++k) {
                            array[i][j][k] = (byte)bitsToByteInputStream.readBits(depth);
                        }
                    }
                }
                this.dataParser.parseData(array, bufferedImage, imageContents);
                IoUtils.closeQuietly(true, bitsToByteInputStream);
                return;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
}
