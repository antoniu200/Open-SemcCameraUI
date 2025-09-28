package org.apache.commons.imaging.formats.tiff.taginfos;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TagInfo {
    public static final int LENGTH_UNKNOWN = -1;
    public final List<FieldType> dataTypes;
    public final TiffDirectoryType directoryType;
    private final boolean isOffset;
    public final int length;
    public final String name;
    public final int tag;

    public boolean isText() {
        return false;
    }

    public TagInfo(String str, int i, FieldType fieldType, int i2, TiffDirectoryType tiffDirectoryType) {
        this(str, i, (List<FieldType>) Arrays.asList(fieldType), i2, tiffDirectoryType);
    }

    public TagInfo(String str, int i, FieldType fieldType, int i2, TiffDirectoryType tiffDirectoryType, boolean z) {
        this(str, i, (List<FieldType>) Arrays.asList(fieldType), i2, tiffDirectoryType, z);
    }

    public TagInfo(String str, int i, FieldType fieldType, int i2) {
        this(str, i, (List<FieldType>) Arrays.asList(fieldType), i2, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String str, int i, FieldType fieldType) {
        this(str, i, fieldType, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    }

    public TagInfo(String str, int i, List<FieldType> list, int i2, TiffDirectoryType tiffDirectoryType) {
        this(str, i, list, i2, tiffDirectoryType, false);
    }

    public TagInfo(String str, int i, List<FieldType> list, int i2, TiffDirectoryType tiffDirectoryType, boolean z) {
        this.name = str;
        this.tag = i;
        this.dataTypes = Collections.unmodifiableList(new ArrayList(list));
        this.length = i2;
        this.directoryType = tiffDirectoryType;
        this.isOffset = z;
    }

    public Object getValue(TiffField tiffField) throws ImageReadException {
        return tiffField.getFieldType().getValue(tiffField);
    }

    public byte[] encodeValue(FieldType fieldType, Object obj, ByteOrder byteOrder) throws ImageWriteException {
        return fieldType.writeData(obj, byteOrder);
    }

    public String getDescription() {
        return this.tag + " (0x" + Integer.toHexString(this.tag) + ": " + this.name + "): ";
    }

    public String toString() {
        return "[TagInfo. tag: " + this.tag + " (0x" + Integer.toHexString(this.tag) + ", name: " + this.name + "]";
    }

    public boolean isOffset() {
        return this.isOffset;
    }
}
