package org.apache.commons.imaging;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum ImageFormats implements ImageFormat {
    UNKNOWN,
    BMP,
    DCX,
    GIF,
    ICNS,
    ICO,
    JBIG2,
    JPEG,
    PAM,
    PSD,
    PBM,
    PGM,
    PNM,
    PPM,
    PCX,
    PNG,
    RGBE,
    TGA,
    TIFF,
    WBMP,
    XBM,
    XPM;

    @Override // org.apache.commons.imaging.ImageFormat
    public String getName() {
        return name();
    }

    @Override // org.apache.commons.imaging.ImageFormat
    public String getExtension() {
        return name();
    }
}
