package org.apache.commons.imaging.formats.psd;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageContents {
    public final int ColorModeDataLength;
    public final int Compression;
    public final int ImageResourcesLength;
    public final int LayerAndMaskDataLength;
    public final PsdHeaderInfo header;

    public ImageContents(PsdHeaderInfo psdHeaderInfo, int i, int i2, int i3, int i4) {
        this.header = psdHeaderInfo;
        this.ColorModeDataLength = i;
        this.ImageResourcesLength = i2;
        this.LayerAndMaskDataLength = i3;
        this.Compression = i4;
    }

    public void dump() {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        dump(printWriter);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("");
        printWriter.println("ImageContents");
        printWriter.println("Compression: " + this.Compression + " (" + Integer.toHexString(this.Compression) + ")");
        printWriter.println("ColorModeDataLength: " + this.ColorModeDataLength + " (" + Integer.toHexString(this.ColorModeDataLength) + ")");
        printWriter.println("ImageResourcesLength: " + this.ImageResourcesLength + " (" + Integer.toHexString(this.ImageResourcesLength) + ")");
        printWriter.println("LayerAndMaskDataLength: " + this.LayerAndMaskDataLength + " (" + Integer.toHexString(this.LayerAndMaskDataLength) + ")");
        printWriter.println("");
        printWriter.flush();
    }
}
