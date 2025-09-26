// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;
import java.util.List;

public interface MedianCut
{
    boolean performNextMedianCut(final List<ColorGroup> p0, final boolean p1) throws ImageWriteException;
}
