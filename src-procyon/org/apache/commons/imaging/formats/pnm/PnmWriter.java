// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.pnm;

import java.io.IOException;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Map;
import java.io.OutputStream;
import java.awt.image.BufferedImage;

interface PnmWriter
{
    void writeImage(final BufferedImage p0, final OutputStream p1, final Map<String, Object> p2) throws ImageWriteException, IOException;
}
