package org.apache.commons.imaging.formats.bmp;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.PixelDensity;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.bmp.BmpHeaderInfo;
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.palette.SimplePalette;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BmpImageParser extends ImageParser {
    private static final int BITMAP_FILE_HEADER_SIZE = 14;
    private static final int BITMAP_INFO_HEADER_SIZE = 40;
    private static final int BI_BITFIELDS = 3;
    private static final int BI_RGB = 0;
    private static final int BI_RLE4 = 2;
    private static final int BI_RLE8 = 1;
    private static final String DEFAULT_EXTENSION = ".bmp";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};
    private static final byte[] BMP_HEADER_SIGNATURE = {66, 77};

    private String getBmpTypeDescription(int i, int i2) {
        return (i == 66 && i2 == 77) ? "Windows 3.1x, 95, NT," : (i == 66 && i2 == 65) ? "OS/2 Bitmap Array" : (i == 67 && i2 == 73) ? "OS/2 Color Icon" : (i == 67 && i2 == 80) ? "OS/2 Color Pointer" : (i == 73 && i2 == 67) ? "OS/2 Icon" : (i == 80 && i2 == 84) ? "OS/2 Pointer" : "Unknown";
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
        return "Bmp-Custom";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    public BmpImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.BMP};
    }

    private BmpHeaderInfo readBmpHeaderInfo(InputStream inputStream, FormatCompliance formatCompliance, boolean z) throws IOException, ImageReadException {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        BmpHeaderInfo.ColorSpace colorSpace;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        int i28;
        int i29;
        int i30;
        int i31;
        int i32;
        int i33;
        int i34;
        int i35;
        int i36;
        int i37;
        int i38;
        int i39;
        int i40;
        int i41;
        byte b = BinaryFunctions.readByte("Identifier1", inputStream, "Not a Valid BMP File");
        byte b2 = BinaryFunctions.readByte("Identifier2", inputStream, "Not a Valid BMP File");
        if (formatCompliance != null) {
            formatCompliance.compareBytes("Signature", BMP_HEADER_SIGNATURE, new byte[]{b, b2});
        }
        int i42 = BinaryFunctions.read4Bytes("File Size", inputStream, "Not a Valid BMP File", getByteOrder());
        int i43 = BinaryFunctions.read4Bytes("Reserved", inputStream, "Not a Valid BMP File", getByteOrder());
        int i44 = BinaryFunctions.read4Bytes("Bitmap Data Offset", inputStream, "Not a Valid BMP File", getByteOrder());
        int i45 = BinaryFunctions.read4Bytes("Bitmap Header Size", inputStream, "Not a Valid BMP File", getByteOrder());
        BmpHeaderInfo.ColorSpace colorSpace2 = new BmpHeaderInfo.ColorSpace();
        colorSpace2.red = new BmpHeaderInfo.ColorSpaceCoordinate();
        colorSpace2.green = new BmpHeaderInfo.ColorSpaceCoordinate();
        colorSpace2.blue = new BmpHeaderInfo.ColorSpaceCoordinate();
        if (i45 >= 40) {
            int i46 = BinaryFunctions.read4Bytes("Width", inputStream, "Not a Valid BMP File", getByteOrder());
            int i47 = BinaryFunctions.read4Bytes("Height", inputStream, "Not a Valid BMP File", getByteOrder());
            int i48 = BinaryFunctions.read2Bytes("Planes", inputStream, "Not a Valid BMP File", getByteOrder());
            int i49 = BinaryFunctions.read2Bytes("Bits Per Pixel", inputStream, "Not a Valid BMP File", getByteOrder());
            int i50 = BinaryFunctions.read4Bytes("Compression", inputStream, "Not a Valid BMP File", getByteOrder());
            int i51 = BinaryFunctions.read4Bytes("Bitmap Data Size", inputStream, "Not a Valid BMP File", getByteOrder());
            int i52 = BinaryFunctions.read4Bytes("HResolution", inputStream, "Not a Valid BMP File", getByteOrder());
            int i53 = BinaryFunctions.read4Bytes("VResolution", inputStream, "Not a Valid BMP File", getByteOrder());
            int i54 = BinaryFunctions.read4Bytes("ColorsUsed", inputStream, "Not a Valid BMP File", getByteOrder());
            int i55 = BinaryFunctions.read4Bytes("ColorsImportant", inputStream, "Not a Valid BMP File", getByteOrder());
            if (i45 >= 52 || i50 == 3) {
                i = i55;
                int i56 = BinaryFunctions.read4Bytes("RedMask", inputStream, "Not a Valid BMP File", getByteOrder());
                int i57 = BinaryFunctions.read4Bytes("GreenMask", inputStream, "Not a Valid BMP File", getByteOrder());
                i2 = BinaryFunctions.read4Bytes("BlueMask", inputStream, "Not a Valid BMP File", getByteOrder());
                i3 = i56;
                i4 = i57;
            } else {
                i = i55;
                i3 = 0;
                i4 = 0;
                i2 = 0;
            }
            if (i45 >= 56) {
                i6 = i4;
                i5 = i3;
                i7 = BinaryFunctions.read4Bytes("AlphaMask", inputStream, "Not a Valid BMP File", getByteOrder());
            } else {
                i5 = i3;
                i6 = i4;
                i7 = 0;
            }
            if (i45 >= 108) {
                i9 = i7;
                int i58 = BinaryFunctions.read4Bytes("ColorSpaceType", inputStream, "Not a Valid BMP File", getByteOrder());
                i8 = i50;
                colorSpace2.red.x = BinaryFunctions.read4Bytes("ColorSpaceRedX", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.red.y = BinaryFunctions.read4Bytes("ColorSpaceRedY", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.red.z = BinaryFunctions.read4Bytes("ColorSpaceRedZ", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.green.x = BinaryFunctions.read4Bytes("ColorSpaceGreenX", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.green.y = BinaryFunctions.read4Bytes("ColorSpaceGreenY", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.green.z = BinaryFunctions.read4Bytes("ColorSpaceGreenZ", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.blue.x = BinaryFunctions.read4Bytes("ColorSpaceBlueX", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.blue.y = BinaryFunctions.read4Bytes("ColorSpaceBlueY", inputStream, "Not a Valid BMP File", getByteOrder());
                colorSpace2.blue.z = BinaryFunctions.read4Bytes("ColorSpaceBlueZ", inputStream, "Not a Valid BMP File", getByteOrder());
                int i59 = BinaryFunctions.read4Bytes("GammaRed", inputStream, "Not a Valid BMP File", getByteOrder());
                int i60 = BinaryFunctions.read4Bytes("GammaGreen", inputStream, "Not a Valid BMP File", getByteOrder());
                i13 = BinaryFunctions.read4Bytes("GammaBlue", inputStream, "Not a Valid BMP File", getByteOrder());
                i12 = i60;
                i10 = i58;
                i11 = i59;
            } else {
                i8 = i50;
                i9 = i7;
                i10 = 0;
                i11 = 0;
                i12 = 0;
                i13 = 0;
            }
            if (i45 >= 124) {
                i15 = i12;
                i14 = i11;
                int i61 = BinaryFunctions.read4Bytes("Intent", inputStream, "Not a Valid BMP File", getByteOrder());
                int i62 = BinaryFunctions.read4Bytes("ProfileData", inputStream, "Not a Valid BMP File", getByteOrder());
                int i63 = BinaryFunctions.read4Bytes("ProfileSize", inputStream, "Not a Valid BMP File", getByteOrder());
                i19 = BinaryFunctions.read4Bytes("Reserved", inputStream, "Not a Valid BMP File", getByteOrder());
                i16 = i61;
                i18 = i62;
                i17 = i63;
            } else {
                i14 = i11;
                i15 = i12;
                i16 = 0;
                i17 = 0;
                i18 = 0;
                i19 = 0;
            }
            if (z) {
                int i64 = i17;
                debugNumber("identifier1", b, 1);
                debugNumber("identifier2", b2, 1);
                debugNumber("fileSize", i42, 4);
                debugNumber("reserved", i43, 4);
                debugNumber("bitmapDataOffset", i44, 4);
                debugNumber("bitmapHeaderSize", i45, 4);
                debugNumber("width", i46, 4);
                debugNumber("height", i47, 4);
                debugNumber("planes", i48, 2);
                i22 = i48;
                debugNumber("bitsPerPixel", i49, 2);
                i23 = i49;
                int i65 = i8;
                debugNumber("compression", i65, 4);
                i21 = i47;
                debugNumber("bitmapDataSize", i51, 4);
                i24 = i51;
                debugNumber("hResolution", i52, 4);
                i25 = i52;
                debugNumber("vResolution", i53, 4);
                i26 = i53;
                debugNumber("colorsUsed", i54, 4);
                i27 = i54;
                int i66 = i;
                debugNumber("colorsImportant", i66, 4);
                if (i45 >= 52 || i65 == 3) {
                    i28 = i66;
                    int i67 = i5;
                    debugNumber("redMask", i67, 4);
                    i31 = i67;
                    i38 = i6;
                    debugNumber("greenMask", i38, 4);
                    i33 = i65;
                    i39 = i2;
                    debugNumber("blueMask", i39, 4);
                } else {
                    i33 = i65;
                    i28 = i66;
                    i39 = i2;
                    i38 = i6;
                    i31 = i5;
                }
                if (i45 >= 56) {
                    i30 = i38;
                    i32 = i9;
                    debugNumber("alphaMask", i32, 4);
                } else {
                    i30 = i38;
                    i32 = i9;
                }
                if (i45 >= 108) {
                    debugNumber("colorSpaceType", i10, 4);
                    i29 = i39;
                    debugNumber("colorSpace.red.x", colorSpace2.red.x, 1);
                    debugNumber("colorSpace.red.y", colorSpace2.red.y, 1);
                    debugNumber("colorSpace.red.z", colorSpace2.red.z, 1);
                    debugNumber("colorSpace.green.x", colorSpace2.green.x, 1);
                    debugNumber("colorSpace.green.y", colorSpace2.green.y, 1);
                    debugNumber("colorSpace.green.z", colorSpace2.green.z, 1);
                    debugNumber("colorSpace.blue.x", colorSpace2.blue.x, 1);
                    debugNumber("colorSpace.blue.y", colorSpace2.blue.y, 1);
                    debugNumber("colorSpace.blue.z", colorSpace2.blue.z, 1);
                    i36 = i14;
                    i40 = 4;
                    debugNumber("gammaRed", i36, 4);
                    colorSpace = colorSpace2;
                    int i68 = i15;
                    debugNumber("gammaGreen", i68, 4);
                    i35 = i68;
                    i41 = i13;
                    debugNumber("gammaBlue", i41, 4);
                } else {
                    colorSpace = colorSpace2;
                    i29 = i39;
                    i40 = 4;
                    i41 = i13;
                    i35 = i15;
                    i36 = i14;
                }
                if (i45 >= 124) {
                    debugNumber("intent", i16, i40);
                    debugNumber("profileData", i18, i40);
                    i34 = i41;
                    debugNumber("profileSize", i64, i40);
                    i20 = i64;
                    i37 = i19;
                    debugNumber("reservedV5", i37, i40);
                } else {
                    i34 = i41;
                    i37 = i19;
                    i20 = i64;
                }
            } else {
                colorSpace = colorSpace2;
                i20 = i17;
                i21 = i47;
                i22 = i48;
                i23 = i49;
                i24 = i51;
                i25 = i52;
                i26 = i53;
                i27 = i54;
                i28 = i;
                i29 = i2;
                i30 = i6;
                i31 = i5;
                i32 = i9;
                i33 = i8;
                i34 = i13;
                i35 = i15;
                i36 = i14;
                i37 = i19;
            }
            return new BmpHeaderInfo(b, b2, i42, i43, i44, i45, i46, i21, i22, i23, i33, i24, i25, i26, i27, i28, i31, i30, i29, i32, i10, colorSpace, i36, i35, i34, i16, i18, i20, i37);
        }
        throw new ImageReadException("Invalid/unsupported BMP file");
    }

    private byte[] getRLEBytes(InputStream inputStream, int i) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean z = false;
        while (!z) {
            int i2 = BinaryFunctions.readByte("RLE a", inputStream, "BMP: Bad RLE") & 255;
            byteArrayOutputStream.write(i2);
            int i3 = BinaryFunctions.readByte("RLE b", inputStream, "BMP: Bad RLE") & 255;
            byteArrayOutputStream.write(i3);
            if (i2 == 0) {
                switch (i3) {
                    case 0:
                        break;
                    case 1:
                        z = true;
                        break;
                    case 2:
                        byteArrayOutputStream.write(BinaryFunctions.readByte("RLE c", inputStream, "BMP: Bad RLE") & 255);
                        byteArrayOutputStream.write(BinaryFunctions.readByte("RLE d", inputStream, "BMP: Bad RLE") & 255);
                        break;
                    default:
                        int i4 = i3 / i;
                        if (i3 % i > 0) {
                            i4++;
                        }
                        if (i4 % 2 != 0) {
                            i4++;
                        }
                        byteArrayOutputStream.write(BinaryFunctions.readBytes("bytes", inputStream, i4, "RLE: Absolute Mode"));
                        break;
                }
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00e9 A[LOOP:0: B:43:0x00e5->B:45:0x00e9, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x014a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.apache.commons.imaging.formats.bmp.ImageContents readImageContents(java.io.InputStream r11, org.apache.commons.imaging.FormatCompliance r12, boolean r13) throws java.io.IOException, org.apache.commons.imaging.ImageReadException {
        /*
            Method dump skipped, instructions count: 450
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.bmp.BmpImageParser.readImageContents(java.io.InputStream, org.apache.commons.imaging.FormatCompliance, boolean):org.apache.commons.imaging.formats.bmp.ImageContents");
    }

    private BmpHeaderInfo readBmpHeaderInfo(ByteSource byteSource, boolean z) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            BmpHeaderInfo bmpHeaderInfo = readBmpHeaderInfo(inputStream, null, z);
            IoUtils.closeQuietly(true, inputStream);
            return bmpHeaderInfo;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        boolean zEquals = Boolean.TRUE.equals(map2.get(ImagingConstants.PARAM_KEY_VERBOSE));
        if (map2.containsKey(ImagingConstants.PARAM_KEY_VERBOSE)) {
            map2.remove(ImagingConstants.PARAM_KEY_VERBOSE);
        }
        if (!map2.isEmpty()) {
            throw new ImageReadException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        BmpHeaderInfo bmpHeaderInfo = readBmpHeaderInfo(byteSource, zEquals);
        if (bmpHeaderInfo == null) {
            throw new ImageReadException("BMP: couldn't read header");
        }
        return new Dimension(bmpHeaderInfo.width, bmpHeaderInfo.height);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        Throwable th;
        InputStream inputStream;
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        boolean zEquals = Boolean.TRUE.equals(map2.get(ImagingConstants.PARAM_KEY_VERBOSE));
        if (map2.containsKey(ImagingConstants.PARAM_KEY_VERBOSE)) {
            map2.remove(ImagingConstants.PARAM_KEY_VERBOSE);
        }
        if (!map2.isEmpty()) {
            throw new ImageReadException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
        }
        try {
            ImageContents imageContents = readImageContents(inputStream, FormatCompliance.getDefault(), zEquals);
            IoUtils.closeQuietly(true, inputStream);
            if (imageContents == null) {
                throw new ImageReadException("Couldn't read BMP Data");
            }
            BmpHeaderInfo bmpHeaderInfo = imageContents.bhi;
            byte[] bArr = imageContents.colorTable;
            if (bmpHeaderInfo == null) {
                throw new ImageReadException("BMP: couldn't read header");
            }
            int i = bmpHeaderInfo.height;
            int i2 = bmpHeaderInfo.width;
            ArrayList arrayList = new ArrayList();
            int i3 = (int) (bmpHeaderInfo.hResolution * 0.0254d);
            int i4 = (int) (bmpHeaderInfo.vResolution * 0.0254d);
            return new ImageInfo("Bmp (" + ((char) bmpHeaderInfo.identifier1) + ((char) bmpHeaderInfo.identifier2) + ": " + getBmpTypeDescription(bmpHeaderInfo.identifier1, bmpHeaderInfo.identifier2) + ")", bmpHeaderInfo.bitsPerPixel, arrayList, ImageFormats.BMP, "BMP Windows Bitmap", i, "image/x-ms-bmp", -1, i4, (float) (i / i4), i3, (float) (i2 / i3), i2, false, false, bArr != null, ImageInfo.ColorType.RGB, ImageInfo.CompressionAlgorithm.RLE);
        } catch (Throwable th3) {
            th = th3;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        printWriter.println("bmp.dumpImageFile");
        getImageInfo(byteSource, (Map<String, Object>) null).toString(printWriter, "");
        printWriter.println("");
        return true;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        FormatCompliance formatCompliance = new FormatCompliance(byteSource.getDescription());
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            readImageContents(inputStream, formatCompliance, false);
            IoUtils.closeQuietly(true, inputStream);
            return formatCompliance;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            BufferedImage bufferedImage = getBufferedImage(inputStream, map);
            IoUtils.closeQuietly(true, inputStream);
            return bufferedImage;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    public BufferedImage getBufferedImage(InputStream inputStream, Map<String, Object> map) throws IOException, ImageReadException {
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        boolean zEquals = Boolean.TRUE.equals(map2.get(ImagingConstants.PARAM_KEY_VERBOSE));
        if (map2.containsKey(ImagingConstants.PARAM_KEY_VERBOSE)) {
            map2.remove(ImagingConstants.PARAM_KEY_VERBOSE);
        }
        if (map2.containsKey(ImagingConstants.BUFFERED_IMAGE_FACTORY)) {
            map2.remove(ImagingConstants.BUFFERED_IMAGE_FACTORY);
        }
        if (!map2.isEmpty()) {
            throw new ImageReadException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        ImageContents imageContents = readImageContents(inputStream, FormatCompliance.getDefault(), zEquals);
        if (imageContents == null) {
            throw new ImageReadException("Couldn't read BMP Data");
        }
        BmpHeaderInfo bmpHeaderInfo = imageContents.bhi;
        int i = bmpHeaderInfo.width;
        int i2 = bmpHeaderInfo.height;
        if (zEquals) {
            System.out.println("width: " + i);
            System.out.println("height: " + i2);
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("width*height: ");
            int i3 = i * i2;
            sb.append(i3);
            printStream.println(sb.toString());
            System.out.println("width*height*4: " + (i3 * 4));
        }
        PixelParser pixelParser = imageContents.pixelParser;
        ImageBuilder imageBuilder = new ImageBuilder(i, i2, true);
        pixelParser.processImage(imageBuilder);
        return imageBuilder.getBufferedImage();
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        BmpWriter bmpWriterPalette;
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        PixelDensity pixelDensity = map2.containsKey(ImagingConstants.PARAM_KEY_PIXEL_DENSITY) ? (PixelDensity) map2.remove(ImagingConstants.PARAM_KEY_PIXEL_DENSITY) : null;
        if (!map2.isEmpty()) {
            throw new ImageWriteException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        SimplePalette simplePaletteMakeExactRgbPaletteSimple = new PaletteFactory().makeExactRgbPaletteSimple(bufferedImage, 256);
        if (simplePaletteMakeExactRgbPaletteSimple == null) {
            bmpWriterPalette = new BmpWriterRgb();
        } else {
            bmpWriterPalette = new BmpWriterPalette(simplePaletteMakeExactRgbPaletteSimple);
        }
        byte[] imageData = bmpWriterPalette.getImageData(bufferedImage);
        BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        outputStream.write(66);
        outputStream.write(77);
        binaryOutputStream.write4Bytes((bmpWriterPalette.getPaletteSize() * 4) + 54 + imageData.length);
        binaryOutputStream.write4Bytes(0);
        binaryOutputStream.write4Bytes(54 + (4 * bmpWriterPalette.getPaletteSize()));
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        binaryOutputStream.write4Bytes(40);
        binaryOutputStream.write4Bytes(width);
        binaryOutputStream.write4Bytes(height);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(bmpWriterPalette.getBitsPerPixel());
        binaryOutputStream.write4Bytes(0);
        binaryOutputStream.write4Bytes(imageData.length);
        binaryOutputStream.write4Bytes(pixelDensity != null ? (int) Math.round(pixelDensity.horizontalDensityMetres()) : 0);
        binaryOutputStream.write4Bytes(pixelDensity != null ? (int) Math.round(pixelDensity.verticalDensityMetres()) : 0);
        if (simplePaletteMakeExactRgbPaletteSimple == null) {
            binaryOutputStream.write4Bytes(0);
        } else {
            binaryOutputStream.write4Bytes(simplePaletteMakeExactRgbPaletteSimple.length());
        }
        binaryOutputStream.write4Bytes(0);
        bmpWriterPalette.writePalette(binaryOutputStream);
        binaryOutputStream.write(imageData);
    }
}
