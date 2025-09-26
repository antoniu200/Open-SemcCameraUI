package org.apache.commons.imaging.common;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;
import java.util.Properties;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageBuilder {
    private final int[] data;
    private final boolean hasAlpha;
    private final int height;
    private final int width;

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.awt.image.RasterFormatException */
    public ImageBuilder(int i, int i2, boolean z) throws RasterFormatException {
        if (i <= 0) {
            throw new RasterFormatException("zero or negative width value");
        }
        if (i2 <= 0) {
            throw new RasterFormatException("zero or negative height value");
        }
        this.data = new int[i * i2];
        this.width = i;
        this.height = i2;
        this.hasAlpha = z;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRGB(int i, int i2) {
        return this.data[(i2 * this.width) + i];
    }

    public void setRGB(int i, int i2, int i3) {
        this.data[(i2 * this.width) + i] = i3;
    }

    public BufferedImage getBufferedImage() {
        return makeBufferedImage(this.data, this.width, this.height, this.hasAlpha);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: java.awt.image.RasterFormatException */
    public BufferedImage getSubimage(int i, int i2, int i3, int i4) throws RasterFormatException {
        if (i3 <= 0) {
            throw new RasterFormatException("negative or zero subimage width");
        }
        if (i4 <= 0) {
            throw new RasterFormatException("negative or zero subimage height");
        }
        if (i < 0 || i >= this.width) {
            throw new RasterFormatException("subimage x is outside raster");
        }
        if (i + i3 > this.width) {
            throw new RasterFormatException("subimage (x+width) is outside raster");
        }
        if (i2 < 0 || i2 >= this.height) {
            throw new RasterFormatException("subimage y is outside raster");
        }
        if (i2 + i4 > this.height) {
            throw new RasterFormatException("subimage (y+height) is outside raster");
        }
        int[] iArr = new int[i3 * i4];
        int i5 = 0;
        for (int i6 = 0; i6 < i4; i6++) {
            System.arraycopy(this.data, ((i6 + i2) * this.width) + i, iArr, i5, i3);
            i5 += i3;
        }
        return makeBufferedImage(iArr, i3, i4, this.hasAlpha);
    }

    private BufferedImage makeBufferedImage(int[] iArr, int i, int i2, boolean z) {
        DirectColorModel directColorModel;
        WritableRaster writableRasterCreatePackedRaster;
        DataBufferInt dataBufferInt = new DataBufferInt(iArr, i * i2);
        if (z) {
            directColorModel = new DirectColorModel(32, 16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255, ViewCompat.MEASURED_STATE_MASK);
            writableRasterCreatePackedRaster = Raster.createPackedRaster(dataBufferInt, i, i2, i, new int[]{16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255, ViewCompat.MEASURED_STATE_MASK}, (Point) null);
        } else {
            directColorModel = new DirectColorModel(24, 16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255);
            writableRasterCreatePackedRaster = Raster.createPackedRaster(dataBufferInt, i, i2, i, new int[]{16711680, MotionEventCompat.ACTION_POINTER_INDEX_MASK, 255}, (Point) null);
        }
        return new BufferedImage(directColorModel, writableRasterCreatePackedRaster, directColorModel.isAlphaPremultiplied(), new Properties());
    }
}
