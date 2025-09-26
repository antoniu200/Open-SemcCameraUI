// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pcx;

import java.nio.ByteOrder;
import org.apache.commons.imaging.palette.PaletteFactory;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.palette.SimplePalette;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.ImageWriteException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.imaging.PixelDensity;

class PcxWriter
{
    private int bitDepth;
    private int encoding;
    private PixelDensity pixelDensity;
    
    public PcxWriter(final Map<String, Object> m) throws ImageWriteException {
        this.bitDepth = -1;
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        if (hashMap.containsKey("FORMAT")) {
            hashMap.remove("FORMAT");
        }
        this.encoding = 1;
        if (hashMap.containsKey("PCX_COMPRESSION")) {
            final Object remove = hashMap.remove("PCX_COMPRESSION");
            if (remove != null) {
                if (!(remove instanceof Number)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid compression parameter: ");
                    sb.append(remove);
                    throw new ImageWriteException(sb.toString());
                }
                if (((Number)remove).intValue() == 0) {
                    this.encoding = 0;
                }
            }
        }
        if (hashMap.containsKey("PCX_BIT_DEPTH")) {
            final Object remove2 = hashMap.remove("PCX_BIT_DEPTH");
            if (remove2 != null) {
                if (!(remove2 instanceof Number)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid bit depth parameter: ");
                    sb2.append(remove2);
                    throw new ImageWriteException(sb2.toString());
                }
                this.bitDepth = ((Number)remove2).intValue();
            }
        }
        if (hashMap.containsKey("PIXEL_DENSITY")) {
            final Object remove3 = hashMap.remove("PIXEL_DENSITY");
            if (remove3 != null) {
                if (!(remove3 instanceof PixelDensity)) {
                    throw new ImageWriteException("Invalid pixel density parameter");
                }
                this.pixelDensity = (PixelDensity)remove3;
            }
        }
        if (this.pixelDensity == null) {
            this.pixelDensity = PixelDensity.createFromPixelsPerInch(72.0, 72.0);
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unknown parameter: ");
            sb3.append(next);
            throw new ImageWriteException(sb3.toString());
        }
    }
    
    private void write16ColorPCX(final BufferedImage bufferedImage, final SimplePalette simplePalette, final BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int n2;
        final int n = n2 = (bufferedImage.getWidth() + 1) / 2;
        if (n % 2 != 0) {
            n2 = n + 1;
        }
        final byte[] array = new byte[48];
        for (int i = 0; i < 16; ++i) {
            int entry;
            if (i < simplePalette.length()) {
                entry = simplePalette.getEntry(i);
            }
            else {
                entry = 0;
            }
            final int n3 = 3 * i;
            array[n3 + 0] = (byte)(entry >> 16 & 0xFF);
            array[n3 + 1] = (byte)(entry >> 8 & 0xFF);
            array[n3 + 2] = (byte)(entry & 0xFF);
        }
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(4);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(array);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(n2);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        final byte[] a = new byte[n2];
        for (int j = 0; j < bufferedImage.getHeight(); ++j) {
            Arrays.fill(a, (byte)0);
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                final int paletteIndex = simplePalette.getPaletteIndex(bufferedImage.getRGB(k, j) & 0xFFFFFF);
                final int n4 = k / 2;
                a[n4] |= (byte)(paletteIndex << (1 - k % 2) * 4);
            }
            this.writeScanLine(binaryOutputStream, a);
        }
    }
    
    private void write24BppPCX(final BufferedImage bufferedImage, final BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int width;
        if (bufferedImage.getWidth() % 2 == 0) {
            width = bufferedImage.getWidth();
        }
        else {
            width = bufferedImage.getWidth() + 1;
        }
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(8);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(3);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        final int[] rgbArray = new int[bufferedImage.getWidth()];
        final byte[] array = new byte[3 * width];
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            bufferedImage.getRGB(0, i, bufferedImage.getWidth(), 1, rgbArray, 0, bufferedImage.getWidth());
            for (int j = 0; j < rgbArray.length; ++j) {
                array[j] = (byte)(rgbArray[j] >> 16 & 0xFF);
                array[width + j] = (byte)(rgbArray[j] >> 8 & 0xFF);
                array[2 * width + j] = (byte)(rgbArray[j] & 0xFF);
            }
            this.writeScanLine(binaryOutputStream, array);
        }
    }
    
    private void write256ColorPCX(final BufferedImage bufferedImage, final SimplePalette simplePalette, final BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int width;
        if (bufferedImage.getWidth() % 2 == 0) {
            width = bufferedImage.getWidth();
        }
        else {
            width = bufferedImage.getWidth() + 1;
        }
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(8);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        final byte[] array = new byte[width];
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                array[j] = (byte)simplePalette.getPaletteIndex(bufferedImage.getRGB(j, i) & 0xFFFFFF);
            }
            this.writeScanLine(binaryOutputStream, array);
        }
        binaryOutputStream.write(12);
        for (int k = 0; k < 256; ++k) {
            int entry;
            if (k < simplePalette.length()) {
                entry = simplePalette.getEntry(k);
            }
            else {
                entry = 0;
            }
            binaryOutputStream.write(entry >> 16 & 0xFF);
            binaryOutputStream.write(entry >> 8 & 0xFF);
            binaryOutputStream.write(entry & 0xFF);
        }
    }
    
    private void write32BppPCX(final BufferedImage bufferedImage, final BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int width;
        if (bufferedImage.getWidth() % 2 == 0) {
            width = bufferedImage.getWidth();
        }
        else {
            width = bufferedImage.getWidth() + 1;
        }
        binaryOutputStream.write(10);
        binaryOutputStream.write(5);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(32);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(width);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        final int[] rgbArray = new int[bufferedImage.getWidth()];
        final byte[] array = new byte[width * 4];
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            bufferedImage.getRGB(0, i, bufferedImage.getWidth(), 1, rgbArray, 0, bufferedImage.getWidth());
            for (int j = 0; j < rgbArray.length; ++j) {
                final int n = 4 * j;
                array[n + 0] = (byte)(rgbArray[j] & 0xFF);
                array[n + 1] = (byte)(rgbArray[j] >> 8 & 0xFF);
                array[n + 2] = (byte)(rgbArray[j] >> 16 & 0xFF);
                array[n + 3] = 0;
            }
            this.writeScanLine(binaryOutputStream, array);
        }
    }
    
    private void writeBlackAndWhitePCX(final BufferedImage bufferedImage, final BinaryOutputStream binaryOutputStream) throws ImageWriteException, IOException {
        int n2;
        final int n = n2 = (bufferedImage.getWidth() + 7) / 8;
        if (n % 2 != 0) {
            n2 = n + 1;
        }
        binaryOutputStream.write(10);
        binaryOutputStream.write(3);
        binaryOutputStream.write(this.encoding);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(bufferedImage.getWidth() - 1);
        binaryOutputStream.write2Bytes(bufferedImage.getHeight() - 1);
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.horizontalDensityInches()));
        binaryOutputStream.write2Bytes((short)Math.round(this.pixelDensity.verticalDensityInches()));
        binaryOutputStream.write(new byte[48]);
        binaryOutputStream.write(0);
        binaryOutputStream.write(1);
        binaryOutputStream.write2Bytes(n2);
        binaryOutputStream.write2Bytes(1);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write2Bytes(0);
        binaryOutputStream.write(new byte[54]);
        final byte[] a = new byte[n2];
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            Arrays.fill(a, (byte)0);
            for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                final int n3 = bufferedImage.getRGB(j, i) & 0xFFFFFF;
                int n4;
                if (n3 == 0) {
                    n4 = 0;
                }
                else {
                    if (n3 != 16777215) {
                        throw new ImageWriteException("Pixel neither black nor white");
                    }
                    n4 = 1;
                }
                final int n5 = j / 8;
                a[n5] |= (byte)(n4 << 7 - j % 8);
            }
            this.writeScanLine(binaryOutputStream, a);
        }
    }
    
    private void writeScanLine(final BinaryOutputStream binaryOutputStream, final byte[] array) throws IOException, ImageWriteException {
        if (this.encoding == 0) {
            binaryOutputStream.write(array);
        }
        else {
            if (this.encoding != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid PCX encoding ");
                sb.append(this.encoding);
                throw new ImageWriteException(sb.toString());
            }
            int n = -1;
            final int length = array.length;
            int i = 0;
            int n2 = 0;
            while (i < length) {
                final int n3 = array[i] & 0xFF;
                if (n3 == n && n2 < 63) {
                    ++n2;
                }
                else {
                    if (n2 > 0) {
                        if (n2 == 1 && (n & 0xC0) != 0xC0) {
                            binaryOutputStream.write(n);
                        }
                        else {
                            binaryOutputStream.write(n2 | 0xC0);
                            binaryOutputStream.write(n);
                        }
                    }
                    n2 = 1;
                    n = n3;
                }
                ++i;
            }
            if (n2 > 0) {
                if (n2 == 1 && (n & 0xC0) != 0xC0) {
                    binaryOutputStream.write(n);
                }
                else {
                    binaryOutputStream.write(0xC0 | n2);
                    binaryOutputStream.write(n);
                }
            }
        }
    }
    
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream) throws ImageWriteException, IOException {
        final SimplePalette exactRgbPaletteSimple = new PaletteFactory().makeExactRgbPaletteSimple(bufferedImage, 256);
        final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        if (exactRgbPaletteSimple != null && this.bitDepth != 24 && this.bitDepth != 32) {
            if (exactRgbPaletteSimple.length() <= 16 && this.bitDepth != 8) {
                if (exactRgbPaletteSimple.length() <= 2 && this.bitDepth != 4) {
                    boolean b = false;
                    Label_0129: {
                        if (exactRgbPaletteSimple.length() >= 1) {
                            final int entry = exactRgbPaletteSimple.getEntry(0);
                            if (entry != 0 && entry != 16777215) {
                                b = false;
                                break Label_0129;
                            }
                        }
                        b = true;
                    }
                    boolean b2 = b;
                    if (exactRgbPaletteSimple.length() == 2) {
                        final int entry2 = exactRgbPaletteSimple.getEntry(1);
                        b2 = b;
                        if (entry2 != 0) {
                            b2 = b;
                            if (entry2 != 16777215) {
                                b2 = false;
                            }
                        }
                    }
                    if (b2) {
                        this.writeBlackAndWhitePCX(bufferedImage, binaryOutputStream);
                    }
                    else {
                        this.write16ColorPCX(bufferedImage, exactRgbPaletteSimple, binaryOutputStream);
                    }
                }
                else {
                    this.write16ColorPCX(bufferedImage, exactRgbPaletteSimple, binaryOutputStream);
                }
            }
            else {
                this.write256ColorPCX(bufferedImage, exactRgbPaletteSimple, binaryOutputStream);
            }
        }
        else if (this.bitDepth == 32) {
            this.write32BppPCX(bufferedImage, binaryOutputStream);
        }
        else {
            this.write24BppPCX(bufferedImage, binaryOutputStream);
        }
    }
}
