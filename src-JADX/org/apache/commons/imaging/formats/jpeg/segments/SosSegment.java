package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.common.BinaryFunctions;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SosSegment extends Segment {
    private final Component[] components;
    public final int endOfSpectralSelection;
    public final int numberOfComponents;
    public final int startOfSpectralSelection;
    public final int successiveApproximationBitHigh;
    public final int successiveApproximationBitLow;

    public static class Component {
        public final int acCodingTableSelector;
        public final int dcCodingTableSelector;
        public final int scanComponentSelector;

        public Component(int i, int i2, int i3) {
            this.scanComponentSelector = i;
            this.dcCodingTableSelector = i2;
            this.acCodingTableSelector = i3;
        }
    }

    public SosSegment(int i, byte[] bArr) throws IOException {
        this(i, bArr.length, new ByteArrayInputStream(bArr));
    }

    public SosSegment(int i, int i2, InputStream inputStream) throws IOException {
        super(i, i2);
        if (getDebug()) {
            System.out.println("SosSegment marker_length: " + i2);
        }
        this.numberOfComponents = BinaryFunctions.readByte("number_of_components_in_scan", inputStream, "Not a Valid JPEG File");
        this.components = new Component[this.numberOfComponents];
        for (int i3 = 0; i3 < this.numberOfComponents; i3++) {
            byte b = BinaryFunctions.readByte("scanComponentSelector", inputStream, "Not a Valid JPEG File");
            byte b2 = BinaryFunctions.readByte("acDcEntropoyCodingTableSelector", inputStream, "Not a Valid JPEG File");
            this.components[i3] = new Component(b, (b2 >> 4) & 15, b2 & 15);
        }
        this.startOfSpectralSelection = BinaryFunctions.readByte("start_of_spectral_selection", inputStream, "Not a Valid JPEG File");
        this.endOfSpectralSelection = BinaryFunctions.readByte("end_of_spectral_selection", inputStream, "Not a Valid JPEG File");
        byte b3 = BinaryFunctions.readByte("successive_approximation_bit_position", inputStream, "Not a Valid JPEG File");
        this.successiveApproximationBitHigh = (b3 >> 4) & 15;
        this.successiveApproximationBitLow = b3 & 15;
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
        return "SOS (" + getSegmentType() + ")";
    }
}
