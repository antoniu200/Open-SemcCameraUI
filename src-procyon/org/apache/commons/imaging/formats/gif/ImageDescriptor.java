// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.gif;

class ImageDescriptor extends GifBlock
{
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
    
    ImageDescriptor(final int n, final int imageLeftPosition, final int imageTopPosition, final int imageWidth, final int imageHeight, final byte packedFields, final boolean localColorTableFlag, final boolean interlaceFlag, final boolean sortFlag, final byte sizeOfLocalColorTable, final byte[] localColorTable, final byte[] imageData) {
        super(n);
        this.imageLeftPosition = imageLeftPosition;
        this.imageTopPosition = imageTopPosition;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.packedFields = packedFields;
        this.localColorTableFlag = localColorTableFlag;
        this.interlaceFlag = interlaceFlag;
        this.sortFlag = sortFlag;
        this.sizeOfLocalColorTable = sizeOfLocalColorTable;
        this.localColorTable = localColorTable;
        this.imageData = imageData;
    }
}
