package org.apache.commons.imaging.common;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BinaryFileParser {
    private ByteOrder byteOrder;
    private boolean debug;

    public BinaryFileParser(ByteOrder byteOrder) {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
        this.byteOrder = byteOrder;
    }

    public BinaryFileParser() {
        this.byteOrder = ByteOrder.BIG_ENDIAN;
    }

    protected void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public void setDebug(boolean z) {
        this.debug = z;
    }

    protected final void debugNumber(String str, int i, int i2) {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        debugNumber(printWriter, str, i, i2);
        printWriter.flush();
    }

    protected final void debugNumber(PrintWriter printWriter, String str, int i, int i2) {
        printWriter.print(str + ": " + i + " (");
        int i3 = i;
        for (int i4 = 0; i4 < i2; i4++) {
            if (i4 > 0) {
                printWriter.print(",");
            }
            int i5 = 255 & i3;
            printWriter.print(((char) i5) + " [" + i5 + "]");
            i3 >>= 8;
        }
        printWriter.println(") [0x" + Integer.toHexString(i) + ", " + Integer.toBinaryString(i) + "]");
        printWriter.flush();
    }
}
