// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import org.apache.commons.imaging.palette.SimplePalette;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.PixelDensity;
import java.io.OutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.common.ImageBuilder;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.util.Map;
import java.io.PrintWriter;
import java.io.PrintStream;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.FormatCompliance;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class BmpImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final int BITMAP_FILE_HEADER_SIZE = 14;
    private static final int BITMAP_INFO_HEADER_SIZE = 40;
    private static final int BI_BITFIELDS = 3;
    private static final int BI_RGB = 0;
    private static final int BI_RLE4 = 2;
    private static final int BI_RLE8 = 1;
    private static final byte[] BMP_HEADER_SIGNATURE;
    private static final String DEFAULT_EXTENSION = ".bmp";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".bmp" };
        BMP_HEADER_SIGNATURE = new byte[] { 66, 77 };
    }
    
    public BmpImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }
    
    private String getBmpTypeDescription(final int n, final int n2) {
        if (n == 66 && n2 == 77) {
            return "Windows 3.1x, 95, NT,";
        }
        if (n == 66 && n2 == 65) {
            return "OS/2 Bitmap Array";
        }
        if (n == 67 && n2 == 73) {
            return "OS/2 Color Icon";
        }
        if (n == 67 && n2 == 80) {
            return "OS/2 Color Pointer";
        }
        if (n == 73 && n2 == 67) {
            return "OS/2 Icon";
        }
        if (n == 80 && n2 == 84) {
            return "OS/2 Pointer";
        }
        return "Unknown";
    }
    
    private byte[] getRLEBytes(final InputStream inputStream, final int n) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i == 0) {
            final int b = BinaryFunctions.readByte("RLE a", inputStream, "BMP: Bad RLE") & 0xFF;
            byteArrayOutputStream.write(b);
            final int b2 = BinaryFunctions.readByte("RLE b", inputStream, "BMP: Bad RLE") & 0xFF;
            byteArrayOutputStream.write(b2);
            if (b == 0) {
                switch (b2) {
                    case 1: {
                        i = 1;
                        continue;
                    }
                    case 2: {
                        byteArrayOutputStream.write(BinaryFunctions.readByte("RLE c", inputStream, "BMP: Bad RLE") & 0xFF);
                        byteArrayOutputStream.write(BinaryFunctions.readByte("RLE d", inputStream, "BMP: Bad RLE") & 0xFF);
                    }
                    case 0: {
                        continue;
                    }
                    default: {
                        int n3;
                        final int n2 = n3 = b2 / n;
                        if (b2 % n > 0) {
                            n3 = n2 + 1;
                        }
                        int n4 = n3;
                        if (n3 % 2 != 0) {
                            n4 = n3 + 1;
                        }
                        byteArrayOutputStream.write(BinaryFunctions.readBytes("bytes", inputStream, n4, "RLE: Absolute Mode"));
                        continue;
                    }
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    private BmpHeaderInfo readBmpHeaderInfo(final InputStream inputStream, final FormatCompliance formatCompliance, final boolean b) throws ImageReadException, IOException {
        final byte byte1 = BinaryFunctions.readByte("Identifier1", inputStream, "Not a Valid BMP File");
        final byte byte2 = BinaryFunctions.readByte("Identifier2", inputStream, "Not a Valid BMP File");
        if (formatCompliance != null) {
            formatCompliance.compareBytes("Signature", BmpImageParser.BMP_HEADER_SIGNATURE, new byte[] { byte1, byte2 });
        }
        final int read4Bytes = BinaryFunctions.read4Bytes("File Size", inputStream, "Not a Valid BMP File", this.getByteOrder());
        final int read4Bytes2 = BinaryFunctions.read4Bytes("Reserved", inputStream, "Not a Valid BMP File", this.getByteOrder());
        final int read4Bytes3 = BinaryFunctions.read4Bytes("Bitmap Data Offset", inputStream, "Not a Valid BMP File", this.getByteOrder());
        final int read4Bytes4 = BinaryFunctions.read4Bytes("Bitmap Header Size", inputStream, "Not a Valid BMP File", this.getByteOrder());
        final BmpHeaderInfo.ColorSpace colorSpace = new BmpHeaderInfo.ColorSpace();
        colorSpace.red = new BmpHeaderInfo.ColorSpaceCoordinate();
        colorSpace.green = new BmpHeaderInfo.ColorSpaceCoordinate();
        colorSpace.blue = new BmpHeaderInfo.ColorSpaceCoordinate();
        if (read4Bytes4 >= 40) {
            final int read4Bytes5 = BinaryFunctions.read4Bytes("Width", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read4Bytes6 = BinaryFunctions.read4Bytes("Height", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read2Bytes = BinaryFunctions.read2Bytes("Planes", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read2Bytes2 = BinaryFunctions.read2Bytes("Bits Per Pixel", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read4Bytes7 = BinaryFunctions.read4Bytes("Compression", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read4Bytes8 = BinaryFunctions.read4Bytes("Bitmap Data Size", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read4Bytes9 = BinaryFunctions.read4Bytes("HResolution", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read4Bytes10 = BinaryFunctions.read4Bytes("VResolution", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read4Bytes11 = BinaryFunctions.read4Bytes("ColorsUsed", inputStream, "Not a Valid BMP File", this.getByteOrder());
            final int read4Bytes12 = BinaryFunctions.read4Bytes("ColorsImportant", inputStream, "Not a Valid BMP File", this.getByteOrder());
            int read4Bytes13;
            int read4Bytes14;
            int read4Bytes15;
            if (read4Bytes4 < 52 && read4Bytes7 != 3) {
                read4Bytes13 = 0;
                read4Bytes14 = 0;
                read4Bytes15 = 0;
            }
            else {
                read4Bytes13 = BinaryFunctions.read4Bytes("RedMask", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes14 = BinaryFunctions.read4Bytes("GreenMask", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes15 = BinaryFunctions.read4Bytes("BlueMask", inputStream, "Not a Valid BMP File", this.getByteOrder());
            }
            int read4Bytes16;
            if (read4Bytes4 >= 56) {
                read4Bytes16 = BinaryFunctions.read4Bytes("AlphaMask", inputStream, "Not a Valid BMP File", this.getByteOrder());
            }
            else {
                read4Bytes16 = 0;
            }
            int read4Bytes17;
            int read4Bytes18;
            int read4Bytes19;
            int read4Bytes20;
            if (read4Bytes4 >= 108) {
                read4Bytes17 = BinaryFunctions.read4Bytes("ColorSpaceType", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.red.x = BinaryFunctions.read4Bytes("ColorSpaceRedX", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.red.y = BinaryFunctions.read4Bytes("ColorSpaceRedY", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.red.z = BinaryFunctions.read4Bytes("ColorSpaceRedZ", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.green.x = BinaryFunctions.read4Bytes("ColorSpaceGreenX", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.green.y = BinaryFunctions.read4Bytes("ColorSpaceGreenY", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.green.z = BinaryFunctions.read4Bytes("ColorSpaceGreenZ", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.blue.x = BinaryFunctions.read4Bytes("ColorSpaceBlueX", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.blue.y = BinaryFunctions.read4Bytes("ColorSpaceBlueY", inputStream, "Not a Valid BMP File", this.getByteOrder());
                colorSpace.blue.z = BinaryFunctions.read4Bytes("ColorSpaceBlueZ", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes18 = BinaryFunctions.read4Bytes("GammaRed", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes19 = BinaryFunctions.read4Bytes("GammaGreen", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes20 = BinaryFunctions.read4Bytes("GammaBlue", inputStream, "Not a Valid BMP File", this.getByteOrder());
            }
            else {
                read4Bytes17 = 0;
                read4Bytes18 = 0;
                read4Bytes19 = 0;
                read4Bytes20 = 0;
            }
            int read4Bytes21;
            int read4Bytes22;
            int read4Bytes23;
            int read4Bytes24;
            if (read4Bytes4 >= 124) {
                read4Bytes21 = BinaryFunctions.read4Bytes("Intent", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes22 = BinaryFunctions.read4Bytes("ProfileData", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes23 = BinaryFunctions.read4Bytes("ProfileSize", inputStream, "Not a Valid BMP File", this.getByteOrder());
                read4Bytes24 = BinaryFunctions.read4Bytes("Reserved", inputStream, "Not a Valid BMP File", this.getByteOrder());
            }
            else {
                read4Bytes21 = 0;
                read4Bytes23 = 0;
                read4Bytes22 = 0;
                read4Bytes24 = 0;
            }
            if (b) {
                this.debugNumber("identifier1", byte1, 1);
                this.debugNumber("identifier2", byte2, 1);
                this.debugNumber("fileSize", read4Bytes, 4);
                this.debugNumber("reserved", read4Bytes2, 4);
                this.debugNumber("bitmapDataOffset", read4Bytes3, 4);
                this.debugNumber("bitmapHeaderSize", read4Bytes4, 4);
                this.debugNumber("width", read4Bytes5, 4);
                this.debugNumber("height", read4Bytes6, 4);
                this.debugNumber("planes", read2Bytes, 2);
                this.debugNumber("bitsPerPixel", read2Bytes2, 2);
                this.debugNumber("compression", read4Bytes7, 4);
                this.debugNumber("bitmapDataSize", read4Bytes8, 4);
                this.debugNumber("hResolution", read4Bytes9, 4);
                this.debugNumber("vResolution", read4Bytes10, 4);
                this.debugNumber("colorsUsed", read4Bytes11, 4);
                this.debugNumber("colorsImportant", read4Bytes12, 4);
                if (read4Bytes4 >= 52 || read4Bytes7 == 3) {
                    this.debugNumber("redMask", read4Bytes13, 4);
                    this.debugNumber("greenMask", read4Bytes14, 4);
                    this.debugNumber("blueMask", read4Bytes15, 4);
                }
                if (read4Bytes4 >= 56) {
                    this.debugNumber("alphaMask", read4Bytes16, 4);
                }
                if (read4Bytes4 >= 108) {
                    this.debugNumber("colorSpaceType", read4Bytes17, 4);
                    this.debugNumber("colorSpace.red.x", colorSpace.red.x, 1);
                    this.debugNumber("colorSpace.red.y", colorSpace.red.y, 1);
                    this.debugNumber("colorSpace.red.z", colorSpace.red.z, 1);
                    this.debugNumber("colorSpace.green.x", colorSpace.green.x, 1);
                    this.debugNumber("colorSpace.green.y", colorSpace.green.y, 1);
                    this.debugNumber("colorSpace.green.z", colorSpace.green.z, 1);
                    this.debugNumber("colorSpace.blue.x", colorSpace.blue.x, 1);
                    this.debugNumber("colorSpace.blue.y", colorSpace.blue.y, 1);
                    this.debugNumber("colorSpace.blue.z", colorSpace.blue.z, 1);
                    this.debugNumber("gammaRed", read4Bytes18, 4);
                    this.debugNumber("gammaGreen", read4Bytes19, 4);
                    this.debugNumber("gammaBlue", read4Bytes20, 4);
                }
                if (read4Bytes4 >= 124) {
                    this.debugNumber("intent", read4Bytes21, 4);
                    this.debugNumber("profileData", read4Bytes22, 4);
                    this.debugNumber("profileSize", read4Bytes23, 4);
                    this.debugNumber("reservedV5", read4Bytes24, 4);
                }
            }
            return new BmpHeaderInfo(byte1, byte2, read4Bytes, read4Bytes2, read4Bytes3, read4Bytes4, read4Bytes5, read4Bytes6, read2Bytes, read2Bytes2, read4Bytes7, read4Bytes8, read4Bytes9, read4Bytes10, read4Bytes11, read4Bytes12, read4Bytes13, read4Bytes14, read4Bytes15, read4Bytes16, read4Bytes17, colorSpace, read4Bytes18, read4Bytes19, read4Bytes20, read4Bytes21, read4Bytes22, read4Bytes23, read4Bytes24);
        }
        throw new ImageReadException("Invalid/unsupported BMP file");
    }
    
    private BmpHeaderInfo readBmpHeaderInfo(final ByteSource byteSource, final boolean b) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final BmpHeaderInfo bmpHeaderInfo = this.readBmpHeaderInfo(inputStream, null, b);
                IoUtils.closeQuietly(true, inputStream);
                return bmpHeaderInfo;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    private ImageContents readImageContents(final InputStream inputStream, final FormatCompliance formatCompliance, final boolean b) throws ImageReadException, IOException {
        final BmpHeaderInfo bmpHeaderInfo = this.readBmpHeaderInfo(inputStream, formatCompliance, b);
        final int colorsUsed = bmpHeaderInfo.colorsUsed;
        final int n = 1;
        final int n2 = 1;
        int n3 = colorsUsed;
        if (colorsUsed == 0) {
            n3 = 1 << bmpHeaderInfo.bitsPerPixel;
        }
        if (b) {
            this.debugNumber("ColorsUsed", bmpHeaderInfo.colorsUsed, 4);
            this.debugNumber("BitsPerPixel", bmpHeaderInfo.bitsPerPixel, 4);
            this.debugNumber("ColorTableSize", n3, 4);
            this.debugNumber("bhi.colorsUsed", bmpHeaderInfo.colorsUsed, 4);
            this.debugNumber("Compression", bmpHeaderInfo.compression, 4);
        }
        final int compression = bmpHeaderInfo.compression;
        final int n4 = 0;
        int i = 0;
        int n6 = 0;
        int n7 = 0;
        Label_0329: {
            while (true) {
                Label_0320: {
                    Label_0314: {
                        switch (compression) {
                            default: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("BMP: Unknown Compression: ");
                                sb.append(bmpHeaderInfo.compression);
                                throw new ImageReadException(sb.toString());
                            }
                            case 3: {
                                if (b) {
                                    System.out.println("Compression: BI_BITFIELDS");
                                }
                                if (bmpHeaderInfo.bitsPerPixel <= 8) {
                                    i = n3 * 4;
                                    break Label_0314;
                                }
                                break Label_0320;
                            }
                            case 2: {
                                if (b) {
                                    System.out.println("Compression: BI_RLE4");
                                }
                                i = n3 * 4;
                                final int n5 = 2;
                                n6 = n;
                                n7 = n5;
                                break Label_0329;
                            }
                            case 1: {
                                if (b) {
                                    System.out.println("Compression: BI_RLE8");
                                }
                                final int n8 = n3 * 4;
                                n6 = n2;
                                i = n8;
                                break;
                            }
                            case 0: {
                                if (b) {
                                    System.out.println("Compression: BI_RGB");
                                }
                                if (bmpHeaderInfo.bitsPerPixel <= 8) {
                                    i = n3 * 4;
                                    break Label_0314;
                                }
                                break Label_0320;
                            }
                        }
                        n7 = n6;
                        break Label_0329;
                    }
                    n6 = 0;
                    continue;
                }
                i = 0;
                n6 = 0;
                continue;
            }
        }
        byte[] bytes = null;
        if (i > 0) {
            bytes = BinaryFunctions.readBytes("ColorTable", inputStream, i, "Not a Valid BMP File");
        }
        if (b) {
            this.debugNumber("paletteLength", i, 4);
            final PrintStream out = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("ColorTable: ");
            String string;
            if (bytes == null) {
                string = "null";
            }
            else {
                string = Integer.toString(bytes.length);
            }
            sb2.append(string);
            out.println(sb2.toString());
        }
        final int width = bmpHeaderInfo.width;
        final int height = bmpHeaderInfo.height;
        int n10;
        final int n9 = n10 = (bmpHeaderInfo.bitsPerPixel * bmpHeaderInfo.width + 7) / 8;
        if (b) {
            this.debugNumber("bhi.Width", bmpHeaderInfo.width, 4);
            this.debugNumber("bhi.Height", bmpHeaderInfo.height, 4);
            this.debugNumber("ImageLineLength", n9, 4);
            this.debugNumber("PixelCount", width * height, 4);
            n10 = n9;
        }
        while (n10 % 4 != 0) {
            ++n10;
        }
        final int bitmapHeaderSize = bmpHeaderInfo.bitmapHeaderSize;
        int n11 = n4;
        if (bmpHeaderInfo.bitmapHeaderSize == 40) {
            n11 = n4;
            if (bmpHeaderInfo.compression == 3) {
                n11 = 12;
            }
        }
        final int j = 14 + bitmapHeaderSize + n11;
        final int k = j + i;
        if (b) {
            this.debugNumber("bhi.BitmapDataOffset", bmpHeaderInfo.bitmapDataOffset, 4);
            this.debugNumber("expectedDataOffset", k, 4);
        }
        final int n12 = bmpHeaderInfo.bitmapDataOffset - k;
        if (n12 < 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("BMP has invalid image data offset: ");
            sb3.append(bmpHeaderInfo.bitmapDataOffset);
            sb3.append(" (expected: ");
            sb3.append(k);
            sb3.append(", paletteLength: ");
            sb3.append(i);
            sb3.append(", headerSize: ");
            sb3.append(j);
            sb3.append(")");
            throw new ImageReadException(sb3.toString());
        }
        if (n12 > 0) {
            BinaryFunctions.readBytes("BitmapDataOffset", inputStream, n12, "Not a Valid BMP File");
        }
        final int n13 = bmpHeaderInfo.height * n10;
        if (b) {
            this.debugNumber("imageDataSize", n13, 4);
        }
        byte[] array;
        if (n6 != 0) {
            array = this.getRLEBytes(inputStream, n7);
        }
        else {
            array = BinaryFunctions.readBytes("ImageData", inputStream, n13, "Not a Valid BMP File");
        }
        if (b) {
            this.debugNumber("ImageData.length", array.length, 4);
        }
        PixelParser pixelParser = null;
        switch (bmpHeaderInfo.compression) {
            default: {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("BMP: Unknown Compression: ");
                sb4.append(bmpHeaderInfo.compression);
                throw new ImageReadException(sb4.toString());
            }
            case 3: {
                pixelParser = new PixelParserBitFields(bmpHeaderInfo, bytes, array);
                break;
            }
            case 1:
            case 2: {
                pixelParser = new PixelParserRle(bmpHeaderInfo, bytes, array);
                break;
            }
            case 0: {
                pixelParser = new PixelParserRgb(bmpHeaderInfo, bytes, array);
                break;
            }
        }
        return new ImageContents(bmpHeaderInfo, bytes, array, pixelParser);
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("bmp.dumpImageFile");
        this.getImageInfo(byteSource, null).toString(printWriter, "");
        printWriter.println("");
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return BmpImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.BMP };
    }
    
    public BufferedImage getBufferedImage(final InputStream inputStream, final Map<String, Object> m) throws ImageReadException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        final boolean equals = Boolean.TRUE.equals(hashMap.get("VERBOSE"));
        if (hashMap.containsKey("VERBOSE")) {
            hashMap.remove("VERBOSE");
        }
        if (hashMap.containsKey("BUFFERED_IMAGE_FACTORY")) {
            hashMap.remove("BUFFERED_IMAGE_FACTORY");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageReadException(sb.toString());
        }
        final ImageContents imageContents = this.readImageContents(inputStream, FormatCompliance.getDefault(), equals);
        if (imageContents == null) {
            throw new ImageReadException("Couldn't read BMP Data");
        }
        final BmpHeaderInfo bhi = imageContents.bhi;
        final int width = bhi.width;
        final int height = bhi.height;
        if (equals) {
            final PrintStream out = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("width: ");
            sb2.append(width);
            out.println(sb2.toString());
            final PrintStream out2 = System.out;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("height: ");
            sb3.append(height);
            out2.println(sb3.toString());
            final PrintStream out3 = System.out;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("width*height: ");
            final int i = width * height;
            sb4.append(i);
            out3.println(sb4.toString());
            final PrintStream out4 = System.out;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("width*height*4: ");
            sb5.append(i * 4);
            out4.println(sb5.toString());
        }
        final PixelParser pixelParser = imageContents.pixelParser;
        final ImageBuilder imageBuilder = new ImageBuilder(width, height, true);
        pixelParser.processImage(imageBuilder);
        return imageBuilder.getBufferedImage();
    }
    
    @Override
    public BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final BufferedImage bufferedImage = this.getBufferedImage(inputStream, map);
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
        return ".bmp";
    }
    
    @Override
    public FormatCompliance getFormatCompliance(final ByteSource byteSource) throws ImageReadException, IOException {
        final FormatCompliance formatCompliance = new FormatCompliance(byteSource.getDescription());
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                this.readImageContents(inputStream, formatCompliance, false);
                IoUtils.closeQuietly(true, inputStream);
                return formatCompliance;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> m) throws ImageReadException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        final boolean equals = Boolean.TRUE.equals(hashMap.get("VERBOSE"));
        if (hashMap.containsKey("VERBOSE")) {
            hashMap.remove("VERBOSE");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageReadException(sb.toString());
        }
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final ImageContents imageContents = this.readImageContents(inputStream, FormatCompliance.getDefault(), equals);
                IoUtils.closeQuietly(true, inputStream);
                if (imageContents == null) {
                    throw new ImageReadException("Couldn't read BMP Data");
                }
                final BmpHeaderInfo bhi = imageContents.bhi;
                final byte[] colorTable = imageContents.colorTable;
                if (bhi == null) {
                    throw new ImageReadException("BMP: couldn't read header");
                }
                final int height = bhi.height;
                final int width = bhi.width;
                final ArrayList list = new ArrayList();
                final int bitsPerPixel = bhi.bitsPerPixel;
                final ImageFormats bmp = ImageFormats.BMP;
                final int n = (int)(bhi.hResolution * 0.0254);
                final float n2 = (float)(width / (double)n);
                final int n3 = (int)(bhi.vResolution * 0.0254);
                final float n4 = (float)(height / (double)n3);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Bmp (");
                sb2.append((char)bhi.identifier1);
                sb2.append((char)bhi.identifier2);
                sb2.append(": ");
                sb2.append(this.getBmpTypeDescription(bhi.identifier1, bhi.identifier2));
                sb2.append(")");
                return new ImageInfo(sb2.toString(), bitsPerPixel, list, bmp, "BMP Windows Bitmap", height, "image/x-ms-bmp", -1, n3, n4, n, n2, width, false, false, colorTable != null, ImageInfo.ColorType.RGB, ImageInfo.CompressionAlgorithm.RLE);
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> m) throws ImageReadException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        final boolean equals = Boolean.TRUE.equals(hashMap.get("VERBOSE"));
        if (hashMap.containsKey("VERBOSE")) {
            hashMap.remove("VERBOSE");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageReadException(sb.toString());
        }
        final BmpHeaderInfo bmpHeaderInfo = this.readBmpHeaderInfo(byteSource, equals);
        if (bmpHeaderInfo == null) {
            throw new ImageReadException("BMP: couldn't read header");
        }
        return new Dimension(bmpHeaderInfo.width, bmpHeaderInfo.height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "Bmp-Custom";
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
        PixelDensity pixelDensity = null;
        if (hashMap.containsKey("FORMAT")) {
            hashMap.remove("FORMAT");
        }
        if (hashMap.containsKey("PIXEL_DENSITY")) {
            pixelDensity = (PixelDensity)hashMap.remove("PIXEL_DENSITY");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageWriteException(sb.toString());
        }
        final SimplePalette exactRgbPaletteSimple = new PaletteFactory().makeExactRgbPaletteSimple(bufferedImage, 256);
        BmpWriter bmpWriter;
        if (exactRgbPaletteSimple == null) {
            bmpWriter = new BmpWriterRgb();
        }
        else {
            bmpWriter = new BmpWriterPalette(exactRgbPaletteSimple);
        }
        final byte[] imageData = bmpWriter.getImageData(bufferedImage);
        final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        outputStream.write(66);
        outputStream.write(77);
        binaryOutputStream.write4Bytes(bmpWriter.getPaletteSize() * 4 + 54 + imageData.length);
        binaryOutputStream.write4Bytes(0);
        binaryOutputStream.write4Bytes(54 + 4 * bmpWriter.getPaletteSize());
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        binaryOutputStream.write4Bytes(40);
        binaryOutputStream.write4Bytes(width);
        binaryOutputStream.write4Bytes(height);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(bmpWriter.getBitsPerPixel());
        binaryOutputStream.write4Bytes(0);
        binaryOutputStream.write4Bytes(imageData.length);
        int n;
        if (pixelDensity != null) {
            n = (int)Math.round(pixelDensity.horizontalDensityMetres());
        }
        else {
            n = 0;
        }
        binaryOutputStream.write4Bytes(n);
        int n2;
        if (pixelDensity != null) {
            n2 = (int)Math.round(pixelDensity.verticalDensityMetres());
        }
        else {
            n2 = 0;
        }
        binaryOutputStream.write4Bytes(n2);
        if (exactRgbPaletteSimple == null) {
            binaryOutputStream.write4Bytes(0);
        }
        else {
            binaryOutputStream.write4Bytes(exactRgbPaletteSimple.length());
        }
        binaryOutputStream.write4Bytes(0);
        bmpWriter.writePalette(binaryOutputStream);
        binaryOutputStream.write(imageData);
    }
}
