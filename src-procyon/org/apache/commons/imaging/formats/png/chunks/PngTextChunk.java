// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png.chunks;

import org.apache.commons.imaging.formats.png.PngText;

public abstract class PngTextChunk extends PngChunk
{
    public PngTextChunk(final int n, final int n2, final int n3, final byte[] array) {
        super(n, n2, n3, array);
    }
    
    public abstract PngText getContents();
    
    public abstract String getKeyword();
    
    public abstract String getText();
}
