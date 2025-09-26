// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import java.util.List;

public class TagInfo
{
    public static final int LENGTH_UNKNOWN = -1;
    public final List<FieldType> dataTypes;
    public final TiffDirectoryType directoryType;
    private final boolean isOffset;
    public final int length;
    public final String name;
    public final int tag;
    
    public TagInfo(final String s, final int n, final List<FieldType> list, final int n2, final TiffDirectoryType tiffDirectoryType) {
        this(s, n, list, n2, tiffDirectoryType, false);
    }
    
    public TagInfo(final String name, final int tag, final List<FieldType> c, final int length, final TiffDirectoryType directoryType, final boolean isOffset) {
        this.name = name;
        this.tag = tag;
        this.dataTypes = Collections.unmodifiableList((List<? extends FieldType>)new ArrayList<FieldType>(c));
        this.length = length;
        this.directoryType = directoryType;
        this.isOffset = isOffset;
    }
    
    public TagInfo(final String s, final int n, final FieldType fieldType) {
        this(s, n, fieldType, -1, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    }
    
    public TagInfo(final String s, final int n, final FieldType fieldType, final int n2) {
        this(s, n, Arrays.asList(fieldType), n2, TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN);
    }
    
    public TagInfo(final String s, final int n, final FieldType fieldType, final int n2, final TiffDirectoryType tiffDirectoryType) {
        this(s, n, Arrays.asList(fieldType), n2, tiffDirectoryType);
    }
    
    public TagInfo(final String s, final int n, final FieldType fieldType, final int n2, final TiffDirectoryType tiffDirectoryType, final boolean b) {
        this(s, n, Arrays.asList(fieldType), n2, tiffDirectoryType, b);
    }
    
    public byte[] encodeValue(final FieldType fieldType, final Object o, final ByteOrder byteOrder) throws ImageWriteException {
        return fieldType.writeData(o, byteOrder);
    }
    
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.tag);
        sb.append(" (0x");
        sb.append(Integer.toHexString(this.tag));
        sb.append(": ");
        sb.append(this.name);
        sb.append("): ");
        return sb.toString();
    }
    
    public Object getValue(final TiffField tiffField) throws ImageReadException {
        return tiffField.getFieldType().getValue(tiffField);
    }
    
    public boolean isOffset() {
        return this.isOffset;
    }
    
    public boolean isText() {
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[TagInfo. tag: ");
        sb.append(this.tag);
        sb.append(" (0x");
        sb.append(Integer.toHexString(this.tag));
        sb.append(", name: ");
        sb.append(this.name);
        sb.append("]");
        return sb.toString();
    }
}
