package org.apache.commons.imaging.formats.tiff;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffEpTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.datareaders.DataReader;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterBiLevel;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterCieLab;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterCmyk;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterLogLuv;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterPalette;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterRgb;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreterYCbCr;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossy;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffImageParser extends ImageParser {
    private static final String DEFAULT_EXTENSION = ".tif";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION, ".tiff"};

    @Override // org.apache.commons.imaging.ImageParser
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "Tiff-Custom";
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.TIFF};
    }

    @Override // org.apache.commons.imaging.ImageParser
    public byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return new TiffReader(isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0).getFieldValue((TagInfoByte) TiffEpTagConstants.EXIF_TAG_INTER_COLOR_PROFILE, false);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        TiffDirectory tiffDirectory = new TiffReader(isStrict(map)).readFirstDirectory(byteSource, map, false, FormatCompliance.getDefault()).directories.get(0);
        TiffField tiffFieldFindField = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH, true);
        TiffField tiffFieldFindField2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH, true);
        if (tiffFieldFindField == null || tiffFieldFindField2 == null) {
            throw new ImageReadException("TIFF image missing size info.");
        }
        return new Dimension(tiffFieldFindField.getIntValue(), tiffFieldFindField2.getIntValue());
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        FormatCompliance formatCompliance = FormatCompliance.getDefault();
        TiffReader tiffReader = new TiffReader(isStrict(map));
        TiffContents contents = tiffReader.readContents(byteSource, map, formatCompliance);
        List<TiffDirectory> list = contents.directories;
        TiffImageMetadata tiffImageMetadata = new TiffImageMetadata(contents);
        for (TiffDirectory tiffDirectory : list) {
            TiffImageMetadata.Directory directory = new TiffImageMetadata.Directory(tiffReader.getByteOrder(), tiffDirectory);
            Iterator<TiffField> it = tiffDirectory.getDirectoryEntries().iterator();
            while (it.hasNext()) {
                directory.add(it.next());
            }
            tiffImageMetadata.add(directory);
        }
        return tiffImageMetadata;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        int iRound;
        float f;
        int i;
        float f2;
        ImageInfo.CompressionAlgorithm compressionAlgorithm;
        int iRound2;
        float f3;
        TiffContents directories = new TiffReader(isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault());
        TiffDirectory tiffDirectory = directories.directories.get(0);
        TiffField tiffFieldFindField = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH, true);
        TiffField tiffFieldFindField2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH, true);
        if (tiffFieldFindField == null || tiffFieldFindField2 == null) {
            throw new ImageReadException("TIFF image missing size info.");
        }
        int intValue = tiffFieldFindField2.getIntValue();
        int intValue2 = tiffFieldFindField.getIntValue();
        TiffField tiffFieldFindField3 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_RESOLUTION_UNIT);
        int intValue3 = 2;
        if (tiffFieldFindField3 != null && tiffFieldFindField3.getValue() != null) {
            intValue3 = tiffFieldFindField3.getIntValue();
        }
        double d = -1.0d;
        switch (intValue3) {
            case 2:
                d = 1.0d;
                break;
            case 3:
                d = 2.54d;
                break;
        }
        TiffField tiffFieldFindField4 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_XRESOLUTION);
        TiffField tiffFieldFindField5 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_YRESOLUTION);
        if (d > 0.0d) {
            if (tiffFieldFindField4 == null || tiffFieldFindField4.getValue() == null) {
                iRound2 = -1;
                f3 = -1.0f;
            } else {
                double doubleValue = tiffFieldFindField4.getDoubleValue() * d;
                iRound2 = (int) Math.round(doubleValue);
                f3 = (float) (intValue2 / doubleValue);
            }
            if (tiffFieldFindField5 == null || tiffFieldFindField5.getValue() == null) {
                i = iRound2;
                f2 = f3;
                iRound = -1;
                f = -1.0f;
            } else {
                double doubleValue2 = tiffFieldFindField5.getDoubleValue() * d;
                i = iRound2;
                f = (float) (intValue / doubleValue2);
                iRound = (int) Math.round(doubleValue2);
                f2 = f3;
            }
        } else {
            iRound = -1;
            f = -1.0f;
            i = -1;
            f2 = -1.0f;
        }
        TiffField tiffFieldFindField6 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_BITS_PER_SAMPLE);
        int intValueOrArraySum = (tiffFieldFindField6 == null || tiffFieldFindField6.getValue() == null) ? 1 : tiffFieldFindField6.getIntValueOrArraySum();
        ArrayList arrayList = new ArrayList();
        Iterator<TiffField> it = tiffDirectory.entries.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toString());
        }
        ImageFormats imageFormats = ImageFormats.TIFF;
        int size = directories.directories.size();
        String str = "Tiff v." + directories.header.tiffVersion;
        boolean z = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_COLOR_MAP) != null;
        ImageInfo.ColorType colorType = ImageInfo.ColorType.RGB;
        int singleFieldValue = 65535 & tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_COMPRESSION);
        if (singleFieldValue == 32771) {
            compressionAlgorithm = ImageInfo.CompressionAlgorithm.NONE;
        } else if (singleFieldValue != 32773) {
            switch (singleFieldValue) {
                case 1:
                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.NONE;
                    break;
                case 2:
                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.CCITT_1D;
                    break;
                case 3:
                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.CCITT_GROUP_3;
                    break;
                case 4:
                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.CCITT_GROUP_4;
                    break;
                case 5:
                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.LZW;
                    break;
                case 6:
                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.JPEG;
                    break;
                default:
                    compressionAlgorithm = ImageInfo.CompressionAlgorithm.UNKNOWN;
                    break;
            }
        } else {
            compressionAlgorithm = ImageInfo.CompressionAlgorithm.PACKBITS;
        }
        return new ImageInfo(str, intValueOrArraySum, arrayList, imageFormats, "TIFF Tag-based Image File Format", intValue, "image/tiff", size, iRound, f, i, f2, intValue2, false, false, z, colorType, compressionAlgorithm);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        byte[] fieldValue = new TiffReader(isStrict(map)).readDirectories(byteSource, false, FormatCompliance.getDefault()).directories.get(0).getFieldValue(TiffTagConstants.TIFF_TAG_XMP, false);
        if (fieldValue == null) {
            return null;
        }
        try {
            return new String(fieldValue, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ImageReadException("Invalid JPEG XMP Segment.", e);
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        try {
            printWriter.println("tiff.dumpImageFile");
            ImageInfo imageInfo = getImageInfo(byteSource);
            if (imageInfo != null) {
                imageInfo.toString(printWriter, "");
                printWriter.println("");
                List<TiffDirectory> list = new TiffReader(true).readContents(byteSource, null, FormatCompliance.getDefault()).directories;
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        List<TiffField> list2 = list.get(i).entries;
                        if (list2 != null) {
                            Iterator<TiffField> it = list2.iterator();
                            while (it.hasNext()) {
                                it.next().dump(printWriter, Integer.toString(i));
                            }
                        }
                    }
                    printWriter.println("");
                    return true;
                }
            }
            return false;
        } finally {
            printWriter.println("");
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws IOException, ImageReadException {
        FormatCompliance formatCompliance = FormatCompliance.getDefault();
        new TiffReader(isStrict(null)).readContents(byteSource, null, formatCompliance);
        return formatCompliance;
    }

    public List<byte[]> collectRawImageData(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        TiffContents directories = new TiffReader(isStrict(map)).readDirectories(byteSource, true, FormatCompliance.getDefault());
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < directories.directories.size(); i++) {
            for (TiffDirectory.ImageDataElement imageDataElement : directories.directories.get(i).getTiffRawImageDataElements()) {
                arrayList.add(byteSource.getBlock(imageDataElement.offset, imageDataElement.length));
            }
        }
        return arrayList;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        FormatCompliance formatCompliance = FormatCompliance.getDefault();
        TiffReader tiffReader = new TiffReader(isStrict(map));
        TiffContents firstDirectory = tiffReader.readFirstDirectory(byteSource, map, true, formatCompliance);
        BufferedImage tiffImage = firstDirectory.directories.get(0).getTiffImage(tiffReader.getByteOrder(), map);
        if (tiffImage == null) {
            throw new ImageReadException("TIFF does not contain an image.");
        }
        return tiffImage;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public List<BufferedImage> getAllBufferedImages(ByteSource byteSource) throws IOException, ImageReadException {
        FormatCompliance formatCompliance = FormatCompliance.getDefault();
        TiffReader tiffReader = new TiffReader(true);
        TiffContents directories = tiffReader.readDirectories(byteSource, true, formatCompliance);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < directories.directories.size(); i++) {
            BufferedImage tiffImage = directories.directories.get(i).getTiffImage(tiffReader.getByteOrder(), null);
            if (tiffImage != null) {
                arrayList.add(tiffImage);
            }
        }
        return arrayList;
    }

    private Integer getIntegerParameter(String str, Map<String, Object> map) throws ImageReadException {
        if (map == null || !map.containsKey(str)) {
            return null;
        }
        Object obj = map.get(str);
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        throw new ImageReadException("Non-Integer parameter " + str);
    }

    private Rectangle checkForSubImage(Map<String, Object> map) throws ImageReadException {
        Integer integerParameter = getIntegerParameter(TiffConstants.PARAM_KEY_SUBIMAGE_X, map);
        Integer integerParameter2 = getIntegerParameter(TiffConstants.PARAM_KEY_SUBIMAGE_Y, map);
        Integer integerParameter3 = getIntegerParameter(TiffConstants.PARAM_KEY_SUBIMAGE_WIDTH, map);
        Integer integerParameter4 = getIntegerParameter(TiffConstants.PARAM_KEY_SUBIMAGE_HEIGHT, map);
        if (integerParameter == null && integerParameter2 == null && integerParameter3 == null && integerParameter4 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(32);
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
            sb.setLength(sb.length() - 1);
            throw new ImageReadException("Incomplete subimage parameters, missing" + sb.toString());
        }
        return new Rectangle(integerParameter.intValue(), integerParameter2.intValue(), integerParameter3.intValue(), integerParameter4.intValue());
    }

    protected BufferedImage getBufferedImage(TiffDirectory tiffDirectory, ByteOrder byteOrder, Map<String, Object> map) throws IOException, ImageReadException {
        int[] intArrayValue;
        int intValueOrArraySum;
        if (tiffDirectory.entries == null) {
            throw new ImageReadException("TIFF missing entries");
        }
        int singleFieldValue = 65535 & tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_PHOTOMETRIC_INTERPRETATION);
        int singleFieldValue2 = 65535 & tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_COMPRESSION);
        int singleFieldValue3 = tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH);
        int singleFieldValue4 = tiffDirectory.getSingleFieldValue(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH);
        Rectangle rectangleCheckForSubImage = checkForSubImage(map);
        if (rectangleCheckForSubImage != null) {
            if (rectangleCheckForSubImage.width <= 0) {
                throw new ImageReadException("negative or zero subimage width");
            }
            if (rectangleCheckForSubImage.height <= 0) {
                throw new ImageReadException("negative or zero subimage height");
            }
            if (rectangleCheckForSubImage.x < 0 || rectangleCheckForSubImage.x >= singleFieldValue3) {
                throw new ImageReadException("subimage x is outside raster");
            }
            if (rectangleCheckForSubImage.x + rectangleCheckForSubImage.width > singleFieldValue3) {
                throw new ImageReadException("subimage (x+width) is outside raster");
            }
            if (rectangleCheckForSubImage.y < 0 || rectangleCheckForSubImage.y >= singleFieldValue4) {
                throw new ImageReadException("subimage y is outside raster");
            }
            if (rectangleCheckForSubImage.y + rectangleCheckForSubImage.height > singleFieldValue4) {
                throw new ImageReadException("subimage (y+height) is outside raster");
            }
            if (rectangleCheckForSubImage.x == 0 && rectangleCheckForSubImage.y == 0 && rectangleCheckForSubImage.width == singleFieldValue3 && rectangleCheckForSubImage.height == singleFieldValue4) {
                rectangleCheckForSubImage = null;
            }
        }
        Rectangle rectangle = rectangleCheckForSubImage;
        TiffField tiffFieldFindField = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_SAMPLES_PER_PIXEL);
        int intValue = tiffFieldFindField != null ? tiffFieldFindField.getIntValue() : 1;
        int[] iArr = {1};
        TiffField tiffFieldFindField2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_BITS_PER_SAMPLE);
        if (tiffFieldFindField2 != null) {
            intArrayValue = tiffFieldFindField2.getIntArrayValue();
            intValueOrArraySum = tiffFieldFindField2.getIntValueOrArraySum();
        } else {
            intArrayValue = iArr;
            intValueOrArraySum = intValue;
        }
        TiffField tiffFieldFindField3 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_PREDICTOR);
        int intValueOrArraySum2 = tiffFieldFindField3 != null ? tiffFieldFindField3.getIntValueOrArraySum() : -1;
        if (intValue != intArrayValue.length) {
            throw new ImageReadException("Tiff: samplesPerPixel (" + intValue + ")!=fBitsPerSample.length (" + intArrayValue.length + ")");
        }
        int i = intValueOrArraySum;
        int i2 = intValue;
        DataReader dataReader = tiffDirectory.getTiffImageData().getDataReader(tiffDirectory, getPhotometricInterpreter(tiffDirectory, singleFieldValue, i, intArrayValue, intValueOrArraySum2, i2, singleFieldValue3, singleFieldValue4), i, intArrayValue, intValueOrArraySum2, i2, singleFieldValue3, singleFieldValue4, singleFieldValue2, byteOrder);
        if (rectangle != null) {
            return dataReader.readImageData(rectangle);
        }
        ImageBuilder imageBuilder = new ImageBuilder(singleFieldValue3, singleFieldValue4, false);
        dataReader.readImageData(imageBuilder);
        return imageBuilder.getBufferedImage();
    }

    private PhotometricInterpreter getPhotometricInterpreter(TiffDirectory tiffDirectory, int i, int i2, int[] iArr, int i3, int i4, int i5, int i6) throws ImageReadException {
        switch (i) {
            case 0:
            case 1:
                return new PhotometricInterpreterBiLevel(i4, iArr, i3, i5, i6, i == 0);
            case 2:
                return new PhotometricInterpreterRgb(i4, iArr, i3, i5, i6);
            case 3:
                int[] intArrayValue = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_COLOR_MAP, true).getIntArrayValue();
                int i7 = 3 * (1 << i2);
                if (intArrayValue.length != i7) {
                    throw new ImageReadException("Tiff: fColorMap.length (" + intArrayValue.length + ")!=expectedColormapSize (" + i7 + ")");
                }
                return new PhotometricInterpreterPalette(i4, iArr, i3, i5, i6, intArrayValue);
            case 5:
                return new PhotometricInterpreterCmyk(i4, iArr, i3, i5, i6);
            case 6:
                return new PhotometricInterpreterYCbCr(i4, iArr, i3, i5, i6);
            case 8:
                return new PhotometricInterpreterCieLab(i4, iArr, i3, i5, i6);
            case TiffTagConstants.PHOTOMETRIC_INTERPRETATION_VALUE_PIXAR_LOG_L /* 32844 */:
            case TiffTagConstants.PHOTOMETRIC_INTERPRETATION_VALUE_PIXAR_LOG_LUV /* 32845 */:
                return new PhotometricInterpreterLogLuv(i4, iArr, i3, i5, i6);
            default:
                throw new ImageReadException("TIFF: Unknown fPhotometricInterpretation: " + i);
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        new TiffImageWriterLossy().writeImage(bufferedImage, outputStream, map);
    }
}
