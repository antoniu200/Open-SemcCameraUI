package org.apache.commons.imaging.formats.png;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.png.chunks.PngChunk;
import org.apache.commons.imaging.formats.png.chunks.PngChunkGama;
import org.apache.commons.imaging.formats.png.chunks.PngChunkIccp;
import org.apache.commons.imaging.formats.png.chunks.PngChunkIdat;
import org.apache.commons.imaging.formats.png.chunks.PngChunkIhdr;
import org.apache.commons.imaging.formats.png.chunks.PngChunkItxt;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPhys;
import org.apache.commons.imaging.formats.png.chunks.PngChunkPlte;
import org.apache.commons.imaging.formats.png.chunks.PngChunkText;
import org.apache.commons.imaging.formats.png.chunks.PngChunkZtxt;
import org.apache.commons.imaging.formats.png.chunks.PngTextChunk;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilter;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilterGrayscale;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilterIndexedColor;
import org.apache.commons.imaging.formats.png.transparencyfilters.TransparencyFilterTrueColor;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PngImageParser extends ImageParser {
    private static final String DEFAULT_EXTENSION = ".png";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};

    @Override // org.apache.commons.imaging.ImageParser
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "Png-Custom";
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.PNG};
    }

    public static String getChunkTypeName(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append((char) ((i >> 24) & 255));
        sb.append((char) ((i >> 16) & 255));
        sb.append((char) ((i >> 8) & 255));
        sb.append((char) ((i >> 0) & 255));
        return sb.toString();
    }

    public List<String> getChuckTypes(InputStream inputStream) throws IOException, ImageReadException {
        List<PngChunk> chunks = readChunks(inputStream, (ChunkType[]) null, false);
        ArrayList arrayList = new ArrayList();
        Iterator<PngChunk> it = chunks.iterator();
        while (it.hasNext()) {
            arrayList.add(getChunkTypeName(it.next().chunkType));
        }
        return arrayList;
    }

    public boolean hasChuckType(ByteSource byteSource, ChunkType chunkType) throws Throwable {
        InputStream inputStream;
        InputStream inputStream2;
        boolean z;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            readSignature(inputStream);
            try {
                boolean z2 = !readChunks(inputStream, new ChunkType[]{chunkType}, true).isEmpty();
                IoUtils.closeQuietly(true, inputStream);
                return z2;
            } catch (Throwable th2) {
                th = th2;
                inputStream2 = inputStream;
                z = true;
                IoUtils.closeQuietly(z, inputStream2);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            inputStream2 = inputStream;
            z = false;
            IoUtils.closeQuietly(z, inputStream2);
            throw th;
        }
    }

    private boolean keepChunk(int i, ChunkType[] chunkTypeArr) {
        if (chunkTypeArr == null) {
            return true;
        }
        for (ChunkType chunkType : chunkTypeArr) {
            if (chunkType.value == i) {
                return true;
            }
        }
        return false;
    }

    private List<PngChunk> readChunks(InputStream inputStream, ChunkType[] chunkTypeArr, boolean z) throws IOException, ImageReadException {
        int i;
        ArrayList arrayList = new ArrayList();
        do {
            if (getDebug()) {
                System.out.println("");
            }
            int i2 = BinaryFunctions.read4Bytes("Length", inputStream, "Not a Valid PNG File", getByteOrder());
            i = BinaryFunctions.read4Bytes("ChunkType", inputStream, "Not a Valid PNG File", getByteOrder());
            if (getDebug()) {
                BinaryFunctions.printCharQuad("ChunkType", i);
                debugNumber("Length", i2, 4);
            }
            boolean zKeepChunk = keepChunk(i, chunkTypeArr);
            byte[] bytes = null;
            if (zKeepChunk) {
                bytes = BinaryFunctions.readBytes("Chunk Data", inputStream, i2, "Not a Valid PNG File: Couldn't read Chunk Data.");
            } else {
                BinaryFunctions.skipBytes(inputStream, i2, "Not a Valid PNG File");
            }
            if (getDebug() && bytes != null) {
                debugNumber("bytes", bytes.length, 4);
            }
            int i3 = BinaryFunctions.read4Bytes("CRC", inputStream, "Not a Valid PNG File", getByteOrder());
            if (zKeepChunk) {
                if (i == ChunkType.iCCP.value) {
                    arrayList.add(new PngChunkIccp(i2, i, i3, bytes));
                } else if (i == ChunkType.tEXt.value) {
                    arrayList.add(new PngChunkText(i2, i, i3, bytes));
                } else if (i == ChunkType.zTXt.value) {
                    arrayList.add(new PngChunkZtxt(i2, i, i3, bytes));
                } else if (i == ChunkType.IHDR.value) {
                    arrayList.add(new PngChunkIhdr(i2, i, i3, bytes));
                } else if (i == ChunkType.PLTE.value) {
                    arrayList.add(new PngChunkPlte(i2, i, i3, bytes));
                } else if (i == ChunkType.pHYs.value) {
                    arrayList.add(new PngChunkPhys(i2, i, i3, bytes));
                } else if (i == ChunkType.IDAT.value) {
                    arrayList.add(new PngChunkIdat(i2, i, i3, bytes));
                } else if (i == ChunkType.gAMA.value) {
                    arrayList.add(new PngChunkGama(i2, i, i3, bytes));
                } else if (i == ChunkType.iTXt.value) {
                    arrayList.add(new PngChunkItxt(i2, i, i3, bytes));
                } else {
                    arrayList.add(new PngChunk(i2, i, i3, bytes));
                }
                if (z) {
                    return arrayList;
                }
            }
        } while (i != ChunkType.IEND.value);
        return arrayList;
    }

    public void readSignature(InputStream inputStream) throws IOException, ImageReadException {
        BinaryFunctions.readAndVerifyBytes(inputStream, PngConstants.PNG_SIGNATURE, "Not a Valid PNG Segment: Incorrect Signature");
    }

    private List<PngChunk> readChunks(ByteSource byteSource, ChunkType[] chunkTypeArr, boolean z) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            readSignature(inputStream);
            List<PngChunk> chunks = readChunks(inputStream, chunkTypeArr, z);
            IoUtils.closeQuietly(true, inputStream);
            return chunks;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        List<PngChunk> chunks = readChunks(byteSource, new ChunkType[]{ChunkType.iCCP}, true);
        if (chunks == null || chunks.isEmpty()) {
            return null;
        }
        if (chunks.size() > 1) {
            throw new ImageReadException("PNG contains more than one ICC Profile ");
        }
        return ((PngChunkIccp) chunks.get(0)).getUncompressedProfile();
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        List<PngChunk> chunks = readChunks(byteSource, new ChunkType[]{ChunkType.IHDR}, true);
        if (chunks == null || chunks.isEmpty()) {
            throw new ImageReadException("Png: No chunks");
        }
        if (chunks.size() > 1) {
            throw new ImageReadException("PNG contains more than one Header");
        }
        PngChunkIhdr pngChunkIhdr = (PngChunkIhdr) chunks.get(0);
        return new Dimension(pngChunkIhdr.width, pngChunkIhdr.height);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        List<PngChunk> chunks = readChunks(byteSource, new ChunkType[]{ChunkType.tEXt, ChunkType.zTXt}, true);
        if (chunks == null || chunks.isEmpty()) {
            return null;
        }
        GenericImageMetadata genericImageMetadata = new GenericImageMetadata();
        Iterator<PngChunk> it = chunks.iterator();
        while (it.hasNext()) {
            PngTextChunk pngTextChunk = (PngTextChunk) it.next();
            genericImageMetadata.add(pngTextChunk.getKeyword(), pngTextChunk.getText());
        }
        return genericImageMetadata;
    }

    private List<PngChunk> filterChunks(List<PngChunk> list, ChunkType chunkType) {
        ArrayList arrayList = new ArrayList();
        for (PngChunk pngChunk : list) {
            if (pngChunk.chunkType == chunkType.value) {
                arrayList.add(pngChunk);
            }
        }
        return arrayList;
    }

    private TransparencyFilter getTransparencyFilter(PngColorType pngColorType, PngChunk pngChunk) throws IOException, ImageReadException {
        switch (pngColorType) {
            case GREYSCALE:
                return new TransparencyFilterGrayscale(pngChunk.getBytes());
            case TRUE_COLOR:
                return new TransparencyFilterTrueColor(pngChunk.getBytes());
            case INDEXED_COLOR:
                return new TransparencyFilterIndexedColor(pngChunk.getBytes());
            default:
                throw new ImageReadException("Simple Transparency not compatible with ColorType: " + pngColorType);
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        float f;
        float f2;
        int i;
        int iRound;
        ImageInfo.ColorType colorType;
        List<PngChunk> chunks = readChunks(byteSource, new ChunkType[]{ChunkType.IHDR, ChunkType.pHYs, ChunkType.tEXt, ChunkType.zTXt, ChunkType.tRNS, ChunkType.PLTE, ChunkType.iTXt}, false);
        if (chunks == null || chunks.isEmpty()) {
            throw new ImageReadException("PNG: no chunks");
        }
        List<PngChunk> listFilterChunks = filterChunks(chunks, ChunkType.IHDR);
        if (listFilterChunks.size() != 1) {
            throw new ImageReadException("PNG contains more than one Header");
        }
        PngChunkIhdr pngChunkIhdr = (PngChunkIhdr) listFilterChunks.get(0);
        boolean zHasAlpha = !filterChunks(chunks, ChunkType.tRNS).isEmpty() ? true : pngChunkIhdr.pngColorType.hasAlpha();
        List<PngChunk> listFilterChunks2 = filterChunks(chunks, ChunkType.pHYs);
        if (listFilterChunks2.size() > 1) {
            throw new ImageReadException("PNG contains more than one pHYs: " + listFilterChunks2.size());
        }
        PngChunkPhys pngChunkPhys = listFilterChunks2.size() == 1 ? (PngChunkPhys) listFilterChunks2.get(0) : null;
        List<PngChunk> listFilterChunks3 = filterChunks(chunks, ChunkType.tEXt);
        List<PngChunk> listFilterChunks4 = filterChunks(chunks, ChunkType.zTXt);
        List<PngChunk> listFilterChunks5 = filterChunks(chunks, ChunkType.iTXt);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Iterator<PngChunk> it = listFilterChunks3.iterator();
        while (it.hasNext()) {
            PngChunkText pngChunkText = (PngChunkText) it.next();
            arrayList.add(pngChunkText.keyword + ": " + pngChunkText.text);
            arrayList2.add(pngChunkText.getContents());
        }
        Iterator<PngChunk> it2 = listFilterChunks4.iterator();
        while (it2.hasNext()) {
            PngChunkZtxt pngChunkZtxt = (PngChunkZtxt) it2.next();
            arrayList.add(pngChunkZtxt.keyword + ": " + pngChunkZtxt.text);
            arrayList2.add(pngChunkZtxt.getContents());
        }
        Iterator<PngChunk> it3 = listFilterChunks5.iterator();
        while (it3.hasNext()) {
            PngChunkItxt pngChunkItxt = (PngChunkItxt) it3.next();
            arrayList.add(pngChunkItxt.keyword + ": " + pngChunkItxt.text);
            arrayList2.add(pngChunkItxt.getContents());
        }
        int samplesPerPixel = pngChunkIhdr.bitDepth * pngChunkIhdr.pngColorType.getSamplesPerPixel();
        ImageFormats imageFormats = ImageFormats.PNG;
        int i2 = pngChunkIhdr.height;
        int i3 = pngChunkIhdr.width;
        boolean zIsProgressive = pngChunkIhdr.interlaceMethod.isProgressive();
        if (pngChunkPhys == null || pngChunkPhys.unitSpecifier != 1) {
            f = -1.0f;
            f2 = -1.0f;
            i = -1;
            iRound = -1;
        } else {
            int iRound2 = (int) Math.round(pngChunkPhys.pixelsPerUnitXAxis * 0.0254d);
            iRound = (int) Math.round(pngChunkPhys.pixelsPerUnitYAxis * 0.0254d);
            f = (float) (i2 / (pngChunkPhys.pixelsPerUnitYAxis * 0.0254d));
            i = iRound2;
            f2 = (float) (i3 / (pngChunkPhys.pixelsPerUnitXAxis * 0.0254d));
        }
        boolean z = filterChunks(chunks, ChunkType.PLTE).size() > 1;
        switch (pngChunkIhdr.pngColorType) {
            case GREYSCALE:
            case GREYSCALE_WITH_ALPHA:
                colorType = ImageInfo.ColorType.GRAYSCALE;
                break;
            case TRUE_COLOR:
            case INDEXED_COLOR:
            case TRUE_COLOR_WITH_ALPHA:
                colorType = ImageInfo.ColorType.RGB;
                break;
            default:
                throw new ImageReadException("Png: Unknown ColorType: " + pngChunkIhdr.pngColorType);
        }
        return new PngImageInfo("Png", samplesPerPixel, arrayList, imageFormats, "PNG Portable Network Graphics", i2, "image/png", 1, iRound, f, i, f2, i3, zIsProgressive, zHasAlpha, z, colorType, ImageInfo.CompressionAlgorithm.PNG_FILTER, arrayList2);
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01b1  */
    @Override // org.apache.commons.imaging.ImageParser
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.awt.image.BufferedImage getBufferedImage(org.apache.commons.imaging.common.bytesource.ByteSource r21, java.util.Map<java.lang.String, java.lang.Object> r22) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 604
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.png.PngImageParser.getBufferedImage(org.apache.commons.imaging.common.bytesource.ByteSource, java.util.Map):java.awt.image.BufferedImage");
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws Throwable {
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        List<PngChunk> chunks = readChunks(byteSource, (ChunkType[]) null, false);
        List<PngChunk> listFilterChunks = filterChunks(chunks, ChunkType.IHDR);
        if (listFilterChunks.size() != 1) {
            if (getDebug()) {
                System.out.println("PNG contains more than one Header");
            }
            return false;
        }
        printWriter.println("Color: " + ((PngChunkIhdr) listFilterChunks.get(0)).pngColorType.name());
        printWriter.println("chunks: " + chunks.size());
        if (chunks.isEmpty()) {
            return false;
        }
        for (int i = 0; i < chunks.size(); i++) {
            BinaryFunctions.printCharQuad(printWriter, "\t" + i + ": ", chunks.get(i).chunkType);
        }
        printWriter.println("");
        printWriter.flush();
        return true;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        new PngWriter(map).writeImage(bufferedImage, outputStream, map);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        List<PngChunk> chunks = readChunks(byteSource, new ChunkType[]{ChunkType.iTXt}, false);
        if (chunks == null || chunks.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<PngChunk> it = chunks.iterator();
        while (it.hasNext()) {
            PngChunkItxt pngChunkItxt = (PngChunkItxt) it.next();
            if (pngChunkItxt.getKeyword().equals(PngConstants.XMP_KEYWORD)) {
                arrayList.add(pngChunkItxt);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        if (arrayList.size() > 1) {
            throw new ImageReadException("PNG contains more than one XMP chunk.");
        }
        return ((PngChunkItxt) arrayList.get(0)).getText();
    }
}
