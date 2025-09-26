package org.apache.commons.imaging.formats.psd;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PsdHeaderInfo {
    public final int channels;
    public final int columns;
    public final int depth;
    public final int mode;
    private final byte[] reserved;
    public final int rows;
    public final int version;

    public PsdHeaderInfo(int i, byte[] bArr, int i2, int i3, int i4, int i5, int i6) {
        this.version = i;
        this.reserved = bArr;
        this.channels = i2;
        this.rows = i3;
        this.columns = i4;
        this.depth = i5;
        this.mode = i6;
    }

    public byte[] getReserved() {
        return (byte[]) this.reserved.clone();
    }

    public void dump() {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        dump(printWriter);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("");
        printWriter.println("Header");
        printWriter.println("Version: " + this.version + " (" + Integer.toHexString(this.version) + ")");
        printWriter.println("Channels: " + this.channels + " (" + Integer.toHexString(this.channels) + ")");
        printWriter.println("Rows: " + this.rows + " (" + Integer.toHexString(this.rows) + ")");
        printWriter.println("Columns: " + this.columns + " (" + Integer.toHexString(this.columns) + ")");
        printWriter.println("Depth: " + this.depth + " (" + Integer.toHexString(this.depth) + ")");
        printWriter.println("Mode: " + this.mode + " (" + Integer.toHexString(this.mode) + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("Reserved: ");
        sb.append(this.reserved.length);
        printWriter.println(sb.toString());
        printWriter.println("");
        printWriter.flush();
    }
}
