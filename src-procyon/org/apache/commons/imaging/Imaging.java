// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.icc.IccProfileInfo;
import org.apache.commons.imaging.icc.IccProfileParser;
import java.awt.color.ICC_Profile;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import java.io.IOException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.io.File;

public final class Imaging
{
    private static final int[] MAGIC_NUMBERS_BMP;
    private static final int[] MAGIC_NUMBERS_DCX;
    private static final int[] MAGIC_NUMBERS_GIF;
    private static final int[] MAGIC_NUMBERS_ICNS;
    private static final int[] MAGIC_NUMBERS_JBIG2_1;
    private static final int[] MAGIC_NUMBERS_JBIG2_2;
    private static final int[] MAGIC_NUMBERS_JPEG;
    private static final int[] MAGIC_NUMBERS_PAM;
    private static final int[] MAGIC_NUMBERS_PBM_A;
    private static final int[] MAGIC_NUMBERS_PBM_B;
    private static final int[] MAGIC_NUMBERS_PGM_A;
    private static final int[] MAGIC_NUMBERS_PGM_B;
    private static final int[] MAGIC_NUMBERS_PNG;
    private static final int[] MAGIC_NUMBERS_PPM_A;
    private static final int[] MAGIC_NUMBERS_PPM_B;
    private static final int[] MAGIC_NUMBERS_PSD;
    private static final int[] MAGIC_NUMBERS_RGBE;
    private static final int[] MAGIC_NUMBERS_TIFF_INTEL;
    private static final int[] MAGIC_NUMBERS_TIFF_MOTOROLA;
    
    static {
        MAGIC_NUMBERS_GIF = new int[] { 71, 73 };
        MAGIC_NUMBERS_PNG = new int[] { 137, 80 };
        MAGIC_NUMBERS_JPEG = new int[] { 255, 216 };
        MAGIC_NUMBERS_BMP = new int[] { 66, 77 };
        MAGIC_NUMBERS_TIFF_MOTOROLA = new int[] { 77, 77 };
        MAGIC_NUMBERS_TIFF_INTEL = new int[] { 73, 73 };
        MAGIC_NUMBERS_PAM = new int[] { 80, 55 };
        MAGIC_NUMBERS_PSD = new int[] { 56, 66 };
        MAGIC_NUMBERS_PBM_A = new int[] { 80, 49 };
        MAGIC_NUMBERS_PBM_B = new int[] { 80, 52 };
        MAGIC_NUMBERS_PGM_A = new int[] { 80, 50 };
        MAGIC_NUMBERS_PGM_B = new int[] { 80, 53 };
        MAGIC_NUMBERS_PPM_A = new int[] { 80, 51 };
        MAGIC_NUMBERS_PPM_B = new int[] { 80, 54 };
        MAGIC_NUMBERS_JBIG2_1 = new int[] { 151, 74 };
        MAGIC_NUMBERS_JBIG2_2 = new int[] { 66, 50 };
        MAGIC_NUMBERS_ICNS = new int[] { 105, 99 };
        MAGIC_NUMBERS_DCX = new int[] { 177, 104 };
        MAGIC_NUMBERS_RGBE = new int[] { 35, 63 };
    }
    
    private Imaging() {
    }
    
    private static boolean compareBytePair(final int[] array, final int[] array2) {
        if (array.length != 2 && array2.length != 2) {
            throw new RuntimeException("Invalid Byte Pair.");
        }
        boolean b = false;
        if (array[0] == array2[0]) {
            b = b;
            if (array[1] == array2[1]) {
                b = true;
            }
        }
        return b;
    }
    
    public static String dumpImageFile(final File file) throws ImageReadException, IOException {
        return dumpImageFile(new ByteSourceFile(file));
    }
    
    private static String dumpImageFile(final ByteSource byteSource) throws ImageReadException, IOException {
        return getImageParser(byteSource).dumpImageFile(byteSource);
    }
    
    public static String dumpImageFile(final byte[] array) throws ImageReadException, IOException {
        return dumpImageFile(new ByteSourceArray(array));
    }
    
    public static List<BufferedImage> getAllBufferedImages(final File file) throws ImageReadException, IOException {
        return getAllBufferedImages(new ByteSourceFile(file));
    }
    
    public static List<BufferedImage> getAllBufferedImages(final InputStream inputStream, final String s) throws ImageReadException, IOException {
        return getAllBufferedImages(new ByteSourceInputStream(inputStream, s));
    }
    
    private static List<BufferedImage> getAllBufferedImages(final ByteSource byteSource) throws ImageReadException, IOException {
        return getImageParser(byteSource).getAllBufferedImages(byteSource);
    }
    
    public static List<BufferedImage> getAllBufferedImages(final byte[] array) throws ImageReadException, IOException {
        return getAllBufferedImages(new ByteSourceArray(array));
    }
    
    public static BufferedImage getBufferedImage(final File file) throws ImageReadException, IOException {
        return getBufferedImage(new ByteSourceFile(file), null);
    }
    
    public static BufferedImage getBufferedImage(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        return getBufferedImage(new ByteSourceFile(file), map);
    }
    
    public static BufferedImage getBufferedImage(final InputStream inputStream) throws ImageReadException, IOException {
        return getBufferedImage(inputStream, null);
    }
    
    public static BufferedImage getBufferedImage(final InputStream inputStream, final Map<String, Object> map) throws ImageReadException, IOException {
        String s;
        if (map != null && map.containsKey("FILENAME")) {
            s = map.get("FILENAME");
        }
        else {
            s = null;
        }
        return getBufferedImage(new ByteSourceInputStream(inputStream, s), map);
    }
    
    private static BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ImageParser imageParser = getImageParser(byteSource);
        Map<String, Object> map2 = map;
        if (map == null) {
            map2 = new HashMap<String, Object>();
        }
        return imageParser.getBufferedImage(byteSource, map2);
    }
    
    public static BufferedImage getBufferedImage(final byte[] array) throws ImageReadException, IOException {
        return getBufferedImage(new ByteSourceArray(array), null);
    }
    
    public static BufferedImage getBufferedImage(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getBufferedImage(new ByteSourceArray(array), map);
    }
    
    public static FormatCompliance getFormatCompliance(final File file) throws ImageReadException, IOException {
        return getFormatCompliance(new ByteSourceFile(file));
    }
    
    private static FormatCompliance getFormatCompliance(final ByteSource byteSource) throws ImageReadException, IOException {
        return getImageParser(byteSource).getFormatCompliance(byteSource);
    }
    
    public static FormatCompliance getFormatCompliance(final byte[] array) throws ImageReadException, IOException {
        return getFormatCompliance(new ByteSourceArray(array));
    }
    
    public static ICC_Profile getICCProfile(final File file) throws ImageReadException, IOException {
        return getICCProfile(file, null);
    }
    
    public static ICC_Profile getICCProfile(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        return getICCProfile(new ByteSourceFile(file), map);
    }
    
    public static ICC_Profile getICCProfile(final InputStream inputStream, final String s) throws ImageReadException, IOException {
        return getICCProfile(inputStream, s, null);
    }
    
    public static ICC_Profile getICCProfile(final InputStream inputStream, final String s, final Map<String, Object> map) throws ImageReadException, IOException {
        return getICCProfile(new ByteSourceInputStream(inputStream, s), map);
    }
    
    protected static ICC_Profile getICCProfile(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final byte[] iccProfileBytes = getICCProfileBytes(byteSource, map);
        if (iccProfileBytes == null) {
            return null;
        }
        final IccProfileInfo iccProfileInfo = new IccProfileParser().getICCProfileInfo(iccProfileBytes);
        if (iccProfileInfo == null) {
            return null;
        }
        if (iccProfileInfo.issRGB()) {
            return null;
        }
        return ICC_Profile.getInstance(iccProfileBytes);
    }
    
    public static ICC_Profile getICCProfile(final byte[] array) throws ImageReadException, IOException {
        return getICCProfile(array, null);
    }
    
    public static ICC_Profile getICCProfile(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getICCProfile(new ByteSourceArray(array), map);
    }
    
    public static byte[] getICCProfileBytes(final File file) throws ImageReadException, IOException {
        return getICCProfileBytes(file, null);
    }
    
    public static byte[] getICCProfileBytes(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        return getICCProfileBytes(new ByteSourceFile(file), map);
    }
    
    private static byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getICCProfileBytes(byteSource, map);
    }
    
    public static byte[] getICCProfileBytes(final byte[] array) throws ImageReadException, IOException {
        return getICCProfileBytes(array, null);
    }
    
    public static byte[] getICCProfileBytes(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getICCProfileBytes(new ByteSourceArray(array), map);
    }
    
    public static ImageInfo getImageInfo(final File file) throws ImageReadException, IOException {
        return getImageInfo(file, null);
    }
    
    public static ImageInfo getImageInfo(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceFile(file), map);
    }
    
    public static ImageInfo getImageInfo(final InputStream inputStream, final String s) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceInputStream(inputStream, s), null);
    }
    
    public static ImageInfo getImageInfo(final InputStream inputStream, final String s, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceInputStream(inputStream, s), map);
    }
    
    public static ImageInfo getImageInfo(final String s, final byte[] array) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceArray(s, array), null);
    }
    
    public static ImageInfo getImageInfo(final String s, final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceArray(s, array), map);
    }
    
    private static ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getImageInfo(byteSource, map);
    }
    
    public static ImageInfo getImageInfo(final byte[] array) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceArray(array), null);
    }
    
    public static ImageInfo getImageInfo(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageInfo(new ByteSourceArray(array), map);
    }
    
    private static ImageParser getImageParser(final ByteSource byteSource) throws ImageReadException, IOException {
        final ImageFormat guessFormat = guessFormat(byteSource);
        final boolean equals = guessFormat.equals(ImageFormats.UNKNOWN);
        final int n = 0;
        if (!equals) {
            for (final ImageParser imageParser : ImageParser.getAllImageParsers()) {
                if (imageParser.canAcceptType(guessFormat)) {
                    return imageParser;
                }
            }
        }
        final String filename = byteSource.getFilename();
        if (filename != null) {
            final ImageParser[] allImageParsers2 = ImageParser.getAllImageParsers();
            for (int length2 = allImageParsers2.length, j = n; j < length2; ++j) {
                final ImageParser imageParser2 = allImageParsers2[j];
                if (imageParser2.canAcceptExtension(filename)) {
                    return imageParser2;
                }
            }
        }
        throw new ImageReadException("Can't parse this format.");
    }
    
    public static Dimension getImageSize(final File file) throws ImageReadException, IOException {
        return getImageSize(file, null);
    }
    
    public static Dimension getImageSize(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageSize(new ByteSourceFile(file), map);
    }
    
    public static Dimension getImageSize(final InputStream inputStream, final String s) throws ImageReadException, IOException {
        return getImageSize(inputStream, s, null);
    }
    
    public static Dimension getImageSize(final InputStream inputStream, final String s, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageSize(new ByteSourceInputStream(inputStream, s), map);
    }
    
    public static Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getImageSize(byteSource, map);
    }
    
    public static Dimension getImageSize(final byte[] array) throws ImageReadException, IOException {
        return getImageSize(array, null);
    }
    
    public static Dimension getImageSize(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageSize(new ByteSourceArray(array), map);
    }
    
    public static ImageMetadata getMetadata(final File file) throws ImageReadException, IOException {
        return getMetadata(file, null);
    }
    
    public static ImageMetadata getMetadata(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceFile(file), map);
    }
    
    public static ImageMetadata getMetadata(final InputStream inputStream, final String s) throws ImageReadException, IOException {
        return getMetadata(inputStream, s, null);
    }
    
    public static ImageMetadata getMetadata(final InputStream inputStream, final String s, final Map<String, Object> map) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceInputStream(inputStream, s), map);
    }
    
    private static ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getMetadata(byteSource, map);
    }
    
    public static ImageMetadata getMetadata(final byte[] array) throws ImageReadException, IOException {
        return getMetadata(array, null);
    }
    
    public static ImageMetadata getMetadata(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getMetadata(new ByteSourceArray(array), map);
    }
    
    public static String getXmpXml(final File file) throws ImageReadException, IOException {
        return getXmpXml(file, null);
    }
    
    public static String getXmpXml(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        return getXmpXml(new ByteSourceFile(file), map);
    }
    
    public static String getXmpXml(final InputStream inputStream, final String s) throws ImageReadException, IOException {
        return getXmpXml(inputStream, s, null);
    }
    
    public static String getXmpXml(final InputStream inputStream, final String s, final Map<String, Object> map) throws ImageReadException, IOException {
        return getXmpXml(new ByteSourceInputStream(inputStream, s), map);
    }
    
    public static String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return getImageParser(byteSource).getXmpXml(byteSource, map);
    }
    
    public static String getXmpXml(final byte[] array) throws ImageReadException, IOException {
        return getXmpXml(array, null);
    }
    
    public static String getXmpXml(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return getXmpXml(new ByteSourceArray(array), map);
    }
    
    public static ImageFormat guessFormat(final File file) throws ImageReadException, IOException {
        return guessFormat(new ByteSourceFile(file));
    }
    
    public static ImageFormat guessFormat(ByteSource inputStream) throws ImageReadException, IOException {
        if (inputStream == null) {
            return ImageFormats.UNKNOWN;
        }
        Closeable closeable;
        try {
            inputStream = (ByteSource)inputStream.getInputStream();
            try {
                final int read = ((InputStream)inputStream).read();
                final int read2 = ((InputStream)inputStream).read();
                if (read < 0 || read2 < 0) {
                    throw new ImageReadException("Couldn't read magic numbers to guess format.");
                }
                final int[] array = { read & 0xFF, read2 & 0xFF };
                boolean b = compareBytePair(Imaging.MAGIC_NUMBERS_GIF, array);
                while (true) {
                    Label_0111: {
                        if (!b) {
                            break Label_0111;
                        }
                        try {
                            final Object gif = ImageFormats.GIF;
                            final Closeable[] array2 = { (Closeable)inputStream };
                            inputStream = (ByteSource)gif;
                            IoUtils.closeQuietly(true, array2);
                            return (ImageFormat)inputStream;
                        }
                        finally {
                            b = true;
                        }
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PNG, array);
                    if (b) {
                        final Object png = ImageFormats.PNG;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)png;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_JPEG, array);
                    if (b) {
                        final Object jpeg = ImageFormats.JPEG;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)jpeg;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_BMP, array);
                    if (b) {
                        final Object bmp = ImageFormats.BMP;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)bmp;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_TIFF_MOTOROLA, array);
                    if (b) {
                        final Object tiff = ImageFormats.TIFF;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)tiff;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_TIFF_INTEL, array);
                    if (b) {
                        final Object tiff2 = ImageFormats.TIFF;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)tiff2;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PSD, array);
                    if (b) {
                        final Object psd = ImageFormats.PSD;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)psd;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PAM, array);
                    if (b) {
                        final Object pam = ImageFormats.PAM;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)pam;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PBM_A, array);
                    if (b) {
                        final Object pbm = ImageFormats.PBM;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)pbm;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PBM_B, array);
                    if (b) {
                        final Object pbm2 = ImageFormats.PBM;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)pbm2;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PGM_A, array);
                    if (b) {
                        final Object pgm = ImageFormats.PGM;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)pgm;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PGM_B, array);
                    if (b) {
                        final Object pgm2 = ImageFormats.PGM;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)pgm2;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PPM_A, array);
                    if (b) {
                        final Object ppm = ImageFormats.PPM;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)ppm;
                        continue;
                    }
                    b = compareBytePair(Imaging.MAGIC_NUMBERS_PPM_B, array);
                    if (b) {
                        final Object ppm2 = ImageFormats.PPM;
                        final Closeable[] array2 = { (Closeable)inputStream };
                        inputStream = (ByteSource)ppm2;
                        continue;
                    }
                    if (compareBytePair(Imaging.MAGIC_NUMBERS_JBIG2_1, array)) {
                        final int read3 = ((InputStream)inputStream).read();
                        final int read4 = ((InputStream)inputStream).read();
                        if (read3 < 0 || read4 < 0) {
                            throw new ImageReadException("Couldn't read magic numbers to guess format.");
                        }
                        b = compareBytePair(Imaging.MAGIC_NUMBERS_JBIG2_2, new int[] { read3 & 0xFF, read4 & 0xFF });
                        if (b) {
                            final Object jbig2 = ImageFormats.JBIG2;
                            final Closeable[] array2 = { (Closeable)inputStream };
                            inputStream = (ByteSource)jbig2;
                            continue;
                        }
                    }
                    else {
                        b = compareBytePair(Imaging.MAGIC_NUMBERS_ICNS, array);
                        if (b) {
                            final Object icns = ImageFormats.ICNS;
                            final Closeable[] array2 = { (Closeable)inputStream };
                            inputStream = (ByteSource)icns;
                            continue;
                        }
                        b = compareBytePair(Imaging.MAGIC_NUMBERS_DCX, array);
                        if (b) {
                            final Object dcx = ImageFormats.DCX;
                            final Closeable[] array2 = { (Closeable)inputStream };
                            inputStream = (ByteSource)dcx;
                            continue;
                        }
                        b = compareBytePair(Imaging.MAGIC_NUMBERS_RGBE, array);
                        if (b) {
                            final Object rgbe = ImageFormats.RGBE;
                            final Closeable[] array2 = { (Closeable)inputStream };
                            inputStream = (ByteSource)rgbe;
                            continue;
                        }
                    }
                    final Object unknown = ImageFormats.UNKNOWN;
                    final Closeable[] array2 = { (Closeable)inputStream };
                    inputStream = (ByteSource)unknown;
                    continue;
                }
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        boolean b = false;
        final Throwable t2;
        final Throwable t = t2;
        IoUtils.closeQuietly(b, closeable);
        throw t;
    }
    
    public static ImageFormat guessFormat(final byte[] array) throws ImageReadException, IOException {
        return guessFormat(new ByteSourceArray(array));
    }
    
    public static boolean hasImageFileExtension(final File file) {
        return file != null && file.isFile() && hasImageFileExtension(file.getName());
    }
    
    public static boolean hasImageFileExtension(String lowerCase) {
        if (lowerCase == null) {
            return false;
        }
        lowerCase = lowerCase.toLowerCase(Locale.ENGLISH);
        final ImageParser[] allImageParsers = ImageParser.getAllImageParsers();
        for (int length = allImageParsers.length, i = 0; i < length; ++i) {
            final String[] acceptedExtensions = allImageParsers[i].getAcceptedExtensions();
            for (int length2 = acceptedExtensions.length, j = 0; j < length2; ++j) {
                if (lowerCase.endsWith(acceptedExtensions[j].toLowerCase(Locale.ENGLISH))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void writeImage(final BufferedImage bufferedImage, final File file, final ImageFormat imageFormat, final Map<String, Object> map) throws ImageWriteException, IOException {
        Closeable closeable = null;
        try {
            closeable = closeable;
            final FileOutputStream out = new FileOutputStream(file);
            try {
                final BufferedOutputStream bufferedOutputStream = (BufferedOutputStream)(closeable = new BufferedOutputStream(out));
                writeImage(bufferedImage, bufferedOutputStream, imageFormat, map);
                IoUtils.closeQuietly(true, bufferedOutputStream);
                return;
            }
            finally {
                closeable = out;
            }
        }
        finally {}
        IoUtils.closeQuietly(false, closeable);
    }
    
    public static void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final ImageFormat obj, final Map<String, Object> map) throws ImageWriteException, IOException {
        final ImageParser[] allImageParsers = ImageParser.getAllImageParsers();
        Map<String, Object> map2 = map;
        if (map == null) {
            map2 = new HashMap<String, Object>();
        }
        map2.put("FORMAT", obj);
        final ImageParser imageParser = null;
        final int length = allImageParsers.length;
        int n = 0;
        ImageParser imageParser2;
        while (true) {
            imageParser2 = imageParser;
            if (n >= length) {
                break;
            }
            imageParser2 = allImageParsers[n];
            if (imageParser2.canAcceptType(obj)) {
                break;
            }
            ++n;
        }
        if (imageParser2 != null) {
            imageParser2.writeImage(bufferedImage, outputStream, map2);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown Format: ");
        sb.append(obj);
        throw new ImageWriteException(sb.toString());
    }
    
    public static byte[] writeImageToBytes(final BufferedImage bufferedImage, final ImageFormat imageFormat, final Map<String, Object> map) throws ImageWriteException, IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeImage(bufferedImage, byteArrayOutputStream, imageFormat, map);
        return byteArrayOutputStream.toByteArray();
    }
}
