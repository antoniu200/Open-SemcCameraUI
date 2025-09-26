// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.formats.tiff.TiffElement;
import java.util.Collections;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLongOrRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoGpsText;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoFloat;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoDouble;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByteOrShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAsciiOrRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAsciiOrByte;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.ArrayList;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.JpegImageData;
import java.util.List;
import java.nio.ByteOrder;
import java.util.Comparator;

public final class TiffOutputDirectory extends TiffOutputItem
{
    public static final Comparator<TiffOutputDirectory> COMPARATOR;
    private final ByteOrder byteOrder;
    private final List<TiffOutputField> fields;
    private JpegImageData jpegImageData;
    private TiffOutputDirectory nextDirectory;
    private TiffImageData tiffImageData;
    public final int type;
    
    static {
        COMPARATOR = new Comparator<TiffOutputDirectory>() {
            @Override
            public int compare(final TiffOutputDirectory tiffOutputDirectory, final TiffOutputDirectory tiffOutputDirectory2) {
                if (tiffOutputDirectory.type < tiffOutputDirectory2.type) {
                    return -1;
                }
                if (tiffOutputDirectory.type > tiffOutputDirectory2.type) {
                    return 1;
                }
                return 0;
            }
        };
    }
    
    public TiffOutputDirectory(final int type, final ByteOrder byteOrder) {
        this.fields = new ArrayList<TiffOutputField>();
        this.type = type;
        this.byteOrder = byteOrder;
    }
    
    private void removeFieldIfPresent(final TagInfo tagInfo) {
        final TiffOutputField field = this.findField(tagInfo);
        if (field != null) {
            this.fields.remove(field);
        }
    }
    
    public void add(final TagInfoAscii tagInfoAscii, final String... array) throws ImageWriteException {
        final byte[] encodeValue = tagInfoAscii.encodeValue(this.byteOrder, array);
        if (tagInfoAscii.length > 0 && tagInfoAscii.length != encodeValue.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoAscii.length);
            sb.append(" byte(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoAscii.tag, tagInfoAscii, FieldType.ASCII, encodeValue.length, encodeValue));
    }
    
    public void add(final TagInfoAsciiOrByte tagInfoAsciiOrByte, final String... array) throws ImageWriteException {
        final byte[] encodeValue = tagInfoAsciiOrByte.encodeValue(FieldType.ASCII, array, this.byteOrder);
        if (tagInfoAsciiOrByte.length > 0 && tagInfoAsciiOrByte.length != encodeValue.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoAsciiOrByte.length);
            sb.append(" byte(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoAsciiOrByte.tag, tagInfoAsciiOrByte, FieldType.ASCII, encodeValue.length, encodeValue));
    }
    
    public void add(final TagInfoAsciiOrRational tagInfoAsciiOrRational, final String... array) throws ImageWriteException {
        final byte[] encodeValue = tagInfoAsciiOrRational.encodeValue(FieldType.ASCII, array, this.byteOrder);
        if (tagInfoAsciiOrRational.length > 0 && tagInfoAsciiOrRational.length != encodeValue.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoAsciiOrRational.length);
            sb.append(" byte(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoAsciiOrRational.tag, tagInfoAsciiOrRational, FieldType.ASCII, encodeValue.length, encodeValue));
    }
    
    public void add(final TagInfoAsciiOrRational tagInfoAsciiOrRational, final RationalNumber... array) throws ImageWriteException {
        if (tagInfoAsciiOrRational.length > 0 && tagInfoAsciiOrRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoAsciiOrRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        final byte[] encodeValue = tagInfoAsciiOrRational.encodeValue(FieldType.RATIONAL, array, this.byteOrder);
        this.add(new TiffOutputField(tagInfoAsciiOrRational.tag, tagInfoAsciiOrRational, FieldType.RATIONAL, encodeValue.length, encodeValue));
    }
    
    public void add(final TagInfoByte tagInfoByte, final byte... array) throws ImageWriteException {
        if (tagInfoByte.length > 0 && tagInfoByte.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoByte.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoByte.tag, tagInfoByte, FieldType.BYTE, array.length, tagInfoByte.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoByteOrShort tagInfoByteOrShort, final byte... array) throws ImageWriteException {
        if (tagInfoByteOrShort.length > 0 && tagInfoByteOrShort.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoByteOrShort.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoByteOrShort.tag, tagInfoByteOrShort, FieldType.BYTE, array.length, tagInfoByteOrShort.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoByteOrShort tagInfoByteOrShort, final short... array) throws ImageWriteException {
        if (tagInfoByteOrShort.length > 0 && tagInfoByteOrShort.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoByteOrShort.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoByteOrShort.tag, tagInfoByteOrShort, FieldType.SHORT, array.length, tagInfoByteOrShort.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoDouble tagInfoDouble, final double... array) throws ImageWriteException {
        if (tagInfoDouble.length > 0 && tagInfoDouble.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoDouble.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoDouble.tag, tagInfoDouble, FieldType.DOUBLE, array.length, tagInfoDouble.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoFloat tagInfoFloat, final float... array) throws ImageWriteException {
        if (tagInfoFloat.length > 0 && tagInfoFloat.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoFloat.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoFloat.tag, tagInfoFloat, FieldType.FLOAT, array.length, tagInfoFloat.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoGpsText tagInfoGpsText, final String s) throws ImageWriteException {
        final byte[] encodeValue = tagInfoGpsText.encodeValue(FieldType.UNDEFINED, s, this.byteOrder);
        this.add(new TiffOutputField(tagInfoGpsText.tag, tagInfoGpsText, tagInfoGpsText.dataTypes.get(0), encodeValue.length, encodeValue));
    }
    
    public void add(final TagInfoLong tagInfoLong, final int... array) throws ImageWriteException {
        if (tagInfoLong.length > 0 && tagInfoLong.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoLong.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoLong.tag, tagInfoLong, FieldType.LONG, array.length, tagInfoLong.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoRational tagInfoRational, final RationalNumber... array) throws ImageWriteException {
        if (tagInfoRational.length > 0 && tagInfoRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoRational.tag, tagInfoRational, FieldType.RATIONAL, array.length, tagInfoRational.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoSByte tagInfoSByte, final byte... array) throws ImageWriteException {
        if (tagInfoSByte.length > 0 && tagInfoSByte.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoSByte.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoSByte.tag, tagInfoSByte, FieldType.SBYTE, array.length, tagInfoSByte.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoSLong tagInfoSLong, final int... array) throws ImageWriteException {
        if (tagInfoSLong.length > 0 && tagInfoSLong.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoSLong.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoSLong.tag, tagInfoSLong, FieldType.SLONG, array.length, tagInfoSLong.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoSRational tagInfoSRational, final RationalNumber... array) throws ImageWriteException {
        if (tagInfoSRational.length > 0 && tagInfoSRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoSRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoSRational.tag, tagInfoSRational, FieldType.SRATIONAL, array.length, tagInfoSRational.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoSShort tagInfoSShort, final short... array) throws ImageWriteException {
        if (tagInfoSShort.length > 0 && tagInfoSShort.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoSShort.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoSShort.tag, tagInfoSShort, FieldType.SSHORT, array.length, tagInfoSShort.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShort tagInfoShort, final short... array) throws ImageWriteException {
        if (tagInfoShort.length > 0 && tagInfoShort.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShort.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShort.tag, tagInfoShort, FieldType.SHORT, array.length, tagInfoShort.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShortOrLong tagInfoShortOrLong, final int... array) throws ImageWriteException {
        if (tagInfoShortOrLong.length > 0 && tagInfoShortOrLong.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShortOrLong.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShortOrLong.tag, tagInfoShortOrLong, FieldType.LONG, array.length, tagInfoShortOrLong.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShortOrLong tagInfoShortOrLong, final short... array) throws ImageWriteException {
        if (tagInfoShortOrLong.length > 0 && tagInfoShortOrLong.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShortOrLong.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShortOrLong.tag, tagInfoShortOrLong, FieldType.SHORT, array.length, tagInfoShortOrLong.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final int... array) throws ImageWriteException {
        if (tagInfoShortOrLongOrRational.length > 0 && tagInfoShortOrLongOrRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShortOrLongOrRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShortOrLongOrRational.tag, tagInfoShortOrLongOrRational, FieldType.LONG, array.length, tagInfoShortOrLongOrRational.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final RationalNumber... array) throws ImageWriteException {
        if (tagInfoShortOrLongOrRational.length > 0 && tagInfoShortOrLongOrRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShortOrLongOrRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShortOrLongOrRational.tag, tagInfoShortOrLongOrRational, FieldType.RATIONAL, array.length, tagInfoShortOrLongOrRational.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final short... array) throws ImageWriteException {
        if (tagInfoShortOrLongOrRational.length > 0 && tagInfoShortOrLongOrRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShortOrLongOrRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShortOrLongOrRational.tag, tagInfoShortOrLongOrRational, FieldType.SHORT, array.length, tagInfoShortOrLongOrRational.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShortOrRational tagInfoShortOrRational, final RationalNumber... array) throws ImageWriteException {
        if (tagInfoShortOrRational.length > 0 && tagInfoShortOrRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShortOrRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShortOrRational.tag, tagInfoShortOrRational, FieldType.RATIONAL, array.length, tagInfoShortOrRational.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoShortOrRational tagInfoShortOrRational, final short... array) throws ImageWriteException {
        if (tagInfoShortOrRational.length > 0 && tagInfoShortOrRational.length != array.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tag expects ");
            sb.append(tagInfoShortOrRational.length);
            sb.append(" value(s), not ");
            sb.append(array.length);
            throw new ImageWriteException(sb.toString());
        }
        this.add(new TiffOutputField(tagInfoShortOrRational.tag, tagInfoShortOrRational, FieldType.SHORT, array.length, tagInfoShortOrRational.encodeValue(this.byteOrder, array)));
    }
    
    public void add(final TagInfoXpString tagInfoXpString, final String s) throws ImageWriteException {
        final byte[] encodeValue = tagInfoXpString.encodeValue(FieldType.BYTE, s, this.byteOrder);
        this.add(new TiffOutputField(tagInfoXpString.tag, tagInfoXpString, FieldType.BYTE, encodeValue.length, encodeValue));
    }
    
    public void add(final TiffOutputField tiffOutputField) {
        this.fields.add(tiffOutputField);
    }
    
    public String description() {
        return TiffDirectory.description(this.type);
    }
    
    public TiffOutputField findField(final int n) {
        for (final TiffOutputField tiffOutputField : this.fields) {
            if (tiffOutputField.tag == n) {
                return tiffOutputField;
            }
        }
        return null;
    }
    
    public TiffOutputField findField(final TagInfo tagInfo) {
        return this.findField(tagInfo.tag);
    }
    
    public List<TiffOutputField> getFields() {
        return new ArrayList<TiffOutputField>(this.fields);
    }
    
    @Override
    public String getItemDescription() {
        final TiffDirectoryType exifDirectoryType = TiffDirectoryType.getExifDirectoryType(this.type);
        final StringBuilder sb = new StringBuilder();
        sb.append("Directory: ");
        sb.append(exifDirectoryType.name);
        sb.append(" (");
        sb.append(this.type);
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public int getItemLength() {
        return 12 * this.fields.size() + 2 + 4;
    }
    
    protected List<TiffOutputItem> getOutputItems(final TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        this.removeFieldIfPresent(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        this.removeFieldIfPresent(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        final JpegImageData jpegImageData = this.jpegImageData;
        ImageDataOffsets imageDataOffsets = null;
        TiffOutputField tiffOutputField;
        if (jpegImageData != null) {
            tiffOutputField = new TiffOutputField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT, FieldType.LONG, 1, new byte[4]);
            this.add(tiffOutputField);
            this.add(new TiffOutputField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, FieldType.LONG, 1, FieldType.LONG.writeData(this.jpegImageData.length, tiffOutputSummary.byteOrder)));
        }
        else {
            tiffOutputField = null;
        }
        this.removeFieldIfPresent(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS);
        this.removeFieldIfPresent(TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS);
        this.removeFieldIfPresent(TiffTagConstants.TIFF_TAG_TILE_OFFSETS);
        this.removeFieldIfPresent(TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS);
        if (this.tiffImageData != null) {
            TagInfo tagInfo;
            TagInfoShortOrLong tagInfoShortOrLong;
            if (this.tiffImageData.stripsNotTiles()) {
                tagInfo = TiffTagConstants.TIFF_TAG_STRIP_OFFSETS;
                tagInfoShortOrLong = TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS;
            }
            else {
                tagInfo = TiffTagConstants.TIFF_TAG_TILE_OFFSETS;
                tagInfoShortOrLong = TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS;
            }
            final TiffElement.DataElement[] imageData = this.tiffImageData.getImageData();
            final int[] array = new int[imageData.length];
            final int[] array2 = new int[imageData.length];
            for (int i = 0; i < imageData.length; ++i) {
                array2[i] = imageData[i].length;
            }
            final TiffOutputField tiffOutputField2 = new TiffOutputField(tagInfo, FieldType.LONG, array.length, FieldType.LONG.writeData(array, tiffOutputSummary.byteOrder));
            this.add(tiffOutputField2);
            this.add(new TiffOutputField(tagInfoShortOrLong, FieldType.LONG, array2.length, FieldType.LONG.writeData(array2, tiffOutputSummary.byteOrder)));
            imageDataOffsets = new ImageDataOffsets(imageData, array, tiffOutputField2);
        }
        final ArrayList c = new ArrayList();
        c.add(this);
        this.sortFields();
        for (final TiffOutputField tiffOutputField3 : this.fields) {
            if (tiffOutputField3.isLocalValue()) {
                continue;
            }
            c.add(tiffOutputField3.getSeperateValue());
        }
        if (imageDataOffsets != null) {
            Collections.addAll(c, imageDataOffsets.outputItems);
            tiffOutputSummary.addTiffImageData(imageDataOffsets);
        }
        if (this.jpegImageData != null) {
            final Value value = new Value("JPEG image data", ((TiffElement.DataElement)this.jpegImageData).getData());
            c.add(value);
            tiffOutputSummary.add(value, tiffOutputField);
        }
        return c;
    }
    
    public JpegImageData getRawJpegImageData() {
        return this.jpegImageData;
    }
    
    public TiffImageData getRawTiffImageData() {
        return this.tiffImageData;
    }
    
    public void removeField(final int n) {
        final ArrayList list = new ArrayList();
        for (final TiffOutputField tiffOutputField : this.fields) {
            if (tiffOutputField.tag == n) {
                list.add(tiffOutputField);
            }
        }
        this.fields.removeAll(list);
    }
    
    public void removeField(final TagInfo tagInfo) {
        this.removeField(tagInfo.tag);
    }
    
    public void setJpegImageData(final JpegImageData jpegImageData) {
        this.jpegImageData = jpegImageData;
    }
    
    public void setNextDirectory(final TiffOutputDirectory nextDirectory) {
        this.nextDirectory = nextDirectory;
    }
    
    public void setTiffImageData(final TiffImageData tiffImageData) {
        this.tiffImageData = tiffImageData;
    }
    
    public void sortFields() {
        Collections.sort(this.fields, new Comparator<TiffOutputField>(this) {
            final TiffOutputDirectory this$0;
            
            @Override
            public int compare(final TiffOutputField tiffOutputField, final TiffOutputField tiffOutputField2) {
                if (tiffOutputField.tag != tiffOutputField2.tag) {
                    return tiffOutputField.tag - tiffOutputField2.tag;
                }
                return tiffOutputField.getSortHint() - tiffOutputField2.getSortHint();
            }
        });
    }
    
    @Override
    public void writeItem(final BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
        binaryOutputStream.write2Bytes(this.fields.size());
        final Iterator<TiffOutputField> iterator = this.fields.iterator();
        while (iterator.hasNext()) {
            iterator.next().writeField(binaryOutputStream);
        }
        long offset = 0L;
        if (this.nextDirectory != null) {
            offset = this.nextDirectory.getOffset();
        }
        if (offset == -1L) {
            binaryOutputStream.write4Bytes(0);
        }
        else {
            binaryOutputStream.write4Bytes((int)offset);
        }
    }
}
