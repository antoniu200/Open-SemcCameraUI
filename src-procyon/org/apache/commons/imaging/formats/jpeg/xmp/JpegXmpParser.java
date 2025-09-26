// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.xmp;

import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;

public class JpegXmpParser extends BinaryFileParser
{
    public JpegXmpParser() {
        this.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    public boolean isXmpJpegSegment(final byte[] array) {
        return BinaryFunctions.startsWith(array, JpegConstants.XMP_IDENTIFIER);
    }
    
    public String parseXmpJpegSegment(final byte[] bytes) throws ImageReadException {
        if (!this.isXmpJpegSegment(bytes)) {
            throw new ImageReadException("Invalid JPEG XMP Segment.");
        }
        final int size = JpegConstants.XMP_IDENTIFIER.size();
        try {
            return new String(bytes, size, bytes.length - size, "utf-8");
        }
        catch (final UnsupportedEncodingException ex) {
            throw new ImageReadException("Invalid JPEG XMP Segment.", ex);
        }
    }
}
