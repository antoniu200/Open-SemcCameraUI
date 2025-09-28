package org.apache.commons.imaging.formats.tiff.constants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public enum TiffDirectoryType {
    TIFF_DIRECTORY_IFD0(true, 0, "IFD0"),
    TIFF_DIRECTORY_IFD1(true, 1, "IFD1"),
    TIFF_DIRECTORY_IFD2(true, 2, "IFD2"),
    TIFF_DIRECTORY_IFD3(true, 3, "IFD3"),
    EXIF_DIRECTORY_INTEROP_IFD(false, -4, "Interop IFD"),
    EXIF_DIRECTORY_MAKER_NOTES(false, -5, "Maker Notes"),
    EXIF_DIRECTORY_EXIF_IFD(false, -2, "Exif IFD"),
    EXIF_DIRECTORY_GPS(false, -3, "GPS IFD");

    public static final TiffDirectoryType EXIF_DIRECTORY_UNKNOWN = null;
    public final int directoryType;
    private final boolean isImageDirectory;
    public final String name;
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD0 = TIFF_DIRECTORY_IFD0;
    public static final TiffDirectoryType TIFF_DIRECTORY_ROOT = TIFF_DIRECTORY_IFD0;
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD1 = TIFF_DIRECTORY_IFD1;
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD2 = TIFF_DIRECTORY_IFD2;
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD3 = TIFF_DIRECTORY_IFD3;
    public static final TiffDirectoryType EXIF_DIRECTORY_SUB_IFD = TIFF_DIRECTORY_IFD1;
    public static final TiffDirectoryType EXIF_DIRECTORY_SUB_IFD1 = TIFF_DIRECTORY_IFD2;
    public static final TiffDirectoryType EXIF_DIRECTORY_SUB_IFD2 = TIFF_DIRECTORY_IFD3;

    TiffDirectoryType(boolean z, int i, String str) {
        this.isImageDirectory = z;
        this.directoryType = i;
        this.name = str;
    }

    public boolean isImageDirectory() {
        return this.isImageDirectory;
    }

    public static TiffDirectoryType getExifDirectoryType(int i) {
        for (TiffDirectoryType tiffDirectoryType : values()) {
            if (tiffDirectoryType.directoryType == i) {
                return tiffDirectoryType;
            }
        }
        return EXIF_DIRECTORY_UNKNOWN;
    }
}
