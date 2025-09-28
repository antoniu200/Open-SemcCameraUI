package org.apache.commons.imaging.formats.pnm;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PnmImageParser extends ImageParser {
    public static final String PARAM_KEY_PNM_RAWBITS = "PNM_RAWBITS";
    public static final String PARAM_VALUE_PNM_RAWBITS_NO = "NO";
    public static final String PARAM_VALUE_PNM_RAWBITS_YES = "YES";
    private static final String DEFAULT_EXTENSION = ".pnm";
    private static final String[] ACCEPTED_EXTENSIONS = {".pbm", ".pgm", ".ppm", DEFAULT_EXTENSION, ".pam"};

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
        return "Pbm-Custom";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    public PnmImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.PBM, ImageFormats.PGM, ImageFormats.PPM, ImageFormats.PNM, ImageFormats.PAM};
    }

    private FileInfo readHeader(InputStream inputStream) throws IOException, NumberFormatException, ImageReadException {
        byte b = BinaryFunctions.readByte("Identifier1", inputStream, "Not a Valid PNM File");
        byte b2 = BinaryFunctions.readByte("Identifier2", inputStream, "Not a Valid PNM File");
        if (b != 80) {
            throw new ImageReadException("PNM file has invalid prefix byte 1");
        }
        WhiteSpaceReader whiteSpaceReader = new WhiteSpaceReader(inputStream);
        if (b2 == 49 || b2 == 52 || b2 == 50 || b2 == 53 || b2 == 51 || b2 == 54) {
            int i = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
            int i2 = Integer.parseInt(whiteSpaceReader.readtoWhiteSpace());
            if (b2 == 49) {
                return new PbmFileInfo(i, i2, false);
            }
            if (b2 == 52) {
                return new PbmFileInfo(i, i2, true);
            }
            if (b2 == 50) {
                return new PgmFileInfo(i, i2, false, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (b2 == 53) {
                return new PgmFileInfo(i, i2, true, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (b2 == 51) {
                return new PpmFileInfo(i, i2, false, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            if (b2 == 54) {
                return new PpmFileInfo(i, i2, true, Integer.parseInt(whiteSpaceReader.readtoWhiteSpace()));
            }
            throw new ImageReadException("PNM file has invalid header.");
        }
        if (b2 == 55) {
            StringBuilder sb = new StringBuilder();
            whiteSpaceReader.readLine();
            int i3 = -1;
            int i4 = -1;
            int i5 = -1;
            int i6 = -1;
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            while (true) {
                String line = whiteSpaceReader.readLine();
                if (line == null) {
                    break;
                }
                String strTrim = line.trim();
                if (strTrim.charAt(0) != '#') {
                    StringTokenizer stringTokenizer = new StringTokenizer(strTrim, " ", false);
                    String strNextToken = stringTokenizer.nextToken();
                    if ("WIDTH".equals(strNextToken)) {
                        i3 = Integer.parseInt(stringTokenizer.nextToken());
                        z2 = true;
                    } else if ("HEIGHT".equals(strNextToken)) {
                        i4 = Integer.parseInt(stringTokenizer.nextToken());
                        z3 = true;
                    } else if ("DEPTH".equals(strNextToken)) {
                        i5 = Integer.parseInt(stringTokenizer.nextToken());
                        z4 = true;
                    } else if ("MAXVAL".equals(strNextToken)) {
                        i6 = Integer.parseInt(stringTokenizer.nextToken());
                        z5 = true;
                    } else if ("TUPLTYPE".equals(strNextToken)) {
                        sb.append(stringTokenizer.nextToken());
                        z = true;
                    } else if (!"ENDHDR".equals(strNextToken)) {
                        throw new ImageReadException("Invalid PAM file header type " + strNextToken);
                    }
                }
            }
            if (!z2) {
                throw new ImageReadException("PAM header has no WIDTH");
            }
            if (!z3) {
                throw new ImageReadException("PAM header has no HEIGHT");
            }
            if (!z4) {
                throw new ImageReadException("PAM header has no DEPTH");
            }
            if (!z5) {
                throw new ImageReadException("PAM header has no MAXVAL");
            }
            if (!z) {
                throw new ImageReadException("PAM header has no TUPLTYPE");
            }
            return new PamFileInfo(i3, i4, i5, i6, sb.toString());
        }
        throw new ImageReadException("PNM file has invalid prefix byte 2");
    }

    private FileInfo readHeader(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            FileInfo header = readHeader(inputStream);
            IoUtils.closeQuietly(true, inputStream);
            return header;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        FileInfo header = readHeader(byteSource);
        if (header == null) {
            throw new ImageReadException("PNM: Couldn't read Header");
        }
        return new Dimension(header.width, header.height);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        FileInfo header = readHeader(byteSource);
        if (header == null) {
            throw new ImageReadException("PNM: Couldn't read Header");
        }
        ArrayList arrayList = new ArrayList();
        return new ImageInfo(header.getImageTypeDescription(), header.getBitDepth() * header.getNumComponents(), arrayList, header.getImageType(), header.getImageTypeDescription(), header.height, header.getMIMEType(), 1, 72, (float) (header.height / 72.0d), 72, (float) (header.width / 72.0d), header.width, false, header.hasAlpha(), false, header.getColorType(), ImageInfo.CompressionAlgorithm.NONE);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        printWriter.println("pnm.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        printWriter.println("");
        return true;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            FileInfo header = readHeader(inputStream);
            ImageBuilder imageBuilder = new ImageBuilder(header.width, header.height, header.hasAlpha());
            header.readImage(imageBuilder, inputStream);
            BufferedImage bufferedImage = imageBuilder.getBufferedImage();
            IoUtils.closeQuietly(true, inputStream);
            return bufferedImage;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        HashMap map2;
        boolean zHasTransparency = new PaletteFactory().hasTransparency(bufferedImage);
        PnmWriter ppmWriter = null;
        boolean z = true;
        if (map != null) {
            Object obj = map.get(PARAM_KEY_PNM_RAWBITS);
            if (obj != null && obj.equals(PARAM_VALUE_PNM_RAWBITS_NO)) {
                z = false;
            }
            Object obj2 = map.get(ImagingConstants.PARAM_KEY_FORMAT);
            if (obj2 != null) {
                if (obj2.equals(ImageFormats.PBM)) {
                    ppmWriter = new PbmWriter(z);
                } else if (obj2.equals(ImageFormats.PGM)) {
                    ppmWriter = new PgmWriter(z);
                } else if (obj2.equals(ImageFormats.PPM)) {
                    ppmWriter = new PpmWriter(z);
                } else if (obj2.equals(ImageFormats.PAM)) {
                    ppmWriter = new PamWriter();
                }
            }
        }
        if (ppmWriter == null) {
            if (zHasTransparency) {
                ppmWriter = new PamWriter();
            } else {
                ppmWriter = new PpmWriter(z);
            }
        }
        if (map != null) {
            map2 = new HashMap(map);
        } else {
            map2 = new HashMap();
        }
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        if (!map2.isEmpty()) {
            throw new ImageWriteException("Unknown parameter: " + ((Object) map2.keySet().iterator().next()));
        }
        ppmWriter.writeImage(bufferedImage, outputStream, map2);
    }
}
