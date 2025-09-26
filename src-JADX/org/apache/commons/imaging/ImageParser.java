package org.apache.commons.imaging;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.BufferedImageFactory;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.SimpleBufferedImageFactory;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.bmp.BmpImageParser;
import org.apache.commons.imaging.formats.dcx.DcxImageParser;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import org.apache.commons.imaging.formats.icns.IcnsImageParser;
import org.apache.commons.imaging.formats.ico.IcoImageParser;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.pnm.PnmImageParser;
import org.apache.commons.imaging.formats.psd.PsdImageParser;
import org.apache.commons.imaging.formats.rgbe.RgbeImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.wbmp.WbmpImageParser;
import org.apache.commons.imaging.formats.xbm.XbmImageParser;
import org.apache.commons.imaging.formats.xpm.XpmImageParser;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class ImageParser extends BinaryFileParser {
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        return false;
    }

    protected abstract String[] getAcceptedExtensions();

    protected abstract ImageFormat[] getAcceptedTypes();

    public abstract BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException;

    public abstract String getDefaultExtension();

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws IOException, ImageReadException {
        return null;
    }

    public abstract byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException;

    public abstract ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException;

    public abstract Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException;

    public abstract ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException;

    public abstract String getName();

    public abstract String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException;

    public static ImageParser[] getAllImageParsers() {
        return new ImageParser[]{new BmpImageParser(), new DcxImageParser(), new GifImageParser(), new IcnsImageParser(), new IcoImageParser(), new JpegImageParser(), new PcxImageParser(), new PngImageParser(), new PnmImageParser(), new PsdImageParser(), new RgbeImageParser(), new TiffImageParser(), new WbmpImageParser(), new XbmImageParser(), new XpmImageParser()};
    }

    public final ImageMetadata getMetadata(ByteSource byteSource) throws IOException, ImageReadException {
        return getMetadata(byteSource, (Map<String, Object>) null);
    }

    public final ImageMetadata getMetadata(byte[] bArr) throws IOException, ImageReadException {
        return getMetadata(bArr, (Map<String, Object>) null);
    }

    public final ImageMetadata getMetadata(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getMetadata(new ByteSourceArray(bArr), map);
    }

    public final ImageMetadata getMetadata(File file) throws IOException, ImageReadException {
        return getMetadata(file, (Map<String, Object>) null);
    }

    public final ImageMetadata getMetadata(File file, Map<String, Object> map) throws IOException, ImageReadException {
        if (getDebug()) {
            System.out.println(getName() + ".getMetadata: " + file.getName());
        }
        if (canAcceptExtension(file)) {
            return getMetadata(new ByteSourceFile(file), map);
        }
        return null;
    }

    public final ImageInfo getImageInfo(ByteSource byteSource) throws IOException, ImageReadException {
        return getImageInfo(byteSource, (Map<String, Object>) null);
    }

    public final ImageInfo getImageInfo(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageInfo(new ByteSourceArray(bArr), map);
    }

    public final ImageInfo getImageInfo(File file, Map<String, Object> map) throws IOException, ImageReadException {
        if (canAcceptExtension(file)) {
            return getImageInfo(new ByteSourceFile(file), map);
        }
        return null;
    }

    public final FormatCompliance getFormatCompliance(byte[] bArr) throws IOException, ImageReadException {
        return getFormatCompliance(new ByteSourceArray(bArr));
    }

    public final FormatCompliance getFormatCompliance(File file) throws IOException, ImageReadException {
        if (canAcceptExtension(file)) {
            return getFormatCompliance(new ByteSourceFile(file));
        }
        return null;
    }

    public List<BufferedImage> getAllBufferedImages(ByteSource byteSource) throws IOException, ImageReadException {
        BufferedImage bufferedImage = getBufferedImage(byteSource, (Map<String, Object>) null);
        ArrayList arrayList = new ArrayList();
        arrayList.add(bufferedImage);
        return arrayList;
    }

    public final List<BufferedImage> getAllBufferedImages(byte[] bArr) throws IOException, ImageReadException {
        return getAllBufferedImages(new ByteSourceArray(bArr));
    }

    public final List<BufferedImage> getAllBufferedImages(File file) throws IOException, ImageReadException {
        if (canAcceptExtension(file)) {
            return getAllBufferedImages(new ByteSourceFile(file));
        }
        return null;
    }

    public final BufferedImage getBufferedImage(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getBufferedImage(new ByteSourceArray(bArr), map);
    }

    public final BufferedImage getBufferedImage(File file, Map<String, Object> map) throws IOException, ImageReadException {
        if (canAcceptExtension(file)) {
            return getBufferedImage(new ByteSourceFile(file), map);
        }
        return null;
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.close();
        throw new ImageWriteException("This image format (" + getName() + ") cannot be written.");
    }

    public final Dimension getImageSize(byte[] bArr) throws IOException, ImageReadException {
        return getImageSize(bArr, (Map<String, Object>) null);
    }

    public final Dimension getImageSize(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getImageSize(new ByteSourceArray(bArr), map);
    }

    public final Dimension getImageSize(File file) throws IOException, ImageReadException {
        return getImageSize(file, (Map<String, Object>) null);
    }

    public final Dimension getImageSize(File file, Map<String, Object> map) throws IOException, ImageReadException {
        if (canAcceptExtension(file)) {
            return getImageSize(new ByteSourceFile(file), map);
        }
        return null;
    }

    public final byte[] getICCProfileBytes(byte[] bArr) throws IOException, ImageReadException {
        return getICCProfileBytes(bArr, (Map<String, Object>) null);
    }

    public final byte[] getICCProfileBytes(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        return getICCProfileBytes(new ByteSourceArray(bArr), map);
    }

    public final byte[] getICCProfileBytes(File file) throws IOException, ImageReadException {
        return getICCProfileBytes(file, (Map<String, Object>) null);
    }

    public final byte[] getICCProfileBytes(File file, Map<String, Object> map) throws IOException, ImageReadException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (getDebug()) {
            System.out.println(getName() + ": " + file.getName());
        }
        return getICCProfileBytes(new ByteSourceFile(file), map);
    }

    public final String dumpImageFile(byte[] bArr) throws IOException, ImageReadException {
        return dumpImageFile(new ByteSourceArray(bArr));
    }

    public final String dumpImageFile(File file) throws IOException, ImageReadException {
        if (!canAcceptExtension(file)) {
            return null;
        }
        if (getDebug()) {
            System.out.println(getName() + ": " + file.getName());
        }
        return dumpImageFile(new ByteSourceFile(file));
    }

    public final String dumpImageFile(ByteSource byteSource) throws IOException, ImageReadException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        dumpImageFile(printWriter, byteSource);
        printWriter.flush();
        return stringWriter.toString();
    }

    public boolean canAcceptType(ImageFormat imageFormat) {
        for (ImageFormat imageFormat2 : getAcceptedTypes()) {
            if (imageFormat2.equals(imageFormat)) {
                return true;
            }
        }
        return false;
    }

    protected final boolean canAcceptExtension(File file) {
        return canAcceptExtension(file.getName());
    }

    protected final boolean canAcceptExtension(String str) {
        String[] acceptedExtensions = getAcceptedExtensions();
        if (acceptedExtensions == null) {
            return true;
        }
        int iLastIndexOf = str.lastIndexOf(46);
        if (iLastIndexOf >= 0) {
            String lowerCase = str.substring(iLastIndexOf).toLowerCase(Locale.ENGLISH);
            for (String str2 : acceptedExtensions) {
                if (str2.toLowerCase(Locale.ENGLISH).equals(lowerCase)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected BufferedImageFactory getBufferedImageFactory(Map<String, Object> map) {
        if (map == null) {
            return new SimpleBufferedImageFactory();
        }
        BufferedImageFactory bufferedImageFactory = (BufferedImageFactory) map.get(ImagingConstants.BUFFERED_IMAGE_FACTORY);
        return bufferedImageFactory != null ? bufferedImageFactory : new SimpleBufferedImageFactory();
    }

    public static boolean isStrict(Map<String, Object> map) {
        if (map == null || !map.containsKey(ImagingConstants.PARAM_KEY_STRICT)) {
            return false;
        }
        return ((Boolean) map.get(ImagingConstants.PARAM_KEY_STRICT)).booleanValue();
    }
}
