// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;

public interface Palette
{
    int getEntry(final int p0);
    
    int getPaletteIndex(final int p0) throws ImageWriteException;
    
    int length();
}
