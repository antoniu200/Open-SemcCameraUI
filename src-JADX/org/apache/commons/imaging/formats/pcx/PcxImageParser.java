package org.apache.commons.imaging.formats.pcx;

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
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PcxImageParser extends ImageParser {
    private static final String DEFAULT_EXTENSION = ".pcx";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION, ".pcc"};

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
        return "Pcx-Custom";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    public PcxImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.PCX};
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        PcxHeader pcxHeader = readPcxHeader(byteSource);
        Dimension imageSize = getImageSize(byteSource, map);
        return new ImageInfo("PCX", pcxHeader.bitsPerPixel * pcxHeader.nPlanes, new ArrayList(), ImageFormats.PCX, "ZSoft PCX Image", imageSize.height, "image/x-pcx", 1, pcxHeader.vDpi, Math.round(imageSize.getHeight() / pcxHeader.vDpi), pcxHeader.hDpi, Math.round(imageSize.getWidth() / pcxHeader.hDpi), imageSize.width, false, false, (pcxHeader.nPlanes == 3 && pcxHeader.bitsPerPixel == 8) ? false : true, ImageInfo.ColorType.RGB, pcxHeader.encoding == 1 ? ImageInfo.CompressionAlgorithm.RLE : ImageInfo.CompressionAlgorithm.NONE);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        PcxHeader pcxHeader = readPcxHeader(byteSource);
        int i = (pcxHeader.xMax - pcxHeader.xMin) + 1;
        if (i < 0) {
            throw new ImageReadException("Image width is negative");
        }
        int i2 = (pcxHeader.yMax - pcxHeader.yMin) + 1;
        if (i2 < 0) {
            throw new ImageReadException("Image height is negative");
        }
        return new Dimension(i, i2);
    }

    static class PcxHeader {
        public static final int ENCODING_RLE = 1;
        public static final int ENCODING_UNCOMPRESSED = 0;
        public static final int PALETTE_INFO_COLOR = 1;
        public static final int PALETTE_INFO_GRAYSCALE = 2;
        public final int bitsPerPixel;
        public final int bytesPerLine;
        public final int[] colormap;
        public final int encoding;
        public final int hDpi;
        public final int hScreenSize;
        public final int manufacturer;
        public final int nPlanes;
        public final int paletteInfo;
        public final int reserved;
        public final int vDpi;
        public final int vScreenSize;
        public final int version;
        public final int xMax;
        public final int xMin;
        public final int yMax;
        public final int yMin;

        public PcxHeader(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int[] iArr, int i11, int i12, int i13, int i14, int i15, int i16) {
            this.manufacturer = i;
            this.version = i2;
            this.encoding = i3;
            this.bitsPerPixel = i4;
            this.xMin = i5;
            this.yMin = i6;
            this.xMax = i7;
            this.yMax = i8;
            this.hDpi = i9;
            this.vDpi = i10;
            this.colormap = iArr;
            this.reserved = i11;
            this.nPlanes = i12;
            this.bytesPerLine = i13;
            this.paletteInfo = i14;
            this.hScreenSize = i15;
            this.vScreenSize = i16;
        }

        public void dump(PrintWriter printWriter) {
            printWriter.println("PcxHeader");
            printWriter.println("Manufacturer: " + this.manufacturer);
            printWriter.println("Version: " + this.version);
            printWriter.println("Encoding: " + this.encoding);
            printWriter.println("BitsPerPixel: " + this.bitsPerPixel);
            printWriter.println("xMin: " + this.xMin);
            printWriter.println("yMin: " + this.yMin);
            printWriter.println("xMax: " + this.xMax);
            printWriter.println("yMax: " + this.yMax);
            printWriter.println("hDpi: " + this.hDpi);
            printWriter.println("vDpi: " + this.vDpi);
            printWriter.print("ColorMap: ");
            for (int i = 0; i < this.colormap.length; i++) {
                if (i > 0) {
                    printWriter.print(",");
                }
                printWriter.print("(" + ((this.colormap[i] >> 16) & 255) + "," + ((this.colormap[i] >> 8) & 255) + "," + (this.colormap[i] & 255) + ")");
            }
            printWriter.println();
            printWriter.println("Reserved: " + this.reserved);
            printWriter.println("nPlanes: " + this.nPlanes);
            printWriter.println("BytesPerLine: " + this.bytesPerLine);
            printWriter.println("PaletteInfo: " + this.paletteInfo);
            printWriter.println("hScreenSize: " + this.hScreenSize);
            printWriter.println("vScreenSize: " + this.vScreenSize);
            printWriter.println();
        }
    }

    private PcxHeader readPcxHeader(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            PcxHeader pcxHeader = readPcxHeader(inputStream, false);
            IoUtils.closeQuietly(true, inputStream);
            return pcxHeader;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    private PcxHeader readPcxHeader(InputStream inputStream, boolean z) throws IOException, ImageReadException {
        byte[] bytes = BinaryFunctions.readBytes("PcxHeader", inputStream, 128, "Not a Valid PCX File");
        int i = 255 & bytes[0];
        int i2 = 255 & bytes[1];
        int i3 = 255 & bytes[2];
        int i4 = bytes[3] & 255;
        int uInt16 = ByteConversions.toUInt16(bytes, 4, getByteOrder());
        int uInt162 = ByteConversions.toUInt16(bytes, 6, getByteOrder());
        int uInt163 = ByteConversions.toUInt16(bytes, 8, getByteOrder());
        int uInt164 = ByteConversions.toUInt16(bytes, 10, getByteOrder());
        int uInt165 = ByteConversions.toUInt16(bytes, 12, getByteOrder());
        int uInt166 = ByteConversions.toUInt16(bytes, 14, getByteOrder());
        int[] iArr = new int[16];
        for (int i5 = 0; i5 < 16; i5++) {
            int i6 = 16 + (3 * i5);
            iArr[i5] = (-16777216) | ((255 & bytes[i6]) << 16) | ((255 & bytes[i6 + 1]) << 8) | (255 & bytes[i6 + 2]);
        }
        int i7 = 255 & bytes[64];
        int i8 = 255 & bytes[65];
        int uInt167 = ByteConversions.toUInt16(bytes, 66, getByteOrder());
        int uInt168 = ByteConversions.toUInt16(bytes, 68, getByteOrder());
        int uInt169 = ByteConversions.toUInt16(bytes, 70, getByteOrder());
        int uInt1610 = ByteConversions.toUInt16(bytes, 72, getByteOrder());
        if (i != 10) {
            throw new ImageReadException("Not a Valid PCX File: manufacturer is " + i);
        }
        if (z && uInt167 % 2 != 0) {
            throw new ImageReadException("Not a Valid PCX File: bytesPerLine is odd");
        }
        return new PcxHeader(i, i2, i3, i4, uInt16, uInt162, uInt163, uInt164, uInt165, uInt166, iArr, i7, i8, uInt167, uInt168, uInt169, uInt1610);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        readPcxHeader(byteSource).dump(printWriter);
        return true;
    }

    private void readScanLine(PcxHeader pcxHeader, InputStream inputStream, byte[] bArr) throws IOException, ImageReadException {
        int i;
        int i2 = 0;
        if (pcxHeader.encoding == 0) {
            while (i2 < bArr.length) {
                int i3 = inputStream.read(bArr, i2, bArr.length - i2);
                if (i3 < 0) {
                    throw new ImageReadException("Premature end of file reading image data");
                }
                i2 += i3;
            }
            return;
        }
        if (pcxHeader.encoding == 1) {
            int i4 = 0;
            while (i4 < bArr.length) {
                byte b = BinaryFunctions.readByte("Pixel", inputStream, "Error reading image data");
                if ((b & 192) == 192) {
                    i = b & 63;
                    b = BinaryFunctions.readByte("Pixel", inputStream, "Error reading image data");
                } else {
                    i = 1;
                }
                for (int i5 = 0; i5 < i; i5++) {
                    int i6 = i4 + i5;
                    if (i6 < bArr.length) {
                        bArr[i6] = b;
                    }
                }
                i4 += i;
            }
            return;
        }
        throw new ImageReadException("Invalid PCX encoding " + pcxHeader.encoding);
    }

    private int[] read256ColorPalette(InputStream inputStream) throws IOException {
        byte[] bytes = BinaryFunctions.readBytes("Palette", inputStream, 769, "Error reading palette");
        if (bytes[0] != 12) {
            return null;
        }
        int[] iArr = new int[256];
        for (int i = 0; i < iArr.length; i++) {
            int i2 = 1 + (3 * i);
            iArr[i] = ((bytes[i2] & 255) << 16) | ((bytes[i2 + 1] & 255) << 8) | (bytes[i2 + 2] & 255);
        }
        return iArr;
    }

    private int[] read256ColorPaletteFromEndOfFile(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                BinaryFunctions.skipBytes(inputStream, (int) (byteSource.getLength() - 769));
                int[] iArr = read256ColorPalette(inputStream);
                IoUtils.closeQuietly(true, inputStream);
                return iArr;
            } catch (Throwable th) {
                th = th;
                IoUtils.closeQuietly(false, inputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:87:0x01fd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.awt.image.BufferedImage readImage(org.apache.commons.imaging.formats.pcx.PcxImageParser.PcxHeader r25, java.io.InputStream r26, org.apache.commons.imaging.common.bytesource.ByteSource r27) throws java.io.IOException, org.apache.commons.imaging.ImageReadException {
        /*
            Method dump skipped, instructions count: 686
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.pcx.PcxImageParser.readImage(org.apache.commons.imaging.formats.pcx.PcxImageParser$PcxHeader, java.io.InputStream, org.apache.commons.imaging.common.bytesource.ByteSource):java.awt.image.BufferedImage");
    }

    @Override // org.apache.commons.imaging.ImageParser
    public final BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        InputStream inputStream;
        Object obj = (map == null ? new HashMap() : new HashMap(map)).get(ImagingConstants.PARAM_KEY_STRICT);
        boolean zBooleanValue = obj != null ? ((Boolean) obj).booleanValue() : false;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            BufferedImage image = readImage(readPcxHeader(inputStream, zBooleanValue), inputStream, byteSource);
            IoUtils.closeQuietly(true, inputStream);
            return image;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        new PcxWriter(map).writeImage(bufferedImage, outputStream);
    }
}
