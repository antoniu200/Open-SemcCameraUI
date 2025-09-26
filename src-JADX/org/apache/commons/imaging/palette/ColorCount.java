package org.apache.commons.imaging.palette;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ColorCount {
    public final int alpha;
    public final int argb;
    public final int blue;
    public int count;
    public final int green;
    public final int red;

    public ColorCount(int i) {
        this.argb = i;
        this.alpha = (i >> 24) & 255;
        this.red = (i >> 16) & 255;
        this.green = (i >> 8) & 255;
        this.blue = (i >> 0) & 255;
    }

    public int hashCode() {
        return this.argb;
    }

    public boolean equals(Object obj) {
        return (obj instanceof ColorCount) && ((ColorCount) obj).argb == this.argb;
    }
}
