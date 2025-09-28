package org.apache.commons.imaging.formats.jpeg.decoder;

import android.support.v4.view.MotionEventCompat;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import org.apache.commons.imaging.formats.jpeg.segments.DhtSegment;
import org.apache.commons.imaging.formats.jpeg.segments.DqtSegment;
import org.apache.commons.imaging.formats.jpeg.segments.SofnSegment;
import org.apache.commons.imaging.formats.jpeg.segments.SosSegment;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JpegDecoder extends BinaryFileParser implements JpegUtils.Visitor {
    private BufferedImage image;
    private ImageReadException imageReadException;
    private IOException ioException;
    private SofnSegment sofnSegment;
    private SosSegment sosSegment;
    private final DqtSegment.QuantizationTable[] quantizationTables = new DqtSegment.QuantizationTable[4];
    private final DhtSegment.HuffmanTable[] huffmanDCTables = new DhtSegment.HuffmanTable[4];
    private final DhtSegment.HuffmanTable[] huffmanACTables = new DhtSegment.HuffmanTable[4];
    private final float[][] scaledQuantizationTables = new float[4][];
    private final int[] zz = new int[64];
    private final int[] blockInt = new int[64];
    private final float[] block = new float[64];

    private int extend(int i, int i2) {
        int i3 = 1 << (i2 - 1);
        while (i < i3) {
            i3 = ((-1) << i2) + 1;
            i += i3;
        }
        return i;
    }

    private static int fastRound(float f) {
        return (int) (f + 0.5f);
    }

    @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
    public boolean beginSOS() {
        return true;
    }

    @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
    public void visitSOS(int i, byte[] bArr, byte[] bArr2) throws ImageReadException {
        int i2;
        WritableRaster writableRaster;
        JpegInputStream jpegInputStream;
        WritableRaster writableRaster2;
        int i3;
        WritableRaster writableRaster3;
        int[] iArr;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr2);
        try {
            this.sosSegment = new SosSegment(i, BinaryFunctions.readBytes("SosSegment", byteArrayInputStream, BinaryFunctions.read2Bytes("segmentLength", byteArrayInputStream, "Not a Valid JPEG File", getByteOrder()) - 2, "Not a Valid JPEG File"));
            int iMax = 0;
            int iMax2 = 0;
            for (int i4 = 0; i4 < this.sofnSegment.numberOfComponents; i4++) {
                iMax = Math.max(iMax, this.sofnSegment.getComponents(i4).horizontalSamplingFactor);
                iMax2 = Math.max(iMax2, this.sofnSegment.getComponents(i4).verticalSamplingFactor);
            }
            int i5 = iMax * 8;
            int i6 = 8 * iMax2;
            JpegInputStream jpegInputStream2 = new JpegInputStream(byteArrayInputStream);
            int i7 = ((this.sofnSegment.width + i5) - 1) / i5;
            int i8 = ((this.sofnSegment.height + i6) - 1) / i6;
            Block[] blockArrAllocateMCUMemory = allocateMCUMemory();
            Block[] blockArr = new Block[blockArrAllocateMCUMemory.length];
            for (int i9 = 0; i9 < blockArr.length; i9++) {
                blockArr[i9] = new Block(i5, i6);
            }
            int[] iArr2 = new int[this.sofnSegment.numberOfComponents];
            if (this.sofnSegment.numberOfComponents == 3 || this.sofnSegment.numberOfComponents == 1) {
                DirectColorModel directColorModel = new DirectColorModel(24, 16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255);
                WritableRaster writableRasterCreatePackedRaster = Raster.createPackedRaster(3, this.sofnSegment.width, this.sofnSegment.height, new int[]{16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255}, (Point) null);
                DataBuffer dataBuffer = writableRasterCreatePackedRaster.getDataBuffer();
                int i10 = 0;
                while (i10 < i6 * i8) {
                    int i11 = 0;
                    while (i11 < i5 * i7) {
                        readMCU(jpegInputStream2, iArr2, blockArrAllocateMCUMemory);
                        rescaleMCU(blockArrAllocateMCUMemory, i5, i6, blockArr);
                        int i12 = (this.sofnSegment.width * i10) + i11;
                        int i13 = 0;
                        int i14 = 0;
                        while (true) {
                            if (i13 >= i6) {
                                i2 = i7;
                                writableRaster = writableRasterCreatePackedRaster;
                                jpegInputStream = jpegInputStream2;
                                break;
                            }
                            i2 = i7;
                            jpegInputStream = jpegInputStream2;
                            if (i10 + i13 >= this.sofnSegment.height) {
                                writableRaster = writableRasterCreatePackedRaster;
                                break;
                            }
                            int i15 = 0;
                            while (true) {
                                if (i15 >= i5) {
                                    writableRaster2 = writableRasterCreatePackedRaster;
                                    i3 = i8;
                                    break;
                                }
                                i3 = i8;
                                if (i11 + i15 >= this.sofnSegment.width) {
                                    writableRaster2 = writableRasterCreatePackedRaster;
                                    break;
                                }
                                if (blockArr.length == 3) {
                                    int i16 = i14 + i15;
                                    iArr = iArr2;
                                    writableRaster3 = writableRasterCreatePackedRaster;
                                    dataBuffer.setElem(i12 + i15, YCbCrConverter.convertYCbCrToRGB(blockArr[0].samples[i16], blockArr[1].samples[i16], blockArr[2].samples[i16]));
                                } else {
                                    writableRaster3 = writableRasterCreatePackedRaster;
                                    iArr = iArr2;
                                    if (blockArrAllocateMCUMemory.length == 1) {
                                        int i17 = blockArr[0].samples[i14 + i15];
                                        dataBuffer.setElem(i12 + i15, (i17 << 16) | (i17 << 8) | i17);
                                    } else {
                                        throw new ImageReadException("Unsupported JPEG with " + blockArrAllocateMCUMemory.length + " components");
                                    }
                                }
                                i15++;
                                i8 = i3;
                                iArr2 = iArr;
                                writableRasterCreatePackedRaster = writableRaster3;
                            }
                            i14 += i5;
                            i12 += this.sofnSegment.width;
                            i13++;
                            i7 = i2;
                            jpegInputStream2 = jpegInputStream;
                            i8 = i3;
                            iArr2 = iArr2;
                            writableRasterCreatePackedRaster = writableRaster2;
                        }
                        i11 += i5;
                        i7 = i2;
                        jpegInputStream2 = jpegInputStream;
                        i8 = i8;
                        iArr2 = iArr2;
                        writableRasterCreatePackedRaster = writableRaster;
                    }
                    i10 += i6;
                    jpegInputStream2 = jpegInputStream2;
                    writableRasterCreatePackedRaster = writableRasterCreatePackedRaster;
                }
                this.image = new BufferedImage(directColorModel, writableRasterCreatePackedRaster, directColorModel.isAlphaPremultiplied(), new Properties());
                return;
            }
            throw new ImageReadException(this.sofnSegment.numberOfComponents + " components are invalid or unsupported");
        } catch (IOException e) {
            this.ioException = e;
        } catch (RuntimeException e2) {
            this.imageReadException = new ImageReadException("Error parsing JPEG", e2);
        } catch (ImageReadException e3) {
            this.imageReadException = e3;
        }
    }

    @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
    public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
        DhtSegment.HuffmanTable[] huffmanTableArr;
        if (Arrays.binarySearch(new int[]{JpegConstants.SOF0_MARKER, JpegConstants.SOF1_MARKER, JpegConstants.SOF2_MARKER, JpegConstants.SOF3_MARKER, JpegConstants.SOF5_MARKER, JpegConstants.SOF6_MARKER, JpegConstants.SOF7_MARKER, JpegConstants.SOF9_MARKER, JpegConstants.SOF10_MARKER, JpegConstants.SOF11_MARKER, JpegConstants.SOF13_MARKER, JpegConstants.SOF14_MARKER, JpegConstants.SOF15_MARKER}, i) < 0) {
            if (i == 65499) {
                DqtSegment dqtSegment = new DqtSegment(i, bArr3);
                for (int i3 = 0; i3 < dqtSegment.quantizationTables.size(); i3++) {
                    DqtSegment.QuantizationTable quantizationTable = dqtSegment.quantizationTables.get(i3);
                    if (quantizationTable.destinationIdentifier < 0 || quantizationTable.destinationIdentifier >= this.quantizationTables.length) {
                        throw new ImageReadException("Invalid quantization table identifier " + quantizationTable.destinationIdentifier);
                    }
                    this.quantizationTables[quantizationTable.destinationIdentifier] = quantizationTable;
                    ZigZag.zigZagToBlock(quantizationTable.getElements(), new int[64]);
                    float[] fArr = new float[64];
                    for (int i4 = 0; i4 < 64; i4++) {
                        fArr[i4] = r1[i4];
                    }
                    Dct.scaleDequantizationMatrix(fArr);
                    this.scaledQuantizationTables[quantizationTable.destinationIdentifier] = fArr;
                }
            } else if (i == 65476) {
                DhtSegment dhtSegment = new DhtSegment(i, bArr3);
                for (int i5 = 0; i5 < dhtSegment.huffmanTables.size(); i5++) {
                    DhtSegment.HuffmanTable huffmanTable = dhtSegment.huffmanTables.get(i5);
                    if (huffmanTable.tableClass == 0) {
                        huffmanTableArr = this.huffmanDCTables;
                    } else if (huffmanTable.tableClass == 1) {
                        huffmanTableArr = this.huffmanACTables;
                    } else {
                        throw new ImageReadException("Invalid huffman table class " + huffmanTable.tableClass);
                    }
                    if (huffmanTable.destinationIdentifier < 0 || huffmanTable.destinationIdentifier >= huffmanTableArr.length) {
                        throw new ImageReadException("Invalid huffman table identifier " + huffmanTable.destinationIdentifier);
                    }
                    huffmanTableArr[huffmanTable.destinationIdentifier] = huffmanTable;
                }
            }
        } else {
            if (i != 65472) {
                throw new ImageReadException("Only sequential, baseline JPEGs are supported at the moment");
            }
            this.sofnSegment = new SofnSegment(i, bArr3);
        }
        return true;
    }

    private void rescaleMCU(Block[] blockArr, int i, int i2, Block[] blockArr2) {
        int i3 = 0;
        int i4 = 0;
        while (i4 < blockArr.length) {
            Block block = blockArr[i4];
            if (block.width == i && block.height == i2) {
                System.arraycopy(block.samples, i3, blockArr2[i4].samples, i3, i * i2);
            } else {
                int i5 = i / block.width;
                int i6 = i2 / block.height;
                if (i5 == 2 && i6 == 2) {
                    int i7 = i3;
                    int i8 = i7;
                    int i9 = i8;
                    while (i7 < block.height) {
                        for (int i10 = i3; i10 < i; i10++) {
                            int i11 = block.samples[(i10 >> 1) + i8];
                            blockArr2[i4].samples[i9 + i10] = i11;
                            blockArr2[i4].samples[i9 + i + i10] = i11;
                        }
                        i8 += block.width;
                        i9 += 2 * i;
                        i7++;
                    }
                } else {
                    int i12 = i3;
                    int i13 = i12;
                    while (i12 < i2) {
                        for (int i14 = i3; i14 < i; i14++) {
                            blockArr2[i4].samples[i13 + i14] = block.samples[((i12 / i6) * block.width) + (i14 / i5)];
                        }
                        i13 += i;
                        i12++;
                        i3 = 0;
                    }
                }
            }
            i4++;
            i3 = 0;
        }
    }

    private Block[] allocateMCUMemory() throws ImageReadException {
        Block[] blockArr = new Block[this.sosSegment.numberOfComponents];
        for (int i = 0; i < this.sosSegment.numberOfComponents; i++) {
            SosSegment.Component components = this.sosSegment.getComponents(i);
            SofnSegment.Component components2 = null;
            int i2 = 0;
            while (true) {
                if (i2 >= this.sofnSegment.numberOfComponents) {
                    break;
                }
                if (this.sofnSegment.getComponents(i2).componentIdentifier == components.scanComponentSelector) {
                    components2 = this.sofnSegment.getComponents(i2);
                    break;
                }
                i2++;
            }
            if (components2 == null) {
                throw new ImageReadException("Invalid component");
            }
            blockArr[i] = new Block(components2.horizontalSamplingFactor * 8, 8 * components2.verticalSamplingFactor);
        }
        return blockArr;
    }

    private void readMCU(JpegInputStream jpegInputStream, int[] iArr, Block[] blockArr) throws IOException, ImageReadException {
        int iFastRound;
        JpegDecoder jpegDecoder = this;
        int i = 0;
        int i2 = 0;
        while (i2 < jpegDecoder.sosSegment.numberOfComponents) {
            SosSegment.Component components = jpegDecoder.sosSegment.getComponents(i2);
            SofnSegment.Component components2 = null;
            int i3 = i;
            while (true) {
                if (i3 >= jpegDecoder.sofnSegment.numberOfComponents) {
                    break;
                }
                if (jpegDecoder.sofnSegment.getComponents(i3).componentIdentifier == components.scanComponentSelector) {
                    components2 = jpegDecoder.sofnSegment.getComponents(i3);
                    break;
                }
                i3++;
            }
            if (components2 == null) {
                throw new ImageReadException("Invalid component");
            }
            Block block = blockArr[i2];
            int i4 = i;
            while (i4 < components2.verticalSamplingFactor) {
                int i5 = i;
                while (i5 < components2.horizontalSamplingFactor) {
                    Arrays.fill(jpegDecoder.zz, i);
                    int iDecode = jpegDecoder.decode(jpegInputStream, jpegDecoder.huffmanDCTables[components.dcCodingTableSelector]);
                    jpegDecoder.zz[i] = iArr[i2] + jpegDecoder.extend(jpegDecoder.receive(iDecode, jpegInputStream), iDecode);
                    iArr[i2] = jpegDecoder.zz[i];
                    int i6 = 1;
                    while (true) {
                        int iDecode2 = jpegDecoder.decode(jpegInputStream, jpegDecoder.huffmanACTables[components.acCodingTableSelector]);
                        int i7 = iDecode2 & 15;
                        int i8 = iDecode2 >> 4;
                        if (i7 != 0) {
                            int i9 = i6 + i8;
                            jpegDecoder.zz[i9] = jpegDecoder.receive(i7, jpegInputStream);
                            jpegDecoder.zz[i9] = jpegDecoder.extend(jpegDecoder.zz[i9], i7);
                            if (i9 == 63) {
                                break;
                            }
                            i6 = i9 + 1;
                            jpegDecoder = this;
                            i = 0;
                        } else {
                            if (i8 != 15) {
                                break;
                            }
                            i6 += 16;
                            jpegDecoder = this;
                            i = 0;
                        }
                    }
                    int i10 = 1 << (jpegDecoder.sofnSegment.precision - 1);
                    int i11 = (1 << jpegDecoder.sofnSegment.precision) - 1;
                    float[] fArr = jpegDecoder.scaledQuantizationTables[components2.quantTabDestSelector];
                    ZigZag.zigZagToBlock(jpegDecoder.zz, jpegDecoder.blockInt);
                    for (int i12 = i; i12 < 64; i12++) {
                        jpegDecoder.block[i12] = jpegDecoder.blockInt[i12] * fArr[i12];
                    }
                    Dct.inverseDCT8x8(jpegDecoder.block);
                    int i13 = 8;
                    int i14 = (8 * i4 * 8 * components2.horizontalSamplingFactor) + (8 * i5);
                    int i15 = 0;
                    int i16 = 0;
                    while (i15 < i13) {
                        int i17 = i16;
                        int i18 = 0;
                        while (i18 < i13) {
                            int i19 = i17 + 1;
                            float f = jpegDecoder.block[i17] + i10;
                            if (f < 0.0f) {
                                iFastRound = 0;
                            } else {
                                iFastRound = f > ((float) i11) ? i11 : fastRound(f);
                            }
                            block.samples[i14 + i18] = iFastRound;
                            i18++;
                            i17 = i19;
                            jpegDecoder = this;
                            i13 = 8;
                        }
                        i13 = 8;
                        i14 += components2.horizontalSamplingFactor * 8;
                        i15++;
                        i16 = i17;
                        jpegDecoder = this;
                    }
                    i5++;
                    jpegDecoder = this;
                    i = 0;
                }
                i4++;
                jpegDecoder = this;
                i = 0;
            }
            i2++;
            jpegDecoder = this;
            i = 0;
        }
    }

    private int receive(int i, JpegInputStream jpegInputStream) throws IOException, ImageReadException {
        int i2 = 0;
        int iNextBit = 0;
        while (i2 != i) {
            i2++;
            iNextBit = (iNextBit << 1) + jpegInputStream.nextBit();
        }
        return iNextBit;
    }

    private int decode(JpegInputStream jpegInputStream, DhtSegment.HuffmanTable huffmanTable) throws IOException, ImageReadException {
        int iNextBit = jpegInputStream.nextBit();
        int i = 1;
        while (iNextBit > huffmanTable.getMaxCode()[i]) {
            i++;
            iNextBit = (iNextBit << 1) | jpegInputStream.nextBit();
        }
        return huffmanTable.getHuffVal()[huffmanTable.getValPtr()[i] + (iNextBit - huffmanTable.getMinCode()[i])];
    }

    public BufferedImage decode(ByteSource byteSource) throws Throwable {
        new JpegUtils().traverseJFIF(byteSource, this);
        if (this.imageReadException != null) {
            throw this.imageReadException;
        }
        if (this.ioException != null) {
            throw this.ioException;
        }
        return this.image;
    }
}
