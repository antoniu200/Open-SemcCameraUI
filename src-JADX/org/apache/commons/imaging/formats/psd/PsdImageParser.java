package org.apache.commons.imaging.formats.psd;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.pnm.PnmConstants;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParser;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserBitmap;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserCmyk;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserGrayscale;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserIndexed;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserLab;
import org.apache.commons.imaging.formats.psd.dataparsers.DataParserRgb;
import org.apache.commons.imaging.formats.psd.datareaders.CompressedDataReader;
import org.apache.commons.imaging.formats.psd.datareaders.DataReader;
import org.apache.commons.imaging.formats.psd.datareaders.UncompressedDataReader;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PsdImageParser extends ImageParser {
    public static final String BLOCK_NAME_XMP = "XMP";
    private static final int COLOR_MODE_INDEXED = 2;
    public static final int IMAGE_RESOURCE_ID_ICC_PROFILE = 1039;
    public static final int IMAGE_RESOURCE_ID_XMP = 1060;
    private static final int PSD_HEADER_LENGTH = 26;
    private static final int PSD_SECTION_COLOR_MODE = 1;
    private static final int PSD_SECTION_HEADER = 0;
    private static final int PSD_SECTION_IMAGE_DATA = 4;
    private static final int PSD_SECTION_IMAGE_RESOURCES = 2;
    private static final int PSD_SECTION_LAYER_AND_MASK_DATA = 3;
    private static final String DEFAULT_EXTENSION = ".psd";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};

    private int getChannelsPerMode(int i) {
        switch (i) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return -1;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
            case 6:
            default:
                return -1;
            case 7:
                return -1;
            case 8:
                return -1;
            case 9:
                return 4;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "PSD-Custom";
    }

    public PsdImageParser() {
        super.setByteOrder(ByteOrder.BIG_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.PSD};
    }

    private PsdHeaderInfo readHeader(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            PsdHeaderInfo header = readHeader(inputStream);
            IoUtils.closeQuietly(true, inputStream);
            return header;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    private PsdHeaderInfo readHeader(InputStream inputStream) throws IOException, ImageReadException {
        BinaryFunctions.readAndVerifyBytes(inputStream, new byte[]{56, 66, PnmConstants.PNM_PREFIX_BYTE, 83}, "Not a Valid PSD File");
        return new PsdHeaderInfo(BinaryFunctions.read2Bytes("Version", inputStream, "Not a Valid PSD File", getByteOrder()), BinaryFunctions.readBytes("Reserved", inputStream, 6, "Not a Valid PSD File"), BinaryFunctions.read2Bytes("Channels", inputStream, "Not a Valid PSD File", getByteOrder()), BinaryFunctions.read4Bytes("Rows", inputStream, "Not a Valid PSD File", getByteOrder()), BinaryFunctions.read4Bytes("Columns", inputStream, "Not a Valid PSD File", getByteOrder()), BinaryFunctions.read2Bytes("Depth", inputStream, "Not a Valid PSD File", getByteOrder()), BinaryFunctions.read2Bytes("Mode", inputStream, "Not a Valid PSD File", getByteOrder()));
    }

    private ImageContents readImageContents(InputStream inputStream) throws IOException, ImageReadException {
        PsdHeaderInfo header = readHeader(inputStream);
        int i = BinaryFunctions.read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File", getByteOrder());
        BinaryFunctions.skipBytes(inputStream, i);
        int i2 = BinaryFunctions.read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File", getByteOrder());
        BinaryFunctions.skipBytes(inputStream, i2);
        int i3 = BinaryFunctions.read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File", getByteOrder());
        BinaryFunctions.skipBytes(inputStream, i3);
        return new ImageContents(header, i, i2, i3, BinaryFunctions.read2Bytes("Compression", inputStream, "Not a Valid PSD File", getByteOrder()));
    }

    private List<ImageResourceBlock> readImageResourceBlocks(byte[] bArr, int[] iArr, int i) throws IOException, ImageReadException {
        return readImageResourceBlocks(new ByteArrayInputStream(bArr), iArr, i, bArr.length);
    }

    private boolean keepImageResourceBlock(int i, int[] iArr) {
        if (iArr == null) {
            return true;
        }
        for (int i2 : iArr) {
            if (i == i2) {
                return true;
            }
        }
        return false;
    }

    private List<ImageResourceBlock> readImageResourceBlocks(InputStream inputStream, int[] iArr, int i, int i2) throws IOException, ImageReadException {
        ArrayList arrayList = new ArrayList();
        while (i2 > 0) {
            BinaryFunctions.readAndVerifyBytes(inputStream, new byte[]{56, 66, 73, 77}, "Not a Valid PSD File");
            int i3 = BinaryFunctions.read2Bytes("ID", inputStream, "Not a Valid PSD File", getByteOrder());
            byte b = BinaryFunctions.readByte("NameLength", inputStream, "Not a Valid PSD File");
            byte[] bytes = BinaryFunctions.readBytes("NameData", inputStream, b, "Not a Valid PSD File");
            int i4 = (((i2 - 4) - 2) - 1) - b;
            if ((b + 1) % 2 != 0) {
                BinaryFunctions.readByte("NameDiscard", inputStream, "Not a Valid PSD File");
                i4--;
            }
            int i5 = BinaryFunctions.read4Bytes("Size", inputStream, "Not a Valid PSD File", getByteOrder());
            byte[] bytes2 = BinaryFunctions.readBytes("Data", inputStream, i5, "Not a Valid PSD File");
            i2 = (i4 - 4) - i5;
            if (i5 % 2 != 0) {
                BinaryFunctions.readByte("DataDiscard", inputStream, "Not a Valid PSD File");
                i2--;
            }
            if (keepImageResourceBlock(i3, iArr)) {
                arrayList.add(new ImageResourceBlock(i3, bytes, bytes2));
                if (i >= 0 && arrayList.size() >= i) {
                    return arrayList;
                }
            }
        }
        return arrayList;
    }

    private List<ImageResourceBlock> readImageResourceBlocks(ByteSource byteSource, int[] iArr, int i) throws Throwable {
        InputStream inputStream;
        InputStream inputStream2;
        ImageContents imageContents;
        try {
            inputStream2 = byteSource.getInputStream();
            try {
                imageContents = readImageContents(inputStream2);
                inputStream = getInputStream(byteSource, 2);
            } catch (Throwable th) {
                th = th;
                inputStream = null;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream2 = null;
        }
        try {
            List<ImageResourceBlock> imageResourceBlocks = readImageResourceBlocks(BinaryFunctions.readBytes("ImageResources", inputStream, imageContents.ImageResourcesLength, "Not a Valid PSD File"), iArr, i);
            IoUtils.closeQuietly(true, inputStream2, inputStream);
            return imageResourceBlocks;
        } catch (Throwable th3) {
            th = th3;
            IoUtils.closeQuietly(false, inputStream2, inputStream);
            throw th;
        }
    }

    private InputStream getInputStream(ByteSource byteSource, int i) throws IOException, ImageReadException {
        InputStream inputStream = byteSource.getInputStream();
        if (i == 0) {
            return inputStream;
        }
        BinaryFunctions.skipBytes(inputStream, 26L);
        int i2 = BinaryFunctions.read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File", getByteOrder());
        if (i == 1) {
            return inputStream;
        }
        BinaryFunctions.skipBytes(inputStream, i2);
        int i3 = BinaryFunctions.read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File", getByteOrder());
        if (i == 2) {
            return inputStream;
        }
        BinaryFunctions.skipBytes(inputStream, i3);
        int i4 = BinaryFunctions.read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File", getByteOrder());
        if (i == 3) {
            return inputStream;
        }
        BinaryFunctions.skipBytes(inputStream, i4);
        BinaryFunctions.read2Bytes("Compression", inputStream, "Not a Valid PSD File", getByteOrder());
        if (i == 4) {
            return inputStream;
        }
        if (inputStream != null) {
            inputStream.close();
        }
        throw new ImageReadException("getInputStream: Unknown Section: " + i);
    }

    private byte[] getData(ByteSource byteSource, int i) throws Throwable {
        InputStream inputStream;
        InputStream inputStream2;
        boolean z;
        byte[] bytes;
        Closeable[] closeableArr;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            if (i == 0) {
                bytes = BinaryFunctions.readBytes("Header", inputStream, 26, "Not a Valid PSD File");
                closeableArr = new Closeable[]{inputStream};
            } else {
                try {
                    BinaryFunctions.skipBytes(inputStream, 26L);
                    int i2 = BinaryFunctions.read4Bytes("ColorModeDataLength", inputStream, "Not a Valid PSD File", getByteOrder());
                    if (i == 1) {
                        bytes = BinaryFunctions.readBytes("ColorModeData", inputStream, i2, "Not a Valid PSD File");
                        closeableArr = new Closeable[]{inputStream};
                    } else {
                        BinaryFunctions.skipBytes(inputStream, i2);
                        int i3 = BinaryFunctions.read4Bytes("ImageResourcesLength", inputStream, "Not a Valid PSD File", getByteOrder());
                        if (i == 2) {
                            bytes = BinaryFunctions.readBytes("ImageResources", inputStream, i3, "Not a Valid PSD File");
                            closeableArr = new Closeable[]{inputStream};
                        } else {
                            BinaryFunctions.skipBytes(inputStream, i3);
                            int i4 = BinaryFunctions.read4Bytes("LayerAndMaskDataLength", inputStream, "Not a Valid PSD File", getByteOrder());
                            if (i != 3) {
                                BinaryFunctions.skipBytes(inputStream, i4);
                                BinaryFunctions.read2Bytes("Compression", inputStream, "Not a Valid PSD File", getByteOrder());
                                IoUtils.closeQuietly(true, inputStream);
                                throw new ImageReadException("getInputStream: Unknown Section: " + i);
                            }
                            bytes = BinaryFunctions.readBytes("LayerAndMaskData", inputStream, i4, "Not a Valid PSD File");
                            closeableArr = new Closeable[]{inputStream};
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inputStream2 = inputStream;
                    z = false;
                    IoUtils.closeQuietly(z, inputStream2);
                    throw th;
                }
            }
            IoUtils.closeQuietly(true, closeableArr);
            return bytes;
        } catch (Throwable th3) {
            th = th3;
            inputStream2 = inputStream;
            z = true;
            IoUtils.closeQuietly(z, inputStream2);
            throw th;
        }
    }

    private ImageContents readImageContents(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            ImageContents imageContents = readImageContents(inputStream);
            IoUtils.closeQuietly(true, inputStream);
            return imageContents;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        byte[] bArr;
        List<ImageResourceBlock> imageResourceBlocks = readImageResourceBlocks(byteSource, new int[]{1039}, 1);
        if (imageResourceBlocks == null || imageResourceBlocks.size() < 1 || (bArr = imageResourceBlocks.get(0).data) == null || bArr.length < 1) {
            return null;
        }
        return bArr;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        PsdHeaderInfo header = readHeader(byteSource);
        if (header == null) {
            throw new ImageReadException("PSD: couldn't read header");
        }
        return new Dimension(header.columns, header.rows);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        ImageInfo.CompressionAlgorithm compressionAlgorithm;
        ImageContents imageContents = readImageContents(byteSource);
        if (imageContents == null) {
            throw new ImageReadException("PSD: Couldn't read blocks");
        }
        PsdHeaderInfo psdHeaderInfo = imageContents.header;
        if (psdHeaderInfo == null) {
            throw new ImageReadException("PSD: Couldn't read Header");
        }
        int i = psdHeaderInfo.columns;
        int i2 = psdHeaderInfo.rows;
        ArrayList arrayList = new ArrayList();
        int channelsPerMode = psdHeaderInfo.depth * getChannelsPerMode(psdHeaderInfo.mode);
        int i3 = channelsPerMode < 0 ? 0 : channelsPerMode;
        ImageFormats imageFormats = ImageFormats.PSD;
        float f = (float) (i / 72.0d);
        float f2 = (float) (i2 / 72.0d);
        boolean z = psdHeaderInfo.mode == 2;
        ImageInfo.ColorType colorType = ImageInfo.ColorType.UNKNOWN;
        switch (imageContents.Compression) {
            case 0:
                compressionAlgorithm = ImageInfo.CompressionAlgorithm.NONE;
                break;
            case 1:
                compressionAlgorithm = ImageInfo.CompressionAlgorithm.PSD;
                break;
            default:
                compressionAlgorithm = ImageInfo.CompressionAlgorithm.UNKNOWN;
                break;
        }
        return new ImageInfo("Psd", i3, arrayList, imageFormats, "Photoshop", i2, "image/x-photoshop", -1, 72, f2, 72, f, i, false, false, z, colorType, compressionAlgorithm);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws Throwable {
        printWriter.println("gif.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        ImageContents imageContents = readImageContents(byteSource);
        imageContents.dump(printWriter);
        imageContents.header.dump(printWriter);
        List<ImageResourceBlock> imageResourceBlocks = readImageResourceBlocks(byteSource, (int[]) null, -1);
        printWriter.println("blocks.size(): " + imageResourceBlocks.size());
        for (int i = 0; i < imageResourceBlocks.size(); i++) {
            ImageResourceBlock imageResourceBlock = imageResourceBlocks.get(i);
            printWriter.println("\t" + i + " (" + Integer.toHexString(imageResourceBlock.id) + ", '" + new String(imageResourceBlock.nameData, "ISO-8859-1") + "' (" + imageResourceBlock.nameData.length + "),  data: " + imageResourceBlock.data.length + " type: '" + ImageResourceType.getDescription(imageResourceBlock.id) + "' )");
        }
        printWriter.println("");
        return true;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        DataParser dataParserBitmap;
        DataReader uncompressedDataReader;
        InputStream inputStream;
        ImageContents imageContents = readImageContents(byteSource);
        if (imageContents == null) {
            throw new ImageReadException("PSD: Couldn't read blocks");
        }
        PsdHeaderInfo psdHeaderInfo = imageContents.header;
        if (psdHeaderInfo == null) {
            throw new ImageReadException("PSD: Couldn't read Header");
        }
        readImageResourceBlocks(byteSource, (int[]) null, -1);
        BufferedImage colorBufferedImage = getBufferedImageFactory(map).getColorBufferedImage(psdHeaderInfo.columns, psdHeaderInfo.rows, false);
        switch (imageContents.header.mode) {
            case 0:
                dataParserBitmap = new DataParserBitmap();
                break;
            case 1:
            case 8:
                dataParserBitmap = new DataParserGrayscale();
                break;
            case 2:
                dataParserBitmap = new DataParserIndexed(getData(byteSource, 1));
                break;
            case 3:
                dataParserBitmap = new DataParserRgb();
                break;
            case 4:
                dataParserBitmap = new DataParserCmyk();
                break;
            case 5:
            case 6:
            case 7:
            default:
                throw new ImageReadException("Unknown Mode: " + imageContents.header.mode);
            case 9:
                dataParserBitmap = new DataParserLab();
                break;
        }
        switch (imageContents.Compression) {
            case 0:
                uncompressedDataReader = new UncompressedDataReader(dataParserBitmap);
                break;
            case 1:
                uncompressedDataReader = new CompressedDataReader(dataParserBitmap);
                break;
            default:
                throw new ImageReadException("Unknown Compression: " + imageContents.Compression);
        }
        try {
            inputStream = getInputStream(byteSource, 4);
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            uncompressedDataReader.readData(inputStream, colorBufferedImage, imageContents, this);
            IoUtils.closeQuietly(true, inputStream);
            return colorBufferedImage;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        ImageContents imageContents = readImageContents(byteSource);
        if (imageContents == null) {
            throw new ImageReadException("PSD: Couldn't read blocks");
        }
        if (imageContents.header == null) {
            throw new ImageReadException("PSD: Couldn't read Header");
        }
        List<ImageResourceBlock> imageResourceBlocks = readImageResourceBlocks(byteSource, new int[]{1060}, -1);
        if (imageResourceBlocks == null || imageResourceBlocks.size() < 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(imageResourceBlocks);
        if (arrayList.size() < 1) {
            return null;
        }
        if (arrayList.size() > 1) {
            throw new ImageReadException("PSD contains more than one XMP block.");
        }
        ImageResourceBlock imageResourceBlock = (ImageResourceBlock) arrayList.get(0);
        try {
            return new String(imageResourceBlock.data, 0, imageResourceBlock.data.length, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ImageReadException("Invalid JPEG XMP Segment.", e);
        }
    }
}
