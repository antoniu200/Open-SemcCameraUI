package org.apache.commons.imaging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageInfo {
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

    public enum ColorType {
        BW("Black and White"),
        GRAYSCALE("Grayscale"),
        RGB("RGB"),
        CMYK("CMYK"),
        YCbCr("YCbCr"),
        YCCK("YCCK"),
        YCC("YCC"),
        OTHER("Other"),
        UNKNOWN("Unknown");

        private String description;

        ColorType(String str) {
            this.description = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.description;
        }
    }

    public enum CompressionAlgorithm {
        UNKNOWN("Unknown"),
        NONE("None"),
        LZW("LZW"),
        PACKBITS("PackBits"),
        JPEG("JPEG"),
        RLE("RLE: Run-Length Encoding"),
        ADAPTIVE_RLE("Adaptive RLE"),
        PSD("Photoshop"),
        PNG_FILTER("PNG Filter"),
        CCITT_GROUP_3("CCITT Group 3 1-Dimensional Modified Huffman run-length encoding."),
        CCITT_GROUP_4("CCITT Group 4"),
        CCITT_1D("CCITT 1D");

        private String description;

        CompressionAlgorithm(String str) {
            this.description = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.description;
        }
    }

    public ImageInfo(String str, int i, List<String> list, ImageFormat imageFormat, String str2, int i2, String str3, int i3, int i4, float f, int i5, float f2, int i6, boolean z, boolean z2, boolean z3, ColorType colorType, CompressionAlgorithm compressionAlgorithm) {
        this.formatDetails = str;
        this.bitsPerPixel = i;
        this.comments = list;
        this.format = imageFormat;
        this.formatName = str2;
        this.height = i2;
        this.mimeType = str3;
        this.numberOfImages = i3;
        this.physicalHeightDpi = i4;
        this.physicalHeightInch = f;
        this.physicalWidthDpi = i5;
        this.physicalWidthInch = f2;
        this.width = i6;
        this.progressive = z;
        this.transparent = z2;
        this.usesPalette = z3;
        this.colorType = colorType;
        this.compressionAlgorithm = compressionAlgorithm;
    }

    public int getBitsPerPixel() {
        return this.bitsPerPixel;
    }

    public List<String> getComments() {
        return new ArrayList(this.comments);
    }

    public ImageFormat getFormat() {
        return this.format;
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

    public ColorType getColorType() {
        return this.colorType;
    }

    public void dump() {
        System.out.print(toString());
    }

    public String toString() {
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            toString(printWriter, "");
            printWriter.flush();
            return stringWriter.toString();
        } catch (Exception unused) {
            return "Image Data: Error";
        }
    }

    public void toString(PrintWriter printWriter, String str) {
        printWriter.println("Format Details: " + this.formatDetails);
        printWriter.println("Bits Per Pixel: " + this.bitsPerPixel);
        printWriter.println("Comments: " + this.comments.size());
        for (int i = 0; i < this.comments.size(); i++) {
            printWriter.println("\t" + i + ": '" + this.comments.get(i) + "'");
        }
        printWriter.println("Format: " + this.format.getName());
        printWriter.println("Format Name: " + this.formatName);
        printWriter.println("Compression Algorithm: " + this.compressionAlgorithm);
        printWriter.println("Height: " + this.height);
        printWriter.println("MimeType: " + this.mimeType);
        printWriter.println("Number Of Images: " + this.numberOfImages);
        printWriter.println("Physical Height Dpi: " + this.physicalHeightDpi);
        printWriter.println("Physical Height Inch: " + this.physicalHeightInch);
        printWriter.println("Physical Width Dpi: " + this.physicalWidthDpi);
        printWriter.println("Physical Width Inch: " + this.physicalWidthInch);
        printWriter.println("Width: " + this.width);
        printWriter.println("Is Progressive: " + this.progressive);
        printWriter.println("Is Transparent: " + this.transparent);
        printWriter.println("Color Type: " + this.colorType.toString());
        printWriter.println("Uses Palette: " + this.usesPalette);
        printWriter.flush();
    }

    public String getFormatDetails() {
        return this.formatDetails;
    }

    public boolean isTransparent() {
        return this.transparent;
    }

    public boolean usesPalette() {
        return this.usesPalette;
    }

    public CompressionAlgorithm getCompressionAlgorithm() {
        return this.compressionAlgorithm;
    }
}
