// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pcx;

import org.apache.commons.imaging.ImageWriteException;
import java.io.OutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageInfo;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.io.PrintWriter;
import org.apache.commons.imaging.common.ByteConversions;
import java.awt.image.WritableRaster;
import java.awt.image.ComponentColorModel;
import java.awt.color.ColorSpace;
import java.util.Arrays;
import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.util.Properties;
import java.awt.image.IndexColorModel;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.DataBufferByte;
import org.apache.commons.imaging.ImageReadException;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class PcxImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".pcx";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".pcx", ".pcc" };
    }
    
    public PcxImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }
    
    private int[] read256ColorPalette(final InputStream inputStream) throws IOException {
        final byte[] bytes = BinaryFunctions.readBytes("Palette", inputStream, 769, "Error reading palette");
        int i = 0;
        if (bytes[0] != 12) {
            return null;
        }
        int[] array;
        for (array = new int[256]; i < array.length; ++i) {
            final int n = 1 + 3 * i;
            array[i] = ((bytes[n] & 0xFF) << 16 | (bytes[n + 1] & 0xFF) << 8 | (bytes[n + 2] & 0xFF));
        }
        return array;
    }
    
    private int[] read256ColorPaletteFromEndOfFile(final ByteSource byteSource) throws IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                BinaryFunctions.skipBytes(inputStream, (int)(byteSource.getLength() - 769L));
                final int[] read256ColorPalette = this.read256ColorPalette(inputStream);
                IoUtils.closeQuietly(true, inputStream);
                return read256ColorPalette;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
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
    
    private PcxHeader readPcxHeader(final InputStream inputStream, final boolean b) throws ImageReadException, IOException {
        final byte[] bytes = BinaryFunctions.readBytes("PcxHeader", inputStream, 128, "Not a Valid PCX File");
        final int i = 0xFF & bytes[0];
        final byte b2 = bytes[1];
        final byte b3 = bytes[2];
        final byte b4 = bytes[3];
        final int uInt16 = ByteConversions.toUInt16(bytes, 4, this.getByteOrder());
        final int uInt17 = ByteConversions.toUInt16(bytes, 6, this.getByteOrder());
        final int uInt18 = ByteConversions.toUInt16(bytes, 8, this.getByteOrder());
        final int uInt19 = ByteConversions.toUInt16(bytes, 10, this.getByteOrder());
        final int uInt20 = ByteConversions.toUInt16(bytes, 12, this.getByteOrder());
        final int uInt21 = ByteConversions.toUInt16(bytes, 14, this.getByteOrder());
        final int[] array = new int[16];
        for (int j = 0; j < 16; ++j) {
            final int n = 16 + 3 * j;
            array[j] = (0xFF000000 | (0xFF & bytes[n]) << 16 | (0xFF & bytes[n + 1]) << 8 | (0xFF & bytes[n + 2]));
        }
        final byte b5 = bytes[64];
        final byte b6 = bytes[65];
        final int uInt22 = ByteConversions.toUInt16(bytes, 66, this.getByteOrder());
        final int uInt23 = ByteConversions.toUInt16(bytes, 68, this.getByteOrder());
        final int uInt24 = ByteConversions.toUInt16(bytes, 70, this.getByteOrder());
        final int uInt25 = ByteConversions.toUInt16(bytes, 72, this.getByteOrder());
        if (i != 10) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Not a Valid PCX File: manufacturer is ");
            sb.append(i);
            throw new ImageReadException(sb.toString());
        }
        if (b && uInt22 % 2 != 0) {
            throw new ImageReadException("Not a Valid PCX File: bytesPerLine is odd");
        }
        return new PcxHeader(i, 0xFF & b2, 0xFF & b3, b4 & 0xFF, uInt16, uInt17, uInt18, uInt19, uInt20, uInt21, array, 0xFF & b5, 0xFF & b6, uInt22, uInt23, uInt24, uInt25);
    }
    
    private PcxHeader readPcxHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final PcxHeader pcxHeader = this.readPcxHeader(inputStream, false);
                IoUtils.closeQuietly(true, inputStream);
                return pcxHeader;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    private void readScanLine(final PcxHeader pcxHeader, final InputStream inputStream, final byte[] b) throws IOException, ImageReadException {
        final int encoding = pcxHeader.encoding;
        int i = 0;
        if (encoding == 0) {
            while (i < b.length) {
                final int read = inputStream.read(b, i, b.length - i);
                if (read < 0) {
                    throw new ImageReadException("Premature end of file reading image data");
                }
                i += read;
            }
        }
        else {
            if (pcxHeader.encoding != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid PCX encoding ");
                sb.append(pcxHeader.encoding);
                throw new ImageReadException(sb.toString());
            }
            int n;
            for (int j = 0; j < b.length; j += n) {
                byte byte1 = BinaryFunctions.readByte("Pixel", inputStream, "Error reading image data");
                if ((byte1 & 0xC0) == 0xC0) {
                    final byte byte2 = BinaryFunctions.readByte("Pixel", inputStream, "Error reading image data");
                    n = (byte1 & 0x3F);
                    byte1 = byte2;
                }
                else {
                    n = 1;
                }
                for (int k = 0; k < n; ++k) {
                    final int n2 = j + k;
                    if (n2 >= b.length) {
                        break;
                    }
                    b[n2] = byte1;
                }
            }
        }
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        this.readPcxHeader(byteSource).dump(printWriter);
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return PcxImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.PCX };
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> m) throws ImageReadException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        final Object value = hashMap.get("STRICT");
        boolean booleanValue;
        if (value != null) {
            booleanValue = (boolean)value;
        }
        else {
            booleanValue = false;
        }
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final BufferedImage image = this.readImage(this.readPcxHeader(inputStream, booleanValue), inputStream, byteSource);
                IoUtils.closeQuietly(true, inputStream);
                return image;
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
        return ".pcx";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final PcxHeader pcxHeader = this.readPcxHeader(byteSource);
        final Dimension imageSize = this.getImageSize(byteSource, map);
        final int nPlanes = pcxHeader.nPlanes;
        final int bitsPerPixel = pcxHeader.bitsPerPixel;
        final ArrayList list = new ArrayList();
        final ImageFormats pcx = ImageFormats.PCX;
        final int height = imageSize.height;
        final int vDpi = pcxHeader.vDpi;
        final float n = (float)Math.round(imageSize.getHeight() / pcxHeader.vDpi);
        final int hDpi = pcxHeader.hDpi;
        final float n2 = (float)Math.round(imageSize.getWidth() / pcxHeader.hDpi);
        final int width = imageSize.width;
        final boolean b = pcxHeader.nPlanes != 3 || pcxHeader.bitsPerPixel != 8;
        final ImageInfo.ColorType rgb = ImageInfo.ColorType.RGB;
        ImageInfo.CompressionAlgorithm compressionAlgorithm;
        if (pcxHeader.encoding == 1) {
            compressionAlgorithm = ImageInfo.CompressionAlgorithm.RLE;
        }
        else {
            compressionAlgorithm = ImageInfo.CompressionAlgorithm.NONE;
        }
        return new ImageInfo("PCX", bitsPerPixel * nPlanes, list, pcx, "ZSoft PCX Image", height, "image/x-pcx", 1, vDpi, n, hDpi, n2, width, false, false, b, rgb, compressionAlgorithm);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final PcxHeader pcxHeader = this.readPcxHeader(byteSource);
        final int width = pcxHeader.xMax - pcxHeader.xMin + 1;
        if (width < 0) {
            throw new ImageReadException("Image width is negative");
        }
        final int height = pcxHeader.yMax - pcxHeader.yMin + 1;
        if (height < 0) {
            throw new ImageReadException("Image height is negative");
        }
        return new Dimension(width, height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "Pcx-Custom";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> map) throws ImageWriteException, IOException {
        new PcxWriter(map).writeImage(bufferedImage, outputStream);
    }
    
    static class PcxHeader
    {
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
        
        public PcxHeader(final int manufacturer, final int version, final int encoding, final int bitsPerPixel, final int xMin, final int yMin, final int xMax, final int yMax, final int hDpi, final int vDpi, final int[] colormap, final int reserved, final int nPlanes, final int bytesPerLine, final int paletteInfo, final int hScreenSize, final int vScreenSize) {
            this.manufacturer = manufacturer;
            this.version = version;
            this.encoding = encoding;
            this.bitsPerPixel = bitsPerPixel;
            this.xMin = xMin;
            this.yMin = yMin;
            this.xMax = xMax;
            this.yMax = yMax;
            this.hDpi = hDpi;
            this.vDpi = vDpi;
            this.colormap = colormap;
            this.reserved = reserved;
            this.nPlanes = nPlanes;
            this.bytesPerLine = bytesPerLine;
            this.paletteInfo = paletteInfo;
            this.hScreenSize = hScreenSize;
            this.vScreenSize = vScreenSize;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("PcxHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("Manufacturer: ");
            sb.append(this.manufacturer);
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Version: ");
            sb2.append(this.version);
            printWriter.println(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Encoding: ");
            sb3.append(this.encoding);
            printWriter.println(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("BitsPerPixel: ");
            sb4.append(this.bitsPerPixel);
            printWriter.println(sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("xMin: ");
            sb5.append(this.xMin);
            printWriter.println(sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("yMin: ");
            sb6.append(this.yMin);
            printWriter.println(sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("xMax: ");
            sb7.append(this.xMax);
            printWriter.println(sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("yMax: ");
            sb8.append(this.yMax);
            printWriter.println(sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("hDpi: ");
            sb9.append(this.hDpi);
            printWriter.println(sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("vDpi: ");
            sb10.append(this.vDpi);
            printWriter.println(sb10.toString());
            printWriter.print("ColorMap: ");
            for (int i = 0; i < this.colormap.length; ++i) {
                if (i > 0) {
                    printWriter.print(",");
                }
                final StringBuilder sb11 = new StringBuilder();
                sb11.append("(");
                sb11.append(this.colormap[i] >> 16 & 0xFF);
                sb11.append(",");
                sb11.append(this.colormap[i] >> 8 & 0xFF);
                sb11.append(",");
                sb11.append(this.colormap[i] & 0xFF);
                sb11.append(")");
                printWriter.print(sb11.toString());
            }
            printWriter.println();
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("Reserved: ");
            sb12.append(this.reserved);
            printWriter.println(sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("nPlanes: ");
            sb13.append(this.nPlanes);
            printWriter.println(sb13.toString());
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("BytesPerLine: ");
            sb14.append(this.bytesPerLine);
            printWriter.println(sb14.toString());
            final StringBuilder sb15 = new StringBuilder();
            sb15.append("PaletteInfo: ");
            sb15.append(this.paletteInfo);
            printWriter.println(sb15.toString());
            final StringBuilder sb16 = new StringBuilder();
            sb16.append("hScreenSize: ");
            sb16.append(this.hScreenSize);
            printWriter.println(sb16.toString());
            final StringBuilder sb17 = new StringBuilder();
            sb17.append("vScreenSize: ");
            sb17.append(this.vScreenSize);
            printWriter.println(sb17.toString());
            printWriter.println();
        }
    }
}
