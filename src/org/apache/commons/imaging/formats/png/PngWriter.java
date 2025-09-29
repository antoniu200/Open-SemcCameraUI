package org.apache.commons.imaging.formats.png;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.formats.png.PngText;
import org.apache.commons.imaging.palette.Palette;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PngWriter {
    private final boolean verbose;

    public PngWriter(boolean z) {
        this.verbose = z;
    }

    public PngWriter(Map<String, Object> map) {
        this.verbose = map != null && Boolean.TRUE.equals(map.get(ImagingConstants.PARAM_KEY_VERBOSE));
    }

    private void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 24) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 0) & 255);
    }

    private void writeChunk(OutputStream outputStream, ChunkType chunkType, byte[] bArr) throws IOException {
        writeInt(outputStream, bArr == null ? 0 : bArr.length);
        outputStream.write(chunkType.array);
        if (bArr != null) {
            outputStream.write(bArr);
        }
        PngCrc pngCrc = new PngCrc();
        long jStart_partial_crc = pngCrc.start_partial_crc(chunkType.array, chunkType.array.length);
        if (bArr != null) {
            jStart_partial_crc = pngCrc.continue_partial_crc(jStart_partial_crc, bArr, bArr.length);
        }
        writeInt(outputStream, (int) pngCrc.finish_partial_crc(jStart_partial_crc));
    }

    private static class ImageHeader {
        public final byte bitDepth;
        public final byte compressionMethod;
        public final byte filterMethod;
        public final int height;
        public final InterlaceMethod interlaceMethod;
        public final PngColorType pngColorType;
        public final int width;

        public ImageHeader(int i, int i2, byte b, PngColorType pngColorType, byte b2, byte b3, InterlaceMethod interlaceMethod) {
            this.width = i;
            this.height = i2;
            this.bitDepth = b;
            this.pngColorType = pngColorType;
            this.compressionMethod = b2;
            this.filterMethod = b3;
            this.interlaceMethod = interlaceMethod;
        }
    }

    private void writeChunkIHDR(OutputStream outputStream, ImageHeader imageHeader) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeInt(byteArrayOutputStream, imageHeader.width);
        writeInt(byteArrayOutputStream, imageHeader.height);
        byteArrayOutputStream.write(imageHeader.bitDepth & 255);
        byteArrayOutputStream.write(imageHeader.pngColorType.getValue() & 255);
        byteArrayOutputStream.write(imageHeader.compressionMethod & 255);
        byteArrayOutputStream.write(imageHeader.filterMethod & 255);
        byteArrayOutputStream.write(imageHeader.interlaceMethod.ordinal() & 255);
        writeChunk(outputStream, ChunkType.IHDR, byteArrayOutputStream.toByteArray());
    }

    private void writeChunkiTXt(OutputStream outputStream, PngText.Itxt itxt) throws ImageWriteException, IOException {
        if (!isValidISO_8859_1(itxt.keyword)) {
            throw new ImageWriteException("Png tEXt chunk keyword is not ISO-8859-1: " + itxt.keyword);
        }
        if (!isValidISO_8859_1(itxt.languageTag)) {
            throw new ImageWriteException("Png tEXt chunk language tag is not ISO-8859-1: " + itxt.languageTag);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(itxt.keyword.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(itxt.languageTag.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(itxt.translatedKeyword.getBytes("utf-8"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(deflate(itxt.text.getBytes("utf-8")));
        writeChunk(outputStream, ChunkType.iTXt, byteArrayOutputStream.toByteArray());
    }

    private void writeChunkzTXt(OutputStream outputStream, PngText.Ztxt ztxt) throws ImageWriteException, IOException {
        if (!isValidISO_8859_1(ztxt.keyword)) {
            throw new ImageWriteException("Png zTXt chunk keyword is not ISO-8859-1: " + ztxt.keyword);
        }
        if (!isValidISO_8859_1(ztxt.text)) {
            throw new ImageWriteException("Png zTXt chunk text is not ISO-8859-1: " + ztxt.text);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(ztxt.keyword.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(deflate(ztxt.text.getBytes("ISO-8859-1")));
        writeChunk(outputStream, ChunkType.zTXt, byteArrayOutputStream.toByteArray());
    }

    private void writeChunktEXt(OutputStream outputStream, PngText.Text text) throws ImageWriteException, IOException {
        if (!isValidISO_8859_1(text.keyword)) {
            throw new ImageWriteException("Png tEXt chunk keyword is not ISO-8859-1: " + text.keyword);
        }
        if (!isValidISO_8859_1(text.text)) {
            throw new ImageWriteException("Png tEXt chunk text is not ISO-8859-1: " + text.text);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(text.keyword.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(text.text.getBytes("ISO-8859-1"));
        writeChunk(outputStream, ChunkType.tEXt, byteArrayOutputStream.toByteArray());
    }

    private byte[] deflate(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        try {
            deflaterOutputStream.write(bArr);
            IoUtils.closeQuietly(true, deflaterOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable th) {
            IoUtils.closeQuietly(false, deflaterOutputStream);
            throw th;
        }
    }

    private boolean isValidISO_8859_1(String str) {
        try {
            return str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing string.", e);
        }
    }

    private void writeChunkXmpiTXt(OutputStream outputStream, String str) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(PngConstants.XMP_KEYWORD.getBytes("ISO-8859-1"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(PngConstants.XMP_KEYWORD.getBytes("utf-8"));
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(deflate(str.getBytes("utf-8")));
        writeChunk(outputStream, ChunkType.iTXt, byteArrayOutputStream.toByteArray());
    }

    private void writeChunkPLTE(OutputStream outputStream, Palette palette) throws IOException {
        int length = palette.length();
        byte[] bArr = new byte[length * 3];
        for (int i = 0; i < length; i++) {
            int entry = palette.getEntry(i);
            int i2 = i * 3;
            bArr[i2 + 0] = (byte) ((entry >> 16) & 255);
            bArr[i2 + 1] = (byte) ((entry >> 8) & 255);
            bArr[i2 + 2] = (byte) ((entry >> 0) & 255);
        }
        writeChunk(outputStream, ChunkType.PLTE, bArr);
    }

    private void writeChunkTRNS(OutputStream outputStream, Palette palette) throws IOException {
        byte[] bArr = new byte[palette.length()];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (255 & (palette.getEntry(i) >> 24));
        }
        writeChunk(outputStream, ChunkType.tRNS, bArr);
    }

    private void writeChunkIEND(OutputStream outputStream) throws IOException {
        writeChunk(outputStream, ChunkType.IEND, null);
    }

    private void writeChunkIDAT(OutputStream outputStream, byte[] bArr) throws IOException {
        writeChunk(outputStream, ChunkType.IDAT, bArr);
    }

    private void writeChunkPHYS(OutputStream outputStream, int i, int i2, byte b) throws IOException {
        writeChunk(outputStream, ChunkType.pHYs, new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 0) & 255), (byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) ((i2 >> 0) & 255), b});
    }

    private byte getBitDepth(PngColorType pngColorType, Map<String, Object> map) {
        Object obj = map.get(PngConstants.PARAM_KEY_PNG_BIT_DEPTH);
        byte bByteValue = obj instanceof Number ? ((Number) obj).byteValue() : (byte) 8;
        if (pngColorType.isBitDepthAllowed(bByteValue)) {
            return bByteValue;
        }
        return (byte) 8;
    }

    private static class TransparentPalette implements Palette {
        private final Palette palette;

        TransparentPalette(Palette palette) {
            this.palette = palette;
        }

        @Override // org.apache.commons.imaging.palette.Palette
        public int getEntry(int i) {
            if (i == 0) {
                return 0;
            }
            return this.palette.getEntry(i - 1);
        }

        @Override // org.apache.commons.imaging.palette.Palette
        public int length() {
            return 1 + this.palette.length();
        }

        @Override // org.apache.commons.imaging.palette.Palette
        public int getPaletteIndex(int i) throws ImageWriteException {
            if (i == 0) {
                return 0;
            }
            int paletteIndex = this.palette.getPaletteIndex(i);
            return paletteIndex >= 0 ? 1 + paletteIndex : paletteIndex;
        }
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
}
