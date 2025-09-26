// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.xbm;

import org.apache.commons.imaging.ImageWriteException;
import java.io.OutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.util.Properties;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.BufferedImage;
import java.util.UUID;
import java.io.IOException;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.ImageReadException;
import java.util.Map;
import org.apache.commons.imaging.common.BasicCParser;
import java.util.HashMap;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.ImageParser;

public class XbmImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".xbm";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".xbm" };
    }
    
    private XbmParseResult parseXbmHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                final ByteArrayOutputStream preprocess = BasicCParser.preprocess(inputStream, null, hashMap);
                final Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
                int int1 = -1;
                int int2 = -1;
                int int4;
                int int3 = int4 = int2;
                while (iterator.hasNext()) {
                    final Map.Entry<String, V> entry = (Map.Entry<String, V>)iterator.next();
                    final String s = entry.getKey();
                    if (s.endsWith("_width")) {
                        int1 = Integer.parseInt((String)entry.getValue());
                    }
                    else if (s.endsWith("_height")) {
                        int2 = Integer.parseInt((String)entry.getValue());
                    }
                    else if (s.endsWith("_x_hot")) {
                        int3 = Integer.parseInt((String)entry.getValue());
                    }
                    else {
                        if (!s.endsWith("_y_hot")) {
                            continue;
                        }
                        int4 = Integer.parseInt((String)entry.getValue());
                    }
                }
                if (int1 == -1) {
                    throw new ImageReadException("width not found");
                }
                if (int2 == -1) {
                    throw new ImageReadException("height not found");
                }
                final XbmParseResult xbmParseResult = new XbmParseResult();
                xbmParseResult.cParser = new BasicCParser(new ByteArrayInputStream(preprocess.toByteArray()));
                xbmParseResult.xbmHeader = new XbmHeader(int1, int2, int3, int4);
                IoUtils.closeQuietly(true, inputStream);
                return xbmParseResult;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    private String randomName() {
        final UUID randomUUID = UUID.randomUUID();
        final StringBuilder sb = new StringBuilder("a");
        final long mostSignificantBits = randomUUID.getMostSignificantBits();
        final int n = 56;
        for (int i = 56; i >= 0; i -= 8) {
            sb.append(Integer.toHexString((int)(0xFFL & mostSignificantBits >> i)));
        }
        final long leastSignificantBits = randomUUID.getLeastSignificantBits();
        for (int j = n; j >= 0; j -= 8) {
            sb.append(Integer.toHexString((int)(leastSignificantBits >> j & 0xFFL)));
        }
        return sb.toString();
    }
    
    private XbmHeader readXbmHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        return this.parseXbmHeader(byteSource).xbmHeader;
    }
    
    private BufferedImage readXbmImage(final XbmHeader xbmHeader, final BasicCParser basicCParser) throws ImageReadException, IOException {
        if (!"static".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XBM file failed, no 'static' token");
        }
        final String nextToken = basicCParser.nextToken();
        if (nextToken == null) {
            throw new ImageReadException("Parsing XBM file failed, no 'unsigned' or 'char' token");
        }
        String nextToken2 = nextToken;
        if ("unsigned".equals(nextToken)) {
            nextToken2 = basicCParser.nextToken();
        }
        if (!"char".equals(nextToken2)) {
            throw new ImageReadException("Parsing XBM file failed, no 'char' token");
        }
        final String nextToken3 = basicCParser.nextToken();
        if (nextToken3 == null) {
            throw new ImageReadException("Parsing XBM file failed, no variable name");
        }
        if (nextToken3.charAt(0) != '_' && !Character.isLetter(nextToken3.charAt(0))) {
            throw new ImageReadException("Parsing XBM file failed, variable name doesn't start with letter or underscore");
        }
        for (int i = 0; i < nextToken3.length(); ++i) {
            final char char1 = nextToken3.charAt(i);
            if (!Character.isLetterOrDigit(char1) && char1 != '_') {
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
        final byte[] dataArray = new byte[(xbmHeader.width + 7) / 8 * xbmHeader.height];
        for (int j = 0; j < dataArray.length; ++j) {
            final String nextToken4 = basicCParser.nextToken();
            if (nextToken4 == null || !nextToken4.startsWith("0x")) {
                throw new ImageReadException("Parsing XBM file failed, hex value missing");
            }
            if (nextToken4.length() > 4) {
                throw new ImageReadException("Parsing XBM file failed, hex value too long");
            }
            final int int1 = Integer.parseInt(nextToken4.substring(2), 16);
            int k = 0;
            int n = 0;
            while (k < 8) {
                int n2 = n;
                if ((1 << k & int1) != 0x0) {
                    n2 = (n | 128 >>> k);
                }
                ++k;
                n = n2;
            }
            dataArray[j] = (byte)n;
            final String nextToken5 = basicCParser.nextToken();
            if (nextToken5 == null) {
                throw new ImageReadException("Parsing XBM file failed, premature end of file");
            }
            if (!",".equals(nextToken5) && (j < dataArray.length - 1 || !"}".equals(nextToken5))) {
                throw new ImageReadException("Parsing XBM file failed, punctuation error");
            }
        }
        final IndexColorModel cm = new IndexColorModel(1, 2, new int[] { 16777215, 0 }, 0, false, -1, 0);
        return new BufferedImage(cm, Raster.createPackedRaster(new DataBufferByte(dataArray, dataArray.length), xbmHeader.width, xbmHeader.height, 1, null), cm.isAlphaPremultiplied(), new Properties());
    }
    
    private String toPrettyHex(final int n) {
        final String hexString = Integer.toHexString(0xFF & n);
        if (hexString.length() == 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("0x");
            sb.append(hexString);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("0x0");
        sb2.append(hexString);
        return sb2.toString();
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        this.readXbmHeader(byteSource).dump(printWriter);
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return XbmImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.XBM };
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final XbmParseResult xbmHeader = this.parseXbmHeader(byteSource);
        return this.readXbmImage(xbmHeader.xbmHeader, xbmHeader.cParser);
    }
    
    @Override
    public String getDefaultExtension() {
        return ".xbm";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final XbmHeader xbmHeader = this.readXbmHeader(byteSource);
        return new ImageInfo("XBM", 1, new ArrayList<String>(), ImageFormats.XBM, "X BitMap", xbmHeader.height, "image/x-xbitmap", 1, 0, 0.0f, 0, 0.0f, xbmHeader.width, false, false, false, ImageInfo.ColorType.BW, ImageInfo.CompressionAlgorithm.NONE);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final XbmHeader xbmHeader = this.readXbmHeader(byteSource);
        return new Dimension(xbmHeader.width, xbmHeader.height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "X BitMap";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> m) throws ImageWriteException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        if (hashMap.containsKey("FORMAT")) {
            hashMap.remove("FORMAT");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageWriteException(sb.toString());
        }
        final String randomName = this.randomName();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("#define ");
        sb2.append(randomName);
        sb2.append("_width ");
        sb2.append(bufferedImage.getWidth());
        sb2.append("\n");
        outputStream.write(sb2.toString().getBytes("US-ASCII"));
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("#define ");
        sb3.append(randomName);
        sb3.append("_height ");
        sb3.append(bufferedImage.getHeight());
        sb3.append("\n");
        outputStream.write(sb3.toString().getBytes("US-ASCII"));
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("static unsigned char ");
        sb4.append(randomName);
        sb4.append("_bits[] = {");
        outputStream.write(sb4.toString().getBytes("US-ASCII"));
        String s = "\n  ";
        int i = 0;
        int n = 0;
        int n3;
        int n2 = n3 = n;
        while (i < bufferedImage.getHeight()) {
            String s2;
            int n7;
            for (int j = 0; j < bufferedImage.getWidth(); ++j, s = s2, n3 = n7) {
                final int rgb = bufferedImage.getRGB(j, i);
                int n4;
                if (((rgb >> 16 & 0xFF) + (rgb >> 8 & 0xFF) + (rgb >> 0 & 0xFF)) / 3 > 127) {
                    n4 = 0;
                }
                else {
                    n4 = 1;
                }
                final int n5 = n | n4 << n2;
                final int n6 = ++n2;
                s2 = s;
                n7 = n3;
                n = n5;
                if (n6 == 8) {
                    outputStream.write(s.getBytes("US-ASCII"));
                    int n8;
                    if ((n8 = n3) == 12) {
                        outputStream.write("\n  ".getBytes("US-ASCII"));
                        n8 = 0;
                    }
                    outputStream.write(this.toPrettyHex(n5).getBytes("US-ASCII"));
                    n7 = n8 + 1;
                    n = 0;
                    s2 = ",";
                    n2 = 0;
                }
            }
            if (n2 != 0) {
                outputStream.write(s.getBytes("US-ASCII"));
                int n9;
                if ((n9 = n3) == 12) {
                    outputStream.write("\n  ".getBytes("US-ASCII"));
                    n9 = 0;
                }
                outputStream.write(this.toPrettyHex(n).getBytes("US-ASCII"));
                n3 = n9 + 1;
                n2 = 0;
                s = ",";
                n = 0;
            }
            ++i;
        }
        outputStream.write("\n};\n".getBytes("US-ASCII"));
    }
    
    private static class XbmHeader
    {
        int height;
        int width;
        int xHot;
        int yHot;
        
        public XbmHeader(final int width, final int height, final int xHot, final int yHot) {
            this.xHot = -1;
            this.yHot = -1;
            this.width = width;
            this.height = height;
            this.xHot = xHot;
            this.yHot = yHot;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("XbmHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("Width: ");
            sb.append(this.width);
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Height: ");
            sb2.append(this.height);
            printWriter.println(sb2.toString());
            if (this.xHot != -1 && this.yHot != -1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("X hot: ");
                sb3.append(this.xHot);
                printWriter.println(sb3.toString());
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Y hot: ");
                sb4.append(this.yHot);
                printWriter.println(sb4.toString());
            }
        }
    }
    
    private static class XbmParseResult
    {
        BasicCParser cParser;
        XbmHeader xbmHeader;
    }
}
