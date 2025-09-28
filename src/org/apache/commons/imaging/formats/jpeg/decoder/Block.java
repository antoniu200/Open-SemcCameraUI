package org.apache.commons.imaging.formats.jpeg.decoder;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class Block {
    final int height;
    final int[] samples;
    final int width;

    Block(int i, int i2) {
        this.samples = new int[i * i2];
        this.width = i;
        this.height = i2;
    }
}
