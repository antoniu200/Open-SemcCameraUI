// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.List;

public class DqtSegment extends Segment
{
    public final List<QuantizationTable> quantizationTables;
    
    public DqtSegment(int i, int j, final InputStream inputStream) throws ImageReadException, IOException {
        super(i, j);
        this.quantizationTables = new ArrayList<QuantizationTable>();
        i = j;
        while (i > 0) {
            final byte byte1 = BinaryFunctions.readByte("QuantizationTablePrecisionAndDestination", inputStream, "Not a Valid JPEG File");
            --i;
            final int k = byte1 >> 4 & 0xF;
            final int[] array = new int[64];
            StringBuilder sb;
            for (j = 0; j < 64; ++j) {
                if (k == 0) {
                    array[j] = (0xFF & BinaryFunctions.readByte("QuantizationTableElement", inputStream, "Not a Valid JPEG File"));
                    --i;
                }
                else {
                    if (k != 1) {
                        sb = new StringBuilder();
                        sb.append("Quantization table precision '");
                        sb.append(k);
                        sb.append("' is invalid");
                        throw new ImageReadException(sb.toString());
                    }
                    array[j] = BinaryFunctions.read2Bytes("QuantizationTableElement", inputStream, "Not a Valid JPEG File", this.getByteOrder());
                    i -= 2;
                }
            }
            this.quantizationTables.add(new QuantizationTable(k, byte1 & 0xF, array));
        }
    }
    
    public DqtSegment(final int n, final byte[] buf) throws ImageReadException, IOException {
        this(n, buf.length, new ByteArrayInputStream(buf));
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DQT (");
        sb.append(this.getSegmentType());
        sb.append(")");
        return sb.toString();
    }
    
    public static class QuantizationTable
    {
        public final int destinationIdentifier;
        private final int[] elements;
        public final int precision;
        
        public QuantizationTable(final int precision, final int destinationIdentifier, final int[] elements) {
            this.precision = precision;
            this.destinationIdentifier = destinationIdentifier;
            this.elements = elements;
        }
        
        public int[] getElements() {
            return this.elements;
        }
    }
}
