// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.gif;

import org.apache.commons.imaging.palette.Palette;
import org.apache.commons.imaging.palette.SimplePalette;
import org.apache.commons.imaging.common.mylzw.MyLzwCompressor;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.ImageWriteException;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.common.ImageBuilder;
import java.awt.image.BufferedImage;
import java.util.Map;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.mylzw.MyLzwDecompressor;
import java.io.PrintStream;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.FormatCompliance;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageReadException;
import java.util.Iterator;
import java.util.List;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class GifImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final int APPLICATION_EXTENSION_LABEL = 255;
    private static final int COMMENT_EXTENSION = 254;
    private static final String DEFAULT_EXTENSION = ".gif";
    private static final int EXTENSION_CODE = 33;
    private static final byte[] GIF_HEADER_SIGNATURE;
    private static final int GRAPHIC_CONTROL_EXTENSION = 8697;
    private static final int IMAGE_SEPARATOR = 44;
    private static final int INTERLACE_FLAG_MASK = 64;
    private static final int LOCAL_COLOR_TABLE_FLAG_MASK = 128;
    private static final int PLAIN_TEXT_EXTENSION = 1;
    private static final int SORT_FLAG_MASK = 32;
    private static final int TERMINATOR_BYTE = 59;
    private static final byte[] XMP_APPLICATION_ID_AND_AUTH_CODE;
    private static final int XMP_COMPLETE_CODE = 8703;
    private static final int XMP_EXTENSION = 255;
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".gif" };
        GIF_HEADER_SIGNATURE = new byte[] { 71, 73, 70 };
        XMP_APPLICATION_ID_AND_AUTH_CODE = new byte[] { 88, 77, 80, 32, 68, 97, 116, 97, 88, 77, 80 };
    }
    
    public GifImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }
    
    private int convertColorTableSize(final int n) {
        return 3 * this.simplePow(2, n + 1);
    }
    
    private GifBlock findBlock(final List<GifBlock> list, final int n) {
        for (final GifBlock gifBlock : list) {
            if (gifBlock.blockCode == n) {
                return gifBlock;
            }
        }
        return null;
    }
    
    private int[] getColorTable(final byte[] array) throws ImageReadException {
        if (array.length % 3 != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad Color Table Length: ");
            sb.append(array.length);
            throw new ImageReadException(sb.toString());
        }
        final int n = array.length / 3;
        final int[] array2 = new int[n];
        for (int i = 0; i < n; ++i) {
            final int n2 = i * 3;
            array2[i] = ((array[n2 + 2] & 0xFF) << 0 | ((array[n2 + 0] & 0xFF) << 16 | 0xFF000000 | (array[n2 + 1] & 0xFF) << 8));
        }
        return array2;
    }
    
    private List<String> getComments(final List<GifBlock> list) throws IOException {
        final ArrayList list2 = new ArrayList();
        for (final GifBlock gifBlock : list) {
            if (gifBlock.blockCode == 8702) {
                list2.add(new String(((GenericGifBlock)gifBlock).appendSubBlocks(), "US-ASCII"));
            }
        }
        return list2;
    }
    
    private List<GifBlock> readBlocks(final GifHeaderInfo gifHeaderInfo, final InputStream inputStream, final boolean b, final FormatCompliance formatCompliance) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        while (true) {
            final int read = inputStream.read();
            if (read != 33) {
                if (read != 44) {
                    if (read == 59) {
                        return list;
                    }
                    switch (read) {
                        case 0: {
                            continue;
                        }
                        default: {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("GIF: unknown code: ");
                            sb.append(read);
                            throw new ImageReadException(sb.toString());
                        }
                        case -1: {
                            throw new ImageReadException("GIF: unexpected end of data");
                        }
                    }
                }
                else {
                    list.add(this.readImageDescriptor(gifHeaderInfo, read, inputStream, b, formatCompliance));
                }
            }
            else {
                final int read2 = inputStream.read();
                final int n = (0xFF & read2) | (read & 0xFF) << 8;
                if (read2 != 1) {
                    if (read2 == 249) {
                        list.add(this.readGraphicControlExtension(n, inputStream));
                        continue;
                    }
                    switch (read2) {
                        default: {
                            if (formatCompliance != null) {
                                formatCompliance.addComment("Unknown block", n);
                            }
                            list.add(this.readGenericGIFBlock(inputStream, n));
                            continue;
                        }
                        case 255: {
                            final byte[] subBlock = this.readSubBlock(inputStream);
                            if (formatCompliance != null) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Unknown Application Extension (");
                                sb2.append(new String(subBlock, "US-ASCII"));
                                sb2.append(")");
                                formatCompliance.addComment(sb2.toString(), n);
                            }
                            if (subBlock != null && subBlock.length > 0) {
                                list.add(this.readGenericGIFBlock(inputStream, n, subBlock));
                                continue;
                            }
                            continue;
                        }
                        case 254: {
                            break;
                        }
                    }
                }
                list.add(this.readGenericGIFBlock(inputStream, n));
            }
        }
    }
    
    private byte[] readColorTable(final InputStream inputStream, final int n) throws IOException {
        return BinaryFunctions.readBytes("block", inputStream, this.convertColorTableSize(n), "GIF: corrupt Color Table");
    }
    
    private ImageContents readFile(final ByteSource byteSource, final boolean b) throws ImageReadException, IOException {
        return this.readFile(byteSource, b, FormatCompliance.getDefault());
    }
    
    private ImageContents readFile(final ByteSource byteSource, final boolean b, final FormatCompliance formatCompliance) throws ImageReadException, IOException {
        final byte[] array = null;
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final GifHeaderInfo header = this.readHeader(inputStream, formatCompliance);
                byte[] colorTable = array;
                if (header.globalColorTableFlag) {
                    colorTable = this.readColorTable(inputStream, header.sizeOfGlobalColorTable);
                }
                final ImageContents imageContents = new ImageContents(header, colorTable, this.readBlocks(header, inputStream, b, formatCompliance));
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
    
    private GenericGifBlock readGenericGIFBlock(final InputStream inputStream, final int n) throws IOException {
        return this.readGenericGIFBlock(inputStream, n, null);
    }
    
    private GenericGifBlock readGenericGIFBlock(final InputStream inputStream, final int n, byte[] subBlock) throws IOException {
        final ArrayList list = new ArrayList();
        if (subBlock != null) {
            list.add(subBlock);
        }
        while (true) {
            subBlock = this.readSubBlock(inputStream);
            if (subBlock.length < 1) {
                break;
            }
            list.add(subBlock);
        }
        return new GenericGifBlock(n, list);
    }
    
    private GraphicControlExtension readGraphicControlExtension(final int n, final InputStream inputStream) throws IOException {
        BinaryFunctions.readByte("block_size", inputStream, "GIF: corrupt GraphicControlExt");
        final byte byte1 = BinaryFunctions.readByte("packed fields", inputStream, "GIF: corrupt GraphicControlExt");
        final boolean b = (byte1 & 0x1) != 0x0;
        final int read2Bytes = BinaryFunctions.read2Bytes("delay in milliseconds", inputStream, "GIF: corrupt GraphicControlExt", this.getByteOrder());
        final byte byte2 = BinaryFunctions.readByte("transparent color index", inputStream, "GIF: corrupt GraphicControlExt");
        BinaryFunctions.readByte("block terminator", inputStream, "GIF: corrupt GraphicControlExt");
        return new GraphicControlExtension(n, byte1, (byte1 & 0x1C) >> 2, b, read2Bytes, 0xFF & byte2);
    }
    
    private GifHeaderInfo readHeader(final InputStream inputStream, final FormatCompliance formatCompliance) throws ImageReadException, IOException {
        final byte byte1 = BinaryFunctions.readByte("identifier1", inputStream, "Not a Valid GIF File");
        final byte byte2 = BinaryFunctions.readByte("identifier2", inputStream, "Not a Valid GIF File");
        final byte byte3 = BinaryFunctions.readByte("identifier3", inputStream, "Not a Valid GIF File");
        final byte byte4 = BinaryFunctions.readByte("version1", inputStream, "Not a Valid GIF File");
        final byte byte5 = BinaryFunctions.readByte("version2", inputStream, "Not a Valid GIF File");
        final byte byte6 = BinaryFunctions.readByte("version3", inputStream, "Not a Valid GIF File");
        if (formatCompliance != null) {
            formatCompliance.compareBytes("Signature", GifImageParser.GIF_HEADER_SIGNATURE, new byte[] { byte1, byte2, byte3 });
            formatCompliance.compare("version", 56, byte4);
            formatCompliance.compare("version", new int[] { 55, 57 }, byte5);
            formatCompliance.compare("version", 97, byte6);
        }
        if (this.getDebug()) {
            BinaryFunctions.printCharQuad("identifier: ", byte1 << 16 | byte2 << 8 | byte3 << 0);
            BinaryFunctions.printCharQuad("version: ", byte4 << 16 | byte5 << 8 | byte6 << 0);
        }
        final int read2Bytes = BinaryFunctions.read2Bytes("Logical Screen Width", inputStream, "Not a Valid GIF File", this.getByteOrder());
        final int read2Bytes2 = BinaryFunctions.read2Bytes("Logical Screen Height", inputStream, "Not a Valid GIF File", this.getByteOrder());
        if (formatCompliance != null) {
            formatCompliance.checkBounds("Width", 1, Integer.MAX_VALUE, read2Bytes);
            formatCompliance.checkBounds("Height", 1, Integer.MAX_VALUE, read2Bytes2);
        }
        final byte byte7 = BinaryFunctions.readByte("Packed Fields", inputStream, "Not a Valid GIF File");
        final byte byte8 = BinaryFunctions.readByte("Background Color Index", inputStream, "Not a Valid GIF File");
        final byte byte9 = BinaryFunctions.readByte("Pixel Aspect Ratio", inputStream, "Not a Valid GIF File");
        if (this.getDebug()) {
            BinaryFunctions.printByteBits("PackedFields bits", byte7);
        }
        final boolean b = (byte7 & 0x80) > 0;
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("GlobalColorTableFlag: ");
            sb.append(b);
            out.println(sb.toString());
        }
        final byte i = (byte)(byte7 >> 4 & 0x7);
        if (this.getDebug()) {
            final PrintStream out2 = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("ColorResolution: ");
            sb2.append(i);
            out2.println(sb2.toString());
        }
        final boolean b2 = (byte7 & 0x8) > 0;
        if (this.getDebug()) {
            final PrintStream out3 = System.out;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("SortFlag: ");
            sb3.append(b2);
            out3.println(sb3.toString());
        }
        final byte j = (byte)(byte7 & 0x7);
        if (this.getDebug()) {
            final PrintStream out4 = System.out;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("SizeofGlobalColorTable: ");
            sb4.append(j);
            out4.println(sb4.toString());
        }
        if (formatCompliance != null && b && byte8 != -1) {
            formatCompliance.checkBounds("Background Color Index", 0, this.convertColorTableSize(j), byte8);
        }
        return new GifHeaderInfo(byte1, byte2, byte3, byte4, byte5, byte6, read2Bytes, read2Bytes2, byte7, byte8, byte9, b, i, b2, j);
    }
    
    private ImageDescriptor readImageDescriptor(final GifHeaderInfo gifHeaderInfo, final int n, final InputStream inputStream, final boolean b, final FormatCompliance formatCompliance) throws ImageReadException, IOException {
        final int read2Bytes = BinaryFunctions.read2Bytes("Image Left Position", inputStream, "Not a Valid GIF File", this.getByteOrder());
        final int read2Bytes2 = BinaryFunctions.read2Bytes("Image Top Position", inputStream, "Not a Valid GIF File", this.getByteOrder());
        final int read2Bytes3 = BinaryFunctions.read2Bytes("Image Width", inputStream, "Not a Valid GIF File", this.getByteOrder());
        final int read2Bytes4 = BinaryFunctions.read2Bytes("Image Height", inputStream, "Not a Valid GIF File", this.getByteOrder());
        final byte byte1 = BinaryFunctions.readByte("Packed Fields", inputStream, "Not a Valid GIF File");
        boolean b2 = false;
        if (formatCompliance != null) {
            formatCompliance.checkBounds("Width", 1, gifHeaderInfo.logicalScreenWidth, read2Bytes3);
            formatCompliance.checkBounds("Height", 1, gifHeaderInfo.logicalScreenHeight, read2Bytes4);
            formatCompliance.checkBounds("Left Position", 0, gifHeaderInfo.logicalScreenWidth - read2Bytes3, read2Bytes);
            formatCompliance.checkBounds("Top Position", 0, gifHeaderInfo.logicalScreenHeight - read2Bytes4, read2Bytes2);
        }
        if (this.getDebug()) {
            BinaryFunctions.printByteBits("PackedFields bits", byte1);
        }
        final boolean b3 = (byte1 >> 7 & 0x1) > 0;
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("LocalColorTableFlag: ");
            sb.append(b3);
            out.println(sb.toString());
        }
        final boolean b4 = (byte1 >> 6 & 0x1) > 0;
        if (this.getDebug()) {
            final PrintStream out2 = System.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Interlace Flag: ");
            sb2.append(b4);
            out2.println(sb2.toString());
        }
        if ((byte1 >> 5 & 0x1) > 0) {
            b2 = true;
        }
        if (this.getDebug()) {
            final PrintStream out3 = System.out;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Sort Flag: ");
            sb3.append(b2);
            out3.println(sb3.toString());
        }
        final byte i = (byte)(byte1 & 0x7);
        if (this.getDebug()) {
            final PrintStream out4 = System.out;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("SizeofLocalColorTable: ");
            sb4.append(i);
            out4.println(sb4.toString());
        }
        byte[] colorTable;
        if (b3) {
            colorTable = this.readColorTable(inputStream, i);
        }
        else {
            colorTable = null;
        }
        byte[] decompress;
        if (!b) {
            decompress = new MyLzwDecompressor(inputStream.read(), ByteOrder.LITTLE_ENDIAN).decompress(new ByteArrayInputStream(this.readGenericGIFBlock(inputStream, -1).appendSubBlocks()), read2Bytes3 * read2Bytes4);
        }
        else {
            final int read = inputStream.read();
            if (this.getDebug()) {
                final PrintStream out5 = System.out;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("LZWMinimumCodeSize: ");
                sb5.append(read);
                out5.println(sb5.toString());
            }
            this.readGenericGIFBlock(inputStream, -1);
            decompress = null;
        }
        return new ImageDescriptor(n, read2Bytes, read2Bytes2, read2Bytes3, read2Bytes4, byte1, b3, b4, b2, i, colorTable, decompress);
    }
    
    private byte[] readSubBlock(final InputStream inputStream) throws IOException {
        return BinaryFunctions.readBytes("block", inputStream, BinaryFunctions.readByte("block_size", inputStream, "GIF: corrupt block") & 0xFF, "GIF: corrupt block");
    }
    
    private int simplePow(final int n, final int n2) {
        int n3 = 1;
        for (int i = 0; i < n2; ++i) {
            n3 *= n;
        }
        return n3;
    }
    
    private void writeAsSubBlocks(final OutputStream outputStream, final byte[] b) throws IOException {
        int min;
        for (int i = 0; i < b.length; i += min) {
            min = Math.min(b.length - i, 255);
            outputStream.write(min);
            outputStream.write(b, i, min);
        }
        outputStream.write(0);
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
        final ImageContents file = this.readFile(byteSource, false);
        final StringBuilder sb = new StringBuilder();
        sb.append("gif.blocks: ");
        sb.append(file.blocks.size());
        printWriter.println(sb.toString());
        while (i < file.blocks.size()) {
            final GifBlock gifBlock = file.blocks.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("\t");
            sb2.append(i);
            sb2.append(" (");
            sb2.append(gifBlock.getClass().getName());
            sb2.append(")");
            this.debugNumber(printWriter, sb2.toString(), gifBlock.blockCode, 4);
            ++i;
        }
        printWriter.println("");
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return GifImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.GIF };
    }
    
    @Override
    public BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageContents file = this.readFile(byteSource, false);
        if (file == null) {
            throw new ImageReadException("GIF: Couldn't read blocks");
        }
        if (file.gifHeaderInfo == null) {
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        final ImageDescriptor imageDescriptor = (ImageDescriptor)this.findBlock(file.blocks, 44);
        if (imageDescriptor == null) {
            throw new ImageReadException("GIF: Couldn't read Image Descriptor");
        }
        final GraphicControlExtension graphicControlExtension = (GraphicControlExtension)this.findBlock(file.blocks, 8697);
        final int imageWidth = imageDescriptor.imageWidth;
        final int imageHeight = imageDescriptor.imageHeight;
        final boolean b = graphicControlExtension != null && graphicControlExtension.transparency;
        final ImageBuilder imageBuilder = new ImageBuilder(imageWidth, imageHeight, b);
        int[] array;
        if (imageDescriptor.localColorTable != null) {
            array = this.getColorTable(imageDescriptor.localColorTable);
        }
        else {
            if (file.globalColorTable == null) {
                throw new ImageReadException("Gif: No Color Table");
            }
            array = this.getColorTable(file.globalColorTable);
        }
        int transparentColorIndex = -1;
        if (graphicControlExtension != null) {
            transparentColorIndex = transparentColorIndex;
            if (b) {
                transparentColorIndex = graphicControlExtension.transparentColorIndex;
            }
        }
        final int n = (imageHeight + 7) / 8;
        final int n2 = (imageHeight + 3) / 8;
        final int n3 = (imageHeight + 1) / 4;
        final int n4 = imageHeight / 2;
        int i = 0;
        int n5 = 0;
        while (i < imageHeight) {
            int n6;
            if (imageDescriptor.interlaceFlag) {
                if (i < n) {
                    n6 = i * 8;
                }
                else {
                    final int n7 = i - n;
                    if (n7 < n2) {
                        n6 = n7 * 8 + 4;
                    }
                    else {
                        final int n8 = n7 - n2;
                        if (n8 < n3) {
                            n6 = 2 + n8 * 4;
                        }
                        else {
                            final int n9 = n8 - n3;
                            if (n9 >= n4) {
                                throw new ImageReadException("Gif: Strange Row");
                            }
                            n6 = n9 * 2 + 1;
                        }
                    }
                }
            }
            else {
                n6 = i;
            }
            for (int j = 0; j < imageWidth; ++j, ++n5) {
                final int n10 = 0xFF & imageDescriptor.imageData[n5];
                int n11 = array[n10];
                if (transparentColorIndex == n10) {
                    n11 = 0;
                }
                imageBuilder.setRGB(j, n6, n11);
            }
            ++i;
        }
        return imageBuilder.getBufferedImage();
    }
    
    @Override
    public String getDefaultExtension() {
        return ".gif";
    }
    
    @Override
    public FormatCompliance getFormatCompliance(final ByteSource byteSource) throws ImageReadException, IOException {
        final FormatCompliance formatCompliance = new FormatCompliance(byteSource.getDescription());
        this.readFile(byteSource, false, formatCompliance);
        return formatCompliance;
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageContents file = this.readFile(byteSource, false);
        if (file == null) {
            throw new ImageReadException("GIF: Couldn't read blocks");
        }
        final GifHeaderInfo gifHeaderInfo = file.gifHeaderInfo;
        if (gifHeaderInfo == null) {
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        final ImageDescriptor imageDescriptor = (ImageDescriptor)this.findBlock(file.blocks, 44);
        if (imageDescriptor == null) {
            throw new ImageReadException("GIF: Couldn't read ImageDescriptor");
        }
        final GraphicControlExtension graphicControlExtension = (GraphicControlExtension)this.findBlock(file.blocks, 8697);
        final int imageHeight = imageDescriptor.imageHeight;
        final int imageWidth = imageDescriptor.imageWidth;
        final List<String> comments = this.getComments(file.blocks);
        final byte colorResolution = gifHeaderInfo.colorResolution;
        final ImageFormats gif = ImageFormats.GIF;
        final boolean interlaceFlag = imageDescriptor.interlaceFlag;
        final float n = (float)(imageWidth / 72.0);
        final float n2 = (float)(imageHeight / 72.0);
        final StringBuilder sb = new StringBuilder();
        sb.append("Gif ");
        sb.append((char)file.gifHeaderInfo.version1);
        sb.append((char)file.gifHeaderInfo.version2);
        sb.append((char)file.gifHeaderInfo.version3);
        return new ImageInfo(sb.toString(), colorResolution + 1, comments, gif, "GIF Graphics Interchange Format", imageHeight, "image/gif", -1, 72, n2, 72, n, imageWidth, interlaceFlag, graphicControlExtension != null && graphicControlExtension.transparency, true, ImageInfo.ColorType.RGB, ImageInfo.CompressionAlgorithm.LZW);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageContents file = this.readFile(byteSource, false);
        if (file == null) {
            throw new ImageReadException("GIF: Couldn't read blocks");
        }
        if (file.gifHeaderInfo == null) {
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        final ImageDescriptor imageDescriptor = (ImageDescriptor)this.findBlock(file.blocks, 44);
        if (imageDescriptor == null) {
            throw new ImageReadException("GIF: Couldn't read ImageDescriptor");
        }
        return new Dimension(imageDescriptor.imageWidth, imageDescriptor.imageHeight);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "Graphics Interchange Format";
    }
    
    @Override
    public String getXmpXml(ByteSource inputStream, Map<String, Object> header) throws ImageReadException, IOException {
        header = null;
        try {
            inputStream = (ByteSource)inputStream.getInputStream();
            try {
                header = this.readHeader((InputStream)inputStream, null);
                if (((GifHeaderInfo)header).globalColorTableFlag) {
                    this.readColorTable((InputStream)inputStream, ((GifHeaderInfo)header).sizeOfGlobalColorTable);
                }
                final List<GifBlock> blocks = this.readBlocks((GifHeaderInfo)header, (InputStream)inputStream, true, null);
                header = new ArrayList();
                for (final GifBlock gifBlock : blocks) {
                    if (gifBlock.blockCode != 8703) {
                        continue;
                    }
                    final byte[] appendSubBlocks = ((GenericGifBlock)gifBlock).appendSubBlocks(true);
                    if (appendSubBlocks.length < GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE.length) {
                        continue;
                    }
                    if (!BinaryFunctions.compareBytes(appendSubBlocks, 0, GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE, 0, GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE.length)) {
                        continue;
                    }
                    final byte[] array = new byte[256];
                    for (int i = 0; i <= 255; ++i) {
                        array[i] = (byte)(255 - i);
                    }
                    if (appendSubBlocks.length < GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE.length + array.length) {
                        continue;
                    }
                    if (!BinaryFunctions.compareBytes(appendSubBlocks, appendSubBlocks.length - array.length, array, 0, array.length)) {
                        header = new ImageReadException("XMP block in GIF missing magic trailer.");
                        throw header;
                    }
                    try {
                        ((List<String>)header).add(new String(appendSubBlocks, GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE.length, appendSubBlocks.length - (GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE.length + array.length), "utf-8"));
                        continue;
                    }
                    catch (final UnsupportedEncodingException header) {
                        throw new ImageReadException("Invalid XMP Block in GIF.", (Throwable)header);
                    }
                    break;
                }
                if (((List)header).size() < 1) {
                    IoUtils.closeQuietly(false, (Closeable)inputStream);
                    return null;
                }
                if (((List)header).size() > 1) {
                    header = new ImageReadException("More than one XMP Block in GIF.");
                    throw header;
                }
                try {
                    final String s = ((List<String>)header).get(0);
                    IoUtils.closeQuietly(true, (Closeable)inputStream);
                    return s;
                }
                finally {
                    final boolean b = true;
                }
            }
            finally {
                header = inputStream;
            }
        }
        finally {}
        final boolean b = false;
        final Throwable t2;
        final Throwable t = t2;
        IoUtils.closeQuietly(b, (Closeable)header);
        throw t;
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> m) throws ImageWriteException, IOException {
        final HashMap hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        final boolean equals = Boolean.TRUE.equals(hashMap.get("VERBOSE"));
        if (hashMap.containsKey("FORMAT")) {
            hashMap.remove("FORMAT");
        }
        if (hashMap.containsKey("VERBOSE")) {
            hashMap.remove("VERBOSE");
        }
        String s = null;
        if (hashMap.containsKey("XMP_XML")) {
            s = (String)hashMap.get("XMP_XML");
            hashMap.remove("XMP_XML");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageWriteException(sb.toString());
        }
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int hasTransparency = new PaletteFactory().hasTransparency(bufferedImage) ? 1 : 0;
        int n;
        if (hasTransparency != 0) {
            n = 255;
        }
        else {
            n = 256;
        }
        final SimplePalette exactRgbPaletteSimple = new PaletteFactory().makeExactRgbPaletteSimple(bufferedImage, n);
        Palette quantizedRgbPalette;
        if (exactRgbPaletteSimple == null) {
            final Palette palette = quantizedRgbPalette = new PaletteFactory().makeQuantizedRgbPalette(bufferedImage, n);
            if (equals) {
                System.out.println("quantizing");
                quantizedRgbPalette = palette;
            }
        }
        else {
            quantizedRgbPalette = exactRgbPaletteSimple;
            if (equals) {
                System.out.println("exact palette");
                quantizedRgbPalette = exactRgbPaletteSimple;
            }
        }
        if (quantizedRgbPalette == null) {
            throw new ImageWriteException("Gif: can't write images with more than 256 colors");
        }
        final int n2 = quantizedRgbPalette.length() + hasTransparency;
        final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        outputStream.write(71);
        outputStream.write(73);
        outputStream.write(70);
        outputStream.write(56);
        outputStream.write(57);
        outputStream.write(97);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(height);
        int n3;
        if (n2 > 128) {
            n3 = 7;
        }
        else if (n2 > 64) {
            n3 = 6;
        }
        else if (n2 > 32) {
            n3 = 5;
        }
        else if (n2 > 16) {
            n3 = 4;
        }
        else if (n2 > 8) {
            n3 = 3;
        }
        else if (n2 > 4) {
            n3 = 2;
        }
        else if (n2 > 2) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        final int n4 = n3 + 1;
        binaryOutputStream.write(((byte)n3 & 0x7) << 4 | 0x0 | 0x0);
        binaryOutputStream.write(0);
        binaryOutputStream.write(0);
        binaryOutputStream.write(33);
        binaryOutputStream.write(-7);
        binaryOutputStream.write(4);
        binaryOutputStream.write((byte)hasTransparency);
        binaryOutputStream.write(0);
        binaryOutputStream.write(0);
        int length;
        if (hasTransparency != 0) {
            length = quantizedRgbPalette.length();
        }
        else {
            length = 0;
        }
        binaryOutputStream.write((byte)length);
        binaryOutputStream.write(0);
        if (s != null) {
            binaryOutputStream.write(33);
            binaryOutputStream.write(255);
            binaryOutputStream.write(GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE.length);
            binaryOutputStream.write(GifImageParser.XMP_APPLICATION_ID_AND_AUTH_CODE);
            binaryOutputStream.write(s.getBytes("utf-8"));
            for (int i = 0; i <= 255; ++i) {
                binaryOutputStream.write(255 - i);
            }
            binaryOutputStream.write(0);
        }
        binaryOutputStream.write(44);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(height);
        binaryOutputStream.write((n3 & 0x7) | 0x80);
        for (int j = 0; j < 1 << n4; ++j) {
            if (j < quantizedRgbPalette.length()) {
                final int entry = quantizedRgbPalette.getEntry(j);
                binaryOutputStream.write(entry >> 16 & 0xFF);
                binaryOutputStream.write(entry >> 8 & 0xFF);
                binaryOutputStream.write(entry >> 0 & 0xFF);
            }
            else {
                binaryOutputStream.write(0);
                binaryOutputStream.write(0);
                binaryOutputStream.write(0);
            }
        }
        int n5;
        if ((n5 = n4) < 2) {
            n5 = 2;
        }
        binaryOutputStream.write(n5);
        final MyLzwCompressor myLzwCompressor = new MyLzwCompressor(n5, ByteOrder.LITTLE_ENDIAN, false);
        final byte[] array = new byte[width * height];
        for (int k = 0; k < height; ++k) {
            for (int l = 0; l < width; ++l) {
                final int rgb = bufferedImage.getRGB(l, k);
                final int n6 = 0xFFFFFF & rgb;
                int n7;
                if (hasTransparency != 0) {
                    if ((rgb >> 24 & 0xFF) < 255) {
                        n7 = quantizedRgbPalette.length();
                    }
                    else {
                        n7 = quantizedRgbPalette.getPaletteIndex(n6);
                    }
                }
                else {
                    n7 = quantizedRgbPalette.getPaletteIndex(n6);
                }
                array[k * width + l] = (byte)n7;
            }
        }
        this.writeAsSubBlocks(binaryOutputStream, myLzwCompressor.compress(array));
        binaryOutputStream.write(59);
        binaryOutputStream.close();
        outputStream.close();
    }
}
