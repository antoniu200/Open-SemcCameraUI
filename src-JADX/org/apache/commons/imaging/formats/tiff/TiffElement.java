package org.apache.commons.imaging.formats.tiff;

import java.util.Comparator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class TiffElement {
    public static final Comparator<TiffElement> COMPARATOR = new Comparator<TiffElement>() { // from class: org.apache.commons.imaging.formats.tiff.TiffElement.1
        @Override // java.util.Comparator
        public int compare(TiffElement tiffElement, TiffElement tiffElement2) {
            if (tiffElement.offset < tiffElement2.offset) {
                return -1;
            }
            return tiffElement.offset > tiffElement2.offset ? 1 : 0;
        }
    };
    public final int length;
    public final long offset;

    public abstract String getElementDescription(boolean z);

    public TiffElement(long j, int i) {
        this.offset = j;
        this.length = i;
    }

    public String getElementDescription() {
        return getElementDescription(false);
    }

    public static abstract class DataElement extends TiffElement {
        private final byte[] data;

        public DataElement(long j, int i, byte[] bArr) {
            super(j, i);
            this.data = bArr;
        }

        public byte[] getData() {
            return this.data;
        }

        public int getDataLength() {
            return this.data.length;
        }
    }

    public static final class Stub extends TiffElement {
        public Stub(long j, int i) {
            super(j, i);
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffElement
        public String getElementDescription(boolean z) {
            return "Element, offset: " + this.offset + ", length: " + this.length + ", last: " + (this.offset + this.length);
        }
    }
}
