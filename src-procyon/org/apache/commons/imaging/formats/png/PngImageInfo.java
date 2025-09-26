// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

import java.util.Collection;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageFormat;
import java.util.List;
import org.apache.commons.imaging.ImageInfo;

public class PngImageInfo extends ImageInfo
{
    private final List<PngText> textChunks;
    
    PngImageInfo(final String s, final int n, final List<String> list, final ImageFormat imageFormat, final String s2, final int n2, final String s3, final int n3, final int n4, final float n5, final int n6, final float n7, final int n8, final boolean b, final boolean b2, final boolean b3, final ColorType colorType, final CompressionAlgorithm compressionAlgorithm, final List<PngText> textChunks) {
        super(s, n, list, imageFormat, s2, n2, s3, n3, n4, n5, n6, n7, n8, b, b2, b3, colorType, compressionAlgorithm);
        this.textChunks = textChunks;
    }
    
    public List<PngText> getTextChunks() {
        return new ArrayList<PngText>(this.textChunks);
    }
}
