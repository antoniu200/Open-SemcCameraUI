package org.apache.commons.imaging.formats.rgbe;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferFloat;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RgbeImageParser extends ImageParser {
    @Override // org.apache.commons.imaging.ImageParser
    public String getDefaultExtension() {
        return ".hdr";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "Radiance HDR";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    public RgbeImageParser() {
        setByteOrder(ByteOrder.BIG_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return new String[]{".hdr", ".pic"};
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.RGBE};
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        RgbeInfo rgbeInfo = new RgbeInfo(byteSource);
        try {
            ImageMetadata metadata = rgbeInfo.getMetadata();
            IoUtils.closeQuietly(true, rgbeInfo);
            return metadata;
        } catch (Throwable th) {
            IoUtils.closeQuietly(false, rgbeInfo);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        RgbeInfo rgbeInfo = new RgbeInfo(byteSource);
        try {
            ImageInfo imageInfo = new ImageInfo(getName(), 32, new ArrayList(), ImageFormats.RGBE, getName(), rgbeInfo.getHeight(), "image/vnd.radiance", 1, -1, -1.0f, -1, -1.0f, rgbeInfo.getWidth(), false, false, false, ImageInfo.ColorType.RGB, ImageInfo.CompressionAlgorithm.ADAPTIVE_RLE);
            IoUtils.closeQuietly(true, rgbeInfo);
            return imageInfo;
        } catch (Throwable th) {
            IoUtils.closeQuietly(false, rgbeInfo);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        RgbeInfo rgbeInfo = new RgbeInfo(byteSource);
        try {
            DataBufferFloat dataBufferFloat = new DataBufferFloat(rgbeInfo.getPixelData(), rgbeInfo.getWidth() * rgbeInfo.getHeight());
            BufferedImage bufferedImage = new BufferedImage(new ComponentColorModel(ColorSpace.getInstance(1000), false, false, 1, dataBufferFloat.getDataType()), Raster.createWritableRaster(new BandedSampleModel(dataBufferFloat.getDataType(), rgbeInfo.getWidth(), rgbeInfo.getHeight(), 3), dataBufferFloat, new Point()), false, (Hashtable) null);
            IoUtils.closeQuietly(true, rgbeInfo);
            return bufferedImage;
        } catch (Throwable th) {
            IoUtils.closeQuietly(false, rgbeInfo);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        RgbeInfo rgbeInfo = new RgbeInfo(byteSource);
        try {
            Dimension dimension = new Dimension(rgbeInfo.getWidth(), rgbeInfo.getHeight());
            IoUtils.closeQuietly(true, rgbeInfo);
            return dimension;
        } catch (Throwable th) {
            IoUtils.closeQuietly(false, rgbeInfo);
            throw th;
        }
    }
}
