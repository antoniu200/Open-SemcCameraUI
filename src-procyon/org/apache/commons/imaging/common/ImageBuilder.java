// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.util.Properties;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.DirectColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

public class ImageBuilder
{
    private final int[] data;
    private final boolean hasAlpha;
    private final int height;
    private final int width;
    
    public ImageBuilder(final int width, final int height, final boolean hasAlpha) {
        if (width <= 0) {
            throw new RasterFormatException("zero or negative width value");
        }
        if (height <= 0) {
            throw new RasterFormatException("zero or negative height value");
        }
        this.data = new int[width * height];
        this.width = width;
        this.height = height;
        this.hasAlpha = hasAlpha;
    }
    
    private BufferedImage makeBufferedImage(final int[] dataArray, final int n, final int n2, final boolean b) {
        final DataBufferInt dataBufferInt = new DataBufferInt(dataArray, n * n2);
        DirectColorModel cm;
        WritableRaster raster;
        if (b) {
            cm = new DirectColorModel(32, 16711680, 65280, 255, -16777216);
            raster = Raster.createPackedRaster(dataBufferInt, n, n2, n, new int[] { 16711680, 65280, 255, -16777216 }, null);
        }
        else {
            cm = new DirectColorModel(24, 16711680, 65280, 255);
            raster = Raster.createPackedRaster(dataBufferInt, n, n2, n, new int[] { 16711680, 65280, 255 }, null);
        }
        return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), new Properties());
    }
    
    public BufferedImage getBufferedImage() {
        return this.makeBufferedImage(this.data, this.width, this.height, this.hasAlpha);
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getRGB(final int n, final int n2) {
        return this.data[n2 * this.width + n];
    }
    
    public BufferedImage getSubimage(final int n, final int n2, final int n3, final int n4) {
        if (n3 <= 0) {
            throw new RasterFormatException("negative or zero subimage width");
        }
        if (n4 <= 0) {
            throw new RasterFormatException("negative or zero subimage height");
        }
        if (n < 0 || n >= this.width) {
            throw new RasterFormatException("subimage x is outside raster");
        }
        if (n + n3 > this.width) {
            throw new RasterFormatException("subimage (x+width) is outside raster");
        }
        if (n2 < 0 || n2 >= this.height) {
            throw new RasterFormatException("subimage y is outside raster");
        }
        if (n2 + n4 > this.height) {
            throw new RasterFormatException("subimage (y+height) is outside raster");
        }
        final int[] array = new int[n3 * n4];
        int i = 0;
        int n5 = 0;
        while (i < n4) {
            System.arraycopy(this.data, (i + n2) * this.width + n, array, n5, n3);
            n5 += n3;
            ++i;
        }
        return this.makeBufferedImage(array, n3, n4, this.hasAlpha);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setRGB(final int n, final int n2, final int n3) {
        this.data[n2 * this.width + n] = n3;
    }
}
