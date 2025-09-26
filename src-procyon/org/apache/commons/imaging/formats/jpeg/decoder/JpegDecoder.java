// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.decoder;

import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.util.Properties;
import java.awt.Point;
import java.awt.image.Raster;
import java.awt.image.DirectColorModel;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.ByteArrayInputStream;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.util.Arrays;
import org.apache.commons.imaging.formats.jpeg.segments.SosSegment;
import org.apache.commons.imaging.formats.jpeg.segments.SofnSegment;
import org.apache.commons.imaging.formats.jpeg.segments.DqtSegment;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.formats.jpeg.segments.DhtSegment;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import org.apache.commons.imaging.common.BinaryFileParser;

public class JpegDecoder extends BinaryFileParser implements Visitor
{
    private final float[] block;
    private final int[] blockInt;
    private final DhtSegment.HuffmanTable[] huffmanACTables;
    private final DhtSegment.HuffmanTable[] huffmanDCTables;
    private BufferedImage image;
    private ImageReadException imageReadException;
    private IOException ioException;
    private final DqtSegment.QuantizationTable[] quantizationTables;
    private final float[][] scaledQuantizationTables;
    private SofnSegment sofnSegment;
    private SosSegment sosSegment;
    private final int[] zz;
    
    public JpegDecoder() {
        this.quantizationTables = new DqtSegment.QuantizationTable[4];
        this.huffmanDCTables = new DhtSegment.HuffmanTable[4];
        this.huffmanACTables = new DhtSegment.HuffmanTable[4];
        this.scaledQuantizationTables = new float[4][];
        this.zz = new int[64];
        this.blockInt = new int[64];
        this.block = new float[64];
    }
    
    private Block[] allocateMCUMemory() throws ImageReadException {
        final Block[] array = new Block[this.sosSegment.numberOfComponents];
        for (int i = 0; i < this.sosSegment.numberOfComponents; ++i) {
            final SosSegment.Component components = this.sosSegment.getComponents(i);
            final SofnSegment.Component component = null;
            int n = 0;
            Object components2;
            while (true) {
                components2 = component;
                if (n >= this.sofnSegment.numberOfComponents) {
                    break;
                }
                if (this.sofnSegment.getComponents(n).componentIdentifier == components.scanComponentSelector) {
                    components2 = this.sofnSegment.getComponents(n);
                    break;
                }
                ++n;
            }
            if (components2 == null) {
                throw new ImageReadException("Invalid component");
            }
            array[i] = new Block(((SofnSegment.Component)components2).horizontalSamplingFactor * 8, 8 * ((SofnSegment.Component)components2).verticalSamplingFactor);
        }
        return array;
    }
    
    private int decode(final JpegInputStream jpegInputStream, final DhtSegment.HuffmanTable huffmanTable) throws IOException, ImageReadException {
        int i;
        int n;
        for (i = jpegInputStream.nextBit(), n = 1; i > huffmanTable.getMaxCode()[n]; ++n, i = (i << 1 | jpegInputStream.nextBit())) {}
        return huffmanTable.getHuffVal()[huffmanTable.getValPtr()[n] + (i - huffmanTable.getMinCode()[n])];
    }
    
    private int extend(int i, final int n) {
        for (int n2 = 1 << n - 1; i < n2; n2 = (-1 << n) + 1, i += n2) {}
        return i;
    }
    
    private static int fastRound(final float n) {
        return (int)(n + 0.5f);
    }
    
    private void readMCU(final JpegInputStream jpegInputStream, final int[] array, final Block[] array2) throws IOException, ImageReadException {
        for (int i = 0; i < this.sosSegment.numberOfComponents; ++i) {
            final SosSegment.Component components = this.sosSegment.getComponents(i);
            final SofnSegment.Component component = null;
            int n = 0;
            Object components2;
            while (true) {
                components2 = component;
                if (n >= this.sofnSegment.numberOfComponents) {
                    break;
                }
                if (this.sofnSegment.getComponents(n).componentIdentifier == components.scanComponentSelector) {
                    components2 = this.sofnSegment.getComponents(n);
                    break;
                }
                ++n;
            }
            if (components2 == null) {
                throw new ImageReadException("Invalid component");
            }
            final Block block = array2[i];
            for (int j = 0; j < ((SofnSegment.Component)components2).verticalSamplingFactor; ++j) {
                for (int k = 0; k < ((SofnSegment.Component)components2).horizontalSamplingFactor; ++k) {
                    Arrays.fill(this.zz, 0);
                    final int decode = this.decode(jpegInputStream, this.huffmanDCTables[components.dcCodingTableSelector]);
                    array[i] = (this.zz[0] = array[i] + this.extend(this.receive(decode, jpegInputStream), decode));
                    int n2 = 1;
                    while (true) {
                        final int decode2 = this.decode(jpegInputStream, this.huffmanACTables[components.acCodingTableSelector]);
                        final int n3 = decode2 & 0xF;
                        final int n4 = decode2 >> 4;
                        if (n3 == 0) {
                            if (n4 != 15) {
                                break;
                            }
                            n2 += 16;
                        }
                        else {
                            n2 += n4;
                            this.zz[n2] = this.receive(n3, jpegInputStream);
                            this.zz[n2] = this.extend(this.zz[n2], n3);
                            if (n2 == 63) {
                                break;
                            }
                            ++n2;
                        }
                    }
                    final int precision = this.sofnSegment.precision;
                    final int n5 = (1 << this.sofnSegment.precision) - 1;
                    final float[] array3 = this.scaledQuantizationTables[((SofnSegment.Component)components2).quantTabDestSelector];
                    ZigZag.zigZagToBlock(this.zz, this.blockInt);
                    for (int l = 0; l < 64; ++l) {
                        this.block[l] = this.blockInt[l] * array3[l];
                    }
                    Dct.inverseDCT8x8(this.block);
                    int n6 = 8 * j * 8 * ((SofnSegment.Component)components2).horizontalSamplingFactor + 8 * k;
                    int n7 = 0;
                    int n8 = 0;
                    while (n7 < 8) {
                        for (int n9 = 0; n9 < 8; ++n9, ++n8) {
                            final float n10 = this.block[n8] + (1 << precision - 1);
                            int fastRound;
                            if (n10 < 0.0f) {
                                fastRound = 0;
                            }
                            else if (n10 > n5) {
                                fastRound = n5;
                            }
                            else {
                                fastRound = fastRound(n10);
                            }
                            block.samples[n6 + n9] = fastRound;
                        }
                        n6 += ((SofnSegment.Component)components2).horizontalSamplingFactor * 8;
                        ++n7;
                    }
                }
            }
        }
    }
    
    private int receive(final int n, final JpegInputStream jpegInputStream) throws IOException, ImageReadException {
        int i;
        int n2;
        for (i = 0, n2 = 0; i != n; ++i, n2 = (n2 << 1) + jpegInputStream.nextBit()) {}
        return n2;
    }
    
    private void rescaleMCU(final Block[] array, final int n, final int n2, final Block[] array2) {
        for (int i = 0; i < array.length; ++i) {
            final Block block = array[i];
            if (block.width == n && block.height == n2) {
                System.arraycopy(block.samples, 0, array2[i].samples, 0, n * n2);
            }
            else {
                final int n3 = n / block.width;
                final int n4 = n2 / block.height;
                if (n3 == 2 && n4 == 2) {
                    int j = 0;
                    int n6;
                    int n5 = n6 = 0;
                    while (j < block.height) {
                        for (int k = 0; k < n; ++k) {
                            final int n7 = block.samples[(k >> 1) + n5];
                            array2[i].samples[n6 + k] = n7;
                            array2[i].samples[n6 + n + k] = n7;
                        }
                        n5 += block.width;
                        n6 += 2 * n;
                        ++j;
                    }
                }
                else {
                    int l = 0;
                    int n8 = 0;
                    while (l < n2) {
                        for (int n9 = 0; n9 < n; ++n9) {
                            array2[i].samples[n8 + n9] = block.samples[l / n4 * block.width + n9 / n3];
                        }
                        n8 += n;
                        ++l;
                    }
                }
            }
        }
    }
    
    @Override
    public boolean beginSOS() {
        return true;
    }
    
    public BufferedImage decode(final ByteSource byteSource) throws IOException, ImageReadException {
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)this);
        if (this.imageReadException != null) {
            throw this.imageReadException;
        }
        if (this.ioException != null) {
            throw this.ioException;
        }
        return this.image;
    }
    
    @Override
    public void visitSOS(int i, final byte[] array, final byte[] buf) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
        try {
            this.sosSegment = new SosSegment(i, BinaryFunctions.readBytes("SosSegment", byteArrayInputStream, BinaryFunctions.read2Bytes("segmentLength", byteArrayInputStream, "Not a Valid JPEG File", this.getByteOrder()) - 2, "Not a Valid JPEG File"));
            int j = 0;
            int max = 0;
            i = 0;
            while (j < this.sofnSegment.numberOfComponents) {
                max = Math.max(max, this.sofnSegment.getComponents(j).horizontalSamplingFactor);
                i = Math.max(i, this.sofnSegment.getComponents(j).verticalSamplingFactor);
                ++j;
            }
            final int n = max * 8;
            final int n2 = 8 * i;
            final JpegInputStream jpegInputStream = new JpegInputStream(byteArrayInputStream);
            final int n3 = (this.sofnSegment.width + n - 1) / n;
            final int n4 = (this.sofnSegment.height + n2 - 1) / n2;
            final Block[] allocateMCUMemory = this.allocateMCUMemory();
            Block[] array2;
            for (array2 = new Block[allocateMCUMemory.length], i = 0; i < array2.length; ++i) {
                array2[i] = new Block(n, n2);
            }
            final int[] array3 = new int[this.sofnSegment.numberOfComponents];
            DirectColorModel cm;
            WritableRaster raster;
            if (this.sofnSegment.numberOfComponents == 3) {
                cm = new DirectColorModel(24, 16711680, 65280, 255);
                raster = Raster.createPackedRaster(3, this.sofnSegment.width, this.sofnSegment.height, new int[] { 16711680, 65280, 255 }, null);
            }
            else {
                if (this.sofnSegment.numberOfComponents != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.sofnSegment.numberOfComponents);
                    sb.append(" components are invalid or unsupported");
                    throw new ImageReadException(sb.toString());
                }
                cm = new DirectColorModel(24, 16711680, 65280, 255);
                raster = Raster.createPackedRaster(3, this.sofnSegment.width, this.sofnSegment.height, new int[] { 16711680, 65280, 255 }, null);
            }
            final DataBuffer dataBuffer = raster.getDataBuffer();
            final int n5 = 0;
            i = n4;
            for (int k = n5; k < n2 * i; k += n2) {
                for (int l = 0; l < n * n3; l += n) {
                    this.readMCU(jpegInputStream, array3, allocateMCUMemory);
                    this.rescaleMCU(allocateMCUMemory, n, n2, array2);
                    int n6 = this.sofnSegment.width * k + l;
                    int n7 = 0;
                    int n8 = 0;
                    while (n7 < n2 && k + n7 < this.sofnSegment.height) {
                        for (int n9 = 0; n9 < n && l + n9 < this.sofnSegment.width; ++n9) {
                            if (array2.length == 3) {
                                final int[] samples = array2[0].samples;
                                final int n10 = n8 + n9;
                                dataBuffer.setElem(n6 + n9, YCbCrConverter.convertYCbCrToRGB(samples[n10], array2[1].samples[n10], array2[2].samples[n10]));
                            }
                            else {
                                if (allocateMCUMemory.length != 1) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Unsupported JPEG with ");
                                    sb2.append(allocateMCUMemory.length);
                                    sb2.append(" components");
                                    throw new ImageReadException(sb2.toString());
                                }
                                final int n11 = array2[0].samples[n8 + n9];
                                dataBuffer.setElem(n6 + n9, n11 << 16 | n11 << 8 | n11);
                            }
                        }
                        n8 += n;
                        n6 += this.sofnSegment.width;
                        ++n7;
                    }
                }
            }
            this.image = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), new Properties());
        }
        catch (final RuntimeException ex) {
            this.imageReadException = new ImageReadException("Error parsing JPEG", ex);
        }
        catch (final IOException ioException) {
            this.ioException = ioException;
        }
        catch (final ImageReadException imageReadException) {
            this.imageReadException = imageReadException;
        }
    }
    
    @Override
    public boolean visitSegment(int i, final byte[] array, int j, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
        if (Arrays.binarySearch(new int[] { 65472, 65473, 65474, 65475, 65477, 65478, 65479, 65481, 65482, 65483, 65485, 65486, 65487 }, i) >= 0) {
            if (i != 65472) {
                throw new ImageReadException("Only sequential, baseline JPEGs are supported at the moment");
            }
            this.sofnSegment = new SofnSegment(i, array3);
        }
        else {
            j = 0;
            if (i == 65499) {
                DqtSegment dqtSegment;
                DqtSegment.QuantizationTable quantizationTable;
                StringBuilder sb;
                int[] array4;
                float[] array5;
                for (dqtSegment = new DqtSegment(i, array3), i = 0; i < dqtSegment.quantizationTables.size(); ++i) {
                    quantizationTable = (DqtSegment.QuantizationTable)dqtSegment.quantizationTables.get(i);
                    if (quantizationTable.destinationIdentifier < 0 || quantizationTable.destinationIdentifier >= this.quantizationTables.length) {
                        sb = new StringBuilder();
                        sb.append("Invalid quantization table identifier ");
                        sb.append(quantizationTable.destinationIdentifier);
                        throw new ImageReadException(sb.toString());
                    }
                    this.quantizationTables[quantizationTable.destinationIdentifier] = quantizationTable;
                    array4 = new int[64];
                    ZigZag.zigZagToBlock(quantizationTable.getElements(), array4);
                    array5 = new float[64];
                    for (j = 0; j < 64; ++j) {
                        array5[j] = (float)array4[j];
                    }
                    Dct.scaleDequantizationMatrix(array5);
                    this.scaledQuantizationTables[quantizationTable.destinationIdentifier] = array5;
                }
            }
            else if (i == 65476) {
                DhtSegment dhtSegment;
                DhtSegment.HuffmanTable huffmanTable;
                DhtSegment.HuffmanTable[] array6;
                StringBuilder sb2;
                StringBuilder sb3;
                for (dhtSegment = new DhtSegment(i, array3), i = j; i < dhtSegment.huffmanTables.size(); ++i) {
                    huffmanTable = (DhtSegment.HuffmanTable)dhtSegment.huffmanTables.get(i);
                    if (huffmanTable.tableClass == 0) {
                        array6 = this.huffmanDCTables;
                    }
                    else {
                        if (huffmanTable.tableClass != 1) {
                            sb2 = new StringBuilder();
                            sb2.append("Invalid huffman table class ");
                            sb2.append(huffmanTable.tableClass);
                            throw new ImageReadException(sb2.toString());
                        }
                        array6 = this.huffmanACTables;
                    }
                    if (huffmanTable.destinationIdentifier < 0 || huffmanTable.destinationIdentifier >= array6.length) {
                        sb3 = new StringBuilder();
                        sb3.append("Invalid huffman table identifier ");
                        sb3.append(huffmanTable.destinationIdentifier);
                        throw new ImageReadException(sb3.toString());
                    }
                    array6[huffmanTable.destinationIdentifier] = huffmanTable;
                }
            }
        }
        return true;
    }
}
