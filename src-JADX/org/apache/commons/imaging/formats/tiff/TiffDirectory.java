package org.apache.commons.imaging.formats.tiff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoDouble;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoFloat;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoGpsText;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffDirectory extends TiffElement {
    public final List<TiffField> entries;
    private JpegImageData jpegImageData;
    public final long nextDirectoryOffset;
    private TiffImageData tiffImageData;
    public final int type;

    public static String description(int i) {
        switch (i) {
            case -4:
                return "Interoperability";
            case -3:
                return "Gps";
            case -2:
                return "Exif";
            case -1:
                return "Unknown";
            case 0:
                return "Root";
            case 1:
                return "Sub";
            case 2:
                return "Thumbnail";
            default:
                return "Bad Type";
        }
    }

    public TiffDirectory(int i, List<TiffField> list, long j, long j2) {
        super(j, 2 + (list.size() * 12) + 4);
        this.type = i;
        this.entries = Collections.unmodifiableList(list);
        this.nextDirectoryOffset = j2;
    }

    public String description() {
        return description(this.type);
    }

    @Override // org.apache.commons.imaging.formats.tiff.TiffElement
    public String getElementDescription(boolean z) {
        if (!z) {
            return "TIFF Directory (" + description() + ")";
        }
        long j = this.offset + 2;
        StringBuilder sb = new StringBuilder();
        for (TiffField tiffField : this.entries) {
            sb.append(String.format("\t[%d]: %s (%d, 0x%x), %s, %d: %s%n", Long.valueOf(j), tiffField.getTagInfo().name, Integer.valueOf(tiffField.getTag()), Integer.valueOf(tiffField.getTag()), tiffField.getFieldType().getName(), Integer.valueOf(tiffField.getBytesLength()), tiffField.getValueDescription()));
            j += 12;
        }
        return sb.toString();
    }

    public List<TiffField> getDirectoryEntries() {
        return new ArrayList(this.entries);
    }

    public void dump() {
        Iterator<TiffField> it = this.entries.iterator();
        while (it.hasNext()) {
            it.next().dump();
        }
    }

    public boolean hasJpegImageData() throws ImageReadException {
        return findField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT) != null;
    }

    public boolean hasTiffImageData() throws ImageReadException {
        return (findField(TiffTagConstants.TIFF_TAG_TILE_OFFSETS) == null && findField(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS) == null) ? false : true;
    }

    public BufferedImage getTiffImage(ByteOrder byteOrder) throws IOException, ImageReadException {
        return getTiffImage(byteOrder, null);
    }

    public BufferedImage getTiffImage(ByteOrder byteOrder, Map<String, Object> map) throws IOException, ImageReadException {
        if (this.tiffImageData == null) {
            return null;
        }
        return new TiffImageParser().getBufferedImage(this, byteOrder, map);
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        return findField(tagInfo, false);
    }

    public TiffField findField(TagInfo tagInfo, boolean z) throws ImageReadException {
        if (this.entries == null) {
            return null;
        }
        for (TiffField tiffField : this.entries) {
            if (tiffField.getTag() == tagInfo.tag) {
                return tiffField;
            }
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Missing expected field: " + tagInfo.getDescription());
    }

    public Object getFieldValue(TagInfo tagInfo) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfo);
        if (tiffFieldFindField == null) {
            return null;
        }
        return tiffFieldFindField.getValue();
    }

    public byte getSingleFieldValue(TagInfoByte tagInfoByte) throws ImageReadException {
        byte[] fieldValue = getFieldValue(tagInfoByte, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoByte.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public String getSingleFieldValue(TagInfoAscii tagInfoAscii) throws ImageReadException {
        String[] fieldValue = getFieldValue(tagInfoAscii, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoAscii.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public short getSingleFieldValue(TagInfoShort tagInfoShort) throws ImageReadException {
        short[] fieldValue = getFieldValue(tagInfoShort, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoShort.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public int getSingleFieldValue(TagInfoLong tagInfoLong) throws ImageReadException {
        int[] fieldValue = getFieldValue(tagInfoLong, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoLong.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public int getSingleFieldValue(TagInfoShortOrLong tagInfoShortOrLong) throws ImageReadException {
        int[] fieldValue = getFieldValue(tagInfoShortOrLong, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoShortOrLong.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public RationalNumber getSingleFieldValue(TagInfoRational tagInfoRational) throws ImageReadException {
        RationalNumber[] fieldValue = getFieldValue(tagInfoRational, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoRational.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public byte getSingleFieldValue(TagInfoSByte tagInfoSByte) throws ImageReadException {
        byte[] fieldValue = getFieldValue(tagInfoSByte, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoSByte.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public short getSingleFieldValue(TagInfoSShort tagInfoSShort) throws ImageReadException {
        short[] fieldValue = getFieldValue(tagInfoSShort, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoSShort.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public int getSingleFieldValue(TagInfoSLong tagInfoSLong) throws ImageReadException {
        int[] fieldValue = getFieldValue(tagInfoSLong, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoSLong.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public RationalNumber getSingleFieldValue(TagInfoSRational tagInfoSRational) throws ImageReadException {
        RationalNumber[] fieldValue = getFieldValue(tagInfoSRational, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoSRational.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public float getSingleFieldValue(TagInfoFloat tagInfoFloat) throws ImageReadException {
        float[] fieldValue = getFieldValue(tagInfoFloat, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoFloat.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public double getSingleFieldValue(TagInfoDouble tagInfoDouble) throws ImageReadException {
        double[] fieldValue = getFieldValue(tagInfoDouble, true);
        if (fieldValue.length != 1) {
            throw new ImageReadException("Field \"" + tagInfoDouble.name + "\" has incorrect length " + fieldValue.length);
        }
        return fieldValue[0];
    }

    public byte[] getFieldValue(TagInfoByte tagInfoByte, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoByte);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoByte.name + "\" is missing");
        }
        if (tagInfoByte.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tiffFieldFindField.getByteArrayValue();
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoByte.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public String[] getFieldValue(TagInfoAscii tagInfoAscii, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoAscii);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoAscii.name + "\" is missing");
        }
        if (tagInfoAscii.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoAscii.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoAscii.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public short[] getFieldValue(TagInfoShort tagInfoShort, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoShort);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoShort.name + "\" is missing");
        }
        if (tagInfoShort.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoShort.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoShort.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public int[] getFieldValue(TagInfoLong tagInfoLong, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoLong);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoLong.name + "\" is missing");
        }
        if (tagInfoLong.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoLong.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoLong.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public int[] getFieldValue(TagInfoShortOrLong tagInfoShortOrLong, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoShortOrLong);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoShortOrLong.name + "\" is missing");
        }
        if (tagInfoShortOrLong.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            byte[] byteArrayValue = tiffFieldFindField.getByteArrayValue();
            if (tiffFieldFindField.getFieldType() == FieldType.SHORT) {
                return ByteConversions.toUInt16s(byteArrayValue, tiffFieldFindField.getByteOrder());
            }
            return ByteConversions.toInts(byteArrayValue, tiffFieldFindField.getByteOrder());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoShortOrLong.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public RationalNumber[] getFieldValue(TagInfoRational tagInfoRational, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoRational);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoRational.name + "\" is missing");
        }
        if (tagInfoRational.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoRational.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoRational.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public byte[] getFieldValue(TagInfoSByte tagInfoSByte, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSByte);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoSByte.name + "\" is missing");
        }
        if (tagInfoSByte.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tiffFieldFindField.getByteArrayValue();
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoSByte.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public short[] getFieldValue(TagInfoSShort tagInfoSShort, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSShort);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoSShort.name + "\" is missing");
        }
        if (tagInfoSShort.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoSShort.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoSShort.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public int[] getFieldValue(TagInfoSLong tagInfoSLong, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSLong);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoSLong.name + "\" is missing");
        }
        if (tagInfoSLong.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoSLong.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoSLong.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public RationalNumber[] getFieldValue(TagInfoSRational tagInfoSRational, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSRational);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoSRational.name + "\" is missing");
        }
        if (tagInfoSRational.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoSRational.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoSRational.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public float[] getFieldValue(TagInfoFloat tagInfoFloat, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoFloat);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoFloat.name + "\" is missing");
        }
        if (tagInfoFloat.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoFloat.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoFloat.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public double[] getFieldValue(TagInfoDouble tagInfoDouble, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoDouble);
        if (tiffFieldFindField == null) {
            if (!z) {
                return null;
            }
            throw new ImageReadException("Required field \"" + tagInfoDouble.name + "\" is missing");
        }
        if (tagInfoDouble.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tagInfoDouble.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoDouble.name + "\" has incorrect type " + tiffFieldFindField.getFieldType().getName());
    }

    public String getFieldValue(TagInfoGpsText tagInfoGpsText, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoGpsText);
        if (tiffFieldFindField != null) {
            return tagInfoGpsText.getValue(tiffFieldFindField);
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoGpsText.name + "\" is missing");
    }

    public String getFieldValue(TagInfoXpString tagInfoXpString, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoXpString);
        if (tiffFieldFindField != null) {
            return tagInfoXpString.getValue(tiffFieldFindField);
        }
        if (!z) {
            return null;
        }
        throw new ImageReadException("Required field \"" + tagInfoXpString.name + "\" is missing");
    }

    public static final class ImageDataElement extends TiffElement {
        @Override // org.apache.commons.imaging.formats.tiff.TiffElement
        public String getElementDescription(boolean z) {
            if (z) {
                return null;
            }
            return "ImageDataElement";
        }

        public ImageDataElement(long j, int i) {
            super(j, i);
        }
    }

    private List<ImageDataElement> getRawImageDataElements(TiffField tiffField, TiffField tiffField2) throws ImageReadException {
        int[] intArrayValue = tiffField.getIntArrayValue();
        int[] intArrayValue2 = tiffField2.getIntArrayValue();
        if (intArrayValue.length != intArrayValue2.length) {
            throw new ImageReadException("offsets.length(" + intArrayValue.length + ") != byteCounts.length(" + intArrayValue2.length + ")");
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < intArrayValue.length; i++) {
            arrayList.add(new ImageDataElement(intArrayValue[i], intArrayValue2[i]));
        }
        return arrayList;
    }

    public List<ImageDataElement> getTiffRawImageDataElements() throws ImageReadException {
        TiffField tiffFieldFindField = findField(TiffTagConstants.TIFF_TAG_TILE_OFFSETS);
        TiffField tiffFieldFindField2 = findField(TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField tiffFieldFindField3 = findField(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS);
        TiffField tiffFieldFindField4 = findField(TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS);
        if (tiffFieldFindField != null && tiffFieldFindField2 != null) {
            return getRawImageDataElements(tiffFieldFindField, tiffFieldFindField2);
        }
        if (tiffFieldFindField3 != null && tiffFieldFindField4 != null) {
            return getRawImageDataElements(tiffFieldFindField3, tiffFieldFindField4);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public boolean imageDataInStrips() throws ImageReadException {
        TiffField tiffFieldFindField = findField(TiffTagConstants.TIFF_TAG_TILE_OFFSETS);
        TiffField tiffFieldFindField2 = findField(TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS);
        TiffField tiffFieldFindField3 = findField(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS);
        TiffField tiffFieldFindField4 = findField(TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS);
        if (tiffFieldFindField != null && tiffFieldFindField2 != null) {
            return false;
        }
        if (tiffFieldFindField3 == null || tiffFieldFindField4 == null) {
            throw new ImageReadException("Couldn't find image data.");
        }
        return true;
    }

    public ImageDataElement getJpegRawImageDataElement() throws ImageReadException {
        TiffField tiffFieldFindField = findField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        TiffField tiffFieldFindField2 = findField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (tiffFieldFindField != null && tiffFieldFindField2 != null) {
            return new ImageDataElement(tiffFieldFindField.getIntArrayValue()[0], tiffFieldFindField2.getIntArrayValue()[0]);
        }
        throw new ImageReadException("Couldn't find image data.");
    }

    public void setTiffImageData(TiffImageData tiffImageData) {
        this.tiffImageData = tiffImageData;
    }

    public TiffImageData getTiffImageData() {
        return this.tiffImageData;
    }

    public void setJpegImageData(JpegImageData jpegImageData) {
        this.jpegImageData = jpegImageData;
    }

    public JpegImageData getJpegImageData() {
        return this.jpegImageData;
    }
}
