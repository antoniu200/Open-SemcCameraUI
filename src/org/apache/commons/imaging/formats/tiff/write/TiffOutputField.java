package org.apache.commons.imaging.formats.tiff.write;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffOutputField {
    private static final String NEWLINE = System.getProperty("line.separator");
    private byte[] bytes;
    public final int count;
    public final FieldType fieldType;
    private final TiffOutputItem.Value separateValueItem;
    private int sortHint;
    public final int tag;
    public final TagInfo tagInfo;

    public TiffOutputField(TagInfo tagInfo, FieldType fieldType, int i, byte[] bArr) {
        this(tagInfo.tag, tagInfo, fieldType, i, bArr);
    }

    public TiffOutputField(int i, TagInfo tagInfo, FieldType fieldType, int i2, byte[] bArr) {
        this.sortHint = -1;
        this.tag = i;
        this.tagInfo = tagInfo;
        this.fieldType = fieldType;
        this.count = i2;
        this.bytes = bArr;
        if (isLocalValue()) {
            this.separateValueItem = null;
            return;
        }
        this.separateValueItem = new TiffOutputItem.Value("Field Seperate value (" + tagInfo.getDescription() + ")", bArr);
    }

    protected static TiffOutputField createOffsetField(TagInfo tagInfo, ByteOrder byteOrder) throws ImageWriteException {
        return new TiffOutputField(tagInfo, FieldType.LONG, 1, FieldType.LONG.writeData(0, byteOrder));
    }

    protected void writeField(BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        binaryOutputStream.write2Bytes(this.tag);
        binaryOutputStream.write2Bytes(this.fieldType.getType());
        binaryOutputStream.write4Bytes(this.count);
        if (isLocalValue()) {
            if (this.separateValueItem != null) {
                throw new ImageWriteException("Unexpected separate value item.");
            }
            if (this.bytes.length > 4) {
                throw new ImageWriteException("Local value has invalid length: " + this.bytes.length);
            }
            binaryOutputStream.write(this.bytes);
            int length = 4 - this.bytes.length;
            for (int i = 0; i < length; i++) {
                binaryOutputStream.write(0);
            }
            return;
        }
        if (this.separateValueItem == null) {
            throw new ImageWriteException("Missing separate value item.");
        }
        binaryOutputStream.write4Bytes((int) this.separateValueItem.getOffset());
    }

    protected TiffOutputItem getSeperateValue() {
        return this.separateValueItem;
    }

    protected final boolean isLocalValue() {
        return this.bytes.length <= 4;
    }

    public boolean bytesEqual(byte[] bArr) {
        return Arrays.equals(this.bytes, bArr);
    }

    protected void setData(byte[] bArr) throws ImageWriteException {
        if (this.bytes.length != bArr.length) {
            throw new ImageWriteException("Cannot change size of value.");
        }
        this.bytes = bArr;
        if (this.separateValueItem != null) {
            this.separateValueItem.updateValue(bArr);
        }
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            str = "";
        }
        return str + this.tagInfo + NEWLINE + str + "count: " + this.count + NEWLINE + str + this.fieldType + NEWLINE;
    }

    public int getSortHint() {
        return this.sortHint;
    }

    public void setSortHint(int i) {
        this.sortHint = i;
    }
}
