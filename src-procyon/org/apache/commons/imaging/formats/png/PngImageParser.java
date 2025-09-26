// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import org.apache.commons.imaging.ImageWriteException;
import java.io.OutputStream;
import org.apache.commons.imaging.formats.png.chunks.PngTextChunk;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.color.ICC_ColorSpace;
import org.apache.commons.imaging.ColorTools;
import org.apache.commons.imaging.icc.IccProfileParser;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import java.awt.color.ICC_Profile;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.util.Map;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import java.io.PrintWriter;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.png.chunks.PngChunkItxt;
import org.apache.commons.imaging.formats.png.chunks.PngChunkGama;
import org.apache.commons.imaging.formats.png.chunks.PngChunkIdat;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPhys;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPlte;
import org.apache.commons.imaging.formats.png.chunks.PngChunkIhdr;
import org.apache.commons.imaging.formats.png.chunks.PngChunkZtxt;
import org.apache.commons.imaging.formats.png.chunks.PngChunkText;
import org.apache.commons.imaging.formats.png.chunks.PngChunkIccp;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilterGrayscale;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilterTrueColor;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilterIndexedColor;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilter;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.commons.imaging.formats.png.chunks.PngChunk;
import java.util.List;
import org.apache.commons.imaging.ImageParser;

public class PngImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".png";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".png" };
    }
    
    private List<PngChunk> filterChunks(final List<PngChunk> list, final ChunkType chunkType) {
        final ArrayList list2 = new ArrayList();
        for (final PngChunk pngChunk : list) {
            if (pngChunk.chunkType == chunkType.value) {
                list2.add(pngChunk);
            }
        }
        return list2;
    }
    
    public static String getChunkTypeName(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append((char)(n >> 24 & 0xFF));
        sb.append((char)(n >> 16 & 0xFF));
        sb.append((char)(n >> 8 & 0xFF));
        sb.append((char)(n >> 0 & 0xFF));
        return sb.toString();
    }
    
    private TransparencyFilter getTransparencyFilter(final PngColorType obj, final PngChunk pngChunk) throws ImageReadException, IOException {
        switch (PngImageParser$1.$SwitchMap$org$apache$commons$imaging$formats$png$PngColorType[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Simple Transparency not compatible with ColorType: ");
                sb.append(obj);
                throw new ImageReadException(sb.toString());
            }
            case 3: {
                return new TransparencyFilterIndexedColor(pngChunk.getBytes());
            }
            case 2: {
                return new TransparencyFilterTrueColor(pngChunk.getBytes());
            }
            case 1: {
                return new TransparencyFilterGrayscale(pngChunk.getBytes());
            }
        }
    }
    
    private boolean keepChunk(final int n, final ChunkType[] array) {
        if (array == null) {
            return true;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i].value == n) {
                return true;
            }
        }
        return false;
    }
    
    private List<PngChunk> readChunks(final InputStream inputStream, final ChunkType[] array, final boolean b) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        int i;
        do {
            if (this.getDebug()) {
                System.out.println("");
            }
            final int read4Bytes = BinaryFunctions.read4Bytes("Length", inputStream, "Not a Valid PNG File", this.getByteOrder());
            i = BinaryFunctions.read4Bytes("ChunkType", inputStream, "Not a Valid PNG File", this.getByteOrder());
            if (this.getDebug()) {
                BinaryFunctions.printCharQuad("ChunkType", i);
                this.debugNumber("Length", read4Bytes, 4);
            }
            final boolean keepChunk = this.keepChunk(i, array);
            byte[] bytes = null;
            if (keepChunk) {
                bytes = BinaryFunctions.readBytes("Chunk Data", inputStream, read4Bytes, "Not a Valid PNG File: Couldn't read Chunk Data.");
            }
            else {
                BinaryFunctions.skipBytes(inputStream, read4Bytes, "Not a Valid PNG File");
            }
            if (this.getDebug() && bytes != null) {
                this.debugNumber("bytes", bytes.length, 4);
            }
            final int read4Bytes2 = BinaryFunctions.read4Bytes("CRC", inputStream, "Not a Valid PNG File", this.getByteOrder());
            if (keepChunk) {
                if (i == ChunkType.iCCP.value) {
                    list.add(new PngChunkIccp(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.tEXt.value) {
                    list.add(new PngChunkText(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.zTXt.value) {
                    list.add(new PngChunkZtxt(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.IHDR.value) {
                    list.add(new PngChunkIhdr(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.PLTE.value) {
                    list.add(new PngChunkPlte(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.pHYs.value) {
                    list.add(new PngChunkPhys(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.IDAT.value) {
                    list.add(new PngChunkIdat(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.gAMA.value) {
                    list.add(new PngChunkGama(read4Bytes, i, read4Bytes2, bytes));
                }
                else if (i == ChunkType.iTXt.value) {
                    list.add(new PngChunkItxt(read4Bytes, i, read4Bytes2, bytes));
                }
                else {
                    list.add(new PngChunk(read4Bytes, i, read4Bytes2, bytes));
                }
                if (b) {
                    return list;
                }
                continue;
            }
        } while (i != ChunkType.IEND.value);
        return list;
    }
    
    private List<PngChunk> readChunks(final ByteSource byteSource, final ChunkType[] array, final boolean b) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                this.readSignature(inputStream);
                final List<PngChunk> chunks = this.readChunks(inputStream, array, b);
                IoUtils.closeQuietly(true, inputStream);
                return chunks;
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
        final ImageInfo imageInfo = this.getImageInfo(byteSource);
        int i = 0;
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        final List<PngChunk> chunks = this.readChunks(byteSource, null, false);
        final List<PngChunk> filterChunks = this.filterChunks(chunks, ChunkType.IHDR);
        if (filterChunks.size() == 0) {
            if (this.getDebug()) {
                System.out.println("PNG contains more than one Header");
            }
            return false;
        }
        final PngChunkIhdr pngChunkIhdr = filterChunks.get(0);
        final StringBuilder sb = new StringBuilder();
        sb.append("Color: ");
        sb.append(pngChunkIhdr.pngColorType.name());
        printWriter.println(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("chunks: ");
        sb2.append(chunks.size());
        printWriter.println(sb2.toString());
        if (chunks.isEmpty()) {
            return false;
        }
        while (i < chunks.size()) {
            final PngChunk pngChunk = chunks.get(i);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("\t");
            sb3.append(i);
            sb3.append(": ");
            BinaryFunctions.printCharQuad(printWriter, sb3.toString(), pngChunk.chunkType);
            ++i;
        }
        printWriter.println("");
        printWriter.flush();
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return PngImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.PNG };
    }
    
    @Override
    public BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> m) throws ImageReadException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        if (hashMap.containsKey("VERBOSE")) {
            hashMap.remove("VERBOSE");
        }
        final ChunkType ihdr = ChunkType.IHDR;
        boolean b = false;
        final List<PngChunk> chunks = this.readChunks(byteSource, new ChunkType[] { ihdr, ChunkType.PLTE, ChunkType.IDAT, ChunkType.tRNS, ChunkType.iCCP, ChunkType.gAMA, ChunkType.sRGB }, false);
        if (chunks == null || chunks.isEmpty()) {
            throw new ImageReadException("PNG: no chunks");
        }
        final List<PngChunk> filterChunks = this.filterChunks(chunks, ChunkType.IHDR);
        if (filterChunks.size() != 1) {
            throw new ImageReadException("PNG contains more than one Header");
        }
        final PngChunkIhdr pngChunkIhdr = filterChunks.get(0);
        final List<PngChunk> filterChunks2 = this.filterChunks(chunks, ChunkType.PLTE);
        if (filterChunks2.size() > 1) {
            throw new ImageReadException("PNG contains more than one Palette");
        }
        final int size = filterChunks2.size();
        ICC_Profile instance = null;
        PngChunkPlte pngChunkPlte;
        if (size == 1) {
            pngChunkPlte = filterChunks2.get(0);
        }
        else {
            pngChunkPlte = null;
        }
        final List<PngChunk> filterChunks3 = this.filterChunks(chunks, ChunkType.IDAT);
        if (filterChunks3.isEmpty()) {
            throw new ImageReadException("PNG missing image data");
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Iterator<PngChunk> iterator = filterChunks3.iterator();
        while (iterator.hasNext()) {
            byteArrayOutputStream.write(iterator.next().getBytes());
        }
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        final List<PngChunk> filterChunks4 = this.filterChunks(chunks, ChunkType.tRNS);
        TransparencyFilter transparencyFilter;
        if (!filterChunks4.isEmpty()) {
            transparencyFilter = this.getTransparencyFilter(pngChunkIhdr.pngColorType, filterChunks4.get(0));
        }
        else {
            transparencyFilter = null;
        }
        final List<PngChunk> filterChunks5 = this.filterChunks(chunks, ChunkType.sRGB);
        final List<PngChunk> filterChunks6 = this.filterChunks(chunks, ChunkType.gAMA);
        final List<PngChunk> filterChunks7 = this.filterChunks(chunks, ChunkType.iCCP);
        if (filterChunks5.size() > 1) {
            throw new ImageReadException("PNG: unexpected sRGB chunk");
        }
        if (filterChunks6.size() > 1) {
            throw new ImageReadException("PNG: unexpected gAMA chunk");
        }
        if (filterChunks7.size() > 1) {
            throw new ImageReadException("PNG: unexpected iCCP chunk");
        }
        GammaCorrection gammaCorrection = null;
        Label_0612: {
            if (filterChunks5.size() == 1) {
                if (this.getDebug()) {
                    System.out.println("sRGB, no color management neccesary.");
                }
            }
            else {
                if (filterChunks7.size() == 1) {
                    if (this.getDebug()) {
                        System.out.println("iCCP.");
                    }
                    instance = ICC_Profile.getInstance(((PngChunkIccp)filterChunks7.get(0)).getUncompressedProfile());
                    gammaCorrection = null;
                    break Label_0612;
                }
                if (filterChunks6.size() == 1) {
                    final double gamma = filterChunks6.get(0).getGamma();
                    GammaCorrection gammaCorrection2;
                    if (Math.abs(1.0 - gamma) >= 0.5) {
                        gammaCorrection2 = new GammaCorrection(gamma, 1.0);
                    }
                    else {
                        gammaCorrection2 = null;
                    }
                    if (gammaCorrection2 != null && pngChunkPlte != null) {
                        pngChunkPlte.correct(gammaCorrection2);
                    }
                    gammaCorrection = gammaCorrection2;
                    break Label_0612;
                }
            }
            gammaCorrection = null;
        }
        final int width = pngChunkIhdr.width;
        final int height = pngChunkIhdr.height;
        final PngColorType pngColorType = pngChunkIhdr.pngColorType;
        final int bitDepth = pngChunkIhdr.bitDepth;
        if (pngChunkIhdr.filterMethod != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PNG: unknown FilterMethod: ");
            sb.append(pngChunkIhdr.filterMethod);
            throw new ImageReadException(sb.toString());
        }
        final int n = bitDepth * pngColorType.getSamplesPerPixel();
        if (pngColorType.hasAlpha() || transparencyFilter != null) {
            b = true;
        }
        BufferedImage bufferedImage;
        if (pngColorType.isGreyscale()) {
            bufferedImage = this.getBufferedImageFactory(hashMap).getGrayscaleBufferedImage(width, height, b);
        }
        else {
            bufferedImage = this.getBufferedImageFactory(hashMap).getColorBufferedImage(width, height, b);
        }
        final InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(byteArray));
        ScanExpediter scanExpediter = null;
        switch (PngImageParser$1.$SwitchMap$org$apache$commons$imaging$formats$png$InterlaceMethod[pngChunkIhdr.interlaceMethod.ordinal()]) {
            default: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown InterlaceMethod: ");
                sb2.append(pngChunkIhdr.interlaceMethod);
                throw new ImageReadException(sb2.toString());
            }
            case 2: {
                scanExpediter = new ScanExpediterInterlaced(width, height, inflaterInputStream, bufferedImage, pngColorType, bitDepth, n, pngChunkPlte, gammaCorrection, transparencyFilter);
                break;
            }
            case 1: {
                scanExpediter = new ScanExpediterSimple(width, height, inflaterInputStream, bufferedImage, pngColorType, bitDepth, n, pngChunkPlte, gammaCorrection, transparencyFilter);
                break;
            }
        }
        scanExpediter.drive();
        BufferedImage convertBetweenColorSpaces = bufferedImage;
        if (instance != null) {
            final Boolean value = new IccProfileParser().issRGB(instance);
            if (value != null) {
                convertBetweenColorSpaces = bufferedImage;
                if (value) {
                    return convertBetweenColorSpaces;
                }
            }
            convertBetweenColorSpaces = new ColorTools().convertBetweenColorSpaces(bufferedImage, new ICC_ColorSpace(instance), ColorModel.getRGBdefault().getColorSpace());
        }
        return convertBetweenColorSpaces;
    }
    
    public List<String> getChuckTypes(final InputStream inputStream) throws ImageReadException, IOException {
        final List<PngChunk> chunks = this.readChunks(inputStream, null, false);
        final ArrayList list = new ArrayList();
        final Iterator<PngChunk> iterator = chunks.iterator();
        while (iterator.hasNext()) {
            list.add(getChunkTypeName(iterator.next().chunkType));
        }
        return list;
    }
    
    @Override
    public String getDefaultExtension() {
        return ".png";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<PngChunk> chunks = this.readChunks(byteSource, new ChunkType[] { ChunkType.iCCP }, true);
        if (chunks == null || chunks.isEmpty()) {
            return null;
        }
        if (chunks.size() > 1) {
            throw new ImageReadException("PNG contains more than one ICC Profile ");
        }
        return ((PngChunkIccp)chunks.get(0)).getUncompressedProfile();
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<PngChunk> chunks = this.readChunks(byteSource, new ChunkType[] { ChunkType.IHDR, ChunkType.pHYs, ChunkType.tEXt, ChunkType.zTXt, ChunkType.tRNS, ChunkType.PLTE, ChunkType.iTXt }, false);
        if (chunks == null || chunks.isEmpty()) {
            throw new ImageReadException("PNG: no chunks");
        }
        final List<PngChunk> filterChunks = this.filterChunks(chunks, ChunkType.IHDR);
        if (filterChunks.size() == 0) {
            throw new ImageReadException("PNG contains more than one Header");
        }
        final PngChunkIhdr pngChunkIhdr = filterChunks.get(0);
        final boolean b = !this.filterChunks(chunks, ChunkType.tRNS).isEmpty() || pngChunkIhdr.pngColorType.hasAlpha();
        PngChunkPhys pngChunkPhys = null;
        final List<PngChunk> filterChunks2 = this.filterChunks(chunks, ChunkType.pHYs);
        if (filterChunks2.size() > 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("PNG contains more than one pHYs: ");
            sb.append(filterChunks2.size());
            throw new ImageReadException(sb.toString());
        }
        if (filterChunks2.size() == 1) {
            pngChunkPhys = (PngChunkPhys)filterChunks2.get(0);
        }
        final List<PngChunk> filterChunks3 = this.filterChunks(chunks, ChunkType.tEXt);
        final List<PngChunk> filterChunks4 = this.filterChunks(chunks, ChunkType.zTXt);
        final List<PngChunk> filterChunks5 = this.filterChunks(chunks, ChunkType.iTXt);
        final ArrayList list = new ArrayList();
        final ArrayList<PngText> list2 = new ArrayList<PngText>();
        for (final PngChunkText pngChunkText : filterChunks3) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(pngChunkText.keyword);
            sb2.append(": ");
            sb2.append(pngChunkText.text);
            list.add(sb2.toString());
            list2.add(pngChunkText.getContents());
        }
        for (final PngChunkZtxt pngChunkZtxt : filterChunks4) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(pngChunkZtxt.keyword);
            sb3.append(": ");
            sb3.append(pngChunkZtxt.text);
            list.add(sb3.toString());
            list2.add(pngChunkZtxt.getContents());
        }
        for (final PngChunkItxt pngChunkItxt : filterChunks5) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(pngChunkItxt.keyword);
            sb4.append(": ");
            sb4.append(pngChunkItxt.text);
            list.add(sb4.toString());
            list2.add(pngChunkItxt.getContents());
        }
        final int bitDepth = pngChunkIhdr.bitDepth;
        final int samplesPerPixel = pngChunkIhdr.pngColorType.getSamplesPerPixel();
        final ImageFormats png = ImageFormats.PNG;
        final int height = pngChunkIhdr.height;
        final int width = pngChunkIhdr.width;
        final boolean progressive = pngChunkIhdr.interlaceMethod.isProgressive();
        int n;
        float n2;
        int n3;
        float n4;
        if (pngChunkPhys != null && pngChunkPhys.unitSpecifier == 1) {
            n = (int)Math.round(pngChunkPhys.pixelsPerUnitXAxis * 0.0254);
            n2 = (float)(width / (pngChunkPhys.pixelsPerUnitXAxis * 0.0254));
            n3 = (int)Math.round(pngChunkPhys.pixelsPerUnitYAxis * 0.0254);
            n4 = (float)(height / (pngChunkPhys.pixelsPerUnitYAxis * 0.0254));
        }
        else {
            n4 = -1.0f;
            n2 = -1.0f;
            n = -1;
            n3 = -1;
        }
        final boolean b2 = this.filterChunks(chunks, ChunkType.PLTE).size() > 1;
        ImageInfo.ColorType colorType = null;
        switch (PngImageParser$1.$SwitchMap$org$apache$commons$imaging$formats$png$PngColorType[pngChunkIhdr.pngColorType.ordinal()]) {
            default: {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Png: Unknown ColorType: ");
                sb5.append(pngChunkIhdr.pngColorType);
                throw new ImageReadException(sb5.toString());
            }
            case 2:
            case 3:
            case 5: {
                colorType = ImageInfo.ColorType.RGB;
                break;
            }
            case 1:
            case 4: {
                colorType = ImageInfo.ColorType.GRAYSCALE;
                break;
            }
        }
        return new PngImageInfo("Png", bitDepth * samplesPerPixel, list, png, "PNG Portable Network Graphics", height, "image/png", 1, n3, n4, n, n2, width, progressive, b, b2, colorType, ImageInfo.CompressionAlgorithm.PNG_FILTER, list2);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<PngChunk> chunks = this.readChunks(byteSource, new ChunkType[] { ChunkType.IHDR }, true);
        if (chunks == null || chunks.isEmpty()) {
            throw new ImageReadException("Png: No chunks");
        }
        if (chunks.size() > 1) {
            throw new ImageReadException("PNG contains more than one Header");
        }
        final PngChunkIhdr pngChunkIhdr = chunks.get(0);
        return new Dimension(pngChunkIhdr.width, pngChunkIhdr.height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<PngChunk> chunks = this.readChunks(byteSource, new ChunkType[] { ChunkType.tEXt, ChunkType.zTXt }, true);
        if (chunks != null && !chunks.isEmpty()) {
            final GenericImageMetadata genericImageMetadata = new GenericImageMetadata();
            for (final PngTextChunk pngTextChunk : chunks) {
                genericImageMetadata.add(pngTextChunk.getKeyword(), pngTextChunk.getText());
            }
            return genericImageMetadata;
        }
        return null;
    }
    
    @Override
    public String getName() {
        return "Png-Custom";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<PngChunk> chunks = this.readChunks(byteSource, new ChunkType[] { ChunkType.iTXt }, false);
        if (chunks == null || chunks.isEmpty()) {
            return null;
        }
        final ArrayList list = new ArrayList();
        for (final PngChunkItxt pngChunkItxt : chunks) {
            if (!pngChunkItxt.getKeyword().equals("XML:com.adobe.xmp")) {
                continue;
            }
            list.add(pngChunkItxt);
        }
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new ImageReadException("PNG contains more than one XMP chunk.");
        }
        return ((PngChunkItxt)list.get(0)).getText();
    }
    
    public boolean hasChuckType(final ByteSource byteSource, final ChunkType chunkType) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                this.readSignature(inputStream);
                final List<PngChunk> chunks = this.readChunks(inputStream, new ChunkType[] { chunkType }, true);
                try {
                    final boolean empty = chunks.isEmpty();
                    IoUtils.closeQuietly(true, inputStream);
                    return empty ^ true;
                }
                finally {
                    final boolean empty = true;
                }
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        final boolean empty = false;
        IoUtils.closeQuietly(empty, closeable);
    }
    
    public void readSignature(final InputStream inputStream) throws ImageReadException, IOException {
        BinaryFunctions.readAndVerifyBytes(inputStream, PngConstants.PNG_SIGNATURE, "Not a Valid PNG Segment: Incorrect Signature");
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> map) throws ImageWriteException, IOException {
        new PngWriter(map).writeImage(bufferedImage, outputStream, map);
    }
}
