// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.util.List;

public interface ImageMetadata
{
    List<? extends ImageMetadataItem> getItems();
    
    String toString(final String p0);
    
    public interface ImageMetadataItem
    {
        String toString();
        
        String toString(final String p0);
    }
}
