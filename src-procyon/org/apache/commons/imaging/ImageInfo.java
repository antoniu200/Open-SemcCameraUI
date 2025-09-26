// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ImageInfo
{
    private final int bitsPerPixel;
    private final ColorType colorType;
    private final List<String> comments;
    private final CompressionAlgorithm compressionAlgorithm;
    private final ImageFormat format;
    private final String formatDetails;
    private final String formatName;
    private final int height;
    private final String mimeType;
    private final int numberOfImages;
    private final int physicalHeightDpi;
    private final float physicalHeightInch;
    private final int physicalWidthDpi;
    private final float physicalWidthInch;
    private final boolean progressive;
    private final boolean transparent;
    private final boolean usesPalette;
    private final int width;
    
    public ImageInfo(final String formatDetails, final int bitsPerPixel, final List<String> comments, final ImageFormat format, final String formatName, final int height, final String mimeType, final int numberOfImages, final int physicalHeightDpi, final float physicalHeightInch, final int physicalWidthDpi, final float physicalWidthInch, final int width, final boolean progressive, final boolean transparent, final boolean usesPalette, final ColorType colorType, final CompressionAlgorithm compressionAlgorithm) {
        this.formatDetails = formatDetails;
        this.bitsPerPixel = bitsPerPixel;
        this.comments = comments;
        this.format = format;
        this.formatName = formatName;
        this.height = height;
        this.mimeType = mimeType;
        this.numberOfImages = numberOfImages;
        this.physicalHeightDpi = physicalHeightDpi;
        this.physicalHeightInch = physicalHeightInch;
        this.physicalWidthDpi = physicalWidthDpi;
        this.physicalWidthInch = physicalWidthInch;
        this.width = width;
        this.progressive = progressive;
        this.transparent = transparent;
        this.usesPalette = usesPalette;
        this.colorType = colorType;
        this.compressionAlgorithm = compressionAlgorithm;
    }
    
    public void dump() {
        System.out.print(this.toString());
    }
    
    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }
    
    public ColorType getColorType() {
        return this.colorType;
    }
    
    public List<String> getComments() {
        return new ArrayList<String>(this.comments);
    }
    
    public CompressionAlgorithm getCompressionAlgorithm() {
        return this.compressionAlgorithm;
    }
    
    public ImageFormat getFormat() {
        return this.format;
    }
    
    public String getFormatDetails() {
        return this.formatDetails;
    }
    
    public String getFormatName() {
        return this.formatName;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public String getMimeType() {
        return this.mimeType;
    }
    
    public int getNumberOfImages() {
        return this.numberOfImages;
    }
    
    public int getPhysicalHeightDpi() {
        return this.physicalHeightDpi;
    }
    
    public float getPhysicalHeightInch() {
        return this.physicalHeightInch;
    }
    
    public int getPhysicalWidthDpi() {
        return this.physicalWidthDpi;
    }
    
    public float getPhysicalWidthInch() {
        return this.physicalWidthInch;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean isProgressive() {
        return this.progressive;
    }
    
    public boolean isTransparent() {
        return this.transparent;
    }
    
    @Override
    public String toString() {
        try {
            final StringWriter out = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(out);
            this.toString(printWriter, "");
            printWriter.flush();
            return out.toString();
        }
        catch (final Exception ex) {
            return "Image Data: Error";
        }
    }
    
    public void toString(final PrintWriter printWriter, String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Format Details: ");
        sb.append(this.formatDetails);
        printWriter.println(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Bits Per Pixel: ");
        sb2.append(this.bitsPerPixel);
        printWriter.println(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Comments: ");
        sb3.append(this.comments.size());
        printWriter.println(sb3.toString());
        for (int i = 0; i < this.comments.size(); ++i) {
            str = this.comments.get(i);
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("\t");
            sb4.append(i);
            sb4.append(": '");
            sb4.append(str);
            sb4.append("'");
            printWriter.println(sb4.toString());
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Format: ");
        sb5.append(this.format.getName());
        printWriter.println(sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("Format Name: ");
        sb6.append(this.formatName);
        printWriter.println(sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("Compression Algorithm: ");
        sb7.append(this.compressionAlgorithm);
        printWriter.println(sb7.toString());
        final StringBuilder sb8 = new StringBuilder();
        sb8.append("Height: ");
        sb8.append(this.height);
        printWriter.println(sb8.toString());
        final StringBuilder sb9 = new StringBuilder();
        sb9.append("MimeType: ");
        sb9.append(this.mimeType);
        printWriter.println(sb9.toString());
        final StringBuilder sb10 = new StringBuilder();
        sb10.append("Number Of Images: ");
        sb10.append(this.numberOfImages);
        printWriter.println(sb10.toString());
        final StringBuilder sb11 = new StringBuilder();
        sb11.append("Physical Height Dpi: ");
        sb11.append(this.physicalHeightDpi);
        printWriter.println(sb11.toString());
        final StringBuilder sb12 = new StringBuilder();
        sb12.append("Physical Height Inch: ");
        sb12.append(this.physicalHeightInch);
        printWriter.println(sb12.toString());
        final StringBuilder sb13 = new StringBuilder();
        sb13.append("Physical Width Dpi: ");
        sb13.append(this.physicalWidthDpi);
        printWriter.println(sb13.toString());
        final StringBuilder sb14 = new StringBuilder();
        sb14.append("Physical Width Inch: ");
        sb14.append(this.physicalWidthInch);
        printWriter.println(sb14.toString());
        final StringBuilder sb15 = new StringBuilder();
        sb15.append("Width: ");
        sb15.append(this.width);
        printWriter.println(sb15.toString());
        final StringBuilder sb16 = new StringBuilder();
        sb16.append("Is Progressive: ");
        sb16.append(this.progressive);
        printWriter.println(sb16.toString());
        final StringBuilder sb17 = new StringBuilder();
        sb17.append("Is Transparent: ");
        sb17.append(this.transparent);
        printWriter.println(sb17.toString());
        final StringBuilder sb18 = new StringBuilder();
        sb18.append("Color Type: ");
        sb18.append(this.colorType.toString());
        printWriter.println(sb18.toString());
        final StringBuilder sb19 = new StringBuilder();
        sb19.append("Uses Palette: ");
        sb19.append(this.usesPalette);
        printWriter.println(sb19.toString());
        printWriter.flush();
    }
    
    public boolean usesPalette() {
        return this.usesPalette;
    }
    
    public enum ColorType
    {
        private static final ColorType[] $VALUES;
        
        BW("Black and White"), 
        CMYK("CMYK"), 
        GRAYSCALE("Grayscale"), 
        OTHER("Other"), 
        RGB("RGB"), 
        UNKNOWN("Unknown"), 
        YCC("YCC"), 
        YCCK("YCCK"), 
        YCbCr("YCbCr");
        
        private String description;
        
        static {
            $VALUES = new ColorType[] { ColorType.BW, ColorType.GRAYSCALE, ColorType.RGB, ColorType.CMYK, ColorType.YCbCr, ColorType.YCCK, ColorType.YCC, ColorType.OTHER, ColorType.UNKNOWN };
        }
        
        private ColorType(final String description) {
            this.description = description;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
    
    public enum CompressionAlgorithm
    {
        private static final CompressionAlgorithm[] $VALUES;
        
        ADAPTIVE_RLE("Adaptive RLE"), 
        CCITT_1D("CCITT 1D"), 
        CCITT_GROUP_3("CCITT Group 3 1-Dimensional Modified Huffman run-length encoding."), 
        CCITT_GROUP_4("CCITT Group 4"), 
        JPEG("JPEG"), 
        LZW("LZW"), 
        NONE("None"), 
        PACKBITS("PackBits"), 
        PNG_FILTER("PNG Filter"), 
        PSD("Photoshop"), 
        RLE("RLE: Run-Length Encoding"), 
        UNKNOWN("Unknown");
        
        private String description;
        
        static {
            $VALUES = new CompressionAlgorithm[] { CompressionAlgorithm.UNKNOWN, CompressionAlgorithm.NONE, CompressionAlgorithm.LZW, CompressionAlgorithm.PACKBITS, CompressionAlgorithm.JPEG, CompressionAlgorithm.RLE, CompressionAlgorithm.ADAPTIVE_RLE, CompressionAlgorithm.PSD, CompressionAlgorithm.PNG_FILTER, CompressionAlgorithm.CCITT_GROUP_3, CompressionAlgorithm.CCITT_GROUP_4, CompressionAlgorithm.CCITT_1D };
        }
        
        private CompressionAlgorithm(final String description) {
            this.description = description;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
}
