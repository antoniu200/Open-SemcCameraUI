package org.apache.commons.imaging.formats.tiff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
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
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffImageMetadata extends GenericImageMetadata {
    public final TiffContents contents;

    public TiffImageMetadata(TiffContents tiffContents) {
        this.contents = tiffContents;
    }

    public static class Directory extends GenericImageMetadata implements ImageMetadata.ImageMetadataItem {
        private final ByteOrder byteOrder;
        private final TiffDirectory directory;
        public final int type;

        public Directory(ByteOrder byteOrder, TiffDirectory tiffDirectory) {
            this.type = tiffDirectory.type;
            this.directory = tiffDirectory;
            this.byteOrder = byteOrder;
        }

        public void add(TiffField tiffField) {
            add(new TiffMetadataItem(tiffField));
        }

        public BufferedImage getThumbnail() throws IOException, ImageReadException {
            return this.directory.getTiffImage(this.byteOrder);
        }

        public TiffImageData getTiffImageData() {
            return this.directory.getTiffImageData();
        }

        public TiffField findField(TagInfo tagInfo) throws ImageReadException {
            return this.directory.findField(tagInfo);
        }

        public List<TiffField> getAllFields() {
            return this.directory.getDirectoryEntries();
        }

        public JpegImageData getJpegImageData() {
            return this.directory.getJpegImageData();
        }

        @Override // org.apache.commons.imaging.common.GenericImageMetadata, org.apache.commons.imaging.common.ImageMetadata
        public String toString(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append(str != null ? str : "");
            sb.append(this.directory.description());
            sb.append(": ");
            sb.append(getTiffImageData() != null ? " (tiffImageData)" : "");
            sb.append(getJpegImageData() != null ? " (jpegImageData)" : "");
            sb.append("\n");
            sb.append(super.toString(str));
            sb.append("\n");
            return sb.toString();
        }

        public TiffOutputDirectory getOutputDirectory(ByteOrder byteOrder) throws ImageWriteException {
            try {
                TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(this.type, byteOrder);
                Iterator<? extends ImageMetadata.ImageMetadataItem> it = getItems().iterator();
                while (it.hasNext()) {
                    TiffField tiffField = ((TiffMetadataItem) it.next()).getTiffField();
                    if (tiffOutputDirectory.findField(tiffField.getTag()) == null && !tiffField.getTagInfo().isOffset()) {
                        TagInfo tagInfo = tiffField.getTagInfo();
                        FieldType fieldType = tiffField.getFieldType();
                        byte[] bArrEncodeValue = tagInfo.encodeValue(fieldType, tiffField.getValue(), byteOrder);
                        TiffOutputField tiffOutputField = new TiffOutputField(tiffField.getTag(), tagInfo, fieldType, bArrEncodeValue.length / fieldType.getSize(), bArrEncodeValue);
                        tiffOutputField.setSortHint(tiffField.getSortHint());
                        tiffOutputDirectory.add(tiffOutputField);
                    }
                }
                tiffOutputDirectory.setTiffImageData(getTiffImageData());
                tiffOutputDirectory.setJpegImageData(getJpegImageData());
                return tiffOutputDirectory;
            } catch (ImageReadException e) {
                throw new ImageWriteException(e.getMessage(), (Throwable) e);
            }
        }
    }

    public List<? extends ImageMetadata.ImageMetadataItem> getDirectories() {
        return super.getItems();
    }

    @Override // org.apache.commons.imaging.common.GenericImageMetadata, org.apache.commons.imaging.common.ImageMetadata
    public List<? extends ImageMetadata.ImageMetadataItem> getItems() {
        ArrayList arrayList = new ArrayList();
        Iterator<? extends ImageMetadata.ImageMetadataItem> it = super.getItems().iterator();
        while (it.hasNext()) {
            arrayList.addAll(((Directory) it.next()).getItems());
        }
        return arrayList;
    }

    public static class TiffMetadataItem extends GenericImageMetadata.GenericImageMetadataItem {
        private final TiffField entry;

        public TiffMetadataItem(TiffField tiffField) {
            super(tiffField.getTagName(), tiffField.getValueDescription());
            this.entry = tiffField;
        }

        public TiffField getTiffField() {
            return this.entry;
        }
    }

    public TiffOutputSet getOutputSet() throws ImageWriteException {
        ByteOrder byteOrder = this.contents.header.byteOrder;
        TiffOutputSet tiffOutputSet = new TiffOutputSet(byteOrder);
        Iterator<? extends ImageMetadata.ImageMetadataItem> it = getDirectories().iterator();
        while (it.hasNext()) {
            Directory directory = (Directory) it.next();
            if (tiffOutputSet.findDirectory(directory.type) == null) {
                tiffOutputSet.addDirectory(directory.getOutputDirectory(byteOrder));
            }
        }
        return tiffOutputSet;
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        return findField(tagInfo, false);
    }

    public TiffField findField(TagInfo tagInfo, boolean z) throws ImageReadException {
        TiffField tiffFieldFindField;
        TiffField tiffFieldFindField2;
        Integer tagCount = TiffTags.getTagCount(tagInfo.tag);
        int iIntValue = tagCount == null ? 0 : tagCount.intValue();
        List<? extends ImageMetadata.ImageMetadataItem> directories = getDirectories();
        if (z || tagInfo.directoryType != TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN) {
            Iterator<? extends ImageMetadata.ImageMetadataItem> it = directories.iterator();
            while (it.hasNext()) {
                Directory directory = (Directory) it.next();
                if (directory.type == tagInfo.directoryType.directoryType && (tiffFieldFindField2 = directory.findField(tagInfo)) != null) {
                    return tiffFieldFindField2;
                }
            }
            if (z || iIntValue > 1) {
                return null;
            }
            Iterator<? extends ImageMetadata.ImageMetadataItem> it2 = directories.iterator();
            while (it2.hasNext()) {
                Directory directory2 = (Directory) it2.next();
                if (tagInfo.directoryType.isImageDirectory() && directory2.type >= 0) {
                    TiffField tiffFieldFindField3 = directory2.findField(tagInfo);
                    if (tiffFieldFindField3 != null) {
                        return tiffFieldFindField3;
                    }
                } else if (!tagInfo.directoryType.isImageDirectory() && directory2.type < 0 && (tiffFieldFindField = directory2.findField(tagInfo)) != null) {
                    return tiffFieldFindField;
                }
            }
        }
        Iterator<? extends ImageMetadata.ImageMetadataItem> it3 = directories.iterator();
        while (it3.hasNext()) {
            TiffField tiffFieldFindField4 = ((Directory) it3.next()).findField(tagInfo);
            if (tiffFieldFindField4 != null) {
                return tiffFieldFindField4;
            }
        }
        return null;
    }

    public Object getFieldValue(TagInfo tagInfo) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfo);
        if (tiffFieldFindField == null) {
            return null;
        }
        return tiffFieldFindField.getValue();
    }

    public byte[] getFieldValue(TagInfoByte tagInfoByte) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoByte);
        if (tiffFieldFindField != null && tagInfoByte.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tiffFieldFindField.getByteArrayValue();
        }
        return null;
    }

    public String[] getFieldValue(TagInfoAscii tagInfoAscii) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoAscii);
        if (tiffFieldFindField == null || !tagInfoAscii.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoAscii.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public short[] getFieldValue(TagInfoShort tagInfoShort) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoShort);
        if (tiffFieldFindField == null || !tagInfoShort.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoShort.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public int[] getFieldValue(TagInfoLong tagInfoLong) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoLong);
        if (tiffFieldFindField == null || !tagInfoLong.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoLong.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public RationalNumber[] getFieldValue(TagInfoRational tagInfoRational) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoRational);
        if (tiffFieldFindField == null || !tagInfoRational.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoRational.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public byte[] getFieldValue(TagInfoSByte tagInfoSByte) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSByte);
        if (tiffFieldFindField != null && tagInfoSByte.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return tiffFieldFindField.getByteArrayValue();
        }
        return null;
    }

    public short[] getFieldValue(TagInfoSShort tagInfoSShort) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSShort);
        if (tiffFieldFindField == null || !tagInfoSShort.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoSShort.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public int[] getFieldValue(TagInfoSLong tagInfoSLong) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSLong);
        if (tiffFieldFindField == null || !tagInfoSLong.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoSLong.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public RationalNumber[] getFieldValue(TagInfoSRational tagInfoSRational) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoSRational);
        if (tiffFieldFindField == null || !tagInfoSRational.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoSRational.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public float[] getFieldValue(TagInfoFloat tagInfoFloat) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoFloat);
        if (tiffFieldFindField == null || !tagInfoFloat.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoFloat.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public double[] getFieldValue(TagInfoDouble tagInfoDouble) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoDouble);
        if (tiffFieldFindField == null || !tagInfoDouble.dataTypes.contains(tiffFieldFindField.getFieldType())) {
            return null;
        }
        return tagInfoDouble.getValue(tiffFieldFindField.getByteOrder(), tiffFieldFindField.getByteArrayValue());
    }

    public String getFieldValue(TagInfoGpsText tagInfoGpsText) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoGpsText);
        if (tiffFieldFindField == null) {
            return null;
        }
        return tagInfoGpsText.getValue(tiffFieldFindField);
    }

    public String getFieldValue(TagInfoXpString tagInfoXpString) throws ImageReadException {
        TiffField tiffFieldFindField = findField(tagInfoXpString);
        if (tiffFieldFindField == null) {
            return null;
        }
        return tagInfoXpString.getValue(tiffFieldFindField);
    }

    public TiffDirectory findDirectory(int i) {
        Iterator<? extends ImageMetadata.ImageMetadataItem> it = getDirectories().iterator();
        while (it.hasNext()) {
            Directory directory = (Directory) it.next();
            if (directory.type == i) {
                return directory.directory;
            }
        }
        return null;
    }

    public List<TiffField> getAllFields() {
        ArrayList arrayList = new ArrayList();
        Iterator<? extends ImageMetadata.ImageMetadataItem> it = getDirectories().iterator();
        while (it.hasNext()) {
            arrayList.addAll(((Directory) it.next()).getAllFields());
        }
        return arrayList;
    }

    public GPSInfo getGPS() throws ImageReadException {
        TiffDirectory tiffDirectoryFindDirectory = findDirectory(-3);
        if (tiffDirectoryFindDirectory == null) {
            return null;
        }
        TiffField tiffFieldFindField = tiffDirectoryFindDirectory.findField(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
        TiffField tiffFieldFindField2 = tiffDirectoryFindDirectory.findField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
        TiffField tiffFieldFindField3 = tiffDirectoryFindDirectory.findField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
        TiffField tiffFieldFindField4 = tiffDirectoryFindDirectory.findField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
        if (tiffFieldFindField == null || tiffFieldFindField2 == null || tiffFieldFindField3 == null || tiffFieldFindField4 == null) {
            return null;
        }
        String stringValue = tiffFieldFindField.getStringValue();
        RationalNumber[] rationalNumberArr = (RationalNumber[]) tiffFieldFindField2.getValue();
        String stringValue2 = tiffFieldFindField3.getStringValue();
        RationalNumber[] rationalNumberArr2 = (RationalNumber[]) tiffFieldFindField4.getValue();
        if (rationalNumberArr.length != 3 || rationalNumberArr2.length != 3) {
            throw new ImageReadException("Expected three values for latitude and longitude.");
        }
        return new GPSInfo(stringValue, stringValue2, rationalNumberArr[0], rationalNumberArr[1], rationalNumberArr[2], rationalNumberArr2[0], rationalNumberArr2[1], rationalNumberArr2[2]);
    }

    public static class GPSInfo {
        public final RationalNumber latitudeDegrees;
        public final RationalNumber latitudeMinutes;
        public final String latitudeRef;
        public final RationalNumber latitudeSeconds;
        public final RationalNumber longitudeDegrees;
        public final RationalNumber longitudeMinutes;
        public final String longitudeRef;
        public final RationalNumber longitudeSeconds;

        public GPSInfo(String str, String str2, RationalNumber rationalNumber, RationalNumber rationalNumber2, RationalNumber rationalNumber3, RationalNumber rationalNumber4, RationalNumber rationalNumber5, RationalNumber rationalNumber6) {
            this.latitudeRef = str;
            this.longitudeRef = str2;
            this.latitudeDegrees = rationalNumber;
            this.latitudeMinutes = rationalNumber2;
            this.latitudeSeconds = rationalNumber3;
            this.longitudeDegrees = rationalNumber4;
            this.longitudeMinutes = rationalNumber5;
            this.longitudeSeconds = rationalNumber6;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(88);
            sb.append("[GPS. Latitude: " + this.latitudeDegrees.toDisplayString() + " degrees, " + this.latitudeMinutes.toDisplayString() + " minutes, " + this.latitudeSeconds.toDisplayString() + " seconds " + this.latitudeRef);
            sb.append(", Longitude: " + this.longitudeDegrees.toDisplayString() + " degrees, " + this.longitudeMinutes.toDisplayString() + " minutes, " + this.longitudeSeconds.toDisplayString() + " seconds " + this.longitudeRef);
            sb.append(']');
            return sb.toString();
        }

        public double getLongitudeAsDegreesEast() throws ImageReadException {
            double dDoubleValue = this.longitudeDegrees.doubleValue() + (this.longitudeMinutes.doubleValue() / 60.0d) + (this.longitudeSeconds.doubleValue() / 3600.0d);
            if (this.longitudeRef.trim().equalsIgnoreCase("e")) {
                return dDoubleValue;
            }
            if (this.longitudeRef.trim().equalsIgnoreCase("w")) {
                return -dDoubleValue;
            }
            throw new ImageReadException("Unknown longitude ref: \"" + this.longitudeRef + "\"");
        }

        public double getLatitudeAsDegreesNorth() throws ImageReadException {
            double dDoubleValue = this.latitudeDegrees.doubleValue() + (this.latitudeMinutes.doubleValue() / 60.0d) + (this.latitudeSeconds.doubleValue() / 3600.0d);
            if (this.latitudeRef.trim().equalsIgnoreCase("n")) {
                return dDoubleValue;
            }
            if (this.latitudeRef.trim().equalsIgnoreCase("s")) {
                return -dDoubleValue;
            }
            throw new ImageReadException("Unknown latitude ref: \"" + this.latitudeRef + "\"");
        }
    }
}
