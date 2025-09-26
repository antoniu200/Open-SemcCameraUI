package org.apache.commons.imaging.formats.gif;

import android.support.v4.view.ViewCompat;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.ByteCompanionObject;
import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.mylzw.MyLzwCompressor;
import org.apache.commons.imaging.common.mylzw.MyLzwDecompressor;
import org.apache.commons.imaging.formats.pnm.PnmConstants;
import org.apache.commons.imaging.palette.Palette;
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GifImageParser extends ImageParser {
    private static final int APPLICATION_EXTENSION_LABEL = 255;
    private static final int COMMENT_EXTENSION = 254;
    private static final int EXTENSION_CODE = 33;
    private static final int GRAPHIC_CONTROL_EXTENSION = 8697;
    private static final int IMAGE_SEPARATOR = 44;
    private static final int INTERLACE_FLAG_MASK = 64;
    private static final int LOCAL_COLOR_TABLE_FLAG_MASK = 128;
    private static final int PLAIN_TEXT_EXTENSION = 1;
    private static final int SORT_FLAG_MASK = 32;
    private static final int TERMINATOR_BYTE = 59;
    private static final int XMP_COMPLETE_CODE = 8703;
    private static final int XMP_EXTENSION = 255;
    private static final String DEFAULT_EXTENSION = ".gif";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};
    private static final byte[] GIF_HEADER_SIGNATURE = {71, 73, 70};
    private static final byte[] XMP_APPLICATION_ID_AND_AUTH_CODE = {88, 77, PnmConstants.PNM_PREFIX_BYTE, PnmConstants.PNM_SEPARATOR, 68, 97, 116, 97, 88, 77, PnmConstants.PNM_PREFIX_BYTE};

    private int simplePow(int i, int i2) {
        int i3 = 1;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 *= i;
        }
        return i3;
    }

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
        return "Graphics Interchange Format";
    }

    public GifImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.GIF};
    }

    private GifHeaderInfo readHeader(InputStream inputStream, FormatCompliance formatCompliance) throws IOException, ImageReadException {
        byte b;
        byte b2;
        boolean z;
        byte b3 = BinaryFunctions.readByte("identifier1", inputStream, "Not a Valid GIF File");
        byte b4 = BinaryFunctions.readByte("identifier2", inputStream, "Not a Valid GIF File");
        byte b5 = BinaryFunctions.readByte("identifier3", inputStream, "Not a Valid GIF File");
        byte b6 = BinaryFunctions.readByte("version1", inputStream, "Not a Valid GIF File");
        byte b7 = BinaryFunctions.readByte("version2", inputStream, "Not a Valid GIF File");
        byte b8 = BinaryFunctions.readByte("version3", inputStream, "Not a Valid GIF File");
        if (formatCompliance != null) {
            formatCompliance.compareBytes("Signature", GIF_HEADER_SIGNATURE, new byte[]{b3, b4, b5});
            formatCompliance.compare("version", 56, b6);
            formatCompliance.compare("version", new int[]{55, 57}, b7);
            formatCompliance.compare("version", 97, b8);
        }
        if (getDebug()) {
            BinaryFunctions.printCharQuad("identifier: ", (b3 << 16) | (b4 << 8) | (b5 << 0));
            BinaryFunctions.printCharQuad("version: ", (b6 << 16) | (b7 << 8) | (b8 << 0));
        }
        int i = BinaryFunctions.read2Bytes("Logical Screen Width", inputStream, "Not a Valid GIF File", getByteOrder());
        int i2 = BinaryFunctions.read2Bytes("Logical Screen Height", inputStream, "Not a Valid GIF File", getByteOrder());
        if (formatCompliance != null) {
            formatCompliance.checkBounds("Width", 1, Integer.MAX_VALUE, i);
            formatCompliance.checkBounds("Height", 1, Integer.MAX_VALUE, i2);
        }
        byte b9 = BinaryFunctions.readByte("Packed Fields", inputStream, "Not a Valid GIF File");
        byte b10 = BinaryFunctions.readByte("Background Color Index", inputStream, "Not a Valid GIF File");
        byte b11 = BinaryFunctions.readByte("Pixel Aspect Ratio", inputStream, "Not a Valid GIF File");
        if (getDebug()) {
            BinaryFunctions.printByteBits("PackedFields bits", b9);
        }
        boolean z2 = (b9 & ByteCompanionObject.MIN_VALUE) > 0;
        if (getDebug()) {
            System.out.println("GlobalColorTableFlag: " + z2);
        }
        byte b12 = (byte) ((b9 >> 4) & 7);
        if (getDebug()) {
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            b = b11;
            sb.append("ColorResolution: ");
            sb.append((int) b12);
            printStream.println(sb.toString());
        } else {
            b = b11;
        }
        boolean z3 = (b9 & 8) > 0;
        if (getDebug()) {
            PrintStream printStream2 = System.out;
            StringBuilder sb2 = new StringBuilder();
            b2 = b12;
            sb2.append("SortFlag: ");
            sb2.append(z3);
            printStream2.println(sb2.toString());
        } else {
            b2 = b12;
        }
        byte b13 = (byte) (b9 & 7);
        if (getDebug()) {
            PrintStream printStream3 = System.out;
            StringBuilder sb3 = new StringBuilder();
            z = z3;
            sb3.append("SizeofGlobalColorTable: ");
            sb3.append((int) b13);
            printStream3.println(sb3.toString());
        } else {
            z = z3;
        }
        if (formatCompliance != null && z2 && b10 != -1) {
            formatCompliance.checkBounds("Background Color Index", 0, convertColorTableSize(b13), b10);
        }
        return new GifHeaderInfo(b3, b4, b5, b6, b7, b8, i, i2, b9, b10, b, z2, b2, z, b13);
    }

    private GraphicControlExtension readGraphicControlExtension(int i, InputStream inputStream) throws IOException {
        BinaryFunctions.readByte("block_size", inputStream, "GIF: corrupt GraphicControlExt");
        byte b = BinaryFunctions.readByte("packed fields", inputStream, "GIF: corrupt GraphicControlExt");
        int i2 = (b & 28) >> 2;
        boolean z = (b & 1) != 0;
        int i3 = BinaryFunctions.read2Bytes("delay in milliseconds", inputStream, "GIF: corrupt GraphicControlExt", getByteOrder());
        int i4 = 255 & BinaryFunctions.readByte("transparent color index", inputStream, "GIF: corrupt GraphicControlExt");
        BinaryFunctions.readByte("block terminator", inputStream, "GIF: corrupt GraphicControlExt");
        return new GraphicControlExtension(i, b, i2, z, i3, i4);
    }

    private byte[] readSubBlock(InputStream inputStream) throws IOException {
        return BinaryFunctions.readBytes("block", inputStream, BinaryFunctions.readByte("block_size", inputStream, "GIF: corrupt block") & 255, "GIF: corrupt block");
    }

    private GenericGifBlock readGenericGIFBlock(InputStream inputStream, int i) throws IOException {
        return readGenericGIFBlock(inputStream, i, null);
    }

    private GenericGifBlock readGenericGIFBlock(InputStream inputStream, int i, byte[] bArr) throws IOException {
        ArrayList arrayList = new ArrayList();
        if (bArr != null) {
            arrayList.add(bArr);
        }
        while (true) {
            byte[] subBlock = readSubBlock(inputStream);
            if (subBlock.length >= 1) {
                arrayList.add(subBlock);
            } else {
                return new GenericGifBlock(i, arrayList);
            }
        }
    }

    private List<GifBlock> readBlocks(GifHeaderInfo gifHeaderInfo, InputStream inputStream, boolean z, FormatCompliance formatCompliance) throws IOException, ImageReadException {
        ArrayList arrayList = new ArrayList();
        while (true) {
            int i = inputStream.read();
            if (i == 33) {
                int i2 = inputStream.read();
                int i3 = (255 & i2) | ((i & 255) << 8);
                if (i2 != 1) {
                    if (i2 == 249) {
                        arrayList.add(readGraphicControlExtension(i3, inputStream));
                    } else {
                        switch (i2) {
                            case 254:
                                break;
                            case 255:
                                byte[] subBlock = readSubBlock(inputStream);
                                if (formatCompliance != null) {
                                    formatCompliance.addComment("Unknown Application Extension (" + new String(subBlock, "US-ASCII") + ")", i3);
                                }
                                if (subBlock == null) {
                                    continue;
                                } else if (subBlock.length <= 0) {
                                    break;
                                } else {
                                    arrayList.add(readGenericGIFBlock(inputStream, i3, subBlock));
                                    break;
                                }
                            default:
                                if (formatCompliance != null) {
                                    formatCompliance.addComment("Unknown block", i3);
                                }
                                arrayList.add(readGenericGIFBlock(inputStream, i3));
                                continue;
                        }
                    }
                }
                arrayList.add(readGenericGIFBlock(inputStream, i3));
            } else if (i == 44) {
                arrayList.add(readImageDescriptor(gifHeaderInfo, i, inputStream, z, formatCompliance));
            } else {
                if (i == 59) {
                    return arrayList;
                }
                switch (i) {
                    case -1:
                        throw new ImageReadException("GIF: unexpected end of data");
                    case 0:
                        break;
                    default:
                        throw new ImageReadException("GIF: unknown code: " + i);
                }
            }
        }
    }

    private ImageDescriptor readImageDescriptor(GifHeaderInfo gifHeaderInfo, int i, InputStream inputStream, boolean z, FormatCompliance formatCompliance) throws IOException, ImageReadException {
        byte[] bArrDecompress;
        int i2 = BinaryFunctions.read2Bytes("Image Left Position", inputStream, "Not a Valid GIF File", getByteOrder());
        int i3 = BinaryFunctions.read2Bytes("Image Top Position", inputStream, "Not a Valid GIF File", getByteOrder());
        int i4 = BinaryFunctions.read2Bytes("Image Width", inputStream, "Not a Valid GIF File", getByteOrder());
        int i5 = BinaryFunctions.read2Bytes("Image Height", inputStream, "Not a Valid GIF File", getByteOrder());
        byte b = BinaryFunctions.readByte("Packed Fields", inputStream, "Not a Valid GIF File");
        if (formatCompliance != null) {
            formatCompliance.checkBounds("Width", 1, gifHeaderInfo.logicalScreenWidth, i4);
            formatCompliance.checkBounds("Height", 1, gifHeaderInfo.logicalScreenHeight, i5);
            formatCompliance.checkBounds("Left Position", 0, gifHeaderInfo.logicalScreenWidth - i4, i2);
            formatCompliance.checkBounds("Top Position", 0, gifHeaderInfo.logicalScreenHeight - i5, i3);
        }
        if (getDebug()) {
            BinaryFunctions.printByteBits("PackedFields bits", b);
        }
        boolean z2 = ((b >> 7) & 1) > 0;
        if (getDebug()) {
            System.out.println("LocalColorTableFlag: " + z2);
        }
        boolean z3 = ((b >> 6) & 1) > 0;
        if (getDebug()) {
            System.out.println("Interlace Flag: " + z3);
        }
        boolean z4 = ((b >> 5) & 1) > 0;
        if (getDebug()) {
            System.out.println("Sort Flag: " + z4);
        }
        byte b2 = (byte) (b & 7);
        if (getDebug()) {
            System.out.println("SizeofLocalColorTable: " + ((int) b2));
        }
        byte[] colorTable = z2 ? readColorTable(inputStream, b2) : null;
        if (!z) {
            bArrDecompress = new MyLzwDecompressor(inputStream.read(), ByteOrder.LITTLE_ENDIAN).decompress(new ByteArrayInputStream(readGenericGIFBlock(inputStream, -1).appendSubBlocks()), i4 * i5);
        } else {
            int i6 = inputStream.read();
            if (getDebug()) {
                System.out.println("LZWMinimumCodeSize: " + i6);
            }
            readGenericGIFBlock(inputStream, -1);
            bArrDecompress = null;
        }
        return new ImageDescriptor(i, i2, i3, i4, i5, b, z2, z3, z4, b2, colorTable, bArrDecompress);
    }

    private int convertColorTableSize(int i) {
        return 3 * simplePow(2, i + 1);
    }

    private byte[] readColorTable(InputStream inputStream, int i) throws IOException {
        return BinaryFunctions.readBytes("block", inputStream, convertColorTableSize(i), "GIF: corrupt Color Table");
    }

    private GifBlock findBlock(List<GifBlock> list, int i) {
        for (GifBlock gifBlock : list) {
            if (gifBlock.blockCode == i) {
                return gifBlock;
            }
        }
        return null;
    }

    private ImageContents readFile(ByteSource byteSource, boolean z) throws IOException, ImageReadException {
        return readFile(byteSource, z, FormatCompliance.getDefault());
    }

    private ImageContents readFile(ByteSource byteSource, boolean z, FormatCompliance formatCompliance) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            GifHeaderInfo header = readHeader(inputStream, formatCompliance);
            ImageContents imageContents = new ImageContents(header, header.globalColorTableFlag ? readColorTable(inputStream, header.sizeOfGlobalColorTable) : null, readBlocks(header, inputStream, z, formatCompliance));
            IoUtils.closeQuietly(true, inputStream);
            return imageContents;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        ImageContents file = readFile(byteSource, false);
        if (file == null) {
            throw new ImageReadException("GIF: Couldn't read blocks");
        }
        if (file.gifHeaderInfo == null) {
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        ImageDescriptor imageDescriptor = (ImageDescriptor) findBlock(file.blocks, 44);
        if (imageDescriptor == null) {
            throw new ImageReadException("GIF: Couldn't read ImageDescriptor");
        }
        return new Dimension(imageDescriptor.imageWidth, imageDescriptor.imageHeight);
    }

    private List<String> getComments(List<GifBlock> list) throws IOException {
        ArrayList arrayList = new ArrayList();
        for (GifBlock gifBlock : list) {
            if (gifBlock.blockCode == 8702) {
                arrayList.add(new String(((GenericGifBlock) gifBlock).appendSubBlocks(), "US-ASCII"));
            }
        }
        return arrayList;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        ImageContents file = readFile(byteSource, false);
        if (file == null) {
            throw new ImageReadException("GIF: Couldn't read blocks");
        }
        GifHeaderInfo gifHeaderInfo = file.gifHeaderInfo;
        if (gifHeaderInfo == null) {
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        ImageDescriptor imageDescriptor = (ImageDescriptor) findBlock(file.blocks, 44);
        if (imageDescriptor == null) {
            throw new ImageReadException("GIF: Couldn't read ImageDescriptor");
        }
        GraphicControlExtension graphicControlExtension = (GraphicControlExtension) findBlock(file.blocks, GRAPHIC_CONTROL_EXTENSION);
        int i = imageDescriptor.imageHeight;
        int i2 = imageDescriptor.imageWidth;
        List<String> comments = getComments(file.blocks);
        int i3 = gifHeaderInfo.colorResolution + 1;
        ImageFormats imageFormats = ImageFormats.GIF;
        boolean z = imageDescriptor.interlaceFlag;
        return new ImageInfo("Gif " + ((char) file.gifHeaderInfo.version1) + ((char) file.gifHeaderInfo.version2) + ((char) file.gifHeaderInfo.version3), i3, comments, imageFormats, "GIF Graphics Interchange Format", i, "image/gif", -1, 72, (float) (i / 72.0d), 72, (float) (i2 / 72.0d), i2, z, graphicControlExtension != null && graphicControlExtension.transparency, true, ImageInfo.ColorType.RGB, ImageInfo.CompressionAlgorithm.LZW);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        printWriter.println("gif.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        ImageContents file = readFile(byteSource, false);
        printWriter.println("gif.blocks: " + file.blocks.size());
        for (int i = 0; i < file.blocks.size(); i++) {
            GifBlock gifBlock = file.blocks.get(i);
            debugNumber(printWriter, "\t" + i + " (" + gifBlock.getClass().getName() + ")", gifBlock.blockCode, 4);
        }
        printWriter.println("");
        return true;
    }

    private int[] getColorTable(byte[] bArr) throws ImageReadException {
        if (bArr.length % 3 != 0) {
            throw new ImageReadException("Bad Color Table Length: " + bArr.length);
        }
        int length = bArr.length / 3;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 3;
            iArr[i] = ((bArr[i2 + 2] & 255) << 0) | ((bArr[i2 + 0] & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | ((bArr[i2 + 1] & 255) << 8);
        }
        return iArr;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws Throwable {
        FormatCompliance formatCompliance = new FormatCompliance(byteSource.getDescription());
        readFile(byteSource, false, formatCompliance);
        return formatCompliance;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        int[] colorTable;
        int i;
        int i2 = 0;
        ImageContents file = readFile(byteSource, false);
        if (file == null) {
            throw new ImageReadException("GIF: Couldn't read blocks");
        }
        if (file.gifHeaderInfo == null) {
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        ImageDescriptor imageDescriptor = (ImageDescriptor) findBlock(file.blocks, 44);
        if (imageDescriptor == null) {
            throw new ImageReadException("GIF: Couldn't read Image Descriptor");
        }
        GraphicControlExtension graphicControlExtension = (GraphicControlExtension) findBlock(file.blocks, GRAPHIC_CONTROL_EXTENSION);
        int i3 = imageDescriptor.imageWidth;
        int i4 = imageDescriptor.imageHeight;
        boolean z = graphicControlExtension != null && graphicControlExtension.transparency;
        ImageBuilder imageBuilder = new ImageBuilder(i3, i4, z);
        if (imageDescriptor.localColorTable != null) {
            colorTable = getColorTable(imageDescriptor.localColorTable);
        } else if (file.globalColorTable != null) {
            colorTable = getColorTable(file.globalColorTable);
        } else {
            throw new ImageReadException("Gif: No Color Table");
        }
        int i5 = -1;
        if (graphicControlExtension != null && z) {
            i5 = graphicControlExtension.transparentColorIndex;
        }
        int i6 = (i4 + 7) / 8;
        int i7 = (i4 + 3) / 8;
        int i8 = (i4 + 1) / 4;
        int i9 = i4 / 2;
        int i10 = 0;
        int i11 = 0;
        while (i10 < i4) {
            if (!imageDescriptor.interlaceFlag) {
                i = i10;
            } else if (i10 < i6) {
                i = i10 * 8;
            } else {
                int i12 = i10 - i6;
                if (i12 < i7) {
                    i = (i12 * 8) + 4;
                } else {
                    int i13 = i12 - i7;
                    if (i13 < i8) {
                        i = 2 + (i13 * 4);
                    } else {
                        int i14 = i13 - i8;
                        if (i14 >= i9) {
                            throw new ImageReadException("Gif: Strange Row");
                        }
                        i = (i14 * 2) + 1;
                    }
                }
            }
            int i15 = i11;
            int i16 = i2;
            while (i16 < i3) {
                int i17 = i15 + 1;
                int i18 = 255 & imageDescriptor.imageData[i15];
                imageBuilder.setRGB(i16, i, i5 == i18 ? 0 : colorTable[i18]);
                i16++;
                i15 = i17;
            }
            i10++;
            i11 = i15;
            i2 = 0;
        }
        return imageBuilder.getBufferedImage();
    }

    private void writeAsSubBlocks(OutputStream outputStream, byte[] bArr) throws IOException {
        int i = 0;
        while (i < bArr.length) {
            int iMin = Math.min(bArr.length - i, 255);
            outputStream.write(iMin);
            outputStream.write(bArr, i, iMin);
            i += iMin;
        }
        outputStream.write(0);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        int paletteIndex;
        HashMap map2 = new HashMap(map);
        boolean zEquals = Boolean.TRUE.equals(map2.get(ImagingConstants.PARAM_KEY_VERBOSE));
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        if (map2.containsKey(ImagingConstants.PARAM_KEY_VERBOSE)) {
            map2.remove(ImagingConstants.PARAM_KEY_VERBOSE);
        }
        String str = null;
        if (map2.containsKey(ImagingConstants.PARAM_KEY_XMP_XML)) {
            str = (String) map2.get(ImagingConstants.PARAM_KEY_XMP_XML);
            map2.remove(ImagingConstants.PARAM_KEY_XMP_XML);
        }
        if (!map2.isEmpty()) {
            throw new ImageWriteException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        boolean zHasTransparency = new PaletteFactory().hasTransparency(bufferedImage);
        int i = zHasTransparency ? 255 : 256;
        Palette paletteMakeExactRgbPaletteSimple = new PaletteFactory().makeExactRgbPaletteSimple(bufferedImage, i);
        if (paletteMakeExactRgbPaletteSimple == null) {
            paletteMakeExactRgbPaletteSimple = new PaletteFactory().makeQuantizedRgbPalette(bufferedImage, i);
            if (zEquals) {
                System.out.println("quantizing");
            }
        } else if (zEquals) {
            System.out.println("exact palette");
        }
        if (paletteMakeExactRgbPaletteSimple == null) {
            throw new ImageWriteException("Gif: can't write images with more than 256 colors");
        }
        int length = paletteMakeExactRgbPaletteSimple.length() + (zHasTransparency ? 1 : 0);
        BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        outputStream.write(71);
        outputStream.write(73);
        outputStream.write(70);
        outputStream.write(56);
        outputStream.write(57);
        outputStream.write(97);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(height);
        int i2 = length > 128 ? 7 : length > 64 ? 6 : length > 32 ? 5 : length > 16 ? 4 : length > 8 ? 3 : length > 4 ? 2 : length > 2 ? 1 : 0;
        int i3 = i2 + 1;
        int i4 = 1 << i3;
        binaryOutputStream.write(((((byte) i2) & 7) << 4) | 0 | 0);
        binaryOutputStream.write(0);
        binaryOutputStream.write(0);
        binaryOutputStream.write(33);
        binaryOutputStream.write(-7);
        binaryOutputStream.write(4);
        binaryOutputStream.write(zHasTransparency ? (byte) 1 : (byte) 0);
        binaryOutputStream.write(0);
        binaryOutputStream.write(0);
        binaryOutputStream.write((byte) (zHasTransparency ? paletteMakeExactRgbPaletteSimple.length() : 0));
        binaryOutputStream.write(0);
        if (str != null) {
            binaryOutputStream.write(33);
            binaryOutputStream.write(255);
            binaryOutputStream.write(XMP_APPLICATION_ID_AND_AUTH_CODE.length);
            binaryOutputStream.write(XMP_APPLICATION_ID_AND_AUTH_CODE);
            binaryOutputStream.write(str.getBytes("utf-8"));
            for (int i5 = 0; i5 <= 255; i5++) {
                binaryOutputStream.write(255 - i5);
            }
            binaryOutputStream.write(0);
        }
        binaryOutputStream.write(44);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(height);
        binaryOutputStream.write((i2 & 7) | 128);
        for (int i6 = 0; i6 < i4; i6++) {
            if (i6 < paletteMakeExactRgbPaletteSimple.length()) {
                int entry = paletteMakeExactRgbPaletteSimple.getEntry(i6);
                binaryOutputStream.write((entry >> 16) & 255);
                binaryOutputStream.write((entry >> 8) & 255);
                binaryOutputStream.write((entry >> 0) & 255);
            } else {
                binaryOutputStream.write(0);
                binaryOutputStream.write(0);
                binaryOutputStream.write(0);
            }
        }
        if (i3 < 2) {
            i3 = 2;
        }
        binaryOutputStream.write(i3);
        MyLzwCompressor myLzwCompressor = new MyLzwCompressor(i3, ByteOrder.LITTLE_ENDIAN, false);
        byte[] bArr = new byte[width * height];
        for (int i7 = 0; i7 < height; i7++) {
            for (int i8 = 0; i8 < width; i8++) {
                int rgb = bufferedImage.getRGB(i8, i7);
                int i9 = 16777215 & rgb;
                if (!zHasTransparency) {
                    paletteIndex = paletteMakeExactRgbPaletteSimple.getPaletteIndex(i9);
                } else if (((rgb >> 24) & 255) < 255) {
                    paletteIndex = paletteMakeExactRgbPaletteSimple.length();
                } else {
                    paletteIndex = paletteMakeExactRgbPaletteSimple.getPaletteIndex(i9);
                }
                bArr[(i7 * width) + i8] = (byte) paletteIndex;
            }
        }
        writeAsSubBlocks(binaryOutputStream, myLzwCompressor.compress(bArr));
        binaryOutputStream.write(59);
        binaryOutputStream.close();
        outputStream.close();
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        boolean z;
        InputStream inputStream;
        InputStream inputStream2 = null;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
        }
        try {
            GifHeaderInfo header = readHeader(inputStream, null);
            if (header.globalColorTableFlag) {
                readColorTable(inputStream, header.sizeOfGlobalColorTable);
            }
            List<GifBlock> blocks = readBlocks(header, inputStream, true, null);
            ArrayList arrayList = new ArrayList();
            for (GifBlock gifBlock : blocks) {
                if (gifBlock.blockCode == XMP_COMPLETE_CODE) {
                    byte[] bArrAppendSubBlocks = ((GenericGifBlock) gifBlock).appendSubBlocks(true);
                    if (bArrAppendSubBlocks.length >= XMP_APPLICATION_ID_AND_AUTH_CODE.length && BinaryFunctions.compareBytes(bArrAppendSubBlocks, 0, XMP_APPLICATION_ID_AND_AUTH_CODE, 0, XMP_APPLICATION_ID_AND_AUTH_CODE.length)) {
                        byte[] bArr = new byte[256];
                        for (int i = 0; i <= 255; i++) {
                            bArr[i] = (byte) (255 - i);
                        }
                        if (bArrAppendSubBlocks.length >= XMP_APPLICATION_ID_AND_AUTH_CODE.length + bArr.length) {
                            if (!BinaryFunctions.compareBytes(bArrAppendSubBlocks, bArrAppendSubBlocks.length - bArr.length, bArr, 0, bArr.length)) {
                                throw new ImageReadException("XMP block in GIF missing magic trailer.");
                            }
                            try {
                                arrayList.add(new String(bArrAppendSubBlocks, XMP_APPLICATION_ID_AND_AUTH_CODE.length, bArrAppendSubBlocks.length - (XMP_APPLICATION_ID_AND_AUTH_CODE.length + bArr.length), "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                throw new ImageReadException("Invalid XMP Block in GIF.", e);
                            }
                        }
                    }
                }
            }
            if (arrayList.size() >= 1) {
                if (arrayList.size() > 1) {
                    throw new ImageReadException("More than one XMP Block in GIF.");
                }
                try {
                    String str = (String) arrayList.get(0);
                    IoUtils.closeQuietly(true, inputStream);
                    return str;
                } catch (Throwable th2) {
                    th = th2;
                    inputStream2 = inputStream;
                    z = true;
                    IoUtils.closeQuietly(z, inputStream2);
                    throw th;
                }
            }
            IoUtils.closeQuietly(false, inputStream);
            return null;
        } catch (Throwable th3) {
            th = th3;
            inputStream2 = inputStream;
            z = false;
            IoUtils.closeQuietly(z, inputStream2);
            throw th;
        }
    }
}
