// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossy;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.constants.TiffEpTagConstants;
import org.apache.commons.imaging.formats.tiff.datareaders.DataReader;
import org.apache.commons.imaging.common.ImageBuilder;
import java.nio.ByteOrder;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageInfo;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.commons.imaging.FormatCompliance;
import java.util.List;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterBiLevel;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterRgb;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterPalette;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterCmyk;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterYCbCr;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterCieLab;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterLogLuv;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;
import org.apache.commons.imaging.ImageReadException;
import java.awt.Rectangle;
import java.util.Map;
import org.apache.commons.imaging.ImageParser;

public class TiffImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".tif";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".tif", ".tiff" };
    }
    
    private Rectangle checkForSubImage(final Map<String, Object> map) throws ImageReadException {
        final Integer integerParameter = this.getIntegerParameter("SUBIMAGE_X", map);
        final Integer integerParameter2 = this.getIntegerParameter("SUBIMAGE_Y", map);
        final Integer integerParameter3 = this.getIntegerParameter("SUBIMAGE_WIDTH", map);
        final Integer integerParameter4 = this.getIntegerParameter("SUBIMAGE_HEIGHT", map);
        if (integerParameter == null && integerParameter2 == null && integerParameter3 == null && integerParameter4 == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(32);
        if (integerParameter == null) {
            sb.append(" x0,");
        }
        if (integerParameter2 == null) {
            sb.append(" y0,");
        }
        if (integerParameter3 == null) {
            sb.append(" width,");
        }
        if (integerParameter4 == null) {
            sb.append(" height,");
        }
        if (sb.length() > 0) {
            sb.setLength();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Incomplete subimage parameters, missing");
            sb2.append(sb.toString());
            throw new ImageReadException(sb2.toString());
        }
        return new Rectangle(integerParameter, integerParameter2, integerParameter3, integerParameter4);
    }
    
    private Integer getIntegerParameter(final String str, final Map<String, Object> map) throws ImageReadException {
        if (map == null) {
            return null;
        }
        if (!map.containsKey(str)) {
            return null;
        }
        final Integer value = map.get(str);
        if (value instanceof Integer) {
            return value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Non-Integer parameter ");
        sb.append(str);
        throw new ImageReadException(sb.toString());
    }
    
    private PhotometricInterpreter getPhotometricInterpreter(final TiffDirectory tiffDirectory, int n, final int n2, final int[] array, final int n3, final int n4, final int n5, final int n6) throws ImageReadException {
        boolean b = true;
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("TIFF: Unknown fPhotometricInterpretation: ");
                sb.append(n);
                throw new ImageReadException(sb.toString());
            }
            case 32844:
            case 32845: {
                return new PhotometricInterpreterLogLuv(n4, array, n3, n5, n6);
            }
            case 8: {
                return new PhotometricInterpreterCieLab(n4, array, n3, n5, n6);
            }
            case 6: {
                return new PhotometricInterpreterYCbCr(n4, array, n3, n5, n6);
            }
            case 5: {
                return new PhotometricInterpreterCmyk(n4, array, n3, n5, n6);
            }
            case 3: {
                final int[] intArrayValue = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_COLOR_MAP, true).getIntArrayValue();
                n = 3 * (1 << n2);
                if (intArrayValue.length != n) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Tiff: fColorMap.length (");
                    sb2.append(intArrayValue.length);
                    sb2.append(")!=expectedColormapSize (");
                    sb2.append(n);
                    sb2.append(")");
                    throw new ImageReadException(sb2.toString());
                }
                return new PhotometricInterpreterPalette(n4, array, n3, n5, n6, intArrayValue);
            }
            case 2: {
                return new PhotometricInterpreterRgb(n4, array, n3, n5, n6);
            }
            case 0:
            case 1: {
                if (n != 0) {
                    b = false;
                }
                return new PhotometricInterpreterBiLevel(n4, array, n3, n5, n6, b);
            }
        }
    }
    
    public List<byte[]> collectRawImageData(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final TiffContents directories = new TiffReader(ImageParser.isStrict(map)).readDirectories(byteSource, true, FormatCompliance.getDefault());
        final ArrayList list = new ArrayList();
        for (int i = 0; i < directories.directories.size(); ++i) {
            for (final TiffDirectory.ImageDataElement imageDataElement : directories.directories.get(i).getTiffRawImageDataElements()) {
                list.add(byteSource.getBlock(imageDataElement.offset, imageDataElement.length));
            }
        }
        return list;
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        try {
            printWriter.println("tiff.dumpImageFile");
            final ImageInfo imageInfo = this.getImageInfo(byteSource);
            if (imageInfo != null) {
                imageInfo.toString(printWriter, "");
                printWriter.println("");
                final List<TiffDirectory> directories = new TiffReader(true).readContents(byteSource, null, FormatCompliance.getDefault()).directories;
                if (directories != null) {
                    for (int i = 0; i < directories.size(); ++i) {
                        final List<TiffField> entries = directories.get(i).entries;
                        if (entries == null) {
                            return false;
                        }
                        final Iterator<TiffField> iterator = entries.iterator();
                        while (iterator.hasNext()) {
                            iterator.next().dump(printWriter, Integer.toString(i));
                        }
                    }
                    printWriter.println("");
                    return true;
                }
            }
            return false;
        }
        finally {
            printWriter.println("");
        }
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return TiffImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.TIFF };
    }
    
    @Override
    public List<BufferedImage> getAllBufferedImages(final ByteSource byteSource) throws ImageReadException, IOException {
        final FormatCompliance default1 = FormatCompliance.getDefault();
        final TiffReader tiffReader = new TiffReader(true);
        final TiffContents directories = tiffReader.readDirectories(byteSource, true, default1);
        final ArrayList list = new ArrayList();
        for (int i = 0; i < directories.directories.size(); ++i) {
            final BufferedImage tiffImage = directories.directories.get(i).getTiffImage(tiffReader.getByteOrder(), null);
            if (tiffImage != null) {
                list.add(tiffImage);
            }
        }
        return list;
    }
    
    @Override
    public BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final FormatCompliance default1 = FormatCompliance.getDefault();
        final TiffReader tiffReader = new TiffReader(ImageParser.isStrict(map));
        final BufferedImage tiffImage = tiffReader.readFirstDirectory(byteSource, map, true, default1).directories.get(0).getTiffImage(tiffReader.getByteOrder(), map);
        if (tiffImage == null) {
            throw new ImageReadException("TIFF does not contain an image.");
        }
        return tiffImage;
    }
    
    protected BufferedImage getBufferedImage(final TiffDirectory tiffDirectory, final ByteOrder byteOrder, final Map<String, Object> map) throws ImageReadException, IOException {
        if (tiffDirectory.entries == null) {
            throw new ImageReadException("TIFF missing entries");
        }
        final short singleFieldValue = tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_PHOTOMETRIC_INTERPRETATION);
        final short singleFieldValue2 = tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_COMPRESSION);
        final int singleFieldValue3 = tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH);
        final int singleFieldValue4 = tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH);
        final Rectangle checkForSubImage = this.checkForSubImage(map);
        Rectangle rectangle;
        if ((rectangle = checkForSubImage) != null) {
            if (checkForSubImage.width <= 0) {
                throw new ImageReadException("negative or zero subimage width");
            }
            if (checkForSubImage.height <= 0) {
                throw new ImageReadException("negative or zero subimage height");
            }
            if (checkForSubImage.x < 0 || checkForSubImage.x >= singleFieldValue3) {
                throw new ImageReadException("subimage x is outside raster");
            }
            if (checkForSubImage.x + checkForSubImage.width > singleFieldValue3) {
                throw new ImageReadException("subimage (x+width) is outside raster");
            }
            if (checkForSubImage.y < 0 || checkForSubImage.y >= singleFieldValue4) {
                throw new ImageReadException("subimage y is outside raster");
            }
            if (checkForSubImage.y + checkForSubImage.height > singleFieldValue4) {
                throw new ImageReadException("subimage (y+height) is outside raster");
            }
            rectangle = checkForSubImage;
            if (checkForSubImage.x == 0) {
                rectangle = checkForSubImage;
                if (checkForSubImage.y == 0) {
                    rectangle = checkForSubImage;
                    if (checkForSubImage.width == singleFieldValue3) {
                        rectangle = checkForSubImage;
                        if (checkForSubImage.height == singleFieldValue4) {
                            rectangle = null;
                        }
                    }
                }
            }
        }
        final TiffField field = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_SAMPLES_PER_PIXEL);
        int intValue;
        if (field != null) {
            intValue = field.getIntValue();
        }
        else {
            intValue = 1;
        }
        final TiffField field2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_BITS_PER_SAMPLE);
        int[] intArrayValue;
        int intValueOrArraySum;
        if (field2 != null) {
            intArrayValue = field2.getIntArrayValue();
            intValueOrArraySum = field2.getIntValueOrArraySum();
        }
        else {
            intArrayValue = new int[] { 1 };
            intValueOrArraySum = intValue;
        }
        int intValueOrArraySum2 = -1;
        final TiffField field3 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_PREDICTOR);
        if (field3 != null) {
            intValueOrArraySum2 = field3.getIntValueOrArraySum();
        }
        if (intValue != intArrayValue.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tiff: samplesPerPixel (");
            sb.append(intValue);
            sb.append(")!=fBitsPerSample.length (");
            sb.append(intArrayValue.length);
            sb.append(")");
            throw new ImageReadException(sb.toString());
        }
        final DataReader dataReader = tiffDirectory.getTiffImageData().getDataReader(tiffDirectory, this.getPhotometricInterpreter(tiffDirectory, 0xFFFF & singleFieldValue, intValueOrArraySum, intArrayValue, intValueOrArraySum2, intValue, singleFieldValue3, singleFieldValue4), intValueOrArraySum, intArrayValue, intValueOrArraySum2, intValue, singleFieldValue3, singleFieldValue4, 0xFFFF & singleFieldValue2, byteOrder);
        BufferedImage bufferedImage;
        if (rectangle != null) {
            bufferedImage = dataReader.readImageData(rectangle);
        }
        else {
            final ImageBuilder imageBuilder = new ImageBuilder(singleFieldValue3, singleFieldValue4, false);
            dataReader.readImageData(imageBuilder);
            bufferedImage = imageBuilder.getBufferedImage();
        }
        return bufferedImage;
    }
    
    @Override
    public String getDefaultExtension() {
        return ".tif";
    }
    
    @Override
    public FormatCompliance getFormatCompliance(final ByteSource byteSource) throws ImageReadException, IOException {
        final FormatCompliance default1 = FormatCompliance.getDefault();
        new TiffReader(ImageParser.isStrict(null)).readContents(byteSource, null, default1);
        return default1;
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return new TiffReader(ImageParser.isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0).getFieldValue(TiffEpTagConstants.EXIF_TAG_INTER_COLOR_PROFILE, false);
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final TiffContents directories = new TiffReader(ImageParser.isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault());
        final TiffDirectory tiffDirectory = directories.directories.get(0);
        final TiffField field = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH, true);
        final TiffField field2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH, true);
        if (field == null || field2 == null) {
            throw new ImageReadException("TIFF image missing size info.");
        }
        final int intValue = field2.getIntValue();
        final int intValue2 = field.getIntValue();
        final TiffField field3 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_RESOLUTION_UNIT);
        int intValue3 = 2;
        if (field3 != null) {
            intValue3 = intValue3;
            if (field3.getValue() != null) {
                intValue3 = field3.getIntValue();
            }
        }
        double n2;
        final double n = n2 = -1.0;
        while (true) {
            switch (intValue3) {
                default: {
                    n2 = n;
                }
                case 1: {
                    final TiffField field4 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_XRESOLUTION);
                    final TiffField field5 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_YRESOLUTION);
                    int n7;
                    int n8;
                    float n9;
                    float n10;
                    if (n2 > 0.0) {
                        int n3;
                        float n4;
                        if (field4 != null && field4.getValue() != null) {
                            final double a = field4.getDoubleValue() * n2;
                            n3 = (int)Math.round(a);
                            n4 = (float)(intValue2 / a);
                        }
                        else {
                            n3 = -1;
                            n4 = -1.0f;
                        }
                        if (field5 != null && field5.getValue() != null) {
                            final double a2 = field5.getDoubleValue() * n2;
                            final int n5 = (int)Math.round(a2);
                            final float n6 = (float)(intValue / a2);
                            n7 = n3;
                            n8 = n5;
                            n9 = n4;
                            n10 = n6;
                        }
                        else {
                            n7 = n3;
                            n9 = n4;
                            n8 = -1;
                            n10 = -1.0f;
                        }
                    }
                    else {
                        n8 = -1;
                        n10 = -1.0f;
                        n7 = -1;
                        n9 = -1.0f;
                    }
                    final TiffField field6 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_BITS_PER_SAMPLE);
                    int intValueOrArraySum;
                    if (field6 != null && field6.getValue() != null) {
                        intValueOrArraySum = field6.getIntValueOrArraySum();
                    }
                    else {
                        intValueOrArraySum = 1;
                    }
                    final ArrayList list = new ArrayList();
                    final Iterator<TiffField> iterator = tiffDirectory.entries.iterator();
                    while (iterator.hasNext()) {
                        list.add(iterator.next().toString());
                    }
                    final ImageFormats tiff = ImageFormats.TIFF;
                    final int size = directories.directories.size();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Tiff v.");
                    sb.append(directories.header.tiffVersion);
                    final String string = sb.toString();
                    final boolean b = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_COLOR_MAP) != null;
                    final ImageInfo.ColorType rgb = ImageInfo.ColorType.RGB;
                    final int n11 = 0xFFFF & tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_COMPRESSION);
                    ImageInfo.CompressionAlgorithm compressionAlgorithm = null;
                    if (n11 != 32771) {
                        if (n11 != 32773) {
                            switch (n11) {
                                default: {
                                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.UNKNOWN;
                                    break;
                                }
                                case 6: {
                                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.JPEG;
                                    break;
                                }
                                case 5: {
                                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.LZW;
                                    break;
                                }
                                case 4: {
                                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.CCITT_GROUP_4;
                                    break;
                                }
                                case 3: {
                                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.CCITT_GROUP_3;
                                    break;
                                }
                                case 2: {
                                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.CCITT_1D;
                                    break;
                                }
                                case 1: {
                                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.NONE;
                                    break;
                                }
                            }
                        }
                        else {
                            compressionAlgorithm = ImageInfo.CompressionAlgorithm.PACKBITS;
                        }
                    }
                    else {
                        compressionAlgorithm = ImageInfo.CompressionAlgorithm.NONE;
                    }
                    return new ImageInfo(string, intValueOrArraySum, list, tiff, "TIFF Tag-based Image File Format", intValue, "image/tiff", size, n8, n10, n7, n9, intValue2, false, false, b, rgb, compressionAlgorithm);
                }
                case 3: {
                    n2 = 2.54;
                    continue;
                }
                case 2: {
                    n2 = 1.0;
                    continue;
                }
            }
            break;
        }
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final TiffDirectory tiffDirectory = new TiffReader(ImageParser.isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0);
        final TiffField field = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH, true);
        final TiffField field2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH, true);
        if (field != null && field2 != null) {
            return new Dimension(field.getIntValue(), field2.getIntValue());
        }
        throw new ImageReadException("TIFF image missing size info.");
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final FormatCompliance default1 = FormatCompliance.getDefault();
        final TiffReader tiffReader = new TiffReader(ImageParser.isStrict(map));
        final TiffContents contents = tiffReader.readContents(byteSource, map, default1);
        final List<TiffDirectory> directories = contents.directories;
        final TiffImageMetadata tiffImageMetadata = new TiffImageMetadata(contents);
        for (final TiffDirectory tiffDirectory : directories) {
            final TiffImageMetadata.Directory directory = new TiffImageMetadata.Directory(tiffReader.getByteOrder(), tiffDirectory);
            final Iterator<TiffField> iterator2 = tiffDirectory.getDirectoryEntries().iterator();
            while (iterator2.hasNext()) {
                directory.add(iterator2.next());
            }
            tiffImageMetadata.add(directory);
        }
        return tiffImageMetadata;
    }
    
    @Override
    public String getName() {
        return "Tiff-Custom";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final byte[] fieldValue = new TiffReader(ImageParser.isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault()).directories.get(0).getFieldValue(TiffTagConstants.TIFF_TAG_XMP, false);
        if (fieldValue == null) {
            return null;
        }
        try {
            return new String(fieldValue, "utf-8");
        }
        catch (final UnsupportedEncodingException ex) {
            throw new ImageReadException("Invalid JPEG XMP Segment.", ex);
        }
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> map) throws ImageWriteException, IOException {
        new TiffImageWriterLossy().writeImage(bufferedImage, outputStream, map);
    }
}
