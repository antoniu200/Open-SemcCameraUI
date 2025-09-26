// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.rgbe;

import org.apache.commons.imaging.common.ByteConversions;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.util.regex.Matcher;
import org.apache.commons.imaging.ImageReadException;
import java.io.IOException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.GenericImageMetadata;
import java.io.InputStream;
import java.util.regex.Pattern;
import java.io.Closeable;

class RgbeInfo implements Closeable
{
    private static final byte[] HEADER;
    private static final Pattern RESOLUTION_STRING;
    private static final byte[] TWO_TWO;
    private int height;
    private final InputStream in;
    private GenericImageMetadata metadata;
    private int width;
    
    static {
        HEADER = new byte[] { 35, 63, 82, 65, 68, 73, 65, 78, 67, 69 };
        RESOLUTION_STRING = Pattern.compile("-Y (\\d+) \\+X (\\d+)");
        TWO_TWO = new byte[] { 2, 2 };
    }
    
    RgbeInfo(final ByteSource byteSource) throws IOException {
        this.width = -1;
        this.height = -1;
        this.in = byteSource.getInputStream();
    }
    
    private static void decompress(final InputStream inputStream, final byte[] array) throws IOException {
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final int read = inputStream.read();
            if (read > 128) {
                final int read2 = inputStream.read();
                for (int j = 0; j < (read & 0x7F); ++j, ++i) {
                    array[i] = (byte)read2;
                }
            }
            else {
                for (int k = 0; k < read; ++k, ++i) {
                    array[i] = (byte)inputStream.read();
                }
            }
        }
    }
    
    private void readDimensions() throws IOException, ImageReadException {
        this.getMetadata();
        final String nextLine = new InfoHeaderReader(this.in).readNextLine();
        final Matcher matcher = RgbeInfo.RESOLUTION_STRING.matcher(nextLine);
        if (!matcher.matches()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid HDR resolution string. Only \"-Y N +X M\" is supported. Found \"");
            sb.append(nextLine);
            sb.append("\"");
            throw new ImageReadException(sb.toString());
        }
        this.height = Integer.parseInt(matcher.group(1));
        this.width = Integer.parseInt(matcher.group(2));
    }
    
    private void readMetadata() throws IOException, ImageReadException {
        BinaryFunctions.readAndVerifyBytes(this.in, RgbeInfo.HEADER, "Not a valid HDR: Incorrect Header");
        final InfoHeaderReader infoHeaderReader = new InfoHeaderReader(this.in);
        if (infoHeaderReader.readNextLine().length() != 0) {
            throw new ImageReadException("Not a valid HDR: Incorrect Header");
        }
        this.metadata = new GenericImageMetadata();
        for (String s = infoHeaderReader.readNextLine(); s.length() != 0; s = infoHeaderReader.readNextLine()) {
            final int index = s.indexOf(61);
            if (index > 0) {
                final String substring = s.substring(0, index);
                final String substring2 = s.substring(index + 1);
                if ("FORMAT".equals(substring2) && !"32-bit_rle_rgbe".equals(substring2)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Only 32-bit_rle_rgbe images are supported, trying to read ");
                    sb.append(substring2);
                    throw new ImageReadException(sb.toString());
                }
                this.metadata.add(substring, substring2);
            }
            else {
                this.metadata.add("<command>", s);
            }
        }
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
    
    int getHeight() throws IOException, ImageReadException {
        if (-1 == this.height) {
            this.readDimensions();
        }
        return this.height;
    }
    
    ImageMetadata getMetadata() throws IOException, ImageReadException {
        if (this.metadata == null) {
            this.readMetadata();
        }
        return this.metadata;
    }
    
    public float[][] getPixelData() throws IOException, ImageReadException {
        final int height = this.getHeight();
        final int width = this.getWidth();
        if (width >= 32768) {
            throw new ImageReadException("Scan lines must be less than 32768 bytes long");
        }
        final byte[] bytes = ByteConversions.toBytes((short)width, ByteOrder.BIG_ENDIAN);
        final byte[] array = new byte[width * 4];
        final float[][] array2 = new float[3][width * height];
        for (int i = 0; i < height; ++i) {
            final InputStream in = this.in;
            final byte[] two_TWO = RgbeInfo.TWO_TWO;
            final StringBuilder sb = new StringBuilder();
            sb.append("Scan line ");
            sb.append(i);
            sb.append(" expected to start with 0x2 0x2");
            BinaryFunctions.readAndVerifyBytes(in, two_TWO, sb.toString());
            final InputStream in2 = this.in;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Scan line ");
            sb2.append(i);
            sb2.append(" length expected");
            BinaryFunctions.readAndVerifyBytes(in2, bytes, sb2.toString());
            decompress(this.in, array);
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < width; ++k) {
                    final int n = array[k + 3 * width] & 0xFF;
                    final int n2 = i * width + k;
                    if (n == 0) {
                        array2[j][n2] = 0.0f;
                    }
                    else {
                        array2[j][n2] = ((array[k + j * width] & 0xFF) + 0.5f) * (float)Math.pow(2.0, n - 136);
                    }
                }
            }
        }
        return array2;
    }
    
    int getWidth() throws IOException, ImageReadException {
        if (-1 == this.width) {
            this.readDimensions();
        }
        return this.width;
    }
}
