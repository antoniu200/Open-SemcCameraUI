// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd.datareaders;

import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.formats.psd.ImageContents;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public interface DataReader
{
    void readData(final InputStream p0, final BufferedImage p1, final ImageContents p2, final BinaryFileParser p3) throws ImageReadException, IOException;
}
