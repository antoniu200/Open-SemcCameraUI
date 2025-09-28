package org.apache.commons.imaging.formats.pcx;

import android.support.v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.PixelDensity;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.palette.PaletteFactory;
import org.apache.commons.imaging.palette.SimplePalette;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PcxWriter {
    private int bitDepth;
    private int encoding;
    private PixelDensity pixelDensity;

    public PcxWriter(Map<String, Object> map) throws ImageWriteException {
        Object objRemove;
        Object objRemove2;
        Object objRemove3;
        this.bitDepth = -1;
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        this.encoding = 1;
        if (map2.containsKey(PcxConstants.PARAM_KEY_PCX_COMPRESSION) && (objRemove3 = map2.remove(PcxConstants.PARAM_KEY_PCX_COMPRESSION)) != null) {
            if (!(objRemove3 instanceof Number)) {
                throw new ImageWriteException("Invalid compression parameter: " + objRemove3);
            }
            if (((Number) objRemove3).intValue() == 0) {
                this.encoding = 0;
            }
        }
        if (map2.containsKey(PcxConstants.PARAM_KEY_PCX_BIT_DEPTH) && (objRemove2 = map2.remove(PcxConstants.PARAM_KEY_PCX_BIT_DEPTH)) != null) {
            if (!(objRemove2 instanceof Number)) {
                throw new ImageWriteException("Invalid bit depth parameter: " + objRemove2);
            }
            this.bitDepth = ((Number) objRemove2).intValue();
        }
        if (map2.containsKey(ImagingConstants.PARAM_KEY_PIXEL_DENSITY) && (objRemove = map2.remove(ImagingConstants.PARAM_KEY_PIXEL_DENSITY)) != null) {
            if (!(objRemove instanceof PixelDensity)) {
                throw new ImageWriteException("Invalid pixel density parameter");
            }
            this.pixelDensity = (PixelDensity) objRemove;
        }
        if (this.pixelDensity == null) {
            this.pixelDensity = PixelDensity.createFromPixelsPerInch(72.0d, 72.0d);
        }
        if (map2.isEmpty()) {
            return;
        }
        throw new ImageWriteException("Unknown parameter: " + map2.keySet().iterator().next());
    }

    private void writeScanLine(BinaryOutputStream binaryOutputStream, byte[] bArr) throws ImageWriteException, IOException {
        if (this.encoding == 0) {
            binaryOutputStream.write(bArr);
            return;
        }
        if (this.encoding == 1) {
            int i = -1;
            int i2 = 0;
            for (byte b : bArr) {
                int i3 = b & 255;
                if (i3 != i || i2 >= 63) {
                    if (i2 > 0) {
                        if (i2 == 1 && (i & 192) != 192) {
                            binaryOutputStream.write(i);
                        } else {
                            binaryOutputStream.write(i2 | 192);
                            binaryOutputStream.write(i);
                        }
                    }
                    i2 = 1;
                    i = i3;
                } else {
                    i2++;
                }
            }
            if (i2 > 0) {
                if (i2 == 1 && (i & 192) != 192) {
                    binaryOutputStream.write(i);
                    return;
                } else {
                    binaryOutputStream.write(192 | i2);
                    binaryOutputStream.write(i);
                    return;
                }
            }
            return;
        }
        throw new ImageWriteException("Invalid PCX encoding " + this.encoding);
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream) throws ImageWriteException, IOException {
        int entry;
        int entry2;
        SimplePalette simplePaletteMakeExactRgbPaletteSimple = new PaletteFactory().makeExactRgbPaletteSimple(bufferedImage, 256);
        BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        if (simplePaletteMakeExactRgbPaletteSimple == null || this.bitDepth == 24 || this.bitDepth == 32) {
            if (this.bitDepth == 32) {
                write32BppPCX(bufferedImage, binaryOutputStream);
                return;
            } else {
                write24BppPCX(bufferedImage, binaryOutputStream);
                return;
            }
        }
        if (simplePaletteMakeExactRgbPaletteSimple.length() > 16 || this.bitDepth == 8) {
            write256ColorPCX(bufferedImage, simplePaletteMakeExactRgbPaletteSimple, binaryOutputStream);
            return;
        }
        if (simplePaletteMakeExactRgbPaletteSimple.length() > 2 || this.bitDepth == 4) {
            write16ColorPCX(bufferedImage, simplePaletteMakeExactRgbPaletteSimple, binaryOutputStream);
            return;
        }
        boolean z = simplePaletteMakeExactRgbPaletteSimple.length() < 1 || (entry2 = simplePaletteMakeExactRgbPaletteSimple.getEntry(0)) == 0 || entry2 == 16777215;
        if (simplePaletteMakeExactRgbPaletteSimple.length() == 2 && (entry = simplePaletteMakeExactRgbPaletteSimple.getEntry(1)) != 0 && entry != 16777215) {
            z = false;
        }
        if (z) {
            writeBlackAndWhitePCX(bufferedImage, binaryOutputStream);
        } else {
            write16ColorPCX(bufferedImage, simplePaletteMakeExactRgbPaletteSimple, binaryOutputStream);
        }
    }

    private void write32BppPCX(BufferedImage bufferedImage, BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int width = bufferedImage.getWidth() % 2 == 0 ? bufferedImage.getWidth() : bufferedImage.getWidth() + 1;
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(32);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        int[] iArr = new int[bufferedImage.getWidth()];
        byte[] bArr = new byte[width * 4];
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            bufferedImage.getRGB(0, i, bufferedImage.getWidth(), 1, iArr, 0, bufferedImage.getWidth());
            for (int i2 = 0; i2 < iArr.length; i2++) {
                int i3 = 4 * i2;
                bArr[i3 + 0] = (byte) (iArr[i2] & 255);
                bArr[i3 + 1] = (byte) ((iArr[i2] >> 8) & 255);
                bArr[i3 + 2] = (byte) ((iArr[i2] >> 16) & 255);
                bArr[i3 + 3] = 0;
            }
            writeScanLine(binaryOutputStream, bArr);
        }
    }

    private void write24BppPCX(BufferedImage bufferedImage, BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int width = bufferedImage.getWidth() % 2 == 0 ? bufferedImage.getWidth() : bufferedImage.getWidth() + 1;
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(8);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(3);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        int[] iArr = new int[bufferedImage.getWidth()];
        byte[] bArr = new byte[3 * width];
        int i = 0;
        while (i < bufferedImage.getHeight()) {
            int i2 = i;
            bufferedImage.getRGB(0, i, bufferedImage.getWidth(), 1, iArr, 0, bufferedImage.getWidth());
            for (int i3 = 0; i3 < iArr.length; i3++) {
                bArr[i3] = (byte) ((iArr[i3] >> 16) & 255);
                bArr[width + i3] = (byte) ((iArr[i3] >> 8) & 255);
                bArr[(2 * width) + i3] = (byte) (iArr[i3] & 255);
            }
            writeScanLine(binaryOutputStream, bArr);
            i = i2 + 1;
        }
    }

    private void writeBlackAndWhitePCX(BufferedImage bufferedImage, BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int i;
        int width = (bufferedImage.getWidth() + 7) / 8;
        if (width % 2 != 0) {
            width++;
        }
        binaryOutputStream.write(10);
        binaryOutputStream.write(3);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        byte[] bArr = new byte[width];
        for (int i2 = 0; i2 < bufferedImage.getHeight(); i2++) {
            Arrays.fill(bArr, (byte) 0);
            for (int i3 = 0; i3 < bufferedImage.getWidth(); i3++) {
                int rgb = bufferedImage.getRGB(i3, i2) & ViewCompat.MEASURED_SIZE_MASK;
                if (rgb == 0) {
                    i = 0;
                } else {
                    if (rgb != 16777215) {
                        throw new ImageWriteException("Pixel neither black nor white");
                    }
                    i = 1;
                }
                int i4 = i3 / 8;
                bArr[i4] = (byte) ((i << (7 - (i3 % 8))) | bArr[i4]);
            }
            writeScanLine(binaryOutputStream, bArr);
        }
    }

    private void write16ColorPCX(BufferedImage bufferedImage, SimplePalette simplePalette, BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int width = (bufferedImage.getWidth() + 1) / 2;
        if (width % 2 != 0) {
            width++;
        }
        byte[] bArr = new byte[48];
        int i = 0;
        while (i < 16) {
            int entry = i < simplePalette.length() ? simplePalette.getEntry(i) : 0;
            int i2 = 3 * i;
            bArr[i2 + 0] = (byte) ((entry >> 16) & 255);
            bArr[i2 + 1] = (byte) ((entry >> 8) & 255);
            bArr[i2 + 2] = (byte) (entry & 255);
            i++;
        }
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(4);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(bArr);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        byte[] bArr2 = new byte[width];
        for (int i3 = 0; i3 < bufferedImage.getHeight(); i3++) {
            Arrays.fill(bArr2, (byte) 0);
            for (int i4 = 0; i4 < bufferedImage.getWidth(); i4++) {
                int i5 = i4 / 2;
                bArr2[i5] = (byte) ((simplePalette.getPaletteIndex(bufferedImage.getRGB(i4, i3) & ViewCompat.MEASURED_SIZE_MASK) << ((1 - (i4 % 2)) * 4)) | bArr2[i5]);
            }
            writeScanLine(binaryOutputStream, bArr2);
        }
    }

    private void write256ColorPCX(BufferedImage bufferedImage, SimplePalette simplePalette, BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int width = bufferedImage.getWidth() % 2 == 0 ? bufferedImage.getWidth() : bufferedImage.getWidth() + 1;
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(8);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short) Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        byte[] bArr = new byte[width];
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int i2 = 0; i2 < bufferedImage.getWidth(); i2++) {
                bArr[i2] = (byte) simplePalette.getPaletteIndex(bufferedImage.getRGB(i2, i) & ViewCompat.MEASURED_SIZE_MASK);
            }
            writeScanLine(binaryOutputStream, bArr);
        }
        binaryOutputStream.write(12);
        int i3 = 0;
        while (i3 < 256) {
            int entry = i3 < simplePalette.length() ? simplePalette.getEntry(i3) : 0;
            binaryOutputStream.write((entry >> 16) & 255);
            binaryOutputStream.write((entry >> 8) & 255);
            binaryOutputStream.write(entry & 255);
            i3++;
        }
    }
}
