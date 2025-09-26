// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.constants;

public enum TiffDirectoryType
{
    private static final TiffDirectoryType[] $VALUES;
    
    EXIF_DIRECTORY_EXIF_IFD(false, -2, "Exif IFD"), 
    EXIF_DIRECTORY_GPS(false, -3, "GPS IFD");
    
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD0;
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD1;
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD2;
    public static final TiffDirectoryType EXIF_DIRECTORY_IFD3;
    
    EXIF_DIRECTORY_INTEROP_IFD(false, -4, "Interop IFD"), 
    EXIF_DIRECTORY_MAKER_NOTES(false, -5, "Maker Notes");
    
    public static final TiffDirectoryType EXIF_DIRECTORY_SUB_IFD;
    public static final TiffDirectoryType EXIF_DIRECTORY_SUB_IFD1;
    public static final TiffDirectoryType EXIF_DIRECTORY_SUB_IFD2;
    public static final TiffDirectoryType EXIF_DIRECTORY_UNKNOWN;
    
    TIFF_DIRECTORY_IFD0(true, 0, "IFD0"), 
    TIFF_DIRECTORY_IFD1(true, 1, "IFD1"), 
    TIFF_DIRECTORY_IFD2(true, 2, "IFD2"), 
    TIFF_DIRECTORY_IFD3(true, 3, "IFD3");
    
    public static final TiffDirectoryType TIFF_DIRECTORY_ROOT;
    public final int directoryType;
    private final boolean isImageDirectory;
    public final String name;
    
    static {
        $VALUES = new TiffDirectoryType[] { TiffDirectoryType.TIFF_DIRECTORY_IFD0, TiffDirectoryType.TIFF_DIRECTORY_IFD1, TiffDirectoryType.TIFF_DIRECTORY_IFD2, TiffDirectoryType.TIFF_DIRECTORY_IFD3, TiffDirectoryType.EXIF_DIRECTORY_INTEROP_IFD, TiffDirectoryType.EXIF_DIRECTORY_MAKER_NOTES, TiffDirectoryType.EXIF_DIRECTORY_EXIF_IFD, TiffDirectoryType.EXIF_DIRECTORY_GPS };
        EXIF_DIRECTORY_IFD0 = TiffDirectoryType.TIFF_DIRECTORY_IFD0;
        TIFF_DIRECTORY_ROOT = TiffDirectoryType.TIFF_DIRECTORY_IFD0;
        EXIF_DIRECTORY_IFD1 = TiffDirectoryType.TIFF_DIRECTORY_IFD1;
        EXIF_DIRECTORY_IFD2 = TiffDirectoryType.TIFF_DIRECTORY_IFD2;
        EXIF_DIRECTORY_IFD3 = TiffDirectoryType.TIFF_DIRECTORY_IFD3;
        EXIF_DIRECTORY_SUB_IFD = TiffDirectoryType.TIFF_DIRECTORY_IFD1;
        EXIF_DIRECTORY_SUB_IFD1 = TiffDirectoryType.TIFF_DIRECTORY_IFD2;
        EXIF_DIRECTORY_SUB_IFD2 = TiffDirectoryType.TIFF_DIRECTORY_IFD3;
    }
    
    private TiffDirectoryType(final boolean isImageDirectory, final int directoryType, final String name2) {
        this.isImageDirectory = isImageDirectory;
        this.directoryType = directoryType;
        this.name = name2;
    }
    
    public static TiffDirectoryType getExifDirectoryType(final int n) {
        for (final TiffDirectoryType tiffDirectoryType : values()) {
            if (tiffDirectoryType.directoryType == n) {
                return tiffDirectoryType;
            }
        }
        return TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN;
    }
    
    public boolean isImageDirectory() {
        return this.isImageDirectory;
    }
}
