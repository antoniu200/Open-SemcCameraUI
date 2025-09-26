// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

import java.io.OutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.common.SimpleBufferedImageFactory;
import org.apache.commons.imaging.common.BufferedImageFactory;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.util.Locale;
import java.io.File;
import java.util.Map;
import org.apache.commons.imaging.formats.xpm.XpmImageParser;
import org.apache.commons.imaging.formats.xbm.XbmImageParser;
import org.apache.commons.imaging.formats.wbmp.WbmpImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.rgbe.RgbeImageParser;
import org.apache.commons.imaging.formats.psd.PsdImageParser;
import org.apache.commons.imaging.formats.pnm.PnmImageParser;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.ico.IcoImageParser;
import org.apache.commons.imaging.formats.icns.IcnsImageParser;
import org.apache.commons.imaging.formats.gif.GifImageParser;
import org.apache.commons.imaging.formats.dcx.DcxImageParser;
import org.apache.commons.imaging.formats.bmp.BmpImageParser;
import org.apache.commons.imaging.common.BinaryFileParser;

public abstract class ImageParser extends BinaryFileParser
{
    public static ImageParser[] getAllImageParsers() {
        return new ImageParser[] { new BmpImageParser(), new DcxImageParser(), new GifImageParser(), new IcnsImageParser(), new IcoImageParser(), new JpegImageParser(), new PcxImageParser(), new PngImageParser(), new PnmImageParser(), new PsdImageParser(), new RgbeImageParser(), new TiffImageParser(), new WbmpImageParser(), new XbmImageParser(), new XpmImageParser() };
    }
    
    public static boolean isStrict(final Map<String, Object> map) {
        return map != null && map.containsKey("STRICT") && map.get("STRICT");
    }
    
    protected final boolean canAcceptExtension(final File file) {
        return this.canAcceptExtension(file.getName());
    }
    
    protected final boolean canAcceptExtension(String lowerCase) {
        final String[] acceptedExtensions = this.getAcceptedExtensions();
        if (acceptedExtensions == null) {
            return true;
        }
        final int lastIndex = lowerCase.lastIndexOf(46);
        if (lastIndex >= 0) {
            lowerCase = lowerCase.substring(lastIndex).toLowerCase(Locale.ENGLISH);
            for (int length = acceptedExtensions.length, i = 0; i < length; ++i) {
                if (acceptedExtensions[i].toLowerCase(Locale.ENGLISH).equals(lowerCase)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean canAcceptType(final ImageFormat obj) {
        final ImageFormat[] acceptedTypes = this.getAcceptedTypes();
        for (int length = acceptedTypes.length, i = 0; i < length; ++i) {
            if (acceptedTypes[i].equals(obj)) {
                return true;
            }
        }
        return false;
    }
    
    public final String dumpImageFile(final File file) throws ImageReadException, IOException {
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getName());
            sb.append(": ");
            sb.append(file.getName());
            out.println(sb.toString());
        }
        return this.dumpImageFile(new ByteSourceFile(file));
    }
    
    public final String dumpImageFile(final ByteSource byteSource) throws ImageReadException, IOException {
        final StringWriter out = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(out);
        this.dumpImageFile(printWriter, byteSource);
        printWriter.flush();
        return out.toString();
    }
    
    public final String dumpImageFile(final byte[] array) throws ImageReadException, IOException {
        return this.dumpImageFile(new ByteSourceArray(array));
    }
    
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        return false;
    }
    
    protected abstract String[] getAcceptedExtensions();
    
    protected abstract ImageFormat[] getAcceptedTypes();
    
    public final List<BufferedImage> getAllBufferedImages(final File file) throws ImageReadException, IOException {
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        return this.getAllBufferedImages(new ByteSourceFile(file));
    }
    
    public List<BufferedImage> getAllBufferedImages(final ByteSource byteSource) throws ImageReadException, IOException {
        final BufferedImage bufferedImage = this.getBufferedImage(byteSource, null);
        final ArrayList list = new ArrayList();
        list.add(bufferedImage);
        return list;
    }
    
    public final List<BufferedImage> getAllBufferedImages(final byte[] array) throws ImageReadException, IOException {
        return this.getAllBufferedImages(new ByteSourceArray(array));
    }
    
    public final BufferedImage getBufferedImage(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        return this.getBufferedImage(new ByteSourceFile(file), map);
    }
    
    public abstract BufferedImage getBufferedImage(final ByteSource p0, final Map<String, Object> p1) throws ImageReadException, IOException;
    
    public final BufferedImage getBufferedImage(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return this.getBufferedImage(new ByteSourceArray(array), map);
    }
    
    protected BufferedImageFactory getBufferedImageFactory(final Map<String, Object> map) {
        if (map == null) {
            return new SimpleBufferedImageFactory();
        }
        final BufferedImageFactory bufferedImageFactory = map.get("BUFFERED_IMAGE_FACTORY");
        if (bufferedImageFactory != null) {
            return bufferedImageFactory;
        }
        return new SimpleBufferedImageFactory();
    }
    
    public abstract String getDefaultExtension();
    
    public final FormatCompliance getFormatCompliance(final File file) throws ImageReadException, IOException {
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        return this.getFormatCompliance(new ByteSourceFile(file));
    }
    
    public FormatCompliance getFormatCompliance(final ByteSource byteSource) throws ImageReadException, IOException {
        return null;
    }
    
    public final FormatCompliance getFormatCompliance(final byte[] array) throws ImageReadException, IOException {
        return this.getFormatCompliance(new ByteSourceArray(array));
    }
    
    public final byte[] getICCProfileBytes(final File file) throws ImageReadException, IOException {
        return this.getICCProfileBytes(file, null);
    }
    
    public final byte[] getICCProfileBytes(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getName());
            sb.append(": ");
            sb.append(file.getName());
            out.println(sb.toString());
        }
        return this.getICCProfileBytes(new ByteSourceFile(file), map);
    }
    
    public abstract byte[] getICCProfileBytes(final ByteSource p0, final Map<String, Object> p1) throws ImageReadException, IOException;
    
    public final byte[] getICCProfileBytes(final byte[] array) throws ImageReadException, IOException {
        return this.getICCProfileBytes(array, null);
    }
    
    public final byte[] getICCProfileBytes(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return this.getICCProfileBytes(new ByteSourceArray(array), map);
    }
    
    public final ImageInfo getImageInfo(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        return this.getImageInfo(new ByteSourceFile(file), map);
    }
    
    public final ImageInfo getImageInfo(final ByteSource byteSource) throws ImageReadException, IOException {
        return this.getImageInfo(byteSource, null);
    }
    
    public abstract ImageInfo getImageInfo(final ByteSource p0, final Map<String, Object> p1) throws ImageReadException, IOException;
    
    public final ImageInfo getImageInfo(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return this.getImageInfo(new ByteSourceArray(array), map);
    }
    
    public final Dimension getImageSize(final File file) throws ImageReadException, IOException {
        return this.getImageSize(file, null);
    }
    
    public final Dimension getImageSize(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        return this.getImageSize(new ByteSourceFile(file), map);
    }
    
    public abstract Dimension getImageSize(final ByteSource p0, final Map<String, Object> p1) throws ImageReadException, IOException;
    
    public final Dimension getImageSize(final byte[] array) throws ImageReadException, IOException {
        return this.getImageSize(array, null);
    }
    
    public final Dimension getImageSize(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return this.getImageSize(new ByteSourceArray(array), map);
    }
    
    public final ImageMetadata getMetadata(final File file) throws ImageReadException, IOException {
        return this.getMetadata(file, null);
    }
    
    public final ImageMetadata getMetadata(final File file, final Map<String, Object> map) throws ImageReadException, IOException {
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getName());
            sb.append(".getMetadata");
            sb.append(": ");
            sb.append(file.getName());
            out.println(sb.toString());
        }
        if (!this.canAcceptExtension(file)) {
            return null;
        }
        return this.getMetadata(new ByteSourceFile(file), map);
    }
    
    public final ImageMetadata getMetadata(final ByteSource byteSource) throws ImageReadException, IOException {
        return this.getMetadata(byteSource, null);
    }
    
    public abstract ImageMetadata getMetadata(final ByteSource p0, final Map<String, Object> p1) throws ImageReadException, IOException;
    
    public final ImageMetadata getMetadata(final byte[] array) throws ImageReadException, IOException {
        return this.getMetadata(array, null);
    }
    
    public final ImageMetadata getMetadata(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        return this.getMetadata(new ByteSourceArray(array), map);
    }
    
    public abstract String getName();
    
    public abstract String getXmpXml(final ByteSource p0, final Map<String, Object> p1) throws ImageReadException, IOException;
    
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> map) throws ImageWriteException, IOException {
        outputStream.close();
        final StringBuilder sb = new StringBuilder();
        sb.append("This image format (");
        sb.append(this.getName());
        sb.append(") cannot be written.");
        throw new ImageWriteException(sb.toString());
    }
}
