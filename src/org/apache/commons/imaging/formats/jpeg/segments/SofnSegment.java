package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SofnSegment extends Segment {
    private final Component[] components;
    public final int height;
    public final int numberOfComponents;
    public final int precision;
    public final int width;

    public static class Component {
        public final int componentIdentifier;
        public final int horizontalSamplingFactor;
        public final int quantTabDestSelector;
        public final int verticalSamplingFactor;

        public Component(int i, int i2, int i3, int i4) {
            this.componentIdentifier = i;
            this.horizontalSamplingFactor = i2;
            this.verticalSamplingFactor = i3;
            this.quantTabDestSelector = i4;
        }
    }

    public SofnSegment(int i, byte[] bArr) throws IOException {
        this(i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public SofnSegment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2);
        if (getDebug()) {
            System.out.println("SOF0Segment marker_length: " + i2);
        }
        this.precision = BinaryFunctions.readByte("Data_precision", inputStream, "Not a Valid JPEG File");
        this.height = BinaryFunctions.read2Bytes("Image_height", inputStream, "Not a Valid JPEG File", getByteOrder());
        this.width = BinaryFunctions.read2Bytes("Image_Width", inputStream, "Not a Valid JPEG File", getByteOrder());
        this.numberOfComponents = BinaryFunctions.readByte("Number_of_components", inputStream, "Not a Valid JPEG File");
        this.components = new Component[this.numberOfComponents];
        for (int i3 = 0; i3 < this.numberOfComponents; i3++) {
            byte b = BinaryFunctions.readByte("ComponentIdentifier", inputStream, "Not a Valid JPEG File");
            byte b2 = BinaryFunctions.readByte("SamplingFactors", inputStream, "Not a Valid JPEG File");
            this.components[i3] = new Component(b, (b2 >> 4) & 15, b2 & 15, BinaryFunctions.readByte("QuantTabDestSel", inputStream, "Not a Valid JPEG File"));
        }
        if (getDebug()) {
            System.out.println("");
        }
    }

    public Component[] getComponents() {
        return (Component[]) this.components.clone();
    }

    public Component getComponents(int i) {
        return this.components[i];
    }

    @Override // org.apache.commons.imaging.formats.jpeg.segments.Segment
    public String getDescription() {
        return "SOFN (SOF" + (this.marker - JpegConstants.SOF0_MARKER) + ") (" + getSegmentType() + ")";
    }
}
