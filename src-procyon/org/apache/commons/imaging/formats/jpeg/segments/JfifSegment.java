// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import java.io.InputStream;

public class JfifSegment extends Segment
{
    public final int densityUnits;
    public final int jfifMajorVersion;
    public final int jfifMinorVersion;
    public final int thumbnailSize;
    public final int xDensity;
    public final int xThumbnail;
    public final int yDensity;
    public final int yThumbnail;
    
    public JfifSegment(final int n, final int n2, final InputStream inputStream) throws ImageReadException, IOException {
        super(n, n2);
        final byte[] bytes = BinaryFunctions.readBytes(inputStream, JpegConstants.JFIF0_SIGNATURE.size());
        if (!JpegConstants.JFIF0_SIGNATURE.equals(bytes) && !JpegConstants.JFIF0_SIGNATURE_ALTERNATIVE.equals(bytes)) {
            throw new ImageReadException("Not a Valid JPEG File: missing JFIF string");
        }
        this.jfifMajorVersion = BinaryFunctions.readByte("JFIF_major_version", inputStream, "Not a Valid JPEG File");
        this.jfifMinorVersion = BinaryFunctions.readByte("JFIF_minor_version", inputStream, "Not a Valid JPEG File");
        this.densityUnits = BinaryFunctions.readByte("density_units", inputStream, "Not a Valid JPEG File");
        this.xDensity = BinaryFunctions.read2Bytes("x_density", inputStream, "Not a Valid JPEG File", this.getByteOrder());
        this.yDensity = BinaryFunctions.read2Bytes("y_density", inputStream, "Not a Valid JPEG File", this.getByteOrder());
        this.xThumbnail = BinaryFunctions.readByte("x_thumbnail", inputStream, "Not a Valid JPEG File");
        this.yThumbnail = BinaryFunctions.readByte("y_thumbnail", inputStream, "Not a Valid JPEG File");
        this.thumbnailSize = this.xThumbnail * this.yThumbnail;
        if (this.thumbnailSize > 0) {
            BinaryFunctions.skipBytes(inputStream, this.thumbnailSize, "Not a Valid JPEG File: missing thumbnail");
        }
        if (this.getDebug()) {
            System.out.println("");
        }
    }
    
    public JfifSegment(final int n, final byte[] buf) throws ImageReadException, IOException {
        this(n, buf.length, new ByteArrayInputStream(buf));
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("JFIF (");
        sb.append(this.getSegmentType());
        sb.append(")");
        return sb.toString();
    }
}
