package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JfifSegment extends Segment {
    public final int densityUnits;
    public final int jfifMajorVersion;
    public final int jfifMinorVersion;
    public final int thumbnailSize;
    public final int xDensity;
    public final int xThumbnail;
    public final int yDensity;
    public final int yThumbnail;

    @Override // org.apache.commons.imaging.formats.jpeg.segments.Segment
    public String getDescription() {
        return "JFIF (" + getSegmentType() + ")";
    }

    public JfifSegment(int i, byte[] bArr) throws IOException, ImageReadException {
        this(i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public JfifSegment(int i, int i2, InputStream inputStream) throws IOException, ImageReadException {
        super(i, i2);
        byte[] bytes = BinaryFunctions.readBytes(inputStream, JpegConstants.JFIF0_SIGNATURE.size());
        if (!JpegConstants.JFIF0_SIGNATURE.equals(bytes) && !JpegConstants.JFIF0_SIGNATURE_ALTERNATIVE.equals(bytes)) {
            throw new ImageReadException("Not a Valid JPEG File: missing JFIF string");
        }
        this.jfifMajorVersion = BinaryFunctions.readByte("JFIF_major_version", inputStream, "Not a Valid JPEG File");
        this.jfifMinorVersion = BinaryFunctions.readByte("JFIF_minor_version", inputStream, "Not a Valid JPEG File");
        this.densityUnits = BinaryFunctions.readByte("density_units", inputStream, "Not a Valid JPEG File");
        this.xDensity = BinaryFunctions.read2Bytes("x_density", inputStream, "Not a Valid JPEG File", getByteOrder());
        this.yDensity = BinaryFunctions.read2Bytes("y_density", inputStream, "Not a Valid JPEG File", getByteOrder());
        this.xThumbnail = BinaryFunctions.readByte("x_thumbnail", inputStream, "Not a Valid JPEG File");
        this.yThumbnail = BinaryFunctions.readByte("y_thumbnail", inputStream, "Not a Valid JPEG File");
        this.thumbnailSize = this.xThumbnail * this.yThumbnail;
        if (this.thumbnailSize > 0) {
            BinaryFunctions.skipBytes(inputStream, this.thumbnailSize, "Not a Valid JPEG File: missing thumbnail");
        }
        if (getDebug()) {
            System.out.println("");
        }
    }
}
