// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;

public class SosSegment extends Segment
{
    private final Component[] components;
    public final int endOfSpectralSelection;
    public final int numberOfComponents;
    public final int startOfSpectralSelection;
    public final int successiveApproximationBitHigh;
    public final int successiveApproximationBitLow;
    
    public SosSegment(int i, int byte1, final InputStream inputStream) throws IOException {
        super(i, byte1);
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("SosSegment marker_length: ");
            sb.append(byte1);
            out.println(sb.toString());
        }
        this.numberOfComponents = BinaryFunctions.readByte("number_of_components_in_scan", inputStream, "Not a Valid JPEG File");
        this.components = new Component[this.numberOfComponents];
        byte byte2;
        for (i = 0; i < this.numberOfComponents; ++i) {
            byte1 = BinaryFunctions.readByte("scanComponentSelector", inputStream, "Not a Valid JPEG File");
            byte2 = BinaryFunctions.readByte("acDcEntropoyCodingTableSelector", inputStream, "Not a Valid JPEG File");
            this.components[i] = new Component(byte1, byte2 >> 4 & 0xF, byte2 & 0xF);
        }
        this.startOfSpectralSelection = BinaryFunctions.readByte("start_of_spectral_selection", inputStream, "Not a Valid JPEG File");
        this.endOfSpectralSelection = BinaryFunctions.readByte("end_of_spectral_selection", inputStream, "Not a Valid JPEG File");
        i = BinaryFunctions.readByte("successive_approximation_bit_position", inputStream, "Not a Valid JPEG File");
        this.successiveApproximationBitHigh = (i >> 4 & 0xF);
        this.successiveApproximationBitLow = (i & 0xF);
        if (this.getDebug()) {
            System.out.println("");
        }
    }
    
    public SosSegment(final int n, final byte[] buf) throws IOException {
        this(n, buf.length, new ByteArrayInputStream(buf));
    }
    
    public Component getComponents(final int n) {
        return this.components[n];
    }
    
    public Component[] getComponents() {
        return this.components.clone();
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SOS (");
        sb.append(this.getSegmentType());
        sb.append(")");
        return sb.toString();
    }
    
    public static class Component
    {
        public final int acCodingTableSelector;
        public final int dcCodingTableSelector;
        public final int scanComponentSelector;
        
        public Component(final int scanComponentSelector, final int dcCodingTableSelector, final int acCodingTableSelector) {
            this.scanComponentSelector = scanComponentSelector;
            this.dcCodingTableSelector = dcCodingTableSelector;
            this.acCodingTableSelector = acCodingTableSelector;
        }
    }
}
