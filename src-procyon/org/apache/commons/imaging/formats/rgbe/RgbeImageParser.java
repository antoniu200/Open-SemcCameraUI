// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.rgbe;

import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageInfo;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.Raster;
import java.awt.Point;
import java.awt.image.BandedSampleModel;
import java.awt.image.ComponentColorModel;
import java.awt.color.ColorSpace;
import java.awt.image.DataBufferFloat;
import java.awt.image.BufferedImage;
import java.util.Map;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class RgbeImageParser extends ImageParser
{
    public RgbeImageParser() {
        this.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return new String[] { ".hdr", ".pic" };
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.RGBE };
    }
    
    @Override
    public BufferedImage getBufferedImage(ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        byteSource = (ByteSource)new RgbeInfo(byteSource);
        try {
            final DataBufferFloat db = new DataBufferFloat(((RgbeInfo)byteSource).getPixelData(), ((RgbeInfo)byteSource).getWidth() * ((RgbeInfo)byteSource).getHeight());
            final BufferedImage bufferedImage = new BufferedImage(new ComponentColorModel(ColorSpace.getInstance(1000), false, false, 1, db.getDataType()), Raster.createWritableRaster(new BandedSampleModel(db.getDataType(), ((RgbeInfo)byteSource).getWidth(), ((RgbeInfo)byteSource).getHeight(), 3), db, new Point()), false, null);
            IoUtils.closeQuietly(true, (Closeable)byteSource);
            return bufferedImage;
        }
        finally {
            IoUtils.closeQuietly(false, (Closeable)byteSource);
        }
    }
    
    @Override
    public String getDefaultExtension() {
        return ".hdr";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        byteSource = (ByteSource)new RgbeInfo(byteSource);
        try {
            final ImageInfo imageInfo = new ImageInfo(this.getName(), 32, new ArrayList<String>(), ImageFormats.RGBE, this.getName(), ((RgbeInfo)byteSource).getHeight(), "image/vnd.radiance", 1, -1, -1.0f, -1, -1.0f, ((RgbeInfo)byteSource).getWidth(), false, false, false, ImageInfo.ColorType.RGB, ImageInfo.CompressionAlgorithm.ADAPTIVE_RLE);
            IoUtils.closeQuietly(true, (Closeable)byteSource);
            return imageInfo;
        }
        finally {
            IoUtils.closeQuietly(false, (Closeable)byteSource);
        }
    }
    
    @Override
    public Dimension getImageSize(ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        byteSource = (ByteSource)new RgbeInfo(byteSource);
        try {
            final Dimension dimension = new Dimension(((RgbeInfo)byteSource).getWidth(), ((RgbeInfo)byteSource).getHeight());
            IoUtils.closeQuietly(true, (Closeable)byteSource);
            return dimension;
        }
        finally {
            IoUtils.closeQuietly(false, (Closeable)byteSource);
        }
    }
    
    @Override
    public ImageMetadata getMetadata(ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        byteSource = (ByteSource)new RgbeInfo(byteSource);
        try {
            final ImageMetadata metadata = ((RgbeInfo)byteSource).getMetadata();
            IoUtils.closeQuietly(true, (Closeable)byteSource);
            return metadata;
        }
        finally {
            IoUtils.closeQuietly(false, (Closeable)byteSource);
        }
    }
    
    @Override
    public String getName() {
        return "Radiance HDR";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
}
