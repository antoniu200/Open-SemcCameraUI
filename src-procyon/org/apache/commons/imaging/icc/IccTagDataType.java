// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.icc;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;

interface IccTagDataType
{
    void dump(final String p0, final byte[] p1) throws ImageReadException, IOException;
    
    String getName();
    
    int getSignature();
}
