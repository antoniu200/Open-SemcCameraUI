// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import org.apache.commons.imaging.ImageWriteException;
import java.util.HashMap;
import org.apache.commons.imaging.palette.PaletteFactory;
import java.io.OutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.imaging.common.ImageBuilder;
import java.awt.image.BufferedImage;
import java.util.Map;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import java.io.PrintWriter;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class PnmImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".pnm";
    public static final String PARAM_KEY_PNM_RAWBITS = "PNM_RAWBITS";
    public static final String PARAM_VALUE_PNM_RAWBITS_NO = "NO";
    public static final String PARAM_VALUE_PNM_RAWBITS_YES = "YES";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".pbm", ".pgm", ".ppm", ".pnm", ".pam" };
    }
    
    public PnmImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }
    
    private FileInfo readHeader(final InputStream inputStream) throws ImageReadException, IOException {
        final byte byte1 = BinaryFunctions.readByte("Identifier1", inputStream, "Not a Valid PNM File");
        final byte byte2 = BinaryFunctions.readByte("Identifier2", inputStream, "Not a Valid PNM File");
        if (byte1 != 80) {
            throw new ImageReadException("PNM file has invalid prefix byte 1");
        }
        final WhiteSpaceReader whiteSpaceReader = new WhiteSpaceReader(inputStream);
        if (byte2 != 49 && byte2 != 52 && byte2 != 50 && byte2 != 53 && byte2 != 51 && byte2 != 54) {
            if (byte2 != 55) {
                throw new ImageReadException("PNM file has invalid prefix byte 2");
            }
            final StringBuilder sb = new StringBuilder();
            whiteSpaceReader.readLine();
            int int1 = -1;
            int int2 = -1;
            int int4;
            int int3 = int4 = int2;
            boolean b = false;
            int n2;
            int n = n2 = 0;
            int n4;
            int n3 = n4 = n2;
            while (true) {
                final String line = whiteSpaceReader.readLine();
                if (line == null) {
                    break;
                }
                final String trim = line.trim();
                if (trim.charAt(0) == '#') {
                    continue;
                }
                final StringTokenizer stringTokenizer = new StringTokenizer(trim, " ", false);
                final String nextToken = stringTokenizer.nextToken();
                if ("WIDTH".equals(nextToken)) {
                    int1 = Integer.parseInt(stringTokenizer.nextToken());
                    n = 1;
                }
                else if ("HEIGHT".equals(nextToken)) {
                    int2 = Integer.parseInt(stringTokenizer.nextToken());
                    n2 = 1;
                }
                else if ("DEPTH".equals(nextToken)) {
                    int3 = Integer.parseInt(stringTokenizer.nextToken());
                    n3 = 1;
                }
                else if ("MAXVAL".equals(nextToken)) {
                    int4 = Integer.parseInt(stringTokenizer.nextToken());
                    n4 = 1;
                }
                else if ("TUPLTYPE".equals(nextToken)) {
                    sb.append(stringTokenizer.nextToken());
                    b = true;
                }
                else {
                    if ("ENDHDR".equals(nextToken)) {
                        break;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid PAM file header type ");
                    sb2.append(nextToken);
                    throw new ImageReadException(sb2.toString());
                }
            }
            if (n == 0) {
                throw new ImageReadException("PAM header has no WIDTH");
            }
            if (n2 == 0) {
                throw new ImageReadException("PAM header has no HEIGHT");
            }
            if (n3 == 0) {
                throw new ImageReadException("PAM header has no DEPTH");
            }
            if (n4 == 0) {
                throw new ImageReadException("PAM header has no MAXVAL");
            }
            if (!b) {
                throw new ImageReadException("PAM header has no TUPLTYPE");
            }
            return new PamFileInfo(int1, int2, int3, int4, sb.toString());
        }
        else {
            final int int5 = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
            final int int6 = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
            if (byte2 == 49) {
                return new PbmFileInfo(int5, int6, false);
            }
            if (byte2 == 52) {
                return new PbmFileInfo(int5, int6, true);
            }
            if (byte2 == 50) {
                return new PgmFileInfo(int5, int6, false, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (byte2 == 53) {
                return new PgmFileInfo(int5, int6, true, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (byte2 == 51) {
                return new PpmFileInfo(int5, int6, false, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (byte2 == 54) {
                return new PpmFileInfo(int5, int6, true, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            throw new ImageReadException("PNM file has invalid header.");
        }
    }
    
    private FileInfo readHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final FileInfo header = this.readHeader(inputStream);
                IoUtils.closeQuietly(true, inputStream);
                return header;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("pnm.dumpImageFile");
        final ImageInfo imageInfo = this.getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        printWriter.println("");
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return PnmImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.PBM, ImageFormats.PGM, ImageFormats.PPM, ImageFormats.PNM, ImageFormats.PAM };
    }
    
    @Override
    public BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final FileInfo header = this.readHeader(inputStream);
                final ImageBuilder imageBuilder = new ImageBuilder(header.width, header.height, header.hasAlpha());
                header.readImage(imageBuilder, inputStream);
                final BufferedImage bufferedImage = imageBuilder.getBufferedImage();
                IoUtils.closeQuietly(true, inputStream);
                return bufferedImage;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    @Override
    public String getDefaultExtension() {
        return ".pnm";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final FileInfo header = this.readHeader(byteSource);
        if (header == null) {
            throw new ImageReadException("PNM: Couldn't read Header");
        }
        return new ImageInfo(header.getImageTypeDescription(), header.getBitDepth() * header.getNumComponents(), new ArrayList<String>(), header.getImageType(), header.getImageTypeDescription(), header.height, header.getMIMEType(), 1, 72, (float)(header.height / 72.0), 72, (float)(header.width / 72.0), header.width, false, header.hasAlpha(), false, header.getColorType(), ImageInfo.CompressionAlgorithm.NONE);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final FileInfo header = this.readHeader(byteSource);
        if (header == null) {
            throw new ImageReadException("PNM: Couldn't read Header");
        }
        return new Dimension(header.width, header.height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "Pbm-Custom";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> m) throws ImageWriteException, IOException {
        final boolean hasTransparency = new PaletteFactory().hasTransparency(bufferedImage);
        final PnmWriter pnmWriter = null;
        int n = 1;
        final boolean b = true;
        PnmWriter pnmWriter2 = pnmWriter;
        if (m != null) {
            final Object value = m.get("PNM_RAWBITS");
            int n2 = b ? 1 : 0;
            if (value != null) {
                n2 = (b ? 1 : 0);
                if (value.equals("NO")) {
                    n2 = 0;
                }
            }
            final Object value2 = m.get("FORMAT");
            pnmWriter2 = pnmWriter;
            n = n2;
            if (value2 != null) {
                if (value2.equals(ImageFormats.PBM)) {
                    pnmWriter2 = new PbmWriter((boolean)(n2 != 0));
                    n = n2;
                }
                else if (value2.equals(ImageFormats.PGM)) {
                    pnmWriter2 = new PgmWriter((boolean)(n2 != 0));
                    n = n2;
                }
                else if (value2.equals(ImageFormats.PPM)) {
                    pnmWriter2 = new PpmWriter((boolean)(n2 != 0));
                    n = n2;
                }
                else {
                    pnmWriter2 = pnmWriter;
                    n = n2;
                    if (value2.equals(ImageFormats.PAM)) {
                        pnmWriter2 = new PamWriter();
                        n = n2;
                    }
                }
            }
        }
        PnmWriter pnmWriter3;
        if ((pnmWriter3 = pnmWriter2) == null) {
            if (hasTransparency) {
                pnmWriter3 = new PamWriter();
            }
            else {
                pnmWriter3 = new PpmWriter((boolean)(n != 0));
            }
        }
        HashMap hashMap;
        if (m != null) {
            hashMap = new HashMap(m);
        }
        else {
            hashMap = new HashMap();
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
        pnmWriter3.writeImage(bufferedImage, outputStream, hashMap);
    }
}
