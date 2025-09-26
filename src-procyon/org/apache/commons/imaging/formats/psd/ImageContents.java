// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class ImageContents
{
    public final int ColorModeDataLength;
    public final int Compression;
    public final int ImageResourcesLength;
    public final int LayerAndMaskDataLength;
    public final PsdHeaderInfo header;
    
    public ImageContents(final PsdHeaderInfo header, final int colorModeDataLength, final int imageResourcesLength, final int layerAndMaskDataLength, final int compression) {
        this.header = header;
        this.ColorModeDataLength = colorModeDataLength;
        this.ImageResourcesLength = imageResourcesLength;
        this.LayerAndMaskDataLength = layerAndMaskDataLength;
        this.Compression = compression;
    }
    
    public void dump() {
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        this.dump(printWriter);
        printWriter.flush();
    }
    
    public void dump(final PrintWriter printWriter) {
        printWriter.println("");
        printWriter.println("ImageContents");
        final StringBuilder sb = new StringBuilder();
        sb.append("Compression: ");
        sb.append(this.Compression);
        sb.append(" (");
        sb.append(Integer.toHexString(this.Compression));
        sb.append(")");
        printWriter.println(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("ColorModeDataLength: ");
        sb2.append(this.ColorModeDataLength);
        sb2.append(" (");
        sb2.append(Integer.toHexString(this.ColorModeDataLength));
        sb2.append(")");
        printWriter.println(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("ImageResourcesLength: ");
        sb3.append(this.ImageResourcesLength);
        sb3.append(" (");
        sb3.append(Integer.toHexString(this.ImageResourcesLength));
        sb3.append(")");
        printWriter.println(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("LayerAndMaskDataLength: ");
        sb4.append(this.LayerAndMaskDataLength);
        sb4.append(" (");
        sb4.append(Integer.toHexString(this.LayerAndMaskDataLength));
        sb4.append(")");
        printWriter.println(sb4.toString());
        printWriter.println("");
        printWriter.flush();
    }
}
