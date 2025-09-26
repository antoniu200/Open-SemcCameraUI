// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.List;

public class DhtSegment extends Segment
{
    public final List<HuffmanTable> huffmanTables;
    
    public DhtSegment(int i, int n, final InputStream inputStream) throws IOException {
        super(i, n);
        final ArrayList list = new ArrayList();
        i = n;
        while (i > 0) {
            final int n2 = BinaryFunctions.readByte("TableClassAndDestinationId", inputStream, "Not a Valid JPEG File") & 0xFF;
            --i;
            final int[] array = new int[17];
            final int n3 = 0;
            int j = 1;
            n = 0;
            while (j < array.length) {
                array[j] = (BinaryFunctions.readByte("Li", inputStream, "Not a Valid JPEG File") & 0xFF);
                --i;
                n += array[j];
                ++j;
            }
            final int[] array2 = new int[n];
            for (int k = n3; k < n; ++k) {
                array2[k] = (BinaryFunctions.readByte("Vij", inputStream, "Not a Valid JPEG File") & 0xFF);
                --i;
            }
            list.add(new HuffmanTable(n2 >> 4 & 0xF, n2 & 0xF, array, array2));
        }
        this.huffmanTables = (List<HuffmanTable>)Collections.unmodifiableList((List<?>)list);
    }
    
    public DhtSegment(final int n, final byte[] buf) throws IOException {
        this(n, buf.length, new ByteArrayInputStream(buf));
    }
    
    @Override
    public String getDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DHT (");
        sb.append(this.getSegmentType());
        sb.append(")");
        return sb.toString();
    }
    
    public static class HuffmanTable
    {
        private final int[] bits;
        public final int destinationIdentifier;
        private final int[] huffCode;
        private final int[] huffSize;
        private final int[] huffVal;
        private final int[] maxCode;
        private final int[] minCode;
        public final int tableClass;
        private final int[] valPtr;
        
        public HuffmanTable(int tableClass, int destinationIdentifier, final int[] bits, final int[] huffVal) {
            this.huffSize = new int[4096];
            this.minCode = new int[17];
            this.maxCode = new int[17];
            this.valPtr = new int[17];
            this.tableClass = tableClass;
            this.destinationIdentifier = destinationIdentifier;
            this.bits = bits;
            this.huffVal = huffVal;
            final int n = 0;
            destinationIdentifier = 0;
            tableClass = 1;
            int n2 = 1;
            while (true) {
                if (tableClass > bits[n2]) {
                    if (++n2 > 16) {
                        break;
                    }
                    tableClass = 1;
                }
                else {
                    this.huffSize[destinationIdentifier] = n2;
                    ++destinationIdentifier;
                    ++tableClass;
                }
            }
            this.huffSize[destinationIdentifier] = 0;
            tableClass = this.huffSize[0];
            this.huffCode = new int[destinationIdentifier];
            int n3 = 0;
            int n4 = 0;
            while (true) {
                this.huffCode[n4] = n3;
                ++n3;
                final int n5 = n4 + 1;
                if (this.huffSize[n5] == tableClass) {
                    destinationIdentifier = tableClass;
                    tableClass = n3;
                }
                else {
                    destinationIdentifier = n3;
                    if (this.huffSize[n5] == 0) {
                        break;
                    }
                    int n6;
                    int n7;
                    do {
                        n7 = destinationIdentifier << 1;
                        n6 = tableClass + 1;
                        destinationIdentifier = n7;
                    } while (this.huffSize[n5] != (tableClass = n6));
                    tableClass = n7;
                    destinationIdentifier = n6;
                }
                n4 = n5;
                n3 = tableClass;
                tableClass = destinationIdentifier;
            }
            destinationIdentifier = 0;
            tableClass = n;
            while (++tableClass <= 16) {
                if (bits[tableClass] == 0) {
                    this.maxCode[tableClass] = -1;
                }
                else {
                    this.valPtr[tableClass] = destinationIdentifier;
                    this.minCode[tableClass] = this.huffCode[destinationIdentifier];
                    destinationIdentifier += bits[tableClass] - 1;
                    this.maxCode[tableClass] = this.huffCode[destinationIdentifier];
                    ++destinationIdentifier;
                }
            }
        }
        
        public int[] getBits() {
            return this.bits;
        }
        
        public int[] getHuffCode() {
            return this.huffCode;
        }
        
        public int[] getHuffSize() {
            return this.huffSize;
        }
        
        public int[] getHuffVal() {
            return this.huffVal;
        }
        
        public int[] getMaxCode() {
            return this.maxCode;
        }
        
        public int[] getMinCode() {
            return this.minCode;
        }
        
        public int[] getValPtr() {
            return this.valPtr;
        }
    }
}
