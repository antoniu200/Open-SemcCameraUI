// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.xpm;

import org.apache.commons.imaging.palette.SimplePalette;
import org.apache.commons.imaging.palette.PaletteFactory;
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
import java.util.Arrays;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Properties;
import java.awt.image.DirectColorModel;
import java.awt.Point;
import java.awt.image.Raster;
import java.awt.image.IndexColorModel;
import java.awt.image.BufferedImage;
import java.util.UUID;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.BasicCParser;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.apache.commons.imaging.ImageReadException;
import java.util.Map;
import org.apache.commons.imaging.ImageParser;

public class XpmImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".xpm";
    private static final char[] WRITE_PALETTE;
    private static Map<String, Integer> colorNames;
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".xpm" };
        WRITE_PALETTE = new char[] { ' ', '.', 'X', 'o', 'O', '+', '@', '#', '$', '%', '&', '*', '=', '-', ';', ':', '>', ',', '<', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'M', 'N', 'B', 'V', 'C', 'Z', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'P', 'I', 'U', 'Y', 'T', 'R', 'E', 'W', 'Q', '!', '~', '^', '/', '(', ')', '_', '`', '\'', ']', '[', '{', '}', '|' };
    }
    
    private static void loadColorNames() throws ImageReadException {
        synchronized (XpmImageParser.class) {
            if (XpmImageParser.colorNames != null) {
                return;
            }
            try {
                final InputStream resourceAsStream = XpmImageParser.class.getResourceAsStream("rgb.txt");
                if (resourceAsStream == null) {
                    throw new ImageReadException("Couldn't find rgb.txt in our resources");
                }
                final HashMap colorNames = new HashMap();
                Closeable closeable;
                try {
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "US-ASCII"));
                    try {
                        while (true) {
                            final String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            if (line.charAt(0) == '!') {
                                continue;
                            }
                            try {
                                colorNames.put(line.substring(11).trim(), Integer.parseInt(line.substring(0, 3).trim()) << 16 | 0xFF000000 | Integer.parseInt(line.substring(4, 7).trim()) << 8 | Integer.parseInt(line.substring(8, 11).trim()));
                                continue;
                            }
                            catch (final NumberFormatException ex) {
                                throw new ImageReadException("Couldn't parse color in rgb.txt", ex);
                            }
                            break;
                        }
                        IoUtils.closeQuietly(true, bufferedReader);
                        XpmImageParser.colorNames = colorNames;
                        return;
                    }
                    finally {}
                }
                finally {
                    closeable = null;
                }
                IoUtils.closeQuietly(false, closeable);
            }
            catch (final IOException ex2) {
                throw new ImageReadException("Could not parse rgb.txt", ex2);
            }
        }
    }
    
    private int parseColor(String substring) throws ImageReadException {
        if (substring.charAt(0) == '#') {
            substring = substring.substring(1);
            if (substring.length() == 3) {
                return Integer.parseInt(substring.substring(0, 1), 16) << 20 | 0xFF000000 | Integer.parseInt(substring.substring(1, 2), 16) << 12 | Integer.parseInt(substring.substring(2, 3), 16) << 4;
            }
            if (substring.length() == 6) {
                return Integer.parseInt(substring, 16) | 0xFF000000;
            }
            if (substring.length() == 9) {
                return Integer.parseInt(substring.substring(0, 1), 16) << 16 | 0xFF000000 | Integer.parseInt(substring.substring(3, 4), 16) << 8 | Integer.parseInt(substring.substring(6, 7), 16);
            }
            if (substring.length() == 12) {
                return Integer.parseInt(substring.substring(0, 1), 16) << 16 | 0xFF000000 | Integer.parseInt(substring.substring(4, 5), 16) << 8 | Integer.parseInt(substring.substring(8, 9), 16);
            }
            return 0;
        }
        else {
            if (substring.charAt(0) == '%') {
                throw new ImageReadException("HSV colors are not implemented even in the XPM specification!");
            }
            if ("None".equals(substring)) {
                return 0;
            }
            loadColorNames();
            if (XpmImageParser.colorNames.containsKey(substring)) {
                return XpmImageParser.colorNames.get(substring);
            }
            return 0;
        }
    }
    
    private boolean parseNextString(final BasicCParser basicCParser, final StringBuilder sb) throws IOException, ImageReadException {
        sb.setLength();
        final String nextToken = basicCParser.nextToken();
        if (nextToken.charAt(0) != '\"') {
            throw new ImageReadException("Parsing XPM file failed, no string found where expected");
        }
        BasicCParser.unescapeString(sb, nextToken);
        String nextToken2;
        while (true) {
            nextToken2 = basicCParser.nextToken();
            if (nextToken2.charAt(0) != '\"') {
                break;
            }
            BasicCParser.unescapeString(sb, nextToken2);
        }
        if (",".equals(nextToken2)) {
            return true;
        }
        if ("}".equals(nextToken2)) {
            return false;
        }
        throw new ImageReadException("Parsing XPM file failed, no ',' or '}' found where expected");
    }
    
    private void parsePaletteEntries(final XpmHeader xpmHeader, final BasicCParser basicCParser) throws IOException, ImageReadException {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xpmHeader.numColors; ++i) {
            sb.setLength();
            if (!this.parseNextString(basicCParser, sb)) {
                throw new ImageReadException("Parsing XPM file failed, file ended while reading palette");
            }
            final String substring = sb.substring();
            final String[] tokenizeRow = BasicCParser.tokenizeRow(sb.substring());
            final PaletteEntry paletteEntry = new PaletteEntry();
            paletteEntry.index = i;
            final StringBuilder sb2 = new StringBuilder();
            int n = Integer.MIN_VALUE;
            for (int j = 0; j < tokenizeRow.length; ++j) {
                final String s = tokenizeRow[j];
                if ((n < j - 1 && "m".equals(s)) || "g4".equals(s) || "g".equals(s) || "c".equals(s) || "s".equals(s)) {
                    if (n >= 0) {
                        final String s2 = tokenizeRow[n];
                        final String string = sb2.toString();
                        sb2.setLength();
                        this.populatePaletteEntry(paletteEntry, s2, string);
                    }
                    n = j;
                }
                else {
                    if (n < 0) {
                        break;
                    }
                    if (sb2.length() > 0) {
                        sb2.append(' ');
                    }
                    sb2.append(s);
                }
            }
            if (n >= 0 && sb2.length() > 0) {
                final String s3 = tokenizeRow[n];
                final String string2 = sb2.toString();
                sb2.setLength();
                this.populatePaletteEntry(paletteEntry, s3, string2);
            }
            xpmHeader.palette.put(substring, paletteEntry);
        }
    }
    
    private XpmHeader parseXpmHeader(final BasicCParser basicCParser) throws ImageReadException, IOException {
        if (!"static".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no 'static' token");
        }
        if (!"char".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no 'char' token");
        }
        if (!"*".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no '*' token");
        }
        final String nextToken = basicCParser.nextToken();
        if (nextToken == null) {
            throw new ImageReadException("Parsing XPM file failed, no variable name");
        }
        int i;
        final int n = i = 0;
        if (nextToken.charAt(0) != '_') {
            i = n;
            if (!Character.isLetter(nextToken.charAt(0))) {
                throw new ImageReadException("Parsing XPM file failed, variable name doesn't start with letter or underscore");
            }
        }
        while (i < nextToken.length()) {
            final char char1 = nextToken.charAt(i);
            if (!Character.isLetterOrDigit(char1) && char1 != '_') {
                throw new ImageReadException("Parsing XPM file failed, variable name contains non-letter non-digit non-underscore");
            }
            ++i;
        }
        if (!"[".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no '[' token");
        }
        if (!"]".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no ']' token");
        }
        if (!"=".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no '=' token");
        }
        if (!"{".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no '{' token");
        }
        final StringBuilder sb = new StringBuilder();
        if (!this.parseNextString(basicCParser, sb)) {
            throw new ImageReadException("Parsing XPM file failed, file too short");
        }
        final XpmHeader xpmValuesSection = this.parseXpmValuesSection(sb.toString());
        this.parsePaletteEntries(xpmValuesSection, basicCParser);
        return xpmValuesSection;
    }
    
    private XpmParseResult parseXpmHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final StringBuilder sb = new StringBuilder();
                final ByteArrayOutputStream preprocess = BasicCParser.preprocess(inputStream, sb, null);
                if (!"XPM".equals(sb.toString().trim())) {
                    throw new ImageReadException("Parsing XPM file failed, signature isn't '/* XPM */'");
                }
                final XpmParseResult xpmParseResult = new XpmParseResult();
                xpmParseResult.cParser = new BasicCParser(new ByteArrayInputStream(preprocess.toByteArray()));
                xpmParseResult.xpmHeader = this.parseXpmHeader(xpmParseResult.cParser);
                IoUtils.closeQuietly(true, inputStream);
                return xpmParseResult;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    private XpmHeader parseXpmValuesSection(final String s) throws ImageReadException {
        final String[] tokenizeRow = BasicCParser.tokenizeRow(s);
        if (tokenizeRow.length < 4 && tokenizeRow.length > 7) {
            throw new ImageReadException("Parsing XPM file failed, <Values> section has incorrect tokens");
        }
        boolean b = false;
        try {
            final int int1 = Integer.parseInt(tokenizeRow[0]);
            final int int2 = Integer.parseInt(tokenizeRow[1]);
            final int int3 = Integer.parseInt(tokenizeRow[2]);
            final int int4 = Integer.parseInt(tokenizeRow[3]);
            final int length = tokenizeRow.length;
            int int5 = -1;
            int int6;
            if (length >= 6) {
                int6 = Integer.parseInt(tokenizeRow[4]);
                int5 = Integer.parseInt(tokenizeRow[5]);
            }
            else {
                int6 = -1;
            }
            if (tokenizeRow.length == 5 || tokenizeRow.length == 7) {
                if (!"XPMEXT".equals(tokenizeRow[tokenizeRow.length - 1])) {
                    throw new ImageReadException("Parsing XPM file failed, can't parse <Values> section XPMEXT");
                }
                b = true;
            }
            return new XpmHeader(int1, int2, int3, int4, int6, int5, b);
        }
        catch (final NumberFormatException ex) {
            throw new ImageReadException("Parsing XPM file failed, error parsing <Values> section", ex);
        }
    }
    
    private String pixelsForIndex(int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        int i = 1;
        int n3 = 1;
        while (i < n2) {
            n3 *= XpmImageParser.WRITE_PALETTE.length;
            ++i;
        }
        for (int j = 0; j < n2; ++j) {
            final int n4 = n / n3;
            n -= n4 * n3;
            n3 /= XpmImageParser.WRITE_PALETTE.length;
            sb.append(XpmImageParser.WRITE_PALETTE[n4]);
        }
        return sb.toString();
    }
    
    private void populatePaletteEntry(final PaletteEntry paletteEntry, final String anObject, final String s) throws ImageReadException {
        if ("m".equals(anObject)) {
            paletteEntry.monoArgb = this.parseColor(s);
            paletteEntry.haveMono = true;
        }
        else if ("g4".equals(anObject)) {
            paletteEntry.gray4LevelArgb = this.parseColor(s);
            paletteEntry.haveGray4Level = true;
        }
        else if ("g".equals(anObject)) {
            paletteEntry.grayArgb = this.parseColor(s);
            paletteEntry.haveGray = true;
        }
        else if ("s".equals(anObject)) {
            paletteEntry.colorArgb = this.parseColor(s);
            paletteEntry.haveColor = true;
        }
        else if ("c".equals(anObject)) {
            paletteEntry.colorArgb = this.parseColor(s);
            paletteEntry.haveColor = true;
        }
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
    
    private XpmHeader readXpmHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        return this.parseXpmHeader(byteSource).xpmHeader;
    }
    
    private BufferedImage readXpmImage(final XpmHeader xpmHeader, final BasicCParser basicCParser) throws ImageReadException, IOException {
        ColorModel cm;
        WritableRaster raster;
        int n;
        if (xpmHeader.palette.size() <= 256) {
            final int[] cmap = new int[xpmHeader.palette.size()];
            final Iterator<Map.Entry<Object, PaletteEntry>> iterator = xpmHeader.palette.entrySet().iterator();
            while (iterator.hasNext()) {
                final PaletteEntry paletteEntry = ((Map.Entry<K, PaletteEntry>)iterator.next()).getValue();
                cmap[paletteEntry.index] = paletteEntry.getBestARGB();
            }
            cm = new IndexColorModel(8, xpmHeader.palette.size(), cmap, 0, true, -1, 0);
            raster = Raster.createInterleavedRaster(0, xpmHeader.width, xpmHeader.height, 1, null);
            n = 8;
        }
        else if (xpmHeader.palette.size() <= 65536) {
            final int[] cmap2 = new int[xpmHeader.palette.size()];
            final Iterator<Map.Entry<Object, PaletteEntry>> iterator2 = xpmHeader.palette.entrySet().iterator();
            while (iterator2.hasNext()) {
                final PaletteEntry paletteEntry2 = ((Map.Entry<K, PaletteEntry>)iterator2.next()).getValue();
                cmap2[paletteEntry2.index] = paletteEntry2.getBestARGB();
            }
            cm = new IndexColorModel(16, xpmHeader.palette.size(), cmap2, 0, true, -1, 1);
            raster = Raster.createInterleavedRaster(1, xpmHeader.width, xpmHeader.height, 1, null);
            n = 16;
        }
        else {
            cm = new DirectColorModel(32, 16711680, 65280, 255, -16777216);
            raster = Raster.createPackedRaster(3, xpmHeader.width, xpmHeader.height, new int[] { 16711680, 65280, 255, -16777216 }, null);
            n = 32;
        }
        final BufferedImage bufferedImage = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), new Properties());
        final DataBuffer dataBuffer = raster.getDataBuffer();
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        boolean b = true;
        while (i < xpmHeader.height) {
            sb.setLength();
            b = this.parseNextString(basicCParser, sb);
            if (i < xpmHeader.height - 1 && !b) {
                throw new ImageReadException("Parsing XPM file failed, insufficient image rows in file");
            }
            final int n2 = xpmHeader.width * i;
            int n3;
            for (int j = 0; j < xpmHeader.width; j = n3) {
                final int numCharsPerPixel = xpmHeader.numCharsPerPixel;
                n3 = j + 1;
                final String substring = sb.substring();
                final PaletteEntry paletteEntry3 = xpmHeader.palette.get(substring);
                if (paletteEntry3 == null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("No palette entry was defined for ");
                    sb2.append(substring);
                    throw new ImageReadException(sb2.toString());
                }
                if (n <= 16) {
                    dataBuffer.setElem(j + n2, paletteEntry3.index);
                }
                else {
                    dataBuffer.setElem(j + n2, paletteEntry3.getBestARGB());
                }
            }
            ++i;
        }
        while (b) {
            sb.setLength();
            b = this.parseNextString(basicCParser, sb);
        }
        if (!";".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Last token wasn't ';'");
        }
        return bufferedImage;
    }
    
    private String toColor(final int i) {
        final String hexString = Integer.toHexString(i);
        if (hexString.length() < 6) {
            final char[] array = new char[6 - hexString.length()];
            Arrays.fill(array, '0');
            final StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(new String(array));
            sb.append(hexString);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("#");
        sb2.append(hexString);
        return sb2.toString();
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        this.readXpmHeader(byteSource).dump(printWriter);
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return XpmImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.XPM };
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final XpmParseResult xpmHeader = this.parseXpmHeader(byteSource);
        return this.readXpmImage(xpmHeader.xpmHeader, xpmHeader.cParser);
    }
    
    @Override
    public String getDefaultExtension() {
        return ".xpm";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final XpmHeader xpmHeader = this.readXpmHeader(byteSource);
        ImageInfo.ColorType colorType = ImageInfo.ColorType.BW;
        final Iterator<Map.Entry<Object, PaletteEntry>> iterator = xpmHeader.palette.entrySet().iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final PaletteEntry paletteEntry = ((Map.Entry<K, PaletteEntry>)iterator.next()).getValue();
            boolean b2 = b;
            if ((paletteEntry.getBestARGB() & 0xFF000000) != 0xFF000000) {
                b2 = true;
            }
            if (paletteEntry.haveColor) {
                colorType = ImageInfo.ColorType.RGB;
                b = b2;
            }
            else {
                b = b2;
                if (colorType == ImageInfo.ColorType.RGB) {
                    continue;
                }
                if (!paletteEntry.haveGray) {
                    b = b2;
                    if (!paletteEntry.haveGray4Level) {
                        continue;
                    }
                }
                colorType = ImageInfo.ColorType.GRAYSCALE;
                b = b2;
            }
        }
        return new ImageInfo("XPM version 3", xpmHeader.numCharsPerPixel * 8, new ArrayList<String>(), ImageFormats.XPM, "X PixMap", xpmHeader.height, "image/x-xpixmap", 1, 0, 0.0f, 0, 0.0f, xpmHeader.width, false, b, true, colorType, ImageInfo.CompressionAlgorithm.NONE);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final XpmHeader xpmHeader = this.readXpmHeader(byteSource);
        return new Dimension(xpmHeader.width, xpmHeader.height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "X PixMap";
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
        final PaletteFactory paletteFactory = new PaletteFactory();
        int i = 1;
        final boolean hasTransparency = paletteFactory.hasTransparency(bufferedImage, 1);
        SimplePalette exactRgbPaletteSimple = null;
        int length = XpmImageParser.WRITE_PALETTE.length;
        while (exactRgbPaletteSimple == null) {
            int n;
            if (hasTransparency) {
                n = length - 1;
            }
            else {
                n = length;
            }
            final SimplePalette simplePalette = exactRgbPaletteSimple = paletteFactory.makeExactRgbPaletteSimple(bufferedImage, n);
            if (simplePalette == null) {
                length *= XpmImageParser.WRITE_PALETTE.length;
                ++i;
                exactRgbPaletteSimple = simplePalette;
            }
        }
        int length2;
        final int n2 = length2 = exactRgbPaletteSimple.length();
        if (hasTransparency) {
            length2 = n2 + 1;
        }
        outputStream.write("/* XPM */\n".getBytes("US-ASCII"));
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("static char *");
        sb2.append(this.randomName());
        sb2.append("[] = {\n");
        outputStream.write(sb2.toString().getBytes("US-ASCII"));
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("\"");
        sb3.append(bufferedImage.getWidth());
        sb3.append(" ");
        sb3.append(bufferedImage.getHeight());
        sb3.append(" ");
        sb3.append(length2);
        sb3.append(" ");
        sb3.append(i);
        sb3.append("\",\n");
        outputStream.write(sb3.toString().getBytes("US-ASCII"));
        for (int j = 0; j < length2; ++j) {
            String color;
            if (j < exactRgbPaletteSimple.length()) {
                color = this.toColor(exactRgbPaletteSimple.getEntry(j));
            }
            else {
                color = "None";
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("\"");
            sb4.append(this.pixelsForIndex(j, i));
            sb4.append(" c ");
            sb4.append(color);
            sb4.append("\",\n");
            outputStream.write(sb4.toString().getBytes("US-ASCII"));
        }
        String s = "";
        String s2;
        for (int k = 0; k < bufferedImage.getHeight(); ++k, s = s2) {
            outputStream.write(s.getBytes("US-ASCII"));
            s2 = ",\n";
            outputStream.write("\"".getBytes("US-ASCII"));
            for (int l = 0; l < bufferedImage.getWidth(); ++l) {
                final int rgb = bufferedImage.getRGB(l, k);
                String s3;
                if ((0xFF000000 & rgb) == 0x0) {
                    s3 = this.pixelsForIndex(exactRgbPaletteSimple.length(), i);
                }
                else {
                    s3 = this.pixelsForIndex(exactRgbPaletteSimple.getPaletteIndex(rgb & 0xFFFFFF), i);
                }
                outputStream.write(s3.getBytes("US-ASCII"));
            }
            outputStream.write("\"".getBytes("US-ASCII"));
        }
        outputStream.write("\n};\n".getBytes("US-ASCII"));
    }
    
    private static class PaletteEntry
    {
        int colorArgb;
        int gray4LevelArgb;
        int grayArgb;
        boolean haveColor;
        boolean haveGray;
        boolean haveGray4Level;
        boolean haveMono;
        int index;
        int monoArgb;
        
        private PaletteEntry() {
            this.haveColor = false;
            this.haveGray = false;
            this.haveGray4Level = false;
            this.haveMono = false;
        }
        
        int getBestARGB() {
            if (this.haveColor) {
                return this.colorArgb;
            }
            if (this.haveGray) {
                return this.grayArgb;
            }
            if (this.haveGray4Level) {
                return this.gray4LevelArgb;
            }
            if (this.haveMono) {
                return this.monoArgb;
            }
            return 0;
        }
    }
    
    private static class XpmHeader
    {
        int height;
        int numCharsPerPixel;
        int numColors;
        Map<Object, PaletteEntry> palette;
        int width;
        int xHotSpot;
        boolean xpmExt;
        int yHotSpot;
        
        public XpmHeader(final int width, final int height, final int numColors, final int numCharsPerPixel, final int xHotSpot, final int yHotSpot, final boolean xpmExt) {
            this.xHotSpot = -1;
            this.yHotSpot = -1;
            this.palette = new HashMap<Object, PaletteEntry>();
            this.width = width;
            this.height = height;
            this.numColors = numColors;
            this.numCharsPerPixel = numCharsPerPixel;
            this.xHotSpot = xHotSpot;
            this.yHotSpot = yHotSpot;
            this.xpmExt = xpmExt;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("XpmHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("Width: ");
            sb.append(this.width);
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Height: ");
            sb2.append(this.height);
            printWriter.println(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("NumColors: ");
            sb3.append(this.numColors);
            printWriter.println(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("NumCharsPerPixel: ");
            sb4.append(this.numCharsPerPixel);
            printWriter.println(sb4.toString());
            if (this.xHotSpot != -1 && this.yHotSpot != -1) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("X hotspot: ");
                sb5.append(this.xHotSpot);
                printWriter.println(sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("Y hotspot: ");
                sb6.append(this.yHotSpot);
                printWriter.println(sb6.toString());
            }
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("XpmExt: ");
            sb7.append(this.xpmExt);
            printWriter.println(sb7.toString());
        }
    }
    
    private static class XpmParseResult
    {
        BasicCParser cParser;
        XpmHeader xpmHeader;
    }
}
