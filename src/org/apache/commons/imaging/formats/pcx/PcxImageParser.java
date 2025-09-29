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

    private BufferedImage readImage(final PcxHeader pcxHeader, final InputStream inputStream, final ByteSource byteSource) throws ImageReadException, IOException {
        final int w = pcxHeader.xMax - pcxHeader.xMin + 1;
        if (w < 0) {
            throw new ImageReadException("Image width is negative");
        }
        final int h = pcxHeader.yMax - pcxHeader.yMin + 1;
        if (h < 0) {
            throw new ImageReadException("Image height is negative");
        }
        final byte[] array = new byte[pcxHeader.bytesPerLine * pcxHeader.nPlanes];
        if ((pcxHeader.bitsPerPixel == 1 || pcxHeader.bitsPerPixel == 2 || pcxHeader.bitsPerPixel == 4 || pcxHeader.bitsPerPixel == 8) && pcxHeader.nPlanes == 1) {
            final int scanlineStride = (pcxHeader.bitsPerPixel * w + 7) / 8;
            final byte[] dataArray = new byte[h * scanlineStride];
            for (int i = 0; i < h; ++i) {
                this.readScanLine(pcxHeader, inputStream, array);
                System.arraycopy(array, 0, dataArray, i * scanlineStride, scanlineStride);
            }
            final DataBufferByte dataBufferByte = new DataBufferByte(dataArray, dataArray.length);
            int[] colormap;
            if (pcxHeader.bitsPerPixel == 1) {
                final int[] array2;
                colormap = (array2 = new int[2]);
                array2[0] = 0;
                array2[1] = 16777215;
            }
            else if (pcxHeader.bitsPerPixel == 8) {
                final int[] read256ColorPalette = this.read256ColorPalette(inputStream);
                int[] read256ColorPaletteFromEndOfFile;
                if (read256ColorPalette == null) {
                    read256ColorPaletteFromEndOfFile = this.read256ColorPaletteFromEndOfFile(byteSource);
                }
                else {
                    read256ColorPaletteFromEndOfFile = read256ColorPalette;
                }
                colormap = read256ColorPaletteFromEndOfFile;
                if (read256ColorPaletteFromEndOfFile == null) {
                    throw new ImageReadException("No 256 color palette found in image that needs it");
                }
            }
            else {
                colormap = pcxHeader.colormap;
            }
            WritableRaster raster;
            if (pcxHeader.bitsPerPixel == 8) {
                raster = Raster.createInterleavedRaster(dataBufferByte, w, h, scanlineStride, 1, new int[] { 0 }, null);
            }
            else {
                raster = Raster.createPackedRaster(dataBufferByte, w, h, pcxHeader.bitsPerPixel, null);
            }
            final IndexColorModel cm = new IndexColorModel(pcxHeader.bitsPerPixel, 1 << pcxHeader.bitsPerPixel, colormap, 0, false, -1, 0);
            return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), new Properties());
        }
        if (pcxHeader.bitsPerPixel == 1 && 2 <= pcxHeader.nPlanes && pcxHeader.nPlanes <= 4) {
            final BufferedImage bufferedImage = new BufferedImage(w, h, 12, new IndexColorModel(pcxHeader.nPlanes, 1 << pcxHeader.nPlanes, pcxHeader.colormap, 0, false, -1, 0));
            final byte[] array3 = new byte[w];
            for (int j = 0; j < h; ++j) {
                this.readScanLine(pcxHeader, inputStream, array);
                Arrays.fill(array3, (byte)0);
                int k = 0;
                int n = 0;
                while (k < pcxHeader.nPlanes) {
                    for (int l = 0; l < pcxHeader.bytesPerLine; ++l, ++n) {
                        final byte b = array[n];
                        for (int n2 = 0; n2 < 8; ++n2) {
                            final int n3 = 8 * l + n2;
                            if (n3 >= array3.length) {
                                break;
                            }
                            array3[n3] |= (byte)(((b & 0xFF) >> 7 - n2 & 0x1) << k);
                        }
                    }
                    ++k;
                }
                bufferedImage.getRaster().setDataElements(0, j, w, 1, array3);
            }
            return bufferedImage;
        }
        if (pcxHeader.bitsPerPixel == 8 && pcxHeader.nPlanes == 3) {
            final byte[][] dataArray2 = new byte[3][];
            final int n4 = w * h;
            dataArray2[0] = new byte[n4];
            dataArray2[1] = new byte[n4];
            dataArray2[2] = new byte[n4];
            for (int n5 = 0; n5 < h; ++n5) {
                this.readScanLine(pcxHeader, inputStream, array);
                final byte[] array4 = dataArray2[0];
                final int n6 = n5 * w;
                System.arraycopy(array, 0, array4, n6, w);
                System.arraycopy(array, pcxHeader.bytesPerLine, dataArray2[1], n6, w);
                System.arraycopy(array, pcxHeader.bytesPerLine * 2, dataArray2[2], n6, w);
            }
            final WritableRaster bandedRaster = Raster.createBandedRaster(new DataBufferByte(dataArray2, dataArray2[0].length), w, h, w, new int[] { 0, 1, 2 }, new int[] { 0, 0, 0 }, null);
            final ComponentColorModel cm2 = new ComponentColorModel(ColorSpace.getInstance(1000), false, false, 1, 0);
            return new BufferedImage(cm2, bandedRaster, cm2.isAlphaPremultiplied(), new Properties());
        }
        if ((pcxHeader.bitsPerPixel == 24 && pcxHeader.nPlanes == 1) || (pcxHeader.bitsPerPixel == 32 && pcxHeader.nPlanes == 1)) {
            final int scanlineStride2 = 3 * w;
            final byte[] dataArray3 = new byte[scanlineStride2 * h];
            for (int n7 = 0; n7 < h; ++n7) {
                this.readScanLine(pcxHeader, inputStream, array);
                if (pcxHeader.bitsPerPixel == 24) {
                    System.arraycopy(array, 0, dataArray3, n7 * scanlineStride2, scanlineStride2);
                }
                else {
                    for (int n8 = 0; n8 < w; ++n8) {
                        final int n9 = n7 * scanlineStride2 + 3 * n8;
                        final int n10 = 4 * n8;
                        dataArray3[n9] = array[n10];
                        dataArray3[n9 + 1] = array[n10 + 1];
                        dataArray3[n9 + 2] = array[n10 + 2];
                    }
                }
            }
            final WritableRaster interleavedRaster = Raster.createInterleavedRaster(new DataBufferByte(dataArray3, dataArray3.length), w, h, scanlineStride2, 3, new int[] { 2, 1, 0 }, null);
            final ComponentColorModel cm3 = new ComponentColorModel(ColorSpace.getInstance(1000), false, false, 1, 0);
            return new BufferedImage(cm3, interleavedRaster, cm3.isAlphaPremultiplied(), new Properties());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid/unsupported image with bitsPerPixel ");
        sb.append(pcxHeader.bitsPerPixel);
        sb.append(" and planes ");
        sb.append(pcxHeader.nPlanes);
        throw new ImageReadException(sb.toString());
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
