// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.io.PrintWriter;
import java.nio.ByteOrder;

public class BinaryFileParser
{
    private ByteOrder byteOrder;
    private boolean debug;
    
    public BinaryFileParser() {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }
    
    public BinaryFileParser(final ByteOrder byteOrder) {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        this.byteOrder = byteOrder;
    }
    
    protected final void debugNumber(final PrintWriter printWriter, final String str, final int i, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": ");
        sb.append(i);
        sb.append(" (");
        printWriter.print(sb.toString());
        int j = 0;
        int n2 = i;
        while (j < n) {
            if (j > 0) {
                printWriter.print(",");
            }
            final int k = 0xFF & n2;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append((char)k);
            sb2.append(" [");
            sb2.append(k);
            sb2.append("]");
            printWriter.print(sb2.toString());
            n2 >>= 8;
            ++j;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(") [0x");
        sb3.append(Integer.toHexString(i));
        sb3.append(", ");
        sb3.append(Integer.toBinaryString(i));
        sb3.append("]");
        printWriter.println(sb3.toString());
        printWriter.flush();
    }
    
    protected final void debugNumber(final String s, final int n, final int n2) {
        final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        this.debugNumber(printWriter, s, n, n2);
        printWriter.flush();
    }
    
    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }
    
    public boolean getDebug() {
        return this.debug;
    }
    
    protected void setByteOrder(final ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
}
