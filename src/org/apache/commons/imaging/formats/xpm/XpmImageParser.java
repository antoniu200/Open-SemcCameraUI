package org.apache.commons.imaging.formats.xpm;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import kotlin.text.Typography;
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
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.palette.SimplePalette;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class XpmImageParser extends ImageParser {
    private static Map<String, Integer> colorNames;
    private static final String DEFAULT_EXTENSION = ".xpm";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};
    private static final char[] WRITE_PALETTE = {' ', '.', 'X', 'o', 'O', '+', '@', '#', Typography.dollar, '%', Typography.amp, '*', '=', '-', ';', ':', Typography.greater, ',', Typography.less, '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'M', 'N', 'B', 'V', 'C', 'Z', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'P', 'I', 'U', 'Y', 'T', 'R', 'E', 'W', 'Q', '!', '~', '^', '/', '(', ')', '_', '`', '\'', ']', '[', '{', '}', '|'};

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
        return "X PixMap";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    private static void loadColorNames() throws ImageReadException {
        BufferedReader bufferedReader;
        synchronized (XpmImageParser.class) {
            if (colorNames != null) {
                return;
            }
            try {
                InputStream resourceAsStream = XpmImageParser.class.getResourceAsStream("rgb.txt");
                if (resourceAsStream == null) {
                    throw new ImageReadException("Couldn't find rgb.txt in our resources");
                }
                HashMap map = new HashMap();
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "US-ASCII"));
                    while (true) {
                        try {
                            String line = bufferedReader.readLine();
                            if (line != null) {
                                if (line.charAt(0) != '!') {
                                    try {
                                        map.put(line.substring(11).trim(), Integer.valueOf((Integer.parseInt(line.substring(0, 3).trim()) << 16) | ViewCompat.MEASURED_STATE_MASK | (Integer.parseInt(line.substring(4, 7).trim()) << 8) | Integer.parseInt(line.substring(8, 11).trim())));
                                    } catch (NumberFormatException e) {
                                        throw new ImageReadException("Couldn't parse color in rgb.txt", e);
                                    }
                                }
                            } else {
                                IoUtils.closeQuietly(true, bufferedReader);
                                colorNames = map;
                                return;
                            }
                        } catch (Throwable th) {
                            th = th;
                            IoUtils.closeQuietly(false, bufferedReader);
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = null;
                }
            } catch (IOException e2) {
                throw new ImageReadException("Could not parse rgb.txt", e2);
            }
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.XPM};
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        XpmHeader xpmHeader = readXpmHeader(byteSource);
        ImageInfo.ColorType colorType = ImageInfo.ColorType.BW;
        Iterator<Map.Entry<Object, PaletteEntry>> it = xpmHeader.palette.entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            PaletteEntry value = it.next().getValue();
            if ((value.getBestARGB() & ViewCompat.MEASURED_STATE_MASK) != -16777216) {
                z = true;
            }
            if (value.haveColor) {
                colorType = ImageInfo.ColorType.RGB;
            } else if (colorType != ImageInfo.ColorType.RGB && (value.haveGray || value.haveGray4Level)) {
                colorType = ImageInfo.ColorType.GRAYSCALE;
            }
        }
        return new ImageInfo("XPM version 3", xpmHeader.numCharsPerPixel * 8, new ArrayList(), ImageFormats.XPM, "X PixMap", xpmHeader.height, "image/x-xpixmap", 1, 0, 0.0f, 0, 0.0f, xpmHeader.width, false, z, true, colorType, ImageInfo.CompressionAlgorithm.NONE);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        XpmHeader xpmHeader = readXpmHeader(byteSource);
        return new Dimension(xpmHeader.width, xpmHeader.height);
    }

    private static class XpmHeader {
        int height;
        int numCharsPerPixel;
        int numColors;
        Map<Object, PaletteEntry> palette = new HashMap();
        int width;
        int xHotSpot;
        boolean xpmExt;
        int yHotSpot;

        public XpmHeader(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
            this.xHotSpot = -1;
            this.yHotSpot = -1;
            this.width = i;
            this.height = i2;
            this.numColors = i3;
            this.numCharsPerPixel = i4;
            this.xHotSpot = i5;
            this.yHotSpot = i6;
            this.xpmExt = z;
        }

        public void dump(PrintWriter printWriter) {
            printWriter.println("XpmHeader");
            printWriter.println("Width: " + this.width);
            printWriter.println("Height: " + this.height);
            printWriter.println("NumColors: " + this.numColors);
            printWriter.println("NumCharsPerPixel: " + this.numCharsPerPixel);
            if (this.xHotSpot != -1 && this.yHotSpot != -1) {
                printWriter.println("X hotspot: " + this.xHotSpot);
                printWriter.println("Y hotspot: " + this.yHotSpot);
            }
            printWriter.println("XpmExt: " + this.xpmExt);
        }
    }

    private static class PaletteEntry {
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

    private static class XpmParseResult {
        BasicCParser cParser;
        XpmHeader xpmHeader;

        private XpmParseResult() {
        }
    }

    private XpmHeader readXpmHeader(ByteSource byteSource) throws IOException, ImageReadException {
        return parseXpmHeader(byteSource).xpmHeader;
    }

    private XpmParseResult parseXpmHeader(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            ByteArrayOutputStream byteArrayOutputStreamPreprocess = BasicCParser.preprocess(inputStream, sb, null);
            if (!"XPM".equals(sb.toString().trim())) {
                throw new ImageReadException("Parsing XPM file failed, signature isn't '/* XPM */'");
            }
            XpmParseResult xpmParseResult = new XpmParseResult();
            xpmParseResult.cParser = new BasicCParser(new ByteArrayInputStream(byteArrayOutputStreamPreprocess.toByteArray()));
            xpmParseResult.xpmHeader = parseXpmHeader(xpmParseResult.cParser);
            IoUtils.closeQuietly(true, inputStream);
            return xpmParseResult;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    private boolean parseNextString(BasicCParser basicCParser, StringBuilder sb) throws IOException, ImageReadException {
        String strNextToken;
        sb.setLength(0);
        String strNextToken2 = basicCParser.nextToken();
        if (strNextToken2.charAt(0) != '\"') {
            throw new ImageReadException("Parsing XPM file failed, no string found where expected");
        }
        BasicCParser.unescapeString(sb, strNextToken2);
        while (true) {
            strNextToken = basicCParser.nextToken();
            if (strNextToken.charAt(0) != '\"') {
                break;
            }
            BasicCParser.unescapeString(sb, strNextToken);
        }
        if (",".equals(strNextToken)) {
            return true;
        }
        if ("}".equals(strNextToken)) {
            return false;
        }
        throw new ImageReadException("Parsing XPM file failed, no ',' or '}' found where expected");
    }

    private XpmHeader parseXpmValuesSection(String str) throws NumberFormatException, ImageReadException {
        int i;
        String[] strArr = BasicCParser.tokenizeRow(str);
        if (strArr.length < 4 && strArr.length > 7) {
            throw new ImageReadException("Parsing XPM file failed, <Values> section has incorrect tokens");
        }
        boolean z = false;
        try {
            int i2 = Integer.parseInt(strArr[0]);
            int i3 = Integer.parseInt(strArr[1]);
            int i4 = Integer.parseInt(strArr[2]);
            int i5 = Integer.parseInt(strArr[3]);
            int i6 = -1;
            if (strArr.length >= 6) {
                i = Integer.parseInt(strArr[4]);
                i6 = Integer.parseInt(strArr[5]);
            } else {
                i = -1;
            }
            if (strArr.length == 5 || strArr.length == 7) {
                if (!"XPMEXT".equals(strArr[strArr.length - 1])) {
                    throw new ImageReadException("Parsing XPM file failed, can't parse <Values> section XPMEXT");
                }
                z = true;
            }
            return new XpmHeader(i2, i3, i4, i5, i, i6, z);
        } catch (NumberFormatException e) {
            throw new ImageReadException("Parsing XPM file failed, error parsing <Values> section", e);
        }
    }

    private int parseColor(String str) throws NumberFormatException, ImageReadException {
        if (str.charAt(0) == '#') {
            String strSubstring = str.substring(1);
            if (strSubstring.length() == 3) {
                return (Integer.parseInt(strSubstring.substring(0, 1), 16) << 20) | ViewCompat.MEASURED_STATE_MASK | (Integer.parseInt(strSubstring.substring(1, 2), 16) << 12) | (Integer.parseInt(strSubstring.substring(2, 3), 16) << 4);
            }
            if (strSubstring.length() == 6) {
                return Integer.parseInt(strSubstring, 16) | ViewCompat.MEASURED_STATE_MASK;
            }
            if (strSubstring.length() == 9) {
                int i = Integer.parseInt(strSubstring.substring(0, 1), 16);
                int i2 = Integer.parseInt(strSubstring.substring(3, 4), 16);
                return (i << 16) | ViewCompat.MEASURED_STATE_MASK | (i2 << 8) | Integer.parseInt(strSubstring.substring(6, 7), 16);
            }
            if (strSubstring.length() != 12) {
                return 0;
            }
            int i3 = Integer.parseInt(strSubstring.substring(0, 1), 16);
            int i4 = Integer.parseInt(strSubstring.substring(4, 5), 16);
            return (i3 << 16) | ViewCompat.MEASURED_STATE_MASK | (i4 << 8) | Integer.parseInt(strSubstring.substring(8, 9), 16);
        }
        if (str.charAt(0) == '%') {
            throw new ImageReadException("HSV colors are not implemented even in the XPM specification!");
        }
        if ("None".equals(str)) {
            return 0;
        }
        loadColorNames();
        if (colorNames.containsKey(str)) {
            return colorNames.get(str).intValue();
        }
        return 0;
    }

    private void populatePaletteEntry(PaletteEntry paletteEntry, String str, String str2) throws ImageReadException {
        if ("m".equals(str)) {
            paletteEntry.monoArgb = parseColor(str2);
            paletteEntry.haveMono = true;
            return;
        }
        if ("g4".equals(str)) {
            paletteEntry.gray4LevelArgb = parseColor(str2);
            paletteEntry.haveGray4Level = true;
            return;
        }
        if ("g".equals(str)) {
            paletteEntry.grayArgb = parseColor(str2);
            paletteEntry.haveGray = true;
        } else if ("s".equals(str)) {
            paletteEntry.colorArgb = parseColor(str2);
            paletteEntry.haveColor = true;
        } else if ("c".equals(str)) {
            paletteEntry.colorArgb = parseColor(str2);
            paletteEntry.haveColor = true;
        }
    }

    private void parsePaletteEntries(XpmHeader xpmHeader, BasicCParser basicCParser) throws IOException, ImageReadException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xpmHeader.numColors; i++) {
            sb.setLength(0);
            if (!parseNextString(basicCParser, sb)) {
                throw new ImageReadException("Parsing XPM file failed, file ended while reading palette");
            }
            String strSubstring = sb.substring(0, xpmHeader.numCharsPerPixel);
            String[] strArr = BasicCParser.tokenizeRow(sb.substring(xpmHeader.numCharsPerPixel));
            PaletteEntry paletteEntry = new PaletteEntry();
            paletteEntry.index = i;
            StringBuilder sb2 = new StringBuilder();
            int i2 = Integer.MIN_VALUE;
            for (int i3 = 0; i3 < strArr.length; i3++) {
                String str = strArr[i3];
                if ((i2 < i3 + (-1) && "m".equals(str)) || "g4".equals(str) || "g".equals(str) || "c".equals(str) || "s".equals(str)) {
                    if (i2 >= 0) {
                        String str2 = strArr[i2];
                        String string = sb2.toString();
                        sb2.setLength(0);
                        populatePaletteEntry(paletteEntry, str2, string);
                    }
                    i2 = i3;
                } else {
                    if (i2 < 0) {
                        break;
                    }
                    if (sb2.length() > 0) {
                        sb2.append(' ');
                    }
                    sb2.append(str);
                }
            }
            if (i2 >= 0 && sb2.length() > 0) {
                String str3 = strArr[i2];
                String string2 = sb2.toString();
                sb2.setLength(0);
                populatePaletteEntry(paletteEntry, str3, string2);
            }
            xpmHeader.palette.put(strSubstring, paletteEntry);
        }
    }

    private XpmHeader parseXpmHeader(BasicCParser basicCParser) throws IOException, NumberFormatException, ImageReadException {
        if (!"static".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no 'static' token");
        }
        if (!"char".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no 'char' token");
        }
        if (!"*".equals(basicCParser.nextToken())) {
            throw new ImageReadException("Parsing XPM file failed, no '*' token");
        }
        String strNextToken = basicCParser.nextToken();
        if (strNextToken == null) {
            throw new ImageReadException("Parsing XPM file failed, no variable name");
        }
        if (strNextToken.charAt(0) != '_' && !Character.isLetter(strNextToken.charAt(0))) {
            throw new ImageReadException("Parsing XPM file failed, variable name doesn't start with letter or underscore");
        }
        for (int i = 0; i < strNextToken.length(); i++) {
            char cCharAt = strNextToken.charAt(i);
            if (!Character.isLetterOrDigit(cCharAt) && cCharAt != '_') {
                throw new ImageReadException("Parsing XPM file failed, variable name contains non-letter non-digit non-underscore");
            }
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
        StringBuilder sb = new StringBuilder();
        if (!parseNextString(basicCParser, sb)) {
            throw new ImageReadException("Parsing XPM file failed, file too short");
        }
        XpmHeader xpmValuesSection = parseXpmValuesSection(sb.toString());
        parsePaletteEntries(xpmValuesSection, basicCParser);
        return xpmValuesSection;
    }

    private BufferedImage readXpmImage(XpmHeader xpmHeader, BasicCParser basicCParser) throws IOException, ImageReadException {
        IndexColorModel directColorModel;
        WritableRaster writableRasterCreatePackedRaster;
        char c;
        int i = 1;
        if (xpmHeader.palette.size() <= 256) {
            int[] iArr = new int[xpmHeader.palette.size()];
            Iterator<Map.Entry<Object, PaletteEntry>> it = xpmHeader.palette.entrySet().iterator();
            while (it.hasNext()) {
                PaletteEntry value = it.next().getValue();
                iArr[value.index] = value.getBestARGB();
            }
            directColorModel = new IndexColorModel(8, xpmHeader.palette.size(), iArr, 0, true, -1, 0);
            writableRasterCreatePackedRaster = Raster.createInterleavedRaster(0, xpmHeader.width, xpmHeader.height, 1, (Point) null);
            c = '\b';
        } else if (xpmHeader.palette.size() <= 65536) {
            int[] iArr2 = new int[xpmHeader.palette.size()];
            Iterator<Map.Entry<Object, PaletteEntry>> it2 = xpmHeader.palette.entrySet().iterator();
            while (it2.hasNext()) {
                PaletteEntry value2 = it2.next().getValue();
                iArr2[value2.index] = value2.getBestARGB();
            }
            directColorModel = new IndexColorModel(16, xpmHeader.palette.size(), iArr2, 0, true, -1, 1);
            writableRasterCreatePackedRaster = Raster.createInterleavedRaster(1, xpmHeader.width, xpmHeader.height, 1, (Point) null);
            c = 16;
        } else {
            directColorModel = new DirectColorModel(32, 16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255, ViewCompat.MEASURED_STATE_MASK);
            writableRasterCreatePackedRaster = Raster.createPackedRaster(3, xpmHeader.width, xpmHeader.height, new int[]{16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255, ViewCompat.MEASURED_STATE_MASK}, (Point) null);
            c = ' ';
        }
        BufferedImage bufferedImage = new BufferedImage(directColorModel, writableRasterCreatePackedRaster, directColorModel.isAlphaPremultiplied(), new Properties());
        DataBuffer dataBuffer = writableRasterCreatePackedRaster.getDataBuffer();
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        boolean nextString = true;
        while (i2 < xpmHeader.height) {
            sb.setLength(0);
            nextString = parseNextString(basicCParser, sb);
            if (i2 < xpmHeader.height - i && !nextString) {
                throw new ImageReadException("Parsing XPM file failed, insufficient image rows in file");
            }
            int i3 = xpmHeader.width * i2;
            int i4 = 0;
            while (i4 < xpmHeader.width) {
                int i5 = i4 + 1;
                String strSubstring = sb.substring(xpmHeader.numCharsPerPixel * i4, xpmHeader.numCharsPerPixel * i5);
                PaletteEntry paletteEntry = xpmHeader.palette.get(strSubstring);
                if (paletteEntry == null) {
                    throw new ImageReadException("No palette entry was defined for " + strSubstring);
                }
                if (c <= 16) {
                    dataBuffer.setElem(i4 + i3, paletteEntry.index);
                } else {
                    dataBuffer.setElem(i4 + i3, paletteEntry.getBestARGB());
                }
                i4 = i5;
            }
            i2++;
            i = 1;
        }
        while (nextString) {
            sb.setLength(0);
            nextString = parseNextString(basicCParser, sb);
        }
        if (SharedPrefsTranslator.DELIMITER.equals(basicCParser.nextToken())) {
            return bufferedImage;
        }
        throw new ImageReadException("Last token wasn't ';'");
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        readXpmHeader(byteSource).dump(printWriter);
        return true;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public final BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        XpmParseResult xpmHeader = parseXpmHeader(byteSource);
        return readXpmImage(xpmHeader.xpmHeader, xpmHeader.cParser);
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

    private String pixelsForIndex(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        int length = 1;
        for (int i3 = 1; i3 < i2; i3++) {
            length *= WRITE_PALETTE.length;
        }
        for (int i4 = 0; i4 < i2; i4++) {
            int i5 = i / length;
            i -= i5 * length;
            length /= WRITE_PALETTE.length;
            sb.append(WRITE_PALETTE[i5]);
        }
        return sb.toString();
    }

    private String toColor(int i) {
        String hexString = Integer.toHexString(i);
        if (hexString.length() < 6) {
            char[] cArr = new char[6 - hexString.length()];
            Arrays.fill(cArr, '0');
            return "#" + new String(cArr) + hexString;
        }
        return "#" + hexString;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        String strPixelsForIndex;
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        if (!map2.isEmpty()) {
            throw new ImageWriteException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        PaletteFactory paletteFactory = new PaletteFactory();
        int i = 1;
        boolean zHasTransparency = paletteFactory.hasTransparency(bufferedImage, 1);
        SimplePalette simplePaletteMakeExactRgbPaletteSimple = null;
        int length = WRITE_PALETTE.length;
        while (simplePaletteMakeExactRgbPaletteSimple == null) {
            simplePaletteMakeExactRgbPaletteSimple = paletteFactory.makeExactRgbPaletteSimple(bufferedImage, zHasTransparency ? length - 1 : length);
            if (simplePaletteMakeExactRgbPaletteSimple == null) {
                length *= WRITE_PALETTE.length;
                i++;
            }
        }
        int length2 = simplePaletteMakeExactRgbPaletteSimple.length();
        if (zHasTransparency) {
            length2++;
        }
        outputStream.write("/* XPM */\n".getBytes("US-ASCII"));
        outputStream.write(("static char *" + randomName() + "[] = {\n").getBytes("US-ASCII"));
        outputStream.write(("\"" + bufferedImage.getWidth() + " " + bufferedImage.getHeight() + " " + length2 + " " + i + "\",\n").getBytes("US-ASCII"));
        int i2 = 0;
        while (i2 < length2) {
            outputStream.write(("\"" + pixelsForIndex(i2, i) + " c " + (i2 < simplePaletteMakeExactRgbPaletteSimple.length() ? toColor(simplePaletteMakeExactRgbPaletteSimple.getEntry(i2)) : "None") + "\",\n").getBytes("US-ASCII"));
            i2++;
        }
        String str = "";
        for (int i3 = 0; i3 < bufferedImage.getHeight(); i3++) {
            outputStream.write(str.getBytes("US-ASCII"));
            str = ",\n";
            outputStream.write("\"".getBytes("US-ASCII"));
            for (int i4 = 0; i4 < bufferedImage.getWidth(); i4++) {
                int rgb = bufferedImage.getRGB(i4, i3);
                if (((-16777216) & rgb) == 0) {
                    strPixelsForIndex = pixelsForIndex(simplePaletteMakeExactRgbPaletteSimple.length(), i);
                } else {
                    strPixelsForIndex = pixelsForIndex(simplePaletteMakeExactRgbPaletteSimple.getPaletteIndex(rgb & ViewCompat.MEASURED_SIZE_MASK), i);
                }
                outputStream.write(strPixelsForIndex.getBytes("US-ASCII"));
            }
            outputStream.write("\"".getBytes("US-ASCII"));
        }
        outputStream.write("\n};\n".getBytes("US-ASCII"));
    }
}
