package org.apache.commons.imaging.formats.tiff;

import java.io.IOException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.tiff.TiffElement;
import org.apache.commons.imaging.formats.tiff.datareaders.DataReader;
import org.apache.commons.imaging.formats.tiff.datareaders.DataReaderStrips;
import org.apache.commons.imaging.formats.tiff.datareaders.DataReaderTiled;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class TiffImageData {
    public abstract DataReader getDataReader(TiffDirectory tiffDirectory, PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6, ByteOrder byteOrder) throws IOException, ImageReadException;

    public abstract TiffElement.DataElement[] getImageData();

    public abstract boolean stripsNotTiles();

    public static class Tiles extends TiffImageData {
        private final int tileLength;
        private final int tileWidth;
        public final TiffElement.DataElement[] tiles;

        @Override // org.apache.commons.imaging.formats.tiff.TiffImageData
        public boolean stripsNotTiles() {
            return false;
        }

        public Tiles(TiffElement.DataElement[] dataElementArr, int i, int i2) {
            this.tiles = dataElementArr;
            this.tileWidth = i;
            this.tileLength = i2;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffImageData
        public TiffElement.DataElement[] getImageData() {
            return this.tiles;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffImageData
        public DataReader getDataReader(TiffDirectory tiffDirectory, PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6, ByteOrder byteOrder) throws IOException, ImageReadException {
            return new DataReaderTiled(tiffDirectory, photometricInterpreter, this.tileWidth, this.tileLength, i, iArr, i2, i3, i4, i5, i6, byteOrder, this);
        }

        public int getTileWidth() {
            return this.tileWidth;
        }

        public int getTileHeight() {
            return this.tileLength;
        }
    }

    public static class Strips extends TiffImageData {
        public final int rowsPerStrip;
        private final TiffElement.DataElement[] strips;

        @Override // org.apache.commons.imaging.formats.tiff.TiffImageData
        public boolean stripsNotTiles() {
            return true;
        }

        public Strips(TiffElement.DataElement[] dataElementArr, int i) {
            this.strips = dataElementArr;
            this.rowsPerStrip = i;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffImageData
        public TiffElement.DataElement[] getImageData() {
            return this.strips;
        }

        public TiffElement.DataElement getImageData(int i) {
            return this.strips[i];
        }

        public int getImageDataLength() {
            return this.strips.length;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffImageData
        public DataReader getDataReader(TiffDirectory tiffDirectory, PhotometricInterpreter photometricInterpreter, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6, ByteOrder byteOrder) throws IOException, ImageReadException {
            return new DataReaderStrips(tiffDirectory, photometricInterpreter, i, iArr, i2, i3, i4, i5, i6, byteOrder, this.rowsPerStrip, this);
        }
    }

    public static class Data extends TiffElement.DataElement {
        public Data(long j, int i, byte[] bArr) {
            super(j, i, bArr);
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffElement
        public String getElementDescription(boolean z) {
            return "Tiff image data: " + getDataLength() + " bytes";
        }
    }

    public static class ByteSourceData extends Data {
        ByteSourceFile byteSourceFile;

        public ByteSourceData(long j, int i, ByteSourceFile byteSourceFile) {
            super(j, i, new byte[0]);
            this.byteSourceFile = byteSourceFile;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffImageData.Data, org.apache.commons.imaging.formats.tiff.TiffElement
        public String getElementDescription(boolean z) {
            return "Tiff image data: " + getDataLength() + " bytes";
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffElement.DataElement
        public byte[] getData() {
            try {
                return this.byteSourceFile.getBlock(this.offset, this.length);
            } catch (IOException unused) {
                return new byte[0];
            }
        }
    }
}
