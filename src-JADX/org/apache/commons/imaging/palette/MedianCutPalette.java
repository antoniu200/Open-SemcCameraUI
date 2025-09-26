package org.apache.commons.imaging.palette;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class MedianCutPalette extends SimplePalette {
    private final ColorGroup root;

    public MedianCutPalette(ColorGroup colorGroup, int[] iArr) {
        super(iArr);
        this.root = colorGroup;
    }

    @Override // org.apache.commons.imaging.palette.SimplePalette, org.apache.commons.imaging.palette.Palette
    public int getPaletteIndex(int i) {
        ColorGroup colorGroup = this.root;
        while (colorGroup.cut != null) {
            colorGroup = colorGroup.cut.getColorGroup(i);
        }
        return colorGroup.paletteIndex;
    }
}
