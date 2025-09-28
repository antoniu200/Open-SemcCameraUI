package org.apache.commons.imaging.formats.tiff.write;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.JpegImageData;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffElement;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAsciiOrByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAsciiOrRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByteOrShort;
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
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrLongOrRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShortOrRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class TiffOutputDirectory extends TiffOutputItem {
    public static final Comparator<TiffOutputDirectory> COMPARATOR = new Comparator<TiffOutputDirectory>() { // from class: org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory.1
        @Override // java.util.Comparator
        public int compare(TiffOutputDirectory tiffOutputDirectory, TiffOutputDirectory tiffOutputDirectory2) {
            if (tiffOutputDirectory.type < tiffOutputDirectory2.type) {
                return -1;
            }
            return tiffOutputDirectory.type > tiffOutputDirectory2.type ? 1 : 0;
        }
    };
    private final ByteOrder byteOrder;
    private final List<TiffOutputField> fields = new ArrayList();
    private JpegImageData jpegImageData;
    private TiffOutputDirectory nextDirectory;
    private TiffImageData tiffImageData;
    public final int type;

    public void setNextDirectory(TiffOutputDirectory tiffOutputDirectory) {
        this.nextDirectory = tiffOutputDirectory;
    }

    public TiffOutputDirectory(int i, ByteOrder byteOrder) {
        this.type = i;
        this.byteOrder = byteOrder;
    }

    public void add(TagInfoByte tagInfoByte, byte... bArr) throws ImageWriteException {
        if (tagInfoByte.length > 0 && tagInfoByte.length != bArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoByte.length + " value(s), not " + bArr.length);
        }
        add(new TiffOutputField(tagInfoByte.tag, tagInfoByte, FieldType.BYTE, bArr.length, tagInfoByte.encodeValue(this.byteOrder, bArr)));
    }

    public void add(TagInfoAscii tagInfoAscii, String... strArr) throws ImageWriteException {
        byte[] bArrEncodeValue = tagInfoAscii.encodeValue(this.byteOrder, strArr);
        if (tagInfoAscii.length > 0 && tagInfoAscii.length != bArrEncodeValue.length) {
            throw new ImageWriteException("Tag expects " + tagInfoAscii.length + " byte(s), not " + strArr.length);
        }
        add(new TiffOutputField(tagInfoAscii.tag, tagInfoAscii, FieldType.ASCII, bArrEncodeValue.length, bArrEncodeValue));
    }

    public void add(TagInfoShort tagInfoShort, short... sArr) throws ImageWriteException {
        if (tagInfoShort.length > 0 && tagInfoShort.length != sArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShort.length + " value(s), not " + sArr.length);
        }
        add(new TiffOutputField(tagInfoShort.tag, tagInfoShort, FieldType.SHORT, sArr.length, tagInfoShort.encodeValue(this.byteOrder, sArr)));
    }

    public void add(TagInfoLong tagInfoLong, int... iArr) throws ImageWriteException {
        if (tagInfoLong.length > 0 && tagInfoLong.length != iArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoLong.length + " value(s), not " + iArr.length);
        }
        add(new TiffOutputField(tagInfoLong.tag, tagInfoLong, FieldType.LONG, iArr.length, tagInfoLong.encodeValue(this.byteOrder, iArr)));
    }

    public void add(TagInfoRational tagInfoRational, RationalNumber... rationalNumberArr) throws ImageWriteException {
        if (tagInfoRational.length > 0 && tagInfoRational.length != rationalNumberArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoRational.length + " value(s), not " + rationalNumberArr.length);
        }
        add(new TiffOutputField(tagInfoRational.tag, tagInfoRational, FieldType.RATIONAL, rationalNumberArr.length, tagInfoRational.encodeValue(this.byteOrder, rationalNumberArr)));
    }

    public void add(TagInfoSByte tagInfoSByte, byte... bArr) throws ImageWriteException {
        if (tagInfoSByte.length > 0 && tagInfoSByte.length != bArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoSByte.length + " value(s), not " + bArr.length);
        }
        add(new TiffOutputField(tagInfoSByte.tag, tagInfoSByte, FieldType.SBYTE, bArr.length, tagInfoSByte.encodeValue(this.byteOrder, bArr)));
    }

    public void add(TagInfoSShort tagInfoSShort, short... sArr) throws ImageWriteException {
        if (tagInfoSShort.length > 0 && tagInfoSShort.length != sArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoSShort.length + " value(s), not " + sArr.length);
        }
        add(new TiffOutputField(tagInfoSShort.tag, tagInfoSShort, FieldType.SSHORT, sArr.length, tagInfoSShort.encodeValue(this.byteOrder, sArr)));
    }

    public void add(TagInfoSLong tagInfoSLong, int... iArr) throws ImageWriteException {
        if (tagInfoSLong.length > 0 && tagInfoSLong.length != iArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoSLong.length + " value(s), not " + iArr.length);
        }
        add(new TiffOutputField(tagInfoSLong.tag, tagInfoSLong, FieldType.SLONG, iArr.length, tagInfoSLong.encodeValue(this.byteOrder, iArr)));
    }

    public void add(TagInfoSRational tagInfoSRational, RationalNumber... rationalNumberArr) throws ImageWriteException {
        if (tagInfoSRational.length > 0 && tagInfoSRational.length != rationalNumberArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoSRational.length + " value(s), not " + rationalNumberArr.length);
        }
        add(new TiffOutputField(tagInfoSRational.tag, tagInfoSRational, FieldType.SRATIONAL, rationalNumberArr.length, tagInfoSRational.encodeValue(this.byteOrder, rationalNumberArr)));
    }

    public void add(TagInfoFloat tagInfoFloat, float... fArr) throws ImageWriteException {
        if (tagInfoFloat.length > 0 && tagInfoFloat.length != fArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoFloat.length + " value(s), not " + fArr.length);
        }
        add(new TiffOutputField(tagInfoFloat.tag, tagInfoFloat, FieldType.FLOAT, fArr.length, tagInfoFloat.encodeValue(this.byteOrder, fArr)));
    }

    public void add(TagInfoDouble tagInfoDouble, double... dArr) throws ImageWriteException {
        if (tagInfoDouble.length > 0 && tagInfoDouble.length != dArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoDouble.length + " value(s), not " + dArr.length);
        }
        add(new TiffOutputField(tagInfoDouble.tag, tagInfoDouble, FieldType.DOUBLE, dArr.length, tagInfoDouble.encodeValue(this.byteOrder, dArr)));
    }

    public void add(TagInfoByteOrShort tagInfoByteOrShort, byte... bArr) throws ImageWriteException {
        if (tagInfoByteOrShort.length > 0 && tagInfoByteOrShort.length != bArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoByteOrShort.length + " value(s), not " + bArr.length);
        }
        add(new TiffOutputField(tagInfoByteOrShort.tag, tagInfoByteOrShort, FieldType.BYTE, bArr.length, tagInfoByteOrShort.encodeValue(this.byteOrder, bArr)));
    }

    public void add(TagInfoByteOrShort tagInfoByteOrShort, short... sArr) throws ImageWriteException {
        if (tagInfoByteOrShort.length > 0 && tagInfoByteOrShort.length != sArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoByteOrShort.length + " value(s), not " + sArr.length);
        }
        add(new TiffOutputField(tagInfoByteOrShort.tag, tagInfoByteOrShort, FieldType.SHORT, sArr.length, tagInfoByteOrShort.encodeValue(this.byteOrder, sArr)));
    }

    public void add(TagInfoShortOrLong tagInfoShortOrLong, short... sArr) throws ImageWriteException {
        if (tagInfoShortOrLong.length > 0 && tagInfoShortOrLong.length != sArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShortOrLong.length + " value(s), not " + sArr.length);
        }
        add(new TiffOutputField(tagInfoShortOrLong.tag, tagInfoShortOrLong, FieldType.SHORT, sArr.length, tagInfoShortOrLong.encodeValue(this.byteOrder, sArr)));
    }

    public void add(TagInfoShortOrLong tagInfoShortOrLong, int... iArr) throws ImageWriteException {
        if (tagInfoShortOrLong.length > 0 && tagInfoShortOrLong.length != iArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShortOrLong.length + " value(s), not " + iArr.length);
        }
        add(new TiffOutputField(tagInfoShortOrLong.tag, tagInfoShortOrLong, FieldType.LONG, iArr.length, tagInfoShortOrLong.encodeValue(this.byteOrder, iArr)));
    }

    public void add(TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, short... sArr) throws ImageWriteException {
        if (tagInfoShortOrLongOrRational.length > 0 && tagInfoShortOrLongOrRational.length != sArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShortOrLongOrRational.length + " value(s), not " + sArr.length);
        }
        add(new TiffOutputField(tagInfoShortOrLongOrRational.tag, tagInfoShortOrLongOrRational, FieldType.SHORT, sArr.length, tagInfoShortOrLongOrRational.encodeValue(this.byteOrder, sArr)));
    }

    public void add(TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, int... iArr) throws ImageWriteException {
        if (tagInfoShortOrLongOrRational.length > 0 && tagInfoShortOrLongOrRational.length != iArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShortOrLongOrRational.length + " value(s), not " + iArr.length);
        }
        add(new TiffOutputField(tagInfoShortOrLongOrRational.tag, tagInfoShortOrLongOrRational, FieldType.LONG, iArr.length, tagInfoShortOrLongOrRational.encodeValue(this.byteOrder, iArr)));
    }

    public void add(TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, RationalNumber... rationalNumberArr) throws ImageWriteException {
        if (tagInfoShortOrLongOrRational.length > 0 && tagInfoShortOrLongOrRational.length != rationalNumberArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShortOrLongOrRational.length + " value(s), not " + rationalNumberArr.length);
        }
        add(new TiffOutputField(tagInfoShortOrLongOrRational.tag, tagInfoShortOrLongOrRational, FieldType.RATIONAL, rationalNumberArr.length, tagInfoShortOrLongOrRational.encodeValue(this.byteOrder, rationalNumberArr)));
    }

    public void add(TagInfoShortOrRational tagInfoShortOrRational, short... sArr) throws ImageWriteException {
        if (tagInfoShortOrRational.length > 0 && tagInfoShortOrRational.length != sArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShortOrRational.length + " value(s), not " + sArr.length);
        }
        add(new TiffOutputField(tagInfoShortOrRational.tag, tagInfoShortOrRational, FieldType.SHORT, sArr.length, tagInfoShortOrRational.encodeValue(this.byteOrder, sArr)));
    }

    public void add(TagInfoShortOrRational tagInfoShortOrRational, RationalNumber... rationalNumberArr) throws ImageWriteException {
        if (tagInfoShortOrRational.length > 0 && tagInfoShortOrRational.length != rationalNumberArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoShortOrRational.length + " value(s), not " + rationalNumberArr.length);
        }
        add(new TiffOutputField(tagInfoShortOrRational.tag, tagInfoShortOrRational, FieldType.RATIONAL, rationalNumberArr.length, tagInfoShortOrRational.encodeValue(this.byteOrder, rationalNumberArr)));
    }

    public void add(TagInfoGpsText tagInfoGpsText, String str) throws ImageWriteException {
        byte[] bArrEncodeValue = tagInfoGpsText.encodeValue(FieldType.UNDEFINED, str, this.byteOrder);
        add(new TiffOutputField(tagInfoGpsText.tag, tagInfoGpsText, tagInfoGpsText.dataTypes.get(0), bArrEncodeValue.length, bArrEncodeValue));
    }

    public void add(TagInfoXpString tagInfoXpString, String str) throws ImageWriteException {
        byte[] bArrEncodeValue = tagInfoXpString.encodeValue(FieldType.BYTE, str, this.byteOrder);
        add(new TiffOutputField(tagInfoXpString.tag, tagInfoXpString, FieldType.BYTE, bArrEncodeValue.length, bArrEncodeValue));
    }

    public void add(TagInfoAsciiOrByte tagInfoAsciiOrByte, String... strArr) throws ImageWriteException {
        byte[] bArrEncodeValue = tagInfoAsciiOrByte.encodeValue(FieldType.ASCII, strArr, this.byteOrder);
        if (tagInfoAsciiOrByte.length > 0 && tagInfoAsciiOrByte.length != bArrEncodeValue.length) {
            throw new ImageWriteException("Tag expects " + tagInfoAsciiOrByte.length + " byte(s), not " + strArr.length);
        }
        add(new TiffOutputField(tagInfoAsciiOrByte.tag, tagInfoAsciiOrByte, FieldType.ASCII, bArrEncodeValue.length, bArrEncodeValue));
    }

    public void add(TagInfoAsciiOrRational tagInfoAsciiOrRational, String... strArr) throws ImageWriteException {
        byte[] bArrEncodeValue = tagInfoAsciiOrRational.encodeValue(FieldType.ASCII, strArr, this.byteOrder);
        if (tagInfoAsciiOrRational.length > 0 && tagInfoAsciiOrRational.length != bArrEncodeValue.length) {
            throw new ImageWriteException("Tag expects " + tagInfoAsciiOrRational.length + " byte(s), not " + strArr.length);
        }
        add(new TiffOutputField(tagInfoAsciiOrRational.tag, tagInfoAsciiOrRational, FieldType.ASCII, bArrEncodeValue.length, bArrEncodeValue));
    }

    public void add(TagInfoAsciiOrRational tagInfoAsciiOrRational, RationalNumber... rationalNumberArr) throws ImageWriteException {
        if (tagInfoAsciiOrRational.length > 0 && tagInfoAsciiOrRational.length != rationalNumberArr.length) {
            throw new ImageWriteException("Tag expects " + tagInfoAsciiOrRational.length + " value(s), not " + rationalNumberArr.length);
        }
        byte[] bArrEncodeValue = tagInfoAsciiOrRational.encodeValue(FieldType.RATIONAL, rationalNumberArr, this.byteOrder);
        add(new TiffOutputField(tagInfoAsciiOrRational.tag, tagInfoAsciiOrRational, FieldType.RATIONAL, bArrEncodeValue.length, bArrEncodeValue));
    }

    public void add(TiffOutputField tiffOutputField) {
        this.fields.add(tiffOutputField);
    }

    public List<TiffOutputField> getFields() {
        return new ArrayList(this.fields);
    }

    public void removeField(TagInfo tagInfo) {
        removeField(tagInfo.tag);
    }

    public void removeField(int i) {
        ArrayList arrayList = new ArrayList();
        for (TiffOutputField tiffOutputField : this.fields) {
            if (tiffOutputField.tag == i) {
                arrayList.add(tiffOutputField);
            }
        }
        this.fields.removeAll(arrayList);
    }

    public TiffOutputField findField(TagInfo tagInfo) {
        return findField(tagInfo.tag);
    }

    public TiffOutputField findField(int i) {
        for (TiffOutputField tiffOutputField : this.fields) {
            if (tiffOutputField.tag == i) {
                return tiffOutputField;
            }
        }
        return null;
    }

    public void sortFields() {
        Collections.sort(this.fields, new Comparator<TiffOutputField>() { // from class: org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory.2
            @Override // java.util.Comparator
            public int compare(TiffOutputField tiffOutputField, TiffOutputField tiffOutputField2) {
                if (tiffOutputField.tag != tiffOutputField2.tag) {
                    return tiffOutputField.tag - tiffOutputField2.tag;
                }
                return tiffOutputField.getSortHint() - tiffOutputField2.getSortHint();
            }
        });
    }

    public String description() {
        return TiffDirectory.description(this.type);
    }

    @Override // org.apache.commons.imaging.formats.tiff.write.TiffOutputItem
    public void writeItem(BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        binaryOutputStream.write2Bytes(this.fields.size());
        Iterator<TiffOutputField> it = this.fields.iterator();
        while (it.hasNext()) {
            it.next().writeField(binaryOutputStream);
        }
        long offset = this.nextDirectory != null ? this.nextDirectory.getOffset() : 0L;
        if (offset == -1) {
            binaryOutputStream.write4Bytes(0);
        } else {
            binaryOutputStream.write4Bytes((int) offset);
        }
    }

    public void setJpegImageData(JpegImageData jpegImageData) {
        this.jpegImageData = jpegImageData;
    }

    public JpegImageData getRawJpegImageData() {
        return this.jpegImageData;
    }

    public void setTiffImageData(TiffImageData tiffImageData) {
        this.tiffImageData = tiffImageData;
    }

    public TiffImageData getRawTiffImageData() {
        return this.tiffImageData;
    }

    @Override // org.apache.commons.imaging.formats.tiff.write.TiffOutputItem
    public int getItemLength() {
        return (12 * this.fields.size()) + 2 + 4;
    }

    @Override // org.apache.commons.imaging.formats.tiff.write.TiffOutputItem
    public String getItemDescription() {
        return "Directory: " + TiffDirectoryType.getExifDirectoryType(this.type).name + " (" + this.type + ")";
    }

    private void removeFieldIfPresent(TagInfo tagInfo) {
        TiffOutputField tiffOutputFieldFindField = findField(tagInfo);
        if (tiffOutputFieldFindField != null) {
            this.fields.remove(tiffOutputFieldFindField);
        }
    }

    protected List<TiffOutputItem> getOutputItems(TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        TiffOutputField tiffOutputField;
        TagInfo tagInfo;
        TagInfoShortOrLong tagInfoShortOrLong;
        removeFieldIfPresent(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT);
        removeFieldIfPresent(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        ImageDataOffsets imageDataOffsets = null;
        if (this.jpegImageData != null) {
            tiffOutputField = new TiffOutputField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT, FieldType.LONG, 1, new byte[4]);
            add(tiffOutputField);
            add(new TiffOutputField(TiffTagConstants.TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, FieldType.LONG, 1, FieldType.LONG.writeData(Integer.valueOf(this.jpegImageData.length), tiffOutputSummary.byteOrder)));
        } else {
            tiffOutputField = null;
        }
        removeFieldIfPresent(TiffTagConstants.TIFF_TAG_STRIP_OFFSETS);
        removeFieldIfPresent(TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS);
        removeFieldIfPresent(TiffTagConstants.TIFF_TAG_TILE_OFFSETS);
        removeFieldIfPresent(TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS);
        if (this.tiffImageData != null) {
            if (this.tiffImageData.stripsNotTiles()) {
                tagInfo = TiffTagConstants.TIFF_TAG_STRIP_OFFSETS;
                tagInfoShortOrLong = TiffTagConstants.TIFF_TAG_STRIP_BYTE_COUNTS;
            } else {
                tagInfo = TiffTagConstants.TIFF_TAG_TILE_OFFSETS;
                tagInfoShortOrLong = TiffTagConstants.TIFF_TAG_TILE_BYTE_COUNTS;
            }
            TiffElement.DataElement[] imageData = this.tiffImageData.getImageData();
            int[] iArr = new int[imageData.length];
            int[] iArr2 = new int[imageData.length];
            for (int i = 0; i < imageData.length; i++) {
                iArr2[i] = imageData[i].length;
            }
            TiffOutputField tiffOutputField2 = new TiffOutputField(tagInfo, FieldType.LONG, iArr.length, FieldType.LONG.writeData(iArr, tiffOutputSummary.byteOrder));
            add(tiffOutputField2);
            add(new TiffOutputField(tagInfoShortOrLong, FieldType.LONG, iArr2.length, FieldType.LONG.writeData(iArr2, tiffOutputSummary.byteOrder)));
            imageDataOffsets = new ImageDataOffsets(imageData, iArr, tiffOutputField2);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this);
        sortFields();
        for (TiffOutputField tiffOutputField3 : this.fields) {
            if (!tiffOutputField3.isLocalValue()) {
                arrayList.add(tiffOutputField3.getSeperateValue());
            }
        }
        if (imageDataOffsets != null) {
            Collections.addAll(arrayList, imageDataOffsets.outputItems);
            tiffOutputSummary.addTiffImageData(imageDataOffsets);
        }
        if (this.jpegImageData != null) {
            TiffOutputItem.Value value = new TiffOutputItem.Value("JPEG image data", this.jpegImageData.getData());
            arrayList.add(value);
            tiffOutputSummary.add(value, tiffOutputField);
        }
        return arrayList;
    }
}
