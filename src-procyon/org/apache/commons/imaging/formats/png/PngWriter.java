// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.PixelDensity;
import org.apache.commons.imaging.palette.SimplePalette;
import org.apache.commons.imaging.util.Debug;
import org.apache.commons.imaging.palette.PaletteFactory;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.palette.Palette;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

class PngWriter
{
    private final boolean verbose;
    
    public PngWriter(final Map<String, Object> map) {
        this.verbose = (map != null && Boolean.TRUE.equals(map.get("VERBOSE")));
    }
    
    public PngWriter(final boolean verbose) {
        this.verbose = verbose;
    }
    
    private byte[] deflate(final byte[] b) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out);
        try {
            deflaterOutputStream.write(b);
            IoUtils.closeQuietly(true, deflaterOutputStream);
            return out.toByteArray();
        }
        finally {
            IoUtils.closeQuietly(false, deflaterOutputStream);
        }
    }
    
    private byte getBitDepth(final PngColorType pngColorType, final Map<String, Object> map) {
        final Number value = map.get("PNG_BIT_DEPTH");
        byte byteValue;
        if (value instanceof Number) {
            byteValue = value.byteValue();
        }
        else {
            byteValue = 8;
        }
        if (!pngColorType.isBitDepthAllowed(byteValue)) {
            byteValue = 8;
        }
        return byteValue;
    }
    
    private boolean isValidISO_8859_1(final String s) {
        try {
            return s.equals(new String(s.getBytes("ISO-8859-1"), "ISO-8859-1"));
        }
        catch (final UnsupportedEncodingException cause) {
            throw new RuntimeException("Error parsing string.", cause);
        }
    }
    
    private void writeChunk(final OutputStream outputStream, final ChunkType chunkType, final byte[] b) throws IOException {
        int length;
        if (b == null) {
            length = 0;
        }
        else {
            length = b.length;
        }
        this.writeInt(outputStream, length);
        outputStream.write(chunkType.array);
        if (b != null) {
            outputStream.write(b);
        }
        final PngCrc pngCrc = new PngCrc();
        long n = pngCrc.start_partial_crc(chunkType.array, chunkType.array.length);
        if (b != null) {
            n = pngCrc.continue_partial_crc(n, b, b.length);
        }
        this.writeInt(outputStream, (int)pngCrc.finish_partial_crc(n));
    }
    
    private void writeChunkIDAT(final OutputStream outputStream, final byte[] array) throws IOException {
        this.writeChunk(outputStream, ChunkType.IDAT, array);
    }
    
    private void writeChunkIEND(final OutputStream outputStream) throws IOException {
        this.writeChunk(outputStream, ChunkType.IEND, null);
    }
    
    private void writeChunkIHDR(final OutputStream outputStream, final ImageHeader imageHeader) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.writeInt(byteArrayOutputStream, imageHeader.width);
        this.writeInt(byteArrayOutputStream, imageHeader.height);
        byteArrayOutputStream.write(imageHeader.bitDepth & 0xFF);
        byteArrayOutputStream.write(imageHeader.pngColorType.getValue() & 0xFF);
        byteArrayOutputStream.write(imageHeader.compressionMethod & 0xFF);
        byteArrayOutputStream.write(imageHeader.filterMethod & 0xFF);
        byteArrayOutputStream.write(imageHeader.interlaceMethod.ordinal() & 0xFF);
        this.writeChunk(outputStream, ChunkType.IHDR, byteArrayOutputStream.toByteArray());
    }
    
    private void writeChunkPHYS(final OutputStream outputStream, final int n, final int n2, final byte b) throws IOException {
        this.writeChunk(outputStream, ChunkType.pHYs, new byte[] { (byte)(n >> 24 & 0xFF), (byte)(n >> 16 & 0xFF), (byte)(n >> 8 & 0xFF), (byte)(n >> 0 & 0xFF), (byte)(n2 >> 24 & 0xFF), (byte)(n2 >> 16 & 0xFF), (byte)(n2 >> 8 & 0xFF), (byte)(n2 >> 0 & 0xFF), b });
    }
    
    private void writeChunkPLTE(final OutputStream outputStream, final Palette palette) throws IOException {
        final int length = palette.length();
        final byte[] array = new byte[length * 3];
        for (int i = 0; i < length; ++i) {
            final int entry = palette.getEntry(i);
            final int n = i * 3;
            array[n + 0] = (byte)(entry >> 16 & 0xFF);
            array[n + 1] = (byte)(entry >> 8 & 0xFF);
            array[n + 2] = (byte)(entry >> 0 & 0xFF);
        }
        this.writeChunk(outputStream, ChunkType.PLTE, array);
    }
    
    private void writeChunkTRNS(final OutputStream outputStream, final Palette palette) throws IOException {
        final byte[] array = new byte[palette.length()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = (byte)(0xFF & palette.getEntry(i) >> 24);
        }
        this.writeChunk(outputStream, ChunkType.tRNS, array);
    }
    
    private void writeChunkXmpiTXt(final OutputStream outputStream, final String s) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("XML:com.adobe.xmp".getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write("XML:com.adobe.xmp".getBytes("utf-8"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(this.deflate(s.getBytes("utf-8")));
        this.writeChunk(outputStream, ChunkType.iTXt, byteArrayOutputStream.toByteArray());
    }
    
    private void writeChunkiTXt(final OutputStream outputStream, final PngText.Itxt itxt) throws IOException, ImageWriteException {
        if (!this.isValidISO_8859_1(itxt.keyword)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Png tEXt chunk keyword is not ISO-8859-1: ");
            sb.append(itxt.keyword);
            throw new ImageWriteException(sb.toString());
        }
        if (!this.isValidISO_8859_1(itxt.languageTag)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Png tEXt chunk language tag is not ISO-8859-1: ");
            sb2.append(itxt.languageTag);
            throw new ImageWriteException(sb2.toString());
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(itxt.keyword.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(itxt.languageTag.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(itxt.translatedKeyword.getBytes("utf-8"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(this.deflate(itxt.text.getBytes("utf-8")));
        this.writeChunk(outputStream, ChunkType.iTXt, byteArrayOutputStream.toByteArray());
    }
    
    private void writeChunktEXt(final OutputStream outputStream, final PngText.Text text) throws IOException, ImageWriteException {
        if (!this.isValidISO_8859_1(text.keyword)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Png tEXt chunk keyword is not ISO-8859-1: ");
            sb.append(text.keyword);
            throw new ImageWriteException(sb.toString());
        }
        if (!this.isValidISO_8859_1(text.text)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Png tEXt chunk text is not ISO-8859-1: ");
            sb2.append(text.text);
            throw new ImageWriteException(sb2.toString());
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(text.keyword.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(text.text.getBytes("ISO-8859-1"));
        this.writeChunk(outputStream, ChunkType.tEXt, byteArrayOutputStream.toByteArray());
    }
    
    private void writeChunkzTXt(final OutputStream outputStream, final PngText.Ztxt ztxt) throws IOException, ImageWriteException {
        if (!this.isValidISO_8859_1(ztxt.keyword)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Png zTXt chunk keyword is not ISO-8859-1: ");
            sb.append(ztxt.keyword);
            throw new ImageWriteException(sb.toString());
        }
        if (!this.isValidISO_8859_1(ztxt.text)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Png zTXt chunk text is not ISO-8859-1: ");
            sb2.append(ztxt.text);
            throw new ImageWriteException(sb2.toString());
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(ztxt.keyword.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(this.deflate(ztxt.text.getBytes("ISO-8859-1")));
        this.writeChunk(outputStream, ChunkType.zTXt, byteArrayOutputStream.toByteArray());
    }
    
    private void writeInt(final OutputStream outputStream, final int n) throws IOException {
        outputStream.write(n >> 24 & 0xFF);
        outputStream.write(n >> 16 & 0xFF);
        outputStream.write(n >> 8 & 0xFF);
        outputStream.write(n >> 0 & 0xFF);
    }
    
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> m) throws ImageWriteException, IOException {
        final HashMap i = new HashMap((Map<? extends K, ? extends V>)m);
        if (i.containsKey("FORMAT")) {
            i.remove("FORMAT");
        }
        if (i.containsKey("VERBOSE")) {
            i.remove("VERBOSE");
        }
        final HashMap hashMap = new HashMap(i);
        if (i.containsKey("PNG_FORCE_TRUE_COLOR")) {
            i.remove("PNG_FORCE_TRUE_COLOR");
        }
        if (i.containsKey("PNG_FORCE_INDEXED_COLOR")) {
            i.remove("PNG_FORCE_INDEXED_COLOR");
        }
        if (i.containsKey("PNG_BIT_DEPTH")) {
            i.remove("PNG_BIT_DEPTH");
        }
        if (i.containsKey("XMP_XML")) {
            i.remove("XMP_XML");
        }
        if (i.containsKey("PNG_TEXT_CHUNKS")) {
            i.remove("PNG_TEXT_CHUNKS");
        }
        i.remove("PIXEL_DENSITY");
        if (!i.isEmpty()) {
            final Object next = i.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageWriteException(sb.toString());
        }
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final boolean hasTransparency = new PaletteFactory().hasTransparency(bufferedImage);
        if (this.verbose) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("hasAlpha: ");
            sb2.append(hasTransparency);
            Debug.debug(sb2.toString());
        }
        boolean grayscale = new PaletteFactory().isGrayscale(bufferedImage);
        if (this.verbose) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("isGrayscale: ");
            sb3.append(grayscale);
            Debug.debug(sb3.toString());
        }
        final boolean equals = Boolean.TRUE.equals(hashMap.get("PNG_FORCE_INDEXED_COLOR"));
        final boolean equals2 = Boolean.TRUE.equals(hashMap.get("PNG_FORCE_TRUE_COLOR"));
        if (equals && equals2) {
            throw new ImageWriteException("Params: Cannot force both indexed and true color modes");
        }
        final int n = 0;
        PngColorType obj = null;
        Label_0467: {
            PngColorType pngColorType;
            if (equals) {
                pngColorType = PngColorType.INDEXED_COLOR;
            }
            else {
                if (equals2) {
                    PngColorType pngColorType2;
                    if (hasTransparency) {
                        pngColorType2 = PngColorType.TRUE_COLOR_WITH_ALPHA;
                    }
                    else {
                        pngColorType2 = PngColorType.TRUE_COLOR;
                    }
                    grayscale = false;
                    obj = pngColorType2;
                    break Label_0467;
                }
                pngColorType = PngColorType.getColorType(hasTransparency, grayscale);
            }
            obj = pngColorType;
        }
        if (this.verbose) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("colorType: ");
            sb4.append(obj);
            Debug.debug(sb4.toString());
        }
        final byte bitDepth = this.getBitDepth(obj, (Map<String, Object>)hashMap);
        if (this.verbose) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("bitDepth: ");
            sb5.append(bitDepth);
            Debug.debug(sb5.toString());
        }
        int j;
        if (obj == PngColorType.INDEXED_COLOR) {
            j = 8;
        }
        else {
            j = bitDepth;
        }
        if (this.verbose) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("sampleDepth: ");
            sb6.append(j);
            Debug.debug(sb6.toString());
        }
        PngConstants.PNG_SIGNATURE.writeTo(outputStream);
        this.writeChunkIHDR(outputStream, new ImageHeader(width, height, bitDepth, obj, (byte)0, (byte)0, InterlaceMethod.NONE));
        Palette quantizedRgbPalette = null;
        final PngColorType indexed_COLOR = PngColorType.INDEXED_COLOR;
        final boolean b = true;
        if (obj == indexed_COLOR) {
            int n2;
            if (hasTransparency) {
                n2 = 255;
            }
            else {
                n2 = 256;
            }
            quantizedRgbPalette = new PaletteFactory().makeQuantizedRgbPalette(bufferedImage, n2);
            if (hasTransparency) {
                quantizedRgbPalette = new TransparentPalette(quantizedRgbPalette);
                this.writeChunkPLTE(outputStream, quantizedRgbPalette);
                this.writeChunkTRNS(outputStream, new SimplePalette(new int[] { 0 }));
            }
            else {
                this.writeChunkPLTE(outputStream, quantizedRgbPalette);
            }
        }
        final Object value = hashMap.get("PIXEL_DENSITY");
        if (value instanceof PixelDensity) {
            final PixelDensity pixelDensity = (PixelDensity)value;
            if (pixelDensity.isUnitless()) {
                this.writeChunkPHYS(outputStream, (int)Math.round(pixelDensity.getRawHorizontalDensity()), (int)Math.round(pixelDensity.getRawVerticalDensity()), (byte)0);
            }
            else {
                this.writeChunkPHYS(outputStream, (int)Math.round(pixelDensity.horizontalDensityMetres()), (int)Math.round(pixelDensity.verticalDensityMetres()), (byte)1);
            }
        }
        if (hashMap.containsKey("XMP_XML")) {
            this.writeChunkXmpiTXt(outputStream, (String)hashMap.get("XMP_XML"));
        }
        if (hashMap.containsKey("PNG_TEXT_CHUNKS")) {
            for (final PngText obj2 : (List)hashMap.get("PNG_TEXT_CHUNKS")) {
                if (obj2 instanceof PngText.Text) {
                    this.writeChunktEXt(outputStream, (PngText.Text)obj2);
                }
                else if (obj2 instanceof PngText.Ztxt) {
                    this.writeChunkzTXt(outputStream, (PngText.Ztxt)obj2);
                }
                else {
                    if (!(obj2 instanceof PngText.Itxt)) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("Unknown text to embed in PNG: ");
                        sb7.append(obj2);
                        throw new ImageWriteException(sb7.toString());
                    }
                    this.writeChunkiTXt(outputStream, (PngText.Itxt)obj2);
                }
            }
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n3 = b ? 1 : 0;
        if (obj != PngColorType.GREYSCALE_WITH_ALPHA) {
            if (obj == PngColorType.TRUE_COLOR_WITH_ALPHA) {
                n3 = (b ? 1 : 0);
            }
            else {
                n3 = 0;
            }
        }
        final int[] rgbArray = new int[width];
        final int n4 = 0;
        int k = n;
        for (int l = n4; l < height; ++l) {
            bufferedImage.getRGB(0, l, width, 1, rgbArray, 0, width);
            byteArrayOutputStream.write(FilterType.NONE.ordinal());
            for (int n5 = k; n5 < width; ++n5) {
                final int n6 = rgbArray[n5];
                if (quantizedRgbPalette != null) {
                    if (hasTransparency && n6 >>> 24 == 0) {
                        byteArrayOutputStream.write(k);
                    }
                    else {
                        byteArrayOutputStream.write(quantizedRgbPalette.getPaletteIndex(n6) & 0xFF);
                    }
                }
                else {
                    final int b2 = n6 >> 16 & 0xFF;
                    final int b3 = n6 >> 8 & 0xFF;
                    final int b4 = n6 >> 0 & 0xFF;
                    if (grayscale) {
                        byteArrayOutputStream.write((b2 + b3 + b4) / 3);
                    }
                    else {
                        byteArrayOutputStream.write(b2);
                        byteArrayOutputStream.write(b3);
                        byteArrayOutputStream.write(b4);
                    }
                    if (n3 != 0) {
                        byteArrayOutputStream.write(n6 >> 24 & 0xFF);
                    }
                }
            }
        }
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out);
        while (k < byteArray.length) {
            final int length = byteArray.length;
            final int b5 = 262144 + k;
            deflaterOutputStream.write(byteArray, k, Math.min(length, b5) - k);
            deflaterOutputStream.flush();
            out.flush();
            final byte[] byteArray2 = out.toByteArray();
            out.reset();
            if (byteArray2.length > 0) {
                this.writeChunkIDAT(outputStream, byteArray2);
            }
            k = b5;
        }
        deflaterOutputStream.finish();
        final byte[] byteArray3 = out.toByteArray();
        if (byteArray3.length > 0) {
            this.writeChunkIDAT(outputStream, byteArray3);
        }
        this.writeChunkIEND(outputStream);
        outputStream.close();
    }
    
    private static class ImageHeader
    {
        public final byte bitDepth;
        public final byte compressionMethod;
        public final byte filterMethod;
        public final int height;
        public final InterlaceMethod interlaceMethod;
        public final PngColorType pngColorType;
        public final int width;
        
        public ImageHeader(final int width, final int height, final byte bitDepth, final PngColorType pngColorType, final byte compressionMethod, final byte filterMethod, final InterlaceMethod interlaceMethod) {
            this.width = width;
            this.height = height;
            this.bitDepth = bitDepth;
            this.pngColorType = pngColorType;
            this.compressionMethod = compressionMethod;
            this.filterMethod = filterMethod;
            this.interlaceMethod = interlaceMethod;
        }
    }
    
    private static class TransparentPalette implements Palette
    {
        private final Palette palette;
        
        TransparentPalette(final Palette palette) {
            this.palette = palette;
        }
        
        @Override
        public int getEntry(final int n) {
            if (n == 0) {
                return 0;
            }
            return this.palette.getEntry(n - 1);
        }
        
        @Override
        public int getPaletteIndex(int paletteIndex) throws ImageWriteException {
            if (paletteIndex == 0) {
                return 0;
            }
            paletteIndex = this.palette.getPaletteIndex(paletteIndex);
            if (paletteIndex >= 0) {
                return 1 + paletteIndex;
            }
            return paletteIndex;
        }
        
        @Override
        public int length() {
            return 1 + this.palette.length();
        }
    }
}
