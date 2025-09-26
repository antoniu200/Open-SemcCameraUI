// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import java.io.IOException;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSRational;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoFloat;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoDouble;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoSByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoGpsText;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.Iterator;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.GenericImageMetadata;

public class TiffImageMetadata extends GenericImageMetadata
{
    public final TiffContents contents;
    
    public TiffImageMetadata(final TiffContents contents) {
        this.contents = contents;
    }
    
    public TiffDirectory findDirectory(final int n) {
        for (final Directory directory : this.getDirectories()) {
            if (directory.type == n) {
                return directory.directory;
            }
        }
        return null;
    }
    
    public TiffField findField(final TagInfo tagInfo) throws ImageReadException {
        return this.findField(tagInfo, false);
    }
    
    public TiffField findField(final TagInfo tagInfo, final boolean b) throws ImageReadException {
        final Integer tagCount = TiffTags.getTagCount(tagInfo.tag);
        int intValue;
        if (tagCount == null) {
            intValue = 0;
        }
        else {
            intValue = tagCount;
        }
        final List<? extends ImageMetadataItem> directories = this.getDirectories();
        if (b || tagInfo.directoryType != TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN) {
            for (final Directory directory : directories) {
                if (directory.type == tagInfo.directoryType.directoryType) {
                    final TiffField field = directory.findField(tagInfo);
                    if (field != null) {
                        return field;
                    }
                    continue;
                }
            }
            if (b || intValue > 1) {
                return null;
            }
            for (final Directory directory2 : directories) {
                if (tagInfo.directoryType.isImageDirectory() && directory2.type >= 0) {
                    final TiffField field2 = directory2.findField(tagInfo);
                    if (field2 != null) {
                        return field2;
                    }
                    continue;
                }
                else {
                    if (tagInfo.directoryType.isImageDirectory() || directory2.type >= 0) {
                        continue;
                    }
                    final TiffField field3 = directory2.findField(tagInfo);
                    if (field3 != null) {
                        return field3;
                    }
                    continue;
                }
            }
        }
        final Iterator<? extends ImageMetadataItem> iterator3 = directories.iterator();
        while (iterator3.hasNext()) {
            final TiffField field4 = ((Directory)iterator3.next()).findField(tagInfo);
            if (field4 != null) {
                return field4;
            }
        }
        return null;
    }
    
    public List<TiffField> getAllFields() {
        final ArrayList list = new ArrayList();
        final Iterator<? extends ImageMetadataItem> iterator = this.getDirectories().iterator();
        while (iterator.hasNext()) {
            list.addAll(((Directory)iterator.next()).getAllFields());
        }
        return list;
    }
    
    public List<? extends ImageMetadataItem> getDirectories() {
        return super.getItems();
    }
    
    public Object getFieldValue(final TagInfo tagInfo) throws ImageReadException {
        final TiffField field = this.findField(tagInfo);
        if (field == null) {
            return null;
        }
        return field.getValue();
    }
    
    public String getFieldValue(final TagInfoGpsText tagInfoGpsText) throws ImageReadException {
        final TiffField field = this.findField(tagInfoGpsText);
        if (field == null) {
            return null;
        }
        return tagInfoGpsText.getValue(field);
    }
    
    public String getFieldValue(final TagInfoXpString tagInfoXpString) throws ImageReadException {
        final TiffField field = this.findField(tagInfoXpString);
        if (field == null) {
            return null;
        }
        return tagInfoXpString.getValue(field);
    }
    
    public byte[] getFieldValue(final TagInfoByte tagInfoByte) throws ImageReadException {
        final TiffField field = this.findField(tagInfoByte);
        if (field == null) {
            return null;
        }
        if (!tagInfoByte.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return field.getByteArrayValue();
    }
    
    public byte[] getFieldValue(final TagInfoSByte tagInfoSByte) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSByte);
        if (field == null) {
            return null;
        }
        if (!tagInfoSByte.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return field.getByteArrayValue();
    }
    
    public double[] getFieldValue(final TagInfoDouble tagInfoDouble) throws ImageReadException {
        final TiffField field = this.findField(tagInfoDouble);
        if (field == null) {
            return null;
        }
        if (!tagInfoDouble.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoDouble.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public float[] getFieldValue(final TagInfoFloat tagInfoFloat) throws ImageReadException {
        final TiffField field = this.findField(tagInfoFloat);
        if (field == null) {
            return null;
        }
        if (!tagInfoFloat.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoFloat.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public int[] getFieldValue(final TagInfoLong tagInfoLong) throws ImageReadException {
        final TiffField field = this.findField(tagInfoLong);
        if (field == null) {
            return null;
        }
        if (!tagInfoLong.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoLong.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public int[] getFieldValue(final TagInfoSLong tagInfoSLong) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSLong);
        if (field == null) {
            return null;
        }
        if (!tagInfoSLong.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoSLong.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public String[] getFieldValue(final TagInfoAscii tagInfoAscii) throws ImageReadException {
        final TiffField field = this.findField(tagInfoAscii);
        if (field == null) {
            return null;
        }
        if (!tagInfoAscii.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoAscii.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public RationalNumber[] getFieldValue(final TagInfoRational tagInfoRational) throws ImageReadException {
        final TiffField field = this.findField(tagInfoRational);
        if (field == null) {
            return null;
        }
        if (!tagInfoRational.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoRational.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public RationalNumber[] getFieldValue(final TagInfoSRational tagInfoSRational) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSRational);
        if (field == null) {
            return null;
        }
        if (!tagInfoSRational.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoSRational.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public short[] getFieldValue(final TagInfoSShort tagInfoSShort) throws ImageReadException {
        final TiffField field = this.findField(tagInfoSShort);
        if (field == null) {
            return null;
        }
        if (!tagInfoSShort.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoSShort.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public short[] getFieldValue(final TagInfoShort tagInfoShort) throws ImageReadException {
        final TiffField field = this.findField(tagInfoShort);
        if (field == null) {
            return null;
        }
        if (!tagInfoShort.dataTypes.contains(field.getFieldType())) {
            return null;
        }
        return tagInfoShort.getValue(field.getByteOrder(), field.getByteArrayValue());
    }
    
    public GPSInfo getGPS() throws ImageReadException {
        final TiffDirectory directory = this.findDirectory(-3);
        if (directory == null) {
            return null;
        }
        final TiffField field = directory.findField(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF);
        final TiffField field2 = directory.findField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
        final TiffField field3 = directory.findField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF);
        final TiffField field4 = directory.findField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
        if (field == null || field2 == null || field3 == null || field4 == null) {
            return null;
        }
        final String stringValue = field.getStringValue();
        final RationalNumber[] array = (RationalNumber[])field2.getValue();
        final String stringValue2 = field3.getStringValue();
        final RationalNumber[] array2 = (RationalNumber[])field4.getValue();
        if (array.length == 3 && array2.length == 3) {
            return new GPSInfo(stringValue, stringValue2, array[0], array[1], array[2], array2[0], array2[1], array2[2]);
        }
        throw new ImageReadException("Expected three values for latitude and longitude.");
    }
    
    @Override
    public List<? extends ImageMetadataItem> getItems() {
        final ArrayList list = new ArrayList();
        final Iterator<? extends ImageMetadataItem> iterator = super.getItems().iterator();
        while (iterator.hasNext()) {
            list.addAll(((Directory)iterator.next()).getItems());
        }
        return list;
    }
    
    public TiffOutputSet getOutputSet() throws ImageWriteException {
        final ByteOrder byteOrder = this.contents.header.byteOrder;
        final TiffOutputSet set = new TiffOutputSet(byteOrder);
        for (final Directory directory : this.getDirectories()) {
            if (set.findDirectory(directory.type) != null) {
                continue;
            }
            set.addDirectory(directory.getOutputDirectory(byteOrder));
        }
        return set;
    }
    
    public static class Directory extends GenericImageMetadata implements ImageMetadataItem
    {
        private final ByteOrder byteOrder;
        private final TiffDirectory directory;
        public final int type;
        
        public Directory(final ByteOrder byteOrder, final TiffDirectory directory) {
            this.type = directory.type;
            this.directory = directory;
            this.byteOrder = byteOrder;
        }
        
        public void add(final TiffField tiffField) {
            this.add(new TiffMetadataItem(tiffField));
        }
        
        public TiffField findField(final TagInfo tagInfo) throws ImageReadException {
            return this.directory.findField(tagInfo);
        }
        
        public List<TiffField> getAllFields() {
            return this.directory.getDirectoryEntries();
        }
        
        public JpegImageData getJpegImageData() {
            return this.directory.getJpegImageData();
        }
        
        public TiffOutputDirectory getOutputDirectory(final ByteOrder byteOrder) throws ImageWriteException {
            try {
                final TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(this.type, byteOrder);
                final Iterator<? extends ImageMetadataItem> iterator = this.getItems().iterator();
                while (iterator.hasNext()) {
                    final TiffField tiffField = ((TiffMetadataItem)iterator.next()).getTiffField();
                    if (tiffOutputDirectory.findField(tiffField.getTag()) != null) {
                        continue;
                    }
                    if (tiffField.getTagInfo().isOffset()) {
                        continue;
                    }
                    final TagInfo tagInfo = tiffField.getTagInfo();
                    final FieldType fieldType = tiffField.getFieldType();
                    final byte[] encodeValue = tagInfo.encodeValue(fieldType, tiffField.getValue(), byteOrder);
                    final TiffOutputField tiffOutputField = new TiffOutputField(tiffField.getTag(), tagInfo, fieldType, encodeValue.length / fieldType.getSize(), encodeValue);
                    tiffOutputField.setSortHint(tiffField.getSortHint());
                    tiffOutputDirectory.add(tiffOutputField);
                }
                tiffOutputDirectory.setTiffImageData(this.getTiffImageData());
                tiffOutputDirectory.setJpegImageData(this.getJpegImageData());
                return tiffOutputDirectory;
            }
            catch (final ImageReadException ex) {
                throw new ImageWriteException(ex.getMessage(), ex);
            }
        }
        
        public BufferedImage getThumbnail() throws ImageReadException, IOException {
            return this.directory.getTiffImage(this.byteOrder);
        }
        
        public TiffImageData getTiffImageData() {
            return this.directory.getTiffImageData();
        }
        
        @Override
        public String toString(final String s) {
            final StringBuilder sb = new StringBuilder();
            String str;
            if (s != null) {
                str = s;
            }
            else {
                str = "";
            }
            sb.append(str);
            sb.append(this.directory.description());
            sb.append(": ");
            String str2;
            if (this.getTiffImageData() != null) {
                str2 = " (tiffImageData)";
            }
            else {
                str2 = "";
            }
            sb.append(str2);
            String str3;
            if (this.getJpegImageData() != null) {
                str3 = " (jpegImageData)";
            }
            else {
                str3 = "";
            }
            sb.append(str3);
            sb.append("\n");
            sb.append(super.toString(s));
            sb.append("\n");
            return sb.toString();
        }
    }
    
    public static class GPSInfo
    {
        public final RationalNumber latitudeDegrees;
        public final RationalNumber latitudeMinutes;
        public final String latitudeRef;
        public final RationalNumber latitudeSeconds;
        public final RationalNumber longitudeDegrees;
        public final RationalNumber longitudeMinutes;
        public final String longitudeRef;
        public final RationalNumber longitudeSeconds;
        
        public GPSInfo(final String latitudeRef, final String longitudeRef, final RationalNumber latitudeDegrees, final RationalNumber latitudeMinutes, final RationalNumber latitudeSeconds, final RationalNumber longitudeDegrees, final RationalNumber longitudeMinutes, final RationalNumber longitudeSeconds) {
            this.latitudeRef = latitudeRef;
            this.longitudeRef = longitudeRef;
            this.latitudeDegrees = latitudeDegrees;
            this.latitudeMinutes = latitudeMinutes;
            this.latitudeSeconds = latitudeSeconds;
            this.longitudeDegrees = longitudeDegrees;
            this.longitudeMinutes = longitudeMinutes;
            this.longitudeSeconds = longitudeSeconds;
        }
        
        public double getLatitudeAsDegreesNorth() throws ImageReadException {
            final double n = this.latitudeDegrees.doubleValue() + this.latitudeMinutes.doubleValue() / 60.0 + this.latitudeSeconds.doubleValue() / 3600.0;
            if (this.latitudeRef.trim().equalsIgnoreCase("n")) {
                return n;
            }
            if (this.latitudeRef.trim().equalsIgnoreCase("s")) {
                return -n;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown latitude ref: \"");
            sb.append(this.latitudeRef);
            sb.append("\"");
            throw new ImageReadException(sb.toString());
        }
        
        public double getLongitudeAsDegreesEast() throws ImageReadException {
            final double n = this.longitudeDegrees.doubleValue() + this.longitudeMinutes.doubleValue() / 60.0 + this.longitudeSeconds.doubleValue() / 3600.0;
            if (this.longitudeRef.trim().equalsIgnoreCase("e")) {
                return n;
            }
            if (this.longitudeRef.trim().equalsIgnoreCase("w")) {
                return -n;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown longitude ref: \"");
            sb.append(this.longitudeRef);
            sb.append("\"");
            throw new ImageReadException(sb.toString());
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(88);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("[GPS. Latitude: ");
            sb2.append(this.latitudeDegrees.toDisplayString());
            sb2.append(" degrees, ");
            sb2.append(this.latitudeMinutes.toDisplayString());
            sb2.append(" minutes, ");
            sb2.append(this.latitudeSeconds.toDisplayString());
            sb2.append(" seconds ");
            sb2.append(this.latitudeRef);
            sb.append(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(", Longitude: ");
            sb3.append(this.longitudeDegrees.toDisplayString());
            sb3.append(" degrees, ");
            sb3.append(this.longitudeMinutes.toDisplayString());
            sb3.append(" minutes, ");
            sb3.append(this.longitudeSeconds.toDisplayString());
            sb3.append(" seconds ");
            sb3.append(this.longitudeRef);
            sb.append(sb3.toString());
            sb.append(']');
            return sb.toString();
        }
    }
    
    public static class TiffMetadataItem extends GenericImageMetadataItem
    {
        private final TiffField entry;
        
        public TiffMetadataItem(final TiffField entry) {
            super(entry.getTagName(), entry.getValueDescription());
            this.entry = entry;
        }
        
        public TiffField getTiffField() {
            return this.entry;
        }
    }
}
