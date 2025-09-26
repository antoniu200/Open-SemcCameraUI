package org.apache.commons.imaging.formats.tiff.datareaders;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.common.PackBits;
import org.apache.commons.imaging.common.itu_t4.T4AndT6Compression;
import org.apache.commons.imaging.common.mylzw.MyLzwDecompressor;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class DataReader {
    private final int[] bitsPerSample;
    protected final int bitsPerSampleLength;
    protected final TiffDirectory directory;
    protected final int height;
    private final int[] last;
    protected final PhotometricInterpreter photometricInterpreter;
    protected final int predictor;
    protected final int samplesPerPixel;
    protected final int width;

    public abstract BufferedImage readImageData(Rectangle rectangle) throws IOException, ImageReadException;

    public abstract void readImageData(ImageBuilder imageBuilder) throws IOException, ImageReadException;

    public DataReader(TiffDirectory tiffDirectory, PhotometricInterpreter photometricInterpreter, int[] iArr, int i, int i2, int i3, int i4) {
        this.directory = tiffDirectory;
        this.photometricInterpreter = photometricInterpreter;
        this.bitsPerSample = iArr;
        this.bitsPerSampleLength = iArr.length;
        this.samplesPerPixel = i2;
        this.predictor = i;
        this.width = i3;
        this.height = i4;
        this.last = new int[i2];
    }

    protected boolean isHomogenous(int i) {
        for (int i2 : this.bitsPerSample) {
            if (i2 != i) {
                return false;
            }
        }
        return true;
    }

    void getSamplesAsBytes(BitInputStream bitInputStream, int[] iArr) throws IOException {
        for (int i = 0; i < this.bitsPerSample.length; i++) {
            int i2 = this.bitsPerSample[i];
            int bits = bitInputStream.readBits(i2);
            if (i2 < 8) {
                int i3 = 8 - i2;
                int i4 = bits << i3;
                if ((bits & 1) > 0) {
                    i4 |= (1 << i3) - 1;
                }
                bits = i4;
            } else if (i2 > 8) {
                bits >>= i2 - 8;
            }
            iArr[i] = bits;
        }
    }

    protected void resetPredictor() {
        for (int i = 0; i < this.last.length; i++) {
            this.last[i] = 0;
        }
    }

    protected int[] applyPredictor(int[] iArr) {
        if (this.predictor == 2) {
            for (int i = 0; i < iArr.length; i++) {
                iArr[i] = 255 & (iArr[i] + this.last[i]);
                this.last[i] = iArr[i];
            }
        }
        return iArr;
    }

    protected byte[] decompress(byte[] bArr, int i, int i2, int i3, int i4) throws IOException, ImageReadException {
        TiffField tiffFieldFindField = this.directory.findField(TiffTagConstants.TIFF_TAG_FILL_ORDER);
        int intValue = tiffFieldFindField != null ? tiffFieldFindField.getIntValue() : 1;
        if (intValue != 1) {
            if (intValue == 2) {
                for (int i5 = 0; i5 < bArr.length; i5++) {
                    bArr[i5] = (byte) (Integer.reverse(255 & bArr[i5]) >>> 24);
                }
            } else {
                throw new ImageReadException("TIFF FillOrder=" + intValue + " is invalid");
            }
        }
        if (i != 32773) {
            switch (i) {
                case 1:
                    return bArr;
                case 2:
                    return T4AndT6Compression.decompressModifiedHuffman(bArr, i3, i4);
                case 3:
                    TiffField tiffFieldFindField2 = this.directory.findField(TiffTagConstants.TIFF_TAG_T4_OPTIONS);
                    int intValue2 = tiffFieldFindField2 != null ? tiffFieldFindField2.getIntValue() : 0;
                    boolean z = (intValue2 & 1) != 0;
                    if ((intValue2 & 2) != 0) {
                        throw new ImageReadException("T.4 compression with the uncompressed mode extension is not yet supported");
                    }
                    boolean z2 = (intValue2 & 4) != 0;
                    if (z) {
                        return T4AndT6Compression.decompressT4_2D(bArr, i3, i4, z2);
                    }
                    return T4AndT6Compression.decompressT4_1D(bArr, i3, i4, z2);
                case 4:
                    TiffField tiffFieldFindField3 = this.directory.findField(TiffTagConstants.TIFF_TAG_T6_OPTIONS);
                    if (((tiffFieldFindField3 != null ? tiffFieldFindField3.getIntValue() : 0) & 2) != 0) {
                        throw new ImageReadException("T.6 compression with the uncompressed mode extension is not yet supported");
                    }
                    return T4AndT6Compression.decompressT6(bArr, i3, i4);
                case 5:
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                    MyLzwDecompressor myLzwDecompressor = new MyLzwDecompressor(8, ByteOrder.BIG_ENDIAN);
                    myLzwDecompressor.setTiffLZWMode();
                    return myLzwDecompressor.decompress(byteArrayInputStream, i2);
                default:
                    throw new ImageReadException("Tiff: unknown/unsupported compression: " + i);
            }
        }
        return new PackBits().decompress(bArr, i2);
    }
}
