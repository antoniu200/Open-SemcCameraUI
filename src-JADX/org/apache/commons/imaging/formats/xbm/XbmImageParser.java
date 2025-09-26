package org.apache.commons.imaging.formats.xbm;

import android.support.v4.view.ViewCompat;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BasicCParser;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class XbmImageParser extends ImageParser {
    private static final String DEFAULT_EXTENSION = ".xbm";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};

    @Override // org.apache.commons.imaging.ImageParser
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "X BitMap";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.XBM};
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        XbmHeader xbmHeader = readXbmHeader(byteSource);
        return new ImageInfo("XBM", 1, new ArrayList(), ImageFormats.XBM, "X BitMap", xbmHeader.height, "image/x-xbitmap", 1, 0, 0.0f, 0, 0.0f, xbmHeader.width, false, false, false, ImageInfo.ColorType.BW, ImageInfo.CompressionAlgorithm.NONE);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        XbmHeader xbmHeader = readXbmHeader(byteSource);
        return new Dimension(xbmHeader.width, xbmHeader.height);
    }

    private static class XbmHeader {
        int height;
        int width;
        int xHot;
        int yHot;

        public XbmHeader(int i, int i2, int i3, int i4) {
            this.xHot = -1;
            this.yHot = -1;
            this.width = i;
            this.height = i2;
            this.xHot = i3;
            this.yHot = i4;
        }

        public void dump(PrintWriter printWriter) {
            printWriter.println("XbmHeader");
            printWriter.println("Width: " + this.width);
            printWriter.println("Height: " + this.height);
            if (this.xHot == -1 || this.yHot == -1) {
                return;
            }
            printWriter.println("X hot: " + this.xHot);
            printWriter.println("Y hot: " + this.yHot);
        }
    }

    private static class XbmParseResult {
        BasicCParser cParser;
        XbmHeader xbmHeader;

        private XbmParseResult() {
        }
    }

    private XbmHeader readXbmHeader(ByteSource byteSource) throws IOException, ImageReadException {
        return parseXbmHeader(byteSource).xbmHeader;
    }

    private XbmParseResult parseXbmHeader(ByteSource byteSource) throws Throwable {
        Throwable th;
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
        }
        try {
            HashMap map = new HashMap();
            ByteArrayOutputStream byteArrayOutputStreamPreprocess = BasicCParser.preprocess(inputStream, null, map);
            int i = -1;
            int i2 = -1;
            int i3 = -1;
            int i4 = -1;
            for (Map.Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                if (str.endsWith("_width")) {
                    i = Integer.parseInt((String) entry.getValue());
                } else if (str.endsWith("_height")) {
                    i2 = Integer.parseInt((String) entry.getValue());
                } else if (str.endsWith("_x_hot")) {
                    i3 = Integer.parseInt((String) entry.getValue());
                } else if (str.endsWith("_y_hot")) {
                    i4 = Integer.parseInt((String) entry.getValue());
                }
            }
            if (i == -1) {
                throw new ImageReadException("width not found");
            }
            if (i2 == -1) {
                throw new ImageReadException("height not found");
            }
            XbmParseResult xbmParseResult = new XbmParseResult();
            xbmParseResult.cParser = new BasicCParser(new ByteArrayInputStream(byteArrayOutputStreamPreprocess.toByteArray()));
            xbmParseResult.xbmHeader = new XbmHeader(i, i2, i3, i4);
            IoUtils.closeQuietly(true, inputStream);
            return xbmParseResult;
        } catch (Throwable th3) {
            th = th3;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    private BufferedImage readXbmImage(XbmHeader xbmHeader, BasicCParser basicCParser) throws IOException, NumberFormatException, ImageReadException {
        if (!"static".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XBM file failed, no 'static' token");
        }
        String strNextToken = basicCParser.nextToken();
        if (strNextToken == null) {
            throw new ImageReadException("Parsing XBM file failed, no 'unsigned' or 'char' token");
        }
        if ("unsigned".equals(strNextToken)) {
            strNextToken = basicCParser.nextToken();
        }
        if (!"char".equals(strNextToken)) {
            throw new ImageReadException("Parsing XBM file failed, no 'char' token");
        }
        String strNextToken2 = basicCParser.nextToken();
        if (strNextToken2 == null) {
            throw new ImageReadException("Parsing XBM file failed, no variable name");
        }
        if (strNextToken2.charAt(0) != '_' && !Character.isLetter(strNextToken2.charAt(0))) {
            throw new ImageReadException("Parsing XBM file failed, variable name doesn't start with letter or underscore");
        }
        for (int i = 0; i < strNextToken2.length(); i++) {
            char cCharAt = strNextToken2.charAt(i);
            if (!Character.isLetterOrDigit(cCharAt) && cCharAt != '_') {
                throw new ImageReadException("Parsing XBM file failed, variable name contains non-letter non-digit non-underscore");
            }
        }
        if (!"[".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XBM file failed, no '[' token");
        }
        if (!"]".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XBM file failed, no ']' token");
        }
        if (!"=".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XBM file failed, no '=' token");
        }
        if (!"{".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XBM file failed, no '{' token");
        }
        byte[] bArr = new byte[((xbmHeader.width + 7) / 8) * xbmHeader.height];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            String strNextToken3 = basicCParser.nextToken();
            if (strNextToken3 == null || !strNextToken3.startsWith("0x")) {
                throw new ImageReadException("Parsing XBM file failed, hex value missing");
            }
            if (strNextToken3.length() > 4) {
                throw new ImageReadException("Parsing XBM file failed, hex value too long");
            }
            int i3 = Integer.parseInt(strNextToken3.substring(2), 16);
            int i4 = 0;
            for (int i5 = 0; i5 < 8; i5++) {
                if (((1 << i5) & i3) != 0) {
                    i4 |= 128 >>> i5;
                }
            }
            bArr[i2] = (byte) i4;
            String strNextToken4 = basicCParser.nextToken();
            if (strNextToken4 == null) {
                throw new ImageReadException("Parsing XBM file failed, premature end of file");
            }
            if (!",".equals(strNextToken4) && (i2 < bArr.length - 1 || !"}".equals(strNextToken4))) {
                throw new ImageReadException("Parsing XBM file failed, punctuation error");
            }
        }
        IndexColorModel indexColorModel = new IndexColorModel(1, 2, new int[]{ViewCompat.MEASURED_SIZE_MASK, 0}, 0, false, -1, 0);
        return new BufferedImage(indexColorModel, Raster.createPackedRaster(new DataBufferByte(bArr, bArr.length), xbmHeader.width, xbmHeader.height, 1, (Point) null), indexColorModel.isAlphaPremultiplied(), new Properties());
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        readXbmHeader(byteSource).dump(printWriter);
        return true;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public final BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        XbmParseResult xbmHeader = parseXbmHeader(byteSource);
        return readXbmImage(xbmHeader.xbmHeader, xbmHeader.cParser);
    }

    private String randomName() {
        UUID uuidRandomUUID = UUID.randomUUID();
        StringBuilder sb = new StringBuilder("a");
        long mostSignificantBits = uuidRandomUUID.getMostSignificantBits();
        for (int i = 56; i >= 0; i -= 8) {
            sb.append(Integer.toHexString((int) (255 & (mostSignificantBits >> i))));
        }
        long leastSignificantBits = uuidRandomUUID.getLeastSignificantBits();
        for (int i2 = 56; i2 >= 0; i2 -= 8) {
            sb.append(Integer.toHexString((int) ((leastSignificantBits >> i2) & 255)));
        }
        return sb.toString();
    }

    private String toPrettyHex(int i) {
        String hexString = Integer.toHexString(255 & i);
        if (hexString.length() == 2) {
            return "0x" + hexString;
        }
        return "0x0" + hexString;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        if (!map2.isEmpty()) {
            throw new ImageWriteException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        String strRandomName = randomName();
        outputStream.write(("#define " + strRandomName + "_width " + bufferedImage.getWidth() + "\n").getBytes("US-ASCII"));
        outputStream.write(("#define " + strRandomName + "_height " + bufferedImage.getHeight() + "\n").getBytes("US-ASCII"));
        StringBuilder sb = new StringBuilder();
        sb.append("static unsigned char ");
        sb.append(strRandomName);
        sb.append("_bits[] = {");
        outputStream.write(sb.toString().getBytes("US-ASCII"));
        String str = "\n  ";
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < bufferedImage.getHeight(); i4++) {
            int i5 = i;
            for (int i6 = 0; i6 < bufferedImage.getWidth(); i6++) {
                int rgb = bufferedImage.getRGB(i6, i4);
                i5 |= (((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + ((rgb >> 0) & 255)) / 3 > 127 ? 0 : 1) << i2;
                i2++;
                if (i2 == 8) {
                    outputStream.write(str.getBytes("US-ASCII"));
                    if (i3 == 12) {
                        outputStream.write("\n  ".getBytes("US-ASCII"));
                        i3 = 0;
                    }
                    outputStream.write(toPrettyHex(i5).getBytes("US-ASCII"));
                    i3++;
                    i5 = 0;
                    str = ",";
                    i2 = 0;
                }
            }
            if (i2 != 0) {
                outputStream.write(str.getBytes("US-ASCII"));
                if (i3 == 12) {
                    outputStream.write("\n  ".getBytes("US-ASCII"));
                    i3 = 0;
                }
                outputStream.write(toPrettyHex(i5).getBytes("US-ASCII"));
                i3++;
                i2 = 0;
                str = ",";
                i = 0;
            } else {
                i = i5;
            }
        }
        outputStream.write("\n};\n".getBytes("US-ASCII"));
    }
}
