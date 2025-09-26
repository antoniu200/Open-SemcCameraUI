package org.apache.commons.imaging.palette;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
enum ColorComponent {
    ALPHA(24),
    RED(16),
    GREEN(8),
    BLUE(0);

    private final int shift;

    ColorComponent(int i) {
        this.shift = i;
    }

    public int argbComponent(int i) {
        return (i >> this.shift) & 255;
    }
}
