// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.datareaders;

import org.apache.commons.imaging.common.ImageBuilder;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.common.itu_t4.T4AndT6Compression;
import java.io.InputStream;
import org.apache.commons.imaging.common.mylzw.MyLzwDecompressor;
import java.nio.ByteOrder;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.PackBits;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.photometricinterpreters.PhotometricInterpreter;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;

public abstract class DataReader
{
    private final int[] bitsPerSample;
    protected final int bitsPerSampleLength;
    protected final TiffDirectory directory;
    protected final int height;
    private final int[] last;
    protected final PhotometricInterpreter photometricInterpreter;
    protected final int predictor;
    protected final int samplesPerPixel;
    protected final int width;
    
    public DataReader(final TiffDirectory directory, final PhotometricInterpreter photometricInterpreter, final int[] bitsPerSample, final int predictor, final int samplesPerPixel, final int width, final int height) {
        this.directory = directory;
        this.photometricInterpreter = photometricInterpreter;
        this.bitsPerSample = bitsPerSample;
        this.bitsPerSampleLength = bitsPerSample.length;
        this.samplesPerPixel = samplesPerPixel;
        this.predictor = predictor;
        this.width = width;
        this.height = height;
        this.last = new int[samplesPerPixel];
    }
    
    protected int[] applyPredictor(final int[] array) {
        if (this.predictor == 2) {
            for (int i = 0; i < array.length; ++i) {
                array[i] = (0xFF & array[i] + this.last[i]);
                this.last[i] = array[i];
            }
        }
        return array;
    }
    
    protected byte[] decompress(final byte[] buf, int i, int n, final int n2, final int n3) throws ImageReadException, IOException {
        final TiffField field = this.directory.findField(TiffTagConstants.TIFF_TAG_FILL_ORDER);
        boolean b = true;
        final int n4 = 1;
        int intValue;
        if (field != null) {
            intValue = field.getIntValue();
        }
        else {
            intValue = 1;
        }
        if (intValue != 1) {
            if (intValue != 2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TIFF FillOrder=");
                sb.append(intValue);
                sb.append(" is invalid");
                throw new ImageReadException(sb.toString());
            }
            for (int j = 0; j < buf.length; ++j) {
                buf[j] = (byte)(Integer.reverse(0xFF & buf[j]) >>> 24);
            }
        }
        if (i == 32773) {
            return new PackBits().decompress(buf, n);
        }
        switch (i) {
            default: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Tiff: unknown/unsupported compression: ");
                sb2.append(i);
                throw new ImageReadException(sb2.toString());
            }
            case 5: {
                final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                final MyLzwDecompressor myLzwDecompressor = new MyLzwDecompressor(8, ByteOrder.BIG_ENDIAN);
                myLzwDecompressor.setTiffLZWMode();
                return myLzwDecompressor.decompress(byteArrayInputStream, n);
            }
            case 4: {
                final TiffField field2 = this.directory.findField(TiffTagConstants.TIFF_TAG_T6_OPTIONS);
                if (field2 != null) {
                    i = field2.getIntValue();
                }
                else {
                    i = 0;
                }
                if ((i & 0x2) != 0x0) {
                    i = n4;
                }
                else {
                    i = 0;
                }
                if (i != 0) {
                    throw new ImageReadException("T.6 compression with the uncompressed mode extension is not yet supported");
                }
                return T4AndT6Compression.decompressT6(buf, n2, n3);
            }
            case 3: {
                final TiffField field3 = this.directory.findField(TiffTagConstants.TIFF_TAG_T4_OPTIONS);
                if (field3 != null) {
                    i = field3.getIntValue();
                }
                else {
                    i = 0;
                }
                if ((i & 0x1) != 0x0) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                if ((i & 0x2) != 0x0) {
                    throw new ImageReadException("T.4 compression with the uncompressed mode extension is not yet supported");
                }
                if ((i & 0x4) == 0x0) {
                    b = false;
                }
                if (n != 0) {
                    return T4AndT6Compression.decompressT4_2D(buf, n2, n3, b);
                }
                return T4AndT6Compression.decompressT4_1D(buf, n2, n3, b);
            }
            case 2: {
                return T4AndT6Compression.decompressModifiedHuffman(buf, n2, n3);
            }
            case 1: {
                return buf;
            }
        }
    }
    
    void getSamplesAsBytes(final BitInputStream bitInputStream, final int[] array) throws IOException {
        for (int i = 0; i < this.bitsPerSample.length; ++i) {
            final int n = this.bitsPerSample[i];
            final int bits = bitInputStream.readBits(n);
            int n3;
            if (n < 8) {
                final int n2 = 8 - n;
                n3 = bits << n2;
                if ((bits & 0x1) > 0) {
                    n3 |= (1 << n2) - 1;
                }
            }
            else {
                n3 = bits;
                if (n > 8) {
                    n3 = bits >> n - 8;
                }
            }
            array[i] = n3;
        }
    }
    
    protected boolean isHomogenous(final int n) {
        final int[] bitsPerSample = this.bitsPerSample;
        for (int length = bitsPerSample.length, i = 0; i < length; ++i) {
            if (bitsPerSample[i] != n) {
                return false;
            }
        }
        return true;
    }
    
    public abstract BufferedImage readImageData(final Rectangle p0) throws ImageReadException, IOException;
    
    public abstract void readImageData(final ImageBuilder p0) throws ImageReadException, IOException;
    
    protected void resetPredictor() {
        for (int i = 0; i < this.last.length; ++i) {
            this.last[i] = 0;
        }
    }
}
