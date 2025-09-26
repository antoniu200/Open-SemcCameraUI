package org.apache.commons.imaging.formats.gif;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ImageDescriptor extends GifBlock {
    final byte[] imageData;
    final int imageHeight;
    final int imageLeftPosition;
    final int imageTopPosition;
    final int imageWidth;
    final boolean interlaceFlag;
    final byte[] localColorTable;
    final boolean localColorTableFlag;
    final byte packedFields;
    final byte sizeOfLocalColorTable;
    final boolean sortFlag;

    ImageDescriptor(int i, int i2, int i3, int i4, int i5, byte b, boolean z, boolean z2, boolean z3, byte b2, byte[] bArr, byte[] bArr2) {
        super(i);
        this.imageLeftPosition = i2;
        this.imageTopPosition = i3;
        this.imageWidth = i4;
        this.imageHeight = i5;
        this.packedFields = b;
        this.localColorTableFlag = z;
        this.interlaceFlag = z2;
        this.sortFlag = z3;
        this.sizeOfLocalColorTable = b2;
        this.localColorTable = bArr;
        this.imageData = bArr2;
    }
}
