// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.gif;

class GifHeaderInfo
{
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
    
    public GifHeaderInfo(final byte identifier1, final byte identifier2, final byte identifier3, final byte version1, final byte version2, final byte version3, final int logicalScreenWidth, final int logicalScreenHeight, final byte packedFields, final byte backgroundColorIndex, final byte pixelAspectRatio, final boolean globalColorTableFlag, final byte colorResolution, final boolean sortFlag, final byte sizeOfGlobalColorTable) {
        this.identifier1 = identifier1;
        this.identifier2 = identifier2;
        this.identifier3 = identifier3;
        this.version1 = version1;
        this.version2 = version2;
        this.version3 = version3;
        this.logicalScreenWidth = logicalScreenWidth;
        this.logicalScreenHeight = logicalScreenHeight;
        this.packedFields = packedFields;
        this.backgroundColorIndex = backgroundColorIndex;
        this.pixelAspectRatio = pixelAspectRatio;
        this.globalColorTableFlag = globalColorTableFlag;
        this.colorResolution = colorResolution;
        this.sortFlag = sortFlag;
        this.sizeOfGlobalColorTable = sizeOfGlobalColorTable;
    }
}
