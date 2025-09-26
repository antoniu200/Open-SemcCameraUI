package org.apache.commons.imaging.formats.gif;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class GifHeaderInfo {
    public final byte backgroundColorIndex;
    public final byte colorResolution;
    public final boolean globalColorTableFlag;
    public final byte identifier1;
    public final byte identifier2;
    public final byte identifier3;
    public final int logicalScreenHeight;
    public final int logicalScreenWidth;
    public final byte packedFields;
    public final byte pixelAspectRatio;
    public final byte sizeOfGlobalColorTable;
    public final boolean sortFlag;
    public final byte version1;
    public final byte version2;
    public final byte version3;

    public GifHeaderInfo(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, int i, int i2, byte b7, byte b8, byte b9, boolean z, byte b10, boolean z2, byte b11) {
        this.identifier1 = b;
        this.identifier2 = b2;
        this.identifier3 = b3;
        this.version1 = b4;
        this.version2 = b5;
        this.version3 = b6;
        this.logicalScreenWidth = i;
        this.logicalScreenHeight = i2;
        this.packedFields = b7;
        this.backgroundColorIndex = b8;
        this.pixelAspectRatio = b9;
        this.globalColorTableFlag = z;
        this.colorResolution = b10;
        this.sortFlag = z2;
        this.sizeOfGlobalColorTable = b11;
    }
}
