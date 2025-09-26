// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.ico;

import org.apache.commons.imaging.palette.SimplePalette;
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.PixelDensity;
import java.util.HashMap;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.ImageInfo;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.imaging.ImageFormat;
import java.io.PrintWriter;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.imaging.formats.bmp.BmpImageParser;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.OutputStream;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.commons.imaging.ImageReadException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class IcoImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".ico";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".ico", ".cur" };
    }
    
    public IcoImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }
    
    private IconData readBitmapIconData(byte[] bytes, final IconInfo iconInfo) throws ImageReadException, IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        final int read4Bytes = BinaryFunctions.read4Bytes("size", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes2 = BinaryFunctions.read4Bytes("width", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes3 = BinaryFunctions.read4Bytes("height", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read2Bytes = BinaryFunctions.read2Bytes("planes", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read2Bytes2 = BinaryFunctions.read2Bytes("bitCount", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes4 = BinaryFunctions.read4Bytes("compression", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes5 = BinaryFunctions.read4Bytes("sizeImage", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes6 = BinaryFunctions.read4Bytes("xPelsPerMeter", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes7 = BinaryFunctions.read4Bytes("yPelsPerMeter", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes8 = BinaryFunctions.read4Bytes("colorsUsed", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read4Bytes9 = BinaryFunctions.read4Bytes("ColorsImportant", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        int read4Bytes11;
        int read4Bytes12;
        if (read4Bytes4 == 3) {
            final int read4Bytes10 = BinaryFunctions.read4Bytes("redMask", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
            read4Bytes11 = BinaryFunctions.read4Bytes("greenMask", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
            read4Bytes12 = BinaryFunctions.read4Bytes("blueMask", byteArrayInputStream, "Not a Valid ICO File", this.getByteOrder());
        }
        else {
            read4Bytes12 = 0;
            final int read4Bytes10 = 0;
            read4Bytes11 = 0;
        }
        final byte[] bytes2 = BinaryFunctions.readBytes("RestOfFile", byteArrayInputStream, byteArrayInputStream.available());
        if (read4Bytes != 40) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Not a Valid ICO File: Wrong bitmap header size ");
            sb.append(read4Bytes);
            throw new ImageReadException(sb.toString());
        }
        if (read2Bytes != 1) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Not a Valid ICO File: Planes can't be ");
            sb2.append(read2Bytes);
            throw new ImageReadException(sb2.toString());
        }
        int read4Bytes10;
        int n;
        int n2;
        int n3;
        int n4;
        if (read4Bytes4 == 0 && read2Bytes2 == 32) {
            read4Bytes10 = 16711680;
            n = 65280;
            n2 = -16777216;
            n3 = 255;
            n4 = 3;
        }
        else {
            final int n5 = read4Bytes11;
            final int n6 = 0;
            n4 = read4Bytes4;
            n2 = n6;
            n3 = read4Bytes12;
            n = n5;
        }
        final BitmapHeader bitmapHeader = new BitmapHeader(read4Bytes, read4Bytes2, read4Bytes3, read2Bytes, read2Bytes2, n4, read4Bytes5, read4Bytes6, read4Bytes7, read4Bytes8, read4Bytes9);
        int n7;
        if (read4Bytes8 == 0 && read2Bytes2 <= 8) {
            n7 = 1 << read2Bytes2;
        }
        else {
            n7 = read4Bytes8;
        }
        final int size = 70 + bytes2.length;
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(size);
        bytes = null;
        Closeable closeable;
        try {
            Object o = new BinaryOutputStream(byteArrayOutputStream, ByteOrder.LITTLE_ENDIAN);
            try {
                ((BinaryOutputStream)o).write(66);
                ((BinaryOutputStream)o).write(77);
                ((BinaryOutputStream)o).write4Bytes(size);
                try {
                    ((BinaryOutputStream)o).write4Bytes(0);
                    ((BinaryOutputStream)o).write4Bytes(n7 * 4 + 70);
                    ((BinaryOutputStream)o).write4Bytes(56);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes2);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes3 / 2);
                    ((BinaryOutputStream)o).write2Bytes(read2Bytes);
                    ((BinaryOutputStream)o).write2Bytes(read2Bytes2);
                    ((BinaryOutputStream)o).write4Bytes(n4);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes5);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes6);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes7);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes8);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes9);
                    ((BinaryOutputStream)o).write4Bytes(read4Bytes10);
                    ((BinaryOutputStream)o).write4Bytes(n);
                    ((BinaryOutputStream)o).write4Bytes(n3);
                    ((BinaryOutputStream)o).write4Bytes(n2);
                    ((BinaryOutputStream)o).write(bytes2);
                    ((BinaryOutputStream)o).flush();
                    IoUtils.closeQuietly(true, (Closeable)o);
                    o = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    final BufferedImage bufferedImage = new BmpImageParser().getBufferedImage((InputStream)o, null);
                    final int n8 = (read4Bytes2 + 7) / 8;
                    final int n9 = n8 % 4;
                    int n10 = n8;
                    if (n9 != 0) {
                        n10 = n8 + (4 - n9);
                    }
                    final int n11 = read4Bytes3 / 2;
                    try {
                        o = (bytes = BinaryFunctions.readBytes("transparency_map", (InputStream)o, n11 * n10, "Not a Valid ICO File"));
                    }
                    catch (final IOException o) {
                        if (read2Bytes2 != 32) {
                            throw o;
                        }
                    }
                    int n13;
                    if (read2Bytes2 == 32) {
                        int y = 0;
                        int n12 = 1;
                        while (true) {
                            n13 = n12;
                            if (n12 == 0) {
                                break;
                            }
                            n13 = n12;
                            if (y >= bufferedImage.getHeight()) {
                                break;
                            }
                            int x = 0;
                            int n14;
                            while (true) {
                                n14 = n12;
                                if (x >= bufferedImage.getWidth()) {
                                    break;
                                }
                                if ((bufferedImage.getRGB(x, y) & 0xFF000000) != 0x0) {
                                    n14 = 0;
                                    break;
                                }
                                ++x;
                            }
                            ++y;
                            n12 = n14;
                        }
                    }
                    else {
                        n13 = 1;
                    }
                    if (n13 != 0) {
                        final BufferedImage bufferedImage2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 2);
                        int n15 = 0;
                        while (true) {
                            o = bufferedImage2;
                            if (n15 >= bufferedImage2.getHeight()) {
                                break;
                            }
                            for (int i = 0; i < bufferedImage2.getWidth(); ++i) {
                                int n16;
                                if (bytes != null && ((0xFF & bytes[(bufferedImage.getHeight() - n15 - 1) * n10 + i / 8]) >> 7 - i % 8 & 0x1) != 0x0) {
                                    n16 = 0;
                                }
                                else {
                                    n16 = 255;
                                }
                                bufferedImage2.setRGB(i, n15, n16 << 24 | (0xFFFFFF & bufferedImage.getRGB(i, n15)));
                            }
                            ++n15;
                        }
                    }
                    else {
                        o = bufferedImage;
                    }
                    return (IconData)new BitmapIconData(iconInfo, bitmapHeader, (BufferedImage)o);
                }
                finally {}
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    private FileHeader readFileHeader(final InputStream inputStream) throws ImageReadException, IOException {
        final int read2Bytes = BinaryFunctions.read2Bytes("Reserved", inputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read2Bytes2 = BinaryFunctions.read2Bytes("IconType", inputStream, "Not a Valid ICO File", this.getByteOrder());
        final int read2Bytes3 = BinaryFunctions.read2Bytes("IconCount", inputStream, "Not a Valid ICO File", this.getByteOrder());
        if (read2Bytes != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Not a Valid ICO File: reserved is ");
            sb.append(read2Bytes);
            throw new ImageReadException(sb.toString());
        }
        if (read2Bytes2 != 1 && read2Bytes2 != 2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Not a Valid ICO File: icon type is ");
            sb2.append(read2Bytes2);
            throw new ImageReadException(sb2.toString());
        }
        return new FileHeader(read2Bytes, read2Bytes2, read2Bytes3);
    }
    
    private IconData readIconData(final byte[] array, final IconInfo iconInfo) throws ImageReadException, IOException {
        if (Imaging.guessFormat(array).equals(ImageFormats.PNG)) {
            return (IconData)new PNGIconData(iconInfo, Imaging.getBufferedImage(array));
        }
        return this.readBitmapIconData(array, iconInfo);
    }
    
    private IconInfo readIconInfo(final InputStream inputStream) throws IOException {
        return new IconInfo(BinaryFunctions.readByte("Width", inputStream, "Not a Valid ICO File"), BinaryFunctions.readByte("Height", inputStream, "Not a Valid ICO File"), BinaryFunctions.readByte("ColorCount", inputStream, "Not a Valid ICO File"), BinaryFunctions.readByte("Reserved", inputStream, "Not a Valid ICO File"), BinaryFunctions.read2Bytes("Planes", inputStream, "Not a Valid ICO File", this.getByteOrder()), BinaryFunctions.read2Bytes("BitCount", inputStream, "Not a Valid ICO File", this.getByteOrder()), BinaryFunctions.read4Bytes("ImageSize", inputStream, "Not a Valid ICO File", this.getByteOrder()), BinaryFunctions.read4Bytes("ImageOffset", inputStream, "Not a Valid ICO File", this.getByteOrder()));
    }
    
    private ImageContents readImage(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final FileHeader fileHeader = this.readFileHeader(inputStream);
                final IconInfo[] array = new IconInfo[fileHeader.iconCount];
                for (int i = 0; i < fileHeader.iconCount; ++i) {
                    array[i] = this.readIconInfo(inputStream);
                }
                final IconData[] array2 = new IconData[fileHeader.iconCount];
                for (int j = 0; j < fileHeader.iconCount; ++j) {
                    array2[j] = this.readIconData(byteSource.getBlock(array[j].imageOffset, array[j].imageSize), array[j]);
                }
                final ImageContents imageContents = new ImageContents(fileHeader, array2);
                IoUtils.closeQuietly(true, inputStream);
                return imageContents;
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
        final ImageContents image = this.readImage(byteSource);
        image.fileHeader.dump(printWriter);
        final IconData[] iconDatas = image.iconDatas;
        for (int length = iconDatas.length, i = 0; i < length; ++i) {
            iconDatas[i].dump(printWriter);
        }
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return IcoImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.ICO };
    }
    
    @Override
    public List<BufferedImage> getAllBufferedImages(final ByteSource byteSource) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        final ImageContents image = this.readImage(byteSource);
        final FileHeader fileHeader = image.fileHeader;
        for (int i = 0; i < fileHeader.iconCount; ++i) {
            list.add(image.iconDatas[i].readBufferedImage());
        }
        return list;
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageContents image = this.readImage(byteSource);
        if (image.fileHeader.iconCount > 0) {
            return image.iconDatas[0].readBufferedImage();
        }
        throw new ImageReadException("No icons in ICO file");
    }
    
    @Override
    public String getDefaultExtension() {
        return ".ico";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "ico-Custom";
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
        final PixelDensity pixelDensity = (PixelDensity)hashMap.remove("PIXEL_DENSITY");
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageWriteException(sb.toString());
        }
        final PaletteFactory paletteFactory = new PaletteFactory();
        final SimplePalette exactRgbPaletteSimple = paletteFactory.makeExactRgbPaletteSimple(bufferedImage, 256);
        final boolean hasTransparency = paletteFactory.hasTransparency(bufferedImage);
        int n;
        if (exactRgbPaletteSimple == null) {
            if (hasTransparency) {
                n = 32;
            }
            else {
                n = 24;
            }
        }
        else if (exactRgbPaletteSimple.length() <= 2) {
            n = 1;
        }
        else if (exactRgbPaletteSimple.length() <= 16) {
            n = 4;
        }
        else {
            n = 8;
        }
        final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        final int n2 = (bufferedImage.getWidth() * n + 7) / 8;
        final int n3 = n2 % 4;
        int n4 = n2;
        if (n3 != 0) {
            n4 = n2 + (4 - n3);
        }
        final int n5 = (bufferedImage.getWidth() + 7) / 8;
        final int n6 = n5 % 4;
        int n7 = n5;
        if (n6 != 0) {
            n7 = n5 + (4 - n6);
        }
        int n8;
        if (n <= 8) {
            n8 = 1 << n;
        }
        else {
            n8 = 0;
        }
        final int height = bufferedImage.getHeight();
        final int height2 = bufferedImage.getHeight();
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(1);
        int width = bufferedImage.getWidth();
        final int height3 = bufferedImage.getHeight();
        int n9;
        if (width > 255 || (n9 = height3) > 255) {
            width = 0;
            n9 = 0;
        }
        binaryOutputStream.write(width);
        binaryOutputStream.write(n9);
        int n10;
        if (n >= 8) {
            n10 = 0;
        }
        else {
            n10 = 1 << n;
        }
        binaryOutputStream.write(n10);
        binaryOutputStream.write(0);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(n);
        binaryOutputStream.write4Bytes(4 * n8 + 40 + height * n4 + height2 * n7);
        binaryOutputStream.write4Bytes(22);
        binaryOutputStream.write4Bytes(40);
        binaryOutputStream.write4Bytes(bufferedImage.getWidth());
        binaryOutputStream.write4Bytes(2 * bufferedImage.getHeight());
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(n);
        binaryOutputStream.write4Bytes(0);
        binaryOutputStream.write4Bytes(0);
        int n11;
        if (pixelDensity == null) {
            n11 = 0;
        }
        else {
            n11 = (int)Math.round(pixelDensity.horizontalDensityMetres());
        }
        binaryOutputStream.write4Bytes(n11);
        int n12;
        if (pixelDensity == null) {
            n12 = 0;
        }
        else {
            n12 = (int)Math.round(pixelDensity.horizontalDensityMetres());
        }
        binaryOutputStream.write4Bytes(n12);
        binaryOutputStream.write4Bytes(0);
        binaryOutputStream.write4Bytes(0);
        if (exactRgbPaletteSimple != null) {
            for (int i = 0; i < 1 << n; ++i) {
                if (i < exactRgbPaletteSimple.length()) {
                    final int entry = exactRgbPaletteSimple.getEntry(i);
                    binaryOutputStream.write(0xFF & entry);
                    binaryOutputStream.write(entry >> 8 & 0xFF);
                    binaryOutputStream.write(entry >> 16 & 0xFF);
                    binaryOutputStream.write(0);
                }
                else {
                    binaryOutputStream.write(0);
                    binaryOutputStream.write(0);
                    binaryOutputStream.write(0);
                    binaryOutputStream.write(0);
                }
            }
        }
        final int n13 = (bufferedImage.getWidth() * n + 7) / 8;
        int j = bufferedImage.getHeight() - 1;
        int n14 = 0;
        int n15 = 0;
        while (j >= 0) {
            int k = 0;
            int n16 = n14;
            int n17 = n15;
            while (k < bufferedImage.getWidth()) {
                final int rgb = bufferedImage.getRGB(k, j);
                int n19;
                int n20;
                if (n < 8) {
                    final int n18 = n16 << n | exactRgbPaletteSimple.getPaletteIndex(rgb & 0xFFFFFF);
                    n19 = n17 + n;
                    n20 = n18;
                    if (n19 >= 8) {
                        binaryOutputStream.write(0xFF & n18);
                        n19 = 0;
                        n20 = 0;
                    }
                }
                else if (n == 8) {
                    binaryOutputStream.write(exactRgbPaletteSimple.getPaletteIndex(rgb & 0xFFFFFF) & 0xFF);
                    n19 = n17;
                    n20 = n16;
                }
                else if (n == 24) {
                    binaryOutputStream.write(0xFF & rgb);
                    binaryOutputStream.write(rgb >> 8 & 0xFF);
                    binaryOutputStream.write(rgb >> 16 & 0xFF);
                    n19 = n17;
                    n20 = n16;
                }
                else {
                    n19 = n17;
                    n20 = n16;
                    if (n == 32) {
                        binaryOutputStream.write(0xFF & rgb);
                        binaryOutputStream.write(rgb >> 8 & 0xFF);
                        binaryOutputStream.write(rgb >> 16 & 0xFF);
                        binaryOutputStream.write(rgb >> 24 & 0xFF);
                        n20 = n16;
                        n19 = n17;
                    }
                }
                ++k;
                n17 = n19;
                n16 = n20;
            }
            if (n17 > 0) {
                binaryOutputStream.write(n16 << 8 - n17 & 0xFF);
                n14 = 0;
                n15 = 0;
            }
            else {
                n15 = n17;
                n14 = n16;
            }
            for (int l = 0; l < n4 - n13; ++l) {
                binaryOutputStream.write(0);
            }
            --j;
        }
        final int n21 = (bufferedImage.getWidth() + 7) / 8;
        int y = bufferedImage.getHeight() - 1;
        int n22 = n15;
        while (y >= 0) {
            final int n23 = 0;
            int n24 = n14;
            for (int x = n23; x < bufferedImage.getWidth(); ++x) {
                final int rgb2 = bufferedImage.getRGB(x, y);
                int n25 = n24 << 1;
                if ((rgb2 >> 24 & 0xFF) == 0x0) {
                    n25 |= 0x1;
                }
                if (++n22 >= 8) {
                    binaryOutputStream.write(n25 & 0xFF);
                    n24 = 0;
                    n22 = 0;
                }
                else {
                    n24 = n25;
                }
            }
            n14 = n24;
            int n26;
            if ((n26 = n22) > 0) {
                binaryOutputStream.write(n24 << 8 - n22 & 0xFF);
                n14 = 0;
                n26 = 0;
            }
            for (int n27 = 0; n27 < n7 - n21; ++n27) {
                binaryOutputStream.write(0);
            }
            --y;
            n22 = n26;
        }
        binaryOutputStream.close();
    }
    
    private static class BitmapHeader
    {
        public final int bitCount;
        public final int colorsImportant;
        public final int colorsUsed;
        public final int compression;
        public final int height;
        public final int planes;
        public final int size;
        public final int sizeImage;
        public final int width;
        public final int xPelsPerMeter;
        public final int yPelsPerMeter;
        
        public BitmapHeader(final int size, final int width, final int height, final int planes, final int bitCount, final int compression, final int sizeImage, final int xPelsPerMeter, final int yPelsPerMeter, final int colorsUsed, final int colorsImportant) {
            this.size = size;
            this.width = width;
            this.height = height;
            this.planes = planes;
            this.bitCount = bitCount;
            this.compression = compression;
            this.sizeImage = sizeImage;
            this.xPelsPerMeter = xPelsPerMeter;
            this.yPelsPerMeter = yPelsPerMeter;
            this.colorsUsed = colorsUsed;
            this.colorsImportant = colorsImportant;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("BitmapHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("Size: ");
            sb.append(this.size);
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Width: ");
            sb2.append(this.width);
            printWriter.println(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Height: ");
            sb3.append(this.height);
            printWriter.println(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Planes: ");
            sb4.append(this.planes);
            printWriter.println(sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("BitCount: ");
            sb5.append(this.bitCount);
            printWriter.println(sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Compression: ");
            sb6.append(this.compression);
            printWriter.println(sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("SizeImage: ");
            sb7.append(this.sizeImage);
            printWriter.println(sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("XPelsPerMeter: ");
            sb8.append(this.xPelsPerMeter);
            printWriter.println(sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("YPelsPerMeter: ");
            sb9.append(this.yPelsPerMeter);
            printWriter.println(sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("ColorsUsed: ");
            sb10.append(this.colorsUsed);
            printWriter.println(sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("ColorsImportant: ");
            sb11.append(this.colorsImportant);
            printWriter.println(sb11.toString());
        }
    }
    
    private static class BitmapIconData extends IconData
    {
        public final BufferedImage bufferedImage;
        public final BitmapHeader header;
        
        public BitmapIconData(final IconInfo iconInfo, final BitmapHeader header, final BufferedImage bufferedImage) {
            super(iconInfo);
            this.header = header;
            this.bufferedImage = bufferedImage;
        }
        
        @Override
        protected void dumpSubclass(final PrintWriter printWriter) {
            printWriter.println("BitmapIconData");
            this.header.dump(printWriter);
            printWriter.println();
        }
        
        @Override
        public BufferedImage readBufferedImage() throws ImageReadException {
            return this.bufferedImage;
        }
    }
    
    private abstract static class IconData
    {
        public final IconInfo iconInfo;
        
        public IconData(final IconInfo iconInfo) {
            this.iconInfo = iconInfo;
        }
        
        public void dump(final PrintWriter printWriter) {
            this.iconInfo.dump(printWriter);
            printWriter.println();
            this.dumpSubclass(printWriter);
        }
        
        protected abstract void dumpSubclass(final PrintWriter p0);
        
        public abstract BufferedImage readBufferedImage() throws ImageReadException;
    }
    
    private static class FileHeader
    {
        public final int iconCount;
        public final int iconType;
        public final int reserved;
        
        public FileHeader(final int reserved, final int iconType, final int iconCount) {
            this.reserved = reserved;
            this.iconType = iconType;
            this.iconCount = iconCount;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("FileHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("Reserved: ");
            sb.append(this.reserved);
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("IconType: ");
            sb2.append(this.iconType);
            printWriter.println(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("IconCount: ");
            sb3.append(this.iconCount);
            printWriter.println(sb3.toString());
            printWriter.println();
        }
    }
    
    private static class IconInfo
    {
        public final int bitCount;
        public final byte colorCount;
        public final byte height;
        public final int imageOffset;
        public final int imageSize;
        public final int planes;
        public final byte reserved;
        public final byte width;
        
        public IconInfo(final byte width, final byte height, final byte colorCount, final byte reserved, final int planes, final int bitCount, final int imageSize, final int imageOffset) {
            this.width = width;
            this.height = height;
            this.colorCount = colorCount;
            this.reserved = reserved;
            this.planes = planes;
            this.bitCount = bitCount;
            this.imageSize = imageSize;
            this.imageOffset = imageOffset;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("IconInfo");
            final StringBuilder sb = new StringBuilder();
            sb.append("Width: ");
            sb.append(this.width);
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Height: ");
            sb2.append(this.height);
            printWriter.println(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("ColorCount: ");
            sb3.append(this.colorCount);
            printWriter.println(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Reserved: ");
            sb4.append(this.reserved);
            printWriter.println(sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Planes: ");
            sb5.append(this.planes);
            printWriter.println(sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("BitCount: ");
            sb6.append(this.bitCount);
            printWriter.println(sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("ImageSize: ");
            sb7.append(this.imageSize);
            printWriter.println(sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("ImageOffset: ");
            sb8.append(this.imageOffset);
            printWriter.println(sb8.toString());
        }
    }
    
    private static class ImageContents
    {
        public final FileHeader fileHeader;
        public final IconData[] iconDatas;
        
        public ImageContents(final FileHeader fileHeader, final IconData[] iconDatas) {
            this.fileHeader = fileHeader;
            this.iconDatas = iconDatas;
        }
    }
    
    private static class PNGIconData extends IconData
    {
        public final BufferedImage bufferedImage;
        
        public PNGIconData(final IconInfo iconInfo, final BufferedImage bufferedImage) {
            super(iconInfo);
            this.bufferedImage = bufferedImage;
        }
        
        @Override
        protected void dumpSubclass(final PrintWriter printWriter) {
            printWriter.println("PNGIconData");
            printWriter.println();
        }
        
        @Override
        public BufferedImage readBufferedImage() {
            return this.bufferedImage;
        }
    }
}
