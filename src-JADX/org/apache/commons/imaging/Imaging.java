package org.apache.commons.imaging;

import java.awt.Dimension;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import org.apache.commons.imaging.icc.IccProfileInfo;
import org.apache.commons.imaging.icc.IccProfileParser;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class Imaging {
    private static final int[] MAGIC_NUMBERS_GIF = {71, 73};
    private static final int[] MAGIC_NUMBERS_PNG = {137, 80};
    private static final int[] MAGIC_NUMBERS_JPEG = {255, 216};
    private static final int[] MAGIC_NUMBERS_BMP = {66, 77};
    private static final int[] MAGIC_NUMBERS_TIFF_MOTOROLA = {77, 77};
    private static final int[] MAGIC_NUMBERS_TIFF_INTEL = {73, 73};
    private static final int[] MAGIC_NUMBERS_PAM = {80, 55};
    private static final int[] MAGIC_NUMBERS_PSD = {56, 66};
    private static final int[] MAGIC_NUMBERS_PBM_A = {80, 49};
    private static final int[] MAGIC_NUMBERS_PBM_B = {80, 52};
    private static final int[] MAGIC_NUMBERS_PGM_A = {80, 50};
    private static final int[] MAGIC_NUMBERS_PGM_B = {80, 53};
    private static final int[] MAGIC_NUMBERS_PPM_A = {80, 51};
    private static final int[] MAGIC_NUMBERS_PPM_B = {80, 54};
    private static final int[] MAGIC_NUMBERS_JBIG2_1 = {151, 74};
    private static final int[] MAGIC_NUMBERS_JBIG2_2 = {66, 50};
    private static final int[] MAGIC_NUMBERS_ICNS = {105, 99};
    private static final int[] MAGIC_NUMBERS_DCX = {177, 104};
    private static final int[] MAGIC_NUMBERS_RGBE = {35, 63};

    private Imaging() {
    }

    public static boolean hasImageFileExtension(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }
        return hasImageFileExtension(file.getName());
    }

    public static boolean hasImageFileExtension(String str) {
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        for (ImageParser imageParser : ImageParser.getAllImageParsers()) {
            for (String str2 : imageParser.getAcceptedExtensions()) {
                if (lowerCase.endsWith(str2.toLowerCase(Locale.ENGLISH))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ImageFormat guessFormat(byte[] bArr) throws IOException, ImageReadException {
        return guessFormat(new ByteSourceArray(bArr));
    }

    public static ImageFormat guessFormat(File file) throws IOException, ImageReadException {
        return guessFormat(new ByteSourceFile(file));
    }

    private static boolean compareBytePair(int[] iArr, int[] iArr2) {
        if (iArr.length == 2 || iArr2.length == 2) {
            return iArr[0] == iArr2[0] && iArr[1] == iArr2[1];
        }
        throw new RuntimeException("Invalid Byte Pair.");
    }

    /* JADX WARN: Removed duplicated region for block: B:95:0x016c A[Catch: all -> 0x0037, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0037, blocks: (B:13:0x002d, B:21:0x0044, B:25:0x0053, B:29:0x0062, B:33:0x0071, B:37:0x0080, B:41:0x008f, B:45:0x009e, B:49:0x00ad, B:53:0x00bd, B:57:0x00cd, B:61:0x00dd, B:65:0x00ed, B:69:0x00fd, B:79:0x012c, B:95:0x016c, B:85:0x0144, B:89:0x0154, B:93:0x0164), top: B:106:0x002b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.apache.commons.imaging.ImageFormat guessFormat(org.apache.commons.imaging.common.bytesource.ByteSource r6) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 395
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.Imaging.guessFormat(org.apache.commons.imaging.common.bytesource.ByteSource):org.apache.commons.imaging.ImageFormat");
    }

    public static ICC_Profile getICCProfile(byte[] bArr) throws IOException, ImageReadException {
        return getICCProfile(bArr, (Map<String, Object>) null);
    }

    public static ICC_Profile getICCProfile(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getICCProfile(new ByteSourceArray(bArr), map);
    }

    public static ICC_Profile getICCProfile(InputStream inputStream, String str) throws IOException, ImageReadException {
        return getICCProfile(inputStream, str, null);
    }

    public static ICC_Profile getICCProfile(InputStream inputStream, String str, Map<String, Object> map) throws IOException, ImageReadException {
        return getICCProfile(new ByteSourceInputStream(inputStream, str), map);
    }

    public static ICC_Profile getICCProfile(File file) throws IOException, ImageReadException {
        return getICCProfile(file, (Map<String, Object>) null);
    }

    public static ICC_Profile getICCProfile(File file, Map<String, Object> map) throws IOException, ImageReadException {
        return getICCProfile(new ByteSourceFile(file), map);
    }

    protected static ICC_Profile getICCProfile(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        IccProfileInfo iCCProfileInfo;
        byte[] iCCProfileBytes = getICCProfileBytes(byteSource, map);
        if (iCCProfileBytes == null || (iCCProfileInfo = new IccProfileParser().getICCProfileInfo(iCCProfileBytes)) == null || iCCProfileInfo.issRGB()) {
            return null;
        }
        return ICC_Profile.getInstance(iCCProfileBytes);
    }

    public static byte[] getICCProfileBytes(byte[] bArr) throws IOException, ImageReadException {
        return getICCProfileBytes(bArr, (Map<String, Object>) null);
    }

    public static byte[] getICCProfileBytes(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getICCProfileBytes(new ByteSourceArray(bArr), map);
    }

    public static byte[] getICCProfileBytes(File file) throws IOException, ImageReadException {
        return getICCProfileBytes(file, (Map<String, Object>) null);
    }

    public static byte[] getICCProfileBytes(File file, Map<String, Object> map) throws IOException, ImageReadException {
        return getICCProfileBytes(new ByteSourceFile(file), map);
    }

    private static byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageParser(byteSource).getICCProfileBytes(byteSource, map);
    }

    public static ImageInfo getImageInfo(String str, byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceArray(str, bArr), map);
    }

    public static ImageInfo getImageInfo(String str, byte[] bArr) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceArray(str, bArr), (Map<String, Object>) null);
    }

    public static ImageInfo getImageInfo(InputStream inputStream, String str) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceInputStream(inputStream, str), (Map<String, Object>) null);
    }

    public static ImageInfo getImageInfo(InputStream inputStream, String str, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceInputStream(inputStream, str), map);
    }

    public static ImageInfo getImageInfo(byte[] bArr) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceArray(bArr), (Map<String, Object>) null);
    }

    public static ImageInfo getImageInfo(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceArray(bArr), map);
    }

    public static ImageInfo getImageInfo(File file, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceFile(file), map);
    }

    public static ImageInfo getImageInfo(File file) throws IOException, ImageReadException {
        return getImageInfo(file, (Map<String, Object>) null);
    }

    private static ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageParser(byteSource).getImageInfo(byteSource, map);
    }

    private static ImageParser getImageParser(ByteSource byteSource) throws Throwable {
        ImageFormat imageFormatGuessFormat = guessFormat(byteSource);
        if (!imageFormatGuessFormat.equals(ImageFormats.UNKNOWN)) {
            for (ImageParser imageParser : ImageParser.getAllImageParsers()) {
                if (imageParser.canAcceptType(imageFormatGuessFormat)) {
                    return imageParser;
                }
            }
        }
        String filename = byteSource.getFilename();
        if (filename != null) {
            for (ImageParser imageParser2 : ImageParser.getAllImageParsers()) {
                if (imageParser2.canAcceptExtension(filename)) {
                    return imageParser2;
                }
            }
        }
        throw new ImageReadException("Can't parse this format.");
    }

    public static Dimension getImageSize(InputStream inputStream, String str) throws IOException, ImageReadException {
        return getImageSize(inputStream, str, null);
    }

    public static Dimension getImageSize(InputStream inputStream, String str, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageSize(new ByteSourceInputStream(inputStream, str), map);
    }

    public static Dimension getImageSize(byte[] bArr) throws IOException, ImageReadException {
        return getImageSize(bArr, (Map<String, Object>) null);
    }

    public static Dimension getImageSize(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageSize(new ByteSourceArray(bArr), map);
    }

    public static Dimension getImageSize(File file) throws IOException, ImageReadException {
        return getImageSize(file, (Map<String, Object>) null);
    }

    public static Dimension getImageSize(File file, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageSize(new ByteSourceFile(file), map);
    }

    public static Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageParser(byteSource).getImageSize(byteSource, map);
    }

    public static String getXmpXml(InputStream inputStream, String str) throws IOException, ImageReadException {
        return getXmpXml(inputStream, str, null);
    }

    public static String getXmpXml(InputStream inputStream, String str, Map<String, Object> map) throws IOException, ImageReadException {
        return getXmpXml(new ByteSourceInputStream(inputStream, str), map);
    }

    public static String getXmpXml(byte[] bArr) throws IOException, ImageReadException {
        return getXmpXml(bArr, (Map<String, Object>) null);
    }

    public static String getXmpXml(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getXmpXml(new ByteSourceArray(bArr), map);
    }

    public static String getXmpXml(File file) throws IOException, ImageReadException {
        return getXmpXml(file, (Map<String, Object>) null);
    }

    public static String getXmpXml(File file, Map<String, Object> map) throws IOException, ImageReadException {
        return getXmpXml(new ByteSourceFile(file), map);
    }

    public static String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageParser(byteSource).getXmpXml(byteSource, map);
    }

    public static ImageMetadata getMetadata(byte[] bArr) throws IOException, ImageReadException {
        return getMetadata(bArr, (Map<String, Object>) null);
    }

    public static ImageMetadata getMetadata(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getMetadata(new ByteSourceArray(bArr), map);
    }

    public static ImageMetadata getMetadata(InputStream inputStream, String str) throws IOException, ImageReadException {
        return getMetadata(inputStream, str, null);
    }

    public static ImageMetadata getMetadata(InputStream inputStream, String str, Map<String, Object> map) throws IOException, ImageReadException {
        return getMetadata(new ByteSourceInputStream(inputStream, str), map);
    }

    public static ImageMetadata getMetadata(File file) throws IOException, ImageReadException {
        return getMetadata(file, (Map<String, Object>) null);
    }

    public static ImageMetadata getMetadata(File file, Map<String, Object> map) throws IOException, ImageReadException {
        return getMetadata(new ByteSourceFile(file), map);
    }

    private static ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageParser(byteSource).getMetadata(byteSource, map);
    }

    public static String dumpImageFile(byte[] bArr) throws IOException, ImageReadException {
        return dumpImageFile(new ByteSourceArray(bArr));
    }

    public static String dumpImageFile(File file) throws IOException, ImageReadException {
        return dumpImageFile(new ByteSourceFile(file));
    }

    private static String dumpImageFile(ByteSource byteSource) throws IOException, ImageReadException {
        return getImageParser(byteSource).dumpImageFile(byteSource);
    }

    public static FormatCompliance getFormatCompliance(byte[] bArr) throws IOException, ImageReadException {
        return getFormatCompliance(new ByteSourceArray(bArr));
    }

    public static FormatCompliance getFormatCompliance(File file) throws IOException, ImageReadException {
        return getFormatCompliance(new ByteSourceFile(file));
    }

    private static FormatCompliance getFormatCompliance(ByteSource byteSource) throws IOException, ImageReadException {
        return getImageParser(byteSource).getFormatCompliance(byteSource);
    }

    public static List<BufferedImage> getAllBufferedImages(InputStream inputStream, String str) throws IOException, ImageReadException {
        return getAllBufferedImages(new ByteSourceInputStream(inputStream, str));
    }

    public static List<BufferedImage> getAllBufferedImages(byte[] bArr) throws IOException, ImageReadException {
        return getAllBufferedImages(new ByteSourceArray(bArr));
    }

    public static List<BufferedImage> getAllBufferedImages(File file) throws IOException, ImageReadException {
        return getAllBufferedImages(new ByteSourceFile(file));
    }

    private static List<BufferedImage> getAllBufferedImages(ByteSource byteSource) throws IOException, ImageReadException {
        return getImageParser(byteSource).getAllBufferedImages(byteSource);
    }

    public static BufferedImage getBufferedImage(InputStream inputStream) throws IOException, ImageReadException {
        return getBufferedImage(inputStream, (Map<String, Object>) null);
    }

    public static BufferedImage getBufferedImage(InputStream inputStream, Map<String, Object> map) throws IOException, ImageReadException {
        return getBufferedImage(new ByteSourceInputStream(inputStream, (map == null || !map.containsKey(ImagingConstants.PARAM_KEY_FILENAME)) ? null : (String) map.get(ImagingConstants.PARAM_KEY_FILENAME)), map);
    }

    public static BufferedImage getBufferedImage(byte[] bArr) throws IOException, ImageReadException {
        return getBufferedImage(new ByteSourceArray(bArr), (Map<String, Object>) null);
    }

    public static BufferedImage getBufferedImage(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getBufferedImage(new ByteSourceArray(bArr), map);
    }

    public static BufferedImage getBufferedImage(File file) throws IOException, ImageReadException {
        return getBufferedImage(new ByteSourceFile(file), (Map<String, Object>) null);
    }

    public static BufferedImage getBufferedImage(File file, Map<String, Object> map) throws IOException, ImageReadException {
        return getBufferedImage(new ByteSourceFile(file), map);
    }

    private static BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        ImageParser imageParser = getImageParser(byteSource);
        if (map == null) {
            map = new HashMap<>();
        }
        return imageParser.getBufferedImage(byteSource, map);
    }

    public static void writeImage(BufferedImage bufferedImage, File file, ImageFormat imageFormat, Map<String, Object> map) throws Throwable {
        OutputStream fileOutputStream;
        OutputStream bufferedOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (Throwable th) {
            th = th;
        }
        try {
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            writeImage(bufferedImage, bufferedOutputStream, imageFormat, map);
            IoUtils.closeQuietly(true, bufferedOutputStream);
        } catch (Throwable th2) {
            th = th2;
            bufferedOutputStream = fileOutputStream;
            IoUtils.closeQuietly(false, bufferedOutputStream);
            throw th;
        }
    }

    public static byte[] writeImageToBytes(BufferedImage bufferedImage, ImageFormat imageFormat, Map<String, Object> map) throws ImageWriteException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeImage(bufferedImage, byteArrayOutputStream, imageFormat, map);
        return byteArrayOutputStream.toByteArray();
    }

    public static void writeImage(BufferedImage bufferedImage, OutputStream outputStream, ImageFormat imageFormat, Map<String, Object> map) throws ImageWriteException, IOException {
        ImageParser[] allImageParsers = ImageParser.getAllImageParsers();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(ImagingConstants.PARAM_KEY_FORMAT, imageFormat);
        ImageParser imageParser = null;
        int length = allImageParsers.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            ImageParser imageParser2 = allImageParsers[i];
            if (imageParser2.canAcceptType(imageFormat)) {
                imageParser = imageParser2;
                break;
            }
            i++;
        }
        if (imageParser != null) {
            imageParser.writeImage(bufferedImage, outputStream, map);
            return;
        }
        throw new ImageWriteException("Unknown Format: " + imageFormat);
    }
}
