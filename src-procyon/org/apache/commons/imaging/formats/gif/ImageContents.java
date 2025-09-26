// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.gif;

import java.util.List;

class ImageContents
{
    final List<GifBlock> blocks;
    final GifHeaderInfo gifHeaderInfo;
    final byte[] globalColorTable;
    
    ImageContents(final GifHeaderInfo gifHeaderInfo, final byte[] globalColorTable, final List<GifBlock> blocks) {
        this.gifHeaderInfo = gifHeaderInfo;
        this.globalColorTable = globalColorTable;
        this.blocks = blocks;
    }
}
