// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

public enum ImageFormats implements ImageFormat
{
    private static final ImageFormats[] $VALUES;
    
    BMP, 
    DCX, 
    GIF, 
    ICNS, 
    ICO, 
    JBIG2, 
    JPEG, 
    PAM, 
    PBM, 
    PCX, 
    PGM, 
    PNG, 
    PNM, 
    PPM, 
    PSD, 
    RGBE, 
    TGA, 
    TIFF, 
    UNKNOWN, 
    WBMP, 
    XBM, 
    XPM;
    
    static {
        $VALUES = new ImageFormats[] { ImageFormats.UNKNOWN, ImageFormats.BMP, ImageFormats.DCX, ImageFormats.GIF, ImageFormats.ICNS, ImageFormats.ICO, ImageFormats.JBIG2, ImageFormats.JPEG, ImageFormats.PAM, ImageFormats.PSD, ImageFormats.PBM, ImageFormats.PGM, ImageFormats.PNM, ImageFormats.PPM, ImageFormats.PCX, ImageFormats.PNG, ImageFormats.RGBE, ImageFormats.TGA, ImageFormats.TIFF, ImageFormats.WBMP, ImageFormats.XBM, ImageFormats.XPM };
    }
    
    @Override
    public String getExtension() {
        return this.name();
    }
    
    @Override
    public String getName() {
        return this.name();
    }
}
