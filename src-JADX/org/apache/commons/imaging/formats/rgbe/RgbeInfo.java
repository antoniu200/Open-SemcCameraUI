package org.apache.commons.imaging.formats.rgbe;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class RgbeInfo implements Closeable {
    private static final byte[] HEADER = {35, 63, 82, 65, 68, 73, 65, 78, 67, 69};
    private static final Pattern RESOLUTION_STRING = Pattern.compile("-Y (\\d+) \\+X (\\d+)");
    private static final byte[] TWO_TWO = {2, 2};
    private final InputStream in;
    private GenericImageMetadata metadata;
    private int width = -1;
    private int height = -1;

    RgbeInfo(ByteSource byteSource) throws IOException {
        this.in = byteSource.getInputStream();
    }

    ImageMetadata getMetadata() throws IOException, ImageReadException {
        if (this.metadata == null) {
            readMetadata();
        }
        return this.metadata;
    }

    int getWidth() throws IOException, ImageReadException {
        if (-1 == this.width) {
            readDimensions();
        }
        return this.width;
    }

    int getHeight() throws IOException, ImageReadException {
        if (-1 == this.height) {
            readDimensions();
        }
        return this.height;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
    }

    private void readDimensions() throws IOException, ImageReadException {
        getMetadata();
        String nextLine = new InfoHeaderReader(this.in).readNextLine();
        Matcher matcher = RESOLUTION_STRING.matcher(nextLine);
        if (!matcher.matches()) {
            throw new ImageReadException("Invalid HDR resolution string. Only \"-Y N +X M\" is supported. Found \"" + nextLine + "\"");
        }
        this.height = Integer.parseInt(matcher.group(1));
        this.width = Integer.parseInt(matcher.group(2));
    }

    private void readMetadata() throws IOException, ImageReadException {
        BinaryFunctions.readAndVerifyBytes(this.in, HEADER, "Not a valid HDR: Incorrect Header");
        InfoHeaderReader infoHeaderReader = new InfoHeaderReader(this.in);
        if (infoHeaderReader.readNextLine().length() != 0) {
            throw new ImageReadException("Not a valid HDR: Incorrect Header");
        }
        this.metadata = new GenericImageMetadata();
        for (String nextLine = infoHeaderReader.readNextLine(); nextLine.length() != 0; nextLine = infoHeaderReader.readNextLine()) {
            int iIndexOf = nextLine.indexOf(61);
            if (iIndexOf > 0) {
                String strSubstring = nextLine.substring(0, iIndexOf);
                String strSubstring2 = nextLine.substring(iIndexOf + 1);
                if (ImagingConstants.PARAM_KEY_FORMAT.equals(strSubstring2) && !"32-bit_rle_rgbe".equals(strSubstring2)) {
                    throw new ImageReadException("Only 32-bit_rle_rgbe images are supported, trying to read " + strSubstring2);
                }
                this.metadata.add(strSubstring, strSubstring2);
            } else {
                this.metadata.add("<command>", nextLine);
            }
        }
    }

    public float[][] getPixelData() throws IOException, ImageReadException {
        int i;
        RgbeInfo rgbeInfo = this;
        int height = getHeight();
        int width = getWidth();
        if (width >= 32768) {
            throw new ImageReadException("Scan lines must be less than 32768 bytes long");
        }
        byte[] bytes = ByteConversions.toBytes((short) width, ByteOrder.BIG_ENDIAN);
        byte[] bArr = new byte[width * 4];
        int i2 = 3;
        float[][] fArr = (float[][]) Array.newInstance((Class<?>) float.class, 3, width * height);
        int i3 = 0;
        while (i3 < height) {
            BinaryFunctions.readAndVerifyBytes(rgbeInfo.in, TWO_TWO, "Scan line " + i3 + " expected to start with 0x2 0x2");
            BinaryFunctions.readAndVerifyBytes(rgbeInfo.in, bytes, "Scan line " + i3 + " length expected");
            decompress(rgbeInfo.in, bArr);
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i4 * width;
                int i6 = i2 * width;
                int i7 = 0;
                while (i7 < width) {
                    int i8 = (i3 * width) + i7;
                    if ((bArr[i7 + i6] & 255) == 0) {
                        fArr[i4][i8] = 0.0f;
                        i = height;
                    } else {
                        i = height;
                        fArr[i4][i8] = ((bArr[i7 + i5] & 255) + 0.5f) * ((float) Math.pow(2.0d, r13 - 136));
                    }
                    i7++;
                    height = i;
                }
                i4++;
                i2 = 3;
            }
            i3++;
            rgbeInfo = this;
            i2 = 3;
        }
        return fArr;
    }

    private static void decompress(InputStream inputStream, byte[] bArr) throws IOException {
        int length = bArr.length;
        int i = 0;
        while (i < length) {
            int i2 = inputStream.read();
            if (i2 > 128) {
                int i3 = inputStream.read();
                int i4 = i;
                int i5 = 0;
                while (i5 < (i2 & 127)) {
                    bArr[i4] = (byte) i3;
                    i5++;
                    i4++;
                }
                i = i4;
            } else {
                int i6 = i;
                int i7 = 0;
                while (i7 < i2) {
                    bArr[i6] = (byte) inputStream.read();
                    i7++;
                    i6++;
                }
                i = i6;
            }
        }
    }
}
