// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.segments;

import java.io.PrintWriter;
import org.apache.commons.imaging.common.BinaryFileParser;

public abstract class Segment extends BinaryFileParser
{
    public final int length;
    public final int marker;
    
    public Segment(final int marker, final int length) {
        this.marker = marker;
        this.length = length;
    }
    
    public void dump(final PrintWriter printWriter) {
    }
    
    public abstract String getDescription();
    
    public String getSegmentType() {
        final int marker = this.marker;
        if (marker == 65281) {
            return "For temporary private use in arithmetic coding";
        }
        if (marker == 65534) {
            return "Comment";
        }
        switch (marker) {
            default: {
                if (this.marker >= 65282 && this.marker <= 65471) {
                    return "Reserved";
                }
                if (this.marker >= 65504 && this.marker <= 65519) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("APP");
                    sb.append(this.marker - 65504);
                    return sb.toString();
                }
                if (this.marker >= 65520 && this.marker <= 65533) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("JPG");
                    sb2.append(this.marker - 65504);
                    return sb2.toString();
                }
                return "Unknown";
            }
            case 65503: {
                return "Expand reference component(s)";
            }
            case 65502: {
                return "Define hierarchical progression";
            }
            case 65501: {
                return "Define restart interval";
            }
            case 65500: {
                return "Define number of lines";
            }
            case 65499: {
                return "Define quantization table(s)";
            }
            case 65498: {
                return "Start of scan";
            }
            case 65497: {
                return "End of image";
            }
            case 65496: {
                return "Start of image";
            }
            case 65495: {
                return "Restart with modulo 8 count 7";
            }
            case 65494: {
                return "Restart with modulo 8 count 6";
            }
            case 65493: {
                return "Restart with modulo 8 count 5";
            }
            case 65492: {
                return "Restart with modulo 8 count 4";
            }
            case 65491: {
                return "Restart with modulo 8 count 3";
            }
            case 65490: {
                return "Restart with modulo 8 count 2";
            }
            case 65489: {
                return "Restart with modulo 8 count 1";
            }
            case 65488: {
                return "Restart with modulo 8 count 0";
            }
            case 65487: {
                return "Start Of Frame, Differential lossless (sequential), arithmetic coding";
            }
            case 65486: {
                return "Start Of Frame, Differential progressive Dct, arithmetic coding";
            }
            case 65485: {
                return "Start Of Frame, Differential sequential Dct, arithmetic coding";
            }
            case 65484: {
                return "Define arithmetic coding conditioning(s)";
            }
            case 65483: {
                return "Start Of Frame, Lossless (sequential), arithmetic coding";
            }
            case 65482: {
                return "Start Of Frame, Progressive Dct, arithmetic coding";
            }
            case 65481: {
                return "Start Of Frame, Extended sequential Dct, arithmetic coding";
            }
            case 65480: {
                return "Start Of Frame, Reserved for JPEG extensions, arithmetic coding";
            }
            case 65479: {
                return "Start Of Frame, Differential lossless (sequential), Huffman coding";
            }
            case 65478: {
                return "Start Of Frame, Differential progressive Dct, Huffman coding";
            }
            case 65477: {
                return "Start Of Frame, Differential sequential Dct, Huffman coding";
            }
            case 65476: {
                return "Define Huffman table(s)";
            }
            case 65475: {
                return "Start Of Frame, Lossless (sequential), Huffman coding";
            }
            case 65474: {
                return "Start Of Frame, Progressive Dct, Huffman coding";
            }
            case 65473: {
                return "Start Of Frame, Extended sequential Dct, Huffman coding";
            }
            case 65472: {
                return "Start Of Frame, Baseline Dct, Huffman coding";
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Segment: ");
        sb.append(this.getDescription());
        sb.append("]");
        return sb.toString();
    }
}
