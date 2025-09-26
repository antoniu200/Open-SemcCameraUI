package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DhtSegment extends Segment {
    public final List<HuffmanTable> huffmanTables;

    public static class HuffmanTable {
        private final int[] bits;
        public final int destinationIdentifier;
        private final int[] huffCode;
        private final int[] huffVal;
        public final int tableClass;
        private final int[] huffSize = new int[4096];
        private final int[] minCode = new int[17];
        private final int[] maxCode = new int[17];
        private final int[] valPtr = new int[17];

        public HuffmanTable(int i, int i2, int[] iArr, int[] iArr2) {
            this.tableClass = i;
            this.destinationIdentifier = i2;
            this.bits = iArr;
            this.huffVal = iArr2;
            int i3 = 0;
            int i4 = 0;
            int i5 = 1;
            int i6 = 1;
            while (true) {
                if (i5 > iArr[i6]) {
                    i6++;
                    if (i6 > 16) {
                        break;
                    } else {
                        i5 = 1;
                    }
                } else {
                    this.huffSize[i4] = i6;
                    i4++;
                    i5++;
                }
            }
            this.huffSize[i4] = 0;
            int i7 = this.huffSize[0];
            this.huffCode = new int[i4];
            int i8 = 0;
            int i9 = i7;
            int i10 = 0;
            while (true) {
                this.huffCode[i10] = i8;
                i8++;
                int i11 = i10 + 1;
                if (this.huffSize[i11] != i9) {
                    if (this.huffSize[i11] == 0) {
                        break;
                    }
                    do {
                        i8 <<= 1;
                        i9++;
                    } while (this.huffSize[i11] != i9);
                }
                i10 = i11;
            }
            int i12 = 0;
            while (true) {
                i3++;
                if (i3 > 16) {
                    return;
                }
                if (iArr[i3] == 0) {
                    this.maxCode[i3] = -1;
                } else {
                    this.valPtr[i3] = i12;
                    this.minCode[i3] = this.huffCode[i12];
                    int i13 = i12 + (iArr[i3] - 1);
                    this.maxCode[i3] = this.huffCode[i13];
                    i12 = i13 + 1;
                }
            }
        }

        public int[] getBits() {
            return this.bits;
        }

        public int[] getHuffVal() {
            return this.huffVal;
        }

        public int[] getHuffSize() {
            return this.huffSize;
        }

        public int[] getHuffCode() {
            return this.huffCode;
        }

        public int[] getMinCode() {
            return this.minCode;
        }

        public int[] getMaxCode() {
            return this.maxCode;
        }

        public int[] getValPtr() {
            return this.valPtr;
        }
    }

    public DhtSegment(int i, byte[] bArr) throws IOException {
        this(i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public DhtSegment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2);
        ArrayList arrayList = new ArrayList();
        while (i2 > 0) {
            int i3 = BinaryFunctions.readByte("TableClassAndDestinationId", inputStream, "Not a Valid JPEG File") & 255;
            i2--;
            int i4 = (i3 >> 4) & 15;
            int i5 = i3 & 15;
            int[] iArr = new int[17];
            int i6 = 0;
            for (int i7 = 1; i7 < iArr.length; i7++) {
                iArr[i7] = BinaryFunctions.readByte("Li", inputStream, "Not a Valid JPEG File") & 255;
                i2--;
                i6 += iArr[i7];
            }
            int[] iArr2 = new int[i6];
            for (int i8 = 0; i8 < i6; i8++) {
                iArr2[i8] = BinaryFunctions.readByte("Vij", inputStream, "Not a Valid JPEG File") & 255;
                i2--;
            }
            arrayList.add(new HuffmanTable(i4, i5, iArr, iArr2));
        }
        this.huffmanTables = Collections.unmodifiableList(arrayList);
    }

    @Override // org.apache.commons.imaging.formats.jpeg.segments.Segment
    public String getDescription() {
        return "DHT (" + getSegmentType() + ")";
    }
}
