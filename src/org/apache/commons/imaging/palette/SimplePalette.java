package org.apache.commons.imaging.palette;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SimplePalette implements Palette {
    private final int[] palette;

    public SimplePalette(int[] iArr) {
        this.palette = iArr;
    }

    @Override // org.apache.commons.imaging.palette.Palette
    public int getPaletteIndex(int i) {
        for (int i2 = 0; i2 < this.palette.length; i2++) {
            if (this.palette[i2] == i) {
                return i2;
            }
        }
        return -1;
    }

    @Override // org.apache.commons.imaging.palette.Palette
    public int getEntry(int i) {
        return this.palette[i];
    }

    @Override // org.apache.commons.imaging.palette.Palette
    public int length() {
        return this.palette.length;
    }
}
