// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.bmp;

import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.PrintStream;
import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.ImageReadException;

class PixelParserRle extends PixelParser
{
    public PixelParserRle(final BmpHeaderInfo bmpHeaderInfo, final byte[] array, final byte[] array2) {
        super(bmpHeaderInfo, array, array2);
    }
    
    private int[] convertDataToSamples(final int n) throws ImageReadException {
        int[] array;
        if (this.bhi.bitsPerPixel == 8) {
            array = new int[] { this.getColorTableRGB(n) };
        }
        else {
            if (this.bhi.bitsPerPixel != 4) {
                final StringBuilder sb = new StringBuilder();
                sb.append("BMP RLE: bad BitsPerPixel: ");
                sb.append(this.bhi.bitsPerPixel);
                throw new ImageReadException(sb.toString());
            }
            array = new int[] { this.getColorTableRGB(n >> 4), this.getColorTableRGB(n & 0xF) };
        }
        return array;
    }
    
    private int getSamplesPerByte() throws ImageReadException {
        if (this.bhi.bitsPerPixel == 8) {
            return 1;
        }
        if (this.bhi.bitsPerPixel == 4) {
            return 2;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("BMP RLE: bad BitsPerPixel: ");
        sb.append(this.bhi.bitsPerPixel);
        throw new ImageReadException(sb.toString());
    }
    
    private int processByteOfData(final int[] array, final int n, int i, final int j, final int n2, final int n3, final ImageBuilder imageBuilder) {
        final int n4 = 0;
        int n5 = 0;
        int k = i;
        PrintStream out;
        StringBuilder sb;
        for (i = n4; i < n; ++i) {
            if (k >= 0 && k < n2 && j >= 0 && j < n3) {
                imageBuilder.setRGB(k, j, array[i % array.length]);
            }
            else {
                out = System.out;
                sb = new StringBuilder();
                sb.append("skipping bad pixel (");
                sb.append(k);
                sb.append(",");
                sb.append(j);
                sb.append(")");
                out.println(sb.toString());
            }
            ++k;
            ++n5;
        }
        return n5;
    }
    
    @Override
    public void processImage(final ImageBuilder imageBuilder) throws ImageReadException, IOException {
        final int width = this.bhi.width;
        final int height = this.bhi.height;
        int n = height - 1;
        int i = 0;
        int n2 = 0;
        while (i == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("RLE (");
            sb.append(n2);
            sb.append(",");
            sb.append(n);
            sb.append(") a");
            final int n3 = 0xFF & BinaryFunctions.readByte(sb.toString(), this.is, "BMP: Bad RLE");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("RLE (");
            sb2.append(n2);
            sb2.append(",");
            sb2.append(n);
            sb2.append(") b");
            final int n4 = BinaryFunctions.readByte(sb2.toString(), this.is, "BMP: Bad RLE") & 0xFF;
            if (n3 == 0) {
                switch (n4) {
                    default: {
                        final int samplesPerByte = this.getSamplesPerByte();
                        int n6;
                        final int n5 = n6 = n4 / samplesPerByte;
                        if (n4 % samplesPerByte > 0) {
                            n6 = n5 + 1;
                        }
                        int n7 = n6;
                        if (n6 % 2 != 0) {
                            n7 = n6 + 1;
                        }
                        final byte[] bytes = BinaryFunctions.readBytes("bytes", this.is, n7, "RLE: Absolute Mode");
                        int processByteOfData;
                        for (int n8 = 0, j = n4; j > 0; j -= processByteOfData, ++n8) {
                            processByteOfData = this.processByteOfData(this.convertDataToSamples(bytes[n8] & 0xFF), Math.min(j, samplesPerByte), n2, n, width, height, imageBuilder);
                            n2 += processByteOfData;
                        }
                        continue;
                    }
                    case 2: {
                        final byte byte1 = BinaryFunctions.readByte("RLE deltaX", this.is, "BMP: Bad RLE");
                        final byte byte2 = BinaryFunctions.readByte("RLE deltaY", this.is, "BMP: Bad RLE");
                        n2 += (byte1 & 0xFF);
                        n -= (byte2 & 0xFF);
                        continue;
                    }
                    case 1: {
                        i = 1;
                        continue;
                    }
                    case 0: {
                        --n;
                        n2 = 0;
                        continue;
                    }
                }
            }
            else {
                n2 += this.processByteOfData(this.convertDataToSamples(n4), n3, n2, n, width, height, imageBuilder);
            }
        }
    }
}
