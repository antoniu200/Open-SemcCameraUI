// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.psd;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class PsdHeaderInfo
{
    public final int channels;
    public final int columns;
    public final int depth;
    public final int mode;
    private final byte[] reserved;
    public final int rows;
    public final int version;
    
    public PsdHeaderInfo(final int version, final byte[] reserved, final int channels, final int rows, final int columns, final int depth, final int mode) {
        this.version = version;
        this.reserved = reserved;
        this.channels = channels;
        this.rows = rows;
        this.columns = columns;
        this.depth = depth;
        this.mode = mode;
    }
    
    public void dump() {
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        this.dump(printWriter);
        printWriter.flush();
    }
    
    public void dump(final PrintWriter printWriter) {
        printWriter.println("");
        printWriter.println("Header");
        final StringBuilder sb = new StringBuilder();
        sb.append("Version: ");
        sb.append(this.version);
        sb.append(" (");
        sb.append(Integer.toHexString(this.version));
        sb.append(")");
        printWriter.println(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Channels: ");
        sb2.append(this.channels);
        sb2.append(" (");
        sb2.append(Integer.toHexString(this.channels));
        sb2.append(")");
        printWriter.println(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Rows: ");
        sb3.append(this.rows);
        sb3.append(" (");
        sb3.append(Integer.toHexString(this.rows));
        sb3.append(")");
        printWriter.println(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Columns: ");
        sb4.append(this.columns);
        sb4.append(" (");
        sb4.append(Integer.toHexString(this.columns));
        sb4.append(")");
        printWriter.println(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Depth: ");
        sb5.append(this.depth);
        sb5.append(" (");
        sb5.append(Integer.toHexString(this.depth));
        sb5.append(")");
        printWriter.println(sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("Mode: ");
        sb6.append(this.mode);
        sb6.append(" (");
        sb6.append(Integer.toHexString(this.mode));
        sb6.append(")");
        printWriter.println(sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("Reserved: ");
        sb7.append(this.reserved.length);
        printWriter.println(sb7.toString());
        printWriter.println("");
        printWriter.flush();
    }
    
    public byte[] getReserved() {
        return this.reserved.clone();
    }
}
