// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import java.io.IOException;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSRational;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoFloat;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoDouble;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoGpsText;
import java.util.Collection;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.Iterator;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageReadException;
import java.util.Collections;
import java.util.List;

public class TiffDirectory extends TiffElement
{
    public final List<TiffField> entries;
    private JpegImageData jpegImageData;
    public final long nextDirectoryOffset;
    private TiffImageData tiffImageData;
    public final int type;
    
    public TiffDirectory(final int type, final List<TiffField> list, final long n, final long nextDirectoryOffset) {
        super(n, 2 + list.size() * 12 + 4);
        this.type = type;
        this.entries = Collections.unmodifiableList((List<? extends TiffField>)list);
        this.nextDirectoryOffset = nextDirectoryOffset;
    }
    
    public static String description(final int n) {
        switch (n) {
            default: {
                return "Bad Type";
            }
            case 2: {
                return "Thumbnail";
            }
            case 1: {
                return "Sub";
            }
            case 0: {
                return "Root";
            }
            case -1: {
                return "Unknown";
            }
            case -2: {
                return "Exif";
            }
            case -3: {
                return "Gps";
            }
            case -4: {
                return "Interoperability";
            }
        }
    }
    
    private List<ImageDataElement> getRawImageDataElements(final TiffField tiffField, final TiffField tiffField2) throws ImageReadException {
        final int[] intArrayValue = tiffField.getIntArrayValue();
        final int[] intArrayValue2 = tiffField2.getIntArrayValue();
        if (intArrayValue.length != intArrayValue2.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("offsets.length(");
            sb.append(intArrayValue.length);
            sb.append(") != byteCounts.length(");
            sb.append(intArrayValue2.length);
            sb.append(")");
            throw new ImageReadException(sb.toString());
        }
        final ArrayList list = new ArrayList();
        for (int i = 0; i < intArrayValue.length; ++i) {
            list.add(new ImageDataElement(intArrayValue[i], intArrayValue2[i]));
        }
        return list;
    }
    
    public String description() {
        return description(this.type);
    }
    
    public void dump() {
        final Iterator<TiffField> iterator = this.entries.iterator();
        while (iterator.hasNext()) {
            iterator.next().dump();
        }
    }
    
    public TiffField findField(final TagInfo tagInfo) throws ImageReadException {
        return this.findField(tagInfo, false);
    }
    
    public TiffField findField(final TagInfo tagInfo, final boolean b) throws ImageReadException {
        if (this.entries == null) {
            return null;
        }
        for (final TiffField tiffField : this.entries) {
            if (tiffField.getTag() == tagInfo.tag) {
                return tiffField;
            }
        }
        if (b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Missing expected field: ");
            sb.append(tagInfo.getDescription());
            throw new ImageReadException(sb.toString());
        }
        return null;
    }
    
    public List<TiffField> getDirectoryEntries() {
        return new ArrayList<TiffField>(this.entries);
    }
    
    @Override
    public String getElementDescription(final boolean b) {
        if (!b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("TIFF Directory (");
            sb.append(this.description());
            sb.append(")");
            return sb.toString();
        }
        long l = this.offset + 2L;
        final StringBuilder sb2 = new StringBuilder();
        for (final TiffField tiffField : this.entries) {
            sb2.append(String.format("\t[%d]: %s (%d, 0x%x), %s, %d: %s%n", l, tiffField.getTagInfo().name, tiffField.getTag(), tiffField.getTag(), tiffField.getFieldType().getName(), tiffField.getBytesLength(), tiffField.getValueDescription()));
            l += 12L;
        }
        return sb2.toString();
    }
    
    public Object getFieldValue(final TagInfo tagInfo) throws ImageReadException {
        final TiffField field = this.findField(tagInfo);
        if (field == null) {
            return null;
        }
        return field.getValue();
    }
    
    public String getFieldValue(final TagInfoGpsText tagInfoGpsText, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoGpsText);
        if (field != null) {
            return tagInfoGpsText.getValue(field);
        }
        if (b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Required field \"");
            sb.append(tagInfoGpsText.name);
            sb.append("\" is missing");
            throw new ImageReadException(sb.toString());
        }
        return null;
    }
    
    public String getFieldValue(final TagInfoXpString tagInfoXpString, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoXpString);
        if (field != null) {
            return tagInfoXpString.getValue(field);
        }
        if (b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Required field \"");
            sb.append(tagInfoXpString.name);
            sb.append("\" is missing");
            throw new ImageReadException(sb.toString());
        }
        return null;
    }
    
    public byte[] getFieldValue(final TagInfoByte tagInfoByte, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoByte);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoByte.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoByte.dataTypes.contains(field.getFieldType())) {
                return field.getByteArrayValue();
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoByte.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public byte[] getFieldValue(final TagInfoSByte tagInfoSByte, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSByte);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoSByte.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoSByte.dataTypes.contains(field.getFieldType())) {
                return field.getByteArrayValue();
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoSByte.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public double[] getFieldValue(final TagInfoDouble tagInfoDouble, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoDouble);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoDouble.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoDouble.dataTypes.contains(field.getFieldType())) {
                return tagInfoDouble.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoDouble.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public float[] getFieldValue(final TagInfoFloat tagInfoFloat, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoFloat);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoFloat.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoFloat.dataTypes.contains(field.getFieldType())) {
                return tagInfoFloat.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoFloat.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public int[] getFieldValue(final TagInfoLong tagInfoLong, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoLong);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoLong.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoLong.dataTypes.contains(field.getFieldType())) {
                return tagInfoLong.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoLong.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public int[] getFieldValue(final TagInfoSLong tagInfoSLong, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSLong);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoSLong.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoSLong.dataTypes.contains(field.getFieldType())) {
                return tagInfoSLong.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoSLong.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public int[] getFieldValue(final TagInfoShortOrLong tagInfoShortOrLong, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoShortOrLong);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoShortOrLong.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else if (!tagInfoShortOrLong.dataTypes.contains(field.getFieldType())) {
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoShortOrLong.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
        else {
            final byte[] byteArrayValue = field.getByteArrayValue();
            if (field.getFieldType() == FieldType.SHORT) {
                return ByteConversions.toUInt16s(byteArrayValue, field.getByteOrder());
            }
            return ByteConversions.toInts(byteArrayValue, field.getByteOrder());
        }
    }
    
    public String[] getFieldValue(final TagInfoAscii tagInfoAscii, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoAscii);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoAscii.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoAscii.dataTypes.contains(field.getFieldType())) {
                return tagInfoAscii.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoAscii.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public RationalNumber[] getFieldValue(final TagInfoRational tagInfoRational, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoRational);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoRational.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoRational.dataTypes.contains(field.getFieldType())) {
                return tagInfoRational.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoRational.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public RationalNumber[] getFieldValue(final TagInfoSRational tagInfoSRational, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSRational);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoSRational.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoSRational.dataTypes.contains(field.getFieldType())) {
                return tagInfoSRational.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoSRational.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public short[] getFieldValue(final TagInfoSShort tagInfoSShort, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSShort);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoSShort.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoSShort.dataTypes.contains(field.getFieldType())) {
                return tagInfoSShort.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoSShort.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public short[] getFieldValue(final TagInfoShort tagInfoShort, final boolean b) throws ImageReadException {
        final TiffField field = this.findField(tagInfoShort);
        if (field == null) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Required field \"");
                sb.append(tagInfoShort.name);
                sb.append("\" is missing");
                throw new ImageReadException(sb.toString());
            }
            return null;
        }
        else {
            if (tagInfoShort.dataTypes.contains(field.getFieldType())) {
                return tagInfoShort.getValue(field.getByteOrder(), field.getByteArrayValue());
            }
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Required field \"");
                sb2.append(tagInfoShort.name);
                sb2.append("\" has incorrect type ");
                sb2.append(field.getFieldType().getName());
                throw new ImageReadException(sb2.toString());
            }
            return null;
        }
    }
    
    public JpegImageData getJpegImageData() {
        return this.jpegImageData;
    }
    
    public ImageDataElement getJpegRawImageDataElement() throws ImageReadException {
        final TiffField field = this.findField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        final TiffField field2 = this.findField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (field != null && field2 != null) {
            return new ImageDataElement(field.getIntArrayValue()[0], field2.getIntArrayValue()[0]);
        }
        throw new ImageReadException("Couldn't find image data.");
    }
    
    public byte getSingleFieldValue(final TagInfoByte tagInfoByte) throws ImageReadException {
        final byte[] fieldValue = this.getFieldValue(tagInfoByte, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoByte.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public byte getSingleFieldValue(final TagInfoSByte tagInfoSByte) throws ImageReadException {
        final byte[] fieldValue = this.getFieldValue(tagInfoSByte, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoSByte.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public double getSingleFieldValue(final TagInfoDouble tagInfoDouble) throws ImageReadException {
        final double[] fieldValue = this.getFieldValue(tagInfoDouble, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoDouble.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public float getSingleFieldValue(final TagInfoFloat tagInfoFloat) throws ImageReadException {
        final float[] fieldValue = this.getFieldValue(tagInfoFloat, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoFloat.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public int getSingleFieldValue(final TagInfoLong tagInfoLong) throws ImageReadException {
        final int[] fieldValue = this.getFieldValue(tagInfoLong, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoLong.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public int getSingleFieldValue(final TagInfoSLong tagInfoSLong) throws ImageReadException {
        final int[] fieldValue = this.getFieldValue(tagInfoSLong, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoSLong.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public int getSingleFieldValue(final TagInfoShortOrLong tagInfoShortOrLong) throws ImageReadException {
        final int[] fieldValue = this.getFieldValue(tagInfoShortOrLong, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoShortOrLong.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public String getSingleFieldValue(final TagInfoAscii tagInfoAscii) throws ImageReadException {
        final String[] fieldValue = this.getFieldValue(tagInfoAscii, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoAscii.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public RationalNumber getSingleFieldValue(final TagInfoRational tagInfoRational) throws ImageReadException {
        final RationalNumber[] fieldValue = this.getFieldValue(tagInfoRational, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoRational.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public RationalNumber getSingleFieldValue(final TagInfoSRational tagInfoSRational) throws ImageReadException {
        final RationalNumber[] fieldValue = this.getFieldValue(tagInfoSRational, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoSRational.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public short getSingleFieldValue(final TagInfoSShort tagInfoSShort) throws ImageReadException {
        final short[] fieldValue = this.getFieldValue(tagInfoSShort, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoSShort.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public short getSingleFieldValue(final TagInfoShort tagInfoShort) throws ImageReadException {
        final short[] fieldValue = this.getFieldValue(tagInfoShort, true);
        if (fieldValue.length != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field \"");
            sb.append(tagInfoShort.name);
            sb.append("\" has incorrect length ");
            sb.append(fieldValue.length);
            throw new ImageReadException(sb.toString());
        }
        return fieldValue[0];
    }
    
    public BufferedImage getTiffImage(final ByteOrder byteOrder) throws ImageReadException, IOException {
        return this.getTiffImage(byteOrder, null);
    }
    
    public BufferedImage getTiffImage(final ByteOrder byteOrder, final Map<String, Object> map) throws ImageReadException, IOException {
        if (this.tiffImageData == null) {
            return null;
        }
        return new TiffImageParser().getBufferedImage(this, byteOrder, map);
    }
    
    public TiffImageData getTiffImageData() {
        return this.tiffImageData;
    }
    
    public List<ImageDataElement> getTiffRawImageDataElements() throws ImageReadException {
        final TiffField field = this.findField(TiffTagConstants.TIFF_TAG_TILE_OFFSETS);
        final TiffField field2 = this.findField(TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS);
        final TiffField field3 = this.findField(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS);
        final TiffField field4 = this.findField(TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS);
        if (field != null && field2 != null) {
            return this.getRawImageDataElements(field, field2);
        }
        if (field3 != null && field4 != null) {
            return this.getRawImageDataElements(field3, field4);
        }
        throw new ImageReadException("Couldn't find image data.");
    }
    
    public boolean hasJpegImageData() throws ImageReadException {
        return this.findField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT) != null;
    }
    
    public boolean hasTiffImageData() throws ImageReadException {
        return this.findField(TiffTagConstants.TIFF_TAG_TILE_OFFSETS) != null || this.findField(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS) != null;
    }
    
    public boolean imageDataInStrips() throws ImageReadException {
        final TiffField field = this.findField(TiffTagConstants.TIFF_TAG_TILE_OFFSETS);
        final TiffField field2 = this.findField(TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS);
        final TiffField field3 = this.findField(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS);
        final TiffField field4 = this.findField(TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS);
        if (field != null && field2 != null) {
            return false;
        }
        if (field3 != null && field4 != null) {
            return true;
        }
        throw new ImageReadException("Couldn't find image data.");
    }
    
    public void setJpegImageData(final JpegImageData jpegImageData) {
        this.jpegImageData = jpegImageData;
    }
    
    public void setTiffImageData(final TiffImageData tiffImageData) {
        this.tiffImageData = tiffImageData;
    }
    
    public static final class ImageDataElement extends TiffElement
    {
        public ImageDataElement(final long n, final int n2) {
            super(n, n2);
        }
        
        @Override
        public String getElementDescription(final boolean b) {
            if (b) {
                return null;
            }
            return "ImageDataElement";
        }
    }
}
