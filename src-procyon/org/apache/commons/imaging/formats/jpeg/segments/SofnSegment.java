// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;

public class SofnSegment extends Segment
{
    private final Component[] components;
    public final int height;
    public final int numberOfComponents;
    public final int precision;
    public final int width;
    
    public SofnSegment(int i, int byte1, final InputStream inputStream) throws IOException {
        super(i, byte1);
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("SOF0Segment marker_length: ");
            sb.append(byte1);
            out.println(sb.toString());
        }
        this.precision = BinaryFunctions.readByte("Data_precision", inputStream, "Not a Valid JPEG File");
        this.height = BinaryFunctions.read2Bytes("Image_height", inputStream, "Not a Valid JPEG File", this.getByteOrder());
        this.width = BinaryFunctions.read2Bytes("Image_Width", inputStream, "Not a Valid JPEG File", this.getByteOrder());
        this.numberOfComponents = BinaryFunctions.readByte("Number_of_components", inputStream, "Not a Valid JPEG File");
        this.components = new Component[this.numberOfComponents];
        byte byte2;
        for (i = 0; i < this.numberOfComponents; ++i) {
            byte2 = BinaryFunctions.readByte("ComponentIdentifier", inputStream, "Not a Valid JPEG File");
            byte1 = BinaryFunctions.readByte("SamplingFactors", inputStream, "Not a Valid JPEG File");
            this.components[i] = new Component(byte2, byte1 >> 4 & 0xF, byte1 & 0xF, BinaryFunctions.readByte("QuantTabDestSel", inputStream, "Not a Valid JPEG File"));
        }
        if (this.getDebug()) {
            System.out.println("");
        }
    }
    
    public SofnSegment(final int n, final byte[] buf) throws IOException {
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
        sb.append("SOFN (SOF");
        sb.append(this.marker - 65472);
        sb.append(") (");
        sb.append(this.getSegmentType());
        sb.append(")");
        return sb.toString();
    }
    
    public static class Component
    {
        public final int componentIdentifier;
        public final int horizontalSamplingFactor;
        public final int quantTabDestSelector;
        public final int verticalSamplingFactor;
        
        public Component(final int componentIdentifier, final int horizontalSamplingFactor, final int verticalSamplingFactor, final int quantTabDestSelector) {
            this.componentIdentifier = componentIdentifier;
            this.horizontalSamplingFactor = horizontalSamplingFactor;
            this.verticalSamplingFactor = verticalSamplingFactor;
            this.quantTabDestSelector = quantTabDestSelector;
        }
    }
}
