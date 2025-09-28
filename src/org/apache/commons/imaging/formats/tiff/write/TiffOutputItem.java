package org.apache.commons.imaging.formats.tiff.write;

import java.io.IOException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryOutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class TiffOutputItem {
    public static final long UNDEFINED_VALUE = -1;
    private long offset = -1;

    public abstract String getItemDescription();

    public abstract int getItemLength();

    public abstract void writeItem(BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException;

    TiffOutputItem() {
    }

    protected long getOffset() {
        return this.offset;
    }

    protected void setOffset(long j) {
        this.offset = j;
    }

    public static class Value extends TiffOutputItem {
        private final byte[] bytes;
        private final String name;

        public Value(String str, byte[] bArr) {
            this.name = str;
            this.bytes = bArr;
        }

        @Override // org.apache.commons.imaging.formats.tiff.write.TiffOutputItem
        public int getItemLength() {
            return this.bytes.length;
        }

        @Override // org.apache.commons.imaging.formats.tiff.write.TiffOutputItem
        public String getItemDescription() {
            return this.name;
        }

        public void updateValue(byte[] bArr) throws ImageWriteException {
            if (this.bytes.length != bArr.length) {
                throw new ImageWriteException("Updated data size mismatch: " + this.bytes.length + " vs. " + bArr.length);
            }
            System.arraycopy(bArr, 0, this.bytes, 0, bArr.length);
        }

        @Override // org.apache.commons.imaging.formats.tiff.write.TiffOutputItem
        public void writeItem(BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
            binaryOutputStream.write(this.bytes);
        }
    }
}
