// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.formats.psd.datareaders.DataReader;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParser;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.formats.psd.datareaders.UncompressedDataReader;
import org.apache.commons.imaging.formats.psd.datareaders.CompressedDataReader;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserBitmap;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserGrayscale;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserIndexed;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserRgb;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserCmyk;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserLab;
import java.awt.image.BufferedImage;
import java.util.Map;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import java.io.PrintWriter;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class PsdImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    public static final String BLOCK_NAME_XMP = "XMP";
    private static final int COLOR_MODE_INDEXED = 2;
    private static final String DEFAULT_EXTENSION = ".psd";
    public static final int IMAGE_RESOURCE_ID_ICC_PROFILE = 1039;
    public static final int IMAGE_RESOURCE_ID_XMP = 1060;
    private static final int PSD_HEADER_LENGTH = 26;
    private static final int PSD_SECTION_COLOR_MODE = 1;
    private static final int PSD_SECTION_HEADER = 0;
    private static final int PSD_SECTION_IMAGE_DATA = 4;
    private static final int PSD_SECTION_IMAGE_RESOURCES = 2;
    private static final int PSD_SECTION_LAYER_AND_MASK_DATA = 3;
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".psd" };
    }
    
    public PsdImageParser() {
        super.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    private int getChannelsPerMode(final int n) {
        switch (n) {
            default: {
                return -1;
            }
            case 9: {
                return 4;
            }
            case 8: {
                return -1;
            }
            case 7: {
                return -1;
            }
            case 4: {
                return 4;
            }
            case 3: {
                return 3;
            }
            case 2: {
                return -1;
            }
            case 1: {
                return 1;
            }
            case 0: {
                return 1;
            }
        }
    }
    
    private byte[] getData(final ByteSource byteSource, final int i) throws ImageReadException, IOException {
        boolean b = false;
        Closeable closeable = null;
        Label_0312: {
            try {
                Object inputStream = byteSource.getInputStream();
                Label_0053: {
                    if (i != 0) {
                        break Label_0053;
                    }
                    while (true) {
                        try {
                            final byte[] bytes = BinaryFunctions.readBytes("Header", (InputStream)inputStream, 26, "Not a Valid PSD File");
                            inputStream = new Closeable[] { (Closeable)inputStream };
                            IoUtils.closeQuietly(true, (Closeable[])inputStream);
                            return bytes;
                        }
                        finally {
                            b = true;
                            break Label_0312;
                        }
                        try {
                            BinaryFunctions.skipBytes((InputStream)inputStream, 26L);
                            final int read4Bytes = BinaryFunctions.read4Bytes("ColorModeDataLength", (InputStream)inputStream, "Not a Valid PSD File", this.getByteOrder());
                            if (i == 1) {
                                BinaryFunctions.readBytes("ColorModeData", (InputStream)inputStream, read4Bytes, "Not a Valid PSD File");
                                inputStream = new Closeable[] { (Closeable)inputStream };
                                continue;
                            }
                            BinaryFunctions.skipBytes((InputStream)inputStream, read4Bytes);
                            final int read4Bytes2 = BinaryFunctions.read4Bytes("ImageResourcesLength", (InputStream)inputStream, "Not a Valid PSD File", this.getByteOrder());
                            if (i == 2) {
                                BinaryFunctions.readBytes("ImageResources", (InputStream)inputStream, read4Bytes2, "Not a Valid PSD File");
                                inputStream = new Closeable[] { (Closeable)inputStream };
                                continue;
                            }
                            BinaryFunctions.skipBytes((InputStream)inputStream, read4Bytes2);
                            final int read4Bytes3 = BinaryFunctions.read4Bytes("LayerAndMaskDataLength", (InputStream)inputStream, "Not a Valid PSD File", this.getByteOrder());
                            if (i == 3) {
                                BinaryFunctions.readBytes("LayerAndMaskData", (InputStream)inputStream, read4Bytes3, "Not a Valid PSD File");
                                inputStream = new Closeable[] { (Closeable)inputStream };
                                continue;
                            }
                            BinaryFunctions.skipBytes((InputStream)inputStream, read4Bytes3);
                            BinaryFunctions.read2Bytes("Compression", (InputStream)inputStream, "Not a Valid PSD File", this.getByteOrder());
                            IoUtils.closeQuietly(true, (Closeable)inputStream);
                            final StringBuilder sb = new StringBuilder();
                            sb.append("getInputStream: Unknown Section: ");
                            sb.append(i);
                            throw new ImageReadException(sb.toString());
                        }
                        finally {}
                        break;
                    }
                }
            }
            finally {
                closeable = null;
            }
            b = false;
        }
        IoUtils.closeQuietly(b, closeable);
    }
    
    private InputStream getInputStream(final ByteSource byteSource, final int i) throws ImageReadException, IOException {
        try {
            final InputStream inputStream = byteSource.getInputStream();
            if (i == 0) {
                return inputStream;
            }
            BinaryFunctions.skipBytes(inputStream, 26L);
            final int read4Bytes = BinaryFunctions.read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File", this.getByteOrder());
            if (i == 1) {
                return inputStream;
            }
            BinaryFunctions.skipBytes(inputStream, read4Bytes);
            final int read4Bytes2 = BinaryFunctions.read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File", this.getByteOrder());
            if (i == 2) {
                return inputStream;
            }
            BinaryFunctions.skipBytes(inputStream, read4Bytes2);
            final int read4Bytes3 = BinaryFunctions.read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File", this.getByteOrder());
            if (i == 3) {
                return inputStream;
            }
            BinaryFunctions.skipBytes(inputStream, read4Bytes3);
            BinaryFunctions.read2Bytes("Compression", inputStream, "Not a Valid PSD File", this.getByteOrder());
            if (i == 4) {
                return inputStream;
            }
            if (inputStream != null) {
                inputStream.close();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("getInputStream: Unknown Section: ");
            sb.append(i);
            throw new ImageReadException(sb.toString());
        }
        finally {}
    }
    
    private boolean keepImageResourceBlock(final int n, final int[] array) {
        if (array == null) {
            return true;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            if (n == array[i]) {
                return true;
            }
        }
        return false;
    }
    
    private PsdHeaderInfo readHeader(final InputStream inputStream) throws ImageReadException, IOException {
        BinaryFunctions.readAndVerifyBytes(inputStream, new byte[] { 56, 66, 80, 83 }, "Not a Valid PSD File");
        return new PsdHeaderInfo(BinaryFunctions.read2Bytes("Version", inputStream, "Not a Valid PSD File", this.getByteOrder()), BinaryFunctions.readBytes("Reserved", inputStream, 6, "Not a Valid PSD File"), BinaryFunctions.read2Bytes("Channels", inputStream, "Not a Valid PSD File", this.getByteOrder()), BinaryFunctions.read4Bytes("Rows", inputStream, "Not a Valid PSD File", this.getByteOrder()), BinaryFunctions.read4Bytes("Columns", inputStream, "Not a Valid PSD File", this.getByteOrder()), BinaryFunctions.read2Bytes("Depth", inputStream, "Not a Valid PSD File", this.getByteOrder()), BinaryFunctions.read2Bytes("Mode", inputStream, "Not a Valid PSD File", this.getByteOrder()));
    }
    
    private PsdHeaderInfo readHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final PsdHeaderInfo header = this.readHeader(inputStream);
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
    
    private ImageContents readImageContents(final InputStream inputStream) throws ImageReadException, IOException {
        final PsdHeaderInfo header = this.readHeader(inputStream);
        final int read4Bytes = BinaryFunctions.read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File", this.getByteOrder());
        BinaryFunctions.skipBytes(inputStream, read4Bytes);
        final int read4Bytes2 = BinaryFunctions.read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File", this.getByteOrder());
        BinaryFunctions.skipBytes(inputStream, read4Bytes2);
        final int read4Bytes3 = BinaryFunctions.read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File", this.getByteOrder());
        BinaryFunctions.skipBytes(inputStream, read4Bytes3);
        return new ImageContents(header, read4Bytes, read4Bytes2, read4Bytes3, BinaryFunctions.read2Bytes("Compression", inputStream, "Not a Valid PSD File", this.getByteOrder()));
    }
    
    private ImageContents readImageContents(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final ImageContents imageContents = this.readImageContents(inputStream);
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
    
    private List<ImageResourceBlock> readImageResourceBlocks(final InputStream inputStream, final int[] array, final int n, int n2) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        int i = n2;
        while (i > 0) {
            BinaryFunctions.readAndVerifyBytes(inputStream, new byte[] { 56, 66, 73, 77 }, "Not a Valid PSD File");
            final int read2Bytes = BinaryFunctions.read2Bytes("ID", inputStream, "Not a Valid PSD File", this.getByteOrder());
            final byte byte1 = BinaryFunctions.readByte("NameLength", inputStream, "Not a Valid PSD File");
            final byte[] bytes = BinaryFunctions.readBytes("NameData", inputStream, byte1, "Not a Valid PSD File");
            final int n3 = n2 = i - 4 - 2 - 1 - byte1;
            if ((byte1 + 1) % 2 != 0) {
                BinaryFunctions.readByte("NameDiscard", inputStream, "Not a Valid PSD File");
                n2 = n3 - 1;
            }
            final int read4Bytes = BinaryFunctions.read4Bytes("Size", inputStream, "Not a Valid PSD File", this.getByteOrder());
            final byte[] bytes2 = BinaryFunctions.readBytes("Data", inputStream, read4Bytes, "Not a Valid PSD File");
            final int n4 = n2 = n2 - 4 - read4Bytes;
            if (read4Bytes % 2 != 0) {
                BinaryFunctions.readByte("DataDiscard", inputStream, "Not a Valid PSD File");
                n2 = n4 - 1;
            }
            i = n2;
            if (this.keepImageResourceBlock(read2Bytes, array)) {
                list.add(new ImageResourceBlock(read2Bytes, bytes, bytes2));
                i = n2;
                if (n < 0) {
                    continue;
                }
                i = n2;
                if (list.size() >= n) {
                    return list;
                }
                continue;
            }
        }
        return list;
    }
    
    private List<ImageResourceBlock> readImageResourceBlocks(final ByteSource byteSource, final int[] array, final int n) throws ImageReadException, IOException {
        Closeable closeable;
        Closeable closeable2;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final ImageContents imageContents = this.readImageContents(inputStream);
                final InputStream inputStream2 = this.getInputStream(byteSource, 2);
                try {
                    final List<ImageResourceBlock> imageResourceBlocks = this.readImageResourceBlocks(BinaryFunctions.readBytes("ImageResources", inputStream2, imageContents.ImageResourcesLength, "Not a Valid PSD File"), array, n);
                    IoUtils.closeQuietly(true, inputStream, inputStream2);
                    return imageResourceBlocks;
                }
                finally {}
            }
            finally {}
        }
        finally {
            closeable = null;
            closeable2 = null;
        }
        IoUtils.closeQuietly(false, closeable2, closeable);
    }
    
    private List<ImageResourceBlock> readImageResourceBlocks(final byte[] buf, final int[] array, final int n) throws ImageReadException, IOException {
        return this.readImageResourceBlocks(new ByteArrayInputStream(buf), array, n, buf.length);
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("gif.dumpImageFile");
        final ImageInfo imageInfo = this.getImageInfo(byteSource);
        int i = 0;
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        final ImageContents imageContents = this.readImageContents(byteSource);
        imageContents.dump(printWriter);
        imageContents.header.dump(printWriter);
        final List<ImageResourceBlock> imageResourceBlocks = this.readImageResourceBlocks(byteSource, null, -1);
        final StringBuilder sb = new StringBuilder();
        sb.append("blocks.size(): ");
        sb.append(imageResourceBlocks.size());
        printWriter.println(sb.toString());
        while (i < imageResourceBlocks.size()) {
            final ImageResourceBlock imageResourceBlock = imageResourceBlocks.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("\t");
            sb2.append(i);
            sb2.append(" (");
            sb2.append(Integer.toHexString(imageResourceBlock.id));
            sb2.append(", ");
            sb2.append("'");
            sb2.append(new String(imageResourceBlock.nameData, "ISO-8859-1"));
            sb2.append("' (");
            sb2.append(imageResourceBlock.nameData.length);
            sb2.append("), ");
            sb2.append(" data: ");
            sb2.append(imageResourceBlock.data.length);
            sb2.append(" type: '");
            sb2.append(ImageResourceType.getDescription(imageResourceBlock.id));
            sb2.append("' ");
            sb2.append(")");
            printWriter.println(sb2.toString());
            ++i;
        }
        printWriter.println("");
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return PsdImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.PSD };
    }
    
    @Override
    public BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageContents imageContents = this.readImageContents(byteSource);
        if (imageContents == null) {
            throw new ImageReadException("PSD: Couldn't read blocks");
        }
        final PsdHeaderInfo header = imageContents.header;
        if (header == null) {
            throw new ImageReadException("PSD: Couldn't read Header");
        }
        this.readImageResourceBlocks(byteSource, null, -1);
        final BufferedImage colorBufferedImage = this.getBufferedImageFactory(map).getColorBufferedImage(header.columns, header.rows, false);
        DataParser dataParser = null;
        switch (imageContents.header.mode) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown Mode: ");
                sb.append(imageContents.header.mode);
                throw new ImageReadException(sb.toString());
            }
            case 9: {
                dataParser = new DataParserLab();
                break;
            }
            case 4: {
                dataParser = new DataParserCmyk();
                break;
            }
            case 3: {
                dataParser = new DataParserRgb();
                break;
            }
            case 2: {
                dataParser = new DataParserIndexed(this.getData(byteSource, 1));
                break;
            }
            case 1:
            case 8: {
                dataParser = new DataParserGrayscale();
                break;
            }
            case 0: {
                dataParser = new DataParserBitmap();
                break;
            }
        }
        DataReader dataReader = null;
        switch (imageContents.Compression) {
            default: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown Compression: ");
                sb2.append(imageContents.Compression);
                throw new ImageReadException(sb2.toString());
            }
            case 1: {
                dataReader = new CompressedDataReader(dataParser);
                break;
            }
            case 0: {
                dataReader = new UncompressedDataReader(dataParser);
                break;
            }
        }
        Closeable closeable;
        try {
            final InputStream inputStream = this.getInputStream(byteSource, 4);
            try {
                dataReader.readData(inputStream, colorBufferedImage, imageContents, this);
                IoUtils.closeQuietly(true, inputStream);
                return colorBufferedImage;
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
        return ".psd";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<ImageResourceBlock> imageResourceBlocks = this.readImageResourceBlocks(byteSource, new int[] { 1039 }, 1);
        if (imageResourceBlocks == null || imageResourceBlocks.size() < 1) {
            return null;
        }
        final byte[] data = imageResourceBlocks.get(0).data;
        if (data != null && data.length >= 1) {
            return data;
        }
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageContents imageContents = this.readImageContents(byteSource);
        if (imageContents == null) {
            throw new ImageReadException("PSD: Couldn't read blocks");
        }
        final PsdHeaderInfo header = imageContents.header;
        if (header == null) {
            throw new ImageReadException("PSD: Couldn't read Header");
        }
        final int columns = header.columns;
        final int rows = header.rows;
        final ArrayList list = new ArrayList();
        int n = header.depth * this.getChannelsPerMode(header.mode);
        if (n < 0) {
            n = 0;
        }
        final ImageFormats psd = ImageFormats.PSD;
        final float n2 = (float)(columns / 72.0);
        final float n3 = (float)(rows / 72.0);
        final boolean b = header.mode == 2;
        final ImageInfo.ColorType unknown = ImageInfo.ColorType.UNKNOWN;
        ImageInfo.CompressionAlgorithm compressionAlgorithm = null;
        switch (imageContents.Compression) {
            default: {
                compressionAlgorithm = ImageInfo.CompressionAlgorithm.UNKNOWN;
                break;
            }
            case 1: {
                compressionAlgorithm = ImageInfo.CompressionAlgorithm.PSD;
                break;
            }
            case 0: {
                compressionAlgorithm = ImageInfo.CompressionAlgorithm.NONE;
                break;
            }
        }
        return new ImageInfo("Psd", n, list, psd, "Photoshop", rows, "image/x-photoshop", -1, 72, n3, 72, n2, columns, false, false, b, unknown, compressionAlgorithm);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final PsdHeaderInfo header = this.readHeader(byteSource);
        if (header == null) {
            throw new ImageReadException("PSD: couldn't read header");
        }
        return new Dimension(header.columns, header.rows);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "PSD-Custom";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageContents imageContents = this.readImageContents(byteSource);
        if (imageContents == null) {
            throw new ImageReadException("PSD: Couldn't read blocks");
        }
        if (imageContents.header == null) {
            throw new ImageReadException("PSD: Couldn't read Header");
        }
        final List<ImageResourceBlock> imageResourceBlocks = this.readImageResourceBlocks(byteSource, new int[] { 1060 }, -1);
        if (imageResourceBlocks != null) {
            if (imageResourceBlocks.size() >= 1) {
                final ArrayList list = new ArrayList();
                list.addAll(imageResourceBlocks);
                if (list.size() < 1) {
                    return null;
                }
                if (list.size() > 1) {
                    throw new ImageReadException("PSD contains more than one XMP block.");
                }
                final ImageResourceBlock imageResourceBlock = (ImageResourceBlock)list.get(0);
                try {
                    return new String(imageResourceBlock.data, 0, imageResourceBlock.data.length, "utf-8");
                }
                catch (final UnsupportedEncodingException ex) {
                    throw new ImageReadException("Invalid JPEG XMP Segment.", ex);
                }
            }
        }
        return null;
    }
}
