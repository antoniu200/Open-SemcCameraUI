// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
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
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.io.File;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;

public class JpegMetadata
{
    private TiffImageMetadata mMetadata;
    private TiffOutputSet mOutput;
    
    JpegMetadata() {
    }
    
    JpegMetadata(final File file) throws ImageReadException, IOException {
        this.mMetadata = this.parse(new ByteSourceFile(file));
    }
    
    JpegMetadata(final InputStream inputStream) throws ImageReadException, IOException {
        this.mMetadata = this.parse(new ByteSourceInputStream(inputStream, null));
    }
    
    private void checkHasRoot() throws ImageWriteException {
        if (this.getOutputSet().getRootDirectory() == null) {
            this.getOutputSet().addRootDirectory();
        }
    }
    
    private TiffOutputDirectory getDirectoryForTag(final TagInfo tagInfo) throws ImageWriteException {
        final TiffOutputSet outputSet = this.getOutputSet();
        int directoryType;
        if (tagInfo.directoryType == TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN) {
            directoryType = -2;
        }
        else {
            directoryType = tagInfo.directoryType.directoryType;
        }
        this.checkHasRoot();
        TiffOutputDirectory directory;
        if ((directory = outputSet.findDirectory(directoryType)) == null) {
            directory = new TiffOutputDirectory(directoryType, outputSet.byteOrder);
            outputSet.addDirectory(directory);
        }
        return directory;
    }
    
    private TiffOutputSet getOutputSet() throws ImageWriteException {
        if (this.mOutput == null) {
            if (this.mMetadata == null) {
                this.mOutput = new TiffOutputSet();
            }
            else {
                this.mOutput = this.mMetadata.getOutputSet();
            }
        }
        return this.mOutput;
    }
    
    private TiffImageMetadata parse(final ByteSource byteSource) throws ImageReadException, IOException {
        return new JpegImageParser().getExifMetadata(byteSource, new HashMap<String, Object>());
    }
    
    private void rewrite(final ByteSource byteSource, final OutputStream outputStream, final boolean b) throws ImageReadException, ImageWriteException, IOException {
        final ExifRewriter exifRewriter = new ExifRewriter();
        if (this.hasMetadata()) {
            if (b) {
                exifRewriter.updateExifMetadataLossy(byteSource, outputStream, this.getOutputSet());
            }
            else {
                exifRewriter.updateExifMetadataLossless(byteSource, outputStream, this.getOutputSet());
            }
        }
        else {
            exifRewriter.removeExifMetadata(byteSource, outputStream);
        }
    }
    
    void add(final TagInfoAscii tagInfoAscii, final String... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoAscii).add(tagInfoAscii, array);
    }
    
    void add(final TagInfoAsciiOrByte tagInfoAsciiOrByte, final String... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoAsciiOrByte).add(tagInfoAsciiOrByte, array);
    }
    
    void add(final TagInfoAsciiOrRational tagInfoAsciiOrRational, final String... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoAsciiOrRational).add(tagInfoAsciiOrRational, array);
    }
    
    void add(final TagInfoAsciiOrRational tagInfoAsciiOrRational, final RationalNumber... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoAsciiOrRational).add(tagInfoAsciiOrRational, array);
    }
    
    void add(final TagInfoByte tagInfoByte, final byte... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoByte).add(tagInfoByte, array);
    }
    
    void add(final TagInfoByteOrShort tagInfoByteOrShort, final byte... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoByteOrShort).add(tagInfoByteOrShort, array);
    }
    
    void add(final TagInfoByteOrShort tagInfoByteOrShort, final short... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoByteOrShort).add(tagInfoByteOrShort, array);
    }
    
    void add(final TagInfoDouble tagInfoDouble, final double... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoDouble).add(tagInfoDouble, array);
    }
    
    void add(final TagInfoFloat tagInfoFloat, final float... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoFloat).add(tagInfoFloat, array);
    }
    
    void add(final TagInfoGpsText tagInfoGpsText, final String s) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoGpsText).add(tagInfoGpsText, s);
    }
    
    void add(final TagInfoLong tagInfoLong, final int... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoLong).add(tagInfoLong, array);
    }
    
    void add(final TagInfoRational tagInfoRational, final RationalNumber... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoRational).add(tagInfoRational, array);
    }
    
    void add(final TagInfoSByte tagInfoSByte, final byte... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoSByte).add(tagInfoSByte, array);
    }
    
    void add(final TagInfoSLong tagInfoSLong, final int... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoSLong).add(tagInfoSLong, array);
    }
    
    void add(final TagInfoSRational tagInfoSRational, final RationalNumber... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoSRational).add(tagInfoSRational, array);
    }
    
    void add(final TagInfoSShort tagInfoSShort, final short... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoSShort).add(tagInfoSShort, array);
    }
    
    void add(final TagInfoShort tagInfoShort, final short... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShort).add(tagInfoShort, array);
    }
    
    void add(final TagInfoShortOrLong tagInfoShortOrLong, final int... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShortOrLong).add(tagInfoShortOrLong, array);
    }
    
    void add(final TagInfoShortOrLong tagInfoShortOrLong, final short... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShortOrLong).add(tagInfoShortOrLong, array);
    }
    
    void add(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final int... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShortOrLongOrRational).add(tagInfoShortOrLongOrRational, array);
    }
    
    void add(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final RationalNumber... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShortOrLongOrRational).add(tagInfoShortOrLongOrRational, array);
    }
    
    void add(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final short... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShortOrLongOrRational).add(tagInfoShortOrLongOrRational, array);
    }
    
    void add(final TagInfoShortOrRational tagInfoShortOrRational, final RationalNumber... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShortOrRational).add(tagInfoShortOrRational, array);
    }
    
    void add(final TagInfoShortOrRational tagInfoShortOrRational, final short... array) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoShortOrRational).add(tagInfoShortOrRational, array);
    }
    
    void add(final TagInfoXpString tagInfoXpString, final String s) throws ImageWriteException {
        this.getDirectoryForTag(tagInfoXpString).add(tagInfoXpString, s);
    }
    
    void add(final TiffOutputField tiffOutputField) throws ImageWriteException {
        this.getDirectoryForTag(tiffOutputField.tagInfo).add(tiffOutputField);
    }
    
    boolean contains(final TagInfo tagInfo) throws ImageReadException {
        final TiffOutputSet mOutput = this.mOutput;
        final boolean b = false;
        if (mOutput != null) {
            final boolean b2 = b;
            if (this.mOutput.findField(tagInfo) == null) {
                return b2;
            }
        }
        else {
            boolean b2 = b;
            if (this.mMetadata == null) {
                return b2;
            }
            b2 = b;
            if (this.mMetadata.findField(tagInfo) == null) {
                return b2;
            }
        }
        return true;
    }
    
    TiffField getOriginalInputField(final TagInfo tagInfo) throws ImageReadException {
        TiffField field;
        if (this.mMetadata != null) {
            field = this.mMetadata.findField(tagInfo, false);
        }
        else {
            field = null;
        }
        return field;
    }
    
    boolean hasMetadata() {
        final TiffImageMetadata mMetadata = this.mMetadata;
        boolean b = true;
        final boolean b2 = true;
        if (mMetadata != null) {
            return this.mMetadata.findDirectory(0) != null && b2;
        }
        if (this.mOutput == null) {
            b = false;
        }
        return b;
    }
    
    boolean remove(final TagInfo tagInfo) throws ImageWriteException {
        if (this.mMetadata != null || this.mOutput != null) {
            final TiffOutputSet outputSet = this.getOutputSet();
            if (outputSet.findField(tagInfo) != null) {
                outputSet.removeField(tagInfo);
                return true;
            }
        }
        return false;
    }
    
    void rewrite(final File file, final OutputStream outputStream) throws ImageReadException, ImageWriteException, IOException {
        this.rewrite(file, outputStream, true);
    }
    
    void rewrite(final File file, final OutputStream outputStream, final boolean b) throws ImageReadException, ImageWriteException, IOException {
        this.rewrite(new ByteSourceFile(file), outputStream, b);
    }
    
    void rewrite(final InputStream inputStream, final OutputStream outputStream) throws ImageReadException, ImageWriteException, IOException {
        this.rewrite(inputStream, outputStream, true);
    }
    
    void rewrite(final InputStream inputStream, final OutputStream outputStream, final boolean b) throws ImageReadException, ImageWriteException, IOException {
        this.rewrite(new ByteSourceInputStream(inputStream, null), outputStream, b);
    }
    
    void set(final TagInfoAscii tagInfoAscii, final String... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoAscii);
        directoryForTag.removeField(tagInfoAscii);
        directoryForTag.add(tagInfoAscii, array);
    }
    
    void set(final TagInfoAsciiOrByte tagInfoAsciiOrByte, final String... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoAsciiOrByte);
        directoryForTag.removeField(tagInfoAsciiOrByte);
        directoryForTag.add(tagInfoAsciiOrByte, array);
    }
    
    void set(final TagInfoAsciiOrRational tagInfoAsciiOrRational, final String... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoAsciiOrRational);
        directoryForTag.removeField(tagInfoAsciiOrRational);
        directoryForTag.add(tagInfoAsciiOrRational, array);
    }
    
    void set(final TagInfoAsciiOrRational tagInfoAsciiOrRational, final RationalNumber... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoAsciiOrRational);
        directoryForTag.removeField(tagInfoAsciiOrRational);
        directoryForTag.add(tagInfoAsciiOrRational, array);
    }
    
    void set(final TagInfoByte tagInfoByte, final byte... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoByte);
        directoryForTag.removeField(tagInfoByte);
        directoryForTag.add(tagInfoByte, array);
    }
    
    void set(final TagInfoByteOrShort tagInfoByteOrShort, final byte... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoByteOrShort);
        directoryForTag.removeField(tagInfoByteOrShort);
        directoryForTag.add(tagInfoByteOrShort, array);
    }
    
    void set(final TagInfoByteOrShort tagInfoByteOrShort, final short... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoByteOrShort);
        directoryForTag.removeField(tagInfoByteOrShort);
        directoryForTag.add(tagInfoByteOrShort, array);
    }
    
    void set(final TagInfoDouble tagInfoDouble, final double... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoDouble);
        directoryForTag.removeField(tagInfoDouble);
        directoryForTag.add(tagInfoDouble, array);
    }
    
    void set(final TagInfoFloat tagInfoFloat, final float... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoFloat);
        directoryForTag.removeField(tagInfoFloat);
        directoryForTag.add(tagInfoFloat, array);
    }
    
    void set(final TagInfoGpsText tagInfoGpsText, final String s) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoGpsText);
        directoryForTag.removeField(tagInfoGpsText);
        directoryForTag.add(tagInfoGpsText, s);
    }
    
    void set(final TagInfoLong tagInfoLong, final int... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoLong);
        directoryForTag.removeField(tagInfoLong);
        directoryForTag.add(tagInfoLong, array);
    }
    
    void set(final TagInfoRational tagInfoRational, final RationalNumber... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoRational);
        directoryForTag.removeField(tagInfoRational);
        directoryForTag.add(tagInfoRational, array);
    }
    
    void set(final TagInfoSByte tagInfoSByte, final byte... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoSByte);
        directoryForTag.removeField(tagInfoSByte);
        directoryForTag.add(tagInfoSByte, array);
    }
    
    void set(final TagInfoSLong tagInfoSLong, final int... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoSLong);
        directoryForTag.removeField(tagInfoSLong);
        directoryForTag.add(tagInfoSLong, array);
    }
    
    void set(final TagInfoSRational tagInfoSRational, final RationalNumber... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoSRational);
        directoryForTag.removeField(tagInfoSRational);
        directoryForTag.add(tagInfoSRational, array);
    }
    
    void set(final TagInfoSShort tagInfoSShort, final short... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoSShort);
        directoryForTag.removeField(tagInfoSShort);
        directoryForTag.add(tagInfoSShort, array);
    }
    
    void set(final TagInfoShort tagInfoShort, final short... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShort);
        directoryForTag.removeField(tagInfoShort);
        directoryForTag.add(tagInfoShort, array);
    }
    
    void set(final TagInfoShortOrLong tagInfoShortOrLong, final int... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShortOrLong);
        directoryForTag.removeField(tagInfoShortOrLong);
        directoryForTag.add(tagInfoShortOrLong, array);
    }
    
    void set(final TagInfoShortOrLong tagInfoShortOrLong, final short... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShortOrLong);
        directoryForTag.removeField(tagInfoShortOrLong);
        directoryForTag.add(tagInfoShortOrLong, array);
    }
    
    void set(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final int... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShortOrLongOrRational);
        directoryForTag.removeField(tagInfoShortOrLongOrRational);
        directoryForTag.add(tagInfoShortOrLongOrRational, array);
    }
    
    void set(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final RationalNumber... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShortOrLongOrRational);
        directoryForTag.removeField(tagInfoShortOrLongOrRational);
        directoryForTag.add(tagInfoShortOrLongOrRational, array);
    }
    
    void set(final TagInfoShortOrLongOrRational tagInfoShortOrLongOrRational, final short... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShortOrLongOrRational);
        directoryForTag.removeField(tagInfoShortOrLongOrRational);
        directoryForTag.add(tagInfoShortOrLongOrRational, array);
    }
    
    void set(final TagInfoShortOrRational tagInfoShortOrRational, final RationalNumber... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShortOrRational);
        directoryForTag.removeField(tagInfoShortOrRational);
        directoryForTag.add(tagInfoShortOrRational, array);
    }
    
    void set(final TagInfoShortOrRational tagInfoShortOrRational, final short... array) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoShortOrRational);
        directoryForTag.removeField(tagInfoShortOrRational);
        directoryForTag.add(tagInfoShortOrRational, array);
    }
    
    void set(final TagInfoXpString tagInfoXpString, final String s) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tagInfoXpString);
        directoryForTag.removeField(tagInfoXpString);
        directoryForTag.add(tagInfoXpString, s);
    }
    
    void set(final TiffOutputField tiffOutputField) throws ImageWriteException {
        final TiffOutputDirectory directoryForTag = this.getDirectoryForTag(tiffOutputField.tagInfo);
        directoryForTag.removeField(tiffOutputField.tagInfo);
        directoryForTag.add(tiffOutputField);
    }
}
