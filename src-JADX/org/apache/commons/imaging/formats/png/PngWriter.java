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

    /* JADX WARN: Removed duplicated region for block: B:104:0x0279  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0302  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01bf A[PHI: r1
  0x01bf: PHI (r1v16 org.apache.commons.imaging.palette.Palette) = (r1v15 org.apache.commons.imaging.palette.Palette), (r1v45 org.apache.commons.imaging.palette.Palette) binds: [B:62:0x0192, B:69:0x01bc] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01ca  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0218  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeImage(java.awt.image.BufferedImage r23, java.io.OutputStream r24, java.util.Map<java.lang.String, java.lang.Object> r25) throws org.apache.commons.imaging.ImageWriteException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 823
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.png.PngWriter.writeImage(java.awt.image.BufferedImage, java.io.OutputStream, java.util.Map):void");
    }
}
