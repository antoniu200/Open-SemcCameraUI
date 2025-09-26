// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.util.Arrays;
import org.apache.commons.imaging.ImageWriteException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

public class TiffOutputField
{
    private static final String NEWLINE;
    private byte[] bytes;
    public final int count;
    public final FieldType fieldType;
    private final TiffOutputItem.Value separateValueItem;
    private int sortHint;
    public final int tag;
    public final TagInfo tagInfo;
    
    static {
        NEWLINE = System.getProperty("line.separator");
    }
    
    public TiffOutputField(final int tag, final TagInfo tagInfo, final FieldType fieldType, final int count, final byte[] bytes) {
        this.sortHint = -1;
        this.tag = tag;
        this.tagInfo = tagInfo;
        this.fieldType = fieldType;
        this.count = count;
        this.bytes = bytes;
        if (this.isLocalValue()) {
            this.separateValueItem = null;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field Seperate value (");
            sb.append(tagInfo.getDescription());
            sb.append(")");
            this.separateValueItem = new TiffOutputItem.Value(sb.toString(), bytes);
        }
    }
    
    public TiffOutputField(final TagInfo tagInfo, final FieldType fieldType, final int n, final byte[] array) {
        this(tagInfo.tag, tagInfo, fieldType, n, array);
    }
    
    protected static TiffOutputField createOffsetField(final TagInfo tagInfo, final ByteOrder byteOrder) throws ImageWriteException {
        return new TiffOutputField(tagInfo, FieldType.LONG, 1, FieldType.LONG.writeData(0, byteOrder));
    }
    
    public boolean bytesEqual(final byte[] a2) {
        return Arrays.equals(this.bytes, a2);
    }
    
    protected TiffOutputItem getSeperateValue() {
        return this.separateValueItem;
    }
    
    public int getSortHint() {
        return this.sortHint;
    }
    
    protected final boolean isLocalValue() {
        return this.bytes.length <= 4;
    }
    
    protected void setData(final byte[] bytes) throws ImageWriteException {
        if (this.bytes.length != bytes.length) {
            throw new ImageWriteException("Cannot change size of value.");
        }
        this.bytes = bytes;
        if (this.separateValueItem != null) {
            this.separateValueItem.updateValue(bytes);
        }
    }
    
    public void setSortHint(final int sortHint) {
        this.sortHint = sortHint;
    }
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    public String toString(final String s) {
        String str = s;
        if (s == null) {
            str = "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(this.tagInfo);
        sb.append(TiffOutputField.NEWLINE);
        sb.append(str);
        sb.append("count: ");
        sb.append(this.count);
        sb.append(TiffOutputField.NEWLINE);
        sb.append(str);
        sb.append(this.fieldType);
        sb.append(TiffOutputField.NEWLINE);
        return sb.toString();
    }
    
    protected void writeField(final BinaryOutputStream binaryOutputStream) throws IOException, ImageWriteException {
        binaryOutputStream.write2Bytes(this.tag);
        binaryOutputStream.write2Bytes(this.fieldType.getType());
        binaryOutputStream.write4Bytes(this.count);
        if (this.isLocalValue()) {
            if (this.separateValueItem != null) {
                throw new ImageWriteException("Unexpected separate value item.");
            }
            if (this.bytes.length > 4) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Local value has invalid length: ");
                sb.append(this.bytes.length);
                throw new ImageWriteException(sb.toString());
            }
            binaryOutputStream.write(this.bytes);
            for (int length = this.bytes.length, i = 0; i < 4 - length; ++i) {
                binaryOutputStream.write(0);
            }
        }
        else {
            if (this.separateValueItem == null) {
                throw new ImageWriteException("Missing separate value item.");
            }
            binaryOutputStream.write4Bytes((int)this.separateValueItem.getOffset());
        }
    }
}
